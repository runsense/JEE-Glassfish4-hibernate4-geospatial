/*
 * To change this template, choose Tools | Templates
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
import run.ejb.entite.geo.interf.RsEntite;
import runsense.plateformeweb.bean.RchBean;

/**
 *
 * @author selekta
 */
@FacesConverter(value = "runsense.converter.OtoConverter")
public class OtoConverter implements Converter {

    private RchBean rchbean;
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) 
    {
        List<RsEntite> vues = null;
        if(rchbean==null)
        {
            rchbean=(RchBean) context.getCurrentInstance().getApplication().getELResolver()
                    .getValue(context.getELContext(), null, "rchBean");
             vues = rchbean.getVue();
        }
        if(value.trim().isEmpty())
            return null;
        else
        {
           /* RsEntite retour=null;
            
            for(RsEntite rs:vues)
                if(rs.getNom().equals(value))
                    retour=rs;

           return retour; */
           return Optional.ofNullable(vues.stream().
                    filter(il->il.getNom().equals(value)).
                    distinct().collect(Collectors.toList()))
                    .get().get(0);
        }
        
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) 
    {
         
        RsEntite rs;
        String retour=null;
        if(value==null || value.equals(""))
        {
            retour="";
        }else if(value instanceof RsEntite)
        {
            rs=(RsEntite) value; 
            retour = rs.getNom();
        }else if(value instanceof String)
        {
            retour=(String) value;
        }else
            retour= value.toString();
        
       
        return retour;
    } 
    
}
