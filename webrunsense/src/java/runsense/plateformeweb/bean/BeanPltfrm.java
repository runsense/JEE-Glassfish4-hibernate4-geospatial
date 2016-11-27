/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.bean;

import com.google.common.collect.Lists;
import run.ejb.entite.util.kml.ChargeKml;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.NativeUploadedFile;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import run.ejb.base.Variable;

import run.ejb.ejbkml.GereCmpte;
import run.ejb.ejbkml.GerePOI;
import run.ejb.ejbkml.GerePOILocal;
import run.ejb.entite.client.Compte;
import run.ejb.entite.client.Contact;
import run.ejb.entite.geo.Coord;
import run.ejb.entite.geo.Evnmt;
import run.ejb.entite.geo.Ligne;

import run.ejb.entite.geo.Prmtr;
import run.ejb.entite.geo.Pt;
import run.ejb.entite.geo.Tour;
import run.ejb.entite.geo.interf.RsEntite;
import run.ejb.entite.util.bdd.ChrSpe;

import run.ejb.util.ex.Xcption;
import runsense.plateformeweb.util.ImgLien;
import runsense.plateformeweb.util.NewActivite;


/**
 *
 * @author Selekta
 */
@ManagedBean(name = "BeanPltfrm")
@SessionScoped
public class BeanPltfrm implements Serializable
{
    private Boolean badmin=false;
    private int nbrevis;
    private List<String> ips;
    private List<String> nbreip;
    private List<String> dern;
    private List<Integer> qte;
    private int indexMenu=0;
    private Object[] onNode;
    private String chg;

    private Boolean bident=true;
    private Boolean bchg=false;
    private Boolean bdt=false;
    
    private String swhaut;
    

    private String res;
      
    private File upload;
    //@EJB
    //private GereKmlLocal gKml;
    //@EJB
    private GereCmpte gCmpte;
    @EJB
    private GerePOILocal gPoi;

    @ManagedProperty( value = "#{rchBean}")
    private final RchBean rchBean;
  
    private Compte cpte;
    
    private List<String> lrdbtn;
    private List<Ligne> lfltTbLgn; 
    private List<Coord> lfltTbCo;
    private List<Pt> lfltTbPt;
    private List<Evnmt> lfltTbEv;
 
    private String gbool="tour";
    private boolean blg;
    private Ligne lgn;
    private List<Ligne> llg;
  
    
    private boolean blpt;   
    private List<Pt> lpt;
    private Pt pt;
    private Coord coord;
    private List<Coord> lco;
  
    private boolean blmu;  

    
    private boolean blevnmt;
    private List<Evnmt> levnmt;
    private Evnmt evnmt;
    
     private boolean bltr=true;
    private List<Tour> lvueTour=new ArrayList<Tour>();
    private List<Tour> lfltTbtr; 
       private Tour tour;
    private List<Prmtr> lprmtr;
    private List<Prmtr> lfltTbpmr; 
    private Prmtr tabpmr;
    

    private NewActivite activite;

    private List<Contact> lctc;
    
    private RsEntite entite=new Ligne();

    
    private String trKml;
    private String tourKml;
    
    private   String latkml;    
    private   String lngkml;
    
    private  TreeNode node;
    private TreeNode[] select;

    private List<String> tbverif=new ArrayList();
    
    private String nom;
    private String pswd;
    
    private String temp;
    private List ltemp;
    private ArrayList<Placemark> detail;
    
    private Placemark plcmk;
 
    private String critere="";
    private StreamedContent dwnld;
    

    
    
    public BeanPltfrm() 
    {
       
        
        activite=new NewActivite();
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
             rchBean = (RchBean) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null,"rchBean");
             rchBean.clearGkml();
       
           // repbean.setLring(new ArrayList<String>());
        gCmpte=new GereCmpte();
        setCpte(gCmpte.getCpte());
        //compte vue
        
   
        evnmt=new Evnmt();
        
