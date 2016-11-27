/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.util.kml.schema;

import java.util.List;
import java.util.Map;
import run.ejb.entite.geo.interf.RsEntite;

/**
 *
 * @author Fujitsu
 */
public class RsnsObj 
{
    private String ref;

    private Ref prmtrVue; 

    private String rftab;
     private List<List<RsEntite>> pourKml;
    public  RsnsObj(String ref, Ref prmtrVue, String rftab) 
    {
        
        
        this.ref = ref;
        this.prmtrVue = prmtrVue;
        this.rftab = rftab;
        
    }

    public Ref RsnsObj(String ref, Ref prmtrVue,String rftab) 
    {
        
        
        this.ref = ref;     
        this.prmtrVue = prmtrVue;
        this.rftab = rftab;
        return prmtrVue;
    }
   
    

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Ref getPrmtrVue() {
        return prmtrVue;
    }

    public void setPrmtrVue(Ref prmtrVue) {
        this.prmtrVue = prmtrVue;
    }

    public List<List<RsEntite>> getPourKml() {
        return pourKml;
    }

    public void setPourKml(List<List<RsEntite>> pourKml) {
        this.pourKml = pourKml;
    }

    public String getRftab() {
        return rftab;
    }

    public void setRftab(String rftab) {
        this.rftab = rftab;
    }


    
}
