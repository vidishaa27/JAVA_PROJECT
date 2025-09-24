package edu.ccrm.domain;

/**
 * Grade enum. Letter with grade points. Adjust points as per your scheme.
 */
public enum Grade {
    S("S", 10),
    A("A", 9),
    B("B", 8),
    C("C", 7),
    D("D", 6),
    E("E", 5),
    F("F", 0),
    NA("N/A", 0);

    private final String letter;
    private final double points;

    Grade(String letter, double points) {
        this.letter = letter;
        this.points = points;
    }

    public String getLetter() { return letter; }
    public double getPoints() { return points; }

    @Override
    public String toString() { return letter; }
}