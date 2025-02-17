import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeliveryTest {
    @Test
    @Tag("Positive")
    @DisplayName("Рассчет минимальной доставки")
    void testMinDeliveryCost()
    {
        Delivery del = new Delivery(1, CargoDimension.LARGE, false, ServiceWorkload.INCREASED);
        assertEquals(400, del.calculateDeliveryCost());
    }

    @ParameterizedTest(name = "Рассчет доставки до 2 км")
    @ValueSource(ints = {1, 2})
    @Tag("Positive")
    @DisplayName("Рассчет доставки до 2 км")
    void testDeliveryCostUpTo2Km(Integer distance)
    {
        Delivery del = new Delivery(distance, CargoDimension.LARGE, true, ServiceWorkload.VERY_HIGH);
        assertEquals(880, del.calculateDeliveryCost());
    }

    @DisplayName("Рассчет доставки до 10 км")
    @ParameterizedTest(name = "Рассчет доставки до 10 км")
    @ValueSource(ints = {3, 9, 10})
    @Tag("Positive")
    void testDeliveryCostUpTo10Km(Integer distance)
    {
        Delivery del = new Delivery(distance, CargoDimension.LARGE, true, ServiceWorkload.INCREASED);
        assertEquals(720, del.calculateDeliveryCost());
    }

    @ParameterizedTest(name = "Рассчет доставки до 30 км")
    @ValueSource(ints = {11, 29, 30})
    @Tag("Positive")
    @DisplayName("Рассчет доставки до 30 км")
    void testDeliveryCostUpTo30Km(Integer distance)
    {
        Delivery del = new Delivery(distance, CargoDimension.LARGE, true, ServiceWorkload.NORMAL);
        assertEquals(700, del.calculateDeliveryCost());
    }

    @Test
    @Tag("Positive")
    @DisplayName("Рассчет доставки более 30 км")
    void testDeliveryCostMoreThan30Km()
    {
        Delivery del = new Delivery(31, CargoDimension.LARGE, false, ServiceWorkload.NORMAL);
        assertEquals(500, del.calculateDeliveryCost());
    }

    @Test
    @Tag("Positive")
    @DisplayName("Рассчет доставки для высокой загруженности")
    void testDeliveryLoadHigh()
    {
        Delivery del = new Delivery(31, CargoDimension.LARGE, false, ServiceWorkload.HIGH);
        assertEquals(700, del.calculateDeliveryCost());
    }

    @Test
    @Tag("Positive")
    @DisplayName("Рассчет доставки для маленьких габаритов")
    void testDeliveryCargoSmall()
    {
        Delivery del = new Delivery(31, CargoDimension.SMALL, false, ServiceWorkload.HIGH);
        assertEquals(560, del.calculateDeliveryCost());
    }

    @Test
    @Tag("Negative")
    @DisplayName("Рассчет доставки хрупкого товара далее 30 км]")
    void testFragileDeliveryOver31Km()
    {
        Delivery del = new Delivery(31, CargoDimension.SMALL, true, ServiceWorkload.NORMAL);
        Throwable exception = assertThrows(UnsupportedOperationException.class, del::calculateDeliveryCost);
        assertEquals("Fragile cargo cannot be delivered for the distance more than 30", exception.getMessage());
    }

    @Test
    @Tag("Negative")
    @DisplayName("Рассчет доставки товара на -1 км]")
    void testNegativeDistance() {
        Delivery del = new Delivery(-1, CargoDimension.SMALL, false, ServiceWorkload.NORMAL);
        Throwable exception = assertThrows(IllegalArgumentException.class, del::calculateDeliveryCost        );
        assertEquals("destinationDistance should be a positive number!", exception.getMessage());
    }
}
