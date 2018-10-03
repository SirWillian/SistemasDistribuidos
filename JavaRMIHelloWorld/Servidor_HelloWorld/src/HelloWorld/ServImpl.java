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
public class ServImpl extends UnicastRemoteObject implements InterfaceServ {
    public ServImpl() throws RemoteException {
        
    }
    
    @Override
    public void chamar(String mensagem, InterfaceCli referencia) throws RemoteException {
        System.out.println("chamado");
        referencia.echo(mensagem);
    }
    
}
