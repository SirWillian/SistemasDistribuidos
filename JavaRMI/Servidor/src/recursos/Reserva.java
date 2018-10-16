/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recursos;

import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author a1717553
 */
public class Reserva implements Serializable{
    public String nome;
    public String destino;
    public LocalDate dataIda;
    public LocalDate dataVolta;
    public int nQuartos;
    public int nPessoas;
    
    public Reserva(String n, String d, LocalDate di, LocalDate dv, int q, int p){
        this.nome=n;
        this.destino=d;
        this.dataIda=di;
        this.dataVolta=dv;
        this.nQuartos=q;
        this.nPessoas=p;
    }
}
