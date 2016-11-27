/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.util;

import java.net.Socket;
import java.io.FileInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
 
public class FileTransfer extends Thread{
 
	private File fileToSend;
	private Socket socket;
	private DataOutputStream dataOut;
	private FileInputStream fis = null;
 
	public FileTransfer(Socket socket, File fileToSend)
        {
		super();
		this.socket = socket;
		this.fileToSend = fileToSend;
 
		try{
			dataOut = new DataOutputStream(this.socket.getOutputStream());
		}
		catch(IOException e){
			System.out.println("Erreur d IO");
		}
		boolean fileExists = true;
		try{
			//Flux de lecture dans le fichier
			fis = new FileInputStream(this.fileToSend);
		}
		catch(FileNotFoundException e){
			fileExists = false;
		}
 
		if(fileExists){
			//Creation d'un buffer de 2Ko
			byte[] buffer = new byte[2048];
			int bytes = 0;
 
			try{
				//Envoie du nom du fichier dans le flux de sortie
				dataOut.writeUTF(fileToSend.getName());
 
				//Copie du fichier dans le flux de sortie
				while((bytes = fis.read(buffer))!= -1){
					dataOut.write(buffer,0,bytes);
				}
				fis.close();
			}
			catch(IOException e){
				System.out.println("Erreur d IO");
			}
		}
	}
}
