package blog.thoughts.on.java.forge.rest.dto;

import java.io.Serializable;
import blog.thoughts.on.java.forge.model.Book;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;

public class NestedBookDTO implements Serializable
{

   private Long id;
   private String title;
   private int pages;
   private Date publicationDate;
   private int version;

   public NestedBookDTO()
   {
   }

   public NestedBookDTO(final Book entity)
   {
      if (entity != null)
      {
         this.id = entity.getId();
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
      if (this.id != null)
      {
         TypedQuery<Book> findByIdQuery = em.createQuery(
               "SELECT DISTINCT b FROM Book b WHERE b.id = :entityId",
               Book.class);
         findByIdQuery.setParameter("entityId", this.id);
         entity = findByIdQuery.getSingleResult();
         return entity;
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