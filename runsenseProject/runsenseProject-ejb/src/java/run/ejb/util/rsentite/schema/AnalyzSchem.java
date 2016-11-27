/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.util.rsentite.schema;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.lucene.document.DateTools;
import run.ejb.base.CherchTemp;
import run.ejb.base.Variable;
import run.ejb.ejbkml.GereKml;
import run.ejb.entite.client.Compte;
import run.ejb.entite.climat.Meteo;
import run.ejb.entite.climat.PrevMt;
import run.ejb.entite.geo.Coord;
import run.ejb.entite.geo.Evnmt;
import run.ejb.entite.geo.Ligne;
import run.ejb.entite.geo.Localite;
import run.ejb.entite.geo.Prmtr;
import run.ejb.entite.geo.Pt;
import run.ejb.entite.geo.Tour;
import run.ejb.entite.geo.interf.RsEntite;
import run.ejb.entite.util.bdd.ChrSpe;
import run.ejb.entite.util.runsense.UtilRsns;

import run.ejb.util.YahooClimat.WeatherService;
import run.ejb.util.YahooClimat.YAHOOWeatherService;
import run.ejb.util.ex.Xcption;
import run.ejb.util.kml.schema.RsnsObj;
import run.ejb.util.traduction.RBint;

/**
 *
 * @author Fujitsu
 */
public class AnalyzSchem 
{
    private GereKml gkml;
    private PrmtrSchm schema;
    
    private Object[] varBean;//0zone 1villbase=new ArrayList()le 2rb 3res 4categ 
    private List<RsEntite> lbase;
    private Set<Meteo> llpremeteo;
    private String woied;
    private final Map<String, String> rvcti;
    private final List<String> jour;
    private int cptxtra=0;
    private int j;
    
    /*private final  HashMap<String, String> mwoeid ;
    private final Map<String, String> woeid;*/
     private static Map<String, String> srvcti;
    private static Map<String,Meteo> mSpreM=new HashMap<String, Meteo>();
    
    
    
    private final Map<String, List<String>> vregion ;
    public void verifmSpreM()
    {
       if(mSpreM.isEmpty())
        {
           
           SreInitmSpreM();
        }
        
        
    }
 
    public static void SreInitmSpreM()
    {
        mSpreM=new HashMap<String, Meteo>();
        SchMeteo();
        CherchTemp.SaddMeteo(
                mSpreM.values().stream().collect(Collectors.toList()));
    }
    public static List<String> cvtmTol(Map.Entry<String,List<String>> en)
    {
         List<String> value = en.getValue();
            value.add(en.getKey());
        return value;     
    }
    public static void SchMeteo()
    {
        final Map<String, String> swoeid = Variable.getWoeid();
        final Map<String, List<String>> sregion = Variable.getRegion();
       
        srvcti=new HashMap<String, String>(swoeid.size());
            for(String cle:swoeid.keySet()) 
                if(!srvcti.containsKey(swoeid.get(cle)))
                 srvcti.put(swoeid.get(cle),cle); 
          //Meteo yahooClimat = null;
           //List<String> lrch=new ArrayList();
          final List<String> lrch=Lists.newArrayList(sregion.keySet());
            sregion.values().stream().forEach(l->lrch.addAll(l));
            mSpreM=new HashMap();
        /*final List<Meteo> collect = lrch.stream().map(AnalyzSchem::SyahooClimat).
                collect(Collectors.toList());*/
        for(Meteo m:lrch.stream().map(AnalyzSchem::SyahooClimat).
                collect(Collectors.toList()))
            if(!mSpreM.containsKey(m.getNom()))
                mSpreM.put(m.getNom(),m);
          /*sregion.entrySet().stream().
                  peek(e->lrch.add(e.getKey())).peek(e->lrch.addAll(e.getValue()));*/
         /* lrch.stream().map(cle->SyahooClimat((String)cle)).
                  map(yahooClimat->mSpreM.put(yahooClimat.getWoied(), yahooClimat));*/
               /* for(Object cle:lrch)
                   {
                    if(!mSpreM.containsKey(cle))
                    {
                        
                        yahooClimat=SyahooClimat((String)cle);
                        if(yahooClimat!=null)
                        {
                             mSpreM.put((String)cle, yahooClimat);
                        }
                    }
                   }*/
    }
    public AnalyzSchem(GereKml gkml) 
    {
        
         vregion = Variable.getRegion();
       
       /* woeid = Variable.getWoeid();
        mwoeid = new HashMap<String, String>(Variable.getWoeid());*/
        jour=Arrays.asList("dimanche","lundi","mardi","mercredi","jeudi","vendredi","samedi");   
        this.gkml=gkml;
        
        rvcti=Variable.getWoeid();
         /*rvcti=new HashMap<String, String>(cont.getCities().size());
            for(String cle:cont.getCities().keySet()) 
                rvcti.put(cont.getCities().get(cle),cle);*/
            
    }
    
