import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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