/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.entite.variableRch;

import javax.persistence.MappedSuperclass;

/**
 *
 * @author Fujitsu
 */
@MappedSuperclass
public interface IntRqt 
{
    public Long getId() ;

    public void setId(Long id) ;
}
