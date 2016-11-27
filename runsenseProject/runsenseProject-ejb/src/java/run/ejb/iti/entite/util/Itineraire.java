/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.iti.entite.util;

import java.util.ArrayList;
import run.ejb.entite.geo.Coord;

/**
 *
 * @author runsense
 */
public class Itineraire 
{
        Coord dloc;
        Coord floc;
        String dure;
        String inform;
        String distance;

    public Itineraire() {
    }

    public Itineraire(Coord dloc, Coord floc, String dure, String inform, String distance) {
        this.dloc = dloc;
        this.floc = floc;
        this.dure = dure;
        this.inform = inform;
        this.distance = distance;
    }

    public Coord getDloc() {
        return dloc;
    }

    public void setDloc(Coord dloc) {
        this.dloc = dloc;
    }

    public Coord getFloc() {
        return floc;
    }

    public void setFloc(Coord floc) {
        this.floc = floc;
    }

    public String getDure() {
        return dure;
    }

    public void setDure(String dure) {
        this.dure = dure;
    }

    public String getInform() {
        return inform;
    }

    public void setInform(String inform) {
        this.inform = inform;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
        
    
}
