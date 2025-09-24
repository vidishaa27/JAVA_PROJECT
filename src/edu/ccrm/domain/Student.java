package edu.ccrm.domain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class Student extends Person {

    private final List<Enrollment> enrollments = new ArrayList<>();

    public Student(long id, String regNo, Name name, String email) {
        super(id, regNo, name, email);
    }

    public List<Enrollment> getEnrollments() {
        return Collections.unmodifiableList(enrollments);
    }

    public boolean addEnrollment(Enrollment enrollment) {
        Objects.requireNonNull(enrollment, "enrollment");
        for (Enrollment e : enrollments) {
            if (e.getCourse().getCode().equalsIgnoreCase(enrollment.getCourse().getCode())) {
                return false;
            }
        }
        return enrollments.add(enrollment);
    }

    public boolean removeEnrollment(Enrollment enrollment) {
        return enrollments.remove(enrollment);
    }

    public int currentCreditLoad() {
        return enrollments.stream().mapToInt(e -> e.getCourse().getCredits()).sum();
    }

    @Override
    public String toString() {
        return String.format("Student %s | regNo=%s | credits=%d | active=%b",
                getName().getFullName(), getRegNo(), currentCreditLoad(), isActive());
    }


    public interface TranscriptLine {
        String getCourseCode();
        String getCourseTitle();
        int getCredits();
        String getGrade();
        String toString();
    }


    public class TranscriptEntry implements TranscriptLine {
        private final Course course;
        private final String grade;

        public TranscriptEntry(Course course, String grade) {
            this.course = Objects.requireNonNull(course, "course");
            this.grade = Objects.requireNonNull(grade, "grade");
        }

        @Override
        public String getCourseCode() {
            return course.getCode();
        }

        @Override
        public String getCourseTitle() {
            return course.getTitle();
        }

        @Override
        public int getCredits() {
            return course.getCredits();
        }

        @Override
        public String getGrade() {
            return grade;
        }

        @Override
        public String toString() {
            return String.format("%s - %s (%d credits) | Grade: %s",
                    getCourseCode(), getCourseTitle(), getCredits(), grade);
        }
    }


    public List<TranscriptLine> generateTranscript() {
        List<TranscriptLine> transcript = new ArrayList<>();
        for (Enrollment e : enrollments) {
            // Convert Grade enum to String safely
            String gradeStr = e.getGrade() != null ? e.getGrade().name() : "N/A";
            transcript.add(new TranscriptEntry(e.getCourse(), gradeStr));
        }
        return transcript;
    }
}
