/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.ejbkml;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
//import javax.persistence.SynchronizationType;

/**
 *
 * @author runsense
 */
public class ControlFactory 
{
    private static final String pu="webPU";
    private static  EntityManagerFactory emf;
    private  EntityManager em;
    /*private static final SessionFactory sessionFactory;
    static {
        try {
            sessionFactory = new AnnotationConfiguration()
                    .configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Log exception!
            throw new ExceptionInInitializerError(ex);
        }
    }*/
    public  ControlFactory()
    {
        
       /*final Map<String,String> propriete=new HashMap();
           propriete.put("QueryHints.CACHE_RETRIEVE_MODE", "CacheRetrieveMode.USE");
            propriete.put("QueryHints.CACHE_STORE_MODE", "CacheRetrieveMode.BYPASS");*/
            
            
           /* propriete.put("hibernate.connection.driver_class" ,"org.postgresql.ds.PGSimpleDataSource" );
            propriete.put("hibernate.connection.url" ,"jdbc:postgresql://localhost:5432/postgres [postgres sur public]" );
            propriete.put("hibernate.connection.username" ,"postgres" );
            propriete.put("hibernate.connection.password" ,"reunion974");
            propriete.put("hibernate.dialect", "org.hibernate.dialect.DerbyDialect ");
            propriete.put("javax.persistence.jdbc.driver" ,"org.apache.derby.jdbc.ClientDriver" );
            propriete.put("javax.persistence.jdbc.url" ,"jdbc:derby://localhost:1527/sample[app sur APP]" );
            propriete.put("javax.persistence.jdbc.user" ,"app" );
            propriete.put("javax.persistence.jdbc.password" ,"app");*/
           
                emf = Persistence.createEntityManagerFactory(pu);
               
                em=emf.createEntityManager();
            
    }

    public EntityManager newFactory()
    {
        
       /* Map<String,String> propriete=new HashMap();
           propriete.put("QueryHints.CACHE_RETRIEVE_MODE", "CacheRetrieveMode.USE");
            propriete.put("QueryHints.CACHE_STORE_MODE", "CacheRetrieveMode.BYPASS");*/
            
         
            if(emf==null) 
            {
                emf = Persistence.createEntityManagerFactory(pu);
                em=emf.createEntityManager();
            }else
            {
                
                    try{

                        em=emf.createEntityManager();
                    }catch(Exception ex)
                    {
                        System.err.print("ControlFactory newFactory(String pu)");System.out.println(ex.getMessage());
                    }
                
                
            }
        
            return em;
    }

    public  EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }
  
     /*public static Session getSession()
            throws HibernateException {
        return sessionFactory.openSession();
    }*/

    
}