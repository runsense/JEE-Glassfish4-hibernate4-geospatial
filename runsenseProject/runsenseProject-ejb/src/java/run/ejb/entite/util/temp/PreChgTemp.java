/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package run.ejb.entite.util.temp;

import java.util.HashSet;
import java.util.Set;
import run.ejb.base.CherchTemp;
import run.ejb.base.entite.TempKml;
import run.ejb.ejbkml.GereKml;
import run.ejb.ejbkml.GereKmlLocal;
import run.ejb.entite.geo.Ligne;
import run.ejb.entite.geo.Pt;
import run.ejb.entite.geo.interf.RsEntite;
import run.ejb.util.rsentite.schema.PrmtrSchm;
import run.ejb.util.traduction.RBint;

/**
 *
 * @author paskal
 */
public class PreChgTemp 
{
    private boolean stic=false;
    private GereKmlLocal gKml;
    private CherchTemp temp;
    private PrmtrSchm schema;
    public PreChgTemp() 
    {
        this.gKml=new GereKml();
       temp=new CherchTemp((GereKml) gKml);  
       
        
    }

    @Override
    protected void finalize() throws Throwable {
        gKml.fEm();
    }
    
    public String[] chgtmp(String notif , int[] categSchem,Object[] varBean)
    {
        gKml.cEm();
        System.err.print("PreChgTemp tempoH()");
              System.out.println(notif);
              
        schema=new PrmtrSchm((GereKml) gKml);
        boolean baf=false;
        String[] strtr=new String[2];
        
        TempKml chchKml = temp.cherche(notif);
       
        varBean[2]=new RBint(null);
        
            System.out.println(schema.PrmtrSchm(categSchem[0], categSchem[1], varBean, true));
                 gKml.createKml(null, null, schema);
                strtr[0]=gKml.getTracer();
                strtr[1]=gKml.geTour();
               
                Set tmpl=new HashSet();
                Set tmpp=new HashSet();
                for(RsEntite rs:schema.getMvue().values())
                    if(rs instanceof Ligne)
                        tmpl.add(rs);
                    else if(rs instanceof Pt)
                        tmpp.add(rs);
          
                chchKml=new TempKml(notif,strtr[0], strtr[1],tmpl,tmpp);
                chchKml.setLmto(schema.getLlpremeteo());
                if(stic)
                {
                    
                    temp.addTmp(chchKml);
                }else
                   temp.addTmp(chchKml);
       
        return strtr;
    }

    public boolean isStic() {
        return stic;
    }

    public void setStic(boolean stic) {
        this.stic = stic;
    }

    
    
}
