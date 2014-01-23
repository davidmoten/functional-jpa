functional-jpa
==============

Functional style helpers for jpa and guava

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
EntityManager em = emf.createEntityManager();
em.getTransaction().begin();
em.persist(new Document("a"));
em.persist(new Document("b"));
em.persist(new Document("c"));
em.getTransaction().commit();
Query q = em.createQuery("from Document order by id");

// get a list of all ids in documents
List<String> list = 
	Iterators.query(q,Document.class)
		.pageSize(2)        //default page size is 100
	    .fluent()           //as FluentIterable
		.transform(toId())  //get id (lazily)
		.toList();          //force evaluation to list
```
