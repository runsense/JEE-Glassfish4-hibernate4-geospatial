/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package runsense.plateformeweb.util;

import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author paskal
 */
public class ImgLien implements Serializable
{
    
 private String nom;
 private String liens;

 private static final List<String> sllien=Lists.newArrayList("partly cloudy","showers","sunny","thunderstorm");


    public ImgLien(String nom) {
        this.nom = nom;
        this.liens="";
    }
 
    public ImgLien(String nom, final String liens) {
        
        this.nom = nom;
       String lien=liens;
      if(liens!=null)
        for(String str:sllien)
            if(liens.contains(str))
                lien=str;
        this.liens=lien;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLiens() {
        return liens;
    }

    public void setLiens(String liens) {
        this.liens = liens;
    }
 
}
