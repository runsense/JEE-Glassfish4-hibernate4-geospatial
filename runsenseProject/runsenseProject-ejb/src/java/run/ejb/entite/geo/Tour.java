/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.entite.geo;

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
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;
import run.ejb.entite.client.Compte;
import run.ejb.entite.geo.interf.IntBaz;
import run.ejb.entite.util.bdd.FRAnalyzer;
import run.ejb.entite.util.bdd.LgneBridge;

/**
 *
 * @author runsense
 */
@Entity
@Table(name = "TOUR", catalog = "")//,indexes = {@javax.persistence.Index(columnList="IDTOUR"),@javax.persistence.Index(columnList="IDLGN"), @javax.persistence.Index(columnList="IDCPTE"),@javax.persistence.Index(columnList="IDPRMTR")}                                          
@XmlRootElement
@Indexed(index="TOUR")
@ClassBridge(name="FrenchTour", impl = LgneBridge.class, analyzer = @Analyzer(impl = FRAnalyzer.class))
public class Tour implements Serializable, IntBaz
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq_tour")
    @SequenceGenerator(sequenceName="seq", name="seq_tour", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "IDTOUR", nullable = false)
    @DocumentId
    private Long idTour;
    
    //adresse du RsEntite
    @Size(max = 1500)
    @Column(name = "NMTOUR", length = 1500)
    @Field(index= Index.YES,store= Store.YES)
    private String nmTour;
    
    @Size(max = 150)
    @Column(name = "TYPE", length = 150)
    private String type;
    
    /*@ManyToMany( cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH} ) 
    @JoinTable(name="tour_prmtr",
            joinColumns={@JoinColumn(name="tourPrmtr")} 
            ) 
    @IndexedEmbedded*/
    @ManyToMany(targetEntity = Prmtr.class, cascade =CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @IndexedEmbedded
    private List<Prmtr> parametreTour;
 
    @ManyToMany(mappedBy="cptetour", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH} )
    @ContainedIn
    private Set<Compte> cptTour;

    public Tour() {
    }

   

    public Tour(String nmTour, List<Prmtr> parametreTour, String type, Set<Compte> cptTour) {
        this.nmTour = nmTour;
        this.parametreTour = parametreTour;
        this.type = type;
        this.cptTour = cptTour;
    }


    public Long getIdTour() {
        return idTour;
    }

    public void setIdTour(Long idTour) {
        this.idTour = idTour;
    }

    public String getNmTour() {
        return nmTour;
    }

    public void setNmTour(String nmTour) {
        this.nmTour = nmTour;
    }

  

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Prmtr> getParametreTour() {
        return parametreTour;
    }

    public void setParametreTour(List<Prmtr> parametreTour) {
        this.parametreTour = parametreTour;
    }

    public Set<Compte> getCptTour() {
        return cptTour;
    }

    public void setCptTour(Set<Compte> cptTour) {
        this.cptTour = cptTour;
    }

   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTour!= null ? idTour.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tour)) {
            return false;
        }
        Tour other = (Tour) object;
        if ((this.idTour == null && other.idTour != null) || (this.idTour != null && !this.idTour.equals(other.idTour))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "runsense.plateformeweb.entite.Hote[ id=" + idTour + " ]";
    }

    @Override
    public void setNom(String nom) {
        this.nmTour=nom;
    }

    @Override
    public String getNom() {
        return nmTour;
    }

    @Override
    public Long getId() {
       if(idTour!=null) return idTour;
       else return null;
    }

 
}
