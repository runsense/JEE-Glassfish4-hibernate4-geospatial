/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.entite.variableRch;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

/**
 *
 * @author Fujitsu
 */
@Entity
public class RqtMulti implements Serializable, IntRqt {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 1000)
    @Column(name = "RCH", length = 1000)
    @Field(index= Index.YES,store= Store.COMPRESS)
    private String rch;
    
    @Size(max = 1000)
    @Column(name = "RSENTITE", length = 1000)
    @Field(index= Index.YES,store= Store.COMPRESS)
    private String rsentite;
 
    @Size(max = 1000)
    @Column(name = "FIELDS", length = 1000)
    @Field(index= Index.YES,store= Store.COMPRESS)
    private String fields;
    
    @Size(max = 1000)
    @Column(name = "PRECS", length = 1000)
    @Field(index= Index.YES,store= Store.COMPRESS)
    private String precs;
    
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH} )
    @ContainedIn
    private RqtRch requete;
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getRch() {
        return rch;
    }

    public void setRch(String rch) {
        this.rch = rch;
    }

    public String getRsentite() {
        return rsentite;
    }

    public void setRsentite(String rsentite) {
        this.rsentite = rsentite;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getPrecs() {
        return precs;
    }

    public void setPrecs(String precs) {
        this.precs = precs;
    }

    public RqtRch getRequete() {
        return requete;
    }

    public void setRequete(RqtRch requete) {
        this.requete = requete;
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
        if (!(object instanceof RqtMulti)) {
            return false;
        }
        RqtMulti other = (RqtMulti) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "re.ejb.entite.variableRch.RqtMulti[ id=" + id + " ]";
    }
    
}
