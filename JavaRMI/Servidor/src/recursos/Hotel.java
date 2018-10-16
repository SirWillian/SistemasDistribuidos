/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recursos;

import java.io.Serializable;

/**
 *
 * @author a1717553
 */
public class Hotel implements Serializable{
    public String nome;
    public String local;
    public int precoQuarto;
    public int quartosVagos;
    
    public Hotel(String n, String l, int p, int q){
        this.nome=n;
        this.local=l;
        this.precoQuarto=p;
        this.quartosVagos=q;
    }
    
    @Override
    public String toString(){
        return(nome + " - " + local + " - R$" + precoQuarto + " - " + quartosVagos);
    }
}