    public void AnalyzSchem(PrmtrSchm schema, Object[] vB) 
    {
        try{
        this.varBean=vB;
        
        this.schema = schema;
        lbase=new ArrayList(); 
        /* String temp=(String) varBean[0];
         String str="";
          
         if(temp!=null)
            for(char c:temp.toCharArray())
                 {
                     if(c=='O')
                         c='0';
                     str+=c;
                 }
         varBean[0]=str;  */       
        
        verifmSpreM();
        boolean bpmeteo=true;
        //schema.getLschmvu().stream().filter(rsnsSchema->Variable.getTabApprox().get(rsnsSchema.getRef())!=null);
         for(RsnsObj rsnsSchema:schema.getLschmvu())
                {
                    List lrslt=new ArrayList();
                     String[] clerch = Variable.getTabApprox().get(rsnsSchema.getRef());
                     
                     
                   if(clerch!=null)
                   { 
                       
                        try{
                            rsnsSchema.setPourKml(new ArrayList());
                       for(String crch:clerch)
                     {
                         String[] split=crch.split("/");
                         if(split[0].equals("chchTout"))
                         {
                             try {
                                 List<RsEntite> chTout = gkml.chTout(split[1]);
                                 lrslt=supDoublons(lrslt, chTout);
                                 addMvue(chTout);
                                 
                             } catch (Exception ex) {
                                 
                               Xcption.Xcption("AnalyzSchem gkml.chTout(new Pt())","lrch :"+lrslt+"point");
                             }
                         }else if(split[0].equals("rchMulti"))
                         {
                              List<String> lfields=new ArrayList(2);
                              List lrch=new ArrayList(2);
                                        
                             String[] spltemp = split[3].split("-");
                                
                             
                             Object e=castRsEntite(split[2]);
                             if(e instanceof Evnmt)
                             {
                                 if(split[1].equals("sem"))
                                 {
                                     Date date=new Date(System.currentTimeMillis());
                                        Calendar c = Calendar.getInstance();
                                            c.setTime(date);
                                        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
                                            c.add(Calendar.DAY_OF_MONTH, -dayOfWeek);

                                        lrch.add(c.getTime());lfields.add("dt");
                                        // we do not need the same day a week after, that's why use 6, not 7
                                        c.add(Calendar.DAY_OF_MONTH, 6); 
                                        lrch.add( c.getTime());lfields.add("dt");
                                        
                                        
                                        Xcption.Xcption("AnalyzSchem gkml.rchMulti(lrch, e, lfields, split[4], null));","lrch :"+lrch+" e "+e+"lfields :"+lfields+"clause :"+split[4]);

                                       ArrayList<Evnmt> rchMulti = gkml.rchMulti(lrch, e, lfields, split[4], null);
                                        for(Evnmt evnmt:rchMulti)
                                        {
                                            ArrayList rchBdd = gkml.rchBdd(
                                                                   evnmt.getNom(), "point", "fix", "nom");
                                            lrslt=supDoublons(lrslt, rchBdd);
                                             addMvue(rchBdd);
                                        }
                                       
                                
                                 System.out.println(lrslt);
                                 }
                             }
                             else if(split[1].contains("List"))
                             {
                               try{
                                 String[] splrch = split[1].replace("List:", "").split("-");
                              
                                        lfields.add(spltemp[0]);
                                            lrch.add(stRch(splrch[0],lfields.get(0)));
                                               

                                  String stRch = stRch(splrch[1],lfields.get(0));
                                     if(stRch.contains("_"))
                                    {
                                        lfields.add(spltemp[1]);
                                        for(String str:stRch.split("_"))
                                        {  
                                            if(lrch.size()>1)
                                                lrch.remove(1);
                                            
                                            lrch.add(str);
                                            
                                            Xcption.Xcption("AnalyzSchem gkml.rchMulti(lrch, e, lfields, split[4], null));","lrch :"+lrch+" e "+e+"lfields :"+lfields+"clause :"+split[4]);

                                            ArrayList<RsEntite> rchMulti = gkml.rchMulti(lrch, e, lfields, split[4], null);
                                            if(str.contains("au"))
                                            {
                                                ArrayList ltmp=new ArrayList();
                                                for(RsEntite rs:rchMulti)
                                                {
                                                    String[] tprd=rs.getDescription().split(Variable.getLtitre().get(30))[1]
                                                            .split("_")[0].replace(" ", "")
                                                            .split("au");
                                                            
                                                    if(jour.indexOf(tprd[0])<=j&&
                                                            j<=jour.indexOf(tprd[1]))
                                                        ltmp.add(rs);
                                                }
                                                rchMulti= ltmp;
                                            }
                                            
                                            lrslt=supDoublons(lrslt, rchMulti);
                                            
                                            addMvue(rchMulti);
                                        }
                                    }else
                                     {
                                         lrch.add(stRch);
                                            lfields.add(spltemp[1]);
                                            Xcption.Xcption("AnalyzSchem gkml.rchMulti(lrch, e, lfields, split[4], null));","lrch :"+lrch+" e "+e+"lfields :"+lfields+"clause :"+split[4]);


                                            ArrayList<RsEntite> rchMulti = gkml.rchMulti(lrch, e, lfields, split[4], null);
                                            lrslt=supDoublons(lrslt, rchMulti);
                                            addMvue(rchMulti);
                                     }
                               }catch(Exception ex)
                               {
                                   
                                   Xcption.Xcption("AnalyzSchem gkml.rchMulti(lrch, e, lfields, split[4], null)); Exception",ex.getMessage());


                               }
                                    
                                 System.out.println(lrslt);
                             }
                             
                         }else if(split[0].equals("yahooClimat"))
                         {
                             bpmeteo=false;
                             addClimat(lrslt);
                             
                         }else if(split[1].equals("rchbdd"))
                         {
                             String stRch = stRch(split[0],split[4]);
                                ArrayList rchBdd = new ArrayList();
                             
                                if(stRch.contains(Variable.getPrmtr().get(6)+"_")||
                                        stRch.contains(Variable.getPrmtr().get(5)+"_"))
                                {
                                    Xcption.Xcption("gkml.rchBdd(stRch(split[0]), split[2], split[3], split[4]))","stRch :"+stRch+"entite"+split[2]+"categ :"+split[3]+"field :"+split[4]);
                                         rchBdd = gkml.rchBdd(
                                                                   stRch, split[2], split[3], split[4]);
                                }
                                else if(stRch.contains("_"))
                                {
                                    for(String str:stRch.split("_"))
                                    {
                                        
                                        Xcption.Xcption("gkml.rchBdd(stRch(split[0]), split[2], split[3], split[4]))","str :"+str+"entite"+split[2]+"categ :"+split[3]+"field :"+split[4]);
                                        rchBdd.addAll(gkml.rchBdd(str, split[2], split[3], split[4]));
                                         
                                    }
                                }else
                                {
                                    
                                    Xcption.Xcption("gkml.rchBdd(stRch(split[0]), split[2], split[3], split[4]))","stRch :"+stRch+"entite"+split[2]+"categ :"+split[3]+"field :"+split[4]);
                                         rchBdd = gkml.rchBdd(
                                                                   stRch, split[2], split[3], split[4]);
                                }
                                 
                                        
                                        if(rchBdd.isEmpty())
                                        {
                                            Object castRsEntite = gkml.castRsEntite(split[2]);
                                            boolean faire=false;
                                            for(String str:Variable.getMprmtrEnt().get(castRsEntite.getClass().getCanonicalName()))
                                                if(str.equals(split[4]))
                                                    faire=true;
                                                else if(faire)
                                                {
                                                   split[4]=str;
                                                   faire=false;
                                                }
                                            
                                            rchBdd =gkml.rchBdd(
                                                    stRch, split[2], split[3], split[4]);
                                        }
                                        lrslt=supDoublons(lrslt, rchBdd);
                                        addMvue(rchBdd);
                                        Xcption.Xcption("donner BDD sortie...",lrslt);
                                   
                         }
                         
                         if(lbase.isEmpty())
                           lbase=lrslt;
                         
                     }
                        }catch(javax.persistence.PersistenceException pex)
                       {
                            
                            Xcption.Xcption("AnalyzSchema PersistenceException pex", pex.getMessage());
                       }
                        
                        List ltmp=new ArrayList(lrslt);
                        if(!rsnsSchema.getPourKml().isEmpty())
                            for(List list:rsnsSchema.getPourKml())
                                ltmp=supDoublons(ltmp, list);
                        else
                            ltmp.addAll(lrslt);
                        
                            rsnsSchema.getPourKml().add(ltmp);
                        
                   }else
                   {
                       
                   }
                    
                    
                    
                }
         
         /*if(bpmeteo)
             addClimat(llpremeteo);*/
        }catch(Exception ex)
        {
            Xcption.Xcption("AnalyzSchem public void AnalyzSchem(PrmtrSchm schema, Object[] vB) ",ex.getMessage());
        }finally
        {
            
              
                llpremeteo=new HashSet<Meteo>(mSpreM.values());
              gkml.fEm();
           gkml=new GereKml();
        }
        
    }
    
