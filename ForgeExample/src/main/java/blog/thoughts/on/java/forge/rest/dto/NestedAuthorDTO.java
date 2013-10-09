package blog.thoughts.on.java.forge.rest.dto;

import java.io.Serializable;
import blog.thoughts.on.java.forge.model.Author;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class NestedAuthorDTO implements Serializable
{

   private Long id;
   private String lastName;
   private String firstName;
   private int version;

   public NestedAuthorDTO()
   {
   }

   public NestedAuthorDTO(final Author entity)
   {
      if (entity != null)
      {
         this.id = entity.getId();
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
      if (this.id != null)
      {
         TypedQuery<Author> findByIdQuery = em.createQuery(
               "SELECT DISTINCT a FROM Author a WHERE a.id = :entityId",
               Author.class);
         findByIdQuery.setParameter("entityId", this.id);
         entity = findByIdQuery.getSingleResult();
         return entity;
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