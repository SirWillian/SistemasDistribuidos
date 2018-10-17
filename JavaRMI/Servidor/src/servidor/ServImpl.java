/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import enums.EnumTipoInteresse;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import recursos.*;

/**
 *
 * @author a1717553
 */
public class ServImpl extends UnicastRemoteObject implements InterfaceServ {
    private List<Voo> listaVoos;
    private List<Hotel> listaHotels;
    private List<Pacote> listaPacotes;
    private List<Interesse> interesseClientes;
    
    public ServImpl() throws RemoteException {
        listaVoos = new ArrayList<>();
        listaHotels = new ArrayList<>();
        listaPacotes = new ArrayList<>();
        interesseClientes = new ArrayList<>();
    }

    /**
     * Pesquisa hotéis registrados com as características recebidas
     * 
     * @param destino
     * @param dataIda
     * @param dataVolta
     * @param nQuartos
     * @param nPessoas
     * @return
     * @throws RemoteException
     */
    @Override
    public List<Hotel> consultarHotel(String destino, LocalDate dataIda, LocalDate dataVolta, int nQuartos, int nPessoas) throws RemoteException {
        List<Hotel> consulta = listaHotels.stream()
                .filter(hotel -> (destino.equals(hotel.nome) || destino.equals(hotel.local)) &&
                        nQuartos <= hotel.quartosVagos)
                .collect(Collectors.toList());
        return consulta;
    }

    /**
     * Pesquisa voos de avião com as características recebidas
     * 
     * @param idaVolta
     * @param origem
     * @param destino
     * @param data
     * @param nPessoas
     * @return
     * @throws RemoteException
     */
    @Override
    public List<Voo> consultarPassagem(boolean idaVolta, String origem, String destino, LocalDate data, int nPessoas) throws RemoteException {
        List <Voo> consulta;
        consulta = listaVoos.stream()
                .filter(voo -> origem.equals(voo.origem) &&
                        destino.equals(voo.destino) &&
                        data.isEqual(voo.data) &&
                        nPessoas<=voo.assentosVagos)
                .collect(Collectors.toList());
        
        return consulta;
    }
    
    @Override
    public List<Pacote> consultarPacote(String origem, String destino, LocalDate dataIda, LocalDate dataVolta, int nPessoas, int nQuartos) throws RemoteException {
        List<Pacote> consulta;
        consulta = listaPacotes.stream()
                .filter(pacote -> origem.equals(pacote.vooIda.origem) &&
                        destino.equals(pacote.vooVolta.origem) &&
                        dataIda.isEqual(pacote.vooIda.data) &&
                        dataVolta.isEqual(pacote.vooVolta.data) &&
                        nPessoas<=pacote.vooIda.assentosVagos &&
                        nPessoas<=pacote.vooVolta.assentosVagos &&
                        nQuartos<=pacote.hotel.quartosVagos)
                .collect(Collectors.toList());
        
        return consulta;
    }

    /**
     * Tenta reservar quartos num hotel
     * 
     * @param reserva
     * @return Sucesso (ou falha) da compra
     * @throws RemoteException
     */
    @Override
    public synchronized boolean comprarReserva(Reserva reserva) throws RemoteException {
        Hotel compra = listaHotels.stream()
                .filter(hotel -> reserva.nome.equals(hotel.nome) &&
                        reserva.nQuartos<=hotel.quartosVagos)
                .findAny()
                .orElse(null);
        if(compra!=null){
            compra.quartosVagos-=reserva.nQuartos;
            if(compra.quartosVagos<=0)
                listaHotels.remove(compra);
            return true;
        }
        return false;
    }

