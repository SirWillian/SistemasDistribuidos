/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recursos;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author a1717553
 */

@XmlRootElement(name="Hotel")
@XmlAccessorType(XmlAccessType.FIELD)
public class Hotel implements Serializable{
    @XmlElement(required=true)
    public String nome;
    @XmlElement(required=true) 
    public String local;
    @XmlElement(required=true) 
    public int precoQuarto;
    @XmlElement(required=true) 
    public int quartosVagos;
    
    public Hotel(){}
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
