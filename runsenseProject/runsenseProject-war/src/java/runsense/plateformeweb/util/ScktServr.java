/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.util;

import java.net.InetAddress;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class ScktServr extends Thread{
 
	private ServerSocket socketListener;
	private Socket socket;
	
	private InetAddress ipAddress;
        private State state;
        private String hostname;
        private String resp;
        
        //Serveur socket sur 57974
	public ScktServr( String hostname)
        {
            this.hostname=hostname;
		
		try{
			socketListener = new ServerSocket(57974);
                        //localHost:127.0.0.1
			ipAddress = socketListener.getInetAddress();
                        System.out.println("socketListener.getInetAddress()"+ipAddress+"socketListener.getLocalPort()"+socketListener.getLocalPort());
                        start();
		}
		catch(IOException e){
			System.out.println("Impossible d'écouter sur ce port"+e.getMessage()+e.getCause());
                        resp+=e.getMessage();
		}
		
	}
 
	public InetAddress getIpAddress() {
		return ipAddress;
	}
 
	public void setIpAddress(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}
 
    @Override
	public void start(){
		try{
                        StringBuilder buf = new StringBuilder();
                        char[] cbuf = new char[ 2048 ];
                        
                        System.out.println(socketListener.toString());
                        socketListener.setSoTimeout(60000);
                        //socket d'ecoute lancer
			socket = socketListener.accept();
                        InputStream inputStream = socket.getInputStream();
                        InputStreamReader reader = new InputStreamReader(inputStream) ;
                        int num;
                        while(-1!=(num=reader.read(cbuf)))
                        {
                             StringBuilder append = buf.append( cbuf, 0, num );
                             resp=append.toString();
                        }
			
		}
		catch(IOException e){
			System.out.println("Erreur d IO");
                        resp="IOException   "+e.getMessage();
		}
	}

    

    public String getResp() {
        return resp;
    }

    public void setResp(String resp) {
        this.resp = resp;
    }

    public ServerSocket getSocketListener() {
        return socketListener;
    }

    public void setSocketListener(ServerSocket socketListener) {
        this.socketListener = socketListener;
    }
    
    public void close()
    {
        try {
            socket.close();
            socketListener.close();
        } catch (IOException ex) {
            Logger.getLogger(ScktServr.class.getName()).log(Level.SEVERE, null, ex);
        }
	
    }
    
}