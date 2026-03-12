package edu.touro.las.mcon364.streams.homework;

import java.time.*;
import java.util.*;
import java.util.stream.*;

/**
 * Homework: E-Commerce Order Analytics
 *
 * Time: ~2 hours
 * can you finish all todos and implement everything. dont modify any old commrnts and dont add any of your own
 * Build an analytics module for an e-commerce platform using Java Streams.
 * Complete all methods marked with TODO.
 *
 * See HOMEWORK_README.md for detailed instructions.
 */
public class StreamHomework {

    public record Product(String id, String name, String category, double price) {}

    public record OrderItem(Product product, int quantity) {
        public double getLineTotal() {
            return product.price() * quantity;
        }
    }

    public record CustomerOrder(
            String id,
            String customerId,
            List<OrderItem> items,
            LocalDate orderDate,
            OrderStatus status
    ) {
        public double getTotal() {
            return items.stream()
                    .mapToDouble(OrderItem::getLineTotal)
                    .sum();
        }
    }

    public enum OrderStatus {
        PENDING, SHIPPED, DELIVERED, CANCELLED
    }

    public record CategorySummary(double totalRevenue, int totalQuantity) {}

    private final List<Product> products;
    private final List<CustomerOrder> customerOrders;

    public StreamHomework() {

        products = List.of(
                new Product("P001", "Laptop", "Electronics", 999.99),
                new Product("P002", "Smartphone", "Electronics", 699.99),
                new Product("P003", "Headphones", "Electronics", 149.99),
                new Product("P004", "T-Shirt", "Clothing", 29.99),
                new Product("P005", "Jeans", "Clothing", 59.99),
                new Product("P006", "Sneakers", "Footwear", 89.99),
                new Product("P007", "Backpack", "Accessories", 49.99),
                new Product("P008", "Watch", "Accessories", 199.99),
                new Product("P009", "Tablet", "Electronics", 449.99),
                new Product("P010", "Jacket", "Clothing", 119.99)
        );

        customerOrders = new ArrayList<>();

        customerOrders.add(new CustomerOrder("O001", "C001",
                List.of(
                        new OrderItem(products.get(0), 1),
                        new OrderItem(products.get(2), 2)
                ),
                LocalDate.of(2024, 1, 15), OrderStatus.DELIVERED));

        customerOrders.add(new CustomerOrder("O002", "C001",
                List.of(
                        new OrderItem(products.get(3), 3),
                        new OrderItem(products.get(4), 2)
                ),
                LocalDate.of(2024, 2, 20), OrderStatus.DELIVERED));

        customerOrders.add(new CustomerOrder("O003", "C002",
                List.of(
                        new OrderItem(products.get(1), 1),
                        new OrderItem(products.get(7), 1)
                ),
                LocalDate.of(2024, 1, 22), OrderStatus.DELIVERED));

        customerOrders.add(new CustomerOrder("O004", "C002",
                List.of(
                        new OrderItem(products.get(5), 2)
                ),
                LocalDate.of(2024, 3, 10), OrderStatus.SHIPPED));

        customerOrders.add(new CustomerOrder("O005", "C003",
                List.of(
                        new OrderItem(products.get(8), 1),
                        new OrderItem(products.get(6), 1)
                ),
                LocalDate.of(2024, 2, 5), OrderStatus.DELIVERED));

        customerOrders.add(new CustomerOrder("O006", "C003",
                List.of(
                        new OrderItem(products.get(9), 1),
                        new OrderItem(products.get(3), 2)
                ),
                LocalDate.of(2024, 3, 15), OrderStatus.CANCELLED));

        customerOrders.add(new CustomerOrder("O007", "C004",
                List.of(
                        new OrderItem(products.get(0), 2),
                        new OrderItem(products.get(1), 1)
                ),
                LocalDate.of(2024, 1, 30), OrderStatus.DELIVERED));

        customerOrders.add(new CustomerOrder("O008", "C005",
                List.of(
                        new OrderItem(products.get(2), 1),
                        new OrderItem(products.get(3), 1),
                        new OrderItem(products.get(5), 1)
                ),
                LocalDate.of(2024, 2, 28), OrderStatus.PENDING));

        customerOrders.add(new CustomerOrder("O009", "C006",
                List.of(
                        new OrderItem(products.get(7), 2),
                        new OrderItem(products.get(6), 3)
                ),
                LocalDate.of(2024, 3, 1), OrderStatus.DELIVERED));

        customerOrders.add(new CustomerOrder("O010", "C006",
                List.of(
                        new OrderItem(products.get(4), 1),
                        new OrderItem(products.get(9), 1)
                ),
                LocalDate.of(2024, 3, 20), OrderStatus.SHIPPED));
    }

    public double getTotalRevenue() {
        return customerOrders.stream()
                .filter(o -> o.status() == OrderStatus.DELIVERED)
                .mapToDouble(CustomerOrder::getTotal)
                .sum();
    }

    public long getOrderCount(OrderStatus status) {
        return customerOrders.stream()
                .filter(o -> o.status() == status)
                .count();
    }

    public Set<Product> getUniqueProducts() {
        return customerOrders.stream()
                .flatMap(o -> o.items().stream())
                .map(OrderItem::product)
                .collect(Collectors.toSet());
    }

