/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.base;

/**
 *
 * @author selekta
 */
public class Statistique 
{
    private static int incrementation=5200;

    public static int getIncrementation() {
        return incrementation++;
    }

    public static void setIncrementation(int incrementation) {
        Statistique.incrementation = incrementation;
    }
   
    
}
