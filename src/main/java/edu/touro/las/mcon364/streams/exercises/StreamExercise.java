package edu.touro.las.mcon364.streams.exercises;

import java.util.*;
import java.util.stream.*;

/**
 * In-Class Exercise: Working with Streams
 * Time: ~40 minutes
 * This exercise focuses on applying stream operations to analyze a gradebook
 * represented as a Map<String, List<Integer>> where:
 * - Key: Student name
 * - Value: List of grades (0-100)
 * Complete all methods marked with TODO.
 * Use stream operations - no explicit loops allowed!
 *
 * See EXERCISES_README.md for detailed instructions.
 */
public class StreamExercise {
    // The gradebook: student name -> list of grades
    private final Map<String, List<Integer>> gradebook;
    /**
     * Constructor initializes the gradebook with sample data.
     */
    public StreamExercise() {
        gradebook = new LinkedHashMap<>();
        gradebook.put("Alice", List.of(95, 87, 92, 88, 91));
        gradebook.put("Bob", List.of(78, 82, 75, 80, 79));
        gradebook.put("Carol", List.of(92, 95, 98, 94, 100));
        gradebook.put("David", List.of(65, 70, 68, 72, 66));
        gradebook.put("Eva", List.of(88, 85, 90, 87, 89));
        gradebook.put("Frank", List.of(55, 60, 58, 62, 52));
        gradebook.put("Grace", List.of(100, 98, 95, 97, 99));
        gradebook.put("Henry", List.of(72, 75, 70, 78, 74));
    }

    // =========================================================================
    // PART 1: Basic Queries
    // =========================================================================

    /**
     * Task 1.1: Return a sorted list of all student names.
     *
     * Expected output: [Alice, Bob, Carol, David, Eva, Frank, Grace, Henry]
     */
    public List<String> getAllStudentNames() {
        // TODO: Implement using streams
        // Hint: Use keySet().stream() and sorted()
        return gradebook.keySet().stream()
                .sorted()
                .toList();
    }

    /**
     * Task 1.2: Count the total number of students.
     *
     * Expected output: 8
     */
    public long countStudents() {
        // TODO: Implement using streams
        return gradebook.keySet().stream().count();
    }

    /**
     * Task 1.3: Get grades for a specific student.
     * Return an empty list if the student is not found.
     *
     * Example:
     *   getStudentGrades("Alice")   -> [95, 87, 92, 88, 91]
     *   getStudentGrades("Unknown") -> []
     *
     * Implementation Requirements:
     *
     * 1. Do NOT return null.
     * 2. Use Optional to safely handle the possibility that the student
     *    may not exist in the map.
     */
    public List<Integer> getStudentGrades(String studentName) {

        // TODO:
        // 1. Retrieve the value from the map using gradebook.get(studentName)
        // 2. Wrap it in Optional.ofNullable(...)
        // 3. Use orElse(...) to return an empty list if null

        return Optional.ofNullable(gradebook.get(studentName))
                .orElse(List.of());
    }

    // =========================================================================
    // PART 2: Grade Analysis
    // =========================================================================

    /**
     * Task 2.1: Calculate the average grade for a specific student.
     * Return 0.0 if student not found.
     *
     * Example: calculateAverage("Alice") -> 90.6
     * Example: calculateAverage("Unknown") -> 0.0
     */
    public double calculateAverage(String studentName) {
        // TODO: Implement using streams
        // Hint: Use mapToInt() and average()
        return Optional.ofNullable(gradebook.get(studentName))
                .orElse(List.of())
                .stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    /**
     * Task 2.2: Flatten all grades into a single sorted list.
     *
     * Expected: A sorted list of all grades from all students
     */
    public List<Integer> getAllGradesFlattened() {
        // TODO: Implement using streams
        // Hint: Use flatMap() to flatten the lists
        return gradebook.values().stream()
                .flatMap(List::stream)
                .sorted()
                .toList();
    }

    /**
     * Task 2.3: Find the highest grade across all students.
     *
     * Expected output: 100 (Grace has perfect scores)
     */
    public int findHighestGrade() {
        // TODO: Implement using streams
        // Hint: Flatten first, then find max
        return gradebook.values().stream()
                .flatMap(List::stream)
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);
    }

