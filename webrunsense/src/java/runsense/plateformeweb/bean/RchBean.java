/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.bean;

import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.omnifaces.util.Faces;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.chart.CartesianChartModel;
import run.ejb.base.CherchTemp;
import run.ejb.base.Classmt;
import run.ejb.base.Statistique;
import run.ejb.base.Variable;
import run.ejb.base.entite.TempKml;
import run.ejb.ejbkml.GereItiLocal;
import run.ejb.ejbkml.GereKml;
import run.ejb.ejbkml.GereKmlLocal;
import run.ejb.entite.climat.Meteo;
import run.ejb.entite.climat.PrevMt;
import run.ejb.entite.geo.Coord;
import run.ejb.entite.geo.Evnmt;
import run.ejb.entite.geo.Ligne;
import run.ejb.entite.geo.Pt;
import run.ejb.entite.geo.Tour;
import run.ejb.entite.geo.interf.RsEntite;
import run.ejb.entite.util.kml.TourRsns;
import run.ejb.entite.util.runsense.UtilRsns;
import run.ejb.iti.entite.util.Itineraire;
import run.ejb.util.ex.Xcption;
import run.ejb.util.kml.schema.RsnsObj;
import run.ejb.util.rsentite.schema.AnalyzSchem;
import run.ejb.util.rsentite.schema.PrmtrSchm;
import run.ejb.util.traduction.RBint;
import runsense.plateformeweb.util.ImgLien;
import runsense.plateformeweb.util.ItiJsf;
import runsense.plateformeweb.util.TabRun;




/**
 *
 * @author Selekta
 */
@ManagedBean(name="rchBean")
@SessionScoped
public class RchBean implements Serializable
{
   private static String stat;
  
    private long id;
    private Object[] titres;
    
    private Map <String, String> lthm;
    private String theme;
    
    private ImgLien categ;
    private List<ImgLien> lctg;
   private List<ImgLien> lchxctg;
   private Boolean bssact=false;
   private Boolean bsszone=false;
   
    private Object[] varBean=new Object[6];
    private RsEntite slcent;
   
    private Boolean bdesc=false;
    private Boolean blien=false;
     private Boolean bevnmt=false;
    //reponse
    private ImgLien zone;
    private ImgLien ville;

    private  String res="";
    
    //trnsfert kml
    private  String trKml;  
    private  String tourKml;  
    private String clitoKml;
    
    private   String latkml;    
    private   String lngkml;
    private   String altkml;
   
   // @EJB
    private GereKmlLocal gKml;
    @EJB
    private GereItiLocal gereIti;
    //par appel
 
    private GrphElv graphBean;
 
   
    //variable climat
 
    private int multtab=100;
    //private List<Meteo> ltotcli;
    private Meteo cli;
    private List<PrevMt> lcli;
    private List<String> froz;
   
 
    private  List<ImgLien>lzone;
    private  List<ImgLien>lville;
   
    private SelectItem itm;
    
    private List<RsEntite> vue;
    
    
    private List<Evnmt> levnmt;
    private List<TabRun> ltabRun;
    
    private List<TabRun> lfltTbEvnt;
    private List<TabRun> lfltTbRun;
    
    private TabRun tabrun;
    
    private List<Coord> lCoord;
  
  // private ScheduleModel agenda;
     
    private Boolean btab=false;
  

    private  Boolean belev=false;
 
    private Boolean biti=false;
 
    private PrmtrSchm schema;//0zone 1villbase=new ArrayList()le 2rb 3res 4categ 
    private int[] categSchem;

   private Boolean bpltfrm=false;
   
   
   private List<CartesianChartModel> lmdlgrph;
   private CartesianChartModel mdlgrph;
   
   private Date calend;
    
    private ItiJsf itijsf;
    private Itineraire itineraire;
    private ArrayList<Itineraire> liti;

    private List lHotkey;
     private Object chHot;
    private RBint rb;
    private String lg;
    
   
    private  String remoteAddr;
    private TempKml chchKml;
    private String notif;
   
    private List<String> luv;

   
   
    private Set<Meteo> meteoDuJour;
    
    
    
     public RchBean() 
    {  
        //AnalyzSchem.SreInitmSpreM();
        gKml=new GereKml();
            //gKml.cEm();
         slcent=new Ligne();
         initLangue();
           
            
            
         theme=Variable.getLthm().get("sentier");
         lthm=Variable.getEsthm().collect(Collectors.toMap(e->rb.getString(e.getKey()), e->e.getValue()));
           /*for(String s: Variable.getLthm().keySet())
           {
               lthm.put(rb.getString(s), Variable.getLthm().get(s));
           }*/
           
            calend=new Date(System.currentTimeMillis());  
            categSchem=new int[2];
                categSchem[0]=0;categSchem[1]=0;
               
                 
                 
          /*Stream.of(Variable.getPrmtr().get(0),Variable.getPrmtr().get(5),Variable.getPrmtr().get(6),
                  Variable.getZact(),Variable.categSentier())
                  .forEach(c ->
                  lctg.add(
                            new ImgLien(rb.getString((String) c),getIconeCatg((String) c))));*/
            
       lctg= Stream.of(Variable.getPrcrs(),Variable.getPrmtr().get(5),Variable.getPrmtr().get(6),Variable.getZact()).
               map(c->new ImgLien(rb.getString(c),getIconeCatg(c))).collect(Collectors.toList());
       /*
       String get = Variable.getPrcrs();
       lctg.add(
       new ImgLien(rb.getString(get),getIconeCatg(get)));
       get = Variable.getPrmtr().get(5);
       lctg.add(
       new ImgLien(rb.getString(get),getIconeCatg(get)));
       get = Variable.getPrmtr().get(6);
       lctg.add(
       new ImgLien(rb.getString(get),getIconeCatg(get)));
       get = Variable.getZact();
       lctg.add(
       new ImgLien(rb.getString(get),getIconeCatg(get)));*/
          
              
              lchxctg=Variable.categSentier().stream().map(str -> 
                         new ImgLien(rb.getString(str),getIconeCatg(str))).collect(Collectors.toList());
                   /* for(String str:Variable.categSentier())
                        lchxctg.add(
                         new ImgLien(rb.getString(str),getIconeCatg(str)));*/
                   
           titres=Variable.getLtitre().stream().map(str->rb.getString(str)).toArray();      
           /*titres=new String[Variable.getLtitre().size()];
           
           int cpte=0;
           
                for(String str:Variable.getLtitre())
                    titres[cpte++]=rb.getString(str); */
     if(Variable.isBfe())
     {
       HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        
                    
        String sentier = request.getParameter(
                Variable.getPrmtr().get(0));
        String zact = request.getParameter("zoneActivite");
        String activite = request.getParameter("activite");
        String itineraire = request.getParameter("itineraire");

        
        String ad=request.getParameter("ad");
        if(ad!=null)
            ad=ad.replace("/", " ");
       final String fad=ad;
        if(fad!=null)
              {
                
                for(Map.Entry<String, List<String>> ent: Variable.getRegion().entrySet())
                {
                    if(AnalyzSchem.getmSpreM().isEmpty())
                        ent.getValue().stream().map(s->s.toLowerCase()).
                                filter(sf->sf.contains(fad.toLowerCase())).
                                forEach(str->ville=new ImgLien(
                                        rb.getString(str),AnalyzSchem.getmSpreM().get(str).getTemps()));
                    else
                        ent.getValue().stream().map(s->s.toLowerCase()).
                                filter(sf->sf.contains(fad.toLowerCase())).
                                forEach(str->ville=new ImgLien(
                                        rb.getString(str),str));
                    /*for(String str:ent.getValue())
                    {
                        if(str.toLowerCase().contains(ad.toLowerCase()))
                        {
                            if(AnalyzSchem.getmSpreM().isEmpty())
                                ville=new ImgLien(
                                    rb.getString(str),str);
                            else
                                ville=new ImgLien(
                                    rb.getString(str),AnalyzSchem.getmSpreM().get(str).getTemps());
                        }
                    }*/
                }
                if(ville==null)
                {
                    if(AnalyzSchem.getmSpreM().isEmpty())
                         zone=new ImgLien(ad, null);
                    else
                        zone=new ImgLien(ad, AnalyzSchem.getmSpreM().get(ad).getTemps());
                }
                
              }
        
        
        
         
                
        remoteAddr = Faces.getRemoteAddr();
        
       
        
            stat=String.valueOf(
                    Statistique.getIncrementation());
         Xcption.Xcption(" remoteAddr",remoteAddr);
         
      
         if(sentier!=null)
        {
            chchKml=null;
            res=sentier;
            categ=new ImgLien(Variable.getPrmtr().get(0), Variable.getPrmtr().get(0));
           
            categSchem[0]=3;
            categSchem[1]=1;
            lancement();
        }else if(activite!=null)
        {
            if(ville!=null)
            {
                categSchem[0]=2;
            }else if(zone!=null)
                categSchem[0]=1;
            else
                categSchem[0]=0;
            
            categSchem[1]=3;
            chchKml=null;
            if(Variable.getPrmtr().contains(activite))
            {
                categ=new ImgLien(activite, activite);
                lancement();
            }
            else
            {
                categ=new ImgLien(Variable.getPrmtr().get(5), Variable.getPrmtr().get(5));
                initCateg();
            }
            
            
            
            
        }else if(zact!=null)
        {
            
            chchKml=null;
            if(ville!=null)
            {
                categSchem[0]=2;
            }else if(zone!=null)
                categSchem[0]=1;
            else
                categSchem[0]=0;
            
            categSchem[1]=5;
              String nom = request.getParameter("nom");
              if(nom!=null)
                  nom=nom.replace("/", " ");
              if(nom!=null)
              {
                  categSchem[0]=3;
                  varBean[3]=nom;
              }
              
              
            
            Xcption.Xcption(" request zoneActivite",zact);
            Stream.of(Variable.getLzact()).filter(lz -> 
                lz.contains(zact)||lz.contains(rb.getReverse(zact)));
           /*for(String lz:Variable.getLzact()) 
            if(lz.contains(zact)||lz.contains(rb.getReverse(zact)))
            {
                res=rb.getString(zact);
                lancement();
            }*/
           
            if(res==null)
            {
                res=rb.getString(Variable.getZact());
                initCateg();
            }
            
            
        }else if(itineraire!=null)
        {
            Xcption.Xcption(" request itineraire",itineraire);
            
            res=itineraire;
            catchSouris();
        }else if(bpltfrm)
        {
             gKml.fEm();
             gKml=null;
        }else
        {
         /* if(CherchTemp.isBchgmto()
                  ||CherchTemp.getStemp()==null)  
          {*/
              res="";
          
          
                 
            varBean[5]=calend;
            lancement();
             meteoDuJour=schema.getLlpremeteo();
            initComp();
           /* if(!CherchTemp.getStmp().equals(chchKml))
           CherchTemp.setStmp(chchKml);*/
         
                    
            if(schema.getLschmvu().size()>1)
            {
                 if(Optional.of(schema.getLschmvu()).flatMap(lschmu->Optional.ofNullable(lschmu.get(1))).
                         flatMap(list->Optional.ofNullable(list.getPourKml())).isPresent())
                    slcent= schema.getLschmvu().get(1).getPourKml().get(0).get(0);
                 else if(!schema.getMvue().isEmpty())
                    slcent= schema.getMvue().values().iterator().next();
                 else 
                    slcent= new Ligne(null, null, null, "rien", null, null, null);
                /*if(schema.getLschmvu().get(1)!=null)
                  if( !schema.getLschmvu().get(1).getPourKml().get(0).isEmpty())
                      slcent= schema.getLschmvu().get(1).getPourKml().get(0).get(0);
                  else if(schema.getMvue().values().iterator().hasNext())
                      slcent= schema.getMvue().values().iterator().next();*/

            }else
            {// visite
                if(slcent==null)
                   slcent=new Pt();
                if(slcent.getNom()==null||slcent.getNom()=="")
                    slcent.setNom("dernières visite ");
              if(chchKml!=null)  
                slcent.setDescription(chchKml.getRch().replace("true", "").substring(4));



            }

        /*   CherchTemp.setStemp(chchKml);
          }else
          {
              chchKml=CherchTemp.getStemp();
              construireChKml();     
          }*/

        }
         
       /* luv=new ArrayList<String>();
        if(tmpuser==null)
            tmpuser=tmpu;
           
        if(!tmpuser.getLuve().isEmpty())
                luv.add( tmpuser.getLuve().get(tmpuser.getLuve().size()-1)
                        .getTmpk().getRch());*/
     }
    
            
    }
     public void clearGkml()
     {
         if(gKml!=null)
         {
             gKml.fEm();
             gKml=null;
         }
         
     }
  public String getIconeCatg(String ch)
  {
       final Map<String, String> micone = Variable.getMicone();
       
       ch=rb.getReverse(ch);
      String rtr="";
      try{
      rtr=micone.get(ch);
      if(rtr==null)
        if(ch.contains(Variable.getLtitre().get(14)))
        {
            rtr = micone.get(Variable.getLtitre().get(14));
        }
        else if(ch.contains(Variable.getLzact()[3]))
        {
            rtr = micone.get(Variable.getLzact()[3]);
        }else if(ch.contains(Variable.getPrmtr().get(5)))
        {
             rtr = micone.get(Variable.getPrmtr().get(5));
        }else if(ch.contains(Variable.getLtitre().get(5)))
        {
            
        }else
        {
            if(ch!="")
            {
                Xcption.Xcption(ch, "");
                  rtr = micone.get(ch);

                  if(rtr==null)
                  {
                      boolean bpfe=true;
                      for(Map.Entry<String, String> entry:micone.entrySet())
                             if(bpfe)
                                 if(ch.contains(entry.getKey()))
                              {
                                   rtr = micone.get(entry.getKey());
                                   bpfe=false;
                              }
                  }
                  

            }
        }

                  if(rtr==null)
                      rtr="";
                  else if(rtr.contains(Variable.getIcgoogle()))
                      rtr=rtr.replace(Variable.getIcgoogle(),"");
                  else if(rtr.contains(Variable.getIcrunsense()))
                  {
                      rtr=rtr.replace(Variable.getIcrunsense(),"");
                  }else
                      rtr="";

       
        
      }catch(Exception ex)
      {
          Xcption.Xcption("getIconeCatg", ex.getMessage());
      }
          
      return rtr;
  }
    private void addStat()
    {
        if(chchKml!=null)
        {
            
        /*    gKml=new GereKml();
            tmpuser = (TempUser) gKml.rchBdd(remoteAddr, "TempUser", "fix", "rmtip").get(0);
            UserVue usv=new UserVue(new Date(System.currentTimeMillis()).toGMTString(), chchKml);
            tmpuser.getLuve().add(usv);
  
                    gKml.addBdd(usv, true);
            tmpuser.getLuve().add(usv);
               
             gKml.addBdd(tmpuser, false);
             gKml.getGereDonne().finish();
             gKml.getGereDonne().createEM();
            if(luv==null)
             luv=new ArrayList();
            
            luv.add(usv.getTmpk().getRch());*/
        }
    }
    
