/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.entite;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author runsense
 */
@Entity
@Table(name = "contact")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Contact.findAll", query = "SELECT c FROM Contact c"),
    @NamedQuery(name = "Contact.findById", query = "SELECT c FROM Contact c WHERE c.id = :id"),
    @NamedQuery(name = "Contact.findByComment", query = "SELECT c FROM Contact c WHERE c.comment = :comment"),
    @NamedQuery(name = "Contact.findByContact", query = "SELECT c FROM Contact c WHERE c.contact = :contact"),
    @NamedQuery(name = "Contact.findByNom", query = "SELECT c FROM Contact c WHERE c.nom = :nom"),
    @NamedQuery(name = "Contact.findByPrenom", query = "SELECT c FROM Contact c WHERE c.prenom = :prenom")})
public class Contact implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id", nullable = false)
    private Integer id;
    @Size(max = 255)
    @Column(name = "comment", length = 255)
    private String comment;
    @Size(max = 255)
    @Column(name = "contact", length = 255)
    private String contact;
    @Size(max = 255)
    @Column(name = "nom", length = 255)
    private String nom;
    @Size(max = 255)
    @Column(name = "prenom", length = 255)
    private String prenom;
    @Size(max = 3)
    @Column(name = "civilite", length = 255)
    private String civilite;
    

    public Contact() {
    }

    public Contact(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
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
    
}
