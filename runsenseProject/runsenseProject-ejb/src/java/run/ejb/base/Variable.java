package run.ejb.base;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Stream;
import run.ejb.entite.util.bdd.ChrSpe;
import run.ejb.util.kml.schema.IconKml;
import run.ejb.util.kml.schema.LigneKml;
import run.ejb.util.kml.schema.ObjetKml;
import run.ejb.util.kml.schema.RsnsObj;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Fujitsu
 */
public  class Variable {
    private static String sv="C:/home";
    
    private final static boolean bfe=Boolean.TRUE;
    private  static final String lieu="Ã®le de la Reunion";
    private final static String zact="zoneActivitÃ©";
    private final static String prcrs="parcours";
    private final static String[] lzact;
    private final static String[] linf;
    private final static List<String> ltitre;
    private final static Map<String, List<String>> region;

    private final static Map<String, String[]> mprmtrEnt;
    private final static Map<String,String> micone;
    
    private  final static String icgoogle="http://maps.google.com/mapfiles/kml/shapes/";
    private  final static String icrunsense="http://runsense.re/image/icone/";
    
    private final static Map<String,String> mIclim;
    private final static Map<String, String[]> tabApprox;
    private final static java.util.TreeMap<String, String> lthm;
    private final static List<String> prmtr;
    private final static List<String> rdmCouleur;
    private final static Map<String,String> woeid;
    private final static Map<Object,Object> mlagu;
    //map pramétrage des vues
    private final static Map<Object,List<RsnsObj>> affichage; 
    private final static String[] anxdact={"tÃ©l","gsm","label","acces hdcp"};
   
    private static void addClssmt(final Classmt clsmt, List tmpl)
    {
        tmpl.add(clsmt.getRef());
        clsmt.getList().stream().forEach((Object so) -> {
            if(so instanceof Classmt)
            {
                Classmt sclsmt=(Classmt) so;
                // sclsmt.getList().forEach(ssp->tmpl.add(ssp));
                sclsmt.getList().stream().forEach((sso) -> {
                    tmpl.add((String) sso);
                });
            }else
            {
                tmpl.add((String) so);
            }
        });
       
    }
     private static void ssdcClieudit(final Object so,List tmpl)
    {
                    if(so instanceof Classmt)
                        {
                            
                                ssdcClassmt(so, tmpl);
                        }else
                        {
                             tmpl.add((String) so);
                        }
    }
    private static void ssdcClassmt(final Object so,List tmpl)
    {
        if(so instanceof Classmt)
                        {
                            Classmt sclsmt=(Classmt) so;
                                sclsmt.getList().stream().
                                      forEach(sso->tmpl.add((String) sso));
                                /*for(Object sso:sclsmt.getList())
                                    tmpl.add((String) sso);*/
                        }else
                        {
                             tmpl.add((String) so);
                        }
    }
    public static boolean verifVarEntrer(String str)
    {
        List<String> tmpl=new ArrayList();
        categActivite().stream().
                peek(clsmt->tmpl.add(clsmt.getRef())).
                map(clsmt->clsmt.getList()).
                forEach(so->ssdcClassmt(so, tmpl));
                
       /* for(Classmt clsmt:categActivite()) 
           {
               tmpl.add(clsmt.getRef());
                    for(Object so:clsmt.getList())
                        ssdcpctgact(so, tmpl);
           }*/
        /*categlieuDit().stream().filter(o -> o instanceof Classmt).
                forEach(o -> addClssmt((Classmt) o, tmpl));*/
        categlieuDit().stream().
                forEach(so->ssdcClieudit(so, tmpl));
                
        /*for(Object o:categlieuDit()) 
           {
               if(o instanceof Classmt)
                {
                    Classmt clsmt=(Classmt) o;
                    tmpl.add(clsmt.getRef());
                    for(Object so:clsmt.getList())
                        if(so instanceof Classmt)
                        {
                            Classmt sclsmt=(Classmt) so;
                            //sclsmt.getList().forEach(sso -> tmpl.add((String) sso));
                                for(Object sso:sclsmt.getList())
                                    tmpl.add((String) sso);
                        }else
                        {
                             tmpl.add((String) so);
                        }
                }
           }*/
                        
        tmpl.addAll(categSentier());
        boolean rtrn=false;
        if(tmpl.contains(str))
            rtrn=true;
        
        return rtrn;
    }
    public static List<String> categSentier()
    {
        return Arrays.asList(prmtr.get(0), prmtr.get(1),prmtr.get(2));
    }
    public static List<Classmt> categActivite()
    {
       Classmt clmnt=Classmt.buid(ltitre.get(21), Lists.newArrayList(prmtr.get(38),prmtr.get(32),prmtr.get(33),prmtr.get(34)
                      ,prmtr.get(35),prmtr.get(36),prmtr.get(37)));
       Classmt clmnt1=Classmt.buid(ltitre.get(22), Lists.newArrayList(prmtr.get(39),prmtr.get(43),prmtr.get(60),prmtr.get(61)));
       Classmt clmnt2=Classmt.buid(ltitre.get(23), Lists.newArrayList(prmtr.get(19),prmtr.get(26),prmtr.get(31),prmtr.get(45),
                      prmtr.get(47),prmtr.get(48),prmtr.get(50),prmtr.get(52),prmtr.get(49),prmtr.get(55),prmtr.get(60)));
       Classmt clmnt3=Classmt.buid(ltitre.get(24), Lists.newArrayList(prmtr.get(31),prmtr.get(45),prmtr.get(46),prmtr.get(53)
                      ,prmtr.get(54),prmtr.get(56),prmtr.get(57),prmtr.get(58),prmtr.get(59)));
        return Arrays.asList(clmnt,clmnt1,clmnt2,clmnt3);
    }
    public static List categlieuDit()
    {
        Classmt clmnt=Classmt.buid(prmtr.get(6),Lists.newArrayList(prmtr.get(23),prmtr.get(24),prmtr.get(25),prmtr.get(26),prmtr.get(29)));
        ArrayList arrlist = new ArrayList(linf.length);
        arrlist.addAll(Arrays.asList(linf));
        Classmt clmnt1=Classmt.buid(prmtr.get(4),arrlist);
       Classmt clmnt2=Classmt.buid(ltitre.get(28),Lists.newArrayList(ltitre.get(27),prmtr.get(25),prmtr.get(14),prmtr.get(15),
               prmtr.get(17)));
       
        return Arrays.asList(clmnt,clmnt1,clmnt2,ltitre.get(19),ltitre.get(20));
    }
 
