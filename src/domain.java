import java.util.List;

final class Student {
    private studentRepository repository;

    Student(String name) {
        repository.setName(name);
    }

    void addGrade(int g) {
        repository.addGrade(g);
    }

    double average() {
        List<Integer> grades = repository.getGrades();
        if (grades.isEmpty()) return 0.0;
        long sum = 0; for (int g : grades) sum += g; // no boxing
        return sum / (double) grades.size();
    }
}