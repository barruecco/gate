package com.example.gate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="GATE_TABLE")
public class Gate {

    @Id @GeneratedValue
    Long id;

    String fromA;
    String toB;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromA() {
        return fromA;
    }

    public void setFromA(String from) {
        this.fromA = from;
    }

    public String getToB() {
        return toB;
    }

    public void setToB(String to) {
        this.toB = to;
    }
}
