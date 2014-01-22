package com.github.davidmoten.fjpa;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Document {

	public Document(String id) {
		this.id = id;
	}

	public Document() {
	}

	@Id
	public String id;

}
