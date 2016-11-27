/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.ejbkml;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import run.ejb.base.Variable;
import run.ejb.entite.geo.Ligne;
import run.ejb.entite.geo.Pt;
import run.ejb.entite.geo.Tour;
import run.ejb.entite.util.kml.ChargeKml;
import run.ejb.entite.geo.interf.RsEntite;
import run.ejb.entite.util.kml.TourRsns;
import run.ejb.util.rsentite.schema.PrmtrSchm;
import run.ejb.entite.client.Compte;
import run.ejb.entite.client.Contact;
import run.ejb.entite.geo.Coord;

import run.ejb.entite.geo.interf.IntBaz;
import run.ejb.util.ex.Xcption;
import run.ejb.util.jak.Coordinates;

/**
 *
 * @author runsense
 */

public class GereCmpte 
{

   // private GereDonne gDonne;
    
    private GereKml gKml;
    private Document document;
    private ChargeKml charge;
    private DefaultTreeNode root;

    private Compte cpte;
    private Object chg;
   
    private List<de.micromata.opengis.kml.v_2_2_0.gx.Tour> ltour;
    private List<run.ejb.entite.geo.Tour> lbddtour;
    private List<RsEntite> lrsentite;
    
    private List<String> tbverif;
    private Object[] tempreponse;
    
    private PrmtrSchm schema;
    public GereCmpte() 
    {
        try {
            System.runFinalization();
            
            gKml=new GereKml();
              
           // supLock();
            //init
         
           // cpte= rchCmpte("paskal","nom");
               
           /*if(cpte==null)     
           {  
               cpte=new Compte();
                       
                    cpte.setNom("pascal");
                    cpte.setPassword("admin");
                      addBdd(cpte, true);
                      
                    cpte= rchCmpte("pascal","nom");
           }*/
           
           
        } catch (Exception ex) {
            
            Xcption.Xcption("GereCmpte GereCmpte() IOException",ex.getMessage());
        }
        
    }

 
    public List chTout(Object forml)
    {
        ArrayList chchTout=null;
        try {
            gKml.cEm();
             chchTout= gKml.getGereDonne().chchTout(forml.getClass().newInstance());
            
        } catch (Exception ex) {
            Xcption.Xcption(GereCmpte.class.getName(),ex);
            
        }
         return chchTout;   
    }
    
    public void verifBDD()
    {
        List<RsEntite> lresult=null;
        try {
            lresult=gKml.getGereDonne().chchTout(new Ligne());
                ArrayList<RsEntite> chchTout = gKml.getGereDonne().chchTout(new Pt());
            if(!lresult.equals(chchTout))
                lresult.addAll(chchTout);
            
        } catch (Exception ex) {
            
            Xcption.Xcption("GereCmpte verifBDD chchTout Exception",ex.getMessage());
        }
        if(lresult!=null)
        {
            for(RsEntite r:lresult)
            {
                charge.setTbverif(tbverif);
                RsEntite verifKml = (RsEntite) charge.verifKml(r);
                
            }
            
        }
    }
    private void supLock() throws IOException
    {
        
            Path jdkPath = Paths.get("/home/runsense/index");
            DirectoryStream<Path> stream = Files.newDirectoryStream(jdkPath);
            try { 
              Iterator<Path> iterator = stream.iterator();
              while(iterator.hasNext()) {
                Path p = iterator.next();
                System.out.println(p);
                try{
                stream = Files.newDirectoryStream(p);
                
                Iterator<Path> iterssdossier = stream.iterator();
                    while(iterssdossier.hasNext()) 
                    {
                        Path chsup = iterssdossier.next();
                        Path filech = chsup.getFileName();
                        Path pth = filech.getName(0);
              
                            if(pth.toString().equals("write.lock"))
                            {
                                File file = filech.toFile();
                                    file.delete();
                            }
                    }
                }catch(java.nio.file.NotDirectoryException ndex)
                {
                   
                    Xcption.Xcption("GereCmpte supLock NotDirectoryException",ndex.getMessage());
                }
              }
            } finally { 
              stream.close(); 
            } 
          
    }
    
    public void createKml()
    {
        gKml.createKml(chg, null, schema);
    }
   
