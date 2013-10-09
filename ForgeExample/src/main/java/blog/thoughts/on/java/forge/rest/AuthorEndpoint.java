package blog.thoughts.on.java.forge.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import blog.thoughts.on.java.forge.model.Author;
import blog.thoughts.on.java.forge.rest.dto.AuthorDTO;

/**
 * 
 */
@Stateless
@Path("/authors")
public class AuthorEndpoint
{
   @PersistenceContext(unitName = "forge-default")
   private EntityManager em;

   @POST
   @Consumes("application/xml")
   public Response create(AuthorDTO dto)
   {
      Author entity = dto.fromDTO(null, em);
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(AuthorEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") Long id)
   {
      Author entity = em.find(Author.class, id);
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      em.remove(entity);
      return Response.noContent().build();
   }

   @GET
   @Path("/{id:[0-9][0-9]*}")
   @Produces("application/xml")
   public Response findById(@PathParam("id") Long id)
   {
      TypedQuery<Author> findByIdQuery = em.createQuery("SELECT DISTINCT a FROM Author a LEFT JOIN FETCH a.books WHERE a.id = :entityId ORDER BY a.id", Author.class);
      findByIdQuery.setParameter("entityId", id);
      Author entity = findByIdQuery.getSingleResult();
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      AuthorDTO dto = new AuthorDTO(entity);
      return Response.ok(dto).build();
   }

   @GET
   @Produces("application/xml")
   public List<AuthorDTO> listAll()
   {
      final List<Author> searchResults = em.createQuery("SELECT DISTINCT a FROM Author a LEFT JOIN FETCH a.books ORDER BY a.id", Author.class).getResultList();
      final List<AuthorDTO> results = new ArrayList<AuthorDTO>();
      for (Author searchResult : searchResults)
      {
         AuthorDTO dto = new AuthorDTO(searchResult);
         results.add(dto);
      }
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes("application/xml")
   public Response update(@PathParam("id") Long id, AuthorDTO dto)
   {
      TypedQuery<Author> findByIdQuery = em.createQuery("SELECT DISTINCT a FROM Author a LEFT JOIN FETCH a.books WHERE a.id = :entityId ORDER BY a.id", Author.class);
      findByIdQuery.setParameter("entityId", id);
      Author entity = findByIdQuery.getSingleResult();
      entity = dto.fromDTO(entity, em);
      entity = em.merge(entity);
      return Response.noContent().build();
   }
}