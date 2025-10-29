import java.util.*;
import java.util.stream.Collectors;

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