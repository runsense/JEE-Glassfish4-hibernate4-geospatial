/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.entite.util.kml;

import java.util.List;

/**
 *
 * @author Fujitsu
 */
public class YahooRep 
{
    private String url;
    private String jour;
    private List<String> prevision;

    public YahooRep()
    {
        
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public List<String> getPrevision() {
        return prevision;
    }

    public void setPrevision(List<String> prevision) {
        this.prevision = prevision;
    }

}
