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
public class PassagemAerea implements Serializable{
    public boolean idaVolta;
    public String origem;
    public String destino;
    public String companhiaIda;
    public String companhiaVolta;
    public LocalDate dataIda;
    public LocalDate dataVolta;
    public int nPessoas;
    
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