   /* public void chgTemp()
    {
        for(UserVue uv:tmpuser.getLuve())
            if(uv.getHeure().equals(res))
                try {
                    tabCategList(uv.getTmpk().getLlgn());
                    tabCategList(uv.getTmpk().getLpt());
                } catch (Exception ex) {
                    System.err.print("RchBean chgTemp");System.out.println(ex.getMessage());
                }
    }*/
    @PreDestroy
    public void finishWeb()
    {
        
        Optional.ofNullable(gKml).get().
                fEm();
        System.gc();
    }
     public void changeLang(javax.faces.event.AjaxBehaviorEvent event)
     {
         final  Map<String, Meteo> mSpreM = AnalyzSchem.getmSpreM();
         chchKml=null;
         Xcption.Xcption("changeLang(javax.faces.event.AjaxBehaviorEvent",event.getBehavior());
         Locale local=null;
        Map<Object, Object> mlagu = Variable.getMlagu();
        
        if(mlagu.containsKey(lg))
        {
            if(mlagu.get(lg) instanceof String)
                local=new Locale((String) mlagu.get(lg));
            else if(mlagu.get(lg) instanceof Object)
            {
                String[] str=(String[])mlagu.get(lg);
                local=new Locale(str[0],str[1]);
            }
        }else
        {
                local=new Locale("en");
        }
         /*if(lg.equals("francais"))
            local=new Locale("fr");
         else if(lg.equals("english"))
            local=new Locale("en");
         else if(lg.equals("kréol"))
            local=new Locale("fr","re");
         else if(lg.equals("allemand"))
            local=new Locale("de");
         else if(lg.equals("espagnol"))
            local=new Locale("es");
         else if(lg.equals("portugais"))
            local=new Locale("pt");
         else if(lg.equals("chinois"))
            local=new Locale("zh");
         else if(lg.equals("tamoul"))
            local=new Locale("ta");*/
        
         rb =new RBint(
                 ResourceBundle.getBundle("langue."+lg, local));
         titres=Variable.getLtitre().stream().map(str->rb.getString(str)).toArray();
        /* titres=new String[Variable.getLtitre().size()];
         
                 rb =new RBint(
                 ResourceBundle.getBundle("langue."+lg, local));
         
            for(int i=0; i<Variable.getLtitre().size()-1; i++)
                titres[i]=rb.getString(
                        Variable.getLtitre().get(i));*/
            lzone=new ArrayList<ImgLien>();
            
         if(!mSpreM.isEmpty())
         {
             Variable.getRegion().keySet().stream().forEach(
                     tmp->lzone.add(new ImgLien(
                          rb.getString(tmp),
                          mSpreM.get(tmp).getTemps())));
         }else
         {
             Variable.getRegion().keySet().stream().forEach(
                     tmp->lzone.add(new ImgLien(
                          rb.getString(tmp),
                          null)));
         }
             
         /*for(String tmp:Variable.getRegion().keySet()) 
            if(!mSpreM.isEmpty()) 
                    lzone.add(new ImgLien(
                          rb.getString(tmp),
                          mSpreM.get(tmp).getTemps()));
             else
                  lzone.add(new ImgLien(
                          rb.getString(tmp),
                          null));*/
         
        List<String> vgrz = Variable.getRegion().get(
                rb.getReverse(lzone.get(0).getNom()));
          lville=new ArrayList<ImgLien>(vgrz.size());
          if(!mSpreM.isEmpty())
         {
             
             vgrz.stream().forEach(
                     tmp->lville.add(new ImgLien(
                          rb.getString(tmp),
                          Optional.ofNullable(mSpreM.get(tmp)).get()
                                  .getTemps())));
         }else
         {
             vgrz.stream().forEach(
                     tmp->lville.add(new ImgLien(
                          rb.getString(tmp),
                          null)));
         }
         /*for(String tmp:vgrz)   
            if(mSpreM.get(tmp)!=null) 
                    lville.add(new ImgLien(
                          rb.getString(tmp),
                          mSpreM.get(tmp).getTemps()));
             else
                  lville.add(new ImgLien(
                          rb.getString(tmp),
                          null));*/
         
         lctg = new ArrayList<ImgLien>(); 
         
         ArrayList<String> prmtr = new ArrayList(Variable.getPrmtr());
            lctg = new ArrayList<ImgLien>();  
            Stream.of(rb.getString(prmtr.get(0)),rb.getString(prmtr.get(5)),rb.getString(prmtr.get(6))).
                    forEach(p->lctg.add(new ImgLien(rb.getString(p),getIconeCatg(p))));
               

                 /*   lctg.add(
                            new ImgLien(rb.getString(prmtr.get(0)),getIconeCatg(prmtr.get(0))));  
                    lctg.add(
                            new ImgLien(rb.getString(prmtr.get(5)),getIconeCatg(prmtr.get(5))));
                    lctg.add(
                            new ImgLien(rb.getString(prmtr.get(6)),getIconeCatg(prmtr.get(6))));  
                    lctg.add(
                            new ImgLien(rb.getString(Variable.getZact()),getIconeCatg(Variable.getZact()))*/
              lchxctg=new ArrayList<ImgLien>();
                Variable.categSentier().forEach(p->
                        lchxctg.add(
                         new ImgLien(rb.getString(p),getIconeCatg(p))));
                /* lchxctg.add(
                         new ImgLien(rb.getString(prmtr.get(0)),getIconeCatg(prmtr.get(0))));
                 lchxctg.add(
                         new ImgLien(rb.getString(prmtr.get(1)),getIconeCatg(prmtr.get(1))));  
                 lchxctg.add(
                         new ImgLien(rb.getString(prmtr.get(2)),getIconeCatg(prmtr.get(2))));*/
                    
        // lthm=new HashMap<String, String>();
         lthm=Variable.getEsthm().collect(Collectors.toMap(e->rb.getString(e.getKey()), e->e.getValue()));
          /* for(String s: Variable.getLthm().keySet())
           {
               lthm.put(rb.getString(s), Variable.getLthm().get(s));
           }  */
           if(zone.getNom()!=null)
            varBean[0]=rb.getReverse(zone.getNom());
           if(ville.getNom()!=null)
            varBean[1]=rb.getReverse(ville.getNom());
            varBean[2]=rb;
           if(res!=null)
            varBean[3]=rb.getReverse(res);
            if(categ.getNom()!=null)
                varBean[4]=rb.getReverse(categ.getNom());
          if(slcent!=null)
          {
              slcent.setNom(
                rb.getString(slcent.getNom()));
              slcent.setDescription(
                rb.getString(slcent.getDescription()));
          }
        try {
            tabCateg();
        } catch (Exception ex) {
            Xcption.Xcption("exception tabchangelang",ex.getMessage());
        }
        varBean[2]=null;
               
                
                lancement();
     }
     private void initLangue()
    {
        
        Locale requestLocale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
        String lgu = requestLocale.getLanguage();
        if(lgu.equals("fr"))
            lgu="francais";
        else if(lgu.equals("en"))
            lgu="english";
        
        lg=lgu;
        rb =new RBint(
                ResourceBundle.getBundle("langue."+lgu, requestLocale));
        System.err.print(lgu);
        
    }
     
     
     public void befHotkey()
     {
        
         if(lHotkey!=null)
         {
             int indexOf = lHotkey.indexOf(chHot)-2;
             if(0<=indexOf)
                chHot=lHotkey.get(indexOf);
             
         }
     }
     public void aftHotkey()
     {
         
         if(lHotkey!=null)
         {
             int indexOf = lHotkey.indexOf(chHot);
             if(lHotkey.size()>indexOf)
             chHot=lHotkey.get(indexOf);
         }
     }
     public void slcLHotkey(String str)
     {
         char hot=str.toCharArray()[0];
         switch(hot)
         {
             case 'z':
                 lHotkey=lzone;
                 chHot=zone;
                 break;
             case 'v':
                 lHotkey=lville;
                 chHot=ville;
                 break;
             case 'c':
                 lHotkey=lctg;
                 chHot=categ;
                 break;
             case 'x':
                 lHotkey=lchxctg;
                 chHot=categ;
                 break;
             case 'b':
                 lHotkey=ltabRun;
                 chHot=tabrun;
                 break;
             case 't':
                 lHotkey=new ArrayList(lthm.keySet());
                 chHot=theme;
                 break;
         }
                 
     }
     public void traitIti()
     {
         Xcption.Xcption(" iti",itijsf.getIti());
         if(itijsf.getIti()!="null"&&!itijsf.getIti().isEmpty())
         {
             gereIti.setIti(itijsf.getIti());
                gereIti.traitIti();
                liti=gereIti.getLiti();
                trKml=gereIti.getTracer();
                latkml=gereIti.getLatlng()[0];
                lngkml=gereIti.getLatlng()[1];
         }
         
         
     }
     public void vuePortIti()
     {
         
         gereIti.vuePortIti(itineraire);
         latkml=gereIti.getLatlng()[0];
         lngkml=gereIti.getLatlng()[1];
         
         RequestContext.getCurrentInstance().execute("positionne()");
     }
   
    
     /* private void initRch()
      {
         lzone=new ArrayList<String>(
                 Variable.getRegion().keySet()); 
      }*/
     /* public void initCli()
      {
          lctg=new ArrayList<ImgLien>();
          if(ltotcli!=null)
          {
           
            for(int i=0; i<ltotcli.size(); i++)
            {
                
                lctg.add(ltotcli.get(i).getWoied());
                
            }
        
          }
      }*/
     public void lancement()
     {
         bdesc=true;
         blien=false;
         bevnmt=false;
         CherchTemp temp=new CherchTemp((GereKml) gKml);
        
       try {
         if(chchKml==null)
            { 
             schema=new PrmtrSchm((GereKml)gKml);
     
             varBean[0]= Optional.ofNullable(zone).map(z->rb.getReverse(z.getNom())).
                     orElse(null);
             varBean[1]= Optional.ofNullable(ville).map(v->rb.getReverse(v.getNom())).
                     orElse(null);
             varBean[4]= Optional.ofNullable(categ).map(c->rb.getReverse(c.getNom())).
                     orElse(null);
           /* if(zone!=null)
                varBean[0]=rb.getReverse(zone.getNom());
            else
                varBean[0]=null;
            
            if(ville!=null)
                varBean[1]=rb.getReverse(ville.getNom());
            else
                varBean[1]=null;*/
          
            varBean[3]=rb.getReverse(res);
            /*if(categ!=null)
               varBean[4]=rb.getReverse(categ.getNom());
            else
                varBean[4]=null;*/
            
            
        //init
            
             String chchr="";
         
           for(Object o:varBean)
                if(o!=null&&o!="")
                {
                    if(o instanceof Date)
                    {
                        chchr+= UtilRsns.datePourStringVue(
                                (Date) o);
                    }else
                        if(!chchr.contains((String)o))
                        {
                            chchr+=o;
                            
                        }
                }
            chchr+=true+lg;
            notif=(String.valueOf(categSchem[0])+String.valueOf(categSchem[1])+chchr).
                    replaceAll("/", "");
            // notif = notif.replaceAll("/", "");
            Xcption.Xcption(" lancement() clé TempKml",notif);
            
            
            try{
                boolean brmv=false;
                chchKml=temp.cherche(notif);
                
                if(chchKml!=null)
                {
                    
                  if(chchKml.getLmto()!=null)
                  {
                     if(!chchKml.getLmto().isEmpty())
                    {
                        brmv=chDate(brmv,new Date(chchKml.getLmto().iterator().next().getJour()));
                    }else
                         brmv=true;
                  }else if(!AnalyzSchem.getmSpreM().isEmpty())
                    {
                        schema.setLlpremeteo(new HashSet(AnalyzSchem.getmSpreM().values()));
                    }
                  else
                  {
                      brmv=true;
                  }
                    
                    
                }else if(!AnalyzSchem.getmSpreM().isEmpty())
                    {
                        brmv=chDate(brmv,new Date(AnalyzSchem.getmSpreM().values().iterator().next().getJour()));
                         
                    }else
                        brmv=true;
                
                if(brmv)
                {
                    if(meteoDuJour!=null)
                    {
                        schema.setLlpremeteo(meteoDuJour);
                    }else
                    {
                        chchKml=null;
                    }
                    temp.supbdd(notif);
                            barMsg();
                            
                }
                }catch(Exception ex)
                {

                }
            }
            
            /*if(chchKml==null)
              chchKml = CherchTemp.chchKml(""+categSchem[0]+categSchem[1]+
                    chchr+true+lg);*/
             
             Xcption.Xcption(" lancement() chchKml",notif);
             if(chchKml!=null)
                System.out.println(chchKml);
            if(chchKml==null)
            {
                varBean[2]=rb;
                
                schema.PrmtrSchm(categSchem[0], categSchem[1], varBean, true);
               gKml.cEm();
                if(!schema.getMvue().isEmpty())
                 gKml.createKml(null, null, schema);
                gKml.fEm();
                //clitoKml=gKml.getMeteo();
                trKml=gKml.getTracer();
                tourKml=gKml.geTour();
                Xcption.Xcption("trcer kml : ", trKml);
               
                        Xcption.Xcption("tour kml : ",tourKml);
                        System.out.println("tour et tracer creer");
               // CherchTemp.addKml(categSchem[0]+categSchem[0]+chchr+true+lg, new TempKml(trKml, tourKml, lids,lg));
                
          
              
                try{
                        Set tmpl=new HashSet();
                        Set tmpp=new HashSet();
                            
                        if(!schema.getMvue().isEmpty())
                        { 
                            /*for(RsEntite rs:schema.getMvue().values())
                                if(rs instanceof Ligne)
                                    tmpl.add(rs);
                                else if(rs instanceof Pt)
                                    tmpp.add(rs);*/
                            tmpl.add(schema.getMvue().values().stream().filter(rs->Ligne.class.getClass().isAssignableFrom(rs.getClass())).
                                          collect(Collectors.toList()));
                            tmpp.add(schema.getMvue().values().stream().filter(rs->Pt.class.getClass().isAssignableFrom(rs.getClass())).
                                      collect(Collectors.toList()));
                        }else
                         {
                           
                                    
                              tmpl.add(schema.getLschmvu().stream().filter(schema.prediLschm).
                                      map(rsns->rsns.getPourKml()).map(this::dcpLofLligne));
                               tmpp.add(schema.getLschmvu().stream().filter(schema.prediLschm).
                                      map(rsns->rsns.getPourKml()).map(this::dcpLofLpoint));       
                              /*for(RsnsObj rsns:schema.getLschmvu())
                                  if(rsns.getPourKml()!=null)
                                  for(List<RsEntite> lrs:rsns.getPourKml())
                                      for(RsEntite rs:lrs)
                                        if(rs instanceof Ligne)
                                            tmpl.add(rs);
                                        else if(rs instanceof Pt)
                                            tmpp.add(rs);*/
                         }
                    final Set<Meteo> ltotcli = new HashSet();
                    Optional.ofNullable(schema.getLlpremeteo()).
                            map(m->ltotcli.addAll(m)).
                            orElse(ltotcli.addAll(new HashSet(AnalyzSchem.getmSpreM().values())));
                       /* if(schema.getLlpremeteo()!=null)
                             ltotcli=schema.getLlpremeteo();
                        else
                            ltotcli=new HashSet(AnalyzSchem.getmSpreM().values());*/
           
                    TempKml persTmp=new TempKml(notif, trKml, tourKml, tmpl, tmpp);
                    persTmp.setLmto(ltotcli);
                    synchronized(this)
                    {
                        gKml=new GereKml();
                       
                        gKml.cEm();
                        temp=new CherchTemp((GereKml) gKml);
                         chchKml=temp.addTmp(persTmp);
                         gKml.fEm();
                    }
                }catch(org.hibernate.exception.DataException dex)
                {
                    Xcption.Xcption(dex.getMessage(),"lancement() trKml "+trKml.toCharArray().length+" tourKml "+tourKml.toCharArray());
                }
                
                  tabCateg();
              
            }else
            {
               // chchKml=temp.chchKml(chchKml);
              construireChKml();
                
            }
            
            
                  chchSclent(schema.getMvue().values().iterator().next());
            
           // addStat();
             
             
            } catch (Exception ex) {
                Xcption.Xcption(" RchBean() lancement()  ex ",notif+ex.getMessage());
            }
      
        varBean[2]=null;
     
        
     }
     private List<RsEntite> dcpLofLpoint(List<List<RsEntite>> llrs)
     {
         return llrs.stream().flatMap(l->l.stream().
                 filter(rs->Pt.class.getClass().isAssignableFrom(rs.getClass()))).
                 collect(Collectors.toList());
     }
     private List<RsEntite> dcpLofLligne(List<List<RsEntite>> llrs)
     {
         return llrs.stream().flatMap(l->l.stream().
                 filter(rs->Ligne.class.getClass().isAssignableFrom(rs.getClass()))).
                 collect(Collectors.toList());
     }
     
