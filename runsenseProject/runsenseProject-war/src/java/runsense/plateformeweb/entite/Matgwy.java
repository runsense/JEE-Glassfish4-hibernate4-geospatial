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
@Table(name = "matgwy")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Matgwy.findAll", query = "SELECT m FROM Matgwy m"),
    @NamedQuery(name = "Matgwy.findByIdgwy", query = "SELECT m FROM Matgwy m WHERE m.idgwy = :idgwy"),
    @NamedQuery(name = "Matgwy.findByDescgwy", query = "SELECT m FROM Matgwy m WHERE m.descgwy = :descgwy")})
public class Matgwy implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idgwy", nullable = false)
    private Integer idgwy;
    @Size(max = 255)
    @Column(name = "descgwy", length = 255)
    private String descgwy;

    public Matgwy() {
    }

    public Matgwy(Integer idgwy) {
        this.idgwy = idgwy;
    }

    public Integer getIdgwy() {
        return idgwy;
    }

    public void setIdgwy(Integer idgwy) {
        this.idgwy = idgwy;
    }

    public String getDescgwy() {
        return descgwy;
    }

    public void setDescgwy(String descgwy) {
        this.descgwy = descgwy;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idgwy != null ? idgwy.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Matgwy)) {
            return false;
        }
        Matgwy other = (Matgwy) object;
        if ((this.idgwy == null && other.idgwy != null) || (this.idgwy != null && !this.idgwy.equals(other.idgwy))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "runsense.plateformeweb.entite.Matgwy[ idgwy=" + idgwy + " ]";
    }
    
}
