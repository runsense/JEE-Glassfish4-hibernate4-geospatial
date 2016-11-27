/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.bean;

import de.micromata.opengis.kml.v_2_2_0.*;
import de.micromata.opengis.kml.v_2_2_0.gx.FlyTo;
import de.micromata.opengis.kml.v_2_2_0.gx.FlyToMode;
import de.micromata.opengis.kml.v_2_2_0.gx.Playlist;
import de.micromata.opengis.kml.v_2_2_0.gx.Tour;
import de.micromata.opengis.kml.v_2_2_0.gx.Wait;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import runsense.plateformeweb.util.geo.TourRsns;
import runsense.plateformeweb.util.geo.TracerRsns;

/**
 *
 * @author Administrateur
 */
@ManagedBean
@SessionScoped
public class Treetable  {

    private static final Logger logger = Logger.getLogger(Treetable.class.getName());  
  
    private TreeNode root;  
  
    private Doc selectedDocument;
    private Document document;
    private List<Coordinate> coordinates;
    private HashMap<String, Feature> sentier;
    private HashMap<String, Polygon> lieuDi;
    private HashMap<String, Point> lieu;
    
    private String Ttext="vide";
    private String btnMap;
    private String btnTracer;
    private Kml marshKml;
    
    private List lat;
    private List lng;
    private final String glassfish="/home/runsense/glassfish3/glassfish/domains/domainRun/applications";
    private final String project="/runsenseProject/runsenseProject-war_war/";
    public Treetable()
    {
        System.err.println("-----Treetable---------");
        sentier=new HashMap<String, Feature>();
        chargeTable();
        
    }
    
