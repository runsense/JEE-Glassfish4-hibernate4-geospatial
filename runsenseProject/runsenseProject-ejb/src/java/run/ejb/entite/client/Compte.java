/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.entite.client;

import run.ejb.entite.geo.Pt;

import run.ejb.entite.geo.Ligne;
import run.ejb.entite.geo.Tour;
import run.ejb.entite.geo.Evnmt;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
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
import org.hibernate.search.annotations.Store;
import run.ejb.entite.geo.Coord;
import run.ejb.entite.geo.interf.IntBaz;
import run.ejb.entite.util.bdd.CpteBridge;
import run.ejb.entite.util.bdd.FRAnalyzer;

/**
 *
 * @author runsense
 */
@Entity
@Table(name = "COMPTE")
@XmlRootElement
@Indexed(index="COMPTE")
@ClassBridge(name="FrenchCpte", impl = CpteBridge.class, analyzer = @Analyzer(impl = FRAnalyzer.class))
public class Compte implements Serializable, IntBaz
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq_cpte")
    @SequenceGenerator(sequenceName="seq", name="seq_cpte", allocationSize=1)
    @Column(name = "IDCPTE", nullable = false)
    @DocumentId
    private Long idcpte;

    @Size(max = 255)
    @Column(name = "NOM", length = 255)
    @Field(index= Index.YES,store= Store.YES)
    private String nom;
    
    @Size(max = 10)
    @Column(name = "PASSWORD", length = 10)
    @Field(index= Index.YES,store= Store.YES)
    private String password;
    
    
    @OneToMany( targetEntity = Contact.class, cascade =CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)/*cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH} )
    @JoinTable(name="liensCpteContact",
            joinColumns={@JoinColumn(name="cptectc")}, 
            inverseJoinColumns={@JoinColumn(name="contact")})*/
    @IndexedEmbedded()
    private Set<Contact> contact;
    
    @ManyToMany(targetEntity = Ligne.class, cascade =CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT) /*cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH} )
    @JoinTable(name="liensCpteLigne",
            joinColumns={@JoinColumn(name="cptligne")}, 
            inverseJoinColumns={@JoinColumn(name="lgncpte")})*/
     @IndexedEmbedded()
    private Set<Ligne> lgncpte;
    
    @ManyToMany(targetEntity = Tour.class, cascade =CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)/* cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH} )
    @JoinTable(name="liensCpteTour",
            joinColumns={@JoinColumn(name="cptTour")}, 
            inverseJoinColumns={@JoinColumn(name="cptetour")})*/
    @IndexedEmbedded()
    private Set<Tour> cptetour;
    
   
    /*@ManyToMany( cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH} )
    @JoinTable(name="liensCptePoint",
            joinColumns={@JoinColumn(name="cpte")}, 
            inverseJoinColumns={@JoinColumn(name="point")}) 
     @ContainedIn*/
     @ManyToMany(mappedBy="cpte",targetEntity = Pt.class, cascade =CascadeType.ALL )
    @ContainedIn
    private Set<Pt> point;
    
    /*@OneToMany( cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH} )
    @JoinTable(name="evnmt_compte",
            joinColumns={@JoinColumn(name="cptevnmt")}, 
            inverseJoinColumns={@JoinColumn(name="evnmtcpte")})  
   @ContainedIn*/
    @ManyToMany(targetEntity = Evnmt.class, cascade =CascadeType.ALL, fetch = FetchType.EAGER)//
    @Fetch(value = FetchMode.SUBSELECT)
    @IndexedEmbedded
    private Set<Evnmt> evnmtcpte;
    
    /*@OneToMany( cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH} )
    @JoinTable(name="musik_compte",
            joinColumns={@JoinColumn(name="cptemusik")}, 
            inverseJoinColumns={@JoinColumn(name="musikcpte")})  
   @ContainedIn*/

    
    public Compte() {
    }

    
    public Long getIdcpte() {
        return idcpte;
    }

    public void setIdcpte(Long idcpte) {
        this.idcpte = idcpte;
    }

    @Override
    public String getNom() {
        return nom;
    }

    @Override
    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Contact> getContact() {
        return contact;
    }

    public void setContact(Set<Contact> contact) {
        this.contact = contact;
    }

    public Set<Ligne> getLgncpte() {
        return lgncpte;
    }

    public void setLgncpte(Set<Ligne> lgncpte) {
        this.lgncpte = lgncpte;
    }

    public Set<Tour> getCptetour() {
        return cptetour;
    }

    public void setCptetour(Set<Tour> cptetour) {
        this.cptetour = cptetour;
    }

    public Set<Pt> getPoint() {
        return point;
    }

    public void setPoint(Set<Pt> point) {
        this.point = point;
    }

    public Set<Evnmt> getEvnmtcpte() {
        return evnmtcpte;
    }

    public void setEvnmtcpte(Set<Evnmt> evnmtcpte) {
        this.evnmtcpte = evnmtcpte;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcpte != null ? idcpte.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Compte)) {
            return false;
        }
        Compte other = (Compte) object;
        if ((this.idcpte == null && other.idcpte != null) || (this.idcpte != null && !this.idcpte.equals(other.idcpte))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "runsense.plateformeweb.entite.Hote[ id=" + idcpte + " ]";
    }

    @Override
    public Long getId() {
       if(idcpte!=null) return idcpte;
       else return null;
    }
}