    /**
     * Tenta comprar uma passagem de avião
     * 
     * @param passagem
     * @return Sucesso (ou falha) da compra
     * @throws RemoteException
     */
    @Override
    public synchronized boolean comprarPassagem(PassagemAerea passagem) throws RemoteException {
        Voo compra1 = listaVoos.stream()
                .filter(voo -> passagem.dataIda.isEqual(voo.data) &&
                        passagem.origem.equals(voo.origem) &&
                        passagem.destino.equals(voo.destino) &&
                        passagem.nPessoas<=voo.assentosVagos)
                .findAny()
                .orElse(null);
        
        if(passagem.idaVolta){
            Voo compra2 = listaVoos.stream()
                .filter(voo -> passagem.dataVolta.isEqual(voo.data) &&
                        passagem.origem.equals(voo.destino) &&
                        passagem.destino.equals(voo.origem) &&
                        passagem.nPessoas<=voo.assentosVagos)
                .findAny()
                .orElse(null);
            
            if(compra1 != null && compra2 != null){
                compra1.assentosVagos-=passagem.nPessoas;
                compra2.assentosVagos-=passagem.nPessoas;
                if(compra1.assentosVagos<=0)
                    listaVoos.remove(compra1);
                if(compra2.assentosVagos<=0)
                    listaVoos.remove(compra2);
                return true;
            }
            return false;
        }
        else if(compra1 != null){
            compra1.assentosVagos-=passagem.nPessoas;
            if(compra1.assentosVagos<=0)
                listaVoos.remove(compra1);
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean comprarPacote(Pacote pacote, int nPessoas, int nQuartos) throws RemoteException {
        if(nPessoas<=pacote.vooIda.assentosVagos &&
            nPessoas<=pacote.vooVolta.assentosVagos &&
            nQuartos<=pacote.hotel.quartosVagos){
            
            pacote.vooIda.assentosVagos-=nPessoas;
            pacote.vooVolta.assentosVagos-=nPessoas;
            pacote.hotel.quartosVagos-=nQuartos;
            
            if(pacote.vooIda.assentosVagos<=0 || pacote.vooVolta.assentosVagos<=0 || pacote.hotel.quartosVagos<=0){
                listaPacotes.remove(pacote);
                if(pacote.vooIda.assentosVagos<=0)
                    listaVoos.remove(pacote.vooIda);
                if(pacote.vooVolta.assentosVagos<=0)
                    listaVoos.remove(pacote.vooVolta);
                if(pacote.hotel.quartosVagos<=0)
                    listaHotels.remove(pacote.hotel);
            }
            
            return true;
        }
        return false;
    }
    /**
     * Registra interesse de notificações de um cliente
     *
     * @param cliente
     * @param tipo
     * @param destino
     * @param precoMaximo
     * @throws RemoteException
     */
    @Override
    public void registrarInteresse(InterfaceCliente cliente, EnumTipoInteresse tipo, String destino, int precoMaximo) throws RemoteException {
        Interesse novoInteresse = new Interesse(cliente, tipo, destino, precoMaximo);
        Interesse registrado = interesseClientes.stream()
                .filter(interesse -> cliente.equals(interesse.cliente) &&
                        tipo.equals(interesse.tipo) &&
                        destino.equals(interesse.destino))
                .findAny()
                .orElse(null);
        if(registrado==null)
            interesseClientes.add(novoInteresse);
    }

    /**
     * Cancela o registro de interesse de notificações de um cliente
     * 
     * @param cliente
     * @param tipo
     * @throws RemoteException
     */
    @Override
    public void cancelarInteresse(InterfaceCliente cliente, EnumTipoInteresse tipo) throws RemoteException {
        Interesse cancelado = interesseClientes.stream()
                .filter(interesse -> cliente.equals(interesse.cliente) &&
                        tipo.equals(tipo))
                .findAny()
                .orElse(null);
        
        if(cancelado!=null)
            interesseClientes.remove(cancelado);
    }
    
    /**
     * Registra um voo de avião no sistema
     * 
     * @param da Data do voo
     * @param o Origem do voo
     * @param de Destino do voo
     * @param c Companhia aérea do voo
     * @param p Preço da passagem
     * @param av Assentos vagos no avião
     */
    public void registrarVoo(LocalDate da, String o, String de, String c, int p, int av){
        Voo novoVoo = new Voo(da, o, de, c, p, av);
        listaVoos.add(novoVoo);
        notificacaoCliente(EnumTipoInteresse.PASSAGEM, novoVoo.destino, p);
    }
    
    /**
     * Registra um hotel no sistema
     * 
     * @param n Nome do hotel
     * @param l Local do hotel
     * @param p Preço do quarto
     * @param q Quartos vagos no hotel
     */
    public void registrarHotel(String n, String l, int p, int q){
        Hotel novoHotel = new Hotel(n, l, p, q);
        listaHotels.add(novoHotel);
        notificacaoCliente(EnumTipoInteresse.RESERVA, novoHotel.local, p);
    }
    
    /**
     * Registra um novo pacote de viagens
     */
    public void registrarPacote(){
        listarVoos();
        System.out.println("Selecione o índice do voo de ida (começando em 1): ");
        Scanner scanner = new Scanner(System.in);
        int escolha = Integer.valueOf(scanner.nextLine());
        Voo voo1 = listaVoos.get(escolha-1);
        List<Voo> listaVooVolta = listaVoos.stream()
                .filter(voo -> voo.destino.equals(voo1.origem))
                .collect(Collectors.toList());
        if(listaVooVolta.isEmpty()){
            System.out.println("Não há voos de volta para fazer o pacote");
            return;
        }
            
        for(Voo voo : listaVooVolta){
            System.out.println(voo.toString());
        }
        System.out.println("Selecione o índice do voo de volta (começando em 1): ");
        escolha = Integer.valueOf(scanner.nextLine());
        Voo voo2 = listaVooVolta.get(escolha-1);
        
        List<Hotel> listaHotelPacote = listaHotels.stream()
                .filter(hotel -> hotel.local.equals(voo2.origem))
                .collect(Collectors.toList());
        
        if(listaHotelPacote.isEmpty()){
            System.out.println("Não há hotéis no local de destino para fazer o pacote.");
            return;
        }
        
        for(Hotel hotel : listaHotelPacote){
            System.out.println(hotel.toString());
        }
        
        System.out.println("Selecione o índice do hotel (começando em 1): ");
        escolha = Integer.valueOf(scanner.nextLine());
        Hotel hotelPacote = listaHotels.get(escolha-1);
        
        int precoFinal = (int) ((voo1.preco + voo2.preco + hotelPacote.precoQuarto)*0.9);
        Pacote novoPacote = new Pacote(voo1, voo2, hotelPacote, precoFinal);
        listaPacotes.add(novoPacote);
        
        notificacaoCliente(EnumTipoInteresse.PACOTE, voo1.destino, precoFinal);
    }
    
    /**
     * Exibe os hotéis registrados
     */
    public void listarHoteis(){
        for(Hotel hotel : listaHotels)
            System.out.println(hotel.toString());
    }
    
    /**
     * Exibe os voos registrados
     */
    public void listarVoos(){
        for(Voo voo : listaVoos)
            System.out.println(voo.toString());
    }
    
    /**
     * Exibe os pacotes registrados
     */
    public void listarPacotes(){
        for(Pacote pacote : listaPacotes)
            System.out.println(pacote.toString());
    }
    
    /**
     * Notifica os clientes interessados
     * 
     * @param tipo
     * @param destino
     * @param preco
     */
    public void notificacaoCliente(EnumTipoInteresse tipo, String destino, int preco){
        for(Interesse cliente : interesseClientes){
            if(cliente.tipo==tipo && preco<=cliente.precoMaximo && destino.equals(cliente.destino)){
                try{
                    cliente.cliente.callback("Nova oferta de " + tipo.toString() + " na qual você pode estar interessado.");
                }
                /*catch(ConnectException ex){
                    interesseClientes.remove(cliente);
                }*/
                catch(Exception ex){
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
}
