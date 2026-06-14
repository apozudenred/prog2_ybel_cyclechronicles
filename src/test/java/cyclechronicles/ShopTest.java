package cyclechronicles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShopTest {

    private Shop shop;

    @BeforeEach
    void setUp() {
        shop = new Shop();
    }

    private Order mockOrder(String customer, Type type) {
        Order o = Mockito.mock(Order.class);
        when(o.getCustomer()).thenReturn(customer);
        when(o.getBicycleType()).thenReturn(type);
        return o;
    }

    // --- Äquivalenzklassen: gültige Aufträge ---

    @Test
    void acceptValidRaceOrder() {
        assertTrue(shop.accept(mockOrder("Alice", Type.RACE)));
    }

    @Test
    void acceptValidSingleSpeedOrder() {
        assertTrue(shop.accept(mockOrder("Alice", Type.SINGLE_SPEED)));
    }

    @Test
    void acceptValidFixieOrder() {
        assertTrue(shop.accept(mockOrder("Alice", Type.FIXIE)));
    }

    // --- Grenzwert: genau 5 Aufträge (4 andere + 1 neuer) ---

    @Test
    void acceptFifthOrderSucceeds() {
        shop.accept(mockOrder("B", Type.RACE));
        shop.accept(mockOrder("C", Type.RACE));
        shop.accept(mockOrder("D", Type.RACE));
        shop.accept(mockOrder("E", Type.RACE));
        assertTrue(shop.accept(mockOrder("F", Type.RACE)));
    }

    // --- Grenzwert: 6. Auftrag wird abgelehnt ---

    @Test
    void rejectSixthOrder() {
        shop.accept(mockOrder("B", Type.RACE));
        shop.accept(mockOrder("C", Type.RACE));
        shop.accept(mockOrder("D", Type.RACE));
        shop.accept(mockOrder("E", Type.RACE));
        shop.accept(mockOrder("F", Type.RACE));
        assertFalse(shop.accept(mockOrder("G", Type.RACE)));
    }

    // --- E-Bike wird abgelehnt ---

    @Test
    void rejectEbike() {
        assertFalse(shop.accept(mockOrder("Alice", Type.EBIKE)));
    }

    // --- Gravel-Bike wird abgelehnt ---

    @Test
    void rejectGravel() {
        assertFalse(shop.accept(mockOrder("Alice", Type.GRAVEL)));
    }

    // --- Kunde hat bereits offenen Auftrag ---

    @Test
    void rejectDuplicateCustomer() {
        shop.accept(mockOrder("Alice", Type.RACE));
        assertFalse(shop.accept(mockOrder("Alice", Type.FIXIE)));
    }

    // --- Gleicher Kunde, anderer Typ, nach erstem Auftrag ---

    @Test
    void rejectSameCustomerDifferentType() {
        shop.accept(mockOrder("Alice", Type.SINGLE_SPEED));
        assertFalse(shop.accept(mockOrder("Alice", Type.RACE)));
    }

    // --- Leere Queue: erster Auftrag wird angenommen ---

    @Test
    void acceptFirstOrder() {
        assertTrue(shop.accept(mockOrder("Alice", Type.RACE)));
    }
}
