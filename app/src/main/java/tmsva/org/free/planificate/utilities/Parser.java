package tmsva.org.free.planificate.utilities;

public class Parser {

    public static Integer tryIntParse(Object o) {
        Integer parsed = null;
        if(o instanceof String) {
            try {
                parsed = Integer.parseInt((String) o);
            } catch (NumberFormatException e) { }
        }
        return parsed;
    }
}
