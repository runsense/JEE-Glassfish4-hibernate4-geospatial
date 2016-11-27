/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.base;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import run.ejb.base.entite.TempKml;

import run.ejb.ejbkml.GereKml;
import run.ejb.entite.climat.Meteo;
import run.ejb.entite.climat.PrevMt;
import run.ejb.util.ex.Xcption;

/**
 *
 * @author selekta
 */
public class CherchTemp {
   
    private GereKml gKml;
    private static boolean bchgmto=true;
    //private static TempKml stemp;
    /*private static TempKml stmp;
    static{
        stmp=new TempKml("", "", "", null, null);
        
    }*/
    public CherchTemp(GereKml g) 
    {
        
        this.gKml=g;
         
    }
           
    public TempKml cherche(String id)
    {
       // gKml.cEm();
        final String sv = Variable.getSv();
        TempKml tmprtr=null;
        /*if(stmp.getRch().equals(id))
            tmprtr=stmp;
        else
        {*/
         ArrayList<TempKml> rchBdd = gKml.rchBdd(id, "tempkml", "fix", "rch");
     if(!rchBdd.isEmpty())
         if(rchBdd.get(0).getRch().equals(id))
         {
             TempKml get = rchBdd.get(0);
            
             try{
             File f=new File(sv+"/runsense/index/"+get.getTc()+".txt");
                get.setTc(sortieFile(f));
                
                f=new File(sv+"/runsense/index/"+get.getTr()+".txt");
                get.setTr(sortieFile(f));
             }catch(NullPointerException npex)
             {
                     get=null;
             }
             
                 
             
                 
             
             tmprtr= get;
            // gKml.fEm();
         }
             
        //}
        return tmprtr;
    }
    private String sortieFile(File file)
    {
         String rtr=null;
         try {
             rtr = new String(Files.readAllBytes(file.toPath()));
         } catch (IOException ex) { Xcption.Xcption(" addTmp() entrerFile(File file)", ex.getMessage()); }
          return rtr;
    }
    
    public void supbdd(String notif)
    {
        gKml.cEm();
        ArrayList<TempKml> rchBdd=new ArrayList<TempKml>();
        if(notif==null)
        {
            try {
                rchBdd=(ArrayList<TempKml>) gKml.chTout(new TempKml());
            } catch (Exception ex) {
                Xcption.Xcption(" supbdd() gKml.chTout(new TempKml());", ex.getMessage());;
            }
        }
        else
            rchBdd = gKml.rchBdd(notif, "tempkml", "fix", "rch");
        try{
            if(rchBdd!=null)rchBdd.stream(). 
                            map(gKml::supBdd);
        /*if(!rchBdd.isEmpty()) {
           for(TempKml tmpkml:rchBdd)
           {
           
               Set<Meteo> ltmpmto = tmpkml.getLmto();
                    tmpkml.setLmto(null);
                    tmpkml.setLlgn(null);tmpkml.setLpt(null);tmpkml.setLids(null);
                    Xcption.Xcption(" supbdd()tmpkml.getLmto()", gKml.supBdd(tmpkml));
            for(Meteo meteo:ltmpmto)
            {
                meteo = (Meteo) gKml.find(meteo, meteo.getId());
                Set<PrevMt> ltmpprev = meteo.getLprev();
                    meteo.setLprev(null);
                gKml.addBdd(meteo, false);
                for(PrevMt prevm:ltmpprev)
                {
                    prevm.setMeteo(null);
                    gKml.fEm();
                    gKml.supBdd(prevm);
                }
                
                gKml.fEm();
                
                gKml.addBdd(tmpkml, false);
                gKml.fEm();
                gKml.supBdd(meteo);
                    
            }
            gKml.fEm();
            gKml.supBdd(tmpkml);
           }
        }*/
        } catch (Exception ex) { Xcption.Xcption(" supTmp() ex", ex.getMessage()); }
    }
    
