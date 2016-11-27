/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.entite.climat;

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
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.ClassBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.NumericField;
import org.hibernate.search.annotations.Store;
import run.ejb.entite.geo.interf.IntBaz;
import run.ejb.entite.util.bdd.FRAnalyzer;
import run.ejb.entite.util.bdd.LgneBridge;

/**
 *
 * @author selekta
 */
@Entity
@Table(name = "METEO", catalog = "")
@XmlRootElement
@Indexed(index="METEO")
@ClassBridge(name="FrenchMeteo", impl = LgneBridge.class, analyzer = @Analyzer(impl = FRAnalyzer.class))
public class Meteo implements IntBaz, Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    @NumericField
    @DocumentId
    private Long id;
    
    @Size(max=30)
    @Column(name = "WOIED", length = 30)
    @Field(index= Index.YES,store= Store.YES)
    private String woied;
    
    @Size(max=15)
    @Column(name = "JOUR", length = 15)
    @Field(index= Index.YES,analyze = Analyze.NO,store= Store.YES)
    @NumericField
    private Long jour;
    
    @Size(max=100)
    @Column(name = "TEMPS", length = 100)
    @Field(index= Index.YES,store= Store.NO)
    private String temps;
    
    @Size(max=150)
    @Column(name = "URLIMG", length = 150)
    @Field(index= Index.NO,store= Store.NO)
    private String urlimg;
    
    @Size(max=2)
    @Column(name = "DEG", length = 2)
    @Field(index= Index.NO,store= Store.NO)
    @NumericField
    private Integer deg;
    
   @ManyToMany(targetEntity =PrevMt.class, cascade =CascadeType.ALL, fetch = FetchType.LAZY ) 
   /* @JoinTable(name="prevmt_meteo",
            joinColumns={@JoinColumn(name="tourPrmtr")} 
            )*/
 
    @Fetch(value = FetchMode.SUBSELECT)
   @IndexedEmbedded
   private Set<PrevMt> lprev;

    public Meteo() {
    }
     
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWoied() {
        return woied;
    }

    public void setWoied(String woied) {
        this.woied = woied;
    }

    public Long getJour() {
        return jour;
    }

    public void setJour(Long jour) {
        this.jour = jour;
    }

    public String getTemps() {
        return temps;
    }

    public void setTemps(String temps) {
        this.temps = temps;
    }

    public Integer getDeg() {
        return deg;
    }

    public void setDeg(Integer deg) {
        this.deg = deg;
    }

    public String getUrlimg() {
        return urlimg;
    }

    public void setUrlimg(String urlimg) {
        this.urlimg = urlimg;
    }

    public Set<PrevMt> getLprev() {
        return lprev;
    }

    public void setLprev(Set<PrevMt> lprev) {
        this.lprev = lprev;
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
        if (!(object instanceof Meteo)) {
            return false;
        }
        Meteo other = (Meteo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "re.ejb.entite.climat.Meteo[ id=" + id + " ]";
    }

    @Override
    public void setNom(String nom) {
        
        woied=nom;
        
    }

    @Override
    public String getNom() {
        return woied;//To change body of generated methods, choose Tools | Templates.
    }
    
}
