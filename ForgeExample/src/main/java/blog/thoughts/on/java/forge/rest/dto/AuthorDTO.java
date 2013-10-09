package blog.thoughts.on.java.forge.rest.dto;

import java.io.Serializable;
import blog.thoughts.on.java.forge.model.Author;
import javax.persistence.EntityManager;
import java.util.Set;
import java.util.HashSet;
import blog.thoughts.on.java.forge.rest.dto.NestedBookDTO;
import blog.thoughts.on.java.forge.model.Book;
import java.util.Iterator;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AuthorDTO implements Serializable
{

   private Long id;
   private Set<NestedBookDTO> books = new HashSet<NestedBookDTO>();
   private String lastName;
   private String firstName;
   private int version;

   public AuthorDTO()
   {
   }

   public AuthorDTO(final Author entity)
   {
      if (entity != null)
      {
         this.id = entity.getId();
         Iterator<Book> iterBooks = entity.getBooks().iterator();
         for (; iterBooks.hasNext();)
         {
            Book element = iterBooks.next();
            this.books.add(new NestedBookDTO(element));
         }
         this.lastName = entity.getLastName();
         this.firstName = entity.getFirstName();
         this.version = entity.getVersion();
      }
   }

   public Author fromDTO(Author entity, EntityManager em)
   {
      if (entity == null)
      {
         entity = new Author();
      }
      Iterator<Book> iterBooks = entity.getBooks().iterator();
      for (; iterBooks.hasNext();)
      {
         boolean found = false;
         Book book = iterBooks.next();
         Iterator<NestedBookDTO> iterDtoBooks = this.getBooks().iterator();
         for (; iterDtoBooks.hasNext();)
         {
            NestedBookDTO dtoBook = iterDtoBooks.next();
            if (dtoBook.getId().equals(book.getId()))
            {
               found = true;
               break;
            }
         }
         if (found == false)
         {
            iterBooks.remove();
         }
      }
      Iterator<NestedBookDTO> iterDtoBooks = this.getBooks().iterator();
      for (; iterDtoBooks.hasNext();)
      {
         boolean found = false;
         NestedBookDTO dtoBook = iterDtoBooks.next();
         iterBooks = entity.getBooks().iterator();
         for (; iterBooks.hasNext();)
         {
            Book book = iterBooks.next();
            if (dtoBook.getId().equals(book.getId()))
            {
               found = true;
               break;
            }
         }
         if (found == false)
         {
            Iterator<Book> resultIter = em
                  .createQuery("SELECT DISTINCT b FROM Book b",
                        Book.class).getResultList().iterator();
            for (; resultIter.hasNext();)
            {
               Book result = resultIter.next();
               if (result.getId().equals(dtoBook.getId()))
               {
                  entity.getBooks().add(result);
                  break;
               }
            }
         }
      }
      entity.setLastName(this.lastName);
      entity.setFirstName(this.firstName);
      entity.setVersion(this.version);
      entity = em.merge(entity);
      return entity;
   }

   public Long getId()
   {
      return this.id;
   }

   public void setId(final Long id)
   {
      this.id = id;
   }

   public Set<NestedBookDTO> getBooks()
   {
      return this.books;
   }

   public void setBooks(final Set<NestedBookDTO> books)
   {
      this.books = books;
   }

   public String getLastName()
   {
      return this.lastName;
   }

   public void setLastName(final String lastName)
   {
      this.lastName = lastName;
   }

   public String getFirstName()
   {
      return this.firstName;
   }

   public void setFirstName(final String firstName)
   {
      this.firstName = firstName;
   }

   public int getVersion()
   {
      return this.version;
   }

   public void setVersion(final int version)
   {
      this.version = version;
   }
}