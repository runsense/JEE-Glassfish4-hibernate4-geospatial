/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.ejbkml;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import run.ejb.entite.geo.Coord;
import run.ejb.entite.geo.interf.RsEntite;
import run.ejb.entite.util.kml.YahooRep;
import run.ejb.util.rsentite.schema.PrmtrSchm;



/**
 *
 * @author Administrateur
 */
//@Local
public interface GereKmlLocal {

     public void cEm();
     public void fEm();
     
    public java.util.ArrayList rchBdd(Object recherche, String entcl, String ctgrch, String field);
    public void boolcreateObj(boolean b);
    public Object addBdd(Object entite);
  
    public java.lang.String getTracer();

    public void setTracer(java.lang.String tracer);

    public java.util.List chTout(Object Entite) throws java.lang.Exception;

    public boolean isbCoord();

    public void setbCoord(boolean bCoord);

    public boolean supBdd(Object entite);

    public java.util.ArrayList<RsEntite> rchMulti(Object rc, Object entcl, List<String> fields, String operation, String genre) throws org.hibernate.search.SearchException;

    public ArrayList createTracer(String dmd, String field);

    public String createTracerbyRs(RsEntite rs);
    
    public String[] getLatlng();

    public void setLatlng(String[] latlng);

   // public String rchItineraire(String itid, String itia, String referer);

    public String createIti(ArrayList<Coord> lCo);

    public GereDonneLocal getGereDonne();

    public boolean isBcrit();

    public void setBcrit(boolean bcrit);

    public void setSlCrit(List slCrit);

    public void setBclim(boolean bclim);

    public boolean isBclim();

    public void setRoot(DefaultTreeNode root);

    public DefaultTreeNode getRoot();

    public void dcpchg(TreeNode  noeud);

    public YahooRep getMeteoKml();

    public void setMeteoKml(YahooRep meteoKml);

    public List<Long> createKml(Object res, String field, PrmtrSchm schema);

    public String geTour();

    public String formTourbyRs(RsEntite rch);

    public List<RsEntite> rchIds(List<Long> lids);
    
    public RsEntite findrs(RsEntite rs);
    
    public  Object find(Object o, long id);


}
