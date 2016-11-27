/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.entite.geo;

import run.ejb.entite.geo.interf.RsEntite;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.ClassBridge;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;
import run.ejb.entite.client.Compte;
import run.ejb.entite.util.bdd.FRAnalyzer;
import run.ejb.entite.util.bdd.LgneBridge;

/**
 *
 * @author runsense
 */
@Entity
@Table(name = "EVNMT", catalog = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Evnmt.findAll", query = "SELECT e FROM Evnmt e")})

@Indexed(index="EVNMT")
@ClassBridge(name="FrenchLig", impl = LgneBridge.class, analyzer = @Analyzer(impl = FRAnalyzer.class))
public class Evnmt implements Serializable, RsEntite {
     private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq_Evnmt")
    @SequenceGenerator(sequenceName="seq", name="seq_Evnmt", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "idEvnmt", nullable = false)
    @DocumentId
    private Long idEvnmt;
    
    @Size(max = 500)
    @Column(name = "NOM", length = 500)
    @Field(index= Index.YES,store= Store.YES)
    private String nom;
    
     //date
    @Temporal(javax.persistence.TemporalType.DATE)
    @Field(index= Index.YES,store= Store.YES)
    @DateBridge(resolution=Resolution.MINUTE) 
    private Date dt;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @Field(index= Index.YES,store= Store.YES)
    @DateBridge(resolution=Resolution.MINUTE) 
    private Date fin;
    
    @Size(max = 50)
    @Column(name = "CATEG", length = 50)
    @Field(index= Index.YES,store= Store.YES,analyze = Analyze.NO)
    private String categ;
  
    @Size(max = 500)
    @Column(name = "ADRESSE", length = 500)
    @Field(index= Index.YES,store= Store.YES)
    private String adresse;
        
    @Size(max = 5000)
    @Column(name = "DESCRIPTION", length = 5000)
    @Field(index= Index.YES,store= Store.YES)
    private String description;
    
    @Size(max = 5000)
    @Column(name = "LIEUX", length = 5000)
    @Field(index= Index.YES,store= Store.YES)
    private String lieux;
    
    @ManyToOne()
    @JoinColumn(name="evnmtcpte")
     @ContainedIn
    private Compte cptevnmt;
    
    @ManyToMany(mappedBy="evnmts",targetEntity = Pt.class, cascade =CascadeType.ALL )
    @ContainedIn
    private Set<Pt> point;
    public Evnmt() {
    }

    public Evnmt(String nom, Date dt, String adresse, String description, String lieux) {
        this.nom = nom;
        this.dt=dt;
        this.adresse = adresse;
        this.description = description;
        this.lieux = lieux;
    }
    
    public Evnmt(String nom, Date dt, Date fin, String adresse, String categ) {
        this.nom = nom;
        this.dt=dt;
        this.fin=fin;
        this.adresse = adresse;
        this.categ = categ;
        
    }
    
    public Evnmt( String nom, Date dt, String adresse, String categ) {
        
        this.nom = nom;
        this.dt=dt;
        this.adresse = adresse;
        this.categ = categ;
       
    }

    public Long getIdEvnmt() {
        return idEvnmt;
    }

    public void setIdEvnmt(Long idEvnmt) {
        this.idEvnmt = idEvnmt;
    }

     @Override
    public String getNom() {
        return nom;
    }

     @Override
    public void setNom(String nom) {
        this.nom = nom;
    }


     @Override
    public String getDescription() {
        return description;
    }

     @Override
    public void setDescription(String description) {
        this.description = description;
    }

    public String getCateg() {
        return categ;
    }

    public void setCateg(String categ) {
        this.categ = categ;
    }

    public String getLieux() {
        return lieux;
    }

    public void setLieux(String lieux) {
        this.lieux = lieux;
    }

    @Override
    public void setId(Long id) {
        setIdEvnmt(id); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long getId() {
      return getIdEvnmt();
    }

     @Override
    public String getAdresse() {
        return adresse;
    }

     @Override
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }


    public Compte getCptevnmt() {
        return cptevnmt;
    }

    public void setCptevnmt(Compte cptevnmt) {
        this.cptevnmt = cptevnmt;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public Set<Pt> getPoint() {
        return point;
    }

    public void setPoint(Set<Pt> point) {
        this.point = point;
    }

    @Override
    public void setEntite(RsEntite entite) throws Exception{
        
    }

    @Override
    public String getProp() {
        return "Evenement";
    }
    
}
