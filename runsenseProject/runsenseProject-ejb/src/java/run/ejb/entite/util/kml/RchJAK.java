/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.entite.util.kml;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import java.util.ArrayList;
import java.util.List;
import run.ejb.ejbkml.GereDonne;
import run.ejb.entite.geo.Ligne;
import run.ejb.entite.geo.Pt;
import run.ejb.entite.util.bdd.ChrSpe;
import run.ejb.entite.geo.interf.RsEntite;
import run.ejb.util.ex.Xcption;

/**
 *
 * @author Fujitsu
 */
public class RchJAK 
{
    
    private Document doc;
    private GereDonne gDonne;
    public RchJAK(Document doc) 
    {
        gDonne=new GereDonne();
        gDonne.createEM();
        this.doc = doc;
    }
    public RsEntite rch(RsEntite l)
    {
       for(Feature feat:doc.getFeature())
       {
                   
          l=rchFoler(l, feat);  
         
       }
       return l;
    }
    private RsEntite rchFoler( RsEntite l, Feature feat)
    {
       final RsEntite rs=l;
        ChrSpe env=new ChrSpe();
        List<String> lParent=new ArrayList<String>();
        if(feat instanceof Folder)
            {
                   Folder folder=(Folder) feat;  
                   lParent.add(
                           folder.getName());
                   l=folder.getFeature().stream().
                           map(f->rchFoler(rs, f)).findFirst().get();
                   /*for(Feature f2:folder.getFeature())
                   {
                      l=rchFoler(l, f2);
                   }*/
            }
        else if(feat instanceof Placemark)
                 {
                     if(feat.getAddress()==null)
                     {
                         String ad="";

                                 for(int i=lParent.size()-1; i>=0; i--)
                                 {
                                         ad+=env.chrchUTF8(
                                                 lParent.get(i)
                                                 )+" ";
                                 }
                                 ad+=env.chrchUTF8(feat.getName());
                                 ad=ad.substring(0, ad.split("  ")[0].length());
                         feat.setAddress(ad);
                     }
                     l = verifAd(l,feat);
                 }
        return l;
    }
    //bool chg depuis source kml
 
    private RsEntite verifAd(RsEntite rs, Feature feat)
    {
       try{
        if(rs.getNom().equals(
                feat.getName()))
        {
            Xcption.Xcption("RchJAK verifAd feat.getAddress()+::::+l.getAdresse()",feat.getAddress()+"::::"+rs.getAdresse());
            List rch=new ArrayList();List fields=new ArrayList<String>();
            rch.add(feat.getName());rch.add(feat.getAddress());fields.add("nom");fields.add("adresse");
            ArrayList<RsEntite> rchBoolean = gDonne.rchBoolean(rch, rs, fields, "MUST_SHOULD", null);
            for(RsEntite tmp:rchBoolean)
            {
                if(tmp.getNom().equals(rs.getNom()))
                  if(tmp.getAdresse().contains(
                                rs.getAdresse().substring(
                                rs.getAdresse().length()-10)))
                        { 
                            rs=chCods( tmp,  rs);
                            //gDonne.deleteObject(tmp);
                        }else
                        {//cherche sur coordonnées
                            rs=tmp;

                        }
            }
                
            
            
        }else if(feat.getAddress()==rs.getAdresse())
        {
            //comparaison coordonnées
           
            System.err.print("RchJAK verifAd feat.getAddress()+::::+l.getAdresse()");
            System.out.println(feat.getAddress()+"::::"+rs.getAdresse());
            List rch=new ArrayList();List fields=new ArrayList<String>();
            rch.add(feat.getName());rch.add(feat.getAddress());fields.add("nom");fields.add("adresse");
            ArrayList<RsEntite> rchBoolean = gDonne.rchBoolean(rch, rs, fields, "MUST_SHOULD", null);
            for(RsEntite tmp:rchBoolean)
            {
                
                  if(tmp.getAdresse().contains(
                                rs.getAdresse().substring(
                                rs.getAdresse().length()-10)))
                        { 
                            rs=chCods( tmp,  rs);
                            //gDonne.deleteObject(tmp);
                        }else
                        {//cherche sur coordonnées
                            rs=tmp;

                        }
            }
                            
                          
                      
        }
        }catch(Exception ex)
        {
            System.err.print("RchJAK verifAd(RsEntite rs, Feature feat) Exception");
            System.out.println(ex.getMessage());
        }
        return rs;
    }
    private RsEntite chCods(RsEntite tmp, RsEntite rs)
    {
        if(tmp instanceof Ligne)
                            {
                               
                               Ligne tmplgn=(Ligne) tmp;
                               Ligne lgn=(Ligne) rs;
                               
                               if(tmplgn.getCoords().equals(lgn.getCoords()))
                               {
                                        lgn.setCoords(tmplgn.getCoords());
                                        lgn.setDescription(tmplgn.getDescription());
                                   
                                        rs=lgn;

                                    
                               }else
                               {
                                   String adresse = lgn.getAdresse();
                                   String[] split = adresse.split(" ");
                                   String plc=split[split.length-1];
                                 
                                   try{
                                       int parseInt = Integer.parseInt(plc);
                                       lgn.setAdresse(adresse.replace(plc, String.valueOf(parseInt+1)));
                                   }catch(Exception ex)
                                   {
                                       lgn.setAdresse(adresse+" "+0);
                                   }
                                   lgn.setCoords(tmplgn.getCoords());
                                        lgn.setDescription(tmplgn.getDescription());
                                   
                                        rs=lgn;
                                  
                               }
                                
                            }else if(tmp instanceof Pt)
                            {
                                
                                Pt tmppt=(Pt) tmp;
                               Pt point=(Pt) rs;
                               
                               if(tmppt.getLatitude().equals(point.getLatitude())&&
                                       tmppt.getLongitude().equals(point.getLongitude()))
                               {
                                    point.setLatitude(tmppt.getLatitude());
                                     point.setLongitude(tmppt.getLongitude());
                                 rs = point;

                                 
                               }else
                               {
                                   String adresse = point.getAdresse();
                                   String[] split = adresse.split(" ");
                                   String plc=split[split.length-1];
                                 
                                   try{
                                       int parseInt = Integer.parseInt(plc);
                                       point.setAdresse(adresse.replace(plc, String.valueOf(parseInt+1)));
                                   }catch(Exception ex)
                                   {
                                       point.setAdresse(adresse+" "+0);
                                   }
                                   point.setLatitude(tmppt.getLatitude());
                                     point.setLongitude(tmppt.getLongitude());
                                 rs = point;
                                   
                               }
                            }
        return rs;
    }

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }
    
}
