/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.util.traduction;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 *
 * @author selekta
 */

public class RBint {
    
    private final ResourceBundle rb;

    private Map<String, String> mrsorc;

    public RBint(ResourceBundle rb) {
        
        this.rb = rb;
        
        mrsorc=new HashMap<String, String>();
        
    }
     public String getReverse(String rch)
     {
         String rtr=rch;
         try{
             rtr=mrsorc.get(rch);
         }catch(Exception ex)
         {
             rtr=rch;
         }
         if(rtr!=null)
             return invesp(rtr);
         else
             return rch;
     }
     public String getString(String rch)
     {
         String rtr=rch;
         try{
          
         rtr=rb.getString(
                 espace(rch));
            mrsorc.put(rtr, rch);
         }catch(Exception ex)
         {
             
         }
         return rtr;
     }
     private String invesp(String i)
     {
         return i.replace('/', ' ').replace('$', '/');
     }
     private String espace(String e)
     {
         return e.replace(' ', '/').replace('/', '$');
     }

    public ResourceBundle getRb() {
        return rb;
    }
     
}
