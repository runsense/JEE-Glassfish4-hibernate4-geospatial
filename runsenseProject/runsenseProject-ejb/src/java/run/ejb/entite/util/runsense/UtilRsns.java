/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package run.ejb.entite.util.runsense;

import com.google.common.collect.Maps;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.lucene.document.DateTools;

import run.ejb.base.Variable;

/**
 *
 * @author paskal
 */
public class UtilRsns 
{

   
    /**
     *
     * @param v 
     * @return
     */
  public static String datePourStringVue(Date d)
  {
      return org.apache.commons.lang3.time.DateFormatUtils.format(d, "EEE, dd MMM yyyy");
  }
  public static Date stringPourDateVue(String str)
  {
      try {
          return DateTools.stringToDate(str);
      } catch (ParseException ex) {
            return null;
      }
        
  }
  /**
     * ajout - et espace pour vue
     * @param v
     * @return
     */
  public static String dcdVille(String v)
    {
        Map<String, List<String>> mvreg=Variable.getRegion();
          String[] str=new String[2];
           str[0]=" ";
           str[1]="-";

      char[] toChAr = v.toCharArray();
        str[0]+=toChAr[0];str[1]+=toChAr[0];
             for(int i=1; i<toChAr.length;i++)
                  {
                      char c=toChAr[i];
                      
                      if(Character.isUpperCase(c))
                          {
                              str[0]+="-";
                              str[1]+=" ";
                          }

                      str[0]+=c;
                      str[1]+=c;
                  }
     
             return v;
    }
       
    /**
     * suppréssion - et espace pour adresse
     * @param v
     * @return
     */
    public static String invDcdVille(String v)
   {
       Map<String, List<String>> mvreg=Maps.newHashMap(Variable.getRegion());
       String[] chrsup=new String[2];
           chrsup[0]=" ";
           chrsup[1]="-";
           for(String chr:chrsup)
            if(v.contains(chr))
                {
                    int iof = v.indexOf(chr);
                    char c=Character.toUpperCase(v.charAt(
                             iof+1));
                    v=v.substring(0,iof-1)+c+v.substring(iof+2);
                    
                }
            
            return v;
   }
    
    /**
     * 
     * @param date
     * @param r
     * @return
     */
    public static String datePourString(Date date, DateTools.Resolution r)
    {
        
        String res = DateTools.dateToString(date, r); 
        char[] chd = res.toCharArray();
        if(r.equals(DateTools.Resolution.MINUTE))
            res=chd[6]+""+chd[7]+"/"+chd[4]+""+chd[5]+"/"+chd[0]+""+chd[1]+""+chd[2]+""+chd[3]
                        +"à"+chd[8]+""+chd[9]+":"+chd[10]+""+chd[11];
        else if(r.equals(DateTools.Resolution.DAY))
            res=chd[6]+""+chd[7]+"/"+chd[4]+""+chd[5]+"/"+chd[0]+""+chd[1]+""+chd[2]+""+chd[3]
                      ;
            
        return res;
    }

    /**
     *
     * @param str
     * @return
     */
    public static Date stringPourDate(String str)
    {
        Date stringToDate = null;
        str=str.replace("/", "").replace("à", "").replace(":", "");
        char[] tch = str.toCharArray();
        if(tch.length>8)
            str=tch[4]+""+tch[5]+""+tch[6]+""+tch[7]+""+tch[2]+""+tch[3]+""+tch[0]+""+tch[1]+""+tch[8]+""+tch[9]+""+tch[10]+""+tch[11]+"";
        else
            str=tch[4]+""+tch[5]+""+tch[6]+""+tch[7]+""+tch[2]+""+tch[3]+""+tch[0]+""+tch[1];
        try {
            stringToDate = DateTools.stringToDate(str);
        } catch (ParseException ex) {
            System.err.print("UtilDateRsns stringToDate ParseException ex.getErrorOffset()");
            System.out.println(ex.getErrorOffset());
            System.err.print("UtilDateRsns stringToDate ParseException ex");
            System.out.println(ex.getMessage());
        }
       return  stringToDate;
    }
}
