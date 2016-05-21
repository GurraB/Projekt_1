package se.mah.projekt_1;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * Created by Gustaf on 18/05/2016.
 */
public class ScheduleStampSerializer extends JsonDeserializer<ScheduleStamp> {
    @Override
    public ScheduleStamp deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);

        ScheduleStamp stamp = new ScheduleStamp();
        stamp.setFrom(node.get("from").asLong());
        stamp.setTo(node.get("to").asLong());
        return stamp;
    }
}
