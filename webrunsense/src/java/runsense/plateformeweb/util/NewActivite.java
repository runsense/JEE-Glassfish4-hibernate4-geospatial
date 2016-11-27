/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.util;

import java.util.List;
import run.ejb.base.Variable;
import run.ejb.entite.geo.Ligne;
import run.ejb.entite.geo.Pt;

/**
 *
 * @author selekta
 */
public class NewActivite 
{
   private Pt point;
   private Ligne zone;
   private String nom;
   private String tel;
   private String gsm;
   private String act;
   private List<String> acts;
   private String desc;

    public NewActivite() 
    {
        acts=Variable.getPrmtr();
    }
    
    private void construcPt()
    {
        point.setNom(nom);
        point.setDescription(Variable.getPrmtr().get(5)+":");
        for(String str:acts)
            point.setDescription(point.getDescription()+" "+
                    str+", ");
        point.setDescription(point.getDescription()+" "+
                    "tel "+tel+" gsm "+gsm+" "+desc);
    }
    public Pt getPoint() {
        construcPt();
        return point;
    }

    public void setPoint(Pt point) {
        this.point = point;
    }

    public Ligne getZone() {
        return zone;
    }

    public void setZone(Ligne zone) {
        this.zone = zone;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getGsm() {
        return gsm;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public List<String> getActs() {
        return acts;
    }

    public void setActs(List<String> acts) {
        this.acts = acts;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    
   
}