        //RequestContext.getCurrentInstance().execute("init();");
         
         
        
        
            
            
       /* lentite = gKml.rchBdd(res, "ligne", "fix"); 
        System.out.println(lentite.toString());
        entite=lentite.get(0);
       chCoord(); */
        
    }
    public void disetPlftrm()
    {
        rchBean.setpltfrm(false);
         rchBean.setLzone(new ArrayList(Variable.getRegion().keySet()));
    }
    public void download()
    {
        gPoi=new GerePOI() ;
        Map tableau=new HashMap<String, Object[]>();
            tableau.put("entete", new Object[] {"Information Activité", "Nom", "Description", "Adresse :"});
            tableau.put("vide", new Object[] {"", "", "", ""});
        gPoi.createTab("creation", tableau);
        dwnld=new DefaultStreamedContent( FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(pswd));
    }
    private void chgRdbtn()
    {
        lrdbtn=new ArrayList<String>();
        if(bdt)
        {
            lrdbtn.add("tour");
            if(!llg.isEmpty())
                lrdbtn.add("tracer");
            if(!lpt.isEmpty())
                lrdbtn.add("point");
        }else
        {
            lrdbtn.add("tour");
            lrdbtn.add("tracer");
            lrdbtn.add("point");
        }
    }
    public void chgIdxmenu(String numb)
    {
        indexMenu = Integer.parseInt(numb);
        
    }
    public void slPt()
    {
        rchBean.setZone(new ImgLien(entite.getNom(),""));
        rchBean.slcZone();
    }
    public void slVst()
    {
        rchBean.setZone(new ImgLien(entite.getNom(),""));
        rchBean.slcZone();
    }
    public void verifKml()
    {
        gCmpte.setTbverif(tbverif);
        gCmpte.verifBDD();
    }
    public void swtCh(String sw)
    {
        swhaut=sw;
    }
    public void vueCoord()
    {
   
        Ligne l=(Ligne) entite;
        lco=new ArrayList<Coord>(l.getCoords());
    }
    private void chgDonneCpte(Object forml)
    {
       
        /*gKml=new GereKml();
        gKml.cEm();*/
        
        llg=new ArrayList<Ligne>();
        lpt=new ArrayList<Pt>();lvueTour=new ArrayList<Tour>();
        levnmt=new ArrayList<Evnmt>();lctc=new ArrayList<Contact>();
            rchBean.setLzone(new ArrayList<ImgLien>());
       if(cpte.getNom().equals("kassiel"))
            try {
                badmin=true;
                
       
                
                   /* nbrevis=Statistique.getIncrementation();
                    ips=new ArrayList<String>(Statistique.getmConnect().keySet());
                    
                    for(String cle:Statistique.getmConnect().keySet())
                    {
                        TempUser tmpuser=(TempUser) gKml.rchBdd(cle, "TempUser", "fix", "rmtip").get(0);
                        nbreip.add(tmpuser.getRmtip());
                        if(!tmpuser.getLuve().isEmpty())
                            dern.add(tmpuser.getLuve().get(
                                tmpuser.getLuve().size()-1).getHeure());
                        qte.add(Statistique.getmConnect().get(cle).getNbre());
                    }*/
                if(forml instanceof Ligne)
                llg.addAll(gCmpte.chTout(forml));
               
                else if(forml instanceof Pt)
                lpt.addAll(gCmpte.chTout(forml));
                
                else if(forml instanceof Tour)
                lvueTour.addAll(gCmpte.chTout(forml));
                
                else if(forml instanceof Evnmt)
                levnmt.addAll(gCmpte.chTout(forml));
               
               
                
                else if(forml instanceof Contact)
                lctc.addAll(gCmpte.chTout(forml));
                
            
            } catch (Exception ex) {
                Xcption.Xcption("BeanPltfrm chgDonneCpte Exception",ex.getMessage());
            }
       else
       {
            List<ImgLien> lstr=new ArrayList();
             if(cpte.getLgncpte()!=null
                     &&forml instanceof Ligne)
             {

                 for(Ligne l:cpte.getLgncpte())
                 {

                   //  llg.add(l);
                     lstr.add(new ImgLien(l.getNom(),null));
                 }

                 rchBean.setLzone(lstr);

             }
             else if(cpte.getCptetour()!=null
                     &&forml instanceof Tour)
             {
                 lvueTour.addAll(cpte.getCptetour());
                for(Tour tr:cpte.getCptetour())
                   {
                       lstr.add(new ImgLien(tr.getNmTour(),null));
                   }
               if(!lvueTour.isEmpty())
               {
                   tour=lvueTour.get(0);
                   chDetTour();

               }else if(lvueTour.iterator().hasNext())
                   tour=lvueTour.iterator().next();


                 rchBean.getLzone().addAll(lstr);

             }else if(cpte.getPoint()!=null
                     &&forml instanceof Pt)
             {

                 for(Pt pt:cpte.getPoint())
                 {

                         lpt.add(pt);
                         lstr.add(new ImgLien(pt.getNom(),null));

                 }

                rchBean.getLzone().addAll(lstr);
             }
             else if(cpte.getEvnmtcpte()!=null
                     &&forml instanceof Evnmt)
             {

                 levnmt.addAll(cpte.getEvnmtcpte());
             }
            
             else if(cpte.getContact()!=null
                     &&forml instanceof Contact)
             {



                 for(Contact ctc:cpte.getContact())
                 {
                     lctc.add(ctc);

                 }

             }
       }
     
       
        //pour rsentite
        bdt=false;
    }
    public void chDetTour()
    {
        if(tour!=null )
         lprmtr= Lists.newArrayList(tour.getParametreTour());
        
       
    }
    public void vueKml(org.primefaces.event.SelectEvent sevt)
    {
        /*gKml=new GereKml();
        gKml.createKml( sevt.getObject(), null, null);*/
    }
    public void chDetLigne()
    {
        try{
        if(lgn!=null )
        {
            List rch=new ArrayList(2);List field=new ArrayList(2);
                rch.add(lgn.getNom());field.add("nom");
                rch.add(lgn.getAdresse());field.add("adresse");
                
           /* ArrayList<RsEntite> rchMulti = gCmpte.rchMulti(rch, new Ligne(), field, "MUST_MUST", null);
            Ligne l = null;
            if(!rchMulti.isEmpty())
            {
                 l=(Ligne) rchMulti.get(0);
                lco = Lists.newArrayList(l.getCoords());
            }
            
            
                gKml.getGereDonne().stLazy(l);*/
        }
        }catch(org.hibernate.LazyInitializationException liex)
        {
            Xcption.Xcption("org.hibernate.LazyInitializationException liex  ",liex.getMessage());
            
        }
       
    }
    public void unNode(NodeUnselectEvent event)
    {
      /*  if(event!=null)
        {
            TreeNode treeNode = event.getTreeNode();
            boolean supNode = gCmpte.supNode(treeNode);
        }*/
        
        //selection sur detail
         
    }
    public void onNode(NodeSelectEvent event)
    {
        TreeNode treeNode=null;
      if(event!=null)
         treeNode = event.getTreeNode();
      else
      {
          treeNode = node;
         // gCmpte.dltUservue();
      } 
        //création du tour


        onNode = gCmpte.onNode(treeNode);
        trKml=(String) onNode[0];
        tourKml = (String) onNode[1];
        
        detail=new ArrayList<Placemark>();
        crVisu(onNode[2]);
        crVisu(onNode[3]);
    }
    private void crvisufolder(Folder folder, ChargeKml trfm) {
        
        for(Feature feat:folder.getFeature())
                         {
                             if(feat instanceof Placemark)
                             {
                                 Placemark get = trfm.getMplcchg().get(
                                                        feat.getName());
                                 detail.add(get);
                             }
                             else if(feat instanceof Folder)
                                 crvisufolder((Folder)feat,trfm);
                         }
    }
    private void crVisu(Object noeud)
    {
        
        
        
         ChargeKml trfm=gCmpte.getCharge();
            if(trfm!=null) trfm.setBvisualise(true);

            if(noeud instanceof Folder)
            {
                //trfm.defini(noeud);
                for (Iterator<Folder> itPlcmk = trfm.getMdchg().values().iterator(); itPlcmk.hasNext();)
                {
                    Folder f = itPlcmk.next();
                            
                         crvisufolder(f,trfm);
                            

                }
                lvueTour=gCmpte.getLbddtour();
                if(!lvueTour.isEmpty())
                {
                    tour=lvueTour.get(0);
                    chDetTour();
                }
                
                bdt=true; 
            }else if(noeud instanceof Placemark)
            {
                
                    
                plcmk=(Placemark) noeud;

                            detail.add(plcmk);
                
            lvueTour=gCmpte.getLbddtour();
                for(Tour t:lvueTour)
                    if(t.getNmTour().contains(
                            plcmk.getAddress()))
                        tour=t;
            chDetTour();
                bdt=true; 

            }else if(noeud instanceof RsEntite)
            {

               
                    lvueTour=new ArrayList<Tour>();
                    levnmt=new ArrayList<Evnmt>();
                addRs((RsEntite) noeud);
                bdt=false; 

            }else if(noeud instanceof List)
            {
                List l=(List) noeud;
              if(l!=null)
               if(!l.isEmpty())
                 if(l.get(0) instanceof Tour)
                   {
                       lvueTour=new ArrayList<Tour>();
                       lvueTour.addAll(l);
                   }else if(l.get(0) instanceof Evnmt)
                   {
                       levnmt=new ArrayList<Evnmt>();
                       levnmt.addAll(l);
                   }else if(l.get(0) instanceof Ligne)
                   {
                       llg=new ArrayList<Ligne>();
                       llg.addAll(l);
                      
                   }else if(l.get(0) instanceof Pt)
                   {
                       lpt=new ArrayList<Pt>();
                       lpt.addAll(l);
                   }else if(l.get(0) instanceof Coord)
                   {
                       lco=new ArrayList<Coord>();
                       lco.addAll(l);
                   }
                    
                    
                
                
                bdt=false; 
                

            }else
            {
                
            }
       
          
          
        
        
    }
    private void addRs(RsEntite rs)
    {
        if(rs instanceof Ligne)
           gCmpte.addObject((Ligne) rs);
        else if(rs instanceof Pt)
           gCmpte.addObject((Pt) rs);
        else if(rs instanceof Evnmt)
           levnmt.add((Evnmt) rs);
        else if(rs instanceof Tour)
        {
            lvueTour.add((Tour) rs);
            tour=(Tour) rs;
            chDetTour();
        }
    }
    public void nvoUser()
    {
       
        cpte = gCmpte.creerUser(cpte);
    }
    public void ident()
    {
      if(nom!=null || !nom.equals("") 
              || pswd!=null || !pswd.equals(""))
      {
          
            cpte = gCmpte.identification(
                        nom, pswd);
      }
        
       if(cpte!=null)
       {
           chgDonneCpte(new Tour());
           bident=false;
           bdt=false;
           
              crVisu(cpte.getCptetour()); 
                
           /*crVisu(cpte.getLgncpte());
           crVisu(cpte.getPoint());
           
           if(cpte.getEvnmtcpte()!=null)
            crVisu(cpte.getEvnmtcpte());*/
          
           
       }
    }

    public void cComte()
    {
       // cpte = gCmpte.identification( nom, pswd);
                       
        if(cpte==null)
      {
          cpte=new Compte();
            cpte.setNom(nom);
            cpte.setPassword(pswd);
            cpte = gCmpte.creerUser(cpte);
            
      }else if(cpte.getId()==null)
      {
          cpte = gCmpte.creerUser(cpte);
      }
           if(cpte.getIdcpte()!=null)
            {
               
                bident=false;
            } 
        
        
        
    }

   
    public void chgUpload(FileUploadEvent event)
    {
       /* cpte=gCmpte.getCpte();
            gCmpte=new GereCmpte();*/
        
        synchronized(this)
        {
            
           /* cpte = gCmpte.identification(
                        cpte.getNom(), cpte.getPassword());*/
            File f;
               ltemp=new ArrayList();
               NativeUploadedFile uplfl=(NativeUploadedFile) event.getFile();
               //UploadedFile uplfl = (UploadedFile) event.getFile();

               DiskFileItem dfitm =new DiskFileItem(uplfl.getContentType(), "text/plain", false,Variable.getSv()+"/runsense/sentier/"+uplfl.getFileName(), 0, new java.io.File(System.getProperty("java.io.tmpdir")));
               f=new File(Variable.getSv()+"/runsense/sentier/"+uplfl.getFileName());
                   FileWriter fw;
                       try {
                           fw = new FileWriter(f);

                               fw.write(IOUtils.toString(uplfl.getInputstream()));
                           fw.close();
                            } catch (IOException ex) {
                           Logger.getLogger(BeanPltfrm.class.getName()).log(Level.SEVERE, null, ex);
                       }
                       upload=f;
               //cherche dossier fichier temp
                  /* Date now = new Date(System.currentTimeMillis());
                   File dossier=new File(Variable.getSv()+"/runsense/sentier");
                   File fichier;
                   List<File> lepld=new ArrayList<File>();
                   long entrer=0;
                       for(String tmp: dossier.list())
                       {
                           fichier=new File(dossier, tmp);

                       long lastModified = fichier.lastModified();

                       Date date = new Date(lastModified);

                           if(entrer<lastModified)
                           {
                               lepld.add(fichier);
                               entrer=lastModified;
                               upload=fichier;
                           }
                       }*/
                      // upload=lepld.get(0);
                       /*ltemp.add(upload.getName());
                       if(lepld.size()>1)
                           for(Iterator<File> iter=lepld.iterator(); iter.hasNext();)
                           {
                                File next = iter.next();

                                ltemp.add(next.getName());

                                if(upload.lastModified()<next.lastModified())
                                    upload=next;

                           }
                   */
                   //try{//Ecriture fichier disk 
                   /*InputStream entrer= event.getFile().getInputstream();
                   upload = new File(dossier, event.getFile().getFileName());

                    sortie= new FileOutputStream(upload);
                           int read=0;
                           byte[] tbyte=new byte[1024];
                           while(entrer.read(tbyte)!=-1)
                           {

                               read=tbyte.length;
                               if(read<1024)
                               {
                                 byte[] temp=new byte[read];  
                                 for(int i=0; i<read; i++)
                                 {
                                     temp[i]=tbyte[i];
                                 }
                                 sortie.write(temp,0,read);
                               }else
                               {
                                   sortie.write(tbyte,0,read);
                               }

                               tbyte=new byte[1024];

                           }
                           entrer.close();

                           sortie.flush();


               }catch(IOException io)
               {         
                   System.err.print("BeanPltfrm  chgUpload  IOException");System.out.println(io.getMessage());
               }finally {
                               try {
                                       if (sortie != null) {
                                               sortie.close();
                                       }
                               } catch (IOException e) {
                                       e.printStackTrace();
                               }
                       }
               */
               node=gCmpte.charge(upload,  false);

               //charge table détail
               Map<String, Placemark> mplcchg = gCmpte.getCharge().getMplcchg();

               Collection<Placemark> values = mplcchg.values();       
               detail = new ArrayList(values);

               if(node==null)
               {
                  bchg=false; 
               }else
                  bchg=true;
             if(detail!=null)
               bdt=true;
             else
                bdt=false; 
             bident=false;

                   List<TreeNode> children = node.getChildren();

                   int cpte=0;
                   select=new TreeNode[children.size()];
                for(Iterator<TreeNode> iterator = children.iterator();iterator.hasNext();)
                {
                    select[cpte]=iterator.next();
                    cpte++;
                }
                   onNode=select;

                  // onNode(null);
                   chargePartie();
        }
        
    }
    public void updateCo(CellEditEvent  event)
   {
        Coord get = lfltTbCo.get(event.getRowIndex());
        List envoie = new ArrayList();
        envoie.add(get);
        System.out.print(event.getColumn()+"    "+event.getOldValue()+"     "+event.getNewValue());
      Object updateRs = gCmpte.updateRs(envoie);
   }
   public void updatePt(CellEditEvent  event)
   {
      pt= lfltTbPt.get(event.getRowIndex());
      System.out.print(event.getColumn()+"    "+event.getOldValue()+"     "+event.getNewValue());
      Object updateRs = gCmpte.updateRs(pt);
   }
   public void updatePrmtr(CellEditEvent  event)
   {
       Prmtr prmtr=lfltTbpmr.get(event.getRowIndex());
      System.out.print(event.getColumn()+"    "+event.getOldValue()+"     "+event.getNewValue());
      Object updateRs = gCmpte.updateRs(prmtr);
   }
   public void updateTr(CellEditEvent  event)
   {
      tour= lfltTbtr.get(event.getRowIndex());
      System.out.print(event.getColumn()+"    "+event.getOldValue()+"     "+event.getNewValue());
      Object updateRs = gCmpte.updateRs(tour);
   }
    public void updateLg(CellEditEvent  event)
    {
       if(event!=null) 
       {
           String filterBy = (String) event.getColumn().getFilterBy();
            

            lgn = lfltTbLgn.get(event.getRowIndex());
            Ligne l=lgn;
            if(filterBy.equals("nom"))
            {
                l.setNom((String) event.getOldValue());
            }
            else if(filterBy.equals("adresse"))
            {
                l.setAdresse((String) event.getOldValue());
            }
            else if(filterBy.equals("description"))
            {
                l.setDescription((String) event.getOldValue());
            }


            if(l.getCptligne()==null)
            {
                l.setCptligne(new HashSet<Compte>());
            }
            
           
            if(l.getCoords()==null)
            {
                l.setCoords(new ArrayList<Coord>());
            }

            //chDetLigne();

             Object updateRs = gCmpte.updateRs(lgn);
             llg.remove(l);
             llg.add((Ligne) updateRs);
            Xcption.Xcption("lgn.getAdresse()",lgn.getAdresse());
           }
       
        chDetLigne();
    }

    public void updatenode()
    {
        Xcption.Xcption("updatenode",plcmk.getName()+"  "+plcmk.getDescription()+"  "+plcmk.getAddress());
        if(gCmpte.getCharge().getMplcchg().put(plcmk.getName(), plcmk)!=null)
        {
            Xcption.Xcption("§§§§mise a jour gCmpte.getCharge().getMplcchg()§§§§",plcmk.getName());
        }
        
    }
    public void chargePartie()
    {
        
    Xcption.Xcption("chargePartie()",select);
        
      if(select!=null)  
        for(TreeNode n:select)
        {
       /* ChargeKml charge = gCmpte.getCharge();
        cpte=gCmpte.getCpte();
            gCmpte=new GereCmpte();
        gCmpte.setCharge(charge);
        gCmpte.setCpte(cpte);*/
            if(gCmpte.chargePartie(n))
            {
                
               
                cpte=gCmpte.getCpte();
                 Xcption.Xcption("updatenode",n.getData());
                if(!node.getChildren().contains(n))
                {
                    if(node.getChildren().contains(n.getParent()))
                    {
                        int indexParent = node.getChildren().indexOf(n.getParent());
                        int indexrech = node.getChildren().get(indexParent)
                                               .getChildren().indexOf(n);
                        
                         node.getChildren().get(
                                           indexParent)
                                .getChildren().get(indexrech)
                                 .setSelectable(false);
                        /*for(TreeNode ne:node.getChildren())
                        {
                            ne.getChildren().remove(n);
                        }*/
                    }else
                    {
                        for(TreeNode ne:node.getChildren())
                        {
                            if(ne.getChildren().contains(n.getParent()))
                            {
                                ne.getChildren().get(
                                           ne.getChildren().indexOf(n.getParent()))
                                .setSelectable(false);
                                /*for(TreeNode nee:node.getChildren())
                                {
                                    nee.getChildren().remove(n);
                                }*/
                            };
                        }
                    };
                }else
                {
                    node.getChildren().get(
                                           node.getChildren().indexOf(n))
                            .setSelectable(false);
                }
                    
               // RequestContext.getCurrentInstance().update("plt_rght:t_node");
                
            }else
            {
                addMsg("problème puissance serveur, réïtérr l'opération");
            }
        }
      
      chgDonneCpte(new Tour());
        select=null;
       // RequestContext.getCurrentInstance().execute("window.location.reload()");
    }
   
     public void charge()
    {
        node=new DefaultTreeNode("root", null); 
      
        if(upload!=null)
            node=gCmpte.charge(upload, false);
       else
           // node=gCmpte.charge(selection,chg, tbool, bserver);
        
       if(node==null)
       {
          bchg=false; 
       }else
          bchg=true;
       /* if(node.getChildren().get(0).getData().equals("alt"))
        {
            select=(TreeNode[]) node.getChildren().toArray();
            onNode=select;
            
            onNode(null);
            chargePartie();
        }*/
      
    }
     public List<String> complete(String query) 
    {     
       
        return gCmpte.complete(query);
      
    }
    /* public List<RsEntite> handleSelect(SelectEvent slcevent)
     {
        List<RsEntite> lentite = null;
         if(slcevent!=null)
             res=(String) slcevent.getObject();
         if(res!=null)
         {
             if(res.isEmpty())
             {
                 
             }else
             {
                  lentite = gCmpte.(res);
             }
         }
         if(entite!=null)
         {
             System.err.print(" BeanPltfrm handleSelect");System.out.println(entite);
         }
         
         return lentite;
     }*/
     public void ajoutActivite()
     {
         Pt act=new Pt();
     
     }
     public void ajoutObjet()
     {
         gCmpte.addObject(entite);
         
     }
     public void modificationObjet(Object o)
     {
         if(o instanceof Placemark)
         {
             Placemark plcmk=(Placemark) o;
                
             if(critere!=null)
                 plcmk.setDescription(
                        critere+"_"+plcmk.getDescription());
             
             detail.remove(o);
             detail.add(plcmk);
         }
         /*add bdd
          * if(entite.getId()!=null)
         {
            if(gKml.addBdd(entite, false))
            {
                addMsg("Object Modifier dans la base de donner");
            }
         }else
         {
             for(int i=0;i<select.length; i++)
             {
                 Object data = select[i].getData();
                 if(data instanceof Placemark)
                 {
                     Placemark pl=(Placemark) data;
                     if(pl.getName().equals(entite.getNom())||pl.getAddress().equals(entite.getAdresse()))
                     {
                         Geometry geom= pl.getGeometry();
                         pl.setName(entite.getNom());
                         pl.setAddress(entite.getAdresse());
                         pl.setDescription(entite.getDescription());
                     }
                 }
             }
         }*/
     }
     public void suppObject()
     {
         if(entite.getId()!=null)
         {
             if(gCmpte.supBdd(entite))
                {
                    addMsg("Object supprimer dans la base de donner");
                }
         }else
         {
             for(int i=0;i<select.length; i++)
             {
                 Object data = select[i].getData();
                 if(data instanceof Placemark)
                 {
                     Placemark pl=(Placemark) data;
                     if(pl.getName().equals(entite.getNom())||pl.getAddress().equals(entite.getAdresse()))
                     {
                         select[i]=null;
                     }
                 }
             }
         }
        
     }
    /* public void handleSelectMod(SelectEvent event) 
     {
         
        if(event.getObject() instanceof String)
        {
          
          res = (String) event.getObject();
        }
        else
        {
            res = event.getObject().toString();
        }
        
     
        List<RsEntite> fix = gKml.createKml( res, null);
            trKml=gKml.getTracer();
       System.out.println("recherche sur :"+res);
        lentite = gKml.rchBdd(res, "ligne", "fix", "nom"); 
        lentite.addAll(gKml.rchBdd(res, "point", "fix", "nom"));
            
       System.err.println("Résultat recherche : "+lentite.get(0).getNom());
       entite=lentite.get(0);
       //chCoord();
       
       lcoord = new ArrayList<Coord>();
            if(entite instanceof Ligne)   
            {
                Ligne lg=(Ligne) entite;


                lcoord.addAll(lg.getCoords());
            }
            else if(entite instanceof Pt)
            {
                Pt pt=(Pt) entite;
                
                 lcoord.add(pt.getCoord());
            } 
     System.out.println("chCoord"+entite+"_"+lcoord.toString());
        rendu=true;
     }*/
         
   
  
    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public List<Ligne> getLlg() {
        return llg;
    }

    public void setLlg(List<Ligne> llg) {
        this.llg = llg;
    }

 

    public List<Pt> getLpt() {
        return lpt;
    }

    public void setLpt(List<Pt> lpt) {
        this.lpt = lpt;
    }

   

    public List<Evnmt> getLevnmt() {
        return levnmt;
    }

    public void setLevnmt(List<Evnmt> levnmt) {
        this.levnmt = levnmt;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String getChg() {
        return chg;
    }

    public void setChg(String chg) {
        this.chg = chg;
    }

    public RsEntite getEntite() {
        return entite;
    }

    public void setEntite(RsEntite entite) 
    {
        System.out.println("modification de l'objet "+entite.getDescription());
        //modifie la base
        
        this.entite = entite;

    }

    public String getTrKml() {
        return trKml;
    }

    public void setTrKml(String trKml) {
        this.trKml = trKml;
    }

    public String getTourKml() {
        return tourKml;
    }

    public void setTourKml(String tourKml) {
        this.tourKml = tourKml;
    }

    public TreeNode getNode() {
        return node;
    }

    public void setNode(TreeNode node) {
        this.node = node;
    }

    public TreeNode[] getSelect() {
        return select;
    }

    public void setSelect(TreeNode[] select) {
        this.select = select;
    }

    public Compte getCpte() {
        return cpte;
    }

    public void setCpte(Compte cpte) 
    {
        if(cpte!=null)
   
        this.cpte = cpte;
    }


    public List<String> getTbverif() {

        return tbverif;
    }

    public void setTbverif(List<String> tbverif) {
        this.tbverif = tbverif;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public List<Prmtr> getLprmtr() {
        return lprmtr;
    }

    public void setLprmtr(List<Prmtr> lprmtr) {
        this.lprmtr = lprmtr;
    }

    public List<Coord> getLco() {
        return lco;
    }

    public void setLco(List<Coord> lco) {
        this.lco = lco;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    
    public List<Tour> getLvueTour() {
        return lvueTour;
    }

    public void setLvueTour(List<Tour> lvueTour) {
        this.lvueTour = lvueTour;
    }

    public List<Contact> getLctc() {
        return lctc;
    }

    public void setLctc(List<Contact> lctc) {
        this.lctc = lctc;
    }

    public Boolean getBchg() {
        return bchg;
    }

    public void setBchg(Boolean bchg) {
        this.bchg = bchg;
    }
    
    

    public String getSwhaut() {
        return swhaut;
    }

    public void setSwhaut(String swhaut) {
        this.swhaut = swhaut;
    }

    public File getUpload() {
        return upload;
    }

    public void setUpload(File upload) 
    {

        this.upload = upload;
    }

    public Ligne getLgn() {
        return lgn;
    }

    public void setLgn(Ligne lgn) {
        this.lgn = lgn;
    }

    public Boolean getBident() {
        return bident;
    }

    public void setBident(Boolean bident) {
        this.bident = bident;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public List getLtemp() {
        return ltemp;
    }

    public void setLtemp(List ltemp) {
        this.ltemp = ltemp;
    }

    public ArrayList<Placemark> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<Placemark> detail) {
        this.detail = detail;
    }

    public Placemark getPlcmk() {
        return plcmk;
    }

    public void setPlcmk(Placemark plcmk) {
        this.plcmk = plcmk;
    }

    public int getIndexMenu() {
        return indexMenu;
    }

    public void setIndexMenu(int indexMenu) {
        this.indexMenu = indexMenu;
    }

    public Boolean getBdt() {
        return bdt;
    }

    public void setBdt(Boolean bdt) {
        this.bdt = bdt;
    }

    public String getCritere() {
        return critere;
    }

    public void setCritere(String critere) {
        this.critere = critere;
    }

    public Evnmt getEvnmt() {
        return evnmt;
    }

    public void setEvnmt(Evnmt evnmt) {
        this.evnmt = evnmt;
    }

    public NewActivite getActivite() {
        return activite;
    }

    public void setActivite(NewActivite activite) {
        this.activite = activite;
    }

    public Pt getPt() {
        return pt;
    }

    public void setPt(Pt pt) {
        this.pt = pt;
    }

    public List<Tour> getLfltTbtr() 
    {
        
        return lfltTbtr;
    }

    public void setLfltTbtr(List<Tour> lfltTbtr) {
        this.lfltTbtr = lfltTbtr;
    }

    public List<Prmtr> getLfltTbpmr() {
        return lfltTbpmr;
    }

    public void setLfltTbpmr(List<Prmtr> lfltTbpmr) {
        this.lfltTbpmr = lfltTbpmr;
    }

    public List<Ligne> getLfltTbLgn() 
    {
        
        return lfltTbLgn;
    }

    public void setLfltTbLgn(List<Ligne> lfltTbLgn) {
        this.lfltTbLgn = lfltTbLgn;
    }

    public List<Coord> getLfltTbCo() {
        return lfltTbCo;
    }

    public void setLfltTbCo(List<Coord> lfltTbCo) {
        this.lfltTbCo = lfltTbCo;
    }

    public List<Pt> getLfltTbPt() {
        return lfltTbPt;
    }

    public void setLfltTbPt(List<Pt> lfltTbPt) {
        this.lfltTbPt = lfltTbPt;
    }

    public List<Evnmt> getLfltTbEv() {
        return lfltTbEv;
    }

    public void setLfltTbEv(List<Evnmt> lfltTbEv) {
        this.lfltTbEv = lfltTbEv;
    }

    public Prmtr getTabpmr() {
        return tabpmr;
    }

    public void setTabpmr(Prmtr tabpmr) {
        this.tabpmr = tabpmr;
    }

    public String getLatkml() {
        return latkml;
    }

    public void setLatkml(String latkml) {
        this.latkml = latkml;
    }

    public String getLngkml() {
        return lngkml;
    }

    public void setLngkml(String lngkml) {
        this.lngkml = lngkml;
    }

    public boolean isBlg() {
        return blg;
    }

    public void setBlg(boolean blg) {
        this.blg = blg;
    }

    public boolean isBlpt() {
        return blpt;
    }

    public void setBlpt(boolean blpt) {
        this.blpt = blpt;
    }

    public boolean isBlmu() {
        return blmu;
    }

    public void setBlmu(boolean blmu) {
        this.blmu = blmu;
    }

    public boolean isBlevnmt() {
        return blevnmt;
    }

    public void setBlevnmt(boolean blevnmt) {
        this.blevnmt = blevnmt;
    }

    public boolean isBltr() {
        return bltr;
    }

    public void setBltr(boolean bltr) {
        this.bltr = bltr;
    }

    public String getGbool() {
        return gbool;
    }

    public void setGbool(String gbool) {
        this.gbool = gbool;
    }

    public Boolean getBadmin() {
        return badmin;
    }

    public void setBadmin(Boolean badmin) {
        this.badmin = badmin;
    }

    public int getNbrevis() {
        return nbrevis;
    }

    public void setNbrevis(int nbrevis) {
        this.nbrevis = nbrevis;
    }

    public List<String> getIps() {
        return ips;
    }

    public void setIps(List<String> ips) {
        this.ips = ips;
    }

    public List<String> getNbreip() {
        return nbreip;
    }

    public void setNbreip(List<String> nbreip) {
        this.nbreip = nbreip;
    }

    public List<String> getDern() {
        return dern;
    }

    public void setDern(List<String> dern) {
        this.dern = dern;
    }

    public List<Integer> getQte() {
        return qte;
    }

    public void setQte(List<Integer> qte) {
        this.qte = qte;
    }

    public List<String> getLrdbtn() {
        return lrdbtn;
    }

    public void setLrdbtn(List<String> lrdbtn) {
        this.lrdbtn = lrdbtn;
    }

    public StreamedContent getDwnld() {
        return dwnld;
    }

    public void setDwnld(StreamedContent dwnld) {
        this.dwnld = dwnld;
    }

    public void vrfRnederer()
    {
        bident=false;
        bltr=false;
        blpt=false;
        blmu=false;
        blg=false;
        blevnmt=false;
        
            try {
          ChrSpe decrypt=new ChrSpe();
        if(gbool.equals("tracer"))
        {
            
            chgDonneCpte(new Ligne());
           
            
            for(Ligne lgn:llg)
            {
                if(lgn.getNom()!=null)
                    lgn.setNom( 
                        decrypt.chrchUTF8(lgn.getNom()));
                else if(lgn.getDescription()!=null)
                    lgn.setDescription(
                        decrypt.chrchUTF8(lgn.getDescription()));
            }
           if(!llg.isEmpty())
            lgn=llg.get(0);
                blg=true;
                
        }else if(gbool.equals("point"))
        {
            chgDonneCpte(new Pt());
            for(Pt pt:lpt)
            {
                if(pt.getNom()!=null)
                    pt.setNom(
                        decrypt.chrchUTF8(pt.getNom()));
                else if(pt.getDescription()!=null)
                    pt.setDescription(
                        decrypt.chrchUTF8(pt.getDescription()));
            }
                blpt=true;
        }else if(gbool.equals("tour"))
        {
            chgDonneCpte(new Tour());
            for(Tour tr:lvueTour)
            {
                if(tr.getNom()!=null)
                    tr.setNom(
                        decrypt.chrchUTF8(tr.getNom()));
                
            }  
                bltr=true;
        }else if(gbool.equals("evenement"))
        {
            chgDonneCpte(new Evnmt());
            for(Evnmt evnmt:levnmt)
            {
                if(evnmt.getNom()!=null)
                    evnmt.setNom(
                        decrypt.chrchUTF8(evnmt.getNom()));
                else if(evnmt.getDescription()!=null)
                    evnmt.setDescription(
                        decrypt.chrchUTF8(evnmt.getDescription()));
            }
                blevnmt=true;
                
        }
       } catch (Exception ex) {
                Xcption.Xcption("BeanPltfrm chgDonneCpte Exception",ex.getMessage());
            }
    }

    public void addMsg(String msg)
    {
        FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, msg,"du fichier:"));
    }

    
}
