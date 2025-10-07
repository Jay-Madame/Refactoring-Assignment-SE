import java.util.List;
import java.util.Scanner;

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