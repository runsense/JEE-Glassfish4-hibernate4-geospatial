/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.entite.variableRch;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

/**
 *
 * @author Fujitsu
 */
@Entity
public class RsnsVar implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 500)
    @Column(name = "LIEU", length = 500)
    @Field(index= Index.YES,store= Store.YES)
    private String lieu;
    
    @Size(max = 1000)
    @Column(name = "MANIERE", length = 1000)
    @Field(index= Index.YES,store= Store.COMPRESS)
    private String maniere;
    
    @OneToMany( cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy ="rsnsVar" )
    @IndexedEmbedded(targetElement=RqtRch.class)
    private Set<RqtRch> rqts;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getManiere() {
        return maniere;
    }

    public void setManiere(String maniere) {
        this.maniere = maniere;
    }

    public Set<RqtRch> getRqts() {
        return rqts;
    }

    public void setRqts(Set<RqtRch> rqts) {
        this.rqts = rqts;
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
        if (!(object instanceof RsnsVar)) {
            return false;
        }
        RsnsVar other = (RsnsVar) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "re.entite.Variable[ id=" + id + " ]";
    }
    
}
