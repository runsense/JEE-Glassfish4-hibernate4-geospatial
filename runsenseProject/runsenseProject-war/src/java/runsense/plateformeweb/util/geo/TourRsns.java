/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.util.geo;

import de.micromata.opengis.kml.v_2_2_0.*;
import de.micromata.opengis.kml.v_2_2_0.gx.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Administrateur
 */
public class TourRsns 
{
    
    private Kml marshKml;
    private Feature feat ;
    

    public TourRsns() {
    }

    public TourRsns(Kml marshKml, Feature feat) {
        this.marshKml = marshKml;
        this.feat=feat;
    }
    
    public Tour formeTour() throws NullPointerException
    {
        
        Tour tour = marshKml.createAndSetTour();
        Playlist playlist = tour.createAndSetPlaylist();
        
        Folder d;
        Placemark p;
        LineString ls;
        try{
            if(feat instanceof Placemark)
            {
                p = (Placemark) feat;
                tour.setFeatureSimpleExtension(feat.getFeatureSimpleExtension());
                cplaceMark(p,playlist);
            }
            else if(feat instanceof Folder)
            {
                d=  (Folder) feat;
                tour.setFeatureSimpleExtension(feat.getFeatureSimpleExtension());
                List<Feature> lPl = d.getFeature();
                                      
                for(Object tr : lPl)
                   {
                      if(tr instanceof Placemark)
                      {
                          p = (Placemark) tr;
                            fontTracer(p);
                            cplaceMark(p,playlist);
                      }
                      else if(tr instanceof Polygon)
                      {
                         p = (Placemark) tr;
                         fontPlace(p);
                            cplaceMark(p,playlist);
                      }
                      else
                      {
                          System.out.println("**************exception cast tour*********for******"+feat.toString()+"***************************");
                      }
                   }
            }
            
            else 
                System.out.println("**************exception cast tour***************"+feat.toString()+"***************************");
        }catch(ClassCastException ex)
        {
            System.out.println("*****************************"+ex.getMessage()+"***************************");
        }
       
                 
        return tour;
    }
    
    public void cplaceMark(Placemark pl, Playlist playlist) throws NullPointerException
    {
        Geometry geo = pl.getGeometry();
        LineString ls;
        LinearRing lr;
        HashMap<Integer, Coordinate> ptElv = null;
    
        if(geo instanceof LineString)
        {
            double alti=1000;
            ls= (LineString) pl.getGeometry();
            List<Coordinate> coors = ls.getCoordinates();
            Iterator<Integer> iterKey = null;
            try{
                 ptElv = chchElv(coors);
             iterKey =  ptElv.keySet().iterator();
            }catch(NullPointerException npe)
            {
                System.out.println("elevation"+npe.getMessage());
            }
            
                
            double fact=0;
            int p=iterKey.next();int oldPt=0;int afterPt=0;
            int pt=Math.abs(p);
             double cherchHead = 0; 
             double cherchTilt = 0;
             
            for(int i=0; i<coors.size();i++)
            {
                if((i==0)||(i==afterPt))
                {
                   oldPt=pt;
                   if(iterKey.hasNext())
                        p=iterKey.next();
                   afterPt=Math.abs(p);
                   
                   
                }
                else if(i==oldPt)
                {
                    if(iterKey.hasNext())
                        p=iterKey.next();
                    
                }
                else if((oldPt<i)&&(i<afterPt))
                        {
                            oldPt=pt;
                            if(iterKey.hasNext())
                                p=iterKey.next();
                            afterPt=Math.abs(p);
                        }
                Coordinate co=(Coordinate)coors.get(i);
                
                if(p>0)
                {
                    fact=(Math.sin(1/((afterPt-oldPt)/(Math.PI))));
                }
                else if(p<0)
                {
                    
                    fact=(0.7-Math.sin(1/((afterPt-oldPt)/(Math.PI))));
                }
                
                
                if(i==1||i==p)
                {
                    //temps d'arret 
                    Wait createAndAddWait = playlist.createAndAddWait();
                    createAndAddWait.setDuration(3);
                }
                //playlist
                FlyTo flyTo = playlist.createAndAddFlyTo();
                    flyTo.setFlyToMode(FlyToMode.SMOOTH);
                    
                    if((i==oldPt)||(i==afterPt))
                        flyTo.setDuration(7);
                    else
                        flyTo.setDuration(2);
                    
                    
                if((i+1)<coors.size()-1)
                     cherchHead = cherchHead(coors.get(i),coors.get(i+1));
                
                 
                cPos(flyTo, co, fact, cherchHead);
                
            }
        }
        else if(geo instanceof Polygon)
        {
            Polygon pol = (Polygon) pl.getGeometry();
            Boundary outerBound = pol.getOuterBoundaryIs();
            
            lr= outerBound.getLinearRing();
            List<Coordinate> lco = lr.getCoordinates();
            List<Coordinate> lvue = new ArrayList<Coordinate>();
             
            Coordinate co=new Coordinate(0.00, 0.00, 0.00);
            
            FlyTo flyTo = playlist.createAndAddFlyTo();
                flyTo.setDuration(4);
            int cpte;    
            for(int i=0; i<4; i++)
            {
                cpte=(int) ((lco.size()/4)*i);
                co.setLongitude(lco.get(cpte).getLongitude());
                co.setAltitude(lco.get(cpte).getAltitude());
                co.setLatitude(lco.get(cpte).getLatitude());
                lvue.add(co);
                
                cPos(flyTo, co, 1, 1800);
                flyTo.setFlyToMode(FlyToMode.SMOOTH);
                
            }
     
        }
            
        else
            System.out.println("no found this \n"+geo.toString());
        
    }
    
