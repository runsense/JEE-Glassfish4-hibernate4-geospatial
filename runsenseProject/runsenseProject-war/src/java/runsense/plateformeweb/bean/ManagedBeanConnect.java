/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.bean;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import javax.persistence.EntityManager;
import org.jdom2.Document;
import org.jdom2.output.XMLOutputter;
import runsense.plateformeweb.entite.Identifiant;
import runsense.plateformeweb.entite.Matcap;
import runsense.plateformeweb.entite.Matgwy;
import runsense.plateformeweb.util.ScktRcpt;
import runsense.plateformeweb.util.ScktServr;
import runsense.plateformeweb.util.Xml;


/**
 *
 * @author runsense
 */
@ManagedBean
@RequestScoped
public class ManagedBeanConnect implements Serializable
{
    private ScktServr fs;
    
    private String choix="0";
  
    private List<Identifiant> identf;
    
    private Identifiant i;
    
    private Matgwy gwy;
    private Matcap cap;
    
    
    
    private java.lang.String do_command;
    private java.lang.String channel_get;
    private java.lang.String resp;
    private java.lang.String respUrl="reponse connection";
    private java.lang.String respSocket="ecoute port 57974";
    
    private String value="valeur";
    
    private Xml xml;
    private String hostname;
    private String hostEcoute;
    private int port;
    private String timeStamp;
    private String val;
    private HttpURLConnection con;
    private ScktRcpt skrcp;

    /**
     * Creates a new instance of ManagedBeanConnect
     */
    public ManagedBeanConnect() 
    {
        hostname="90.63.245.152";
        hostEcoute="127.0.0.1";
        port=57974;
        skrcp=new ScktRcpt(hostEcoute,port);
        
        
        gwy = new Matgwy();
        cap = new Matcap();
        
      

    }
    
    //modification de la feuille XML 
    public void scriptCreateXML()
    {
        
        xml.ajoutElemt("do_command", do_command);
        
        xml.ajoutElemt("channel_get", channel_get);
    }
   
    public void connectURL()
    {
        xml=new Xml();
         System.out.println(xml.getDoc().toString());
         con = null;
        try {
            URL url=new URL("http",hostname,"/UE/rci");
            
           // Proxy prox=new Proxy(Proxy.Type.HTTP, new InetSocketAddress(hostname,80));
            
                con = (HttpURLConnection) url.openConnection();
                con.setDoInput(true);
                con.setDoOutput(true);
                
            con.setRequestProperty("Content-Type", "text/xml");
            
            
            
            
            respUrl+=con.toString();
 
            
                try{
                    con.connect();
                    
                OutputStream outputStream = con.getOutputStream();
                
                XMLOutputter xmlo=new XMLOutputter();
                xmlo.output(xml.getDoc(), outputStream);
                OutputStreamWriter writer = new OutputStreamWriter( outputStream );
               // writer.write();
                writer.flush();
                writer.close();
                
                    
                    // reading the response
                            InputStreamReader reader = new InputStreamReader( con.getInputStream() );
                            respUrl+="\n"+"reponse";
                    StringBuilder buf = new StringBuilder();
                    char[] cbuf = new char[ 2048 ];
                    
                    int num;
                    Document doc;
                    while ( -1 != (num=reader.read( cbuf )))
                    {
                            StringBuilder append = buf.append( cbuf, 0, num );
                        
                        this.setRespUrl(append.toString()+"\n");
                        xml.constructeur(respUrl);
                    }
                
                
                
                }catch(IllegalArgumentException ilex)
                {
                    respUrl+=ilex.getMessage();
                }
            
            } catch (IOException ex) {
               
            respUrl+=ex.getMessage();
            
        }
         //System.out.println(respUrl);
         timeStamp=  xml.rech("channel_get", "timestamp") +"\r";
         String rech = "Heure de capture :"+ timeStamp;
         rech +=" objet de capture :"+  xml.rech("channel_get", "name") +"\r";
         val=xml.rech("channel_get", "value");
         rech +="valeur :"+  val;
         this.setRespUrl(rech);
         
    }
        
    
    public void socketReception()
    {        
        skrcp=new ScktRcpt(hostEcoute,port);
        skrcp.run();
        respSocket=skrcp.getRecpt();
        
    }
    public void connectServSockt()
    {
                
        
        fs=new ScktServr(hostname);
        fs.start();
        System.out.println("fs.getIpAddress()"+fs.getIpAddress());
        respSocket=fs.getResp();
        
        fs.start();
       
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getVal() {
        return val;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHostEcoute() {
        return hostEcoute;
    }

    public void setHostEcoute(String hostEcoute) {
        this.hostEcoute = hostEcoute;
    }

    
    public void setVal(String val) {
        this.val = val;
    }

           
    public String getChannel_get() {
        return channel_get;
    }

    public void setChannel_get(String channel_get) {
        this.channel_get = channel_get;
    }

    public String getDo_command() {
        return do_command;
    }

    public void setDo_command(String do_command) {
        this.do_command = do_command;
    }
    


    public Matcap getCap() {
        return cap;
    }

    public void setCap(Matcap cap) {
        this.cap = cap;
    }

    public Matgwy getGwy() {
        return gwy;
    }

    public void setGwy(Matgwy gwy) {
        this.gwy = gwy;
    }

    public String getChoix() {
        return choix;
    }

    public void setChoix(String choix) {
        this.choix = choix;
    }

    public Identifiant getI() {
        return i;
    }

    public void setI(Identifiant i) {
        this.i = i;
    }

    public List<Identifiant> getIdentf() {
        return identf;
    }

    public void setIdentf(List<Identifiant> identf) {
        this.identf = identf;
    }

    public String getResp() {
        return resp;
    }

    public void setResp(String resp) {
        this.resp = resp;
    }

    public String getRespSocket() 
    {
        respSocket=skrcp.getRecpt();
        return respSocket;
    }

    public void setRespSocket(String respSocket) {
        this.respSocket = respSocket;
    }

    public String getRespUrl() {
        return respUrl;
    }

    public void setRespUrl(String respUrl) {
        this.respUrl = respUrl;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public void closeUrl()
    {
        con.disconnect();
        respUrl="Fermeture de la connection"+con.toString();
    }
  
    public void closeConnect()
    {
            fs.close();    
            skrcp.close();
   
    }
}
