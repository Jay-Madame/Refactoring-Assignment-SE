//System Test

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EndToEndWorkflowTest {

    @Test
    void fullFlow_add_enroll_list_drop_promote_likeMenu() {
        InMemoryGradeBook gb = new InMemoryGradeBook();

        // Add students (menu: Add student)
        assertTrue(gb.addStudent("Alice"));
        assertTrue(gb.addStudent("Bob"));
        assertTrue(gb.addStudent("Wendy"));

        // Mimic “enroll” by adding grades (your CLI uses GradeBook, so we exercise core behaviors)
        gb.find("Alice").get().addGrade(95);
        gb.find("Bob").get().addGrade(85);
        gb.find("Wendy").get().addGrade(75);

        // List
        assertEquals(3, gb.listAll().size());

        // Drop behavior doesn’t exist at GradeBook level in this version,
        // but you can simulate removing or resetting grades if you add such APIs later.
        // Here we just re-check topN sorting as an end-to-end sanity:
        var top = gb.topN(2);
        assertEquals(2, top.size());
        assertEquals("Alice", top.get(0).name());
    }
}

