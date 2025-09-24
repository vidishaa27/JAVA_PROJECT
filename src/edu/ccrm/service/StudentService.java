package edu.ccrm.service;
import edu.ccrm.domain.Student;
import edu.ccrm.domain.Name;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class StudentService {
    private final Map<Long, Student> students = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Student addStudent(String regNo, Name name, String email) {
        long id = idGenerator.getAndIncrement();
        Student s = new Student(id, regNo, name, email);
        students.put(id, s);
        return s;
    }

    public Optional<Student> findById(long id) {
        return Optional.ofNullable(students.get(id));
    }

    public Optional<Student> findByRegNo(String regNo) {
        return students.values().stream()
                .filter(s -> s.getRegNo().equalsIgnoreCase(regNo))
                .findFirst();
    }

    public List<Student> listAll() {
        return new ArrayList<>(students.values());
    }

    public List<Student> searchByName(String part) {
        String p = part.toLowerCase();
        return students.values().stream()
                .filter(s -> s.getName().getFullName().toLowerCase().contains(p))
                .collect(Collectors.toList());
    }
}
