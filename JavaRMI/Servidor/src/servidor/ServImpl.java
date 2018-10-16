/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import enums.EnumTipoInteresse;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import recursos.*;

/**
 *
 * @author a1717553
 */
public class ServImpl extends UnicastRemoteObject implements InterfaceServ {
    private List<Voo> listaVoos;
    private List<Hotel> listaHotels;
    private List<Interesse> interesseClientes;
    
    public ServImpl() throws RemoteException {
        listaVoos = new ArrayList<>();
        listaHotels = new ArrayList<>();
        interesseClientes = new ArrayList<>();
    }

    @Override
    public List<Hotel> consultarHotel(String destino, LocalDate dataIda, LocalDate dataVolta, int nQuartos, int nPessoas) throws RemoteException {
        List<Hotel> consulta = listaHotels.stream()
                .filter(hotel -> (destino.equals(hotel.nome) || destino.equals(hotel.local)) &&
                        nQuartos <= hotel.quartosVagos)
                .collect(Collectors.toList());
        return consulta;
    }

    @Override
    public List<Voo> consultarPassagem(boolean idaVolta, String origem, String destino, LocalDate data, int nPessoas) throws RemoteException {
        List <Voo> consulta;
        consulta = listaVoos.stream()
                .filter(voo -> origem.equals(voo.origem) &&
                        destino.equals(voo.destino) &&
                        data.isEqual(voo.data) &&
                        nPessoas<=voo.assentosVagos)
                .collect(Collectors.toList());
        
        return consulta;
    }

    @Override
    public synchronized boolean comprarReserva(Reserva reserva) throws RemoteException {
        Hotel compra = listaHotels.stream()
                .filter(hotel -> reserva.nome.equals(hotel.nome) &&
                        reserva.nQuartos<=hotel.quartosVagos)
                .findAny()
                .orElse(null);
        if(compra!=null){
            compra.quartosVagos-=reserva.nQuartos;
            if(compra.quartosVagos<=0)
                listaHotels.remove(compra);
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean comprarPassagem(PassagemAerea passagem) throws RemoteException {
        Voo compra1 = listaVoos.stream()
                .filter(voo -> passagem.dataIda.isEqual(voo.data) &&
                        passagem.origem.equals(voo.origem) &&
                        passagem.destino.equals(voo.destino) &&
                        passagem.nPessoas<=voo.assentosVagos)
                .findAny()
                .orElse(null);
        
        if(passagem.idaVolta){
            Voo compra2 = listaVoos.stream()
                .filter(voo -> passagem.dataVolta.isEqual(voo.data) &&
                        passagem.origem.equals(voo.destino) &&
                        passagem.destino.equals(voo.origem) &&
                        passagem.nPessoas<=voo.assentosVagos)
                .findAny()
                .orElse(null);
            
            if(compra1 != null && compra2 != null){
                compra1.assentosVagos-=passagem.nPessoas;
                compra2.assentosVagos-=passagem.nPessoas;
                if(compra1.assentosVagos<=0)
                    listaVoos.remove(compra1);
                if(compra2.assentosVagos<=0)
                    listaVoos.remove(compra2);
                return true;
            }
            return false;
        }
        else if(compra1 != null){
            compra1.assentosVagos-=passagem.nPessoas;
            if(compra1.assentosVagos<=0)
                listaVoos.remove(compra1);
            return true;
        }
        return false;
    }

    @Override
    public void registrarInteresse(InterfaceCliente cliente, EnumTipoInteresse tipo, int precoMaximo) throws RemoteException {
        Interesse novoInteresse = new Interesse(cliente, tipo, precoMaximo);
        Interesse registrado = interesseClientes.stream()
                .filter(interesse -> cliente.equals(interesse.cliente) &&
                        tipo.equals(interesse.tipo))
                .findAny()
                .orElse(null);
        if(registrado==null)
            interesseClientes.add(novoInteresse);
    }

    @Override
    public void cancelarInteresse(InterfaceCliente cliente, EnumTipoInteresse tipo) throws RemoteException {
        Interesse cancelado = interesseClientes.stream()
                .filter(interesse -> cliente.equals(interesse.cliente) &&
                        tipo.equals(tipo))
                .findAny()
                .orElse(null);
        
        if(cancelado!=null)
            interesseClientes.remove(cancelado);
    }
    
    public void registrarVoo(LocalDate da, String o, String de, String c, int p, int av){
        Voo novoVoo = new Voo(da, o, de, c, p, av);
        listaVoos.add(novoVoo);
        notificacaoCliente(EnumTipoInteresse.PASSAGEM, p);
    }
    
    public void registrarHotel(String n, String l, int p, int q){
        Hotel novoHotel = new Hotel(n, l, p, q);
        listaHotels.add(novoHotel);
        notificacaoCliente(EnumTipoInteresse.RESERVA, p);
    }
    
    public void listarHoteis(){
        for(Hotel hotel : listaHotels)
            System.out.println(hotel.toString());
    }
    
    public void listarVoos(){
        for(Voo voo : listaVoos)
            System.out.println(voo.toString());
    }
    
    public void notificacaoCliente(EnumTipoInteresse tipo, int preco){
        for(Interesse cliente : interesseClientes){
            if(cliente.tipo==tipo && preco<=cliente.precoMaximo){
                try{
                    cliente.cliente.callback("Nova oferta de " + tipo.toString() + " na qual você pode estar interessado.");
                }
                catch(Exception ex){
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
}