    /**
     * Task 2.4: Find the lowest grade across all students.
     *
     * Expected output: 52 (Frank's lowest)
     */
    public int findLowestGrade() {
        // TODO: Implement using streams
        return gradebook.values().stream()
                .flatMap(List::stream)
                .mapToInt(Integer::intValue)
                .min()
                .orElse(0);
    }

    /**
     * Task 2.5: Count total number of grades across all students.
     *
     * Expected output: 40 (8 students × 5 grades each)
     */
    public long getTotalGradeCount() {
        // TODO: Implement using streams
        // Hint: You can use flatMap and count, or sum the sizes
        return gradebook.values().stream()
                .flatMap(List::stream)
                .count();
    }

    // =========================================================================
    // PART 3: Filtering and Grouping
    // =========================================================================

    /**
     * Task 3.1: Get names of students whose average is >= threshold.
     *
     * Example: getPassingStudents(80) -> [Alice, Carol, Eva, Grace]
     */
    public List<String> getPassingStudents(double threshold) {
        // TODO: Implement using streams
        // Hint: Filter entries based on average of their grades
        return gradebook.entrySet().stream()
                .filter(e -> e.getValue().stream()
                        .mapToInt(Integer::intValue)
                        .average()
                        .orElse(0.0) >= threshold)
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * Task 3.2: Get names of students whose average is < threshold.
     *
     * Example: getFailingStudents(70) -> [Frank]
     */
    public List<String> getFailingStudents(double threshold) {
        // TODO: Implement using streams
        return gradebook.entrySet().stream()
                .filter(e -> e.getValue().stream()
                        .mapToInt(Integer::intValue)
                        .average()
                        .orElse(0.0) < threshold)
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * Task 3.3: Group students by letter grade based on their average.
     *
     * Grading scale:
     * - "A": 90+
     * - "B": 80-89
     * - "C": 70-79
     * - "D": 60-69
     * - "F": below 60
     *
     * Expected output structure:
     * {
     *   "A" -> [Alice, Carol, Grace],
     *   "B" -> [Eva],
     *   "C" -> [Bob, Henry],
     *   "D" -> [David],
     *   "F" -> [Frank]
     * }
     */
    public Map<String, List<String>> groupByPerformance() {
        // TODO: Implement using streams
        // Hint: Use Collectors.groupingBy() with a classifier function
        return gradebook.entrySet().stream()
                .collect(Collectors.groupingBy(
                        e -> getLetterGrade(
                                e.getValue().stream()
                                        .mapToInt(Integer::intValue)
                                        .average()
                                        .orElse(0.0)),
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())
                ));
    }

    /**
     * Task 3.4: Create a map of student name to their average grade.
     *
     * Expected: {Alice=90.6, Bob=78.8, Carol=95.8, ...}
     */
    public Map<String, Double> getStudentAverages() {
        // TODO: Implement using streams
        // Hint: Use Collectors.toMap() with a value mapper that calculates average
        return gradebook.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream()
                                .mapToInt(Integer::intValue)
                                .average()
                                .orElse(0.0)
                ));
    }

    /**
     * Task 3.5: Find the name of the student with the highest average.
     *
     * Expected output: "Grace" (average 97.8)
     */
    public String findTopPerformer() {
        // TODO: Implement using streams
        // Hint: Use max() with a comparator based on average
        return gradebook.entrySet().stream()
                .max(Comparator.comparingDouble(
                        e -> e.getValue().stream()
                                .mapToInt(Integer::intValue)
                                .average()
                                .orElse(0.0)))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    // =========================================================================
    // BONUS CHALLENGES
    // =========================================================================

    /**
     * Bonus 1: Find all students who have at least one perfect score (100).
     *
     * Expected: [Carol, Grace]
     */
    public List<String> getStudentsWithPerfectScore() {
        // TODO: Implement if time permits
        return gradebook.entrySet().stream()
                .filter(e -> e.getValue().stream().anyMatch(g -> g == 100))
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * Bonus 2: Calculate the class average (average of ALL grades).
     *
     * Expected: approximately 81.275
     */
    public double calculateClassAverage() {
        // TODO: Implement if time permits
        return gradebook.values().stream()
                .flatMap(List::stream)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    /**
     * Bonus 3: Find the student with the most consistent grades
     * (lowest standard deviation).
     *
     * Hint: Standard deviation = sqrt(sum((x - mean)^2) / n)
     */
    public String findMostConsistentStudent() {
        // TODO: Implement if time permits
        return gradebook.entrySet().stream()
                .min(Comparator.comparingDouble(e -> {
                    List<Integer> grades = e.getValue();
                    double mean = grades.stream().mapToInt(Integer::intValue).average().orElse(0.0);
                    double variance = grades.stream()
                            .mapToDouble(g -> Math.pow(g - mean, 2))
                            .sum() / grades.size();
                    return Math.sqrt(variance);
                }))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    // =========================================================================
    // HELPER METHOD - Letter grade classifier
    // =========================================================================

    /**
     * Helper method to convert numeric average to letter grade.
     * You may use this in your groupByPerformance() implementation.
     */
    private String getLetterGrade(double average) {
        if (average >= 90) return "A";
        if (average >= 80) return "B";
        if (average >= 70) return "C";
        if (average >= 60) return "D";
        return "F";
    }

    // =========================================================================
    // MAIN METHOD - Test your implementations
    // =========================================================================

    public static void main(String[] args) {
        StreamExercise exercise = new StreamExercise();

        System.out.println("=".repeat(60));
        System.out.println("STREAM EXERCISE - Testing Your Implementations");
        System.out.println("=".repeat(60));

        System.out.println("\n--- PART 1: Basic Queries ---");
        System.out.println("1.1 All student names: " + exercise.getAllStudentNames());
        System.out.println("1.2 Student count: " + exercise.countStudents());
        System.out.println("1.3 Alice's grades: " + exercise.getStudentGrades("Alice"));
        System.out.println("1.3 Unknown's grades: " + exercise.getStudentGrades("Unknown"));

        System.out.println("\n--- PART 2: Grade Analysis ---");
        System.out.println("2.1 Alice's average: " + exercise.calculateAverage("Alice"));
        System.out.println("2.2 All grades flattened: " + exercise.getAllGradesFlattened());
        System.out.println("2.3 Highest grade: " + exercise.findHighestGrade());
        System.out.println("2.4 Lowest grade: " + exercise.findLowestGrade());
        System.out.println("2.5 Total grade count: " + exercise.getTotalGradeCount());

        System.out.println("\n--- PART 3: Filtering and Grouping ---");
        System.out.println("3.1 Passing students (>=80): " + exercise.getPassingStudents(80));
        System.out.println("3.2 Failing students (<70): " + exercise.getFailingStudents(70));
        System.out.println("3.3 Grouped by performance: " + exercise.groupByPerformance());
        System.out.println("3.4 Student averages: " + exercise.getStudentAverages());
        System.out.println("3.5 Top performer: " + exercise.findTopPerformer());

        System.out.println("\n--- BONUS CHALLENGES ---");
        System.out.println("Bonus 1 - Perfect scores: " + exercise.getStudentsWithPerfectScore());
        System.out.println("Bonus 2 - Class average: " + exercise.calculateClassAverage());
        System.out.println("Bonus 3 - Most consistent: " + exercise.findMostConsistentStudent());

        System.out.println("\n" + "=".repeat(60));
        System.out.println("Check your results against the expected values above!");
        System.out.println("=".repeat(60));
    }
}