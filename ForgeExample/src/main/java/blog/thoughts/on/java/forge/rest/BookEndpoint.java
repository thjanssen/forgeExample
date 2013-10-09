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
import blog.thoughts.on.java.forge.model.Book;
import blog.thoughts.on.java.forge.rest.dto.BookDTO;

/**
 * 
 */
@Stateless
@Path("/books")
public class BookEndpoint
{
   @PersistenceContext(unitName = "forge-default")
   private EntityManager em;

   @POST
   @Consumes("application/xml")
   public Response create(BookDTO dto)
   {
      Book entity = dto.fromDTO(null, em);
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(BookEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") Long id)
   {
      Book entity = em.find(Book.class, id);
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
      TypedQuery<Book> findByIdQuery = em.createQuery("SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.author WHERE b.id = :entityId ORDER BY b.id", Book.class);
      findByIdQuery.setParameter("entityId", id);
      Book entity = findByIdQuery.getSingleResult();
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      BookDTO dto = new BookDTO(entity);
      return Response.ok(dto).build();
   }

   @GET
   @Produces("application/xml")
   public List<BookDTO> listAll()
   {
      final List<Book> searchResults = em.createQuery("SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.author ORDER BY b.id", Book.class).getResultList();
      final List<BookDTO> results = new ArrayList<BookDTO>();
      for (Book searchResult : searchResults)
      {
         BookDTO dto = new BookDTO(searchResult);
         results.add(dto);
      }
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes("application/xml")
   public Response update(@PathParam("id") Long id, BookDTO dto)
   {
      TypedQuery<Book> findByIdQuery = em.createQuery("SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.author WHERE b.id = :entityId ORDER BY b.id", Book.class);
      findByIdQuery.setParameter("entityId", id);
      Book entity = findByIdQuery.getSingleResult();
      entity = dto.fromDTO(entity, em);
      entity = em.merge(entity);
      return Response.noContent().build();
   }
}