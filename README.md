functional-jpa
==============

Functional style helpers for jpa and guava


Query iterators
------------------
The class com.github.davidmoten.fjpa.Iterators has fluent style methods that perform lazy iteration of the result set of a query.

Given this jpa class:

```
@Entity
public class Document {

	public Document(String id) {
		this.id = id;
	}

	@Id
	public String id;

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
```
You can do stuff like this:

```
import com.github.davidmoten.fjpa.Iterators;

Query q = em.createQuery("from Document order by id");

// get a list of all ids in documents
List<String> list = 
	Iterators.query(q,Document.class)
		.pageSize(2)        //default page size is 100
	    .fluent()           //as FluentIterable
		.transform(toId())  //get id (lazily)
		.toList();          //force evaluation to list
```

And almost the same for a TypedQuery:

```
import com.github.davidmoten.fjpa.Iterators;

TypedQuery<Document> q = em.createQuery("from Document order by id",Document.class);

// get a list of all ids in documents
List<String> list = 
	Iterators.query(q)
		.pageSize(2)        //default page size is 100
	    .fluent()           //as FluentIterable
		.transform(toId())  //get id (lazily)
		.toList();          //force evaluation to list
```