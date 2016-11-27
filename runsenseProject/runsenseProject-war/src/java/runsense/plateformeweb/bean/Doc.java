/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.bean;

import de.micromata.opengis.kml.v_2_2_0.Feature;

/**
 *
 * @author Administrateur
 */
public class Doc
{
    private String nom;
    private String parent;
    private String objet;
    private Feature feature;

    public Doc(String nom, String parent, String objet, Feature feature) {
        this.nom = nom;
        this.parent = parent;
        this.objet = objet;
        this.feature = feature;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }
    
    
    
}
