/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recursos;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author a1717553
 */
public class Voo implements Serializable{
    //private static final long serialVersionUID = 1L;
    
    public LocalDate data;
    public String origem;
    public String destino;
    public String companhia;
    public int preco;
    public int assentosVagos;
    
    public Voo(LocalDate da, String o, String de, String c, int p, int av){
        this.data=da;
        this.origem=o;
        this.destino=de;
        this.companhia=c;
        this.preco=p;
        this.assentosVagos=av;
    }
    
    @Override
    public String toString(){
        return(DateTimeFormatter.ofPattern("d/MM/yyyy").format(data)+
                " - " + origem + " - " + destino + " - " + companhia+
                " - R$" + preco + " - " + assentosVagos);
    }
}
