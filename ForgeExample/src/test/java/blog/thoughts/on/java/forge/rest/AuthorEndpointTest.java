package blog.thoughts.on.java.forge.rest;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import blog.thoughts.on.java.forge.model.Author;
import blog.thoughts.on.java.forge.model.Book;

@RunWith(Arquillian.class)
public class AuthorEndpointTest
{
   @Inject
   private AuthorEndpoint authorendpoint;

   @Deployment
   public static JavaArchive createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class, "test.jar")
            .addClasses(AuthorEndpoint.class, Author.class, Book.class)
            .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
   }

   @Test
   public void testIsDeployed()
   {
      Assert.assertNotNull(authorendpoint);
   }
   
   @Test
   public void createAuthor() {
	   Author author = new Author();
	   author.setFirstName("J. R. R.");
	   author.setLastName("Tolkien");
	   Set<Book> books = new HashSet<Book>();
	   Book book = new Book();
	   book.setAuthor(author);
	   book.setTitle("Lord of the rings");
	   books.add(book);
	   author.setBooks(books);
	   authorendpoint.create(author);
	   Assert.assertEquals("Author",(Long) 1L, author.getId());
	   Assert.assertEquals("Books", 1, author.getBooks().size());
	   Assert.assertEquals("Book",(Long) 2L, author.getBooks().toArray(new Book[1])[0].getId());
   }
}
