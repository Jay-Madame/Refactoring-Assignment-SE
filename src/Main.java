import java.util.*;
import java.util.stream.Collectors;

/**
 * GradeBook – Refactored
 * Goals achieved:
 *  - Separate concerns (CLI, domain, storage, formatting, validation)
 *  - No global mutable state
 *  - Replace primitive obsession with domain objects (Student)
 *  - Eliminate magic numbers via Config
 *  - Centralize formatting and validation
 *  - Small, cohesive methods and classes
 */
public class Main {
    public static void main(String[] args) {
        new GradeBookApp(new InMemoryGradeBook(), new Scanner(System.in)).run();
    }
}
