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
        
    }

    @Override
    public List consultarHotel(String destino, LocalDate dataIda, LocalDate dataVolta, int nQuartos, int nPessoas) throws RemoteException {
        List<Hotel> consulta = listaHotels.stream()
                .filter(hotel -> (destino.equals(hotel.nome) || destino.equals(hotel.local)) &&
                        nQuartos <= hotel.quartosVagos)
                .collect(Collectors.toList());
        return consulta;
    }

    @Override
    public List consultarPassagem(boolean idaVolta, String origem, String destino, LocalDate dataIda, LocalDate dataVolta, int nPessoas) throws RemoteException {
        List <Voo> consulta;
        if(idaVolta)
            consulta = listaVoos.stream()
                    .filter(voo -> ((origem.equals(voo.origem) && destino.equals(voo.destino)) ||
                            (origem.equals(voo.destino) && destino.equals(voo.origem))) &&
                            (dataIda.isEqual(voo.data) || dataVolta.isEqual(voo.data)) &&
                            nPessoas <= voo.assentosVagos)
                    .collect(Collectors.toList());
        else
            consulta = listaVoos.stream()
                    .filter(voo -> origem.equals(voo.origem) &&
                            destino.equals(voo.destino) &&
                            dataIda.isEqual(voo.data) &&
                            nPessoas<=voo.assentosVagos)
                    .collect(Collectors.toList());
        
        return consulta;
    }

    @Override
    public synchronized void comprarReserva(Reserva reserva) throws RemoteException {
        Hotel compra = listaHotels.stream()
                .filter(hotel -> reserva.nome.equals(hotel.nome) &&
                        reserva.nQuartos<=hotel.quartosVagos)
                .findAny()
                .orElse(null);
        if(compra!=null){
            compra.quartosVagos-=reserva.nQuartos;
            if(compra.quartosVagos<=0)
                listaHotels.remove(compra);
        }
    }

    @Override
    public synchronized void comprarPassagem(PassagemAerea passagem) throws RemoteException {
        Voo compra1 = listaVoos.stream()
                .filter(voo -> passagem.dataIda.isEqual(voo.data) &&
                        passagem.origem.equals(voo.origem) &&
                        passagem.destino.equals(voo.destino) &&
                        passagem.nPessoas<=voo.assentosVagos)
                .findAny()
                .orElse(null);
        
        Voo compra2;
        if(passagem.idaVolta){
            compra2 = listaVoos.stream()
                .filter(voo -> passagem.dataVolta.isEqual(voo.data) &&
                        passagem.origem.equals(voo.origem) &&
                        passagem.destino.equals(voo.destino) &&
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
            }
        }
        
        if(compra1 != null){
            compra1.assentosVagos-=passagem.nPessoas;
            if(compra1.assentosVagos<=0)
                listaVoos.remove(compra1);
        }
            
    }

    @Override
    public void registrarInteresse(InterfaceCliente cliente, EnumTipoInteresse tipo, int precoMaximo) throws RemoteException {
        Interesse interesse = new Interesse(cliente, tipo, precoMaximo);
        interesseClientes.add(interesse);
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
    
    public void registrarVoo(LocalDate da, String de, int p, int av){
        
    }
    
    public void registrarHotel(){
        
    }
}
