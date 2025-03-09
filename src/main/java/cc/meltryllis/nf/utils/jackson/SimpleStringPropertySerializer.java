package cc.meltryllis.nf.utils.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;

/**
 * {@link SimpleStringProperty} 序列化器。
 *
 * @author Zachary W
 * @date 2025/3/6
 */
public class SimpleStringPropertySerializer extends StdSerializer<SimpleStringProperty> {

    public SimpleStringPropertySerializer() {
        super(SimpleStringProperty.class);
    }

    @Override
    public void serialize(SimpleStringProperty value, JsonGenerator gen,
                          SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeString(value.getValue());
        }
    }
}
