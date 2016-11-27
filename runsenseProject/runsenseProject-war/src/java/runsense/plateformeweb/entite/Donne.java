/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.entite;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author runsense
 */
@Entity
@Table(name = "donne")
@XmlRootElement
public class Donne implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "idCap", nullable = false)
    private Integer idCap;
    
    @Column(name = "valeur", nullable = false)
    private Double valeur;
    
    @Column(name = "methode", nullable = false)
    private char[] methode;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "heure", nullable = false)
    private Date heure;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getHeure() {
        return heure;
    }

    public void setHeure(Date heure) {
        this.heure = heure;
    }

    public Integer getIdCap() {
        return idCap;
    }

    public void setIdCap(Integer idCap) {
        this.idCap = idCap;
    }

    public char[] getMethode() {
        return methode;
    }

    public void setMethode(char[] methode) {
        this.methode = methode;
    }

    public Double getValeur() {
        return valeur;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Donne)) {
            return false;
        }
        Donne other = (Donne) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "runsense.ejb.entite.Donne[ id=" + id + " ]";
    }
    
}