    private void chargeTable()
    {
        
        btnMap="http://runsense.re/kml/reunion.kml";
        
        root = new DefaultTreeNode("root", null); 
        Kml kml;
        String name="";
        
         try{
          kml = Kml.unmarshal(new File(glassfish+project+"/kml/reunion.kml"));
   
         document = (de.micromata.opengis.kml.v_2_2_0.Document)kml.getFeature();
         
         }catch(Exception ex)
         {
             kml = Kml.unmarshal(new File("/home/runsense/NetBeansProjects/runsenseProject/runsenseProject-war/web/kml/reunion.kml"));
            
             document = (de.micromata.opengis.kml.v_2_2_0.Document)kml.getFeature();
         }
	        System.out.println(document.getName());
	        List<Feature> t = document.getFeature();
    
                
	        for(Object o : t)
                {

                    //dossier principale
                    Placemark p;
	            Folder f = (Folder)o;
                    
                    sentier.put(f.getName(), f);                    
                    TreeNode nD = new DefaultTreeNode(new Doc("CLIQUER SUR LA FLECHE A GAUCHE, \n PUIS A DROITE POUR LES VISITE",document.getName(), f.getClass().getName(), f), root);
	            List<Feature> tg = f.getFeature();
                    
                    
	            for(Object ftg : tg)
                    {
                        //traitement par ville
                        if(ftg instanceof Placemark)
                                {
                                    p = (Placemark) ftg;                             
                                    name = p.getName();
                                    LineString points = (LineString) p.getGeometry();   
                                     coordinates = points.getCoordinates();
                                    System.out.println("Placemark************"+name+" :"+coordinates);
                                    
                                    sentier.put(p.getName(), p); 
                                    TreeNode nPl = new DefaultTreeNode(new Doc(p.getName(),f.getName(), p.getClass().getName(),p), nD);
                                    
                                }
                        else
                        {  
                          
	                Folder g = (Folder) ftg; 
                        
                        TreeNode nD1 = new DefaultTreeNode(new Doc(g.getName(),f.getName(), g.getClass().getName(),g), nD);
                        System.out.println("\r traitement par ville :"+g.getName());
	                List<Feature> lOj = g.getFeature(); 
                        
                        for(Object plc : lOj)
                        {
                            //
                            
                            Polygon pg;
                            Point pt;                           
                            
                                if(plc instanceof  Placemark)
                                {
                                    p = (Placemark) plc; 
                                    
                                    sentier.put(p.getName(), p); 
                                    TreeNode nPl = new DefaultTreeNode(new Doc(p.getName(),g.getName(), p.getClass().getName(),p), nD1);
                                    
                                    name = p.getName();
                                    try{
                                    LineString points = (LineString) p.getGeometry();   
                                     coordinates = points.getCoordinates();
                                    System.out.println("Placemark************"+name+" :"+coordinates);
                                                                  
                                                                       
                                    }catch(ClassCastException ex)
                                    {
                                        System.out.println(ex.getMessage());
                                       if(p.getGeometry() instanceof Polygon);
                                       {
                                           try
                                           {
                                            pg = (Polygon)p.getGeometry();
                                             try{
                                              lieuDi.put(pg.getId(), pg); 
                                             }catch(NullPointerException np)
                                             {
                                                 System.out.println(np.getMessage());
                                             }
                                             TreeNode nPo = new DefaultTreeNode(new Doc(p.getName(),g.getName(), pg.getClass().getName(),p), nD1);

                                                              Boundary outerBoundaryIs = pg.getOuterBoundaryIs();
                                                               coordinates = outerBoundaryIs.getLinearRing().getCoordinates();
                                                              System.out.println("Polygon************"+name+" :"+coordinates);
                                           }catch(ClassCastException cce)
                                           {
                                                pt = (Point)p.getGeometry();
                                                try{
                                                 lieu.put(pt.getId(), pt); 
                                                }catch(NullPointerException np)
                                                {
                                                    System.out.println(np.getMessage());
                                                }
                                                TreeNode nPo = new DefaultTreeNode(new Doc(p.getName(),g.getName(), pt.getClass().getName(),p), nD1);

                                                                 
                                                                  coordinates = pt.getCoordinates();
                                                                 System.out.println("Polygon************"+name+" :"+coordinates);


                                           }

                                       }
                                       
                                       
                                    }
                                }
                                if(plc instanceof Folder)
                                {
                                        
                                        
                                        Folder ville = (Folder) plc;
                                        
                                        sentier.put(ville.getName(), ville); 
                                        TreeNode nD2 = new DefaultTreeNode(new Doc(ville.getName(),g.getName(), ville.getClass().getName(),ville), nD1);
                                        
                                        List<Feature> lPl = ville.getFeature();
                                        
                                         for(Object plVl : lPl)
                                            {
                                                if(plVl instanceof Placemark)
                                                {
                                                    p = (Placemark) plVl;
                                                    
                                                    sentier.put(p.getName(), p);
                                                    TreeNode nPl = new DefaultTreeNode(new Doc(p.getName(),ville.getName(), p.getClass().getName(),p), nD1);
                                                    
                                                    name =ville.getName()+" : "+ p.getName();
                                                    LineString points = (LineString) p.getGeometry();   
                                                     coordinates = points.getCoordinates();
                                                    System.out.println("InPlacemark************"+name+" :"+coordinates);
                                                    
                                                    
                                                }else if(plVl instanceof Polygon)
                                                {
                   
                                                        pg = (Polygon) plVl;

                                                        lieuDi.put(pg.getId(), pg);
                                                        TreeNode nPl = new DefaultTreeNode(new Doc(pg.getId(),ville.getName(), pg.getClass().getName(),(Feature)plVl), nD1);
                                                        
                                                        name = pg.getId();
                                                        LinearRing linearRing = pg.getOuterBoundaryIs().getLinearRing();
                                                         coordinates = linearRing.getCoordinates();
                                                        System.out.println("Polygon************"+name+" :"+coordinates);
                                                        
                                                        
                                                }else if(plVl instanceof Point)
                                                {
                                                        pt = (Point) plVl;

                                                        lieu.put(pt.getId(), pt);
                                                        TreeNode nPl = new DefaultTreeNode(new Doc(pt.getId(),ville.getName(), pt.getClass().getName(),(Feature)plVl), nD1);
                                                        
                                                        name = pt.getId();

                                                         coordinates = pt.getCoordinates();
                                                        System.out.println("point************"+name+" :"+coordinates);
                                                        
                                                        
                                                }
                                                
                                            }

                                }
                                        
                            }

                        }//else traitement par ville
                      
	            }
	        }
	          
    }

    public String getBtnTracer() {
        return btnTracer;
    }

    public void setBtnTracer(String btnTracer) {
        this.btnTracer = btnTracer;
    }

    public List getLat() {
        return lat;
    }

    public void setLat(List lat) {
        this.lat = lat;
    }

    public List getLng() {
        return lng;
    }

    public void setLng(List lng) {
        this.lng = lng;
    }
    
    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public Doc getSelectedDocument() {
        
        return selectedDocument;
    }

