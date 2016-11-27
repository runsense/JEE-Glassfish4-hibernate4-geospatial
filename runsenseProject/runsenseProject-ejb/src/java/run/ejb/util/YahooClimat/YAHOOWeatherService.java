/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.util.YahooClimat;
import java.net.URL;
import java.util.logging.Logger;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import run.ejb.util.ex.Xcption;

/**
 *
 * @author runsense
 */
public class YAHOOWeatherService implements WeatherService{
    
        private static final Logger logger = Logger.getLogger(YAHOOWeatherService.class.getName());
       
       
        public String getConditions(String city, String unit) {

            if(city!=null)
                try {
                   
                    Xcption.Xcption("YAHOOWeatherService getConditions city", city);
                    
                        URL feedSource = new URL("http://weather.yahooapis.com/forecastrss?w=" + city + "&u=" + unit);
                    
                        
                    SyndFeedInput input = new SyndFeedInput();
                    
                        SyndFeed feed = input.build(new XmlReader(feedSource));
                        String value = ((SyndEntry) feed.getEntries().get(0)).getDescription().getValue();
                       
                        return value.split("<a href")[0];               //Remove links
                } catch (Exception e) {
                       
                         Xcption.Xcption("YAHOOWeatherService Exception", e.getMessage());
                }
               
                return null;
        }

}
