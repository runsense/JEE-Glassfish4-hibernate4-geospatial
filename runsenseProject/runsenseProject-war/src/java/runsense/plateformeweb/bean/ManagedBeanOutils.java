/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.bean;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import jxl.write.WriteException;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import runsense.plateformeweb.util.Excel;
import runsense.plateformeweb.util.Xml;



/**
 *
 * @author Administrateur
 */
@ManagedBean
@RequestScoped
public class ManagedBeanOutils implements java.io.Serializable
{

    private Excel xlcs;
    
    private java.lang.String nom;
    private java.lang.String donner;
    
    private String[] tablabel;
    private Float[] tabval;

    private String chartImage;
     
    private DefaultStreamedContent excel;
    
    private CartesianChartModel linearModel;
    private double min=15;
    private double max=25;
    
    private FacesContext fc;
    private ManagedBeanConnect connect;
    
    private int cpte;
    private LineChartSeries srsTemp;
    private ArrayList<String> val;
    
    private MapModel marqueur;
    private final LatLng place;
    
    public ManagedBeanOutils() throws WriteException, IOException 
    {
        cpte=0;
        createLinearModel(); 
         xlcs = new Excel();
         xlcs.CreateExcel();
         
         
          place=new LatLng(-21.017452,55.543077);
         marqueur=new DefaultMapModel();
         marqueur.addOverlay(new Marker(place,"capteur temperature salazie "));
         
         
    }
    
      
        private void createLinearModel() 
        {
        Calendar calendrier = Calendar.getInstance();
            fc = FacesContext.getCurrentInstance();
            connect = fc.getApplication().evaluateExpressionGet(fc, "#{connect}", ManagedBeanConnect.class);
                System.err.println(connect.toString());
      
             linearModel = new CartesianChartModel();  
      
            srsTemp = new LineChartSeries();
            
            srsTemp.setLabel("Température Salazie");  
            srsTemp.setMarkerStyle("diamond");  
            String temps = String.valueOf(calendrier.get(Calendar.HOUR_OF_DAY));
            temps += String.valueOf(calendrier.get(Calendar.MINUTE));
            temps += String.valueOf(calendrier.get(Calendar.SECOND));
            srsTemp.set(temps, 20);  
 
            linearModel.addSeries(srsTemp); 
                       
        }
  
    public String getChartImage() {
        return chartImage;
    }

    public void setChartImage(String chartImage) {
        this.chartImage = chartImage;
    }
     
    public CartesianChartModel getLinearModel() 
    {  
        String substring = connect.getTimeStamp();
        System.err.println("substring"+substring); 
        
             String cach = connect.getVal();
        System.err.println("cche"+cach);                     
         
        List<ChartSeries> series = linearModel.getSeries();
         if(cach!=null)
         {
             chchYs(Double.valueOf(cach));
             String timeStamp = substring.substring(11, 19);
             System.err.println("cche"+cach+"timeStamp"+timeStamp);
            String[] split = timeStamp.split(":");
            timeStamp="";
            for(int i=0; i<split.length;i++)
            {
                timeStamp+=split[i];
            }
             
            ChartSeries get = series.get(0);
            
            get.getData().put(timeStamp, Float.valueOf(cach));
            cpte++;
         }
        
        
        return linearModel;  
   }
    public void addMsg()
    {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "point selectionner","de latitude :"+place.getLat()+" et de Longitude :"+place.getLng()));
    }
    public MapModel getMarqueur() {
        return marqueur;
    }

    public void setMarqueur(MapModel marqueur) {
        this.marqueur = marqueur;
    }

    public void setLinearModel(CartesianChartModel linearModel) {
        this.linearModel = linearModel;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }
    
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String[] getTablabel() {
        return tablabel;
    }

    public void setTablabel(String[] tablabel) {
        this.tablabel = tablabel;
    }

    public Float[] getTabval() {
        return tabval;
    }

    public void setTabval(Float[] tabval) {
        this.tabval = tabval;
    }

    public Excel getXlcs() {
        return xlcs;
    }

    public void setXlcs(Excel xlcs) {
        this.xlcs = xlcs;
    }

    public String getDonner() {
        return donner;
    }

    public void setDonner(String donner) {
        this.donner = donner;
    }

    
    public DefaultStreamedContent getExcel() {
        return excel;
    }

    
    public void setExcel(DefaultStreamedContent excel) {
        this.excel = excel;
    }
    
    public String create()
    {
        xlcs = new Excel();
        try 
        {
            xlcs.setNom(nom);
            xlcs.CreateExcel();
        } catch (WriteException ex) {
            Logger.getLogger(ManagedBeanOutils.class.getName()).log(Level.SEVERE, null, ex);
            return "WriteException";
        } catch (IOException ex) {
            Logger.getLogger(ManagedBeanOutils.class.getName()).log(Level.SEVERE, null, ex);
            return "IOException";
        }
        try {
            excel= new DefaultStreamedContent(new FileInputStream(xlcs.getNom()));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManagedBeanOutils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "excel";

    }
    
    
    public void update()
    {
 
        xlcs.UpadteExcel();
    }
    
    public void appel()
    {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/jogl.jnpl");
        } catch (IOException ex) {
            System.out.println(ex.getCause()+"Message :"+ex.getMessage());
        }
    }

    private void chchYs(double niv) 
    {
        
            min=niv-2.5;
            max=niv+2.5;
        
    }
}
