import java.util.List;
import java.util.Optional;

interface GradeBook {
    boolean addStudent(String name);                 // false if exists
    Optional<Student> find(String name);
    List<Student> listAll();                         // deterministic order
    double classAverage();
    List<Student> topN(int n);
}