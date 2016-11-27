/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.util;

/**
 *
 * @author runsense
 */


import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.output.XMLOutputter;



public class XmlClient {

    // Synonymes pour éviter de manipuler les index
    // du tableau args[] directement
    public static final int HOST = 0;
    public static final int URL = 1;
    public static final int REQUEST = 2;

    private String rsp;
    private Socket socket;
    private OutputStream out;
    private InputStream in;
    
    private String[] prop;
        
    public XmlClient()
    {
        
            prop = new String[3]; 
            
       try{
           
           socket = new Socket("localhost", 56974);
           
            // récupération des flux d'écriture et de lecture
                out = socket.getOutputStream();
                in = socket.getInputStream();
         
                
                                   connect(prop);
        
           
            
            
        } catch (UnknownHostException ex) {
            Logger.getLogger(XmlClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XmlClient.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
    }

    public String envoieRequete() throws IOException, InterruptedException
    {
        

        System.out.println("ENVOI REQUETE:");

        // génération de l'entête
        StringBuffer header = new StringBuffer();
        header.append("POST "+prop[URL]+" HTTP/1.0\n");
        header.append("Content-Type: text/xml\n");
        // la taille de la requête correspond à la taille du fichier
        // qui contient la requete XML-RPC
        File file=new File(prop[REQUEST]);
        header.append("Content-Length: "+(file.length()+2)+"\n");
        header.append("\n");

        // envoi de l'entête (écriture sur le flux de sortie)
       byte[] buffer = header.toString().getBytes("UTF-8");
        out.write(buffer);
        // et affichage à l'écran
        System.out.write(buffer);

        // chargement de la requête XML-RPC
        //InputStream src = new FileInputStream(file);  
        InputStream src = nodeToInputStream();
        
        buffer = new byte[1024];
        int b = 0;
        while((b = src.read(buffer)) >= 0){
            // écriture de la requête sur le flux de sortie
            out.write(buffer, 0, b);
            // et affichage à l'écran
            System.out.write(buffer, 0, b);
                     
        }   
       
        //fin de la requete (2 sauts de lignes)
        buffer = "\n\n".getBytes("UTF-8");
        out.write(buffer);
        System.out.write(buffer);

        System.out.println("RECEPTION REPONSE:");

        // affichage de la réponse 
        String stbuf="";
        while((b = in.read(buffer)) >= 0)
        {
            System.out.write(buffer, 0, b);
            stbuf=new String(buffer);
           if((stbuf.equals("<"))||(stbuf.equals(">")))
               rsp+="";
           
            rsp+=stbuf;           
        }

        System.out.println(rsp);
        
        src.close();
        
        out.wait();
        in.wait();
        
       
        return rsp;
    }
    public void connect(String[] prop) throws IOException
    {
         // valeur par défaut
        
        
        if(!isDefined(prop, HOST))       prop[HOST] = "localhost";       
        if(!isDefined(prop, URL))        prop[URL]  = "http://80.8.174.158/UE/rci";
        if(!isDefined(prop, REQUEST))    prop[REQUEST] = "/home/runsense/glassfish-3.1.2/glassfish/domains/domainRunsense/applications/plateformeWeb-1.0-SNAPSHOT/WEB-INF/acess.xml";

        // connexion au serveur (ouverture de la socket)
        
        System.out.println("prop[URL] :"+prop[URL]+"prop[REQUEST] :"+prop[REQUEST]);
         
    }
    public static boolean isDefined(String[] array, int index){
        try{
            return array[index] != null;
        }
        catch(ArrayIndexOutOfBoundsException e){
            return false;
        }
        
    }

    public String fermeture() throws IOException
    {
         // fermeture des différents flux
        
        out.close();
        
        in.close();
        
       return "flux fermé";
    }
    public String getRsp() 
    {
        return rsp;
    }

    public void setRsp(String rsp) {
        this.rsp = rsp;
    }

    private InputStream nodeToInputStream() 
    {
        Xml xml=new Xml("/home/runsense/XMLdigi/acess.xml");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        XMLOutputter writer=new XMLOutputter();
        try 
        {
            writer.output(xml.getDoc(), outputStream);
        } catch (IOException ex) {
            Logger.getLogger(XmlClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                
        return new ByteArrayInputStream(outputStream.toByteArray());
}

    
    
    
}