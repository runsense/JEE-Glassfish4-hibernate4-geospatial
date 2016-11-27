/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author runsense
 */
public class ConnectIGN 
{
    
    private String charset = "UTF-8";

    private String query ;
    
    private String adr="http://wxs.ign.fr/9szjr6mxt7r940lq7mpmr1k5/geoportail/ols?";

    public static void main(String[] args )
    {
        new ConnectIGN();
    }
    public ConnectIGN() 
    {
        /*try {
            param1 = URLEncoder.encode("param1", charset);
            param2 = URLEncoder.encode("param2", charset);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ConnectIGN.class.getName()).log(Level.SEVERE, null, ex);
        }*/
            
            //query = String.format("param1=%s&param2=%s", param1, param2);
            query="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+"\n";
           
        File f=new File("/home/runsense/sentier/reqIgn.xml");
        OutputStream req;
        InputStream result = null;
        try {
            String line; 
            BufferedReader br=new BufferedReader(new FileReader(f));
            while ((line = br.readLine()) != null)
            {
                    query +=" "+ br.readLine()+"\n";
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConnectIGN.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConnectIGN.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        URLConnection connect = null;
        try {
             connect=new URL(adr).openConnection();
             connect.setDoOutput(true);
             connect.setDoInput(true);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ConnectIGN.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConnectIGN.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(connect.getOutputStream(), charset);
            writer.write(query); // Write POST query string (if any needed).
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ConnectIGN.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConnectIGN.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (writer != null) try { writer.close(); } catch (IOException logOrIgnore) {}
        }
        try {
             result = connect.getInputStream();            
        } catch (IOException ex) {
            Logger.getLogger(ConnectIGN.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(result);
    }
    
    
}
