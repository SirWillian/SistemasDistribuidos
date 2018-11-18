/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import recursos.*;
import recursos.PassagemAerea;

/**
 * REST Web Service
 *
 * @author WILLIAN
 */
@Path("/s")
public class Server {

    @Context
    private UriInfo context;
    ServImpl servidor;
    
    static List<Voo> listaVoos = new ArrayList<>();
    static List<Hotel> listaHotels = new ArrayList<>();

    /**
     * Creates a new instance of Server
     */
    public Server() {
        try{
            servidor = new ServImpl();
        }
        catch(Exception ex){
            System.out.println("Server()" + ex.getMessage());
        }
    }

    @GET
    @Path("/admin/hotel")
    @Produces(MediaType.APPLICATION_XML)
    public List<Hotel> getAllHotel(){
        return listaHotels;
    }
    
    @POST
    @Path("/admin/hotel")
    @Consumes(MediaType.APPLICATION_XML)
    public void postNewHotel(Hotel hotel){
        System.out.println(hotel.toString());
        listaHotels.add(hotel);
    }
    
    @GET
    @Path("/admin/voo")
    @Produces(MediaType.APPLICATION_XML)
    public List<Voo> getAllVoo(){
        return listaVoos;
    }
    
    @POST
    @Path("/admin/voo")
    @Consumes(MediaType.APPLICATION_XML)
    public void postNewVoo(Voo voo){
        listaVoos.add(voo);
    }
    
    
    /**
     * Retrieves representation of an instance of servidor.Server
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/hotel")
    @Produces(MediaType.APPLICATION_XML)
    public List<Hotel> getHotel(
            @QueryParam("destino") String destino,
            @QueryParam("nQuartos") int nQuartos,
            @QueryParam("nPessoas") int nPessoas) {
        
        return servidor.consultarHotel(listaHotels, destino, nQuartos, nPessoas);
    }

    /**
     * PUT method for updating or creating an instance of Server
     * @param content representation for the resource
     */
    @POST
    @Path("/hotel")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces("text/plain")
    public String buyHotel(Reserva reserva) {
        if(servidor.comprarReserva(listaHotels, reserva))
            return "Sua reserva foi feita com sucesso!";
        else
            return "Sua reserva não pode ser feita. Tente novamente.";
    }
    
    /**
     * Retrieves representation of an instance of servidor.Server
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/voo")
    @Produces(MediaType.APPLICATION_XML)
    public List<Voo> getVoo(
            @QueryParam("idaVolta") boolean idaVolta,
            @QueryParam("origem") String origem,
            @QueryParam("destino") String destino,
            @QueryParam("data") String data,
            @QueryParam("nPessoas") int nPessoas) {
        System.out.println(origem+" "+destino+" "+data+" "+String.valueOf(nPessoas));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        return servidor.consultarPassagem(listaVoos,idaVolta,origem,destino,LocalDate.parse(data.split(" ")[0],formatter),nPessoas);
        
    }

    /**
     * PUT method for updating or creating an instance of Server
     * @param content representation for the resource
     */
    @POST
    @Path("/voo")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces("text/plain")
    public String buyVoo(PassagemAerea passagem) {
        if(servidor.comprarPassagem(listaVoos, passagem))
            return "Sua reserva foi feita com sucesso!";
        else
            return "Sua reserva não pode ser feita. Tente novamente.";
    }
}
