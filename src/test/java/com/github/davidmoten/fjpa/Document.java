package com.github.davidmoten.fjpa;

import static org.funcito.Funcito.callsTo;
import static org.funcito.FuncitoGuava.functionFor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.funcito.Funcito;
import org.funcito.FuncitoGuava;

import com.google.common.base.Function;

@Entity
public class Document {

	public Document(String id) {
		this.id = id;
	}

	@Id
	private String id;

	public String getId() {
		return id;
	}

	@Column
	public String status;

	private static Function<Document, String> toId = functionFor(callsTo(Document.class).getId());

	public static Function<Document, String> toId() {
		return toId;
	}

}