     private List<RsEntite> tmpwbcm;
    public void initCateg()
    {
        chchKml=null;
       /*if(bwebcam)
       {
           lwebCam=new ArrayList();
           for(int i=0; i<tmpwbcm.size(); i++)
           {
               if(tmpwbcm.get(i).getAdresse().contains(res))
               {                  
                   lwebCam.add( lwebCam.get(i));
               }
           }
           
       }else if(brch)
       { */
        
        if(categ!=null)
        {
            bssact=true;
           
            res=null;
        String scateg=rb.getReverse(categ.getNom());
       
         lchxctg=new ArrayList<ImgLien>();
         
        if(scateg.equals(Variable.getPrcrs())) 
         {
             Variable.categSentier().parallelStream().forEach(str->lchxctg.add(
                     new ImgLien(rb.getString(str),getIconeCatg(str))));
             /*for(String str:Variable.categSentier())
                 lchxctg.add(
                     new ImgLien(rb.getString(str),getIconeCatg(str)));*/
             
                categSchem[1]=1;
                res=scateg;//+"/"+prmtr.get(1)+"/"+prmtr.get(2)
               
         }else if(scateg.equals(Variable.getPrmtr().get(5)))
                 {
                     Variable.categActivite().parallelStream().forEach(cl->lchxctg.add(
                     new ImgLien(rb.getString(cl.getRef()),getIconeCatg(cl.getRef()))));
                /*for(Classmt cl:Variable.categActivite())
                    lchxctg.add(
                     new ImgLien(rb.getString(cl.getRef()),getIconeCatg(cl.getRef())));*/
           
                categSchem[1]=3;
               res=scateg;
                 }       
         else if(scateg.equals(Variable.getPrmtr().get(6)))
         {
            for(Object obj:Variable.categlieuDit())
            {
                if(obj instanceof String)
                    lchxctg.add(
                        new ImgLien(rb.getString((String) obj),getIconeCatg((String) obj)));
                else
                {
                    Classmt clsmt=(Classmt) obj;
                    lchxctg.add(
                        new ImgLien(rb.getString(clsmt.getRef()),getIconeCatg(clsmt.getRef())));
                }
            }
         
            res=Variable.getPrmtr().get(6)+"/"+Variable.getPrmtr().get(4);
         }else if(scateg.equals(Variable.getZact()))
         {
             
             for(String str:Variable.getLzact())
                 lchxctg.add(new ImgLien(rb.getString(str),getIconeCatg(str)));
             
             categSchem[1]=5;
             if(!Arrays.asList(Variable.getLzact()).contains(res))
               res=scateg;
         }
        varBean[5]=null;
        
        
         
         
         lancement();
         
         chchSclent(scateg);
        }else
            bssact=false;
            
       //} 
         //categ=rb.getString(categ);
    }
    public void barMsg()
     {
         if(AnalyzSchem.getmSpreM().isEmpty())
            AnalyzSchem.SreInitmSpreM();
         
     }
     public void chgCateg()
     {
         chchKml=null;
         String scateg=rb.getReverse(categ.getNom());
        
         lchxctg=new ArrayList<ImgLien>();
       List categlieuDit = Variable.categlieuDit();
       List<String> categSentier = Variable.categSentier();
       List<Classmt> categActivite = Variable.categActivite();
       
       if(categSentier.contains(scateg))
         {
          Variable.categSentier().stream().forEach(str->lchxctg.add(
                     new ImgLien(rb.getString(str),getIconeCatg(str))));
            /* for(String str:Variable.categSentier())
                 lchxctg.add(
                     new ImgLien(rb.getString(str),getIconeCatg(str)));*/
            
             res=scateg;
             categ=new ImgLien(scateg, getIconeCatg(scateg));
             categSchem[1]=1;
         }
         else {
           boolean pfe=true;
           for(Classmt clsmt:categActivite) 
           {
               if(scateg.equals(clsmt.getRef())||clsmt.getList().contains(scateg))
                   {
             
                        for(Object ob:categActivite.get(0).getList())
                            if(ob instanceof String)
                            {
                                lchxctg.add(
                                   new ImgLien(rb.getString((String) ob),getIconeCatg((String) ob))); 
                            }else
                            {
                                Classmt cls=(Classmt) ob;
                                lchxctg.add(
                                   new ImgLien(rb.getString((String) cls.getRef()),getIconeCatg(cls.getRef())));
                            }


                        categSchem[1]=3;
                      if(scateg.equals(clsmt.getRef())) 
                      {
                          categ=new ImgLien(Variable.getPrmtr().get(5), getIconeCatg(Variable.getPrmtr().get(5)));
                          res="";
                          for(int i=0;i<lchxctg.size()-1;i++)
                          {
                              res+=lchxctg.get(i).getNom()+"/";
                          }
                          res+=lchxctg.get(lchxctg.size()-1);


                      }else
                       {
                           res=scateg;
                           categ=new ImgLien(Variable.getPrmtr().get(5), getIconeCatg(Variable.getPrmtr().get(5)));

                       }
                      pfe=false;            
                    }
               
           }
           if(pfe)
               for(Object obj:categlieuDit) 
                {
                    if(obj instanceof Classmt)
                    {
                        Classmt clsmt=(Classmt) obj;
                    if(scateg.equals(clsmt.getRef())||clsmt.getList().contains(scateg))
                        {

                             for(Object ob:categActivite.get(0).getList())
                                 if(ob instanceof String)
                                 {
                                     lchxctg.add(
                                        new ImgLien(rb.getString((String) ob),getIconeCatg((String) ob))); 
                                 }else
                                 {
                                     Classmt cls=(Classmt) ob;
                                     lchxctg.add(
                                        new ImgLien(rb.getString((String) cls.getRef()),getIconeCatg(cls.getRef())));
                                 }


                             categSchem[1]=3;
                           if(scateg.equals(clsmt.getRef())) 
                           {
                               categ=new ImgLien(Variable.getPrmtr().get(6), getIconeCatg(Variable.getPrmtr().get(6)));
                               res="";
                               for(int i=0;i<lchxctg.size()-1;i++)
                               {
                                   res+=lchxctg.get(i).getNom()+"/";
                               }
                               res+=lchxctg.get(lchxctg.size()-1);


                           }else
                            {
                                res=scateg;
                                categ=new ImgLien(Variable.getPrmtr().get(6), getIconeCatg(Variable.getPrmtr().get(6)));

                            }
                           pfe=false;            
                         }
                    }else
                    if(scateg.equals(obj))
                    {
                        lchxctg.add(
                                   new ImgLien(rb.getString((String) obj),getIconeCatg((String) obj)));  
                        pfe=false;
                    }
                    
                }
           
           if(pfe)
                if(Arrays.asList(Variable.getLzact()).contains(scateg))
                    {

                        res=scateg;
                        categSchem[1]=4;

                        if(Variable.getLzact()[0].equals(res)||Variable.getLzact()[1].equals(res))
                           categ=new ImgLien(Variable.getZact(),getIconeCatg(Variable.getZact()));
                        else
                            categ=new ImgLien(Variable.getPrmtr().get(5), getIconeCatg(Variable.getPrmtr().get(5)));
                    }
       
          
          
     }
    varBean[5]=null;
          lancement();
          
          categ=new ImgLien(rb.getString(scateg), rb.getString(scateg));
          chchSclent(scateg);
          bsszone=false;
     
     /*public void insertdep(ValueChangeEvent event)
     {
         System.err.print("SESSion insertdep ");
         System.err.print(event.getNewValue());System.err.print(event.getOldValue());
         itijsf.setDep((String) event.getNewValue());
         
         itijsf.setIti("http://localhost:8080/webRunsense/itineraire.html?dep="+itijsf.getDep()+"&amp;?ar="+itijsf.getArv()+";");
         System.err.print("SESSion insertdep ");System.err.print(itijsf.getIti());System.err.print(itijsf.getDep());System.err.print(itijsf.getArv());System.err.print("SESSion inerer");
     }
     public void insertar(ValueChangeEvent event)
     {
         itijsf.setArv((String) event.getNewValue());
         
         itijsf.setIti("http://localhost:8080/webRunsense/itineraire.html?dep="+itijsf.getDep()+"&amp;?ar="+itijsf.getArv()+";");
         System.err.print("SESSion insertar ");System.err.print(itijsf.getIti());System.err.print(itijsf.getDep());System.err.print(itijsf.getArv());System.err.print("SESSion inerer");
     }*/
    /* public void chgCli(List<List<RsEntite>> listlienCli)
     {
         int id=0;
         for(RsEntite rs:vue)
         {
             if(rs.getNom().equals(zone))
                 idcli=id;
             else
                 id++;
         }
                 
         
         if(!ltotcli.isEmpty())
            lcli=ltotcli.get(id).getLprev();
         if(vue!=null)
         {
             
             lwebCam=new ArrayList(listlienCli.get(idcli));
             
                  for(int i=0;i<lwebCam.size()-1; i++)
                  {
                      if(lwebCam.get(i).getDescription().contains("liens"))
                      {
                          String rplc = lwebCam.get(i).getDescription().replace("liens", "");
                            /* if(rplc.contains("id "))
                             {
                                 String[] splitid = rplc.split("id ");
                                 rplc=splitid[0];
                                 RequestContext.getCurrentInstance().execute(
                                         "focIframe('"+splitid[1]+"');");
                             }
                          lwebCam.get(i).setDescription(rplc);
                      }else
                          lwebCam.remove(i);
                  }
                 
             
         }*/
     } 
     public void chgCalend(SelectEvent event)
     {
         varBean=new Object[6];
        calend= (Date) event.getObject();
        varBean[5]=calend;
        lancement();
        chchSclent(calend.toGMTString());
     }
     public void iticentre()
     {
         TourRsns frmTr=new TourRsns();
            
                tourKml = frmTr.ctourbyRs(itijsf.getRs());
            
         lanceTour(slcent, "Itinéraire de "+slcent.getDescription());
     }
     public void addItiJsf()
     {
         Ligne lgn=new Ligne();
         slcent=itijsf.getRs();
         if(slcent instanceof Ligne)
         {
             if(slcent.getNom().contains("Enter"))
             {
                 lgn=(Ligne) slcent;
             }
         }
                 
         if(lgn.getCoords()==null)
             lgn.setCoords(new ArrayList<>(2));
            
       ArrayList tmparr = new ArrayList(2);
         if(res.split("/")[1].equals("f_rch:iti_a"))
         {
             lgn.setNom("Enter arrivé");
             itijsf.setArv(res.split("/")[0].toLowerCase()
                     .replace('é', 'e').replace('î', 'i').replace('è', 'e').replace('â', 'a'));
             tmparr.add(new Coord(Double.parseDouble(latkml),Double.parseDouble(lngkml)));
         }
          else if(res.split("/")[1].equals("f_rch:iti_d"))
          {
              lgn.setNom("Enter départ");
              itijsf.setDep(res.split("/")[0].toLowerCase()
                      .replace('é', 'e').replace('î', 'i').replace('è', 'e').replace('â', 'a'));
              tmparr.add(0,new Coord(Double.parseDouble(latkml),Double.parseDouble(lngkml)));
          }else
              lgn.setNom(res);
         lgn.setCoords(new ArrayList(tmparr));
         slcent=lgn;
         itijsf.setRs(lgn);
         //  http://localhost:8080/webRunsense/itineraire
         itijsf.setIti("http://localhost:8080/webRunsense/itineraire.html?dep="+itijsf.getDep()+"&amp;?ar="+itijsf.getArv()+";");
        slcent.setDescription("départ de "+itijsf.getDep()+" arrivé à "+itijsf.getArv());
     }
     private String tmpdesc="";
     public void chgdesc()
     {
         
         if(!res.equals(tmpdesc))
         {
             
                tmpdesc=res;
             
             slcent=null;
         
            System.out.println(res);
                slcent = schema.getMvue().get(res);
              /*if(schema.getMvue().size()!=ltabRun.size())
              {
                  
                    
                  
              }else*/
                bdesc=false;blien=false;bevnmt=false;
                 if(slcent!=null)
                {
                   // fixtabRun(slcent);
                    slcent.setNom(
                        chtrad(slcent.getNom()));

                    if(slcent.getDescription().contains("lien"))
                    {
                         blien=true;
                        slcent.setDescription(
                            chtrad(slcent.getDescription().replace("liens ", "").replace("lien ", "")));
                       
        
                       // lwebCam=Lists.newArrayList(slcent);
                    }else if(slcent.getDescription().contains(Variable.getLzact()[3]))
                    {
                        bevnmt=true;
                        slcent.setDescription(
                            chtrad(slcent.getDescription()));
                    }else
                    {
                        bdesc=true;
                        slcent.setDescription(
                            chtrad(slcent.getDescription()));
                    }
                }else 
                {
                    
                    slcent=new Pt();
                    slcent.setNom(res);
                    
                }
         }
     } 
     
    
    /* public void vrfRenderer()
    {

        
        brch=false;
        bmet=false;
        belev=false;
        bwebcam=false;
        biti=false;
        
        if(gbool.equals("rech"))
        {
            lctg = new ArrayList<String>();  
         ArrayList<String> prmtr = new ArrayList(lvprmtr);
         
                lctg.add(
                        rb.getString(prmtr.get(0)));  
                lctg.add(
                        rb.getString(prmtr.get(5)));
                lctg.add(
                        rb.getString(prmtr.get(6))); 
            initRch();
        
                brch=true;
           
            //chchSclent();
            RequestContext.getCurrentInstance().execute("fetchKml('visite');");
        }else if(gbool.equals("webcam"))
        {
            //panneau webcam de l'objet schema
        
                chWebCam();
                
                bwebcam=true;
                
            RequestContext.getCurrentInstance().execute("fetchKml('visite');");    
        }else if(gbool.equals("elev"))
        {
            initGraph();
                belev=true;
        }else if(gbool.equals("iti"))
        {
            
                 biti=true;
               
                    gereIti=new GereIti();
                    chgIti();
                    RequestContext.getCurrentInstance().execute("additi('iti_d');");
        }
        else if(gbool.equals("méteo"))
        {
            List<RsEntite> ltmptom=new ArrayList();
            
            lwebCam=new ArrayList<RsEntite>();
            /*if(schema.getAnalyz()!=null)
            {
                if(ltotcli==null)
                    ltotcli=schema.getAnalyz().getLlpremeteo();
                for(String str:schema.getMvue().keySet())
                    if(str.contains(Variable.getPrmtr().get(10)))
                    {
                        RsEntite get = schema.getMvue().get(str);
                            get.setDescription(
                                    get.getDescription().replace("liens ", ""));
                        lwebCam.add(get);
                    }
            }
            else if(chchKml!=null)
            {
                if(ltotcli==null)
                     ltotcli=chchKml.getLmto();
                if(ltotcli==null)
                    for(Pt pt:chchKml.getLpt())
                    {
                        if(pt.getNom().contains(lvprmtr.get(10)))
                        {
                                pt.setDescription(
                                        pt.getDescription().replace("liens ", "").split("d")[0]);
                            lwebCam.add(pt);
                        }
                    }
                else
                    for(Pt pt:chchKml.getLpt())
                    {
                        pt.setDescription(
                              pt.getDescription().replace("liens ", "").split("d")[0]);
                        lwebCam.add(pt);
                        
                    }
            }
            
            
            
            
           // initCli();
           
                 bmet=true;
            RequestContext.getCurrentInstance().execute("fetchKml('visite');");
        }
      
    }*/
     private void chgIti()
     {
         /*private String itid;
            private String itia;
            private String iti;*/
         itijsf=new ItiJsf();
         if(slcent instanceof Pt)
         {
             Pt pt=(Pt) slcent;
                latkml=String.valueOf(pt.getLatitude());
                lngkml=String.valueOf(pt.getLongitude());
         }else if(slcent instanceof Ligne)
         {
             Ligne lgn=(Ligne) slcent;
             if(!lgn.getCoords().isEmpty())
             {
                 Coord next = lgn.getCoords().iterator().next();
                 latkml=String.valueOf(next.getLatitude());
                 lngkml=String.valueOf(next.getLongitude());
             }
                
         }
         
         
            
     }
 
 
 public List complete(String rqst)
    {
        
        
        vue = gKml.rchBdd(rqst,"ligne", "otocple", "nom");   
            vue.addAll(gKml.rchBdd(rqst,"point", "otocple", "nom"));
            
        List<String> nmtype=new ArrayList<String>(vue.size());
         
        for(RsEntite rs:vue)
            nmtype.add(rs.getNom());
        
       return nmtype;
    }
 public void chgComplete()
 {
     chchKml=null;
     slcent=null;
     for(RsEntite rs:vue)
         if(rs.getNom().equals(res))
             slcent=rs;
       
     gKml.createTracerbyRs(slcent);
     trKml=gKml.getTracer();
     lanceTour(slcent, slcent.getNom());
     tourKml=gKml.geTour();
      /*  rchTabCateg();
       lancement();*/
       
 }
    public void chargeDepuisCo()
    {
         Xcption.Xcption("ReponseBean chargeDepuisCo","oto.getLatkml() "+latkml+"oto.getLngkml() "+lngkml);
         Coord co=new Coord();
            co.setLongitude(
                    Double.valueOf(lngkml)
                    );
            co.setLatitude(
                    Double.valueOf(latkml)
                    );
         ArrayList<String> fields=new ArrayList<String>();
                fields.add("lat");fields.add("lng");
        List<RsEntite> rchMulti = gKml.rchMulti(co,(RsEntite) co, fields, "MUST_MUST",null);
            if(!rchMulti.isEmpty())
            {
                co=(Coord) rchMulti.get(0);
               // slcent=co.getLignes().iterator().next();
                
                //recherche sur la ville correspondante a la coordonnée
                fields=new ArrayList<String>();
                fields.add("nom");fields.add("adresse");
                gKml.rchBdd(slcent.getAdresse(), "ligne", "ad","adresse");
                
             try {
                 tabCateg();
             } catch (Exception ex) {
                 Xcption.Xcption("ReponseBean chargeDepuisCo Exception",ex);
         
             }
                
            }
       
    }
    private void rchTabCateg()
    {
        final  Map<String, Meteo> mSpreM = AnalyzSchem.getmSpreM();  
        String adresse = slcent.getAdresse();
       if(adresse.contains(Variable.getPrmtr().get(5)))
         {  //activité
            String[] split = adresse.split(" ");
            String str=split[split.length-1];
                ville=new ImgLien(str, str);
               categSchem[0]=3;categSchem[1]=3;
               
                        
        }else if(adresse.contains(Variable.getPrmtr().get(7)))
       {
           if(AnalyzSchem.getmSpreM().isEmpty())
                         zone=new ImgLien(res, null);
                    else
                        zone=new ImgLien(res, AnalyzSchem.getmSpreM().get(res).getTemps());
         
           ville=null;
           res="";
           categSchem[0]=1;
           categSchem[1]=0;
           // lville=new ArrayList<String>(Variable.getRegion().get(res));
           lville=new ArrayList<ImgLien>();
            for(String tmp:Variable.getRegion().get(rb.getReverse(zone.getNom())))   
            lville.add(new ImgLien(
                    rb.getString(tmp), tmp));
       }else if(adresse.contains(Variable.getPrmtr().get(8)))
       {
           ville=new ImgLien(res, res);
           res="";
           categSchem[0]=2;categSchem[1]=0;
           lville=new ArrayList<ImgLien>();
            for(Map.Entry<String,List<String>> entry:Variable.getRegion().entrySet())  
                if(entry.getValue().contains(rb.getReverse(ville.getNom())))
                    for(String str:entry.getValue())
                         if(!mSpreM.isEmpty()) 
                    lville.add(new ImgLien(
                          rb.getString(str),
                          mSpreM.get(str).getTemps()));
                else
                     lville.add(new ImgLien(
                          rb.getString(str),
                          null));
       }else if(adresse.contains(Variable.getPrmtr().get(0)))
                     {//sentier
                         res=slcent.getNom();
                         
                         categSchem[0]=3;categSchem[1]=1;
                         
                     }
                    else if(adresse.contains(Variable.getPrmtr().get(1)))
                    {//route
                        res=slcent.getNom();
                         categSchem[0]=3;categSchem[1]=1;
                    }else if(adresse.contains(Variable.getPrmtr().get(2)))
                    {//chemin
                        res=slcent.getNom();
                         categSchem[0]=3;categSchem[1]=1;
                    }else if(adresse.contains(Variable.getPrmtr().get(3)))
                    {//place
                         
                        res=slcent.getNom();
                         categSchem[0]=3;categSchem[1]=4;
                    }
                    else if(adresse.contains(Variable.getPrmtr().get(6)))
                    {//lieuDit
                         
                      res=slcent.getNom();
                         categSchem[0]=3;categSchem[1]=4;
                    }
                    else if(adresse.contains(Variable.getPrmtr().get(4)))
                    {//pointInteret
                        
                     res=slcent.getNom();
                         categSchem[0]=3;categSchem[1]=3;
                        
                    }
       
        
       String loc=adresse.split(Variable.getLieu())[1];
        List<String> asList = Arrays.asList(loc.split(" "));
       for(String region:Variable.getRegion().keySet())
        if(asList.contains(region))
        {
            if(AnalyzSchem.getmSpreM().isEmpty())
                         zone=new ImgLien(region, null);
                    else
                        zone=new ImgLien(region, AnalyzSchem.getmSpreM().get(region).getTemps());
            
            for(String str:Variable.getRegion().get(region))
                if(asList.contains(str))
                    ville=new ImgLien(str, str);
        }
        
    }
    private void rchCateg()
    {
       boolean bfe=false;
        res=rb.getReverse(res);
        String[] split = res.split("_");
        String strTmp=split[0];
      Xcption.Xcption( "rchCateg()", res);
      int indx=0;
     /*  Map<String, RsEntite> mvue = schema.getMvue();
      if(mvue.containsKey(strTmp))
         slcent=mvue.get(strTmp);  */   
        
      for(Map.Entry<String, RsEntite> mentry:schema.getMvue().entrySet())           
      {
           String key = mentry.getKey();
       if(key.replace(" ", "").contentEquals(strTmp.replace(" ", "")))
       {
           slcent=mentry.getValue();
          // fixtabRun(slcent);
           tabrun = ltabRun.get(indx);
           bfe=true;
           lanceTour(slcent, "visite de "+slcent.getNom() );
       }
       else
         indx++;
      }
    
     // finishTabRun();
           
    }
    public void preToMeteo()
    {
        final  Map<String, Meteo> mSpreM = AnalyzSchem.getmSpreM();
        System.err.print( "preToMeteo(Meteo pre)");
       Meteo meteo = mSpreM.get(res);
       if(meteo!=null)
           cli=meteo;
        System.out.print( cli);
        if(cli!=null)
        {
            
            //cli=(Meteo) gKml.find(cli, cli.getId());
            
            lcli = Lists.newArrayList(cli.getLprev());
            List<PrevMt> ltmp=new ArrayList(lcli.size());
            PrevMt get = lcli.get(0);
            lcli.get(0).setJour("demain");
            for(PrevMt prev:lcli)
            {
                //prev=(PrevMt) gKml.find(prev, prev.getId());
                String replace = prev.getJour().replace("\n", "").replace(" ", "");
                prev.setJour(rb.getString(replace)+" ");
                
                if(!ltmp.contains(prev))
                {
                    prev.setPre(trfBddprVueCli(prev.getPre()));
                    ltmp.add(prev);
                }
                    
            }
            lcli=ltmp;
            
             
             System.out.println(cli.getJour()+" "+cli.getWoied());
                             
        }
        
    }
    public void clickSouris()
    {
        
            res=res.split("_")[0];
            schema=new PrmtrSchm((GereKml)gKml);
     
                varBean[0]=rb.getReverse(zone.getNom());
                if(ville!=null)
                    varBean[1]=rb.getReverse(ville.getNom());
                else
                    varBean[1]=null;
                
                varBean[3]=rb.getReverse(res);
                varBean[4]=rb.getReverse(categ.getNom());
                
        //init
            
            

            String chchr="";
            for(Object o:varBean)
                if(o!=null&&o!="")
                {
                    if(o instanceof Date)
                    {
                        Date d=(Date) o;
                        chchr+=d.getYear()+d.getMonth()+d.getDay();
                    }else
                        chchr+=o;
                }
            chchr+=false+lg;
            notif=String.valueOf(categSchem[0])+String.valueOf(categSchem[1])+chchr;
            chchKml=null;
    synchronized(this){
        gKml=new GereKml();
            CherchTemp temp=new CherchTemp((GereKml) gKml);
                chchKml=temp.cherche(notif);
                gKml.fEm();
            
             Xcption.Xcption(" RchBean() chchKml",chchKml);
             if(chchKml==null)
            {
                varBean[2]=rb;
                schema.PrmtrSchm(categSchem[0], categSchem[1], varBean, false);
                gKml.createKml(null, null, schema);
                trKml=gKml.getTracer();
                tourKml=gKml.geTour();
                Set tmpl=new HashSet();
                Set tmpp=new HashSet();
                for(RsEntite rs:schema.getMvue().values())
                    if(rs instanceof Ligne)
                        tmpl.add(rs);
                    else if(rs instanceof Pt)
                        tmpp.add(rs);
                TempKml tempKml = new TempKml(notif,trKml, tourKml, tmpl,tmpp);
                    tempKml.setLmto(new HashSet(AnalyzSchem.getmSpreM().values()));
                
                 gKml.cEm();
                temp.addTmp(tempKml);
                 gKml.fEm();
            }else
            {
                RsnsObj rsnsObj = new RsnsObj("tmp", null,"tmp");
                 List ltmp = tmplRsEntite(); 
                    
                    rsnsObj.setPourKml(ltmp);
                 List lrsn=new ArrayList();
                    lrsn.add(rsnsObj);
                schema.setLschmvu(lrsn);
                if(chchKml.getTc()!="")
                {
                    trKml=chchKml.getTc();
                    tourKml=chchKml.getTr();
                }
                else 
                {
                    gKml.createKml(null, null, schema);
                        trKml=gKml.getTracer();
                        tourKml=gKml.geTour();
                }
                 
                //schema=chchKml.getSchema();
            }
      }
            try {
                addStat();
                tabCateg();
            } catch (Exception ex) {
                Xcption.Xcption("clickSouris tabCateg() ex "+notif,ex.getMessage());
            }
             
                slcent=schema.getLschmvu().get(0).getPourKml().get(0).get(0);
                for(TabRun tb:ltabRun)
                    if(tb.getNom().equals(slcent.getNom()))
                        tabrun = tb;
              

             trKml=gKml.getTracer();
            tourKml=gKml.geTour();
        
            
       
          varBean[2]=null;                          
        
        
    }
    private void lanceTour(RsEntite rs, String nm)
    {
        bdesc=false;blien=false;bevnmt=false;
        slcent=rs;
        gKml.cEm();
        ArrayList rchBdd = gKml.rchBdd("tour :"+slcent.getAdresse()+" "+slcent.getNom(), "tour", "fix","nmTour");
        gKml.fEm();
        if(!rchBdd.isEmpty())
        {
            Tour tour=(Tour) rchBdd.get(0);
            tour.setNom(nm);
            TourRsns frmTr=new TourRsns();
            
                tourKml = frmTr.fdonneTour(tour);
            String[] splitad = slcent.getAdresse().split(" ");
            String dcdVille = UtilRsns.dcdVille(splitad[splitad.length-1]);
            ville=new ImgLien(dcdVille, dcdVille);
            
            dcdVille = UtilRsns.dcdVille(splitad[splitad.length-2]);
            if(AnalyzSchem.getmSpreM().isEmpty())
                         zone=new ImgLien(dcdVille, null);
                    else
                        zone=new ImgLien(dcdVille, AnalyzSchem.getmSpreM().get(dcdVille).getTemps());
            
                
            if(!Variable.getLieu().contains(splitad[0]))
                categ=new ImgLien(splitad[0], getIconeCatg(splitad[0]));
            //trKml+=gKml.createTracerbyRs(rs);
           /* System.err.print("trKml catchTab");
            System.out.println(trKml);*/
            Xcption.Xcption("tourKml catchTab",tourKml);
            System.err.print(slcent.getDescription());
            //modTracerKml(rs);
          
        }else
            tourKml=gKml.formTourbyRs(rs);
        
        if(rs!=null)  
            if(slcent.getDescription().contains("http:"))
               {
                   slcent.setDescription(slcent.getDescription()
                           .split("webCam liens ")[1]
                           .split("id ")[0]
                           .replace(" ", "_"));
                   blien=true;
                   
               }else if(slcent.getDescription().contains(Variable.getLzact()[3])
                       ||slcent.getDescription().contains(Variable.getLzact()[4])
                       ||slcent.getDescription().contains(Variable.getLzact()[5]))
               {
                   levnmt = gKml.rchBdd(slcent.getNom(), "evenement", "fix", "lieux");
                   List<Evnmt> ltmp=levnmt;
                   levnmt=new ArrayList();
                   for(Evnmt evnmt:ltmp)
                       if(evnmt.getDt().after(new Date(System.currentTimeMillis())))
                           levnmt.add(evnmt);
                   if(!levnmt.isEmpty())
                    bevnmt=true;
                   else
                       bdesc=true;
               }else if(nm.contains(Variable.getPrmtr().get(10)))
               {
                   String ad;
                   if(!slcent.getAdresse().contains("http:"))
                       ad=slcent.getAdresse().replace("liens http ","http:");
                   else
                       ad=slcent.getAdresse().split("liens ")[1];
                   
                   slcent.setDescription(ad.split(Variable.getLieu())[0]
                                   .split("id ")[0]
                   );
                 
                   blien=true;
               }else
                   bdesc=true;
    }
    private void modTracerKml(RsEntite rs)
    {
        String rtr="";String aspliter;
        if(rs instanceof Ligne)
        {
            String fin="";
            
                aspliter="<Placemark id=\""+rs.getNom()+"\">\n" +
                        "                <name>"+rs.getNom()+"</name>\n" +
                        "                <address>"+rs.getAdresse()+"</address>";
                String[] split = trKml.split(aspliter);
               
                String str=split[1];
                String[] splitfin = str.split("</Placemark>");
                
               if(split.length>1)
                 str=split[1];
               else
               {
                   split =trKml.split("<Placemark id=\""+rs.getNom());
                   if(split.length>1)
                   {
                       str=split[1];
                       String[] tajtasplt=str.split("</address>");
                       
                        split[0]+=tajtasplt[0]+"</address>";
                        str+=tajtasplt[1];
                       
                   }  
               }
            str=splitfin[0];    
        for(int i=1; i<splitfin.length;i++)
            fin+="</Placemark>"+splitfin[i];
        
                
                String[] splitdeb = str.split("<color>");
                
                String colorChg=splitdeb[1];

                
                
            String substr = colorChg.substring(8);
            String[] splwidthchge = substr.split("<width>");
            
            String widthchg=splwidthchge[1];
            
            
            
             rtr+=split[0]+aspliter+splitdeb[0]+"<color>"+"5014B4FF"+splwidthchge[0]+"<width>"+5.4+widthchg.substring(3)+fin;
        
        trKml=rtr;
        }
        System.out.println(rtr);
        Xcption.Xcption("trKml modTracerKml",trKml);
    }
    public void catchTab()
    {     
        slcent=null;
       
        if(tabrun!=null)
       {
            Map<String, RsEntite> mvue = schema.getMvue();
                id=(int) tabrun.getId();
           
            String nm = rvsrtrad(tabrun.getNom());
            Map.Entry<String, RsEntite> get = Lists.newArrayList(mvue.entrySet()).get(new Integer(id+""));
            if(get.getKey().equals(nm))
                slcent=get.getValue();
            else
                for(Map.Entry<String, RsEntite> mentry:mvue.entrySet())
                  if(mentry.getKey()
                          .replace(" ", "")
                          .contentEquals(nm.replace(" ", "")))
                              slcent=mentry.getValue();

                 if(slcent==null)
                 {
                     List rch=Arrays.asList(tabrun.getNom(),tabrun.getCateg());
                     List fields=Arrays.asList("nom","adresse");

                     List prmtrLgn=Variable.getPrmtr().subList(0, 3);
                     prmtrLgn.addAll(Variable.getPrmtr().subList(6, 9));
                     ArrayList rchMulti = gKml.rchMulti(rch, new Pt(),fields, "MUST_MUST", null);
                     if(prmtrLgn.contains(rb.getReverse(tabrun.getCateg())))
                         rchMulti= gKml.rchMulti(rch, new Ligne(),fields, "MUST_MUST", null);
                     if(!rchMulti.isEmpty())
                         slcent=(RsEntite) rchMulti.get(0);
                 }
                 if(slcent==null)
                 {
                     List rch=Arrays.asList(tabrun.getNom(),tabrun.getCateg());
                     List fields=Arrays.asList("nom","description");

                     List prmtrLgn=Variable.getPrmtr().subList(0, 3);
                     prmtrLgn.addAll(Variable.getPrmtr().subList(6, 9));
                     ArrayList rchMulti = gKml.rchMulti(rch, new Pt(),fields, "MUST_MUST", null);
                     if(prmtrLgn.contains(rb.getReverse(tabrun.getCateg())))
                         rchMulti= gKml.rchMulti(rch, new Ligne(),fields, "MUST_MUST", null);
                     if(!rchMulti.isEmpty())
                         slcent=(RsEntite) rchMulti.get(0);
                 }
           
           
           if(slcent!=null)
              lanceTour(slcent,nm);
        
       }
        
    }
   public void catchSouris()
   {
       rchCateg();
      categSchem[0]=0;      
   }
   

