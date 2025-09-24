package edu.ccrm.domain;

public class Course {
    private String code;
    private String title;
    private int credits;
    private String department;
    private Semester semester;


    public String getCode() { return code; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
    public String getDepartment() { return department; }
    public Semester getSemester() { return semester; }

    @Override
    public String toString() {
        return code + " - " + title + " (" + credits + " credits, " + semester + ")";
    }


    public static class Builder {
        private String code;
        private String title;
        private int credits;
        private String department;
        private Semester semester;

        public Builder code(String code) { this.code = code; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder credits(int credits) { this.credits = credits; return this; }
        public Builder department(String department) { this.department = department; return this; }
        public Builder semester(Semester semester) { this.semester = semester; return this; }

        public Course build() {
            Course course = new Course();
            course.code = this.code;
            course.title = this.title;
            course.credits = this.credits;
            course.department = this.department;
            course.semester = this.semester;
            return course;
        }
    }
}
