/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.entite;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author runsense
 */
@Entity
@Table(name = "identifiant")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Identifiant.findAll", query = "SELECT i FROM Identifiant i"),
    @NamedQuery(name = "Identifiant.findByIdhote", query = "SELECT i FROM Identifiant i WHERE i.idhote = :idhote"),
    @NamedQuery(name = "Identifiant.findByIdcap", query = "SELECT i FROM Identifiant i WHERE i.idcap = :idcap"),
    @NamedQuery(name = "Identifiant.findByIdfornsr", query = "SELECT i FROM Identifiant i WHERE i.idfornsr = :idfornsr"),
    @NamedQuery(name = "Identifiant.findByIdgwy", query = "SELECT i FROM Identifiant i WHERE i.idgwy = :idgwy")})
public class Identifiant implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idhote")
    private Integer idhote;
    @Column(name = "idcap")
    private Integer idcap;
    @Column(name = "idfornsr")
    private Integer idfornsr;
    @Column(name = "idgwy")
    private Integer idgwy;

    public Identifiant() {
    }

    public Identifiant(Integer idhote) {
        this.idhote = idhote;
    }

    public Integer getIdhote() {
        return idhote;
    }

    public void setIdhote(Integer idhote) {
        this.idhote = idhote;
    }

    public Integer getIdcap() {
        return idcap;
    }

    public void setIdcap(Integer idcap) {
        this.idcap = idcap;
    }

    public Integer getIdfornsr() {
        return idfornsr;
    }

    public void setIdfornsr(Integer idfornsr) {
        this.idfornsr = idfornsr;
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
        hash += (idhote != null ? idhote.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Identifiant)) {
            return false;
        }
        Identifiant other = (Identifiant) object;
        if ((this.idhote == null && other.idhote != null) || (this.idhote != null && !this.idhote.equals(other.idhote))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "runsense.plateformeweb.entite.Identifiant[ idhote=" + idhote + " ]";
    }
    
}
