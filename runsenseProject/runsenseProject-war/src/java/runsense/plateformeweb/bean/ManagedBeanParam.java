/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import runsense.plateformeweb.entite.Identifiant;


/**
 *
 * @author Administrateur
 */
@ManagedBean
@RequestScoped
public class ManagedBeanParam implements java.io.Serializable
{
     private Identifiant identf;
  
     private EntityManager em;

    /** Creates a new instance of ManagedBeanParam */
    public ManagedBeanParam() 
    {
       // em = Persistence.createEntityManagerFactory("IRunsensPU").createEntityManager();
    }

    public Identifiant getIdentf() {
        return identf;
    }

    public void setIdentf(Identifiant identf) {
        this.identf = identf;
    }
    
    public void refUtili(ManagedBeanIdent bIdent)
    {
        identf = (Identifiant) em.createNamedQuery("Identifiant.findByIdhote")
                .setParameter("idhote", bIdent.getHote().getId()).getSingleResult();
        System.out.println("refUtili :"+identf.toString());
    }
}
