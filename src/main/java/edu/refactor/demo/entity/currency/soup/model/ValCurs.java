package edu.refactor.demo.entity.currency.soup.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JacksonXmlRootElement(localName = "ValCurs")
public class ValCurs implements Serializable {
    private static final long serialVersionUID = -939343614364744585L;

    @JacksonXmlProperty(isAttribute = true, localName = "Date")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date date;

    @JacksonXmlProperty(isAttribute = true, localName = "name")
    private String name;

    @JacksonXmlProperty(localName = "Valute")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Valute> valutes = new ArrayList<>();

    public Date getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public List<Valute> getValutes() {
        return valutes;
    }
}
