/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recursos;

import adapter.LocalDateAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author a1717553
 */
@XmlRootElement(name="PassagemAerea")
@XmlAccessorType(XmlAccessType.FIELD)
public class PassagemAerea implements Serializable{
    @XmlElement(required=true)
    public boolean idaVolta;
    @XmlElement(required=true)
    public String origem;
    @XmlElement(required=true)
    public String destino;
    @XmlElement(required=true)
    public String companhiaIda;
    @XmlElement(required=true)
    public String companhiaVolta;
    @XmlElement(required=true)
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    public LocalDate dataIda;
    @XmlElement(required=true)
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    public LocalDate dataVolta;
    @XmlElement(required=true)
    public int nPessoas;
    
    public PassagemAerea(){}
    public PassagemAerea(boolean iv, String o, String d, String ci, String cv, LocalDate di, LocalDate dv, int np){
        this.idaVolta=iv;
        this.origem=o;
        this.destino=d;
        this.companhiaIda=ci;
        this.companhiaVolta=cv;
        this.dataIda=di;
        this.dataVolta=dv;
        this.nPessoas=np;
    }
}
