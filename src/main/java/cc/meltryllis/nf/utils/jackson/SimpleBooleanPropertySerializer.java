package cc.meltryllis.nf.utils.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;

/**
 * {@link SimpleBooleanProperty} 序列化器。
 *
 * @author Zachary W
 * @date 2025/3/7
 */
public class SimpleBooleanPropertySerializer extends StdSerializer<SimpleBooleanProperty> {

    public SimpleBooleanPropertySerializer() {
        super(SimpleBooleanProperty.class);
    }

    @Override
    public void serialize(SimpleBooleanProperty value, JsonGenerator gen,
                          SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeBoolean(value.getValue());
        }
    }
}
