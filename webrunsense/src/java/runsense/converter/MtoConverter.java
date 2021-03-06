/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package runsense.converter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import runsense.plateformeweb.bean.RchBean;
import runsense.plateformeweb.util.ImgLien;
/**
 *
 * @author paskal
 */
@FacesConverter(value = "runsense.converter.MtoConverter")
public class MtoConverter implements Converter {

    private RchBean rchbean;
  
    @Override
    public ImgLien getAsObject(FacesContext context, UIComponent component, String value) 
    {
        
        
        List<ImgLien> ltmp=null;
        if(rchbean==null)
        {
           
            rchbean=(RchBean) context.getCurrentInstance().getApplication().getELResolver()
                    .getValue(context.getELContext(), null, "rchBean");
            
        }
    
        if(component.getId().equals("rzone"))
             ltmp = rchbean.getLzone();
        else
             ltmp = rchbean.getLville();   
        
        if(value.trim().isEmpty())
            return null;
        else
        {
           /* ImgLien retour=null;
            
            for(ImgLien il:ltmp)
                if(il.getNom().equals(value))
                    retour=il;

           return retour; */
           return Optional.ofNullable(ltmp.stream().
                    filter(il->il.getNom().equals(value)).collect(Collectors.toList()))
                    .get().get(0);
        }
       
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) 
    {
         
        
        String retour=null;
        if(value==null || value.equals(""))
        {
            retour="";
        }else if(value instanceof ImgLien)
        {
            ImgLien il=(ImgLien) value; 
            retour = il.getNom();
        }else if(value instanceof String)
        {
            retour=(String) value;
        }else
            retour= value.toString();
        
       
        return retour; //((ImgLien)value).getNom();
    } 
    
}
