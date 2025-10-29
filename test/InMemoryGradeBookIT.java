// Integration Test

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryGradeBookIT {

    @Test
    void addStudent_find_listAll_work() {
        InMemoryGradeBook gb = new InMemoryGradeBook();
        assertTrue(gb.addStudent("Alice"));
        assertTrue(gb.addStudent("Bob"));
        assertFalse(gb.addStudent("Bob")); // duplicate

        assertTrue(gb.find("Alice").isPresent());
        assertTrue(gb.find(" NOPE ").isEmpty());

        List<Student> all = gb.listAll();
        assertEquals(2, all.size());
    }

    @Test
    void classAverage_and_topN() {
        InMemoryGradeBook gb = new InMemoryGradeBook();
        gb.addStudent("A");
        gb.addStudent("B");
        gb.addStudent("C");
        gb.find("A").get().addGrade(100);
        gb.find("B").get().addGrade(80);
        gb.find("C").get().addGrade(90);

        assertEquals(90.0, gb.classAverage(), 1e-9);

        var top2 = gb.topN(2);
        assertEquals(List.of("A","C"), top2.stream().map(Student::name).toList());
    }
}

