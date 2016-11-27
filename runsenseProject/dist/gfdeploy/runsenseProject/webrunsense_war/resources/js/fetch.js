var ge;
var gex;
var tour;
var repiti;
var map;
var chad;
var biti=false;
var bpltf=false;
var currentKmlObject = null;
  google.load("earth", "1");
  //google.load("maps", "2");
/*blockui = function()
{
    document.getElementById("f_sch:schdl").ajaxEnabled(false);
};*/
message = function(txt)
{
    alert(txt);
};
/* = function()
{
    var lat=-21.15;
    var lng=55.5;
    if(document.getElementById('kml_lat')!==null)
        {
            lat=document.getElementById('kml_lat');
            lng=document.getElementById('kml_long');
        };
    var xhr_object = null;

                if(window.XMLHttpRequest) // Firefox
                   xhr_object = new XMLHttpRequest();
                else if(window.ActiveXObject) // Internet Explorer
                   xhr_object = new ActiveXObject("Microsoft.XMLHTTP");
                else { // XMLHttpRequest non supporté par le navigateur
                   alert("Votre navigateur ne supporte pas les objets XMLHTTPRequest !!");
                   return;
                }
    var ref='http://www.infoclimat.fr/public-api/gfs/xml?_ll='+lat+','+lng+'&_auth=UUtRRg5wU3FecwA3AnQAKVI6DzpZLwAnUCwLaAxpBHkHbAJjVTUBZwRqUSwALwE3BShVNgw3BTVUPwpyWihSM1E7UT0OZVM0XjEAZQItACtSfA9uWXkAJ1AyC2UMYgR5B2YCY1U1AX0EalEzADgBKwU1VT0MLAUiVDYKbloxUjhRNFEwDm9TN145AGICLQArUmQPblk3ADtQMAs4DDIEbwc3AmZVPwE1BDlRMgAuATEFMlU9DDMFOFQxCmRaN1IuUS1RTA4eUyxecQAgAmcAclJ8DzpZOABs&_c=711a113b5d63f3653cb75deef695c540';
    xhr_object.open('GET', ref, false);
                xhr_object.send(null);
                if(xhr_object.readyState === 4)
                {
                
                  var   repvli = xhr_object.responseText;
                  document.getElementById('f_reponse:kml_cli').value=repvli;  
                  document.getElementById('f_reponse:hiddenTps').click();
                    
                    
                }

};*/
focIframe = function(idnt)
{
    try{
    var ifr=document.getElementById('f_webcam:webcam').value;
    
        ifr[0].document.getElementById(idnt).focus();
    }catch(e)
    {
        alert('focIframe'+e);
    }
};
additi = function(butiti)
{
    chad = document.getElementById('f_rch:'+butiti);
    
    biti=true;
};
setPltfrm=function()
{
    bpltf=true;
};
disetPltfrm=function()
{
    bpltf=false;
};
getElevAPI = function ()
{
   var alt;
   
    
              alt = document.getElementById('f_reponse:kml_alt').value;
          alert(alt);
                var xhr_object = null;

                if(window.XMLHttpRequest) // Firefox
                   xhr_object = new XMLHttpRequest();
                else if(window.ActiveXObject) // Internet Explorer
                   xhr_object = new ActiveXObject("Microsoft.XMLHTTP");
                else { // XMLHttpRequest non supporté par le navigateur
                   alert("Votre navigateur ne supporte pas les objets XMLHTTPRequest !!");
                   return;
                }
                document.getElementById('f_reponse:kml_lng').value=alt.split(',')[0];
                document.getElementById('f_reponse:kml_lat').value=alt.split(',')[1];
               
                xhr_object.open('GET', 'http://maps.googleapis.com/maps/api/elevation/xml?locations='+alt+'&sensor=false', false);
                xhr_object.send(null);
                if(xhr_object.readyState === 4)
                {
                
                     repiti = xhr_object.responseText;
                    /* var tsplit = repiti.split('<elevation>');
                      tsplit = tsplit[1].split('</elevation>');
                     var elv=tsplit[0];*/
                   alert('elv '+repiti);
                     document.getElementById('f_reponse:kml_alt').value=repiti;
                    document.getElementById('f_reponse:hiddenElv').click();
                }
    
    };
