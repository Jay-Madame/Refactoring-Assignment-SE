import java.util.List;

final class studentRepository {
    private String name;
    private List<Integer> grades;
    
    studentRepository(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Bad name.");
        this.name = name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Bad name.");
        this.name = name;
    }
    public String getName() {return(name);}

    public void addGrade(Integer grade) {
        if (g < Config.MIN_GRADE || g > Config.MAX_GRADE) {
            throw new IllegalArgumentException("Grade must be between " + Config.MIN_GRADE + " and " + Config.MAX_GRADE + ".");
        }
        grades.add(g);
    }

    public void setGrades(List<Integer> grades) {this.grades = grades;}
    public List<Integer> getGrades() {return(grades);}
}