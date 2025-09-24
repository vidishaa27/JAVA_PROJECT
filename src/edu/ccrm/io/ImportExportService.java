package edu.ccrm.io;
import edu.ccrm.domain.*;
import edu.ccrm.service.CourseService;
import edu.ccrm.service.StudentService;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;


public class ImportExportService {
    private final Path dataFolder;

    public ImportExportService(String dataFolder) {
        this.dataFolder = Paths.get(dataFolder);
        try {
            if (!Files.exists(this.dataFolder)) {
                Files.createDirectories(this.dataFolder);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create data folder", e);
        }
    }


    public void exportStudents(StudentService studentService) throws IOException {
        Path file = dataFolder.resolve("students_export.csv");
        List<String> lines = studentService.listAll().stream()
                .map(s -> String.join(",", s.getRegNo(), s.getName().getFullName(), s.getEmail()))
                .collect(Collectors.toList());
        Files.write(file, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        System.out.println("Exported students to " + file);
    }

    public void exportCourses(CourseService courseService) throws IOException {
        Path file = dataFolder.resolve("courses_export.csv");
        List<String> lines = courseService.listAll().stream()
                .map(c -> String.join(",", c.getCode(), c.getTitle(), String.valueOf(c.getCredits()), c.getDepartment()))
                .collect(Collectors.toList());
        Files.write(file, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        System.out.println("Exported courses to " + file);
    }


    public void importStudents(StudentService studentService) throws IOException {
        Path file = dataFolder.resolve("students_import.csv");
        if (!Files.exists(file)) {
            System.out.println("No import file found: " + file);
            return;
        }
        List<String> lines = Files.readAllLines(file);
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split(",");
            if (parts.length < 3) continue;
            String regNo = parts[0].trim();
            String[] names = parts[1].trim().split(" ");
            String first = names[0];
            String last = names.length > 1 ? names[names.length - 1] : "";
            String email = parts[2].trim();
            studentService.addStudent(regNo, new Name(first, last), email);
        }
        System.out.println("Imported students from " + file);
    }

    public void importCourses(CourseService courseService) throws IOException {
        Path file = dataFolder.resolve("courses_import.csv");
        if (!Files.exists(file)) {
            System.out.println("No import file found: " + file);
            return;
        }
        List<String> lines = Files.readAllLines(file);
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split(",");
            if (parts.length < 4) continue;
            try {
                Course c = new Course.Builder()
                        .code(parts[0].trim())
                        .title(parts[1].trim())
                        .credits(Integer.parseInt(parts[2].trim()))
                        .department(parts[3].trim())
                        .semester(Semester.SPRING)
                        .build();
                courseService.addCourse(c);
            } catch (NumberFormatException ex) {
                System.out.println("Skipping course with invalid credits: " + line);
            }
        }
        System.out.println("Imported courses from " + file);
    }
}
