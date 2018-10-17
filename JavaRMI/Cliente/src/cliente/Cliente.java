/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import enums.EnumTipoInteresse;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import recursos.*;
import servidor.InterfaceServ;

/**
 *
 * @author a1717553
 */
public class Cliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            Registry servicoNomes = LocateRegistry.getRegistry("localhost", 1100);
            InterfaceServ servidorSamba = (InterfaceServ) servicoNomes.lookup("samba");

            ClienteImpl cliente = new ClienteImpl(servidorSamba);
            
            System.out.println("Insira '?' para receber ajuda sobre os comandos disponíveis.");
            while(true){
                Scanner scanner = new Scanner(System.in);
                System.out.println("Insira um comando.");
                String command = scanner.nextLine();
                
                switch(command){
                    case "?":
                        System.out.println("'cv' - Consulta e compra de passagens aéreas;");
                        System.out.println("'ch' - Consulta e reserva de quartos de hotel;");
                        System.out.println("'cp' - Consulta e compra de pacotes de viagem;");
                        System.out.println("'ri' - Registro de interesse;");
                        System.out.println("'ci' - Cancelamento de interesse;");
                        System.out.println("'exit' - Sai do programa.");
                        break;
                        
                    case "exit":
                        System.exit(0);
                        
                    case "ri":
                        System.out.println("Insira um número de 1 a 3.");
                        System.out.println("1 - Interesse em novos voos.");
                        System.out.println("2 - Interesse em novos hotéis.");
                        System.out.println("3 - Interesse em novos pacotes.");
                        int tipo = Integer.valueOf(scanner.nextLine());
                        
                        System.out.println("Insira o preço máximo que está disposto a pagar:");
                        int precoMaximo = Integer.valueOf(scanner.nextLine());
                        
                        System.out.println("Insira o destino no qual está interessado:");
                        String destino = scanner.nextLine();
                        
                        servidorSamba.registrarInteresse(cliente, EnumTipoInteresse.values()[tipo-1], destino, precoMaximo);
                        break;
                    case "ci":
                        System.out.println("Insira o número do tipo de interesse que deseja cancelar.");
                        System.out.println("1 - Interesse em novos voos.");
                        System.out.println("2 - Interesse em novos hotéis.");
                        System.out.println("3 - Interesse em novos pacotes.");
                        tipo = Integer.valueOf(scanner.nextLine());
                        
                        servidorSamba.cancelarInteresse(cliente, EnumTipoInteresse.values()[tipo-1]);
                        break;
                        
                    case "cv":
                        boolean idaVolta=false;
                        System.out.println("Viagem de ida e volta? (y/n)");
                        if(scanner.nextLine().equals("y"))
                            idaVolta=true;
                        
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
                        LocalDate dataPartida;
                        LocalDate dataVolta=null;
                        String origem;
                        int nPessoas;
                        try{
                            System.out.println("Insira a data de partida (formato dd/mm/aaaa)");
                            dataPartida = LocalDate.parse(scanner.nextLine(),formatter);
                            
                            if(idaVolta){
                                System.out.println("Insira a data de volta (formato dd/mm/aaaa)");
                                dataVolta = LocalDate.parse(scanner.nextLine(),formatter);
                            }
                            System.out.println("Insira o origem do voo");
                            origem = scanner.nextLine();

                            System.out.println("Insira o destino do voo");
                            destino = scanner.nextLine();

                            System.out.println("Insira o número de passageiros");
                            nPessoas = Integer.valueOf(scanner.nextLine());
                        }
                        catch(DateTimeException ex){
                            System.out.println("Por favor insira a data no formato coreto");
                            break;
                        }
                        catch(NumberFormatException ex){
                            System.out.println("Por favor insira um número");
                            break;
                        }
                        
                        List<Voo> listaVoosIda=servidorSamba.consultarPassagem(idaVolta, origem, destino, dataPartida, nPessoas);
                        int indice=1;
                        System.out.println("Indice - Data - Origem - Destino - Companhia - Preço - Assentos Vagos");
                        for(Voo voo : listaVoosIda){
                            System.out.println(indice + " - " + voo.toString());
                            indice++;
                        }
                        List<Voo> listaVoosVolta = new ArrayList<>();
                        if(idaVolta){
                            listaVoosVolta=servidorSamba.consultarPassagem(idaVolta, destino, origem, dataVolta, nPessoas);
                            System.out.println();
                            System.out.println("Indice - Data - Origem - Destino - Companhia - Preço - Assentos Vagos");
                            indice=1;
                            for(Voo voo : listaVoosVolta){
                                System.out.println(indice + " - " + voo.toString());
                                indice++;
                            }
                        }
                        
                        System.out.println();
                        if(idaVolta)
                            System.out.println("Insira o índice da passagem de ida que deseja comprar (0 para sair)");
                        else
                            System.out.println("Insira o índice da passagem que deseja comprar (0 para sair)");
                        int escolha = Integer.valueOf(scanner.nextLine());
                        if(escolha==0)
                            break;
                        
                        Voo voo1 = listaVoosIda.get(escolha-1);
                        PassagemAerea passagem;
                        if(idaVolta){
                            System.out.println("Insira o índice da passagem de volta que deseja comprar (0 para sair)");
                            escolha = Integer.valueOf(scanner.nextLine());
                            if(escolha==0)
                                break;
                            Voo voo2 = listaVoosVolta.get(escolha-1);
                            passagem = new PassagemAerea(idaVolta, voo1.origem, voo1.destino, voo1.companhia, voo2.companhia, dataPartida, dataVolta, nPessoas);
                        }
                        else
                            passagem = new PassagemAerea(idaVolta, voo1.origem, voo1.destino, voo1.companhia, null, dataPartida, null, nPessoas);
                        
                        if(servidorSamba.comprarPassagem(passagem))
                            System.out.println("Sua compra foi efetuada com sucesso!");
                        else
                            System.out.println("Sua compra não pôde ser efetuada. Tente novamente.");
                        break;
                        
                    case "ch":
                        formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
                        int nQuartos;
                        try{
                            System.out.println("Insira a data de partida (formato dd/mm/aaaa)");
                            dataPartida = LocalDate.parse(scanner.nextLine(),formatter);
                            
                            System.out.println("Insira a data de volta (formato dd/mm/aaaa)");
                            dataVolta = LocalDate.parse(scanner.nextLine(),formatter);
                            
                            System.out.println("Insira o local de destino:");
                            destino = scanner.nextLine();

                            System.out.println("Insira o número de pessoas:");
                            nPessoas = Integer.valueOf(scanner.nextLine());

                            System.out.println("Insira o número de quartos a serem reservados:");
                            nQuartos = Integer.valueOf(scanner.nextLine());
                        }
                        catch(DateTimeException ex){
                            System.out.println("Por favor insira a data no formato coreto");
                            break;
                        }
                        catch(NumberFormatException ex){
                            System.out.println("Por favor insira um número");
                            break;
                        }
                        
                        List<Hotel> listaHotels = servidorSamba.consultarHotel(destino, dataPartida, dataVolta, nQuartos, nPessoas);
                        indice=1;
                        System.out.println("Indice - Nome - Local - Preço por quarto - Quartos Vagos");
                        for(Hotel hotel : listaHotels){
                            System.out.println(indice + " - " + hotel.toString());
                            indice++;
                        }
                        System.out.println("Insira o índice do hotel em que deseja fazer uma reserva (0 para sair)");
                        escolha = Integer.valueOf(scanner.nextLine());
                        if(escolha==0)
                            break;
                        Hotel hotel = listaHotels.get(escolha-1);
                        Reserva reserva = new Reserva(hotel.nome, hotel.local, dataPartida, dataVolta, nQuartos, nPessoas);
                        if(servidorSamba.comprarReserva(reserva))
                            System.out.println("Sua reserva foi feita com sucesso!");
                        else
                            System.out.println("Sua compra não pôde ser efetuada. Tente novamente.");
                        break;
                        
                    case "cp":
                        formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
                        try{
                            System.out.println("Insira a data de partida (formato dd/mm/aaaa)");
                            dataPartida = LocalDate.parse(scanner.nextLine(),formatter);
                            
                            System.out.println("Insira a data de volta (formato dd/mm/aaaa)");
                            dataVolta = LocalDate.parse(scanner.nextLine(),formatter);
                            
                            System.out.println("Insira a origem da viagem:");
                            origem = scanner.nextLine();

                            System.out.println("Insira o destino da viagem:");
                            destino = scanner.nextLine();

                            System.out.println("Insira o número de pessoas da viagem:");
                            nPessoas = Integer.valueOf(scanner.nextLine());
                            
                            System.out.println("Insira o número de quartos desejados:");
                            nQuartos = Integer.valueOf(scanner.nextLine());
                        }
                        catch(DateTimeException ex){
                            System.out.println("Por favor insira a data no formato coreto");
                            break;
                        }
                        catch(NumberFormatException ex){
                            System.out.println("Por favor insira um número");
                            break;
                        }
                        
                        List<Pacote> listaPacotes = servidorSamba.consultarPacote(origem, destino, dataPartida, dataVolta, nPessoas, nQuartos);
                        indice=1;
                        System.out.println("Indice - Origem - Destino - Nome do Hotel - Preço");
                        for(Pacote pacote : listaPacotes){
                            System.out.println(indice + " - " + pacote.toString());
                            indice++;
                        }
                        System.out.println("Insira o índice do pacote que deseja comprar (0 para sair)");
                        escolha = Integer.valueOf(scanner.nextLine());
                        if(escolha==0)
                            break;
                        Pacote pacote = listaPacotes.get(escolha-1);
                        if(servidorSamba.comprarPacote(pacote, nPessoas, nQuartos))
                            System.out.println("Seu pacote foi comprado com sucesso!");
                        else
                            System.out.println("Sua compra não pôde ser efetuada. Tente novamente.");
                        break;
                }
            }
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }   
            
    }
    
}
