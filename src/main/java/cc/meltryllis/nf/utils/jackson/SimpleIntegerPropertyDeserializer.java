package cc.meltryllis.nf.utils.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * {@link javafx.beans.property.SimpleIntegerProperty} 反序列化器。
 *
 * @author Zachary W
 * @date 2025/3/7
 */
@Slf4j
public class SimpleIntegerPropertyDeserializer extends StdDeserializer<SimpleIntegerProperty> {

    public SimpleIntegerPropertyDeserializer() {
        super(SimpleIntegerProperty.class);
    }

    @Override
    public SimpleIntegerProperty deserialize(JsonParser p,
                                             DeserializationContext ctxt) {
        JsonNode node;
        try {
            node = p.getCodec().readTree(p);
        } catch (IOException e) {
            log.warn("Deserialize SimpleIntegerProperty failed.", e);
            return new SimpleIntegerProperty();
        }
        if (node.isNull()) {
            return new SimpleIntegerProperty();
        } else {
            return new SimpleIntegerProperty(node.asInt());
        }
    }
}
