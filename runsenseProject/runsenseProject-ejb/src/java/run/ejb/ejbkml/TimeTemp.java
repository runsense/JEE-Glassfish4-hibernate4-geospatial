/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.ejbkml;

import java.util.Arrays;
import java.util.Date;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.TimerService;
import org.apache.commons.lang3.time.DateFormatUtils;
import run.ejb.base.CherchTemp;
import run.ejb.base.Variable;
import run.ejb.base.entite.TempKml;
import run.ejb.entite.util.temp.PreChgTemp;
import run.ejb.util.rsentite.schema.AnalyzSchem;
import run.ejb.util.traduction.RBint;

/**
 *
 * @author selekta
 */
@Stateless
@LocalBean
public class TimeTemp {

    private static Date actuel;
    @Resource
    TimerService timerService;

   
    private int[] categSchem;
    private Object[] varBean=new Object[6];
 /*  static {
        
        int[] sctgschm=new int[2];
        
        Map<String,List<String>> mctg = new HashMap<String,List<String>>();  

        
                    
                List<String> lsctg=new ArrayList<String>();
                    //lsctg.add(" ");
                    mctg.put("",lsctg );
                    
                    lsctg=new ArrayList<String>(3);
                    lsctg.add(Variable.getPrmtr().get(0));
                    lsctg.add(Variable.getPrmtr().get(1));
                    lsctg.add(Variable.getPrmtr().get(2));
                    
                    mctg.put(
                            "sentier",lsctg); 
                    lsctg=new ArrayList<String>(4);
                        lsctg.add(Variable.getLtitre().get(21));
                        lsctg.add(Variable.getLtitre().get(22));
                        lsctg.add(Variable.getLtitre().get(23));
                        lsctg.add(Variable.getLtitre().get(24));
                    mctg.put(
                            Variable.getPrmtr().get(5),lsctg);
                    lsctg=new ArrayList<String>(4);
                        lsctg.add(Variable.getPrmtr().get(6));
                        lsctg.add(Variable.getLtitre().get(28));
                        lsctg.add(Variable.getLtitre().get(19));
                        lsctg.add(Variable.getLtitre().get(20));
                    mctg.put(
                           "lieuDit/pointInteret",lsctg);  
                    lsctg=new ArrayList<String>(Variable.getLzact().length);
                        for(String str:Variable.getLzact())
                            lsctg.add(str);
                    mctg.put(
                           Variable.getZact(),lsctg);
                    
        Map<String, List<String>> region = new HashMap(Variable.getRegion());
         region.put("", new ArrayList<String>());
      if(Variable.isBfe())
      {
        for(Map.Entry<String,List<String>> setctg:mctg.entrySet())   
        {
            
          for(Map.Entry<String,List<String>> settmp:region.entrySet())  
          {
              
              for(String mssctg:setctg.getValue())
            {
                String c;
                c=setctg.getKey();
                if(mssctg!=" ")
                    c=mssctg;
                if(settmp.getKey().equals(" "))
                    sctgschm[0]=0;
                else
                    sctgschm[0]=1; 
                        Object[] vBean=new Object[6];
                         
                         scTemp(c, settmp.getKey(),vBean,sctgschm);
                         sctgschm[0]=2;
                     for(String sstmp:settmp.getValue())  
                    {
                        
                       scTemp(c, sstmp,vBean,sctgschm);
                    }
            }
             
            
          }
          
        }
        for(Map.Entry<String,List<String>> setctg:mctg.entrySet())   
          {
              
              for(String mssctg:setctg.getValue())
            {
                String c;
                c=setctg.getKey();
               
                
                     sctgschm[0]=1; 
                        Object[] vBean=new Object[6];
                         
                         scTemp(c, mssctg,vBean,sctgschm);
                         sctgschm[0]=2;
                    
            }
             
            
          }

        }
      else
      {
          System.err.print("TimeTemp static Variable.isBfe()");System.out.println(Variable.isBfe());
      }
    }*/
   
    public TimeTemp() 
    {
      /* ScheduleExpression scheduleExp =
                            new ScheduleExpression().hour("*");
        Timer timer = timerService.createCalendarTimer(scheduleExp);
        System.err.print("TimeTemp tempoH()");System.out.println(scheduleExp);*/
    }
    
    @Schedule( hour = "*/3")
    @Timeout
    public void tempoH()
    {
        //System.err.print("TimeTemp tempoH()");System.out.println("hour = \"*\", minute = \"*\",second = \"*/5\"");
         AnalyzSchem.SreInitmSpreM();
       
    }
   // @Schedule( hour = "1")
   /* public void tempoD()
    {
      
         System.err.print("TimeTemp tempoD()");
        Map<String, TempKml> mstmp = CherchTemp.getMstmp();
        Set<String> keySet = mstmp.keySet();
         Date dj = new Date(System.currentTimeMillis());
        Date dav = new Date(dj.getYear(), dj.getMonth(), dj.getDay()-1);
        String strdav = DateFormatUtils.format(dav, "EEE, dd MMM yyyy");
        for(String str:keySet)
            if(str.contains(strdav))
                mstmp.remove(str);
        CherchTemp.rmzMap(new HashMap(mstmp));
    }*/

