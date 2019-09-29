package edu.refactor.demo.entity.currency.soup.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import edu.refactor.demo.entity.currency.soup.model.CurrencyType;

import java.io.IOException;

public class CurrencyTypeDeserializer extends JsonDeserializer<CurrencyType> {
    @Override
    public CurrencyType deserialize(JsonParser parser,
                                    DeserializationContext context) throws IOException, JsonProcessingException {
        return CurrencyType.valueOf(parser.getValueAsString());
    }
}
