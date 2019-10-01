package edu.refactor.demo.entity.currency;

import edu.refactor.demo.entity.currency.soup.model.CurrencyType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class Currency implements Serializable {
    private static final long serialVersionUID = -2942788777183647656L;

    @Id
    @Column
    private String id;

    @Column
    @NotNull
    private CurrencyType currencyType;

    @Column
    private String fullName;

    @Column
    @NotNull
    private Integer nominal;

    @Column
    @NotNull
    private BigDecimal value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CurrencyType getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(CurrencyType currencyType) {
        this.currencyType = currencyType;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getNominal() {
        return nominal;
    }

    public void setNominal(Integer nominal) {
        this.nominal = nominal;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
