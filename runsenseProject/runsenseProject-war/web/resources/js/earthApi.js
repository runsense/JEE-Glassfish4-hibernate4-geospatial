var ge;
var kmlObject;
var currentKmlObject;
    google.load("earth", "1");
    

    function init() {
      google.earth.createInstance('map3d', initCB, failureCallback);
       alert(window.location.href);    
          
    }
    function enterTour() {
    if (!tour) {
      alert('No tour found!');
      return;
    }
  
    ge.getTourPlayer().setTour(tour);
  }
    function playTour() {
    ge.getTourPlayer().play();
  }
  
  function pauseTour() {
    ge.getTourPlayer().pause();
  }
  
  function resetTour() {
    ge.getTourPlayer().reset();
  }
  
  function exitTour() {
    // just like setBalloon(null)
    ge.getTourPlayer().setTour(null);
  }
    function fecthTour()
    {
    // remove the old KML object if it exists
    if (currentKmlObject) {
        ge.getFeatures().removeChild(currentKmlObject);
        currentKmlObject = null;
    }

   
    var kmlUrl = window.location.href+'/kml/grandcanyon_tour.kmz';

     
    google.earth.fetchKml(ge, href, finishFetchKml);
    
    document.getElementById('installed-plugin-version').innerHTML =
      ge.getPluginVersion().toString();
    }

    function finishFetchKml(kmlObject) {
    // check if the KML was fetched properly
    if (kmlObject) {
        // add the fetched KML to Earth
        currentKmlObject = kmlObject;
        ge.getFeatures().appendChild(currentKmlObject);
    } else {
        // wrap alerts in API callbacks and event handlers
        // in a setTimeout to prevent deadlock in some browsers
        setTimeout(function() {
        alert('Bad or null KML.');
        }, 0);
    }
    }
    function createLineString() {
         
         alert('lat'+lat +'long'+lng);
         //alert('lat');
         function addToLineString(lineString, lat, lng, latOffset, lngOffset) {
                var altitude = 1.0; // give it some altitude
                lineString.getCoordinates().
                pushLatLngAlt(lat , lng , altitude);
            }

            var lookAt = ge.getView().copyAsLookAt(ge.ALTITUDE_RELATIVE_TO_GROUND);
            var lat = document.getElementById('lat');
            var lng = document.getElementById('lng');
            
            // create the line string placemark
            var lineStringPlacemark = ge.createPlacemark('');

            // create the line string geometry
            var lineString = ge.createLineString('');
            lineStringPlacemark.setGeometry(lineString);

            // tessellate (i.e. conform to ground elevation)
            lineString.setTessellate(true);

            // add the the points to the line string geometry
            addToLineString(lineString, lat, lng, 0);
            addToLineString(lineString, lat, lng, 0);
            addToLineString(lineString, lat, lng,   0, 1.0, 0);
            addToLineString(lineString, lat, lng, 1.5, 1.5, 0);
            addToLineString(lineString, lat, lng,   0, 2.0, 0);
            addToLineString(lineString, lat, lng, 1.5, 2.5, 0);
            addToLineString(lineString, lat, lng,   0, 3.0, 0);
            addToLineString(lineString, lat, lng, 1.5, 3.5, 0);
            addToLineString(lineString, lat, lng,   0, 4.0, 0);
            addToLineString(lineString, lat, lng, 1.5, 4.5, 0);

            ge.getFeatures().appendChild(lineStringPlacemark);
      }
      
      
    function initCallback(instance) {
      ge = instance;
    ge.getWindow().setVisibility(true);

    // add a navigation control
    ge.getNavigationControl().setVisibility(ge.VISIBILITY_AUTO);

    // add some layers
    ge.getLayerRoot().enableLayerById(ge.LAYER_BORDERS, true);
    ge.getLayerRoot().enableLayerById(ge.LAYER_ROADS, true);

   var la = ge.getView().copyAsLookAt(ge.ALTITUDE_RELATIVE_TO_GROUND);
    la.setRange(100000);
    la.setLatitude(-21.23340645208472);
    la.setLongitude(55.41718503494243);
    
    alert('kmlUrl');
    ge.getView().setAbstractView(la);

    document.getElementById('installed-plugin-version').innerHTML =
    ge.getPluginVersion().toString();

    }

    function failureCallback(errorCode) 
    {
        document.getElementById(id);
    }
    

    
    
    //////////////////////////////////////////////////
                           //////////////////////////////////////////////////
    //////////////////////////////////////////////////
    function initParseKml(xhr, status, args)
    {
        alert('xhr, status, args'+xhr.toString()+status+args.toString());
        
       
       google.earth.createInstance('map3d', createLineString, failureCallback);
       recharge();
    }
    
    function parseKmlFromTextarea(instance) {
        
       ge=instance;
       ge.getWindow().setVisibility(true);
       
       var kmlBox = document.getElementById('kml-box');
    
            var kmlString = kmlBox.value;
            
            //if(kmlString =='')
                kmlString=''
                         + '<?xml version="1.0" encoding="UTF-8"?>'
                         + '<kml xmlns="http://www.opengis.net/kml/2.2">'

                         + '<Document>'
                         + '<name>cheminBrasChevrettes.kml</name>'
                         + '  <Camera>'
                         + '    <longitude>55.41718503494243</longitude>'
                         + '    <latitude>-21.23340645208472</latitude>'
                         + '    <altitude>139.629438</altitude>'
                         + '    <heading>-70.0</heading>'
                         + '    <tilt>75</tilt>'
                         + '  </Camera>'

                         + '  <Placemark>'
                         + '    <name>cheminBrasChevrettes</name>'
                         + '    <LineString>'
                         + '      <coordinates>55.61573936167142,-21.28778906628847,55.61596230115428,-21.28729611152178,55.61632804717587,-21.28677415501333,55.61675557799318,-21.28651900342715,55.6170851941354,-21.28581940486648,55.61710730152094,-21.28498089390807,55.61741982885185,-21.28448137269252,55.61815255096398,-21.28435756377476,55.61864328262764,-21.28425635875704,55.61874678055251,-21.28371262933705,55.61919524429056,-21.2833952535372,55.61957330783691,-21.28293551871918,55.61948996673861,-21.28265510893921,55.61900315839661,-21.28257024392152,55.61874325637265,-21.28250263868842</coordinates>'
                         + '    </LineString>'
                         + '  </Placemark>'
                         
                         + '  <Placemark>'
                         + '    <name>cheminBrasChevrettes</name>'
                         + '    <Point>'
                         + '      <coordinates>55.41731759613369,-21.23277332166533</coordinates>'
                         + '    </Point>'
                         + '  </Placemark>'

                         + '</Document>'
                         + '</kml>';
            if (currentKmlObject)
            {
                
                ge.getFeatures().removeChild(currentKmlObject);
            }
        try{
            
                
                currentKmlObject = ge.parseKml(kmlString);
                alert('currentKmlObject :'+currentKmlObject);
                ge.getFeatures().appendChild(currentKmlObject);
                ge.getView().setAbstractView(currentKmlObject.getAbstractView());
            } catch (ex) {
                    alert('ge error'+'\n'+ex);
            }    
                        
                  



    ////////////////////////////////////////////////////////////////////////////////
      /* remove the old parsed KML object if one exists
      
      if (currentKmlObject)
       {
           ge.getFeatures().removeChild(currentKmlObject);
       }   

            var kmlBox = document.getElementById('kml-box');
            alert(kmlBox);
            
            var kmlString = kmlBox.value;
            alert(kmlString);
      // parse the text in the box and add it to Earth
      try {
        currentKmlObject = ge.parseKml(kmlString);
        alert(ge);
        ge.getFeatures().appendChild(currentKmlObject);
      } catch (ex) {
        alert('Parse error'+'\n'+ex);
      }
    }
        
        
        
        
        // Create the placemark.
         var lineStringPlacemark = ge.createPlacemark('');

         // Create the LineString; set it to extend down to the ground
         // and set the altitude mode.
         var lineString = ge.createLineString('');
         lineStringPlacemark.setGeometry(lineString);
         lineString.setExtrude(true);
         lineString.setAltitudeMode(ge.ALTITUDE_RELATIVE_TO_GROUND);

         // Add LineString points.
         lineString.getCoordinates().pushLatLngAlt(-21.097313,55.475235, 700);
         lineString.getCoordinates().pushLatLngAlt(-21.097313,55.475228, 700);
         lineString.getCoordinates().pushLatLngAlt(-21.097313,55.475218, 700);
         lineString.getCoordinates().pushLatLngAlt(-21.097313,55.475294, 700);
         lineString.getCoordinates().pushLatLngAlt(-21.097313,55.475278, 700);
         lineString.getCoordinates().pushLatLngAlt(-21.097313,55.475266, 700);
         lineString.getCoordinates().pushLatLngAlt(-21.097313,55.475268, 700);
         lineString.getCoordinates().pushLatLngAlt(-21.097313,55.475273, 700);

         // Create a style and set width and color of line.
         lineStringPlacemark.setStyleSelector(ge.createStyle(''));
         var lineStyle = lineStringPlacemark.getStyleSelector().getLineStyle();
         lineStyle.setWidth(5);
         lineStyle.getColor().set('9900ffff');  // aabbggrr format

         // Add the feature to Earth.
         ge.getFeatures().appendChild(lineStringPlacemark);
         
         
         
  document.getElementById('installed-plugin-version').innerHTML =
    ge.getPluginVersion().toString();
    
    
   
    }
     */
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