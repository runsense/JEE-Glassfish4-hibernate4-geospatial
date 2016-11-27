/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package run.ejb.util.ex;

/**
 *
 * @author paskal
 */
public class Xcption 
{
    public static void Xcption(final Object rouge,final Object noir)
    {
        try{System.err.print(rouge);System.out.println(noir);}
        catch(Exception ex){System.err.print("Xcption");System.out.println(ex.getMessage());}
    }
    public  Boolean Xcption(final Exception ex)
    {
         Xcption.Xcption(null,ex.getMessage());
         return Boolean.TRUE;
    }
}
