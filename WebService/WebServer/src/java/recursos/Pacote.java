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
public class Pacote implements Serializable{
    public Voo vooIda;
    public Voo vooVolta;
    public Hotel hotel;
    int preco;
    
    public Pacote(Voo ida, Voo volta, Hotel h, int p){
        this.vooIda=ida;
        this.vooVolta=volta;
        this.hotel=h;
        this.preco=p;
    }
    
    @Override
    public String toString(){
        return (vooIda.origem + " - " + vooIda.destino + " - " + hotel.nome + " - " + preco);
    }
}
