package edu.refactor.demo.entity.currency.soup.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import edu.refactor.demo.entity.currency.soup.deserializer.BigDecimalDeserializer;
import edu.refactor.demo.entity.currency.soup.deserializer.CurrencyTypeDeserializer;
import edu.refactor.demo.entity.currency.soup.deserializer.IntegerDeserializer;

import java.io.Serializable;
import java.math.BigDecimal;

public class Valute implements Serializable {
    private static final long serialVersionUID = 8320114983734704749L;

    @JacksonXmlProperty(isAttribute = true, localName = "ID")
    private String id;

    @JacksonXmlProperty(localName = "NumCode")
    private String numCode;

    @JacksonXmlProperty(localName = "CharCode")
    @JsonDeserialize(using = CurrencyTypeDeserializer.class)
    private CurrencyType currencyType;

    @JacksonXmlProperty(localName = "Nominal")
    @JsonDeserialize(using = IntegerDeserializer.class)
    private Integer nominal;

    @JacksonXmlProperty(localName = "Name")
    private String name;

    @JacksonXmlProperty(localName = "Value")
    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal value;

    public String getId() {
        return id;
    }

    public String getNumCode() {
        return numCode;
    }

    public CurrencyType getCurrencyType() {
        return currencyType;
    }

    public Integer getNominal() {
        return nominal;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getValue() {
        return value;
    }
}
