/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recursos;

import java.time.LocalDate;

/**
 *
 * @author a1717553
 */
public class PassagemAerea {
    public boolean idaVolta;
    public String origem;
    public String destino;
    public LocalDate dataIda;
    public LocalDate dataVolta;
    public int nPessoas;
    
    public PassagemAerea(boolean iv, String o, String d, LocalDate di, LocalDate dv, int np){
        this.idaVolta=iv;
        this.origem=o;
        this.destino=d;
        this.dataIda=di;
        this.dataVolta=dv;
        this.nPessoas=np;
    }
}