    public void cPos(FlyTo flyTo,Coordinate co, double fact, double head)
    {
        
        LookAt lookAt = flyTo.createAndSetLookAt();
                            lookAt.setTilt(85-85*fact);
                            lookAt.setLatitude(co.getLatitude());
                            lookAt.setLongitude(co.getLongitude());
                            lookAt.setAltitude(co.getAltitude());
                            lookAt.setRange(750-500*fact);
                            lookAt.setHeading(head);
                            
       /*Camera camera = flyTo.createAndSetCamera();
                        camera.setTilt(85-85*fact);
                            camera.setLatitude(co.getLatitude());
                            camera.setLongitude(co.getLongitude());
                            camera.setAltitude(co.getAltitude());
                            camera.setRoll(750-500*fact);
                            camera.setHeading(head);*/
    }

    public double cherchHead(Coordinate bfr, Coordinate aft)
    {
        double hd=0; 
        double dflg=aft.getLongitude()-bfr.getLongitude();
        double dflat=aft.getLatitude()-bfr.getLatitude();
        
        if(dflg>0)  
        {
            System.err.print("*****dflg>0******");System.out.print(dflg);System.err.println("************");
            //vers 90
            if(dflat>0)
            {
                System.err.print("****dflat>0*******");System.out.print(dflat);System.err.println("************");
                //baisse
                
                    System.err.print("*****Math.atan(dflg/dflat)******");System.out.print(Math.atan(dflg/dflat));System.err.println("************");
                    hd=90-Math.atan(dflg/dflat);
                
            }
            else if(dflat<0)
            {
                System.err.print("*****dflat<0******");System.out.print(dflat);System.err.println("************");
                //baisse
                
                    System.err.print("*****Math.atan(dflg/dflat)******");System.out.print(Math.atan(dflg/dflat));System.err.println("************");
                    hd=90+Math.atan(dflg/dflat);
                
            }
            else
            {
                System.err.print("***dflat=0********");System.out.print(dflat);System.err.println("************");
                hd=90;
            }
        }
        else if(dflg<0)      
        {
            System.err.print("*****dflg<0******");System.out.print(dflg);System.err.println("************");
            //vers -90
            if(dflat>0)
            {
                
                    System.err.print("*****Math.atan(dflg/dflat);******");System.out.print(Math.atan(dflg/dflat));System.err.println("************");
                    hd=-90-Math.atan(dflg/dflat);
                
            }
            else if(dflat<0)
            {
                
                    System.err.print("*****dflg/dflat>1******");System.out.print(dflg/dflat);System.err.println("************");
                    hd=-90+Math.atan(dflg/dflat);;
                
            }
            else
            {
                hd=-90;
            }
        }
        else 
        {
            hd=0;
        }
        return hd;
    }
    
    public HashMap<Integer, Coordinate> chchElv(List<Coordinate> coors)
    {
		boolean cste=true;
        double oldPente=0;double pente;
        HashMap<Integer, Coordinate> mapPente = new HashMap<Integer, Coordinate>();
        try{
		//pour tout point elevation
            for(int cpte=0; cpte<coors.size()-1; cpte++)
            {
                pente=coors.get(cpte+1).getAltitude()-coors.get(cpte).getAltitude();
                
                
                    if((pente*oldPente)>0)
                    {
			cste=true;
                        
                    }
                    else if((pente*oldPente)<0)
                    {
			cste=false;
                        mapPente.put(-cpte, coors.get(cpte));
                    } 
                    else if((pente*oldPente)==0)
                    {
                        mapPente.put(cpte, coors.get(cpte));
                    } 
                
                   
                oldPente=pente;
            }
        }catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        
        
        return mapPente;
        
    }

    public Kml getMarshKml() {
        return marshKml;
    }

    public void setMarshKml(Kml marshKml) {
        this.marshKml = marshKml;
    }

    public Feature getFeat() {
        return feat;
    }

    public void setFeat(Feature feat) {
        this.feat = feat;
    }

    private void fontTracer(Placemark p) 
    {
        Style style = p.createAndAddStyle();
        LineStyle lineStyle = style.createAndSetLineStyle();
        lineStyle.setColor("ff0000ff");
        lineStyle.setWidth(2.1);
        
    }

    private void fontPlace(Placemark p) {
        Style style = p.createAndAddStyle();
        PolyStyle polyStyle = style.createAndSetPolyStyle();
        polyStyle.setColor("7dff0000");
        polyStyle.setOutline(Boolean.TRUE);
        polyStyle.setFill(false);
    }

    
}
