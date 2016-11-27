/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.entite;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author runsense
 */
@Entity
@Table(name = "hote")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Hote.findAll", query = "SELECT h FROM Hote h"),
    @NamedQuery(name = "Hote.findById", query = "SELECT h FROM Hote h WHERE h.id = :id"),
    @NamedQuery(name = "Hote.findByPassword", query = "SELECT h FROM Hote h WHERE h.password = :password"),
    @NamedQuery(name = "Hote.findByUtilisateur", query = "SELECT h FROM Hote h WHERE h.utilisateur = :utilisateur")})
public class Hote implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id", nullable = false)
    private Integer id;
    @Size(max = 10)
    @Column(name = "password", length = 10)
    private String password;
    @Size(max = 10)
    @Column(name = "utilisateur", length = 10)
    private String utilisateur;

    public Hote() {
    }

    public Hote(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
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
        if (!(object instanceof Hote)) {
            return false;
        }
        Hote other = (Hote) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "runsense.plateformeweb.entite.Hote[ id=" + id + " ]";
    }
    
}
