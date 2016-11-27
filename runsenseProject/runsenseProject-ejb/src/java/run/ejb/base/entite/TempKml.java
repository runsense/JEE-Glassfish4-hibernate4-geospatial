/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.base.entite;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
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
import org.hibernate.search.annotations.NumericField;
import org.hibernate.search.annotations.Store;
import run.ejb.entite.climat.Meteo;
import run.ejb.entite.geo.Ligne;
import run.ejb.entite.geo.Pt;
import run.ejb.entite.util.bdd.FRAnalyzer;
import run.ejb.entite.util.bdd.LgneBridge;


/**
 *
 * @author selekta
 */
@Entity
@Table(name = "TEMPKML", catalog = "")
@XmlRootElement
@Indexed(index="TEMPKML")
@ClassBridge(name="FrenchKml", impl = LgneBridge.class, analyzer = @Analyzer(impl = FRAnalyzer.class))
public class TempKml implements Serializable 
{
     private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq_TempKml")
    @SequenceGenerator(sequenceName="seq", name="seq_TempKml", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    @NumericField
    @DocumentId
    private Long id;
    
    @Size(max=300)
    @Column(name = "RCH", length = 300)
    @Field(index= Index.YES,store= Store.YES)
   private String rch;
    
    @Size(max=300)
    @Column(name = "TC", length = 350)
    @Field(index= Index.YES,store= Store.NO)
   private String tc;
    
   @Size(max=300)
    @Column(name = "TR", length = 350)
    @Field(index= Index.YES,store= Store.NO)
   private String tr;
    
    @Transient
   private List<Long> lids;
   
    @ManyToMany(targetEntity = Ligne.class, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @IndexedEmbedded
   private Set<Ligne> llgn;
    
    @ManyToMany(targetEntity = Pt.class, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @IndexedEmbedded
   private Set<Pt> lpt;
    
    /*@ManyToMany(targetEntity = Meteo.class, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @IndexedEmbedded*/
    @Transient
   private Set<Meteo> lmto;
    

    public TempKml() {
    }

    
    public TempKml(TempKml tmp)
    {
        this.id=tmp.id;
        this.lids=tmp.getLids();
        this.llgn=tmp.getLlgn();
        this.lmto=tmp.getLmto();
        this.lpt=tmp.getLpt();
        this.rch=tmp.getRch();
        this.tc=tmp.getTc();
        this.tr=tmp.getTr();
      

    }

    public TempKml(String tracer, String tour, List<Long> lids, String rch) {
        this.tc = tracer;
        this.tr = tour;
        this.lids=lids;
        this.rch=rch;
       
    }

    public TempKml(String rch, String tc, String tr, Set<Ligne> llgn, Set<Pt> lpt) {
        this.rch = rch;
        this.tc = tc;
        this.tr = tr;
        this.llgn = llgn;
        this.lpt = lpt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRch() {
        return rch;
    }

    public void setRch(String rch) {
        this.rch = rch;
    }

    public Set<Ligne> getLlgn() {
        return llgn;
    }

    public void setLlgn(Set<Ligne> llgn) {
        this.llgn = llgn;
    }

    public Set<Pt> getLpt() {
        return lpt;
    }

    public void setLpt(Set<Pt> lpt) {
        this.lpt = lpt;
    }

    public Set<Meteo> getLmto() {
        return lmto;
    }

    public void setLmto(Set<Meteo> lmto) {
        this.lmto = lmto;
    }


    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        this.tc = tc;
    }

    public String getTr() {
        return tr;
    }

    public void setTr(String tr) {
        this.tr = tr;
    }


    public List<Long> getLids() {
        return lids;
    }

    public void setLids(List<Long> lids) {
        this.lids = lids;
    }


    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    

    @Override
    public String toString() {
        return "re.ejb.base.entite.UserVue[ id=" + id + " ]";
    }
}
