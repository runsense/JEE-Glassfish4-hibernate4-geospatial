/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.ejbkml;

import de.micromata.opengis.kml.v_2_2_0.gx.Tour;
import java.io.File;
import java.util.List;
import org.primefaces.model.TreeNode;
import run.ejb.entite.util.kml.ChargeKml;
import run.ejb.util.rsentite.schema.PrmtrSchm;
import run.ejb.entite.client.Compte;


/**
 *
 * @author runsense
 */

//@javax.ejb.Local
public interface GereCmpteLocal {

    public List chTout(Object o);
    public run.ejb.entite.client.Compte creerUser(run.ejb.entite.client.Compte hote);

    public void creerContact(run.ejb.entite.client.Contact contact);

    public run.ejb.entite.client.Contact updateContact(run.ejb.entite.client.Contact contact);

    public boolean deleteContact(run.ejb.entite.client.Contact contact);

    public run.ejb.entite.client.Compte rchCmpte(Object cpte, String field);

    public run.ejb.entite.client.Contact rchContact(run.ejb.entite.client.Contact ctc, String field);

    public void dltUservue();

    public void chargeKml(File fichier);

    public run.ejb.entite.client.Compte identification(String nom, String pswd);

    public boolean chargePartie(TreeNode select);

    public TreeNode charge(Object chg, boolean bserver);

    public List<String> complete(String query);

    public boolean supBdd(Object entite);

    public Compte getCpte();

    public void setCpte(Compte cpte);

   // public ArrayList<RsEntite> rchbdd(String rch);

    public void setCharge(ChargeKml charge);
    
    public ChargeKml getCharge();

    public Object[] onNode(TreeNode data);

    public void setLtour(List<Tour> ltour);

    public List<Tour> getLtour();

    public List<run.ejb.entite.geo.Tour> getLbddtour();

    public void verifBDD();

    public void setTbverif(List<String> tbverif);

    public Object updateRs(Object nouv);

    public PrmtrSchm getSchema();

    public void setSchema(PrmtrSchm schema);

    public boolean addObject(Object evnmt);

    public boolean supNode(TreeNode node);


    
}