    public void setSelectedDocument(Doc selectedDocument)
    {
        
        
        this.selectedDocument = selectedDocument;
        
        try{
            transText();
        }catch(NullPointerException npe)
        {
            System.out.println(npe.getMessage());
        }
    }


    public void transText()
    {
                marshKml=new Kml();
                
	        
        Feature feat= selectedDocument.getFeature();
        System.out.println("feat.getName()"+feat.getName());
                btnMap=feat.getName();
                
        //Folder formeTracer = formeTracer(feat);
               
        TracerRsns tRsns=new TracerRsns(feat);
        
        Folder formeTracer=tRsns.formeTracer();
        marshKml.setFeature(formeTracer);
        StringWriter stringWriter = new StringWriter();
        marshKml.marshal(stringWriter);
        btnTracer=stringWriter.getBuffer().toString();
                
        System.out.println("btnTracer "+btnTracer.toString());
        marshKml=new Kml();
        //Tour formeTour = formeTour(feat);
        stringWriter=new StringWriter();
        TourRsns rsns=new TourRsns(marshKml, feat);
        HabVue(rsns.getFeat());
        Tour formeTour = rsns.formeTour();
                marshKml.setFeature(formeTour);
          /*  marshKml.marshal(stringWriter);    
        btnMap= stringWriter.getBuffer().toString();  */     
                System.err.println(formeTour);
        File file = new File(glassfish+project+"/kml/"+"base"+".kml");
        
        try {
            file.createNewFile();
            boolean marshal = marshKml.marshal(file);
        } catch (FileNotFoundException fnex) {
            file=new File("/home/runsense/NetBeansProjects/runsenseProject/runsenseProject-war/web/kml/base.kml");
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Treetable.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
                Logger.getLogger(Treetable.class.getName()).log(Level.SEVERE, null, ex);
            }
        
      
    }

    public String getTtext() 
    {
        System.out.println("avant"+Ttext);
        String[] split = Ttext.split(">");
        
        Ttext="";
        System.err.println(split);
        for(int i=0;i<split.length;i++)
        {
            System.err.println(split[i]);
            if(i==1)
                Ttext+="<kml xmlns='http://www.opengis.net/kml/2.2'><Document>";
            else 
                if(i==split.length-2)
                    Ttext+="</Document>";
                if(i==split.length-1)
                    ;
                else
                    try{
                        Ttext+=split[i]+">";
                    }catch(java.lang.ArrayIndexOutOfBoundsException aiooe )
                    {
                        System.err.println("java.lang.ArrayIndexOutOfBoundsException"+aiooe.getMessage());
                    }
                
        }
            System.out.println("apres"+Ttext);
        return Ttext;
    }

    public void setTtext(String Ttext) {
        this.Ttext = Ttext;
    }

    public String getBtnMap() {
        return btnMap;
    }

    public void setBtnMap(String btnMap) {
        this.btnMap = btnMap;
    }
    
    public void charge()
    {
        
    }
    public StringBuffer convert(List<String> v)
    {
        StringBuffer values = new StringBuffer();
        
        for (int i = 0; i < v.size(); ++i) {
            if (values.length() > 0) {
                values.append(',');
            }
            values.append('"').append(v.get(i)).append('"');
        }
        
        return values;
    }
 
    public Folder formeTracer(Feature feat)
    {
        List<Feature> lFeat=new ArrayList<Feature>();
        
        Folder plan=new Folder();
            
            lFeat.add(feat);

            plan.setFeature(lFeat);
                
        return plan;
    }
    public void HabVue(Feature feat)
    {
         //Create a style map.
        StyleMap styleMap = feat.createAndAddStyleMap();

        //Create the style for normal edge.
        Pair pNormal = styleMap.createAndAddPair();
        Style normalStyle = pNormal.createAndSetStyle();
        pNormal.setKey(StyleState.NORMAL);

        LineStyle normalLineStyle =
            normalStyle.createAndSetLineStyle();
        normalLineStyle.setColor("RED");
        normalLineStyle.setWidth(2);


        //Create the style for highlighted edge.
        Pair pHighlight = styleMap.createAndAddPair();
        Style highlightStyle = pHighlight.createAndSetStyle();
        pHighlight.setKey(StyleState.HIGHLIGHT);

        LineStyle highlightLineStyle =
            highlightStyle.createAndSetLineStyle();
        highlightLineStyle.setColor("YELLOW");
 
highlightLineStyle.setWidth(1);

    }

    
}