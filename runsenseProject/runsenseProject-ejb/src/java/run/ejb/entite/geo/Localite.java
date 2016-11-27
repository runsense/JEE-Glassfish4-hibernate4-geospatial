/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.entite.geo;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.ClassBridge;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;
import run.ejb.entite.util.bdd.FRAnalyzer;
import run.ejb.entite.util.bdd.LgneBridge;
import run.ejb.entite.geo.interf.IntBaz;

/**
 *
 * @author Fujitsu
 */
@Entity
@Table(name = "LOCALITE", catalog = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Localite.findAll", query = "SELECT l FROM Ligne l")})
@Indexed(index="LOCALITE")
@ClassBridge(name="FrenchLig", impl = LgneBridge.class, analyzer = @Analyzer(impl = FRAnalyzer.class))
public class Localite implements Serializable, IntBaz {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Size(max = 500)
    @Column(name = "NOM", length = 500)
    @Field(index= Index.YES,store= Store.YES)
    private String nom;
    
    @Size(max = 15)
    @Column(name = "WOIED", length = 15)
    @Field(index= Index.YES,store= Store.YES)
    private String woied;
    
    @ManyToOne(targetEntity = Localite.class)
    @IndexedEmbedded(depth = 1)
    private Localite parent;
    
    @OneToMany(mappedBy = "parent"  )
    @ContainedIn
    private Set<Localite> enfant;

    public Localite() {
    }

    public Localite(String nom, Localite parent) {
        this.nom = nom;
        this.parent = parent;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getNom() {
        return nom;
    }

    @Override
    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getWoied() {
        return woied;
    }

    public void setWoied(String woied) {
        this.woied = woied;
    }

    public Localite getParent() {
        return parent;
    }

    public void setParent(Localite parent) {
        this.parent = parent;
    }

    public Set<Localite> getEnfant() {
        return enfant;
    }

    public void setEnfant(Set<Localite> enfant) {
        this.enfant = enfant;
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
        if (!(object instanceof Localite)) {
            return false;
        }
        Localite other = (Localite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "re.ejb.entite.geo.Localite[ id=" + id + " ]";
    }
    
}
