/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.util;




import java.io.*;
import java.net.ContentHandler;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.w3c.dom.Node;

/**
 *
 * @author Administrateur
 */
public class Xml 
{
    private String chemin = "/home/runsense/XMLdigi/acess.xml";
    private Document doc;
    
    private Element rootNode;
    
    private List<Element> child;
    private List<Element> child2;

    private List<Element> childChannel;

    
    private SAXBuilder builder;
    
    private String pointeur = "2";
    
    public Xml() 
    {
     
       childChannel = new ArrayList();    
       init();
             
    }
    
    public Xml(String chemin) 
    {
       this.chemin = chemin;
       
       childChannel = new ArrayList();
       
       init();
             
    }


    public String getPointeur() {
        return pointeur;
    }

    public void setPointeur(String pointeur) {
        this.pointeur = pointeur;
    }

    public void constructeur(String read )
    {
        builder = new SAXBuilder();
        try {          
            doc = (Document) builder.build("/home/runsense/XMLdigi/reponse.xml");
            
        } catch (JDOMException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        Element transforme = transforme(read);
        doc.removeContent();
        doc.addContent(transforme);
        XMLOutputter xmlo=new XMLOutputter();
        try 
        {
           
            xmlo.output(doc, new FileWriter("/home/runsense/XMLdigi/reponse.xml"));
        } catch (IOException ex) {
            Logger.getLogger(Xml.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void init()
    {
        builder = new SAXBuilder();
        try {
            
            doc = (Document) builder.build(chemin);
        } catch (JDOMException ex) {
            Logger.getLogger(Xml.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Xml.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(chemin);
        
           
    }

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }
  
    

    public String rech(String balise, String attribut)
    {
        List<String> val=new ArrayList<String>();
        rootNode = doc.getRootElement();
        
        child =  rootNode.getChildren();
        do{
            child2 =  child.get(0).getChildren();
            child=child2;
        }while(!child.get(0).getName().equals(balise));
        Attribute attribute = child.get(0).getAttribute(attribut);
 
        return attribute.getValue();
    }
    

    
    public void ajoutElemt(String nom, String fonction)
    {
        
        Element elmts= new Element(nom);
        
        elmts.setText(fonction);
        
        childChannel.add(elmts);
        
    }

    public HashMap<String,ArrayList> voirSchema(Element elmts)
    {
        HashMap<String,ArrayList> tableau = new HashMap<String, ArrayList>(); 
        
        while(!elmts.getContent().isEmpty())
        {
            ArrayList content = (ArrayList) elmts.getContent();
            tableau.put("1er", content);
            for(int i=0; i<content.size(); i++)
            {               
                Element get = (Element) content.get(i);
                List contentElmts = get.getContent();
            }
                
        }
        
        return tableau;
    }
   public String lireXml(Node docXml)
   {    
       String xml=null;
        try {
            StringWriter stringOut = new StringWriter(); 
                 TransformerFactory transFactory = TransformerFactory.newInstance(); 
                 Transformer trans = transFactory.newTransformer(); 
            
                 trans.transform(new DOMSource(docXml), new StreamResult(stringOut));
                  xml =  stringOut.toString();
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(Xml.class.getName()).log(Level.SEVERE, null, ex);
        }catch (TransformerException ex) {
            Logger.getLogger(Xml.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return xml;
   }
           
    
    public Element transforme(String exp)
    {
        Element rci_reply=doc.getRootElement();
        Element do_command=rci_reply.getChild("do_command");
        Element channel_get=do_command.getChild("channel_get");
        
        String[] split = exp.split("<");
        for(int i=0; i<split.length;i++)
        {
            
            if(split[i].startsWith("do_command"))
            {
                do_command.removeAttribute("target");
                String[] attrib = split[i].split("\"");
                do_command.setAttribute("target", attrib[1]);
            }
            if(split[i].startsWith("channel_get"))
            {
                channel_get.removeAttribute("name");
                channel_get.removeAttribute("value");
                channel_get.removeAttribute("units");
                channel_get.removeAttribute("timestamp");
                String[] attrib = split[i].split("\"");
                channel_get.setAttribute("name", attrib[1]);
                channel_get.setAttribute("value", attrib[3]);
                channel_get.setAttribute("units", attrib[5]);
                channel_get.setAttribute("timestamp", attrib[7]);
            }
        }
         
        do_command.setContent(channel_get);
        rci_reply.setContent(do_command);
         return rci_reply;
    }
}
