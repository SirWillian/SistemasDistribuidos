/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 *
 * @author a1717553
 */
public interface InterfaceCliente extends Remote{
    public void callback(String mensagem) throws RemoteException;
}