   private void addMvue(List<RsEntite> list)
   {
       RBint rb=(RBint) varBean[2];
       for(RsEntite rs:list)
       {
           schema.getMvue().put(rb.getString(rs.getNom()), rs);
       }
   }
   
    public run.ejb.entite.geo.Tour cherchTour(String ad)
    {
        run.ejb.entite.geo.Tour tour=null;
        ArrayList rchBdd = gkml.rchBdd("tour :"+ad, "tour", "fix", "nom");
        if(!rchBdd.isEmpty())
            tour=(Tour) rchBdd.get(0);
        
        return tour;
    }
    public Meteo yahooClimat(String req)
    {
      
        if(Variable.getWoeid().containsKey(req))
        {
            woied=Variable.getWoeid().get(req);
            return retrieveConditions();
        }
        return null;
    }
    public static Meteo SyahooClimat(String req)
    {
       if(Variable.getWoeid().containsKey(req))
            return  SretrieveConditions(Variable.getWoeid().get(req));
      
        return null;
    }
     //climat
    private static Meteo SretrieveConditions(String w) 
    {
        Meteo meteo=new Meteo();
        //YahooRep meteoKml = new YahooRep();
         WeatherService weatherService =  (WeatherService) new YAHOOWeatherService();
         
                String  conditions = weatherService.getConditions(w, "c");
                
                
        //String dt = DateFormat.getDateTimeInstance().format(new Date());
            meteo.setJour(System.currentTimeMillis()-1000*60*60*3);
        
      if(conditions!=null)  
      {
          conditions = conditions.toLowerCase();
                
        
         Xcption.Xcption("WaetherController",conditions);
            
        String[] split = conditions.split("<br />");
        String urlimg=split[0];
        
         meteo.setWoied(srvcti.get(w));
         meteo.setUrlimg(urlimg.split("\"")[1]);
            String[] sptmps= split[2].split(",");
         meteo.setTemps(sptmps[0]);
            String replace = sptmps[1].replace(" ", "").replace("c", "");
         meteo.setDeg(Integer.valueOf(replace));
         
         
            /*meteoKml.setUrl(urlimg.split("\"")[1]);
        split[1] = "<b>prévisions climatique: </b>";
        meteoKml.setJour(split[2]);
        split[4] = "<b>prévisions :</b><BR />";*/
        List<String> temp =new ArrayList(5);
        temp.add(split[5]);temp.add(split[6]);temp.add(split[7]);temp.add(split[8]);temp.add(split[9]);
        //meteoKml.setPrevision(temp);
         meteo.setLprev(new HashSet());
        for(String str:temp)
        {
            String[] splt1 = str.split("-");
            String[] splt2 = splt1[1].split(". high:");
            String[] splt3 = splt2[1].split(" low: ");
            
            meteo.getLprev().add(new PrevMt(splt1[0], splt2[0], 
                    Integer.valueOf(splt3[0].replace(" ", "")),
                    Integer.valueOf(splt3[1].replace(" ", ""))));
        }
           
           
      }else
      {
             meteo=null;
      }
        return meteo;
    }
       private Meteo retrieveConditions() 
    {
        Meteo meteo=new Meteo();
        //YahooRep meteoKml = new YahooRep();
         WeatherService weatherService =  (WeatherService) new YAHOOWeatherService();
         
                String  conditions = weatherService.getConditions(woied, "c");
                
                
        //String dt = DateFormat.getDateTimeInstance().format(new Date());
            meteo.setJour(System.currentTimeMillis()-1000*60*60*3);
        
      if(conditions!=null)  
      {
          conditions = conditions.toLowerCase();
                
         
         Xcption.Xcption("WaetherController",conditions);
            
        String[] split = conditions.split("<br />");
        String urlimg=split[0];
        
         meteo.setWoied(rvcti.get(woied));
         meteo.setUrlimg(urlimg.split("\"")[1]);
            String[] sptmps= split[2].split(",");
         meteo.setTemps(sptmps[0]);
            String replace = sptmps[1].replace(" ", "").replace("c", "");
         meteo.setDeg(Integer.valueOf(replace));
         
         
            /*meteoKml.setUrl(urlimg.split("\"")[1]);
        split[1] = "<b>prévisions climatique: </b>";
        meteoKml.setJour(split[2]);
        split[4] = "<b>prévisions :</b><BR />";*/
        List<String> temp =new ArrayList(5);
        temp.add(split[5]);temp.add(split[6]);temp.add(split[7]);temp.add(split[8]);temp.add(split[9]);
        //meteoKml.setPrevision(temp);
        meteo.setLprev(new HashSet());
        for(String str:temp)
        {
            String[] splt1 = str.split("-");
            String[] splt2 = splt1[1].split(". high:");
            String[] splt3 = splt2[1].split(" low: ");
            
            meteo.getLprev().add(new PrevMt(splt1[0], splt2[0], 
                    Integer.valueOf(splt3[0].replace(" ", "")),
                    Integer.valueOf(splt3[1].replace(" ", ""))));
        }
           
           
      }else
      {
             meteo=null;
      }
        return meteo;
    }
private Object castRsEntite(String entcl)
    {
        Object e = null;
        if(entcl.equals("ligne"))
        {
            e = new Ligne();
        }else if(entcl.equals("localite"))
        {
            e = new Localite();
        }else if(entcl.equals("tour"))
        {
            e = new run.ejb.entite.geo.Tour();
        }else if(entcl.equals("prmtr"))
        {
            e = new Prmtr();
        }else if(entcl.equals("compte"))
        {
            e = new Compte();
        }else if(entcl.equals("point"))
        {
            e = new Pt();
        }else if(entcl.equals("coord"))
        {
            e = new Coord();
        }else if(entcl.equals("evenement"))
        {
            e = new Evnmt();
        }
        
        return e;
    }
    private String stRch(String str, String field)
    {
        ChrSpe env=new ChrSpe();
        
             
        if(str.equals("categ"))
        {
            
            str=(String) varBean[4];
        }
        else if(str.equals("zone"))
        {
            
            str=(String) varBean[0];
            if(str.equals("Ouest"))
                str=" Ouest";
        }
        else if(str.equals("ville"))
        {
            str=(String) varBean[1];
          if(str!=null)
             if(field=="adresse") 
                 str=UtilRsns.invDcdVille(str);
        }
        else if(str.contains("res"))
        {
            if(varBean[3]!=null)
                str=(String) varBean[3];
            else
                str="";
            String[] splitres = str.split("/");
                             if(!str.equals(splitres[0]))
                             {
                                 str=splitres[cptxtra];
                                 cptxtra++;
                             }
        }
       
        else if(str.equals("envrch"))
        {
           
             if(varBean[1]!=null)
             {
                
                    str=(String) varBean[1];
                    str=str.replace("-", "").replace(" ", "");
             
                 
                    
                
             }
            else if(varBean[0]!=null)
            {
                
                    str=(String) varBean[0];
                 
            }
            
             if(str.equals("envrch"))
                    str=Variable.getLieu();
        }
        else if(str.equals("jour"))
        {
            
            Date date=(Date) varBean[5];
            j=date.getDay();
            if(date!=null)
               str=jour.get(date.getDay());
            else
                str=jour.get(new Date(System.currentTimeMillis()).getDay());
            
           // str+="_"+"au";
               
        }else if(str.equals("sem"))
        {
            
            String dcpm="JANV_FEV_MARS_AVR_MAI_JUIN_JUIL_AOUT_SEPT_OCT_NOV_DEC";
            Calendar c=Calendar.getInstance();
            int day = new Date(System.currentTimeMillis()).getDay()+1;
            String datePourString=null;
            if(day==1)
            {
                c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                datePourString = UtilRsns.datePourString(c.getTime(), DateTools.Resolution.DAY);
                    str=datePourString;
                        day++;
                        long tmllis=c.getTimeInMillis()+1000*60*60*24*7;
                for(int d=day;d<=7;d++)
                {
                    c.set(Calendar.DAY_OF_WEEK, d); 
                    datePourString = UtilRsns.datePourString(new Date(tmllis)
                                                                 , DateTools.Resolution.DAY);
                       str+=datePourString+"_";
                } 
                    
            }else
            {
                str="";
                for(int d=day;d<=7;d++)
                {
                    c.set(Calendar.DAY_OF_WEEK, d); 
                    datePourString = UtilRsns.datePourString(c.getTime(), DateTools.Resolution.DAY);
                       str+=datePourString+"_";
                }
                c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                datePourString = UtilRsns.datePourString(c.getTime(), DateTools.Resolution.DAY);
                    str+=datePourString;
            }
                
                    
               /* c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); 
                 datePourString = UtilRsns.datePourString(c.getTime(), DateTools.Resolution.DAY);
                    str=datePourString+"_";
                c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);     
                datePourString = UtilRsns.datePourString(c.getTime(), DateTools.Resolution.DAY);
                    str+=datePourString+"_";
                c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY); 
                 datePourString = UtilRsns.datePourString(c.getTime(), DateTools.Resolution.DAY);
                    str+=datePourString+"_";
                c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);     
                datePourString = UtilRsns.datePourString(c.getTime(), DateTools.Resolution.DAY);
                    str+=datePourString+"_";    
                c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);     
                datePourString = UtilRsns.datePourString(c.getTime(), DateTools.Resolution.DAY);
                    str+=datePourString+"_";
                c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY); 
                 datePourString = UtilRsns.datePourString(c.getTime(), DateTools.Resolution.DAY);
                    str+=datePourString+"_";*/
                 
        }
        else 
        {
            str=str.replace("'", "").replace("£", "'");
            
        }
        
        
       
        return str;
    }
     private List chMeteoMarinne(String adresse, List lrslt) {
      List lfields=new ArrayList(2);
                                      
                                           lfields.add("adresse");lfields.add("nom");

                                        Pt e=new Pt();

                                            List lrch=new ArrayList(2);
                                     String[] splitad = adresse.split(" ");
                                                lrch.add(splitad[splitad.length-1]);

                                                lrch.add("meteo marinne");

                                           Xcption.Xcption("AnalyzSchem meteo/marinne gkml.rchMulti(lrch, e, lfields, split[4], null));","lrch :"+lrch+"lfields :"+lfields+"clause :MUST_MUST");
                                    
                                           ArrayList<RsEntite> rchMulti = gkml.rchMulti(lrch, e, lfields, "MUST_MUST", null);
                                           
                                           lrslt=supDoublons(lrslt, rchMulti);
                                           addMvue(rchMulti);
                                              System.out.println(lrslt);
      return lrslt;
    }

