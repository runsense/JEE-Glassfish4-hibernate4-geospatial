var ge;
var kmlObject;
var currentKmlObject;
    google.load("earth", "1");
    

    function init() {
        
      google.earth.createInstance('map3d', initCallback, failureCallback);
      
      parseKmlFromTextarea();
    }
    
    function initCallback(instance) {
      ge = instance;
      
      ge.getWindow().setVisibility(true);
    
      // add a navigation control
      ge.getNavigationControl().setVisibility(ge.VISIBILITY_AUTO);
    
      // add some layers
      ge.getLayerRoot().enableLayerById(ge.LAYER_BORDERS, true);
      ge.getLayerRoot().enableLayerById(ge.LAYER_ROADS, true);
    
      // look at the San Francisco
      var la = ge.createLookAt('');
      la.set(-21.097313,55.475235,
        10000, // altitude
        ge.ALTITUDE_RELATIVE_TO_GROUND,
        0, // heading
        30, // straight-down tilt
        5000 // range (inverse of zoom)
        );
      ge.getView().setAbstractView(la);
    
      document.getElementById('installed-plugin-version').innerHTML =
        ge.getPluginVersion().toString();
    }
   

      function addKmlFromString(kmlString) 
      {
          // remove the old parsed KML object if one exists
      if (kmlObject)
        ge.getFeatures().removeChild(kmlObject);
    
         kmlObject = ge.parseKml(kmlString);
         
        alert(kmlObject);
        ge.getFeatures().appendChild(kmlObject);
       
        }
     
      function showHideKml() {
         kmlObject.setVisibility(!kmlObject.getVisibility());
      }
    function failureCallback(errorCode) {
    }
    
    function buttonClick() {
      var lookAt = ge.getView().copyAsLookAt(ge.ALTITUDE_RELATIVE_TO_GROUND);
      lookAt.setLatitude(-20.887883);
                lookAt.setLongitude(55.472796);
                lookAt.setRange(8000.0);
                lookAt.setTilt(45.0);
      ge.getView().setAbstractView(lookAt);
    }
    
    function parseKmlFromTextarea() {
       
      // remove the old parsed KML object if one exists
      
      if (currentKmlObject)
       {
           ge.getFeatures().removeChild(currentKmlObject);
       }   

            var kmlBox = document.getElementById('ttext');
            var kmlString = kmlBox.value;
          alert(kmlBox);  
      // parse the text in the box and add it to Earth
      try {
        currentKmlObject = ge.parseKml(kmlString);
        ge.getFeatures().appendChild(currentKmlObject);
      } catch (ex) {
        alert('Parse error');
      }
    }