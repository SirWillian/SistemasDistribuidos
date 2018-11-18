/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recursos;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import adapter.*;

/**
 *
 * @author a1717553
 */

@XmlRootElement(name="Voo")
@XmlAccessorType(XmlAccessType.FIELD)
public class Voo implements Serializable{
    //private static final long serialVersionUID = 1L;
    
    @XmlElement(required=true)
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    public LocalDate data;
    @XmlElement(required=true)
    public String origem;
    @XmlElement(required=true)
    public String destino;
    @XmlElement(required=true)
    public String companhia;
    @XmlElement(required=true)
    public int preco;
    @XmlElement(required=true)
    public int assentosVagos;
    
    public Voo(){}
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