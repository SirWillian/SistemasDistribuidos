/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelloWorld;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author a1717553
 */
public class Cliente {
    public static void main(String[] args){
        try {
            Registry servicoNomes = LocateRegistry.getRegistry("localhost", 1100);
            InterfaceServ servidorSamba = (InterfaceServ) servicoNomes.lookup("samba");
            
            CliImpl cliente = new CliImpl(servidorSamba);
            
            cliente.chamarServidor("olha a festa");
        }
        catch(Exception ex){
            System.out.println("Cliente main: " + ex.getMessage());
        }
    }
}
