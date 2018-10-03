/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recursos;

import enums.EnumTipoInteresse;
import servidor.InterfaceCliente;

/**
 *
 * @author a1717553
 */
public class Interesse {
    public InterfaceCliente cliente;
    public EnumTipoInteresse tipo;
    public int precoMaximo;
    
    public Interesse(InterfaceCliente cli, EnumTipoInteresse t, int p){
        this.cliente=cli;
        this.tipo=t;
        this.precoMaximo=p;
    }
}
