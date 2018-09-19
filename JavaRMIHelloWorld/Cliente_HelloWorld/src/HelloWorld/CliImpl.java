/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelloWorld;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author a1717553
 */
public class CliImpl extends UnicastRemoteObject implements InterfaceCli{
    private InterfaceServ servidor;
    
    public CliImpl(InterfaceServ s) throws RemoteException {
        this.servidor=s;
    }
    
    @Override
    public void echo(String mensagem) throws RemoteException {
        System.out.println(mensagem);
    }
    
    public void chamarServidor(String mensagem) throws RemoteException{
        this.servidor.chamar(mensagem, this);
    }
    
}
