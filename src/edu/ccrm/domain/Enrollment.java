package edu.ccrm.domain;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Enrollment ties a Student to a Course and stores grade (if assigned).
 */
public class Enrollment {
    private final Student student;
    private final Course course;
    private final LocalDate enrolledAt;
    private Grade grade;

    public Enrollment(Student student, Course course) {
        this.student = Objects.requireNonNull(student, "student");
        this.course = Objects.requireNonNull(course, "course");
        this.enrolledAt = LocalDate.now();
        this.grade=Grade.NA;
    }

    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
    public LocalDate getEnrolledAt() { return enrolledAt; }

    public Grade getGrade() { return grade; }
    public void setGrade(Grade grade) { this.grade = grade; }

    public boolean hasGrade() { return grade != null; }

    @Override
    public String toString() {
        return String.format("Enrollment: %s -> %s | credits=%d | enrolled=%s | grade=%s",
                student.getName().getFullName(),
                course.getCode(),
                course.getCredits(),
                enrolledAt,
                grade == null ? "N/A" : grade.getLetter());
    }
}
