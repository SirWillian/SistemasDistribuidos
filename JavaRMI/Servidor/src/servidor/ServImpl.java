/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.List;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List consultarPassagem(boolean idaVolta, String origem, String destino, LocalDate dataIda, LocalDate dataVolta, int nPessoas) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void comprarReserva(Reserva reserva) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void comprarPassagem(PassagemAerea passagem) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void registrarInteresse(InterfaceCliente cliente, PassagemAerea passagem) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void registrarInteresse(InterfaceCliente cliente, Reserva reserva) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cancelarInteresse(InterfaceCliente cliente, PassagemAerea passagem) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cancelarInteresse(InterfaceCliente cliente, Reserva reserva) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void registrarVoo(LocalDate da, String de, int p, int av){
        
    }
    
    public void registrarHotel(){
        
    }
}
