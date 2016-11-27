/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.climat;

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
        private String unit = "c";             
   
     private String conditions;
        private WeatherService weatherService =  (WeatherService) new YAHOOWeatherService();

        private static Map<String,String> cities;
        static {
       
        cities = new LinkedHashMap<String, String>();
        cities.put("Nord", "23424931");
        cities.put("Saint-Denis", "23424931");
        cities.put("La Possession", "1410099");
        cities.put("Sainte-Marie", "1410244");
        cities.put("Sainte-Suzanne", "1410246");
        cities.put("Le Port", "24551469");
        
        cities.put("Est", "24549829");
        cities.put("Salazie", "24551452");
        cities.put("La Plaine des palmistes", "24551455");
        cities.put("Saint Benoit", "1410230");
        cities.put("Bras panon", "1410003");
        cities.put("Saint-Andre", "1410229");
        cities.put("Sainte rose", "1410245");
        cities.put("Sainte-Anne", null);
        
        cities.put("Sud(nouveau volcan)", "24549828");
        cities.put("Sud(anciens volcan)", "1410235");
        cities.put("tampon", "1410141");
        cities.put("la plaine des cafres", "1410097");
        cities.put("L'entre-deux", "1511203");
        cities.put("Saint-Joseph", "1410235");
        cities.put("Petite ile", "1410194");
        cities.put("Saint-Phillipe", "1410239");
        cities.put("enclosTremblet", "12746223");
        
        cities.put("Ouest", "24549831");
        cities.put("Trois Bassins", "1410255");
        cities.put("Saint-Leu", "1410236");
        cities.put("Saint-Paul", "1410238");
        cities.put("Saint-Gille", "1410233");
        cities.put("Les Avirons", "1410145");
        cities.put("L'Etang-Sale", "1410029");
        cities.put("Saint-louis", "1410237");
        cities.put("Mafate", "24551468");
    }

    public WeatherController() 
    {
       
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
        public void setUnit(String unit) {
                this.unit = unit;
        }

    public void setCities(Map<String, String> cities) {
        this.cities = cities;
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
