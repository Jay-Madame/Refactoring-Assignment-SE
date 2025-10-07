import java.util.List;
import java.util.Scanner;

final class GradeBookApp {
    private final GradeBook gradebook;
    private final Input inputToUse;

    GradeBookApp(GradeBook gradebook, Scanner sc) {
        this.gradebook = gradebook;
        this.inputToUse = new Input(sc);
    }

    void run() {
        System.out.println("GradeBook (Refactored)");
        boolean running = true;
        while (running) {
            printMenu();
            String choice = inputToUse.prompt("Choose: ");
            try {
                switch (choice) {
                    case "1" -> addStudent();
                    case "2" -> recordGrade();
                    case "3" -> listStudents();
                    case "4" -> showClassAverage();
                    case "5" -> showTopStudents();
                    case "0" -> running = false;
                    default -> System.out.println("Invalid.");
                }
            } catch (ValidationException ve) {
                System.out.println("Oops! " + ve.getMessage());
            } catch (IllegalArgumentException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
        System.out.println("Bye! Thanks for stopping by :D");
    }

    private void printMenu() {
        System.out.println("\nMenu:");
        System.out.println("1) Add student");
        System.out.println("2) Record grade");
        System.out.println("3) List students");
        System.out.println("4) Class average");
        System.out.println("5) Top students (based on grades)");
        System.out.println("0) Exit");
    }

    private void addStudent() {
        System.out.println("\n");
        String name = inputToUse.prompt("Student name (first name only): ");
        try {
            if (name.isEmpty()) {
                throw new ValidationException("Student name cannot be empty.");
            }
            gradebook.addStudent(name);
            System.out.println("Added: " + name.trim());
        } catch (ValidationException ve) {
            System.out.println("Oops! " + ve.getMessage());
            addStudent();
        }
        System.out.println("\n");
    }

    private void recordGrade() {
        System.out.println("\n");
        if (gradebook.listAll().isEmpty()) {
            System.out.println("You currently have no students entered.");
            String addStudentPrompt = inputToUse.prompt("Would you like to add a student? (Y/N): ");
            if (addStudentPrompt.equalsIgnoreCase("Y")) {
                addStudent();
            }
            return;
        }

        listStudents();
        try {
            String name = inputToUse.prompt("Select which student to add a grade for: ");
            Student student = gradebook.find(name).orElseThrow(() -> new ValidationException("No such student."));
            int grade = inputToUse.promptIntInRange("Grade (" + Config.MIN_GRADE + "-" + Config.MAX_GRADE + "): ", Config.MIN_GRADE, Config.MAX_GRADE);
            student.addGrade(grade);
        } catch (ValidationException ve) {
            System.out.println("Oops! " + ve.getMessage());
            return;
        }

        String loopUserAnswer = inputToUse.prompt("Would you like to add another grade? (Y/N): ");
        if (loopUserAnswer.equalsIgnoreCase("Y")) {
            recordGrade();
        }
        System.out.println("\n");
    }

    private void listStudents() {
        System.out.println("\n");
        if (gradebook.listAll().isEmpty()) {
            System.out.println("You currently have no students entered.");
            String addStudentPrompt = inputToUse.prompt("Would you like to add a student? (Y/N): ");
            if (addStudentPrompt.equalsIgnoreCase("Y")) {
                addStudent();
            }
            return;
        }
        System.out.println("Students:");
        gradebook.listAll().forEach(s -> System.out.println(Format.studentLine(s)));
        System.out.println("\n");
    }

    private void showClassAverage() {
        System.out.println("\n");
        double avg = gradebook.classAverage();
        if (Double.compare(avg, 0.0) == 0 && gradebook.listAll().stream().allMatch(s -> s.grades().isEmpty())) {
            String addGradesPrompt = inputToUse.prompt("No grades. Would you like to add some grades? (Y/N): ");
            if (addGradesPrompt.equalsIgnoreCase("Y")) {
                recordGrade();
            }
        } else {
            System.out.println("Class average = " + Format.avg(avg));
        }
        System.out.println("\n");
    }

    private void showTopStudents() {
        System.out.println("\n");
        try {
            int amountOfStudents = inputToUse.promptInt("Select your top amount of students: ");
            List<Student> best = gradebook.topN(amountOfStudents);
            System.out.println("Top " + amountOfStudents + " students:");
            for (int i = 0; i < best.size(); i++) {
                Student student = best.get(i);
                System.out.println(" " + (i + 1) + ". " + student.name() + " avg=" + Format.avg(student.average()));
            }
            if (best.isEmpty()) System.out.println(" (none)");
        } catch (ValidationException ve) {
            System.out.println("Oops! " + ve.getMessage());
        }
        System.out.println("\n");
    }
}