    private static void scTemp(String ctg, String rg, Object[] vBean,int[] categSchem) 
    {
        String notif;
        String chchr="";
        GereKml g = new GereKml();
        try{
        
        CherchTemp temp=new CherchTemp(g);
          if(vBean!=null)
          {
                if(ctg.equals("sentier"))
                {
                    categSchem[1]=1;

                }
                else if(ctg.equals(Variable.getPrmtr().get(5)))
                {
                    categSchem[1]=3;
                    vBean[3]= ctg;
                }else if(ctg.equals(Variable.getZact()))
                {
                    categSchem[1]=5;
                    vBean[3]= ctg;
                }
                else if(ctg.equals("lieuDit/pointInteret"))
                {
                    categSchem[1]=4;
                    vBean[4]= ctg;
                }

                vBean[0]=rg;


                for(Object o:vBean)
                    if(o!=null&&o!="")
                    {
                         if(!chchr.contains((String)o))
                            {
                                chchr+=o;

                            }
                    }

                chchr+=true+"francais";
                notif=String.valueOf(categSchem[0])+String.valueOf(categSchem[1])+chchr;
                notif=notif.replaceAll("/", "");

                TempKml chtmp = temp.cherche(notif);
                if(chtmp==null)
                        {
                PreChgTemp prechg=new PreChgTemp();

                        System.err.print("TimeTemp tempoH()");
                        try{
                        System.out.println(
                                Arrays.toString(prechg.chgtmp(notif, categSchem, vBean)));
                        }catch(java.lang.OutOfMemoryError oemex)
                        {
                            System.out.println(
                                Arrays.toString(prechg.chgtmp(notif, categSchem, vBean)));
                        }
                    
                    }
          }
          }finally{
            g.fEm();
            g=null;
        }/*else
          {
              chchr+=String.valueOf(categSchem[0])+String.valueOf(categSchem[1]);
              if(categSchem[0]==0&&categSchem[1]==0)
                chchr+=DateFormatUtils.format(new Date(System.currentTimeMillis()), "EEE, dd MMM yyyy");
              else
                  chchr+=rg+ctg;
              chchr+=true+"francais";
              TempKml chtmp=new TempKml();
              
              CherchTemp.trsfmMstmp(chchr, null);
          }*/
           
    }
    /*private void cTemp(String ctg, String tmp) {
        String notif;
         varBean=new Object[6];
        
         
         if(ctg!=null)
            if(ctg.equals(""))
            {
                 
                    varBean[5]=new Date(System.currentTimeMillis());  
                categSchem[1]=0;
            }
            else if(ctg.equals("sentier"))
            {
                categSchem[1]=1;
                varBean[4]="sentier";
            }
            else if(ctg.equals(Variable.getPrmtr().get(5)))
            {
                categSchem[1]=3;
                varBean[3]=ctg;
            }
            else if(ctg.equals(Variable.getPrmtr().get(5)))
            {
                categSchem[1]=5;
                varBean[3]=ctg;
            }
            else if(ctg.equals("lieuDit/pointInteret"))
            {
                categSchem[1]=4;
                varBean[4]="lieuDit/pointInteret";
            }
            varBean[0]=tmp;
            
            String chchr="";
            for(Object o:varBean)
                if(o!=null&&o!="")
                {
                    if(o instanceof Date)
                    {
                        Date d=(Date) o;
                        chchr+= DateFormatUtils.format(d, "EEE, dd MMM yyyy");
                    }else if(o instanceof RBint)
                        ;
                    else if(!chchr.contains((String)o))
                        {
                            chchr+=o;
                            
                        }
                }
            chchr+=true+"francais";
            notif=String.valueOf(categSchem[0])+String.valueOf(categSchem[1])+chchr;
            
                
            CherchTemp temp=new CherchTemp();
            TempKml chtmp = temp.cherche(notif);
                
            PreChgTemp prechg=new PreChgTemp();
                 prechg.setStic(true);
                    if(chtmp!=null&&!chtmp.getLmto().isEmpty())
                    {
                        Date date = new Date(chtmp.getLmto().get(0).getJour()
                                +3*60*60*1000);//3h 
                        if(actuel.after(date))
                        {
                            
                            
                            System.err.print("TimeTemp tempoH()");
                            System.out.println(
                                    Arrays.toString(prechg.chgtmp(notif, categSchem, varBean)));
                        }
                    }else
                    {
                        System.err.print("TimeTemp tempoH()");
                        System.out.println(
                                Arrays.toString(prechg.chgtmp(notif, categSchem, varBean)));
                    }
              
    }*/
}
