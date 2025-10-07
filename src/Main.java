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

//CONFIG & UTILITIES
final class Config {
    private Config() {}
    static final int MIN_GRADE = 0;
    static final int MAX_GRADE = 100;
}

final class Format {
    private Format() {}
    static String avg(double a) { return String.format(Locale.US, "%.2f", a); }
    static String studentLine(Student s) {
        return " - " + s.name() + " | grades=" + s.grades() + " | avg=" + avg(s.average());
    }
}

final class Input {
    private final Scanner sc;
    Input(Scanner sc) { this.sc = sc; }

    String prompt(String msg) {
        System.out.print(msg);
        return sc.nextLine().trim();
    }

    int promptInt(String msg) {
        String s = prompt(msg);
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { throw new IllegalArgumentException("Not a number."); }
    }

    int promptIntInRange(String msg, int lo, int hi) {
        int v = promptInt(msg);
        if (v < lo || v > hi) throw new IllegalArgumentException("Out of range [" + lo + ", " + hi + "].");
        return v;
    }
}

//DOMAIN
final class Student {
    private final String name;
    private final List<Integer> grades = new ArrayList<>();
    Student(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Bad name.");
        this.name = name.trim();
    }
    String name() { return name; }
    List<Integer> grades() { return Collections.unmodifiableList(grades); }

    void addGrade(int g) {
        if (g < Config.MIN_GRADE || g > Config.MAX_GRADE) {
            throw new IllegalArgumentException("Grade must be between " + Config.MIN_GRADE + " and " + Config.MAX_GRADE + ".");
        }
        grades.add(g);
    }

    double average() {
        if (grades.isEmpty()) return 0.0;
        long sum = 0; for (int g : grades) sum += g; // no boxing
        return sum / (double) grades.size();
    }
}

//PORTS/API
interface GradeBook {
    boolean addStudent(String name);                 // false if exists
    Optional<Student> find(String name);
    List<Student> listAll();                         // deterministic order
    double classAverage();
    List<Student> topN(int n);
}

//IMPLEMENTATION
final class InMemoryGradeBook implements GradeBook {
    private final Map<String, Student> byName = new LinkedHashMap<>();

    @Override public boolean addStudent(String name) {
        if (name == null) return false;
        String key = name.trim();
        if (key.isEmpty() || byName.containsKey(key)) return false;
        byName.put(key, new Student(key));
        return true;
    }

    @Override public Optional<Student> find(String name) {
        if (name == null) return Optional.empty();
        return Optional.ofNullable(byName.get(name.trim()));
    }

    @Override public List<Student> listAll() { return new ArrayList<>(byName.values()); }

    @Override public double classAverage() {
        List<Integer> all = byName.values().stream()
                .flatMap(s -> s.grades().stream())
                .collect(Collectors.toList());
        if (all.isEmpty()) return 0.0;
        long sum = 0; for (int g : all) sum += g;
        return sum / (double) all.size();
    }

    @Override public List<Student> topN(int n) {
        return byName.values().stream()
                .sorted(Comparator.comparingDouble(Student::average).reversed())
                .limit(Math.max(0, n))
                .collect(Collectors.toList());
    }
}

//CLI
final class GradeBookApp {
    private final GradeBook gb;
    private final Input in;

    GradeBookApp(GradeBook gb, Scanner sc) {
        this.gb = gb;
        this.in = new Input(sc);
    }

    void run() {
        System.out.println("GradeBook (Refactored)");
        boolean running = true;
        while (running) {
            printMenu();
            String choice = in.prompt("Choose: ");
            try {
                switch (choice) {
                    case "1" -> addStudent();
                    case "2" -> recordGrade();
                    case "3" -> listStudents();
                    case "4" -> showClassAverage();
                    case "5" -> showTopN();
                    case "0" -> running = false;
                    default -> System.out.println("Invalid.");
                }
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            }
        }
        System.out.println("Bye.");
    }

    private void printMenu() {
        System.out.println("\nMenu:");
        System.out.println("1) Add student");
        System.out.println("2) Record grade");
        System.out.println("3) List students");
        System.out.println("4) Class average");
        System.out.println("5) Top N students");
        System.out.println("0) Exit");
    }

    private void addStudent() {
        String name = in.prompt("Student name: ");
        boolean ok = gb.addStudent(name);
        System.out.println(ok ? ("Added: " + name.trim()) : "Exists or bad name.");
    }

    private void recordGrade() {
        String name = in.prompt("Student name: ");
        Student s = gb.find(name).orElseThrow(() -> new IllegalArgumentException("No such student."));
        int g = in.promptIntInRange("Grade (" + Config.MIN_GRADE + "-" + Config.MAX_GRADE + "): ", Config.MIN_GRADE, Config.MAX_GRADE);
        s.addGrade(g);
        System.out.println("OK.");
    }

    private void listStudents() {
        System.out.println("Students:");
        if (gb.listAll().isEmpty()) { System.out.println(" (none)"); return; }
        gb.listAll().forEach(s -> System.out.println(Format.studentLine(s)));
    }

    private void showClassAverage() {
        double avg = gb.classAverage();
        if (Double.compare(avg, 0.0) == 0 && gb.listAll().stream().allMatch(s -> s.grades().isEmpty())) {
            System.out.println("No grades.");
        } else {
            System.out.println("Class average = " + Format.avg(avg));
        }
    }

    private void showTopN() {
        int n = in.promptInt("N: ");
        List<Student> best = gb.topN(n);
        System.out.println("Top " + n + ":");
        for (int i = 0; i < best.size(); i++) {
            Student s = best.get(i);
            System.out.println(" " + (i + 1) + ". " + s.name() + " avg=" + Format.avg(s.average()));
        }
        if (best.isEmpty()) System.out.println(" (none)");
    }
}