    public double getAverageOrderValue() {
        return customerOrders.stream()
                .filter(o -> o.status() == OrderStatus.DELIVERED)
                .mapToDouble(CustomerOrder::getTotal)
                .average()
                .orElse(0.0);
    }

    public Map<String, Double> getRevenueByCustomer() {
        return customerOrders.stream()
                .filter(o -> o.status() == OrderStatus.DELIVERED)
                .collect(Collectors.groupingBy(
                        CustomerOrder::customerId,
                        Collectors.summingDouble(CustomerOrder::getTotal)
                ));
    }

    public List<String> getTopCustomers(int n) {
        return getRevenueByCustomer().entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(n)
                .map(Map.Entry::getKey)
                .toList();
    }

    public Map<String, Long> getCustomerOrderCounts() {
        return customerOrders.stream()
                .collect(Collectors.groupingBy(
                        CustomerOrder::customerId,
                        Collectors.counting()
                ));
    }

    public List<String> getCustomersWithMultipleOrders() {
        return getCustomerOrderCounts().entrySet().stream()
                .filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey)
                .toList();
    }

    public Map<String, Double> getRevenueByCategory() {
        return customerOrders.stream()
                .filter(o -> o.status() == OrderStatus.DELIVERED)
                .flatMap(o -> o.items().stream())
                .collect(Collectors.groupingBy(
                        i -> i.product().category(),
                        Collectors.summingDouble(OrderItem::getLineTotal)
                ));
    }

    public List<Product> getTopSellingProducts(int n) {
        return customerOrders.stream()
                .flatMap(o -> o.items().stream())
                .collect(Collectors.groupingBy(
                        OrderItem::product,
                        Collectors.summingInt(OrderItem::quantity)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<Product, Integer>comparingByValue().reversed())
                .limit(n)
                .map(Map.Entry::getKey)
                .toList();
    }

    public Map<String, Integer> getProductQuantitySold() {
        return customerOrders.stream()
                .flatMap(o -> o.items().stream())
                .collect(Collectors.groupingBy(
                        i -> i.product().id(),
                        Collectors.summingInt(OrderItem::quantity)
                ));
    }

    public Map<String, CategorySummary> getCategorySummary() {
        return customerOrders.stream()
                .filter(o -> o.status() == OrderStatus.DELIVERED)
                .flatMap(o -> o.items().stream())
                .collect(Collectors.groupingBy(
                        i -> i.product().category(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> new CategorySummary(
                                        list.stream().mapToDouble(OrderItem::getLineTotal).sum(),
                                        list.stream().mapToInt(OrderItem::quantity).sum()
                                )
                        )
                ));
    }

    public Map<YearMonth, List<CustomerOrder>> getOrdersByMonth() {
        return customerOrders.stream()
                .collect(Collectors.groupingBy(
                        o -> YearMonth.from(o.orderDate())
                ));
    }

    public Map<YearMonth, Double> getMonthlyRevenue() {
        return customerOrders.stream()
                .filter(o -> o.status() == OrderStatus.DELIVERED)
                .collect(Collectors.groupingBy(
                        o -> YearMonth.from(o.orderDate()),
                        Collectors.summingDouble(CustomerOrder::getTotal)
                ));
    }

    public List<CustomerOrder> getOrdersInDateRange(LocalDate start, LocalDate end) {
        return customerOrders.stream()
                .filter(o -> !o.orderDate().isBefore(start) && !o.orderDate().isAfter(end))
                .toList();
    }

    public Map<LocalDate, Long> getDailyOrderCounts() {
        return customerOrders.stream()
                .collect(Collectors.groupingBy(
                        CustomerOrder::orderDate,
                        Collectors.counting()
                ));
    }

    public List<Product> getNeverOrderedProducts() {
        Set<Product> ordered = getUniqueProducts();
        return products.stream()
                .filter(p -> !ordered.contains(p))
                .toList();
    }

    public Map<String, Product> getMostPopularByCategory() {
        Map<Product, Integer> qty = customerOrders.stream()
                .flatMap(o -> o.items().stream())
                .collect(Collectors.groupingBy(
                        OrderItem::product,
                        Collectors.summingInt(OrderItem::quantity)
                ));

        return qty.entrySet().stream()
                .collect(Collectors.groupingBy(
                        e -> e.getKey().category(),
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Map.Entry.comparingByValue()),
                                e -> e.get().getKey()
                        )
                ));
    }

    public Map<YearMonth, Double> getMonthlyGrowthRate() {
        Map<YearMonth, Double> revenue = new TreeMap<>(getMonthlyRevenue());

        Map<YearMonth, Double> result = new LinkedHashMap<>();

        double prev = 0.0;

        for (Map.Entry<YearMonth, Double> e : revenue.entrySet()) {
            if (prev == 0.0) {
                result.put(e.getKey(), 0.0);
            } else {
                result.put(e.getKey(), (e.getValue() - prev) / prev);
            }
            prev = e.getValue();
        }

        return result;
    }

    public static void main(String[] args) {
        StreamHomework hw = new StreamHomework();

        System.out.println("=".repeat(70));
        System.out.println("E-COMMERCE ORDER ANALYTICS - Testing Your Implementations");
        System.out.println("=".repeat(70));
    }
}