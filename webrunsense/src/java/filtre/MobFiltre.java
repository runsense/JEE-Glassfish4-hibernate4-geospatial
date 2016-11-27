/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filtre;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import run.ejb.base.Statistique;
import run.ejb.util.ex.Xcption;


/**
 *
 * @author runsense
 */
public class MobFiltre implements Filter
{ 
    private boolean mobile=false;
    private boolean ie=false;
    private static final java.util.List<String> ltab;
    static {
        ltab=Lists.newArrayList("vnd.wap.xhtml+xml","sony","symbian","nokia","samsung","safari",
    "mobile","windows ce","epoc","opera mini", 
    "nitro","j2me","midp-","cldc-","netfront",
    "mot","up.browser","up.link","audiovox",
    "blackberry","ericsson","panasonic","philips",
    "sanyo","sharp","sie-","portalmmm","blazer",
    "avantgo","danger","palm","series60","palmsource",
    "pocketpc","smartphone","rover","ipaq","au-mic",
    "alcatel","ericy","vodafone","wap1","wap2","teleca",
    "playstation","lge","lg-","iphone","android","htc", 
    "dream","webos","bolt","nintendo");
        
    }
    

    @Override
    public void init(FilterConfig filterConfig) throws ServletException 
    {
        Statistique.setIncrementation(
                    Statistique.getIncrementation()+1);
    }

    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
    {

        HttpServletResponse res=(HttpServletResponse) response;
        HttpServletRequest req=(HttpServletRequest) request;
        HttpSession session = req.getSession();
        
        String header = req.getHeader("user-agent");
        
        
        
         if(header!=null&&!mobile)
        {
            if(ltab.contains(header.toLowerCase()))
                {
                    Xcption.Xcption("MobFiltre doFilter header.toLowerCase()", header.toLowerCase());
                    mobile=true;
                }else
                if(header.toLowerCase().contains("MSIE"))
                {
                    /*String version=header.toLowerCase().split("MSIE")[1].split(";")[0];
                    String wind=header.toLowerCase().split("Windows NT")[1].split(";")[0];
                    Xcption.Xcption("MobFiltre doFilter header.toLowerCase()",header.toLowerCase()+" compare :  "+"IE");
                    */
                    
                        ie=true;
                    
                }
            
            if(mobile)
            {
                
                res.sendRedirect("m_entrer.re");
                return;
            }
            else{
               
                chain.doFilter(request, response);
            }

        }else
        {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() 
    {
        try {
            super.finalize();
            Xcption.Xcption("Runtime.getRuntime().totalMemory()",Runtime.getRuntime().totalMemory());
        } catch (Throwable ex) {
           Xcption.Xcption("Filtre destroy",ex.getMessage());
        }
    }
    
}
