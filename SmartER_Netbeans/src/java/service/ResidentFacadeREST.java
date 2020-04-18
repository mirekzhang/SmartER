/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import SmartER.Resident;
import java.sql.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Mirek
 */
@Stateless
@Path("smarter.resident")
public class ResidentFacadeREST extends AbstractFacade<Resident> {

    @PersistenceContext(unitName = "SmartERPU")
    private EntityManager em;

    public ResidentFacadeREST() {
        super(Resident.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Resident entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Resident entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Resident find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Resident> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Resident> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @GET
    @Path("findByFname/{fname}")
    @Produces({"application/json"})
    public List<Resident> findByFname(@PathParam("fname") String fname) {
        Query query = em.createNamedQuery("Resident.findByFname");
        query.setParameter("fname", fname);
        return query.getResultList();
    }
    
    @GET
    @Path("findByLname/{lname}")
    @Produces({"application/json"})
    public List<Resident> findByLname(@PathParam("lname") String lname) {
        Query query = em.createNamedQuery("Resident.findByLname");
        query.setParameter("lname", lname);
        return query.getResultList();
    }
    
    @GET
    @Path("findByDoB/{dob}")
    @Produces({"application/json"})
    public List<Resident> findByDoB2(@PathParam("dob") Date dob) {
        Query query = em.createNamedQuery("Resident.findByDob");
        query.setParameter("dob", dob);
        return query.getResultList();
    }
    
    @GET
    @Path("findByAddress/{address}")
    @Produces({"application/json"})
    public List<Resident> findByAddress2(@PathParam("address") String address) {
        Query query = em.createNamedQuery("Resident.findByAddress");
        query.setParameter("address", address);
        return query.getResultList();
    }
    
    @GET
    @Path("findByPostcode/{postcode}")
    @Produces({"application/json"})
    public List<Resident> findByPostcode(@PathParam("postcode") String postcode) {
        Query query = em.createNamedQuery("Resident.findByPostcode");
        query.setParameter("postcode", postcode);
        return query.getResultList();
    }
    
    @GET
    @Path("findByEmail/{email}")
    @Produces({"application/json"})
    public List<Resident> findByEmail(@PathParam("email") String email) {
        Query query = em.createNamedQuery("Resident.findByEmail");
        query.setParameter("email", email);
        return query.getResultList();
    }
    
    @GET
    @Path("findByMobile/{mobile}")
    @Produces({"application/json"})
    public List<Resident> findByMobile(@PathParam("mobile") String mobile) {
        Query query = em.createNamedQuery("Resident.findByMobile");
        query.setParameter("mobile", mobile);
        return query.getResultList();
    }
    
    @GET
    @Path("findByNoofresidents/{noofresidents}")
    @Produces({"application/json"})
    public List<Resident> findByNoofresidents(@PathParam("noofresidents") Integer noofresidents) {
        Query query = em.createNamedQuery("Resident.findByNoofresidents");
        query.setParameter("noofresidents", noofresidents);
        return query.getResultList();
    }
    
    @GET
    @Path("findByProvider/{provider}")
    @Produces({"application/json"})
    public List<Resident> findByProvider(@PathParam("provider") String provider) {
        Query query = em.createNamedQuery("Resident.findByProvider");
        query.setParameter("provider", provider);
        return query.getResultList();
    }
    
    //3.2
    @GET
    @Path("findByFnameANDLname/{fname}/{lname}")
    @Produces({"application/json"})
    public List<Resident> findByFnameANDLname(@PathParam("fname") String fname, @PathParam("lname") String lname) {
        TypedQuery<Resident> q = em.createQuery("SELECT r FROM Resident r WHERE r.fname = :fname AND r.lname = :lname", Resident.class);
        q.setParameter("fname", fname);
        q.setParameter("lname", lname);
        return q.getResultList();
    }
    
}
