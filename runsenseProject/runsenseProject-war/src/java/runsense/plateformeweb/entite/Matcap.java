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
@Table(name = "matcap")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Matcap.findAll", query = "SELECT m FROM Matcap m"),
    @NamedQuery(name = "Matcap.findByIdcap", query = "SELECT m FROM Matcap m WHERE m.idcap = :idcap"),
    @NamedQuery(name = "Matcap.findByDesccap", query = "SELECT m FROM Matcap m WHERE m.desccap = :desccap"),
    @NamedQuery(name = "Matcap.findByIdgwy", query = "SELECT m FROM Matcap m WHERE m.idgwy = :idgwy")})
public class Matcap implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idcap", nullable = false)
    private Integer idcap;
    @Size(max = 255)
    @Column(name = "desccap", length = 255)
    private String desccap;
    @Column(name = "idgwy")
    private Integer idgwy;

    public Matcap() {
    }

    public Matcap(Integer idcap) {
        this.idcap = idcap;
    }

    public Integer getIdcap() {
        return idcap;
    }

    public void setIdcap(Integer idcap) {
        this.idcap = idcap;
    }

    public String getDesccap() {
        return desccap;
    }

    public void setDesccap(String desccap) {
        this.desccap = desccap;
    }

    public Integer getIdgwy() {
        return idgwy;
    }

    public void setIdgwy(Integer idgwy) {
        this.idgwy = idgwy;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcap != null ? idcap.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Matcap)) {
            return false;
        }
        Matcap other = (Matcap) object;
        if ((this.idcap == null && other.idcap != null) || (this.idcap != null && !this.idcap.equals(other.idcap))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "runsense.plateformeweb.entite.Matcap[ idcap=" + idcap + " ]";
    }
    
}
