/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.entite.client;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Indexed;
import run.ejb.entite.geo.interf.IntBaz;

/**
 *
 * @author runsense
 */

@Entity
@Table(name = "CONTACT")
@XmlRootElement
/*@NamedQueries({
    @NamedQuery(name = "Contact.findAll", query = "SELECT c FROM Contact c"),
    @NamedQuery(name = "Contact.findById", query = "SELECT c FROM Contact c WHERE c.id = :id"),
    @NamedQuery(name = "Contact.findByComment", query = "SELECT c FROM Contact c WHERE c.comment = :comment"),
    @NamedQuery(name = "Contact.findByContact", query = "SELECT c FROM Contact c WHERE c.contact = :contact"),
    @NamedQuery(name = "Contact.findByNom", query = "SELECT c FROM Contact c WHERE c.nom = :nom"),
    @NamedQuery(name = "Contact.findByPrenom", query = "SELECT c FROM Contact c WHERE c.prenom = :prenom")})*/
@Indexed(index="CONTACT")
@Analyzer(impl=StandardAnalyzer.class)

public class Contact implements Serializable, IntBaz {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq_ctc")
    @SequenceGenerator(sequenceName="seq", name="seq_ctc", allocationSize=1)
    @Column(name = "id", nullable = false)
    private Long id;
    @Size(max = 2000)
    @Column(name = "COMMENT", length = 200000)
    private String comment;
    @Size(max = 255)
    @Column(name = "OBJET", length = 500)
    private String objet;

    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH} )
    //@Cascade( org.hibernate.annotations.CascadeType.SAVE_UPDATE )
     @ContainedIn
    private Compte cptectc;

    public Contact() {
    }

    public Contact(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public Compte getCptectc() {
        return cptectc;
    }

    public void setCptectc(Compte cptectc) {
        this.cptectc = cptectc;
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
        if (!(object instanceof Contact)) {
            return false;
        }
        Contact other = (Contact) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "runsense.plateformeweb.entite.Contact[ id=" + id + " ]";
    }

    @Override
    public void setNom(String nom) {
       
    }

    @Override
    public String getNom() {
        return "contact de "+cptectc.getNom();
                }
    
}