   public void dltUservue()
   {
      /* try{
       gKml.getGereDonne().hqlDelete("USERVUE");
       }catch(Exception ex)
       {
          
            Xcption.Xcption("GereCmpte dltUservue Exception",ex.getMessage());
       }*/
   }
   
   
    public List<String> complete(String query)
    {
        List<String> rslt=new ArrayList();
        
        Set<Ligne> slgn = Optional.ofNullable(
                Optional.ofNullable(cpte).get().getLgncpte()).
                get();
        if(slgn!=null)
            rslt.addAll(slgn.stream().
                filter(next->next.getNom().contains(query)).
                map(l->l.getNom()).collect(Collectors.toList()));
        else
            {
                
             ArrayList<RsEntite> rchBdd = gKml.rchBdd(query, "ligne", "aprx", "nom");
             rslt.addAll(rchBdd.stream().
                     map(rs->rs.getNom()).collect(Collectors.toList()));
            }
        
         Set<Pt> slpt = Optional.ofNullable(
                Optional.ofNullable(cpte).get().getPoint()).
                get();
        if(slpt!=null)
            rslt.addAll(slpt.stream().
                filter(next->next.getNom().contains(query)).
                map(l->l.getNom()).collect(Collectors.toList()));
        else
            {
                
             ArrayList<RsEntite> rchBdd = gKml.rchBdd(query, "point", "aprx", "nom");
             rslt.addAll(rchBdd.stream().
                     map(rs->rs.getNom()).collect(Collectors.toList()));
            }
       
        return rslt;
    }
  
    public TreeNode charge(Object chg,  boolean bserver)
    {
        /*gKml=new GereKml();
            gKml.cEm();*/
        try{
            gKml=new GereKml();
        if(chg.equals("Alt.kml"));
        {
            
            gKml.setbCoord(true);
        }
      
      String c="";
      File f=null;
      if(chg instanceof String) 
      {
          c="/home/runsense/sentier/";
        f=new File(c+chg);
     }else if(chg instanceof File)
     {
         f=(File) chg;
     }
        System.out.println(c+f.getAbsolutePath());
        chargeKml(f);
        
       f.delete();
        }finally
        {
            gKml=new GereKml();
        }
        return root;
    }
    public void dcpupload(Object kml)
    {
        Feature defini = charge.defini(kml);
    }
    
    
    public boolean addObject(Object o)
    {
            if(charge!=null)
             return !charge.addObject(o).equals(null);
            else
                return false;
    }
  
    public boolean chargePartie(TreeNode select)
    {
        //charge bdd
        
            /* gKml=new GereKml();
                gKml.cEm();*/
            System.out.println(select);
            charge.dcplance(select);
            
           // charge.closeEntityManager();
              
                    lrsentite=charge.getChgCpte();
     if(cpte!=null)               
       if(cpte.getNom().equals("kassiel"))
           charge.setCfm(true);
            
       if(charge.isCfm())
       {
            charge.addDonneCompte(cpte, lrsentite);
                   
              
           /* for(Tour t:lbddtour)            
                {
                    if(t!=null )
                    {
                       charge.addTour(t);
                      
                       charge.addDonneCompte(cpte,t);
                
                    }
                        
                };   */
                lbddtour=new ArrayList();
         
          
       }
       return charge.isCfm();
    }
    
    
   
    public Compte identification(String nom, String pswd)
    {
        gKml.cEm();
        ArrayList<Compte> rch = gKml.rchBdd(nom, "compte","fix", "nom");
        cpte=rch.stream().filter(hote->hote.getPassword().equals(pswd)).findFirst().get();
        
        gKml.fEm();
        return cpte;
    }
   
    public Compte creerUser(Compte hote) {
        gKml.cEm();
        try {
             gKml.boolcreateObj(true);
                cpte=(Compte) gKml.addBdd(hote);
            
        } catch (Exception ex) {
            
            Xcption.Xcption("GereCmpte creerCpte Exception",ex.getMessage());
        }
        gKml.fEm();
        return cpte;
    }
    
