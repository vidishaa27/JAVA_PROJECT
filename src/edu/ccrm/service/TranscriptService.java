package edu.ccrm.service;
import edu.ccrm.domain.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TranscriptService {

    public String generateTranscript(Student student) {
        StringBuilder sb = new StringBuilder();
        sb.append("==== Transcript for ").append(student.getName().getFullName())
                .append(" (").append(student.getRegNo()).append(") ====\n");

        List<Enrollment> enrollments = student.getEnrollments();
        if (enrollments.isEmpty()) {
            sb.append("No enrollments found.\n");
            return sb.toString();
        }

        double totalPoints = 0;
        int totalCredits = 0;

        for (Enrollment e : enrollments) {
            sb.append(String.format("%s | %s | %d credits | Grade: %s\n",
                    e.getCourse().getCode(),
                    e.getCourse().getTitle(),
                    e.getCourse().getCredits(),
                    e.hasGrade() ? e.getGrade().getLetter() : "N/A"));

            if (e.hasGrade()) {
                totalPoints += e.getGrade().getPoints() * e.getCourse().getCredits();
                totalCredits += e.getCourse().getCredits();
            }
        }

        if (totalCredits > 0) {
            double gpa = totalPoints / totalCredits;
            sb.append(String.format("GPA: %.2f\n", gpa));
        } else {
            sb.append("GPA: N/A (no grades yet)\n");
        }

        return sb.toString();
    }

    public Optional<String> tryTranscript(Student student) {
        if (student == null) return Optional.empty();
        return Optional.of(generateTranscript(student));
    }

    public void gpaDistribution(List<Student> students) {
        if (students == null || students.isEmpty()) {
            System.out.println("No students available for GPA distribution.");
            return;
        }

        var distribution = students.stream()
                .flatMap(s -> s.getEnrollments().stream())
                .filter(Enrollment::hasGrade)
                .collect(Collectors.groupingBy(
                        e -> e.getGrade().getLetter(),
                        Collectors.counting()
                ));

        System.out.println("GPA Distribution:");
        distribution.forEach((grade, count) ->
                System.out.println("Grade " + grade + ": " + count + " occurrence(s)"));
    }
}
