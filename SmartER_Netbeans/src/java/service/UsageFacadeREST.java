/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import SmartER.Usage;
import java.sql.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
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
@Path("smarter.usage")
public class UsageFacadeREST extends AbstractFacade<Usage> {

    @PersistenceContext(unitName = "SmartERPU")
    private EntityManager em;

    public UsageFacadeREST() {
        super(Usage.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Usage entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Usage entity) {
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
    public Usage find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Usage> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Usage> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    @Path("findByResid/{resid}")
    @Produces({"application/json"})
    public List<Usage> findByResid(@PathParam("resid") Integer resid) {
        Query query = em.createNamedQuery("Usage.findByResid");
        query.setParameter("resid", resid);
        return query.getResultList();
    }
    
    @GET
    @Path("findByDate/{date}")
    @Produces({"application/json"})
    public List<Usage> findByDate(@PathParam("date") Date date) {
        Query query = em.createNamedQuery("Usage.findByDate");
        query.setParameter("date", date);
        return query.getResultList();
    }
    
    @GET
    @Path("findByHours/{hours}")
    @Produces({"application/json"})
    public List<Usage> findByHours(@PathParam("hours") Integer hours) {
        Query query = em.createNamedQuery("Usage.findByHours");
        query.setParameter("hours", hours);
        return query.getResultList();
    }
    
    @GET
    @Path("findByFridgeusage/{fridgeusage}")
    @Produces({"application/json"})
    public List<Usage> findByFridgeUsage(@PathParam("fridgeusage") Double fridgeusage) {
        Query query = em.createNamedQuery("Usage.findByFridgeusage");
        query.setParameter("fridgeusage", fridgeusage);
        return query.getResultList();
    }
    
    @GET
    @Path("findByAcusage/{acusage}")
    @Produces({"application/json"})
    public List<Usage> findByAcusage(@PathParam("acusage") Double acusage) {
        Query query = em.createNamedQuery("Usage.findByAcusage");
        query.setParameter("acusage", acusage);
        return query.getResultList();
    }
    
    @GET
    @Path("findByWmusage/{wmusage}")
    @Produces({"application/json"})
    public List<Usage> findByWmusage(@PathParam("wmusage") Double wmusage) {
        Query query = em.createNamedQuery("Usage.findByWmusage");
        query.setParameter("wmusage", wmusage);
        return query.getResultList();
    }
    
    @GET
    @Path("findByTemperature/{temperature}")
    @Produces({"application/json"})
    public List<Usage> findByTemperature(@PathParam("temperature") Double temperature) {
        Query query = em.createNamedQuery("Usage.findByTemperature");
        query.setParameter("temperature", temperature);
        return query.getResultList();
    }
    
    //3.3
    @GET
    @Path("findByFnameANDHours/{fname}/{hours}")
    @Produces({"application/json"})
    public List<Usage> findByFnameANDHours(@PathParam("fname") String fname, @PathParam("hours") Integer hours) {
        TypedQuery<Usage> q = em.createQuery("SELECT u FROM Usage u WHERE u.resid.fname = :fname AND u.hours = :hours", Usage.class);
        q.setParameter("fname", fname);
        q.setParameter("hours", hours);
        return q.getResultList();
    }
    
    //3.4
    @GET
    @Path("findByMobileANDDate/{mobile}/{date}")
    @Produces({"application/json"})
    public List<Usage> findByMobileANDDate(@PathParam("mobile") String mobile, @PathParam("date") Date date) {
        Query query = em.createNamedQuery("Usage.findByMobileANDDate");
        query.setParameter("mobile", mobile);
        query.setParameter("date", date);
        return query.getResultList();
    }
    
    //4.1
    @GET
    @Path("getSpecifiedUsage/{resid}/{appliance}/{date}/{hours}")
    @Produces({"application/json"})
    public String getSpecifiedUsage(@PathParam("resid") Integer resid, @PathParam("appliance") String appliance, @PathParam("date") Date date, @PathParam("hours") Integer hours) {
        if(appliance.equalsIgnoreCase("fridge")){
            return "The hourly power usage of the fridge is " + em.createQuery("SELECT u.fridgeusage FROM Usage u WHERE u.resid.resid = :resid AND u.date = :date AND u.hours = :hours", Usage.class)
                    .setParameter("resid", resid)
                    .setParameter("date", date)
                    .setParameter("hours", hours).getSingleResult() + " kWh.";
        }else if (appliance.equalsIgnoreCase("ac")){
            return "The hourly power usage of the air conditioner is " + em.createQuery("SELECT u.acusage FROM Usage u WHERE u.resid.resid = :resid AND u.date = :date AND u.hours = :hours", Usage.class)
                    .setParameter("resid", resid)
                    .setParameter("date", date)
                    .setParameter("hours", hours).getSingleResult() + " kWh.";
        }else if (appliance.equalsIgnoreCase("wm")){
        return "The hourly power usage of washing machine is " + em.createQuery("SELECT u.wmusage FROM Usage u WHERE u.resid.resid = :resid AND u.date = :date AND u.hours = :hours", Usage.class)
                    .setParameter("resid", resid)
                    .setParameter("date", date)
                    .setParameter("hours", hours).getSingleResult() + " kWh.";
        }
        return "Please enter the correct appliance name (fridge/AC/WM).";
    }

    
    //4.2
    @GET
    @Path("getTotalusageForOne/{resid}/{date}/{hours}")
    @Produces({"application/json"})
    public String getTotalusageForOne(@PathParam("resid") Integer resid, @PathParam("date") Date date, @PathParam("hours") Integer hours) {
        return "The total hourly power usage is " + em.createQuery("SELECT u.fridgeusage + u.acusage + u.wmusage FROM Usage u WHERE u.resid.resid = :resid AND u.date = :date AND u.hours = :hours", Usage.class)
                    .setParameter("resid", resid)
                    .setParameter("date", date)
                    .setParameter("hours", hours).getSingleResult() + " kWh.";
    }
    
    //4.3
    @GET
    @Path("getTotalusageForAll/{date}/{hours}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Object getTotalusageForAll(@PathParam("date") Date date, @PathParam("hours") Integer hours) {
        List<Object[]> queryList = em.createQuery("SELECT u.resid.resid, u.resid.address, u.resid.postcode, u.fridgeusage + u.acusage + u.wmusage AS totalusage FROM Usage u WHERE u.date = :date AND u.hours = :hours", Object[].class)
                    .setParameter("date", date)
                    .setParameter("hours", hours)
                    .getResultList();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) {
            JsonObject personObject = Json.createObjectBuilder().
            add("resid", (Integer)row[0])
            .add("address", (String)row[1])
            .add("postcode",(String)row[2])
            .add("totalusage",(Double)row[3]).build();
        arrayBuilder.add(personObject);
        }
        JsonArray jArray = arrayBuilder.build();
        return jArray;
    }
    
    //4.4
    @GET
    @Path("getHighestUsage/{resid}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Object getHighestUsage(@PathParam("resid") Integer resid) {
        List<Object[]> queryList = em.createQuery("SELECT u.date, u.hours, u.fridgeusage + u.acusage + u.wmusage AS totalusage FROM Usage u WHERE u.resid.resid = :resid AND totalusage = (SELECT max(u.fridgeusage + u.acusage + u.wmusage) FROM Usage u WHERE u.resid.resid = :resid)", Object[].class)
                .setParameter("resid", resid)
                .getResultList();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) {
            JsonObject personObject = Json.createObjectBuilder().
            add("date", (String)row[0].toString())
            .add("hours", (Short)row[1])
            .add("totalusage",(Double)row[2]).build();
        arrayBuilder.add(personObject);
        }
        JsonArray jArray = arrayBuilder.build();
        return jArray;
    }
    
