/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.util.kml.schema;

/**
 *
 * @author Fujitsu
 */
public class ObjetKml implements Ref
{
    
    private String hexapoly;
    private String hexacont;
    private String hglhexapoly;
    private String hglhexacont;

    public ObjetKml() {
    }

    public ObjetKml(  String hexapoly,String hglhexapoly, String hexacont,String hglhexacont) {
       
 
        this.hexapoly = hexapoly;
        this.hexacont=hexacont;
        this.hglhexacont=hglhexacont;
        this.hglhexapoly=hglhexapoly;
    }

    public String getHexapoly() {
        return hexapoly;
    }

    public void setHexapoly(String hexapoly) {
        this.hexapoly = hexapoly;
    }

    public String getHglhexapoly() {
        return hglhexapoly;
    }

    public void setHglhexapoly(String hglhexapoly) {
        this.hglhexapoly = hglhexapoly;
    }

    public String getHexacont() {
        return hexacont;
    }

    public void setHexacont(String hexacont) {
        this.hexacont = hexacont;
    }

    public String getHglhexacont() {
        return hglhexacont;
    }

    public void setHglhexacont(String hglhexacont) {
        this.hglhexacont = hglhexacont;
    }

    @Override
    public String toString() {
        return "ObjetKml"+" "+hexapoly+" "+hglhexapoly+" "+ hexacont+" "+hglhexacont; //To change body of generated methods, choose Tools | Templates.
    }

   
}
