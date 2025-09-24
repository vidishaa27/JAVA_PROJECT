package edu.ccrm.util;
import edu.ccrm.domain.Course;
import java.util.Comparator;

public class Comparators {
    public static Comparator<Course> byCode = Comparator.comparing(Course::getCode);
    public static Comparator<Course> byCreditsDesc = (a,b) -> Integer.compare(b.getCredits(), a.getCredits());
}
