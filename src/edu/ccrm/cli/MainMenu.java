package edu.ccrm.cli;
import edu.ccrm.config.AppConfig;
import edu.ccrm.domain.*;
import edu.ccrm.exceptions.DuplicateEnrollmentException;
import edu.ccrm.exceptions.MaxCreditLimitExceededException;
import edu.ccrm.io.ImportExportService;
import edu.ccrm.service.CourseService;
import edu.ccrm.service.EnrollmentService;
import edu.ccrm.service.StudentService;
import edu.ccrm.service.TranscriptService;
import edu.ccrm.util.RecursionUtil;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
public class MainMenu {
    private final Scanner scanner = new Scanner(System.in);

    private final StudentService studentService = new StudentService();
    private final CourseService courseService = new CourseService();
    private final EnrollmentService enrollmentService =
            new EnrollmentService(AppConfig.getInstance().getMaxCredits());
    private final TranscriptService transcriptService = new TranscriptService();

    public static void main(String[] args) {
        AppConfig config = AppConfig.getInstance();
        System.out.println("[AppConfig] dataFolder=" + config.getDataFolder()
                + " maxCredits=" + config.getMaxCredits());

        new MainMenu().start();
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n==== CCRM ====");
            System.out.println("1. Students");
            System.out.println("2. Courses");
            System.out.println("3. Enroll / Grades");
            System.out.println("4. Reports (Transcript)");
            System.out.println("5. Import/Export");
            System.out.println("6. Backup");
            System.out.println("7. Reports (GPA Distribution)");
            System.out.println("0. Exit");
            System.out.print("Choice: ");

            switch (scanner.nextLine()) {
                case "1" -> manageStudents();
                case "2" -> manageCourses();
                case "3" -> manageEnrollments();
                case "4" -> showTranscript();
                case "5" -> manageImportExport();
                case "6" -> performBackup();
                case "7" -> showGpaDistribution();
                case "0" -> {
                    System.out.println("Exiting...");
                    running = false;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void manageStudents() {
        System.out.println("--- Students ---");
        System.out.println("1. List All");
        System.out.println("2. Add Student");
        System.out.println("3. Search by name");
        System.out.println("4. Update Student");
        System.out.println("0. Back");
        System.out.print("Choice: ");

        switch (scanner.nextLine()) {
            case "1" -> {
                List<Student> list = studentService.listAll();
                if (list.isEmpty()) System.out.println("No students found.");
                else list.forEach(System.out::println);
            }
            case "2" -> {
                System.out.print("RegNo: ");
                String reg = scanner.nextLine();
                System.out.print("First Name: ");
                String first = scanner.nextLine();
                System.out.print("Last Name: ");
                String last = scanner.nextLine();
                System.out.print("Email: ");
                String email = scanner.nextLine();
                Student s = studentService.addStudent(reg, new Name(first, last), email);
                System.out.println("Student added: " + s);
            }
            case "3" -> {
                System.out.print("Name part: ");
                String part = scanner.nextLine();
                List<Student> results = studentService.searchByName(part);
                if (results.isEmpty()) System.out.println("No matches.");
                else results.forEach(System.out::println);
            }
            case "4" -> handleUpdateStudent();
            case "0" -> {}
            default -> System.out.println("Invalid.");
        }
    }

    private void handleUpdateStudent() {
        System.out.print("Enter RegNo of student to update: ");
        String regNo = scanner.nextLine();
        Optional<Student> studentOpt = studentService.findByRegNo(regNo);

        if (studentOpt.isEmpty()) {
            System.out.println("Student not found.");
            return;
        }

        Student student = studentOpt.get();
        System.out.println("Updating student: " + student.getFullName());
        System.out.println("Current Email: " + student.getEmail());
        System.out.print("Enter new Email (or press Enter to keep current): ");
        String newEmail = scanner.nextLine();

        if (newEmail != null && !newEmail.isBlank()) {
            student.setEmail(newEmail);
            System.out.println("Student email updated successfully.");
        } else {
            System.out.println("Update cancelled. Email not changed.");
        }
    }


    private void manageCourses() {
        System.out.println("--- Courses ---");
        System.out.println("1. List All");
        System.out.println("2. Add Course");
        System.out.println("3. Search/Filter Courses");
        System.out.println("0. Back");
        System.out.print("Choice: ");

        switch (scanner.nextLine()) {
            case "1" -> {
                List<Course> list = courseService.listAll();
                if (list.isEmpty()) System.out.println("No courses found.");
                else list.forEach(System.out::println);
            }
            case "2" -> {
                System.out.print("Code: ");
                String code = scanner.nextLine();
                System.out.print("Title: ");
                String title = scanner.nextLine();
                System.out.print("Credits: ");
                int credits = Integer.parseInt(scanner.nextLine());
                System.out.print("Department: ");
                String dept = scanner.nextLine();
                // CORRECTED: Uses the builder methods, fixing the "private access" error
                Course c = new Course.Builder()
                        .code(code)
                        .title(title)
                        .credits(credits)
                        .department(dept)
                        .semester(Semester.SPRING)
                        .build();
                courseService.addCourse(c);
                System.out.println("Course added: " + c);
            }
            case "3" -> handleSearchCourses();
            case "0" -> {}
            default -> System.out.println("Invalid.");
        }
    }

    private void handleSearchCourses() {
        System.out.println("--- Search Courses ---");
        System.out.print("Enter department to filter by: ");
        String department = scanner.nextLine();

        List<Course> results = courseService.filterByDepartment(department);

        if (results.isEmpty()) {
            System.out.println("No courses found for department: " + department);
        } else {
            System.out.println("--- Search Results ---");
            results.forEach(System.out::println);
        }
    }


    private void manageEnrollments() {
        System.out.println("--- Enroll / Grades ---");
        System.out.println("1. Enroll Student");
        System.out.println("2. Assign Grade");
        System.out.println("0. Back");
        System.out.print("Choice: ");

        switch (scanner.nextLine()) {
            case "1" -> {
                System.out.print("Student RegNo: ");
                String reg = scanner.nextLine();
                Optional<Student> studentOpt = studentService.findByRegNo(reg);
                if (studentOpt.isEmpty()) {
                    System.out.println("Student not found.");
                    return;
                }

                System.out.print("Course Code: ");
                String code = scanner.nextLine();
                Optional<Course> courseOpt = courseService.findByCode(code);
                if (courseOpt.isEmpty()) {
                    System.out.println("Course not found.");
                    return;
                }

                try {
                    Enrollment e = enrollmentService.enroll(studentOpt.get(), courseOpt.get());
                    System.out.println("Enrolled: " + e);
                } catch (DuplicateEnrollmentException | MaxCreditLimitExceededException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
            case "2" -> {
                System.out.print("Student RegNo: ");
                String reg = scanner.nextLine();
                Optional<Student> studentOpt = studentService.findByRegNo(reg);
                if (studentOpt.isEmpty()) {
                    System.out.println("Student not found.");
                    return;
                }

                Student student = studentOpt.get();
                List<Enrollment> ungradedEnrollments = student.getEnrollments().stream()
                        .filter(e -> e.getGrade() == Grade.NA)
                        .toList();

                if (ungradedEnrollments.isEmpty()) {
                    System.out.println("No ungraded enrollments found for " + student.getFullName() + ".");
                    return;
                }

                System.out.println("Select a course to grade:");
                for (int i = 0; i < ungradedEnrollments.size(); i++) {
                    System.out.println((i + 1) + ". " + ungradedEnrollments.get(i).getCourse().getTitle());
                }
                System.out.println("0. Back");
                System.out.print("Choice: ");

                try {
                    int choice = Integer.parseInt(scanner.nextLine());
                    if (choice > 0 && choice <= ungradedEnrollments.size()) {
                        Enrollment enrollmentToGrade = ungradedEnrollments.get(choice - 1);
                        System.out.print("Enter Grade (S/A/B/C/D/F): ");
                        String gradeLetter = scanner.nextLine().trim().toUpperCase();
                        Grade grade = Grade.valueOf(gradeLetter);
                        enrollmentService.assignGrade(enrollmentToGrade, grade);
                        System.out.println("Grade assigned successfully: " + enrollmentToGrade);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid choice. Please enter a number.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid grade entered. Please use S, A, B, C, D, or F.");
                }
            }
            case "0" -> {}
            default -> System.out.println("Invalid.");
        }
    }


    private void showTranscript() {
        System.out.print("Student RegNo: ");
        String reg = scanner.nextLine();
        Optional<Student> studentOpt = studentService.findByRegNo(reg);
        if (studentOpt.isEmpty()) {
            System.out.println("Student not found.");
            return;
        }

        String transcript = transcriptService.generateTranscript(studentOpt.get());
        System.out.println(transcript);
    }


    private void manageImportExport() {
        ImportExportService ioService = new ImportExportService(AppConfig.getInstance().getDataFolder());

        System.out.println("--- Import/Export ---");
        System.out.println("1. Export Students");
        System.out.println("2. Export Courses");
        System.out.println("3. Import Students");
        System.out.println("4. Import Courses");
        System.out.println("0. Back");
        System.out.print("Choice: ");

        switch (scanner.nextLine()) {
            case "1" -> {
                try { ioService.exportStudents(studentService); }
                catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
            }
            case "2" -> {
                try { ioService.exportCourses(courseService); }
                catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
            }
            case "3" -> {
                try { ioService.importStudents(studentService); }
                catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
            }
            case "4" -> {
                try { ioService.importCourses(courseService); }
                catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
            }
            case "0" -> {}
            default -> System.out.println("Invalid choice.");
        }
    }


    private void performBackup() {
        System.out.println("--- Backup ---");
        Path dataFolder = Paths.get(AppConfig.getInstance().getDataFolder());
        Path backupFolder = dataFolder.resolve("backup_" + System.currentTimeMillis());

        try {
            Files.createDirectories(backupFolder);
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dataFolder, "*.csv")) {
                for (Path file : stream) {
                    Path dest = backupFolder.resolve(file.getFileName());
                    Files.copy(file, dest, StandardCopyOption.REPLACE_EXISTING);
                }
            }
            System.out.println("Backup completed to folder: " + backupFolder);

            long sizeInBytes = RecursionUtil.calculateDirectorySize(backupFolder);
            double sizeInKB = sizeInBytes / 1024.0;
            System.out.printf("Total backup size: %.2f KB%n", sizeInKB);

        } catch (IOException e) {
            System.out.println("Backup failed: " + e.getMessage());
        }
    }


    private void showGpaDistribution() {
        System.out.println("--- GPA Distribution ---");

        Map<String, Double> studentGpaMap = new HashMap<>();

        for (Student s : studentService.listAll()) {
            List<Enrollment> gradedEnrollments = s.getEnrollments().stream()
                    .filter(e -> e.getGrade() != null && e.getGrade() != Grade.NA)
                    .toList();

            if (gradedEnrollments.isEmpty()) continue;

            double totalPoints = 0;
            int totalCredits = 0;
            for (Enrollment e : gradedEnrollments) {
                totalPoints += e.getGrade().getPoints() * e.getCourse().getCredits();
                totalCredits += e.getCourse().getCredits();
            }
            if (totalCredits > 0) {
                double gpa = totalPoints / totalCredits;
                studentGpaMap.put(s.getRegNo(), gpa);
            }
        }

        if (studentGpaMap.isEmpty()) {
            System.out.println("No graded enrollments found.");
            return;
        }

        studentGpaMap.forEach((reg, gpa) ->
                System.out.printf("Student %s | GPA: %.2f%n", reg, gpa));

        Map<String, Long> distribution = studentGpaMap.values().stream()
                .collect(Collectors.groupingBy(
                        gpa -> {
                            if (gpa >= 9.0) return "S (9-10)";
                            if (gpa >= 8.0) return "A (8-9)";
                            if (gpa >= 7.0) return "B (7-8)";
                            if (gpa >= 6.0) return "C (6-7)";
                            if (gpa >= 5.0) return "D (5-6)";
                            else return "F (<5)";
                        }, Collectors.counting()
                ));

        System.out.println("\nGPA Distrition Summary:");
        distribution.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry ->
                        System.out.printf("%s: %d student(s)%n", entry.getKey(), entry.getValue()));
    }
}