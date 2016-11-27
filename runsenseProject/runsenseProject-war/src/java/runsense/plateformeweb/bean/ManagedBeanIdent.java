package runsense.plateformeweb.bean;



import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
/*import runsense.ejb.entite.Donne;
import runsense.ejb.sessionBean.DonneFacadeLocal;*/
import runsense.plateformeweb.entite.Hote;





/**
 *
 * @author Administrateur
 */

@ManagedBean
@SessionScoped
//</editor-fold>
public class ManagedBeanIdent implements java.io.Serializable
{
        
    private List<Hote> Lident;
    
    private List<String> Lname ;
    private List<String> Lpswd ;
    private java.lang.String username="admin" ;
    private java.lang.String password="admin" ;
    private Hote hote;
    
   
   // @PersistenceContext(unitName="IRunsensPU")
     private EntityManager em ;
     
     private EntityTransaction tx;
     
    /*@EJB
    private DonneFacadeLocal DonneEjb;
    
    private Donne donne;*/
    
    /** Creates a new instance of ManagedBeanIdent */

    public ManagedBeanIdent() 
    {
        /*try 
        {
        connectEjb();      
        
        
        } catch (NamingException nax) 
        {
            System.err.println(nax.getCause().toString()+nax.getRootCause()+nax.getResolvedName()+nax.getMessage());
        } catch (Exception ex)
        {
            System.err.println(ex.getMessage());
        }*/
     
       hote = new Hote();
       System.out.println(username+"madagedbeanIdent"+password);
       hote.setUtilisateur(username);
       hote.setPassword(password);
      
         
            
    }

    public Hote getHote() {
        return hote;
    }

    public void setHote(Hote hote) 
    {
       this.hote = hote;
       
    }

    public List<String> getLname() {
        return Lname;
    }

    public void setLname(List<String> Lname) {
        this.Lname = Lname;
    }

    public List<String> getLpswd() {
        return Lpswd;
    }

    public void setLpswd(List<String> Lpswd) {
        this.Lpswd = Lpswd;
    }
    
    public String getPassword() 
    {
        this.password = hote.getPassword();
        return password;
    }

    public void setPassword(String password) {
        hote.setPassword(password);
        this.password = password;
    }

    public String getUsername()
    {
        this.username = hote.getUtilisateur();
        return username;
    }

    public void setUsername(String username) {
        hote.setUtilisateur(username);
        this.username = username;
    }
    

    public String validateRegister(ActionEvent actionEvent)
    {
      /* tx=em.getTransaction();
        String nfindAll="Gere" ;
 
        System.out.println(tx.isActive()+"tx.isActive()"+tx.toString());        
                        
            if(!tx.isActive())
            {
                  System.out.println("lancement transaction");
                tx.begin();
            }
            
            Lident=(List<runsense.plateformeweb.entite.Hote>) em.createNamedQuery("Hote.findAll").getResultList();
            tx.commit();
           if(Lident.size()==0)
            {
                if(!tx.isActive())
                     tx.begin();
                
                insertion("admin","admin");
            //  Lident=(List<runsense.plateformeweb.entite.Hote>) em.createNamedQuery("Hote.findAll").getResultList();
            Query setParameter = em.createNamedQuery("Hote.findByUtilisateur").setParameter("utilisateur", "admin");
            int firstResult = setParameter.getFirstResult();
               
            }
                
            System.out.print("Lident.size()"+Lident.size());
           
            
           for(int i=0; i<Lident.size();i++)  
           {
            Hote h = Lident.get(i);
            System.out.println(h.getUtilisateur()+"**********utili hote");
           if(h.getUtilisateur().equals(this.username))
            {
                if(h.getPassword().equals(this.password))
                {
                    System.out.print(h.getUtilisateur() +username+password);
                    nfindAll="Gere";
                    this.setHote(h);
                    try {
            FacesContext fc = FacesContext.getCurrentInstance();
                        System.out.println("***********facecontext**********"+fc.getExternalContext().toString());
                        ExternalContext externalContext = fc.getExternalContext();
                        NavigationHandler nav = fc.getApplication().getNavigationHandler();
                        nav.handleNavigation(fc, "", "Gere");
                             // externalContext.redirect("/plateforme.re");
                              
           // RequestContext eq=new DefaultRequestContext();
                
                         System.out.println("***********facecontext**********"+externalContext.toString());
                    } catch (Exception ex) 
                    {
                        System.out.println(ex.getMessage()+"cause :"+ex.getCause());
                    } 
                    
                }else
                 {
                    System.err.println("erreur mot de passe");        
                 }
            }else
            {
                System.err.println("erreur utilisateur"); 
            }
          }
        /*}catch(TransactionRequiredException tex)
        {
            System.out.println(tex.getMessage()+"TransactionRequiredException cause:"+tex.getCause());
        }catch(Exception ex)
        {
            System.out.println(ex.getMessage()+"cause:"+ex.getCause());
        }
        */
      // FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Bienvenue sur la plateforme Runsense",username));
      return "";
    }

    public void insertion(String nom, String pswd)
    {
        tx=em.getTransaction();
       if(!tx.isActive())
            {
                  System.out.println(" insertionlancement transaction");
                tx.begin();
            }
        
            String plsql="Insert into Hote(id,password,utilisateur) values(0,'"+nom+"','"+pswd+"')";
            int executeUpdate = em.createNativeQuery(plsql).executeUpdate();
         
        tx.commit();
            System.out.println("executeUpdate"+executeUpdate);
       
           /*
            tx.begin();
            Hote h=new Hote();
            h.setUtilisateur("admin");
            h.setPassword("admin");
              em.persist(h);
            tx.commit();
            System.out.println("executeUpdatepersist"+executeUpdate);
            
            */
    }
    public String selection() throws Exception
    {
        String select = "";
        for(int i=0; i<Lident.size(); i++)
        {
           Lname.add( Lident.get(i).getUtilisateur());
           Lpswd.add( Lident.get(i).getPassword());
        }
            
            
        return select;
    }
    
    public void updateUser()
    {
        
        
    }
    
    public void createUser()
    {
        
    }
    
    public void delete()
    {
        
    }

   /*private void connectEjb() throws NamingException 
    {
        donne=new Donne();
        donne.setId(0);
        donne.setMethode("test".toCharArray());
        System.out.println(donne.toString());
        
        InitialContext ctx=new InitialContext();
        System.out.println(ctx.toString());
            DonneEjb=(DonneFacadeLocal) ctx.lookup("java:comp/env/DonneFacade");
       
        DonneEjb.start();
        DonneEjb.create(donne);  
    }*/
    
    
    
}
