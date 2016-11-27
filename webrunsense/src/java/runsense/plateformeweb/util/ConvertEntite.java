/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import run.ejb.entite.geo.Ligne;
import run.ejb.entite.geo.interf.RsEntite;

/**
 *
 * @author runsense
 */
@FacesConverter(forClass=Ligne.class, value="ConvertEntite")
public class ConvertEntite implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Ligne entite= new Ligne();
        entite.setId(Long.valueOf(value));
        return entite;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return String.valueOf(((RsEntite)value).getId());
    }
    
}
