/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.util.rsentite.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import run.ejb.base.Variable;
import run.ejb.ejbkml.GereKml;
import run.ejb.ejbkml.GereKmlLocal;
import run.ejb.entite.climat.Meteo;
import run.ejb.entite.geo.interf.RsEntite;
import run.ejb.util.ex.Xcption;
import run.ejb.util.kml.schema.Ref;
import run.ejb.util.kml.schema.RsnsObj;
/**
 *
 * @author Fujitsu
 */
public class PrmtrSchm 
{//utilisations schema
    public final static Predicate<RsnsObj> prediLschm = (s) -> Optional.ofNullable(
                                                            Optional.ofNullable(s.getPourKml()).get().get(0)).isPresent();
   private GereKmlLocal gkml;
   private List<RsnsObj> lschmvu;
    private AnalyzSchem analyz;
   private Set<Meteo> llpremeteo;
  private Map<String,RsEntite> mvue;
    private Object[] varBean;
  
    public PrmtrSchm(GereKml gkml) 
    {
      
      
        analyz=new AnalyzSchem(gkml);
        lschmvu=new ArrayList<RsnsObj>();
       mvue=new HashMap<String, RsEntite>();
    }
    public List<RsnsObj> PrmtrSchm(int categ, int env,Object[] varBean, String modifSchem) 
    {
        if(gkml==null)
           gkml=new GereKml();
       
        gkml.cEm();
        try{
        final Map<Object,List<RsnsObj>> affichage;
            affichage= Variable.getAffichage();
        
            analyz=new AnalyzSchem((GereKml) gkml);   
              mvue=new HashMap<String, RsEntite>();
            String rch=String.valueOf(categ)+String.valueOf(env) ;
                  Xcption.Xcption("RchBean PrmtrSchm categSchem[0] categSchem[1] modif", rch);
             
          
             lschmvu=new ArrayList<RsnsObj>();
             if(affichage.containsKey(rch)) 
             {
                 lschmvu.addAll(affichage.get(rch));
                 if(modifSchem.contains("categ: "))
                    {
                        modifSchem=modifSchem.replaceAll("categ: ", "");
                        String[] splitMod = modifSchem.split("/");
                        for(RsnsObj rsns:lschmvu)
                            if(rsns.getRef().contains(splitMod[0]))
                                rsns.getRef().replace(splitMod[0], splitMod[1]);

                    }
             }
             else
                 lschmvu=null;
             
             analyz.setLlpremeteo(llpremeteo);
             
             analyz.AnalyzSchem(this, varBean);
         
         llpremeteo=analyz.getLlpremeteo();
        }catch(Exception ex)
        {
            Xcption.Xcption("public List<RsnsObj> PrmtrSchm(int categ, int env,Object[] varBean, String modifSchem)  Exception", ex.getMessage());
        }finally{
            gkml.fEm();
        }
        return lschmvu;
    }
    public List<RsnsObj> PrmtrSchm(int categ, int env,Object[] vB, boolean avctracer) 
    {
         varBean=vB;
       //map pramétrage des vues
        if(gkml==null)
       
            gkml=new GereKml();
        
        gkml.cEm();
        try{
        final List<String> sprmtr = Variable.getPrmtr();
        final Map<Object,List<RsnsObj>> affichage;
            affichage= Variable.getAffichage();
        
            analyz=new AnalyzSchem((GereKml) gkml);
              lschmvu=new ArrayList<RsnsObj>();
              mvue=new HashMap<String, RsEntite>();
              if(llpremeteo!=null)
                  analyz.setLlpremeteo(llpremeteo);
              
              String rch=String.valueOf(categ)+String.valueOf(env) ;
                  Xcption.Xcption("RchBean PrmtrSchm categSchem[0] categSchem[1]", rch);
                
             
        if(avctracer)
        {
            
            lschmvu.addAll(affichage.getOrDefault(rch,null));
            
        }else
        {
            if(affichage.containsKey(rch)) 
              lschmvu.add(affichage.get(rch).get(0));
            else
              lschmvu=null;
        }
        
        
        lschmvu.stream().filter(rsns->rsns.getRef().equals("envrch/categ")||
                   rsns.getRef().equals(sprmtr.get(5))||
                   rsns.getRef().equals("envrch")).
                forEach(this::schCateg);
        /*if(varBean[3]!=null)
          for(RsnsObj rsns:lschmvu) 
           if(rsns.getRef().equals("envrch/categ")||
                   rsns.getRef().equals(sprmtr.get(5))||
                   rsns.getRef().equals("envrch"))
           {
              RsnsObj rmv=rsns;
              lrmv.add(rmv);
               
           }*/
        
         analyz.AnalyzSchem(this, varBean);
         
         llpremeteo=analyz.getLlpremeteo();
        /* for(int i=0; i<lschmvu.size()-1; i++)
         {
             RsnsObj rmvn=lschmvu.get(i);
             if(rmvn.getPourKml().get(0).isEmpty())
                 lschmvu.remove(rmvn);
         }*/
         }catch(Exception ex)
        {
            Xcption.Xcption("public List<RsnsObj> PrmtrSchm(int categ, int env,Object[] varBean, String modifSchem)  Exception", ex.getMessage());
        }finally{
            gkml.fEm();
        }
        return lschmvu;
    }
    private void schCateg(RsnsObj trf)
    {
        List prmtr=Variable.getPrmtr().subList(0, 9);
     
       if(trf!=null)
        if(trf.getRef().equals("envrch/"+prmtr.get(5))||
                trf.getRef().equals("envrch/"+prmtr.get(6))||
                trf.getRef().equals("envrch/"+prmtr.get(4)))
           {
               
               int indexOf = lschmvu.indexOf(trf);
                    lschmvu.remove(trf);
               String ctg=(String) varBean[3];
               for(String tmp:ctg.split("/"))
               {
                   if(prmtr.contains(tmp))
                        trf.setRef("envrch/"+tmp);
                   
                 if(trf.getRef()!=null)  
                   lschmvu.add(indexOf, trf);
                   indexOf++;
               }
               
           }else if(trf.getRef().equals(Variable.getPrmtr().get(5)))
           {
               
               int indexOf = lschmvu.indexOf(trf);
                    lschmvu.remove(trf);
            Ref prmtrVue = trf.getPrmtrVue();
               String res=(String) varBean[3];
               for(String tmp:res.split("/"))
               {
                   if(prmtr.contains(tmp))
                        trf=new RsnsObj(tmp, prmtrVue,tmp);
                   else
                      trf=new RsnsObj((String) prmtr.get(5), prmtrVue,(String) prmtr.get(5)); 
                 if(trf.getRef()!=null) 
                   lschmvu.add(indexOf, trf);
                   indexOf++;
               }
           }/*else if(trf.getRef().equals("envrch"))
           {
               
               int indexOf = lschmvu.indexOf(trf);
                    lschmvu.remove(trf);
            Ref prmtrVue = trf.getPrmtrVue();
               String res=(String) varBean[3];
               for(String tmp:res.split("/"))
               {
                   if(prmtr.contains(tmp))
                     trf=new RsnsObj("envrch/"+tmp, prmtrVue,tmp);
                   else
                     trf=new RsnsObj((String) varBean[4],prmtrVue,(String) varBean[4]);  
                   
                   if(trf.getRef()!=null)
                      lschmvu.add(indexOf, trf);
                   indexOf++;
               }
           }*/
    }
    public Set<Meteo> chClimat(List<RsEntite> lrs)
    {
        return analyz.chClimat(lrs);
    }
    public Map<String, RsEntite> getMvue() {
        return mvue;
    }

    public void setMvue(Map<String, RsEntite> mvue) {
        this.mvue = mvue;
    }


    public List<RsnsObj> getLschmvu() {
        return lschmvu;
    }

    public void setLschmvu(List<RsnsObj> lschmvu) {
        this.lschmvu = lschmvu;
    }

    public AnalyzSchem getAnalyz() {
        return analyz;
    }

    public void setAnalyz(AnalyzSchem analyz) {
        this.analyz = analyz;
    }

    public Set<Meteo> getLlpremeteo() {
        return llpremeteo;
    }

    public void setLlpremeteo(Set<Meteo> llpremeteo) {
        this.llpremeteo = llpremeteo;
    }


    
    
}
