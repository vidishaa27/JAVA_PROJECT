package edu.ccrm.domain;

import java.util.Objects;


public final class Name {
    private final String firstName;
    private final String middleName;
    private final String lastName;

    public Name(String firstName, String middleName, String lastName) {
        this.firstName = Objects.requireNonNull(firstName, "firstName");
        this.middleName = (middleName == null || middleName.isBlank()) ? "" : middleName.trim();
        this.lastName = Objects.requireNonNull(lastName, "lastName");
    }

    public Name(String firstName, String lastName) {
        this(firstName, "", lastName);
    }

    public String getFirstName() { return firstName; }
    public String getMiddleName() { return middleName; }
    public String getLastName() { return lastName; }

    public String getFullName() {
        if (middleName.isBlank()) {
            return firstName + " " + lastName;
        }
        return firstName + " " + middleName + " " + lastName;
    }

    @Override
    public String toString() {
        return getFullName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Name name = (Name) o;
        return firstName.equals(name.firstName)
                && middleName.equals(name.middleName)
                && lastName.equals(name.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, middleName, lastName);
    }
}
