/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.base;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author paskal
 */
public class Classmt implements Serializable
{
    private String ref;
    private ArrayList list;
    public static Classmt buid(String ref, ArrayList list) {
        return new Classmt(ref, list);
    }
    public Classmt(String ref, ArrayList list) {
        this.ref = ref;
        this.list = list;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public ArrayList getList() {
        return list;
    }

    public void setList(ArrayList list) {
        this.list = list;
    }
    
    
}
