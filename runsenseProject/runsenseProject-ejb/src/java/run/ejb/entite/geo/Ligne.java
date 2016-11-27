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
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.ClassBridge;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
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
@Table(name = "LIGNE", catalog = "" )//,indexes = {@javax.persistence.Index(columnList="IDLGN"),@javax.persistence.Index(columnList="IDCOORD"),@javax.persistence.Index(columnList="IDCPTE")}
@XmlRootElement
@Indexed(index="LIGNE")
@ClassBridge(name="FrenchLig", impl = LgneBridge.class, analyzer = @Analyzer(impl = FRAnalyzer.class))
public class Ligne implements Serializable, RsEntite
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq_ligne")
    @SequenceGenerator(sequenceName="seq", name="seq_ligne", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "IDLGN", nullable = false)
    @NumericField
    @DocumentId
    private Long idlgn;
    
     @Size(max = 500)
    @Column(name = "NOM", length = 500)
    @Field(index= Index.YES,store= Store.YES)
    private String nom;
     
    @Size(max = 2500)
    @Column(name = "ADRESSE", length = 2500)
    @Field(index= Index.YES,store= Store.YES)
    private String adresse;
    
    @Size(max = 5000)
    @Column(name = "DESCRIPTION", length = 5000)
    @Field(index= Index.YES,store= Store.COMPRESS)
    private String description;
    
   
    
    @Size(max = 255)
    @Column(name = "TYPE", length = 255)
    private String type;

    
    
    /*@ManyToMany( cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH} ) 
    @OrderBy
    @JoinTable(name="ligne_Coord",
            joinColumns={@JoinColumn(name="lignes")})  */
    //@ContainedIn
    @ManyToMany(targetEntity = Coord.class, cascade =CascadeType.ALL, fetch = FetchType.EAGER)//
    @Fetch(value = FetchMode.SUBSELECT)
    @OrderBy
    @IndexedEmbedded
    private List<Coord> coords;
     
    @ManyToMany(mappedBy="lgncpte", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH} )
    @ContainedIn
    private Set<Compte> cptligne;

    
    public Ligne() {
    }

    public Ligne(Long idlgn, String adresse, String description, String nom, String type, List<Coord> coords, Set<Compte> cptligne) {
        this.idlgn = idlgn;
        this.adresse = adresse;
        this.description = description;
        this.nom = nom;
        this.type = type;
     
        this.coords = coords;
        this.cptligne = cptligne;
    }

 

    public Ligne(Long id) {
        this.idlgn = id;
    }

    @Override
    public Long getId() {
        return idlgn;
    }

    @Override
    public void setId(Long id) {
        this.idlgn = id;
    }

    public Long getIdlgn() {
        return idlgn;
    }

    public void setIdlgn(Long idlgn) {
        this.idlgn = idlgn;
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

    public List<Coord> getCoords() {
        return coords;
    }

    public void setCoords(List<Coord> coords) {
        this.coords = coords;
    }

    public Set<Compte> getCptligne() {
        return cptligne;
    }

    public void setCptligne(Set<Compte> cptligne) {
        this.cptligne = cptligne;
    }

   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idlgn != null ? idlgn.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ligne)) {
            return false;
        }
        Ligne other = (Ligne) object;
        if ((this.idlgn == null && other.idlgn != null) || (this.idlgn != null && !this.idlgn.equals(other.idlgn))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "re.ejb.entite.geo.Ligne[ id=" + idlgn + " ]";
    }

    @Override
    public void setEntite(RsEntite entite) throws Exception
    {
        if(entite instanceof Ligne)
        {
            idlgn = entite.getId();
        }else if(entite instanceof Compte)
        {
            cptligne.add((Compte) entite);
        }else if(entite instanceof Coord)
        {
            coords.add((Coord) entite);
        }else
            throw new Exception(" que des tours, une ligne, des comptes ou des coordonnées");
    }

    @Override
    public String getProp() {
        return "tracer";
    }
    
    
}
