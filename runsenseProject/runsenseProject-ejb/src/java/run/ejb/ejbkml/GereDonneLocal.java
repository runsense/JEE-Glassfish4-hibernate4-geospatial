/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.ejbkml;

import javax.persistence.EntityManager;






/**
 *
 * @author Administrateur
 */
@javax.ejb.Local
public interface GereDonneLocal {



   // public void triIndex(java.lang.String req, org.apache.lucene.store.Directory dirIndex) throws org.apache.lucene.queryParser.ParseException, org.apache.lucene.index.CorruptIndexException, java.io.IOException;

    public Object createObj(java.lang.Object pers, boolean create)throws java.lang.Exception;

    public boolean deleteObject(java.lang.Object pers);

    public java.util.Set<run.ejb.entite.geo.Coord> getLco();

    public void setCoord(java.lang.String lat, java.lang.String lng, java.lang.String alt);

    public run.ejb.entite.geo.Ligne getLgn();

    public void setLgn(run.ejb.entite.geo.Ligne lgn);


    public void setLgn(java.lang.String nom, java.lang.String ad, java.lang.String des, java.util.List<run.ejb.entite.geo.Coord> coords);

    public void setPt(java.lang.String nom, java.lang.String ad, java.lang.String des, Double lat, Double lng);

    public java.util.ArrayList chchTout(Object entite) throws Exception;

    public java.util.ArrayList rchFuzzy(String rc, java.lang.Object entite, String field, float ad) throws java.io.IOException;
    public java.util.ArrayList rchLuc(java.lang.String searchQuery, java.lang.Object entite, String field) ;
    
    public java.util.ArrayList rchWild(java.lang.String rc, java.lang.Object entite, String field) throws java.io.IOException;

  
    public java.util.ArrayList rch(Object rc, java.lang.Object entite, java.lang.String categ);

    public java.util.ArrayList rchBoolean(java.util.List<Object>rc, Object entite, java.util.List<String> fields, String precis, String genre);

    public java.util.ArrayList rchPhrase(String phr, Object entite, String field );

    public java.util.ArrayList rchSpat(run.ejb.entite.geo.Coord co, java.util.List<String> lfield, double dst);

    public java.util.ArrayList rchRange(java.util.List rc, Object entite, String fields, String avtapr);

    public EntityManager getEm();

    public ControlFactory getcFact();

    public void createEM();

    public void stLazy(Object lazy);

    public void initLuc(Object o);

    public void finish();

    public void attent(int stand);

    public boolean isSynk();

    public void setSynk(boolean synk);


 
  

    
}
