/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class ScktRcpt implements Runnable  
{
 

	private DataInputStream dataIn;
        
        private String recpt;
        private String exception;
        private StringBuilder buf;
        private char[] cbuf;
        private Socket s;


        
    @Override
    public void run() 
    {
        try {
            
                if(s.isConnected())
                    this.close();
                InputStream inputStream = s.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream) ;
               
                System.out.println( reader.getEncoding());
  
                    int num;
                    
                    while ( -1 != (num=reader.read( cbuf )))
                    {
                            StringBuilder append = buf.append( cbuf, 0, num );
                        
                        System.out.println(append.toString());
                        recpt+=append.toString();
                        
                    }
               
                 s.close();
       } catch (IOException ex) {
        exception="***************run"+ex.getMessage();
        }
    }

        
    public ScktRcpt(String hostEcoute,int port) 
    {
        
         buf = new StringBuilder();
         cbuf = new char[ 2048 ];
         
         recpt=" reception sur"+hostEcoute+" port"+port+" :"+"\n";
        try {
             s = new Socket(hostEcoute,port);
        } catch (ConnectException cex) {
            this.setException(cex.getMessage());
        } catch (UnknownHostException ex) {
            this.setException( ex.getMessage());
        } catch (IOException ex) {
            this.setException( ex.getMessage());
        }    
        
    }
        
    public DataInputStream getDataIn() {
        return dataIn;
    }

    public void setDataIn(DataInputStream dataIn) {
        this.dataIn = dataIn;
    }

    public String getRecpt() {
        return recpt;
    }

    public void setRecpt(String recpt) {
        this.recpt = recpt;
    }
    
    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public void close()
    {
        try {
            s.close();
        } catch (IOException ex) {
           exception="++++++++++++******probleme fermeture***+++++++++++++++";
        }
    }
   
}