/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.entite.variableRch;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.IndexedEmbedded;
import run.ejb.entite.client.Compte;
import run.ejb.entite.geo.Localite;

/**
 *
 * @author Fujitsu
 */
@Entity
public class RqtRch implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH} )
    @ContainedIn
    private RsnsVar rsnsVar;
    
    @OneToMany( cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy ="requete"  )
    @IndexedEmbedded(targetElement=RqtSimple.class)
    private Set<RqtSimple> strqts;
    
    @OneToMany( cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy ="requete" )
    @IndexedEmbedded(targetElement=RqtMulti.class)
    private Set<RqtMulti> strqtm;

    public RqtRch() {
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RsnsVar getRsnsVar() {
        return rsnsVar;
    }

    public void setRsnsVar(RsnsVar rsnsVar) {
        this.rsnsVar = rsnsVar;
    }

    public Set<RqtSimple> getStrqts() {
        return strqts;
    }

    public void setStrqts(Set<RqtSimple> strqts) {
        this.strqts = strqts;
    }

    public Set<RqtMulti> getStrqtm() {
        return strqtm;
    }

    public void setStrqtm(Set<RqtMulti> strqtm) {
        this.strqtm = strqtm;
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
        if (!(object instanceof RqtRch)) {
            return false;
        }
        RqtRch other = (RqtRch) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "re.ejb.entite.variableRch.RqtRch[ id=" + id + " ]";
    }

    
}
