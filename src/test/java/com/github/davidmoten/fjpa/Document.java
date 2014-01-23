package com.github.davidmoten.fjpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.common.base.Function;

@Entity
public class Document {

	public Document(String id) {
		this.id = id;
	}

	@Id
	public String id;

	@Column
	public String status;

	private static Function<Document, String> toId = new Function<Document, String>() {
		@Override
		public String apply(Document input) {
			return input.id;
		}
	};

	public static Function<Document, String> toId() {
		return toId;
	}

}
