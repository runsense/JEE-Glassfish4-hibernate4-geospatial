/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.entite.geo.interf;

import javax.persistence.MappedSuperclass;

/**
 *
 * @author Administrateur
 */
@MappedSuperclass
public interface RsEntite 
{
    
    public void setId(Long id);
    public Long getId();
    public void setNom(String nom);
    public String getNom();
    
    public void setDescription(String desc);
    public String getDescription();
    
    public void setAdresse(String ad);
    public String getAdresse();
    
    public void setEntite(RsEntite entite) throws Exception;
    
    public String getProp();
}
