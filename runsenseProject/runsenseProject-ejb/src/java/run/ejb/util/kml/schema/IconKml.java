/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.util.kml.schema;

/**
 *
 * @author Fujitsu
 */
public class IconKml implements Ref
{
 
    private double width;
    private String imgref;


    public IconKml() {
    }

    public IconKml( double width, String imgref) {
       
        this.width = width;
        this.imgref = imgref;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public String getImgref() {
        return imgref;
    }

    public void setImgref(String imgref) {
        this.imgref = imgref;
    }

    @Override
    public String toString() {
        return "IconKml"+" "+width+" "+imgref; //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
