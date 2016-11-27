/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.util.jak;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import java.util.Optional;
import run.ejb.entite.geo.Coord;

/**
 *
 * @author paskal
 */
public class Coordinates 
{
    public static Coordinate CoordtoCoordinate(Coord co)
    {
       return Optional.ofNullable(co.getAlt()).map(a->new Coordinate(co.getLongitude(),co.getLatitude(),co.getAlt())).
                orElse(new Coordinate(co.getLongitude(),co.getLatitude()));
    }
    
}
