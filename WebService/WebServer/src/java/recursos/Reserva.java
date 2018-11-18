/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recursos;

import java.io.Serializable;
import java.time.LocalDate;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author a1717553
 */
@XmlRootElement(name="Reserva")
@XmlAccessorType(XmlAccessType.FIELD)
public class Reserva implements Serializable{
    @XmlElement(required=true)
    public String nome;
    @XmlElement(required=true)
    public String destino;
    @XmlElement(required=true)
    public int nQuartos;
    @XmlElement(required=true)
    public int nPessoas;
    
    public Reserva(){}
    public Reserva(String n, String d, int q, int p){
        this.nome=n;
        this.destino=d;
        this.nQuartos=q;
        this.nPessoas=p;
    }
}
