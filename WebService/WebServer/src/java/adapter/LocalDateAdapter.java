/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adapter;

import java.time.LocalDate;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author WILLIAN
 */
public class LocalDateAdapter 
    extends XmlAdapter<String, LocalDate> {
 
    public LocalDate unmarshal(String v) throws Exception {
        return LocalDate.parse(v.split("T")[0]);
    }
 
    public String marshal(LocalDate v) throws Exception {
        return v.toString();
    }
 
}
