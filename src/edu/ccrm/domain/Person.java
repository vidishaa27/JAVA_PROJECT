package edu.ccrm.domain;

import java.time.LocalDate;
import java.util.Objects;


public abstract class Person {
    private final long id;
    private final String regNo;
    private Name name;
    private String email;
    private final LocalDate createdAt;
    private boolean active = true;

    protected Person(long id, String regNo, Name name, String email) {
        this.id = id;
        this.regNo = regNo;
        this.name = Objects.requireNonNull(name, "name");
        this.email = Objects.requireNonNull(email, "email");
        this.createdAt = LocalDate.now();
    }


    public long getId() { return id; }
    public String getRegNo() { return regNo; }
    public Name getName() { return name; }
    public void setName(Name name) { this.name = Objects.requireNonNull(name); }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = Objects.requireNonNull(email); }
    public LocalDate getCreatedAt() { return createdAt; }
    public boolean isActive() { return active; }
    public void deactivate() { this.active = false; }
    public void activate() { this.active = true; }


    public String getFullName() {
        return name.getFullName();
    }

    public String profileSummary() {
        return String.format("[%d] %s (%s) - %s", id, name.getFullName(), regNo == null ? "N/A" : regNo, email);
    }

    @Override
    public String toString() {
        return profileSummary();
    }
}