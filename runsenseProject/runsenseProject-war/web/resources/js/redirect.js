var url;
function RedirectSmartphone(objurl)
{
    
            if (objurl && objurl.length > 0 && IsSmartphone())     
            {

                    url=objurl;

                    window.location = url;


            }
    
         
}
function IsSmartphone(){
    
    if (DetectUagent("android"))
        {
            url="http://m.google.fr/earth";
            return true;
        }
    else if (DetectUagent("iphone"))
        {
            url="http://m.google.fr/earth";
            return true;
        }
    else if (DetectUagent("ipod"))
    {
        url="http://m.google.fr/earth";
        return true;
    } 
    else if (DetectUagent("symbian"))
        {
            url="http://m.google.fr/earth";
            return true;
        }
    else if (DetectUagent("Mac"))
        {
            url="http://m.google.fr/earth";
            return true;
        }
    else if (DetectUagent("Windows"))
    {
        $("#instPlug").easyconfirm({locale:
       {
		title: 'Installation du plugin Google earth API?',
		text: 'Voulez-vous installer le plugin Google earth api ?',
		button: ['Annuler',' accepter'],
		closeText: 'fermer'
       }});
        $("customDialog").click(function()
        {
        url='http://google-earth-plugin.en.softonic.com/';
        
        });
        return true;
    } 
    else if (DetectUagent("mac"))
    {
        $("#instPlug").easyconfirm({locale:
       {
		title: 'Installation du plugin Google earth API?',
		text: 'Voulez-vous installer le plugin Google earth api ?',
		button: ['Annuler',' accepter'],
		closeText: 'fermer'
       }});
        $("customDialog").click(function()
        {
            url='http://google-earth-plugin.en.softonic.com/';
        });
        return true;
    } 
    else if (DetectUagent("linux"))
    {
        url='http://ubuntuforums.org/showthread.php?t=1470754';
        return false;
    } 
    return false;
}
function DetectUagent(name){
    var uagent = navigator.userAgent.toLowerCase();
    
    if (uagent.search(name) > -1)
    return true;
    else
    return false;
}
