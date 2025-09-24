package edu.ccrm.domain;

import java.util.Objects;


public class Instructor extends Person {
    private String department;
    private String title;

    public Instructor(long id, String regNoOrCode, Name name, String email, String department, String title) {
        super(id, regNoOrCode, Objects.requireNonNull(name), Objects.requireNonNull(email));
        this.department = Objects.requireNonNull(department);
        this.title = Objects.requireNonNull(title);
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = Objects.requireNonNull(department); }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = Objects.requireNonNull(title); }

    @Override
    public String toString() {
        return String.format("Instructor %s | %s | %s", getName().getFullName(), title, department);
    }
}