   static{
     
       Map mtmp=new HashMap<>();
            mtmp.put(new run.ejb.entite.geo.Ligne().getClass().getCanonicalName(), "nom_adresse_description".split("_"));
            mtmp.put(new run.ejb.entite.geo.Pt().getClass().getCanonicalName(), "nom_adresse_description_latitude_longitude_alt".split("_"));
            mtmp.put(new run.ejb.entite.geo.Tour().getClass().getCanonicalName(), "idTour_nom".split("_"));
            mtmp.put(new run.ejb.entite.geo.Prmtr().getClass().getCanonicalName(), "altMode_flyMode_dura_head_tilt_range_tourPrmtr_geoanimat".split("_"));
            mtmp.put(new run.ejb.entite.geo.Coord().getClass().getCanonicalName(), "idcoord_latitude_longitude_alt".split("_"));
            mtmp.put(new run.ejb.entite.geo.Evnmt().getClass().getCanonicalName(), "nom_adresse_categ_deb_fin".split("_"));
         mprmtrEnt = Collections.unmodifiableMap(mtmp);
      linf=new String[4];
        linf[0]="eau";
        linf[1]="foret";
        linf[2]="mont";
        linf[3]="quartier";
     
      lzact=new String[6];
        lzact[0]="stade de foot";
        lzact[1]="tennis";
        lzact[2]="marchÃ©";
        lzact[3]="festivals";
        lzact[4]="concert";
        lzact[5]="boite de nuits"; 
        
     mtmp=new HashMap<>();
        mtmp.put("francais", "fr");
        mtmp.put("english", "en");
            String[] str={"fr","re"};
        mtmp.put("kréol", str);
        mtmp.put("allemand", "de");
        mtmp.put("espagnol", "es");
        mtmp.put("portugais", "pt");
        mtmp.put("chinois", "zh");
        mtmp.put("tamoul", "ta");
      mlagu= Collections.unmodifiableMap(mtmp);
        
      ltitre=Collections.unmodifiableList(Arrays.asList(
        "totaldevue",//0
        "Thèmedelarecherche",//1
        "Catégorie",//2
        "Zone",//3
        "Ville",//4
        "taBleaude",//5
        "nombre",//6
        "nom",//7
        "Localisation",//8
        "Prévision",//9
        "tableauscrollabledesprévisionsgénéraledel'île",//10
        "mobile",//11
        "Plateforme",//12
        "rech",//13
        "webCam",//14
        "prv météo",//15
        "itinéraire",//16
        "graph cote",//17
        "Entrer une recherche",//18
        
        "pointd'infotouristique",//19
        "hopitauxouserviced'urgence",//20
        "Manger&Dormir",//21
        "Montagne",//22
        "Mer",//23
        "Airtoutterrain&autre",//24      
        "plage",//25
        "rivière",//26
        "Monument",//27
        "patrimoine",//28
        "piton",//29
        "pÃ©riode"));//30
      
       prmtr=new ArrayList();
        prmtr.add("sentier");//0
        prmtr.add("route");//1
        prmtr.add("chemin");//2
        prmtr.add("place");//3
        prmtr.add("pointInteret");//4
        prmtr.add("activitÃ©");//5
        prmtr.add("lieuDit");//6
        prmtr.add("zone");//7
        prmtr.add("secteur");//8
        prmtr.add("Ã©venement");//9
        prmtr.add("meteo marinne");//10
        prmtr.add("marchÃ©");//11
        
        
        
       rdmCouleur=Collections.unmodifiableList(Arrays.asList(
        "501478AA",//vertOrange  0 70%
        "50F09614",//bleuciel  1 
        "5078DCB4",//vertterne  2
        
        "501478FF",//orange     3
        "50B41414 ",//bleu nuit    4
        "50147800 ",//vertForet  5
        
        "5014B4FF",//orangeClair   6
        "50F0FA14",//bleuturquoise  7   
        "50B4F014",//vertbleu   8
        
        "4b2878B4",//ONF   9
        
        "641478AA",//vertOrange 10 100%
        "64A0783C",//bleuciel  11 
        "641E783C",//vertterne  12
        
        "640078F0",//orange     13
        "64780000 ",//bleu nuit    14
        "64143C00 ",//vertForet  15
        
        "6478FFF0",//orangeClair   16
        "64F0FF14",//bleuturquoise  17   
        "643C8C14",//vertbleu   18
        
        "6478003C"));//ONF   19
      
        lthm = new java.util.TreeMap<>();
            lthm.put("général", "south-street");
            lthm.put(prmtr.get(0), "le-frog");
            lthm.put(prmtr.get(5), "blitzer");
            lthm.put(prmtr.get(6), "black-tie");
            //lthm.put("evnmt", "rocket");
            
            
       ChrSpe spe=new ChrSpe();
       
          mtmp=new HashMap<>();
          woeid = new LinkedHashMap<String, String>();
          
        List temp=Arrays.asList("Saint-Denis","Sainte-Marie","Sainte-Suzanne","La Possession","Le Port");   
                mtmp.put("Nord", temp);
                
           woeid.put("Nord", "23424931");
            woeid.put("Saint-Denis", "23424931");woeid.put("Sainte-Marie", "1410244");
            woeid.put("Sainte-Suzanne", "1410246");woeid.put("La Possession", "1410099");woeid.put("Le Port", "24551469");
 
         temp=Arrays.asList("Mafate","Saint-Leu","Trois Bassins","Saint-Paul","Saint-Gille","La Saline");      
                mtmp.put("Ouest", temp);
            
                woeid.put("Ouest", "24549831");
               // woeid.put("0uest", "24549831");
                woeid.put("Trois Bassins", "1410255");
                woeid.put("Saint-Leu", "1410236");
                woeid.put("Saint-Paul", "1410238");
                woeid.put("Saint-Gille", "1410233");
                woeid.put("Les Avirons", "1410145");
                woeid.put("L'Etang-Sale", "1410029");
                woeid.put("Saint-louis", "1410237");
                woeid.put("Mafate", "24551468");
        
         temp=Arrays.asList("enclosTremblet","Saint-Phillipe","Saint-Joseph","Petite ile","Saint-pierre","tampon","la plaine des cafres",
                "Cilaos", "Les Avirons","L'Etang-Sale","L'entre-deux","Saint-louis");       
                mtmp.put("Sud", temp);
               
                woeid.put("Sud", "1410235");
                woeid.put("enclosTremblet", "12746223");woeid.put("Saint-Phillipe", "1410239");woeid.put("Saint-Joseph", "1410235");
                woeid.put("Petite ile", "1410194"); woeid.put("tampon", "1410141");woeid.put("la plaine des cafres", "1410097");
               woeid.put("L'entre-deux", "1511203");
                
                
                
                
                
                
        
         temp=Arrays.asList("Salazie","Saint-Andre","Bras panon","Saint Benoit","Sainte-Anne","Sainte rose","La Plaine des palmiste");

                mtmp.put("Est", temp);
     
                woeid.put("Est", "24549829");
                woeid.put("Salazie", "24551452"); woeid.put("Saint-Andre", "1410229");woeid.put("Bras panon", "1410003");
                woeid.put("La Plaine des palmiste", "24551455");
                woeid.put("Saint Benoit", "1410230");woeid.put("Sainte-Anne", null); woeid.put("Sainte rose", "1410245");
                
        region = Collections.unmodifiableMap(mtmp);   
        
        
        
        
        
        
      
                             
        final String[] tab = {"Ã©glise","chapelle","cheminÃ©e","mausolÃ©","indiens","hôtel de ville","chinois",//12...18
            "lno","sno","historique","banque","bassin","cascade","maison","plonger","pont","vierge","domaine","villa","jno",//19...31
            "gite","restaurant","snack","cafet","hotel","chambre d'hote","appartement",//32...38
                        "Ã©quitation","randonnÃ©","plongÃ©","surf","loisir nautique","vtt","pêche","golf","croisière","visio sous-marine","bateau",//39...49
                        "catamaran","jet ski","micro offshore","paint-ball","luge","barque","kart","quad","4X4","moto","hydrospeed","Kayakraft","parapente",//50...62
                        prmtr.get(11),"meteo marinne",ltitre.get(14), "tennis", "foot","athlÃ©tisme",
                        ltitre.get(25),ltitre.get(26),ltitre.get(27),ltitre.get(28),ltitre.get(29),lzact[4],lzact[5],//63...68
                        prmtr.get(0),prmtr.get(6),//69..71
                        ltitre.get(21),ltitre.get(22),ltitre.get(23),ltitre.get(24),lzact[3], zact,prmtr.get(5),
                        prmtr.get(1),prmtr.get(2),prcrs};
        
       final String[] tref={icrunsense+"église.png",icrunsense+"chapelle.png",icrunsense+"cheminée.png",icrunsense+"mausolé.png",icrunsense+"indiens.png",icrunsense+"hôtel de ville.png",icrunsense+"chinois.png",
            icrunsense+"materieldeplage.png",icrunsense+"surf.png",icrunsense+"certificat.png",icrunsense+"banque.png",icrunsense+"bassins.png",icrunsense+"cascade.png",icrunsense+"maison.png",icrunsense+"plonger.png",icrunsense+"pont.png",icrunsense+"vierge.png",icrunsense+"domaine.png",icrunsense+"domaine.png",icrunsense+"jetski.png",
            icrunsense+"gitedefrance.png",icgoogle+"dining.png",icrunsense+"snack_bar.png",icgoogle+"dining.png",icgoogle+"lodging.png",icrunsense+"gitedefrance.png",icrunsense+"appartement.png",
                        icgoogle+"horsebackriding.png",icgoogle+"hiker.png",icrunsense+"plonger.png",icrunsense+"surf.png",icgoogle+"swimming.png",icgoogle+"cycling.png",icgoogle+"fishing.png",icgoogle+"golf.png",icgoogle+"ferry.png",icrunsense+"visio.png",icgoogle+"sailing.png",
                        icrunsense+"catamaran.png",icrunsense+"jetski.png",icrunsense+"micro offshore.png",icrunsense+"paintball.png",icrunsense+"luge.png",icrunsense+"barque.png",icrunsense+"kart.png",icrunsense+"quad.png",icrunsense+"4X4.png", icgoogle+"motorcycling.png",icrunsense+"hydrospeed.png",icrunsense+"Kayakraft.png",icrunsense+"parapente.png",
                        icrunsense+"marché.png",icrunsense+"meteoMarinne.png",icgoogle+"webcam.png",icrunsense+"tennis.png",icrunsense+"foot.png",icrunsense+"athlétisme.png" ,
                        icrunsense+ltitre.get(25)+".png",icrunsense+ltitre.get(26)+".png",icrunsense+ltitre.get(28)+".png",icrunsense+ltitre.get(28)+".png",icrunsense+ltitre.get(29)+".png",icrunsense+lzact[4]+".png",icrunsense+lzact[5]+".png",
                        icrunsense+prmtr.get(0)+".png",icrunsense+prmtr.get(6)+".png",
                        icrunsense+ltitre.get(21)+".png",icrunsense+ltitre.get(22)+".png",icrunsense+ltitre.get(23)+".png",icrunsense+ltitre.get(24)+".png",icrunsense+"etoile.png",icrunsense+"zoneActivité.png",icrunsense+"activité.png",
                        icrunsense+prmtr.get(1)+".png",icrunsense+prmtr.get(2)+".png",icrunsense+prcrs+".png"};
        String[] rclim={
            "soleil",
            "nuage soleil",
            "pluie soleil",
            "forte pluie soleil",
            "orage",
            "pluie",
            "nuage",
            "lune",
            "orage disperse"
        };
        final String[] tclim={
            icrunsense+"temps/"+"sun.png",
            icrunsense+"temps/"+"nuage_soleil.png",
            icrunsense+"temps/"+"pluie_soleil.png",
            icrunsense+"temps/"+"fortePluie_soleil.png",
            icrunsense+"temps/"+"orageux.png",
            icrunsense+"temps/"+"pluie.png",
            icrunsense+"temps/"+"nuage.png",
            icrunsense+"temps/"+"lune.png",
            icrunsense+"temps/"+"orage dispersé.png"
        };
        
       
        prmtr.addAll(Arrays.asList(tab));
        List<String> asList = Arrays.asList(tref);
       
       mtmp=Maps.uniqueIndex(asList, new Function<String, String>() {
            int i=-1;
            @Override
                public String apply(String from) { 
                    i++;
                    return tab[i];
                    
                }});
       micone= Collections.unmodifiableMap(mtmp);
       /* for(int i=0; i<tab.length;i++)
        {
            if(!prmtr.contains(tab[i]))
                prmtr.add(tab[i]);
            micone.put(tab[i], tref[i]);
        }*/
       asList = Arrays.asList(rclim);
        mtmp=Maps.uniqueIndex(asList, new Function<String, String>() {
            int i=-1;
            @Override
                public String apply(String from) { 
                    i++;
                    return tclim[i];
                    
                }});
        mIclim= Collections.unmodifiableMap(mtmp);
       /* mIclim=new HashMap<String, String>();
        for(int i=0; i<rclim.length;i++)
        {
            mIclim.put(rclim[i], tclim[i]);
        }*/
        
         mtmp=new HashMap<>();
           String cle="";
           String[] clerech;
               cle="tourdelIle";
               clerech=new String[1];
                clerech[0]="'tour d£entrÃ©'/rchbdd/ligne/fix/nom";
                    mtmp.put(cle, clerech); 
                   
               cle=ltitre.get(4)+"/point";
               clerech=new String[2];
                clerech[0]="'"+prmtr.get(4)+"_"+ltitre.get(4).toLowerCase()+"'/rchbdd/point/fix/adresse";
                clerech[1]="'"+prmtr.get(6)+"_"+ltitre.get(4).toLowerCase()+"'/rchbdd/ligne/fix/adresse";
                    mtmp.put(cle, clerech); 
              
               
                         
                cle="inf";
                    int cpt=0;
                    clerech=new String[linf.length];
                    
                        for(String inf:linf)
                        {         

                             clerech[cpt]="'"+inf+"'/rchbdd/point/fix/adresse";
                             cpt++;

                        }
                    mtmp.put(cle, clerech);
                
                cle="inf/envrch";
                    cpt=0;
                    clerech=new String[linf.length];
                       for(String inf:linf)
                       {   
                            clerech[cpt]="rchMulti/List:envrch-'"+prmtr.get(4)+"_"+inf+"'/point/adresse-adresse/MUST_MUST";
                            cpt++;
                       }
                   mtmp.put(cle, clerech);
                   
                cle=prmtr.get(6)+"/envrch";
               clerech=new String[1];
                clerech[0]="rchMulti/List:envrch-'lieuDit'/point/adresse-adresse/MUST_MUST";
                    mtmp.put(cle, clerech); 
               
                String pm="categ";
                     cle=pm;
                        clerech=new String[1];
                         clerech[0]="categ"+"/rchbdd/point/fix/adresse";
                         //clerech[1]="'"+"lieuDit_ville"+"'/rchbdd/point/fix/adresse";
                            mtmp.put(cle, clerech); 
               
                       
                pm=zact;
                     cle=pm;
                        clerech=new String[1];
                         clerech[0]="res/rchbdd/point/fix/adresse";
                         //clerech[1]="res/rchbdd/point/fix/description";
                            mtmp.put(cle, clerech);
                    
                pm=lzact[3]+"/sem";
                     cle=pm;
                        clerech=new String[1];
                        //clerech[0]="sem/rchbdd/point/fix/description";
                        clerech[0]="rchMulti/List:'"+lzact[3]+"'-sem/point/adresse-description/MUST_MUST";
                            mtmp.put(cle, clerech);
                            
                pm="envrch/"+lzact[3];
                     cle=pm;
                        clerech=new String[1];
                        clerech[0]="rchMulti/List:'"+lzact[3]+"'-envrch/point/adresse-adresse/MUST_MUST";
                            mtmp.put(cle, clerech); 
                            
                cle="parcours";
                    clerech=new String[3];
                    clerech[0]="rchMulti/List:envrch-'"+prmtr.get(0)+"'/ligne/adresse-adresse/MUST_MUST";
                    clerech[1]="rchMulti/List:envrch-'route'/ligne/adresse-adresse/MUST_MUST";
                    clerech[2]="rchMulti/List:envrch-'chemin'/ligne/adresse-adresse/MUST_MUST";
                         mtmp.put(cle, clerech);
                for(int i=0; i<=2; i++)
                {
                     pm=prmtr.get(i);
                     cle=pm;
                        clerech=new String[1];
                         clerech[0]="'"+prmtr.get(i)+"'/rchbdd/ligne/fix/adresse";
                         mtmp.put(cle, clerech); 
                     pm="envrch/"+prmtr.get(i);
                     cle=pm;
                        clerech=new String[1];
                         clerech[0]="rchMulti/List:envrch-res/ligne/adresse-adresse/MUST_MUST";
                            mtmp.put(cle, clerech); 
                }
                for(int i=3; i<=6; i++)
                {
                    pm=prmtr.get(i);
                     cle=pm;
                        clerech=new String[1];
                         clerech[0]="'"+prmtr.get(i)+"'/rchbdd/point/fix/adresse";
                         mtmp.put(cle, clerech); 
                     pm="envrch/"+prmtr.get(i);
                     cle=pm;
                        clerech=new String[1];
                         clerech[0]="rchMulti/List:envrch-res/point/adresse-adresse/MUST_MUST";
                            mtmp.put(cle, clerech); 
                }
                for(int i=7; i<=8; i++)
                {
                    pm=prmtr.get(i);
                     cle=pm;
                        clerech=new String[1];
                         clerech[0]="'"+prmtr.get(i)+"'/rchbdd/ligne/fix/adresse";
                         mtmp.put(cle, clerech);
                     pm="envrch/"+prmtr.get(i);
                     cle=pm;
                        clerech=new String[1];
                         clerech[0]="rchMulti/List:envrch-res/ligne/adresse-adresse/MUST_MUST";
                            mtmp.put(cle, clerech); 
                }
                pm=prmtr.get(9);
                     cle=pm;
                        clerech=new String[1];
                         clerech[0]="'"+prmtr.get(9)+"'/rchbdd/point/fix/adresse";
                         mtmp.put(cle, clerech);
                 pm="envrch/"+prmtr.get(9);
                     cle=pm;
                        clerech=new String[1];
                         clerech[0]="rchMulti/List:envrch-res/point/adresse-adresse/MUST_MUST";
                            mtmp.put(cle, clerech); 
                            
                 pm="envrch/"+zact;
                     cle=pm;
                        clerech=new String[1];
                         clerech[0]="rchMulti/List:envrch-res/point/adresse-adresse/MUST_MUST";
                            mtmp.put(cle, clerech);
             
                            
               cle=prmtr.get(7)+"/precis";
               clerech=new String[2];
                clerech[0]="rchMulti/List:"+prmtr.get(7)+"-'"+prmtr.get(7)+"'/ligne/adresse-adresse/MUST_MUST";
                clerech[1]="rchMulti/List:"+prmtr.get(7)+"-'"+prmtr.get(8)+"'/ligne/adresse-adresse/MUST_MUST";
                    mtmp.put(cle, clerech); 
                    
               cle=ltitre.get(4)+"/precis";
               clerech=new String[1];
                clerech[0]="rchMulti/List:ville-'"+prmtr.get(8)+"'/ligne/nom-adresse/MUST_MUST";
                
                    mtmp.put(cle, clerech); 
                   
               cle=ltitre.get(14);
               clerech=new String[1];
                //clerech[0]="rchMulti/List:envrch-'webcam'/point/adresse-adresse/MUST_MUST";
                clerech[0]="rchMulti/List:envrch-'"+ltitre.get(14)+"'/point/adresse-adresse/MUST_MUST";
                   mtmp.put(cle, clerech);
                   
               cle="festival/important";
               clerech=new String[1];
                clerech[0]="envrch/rchbdd/evenement/fix/adresse";              
                   mtmp.put(cle, clerech);
                   
               cle="quartier/envrch";
               clerech=new String[1];
               clerech[0]="rchMulti/List:envrch-'"+prmtr.get(6)+"'/ligne/adresse-adresse/MUST_MUST";
                   mtmp.put(cle, clerech);
                   
               cle=prmtr.get(11)+"/envrch";
               clerech=new String[1];
               clerech[0]="rchMulti/List:envrch-'"+prmtr.get(11)+"'/point/adresse-adresse/MUST_MUST";
               //clerech[1]="rchMulti/List:envrch-'"+prmtr.get(11)+"'/point/adresse-description/MUST_MUST";
               
                   mtmp.put(cle, clerech);
                
               cle=prmtr.get(6)+"/autour";
               clerech=new String[1];
               clerech[0]="rchMulti/List:envrch-'"+prmtr.get(6)+"'/ligne/adresse-adresse/MUST_MUST";
                   mtmp.put(cle, clerech);
                   
               cle=prmtr.get(4)+"/autour";
               clerech=new String[1];
               clerech[0]="rchMulti/List:envrch-'"+prmtr.get(4)+"'/point/adresse-adresse/MUST_MUST";
                   mtmp.put(cle, clerech);
                   
               cle="meteo/simple";
               clerech=new String[1];
               clerech[0]="yahooClimat";
                   mtmp.put(cle, clerech);
               
               cle="envrch/evnmt";
               clerech=new String[1];
               clerech[0]="rchMulti/List:'deb'-'fin'/Date/adresse-adresse/MUST_MUST";
              
                    mtmp.put(cle, clerech);
               
               
               cle=prmtr.get(11)+"/jour";
               clerech=new String[1];
               //clerech[0]="rchMulti/List:'"+prmtr.get(11)+"'-jour/point/description-description/MUST_MUST";
               clerech[0]="rchMulti/List:'"+prmtr.get(11)+"'-jour/point/adresse-adresse/MUST_MUST";
                   mtmp.put(cle, clerech); 
                
               cle="tracer/precis";
               clerech=new String[1];
               clerech[0]="rchMulti/List:res-envrch/ligne/nom-adresse/MUST_MUST";
                   mtmp.put(cle, clerech);
               
               cle="recherche/precis";
               clerech=new String[1];
               clerech[0]="rchMulti/List:res-envrch/point/nom-adresse/MUST_MUST";
                   mtmp.put(cle, clerech); 
                   
               cle=zact+"/precis";
               clerech=new String[1];
               clerech[0]="rchMulti/List:res-'"+zact+"'/point/adresse-adresse/MUST_MUST";
                   mtmp.put(cle, clerech); 
                   
               cle="categ/precis";
               clerech=new String[2];
               clerech[0]="rchMulti/List:res-envrch/point/nom-adresse/MUST_MUST";
               clerech[1]="rchMulti/List:res-categ/point/nom-adresse/MUST_MUST";
                   mtmp.put(cle, clerech); 
                   
               cle="tracer/alentour";
               clerech=new String[1];
               clerech[0]="rchMulti/List:envrch-res/point/nom-adresse/MUST_no";
                   mtmp.put(cle, clerech);     
                   
               cle="point/alentour";
               clerech=new String[1];
               //clerech[0]="rchMulti/List:envrch-'pointInteret'/point/adresse-adresse/MUST_MUST";
               clerech[0]="envrch/rchbdd/point/fix/adresse";
                   mtmp.put(cle, clerech);     
                   
               cle="rest/alentour";
               clerech=new String[3];
               clerech[0]="rchMulti/List:envrch-'restaurant'/point/adresse-adresse/MUST_MUST";
               clerech[1]="rchMulti/List:envrch-'snack'/point/adresse-adresse/MUST_MUST";
               clerech[2]="rchMulti/List:envrch-'cafet'/point/adresse-adresse/MUST_MUST";
              
                   mtmp.put(cle, clerech);    
                   
               /* cle[0]="restCult";
               cle[1]="alentour";
               clerech=new String[1];
               clerech[0]="rchbdd_point_fix_adresse";
                   tabApprox.put(cle, clerech);*/
                   
               /* cle[0]="rest";
               cle[1]="alentour";
               clerech=new String[1];
               clerech[0]="rchbdd_point_fix_adresse";
                   tabApprox.put(cle, clerech);*/
                   
               /* cle[0]="evnmtPrinx";
               cle[1]="precis";
               clerech=new String[1];
               clerech[0]="rchbdd_point_fix_adresse";
                   tabApprox.put(cle, clerech);*/
                   
                /* cle[0]="tracer";
               cle[1]="alentour";
               clerech=new String[1];
               clerech[0]="rchbdd_point_fix_adresse";
                   tabApprox.put(cle, clerech);*/
                   
                /* cle[0]="point";
               cle[1]="alentour";
               clerech=new String[1];
               clerech[0]="rchbdd_point_fix_adresse";
                   tabApprox.put(cle, clerech);*/
                   
               /* cle[0]="recherche";
               cle[1]="precis";
               clerech=new String[1];
               clerech[0]="rchbdd_point_fix_adresse";
                   tabApprox.put(cle, clerech);*/
             tabApprox= Collections.unmodifiableMap(mtmp);    
                   
               //geographie 0
        mtmp=new HashMap<>();
        ///////////************//////////// 
            
            List<RsnsObj> lrsns=new ArrayList<>(6);
                
                
                 
                 //region 0 rougeNoir vertBlanc
                 lrsns.add(
                        new RsnsObj(prmtr.get(7), 
                         //new ObjetKml(  "54140078" ,null,null,"50FFFFFF")
                        new LigneKml( 2.7d, "54140078"),
                                prmtr.get(7)
                         ));
                 
                 //tour de l'ile
                 lrsns.add(
                        new RsnsObj("tourdelIle", 
                        new LigneKml( 0.7d, "3214B4F0"),
                         "tour"));
                 
                 lrsns.add(
                        new RsnsObj(prmtr.get(6), 
                        new IconKml( 1d, "text"),
                        prmtr.get(6))); 
                 lrsns.add(
                        new RsnsObj(prmtr.get(4), 
                        new IconKml(1d, "imageurl"),
                        prmtr.get(4)));
            //météo général
                lrsns.add(
                        new RsnsObj("meteo/simple", 
                        new IconKml( 1.3d, "imageurl"),
                        "météo"));
            //marché diverse 75%
                /*lrsns.add(
                        new RsnsObj("festival/important", 
                        new IconKml( 1.2d, "imageurl")));  */ 
            //festivale communales 70%
                lrsns.add(
                        new RsnsObj(prmtr.get(11)+"/jour", 
                        new IconKml(1.2d, "imageurl"),
                        prmtr.get(11)));
                
                lrsns.add(
                        new RsnsObj(ltitre.get(14), 
                        new IconKml( 0.7d, "imageurl"),
                        ltitre.get(14))); 
                
                lrsns.add(
                        new RsnsObj(lzact[3]+"/sem", 
                        new IconKml( 0.7d, "imageurl"),
                        lzact[3])); 
                lrsns.add(
                        new RsnsObj(lzact[5]+"/jour", 
                        new IconKml( 0.7d, "imageurl"),
                        lzact[5])); 
            
            mtmp.put("00", lrsns);
            
            lrsns=new ArrayList<RsnsObj>(5);
            
            //geo sentier
               lrsns.add(
                        new RsnsObj(prmtr.get(7), 
                        new LigneKml(3.6d ,"54140078"),
                        prmtr.get(7)));
            
               lrsns.add(
                        new RsnsObj("tourdelIle", 
                        new LigneKml( 0.3d, "641400FF"),
                         "tour"));
                lrsns.add(
                        new RsnsObj("parcours", 
                         new LigneKml(1.8d ,rdmCouleur.get(8)),
                                "parcours"));
              /* lrsns.add(
                        new RsnsObj(ltitre.get(14), 
                        new IconKml( 0.7d, "imageurl"),
                        ltitre.get(14)));
                lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/autour", 
                        new ObjetKml( "50002814" ,null,null,null),
                        prmtr.get(6)));
                lrsns.add(
                        new RsnsObj(ltitre.get(4)+"/point", 
                        new IconKml( 0.7d, "text"),
                         ltitre.get(4)));*/
                lrsns.add(
                        new RsnsObj("inf", 
                        new IconKml(1d, "text"),
                        prmtr.get(4)));
            mtmp.put("01", lrsns);
            lrsns=new ArrayList<RsnsObj>(3);
            //geo evenement
               
               lrsns.add(
                        new RsnsObj(prmtr.get(7), 
                        new LigneKml(3.6d ,"54140078"),
                        prmtr.get(7)));
            
               lrsns.add(
                        new RsnsObj("tourdelIle", 
                        new LigneKml( 0.3d, "641400FF"),
                        "tour"));
                /*lrsns.add(
                        new RsnsObj("envrch/evnmt", 
                        new ObjetKml( null ,null,"50B4F014","641400FF")));*/
                lrsns.add(
                        new RsnsObj(ltitre.get(4)+"/point", 
                        new IconKml( 0.7d, "text")
                         ,ltitre.get(4)));
                lrsns.add(
                        new RsnsObj("inf", 
                        new IconKml(1d, "text"),
                        prmtr.get(4)));
             
            mtmp.put("02", lrsns);
            lrsns=new ArrayList<RsnsObj>(3);
            //geo activité
               
                lrsns.add(
                        new RsnsObj(prmtr.get(7), 
                        new LigneKml(3.6d ,"54140078"),
                        prmtr.get(7)));
            
                lrsns.add(
                        new RsnsObj("tourdelIle", 
                        new LigneKml( 0.3d, "641400FF"),
                        "tour"));
                lrsns.add(
                        new RsnsObj("categ", 
                        new IconKml(1d, "imageurl"),
                        "categ"));
               /* lrsns.add(
                        new RsnsObj(ltitre.get(4)+"/point", 
                        new IconKml( 0.7d, "text"),
                         ltitre.get(4)));*/
               lrsns.add(
                        new RsnsObj("inf", 
                        new IconKml(1d, "text"),
                        prmtr.get(4)));
             
            mtmp.put("03", lrsns);
            lrsns=new ArrayList<RsnsObj>(4);
            //geo evenement
               
                lrsns.add(
                        new RsnsObj(prmtr.get(7), 
                        new LigneKml(3.6d ,"54140078"),
                        prmtr.get(7)));
            
                lrsns.add(
                        new RsnsObj("tourdelIle", 
                        new LigneKml( 0.3d, "641400FF"),
                        "tour"));
                lrsns.add(
                        new RsnsObj(prmtr.get(4), 
                        new IconKml(1d, "imageurl"),
                        prmtr.get(4)));
                lrsns.add(
                        new RsnsObj("categ", 
                        new ObjetKml( "5078DCB4" ,null,null,"50828282"),
                        "categ"));
                lrsns.add(
                        new RsnsObj(ltitre.get(4)+"/point", 
                        new IconKml( 0.7d, "text"),
                                ltitre.get(4)
                         ));
                lrsns.add(
                        new RsnsObj("inf", 
                        new IconKml(1d, "text"),
                        prmtr.get(4)));
             
            mtmp.put("04", lrsns);
            lrsns=new ArrayList<RsnsObj>(3);
            //geo activité
               
                lrsns.add(
                        new RsnsObj(prmtr.get(7), 
                        new LigneKml(3.6d ,"54140078"),
                        prmtr.get(7)));
            
                lrsns.add(
                        new RsnsObj("tourdelIle", 
                        new LigneKml( 0.3d, "641400FF"),
                        "tour"));
                lrsns.add(
                        new RsnsObj(zact, 
                        new IconKml(1d, "imageurl"),
                        zact));
               /* lrsns.add(
                        new RsnsObj(ltitre.get(4)+"/point", 
                        new IconKml( 0.7d, "text"),
                        ltitre.get(4) ));
               lrsns.add(
                        new RsnsObj("inf", 
                        new IconKml(1d, "text"),
                        prmtr.get(4)));*/
             
            mtmp.put("05", lrsns);
        ///////////************//////////// 
            //zone 1
            lrsns=new ArrayList<RsnsObj>(6);
                 lrsns.add(
                        new RsnsObj(prmtr.get(7)+"/precis", 
                        new LigneKml(3.2d, "50140078"),
                        prmtr.get(7)));
            
                lrsns.add(
                        new RsnsObj("envrch/autour", 
                        new ObjetKml( "5078DCB4" ,null,null,"50828282"),
                        prcrs));
          
            //sentier
                lrsns.add(
                        new RsnsObj("envrch", 
                         new LigneKml(0.9d,"hzd"),
                        prcrs));

               lrsns.add(
                        new RsnsObj(ltitre.get(14), 
                        new IconKml( 0.7d, "imageurl"),
                        ltitre.get(14))); 
             
                lrsns.add(
                        new RsnsObj("envrch/"+lzact[3], 
                        new IconKml( 0.7d, "imageurl"),
                        lzact[3])); 

                lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/envrch", 
                        new IconKml( 0.5d, "text"),
                        prmtr.get(6))); 
                lrsns.add(
                        new RsnsObj("inf/envrch", 
                        new IconKml(1d, "text"),
                        prmtr.get(6)));
            mtmp.put("10", lrsns);
                    
               //zone sentier 11 zone sentier
                lrsns=new ArrayList<RsnsObj>(4);
               //lieu dit touristique mauveVertForet violetVertbleu  
                lrsns.add(
                        new RsnsObj(prmtr.get(7)+"/precis", 
                        new LigneKml(3.2d, "50140078"),
                        prmtr.get(7)));
            
                lrsns.add(
                        new RsnsObj(ltitre.get(14), 
                        new IconKml( 0.7d, "imageurl"),
                        ltitre.get(14)));
               
                lrsns.add(
                        new RsnsObj("envrch", 
                         new LigneKml(1.8d ,rdmCouleur.get(8)),
                        prcrs));
                lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/autour", 
                        new ObjetKml("50002814" ,null,null,null),
                        prmtr.get(6)));
                lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/envrch", 
                        new IconKml( 0.5d, "text"),
                        prmtr.get(6))); 
                lrsns.add(
                        new RsnsObj("inf/envrch", 
                        new IconKml(1d, "text"),
                        prmtr.get(6)));
                
                mtmp.put("11", lrsns);
                
                //zone sentier 12 zone evenements
                lrsns=new ArrayList<RsnsObj>(2);
                lrsns.add(
                        new RsnsObj(prmtr.get(7)+"/precis", 
                        new LigneKml(3.2d, "50140078"),
                        prmtr.get(7)));
               //météo du moment
                /*lrsns.add(
                        new RsnsObj("meteo/simple", 
                        new IconKml(1.2d, ""))); */ 
                //évenement principaux
               /* lrsns.add(
                        new RsnsObj("evenements/principaux", 
                        new IconKml(1.2d, "")));*/
                lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/envrch", 
                        new IconKml( 0.5d, "text"),
                        prmtr.get(6))); 
                mtmp.put("12", lrsns);
                
                //zone sentier 13 zone activité
                lrsns=new ArrayList<RsnsObj>(2);
                lrsns.add(
                        new RsnsObj(prmtr.get(7)+"/precis", 
                        new LigneKml(3.2d, "50140078"),
                        prmtr.get(7)));
                //météo du moment
               /* lrsns.add(
                        new RsnsObj("meteo/simple", 
                        new IconKml(1.2d, ""))); */
               //activité symbole
                lrsns.add(
                        new RsnsObj("envrch/"+prmtr.get(5), 
                        new IconKml(1d, "imageurl"),
                        prmtr.get(5)));  
                lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/envrch", 
                        new IconKml( 0.5d, "text"),
                        prmtr.get(6))); 
                
                mtmp.put("13", lrsns);
                //zone sentier 13 zone activité
                lrsns=new ArrayList<RsnsObj>(3);
                lrsns.add(
                        new RsnsObj(prmtr.get(7)+"/precis", 
                        new LigneKml(3.2d, "507850F0"),
                        prmtr.get(7))); 
                //météo du moment
               /* lrsns.add(
                        new RsnsObj("meteo/simple", 
                        new IconKml(1.2d, ""))); */
                
                lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/autour", 
                        new ObjetKml( "5078DCB4" ,null,null,"50828282"),
                        prmtr.get(6)));
                lrsns.add(
                        new RsnsObj("envrch/"+prmtr.get(4), 
                        new IconKml(1d, "imageurl"),
                        prmtr.get(4)));
                lrsns.add(
                        new RsnsObj("inf/envrch", 
                        new IconKml(1d, "text"),
                        prmtr.get(6)));
                lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/envrch", 
                        new IconKml( 0.5d, "text"),
                        prmtr.get(6))); 
                mtmp.put("14", lrsns);
                //zone sentier 13 zone activité
                lrsns=new ArrayList<RsnsObj>(2);
                lrsns.add(
                        new RsnsObj(prmtr.get(7)+"/precis", 
                        new LigneKml(3.2d, "507850F0"),
                        prmtr.get(7)));
                //météo du moment
               /* lrsns.add(
                        new RsnsObj("meteo/simple", 
                        new IconKml(1.2d, ""))); */
               //activité symbole
                lrsns.add(
                        new RsnsObj("envrch/"+zact, 
                        new IconKml(1d, "imageurl"),
                        zact));  
                lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/envrch", 
                        new IconKml( 0.5d, "text"),
                        prmtr.get(6))); 
                
                mtmp.put("15", lrsns);
            ///////////************////////////     
            //ville 2
             lrsns=new ArrayList<RsnsObj>(6);
            //contours ville 100%
            
             
                lrsns.add(
                        new RsnsObj(ltitre.get(4)+"/precis", 
                        new LigneKml(1.8d, "4b780A00"),
                        ltitre.get(4))); 
                lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/autour", 
                        new ObjetKml( "5078DCB4" ,null,null,"50828282"),
                        prmtr.get(6)));
                lrsns.add(
                        new RsnsObj(prmtr.get(4)+"/autour", 
                        new IconKml(1d, "imageurl"),
                        prmtr.get(4)));
            
            lrsns.add(
                        new RsnsObj(ltitre.get(14), 
                        new IconKml( 0.7d, "imageurl"),
                        ltitre.get(14))); 
               /*lrsns.add(
                        new RsnsObj("meteo/simple", 
                        new IconKml(1.2d, "imageurl")));*/
               lrsns.add(
                        new RsnsObj("envrch/"+lzact[3], 
                        new IconKml( 0.7d, "imageurl"),
                        lzact[3]));
               lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/envrch", 
                        new IconKml( 0.5d, "text"),
                        prmtr.get(6))); 
               lrsns.add(
                        new RsnsObj("inf/envrch", 
                        new IconKml(1d, "text"),
                        prmtr.get(6)));
            mtmp.put("20", lrsns);
            
                //ville sentier 21 ville sentier
                lrsns=new ArrayList<RsnsObj>(4);
                lrsns.add(
                        new RsnsObj(ltitre.get(4)+"/precis", 
                        new LigneKml(2.6d, "4b780A00"),
                        ltitre.get(4)));
               
                lrsns.add(
                        new RsnsObj("envrch", 
                         new LigneKml(1.8d ,rdmCouleur.get(8)),
                         prcrs));
                /*lrsns.add(
                        new RsnsObj("meteo/simple", 
                        new IconKml( 1.2d, "imageurl")));*/
                
		lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/autour", 
                        new ObjetKml( "5078DCB4" ,null,null,"50828282"),
                        prmtr.get(6)));
                  lrsns.add(
                        new RsnsObj(ltitre.get(14), 
                        new IconKml( 0.7d, "imageurl"),
                        ltitre.get(14))); 
                lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/envrch", 
                        new IconKml( 0.5d, "text"),
                        prmtr.get(6)));
                lrsns.add(
                        new RsnsObj("inf/envrch", 
                        new IconKml(1d, "text"),
                        prmtr.get(6)));
                mtmp.put("21", lrsns);
                
                //ville evenements 22 ville evenements
                lrsns=new ArrayList<RsnsObj>(3);
               //lieux festif principaux avec l'évenements du moments, ou le premier
                lrsns.add(
                        new RsnsObj(ltitre.get(4)+"/precis", 
                        new LigneKml(2.6d, "4b780A00"),
                        ltitre.get(4)));
                /*lrsns.add(
                        new RsnsObj("envrch/evnmt", 
                        new ObjetKml( null ,null,"50000000","50FFFFFF")));*/
               //météo du moment
               lrsns.add(
                        new RsnsObj(ltitre.get(14), 
                        new IconKml( 0.7d, "imageurl"),
                        ltitre.get(14)));
               lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/envrch", 
                        new IconKml( 0.5d, "text"),
                        prmtr.get(6))); 
                mtmp.put("22", lrsns);
                
                //ville activité 23 ville activité
                lrsns=new ArrayList<RsnsObj>(2);
               //icone représentatifs des différentes activité de la zone
                lrsns.add(
                        new RsnsObj(ltitre.get(4)+"/precis", 
                        new LigneKml(2.6d, "4b780A00"),
                        ltitre.get(4)));
                lrsns.add(
                        new RsnsObj("envrch/"+prmtr.get(5), 
                        new IconKml(1.2d, "imageurl"),
                        prmtr.get(5))); 
               //météo du moment et détailer de la zone
                /*lrsns.add(
                        new RsnsObj("meteo/simple", 
                        new IconKml(1.2d, "imageurl")));*/
                lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/envrch", 
                        new IconKml( 0.5d, "text"),
                        prmtr.get(6))); 
                mtmp.put("23", lrsns);
                lrsns=new ArrayList<RsnsObj>(3);
                lrsns.add(
                        new RsnsObj(ltitre.get(4)+"/precis", 
                        new LigneKml(2.6d, "4b780A00"),
                        ltitre.get(4)));
                 lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/autour", 
                        new ObjetKml( "5078DCB4" ,null,null,"50828282"),
                        prmtr.get(6)));
                 lrsns.add(
                        new RsnsObj("envrch/"+prmtr.get(4), 
                        new IconKml(1.2d, "imageurl"),
                        prmtr.get(4)));
               //météo du moment et détailer de la zone
               /* lrsns.add(
                        new RsnsObj("meteo/simple", 
                        new IconKml(1.2d, "imageurl")));*/
                 lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/envrch", 
                        new IconKml( 0.5d, "text"),
                        prmtr.get(6))); 
                mtmp.put("24", lrsns);
                //ville activité 23 ville activité
                lrsns=new ArrayList<RsnsObj>(2);
               //icone représentatifs des différentes activité de la zone
                lrsns.add(
                        new RsnsObj(ltitre.get(4)+"/precis", 
                        new LigneKml(2.6d, "4b780A00"),
                        ltitre.get(4)));
                lrsns.add(
                        new RsnsObj("envrch/"+zact, 
                        new IconKml(1.2d, "imageurl"),
                        zact)); 
               //météo du moment et détailer de la zone
              /*  lrsns.add(
                        new RsnsObj("meteo/simple", 
                        new IconKml(1.2d, "imageurl")));*/
                lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/envrch", 
                        new IconKml( 0.5d, "text"),
                        prmtr.get(6))); 
                mtmp.put("25", lrsns);
            ///////////************//////////// 
            //recherche 3
             lrsns=new ArrayList<RsnsObj>(3);
            //recherche 100% noirRougeNoir rougeBlanc        
                lrsns.add(
                        new RsnsObj("tracer/precis", 
                        new ObjetKml( "50140078" ,"501400E6","50000000","50FFFFFF"),
                        prcrs));
            //polygoneTransparent lieu dit alentour 45% mauveVertforet violetVertBleu   
                lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/autour", 
                        new ObjetKml( "50780078" ,"507850F0","50147800","50B4F014"),
                        prmtr.get(6)));
                lrsns.add(
                        new RsnsObj(ltitre.get(4)+"/point", 
                        new ObjetKml( "31080108" ,null,"4b780A00","50B4F014"),
                         ltitre.get(4))); 
                lrsns.add(
                        new RsnsObj("envrch/"+lzact[3], 
                        new IconKml( 0.7d, "imageurl"),
                        lzact[3]));
                lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/envrch", 
                        new IconKml( 0.5d, "text"),
                        prmtr.get(6))); 
                lrsns.add(
                        new RsnsObj("inf/envrch", 
                        new IconKml(1d, "text"),
                        prmtr.get(6)));
            mtmp.put("30", lrsns);
            
                 //recherche sentier 31 recherche sentier
                lrsns=new ArrayList<RsnsObj>(4);
               lrsns.add(
                        new RsnsObj("tracer/precis", 
                        new LigneKml(3.6d, "55781EB4" ),
                        prcrs));
                
                //objRsns alentour , lieu
                  lrsns.add(
                        new RsnsObj("point/alentour", 
                        new IconKml(0.4d, "imageurl"),
                        prmtr.get(5)));
                  
		//météo du moment 100%
              lrsns.add(
                        new RsnsObj(ltitre.get(14), 
                        new IconKml( 0.4d, "imageurl"),
                        ltitre.get(14)));
              lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/autour", 
                        new ObjetKml( "50002814"  ,null,null,null),
                        prmtr.get(6)));  
              lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/envrch", 
                        new IconKml( 0.5d, "text"),
                        prmtr.get(6))); 
              lrsns.add(
                        new RsnsObj("inf/envrch", 
                        new IconKml(1d, "text"),
                        prmtr.get(6)));
                mtmp.put("31", lrsns);
                
                //recherche evenements 32 recherche evenements
                lrsns=new ArrayList<RsnsObj>(3);
               
                lrsns.add(
                        new RsnsObj("tracer/precis", 
                        new ObjetKml( "50140078" ,"501400E6","50000000","50FFFFFF"),
                        prcrs));
                lrsns.add(
                        new RsnsObj("rest/alentour", 
                        new IconKml(1.2d, "imageurl"),
                        prmtr.get(5)));
                lrsns.add(
                        new RsnsObj(ltitre.get(14), 
                        new IconKml( 0.7d, "imageurl"),
                        ltitre.get(14)));
                
                lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/envrch", 
                        new IconKml( 0.5d, "text"),
                        prmtr.get(6))); 
                mtmp.put("32", lrsns);
                
                //recherche activité 33 recherche activité
                lrsns=new ArrayList<RsnsObj>(3);
               lrsns.add(
                        new RsnsObj("recherche/precis", 
                        new IconKml(1.2d, "imageurl"),
                        prmtr.get(5)));
                lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/autour", 
                        new IconKml(1.2d, "imageurl"),
                        prmtr.get(6)));
                lrsns.add(
                        new RsnsObj(ltitre.get(14), 
                        new IconKml( 0.7d, "imageurl"),
                        ltitre.get(14)));
                lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/envrch", 
                        new IconKml( 0.5d, "text"),
                        prmtr.get(6))); 
                mtmp.put("33", lrsns);
                 lrsns=new ArrayList<RsnsObj>(4);
               lrsns.add(
                        new RsnsObj("tracer/precis", 
                        new ObjetKml( "50140078" ,"501400E6","50000000","50FFFFFF"),
                        prcrs));
               lrsns.add(
                        new RsnsObj("recherche/precis", 
                        new IconKml(1.2d, "imageurl"),
                        prmtr.get(5)));
              
                lrsns.add(
                        new RsnsObj("tracer/alentour", 
                        new ObjetKml(null,null, "50F0FA14", "50B41414"),
                        prcrs));
              lrsns.add(
                        new RsnsObj(ltitre.get(14), 
                        new IconKml( 0.7d, "imageurl"),
                        ltitre.get(14)));
              lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/envrch", 
                        new IconKml( 0.5d, "text"),
                        prmtr.get(6))); 
                mtmp.put("34", lrsns);
                  lrsns=new ArrayList<RsnsObj>(6);
                  lrsns.add(
                        new RsnsObj("recherche/precis", 
                        new IconKml(1.2d, "imageurl"),
                        prmtr.get(4)));
                  
                  lrsns.add(
                             new RsnsObj("tracer/precis", 
                             new ObjetKml( "50140078" ,"501400E6","50000000","50FFFFFF"),
                             prcrs));
               
                lrsns.add(
                        new RsnsObj("tracer/alentour", 
                        new ObjetKml(null,null, "50F0FA14", "50B41414"),
                        prcrs));
              lrsns.add(
                        new RsnsObj(ltitre.get(14), 
                        new IconKml( 0.7d, "imageurl"),
                        ltitre.get(14)));
              lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/envrch", 
                        new IconKml( 0.5d, "text"),
                        prmtr.get(6))); 
                mtmp.put("35", lrsns);
                lrsns=new ArrayList<RsnsObj>(2);
                  lrsns.add(
                        new RsnsObj("categ/precis", 
                        new IconKml(1.2d, "imageurl"),
                        "categ"));
               
               lrsns.add(
                        new RsnsObj(ltitre.get(14), 
                        new IconKml(0.7d, "imageurl"),
                        ltitre.get(14)));
               lrsns.add(
                        new RsnsObj(prmtr.get(6)+"/envrch", 
                        new IconKml( 0.5d, "text"),
                        prmtr.get(6))); 
                mtmp.put("36", lrsns);
                
                affichage= Collections.unmodifiableMap(mtmp); 
                
             
    }

    public static List<String> getRdmCouleur() {
        return rdmCouleur;
    }



    public static Map<String, List<String>> getRegion() {
        return region;
    }


    public static Map<String, String> getMicone() {
        return micone;
    }


    public static  Map<String, String> getmIclim() {
        return mIclim;
    }

  

    public static String getLieu() {
        return lieu;
    }

    public static Map<String, String[]> getTabApprox() {
        return tabApprox;
    }

   

    public static Map<Object, List<RsnsObj>> getAffichage() {
        return affichage;
    }

  

    public static Stream<Entry<String, String>> getEsthm() {
        return lthm.entrySet().stream();
    }

    public static TreeMap<String, String> getLthm() {
        return lthm;
    }

  

    public static List<String> getPrmtr() {
        return prmtr;
    }


    public static Map<String, String> getWoeid() {
        return woeid;
    }

   

    public static List<String> getLtitre() {
        return ltitre;
    }

   

    public static String getZact() {
        return zact;
    }


    public static Map<Object, Object> getMlagu() {
        return mlagu;
    }


    public static String[] getLzact() {
        return lzact;
    }

  

    public static boolean isBfe() {
        return bfe;
    }

    

    public static String[] getAnxdact() {
        return anxdact;
    }

   

    public static String getSv() {
        return sv;
    }

    public static void setSv(String sv) {
        Variable.sv = sv;
    }

    public static Map<String, String[]> getMprmtrEnt() {
        return mprmtrEnt;
    }

    public static String getIcgoogle() {
        return icgoogle;
    }

    public static String getIcrunsense() {
        return icrunsense;
    }

    public static String[] getLinf() {
        return linf;
    }

    public static String getPrcrs() {
        return prcrs;
    }

   

    
    
}
