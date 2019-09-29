package edu.refactor.demo.entity.currency.soup.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_HALF_UP;

public class BigDecimalDeserializer extends JsonDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonParser parser,
                                  DeserializationContext context) throws IOException, JsonProcessingException {

        String stringVal = parser.getValueAsString();

        if(stringVal.isEmpty()){
            return null;
        }

        String decimalStrVal = stringVal.replace(",", ".");

        return new BigDecimal(decimalStrVal).setScale(4, ROUND_HALF_UP);
    }
}
