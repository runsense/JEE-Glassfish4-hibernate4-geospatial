/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.ejbkml;

import java.util.ArrayList;
import javax.ejb.Local;
import run.ejb.iti.entite.util.Itineraire;

/**
 *
 * @author selekta
 */
@Local
public interface GereItiLocal {

    public void vuePortIti(Itineraire itineraire);

    public void traitIti();

    public void setIti(String iti);

    public ArrayList<Itineraire> getLiti();

    public String getTracer();

    public String[] getLatlng();

    public void setItia(String itia);

    public void setItid(String itid);
    
}
