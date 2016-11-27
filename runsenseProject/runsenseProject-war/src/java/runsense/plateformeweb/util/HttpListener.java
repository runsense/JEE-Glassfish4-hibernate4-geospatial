/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.util;

import java.net.*;  
import java.io.*; 

/**
 *
 * @author runsense
 */
public class HttpListener 
{  

    private String msg;

    public HttpListener(int portNumber)  
    {  
        try  
        {  
            InetAddress localHost = InetAddress.getLocalHost();
            ServerSocket entrance = new ServerSocket(portNumber);  
            Socket s = new Socket();  
             entrance.setSoTimeout(60000);  
             s.setSoTimeout(100);  
            System.out.println("s.getSoTimeout()"+s.getSoTimeout());  
            s = entrance.accept();  
            InputStream inputData = s.getInputStream();  

            msg = new String();  
            int a = 0;  
            while ( (a = inputData.read()) != -1  )  
            {  
                byte i = (byte) a;  
                String st = new Character((char)i).toString();  
                msg += st;  
            }  
            System.out.println(msg+"\n of length "+msg.length());  
            // System.out.print(outputData.);  
            inputData.close();  
            s.close();  
            entrance.close();  
        }  
        catch(Exception e)  
        {  
            msg = e.getMessage()+"cause:"+e.getCause(); 
        }  
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    


}
