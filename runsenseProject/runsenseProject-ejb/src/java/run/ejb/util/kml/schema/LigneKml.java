/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.util.kml.schema;

/**
 *
 * @author selekta
 */
public class LigneKml implements Ref{
    private double width;
    private String hexacont;
  

    public LigneKml() {
    }

    public LigneKml(double width,String hexacont) {
        this.width=width;
        this.hexacont = hexacont;
     
    }

    public String getHexacont() {
        return hexacont;
    }

    public void setHexacont(String hexacont) {
        this.hexacont = hexacont;
    }

 
    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public String toString() {
        return "LigneKml"+" "+width+" "+hexacont; //To change body of generated methods, choose Tools | Templates.
    }
     
    
}