    private void verifLbase()
    {
        Map<String,RsEntite> mtmp=new HashMap(lbase.size());
        for(RsEntite rs:lbase)
            if(!mtmp.containsKey(rs.getNom()))
              if(rs.getAdresse().split(" ").length<=3+Variable.getLieu().length())
                    mtmp.put(rs.getNom(),rs);
                    
        lbase=new ArrayList(mtmp.values());
    }
    public Set chClimat(List lb)
    {
        lbase=lb;
        addClimat(new ArrayList());
        return llpremeteo;
    }
    private void addClimat(List lrslt) 
    {
        RsEntite b =null;
        if(varBean[5]!=null)
          {
            Date date=(Date) varBean[5];
          /* if(date.before(new Date(System.currentTimeMillis()-1000*60*60*3)))
            {
                SreInitmSpreM();
            }*/
           }
           mSpreM=getmSpreM();
                            
           Meteo yahooClimat ;
                            
           if(lbase!=null) if(!lbase.isEmpty())
                        {
                           verifLbase();
                           llpremeteo=new HashSet<Meteo>();

                           for(RsEntite base:lbase)
                           {

                             try{

                                 String chwoeid=base.getNom();
                                 yahooClimat = null;
                                 
                                 if(mSpreM.containsKey(chwoeid))
                                 {
                                     yahooClimat=mSpreM.get(chwoeid);
                                     llpremeteo.add(yahooClimat);
                                     lrslt.add(yahooClimat);
                                     lrslt.add(base);
                                }
                                else
                                {
                                    String[] splad = base.getAdresse().split(" ");
                                    for(int i=splad.length; i>=0;i--)
                                     if(chwoeid!=null)
                                         chwoeid=UtilRsns.dcdVille(splad[i]);
                                 try{

                                     yahooClimat=yahooClimat(chwoeid);
                                     if(yahooClimat!=null)
                                     {
                                       llpremeteo.add(yahooClimat);
                                       lrslt.add(yahooClimat);
                                       lrslt.add(base);
                                      
                                     }


                                  }catch(Exception ex)
                                  {
                                      Xcption.Xcption("AnalyzSchem yahooClimat yahooClimat(rs.getNom() ); NullPointerException", ex.getMessage());
                                  };

                                 }

                                                lrslt=chMeteoMarinne(base.getAdresse(), lrslt);

                                                 }catch(Exception ex)
                                                  {

                                                      Xcption.Xcption("AnalyzSchem Climat ex", ex.getMessage());
                                                  }
                                              }

                           if(lbase!=null)
                             b= lbase.get(0);                    
                         }/*else if(lbase!=null)
            {
                                 try{
                                 
                                 for(RsEntite base:lbase)
                                 {
                                     String chwoeid=UtilRsns.dcdVille(base.getNom());
                                         if(chwoeid==null)
                                         {
                                             String[] splad = base.getAdresse().split(" ");
                                                chwoeid=UtilRsns.dcdVille(splad[splad.length-1]);
                                            
                                             
                                         }
                                     if(mpreM.containsKey(chwoeid))
                                       {
                                         yahooClimat=mpreM.get(chwoeid);
                                         llpremeteo.add(yahooClimat);
                                         lrslt.add(yahooClimat);
                                         lrslt.add(base);
                                       }
                                     else
                                        {
                                             yahooClimat=yahooClimat(chwoeid);
                                           if(yahooClimat!=null)
                                           {
                                              llpremeteo.add(yahooClimat);
                                              mpreM.put(chwoeid, yahooClimat);
                                           }
                                        }
                                 }
                                 }catch(Exception ex)
                                 {
                                     System.err.print("AnalyzSchem Climat lbase!=null ex");System.out.println(ex.getMessage());
                                 }
            }*/
        // final
           
           
                   if(b!=null)  if(lbase.size()<=2)
                                 {
                                    String[] splitad = b.getAdresse().split(" ");
                                    String reg=splitad[splitad.length-1];
                                     if(vregion.containsKey(reg))
                                     {
                                        
                                        for(String str:vregion.get(reg))
                                         if(mSpreM.containsKey(str))
                                            {
                                              yahooClimat=mSpreM.get(str);
                                              llpremeteo.add(yahooClimat);
                                                lrslt.add(yahooClimat);
                                                
                                               List lrch=Arrays.asList(reg,"zone");
                                               String[] clmns = Variable.getMprmtrEnt().get(
                                                    new Ligne().getClass().getCanonicalName());
                                               List lfields=Arrays.asList(clmns[0],clmns[1]);
                                               
                                               lrslt.add(
                                                       gkml.rchMulti(lrch, new Ligne(), lfields, "MUST_MUST", null));
                                            }
                                     }
                                 } 
                           /*else if(b.getAdresse().contains(
                                   Variable.getPrmtr().get(7)))
                           {
                                RsEntite get = lbase.get(0);
                                     if(vregion.containsKey(get.getNom()))
                                     {
                                        for(String str:vregion.get(get.getNom()))
                                         if(mpreM.containsKey(str))
                                            {
                                              yahooClimat=mpreM.get(str);
                                              llpremeteo.add(yahooClimat);
                                              lrslt.add(str);
                                            }
                                     }
                           }*/
    }
    private List<RsEntite> supDoublons(List<RsEntite> lorg,List<RsEntite> lcomp)
    {  
        RBint rb=(RBint) varBean[2];
        Map<String,RsEntite> msup=new HashMap();
        for(RsEntite rs:lcomp)
        {
            
            if(!msup.containsKey(rs.getNom())&&!schema.getMvue().containsValue(rs))
            {
                String nom = rs.getNom();
                rs.setNom(rb.getString(nom));
                msup.put(nom, rs);
            }
        }
        return Lists.newArrayList(msup.values());
    }
    
