/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.bean;

import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import runsense.plateformeweb.entite.Contact;


/**
 *
 * @author Administrateur
 */
@ManagedBean
@RequestScoped
public class ManagedBeanContact implements java.io.Serializable
{
    
    private Contact ctc;
    private List<Contact> l_ctc;
    
    private EntityManager em;
    
    public ManagedBeanContact() 
    {
       /* ControlFactory cf =new ControlFactory();
         em = cf.getEmf().createEntityManager();*/
        
    }

    public Contact getCtc() {
        return ctc;
    }

    public void setCtc(Contact ctc) {
        this.ctc = ctc;
    }

    public List<Contact> getL_ctc() {
        return l_ctc;
    }

    public void setL_ctc(List<Contact> l_ctc) {
        this.l_ctc = l_ctc;
    }

    public String reinit()
    {
        ctc=new Contact();
        
        return null;
    }
    
    public Boolean submit()
    {
        Boolean nsub=false;
        Contact ctc = new Contact();
        ctc.setId(0);
        ctc.setNom(ctc.getNom());
        ctc.setPrenom(ctc.getPrenom());
        ctc.setContact(ctc.getContact());
        ctc.setComment(ctc.getComment());
        try
        {
           /* em.getTransaction().begin();
            em.persist(ctc);
            em.getTransaction().commit();*/
            System.out.println("Contact enregistrer");
        }catch(Exception ex)
        {
            System.out.println("erreur enregistrement contact"+ex.getMessage());
            nsub=true;
        }
        
        addMsg();
        return nsub;
    }
    public void addMsg()
    {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Contact envoyer","de :"+ctc.getNom()+"  "+ctc.getPrenom()));
    }
    
}