getAddressFromLatLong = function (lat,lng,tmp)
{
    if(chad!==null)
        {
      if(lat===0 && lng===0)
          {
              lat = document.getElementById('f_reponse:kml_lat').value;
              lng = document.getElementById('f_reponse:kml_long').value;
          }
                var xhr_object = null;

                if(window.XMLHttpRequest) // Firefox
                   xhr_object = new XMLHttpRequest();
                else if(window.ActiveXObject) // Internet Explorer
                   xhr_object = new ActiveXObject("Microsoft.XMLHTTP");
                else { // XMLHttpRequest non supporté par le navigateur
                   alert("Votre navigateur ne supporte pas les objets XMLHTTPRequest !!");
                   return;
                }
                
               
                xhr_object.open('GET', 'http://maps.googleapis.com/maps/api/geocode/xml?latlng='+lat+','+lng+'&sensor=false', false);
                xhr_object.send(null);
                if(xhr_object.readyState === 4)
                {
                
                     repiti = xhr_object.responseText;
                     
                     var tsplit = repiti.split('<formatted_address>');
                     if(tsplit!==null)
                         {
                             var tsplit = tsplit[1].split('</formatted_address>');
                             var ad=tsplit[0]+'/'+tmp;
                             //alert(ad);
                             chad.value =ad;
                         }
                     
                     
                   
                     
                    
                }
           
                
        }
    };