    public PrmtrSchm getSchema() {
        return schema;
    }

    public static List<Meteo> getlTotcli(List<RsEntite> lrs)
    {
        HashMap<String,Meteo> mnspreM=new HashMap(mSpreM);
        List<Meteo> lrtr=new ArrayList(lrs.size());
        
            for(RsEntite rs:lrs)
                lrtr.add(mnspreM.get(rs));
        return lrtr;
    }
    public Set<Meteo> getLlpremeteo() 
    {
       /* for(Meteo meteo:llpremeteo)
            if(!mpreM.containsKey(meteo.getWoied()))
            {
                mpreM.put(meteo.getWoied(), meteo);
            }else
            {
                Meteo get = mpreM.get(meteo.getWoied());
                if(!get.getJour().equals(meteo.getJour()))
                {
                    mpreM.remove(get);
                    mpreM.put(meteo.getWoied(), meteo);
                }
            }*/
        return llpremeteo;
    }

    public void setLlpremeteo(Set<Meteo> llpremeteo) 
    {
        
        this.llpremeteo = llpremeteo;
    }

    public List<RsEntite> getLbase() {
        return lbase;
    }

    public void setLbase(List<RsEntite> lbase) {
        this.lbase = lbase;
    }

   

    public static Map<String, Meteo> getmSpreM() {
        return Maps.newHashMap(mSpreM);
    }

}
