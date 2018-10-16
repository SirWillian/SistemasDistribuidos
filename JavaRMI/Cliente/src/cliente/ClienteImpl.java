/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import servidor.InterfaceCliente;
import servidor.InterfaceServ;
/**
 *
 * @author a1717553
 */
public class ClienteImpl extends UnicastRemoteObject implements InterfaceCliente{
    private InterfaceServ servidor;
    
    public ClienteImpl(InterfaceServ s) throws RemoteException{
        this.servidor=s;
    }
    
    @Override
    public void callback(String mensagem) throws RemoteException {
        System.out.println(mensagem);
    }
    
}
