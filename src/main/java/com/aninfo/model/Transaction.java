package com.aninfo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Transaction {
    private Double value;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long transactionId;

    private Long cbu;

    public Transaction(){}

    public void setId(Long id){
        this.transactionId = id;
    }

    public Long getId(){
        return transactionId;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double ammount){
        this.value = ammount;
    }

    public Long getCbu(){
        return cbu;
    }
}
