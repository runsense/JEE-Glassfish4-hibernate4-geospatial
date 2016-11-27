var dep;
    var ar;
    processUser=function ()
    {
      var parameters = location.search.substring(1).split("&");

      var temp = parameters[0].split("=");
      l = unescape(temp[1]);
      temp = parameters[1].split("=");
      p = unescape(temp[1]);
      dep = l;
      ar= p;
      var chmdep=document.getElementById('dep');
        chmdep.value=dep;
      var chmarv=document.getElementById('arv');
        chmarv.value=ar;
    };
    calcIt=function()
    {
        var request = {
       origin: dep, 
       destination: ar,
       travelMode: google.maps.DirectionsTravelMode.DRIVING
        };

        directionsService.route(request, function(response, status) {
          if (status == google.maps.DirectionsStatus.OK) {
            directionsDisplay.setDirections(response);
          alert(response.routes[0].overview_path);
           
        }});
        
       
    };
    enDepArv=function()
    {
        dep=document.getElementById('dep').value;
        
        arv=document.getElementById('arv').value;
        
        calcIt();
    };
     
