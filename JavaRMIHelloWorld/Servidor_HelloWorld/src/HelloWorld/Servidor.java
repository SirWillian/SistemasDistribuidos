/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelloWorld;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author a1717553
 */
public class Servidor {
    public static void main(String[] args){
        try{
            Registry servicoNomes = LocateRegistry.createRegistry(1100);
            ServImpl servidor = new ServImpl();
            
            servicoNomes.bind("samba", servidor);
        }
        catch (Exception ex) {
            System.out.println("Servidor main: " + ex.getMessage());
        }
        
        
    }
}
