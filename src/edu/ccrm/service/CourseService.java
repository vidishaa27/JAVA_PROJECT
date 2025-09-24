package edu.ccrm.service;

import edu.ccrm.domain.Course;
import java.util.*;
import java.util.stream.Collectors;

public class CourseService {
    private final Map<String, Course> courses = new HashMap<>();
    // private final AtomicInteger codeCounter = new AtomicInteger(100);

    public Course addCourse(Course course) {
        courses.put(course.getCode(), course);
        return course;
    }

    public Optional<Course> findByCode(String code) {
        return Optional.ofNullable(courses.get(code));
    }

    public List<Course> listAll() {
        return new ArrayList<>(courses.values());
    }


    public List<Course> filterByDepartment(String department) {
        return courses.values().stream()
                .filter(course -> course.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }
}