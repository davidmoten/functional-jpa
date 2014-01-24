functional-jpa
==============

Functional style helpers for jpa and guava.


Query iterators
------------------

The class `com.github.davidmoten.fjpa.Iterators` has fluent style methods that perform lazy iteration of the result set of a query.

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
import com.github.davidmoten.fjpa.EntityManagers;

RichEntityManagerFactory emf = EntityManagers.emf("test");
RichEntityManager em = emf.createEntityManager();

// get a list of all ids in documents
List<String> list =
    em
       .createQuery("from Document order by id",String.class) 
	   .pageSize(2000)     //default page size is 100
	   .fluent()           //as FluentIterable
	   .transform(toId())  //get id (lazily)
	   .toList();          //force evaluation to list
```

Eliminating try-catch-final noise
---------------------------------------
You can also get the `RichEntityManagerFactory` to perform all of the usual try-catch-final closing of resources and logging of errors using the `RichEntityManagerFactory` run method:

```
List<String> list = EntityManagers
		.emf("test")
		.run(new Task<List<String>>() {
			@Override
			public List<String> run(RichEntityManager em) {
				return em
						.persist(new Document("a"))
						.persist(new Document("b"))
						.persist(new Document("c"))
						.createQuery("from Document order by id",
								Document.class).fluent()
						.transform(toId()).toList();
			}
		});
assertEquals(newArrayList("a", "b", "c"), list);
```  