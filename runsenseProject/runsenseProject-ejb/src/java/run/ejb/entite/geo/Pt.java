/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.entite.geo;

import run.ejb.entite.geo.interf.RsEntite;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.ClassBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Latitude;
import org.hibernate.search.annotations.Longitude;
import org.hibernate.search.annotations.NumericField;
import org.hibernate.search.annotations.Store;
import run.ejb.entite.util.bdd.FRAnalyzer;
import run.ejb.entite.util.bdd.LgneBridge;
import run.ejb.entite.client.Compte;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "PT", catalog = "")
@XmlRootElement
@Indexed(index="PT")
//@Analyzer(impl=StandardAnalyzer.class)
@ClassBridge(name="FrenchPt", impl = LgneBridge.class, analyzer = @Analyzer(impl = FRAnalyzer.class))
//@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)

public class Pt implements Serializable, RsEntite
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq_point")
    @SequenceGenerator(sequenceName="seq", name="seq_point", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    @NumericField
    @DocumentId
    private Long id;
    
    @Size(max = 2500)
    @Column(name = "ADRESSE", length = 2500)
    @Field(index= Index.YES,store= Store.YES)
    private String adresse;
    
    @Size(max = 5000)
    @Column(name = "DESCRIPTION", length = 5000)
    @Field(index= Index.YES,store= Store.COMPRESS)
    private String description;
    
    @Size(max = 500)
    @Column(name = "NOM", length = 500)
    @Field(index= Index.YES,store= Store.YES)
    private String nom;
    
    @Size(max = 255)
    @Column(name = "TYPE", length = 255)
    private String type;
    
    @Column(name = "LATITUDE", precision = 52, scale = 9)
    @Field(index= Index.YES,store= Store.YES) 
    @Latitude
    private Double latitude;
    
    @Column(name = "LONGITUDE", precision = 52, scale = 9)
    @Field(index= Index.YES,store= Store.YES)
    @Longitude
    private Double longitude;
    
    @Column(name = "ALT", precision = 52, scale = 2)
    @Field(index= Index.YES,store= Store.NO)
    @NumericField
    private Double alt;
    
    @ManyToMany(targetEntity = Compte.class, cascade =CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
   @IndexedEmbedded()
    private Set<Compte> cpte;
    
    @ManyToMany(targetEntity = Evnmt.class, cascade =CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
   @IndexedEmbedded()
    private Set<Evnmt> evnmts;
    
    public Pt() {
    }

    public Pt(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getNom() {
        return nom;
    }

    @Override
    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getAlt() {
        return alt;
    }

    public void setAlt(Double alt) {
        this.alt = alt;
    }

    public Set<Compte> getCpte() {
        return cpte;
    }

    public void setCpte(Set<Compte> cpte) {
        this.cpte = cpte;
    }

    public Set<Evnmt> getEvnmts() {
        return evnmts;
    }

    public void setEvnmts(Set<Evnmt> evnmts) {
        this.evnmts = evnmts;
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
        if (!(object instanceof Pt)) {
            return false;
        }
        Pt other = (Pt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "re.ejb.entite.geo.Pt[ id=" + id + " ]";
    }

    @Override
    public void setEntite(RsEntite entite) throws Exception
    {
        if(entite instanceof Compte)
        {
            cpte.add((Compte)entite);
        }else
            throw new Exception(" que un evenement, un compte ou une coordonnée");
    }

    @Override
    public String getProp() {
       return "point";
    }
    
}
