/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.bean;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.List;


import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import run.ejb.entite.geo.Coord;

/**
 *
 * @author Administrateur
 */

public class GrphElv implements Serializable
{
    
    private CartesianChartModel mdlgrph;
    private ChartSeries series;

    private ArrayList<Coord> list=new ArrayList<Coord>();
    private int progress;
    public GrphElv()
    {
        list=new ArrayList();
        mdlgrph =new CartesianChartModel();
        series=new ChartSeries("vue de la pente");

       // createGraph(null, null);
    }

    public GrphElv(String nom, ArrayList<Coord> list)
    {
        this.list=list;
        mdlgrph =new CartesianChartModel();
        series=new ChartSeries(nom);
       createGraph(nom, list);
    }
    
     public CartesianChartModel createGraph(String nm, List<Coord> list)throws ClassCastException
    {
       
        mdlgrph =new CartesianChartModel();
        System.err.print("GrphElv createGraph list valeur ");System.out.println(nm);System.out.println(list);
        Integer cpte=0;
        if(list!=null)
        {
            
            series.setLabel(nm);
            Double alt = 0d;
            for(Coord lg:list)
            {
              
               try{ 
                if(lg.getAlt()!=null) 
                {
                    series.set(cpte.toString(), lg.getAlt());
                     alt = lg.getAlt();
                    
                }else
                {
                    series.set(cpte.toString(), alt);
                }
               }catch(java.lang.NullPointerException npex)
               {
                   series.set(cpte.toString(), alt);
               }
                   cpte++;  
                   
                  progress=20+list.size()*(50/list.size());
                    
            }
            
        }
        else
        {
            series=new ChartSeries("vide");
            
            series.setLabel("");
                
                series.set("", 0);
        }
        mdlgrph.addSeries(series);
        return mdlgrph;
    }


    public ChartSeries getSeries() {
        return series;
    }

    public void setSeries(ChartSeries series) {
        this.series = series;
    }

    public ArrayList<Coord> getList() {
        return list;
    }

    public void setList(ArrayList<Coord> list) {
        this.list = list;
    }
    
    
}
