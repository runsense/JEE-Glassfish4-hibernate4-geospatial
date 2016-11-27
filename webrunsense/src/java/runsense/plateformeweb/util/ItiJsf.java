/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package runsense.plateformeweb.util;

import java.io.Serializable;
import run.ejb.entite.geo.interf.RsEntite;
import run.ejb.iti.entite.util.Itineraire;

/**
 *
 * @author paskal
 */
public class ItiJsf implements Serializable{
    private String dep;
    private String arv;
    private String trt;
    private String iti;
    private RsEntite rs;
    public ItiJsf() {
    }

    public ItiJsf(String dep, String arv) {
        this.dep = dep;
        this.arv = arv;
    }
    
    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        
        this.dep = dep.replace("é", "e");
    }

    public String getArv() {
        return arv;
    }

    public void setArv(String arv) {
        this.arv = arv.replace("é", "e");
    }

    public String getTrt() {
        return trt;
    }

    public void setTrt(String trt) {
        this.trt = trt;
    }

    public String getIti() {
        return iti;
    }

    public void setIti(String iti) {
        this.iti = iti;
    }

    public RsEntite getRs() {
        return rs;
    }

    public void setRs(RsEntite rs) {
        this.rs = rs;
    }

    
}
