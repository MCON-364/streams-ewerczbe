package edu.touro.las.mcon364.streams.homework;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import edu.touro.las.mcon364.streams.homework.StreamHomework.*;

import java.time.*;
import java.util.*;

/**
 * Unit tests for StreamHomework.
 * 
 * Run with: mvn test -Dtest=StreamHomeworkTest
 */
class StreamHomeworkTest {
    
    private StreamHomework hw;
    
    @BeforeEach
    void setUp() {
        hw = new StreamHomework();
    }
    
    // =========================================================================
    // PART 1: Basic Analytics
    // =========================================================================
    
    @Test
    @DisplayName("1.1: getTotalRevenue calculates correctly")
    void testGetTotalRevenue() {
        double revenue = hw.getTotalRevenue();
        assertTrue(revenue > 6000, "Revenue should be greater than $6000");
        assertTrue(revenue < 7000, "Revenue should be less than $7000");
    }
    
    @Test
    @DisplayName("1.2: getOrderCount returns correct counts")
    void testGetOrderCount() {
        assertEquals(6, hw.getOrderCount(OrderStatus.DELIVERED));
        assertEquals(2, hw.getOrderCount(OrderStatus.SHIPPED));
        assertEquals(1, hw.getOrderCount(OrderStatus.PENDING));
        assertEquals(1, hw.getOrderCount(OrderStatus.CANCELLED));
    }
    
    @Test
    @DisplayName("1.3: getUniqueProducts returns all products")
    void testGetUniqueProducts() {
        Set<Product> products = hw.getUniqueProducts();
        assertNotNull(products);
        assertEquals(10, products.size(), "All 10 products should have been ordered");
    }
    
    @Test
    @DisplayName("1.4: getAverageOrderValue calculates correctly")
    void testGetAverageOrderValue() {
        double avg = hw.getAverageOrderValue();
        assertTrue(avg > 1000, "Average should be greater than $1000");
        assertTrue(avg < 1500, "Average should be less than $1500");
    }
    
    // =========================================================================
    // PART 2: Customer Analytics
    // =========================================================================
    
    @Test
    @DisplayName("2.1: getRevenueByCustomer returns correct map")
    void testGetRevenueByCustomer() {
        Map<String, Double> revenue = hw.getRevenueByCustomer();
        assertNotNull(revenue);
        assertTrue(revenue.containsKey("C001"));
        assertTrue(revenue.containsKey("C004"));
        assertTrue(revenue.get("C001") > 1000);
    }
    
    @Test
    @DisplayName("2.2: getTopCustomers returns correct order")
    void testGetTopCustomers() {
        List<String> top = hw.getTopCustomers(3);
        assertNotNull(top);
        assertEquals(3, top.size());
        // C004 should be top (2 laptops + smartphone = ~$2700)
    }
    
    @Test
    @DisplayName("2.3: getCustomerOrderCounts returns correct counts")
    void testGetCustomerOrderCounts() {
        Map<String, Long> counts = hw.getCustomerOrderCounts();
        assertNotNull(counts);
        assertEquals(6, counts.size(), "Should have 6 customers");
        assertEquals(2L, counts.get("C001"));
        assertEquals(2L, counts.get("C002"));
        assertEquals(1L, counts.get("C005"));
    }
    
    @Test
    @DisplayName("2.4: getCustomersWithMultipleOrders returns correct list")
    void testGetCustomersWithMultipleOrders() {
        List<String> customers = hw.getCustomersWithMultipleOrders();
        assertNotNull(customers);
        assertEquals(4, customers.size());
        assertTrue(customers.contains("C001"));
        assertTrue(customers.contains("C002"));
        assertTrue(customers.contains("C003"));
        assertTrue(customers.contains("C006"));
        assertFalse(customers.contains("C005"));
    }
    
    // =========================================================================
    // PART 3: Product Analytics
    // =========================================================================
    
    @Test
    @DisplayName("3.1: getRevenueByCategory returns correct map")
    void testGetRevenueByCategory() {
        Map<String, Double> revenue = hw.getRevenueByCategory();
        assertNotNull(revenue);
        assertTrue(revenue.containsKey("Electronics"));
        assertTrue(revenue.containsKey("Clothing"));
        assertTrue(revenue.get("Electronics") > 4000);
    }
    
    @Test
    @DisplayName("3.2: getTopSellingProducts returns correct products")
    void testGetTopSellingProducts() {
        List<Product> top = hw.getTopSellingProducts(5);
        assertNotNull(top);
        assertEquals(5, top.size());
        // T-Shirt should be in top (ordered multiple times)
    }
    
    @Test
    @DisplayName("3.3: getProductQuantitySold returns correct quantities")
    void testGetProductQuantitySold() {
        Map<String, Integer> quantities = hw.getProductQuantitySold();
        assertNotNull(quantities);
        assertTrue(quantities.containsKey("P001")); // Laptop
        assertTrue(quantities.containsKey("P004")); // T-Shirt
        assertEquals(3, quantities.get("P001")); // 1 + 2 laptops
    }
    
    @Test
    @DisplayName("3.4: getCategorySummary returns correct summaries")
    void testGetCategorySummary() {
        Map<String, CategorySummary> summary = hw.getCategorySummary();
        assertNotNull(summary);
        assertTrue(summary.containsKey("Electronics"));
        assertTrue(summary.get("Electronics").totalRevenue() > 0);
        assertTrue(summary.get("Electronics").totalQuantity() > 0);
    }
    
    // =========================================================================
    // PART 4: Time-Based Analytics
    // =========================================================================
    
    @Test
    @DisplayName("4.1: getOrdersByMonth groups correctly")
    void testGetOrdersByMonth() {
        Map<YearMonth, List<CustomerOrder>> byMonth = hw.getOrdersByMonth();
        assertNotNull(byMonth);
        assertEquals(3, byMonth.size(), "Should have orders in 3 months");
        assertTrue(byMonth.containsKey(YearMonth.of(2024, 1)));
        assertTrue(byMonth.containsKey(YearMonth.of(2024, 2)));
        assertTrue(byMonth.containsKey(YearMonth.of(2024, 3)));
    }
    
    @Test
    @DisplayName("4.2: getMonthlyRevenue calculates correctly")
    void testGetMonthlyRevenue() {
        Map<YearMonth, Double> revenue = hw.getMonthlyRevenue();
        assertNotNull(revenue);
        assertTrue(revenue.get(YearMonth.of(2024, 1)) > 0);
    }
    
    @Test
    @DisplayName("4.3: getOrdersInDateRange returns correct orders")
    void testGetOrdersInDateRange() {
        LocalDate start = LocalDate.of(2024, 2, 1);
        LocalDate end = LocalDate.of(2024, 2, 28);
        List<CustomerOrder> orders = hw.getOrdersInDateRange(start, end);
        assertNotNull(orders);
        assertEquals(3, orders.size());
        
        for (CustomerOrder order : orders) {
            assertFalse(order.orderDate().isBefore(start));
            assertFalse(order.orderDate().isAfter(end));
        }
    }
    
    @Test
    @DisplayName("4.4: getDailyOrderCounts returns correct counts")
    void testGetDailyOrderCounts() {
        Map<LocalDate, Long> counts = hw.getDailyOrderCounts();
        assertNotNull(counts);
        assertEquals(10, counts.size(), "Should have 10 unique dates");
        counts.values().forEach(count -> 
            assertEquals(1L, count, "Each date should have exactly 1 order"));
    }
}
