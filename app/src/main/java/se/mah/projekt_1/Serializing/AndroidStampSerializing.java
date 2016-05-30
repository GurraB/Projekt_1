package se.mah.projekt_1.Serializing;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

import se.mah.projekt_1.Models.AndroidStamp;

/**
 * Created by Gustaf Bohlin on 18/05/2016.
 * A class to easily create an AndroidStamp from a map
 */
public class AndroidStampSerializing extends JsonDeserializer<AndroidStamp> {

    /**
     * Create the AndroidStamp from the map
     * @param p JSONParser
     * @param ctxt DeserializationContext
     * @return created AndroidStamp
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Override
    public AndroidStamp deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);

        AndroidStamp stamp = new AndroidStamp();
        stamp.setTime(node.get("date").asLong());
        stamp.setCheckIn(node.get("checkIn").asBoolean());
        return stamp;
    }
}
