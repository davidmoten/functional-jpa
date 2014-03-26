package com.github.davidmoten.fjpa;

import static org.funcito.FuncitoGuava.callsTo;
import static org.funcito.FuncitoGuava.functionFor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.common.base.Function;

@Entity
public class Document {

    @Id
    private String id;

    public Document(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Column
    public String status;

    public static Function<Document, String> toId = functionFor(callsTo(Document.class).getId());

}
