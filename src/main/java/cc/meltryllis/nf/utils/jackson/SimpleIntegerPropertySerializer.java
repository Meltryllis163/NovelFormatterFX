package cc.meltryllis.nf.utils.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.IOException;

/**
 * {@link javafx.beans.property.SimpleIntegerProperty} 序列化器。
 *
 * @author Zachary W
 * @date 2025/3/7
 */
public class SimpleIntegerPropertySerializer extends StdSerializer<SimpleIntegerProperty> {

    public SimpleIntegerPropertySerializer() {
        super(SimpleIntegerProperty.class);
    }

    @Override
    public void serialize(SimpleIntegerProperty value, JsonGenerator gen,
                          SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeNumber(value.getValue());
        }
    }
}