    public TempKml addTmp(TempKml tmp)
    {
        // System.err.print(" addTmp() addTmp");System.out.println(tmp.getTc().toCharArray().length);System.out.println(tmp.getTr().toCharArray().length);
        
         
        try{
            File f=new File("/home/runsense/index/"+tmp.getRch()+"tc"+".txt");
            FileWriter fw=new FileWriter(f);
            StringWriter strw=new StringWriter();
                strw.write(tmp.getTc());
                fw.write(strw.toString());
            fw.close();
        tmp.setTc(tmp.getRch()+"tc");
        
            f=new File("/home/runsense/index/"+tmp.getRch()+"tr"+".txt");
            fw=new FileWriter(f);
            strw=new StringWriter();
                strw.write(tmp.getTr());
                fw.write(strw.toString());
            fw.close();
        tmp.setTr(tmp.getRch()+"tr");
            
         
        
        //if(tmp!= null ? tmp.getLmto() != null : !tmp.getLmto().isEmpty())
        /*if(tmp.getLmto()!=null) if(!tmp.getLmto().isEmpty())
        {
            Set<Meteo> lmto = tmp.getLmto();
                
                tmp.setLmto(new HashSet(tmp.getLmto().size()));
                if(tmp.getId()==null)
                    gKml.addBdd(tmp, true);
                else
                    gKml.addBdd(tmp, false);
           try{
                
                for(Meteo mto:lmto)
                {
                    if(mto.getId()!=null)
                     mto = (Meteo) gKml.find(mto, mto.getId());
                    else
                    {
                        Set<PrevMt> lpmt = mto.getLprev();
                        mto.setLprev(new HashSet<PrevMt>(mto.getLprev().size()));
                        gKml.addBdd(mto, true);
                        for(PrevMt pmt:lpmt)
                        {
                            gKml.addBdd(pmt, true);
                            mto.getLprev().add(pmt);
                        }
                        gKml.addBdd(mto, false);
                    }
                   
                    tmp.getLmto().add(mto);
                    
                }
            }catch(Exception uoex)
            {
               
                Xcption.Xcption(" CherchTemp addTmp() addTmp tmp.getRch() Exception", uoex.getMessage());
                /*supbdd(null);
                addTmp(tmp);
            }
            gKml.addBdd(tmp, false);
               
        }else*/
        gKml.boolcreateObj(true);
            gKml.addBdd(tmp );
        
        
       /* for(Meteo mto:lmto)
        {
            tmp.getLmto().add(
                    gKml.getGereDonne().getEm().find(mto.getClass(), mto.getId()));
               
        }*/
        
      
       Xcption.Xcption("CherchTemp addTmp() addTmp tmp.getRch()"+tmp.toString(), tmp.getRch());
         
        // System.runFinalization();
         
         }catch(Exception ex)
         {
             
             Xcption.Xcption("CherchTemp addTmp() addTmp Exception"+ex.getLocalizedMessage(), ex.getMessage());
             /*supbdd(null);
             addTmp(tmp);*/
         }
    
        return tmp;
    }
   /* private List supMeteoDoublons(List<Meteo> list)
    {
        Map mtmp=new HashMap(list.size());
        for(Meteo m:list)
            if(!mtmp.containsKey(m.getWoied()))
                    mtmp.put(m.getWoied(),m);
                    
        return new ArrayList(mtmp.values());
    }*/
    public static void SaddMeteo(List<Meteo> lmto)
    {
        bchgmto=true;
      /*  GereKml gKml=new GereKml();  
        List<Meteo> ltmp=new ArrayList(lmto.size());
           try{  
                for(Meteo mto:lmto)
                {
                    List<PrevMt> lpmt = mto.getLprev();
                        mto.setLprev(new ArrayList<PrevMt>(mto.getLprev().size()));
                    gKml.addBdd(mto, true);
                    
                    for(PrevMt pmt:lpmt)
                    {
                        gKml.addBdd(pmt, true);
                        mto.getLprev().add(pmt);
                    }
                    gKml.addBdd(mto, false);
                    ltmp.add(mto);
                }
            }catch(Exception uoex)
            {
                System.err.print(" CherchTemp addTmp() addTmp tmp.getRch() Exception");System.out.println(uoex.getMessage());
            }
        return ltmp;
        */
           
    }
    
   public TempKml chchKml(TempKml tmpkml)
   {
       
       if(tmpkml.getTc().contains(tmpkml.getRch()))
          tmpkml.setTc(sortieFile(new File("/home/runsense/index/"+tmpkml.getTc()+".txt")));
       if(tmpkml.getTr().contains(tmpkml.getRch()))
          tmpkml.setTr(sortieFile(new File("/home/runsense/index/"+tmpkml.getTr()+".txt")));
             
    return (TempKml) tmpkml;    
   }

  
    public GereKml getgKml() {
        return gKml;
    }

    public void setgKml(GereKml gKml) {
        this.gKml = gKml;
    }

   /* public static TempKml getStemp() {
        return stemp;
    }

    public static void setStemp(TempKml stemp) 
    {
        bchgmto=false;
        CherchTemp.stemp = new TempKml(stemp);
    }

    public static boolean isBchgmto() {
        return bchgmto;
    }*/

    @Override
    protected void finalize() throws Throwable {
        gKml.getGereDonne().finish();
        gKml=new GereKml();
    }


   
  
}
