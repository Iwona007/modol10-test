package iwona.pl.modol10test.converColor;

import iwona.pl.modol10test.exception.ColorNotFound;
import iwona.pl.modol10test.model.Color;
import java.util.EnumSet;
import org.springframework.stereotype.Component;

@Component
public class ConvertColor {

    public Color convertToEnum(String color) {
        return EnumSet.allOf(Color.class).stream()
                .filter(color1 -> color1.name().equalsIgnoreCase(color))
                .findAny()
                .orElseThrow(() -> new ColorNotFound(color));
    }
}
