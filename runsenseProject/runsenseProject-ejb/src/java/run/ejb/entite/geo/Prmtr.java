/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.entite.geo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Latitude;
import org.hibernate.search.annotations.Longitude;
import org.hibernate.search.annotations.NumericField;
import org.hibernate.search.annotations.Store;
import run.ejb.entite.geo.interf.IntBaz;

/**
 *
 * @author runsense
 */
@Entity
@Table(name = "PRMTR", catalog = "")
@XmlRootElement
@Indexed(index="PRMTR")
@Analyzer(impl=StandardAnalyzer.class)
public class Prmtr implements Serializable, IntBaz 
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq_prmtr")
    @SequenceGenerator(sequenceName="seq", name="seq_prmtr", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "IDPRMTR", nullable = false)
    @NumericField
    @DocumentId
    private Long idPrmtr;

    @Size(max = 150)
    @Column(name = "ALTMODE",length = 150 )
    private String altMode;
    
    @Size(max = 150)
    @Column(name = "FLYMODE",length = 150 )
    private String flyMode;

    @Column(name = "DURA" )
    private Double dura;
    
    @Column(name = "HEAD")
    private Double head;

    @Column(name = "TILT")
    private Double tilt;
    
    @Column(name = "RANGE")
    private Double range;
    
    @NumericField
    @Field(index= Index.YES,store= Store.NO)
    @Column(name = "GEOANIMAT")
    private Long geoanimat;
    

    
    @Column(name = "LATITUDE", precision = 52)
    @Field(index= Index.YES,store= Store.YES) 
    @Latitude
    private Double latitude;
    
    @Column(name = "LONGITUDE", precision = 52)
    @Field(index= Index.YES,store= Store.YES)
    @Longitude
    private Double longitude;
    
    @ManyToOne()
    @JoinColumn(name="parametreTour")
     @ContainedIn
    private Tour tourPrmtr;

    public Prmtr() {
    }

    public Prmtr( String altMode, String flyMode, Double dura, Double head, Double tilt, Double range, Tour tourPrmtr, Long geoanimat)
    {
        
        this.altMode = altMode;
        this.flyMode = flyMode;
        this.dura = dura;
        this.head = head;
        this.tilt = tilt;
        this.range = range;
        this.geoanimat=geoanimat;
        this.tourPrmtr = tourPrmtr;
    }

    
    public Long getIdPrmtr() {
        return idPrmtr;
    }

    public void setIdPrmtr(Long idPrmtr) {
        this.idPrmtr = idPrmtr;
    }

    public Double getHead() {
        return head;
    }

    public void setHead(Double head) {
        this.head = head;
    }

    public Double getTilt() {
        return tilt;
    }

    public void setTilt(Double tilt) {
        this.tilt = tilt;
    }

    public Double getRange() {
        return range;
    }

    public void setRange(Double range) {
        this.range = range;
    }

    public Tour getTourPrmtr() {
        return tourPrmtr;
    }

    public void setTourPrmtr(Tour tourPrmtr) {
        this.tourPrmtr = tourPrmtr;
    }

    public String getAltMode() {
        return altMode;
    }

    public void setAltMode(String altMode) {
        this.altMode = altMode;
    }

    public String getFlyMode() {
        return flyMode;
    }

    public void setFlyMode(String flyMode) {
        this.flyMode = flyMode;
    }

    public Double getDura() {
        return dura;
    }

    public void setDura(Double dura) {
        this.dura = dura;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Long getGeoanimat() {
        return geoanimat;
    }

    public void setGeoanimat(Long geoanimat) {
        this.geoanimat = geoanimat;
    }
  
     @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPrmtr!= null ? idPrmtr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Prmtr)) {
            return false;
        }
        Prmtr other = (Prmtr) object;
        if ((this.idPrmtr == null && other.idPrmtr != null) || (this.idPrmtr != null && !this.idPrmtr.equals(other.idPrmtr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "runsense.plateformeweb.entite.Hote[ id=" + idPrmtr + " ]";
    }

    @Override
    public void setNom(String nom) {
        
    }

    @Override
    public String getNom() {
     if(tourPrmtr!=null)
       return "paramètre du tour "+tourPrmtr.getNom()+": "+idPrmtr;
     else
         return "";
    }

    @Override
    public Long getId() {
       if(idPrmtr!=null) return idPrmtr;
       else return null;
    }
    
    
}
