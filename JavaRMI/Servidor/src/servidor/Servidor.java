/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 *
 * @author a1717553
 */
public class Servidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            Registry servicoNomes = LocateRegistry.createRegistry(1100);
            ServImpl servidor = new ServImpl();
            
            servicoNomes.bind("samba", servidor);
            System.out.println("Insira '?' para receber ajuda sobre os comandos disponíveis.");
            while(true){
                Scanner scanner = new Scanner(System.in);
                System.out.println("Insira um comando.");
                String command = scanner.nextLine();
                
                switch(command){
                    case "?":
                        System.out.println("'rh' - Registro de um novo hotel;");
                        System.out.println("'rv' - Registro de um novo voo;");
                        System.out.println("'rp' - Registro de um novo pacote de viagens;");
                        System.out.println("'lh' - Listar hotéis registrados;");
                        System.out.println("'lv' - Listar voos registrados;");
                        System.out.println("'lp' - Listar pacotes registrados.");
                        break;
                    case "rh":
                        System.out.println("Nome do hotel: ");
                        String nome = scanner.nextLine();
                        System.out.println("Local do hotel: ");
                        String local = scanner.nextLine();
                        System.out.println("Preço do quarto do hotel: ");
                        int precoQuarto = Integer.valueOf(scanner.nextLine());
                        System.out.println("Número de quartos no hotel: ");
                        int quartosVagos = Integer.valueOf(scanner.nextLine());
                        servidor.registrarHotel(nome, local, precoQuarto, quartosVagos);
                        break;
                    
                    case "rv":
                        System.out.println("Origem do voo:");
                        String origem = scanner.nextLine();
                        System.out.println("Destino do voo:");
                        String destino = scanner.nextLine();
                        System.out.println("Data do voo (formato dd/mm/aaaa):");
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
                        LocalDate data;
                        try{
                            data = LocalDate.parse(scanner.nextLine(), formatter);
                        }
                        catch(Exception ex){
                            System.out.println("Insira a data no formato correto");
                            break;
                        }
                        System.out.println("Nome da companhia aerea:");
                        String companhia = scanner.nextLine();
                        System.out.println("Preço do voo: ");
                        int precoVoo = Integer.valueOf(scanner.nextLine());
                        System.out.println("Número de assentos no aviao: ");
                        int assentosVagos = Integer.valueOf(scanner.nextLine());
                        servidor.registrarVoo(data, origem, destino, companhia, precoVoo, assentosVagos);
                        break;
                        
                    case "rp":
                        servidor.registrarPacote();
                        break;
                        
                    case "lh":
                        servidor.listarHoteis();
                        break;
                    
                    case "lv":
                        servidor.listarVoos();
                        break;
                        
                    case "lp":
                        servidor.listarPacotes();
                        break;
                }
            }
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    
}
