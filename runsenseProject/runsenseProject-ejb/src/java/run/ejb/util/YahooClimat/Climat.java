/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.util.YahooClimat;

/**
 *
 * @author runsense
 */
public class Climat 
{
    private String timestamp;
    private String temp;
    private String tempair;
    private String pluie3h;
    private String humidite;
    private String nebulosite;
    private String ventdirection;
    private String ventmoyen;
    private String ventrafales;

    public Climat() {
    }


    public Climat(String timestamp, String temp, String tempair, String pluie3h, String humidite, String nebulosite, String ventdirection, String ventmoyen, String ventrafales) {
        this.timestamp = timestamp;
        this.temp = temp;

        this.pluie3h = pluie3h;
        this.humidite = humidite;
        this.nebulosite = nebulosite;
        this.ventdirection = ventdirection;
        this.ventmoyen = ventmoyen;
        this.ventrafales = ventrafales;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getPluie3h() {
        return pluie3h;
    }

    public void setPluie3h(String pluie3h) {
        this.pluie3h = pluie3h;
    }

    public String getHumidite() {
        return humidite;
    }

    public void setHumidite(String humidite) {
        this.humidite = humidite;
    }

    public String getNebulosite() {
        return nebulosite;
    }

    public void setNebulosite(String nebulosite) {
        this.nebulosite = nebulosite;
    }

    public String getVentdirection() {
        return ventdirection;
    }

    public void setVentdirection(String ventdirection) {
        this.ventdirection = ventdirection;
    }

    public String getVentmoyen() {
        return ventmoyen;
    }

    public void setVentmoyen(String ventmoyen) {
        this.ventmoyen = ventmoyen;
    }

    public String getVentrafales() {
        return ventrafales;
    }

    public void setVentrafales(String ventrafales) {
        this.ventrafales = ventrafales;
    }

    public String getTempair() {
        return tempair;
    }

    public void setTempair(String tempair) {
        this.tempair = tempair;
    }

}
