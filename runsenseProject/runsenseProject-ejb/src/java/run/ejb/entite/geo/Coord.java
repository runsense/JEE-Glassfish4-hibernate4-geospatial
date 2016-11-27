/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.entite.geo;




import run.ejb.entite.geo.interf.RsEntite;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.ClassBridge;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Latitude;
import org.hibernate.search.annotations.Longitude;
import org.hibernate.search.annotations.NumericField;
import org.hibernate.search.annotations.Store;
import run.ejb.entite.util.bdd.CoAnalyzer;
import run.ejb.entite.util.bdd.CordBridge;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "COORD", catalog = "")//,indexes = {@javax.persistence.Index(columnList="IDCOORD"),@javax.persistence.Index(columnList="IDLGN")}
                                                
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Coord.findAll", query = "SELECT c FROM Coord c")})

//@Spatial(name="location", spatialMode = SpatialMode.RANGE)
@Indexed(index="COORD")
@ClassBridge(name="Space", impl = CordBridge.class, analyzer = @Analyzer(impl = CoAnalyzer.class))
public class Coord  implements Serializable, org.hibernate.search.spatial.Coordinates, RsEntite
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq_coord")
    @SequenceGenerator(sequenceName="seq", name="seq_coord", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "IDCOORD", nullable = false)
    @DocumentId
    private Long idcoord;
    
    @Size(max = 500)
    @Column(name = "ADRESSE", length = 500)
    private String adresse;
   
    @Size(max = 255)
    @Column(name = "DESCRIPTION", length = 255)
    //@Field(index= Index.YES,store= Store.COMPRESS)
    private String description;
    
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
    
    @Size(max = 255)
    @Column(name = "NOM", length = 255)
   // @Field(index= Index.NO,store= Store.NO)
    //@Analyzer(impl=StandardAnalyzer.class)  
    private String nom;
  

   /* @ManyToMany(mappedBy="coords" )
    @IndexedEmbedded(targetElement=Ligne.class)*/
    @ManyToOne()
    @JoinColumn(name="coords")
     @ContainedIn
    private Ligne ligne;
   
   
    public Coord() {
    }

    public Coord(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    
    public Coord(Long idcoord) {
        this.idcoord = idcoord;
    }

    @Override
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
  
    @Override
    public Long getId() {
        return idcoord;
    }

    @Override
    public void setId(Long id) {
        this.idcoord = id;
    }

    @Override
    public String getAdresse() {
        return adresse;
    }

    @Override
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    
    public Long getIdcoord() {
        return idcoord;
    }

    public void setIdcoord(Long idcoord) {
        this.idcoord = idcoord;
    }


    
    public Double getAlt() {
        return alt;
    }

    public void setAlt(Double alt) {
        this.alt = alt;
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

    public void setLigne(Ligne ligne) {
        this.ligne = ligne;
    }

    public Ligne getLigne() {
        return ligne;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcoord != null ? idcoord.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Coord)) {
            return false;
        }
        Coord other = (Coord) object;
        if ((this.idcoord == null && other.idcoord != null) || (this.idcoord != null && !this.idcoord.equals(other.idcoord))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "re.ejb.entite.geo.Coord[ id=" + idcoord + " ]";
    }

    @Override
    public void setEntite(RsEntite entite) {
        
    }

    @Override
    public String getProp() {
        return "Coordonnée";
    }


    
}