//itineraire
    iti =function()
            {
             var iti_d=document.getElementById('f_rch:iti_d').value;
                alert(iti_d);
                var iti_a=document.getElementById('f_rch:iti_a').value;
                //alert(iti_a);
              /* 
              //  alert('directionsService');
                var directionsService = new google.maps.DirectionsService();
               // alert(directionsService);
                 var request = {
                    origin:iti_d,
                    destination:iti_a,
                    travelMode: google.maps.TravelMode.DRIVING
                  };
                  directionsService.route(request, function(result, status) 
                  {
                     // alert(status);
                        if (status == google.maps.DirectionsStatus.OK) {
                          
                          document.getElementById('f_haut:iti').value = result;
                            //alert(result);
                        }
                      });
                  
                   */
                        var xhr_object = null;
                       
                        if(window.XMLHttpRequest) // Firefox
                            xhr_object = new XMLHttpRequest();
                         else if(window.ActiveXObject) // Internet Explorer
                            xhr_object = new ActiveXObject("Microsoft.XMLHTTP");
                         else { // XMLHttpRequest non supporté par le navigateur
                            alert("Votre navigateur ne supporte pas les objets XMLHTTPRequest !!");
                            return;
                         }
             alert(iti_a); 
             try{
               /* xhr_object.open('GET', 'http://maps.googleapis.com/maps/api/directions/xml?origin='+iti_d+'&destination='+iti_a+'&sensor=true&key=AIzaSyD2otijFAsU4Dr1PrkLb8wzFanEkUV268U', true);
                    xhr_object.open('GET', 'http://www.mapquestapi.com/directions/v2/route?key=Fmjtd%7Cluur21u1ll%2C70%3Do5-90y2df&from='+iti_d+'&to='+iti_a+'&callback=renderNarrative', false);*/
                 xhr_object.open('GET', 'http://localhost:8080/webRunsense/itineraire.html?dep='+iti_d+'&amp;?ar='+iti_a+';', true);     
            var body={ origin: iti_d,
                        destination:iti_a };
            xhr_object.send(body);
                      
                   alert(xhr_object.readyState);
                   /*xhr_object.onreadystatechange = function()
                   {*/
                       if(xhr_object.readyState === 4)
                        {
                            alert(xhr_object);
                             repiti = xhr_object.responseText;
                             alert(repiti);
                             document.getElementById('f_rch:iti').value=repiti.value;
                        };
                  // };
                        
                    
             }catch(e)
             {
                 alert(e);
             }

            };
        
    clickSouris= function(event)
    {
        var place=event.getTarget();
        if (place.getType() === 'KmlPlacemark' ||
            place.getType() === 'KmlPoint'||
            place.getType() === 'KmlLineString')
              {
                document.getElementById('f_reponse:kml_res').value=place.getName()+'_'+place.getType();
                
                document.getElementById('f_reponse:hiddenClick').click();
              };
    };
   
   isrtCo=function(event){
       try{
            document.getElementById('f_reponse:kml_lat').value=event.getLatitude();
            document.getElementById('f_reponse:kml_long').value=event.getLongitude();
            
            }catch(e)
            {
            document.getElementById('f_dtl:lat').value=event.getLatitude();
            document.getElementById('f_dtl:long').value=event.getLongitude();
            
            document.getElementById('map3d:lat').value=event.getLatitude();
            document.getElementById('map3d:long').value=event.getLongitude();
           
            //alert('f_dtl latlong');
            }
   };
   overmouse=function(event){
      // alert(event.getLatitude());
     
      isrtCo(event);
       var place=event.getTarget();
       
                    
            if (place.getType() === 'KmlPlacemark' ||
                 place.getType() === 'KmlPoint'||
                 place.getType() === 'KmlLineString')
                   {
                        

                             document.getElementById('f_reponse:kml_res').value=place.getName();

                             document.getElementById('f_reponse:hiddendesc').click();
                             
                   }
        
   };
     //gestion evenement drag and drop
   capSouris=function(event)
   {

            
           // isrtCo(event);
            var place=event.getTarget();
            if(biti)
                {
                    var tmp=chad.getAttribute('id').valueOf();
                    chad=document.getElementById('f_reponse:kml_res');
                    var lat=document.getElementById('f_reponse:kml_lat');
                    var lng=document.getElementById('f_reponse:kml_long');
                    try{
                      lat.value=event.getLatitude(); 
                      lng.value=event.getLongitude(); 
                     getAddressFromLatLong(event.getLatitude(),event.getLongitude(),tmp);
                    
                    document.getElementById('f_reponse:hiddenIti').click();
                    }catch(ex)
                    {
                        alert('getAddressFromLatLong(event.getLatitude(),event.getLongitude())'+ex);
                    }
                }else if(bpltf)
                    {
                        document.getElementById('f_dtl:lat').value=event.getLatitude();
                        document.getElementById('f_dtl:long').value=event.getLongitude();
                        if (place.getType() === 'KmlPlacemark' ||
                        place.getType() === 'KmlPoint'||
                        place.getType() === 'KmlLineString')
                          {

                          
                            document.getElementById('f_reponse:kml_res').value=place.getName();//+'_'+place.getType()
                           
                            document.getElementById('f_reponse:hiddenDblClick').click();

                            /*dragInfo = {
                              placemark: event.getTarget(),
                              dragged: false
                            };*/


                          };
                    }else
                    {
                        if (place.getType() === 'KmlPlacemark' ||
                        place.getType() === 'KmlPoint'||
                        place.getType() === 'KmlLineString')
                          {

                          
                            document.getElementById('f_reponse:kml_res').value=place.getName();//+'_'+place.getType();
                           
                            document.getElementById('f_reponse:hiddenDblClick').click();

                            /*dragInfo = {
                              placemark: event.getTarget(),
                              dragged: false
                            };*/


                          }else
                          {
                             
                              try{
                                chad=document.getElementById('f_reponse:kml_res');
                              chad=getAddressFromLatLong(event.getLatitude(), event.getLongitude(),'');
                              document.getElementById('f_reponse:hiddendesc').click();
                                }catch(ex)
                                {
                                    alert('getAddressFromLatLong(event.getLatitude(),event.getLongitude())'+ex);
                                }
                             
                              
                              
                             // alert(chad.value);
                          };
                    };
            
            
             
              
        
        
            
        
   };
   /* event drag and drop
   function dplSouris(event)
   {
      
       if (dragInfo)
       {   
            event.preventDefault();
            var point = dragInfo.placemark.getGeometry();
            point.setLatitude(event.getLatitude());
            point.setLongitude(event.getLongitude());
            dragInfo.dragged = true;
       }

   }
   function posSouris(event)
   {
      
       if (dragInfo) 
       {  
            if (dragInfo.dragged) {
              // if the placemark was dragged, prevent balloons from popping up
              event.preventDefault();
            }
            alert('draged');
            alert(dragInfo.placemark.toString());
            dragInfo = null;
          }


   }
   
   function addEvent()
   {
      
      
       google.earth.addEventListener(ge.getWindow(), 'mousedown', capSouris);
       
        google.earth.addEventListener(ge.getGlobe(), 'mousemove', dplSouris);
        google.earth.addEventListener(ge.getWindow(), 'mouseup', posSouris);
        
   }*/
  init=function() {
    
   
   /* document.getElementById('f_haut:rzone').focus();
    document.getElementById('f_haut:rzone').click();*/
      try{
          var mapOptions = {
            zoom: 8,
            center: new google.maps.LatLng(-34.397, 150.644)
          };

          var map = new google.maps.Map(document.getElementById('cacheMap'),
              mapOptions);

      }catch(e)
      {
          
      }
   try{
        google.earth.createInstance('map3d', initCB, failureCB);
   }catch(ex)
   {
       
       
   }
    /*map = new GMap2 (document.getElementById("map"));
    map.setCenter(new GLatLng(0, 0), 0);*/

    
    /*try{
    if (navigator.geolocation)
       {
           var watchId = navigator.geolocation.watchPosition(local,
                                  failureCB,
                                  {enableHighAccuracy:true});
       } 
      else
        {
            alert("Votre navigateur ne prend pas en compte la géolocalisation HTML5"); 
        }
    }catch(e)
    {
        alert(e);
    }*/
      
      
    //init val sur lookat
    
    document.getElementById('f_reponse:kml_lat').value=-21.2;
    document.getElementById('f_reponse:kml_long').value=55.6;
    document.getElementById('f_reponse:kml_alt').value=90000;

    
    //initRing();
  };

  positionne=function()
  {
      try{
      // fly to 
            var la = ge.createLookAt('');
            la.set(document.getElementById('f_reponse:kml_lat'), document.getElementById('f_reponse:kml_long'),
              document.getElementById('f_reponse:kml_alt'), // altitude
              ge.ALTITUDE_RELATIVE_TO_GROUND,
              0, // heading
              0, // cabrage tilt
              5000 // range (inverse of zoom)
              );
          
            ge.getView().setAbstractView(la);
      }catch(ex)
            {
                
            }
         // map.setCenter(new GLatLng(position.coords.latitude,position.coords.longitude));
  };
 
  local=function(position){
  
  // fly to 
try{
    var la = ge.createLookAt('');
    la.set(position.coords.latitude, position.coords.longitude,
      50000, // altitude
      ge.ALTITUDE_RELATIVE_TO_GROUND,
      0, // heading
      0, // cabrage tilt
      5000 // range (inverse of zoom)
      );
   try{
       
     document.getElementById('f_reponse:kml_lat').value=position.coords.latitude;
    document.getElementById('f_reponse:kml_long').value=position.coords.longitude;
    document.getElementById('f_reponse:kml_alt').value=position.coords.longitudealtitudeAccuracy;
    }catch(e)
    {
        
    }
    ge.getView().setAbstractView(la);
  
  map.setCenter(new GLatLng(position.coords.latitude,position.coords.longitude));
 }catch(ex)
            {
                
            }
};

  localDe=function()
  {
 try{ 
     
    pauseTour();
  // fly to 
            var alt;
            var lat=document.getElementById('f_reponse:kml_lat').value;
            var lng= document.getElementById('f_reponse:kml_long').value;
            
    
   
    var la = ge.createLookAt('');
  
    la.set(lat, lng,
      2500, // altitude
      ge.ALTITUDE_RELATIVE_TO_GROUND,
      0, // heading
      0, // cabrage tilt
      5000 // range (inverse of zoom)
      );
          

    
    ge.getView().setAbstractView(la);
 }catch(ex)
            {
                
            }
   
};

   
    
  initCB=function initCB(instance) {

    ge = instance;

    gex = new GEarthExtensions(ge);
                          
    ge.getWindow().setVisibility(true);

    // add a navigation control
    ge.getNavigationControl().setVisibility(ge.VISIBILITY_AUTO);
    
    //altitude
    ge.getOptions().setStatusBarVisibility(true);
    ge.getOptions().setScaleLegendVisibility(true);
    ge.getOptions().setUnitsFeetMiles(true);
    
    // add some layers
    ge.getLayerRoot().enableLayerById(ge.LAYER_BORDERS, false);
    ge.getLayerRoot().enableLayerById(ge.LAYER_ROADS, false);

    //amplifier 
    ge.getLayerRoot().enableLayerById(ge.LAYER_TERRAIN, false);
    ge.getOptions().setTerrainExaggeration(6.3);
    // fly to 
    var la = ge.createLookAt('');
    la.set(-21.137472, 55.546906,
      150000, // altitude
      ge.ALTITUDE_RELATIVE_TO_GROUND,
      0, // heading
      0, // cabrage tilt
      7000 // range (inverse of zoom)
      );
        

    var options = ge.getOptions();
    
	options.setOverviewMapVisibility(true);
	options.setStatusBarVisibility(true);
      
      
      
    ge.getView().setAbstractView(la);
    fetchKmlFrom('f_reponse');
   // addEvent();
   
    
    /*if(biti)
        google.earth.addEventListener(ge.getWindow(), 'dblclick', clickSouris);
    else*/
        google.earth.addEventListener(ge.getWindow(), 'click', capSouris);
    
    google.earth.addEventListener(ge.getWindow(), 'mouseover', overmouse);
    
    document.getElementById('installed-plugin-version').innerHTML =
      ge.getPluginVersion().toString();
      
   
  
      
   
             
  };
  
  routeTerrain=function(b)
  {
      if(b)
          {
              ge.getLayerRoot().enableLayerById(ge.LAYER_ROADS, true);
              ge.getLayerRoot().enableLayerById(ge.LAYER_TERRAIN, true);
          }
          else
          {
              ge.getLayerRoot().enableLayerById(ge.LAYER_ROADS, false);
              ge.getLayerRoot().enableLayerById(ge.LAYER_TERRAIN, false);    
          }
        
  };
