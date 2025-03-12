package cc.meltryllis.nf.utils.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import javafx.beans.property.SimpleStringProperty;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * {@link javafx.beans.property.SimpleStringProperty} 反序列化器。
 *
 * @author Zachary W
 * @date 2025/3/6
 */
@Slf4j
public class SimpleStringPropertyDeserializer extends StdDeserializer<SimpleStringProperty> {

    protected SimpleStringPropertyDeserializer() {
        super(SimpleStringProperty.class);
    }

    @Override
    public SimpleStringProperty deserialize(JsonParser p,
                                            DeserializationContext ctxt) {
        JsonNode jsonNode;
        try {
            jsonNode = p.getCodec().readTree(p);
        } catch (IOException e) {
            log.warn("Deserialize SimpleStringProperty failed.", e);
            return new SimpleStringProperty();
        }
        if (jsonNode.isNull()) {
            return new SimpleStringProperty();
        } else {
            return new SimpleStringProperty(jsonNode.asText());
        }
    }
}
