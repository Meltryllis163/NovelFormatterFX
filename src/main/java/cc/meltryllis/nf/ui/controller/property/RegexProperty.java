package cc.meltryllis.nf.ui.controller.property;

import cc.meltryllis.nf.entity.Regex;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.regex.Pattern;

/**
 * @author Zachary W
 * @date 2025/2/12
 */
public class RegexProperty {
    private final Regex regex;
    private final SimpleStringProperty description;
    private final SimpleObjectProperty<Pattern> pattern;
    private final SimpleBooleanProperty enabled;

    public RegexProperty(Regex regex) {
        this.regex = regex;
        this.description = new SimpleStringProperty(regex.getDescription());
        this.pattern = new SimpleObjectProperty<>(regex.getPattern());
        this.enabled = new SimpleBooleanProperty(false);
        this.enabled.addListener((observable, oldValue, newValue) -> RegexProperty.this.regex.setEnabled(newValue));
    }

    public boolean getEnabled() {
        return enabled.get();
    }

    public SimpleBooleanProperty enabledProperty() {
        return enabled;
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public String getPattern() {
        return pattern.get().toString();
    }

    public SimpleObjectProperty<Pattern> patternProperty() {
        return pattern;
    }


}
