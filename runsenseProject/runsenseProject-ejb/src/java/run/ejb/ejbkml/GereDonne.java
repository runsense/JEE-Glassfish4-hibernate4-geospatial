/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.ejbkml;


import com.sun.xml.ws.api.tx.at.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ApplicationException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.transaction.NotSupportedException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockFactory;
import org.apache.lucene.util.NumericUtils;
import org.apache.lucene.util.Version;
import org.hibernate.Hibernate;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;
import run.ejb.entite.geo.Coord;
import run.ejb.entite.geo.Evnmt;
import run.ejb.entite.geo.Ligne;
import run.ejb.entite.geo.Pt;
import run.ejb.entite.geo.interf.IntBaz;
import run.ejb.entite.geo.interf.RsEntite;
import run.ejb.util.ex.Xcption;

/**
 *
 * @author Administrateur
 */
@Stateless
@ApplicationException(rollback=true)

@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class GereDonne  implements GereDonneLocal {
  
    private boolean synk;
    private  EntityManager em;
  
    private UserTransaction utx;
  
    private Coord coord;

    private Ligne lgn;
    
    private Pt pt;
    
    private Query lqn;
    private ArrayList results ;
    private List crits;
    
    private FullTextEntityManager ftem;
    
    private  ControlFactory cFact;
   
    private BooleanQuery bqinit;
   private IndexWriter indexWriter;
   private  Context ctx;
    private Set<Coord> lco;
    private boolean cfm;

    public GereDonne() 
    {
        synk=true;
        /*cFact = new ControlFactory();
       em= cFact.newFactory();
           ftem = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);  */
         /*try {
            ctx = new InitialContext();
        } catch (NamingException ex) {
            
            Xcption.Xcption("GereDonne createEM NamingException",ex.getMessage());
        }*/
    }
    @Override
   public void attent(int stand)
   {
        
        try {
            wait(stand);
        } catch (InterruptedException ex) {
            Logger.getLogger(GereDonne.class.getName()).log(Level.SEVERE, null, ex);
        }
        
   }
    public void finish()
    {
        
        
        try {
         if(utx!=null)
            if(utx.getStatus()==Status.STATUS_ROLLING_BACK||utx.getStatus()==Status.STATUS_MARKED_ROLLBACK||utx.getStatus()==Status.STATUS_COMMITTING)
                {
                    if(cFact.getEm().isOpen())
                    {
                        cFact.getEm().joinTransaction();
                    }
                    utx.rollback();
                }
        } catch (SystemException ex) {
           
            Xcption.Xcption("GereDonne finish SystemException",ex.getMessage());
        }
        if(cFact==null)
        {
            cFact = new ControlFactory();
            cFact.newFactory();
        }else if(cFact.getEm().isOpen())
        {
            cFact.getEm().clear();
            cFact.getEm().close();
        }
        
        
    }
    @Override
    public void stLazy(Object lazy)
    {
        Hibernate.initialize(lazy);
    }
   
    @Override
    public void createEM()
    {
        
        if(em!=null)
            if(em.isOpen())
            {
                em.clear();
                em.close();
            }
        if(cFact==null)
            cFact = new ControlFactory();
        
        em=cFact.newFactory();
        
       
        ftem = org.hibernate.search.jpa.Search.getFullTextEntityManager(em); 
        synk=true;
    }
  @Override   
 public void initLuc(Object o) 
 {
    try{
         indexWriter  = new IndexWriter(
                FSDirectory.open(chchRsentite(o)),       
                new IndexWriterConfig(Version.LUCENE_CURRENT, new StandardAnalyzer(Version.LUCENE_CURRENT)));
         
         } catch (IOException ex) {
            Logger.getLogger(GereDonne.class.getName()).log(Level.SEVERE, null, ex);
        }
        
 }

  @Override
    public ArrayList rchWild(String rc, Object entite, String field) throws IOException
    {
       
            rc+="*";
        
            
        
        
        
        QueryBuilder qb = ftem.getSearchFactory().buildQueryBuilder().forEntity(entite.getClass()).get();

         lqn = qb.keyword()
                .wildcard()
                .onField(field).boostedTo(2).matching(rc).createQuery();
        
    // création de la requête JPA fulltext
    org.hibernate.search.jpa.FullTextQuery ftq =
            ftem.createFullTextQuery(lqn, entite.getClass());
    Xcption.Xcption("GereDonne", ftq.getResultSize());
    
    // exécution de la requête
     results = (ArrayList) ftq.getResultList();

     
    return  results;
    }
    @Override
    public ArrayList rchFuzzy(String rc, Object entite, String field, float ad) throws IOException
    {
        int tal=0;float thold=0.0f;
        
        
        QueryBuilder qb = ftem.getSearchFactory().buildQueryBuilder().forEntity(entite.getClass()).get();

        
        if(ad>1)//boolean true ad
        {
             tal=30; 
           thold=1f;
           
           lqn = qb.keyword()
                .fuzzy().withThreshold(thold).withPrefixLength(tal)
                .onField(field).matching(rc).createQuery();
        }else if(field.equals("description"))
        {
            tal=10; 
            thold=ad;
           
            lqn = qb.keyword()
                .fuzzy().withThreshold(thold).withPrefixLength(tal)
                .onField(field).matching(rc).createQuery();
        }else if(field.equals("nom"))
        {
            tal=1;
            thold=ad;
           
            lqn = qb.keyword()
                .fuzzy().withThreshold(thold).withPrefixLength(tal)
                .onField(field).matching(rc).createQuery(); 
        }else if(field.equals("adresse"))
        {
            tal=8;
            thold=ad;
          
            lqn = qb.keyword()
                .fuzzy().withThreshold(thold).withPrefixLength(tal)
                .onField("adresse").matching(rc).createQuery(); 
        }
         
        
    // création de la requête JPA fulltext
    org.hibernate.search.jpa.FullTextQuery ftq =
            ftem.createFullTextQuery(lqn, entite.getClass());
    Xcption.Xcption("GereDonne", ftq.getResultSize());
    
    // exécution de la requête
         try{
         results = (ArrayList) ftq.getResultList();
         }catch(ClassCastException ccex)
         {
             results = new ArrayList();
         }catch(Exception ex)
         {
           ftq.setMaxResults(ftq.getResultSize()/2);
             Xcption.Xcption("GereDonne rchFuzzy Exception", ex.getMessage());
                results = (ArrayList) ftq.getResultList();
         }

    if(results.isEmpty())
    {
        
            rc="~"+rc+"~";
            lqn = qb.keyword()
                .fuzzy().withThreshold(thold).withPrefixLength(tal)
                .onField(field).boostedTo(2).matching(rc).createQuery();
            ftq =
            ftem.createFullTextQuery(lqn, entite.getClass());
            // création de la requête JPA fulltext
         Xcption.Xcption("GereDonne", ftq.getResultSize());
    
         // exécution de la requête
         try{
         results = (ArrayList) ftq.getResultList();
         }catch(ClassCastException ccex)
         {
             results = new ArrayList();
         }catch(Exception ex)
         {
             Xcption.Xcption("GereDonne rchFuzzy Exception", ex.getMessage());
             ftq.setMaxResults(ftq.getResultSize()/2);
                results = (ArrayList) ftq.getResultList();
         }
    }
         
        
        

        
    

    return results;
    }
    @Override
 public ArrayList rchLuc(String rc, Object entite, String field) //throws ParseException
 {
        
       //rc="salazie";
        QueryBuilder qb = ftem.getSearchFactory().buildQueryBuilder().forEntity(entite.getClass()).get();
         lqn = null;
         results=null;
        if(rc.split(" ").length>1)
        {
            results = rchPhrase(rc,  entite, field);
        }
        else if(rc!="")
        {
            try{

             lqn = qb.keyword().onField(field).matching(rc).createQuery();
                /*lqn = qb.keyword()
                    .fuzzy().withThreshold(0.8f).withPrefixLength(15)
                    .onField(field).matching(rc).createQuery();*/
            }catch(RuntimeException re)
            {
               
                 Xcption.Xcption("GereDonne rchLuc RuntimeException", re.getMessage());
                System.err.println("envoie sur rchPhrase ");
                results = rchPhrase(field, (RsEntite) entite, field);
            }
        // création de la requête JPA fulltext
            org.hibernate.search.jpa.FullTextQuery ftq =
                        ftem.createFullTextQuery(lqn, entite.getClass());
            try{
                
                Xcption.Xcption("GereDonne", ftq.getResultSize());
    
                // exécution de la requête
                 results = (ArrayList) ftq.getResultList();

            }catch(NullPointerException npex)
            {
                
                 Xcption.Xcption("GereDonne rchLuc NullPointerException", String.valueOf(npex.getMessage()));
                results=new ArrayList(0);
            }catch(java.lang.ClassCastException ccex)
            {
                results=new ArrayList(0);
            }
        }else
            results=new ArrayList(0);
    return results;
 }

    @Override
    public ArrayList rchRange(List rc, Object entite ,String field, String avtapr )
    {
         results=null;
       
        try{

             NumericRangeQuery<Long> newIntRange = NumericRangeQuery.newLongRange(field, (Long) rc.get(0), (Long) rc.get(1), true, true);
        FullTextQuery intQuery = ftem.createFullTextQuery(newIntRange, entite.getClass()); 
        
            Xcption.Xcption("GereDonne", intQuery.getResultSize());
    
            // exécution de la requête
                    results = (ArrayList) intQuery.getResultList();

            }catch(NullPointerException npex)
           {
               
               Xcption.Xcption("GereDonne rchPhrase NullPointerException", String.valueOf(npex.getMessage()));
               results=new ArrayList(0);
           }
        
        return results;
    }
    
    @Override
    public ArrayList rchPhrase(String phr, Object entite, String field )
    {
        
        
        QueryBuilder qb = ftem.getSearchFactory().buildQueryBuilder().forEntity(entite.getClass()).get(); 
        Query lqinit = null;
         lqn=null;
        
        
         lqn =  qb.phrase().withSlop(0).onField(field).sentence(phr).createQuery();
     
        // création de la requête JPA fulltext
         results=null;
        try{

            org.hibernate.search.jpa.FullTextQuery ftq =
                    ftem.createFullTextQuery(lqn, entite.getClass());
            Xcption.Xcption("GereDonne", ftq.getResultSize());
    
            // exécution de la requête
                    results = (ArrayList) ftq.getResultList();

            }catch(java.lang.ClassCastException ccex)
            {
                results=new ArrayList(0);
            }catch(Exception npex)
           {
             
               Xcption.Xcption("GereDonne rchPhrase NullPointerException", String.valueOf(npex.getMessage()));
               results=new ArrayList(0);
           }
            
        return results;
     }
 
    @Override
    public ArrayList rchSpat(Coord co, List<String> lfield, double kml )
    {
        
        
        QueryBuilder qb = ftem.getSearchFactory().buildQueryBuilder().forEntity(co.getClass()).get(); 
        Query lqinit = null;
        BooleanQuery blqn=null;
  
        
        
        
        
        Query spq = qb.spatial().onDefaultCoordinates().within(kml, Unit.KM).ofLatitude(co.getLatitude()).andLongitude(co.getLongitude()).createQuery();
     
        /*CircleImpl rimp=new CircleImpl(new PointImpl(co.getLng(), co.getLat(), SpatialContext.GEO)
                , Math.toRadians(50d), SpatialContext.GEO);
        double calcDistanceFromErrPct = SpatialArgs.calcDistanceFromErrPct(rimp, 0.2, SpatialContext.GEO);
        
       
        
        
        
          lqn=qb.keyword().onField(lfield.get(0)).andField(lfield.get(1)).matching(co).createQuery();*/
        // création de la requête JPA fulltext
         results=null;
        try{
            //blqn.add(lqn, BooleanClause.Occur.MUST);
            
             
            org.hibernate.search.jpa.FullTextQuery ftq =
                    ftem.createFullTextQuery(spq, co.getClass());
            Xcption.Xcption("GereDonne", ftq.getResultSize());
 
            // exécution de la requête
                    results = (ArrayList) ftq.getResultList();

               }catch(NullPointerException npex)
           {
               
               Xcption.Xcption("GereDonne rchPhrase NullPointerException", String.valueOf(npex.getMessage()));
               results=new ArrayList(0);
           }
            
        return results;
     }


 @Override
 public ArrayList rchBoolean(List<Object>rc, Object entite,List<String> fields, String precis, String genre) //throws ParseException
 {//recherche aproximative+ recherche contenant
     Occur occr;
    
        Query lqn1=null;
        Query lqn2=null;
        
        String comp="*_~";
        String[] split = comp.split("_");
        BooleanQuery bq=null;
       if(bqinit==null) 
       {
           bq=new BooleanQuery();
       }else
       {
          bq= bqinit;
       }
        if(!ftem.isOpen())
            createEM();
        QueryBuilder qb = ftem.getSearchFactory().buildQueryBuilder().forEntity(entite.getClass()).get(); 
        Occur[] tabprecis=new Occur[2];
        String[] splprecis = precis.split("_");
        int cpte=0;
        for(String prc:splprecis)
        {
            if(prc.equals("MUST"))
            {
                tabprecis[cpte]=BooleanClause.Occur.MUST;
            }else if(prc.equals("SHOULD"))
            {
                tabprecis[cpte]=BooleanClause.Occur.SHOULD;
            }
            else 
            {
                tabprecis[cpte]=BooleanClause.Occur.MUST_NOT;
            }
            cpte++;
        }
        
        
        if(genre!=null)
        {
            String[] splg = genre.split("_");
            if (splg[0].equals("fix"))
            {
               lqn1 = qb.keyword().onField(fields.get(0)).matching(rc.get(0)).createQuery();
            }else if(splg[0].equals("aprox"))
            {
               int tal=1; 
                float thold=0.9f;

                 lqn1 = qb.keyword()
                      .fuzzy().withThreshold(thold).withPrefixLength(tal)
                      .onField(fields.get(0)).matching(rc.get(0)).createQuery();
            }
            
            if (splg[1].equals("fix"))
            {
               lqn2 = qb.keyword().onField(fields.get(1)).matching(rc.get(1)).createQuery();
            }else if(splg[1].equals("aprox"))
            {
               int tal=1; 
                float thold=0.9f;

                 lqn2 = qb.keyword()
                      .fuzzy().withThreshold(thold).withPrefixLength(tal)
                      .onField(fields.get(1)).matching(rc.get(1)).createQuery();
            }
            bq.add(lqn1, tabprecis[0]);
             bq.add(lqn2, tabprecis[1]);
        }
        else if(entite instanceof run.ejb.entite.geo.Coord)
        {//coordonné // (rc=null, entite instanceof coord, precis)
         Coord co=(Coord) entite;

            String stg=null;
         long doubleToSortableLong = NumericUtils.doubleToSortableLong(co.getLatitude());
        
                    stg=String.valueOf(
                             doubleToSortableLong);
                
        
             lqn1 = qb.keyword()
                     .fuzzy().withThreshold(0.8f).withPrefixLength(9)
                     .onField(fields.get(0)).matching(stg).createQuery();
             
             doubleToSortableLong = NumericUtils.doubleToSortableLong(co.getLongitude());
             stg=String.valueOf(doubleToSortableLong);
                
             
             lqn2 = qb.keyword()
                     .fuzzy().withThreshold(0.8f).withPrefixLength(9)
                     .onField(fields.get(1)).matching(stg).createQuery();
             bq.add(lqn1, tabprecis[0]);
             bq.add(lqn2, tabprecis[1]);
        }
        else if(rc.get(0) instanceof java.util.Date&&
                rc.get(1) instanceof java.util.Date)
        {
           
           try{
            lqn1=qb.range().onField(fields.get(0)).from(rc.get(0)).to(rc.get(1)).createQuery();
           }catch(Exception ex)
           {
               
               Xcption.Xcption("lqn1=qb.range().onField(fields.get(0)).from(d).to(f).createQuery(); Exception", String.valueOf(ex.getMessage()));
           }
            bq.add(lqn1, tabprecis[0]);
            
             
            lqn2=qb.range().onField(fields.get(1)).from(rc.get(0)).to(rc.get(1)).createQuery();
           
            bq.add(lqn2, tabprecis[1]);
            
        }else if(entite instanceof Evnmt)
        {//recherche date
           
            if(!rc.get(0).equals(""))
            {
                if(fields.size()<2)
                {
                    comp = DateTools.dateToString((java.util.Date)rc.get(0), DateTools.Resolution.DAY);
                
                    rc.set(0, comp);

                     lqn1 = qb.keyword()
                        .onField(fields.get(0)).matching(rc.get(0)).createQuery();
                      bq.add(lqn1, tabprecis[0]);
                }else if(!"FIN".equals(fields.get(1)))
                {
                    comp = DateTools.dateToString((java.util.Date)rc.get(0), DateTools.Resolution.DAY);
                
                    rc.set(0, comp);

                     lqn1 = qb.keyword()
                        .onField(fields.get(0)).matching(rc.get(0)).createQuery();
                      bq.add(lqn1, tabprecis[0]);
                      fields.add(fields.get(1));rc.add(rc.get(1));
                }
                else
                {
                    comp = DateTools.dateToString((java.util.Date)rc.get(0), DateTools.Resolution.DAY);
                
                    rc.set(0, comp);

                     lqn1 = qb.range()
                        .onField(fields.get(0)).andField(fields.get(1)).from(rc.get(0)).to(rc.get(1)).createQuery();
                      bq.add(lqn1, tabprecis[0]);
                }
                
            }
            if(rc.size()>1)
            {
                lqn2= qb.keyword()
                    .fuzzy().withThreshold(0.9f).withPrefixLength(1000)
                    .onField(fields.get(2)).ignoreFieldBridge().matching(rc.get(2)).createQuery();
                  bq.add(lqn2, tabprecis[1]);
            }
            
        }
        else
        {
             cpte=0;
            for(String field:fields)
            {
                if(cpte>=2)
                {
                    BooleanQuery oldbq=bq;
                    BooleanQuery addbq=new BooleanQuery();
                    bq=new BooleanQuery();
                        addbq.add(oldbq, BooleanClause.Occur.MUST);
                    bq.add(oldbq, BooleanClause.Occur.MUST);
                    bq.add(addbq, BooleanClause.Occur.MUST);
                }
                if(field.equals("nom"))
                {//autocomplete
                   
                    if(!rc.get(cpte).equals(""))
                    {
                        comp=(String) rc.get(cpte);
                        
                        /* 
                        if(comp.contains(" ")&&!comp.endsWith(" "))
                        {
                            rchPhrase(comp, entite, field);
                        }
                        else
                        {*/
                            lqn = qb.keyword().onField(field).matching(comp).createQuery();
                        //}
                        
                          bq.add(lqn, tabprecis[cpte]);
                          
                    }
                    
                }else if(field.equals("adresse"))
                {
                    if(!rc.get(cpte).equals(""))
                    {
                        comp=(String) rc.get(cpte);
                        
                        /* 
                        if(comp.contains(" ")&&!comp.endsWith(" "))
                        {
                            rchPhrase(comp, entite, field);
                        }
                        else
                        {*/
                            
                                lqn = qb.keyword()
                                    .onField(field).matching(comp).createQuery();
                            
                            
                            
                        //}
                          bq.add(lqn, tabprecis[cpte]);
                    }
                }
                else if(field.equals("description"))
                {//autocomplete
                    
                    
                    if(!rc.get(cpte).equals(""))
                    {
                        comp=(String) rc.get(cpte);
                        
                        /*if(comp.contains(" ")&&!comp.endsWith(" "))
                        {
                            rchPhrase(comp, entite, field);
                        }
                        else
                        { */
                             comp=(String) rc.get(cpte);
                            lqn= qb.keyword()
                                .onField(field).matching(comp).createQuery();
                        //}
                          bq.add(lqn, tabprecis[cpte]);
                    }
                }
                cpte++;
            }
            
        }
    //initLuc(entite);
    
    // création de la requête JPA fulltext
    org.hibernate.search.jpa.FullTextQuery ftq =
            ftem.createFullTextQuery(bq, entite.getClass());
    Xcption.Xcption("GereDonne", ftq.getResultSize());
    
    // exécution de la requête
    ArrayList rslts ;
    try{
     rslts = (ArrayList) ftq.getResultList();
    }catch(java.lang.Exception ccex)
    {
        rslts = new ArrayList();
       
    }

    return rslts;
 }   

 @Override
 public ArrayList rch(Object rc, Object entite, String categ) //throws ParseException
 {
        QueryBuilder qb;
       //rc="salazie";
     
         qb= ftem.getSearchFactory().buildQueryBuilder().forEntity(entite.getClass()).get();
        String stg;
        if(rc instanceof String)
        {
            stg=(String) rc;
        }else{
            stg=String.valueOf(rc);
        }
        
        
         lqn = qb.keyword().onField(categ).matching(stg).createQuery();
        
    // création de la requête JPA fulltext
    org.hibernate.search.jpa.FullTextQuery ftq =
            ftem.createFullTextQuery(lqn, entite.getClass());
    
    Xcption.Xcption("GereDonne"+ftq.getMaxResults(), ftq.getResultSize());
    Xcption.Xcption("ftq.getHints()",ftq.getHints());
    try{
    // exécution de la requête
     results = (ArrayList) ftq.getResultList();

     }catch(ClassCastException ccex)
         {
             results = new ArrayList();
         }catch(Exception ex)
     {
         
         ftq.setMaxResults(ftq.getResultSize()/2);
          Xcption.Xcption("GereDonne rch(Object rc, Object entite, String categ) Exception", String.valueOf(ex.getMessage()));
         results = (ArrayList) ftq.getResultList();
     }
    return results;
 }
    @Override
    @Transactional
    //@TransactionAttribute(REQUIRED)
   public boolean deleteObject(Object pers)
   {
       EntityManager manager = null;
       FullTextEntityManager fullTextEntityManager= null;
       boolean cfm=false;
       try{
            manager = cFact.getEm();
            fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(manager);
         try {
            ctx = new InitialContext();
     
            utx = (UserTransaction) ctx.lookup("java:comp/UserTransaction");
       
        } catch (NamingException ex) {
            
             Xcption.Xcption("GereDonne createEM NamingException", String.valueOf(ex.getMessage()));
        }
                 debUtx();
                        
                         
                        
           //fullTextEntityManager.remove(pers);
           manager.remove(pers);
                               
        
        manager.joinTransaction();
            try{
                 debUtx();
                  utx.commit();
                  fullTextEntityManager.flushToIndexes();
                  cfm=true;
                }catch(org.hibernate.HibernateException hex)
                        {
                           utx.rollback();
                            Xcption.Xcption("GereDonne deleteObject org.hibernate.HibernateException", String.valueOf(hex.getMessage()));
                        }
                 
                 Xcption.Xcption("GereDonne deleteObject ", String.valueOf("pers\n"+pers.toString()));
       }catch(Exception ex)
       {
          
           Xcption.Xcption("GereDonne deleteObject Exception", String.valueOf(ex.getMessage()));
       }
       return cfm;
   }

    /**
     *
     * @param pers
     * @param create
     * @throws Exception
     */
    @Override    
    @Transactional
    public Object createObj(Object pers, boolean create)  throws Exception, java.lang.InterruptedException
    {
        em= cFact.getEm();
       /* if(!manager.isOpen())
        {
            manager=cFact.newFactory();
        }*/
        
       // FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(manager);
         cfm=false;
        FSDirectory open;
   
    try{  
       
            
         ctx = new InitialContext();
        
            utx = (UserTransaction) ctx.lookup("java:comp/UserTransaction");
                
             int status = utx.getStatus();
             
        debUtx();
             File file =chchRsentite(pers);
                 open = FSDirectory.open(file);
                 
             File[] listFiles = file.listFiles();
             if(listFiles!=null)
                 for(File f:listFiles)
                 {
                     if(f.getName().equals("write.lock"))
                         f.delete();
                 }
                 
                 LockFactory lockFactory = open.getLockFactory();
                     lockFactory.clearLock(open.getLockID());
                 
                 //Session session = ftem.unwrap(Session.class);

                if(create)
                           {
                             
                              ftem.persist(pers);
                               
                           }else
                           {
                               
                               
                              
                               pers = ftem.merge(pers);
                              
                               
                           }
                
                em.joinTransaction();
                
            
                 if(!create)       
                 {
                     ftem.joinTransaction();
                     try{
                            if(ftem.contains(pers))
                                ftem.flush();
                            else if(utx.getStatus()==Status.STATUS_MARKED_ROLLBACK)
                            {
                                utx.rollback();
                                utx.begin();
                                pers = ftem.merge(pers);
                            }
                            if(Thread.currentThread().isInterrupted())
                                    {
                                        wait(18000);
                                    }

                            
                            Xcption.Xcption("utx.commit()", String.valueOf(utx.getStatus()));
                                    utx.commit();
                                    ftem.flushToIndexes();
                             Xcption.Xcption("Après utx.commit()", String.valueOf(utx.getStatus()));      
                                    cfm=true;
                            
                                        
                        }catch(org.hibernate.HibernateException hex)
                        {
                                   synk=false; 
                                       Xcption.Xcption("GereDonne createObj org.hibernate.HibernateException", hex.getMessage());
                                    utx.setRollbackOnly();
                                    em.clear();
                                    em.close();
                        }catch(Throwable e)
                        {
                            synk=false;
                            wait(9000);
                            em.clear();
                            em.close();
                            new GereDonne().createObj( pers, create) ;
                        }
                     
               }else if(create)
               {
                   try{
                    if(Thread.currentThread().isInterrupted())
                    {
                         wait(2000);
                    }
                    
                    utx.commit();
                    ftem.flushToIndexes();
                    
                    cfm=true;
                   }catch(java.lang.InterruptedException ie)
                    {
                        synk=false;
                        utx.setRollbackOnly();
                        em.clear();
                        em.close();
                        
                        em=cFact.newFactory();
                    }catch(org.hibernate.PersistentObjectException pex)
                    {
                               synk=false;
                                Xcption.Xcption("GereDonne createObj org.hibernate.PersistentObjectException", pex.getMessage());
                                utx.setRollbackOnly();
                                em.clear();
                                em.close();
                                createEM();
                                if(pers instanceof IntBaz)
                                {
                                    IntBaz intBaz=(IntBaz) pers;
                                        pers=em.find(intBaz.getClass(), intBaz.getId());
                                }else if(pers instanceof RsEntite)
                                {
                                    RsEntite rs=(RsEntite) pers;
                                        pers=em.find(rs.getClass(), rs.getId());
                                }
                                
                                createObj(pers, create);
                    }catch(Throwable e)
                    {
                        synk=false;
                        utx.setRollbackOnly();
                        em.clear();
                        em.close();
                        em=cFact.newFactory();
                    }
               }

                     
                        Xcption.Xcption("GereDonne createObj ", "pers\n"+pers.toString());
                 
                
                 
       
        }catch(org.apache.lucene.store.AlreadyClosedException iex)
        {
            synk=false;
            Xcption.Xcption("GereDonne createObj ThreadInterruptedException ", iex.getMessage());
            utx.rollback();
            pers=null;
        }catch(Exception ex)
        {
            
            synk=false;
            Xcption.Xcption("GereDonne createObj Exception ", ex.getMessage());
            utx.rollback();
            em.clear();
            em.close();
            createEM();
            pers=null;
          throw new PersistenceException(ex.getMessage());
        }
    
      
      return pers;
    }

    public Query getLqn() {
        return lqn;
    }

    public void setLqn(Query lqn) {
        this.lqn = lqn;
    }

    @Override
    public ArrayList chchTout(Object entite) throws Exception
    {
        System.err.print("GereDonne chchTout ");
        javax.persistence.Query rqte = ftem.createQuery("from "+entite.getClass().getSimpleName());
       return   (ArrayList) rqte.getResultList();
    }
   
    
    @Override
    public void setCoord( String lat, String lng, String alt) 
    {
        coord=new Coord();
        try{
        coord.setId(System.currentTimeMillis());
        }catch(Exception ex)
        {
            coord.setId(System.currentTimeMillis());
        }
        coord.setLatitude(Double.valueOf(lat));
        coord.setLongitude(Double.valueOf(lng));
        coord.setAlt(Double.valueOf(alt));
        
    }


    @Override
    public void setLgn(String nom, String ad, String des, List<Coord> coords)
    {
        lgn = new Ligne();
              lgn.setNom(nom);
              lgn.setAdresse(ad);
              lgn.setDescription(des);
           //   lgn.setCoords(coords);
    }
    @Override
    public void setPt(String nom, String ad, String des, Double lat, Double lng)
    {
        pt=new Pt();
            pt.setNom(nom);
            pt.setAdresse(ad);
            pt.setDescription(des);
            pt.setLatitude(lat);
            pt.setLongitude(lng);
    }
    @Override
    public Ligne getLgn() {
        return lgn;
    }

    @Override
    public void setLgn(Ligne lgn) {
        this.lgn = lgn;
    }

    @Override
    public Set<Coord> getLco() {
        return lco;
    }

    public boolean isCfm() {
        return cfm;
    }

    public void setCfm(boolean cfm) {
        this.cfm = cfm;
    }

    @Override
    public EntityManager getEm() {
        return em;
    }

    @Override
    public ControlFactory getcFact() {
        return cFact;
    }

    @Override
    public boolean isSynk() {
        return synk;
    }

    @Override
    public void setSynk(boolean synk) {
        this.synk = synk;
    }

    
    public void debUtx() throws SystemException, NotSupportedException 
    {
        if(utx!=null)   
            if(utx.getStatus()==Status.STATUS_ACTIVE)
            {

            }else if(utx.getStatus()==Status.STATUS_MARKED_ROLLBACK)
            {
                utx.rollback();
                utx.begin();
            }else if(utx.getStatus()==Status.STATUS_NO_TRANSACTION)
            {
                utx.begin();
            }else if(utx.getStatus()==Status.STATUS_ROLLING_BACK||utx.getStatus()==Status.STATUS_COMMITTING||utx.getStatus()==Status.STATUS_UNKNOWN)
            {
                utx.rollback();
                utx.begin();
            }else
            {
                utx.begin();
            } 
         
    }

    private File chchRsentite(Object pers)
    {
            String ent="";
             if(pers instanceof RsEntite)    
             {
                 ent=pers.getClass().getCanonicalName().substring(18);
             }else
             if(pers.getClass().getCanonicalName().split("geo.").length>1)
             {
                 ent=pers.getClass().getCanonicalName().substring(18);
             }else
             {
                 ent=pers.getClass().getCanonicalName().substring(10);
             }
             
        return new File("/home/runsense/index/"+ent.toUpperCase());
             
    }
    
    public int hqlDelete(String myTable)
    {
        String hql = String.format("delete from %s",myTable);
            javax.persistence.Query rqte = em.createQuery(hql);
        return rqte.executeUpdate();
    }

}