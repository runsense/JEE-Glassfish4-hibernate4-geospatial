/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.util.YahooClimat;

import run.ejb.base.Variable;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;




/**
 *
 * @author runsense
 */
@ManagedBean(name="weather")
@ViewScoped
public class WeatherController {
   
        private String city;
        private static final String unit = "c";             
   
     private String conditions;
        private WeatherService weatherService =  (WeatherService) new YAHOOWeatherService();

        private final Map<String,String> cities;
       
        
    
    public WeatherController() 
    {
       cities=Variable.getWoeid();
    }


        public String getCity() {
                return city;
        }
        public void setCity(String city) {
                this.city = city;
        }

        public String getConditions() {
                return conditions;
        }
        public void setConditions(String conditions) {
                this.conditions = conditions;
        }
               
        public String getUnit() {
                return unit;
        }
    

        
    public Map<String, String> getCities() {
        return cities;
    }

    public void retrieveConditions() 
    {
                conditions = weatherService.getConditions(city, unit);
                conditions = conditions.toLowerCase();
                
                System.err.print("WaetherController");System.out.println(conditions);
                
        String[] split = conditions.split("<br />");
        split[1] = "<b>prévisions climatique: </b>";
        
        split[4] = "<b>Prévisions :</b><BR />";
        conditions = "";
        for(int i=1; i<split.length; i++)
        {
            if(split[i].split("partly cloudy").length>1)
            {
                 String[] detail = split[i].split("partly cloudy"); 

                 split[i]= "\n nuage soleil "+detail[1];
            }else if(split[i].split("cloudy").length>1)
            {
                String[] detail = split[i].split("cloudy"); 

                 split[i]= "\n nuage "+detail[1];
            }
            else if(split[i].split("rainy").length>1)
            {
                String[] detail = split[i].split("rainy"); 

                 split[i]= "\n pluie "+detail[1];
            }else if(split[i].split("stormy").length>1)
            {
                String[] detail = split[i].split("stormy"); 

                 split[i]= "\n orageux "+detail[1];
            }else if(split[i].split("sunny").length>1)
            {
                String[] detail = split[i].split("sunny"); 

                 split[i]= "\n soleil "+detail[1];
            }
        
            conditions += split[i] + "<br />";
        }
        
    }

    public void saveSettings() {
        conditions = null;
    }
}
