import java.util.Locale;
import java.util.Scanner;

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