   /* 
    public Tour newTour(Tour tour)
    {
        
        Tour nvo=null;
        try{
        List<Tour> cptetour = cpte.getCptetour();
        if(cptetour!=null)
        {
            for(Iterator<Tour> iter=cptetour.iterator(); iter.hasNext();)
            {
                Tour next = iter.next();
                String nmTr = tour.getNmTour();
                Prmtr[] prmtrTour = (Prmtr[]) tour.getParametreTour().toArray();
                
                    if(next.getNmTour().contains(nmTr))
                    {
                        if(!prmtrTour.equals(next.getParametreTour()))
                        {
                            for(Prmtr prmtr: prmtrTour)
                            {
                                gDonne.createObj(prmtr, true);
                            }
                            gDonne.createObj(tour, false);
                           
                        }
                       
                    }else{
                        for(Prmtr prmtr: prmtrTour)
                            {
                                gDonne.createObj(prmtr, true);
                            }
                            gDonne.createObj(tour, true);
                    }
                    
                
            }
           
        }
        }catch(Exception ex)
        {
            System.err.print("GereCmpte newTour Exception");System.out.println(ex.getMessage());
        }
        return nvo;
    }*/

   
    public void creerContact(Contact contact)
    {
        try {
            gKml.boolcreateObj(true);
            gKml.addBdd(contact);
        } catch (Exception ex) {
            
               Xcption.Xcption("GereCmpte creerCpte Exception",ex.getMessage());
        }
    }
      public LineString trCoLg(List<Coord> lco, LineString geom)
    {
         
       
        
      /* try{ 
        HashSet<Coord> hashSet = new HashSet<Coord>(lco);
        lco=new ArrayList<Coord>(hashSet);
       }catch(org.hibernate.LazyInitializationException liex)
       {
           
       }*/
        
        Xcption.Xcption("lco",lco.toString());
         if(!lco.isEmpty())       
         {
            Map<Long, Coordinate> orderList = lco.stream().
                    collect(Collectors.toMap(co->co.getId(), Coordinates::CoordtoCoordinate));
           
            SortedSet<Long> sort=new TreeSet<Long>( orderList.keySet());
            sort.addAll(orderList.keySet());
               
            geom.createAndSetCoordinates().addAll(sort.stream().
                        map(orderList::get).collect(Collectors.toSet()));
        }
         
        
         Xcption.Xcption("geom",geom.toString());
        return geom;
    }
 
    public Object updateRs(Object nouv)
    {
        System.err.print("gCompte updateRs");
       if(cpte!=null)
        if(nouv instanceof Feature)
        {
            Feature f=(Feature) nouv;
            System.out.println(f.getName());
                charge.getMplcchg().remove(f.getName());
                nouv=charge.getMplcchg().put(f.getName(), (Placemark) f);
        }else
        {
            ChargeKml chg=new ChargeKml(gKml);
             if(nouv instanceof Tour)
            {
                Tour tour=(Tour) nouv;
    
                chg.addTour(tour);
            }
            else
            {
                nouv=chg.upRs((RsEntite) nouv);
            }
            System.out.println(nouv);
      
        }
        
        return nouv;
    }
    
  
    public Contact updateContact(Contact contact)
    {
         boolean cobj=true;
        boolean ccmt=true;
            for(Iterator<Contact> iterctc = cpte.getContact().iterator();iterctc.hasNext();)
            {
                Contact next = iterctc.next();
                if(next.getObjet().equals(contact.getObjet()))
                {
                    if(next.getComment().equals(contact.getComment()))
                    {
                        ccmt=false;
                    }
                    cobj=false;
                }
            }
            if(cobj)
            {
                try{
                    gKml.boolcreateObj(true);
                    gKml.addBdd(contact); 
                }catch(Exception ex)
                {
                    
                    Xcption.Xcption("GereCmpte updateContact Exception", ex.getMessage());
                }
            }
        
        
        
        return contact;
    }

    public boolean deleteContact(Contact contact)
    {
        boolean deleteObject=false;
        try {
             deleteObject = gKml.supBdd(contact);
             cpte.getContact().remove(contact);
             gKml.boolcreateObj(false);
            gKml.addBdd(cpte );
            return deleteObject;
        } catch (Exception ex) {
            Logger.getLogger(GereCmpte.class.getName()).log(Level.SEVERE, null, ex);
        }
        return deleteObject;
    }

  
    public Compte rchCmpte(Object rc, String field)
    {
         cpte=null;
        try{
        ArrayList rch=null;
                    
             rch = gKml.rchBdd(rc, "compte","fix", field);
             if(!rch.isEmpty())
                 cpte=(Compte) rch.get(0);
           
        }catch(Exception ex)
        {
           
            Xcption.Xcption("GereCompte rchCmpte Exception ", ex.getMessage());
            cpte=null;
        }
        
        return cpte;
    }

    public Contact rchContact(Contact ctc, String field)
    {
        ArrayList rch = gKml.rchBdd(ctc.getId(), "contact","fix","idcpte");
        if(!rch.isEmpty())
        {
            ctc = (Contact) rch.get(0);
        }
        
        return ctc;
    }
    
   
  
    public void chargeKml(File fichier) 
    {//boolean[]: 0nmad 1nmdesc 2nmco 3adco 4addesc 5descco

        
        Kml kml = null;
           
         try{
             
           
                kml = Kml.unmarshal(fichier);
                document = (de.micromata.opengis.kml.v_2_2_0.Document)kml.getFeature();
            

         
         charge=new ChargeKml(document,gKml);
         
         
         
         charge.lancenew();
            Folder get = charge.getMdchg().get(Variable.getLieu());
         
       root=charge.getRoot();
         }catch(NullPointerException npe)
         {
            
             Xcption.Xcption("NullPointerException", npe.getMessage());
        
         }catch(Exception ex)
         {
             System.err.println("Systeme de fichier ne correspondant pas a la norme : \"http://www.opengis.net/kml/2.2\""+ex.getMessage());
             
         }
	        
                
       
    }
    
    
    
