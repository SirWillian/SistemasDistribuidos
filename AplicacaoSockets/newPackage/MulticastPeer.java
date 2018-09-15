import enums.EnumRecursoId;
import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.UUID;

public class MulticastPeer{
    public static void main(String args[]) {
        MulticastSocket s = null;
        MulticastSocket sRequest = null;
        InetAddress group;
        MulticastTransceiver transceiver;
        try {
            //Inicia identificadores e sockets
            UUID uniqueId = UUID.randomUUID();
            System.out.println("Identificador: " + uniqueId.toString());
            
            group = InetAddress.getByName("232.232.232.233");
            s = new MulticastSocket(6789);
            s.joinGroup(group);
            sRequest = new MulticastSocket(6789);
            sRequest.joinGroup(group);
            sRequest.setSoTimeout(5000);
            
            transceiver = new MulticastTransceiver(s, sRequest, group, uniqueId);
            transceiver.start();
            transceiver.joinGroup();
            
            System.out.println("Digite 'help' ou '?' para saber os comandos disponíveis.");
            while(true){
                Scanner scanner = new Scanner(System.in);
                String message = scanner.nextLine();
                
                switch(message){
                    case "r1":
                        transceiver.requestResource(EnumRecursoId.RECURSO_UM);
                        break;
                    case "r2":
                        transceiver.requestResource(EnumRecursoId.RECURSO_DOIS);
                        break;
                    case "l1":
                        transceiver.releaseResource(EnumRecursoId.RECURSO_UM);
                        break;
                    case "l2":
                        transceiver.releaseResource(EnumRecursoId.RECURSO_DOIS);
                        break;
                    case "?":
                    case "help":
                        System.out.println("'r1' e 'r2': Requisição de recurso");
                        System.out.println("'l1' e '12': Liberação de recurso");
                        System.out.println("'quit': Sai do grupo multicast");
                        break;
                    case "quit":
                        transceiver.leaveGroup();
                        s.leaveGroup(group);
                        sRequest.leaveGroup(group);
                        s.close();
                        sRequest.close();
                        System.out.println("Conexão fechada");
                        System.exit(0);
                        break;
                }
            }        
        }
        catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        }
        catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
        finally {
            if (s != null) {
                s.close();
            }
        }
    }
}