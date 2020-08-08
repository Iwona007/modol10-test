package iwona.pl.modol10test.exception;

public class ColorNotFound extends RuntimeException {

    public ColorNotFound(String color) {
        super(String.format("Invalid enum type of enum %s", color));
    }
}
