package blog.thoughts.on.java.forge.rest.dto;

import java.io.Serializable;
import blog.thoughts.on.java.forge.model.Book;
import javax.persistence.EntityManager;
import blog.thoughts.on.java.forge.rest.dto.NestedAuthorDTO;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BookDTO implements Serializable
{

   private Long id;
   private NestedAuthorDTO author;
   private String title;
   private int pages;
   private Date publicationDate;
   private int version;

   public BookDTO()
   {
   }

   public BookDTO(final Book entity)
   {
      if (entity != null)
      {
         this.id = entity.getId();
         this.author = new NestedAuthorDTO(entity.getAuthor());
         this.title = entity.getTitle();
         this.pages = entity.getPages();
         this.publicationDate = entity.getPublicationDate();
         this.version = entity.getVersion();
      }
   }

   public Book fromDTO(Book entity, EntityManager em)
   {
      if (entity == null)
      {
         entity = new Book();
      }
      if (this.author != null)
      {
         entity.setAuthor(this.author.fromDTO(entity.getAuthor(), em));
      }
      entity.setTitle(this.title);
      entity.setPages(this.pages);
      entity.setPublicationDate(this.publicationDate);
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

   public NestedAuthorDTO getAuthor()
   {
      return this.author;
   }

   public void setAuthor(final NestedAuthorDTO author)
   {
      this.author = author;
   }

   public String getTitle()
   {
      return this.title;
   }

   public void setTitle(final String title)
   {
      this.title = title;
   }

   public int getPages()
   {
      return this.pages;
   }

   public void setPages(final int pages)
   {
      this.pages = pages;
   }

   public Date getPublicationDate()
   {
      return this.publicationDate;
   }

   public void setPublicationDate(final Date publicationDate)
   {
      this.publicationDate = publicationDate;
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