   public void slcZone()
    {   
        bsszone=true;
        final  Map<String, Meteo> mSpreM = AnalyzSchem.getmSpreM();
        chchKml=null;
            ville=null;
           
            categ=null;
            //lville=new ArrayList<String>(Variable.getRegion().get(zone));
            System.err.print(zone);
            zone.setNom(
                    zone.getNom());
            
            List<String> vgrz = Variable.getRegion().get(
                rb.getReverse(zone.getNom()));
                    
            
            lville=new ArrayList<ImgLien>(vgrz.size());
            vgrz.stream().forEach(v->Optional.ofNullable(mSpreM.get(v)).map(m->lville.add(new ImgLien(
                          rb.getString(v),
                          m.getTemps()))).
                    orElse(lville.add(new ImgLien(
                          rb.getString(v),
                          null))));
            /*for(String tmp:vgrz)   
                 if(mSpreM.get(tmp)!=null) 
                    lville.add(new ImgLien(
                          rb.getString(tmp),
                          mSpreM.get(tmp).getTemps()));
                else
                     lville.add(new ImgLien(
                          rb.getString(tmp),
                          null));*/
       
            res=zone.getNom();
            if(res.equals("Ouest"))
                res="uest";
            categSchem[0]=1;
            categSchem[1]=0;
                varBean[5]=null;
            
            lancement();
            if(!schema.getLschmvu().get(0).getPourKml().get(0).isEmpty())
                slcent= schema.getLschmvu().get(0).getPourKml().get(0).get(0);
       //bssact=false;
       
      if(schema.getLlpremeteo()!=null)
           if(schema.getLlpremeteo().size()>8)
           {
               multtab=250;
           }else
           {
               multtab=150;
           }
       
               
    }
   
    
   public void slcVille()
    {//combo selection des villes
        res=ville.getNom();
      chchKml=null;  
         String[] str=new String[2];
          str[0]="";
          str[1]="";
         
            //res=dcdVille(rb.getReverse(ville));
      
      categSchem[0]=2;
      if(categSchem[1]==0)
           varBean[5]=calend;
      else
          varBean[5]=null;
      lancement();
      boolean pfe=true;
      if(!schema.getMvue().isEmpty())
       chchSclent(schema.getMvue().values().iterator().next());
      
      
      bssact=false;
    }
 
public void createGraph(RsEntite rs)throws ClassCastException
    {
        
        Ligne l=new Ligne();
      
      
          if(rs instanceof Ligne)
                {
                    
                    l=(Ligne) gKml.findrs(rs);
                    
                }
                else 
                {
                    Pt pt=(Pt) rs;
                        l.setNom(pt.getNom());
                        l.setDescription(pt.getDescription());
                        l.setCoords(new ArrayList<Coord>(1));

                        Coord coord = new Coord();
                            coord.setLatitude(pt.getLatitude());coord.setLongitude(pt.getLongitude());
                            if(pt.getAlt()!=null)
                                coord.setLongitude(pt.getAlt());
                        l.getCoords().add(coord);

                        

                }
      
        Coord next;
         graphBean = new GrphElv();
        
            try{
                lmdlgrph.add(graphBean.createGraph(l.getNom(), Lists.newArrayList(l.getCoords())));
            }catch(NullPointerException npex)
            {
                Xcption.Xcption("rchBean createGraph lmdlgrph.add(graphBean.createGraph(l.getNom(), l.getCoords())); NullPointerException"+l,npex.getMessage());
            }
               // RequestContext.getCurrentInstance().execute("fetchKml('visite');");
            
        
       
    }
    public void getElv()
    {
            createGraph(schema.getMvue().get(res)); ;
            gKml.createKml(schema.getMvue().get(res), null, null);
            trKml=gKml.getTracer();
            tourKml=gKml.geTour();
    }