    //5.1
    @GET
    @Path("getDailyusage/{resid}/{date}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Object getDailyusage(@PathParam("resid") Integer resid, @PathParam("date") Date date) {
        List<Object[]> queryList = em.createQuery("SELECT u.resid.resid, sum(u.fridgeusage) AS fridge, sum(u.acusage) AS aircon, sum(u.wmusage) AS washingmachine FROM Usage u WHERE u.resid.resid = :resid AND u.date = :date GROUP BY u.resid.resid", Object[].class)
                    .setParameter("resid", resid)
                    .setParameter("date", date)
                    .getResultList();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) {
            JsonObject personObject = Json.createObjectBuilder().
            add("resid", (Integer)row[0])
            .add("fridge", (Double)row[1])
            .add("aircon",(Double)row[2])
            .add("washingmachine",(Double)row[3]).build();
        arrayBuilder.add(personObject);
        }
        JsonArray jArray = arrayBuilder.build();
        return jArray;
    }
    
    //5.2
    @GET
    @Path("getHourlyOrDailyUsage/{resid}/{date}/{option}")
    @Produces({"application/json"})
    public Object getHourlyOrDailyUsage(@PathParam("resid") Integer resid, @PathParam("date") Date date, @PathParam("option") String option) {
        if(option.equalsIgnoreCase("hourly")){
            List<Object[]> queryList = em.createQuery("SELECT u.resid.resid, u.fridgeusage + u.acusage + u.wmusage AS totalusage, u.temperature, u.date, u.hours FROM Usage u WHERE u.resid.resid = :resid AND u.date = :date", Object[].class)
                    .setParameter("resid", resid)
                    .setParameter("date", date)
                    .getResultList();
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (Object[] row : queryList) {
                JsonObject personObject = Json.createObjectBuilder().
                add("resid", (Integer)row[0])
                .add("totalusage", (Double)row[1])
                .add("temperature",(Double)row[2])
                .add("date", (String)row[3].toString())
                .add("hours", (Short)row[4]).build();
            arrayBuilder.add(personObject);
            }
            JsonArray jArray = arrayBuilder.build();
            return jArray;
        }else if (option.equalsIgnoreCase("daily")){
            List<Object[]> queryList = em.createQuery("SELECT u.resid.resid, sum(u.fridgeusage + u.acusage + u.wmusage) AS totalusage, avg(u.temperature) FROM Usage u WHERE u.resid.resid = :resid AND u.date = :date GROUP BY u.resid.resid", Object[].class)
                    .setParameter("resid", resid)
                    .setParameter("date", date)
                    .getResultList();
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (Object[] row : queryList) {
                JsonObject personObject = Json.createObjectBuilder().
                add("resid", (Integer)row[0])
                .add("totalusage", (Double)row[1])
                .add("temperature",(Double)row[2]).build();
            arrayBuilder.add(personObject);
            }
            JsonArray jArray = arrayBuilder.build();
            return jArray;
       }
        return "Please enter the correct option (hourly/daily).";
    }    
    
    @GET
    @Path("getDailyTotalUsage/{resid}/{date}")
    @Produces({"application/json"})
    public Object getDailyTotalUsage(@PathParam("resid") Integer resid, @PathParam("date") Date date) {
            List<Object[]> queryList = em.createQuery("SELECT u.resid.resid, sum(u.fridgeusage + u.acusage + u.wmusage) AS totalusage, avg(u.temperature) FROM Usage u WHERE u.resid.resid = :resid AND u.date = :date GROUP BY u.resid.resid", Object[].class)
                    .setParameter("resid", resid)
                    .setParameter("date", date)
                    .getResultList();
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (Object[] row : queryList) {
                JsonObject personObject = Json.createObjectBuilder().
                add("resid", (Integer)row[0])
                .add("totalusage", (Double)row[1])
                .add("temperature",(Double)row[2]).build();
            arrayBuilder.add(personObject);
            }
            JsonArray jArray = arrayBuilder.build();
            return jArray;
    }    
}
