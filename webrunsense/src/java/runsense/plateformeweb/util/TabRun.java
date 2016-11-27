/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.util;

/**
 *
 * @author runsense
 */
public class TabRun 
{
    private long id;
    private String nom;
    private String categ;
    private String lieu;
    private String icone;

    public TabRun() {
    }

    public TabRun(String nom, String categ, String lieu) {
        this.nom = nom;
        this.categ = categ;
        this.lieu = lieu;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCateg() {
        return categ;
    }

    public void setCateg(String categ) {
        this.categ = categ;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }
    
    
}