    private String chLieu(String rch)
    {
        
        
        Xcption.Xcption("rchBean rabVateg() chLieu",rch);
        String retour="";
         String[] split = rch.split(" ");
                                   int tl=0;
                                   if(split.length>1)
                                   {
                                       retour+=split[split.length-1]+" ";
    
                                   }else
                                       retour=rch;
        
        return retour;
    }
    public void tabCateg(String ctg)
    {
        
        varBean[0]=rb.getReverse(zone.getNom());
            varBean[1]=rb.getReverse(ville.getNom());
          
            varBean[3]=rb.getReverse(res);
            varBean[4]=rb.getReverse(categ.getNom());
            
        //init
     synchronized(this){
        try {
          
             String chchr="";
            for(Object o:varBean)
                if(o!=null&&o!="")
                {
                    if(o instanceof Date)
                    {
                        Date d=(Date) o;
                        chchr+=d.getYear()+d.getMonth()+d.getDay();
                    }else
                        chchr+=o;
                }
            chchr+=true+lg;
            notif=String.valueOf(categSchem[0])+String.valueOf(categSchem[1])+chchr;
            Xcption.Xcption(" RchBean() clé TempKml",notif);
    synchronized(this){
            CherchTemp temp=new CherchTemp((GereKml) gKml);
                chchKml=temp.cherche(notif);
                 gKml.fEm();
    }       
                
             Xcption.Xcption(" RchBean() chchKml",notif);System.out.println(chchKml);
            
            if(chchKml==null)
            {
                varBean[2]=rb;
                  schema.PrmtrSchm(categSchem[0], categSchem[1], varBean, true);
                  
            }else
            {
                RsnsObj rsnsObj = new RsnsObj("tmp", null,"tmp");
                 List ltmp = tmplRsEntite();     
                    rsnsObj.setPourKml(ltmp);
                 List lrsn=new ArrayList(1);
                    lrsn.add(rsnsObj);
                schema.setLschmvu(lrsn);
            }
            addStat();
            tabCateg();
        }catch(Exception ex)
        {
            
        }
     }
    }
    private List tmplRsEntite()
    {
        List<List> lrtr=new ArrayList<List>();
        List<RsEntite> ltmp = new ArrayList<RsEntite>();
        
        
                    if(ltmp==null)
                        ltmp=new ArrayList();
                    if(chchKml.getLlgn()!=null)
                        ltmp.addAll(chchKml.getLlgn());
                    if(chchKml.getLpt()!=null)
                        ltmp.addAll(chchKml.getLpt());
                    
                    Map<String,RsEntite> mvue=new HashMap<String,RsEntite>();
                    for(RsEntite rs:ltmp)
                        mvue.put(rs.getNom(), rs);
                        
        schema.setMvue(mvue);
        lrtr.add(ltmp);
        return lrtr;
    }

public void tabCateg() throws Exception
  {
      List<ImgLien> chxrg = schema.getLschmvu().stream().
              map(rsnso->new ImgLien(rsnso.getRftab(),"")).collect(Collectors.toList());
     /* if(categ!=null)
      {
          
              chxrg=lchxctg;
          
      }else
      {
          
          chxrg=Lists.newArrayList(new ImgLien(Variable.getPrmtr().get(11),""),
                                        new ImgLien(Variable.getLzact()[3],""),
                                            new ImgLien(Variable.getLtitre().get(14),""),
                                                new ImgLien(Variable.getPrmtr().get(10),""));
      }*/
      chxrg.add(new ImgLien(Variable.getPrmtr().get(6), ""));
      chxrg.add(new ImgLien(Variable.getPrmtr().get(4), ""));
       ltabRun=new ArrayList();
        Map<String, RsEntite> mvue = schema.getMvue();
        if(mvue==null)
        {
            mvue=new HashMap();
            for(RsnsObj rsnsobj:schema.getLschmvu())
                for(List<RsEntite> lrs:rsnsobj.getPourKml())
                    for(RsEntite rs:lrs)
                    {
                        schema.getMvue().put(rb.getString(rs.getNom()), rs);
                    }
            
        }
        Map<String,List<TabRun>> mltmp=new HashMap(chxrg.size()+1);
        //Map<String,List> mltmpRs=new HashMap(chxrg.size()+1);
        //List<List> ltmp=new ArrayList(chxrg.size()+1);
        chxrg.stream().forEach(imgl->mltmp.put(imgl.getNom(), new ArrayList()));
        //chxrg.forEach(imgl->mltmpRs.put(imgl.getNom(), new ArrayList()));
        /*for(ImgLien imgl:chxrg)
        {
            mltmp.put(imgl.getNom(), new ArrayList());
            mltmpRs.put(imgl.getNom(), new ArrayList());
        }*/
       // mltmpRs.put("", new ArrayList());
                //ltmp.add(new ArrayList());
         id=0;
         ltabRun=mvue.values().stream()
                 .map(this::rempTabRun).
                 collect(Collectors.toList());
         ltabRun.stream().sorted((t1,t2)->t1.getCateg().compareTo(t2.getCateg()));
       /* for(Map.Entry<String, RsEntite> entryRs:mvue.entrySet())
        {
            
           TabRun rempTabRun = rempTabRun(entryRs.getValue());
           Optional.ofNullable(mltmp.get(rb.getString(entryRs.getKey()))).get().add(rempTabRun);
          mltmp.entrySet().stream().
                  filter(entry->
                          rempTabRun.getNom().contains(rb.getString(entry.getKey()))||
                                  rempTabRun.getCateg().contains(rb.getString(entry.getKey())))
                  .map(entry-> 
                          entry.getValue()
                            .add(rempTabRun));
                    
                  ltabRun.add(rempTabRun);*/
                 /* mltmp.entrySet().stream().forEach(entry-> 
                          entry.getValue().stream().forEach(rs->ltabRun.add(rs)));*/
                 
                  
          
          
          /*for(Map.Entry<String,List> entry:mltmp.entrySet())
          if(pfe)
          if(rempTabRun.getNom().contains(rb.getString(entry.getKey()))||
          rempTabRun.getCateg().contains(rb.getString(entry.getKey())))
          {
          entry.getValue()
          .add(rempTabRun);
          mltmpRs.get(entry.getKey()).add(entryRs.getValue());
          pfe=false;
          }
          if(pfe)
          mltmpRs.get("").add(entryRs.getValue());
           */
          /*for(String chx:chxrg)
          if(rempTabRun.getNom().contains(chx)||
          rempTabRun.getCateg().contains(chx))
          ltmp.get(chxrg.indexOf(chx))
          .add(rempTabRun);*/
          /*if(rempTabRun.getNom().contains(Variable.getPrmtr().get(11)))
          {
          ltmp.add(rempTabRun);
          }else if(rempTabRun.getCateg().contains(Variable.getLzact()[3]))
          {
          tmpfest.add(rempTabRun);
          }else if(rempTabRun.getCateg().contains(Variable.getLtitre().get(14)))
          {
          tmpmwbc.add(rempTabRun);
          }else if(rempTabRun.getNom().contains(lvprmtr.get(10)))
          {
          tmpmrin.add(rempTabRun);
          }else
          ltabRun.add(
          rempTabRun(id,item.getValue()));
          id++;*/
        //}
      
       
      /*  for(Map.Entry<String,List<TabRun>> entr:mltmp.entrySet()) 
        {
          List<TabRun> get = entr.getValue();
            if(!get.isEmpty())
            {
               
                    get.stream().forEach(rs->ltabRun.add(rs));
               
                sizeavant=get.size();
            }
        }*/
                
     
        
        
        
      /*for(RsnsObj schOb:schema.getLschmvu())
      {
          long id=0;
          if(schOb.getPourKml()!=null&&!schOb.getRef().equals("meteo/simple")&&!schOb.getRef().equals("meteo marinne")&&
                  !schOb.getRef().contains("tour")&&!schOb.getRef().contains("zone"))
          {
              
              if(schOb.getRef().equals("activite/tout")||
                      lvprmtr.contains(schOb.getRef()))
              {
                  if(!schOb.getPourKml().isEmpty())
                    for(List list:schOb.getPourKml())
                          for(Object get :list)
                          {
                               if(get instanceof RsEntite)
                                {

                                    ltabRun.add(
                                            rempTabRun(id, get));
                                    id++;

                                 } 

                          }
              }
              else if(schOb.getRef().equals(lvprmtr.get(11)+"/jour"))
                for(List list:schOb.getPourKml())
                      for(Object get :list)
                      {
                           if(get instanceof RsEntite)
                            {

                                ltabRun.add(0,
                                        rempTabRun(id, get));
                                id++;

                             } 

                      }
              else if(!schOb.getPourKml().isEmpty())
                for(List list:schOb.getPourKml())
                      for(Object get :list)
                      {
                           if(get instanceof RsEntite)
                            {
                               TabRun rpTbRn = rempTabRun(id, get);
                                ltabRun.add(rangeTabrun(id, rpTbRn),
                                        rpTbRn);
                                id++;

                             } 

                      }
             
          } 
      }*/
    
      /*List<String> testCatg=new ArrayList<String>();
      List<String> testLieu=new ArrayList<String>();
      for(TabRun tb:ltabRun)
      {
          if(!testCatg.contains(tb.getCateg()))
              testCatg.add(tb.getCateg());
          if(!testLieu.contains(tb.getLieu()))
              testLieu.add(tb.getLieu());
      }*/

       
          
       
  }
private int rangeTabrun(long id,TabRun tb)
{
    long irtr=id;;
    if(tb.getCateg().equals(Variable.getPrmtr().get(11)))
        irtr=0;
    return (int) irtr;
}
private String chtrad(String trad)
{
    String rtr="";
    for(String split:trad.split(" "))
    {
       
        rtr+=rb.getString(split.replace(":"," "))+" ";
    }
    return rtr;
}
private String rvsrtrad(String trad)
{
    String rtr="";
    for(String split:trad.split(" "))
    {
       if(!rtr.equals(""))
           rtr+=" ";
        rtr+=rb.getReverse(split);
    }
    return rtr;
}
private String decompad(String ad)
{
        char[] toCharArray = ad.toCharArray();
        String[] lieu=new String[2];
        for (int i=1; i<toCharArray.length; i++)
        {
            char c=toCharArray[i];
                if(Character.isUpperCase(c))
                    {
                        lieu[1]=ad.subSequence(0, i)+" "+ad.subSequence(i, toCharArray.length);

                        lieu[0]=ad.subSequence(0, i)+"-"+ad.subSequence(i, toCharArray.length);
                        i++;
                    }
        }
            if(lieu[0]!=null)
            {
                ResourceBundle tmprb = rb.getRb();
                if(tmprb.containsKey(lieu[0]))
                   ad=rb.getString(lieu[0]);
                else 
                    if(tmprb.containsKey(lieu[1]))
                       ad=rb.getString(lieu[1]);
            }
    
    return ad;
}
private TabRun rempTabRun(Object get)
{
    TabRun tabRun = new TabRun();
    try{
            
                
                              RsEntite rsentite=(RsEntite) get;
                                String ad = rsentite.getAdresse();
                                String desc= rsentite.getDescription();
                                String nom=chtrad(rsentite.getNom());
                                tabRun.setNom(nom);
                                
                                String ctg=null;
                                if(ad!=null)
                                {
                                    String[] split = ad.split(Variable.getLieu());
                                            tabRun.setLieu(chtrad(
                                                                 decompad(split[split.length-1])));
                                    if(ad.contains(Variable.getLzact()[3]))
                                    {
                                        ctg=Variable.getLzact()[3];
                                        
                                        levnmt = gKml.rchBdd(rsentite.getNom(), "evenement", "fix", "lieux");
                                        
                                    }else
                                    {
                                         
                                            if(split[0]!=ad)
                                            {
                                                ctg=split[0];
                                                if(ctg.contains(Variable.getPrmtr().get(5)))//marché
                                                    ctg=ctg.replace(Variable.getPrmtr().get(5)+":", "");
                                                else if(ctg.contains(Variable.getZact()))//zoneActivité
                                                    ctg=ctg.replace(Variable.getZact()+":", "");
                                                else if(nom.contains(Variable.getPrmtr().get(10)))//météo marinne
                                                    ctg="lien "+Variable.getPrmtr().get(10);
                                                else if(ctg.equals(Variable.getLtitre().get(14)))
                                                    ctg=Variable.getLtitre().get(14);
                                                
                                            }
                                            else
                                                ctg="non défini";
                                    }
                               

                                }
                              
                             tabRun.setIcone(
                                     getIconeCatg(ctg));
                              tabRun.setCateg(
                                        chtrad(ctg));
                                tabRun.setId(id);
                                id++;
                                
                        }catch(Exception ex)
                        {
                            Xcption.Xcption("RchBean rempTabRun Exception ",ex.getMessage());
                        }
                                
                                return tabRun;
                            
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


    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public ImgLien getCateg() {
        return categ;
    }

    public void setCateg(ImgLien categ) {
        this.categ = categ;
    }

    public String getClitoKml() {
        return clitoKml;
    }

    public void setClitoKml(String clitoKml) {
        this.clitoKml = clitoKml;
    }

    public String getAltkml() {
        return altkml;
    }

    public void setAltkml(String altkml) {
        this.altkml = altkml;
    }

    public List<RsEntite> getVue() {
        return vue;
    }

    public void setVue(List<RsEntite> vue) {
        this.vue = vue;
    }

    public List<ImgLien> getLchxctg() {
        return lchxctg;
    }

    public void setLchxctg(List<ImgLien> lchxctg) {
        this.lchxctg = lchxctg;
    }


    public List<TabRun> getLtabRun() {
        return ltabRun;
    }

    public void setLtabRun(List<TabRun> ltabRun) {
        this.ltabRun = ltabRun;
    }

    public List<TabRun> getLfltTbRun() {
        return lfltTbRun;
    }

    public void setLfltTbRun(List<TabRun> lfltTbRun) {
        this.lfltTbRun = lfltTbRun;
    }

    public List<TabRun> getLfltTbEvnt() {
        return lfltTbEvnt;
    }

    public void setLfltTbEvnt(List<TabRun> lfltTbEvnt) {
        this.lfltTbEvnt = lfltTbEvnt;
    }

    public TabRun getTabrun() {
        return tabrun;
    }

    public void setTabrun(TabRun tabrun) {
        this.tabrun = tabrun;
    }

    public int getMulttab() {
        return multtab;
    }

    public void setMulttab(int multtab) {
        this.multtab = multtab;
    }

    public List<PrevMt> getLcli() {
        return lcli;
    }

    public void setLcli(List<PrevMt> lcli) {
        this.lcli = lcli;
    }

    public Meteo getCli() {
        return cli;
    }

    public void setCli(Meteo cli) {
        this.cli = cli;
    }


    public List<String> getFroz() {
        return froz;
    }

    public void setFroz(List<String> froz) {
        this.froz = froz;
    }

    public Boolean getBtab() {
        return btab;
    }

    public void setBtab(Boolean btab) {
        this.btab = btab;
    }

    public Boolean getBelev() {
        return belev;
    }

    public void setBelev(Boolean belev) {
        this.belev = belev;
    }

    public Boolean getBiti() 
    {
       
        return biti;
    }

    public void setBiti(Boolean biti) 
    {
      if(biti)
           chgIti();
        this.biti = biti;
    }

    public RsEntite getSlcent() {
       
        return slcent;
    }

    public void setSlcent(RsEntite slcent) {
        this.slcent = slcent;
    }

    public ImgLien getZone() {
        return zone;
    }

    public void setZone(ImgLien zone) {
        this.zone = zone;
    }


    public ImgLien getVille() {
        return ville;
    }

    public void setVille(ImgLien ville) {
        this.ville = ville;
    }

    public List<Coord> getlCoord() {
        return lCoord;
    }

    public void setlCoord(List<Coord> lCoord) {
        this.lCoord = lCoord;
    }

    public List<ImgLien> getLzone() {
        return lzone;
    }

    public void setLzone(List<ImgLien> lzone) {
        this.lzone = lzone;
    }

    public List<ImgLien> getLville() {
        return lville;
    }

    public void setLville(List<ImgLien> lville) {
        this.lville = lville;
    }

    public List<ImgLien> getLctg() {
        return lctg;
    }

    public void setLctg(List<ImgLien> lctg) {
        this.lctg = lctg;
    }

    public SelectItem getItm() {
        return itm;
    }

    public void setItm(SelectItem itm) {
        this.itm = itm;
    }

    public Boolean getBssact() {
        return bssact;
    }

    public void setBssact(Boolean bssact) {
        this.bssact = bssact;
    }

    public Boolean getBsszone() {
        return bsszone;
    }

    public void setBsszone(Boolean bsszone) {
        this.bsszone = bsszone;
    }

    public Map<String, String> getLthm() {
        return lthm;
    }

    public void setLthm(Map<String, String> lthm) {
        this.lthm = lthm;
    }

    public String getTheme() 
    {
        
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
        
    }

    public Boolean getBpltfrm() {
        return bpltfrm;
    }

    public void setBpltfrm(Boolean bpltfrm) {
        this.bpltfrm = bpltfrm;
    }
    public void setpltfrm(Boolean bpltfrm) {
        this.bpltfrm = bpltfrm;
        gKml.getGereDonne().finish();
        chchKml=null;
    }
    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public Date getCalend() {
        return calend;
    }

    public void setCalend(Date calend) {
        this.calend = calend;
    }

    public ItiJsf getItijsf() {
        return itijsf;
    }

    public void setItijsf(ItiJsf itijsf) {
        this.itijsf = itijsf;
    }

    public Itineraire getItineraire() {
        return itineraire;
    }

    public void setItineraire(Itineraire itineraire) {
        this.itineraire = itineraire;
    }

    public ArrayList<Itineraire> getLiti() {
        return liti;
    }

    public void setLiti(ArrayList<Itineraire> liti) {
        this.liti = liti;
    }

    public String getLg() {
        return lg;
    }

    public void setLg(String lg) {
        this.lg = lg;
        
    }

    public Object[] getTitres() {
        return titres;
    }

    public void setTitres(Object[] titres) {
        this.titres = titres;
    }


    public List<String> getLuv() 
    {
       
        return luv;
    }

    public void setLuv(List<String> luv) {
        this.luv = luv;
    }

    public List<CartesianChartModel> getLmdlgrph() {
        return lmdlgrph;
    }

    public void setLmdlgrph(List<CartesianChartModel> lmdlgrph) {
        this.lmdlgrph = lmdlgrph;
    }

    public List<Evnmt> getLevnmt() {
        return levnmt;
    }

    public void setLevnmt(List<Evnmt> levnmt) {
        this.levnmt = levnmt;
    }

    public Boolean getBlien() {
        return blien;
    }

    public void setBlien(Boolean blien) {
        this.blien = blien;
    }

    public Boolean getBevnmt() {
        return bevnmt;
    }

    public void setBevnmt(Boolean bevnmt) {
        this.bevnmt = bevnmt;
    }

    public Boolean getBdesc() {
        return bdesc;
    }

    public void setBdesc(Boolean bdesc) {
        this.bdesc = bdesc;
    }

    public CartesianChartModel getMdlgrph() {
        return mdlgrph;
    }

    public void setMdlgrph(CartesianChartModel mdlgrph) {
        this.mdlgrph = mdlgrph;
    }
    
    public void updFiltre()
    {
        Xcption.Xcption("RchBean updFiltre"," ");
        System.out.println(" ");
    }
   
    private void chchSclent(Object ch)
    {
       if(ch instanceof Date)
       {
           Date d=(Date) ch;
           slcent=new Pt();
           slcent.setNom(d.toGMTString());
       }else if(schema.getMvue().containsKey(ch))
       {
           slcent=schema.getMvue().get(ch);
       }else if(ch instanceof String)
       {
           slcent=new Pt();
           slcent.setNom((String) ch);
       }
         
    }

   /* private void initGraph() {
        
        Map<String, RsEntite> mvue = new HashMap<String, RsEntite>();
            //graph d'elevation de l'objet selectionneri
            if(schema.getMvue()==null)
            {
                
                for(List<RsEntite> lrs:schema.getLschmvu().get(0).getPourKml())
                    for(RsEntite rs:lrs)
                        if(rs instanceof Ligne)
                            mvue.put(rs.getNom(), rs);
            }else
            {
                
                for(RsEntite rs:schema.getMvue().values())
                    if(rs instanceof Ligne)
                        mvue.put(rs.getNom(), rs);
                
               
            }
            lmdlgrph=new ArrayList<CartesianChartModel>();
            lctg = new ArrayList<String>(mvue.keySet());
                for(String key:mvue.keySet())
                    createGraph(mvue.get(key));
                               
            mdlgrph=lmdlgrph.get(0);
    }*/

    private void construireChKml() {
          schema=new PrmtrSchm((GereKml)gKml);
                RsnsObj rsnsObj = new RsnsObj("tmp", null,"tmp");
                List ltmp =tmplRsEntite();
                   
                    rsnsObj.setPourKml(ltmp);
                    rsnsObj.setRef("chchKml");
                 List lrsn=new ArrayList();
                    lrsn.add(rsnsObj);
                if(!AnalyzSchem.getmSpreM().isEmpty())
                     schema.setLlpremeteo(new HashSet(AnalyzSchem.getmSpreM().values()));
               
                
                schema.setLschmvu(lrsn);
                
                if(chchKml.getTc()!="")
                {
                    trKml=chchKml.getTc();
                    tourKml=chchKml.getTr();
                }
                else 
                {
                    gKml.createKml(null, null, schema);
                        trKml=gKml.getTracer();
                        tourKml=gKml.geTour();
                }
                    
                Xcption.Xcption("chchKml lancement chchkml trckml",trKml);
                Xcption.Xcption("chchKml lancement chchkml tourkml",tourKml);
                    
              try {
                  tabCateg();
                  chchSclent(schema.getMvue().values().iterator().next());
              } catch (Exception ex) {
                  Xcption.Xcption("chchKml lancement Exception schema.getMvue().values().iterator().next()",ex.getMessage());;
              }
                     
    }

    private void fixtabRun(RsEntite slc) {
        int indx=0;
                    
                        for(RsEntite rs:schema.getMvue().values())
                            if(rs.equals(slc))
                                tabrun = ltabRun.get(indx);
                            else
                                indx++;
    }

    private void finishTabRun() 
    {
        if(tabrun!=null)
          RequestContext.getCurrentInstance().execute("PF('dt_tr').unselectAllRows();"
                  + "PF('dt_tr').selectRow( "+tabrun.getId()+", true );");
     
    }

   /* private void verifTotCli() {
        List tmp=ltotcli;
        if(ltotcli!=null)
        {
            for(Object o:tmp)
                if(o instanceof Meteo)
                {
                    Meteo m=(Meteo) o;
                    String[] spliturl = m.getUrlimg().split(("/"));
                    String str=spliturl[spliturl.length-1].replace(".gif", "");
                    if(str.contentEquals("3200"))
                        m.setTemps(
                                trfBddprVueCli(m.getTemps())
                        );
                }
                else
                    ltotcli.remove(o);
        }else
            ltotcli=new ArrayList();
    }*/
   
    private void initComp() 
    {
        final  Map<String, Meteo> mSpreM = AnalyzSchem.getmSpreM();
        lthm=Variable.getEsthm().collect(Collectors.toMap(e->rb.getString(e.getKey()), e->e.getValue()));
           
      
           
            lzone=new ArrayList<ImgLien>(Variable.getRegion().keySet().size());
            for(String tmp:Variable.getRegion().keySet())   
             if(!mSpreM.isEmpty()) 
                    lzone.add(new ImgLien(
                          rb.getString(tmp),
                          mSpreM.get(tmp).getTemps()));
             else
                  lzone.add(new ImgLien(
                          rb.getString(tmp),
                          null));

            List<String> vgrz = Variable.getRegion().get(Variable.getRegion().keySet().iterator().next());
            lville=new ArrayList<ImgLien>(vgrz.size());
            for(String tmp:vgrz)   
                if(mSpreM.get(tmp)!=null) 
                    lville.add(new ImgLien(
                          rb.getString(tmp),
                          mSpreM.get(tmp).getTemps()));
                else
                     lville.add(new ImgLien(
                          rb.getString(tmp),
                          null));
            
            
    }

    private String trfBddprVueCli(String pre)
    {
        String tmp="";
        for(String esp:pre.split(" "))
        {
            if(esp.contains("/"))
            {
                String[] tiret=esp.split("/");
                    tmp+=rb.getString(tiret[0])+"/"+rb.getString(tiret[1]);
            }else
               tmp+=rb.getString(esp)+" ";
        }
                
              pre=tmp;
       /* tmp="";
            for(String str:pre.split("/"))
                        tmp+=rb.getString(str)+"/";
             pre=tmp.replace("/", " ");*/
        
        return pre;
    }

    private boolean chDate(boolean brmv,Date date) 
    {
       int dtmp = date.getHours();
       int bdt = new Date(System.currentTimeMillis()).getHours();
       int[] ttest=new int[5];
            ttest[0]=0;
            ttest[1]=8;
            ttest[2]=13; 
            ttest[3]=19;
            ttest[4]=24;
       for(int i=0;i<=4;i++)
            if(ttest[i]<bdt&&bdt<ttest[i+1])
                if((ttest[i]<dtmp&&dtmp<ttest[i+1]))
                        brmv=true;
                
          
                      
        return brmv;
    }

    
}
