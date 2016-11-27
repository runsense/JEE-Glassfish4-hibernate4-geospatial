/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.entite.util.bdd;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author runsense
 */
public class ChrSpe
{
    private final HashMap<String, String> mchIso;
    private final HashMap<String, String> mHtml;
    private final HashMap<String, String> mKml;
    private final HashMap<String, String> mutf8;
    
    private final HashMap<String, String> mBalise;

    public ChrSpe() 
    {
        mBalise=new HashMap<String, String>();
        mKml=new HashMap<String, String>();
        mchIso=new HashMap<String, String>();
        mHtml=new HashMap<String, String>();
        mutf8=new HashMap<String, String>();
        
        mKml.put("&apos;", "'");
        
            mchIso.put("Â","&#194;");
            mHtml.put("&Acirc;","Â");
            
            mchIso.put("à","&#224;");
            mHtml.put("&agrave;","à");
            
            mchIso.put("â","&#226;");
            mHtml.put("&acirc;","â");
            
            mchIso.put("é","&#233;" );
            mHtml.put("&eacute;","é" );
            
            mchIso.put("ê","&#234;");
            mHtml.put("&ecirc;","ê");
            
            mchIso.put("è","&#232;");
            mHtml.put("&egrave;","è" );
            
            mchIso.put("î","&#238;" );
            mHtml.put("&icirc;","î");
            
       mutf8.put( "Ã®","î");
       mutf8.put( "Ã¨","è");
       mutf8.put( "Ãª","ê");
       mutf8.put( "Ã©","é");
       mutf8.put("Ã¢","â");
       mutf8.put( "Ã ","à");
       
       mBalise.put("&lt;b&gt;", "");
            mBalise.put("&lt;/b&gt;", "\n");
            
            mBalise.put("&lt;div style=&quot;font-size:0.9em&quot;&gt;", "");
            mBalise.put("&lt;/div&gt;", "");    
    }

    public String chrchBaliseCh(String dmd)
    {
        for (String next : mBalise.keySet()) {
            if(dmd.contains(next));
            {
                dmd.replace(next,mBalise.get(next));
                /*String[] split = dmd.split(next);
                if(split.length>1)
                {
                dmd="";
                for(int i=0; i<split.length-1; i++)
                {
                dmd+=split[i]+mBalise.get(next);
                }
                dmd+=split[split.length-1];
                }
                */
                
            }
        }
        return dmd;
    }

    /*recherche charactere speciaux
     * term="map";
     * 
     */
    public String chrchKmlCh(String dmd)
    {
        for (String next : mKml.keySet()) {
            if(dmd.contains(next));
            {
                dmd=dmd.replace(next,mKml.get(next));
                /* String[] split = dmd.split(next);
                if(split.length>1)
                {
                dmd="";
                for(int i=0; i<split.length-1; i++)
                {
                dmd+=split[i]+mKml.get(next)+split[i+1];
                }
                }
                else if(!dmd.equals(split[0]))
                {
                dmd="";
                dmd+=split[0]+mKml.get(next);
                }else
                {
                dmd=dmd.replaceAll(mKml.get(next), next);
                }*/
            }
        }
        return dmd;
    }
    public String chrchChIso(String dmd)
    {

        Collection<String> setIso = mchIso.keySet();
        
        for (String next : setIso) {
            if(dmd.contains(next));
            {
                dmd=dmd.replace(next,mchIso.get(next));
            }
            /*String[] split = dmd.split(next);
            
            
            if(split.length>1)
            {
            dmd="";
            for(int i=0; i<split.length-1; i++)
            {
            dmd+=split[i]+mchIso.get(next)+split[i+1];
            }
            }
            else if(!dmd.equals(split[0]))
            {
            dmd="";
            dmd+=split[0]+mchIso.get(next);
            }else
            {
            dmd=dmd.replaceAll(mchIso.get(next), next);
            }
            
            */
        }
        
        
        return dmd;
    }
    public String chrchHtmlCh(String dmd)
    {

        Collection<String> setIso = mHtml.keySet();
        
        for (String next : setIso) {
            if(dmd.contains(next));
            {
                dmd=dmd.replace(next,mHtml.get(next));
                /* String[] split = dmd.split(next);
                if(split.length!=1)
                {
                dmd="";
                for(int i=0; i<split.length-1; i++)
                {
                
                dmd+=split[i]+mHtml.get(next)+split[i+1];
                }
                }
                else if(!dmd.equals(split[0]))
                {
                dmd="";
                dmd+=split[0]+mHtml.get(next);
                }else
                {
                dmd=dmd.replaceAll(mHtml.get(next), next);
                }*/
                
            }
        }
        
        
        return dmd;
    }
  
    public String chrchUTF8(String dmd)
    {

        Collection<String> setIso = mutf8.keySet();
        char[] toCharArray = dmd.toCharArray();
        char[] reforme = new char[toCharArray.length];
        int cpterfm=0;
        boolean change=false;
        for(char c:toCharArray)
            if(c=='Ã')
            {
                change=true;
                
            }else if(change)
            {
                for(String next:setIso)
                {
                    if(next.charAt(1)==c)
                    {
                        reforme[cpterfm]=mutf8.get(next).charAt(0);
                        cpterfm++;
                        change=false;
                    }
                }
            }else
            {
                reforme[cpterfm]=c;
                cpterfm++;
            }
         
        dmd=new String(reforme);
        
        
        return dmd;
    }
    public String reverseUTF8(String dmd)
    {
        Map<String,String> mRutf8=new HashMap();
        String k="";
        for (String next : mutf8.keySet()) {
            mRutf8.put(mutf8.get(next), next);
        }
        int cpterfm=0;
        char[] reforme = new char[dmd.toCharArray().length*2];
        Set<String> setIso = mRutf8.keySet();
        for(char c:dmd.toCharArray())
        {
            boolean pchg=true;
            for(String next:setIso)
                {
                    
                    if(next.charAt(0)==c)
                    {
                        reforme[cpterfm]=mRutf8.get(next).charAt(0);
                        cpterfm++;
                        reforme[cpterfm]=mRutf8.get(next).charAt(1);
                        cpterfm++;
                        pchg=false;
                        
                    }
                }
            if(pchg)
                {
                      reforme[cpterfm]=c;
                        cpterfm++;
                }
                
        }
        int taille=reforme.length-1;
        for(int ib=reforme.length-1; ib>0; ib--)
            if(reforme[ib]==' ')
                taille--;
            else 
                ib=0;
        
        char[] fin=new char[taille+1];
        System.arraycopy(reforme, 0, fin, 0, reforme.length);
        dmd=new String(fin);
        
        return dmd;
    }
    
}