centreVue=function()
{
    var la = ge.createLookAt('');
    la.set(-21.137472, 55.546906,
      150000, // altitude
      ge.ALTITUDE_RELATIVE_TO_GROUND,
      0, // heading
      0, // cabrage tilt
      7000 // range (inverse of zoom)
      );
      pauseTour();
          ge.getView().setAbstractView(la);
          
};

  
  fetchKmlFromInput=function() {
 try{   
    if (currentKmlObject) {
      ge.getFeatures().removeChild(currentKmlObject);
      currentKmlObject = null;
    }
    
    var kmlUrlBox = document.getElementById('f_reponse:kml-url');
     kmlUrl = kmlUrlBox.value;
    //alert('kml url '+kmlUrl);
    google.earth.fetchKml(ge, kmlUrl, finishFetchKml);
     }catch(ex)
            {
                
            }
  };
  selectChild=function()
  {
      document.getElementById("map3d:tlb_aff").focus();
    
  };
  remvAll=function(){
try{
      var features = ge.getFeatures();
        while (features.getFirstChild())
           features.removeChild(features.getFirstChild());
   }catch(ex)
            {
                
            }
  };
  
  fetchKmlFrom=function(form) 
  {
 try{
    // remove the old KML object if it exists
    remvAll();
    
    if (currentKmlObject) {
      ge.getFeatures().removeChild(currentKmlObject);
      currentKmlObject = null;
    }
    
    var kmlTour;var kmlTracer;

      
            kmlTour = document.getElementById(form+':kml_tour').value;
                /*alert('kml_tour');
                alert(kmlTour.split("%"));*/
            kmlTracer = document.getElementById(form+':kml_tracer').value;
            /*alert('kml_tracer');
            alert(kmlTracer.split("%"));*/
            
           
      try{   
    if(kmlTracer!==null&&kmlTracer!==''&&kmlTracer!==undefined)
        {
           
           currentKmlObject=ge.parseKml(kmlTracer);
            ge.getFeatures().appendChild(currentKmlObject);
             
        }
    }catch(ex)
    {
        alert('Probleme de tracer '+ex);
    }        

    try{
    if(kmlTour!==null&&kmlTour!==''&&kmlTour!==undefined)
        {
           
            currentKmlObject=ge.parseKml(kmlTour);
            
                enterTour(currentKmlObject);
                
            
        }else if(currentKmlObject!==null)
            {
                var gex = new GEarthExtensions(ge);
              
                    gex.dom.walk({
                      rootObject: currentKmlObject,
                      visitCallback: function() {
                        // 'this' is the current DOM node being visited.
                        if ('getType' in this && this.getType() === 'KmlTour')
                          {
                              
                              alert(this.getType());
                              enterTour(this);
                          }
                      }
                    });

               
            }
   }catch(ex)
    {
        alert('Probleme de  tour'+ex);
    }
      
   
    

 }catch(ex)
            {
                
            }
  };
  
  finishFetchKml=function(kmlObject) {
try{
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
        alert('Probleme de finition');
      }, 0);
    }
 }catch(ex)
            {
                
            }
  };
  
  
  domPage=function()
  {
      
        alert('tours.length \n test ');
        
        if ('getType' in this && this.getType() === 'KmlTour')
         {
             tours.push(this);
         }
         
      
  };

  fetchKml=function(page) {
      //alert(page);
      var form;
        if(page==='visite')
        {
            form='f_reponse';
           /* additi('iti_a');
            getAddressFromLatLong(0,0);  */
        }
       else
            {
                form='plt_rght';

            }
       // initCB();
        fetchKmlFrom(form);
        
        
   
  };

