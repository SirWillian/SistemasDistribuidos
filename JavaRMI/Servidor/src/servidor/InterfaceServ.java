/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;
import recursos.PassagemAerea;
import recursos.Reserva;
/**
 *
 * @author a1717553
 */
public interface InterfaceServ extends Remote{
    public List consultarHotel(String destino, LocalDate dataIda, LocalDate dataVolta, int nQuartos, int nPessoas) throws RemoteException;
    public List consultarPassagem(boolean idaVolta, String origem, String destino, LocalDate dataIda, LocalDate dataVolta, int nPessoas) throws RemoteException;
    public void comprarReserva(Reserva reserva) throws RemoteException;
    public void comprarPassagem(PassagemAerea passagem) throws RemoteException;
    public void registrarInteresse(InterfaceCliente cliente, PassagemAerea passagem) throws RemoteException;
    public void registrarInteresse(InterfaceCliente cliente, Reserva reserva) throws RemoteException;
    public void cancelarInteresse(InterfaceCliente cliente, PassagemAerea passagem) throws RemoteException;
    public void cancelarInteresse(InterfaceCliente cliente, Reserva reserva) throws RemoteException;
}