    public boolean supBdd(Object entite)
    {
        long id = 0;
        if(entite instanceof RsEntite)
        {
            RsEntite rs=(RsEntite) entite;
            id=rs.getId();
        }else if(entite instanceof IntBaz)
        {
            IntBaz ib=(IntBaz) entite;
            id=ib.getId();
        }
        gKml.getGereDonne().createEM();
       return gKml.supBdd(entite);
    }

    
    public Compte getCpte() {
        return cpte;
    }

    
    public void setCpte(Compte cpte) {
        this.cpte = cpte;
    }

    
    public ChargeKml getCharge() {
        return charge;
    }

    
    public void setCharge(ChargeKml charge) {
        this.charge = charge;
    }
    
    
    public List<de.micromata.opengis.kml.v_2_2_0.gx.Tour> getLtour() {
        return ltour;
    }

    
    public void setLtour(List<de.micromata.opengis.kml.v_2_2_0.gx.Tour> ltour) {
        this.ltour = ltour;
    }

    
    public List<Tour> getLbddtour() {
        return lbddtour;
    }

    public void setLbddtour(List<Tour> lbddtour) {
        this.lbddtour = lbddtour;
    }

    public List<String> getTbverif() {
        return tbverif;
    }

    
    public PrmtrSchm getSchema() {
        return schema;
    }

    
    public void setSchema(PrmtrSchm schema) {
        this.schema = schema;
    }

    
    public void setTbverif(List<String> tbverif) {
        this.tbverif = tbverif;
    }
    
   /* public void dcpchg(TreeNode  noeud)
    {
        
        List<TreeNode> lparent= new ArrayList<>();
        TreeNode inter=noeud;
        while(!lparent.contains(root))
        {
            inter=inter.getParent();
            lparent.add(inter);
            
        }
        
        charge.dcplance(noeud, ltour);
        //supNode(lparent.size(), root, null);
        
    }*/
    
    public boolean supNode(TreeNode node)
    {
        boolean rmv=false;
        for(Tour t:lbddtour)
        {
            if(t.getNmTour().replaceAll("tour : ", "")
                    .equals(node.getData()))
                 rmv = lbddtour.remove(t);
        }
        ;
        return rmv;
    }
    
    public Object[] onNode(TreeNode node)
    {
        ltour=new ArrayList<de.micromata.opengis.kml.v_2_2_0.gx.Tour>();
         tempreponse = new Object[4];
         String data=(String) node.getData();
         
         Object kml =null;
        if(!node.getChildren().isEmpty()) 
          kml = charge.getMdchg().get(data);
        else
            kml = charge.getMplcchg().get(data);
        Kml msh=new Kml();
       TourRsns rsns = null;
       
        if(kml instanceof Placemark) 
        {
            
            Placemark pl=(Placemark) kml;
            tempreponse[2]=pl;
           msh.setFeature(pl);
           rsns=new TourRsns(msh, pl,gKml);
        }else if(kml instanceof Folder)
        {
            Folder fl= (Folder) kml;
            tempreponse[2]=fl;
            msh.setFeature(fl);
            rsns=new TourRsns(msh, fl,gKml);
        }
       StringWriter stringWriter = new StringWriter();
                            msh.marshal(stringWriter);
                    tempreponse[0]=stringWriter.getBuffer().substring(0);        
       
                            //HabVue(rsns.getFeat());
                       de.micromata.opengis.kml.v_2_2_0.gx.Tour formeTour = null;
                        try{
                            formeTour = rsns.formeTour();
                            
                                ltour.add(formeTour);
                            if(lbddtour==null)
                                lbddtour=new ArrayList<Tour>();
                            lbddtour.addAll(
                                    rsns.getLbddtour());
                        }catch(Exception ex)
                        {
                            System.out.println(ex.getMessage());
                        }
                        tempreponse[3]=formeTour;
                        msh.setFeature(formeTour);
                        stringWriter = new StringWriter();
                            msh.marshal(stringWriter);
               tempreponse[1]=stringWriter.getBuffer().substring(0);
               
               
               Xcption.Xcption("tour ",tempreponse[1].toString());
               lbddtour=rsns.getLbddtour();
             System.out.println(tempreponse[1]);
       return tempreponse;
    }
}
