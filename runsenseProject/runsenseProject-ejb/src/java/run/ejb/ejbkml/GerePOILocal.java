/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.ejbkml;

import java.util.Map;
import javax.ejb.Local;

/**
 *
 * @author selekta
 */
@Local
public interface GerePOILocal {

    public void createTab(String ad, Map<String, Object[]> donne);

    
}
