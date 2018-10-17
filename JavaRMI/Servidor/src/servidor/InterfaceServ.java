/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import enums.EnumTipoInteresse;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;
import recursos.*;
/**
 *
 * @author a1717553
 */
public interface InterfaceServ extends Remote{
    public List<Hotel> consultarHotel(String destino, LocalDate dataIda, LocalDate dataVolta, int nQuartos, int nPessoas) throws RemoteException;
    public List<Voo> consultarPassagem(boolean idaVolta, String origem, String destino, LocalDate data, int nPessoas) throws RemoteException;
    public List<Pacote> consultarPacote(String origem, String destino, LocalDate dataIda, LocalDate dataVolta, int nPessoas, int nQuartos) throws RemoteException;
    public boolean comprarReserva(Reserva reserva) throws RemoteException;
    public boolean comprarPassagem(PassagemAerea passagem) throws RemoteException;
    public boolean comprarPacote(Pacote pacote, int nPessoas, int nQuartos) throws RemoteException;
    public void registrarInteresse(InterfaceCliente cliente, EnumTipoInteresse tipo, String destino, int precoMaximo) throws RemoteException;
    public void cancelarInteresse(InterfaceCliente cliente, EnumTipoInteresse tipo) throws RemoteException;
}
