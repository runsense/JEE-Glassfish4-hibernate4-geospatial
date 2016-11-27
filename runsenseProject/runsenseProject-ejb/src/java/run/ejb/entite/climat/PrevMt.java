/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.entite.climat;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "PREVMT", catalog = "")
@XmlRootElement
@Indexed(index="PREVMT")
@ClassBridge(name="FrenchPrevMt", impl = LgneBridge.class, analyzer = @Analyzer(impl = FRAnalyzer.class))
public class PrevMt implements IntBaz, Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
     @Column(name = "ID", nullable = false)
    @NumericField
    @DocumentId
    private Long id;
    
    @Size(max=10)
    @Column(name = "JOUR", length = 10)
    @Field(index= Index.YES,store= Store.YES)
    private String jour;
    
    @Size(max=150)
    @Column(name = "PRE", length = 150)
    @Field(index= Index.YES,store= Store.NO)
    private String pre;
    
    @Size(max=2)
    @Column(name = "HIGHT", length = 2)
    @Field(index= Index.YES,store= Store.NO)
    @NumericField
    private int hight;
    
    @Size(max=2)
    @Column(name = "LOW", length = 2)
    @Field(index= Index.YES,store= Store.NO)
    @NumericField
    private int low;

    @ManyToOne()
    @JoinColumn(name="lprev")
     @ContainedIn
    private Meteo meteo;

    
    public PrevMt() {
    }

    public PrevMt(String jour, String pre, int hight, int low) {
        this.jour = jour;
        this.pre = pre;
        this.hight = hight;
        this.low = low;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public String getPre() {
        return pre;
    }

    public void setPre(String pre) {
        this.pre = pre;
    }

    public int getHight() {
        return hight;
    }

    public void setHight(int hight) {
        this.hight = hight;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public Meteo getMeteo() {
        return meteo;
    }

    public void setMeteo(Meteo meteo) {
        this.meteo = meteo;
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
        if (!(object instanceof PrevMt)) {
            return false;
        }
        PrevMt other = (PrevMt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "re.ejb.entite.climat.PrevMt[ id=" + id + " ]";
    }

    @Override
    public void setNom(String nom) {
        jour=nom;
    }

    @Override
    public String getNom() {
        return jour;
    }
    
}
