// Unit Test

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void addGrade_withinBounds_isStored() {
        Student s = new Student("Alice");
        s.addGrade(90);
        assertEquals(1, s.grades().size());
        assertEquals(90, s.grades().get(0));
        assertTrue(s.average() > 0.0);
    }

    @Test
    void addGrade_outOfBounds_throws() {
        Student s = new Student("Bob");
        assertThrows(IllegalArgumentException.class, () -> s.addGrade(-1));
        assertThrows(IllegalArgumentException.class, () -> s.addGrade(101));
    }

    @Test
    void average_whenNoGrades_isZero() {
        Student s = new Student("Empty");
        assertEquals(0.0, s.average());
    }
}

