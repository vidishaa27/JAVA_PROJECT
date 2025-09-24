package edu.ccrm.service;
import edu.ccrm.domain.Course;
import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Grade;
import edu.ccrm.domain.Student;
import edu.ccrm.exceptions.DuplicateEnrollmentException;
import edu.ccrm.exceptions.MaxCreditLimitExceededException;

public class EnrollmentService {
    private final int maxCredits;

    public EnrollmentService(int maxCredits) {
        this.maxCredits = maxCredits;
    }

    public Enrollment enroll(Student student, Course course)
            throws DuplicateEnrollmentException, MaxCreditLimitExceededException {

        for (Enrollment e : student.getEnrollments()) {
            if (e.getCourse().getCode().equalsIgnoreCase(course.getCode())) {
                throw new DuplicateEnrollmentException("Student already enrolled in " + course.getCode());
            }
        }

        int totalCredits = student.getEnrollments().stream()
                .mapToInt(e -> e.getCourse().getCredits())
                .sum() + course.getCredits();
        if (totalCredits > maxCredits) {
            throw new MaxCreditLimitExceededException("Exceeds max credits: " + maxCredits);
        }

        Enrollment enrollment = new Enrollment(student, course);
        student.addEnrollment(enrollment);
        return enrollment;
    }

    public void assignGrade(Enrollment enrollment, Grade grade) {
        enrollment.setGrade(grade);
    }
}
