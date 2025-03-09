package cc.meltryllis.nf.utils.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * {@link SimpleBooleanProperty} 反序列化器。
 *
 * @author Zachary W
 * @date 2025/3/7
 */
@Slf4j
public class SimpleBooleanPropertyDeserializer extends StdDeserializer<SimpleBooleanProperty> {

    public SimpleBooleanPropertyDeserializer() {
        super(SimpleBooleanProperty.class);
    }

    @Override
    public SimpleBooleanProperty deserialize(JsonParser p,
                                             DeserializationContext ctxt) {
        JsonNode node;
        try {
            node = p.getCodec().readTree(p);
        } catch (IOException e) {
            log.warn("Deserialize SimpleBooleanProperty failed.", e);
            return new SimpleBooleanProperty();
        }
        if (node.isNull()) {
            return new SimpleBooleanProperty();
        } else {
            return new SimpleBooleanProperty(node.asBoolean());
        }
    }
}