//var cpte=false;
  enterTour=function(cKO) 
  {
      try{
          if (cKO!==null) 
          {
                
            
            currentKmlObject=cKO;
          }
               /*if(cpte)
                   ge.getTourPlayer().reset();
               else
                   cpte=true;*/
               
                ge.getTourPlayer().setTour(currentKmlObject);
                ge.getTourPlayer().play();
           
                
          
      }catch (ex)
      {
          alert('Erreur entrer tour'+ currentKmlObject+ ex);
      }
      
  
  };



pauseTour=function ()
{
  
  ge.getTourPlayer().pause();
};

resetTour=function ()
{
  ge.getTourPlayer().reset();
};

exitTour=function () 
{
  ge.getTourPlayer().setTour(null);
};

  recharge=function ()
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

            repositionEarth=function() {
            earthNode.style.height = earthWatchNode.offsetHeight + 'px';
            earthNode.style.width = earthWatchNode.offsetWidth + 'px';

            // calculate position on page
            var left = earthWatchNode.offsetLeft;
            var top = earthWatchNode.offsetTop;
            var p = earthWatchNode.offsetParent;

            while (p && p !== document.body) 
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
            };

            repositionEarth();

            // reposition every 100ms
            window.setInterval(function() {
            repositionEarth();
            }, 100);
        }
    };
      
 
  failureCB=function(errorCode)
  {
      
    // alert('Veuillez installer le plugin google earth ... veuillez cliquer au milieu du cadre noir !!');
     document.getElementById("f_tlbr:mitm_inst").click();
  };
  
  successCallback=function(pluginInstance)
  {
      
     ge = pluginInstance;
        ge.getWindow().setVisibility(true);

        enterTour((null));

  };
 
google.setOnLoadCallback(successCallback);