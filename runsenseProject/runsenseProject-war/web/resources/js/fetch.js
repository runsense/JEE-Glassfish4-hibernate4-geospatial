var ge;

var currentKmlObject = null;
  google.load("earth", "1");

  function init() {
      

    google.earth.createInstance('map3d', initCB, failureCB);
    if (navigator.geolocation)
        {
            var la ;
            var watchId = navigator.geolocation.watchPosition(local,
                                  null,
                                  {enableHighAccuracy:true});
                                 
            
        }
      else
        alert("Votre navigateur ne prend pas en compte la géolocalisation HTML5"); 
    
    
  }
  
  function initG()
  {
      google.earth.createInstance('map3d', initCB, failureCB);
  }
  function local(position){
  
  //mark postion
      var placemark = ge.createPlacemark('');
                placemark.setName("position actuelle" );
                ge.getFeatures().appendChild(placemark);

                // Create style map for placemark
            var icon = ge.createIcon('');
                icon.setHref('http://maps.google.com/mapfiles/kml/pal2/icon2.png');
            var style = ge.createStyle('');
                style.getIconStyle().setIcon(icon);
                placemark.setStyleSelector(style);

                // Create point
            
            var point = ge.createPoint('');
                point.setLatitude(position.coords.latitude);
                point.setLongitude(position.coords.longitude);
                placemark.setGeometry(point);
                
                
      ge.getFeatures().appendChild(placemark);
      
  // fly to 
    var la = ge.createLookAt('');
    la.set(position.coords.latitude, position.coords.longitude,
      50000, // altitude
      ge.ALTITUDE_RELATIVE_TO_GROUND,
      0, // heading
      0, // cabrage tilt
      5000 // range (inverse of zoom)
      );
          
    ge.getView().setAbstractView(la);
  
   
  
}
  function initCB(instance) {
    ge = instance;
    ge.getWindow().setVisibility(true);
    
    // add a navigation control
    ge.getNavigationControl().setVisibility(ge.VISIBILITY_AUTO);
    
    // add some layers
    ge.getLayerRoot().enableLayerById(ge.LAYER_BORDERS, true);
    ge.getLayerRoot().enableLayerById(ge.LAYER_ROADS, true);

    // fly to Santa Cruz
    var la = ge.createLookAt('');
    la.set(-21.2, 55.6,
      50000, // altitude
      ge.ALTITUDE_RELATIVE_TO_GROUND,
      0, // heading
      0, // straight-down tilt
      5000 // range (inverse of zoom)
      );
          
    ge.getView().setAbstractView(la);
    
    document.getElementById('installed-plugin-version').innerHTML =
      ge.getPluginVersion().toString();
  }

  
  function fetchKmlFromInput() {
    
    if (currentKmlObject) {
      ge.getFeatures().removeChild(currentKmlObject);
      currentKmlObject = null;
    }

    var nomUrl=document.getElementById('f_left:kml-url').value;
    var kmlUrl;
    kmlUrl=nomUrl.value;
    currentKmlObject= ge.parseKml(kmlUrl);
    
    ge.getFeatures().appendChild(currentKmlObject);
    enterTour(currentKmlObject);
    
    var kmlUrlBox = document.getElementById('f_left:kml-tracer');
    var kmlTracer = kmlUrlBox.value;
    currentKmlObject= ge.parseKml(kmlTracer);
    ge.getFeatures().appendChild(currentKmlObject);
  }
  function fetchKmlFrom()
  {
    // remove the old KML object if it exists
    if (currentKmlObject) {
      ge.getFeatures().removeChild(currentKmlObject);
      currentKmlObject = null;
    }
    var nomUrl=document.getElementById('f_left:kml-url').value;
   // alert('test'+nomUrl);
    var kmlUrl;var kmlTracer;
    
     try{
            kmlTracer = document.getElementById('f_left:kml-tracer').value;
            }catch(ex)
            {
                alert("where"+ex.toString());
            }
          
          currentKmlObject= ge.parseKml(kmlTracer);

            ge.getFeatures().appendChild(currentKmlObject);
            enterTour(currentKmlObject);
    if(nomUrl=='http://runsense.re/kml/reunion.kml')
        {
            kmlUrl=nomUrl;
            //alert('sortie'+kmlUrl);
        }
    else
        {
            kmlUrl = 'http://runsense.re/kml/base.kml';
           
           // alert('elsesortie'+kmlUrl);
        }
        
    
    google.earth.fetchKml(ge, kmlUrl, finishFetchKml);
    
  }
  
  function finishFetchKml(kmlObject) {
    // check if the KML was fetched properly
    if (kmlObject) {
      // add the fetched KML to Earth
      currentKmlObject = kmlObject;
      ge.getFeatures().appendChild(currentKmlObject);
      enterTour(kmlObject);
      
    } else {
      // wrap alerts in API callbacks and event handlers
      // in a setTimeout to prevent deadlock in some browsers
      setTimeout(function() {
        alert('probleme de réseau, recommencer !');
      }, 0);
    }
  }
  
  
  function fetchKml() 
  {
    
   //fetchKmlFromInput();
   fetchKmlFrom();
     
  }
  
  function enterTour(kmlObject) 
  {
       try{
          if (!kmlObject) 
          {
                alert('No tour found!');
                return;
            }
            
                ge.getTourPlayer().setTour(kmlObject);
                ge.getTourPlayer().play();
      }catch (ex)
      {
          //alert('Exception'+ex.toString());
      }
  
}

function playTour() 
{
    
    if (!currentKmlObject) {
    alert('No tour found!');
    return;
  }

  ge.getTourPlayer().setTour(currentKmlObject);
  
    ge.getTourPlayer().play();
}

function pauseTour()
{
  ge.getTourPlayer().pause();
}

function resetTour()
{
  ge.getTourPlayer().reset();
}

function exitTour() 
{
  ge.getTourPlayer().setTour(null);
}


  function recharge()
 {
         // IE isn't affected by this bug
        if (navigator.userAgent.toLowerCase().indexOf('msie') < 0) 
        {
            // Firefox, Safari, etc. are affected
            var earthNode = document.getElementById('map3D');
            var earthWatchNode = earthNode.parentNode;

            earthNode.style.position = 'absolute';
            earthNode.style.left = earthNode.style.top = '0';
            earthNode.style.width = earthNode.style.height = '0';
            document.body.appendChild(earthNode);

            function repositionEarth() {
            earthNode.style.height = earthWatchNode.offsetHeight + 'px';
            earthNode.style.width = earthWatchNode.offsetWidth + 'px';

            // calculate position on page
            var left = earthWatchNode.offsetLeft;
            var top = earthWatchNode.offsetTop;
            var p = earthWatchNode.offsetParent;

            while (p && p != document.body) 
            {
                if (isFinite(p.offsetLeft) && isFinite(p.offsetTop)) 
                {
                
                left += p.offsetLeft;
                top += p.offsetTop;
                
                }

                p = p.offsetParent;
            }

            earthNode.style.left = left + 'px';
            earthNode.style.top = top + 'px';
            }

            repositionEarth();

            // reposition every 100ms
            window.setInterval(function() {
            repositionEarth();
            }, 100);
        }
    }
      
  function failureCB(errorCode)
  {
      
      RedirectSmartphone('http://m.google.fr/earth');
  }
  
