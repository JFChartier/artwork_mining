/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte;

import Classification.Classe;
import Matrice.Vectorisateur.VecteurIndicie;
import Matrice.Vectorisateur.VectoriseurUtils;
import Ponderation.CoefficientAssociation;
import UtilsJFC.TrieUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author JF Chartier
 */
public class OeuvresUtils 
{
    private static final File fileCorpus = new File("C:\\Users\\JF Chartier\\Documents\\Collections standard\\Magritte\\html\\Magritte fichier html prepare.txt");
    
    
    public static Set<String> getDescripteur(Collection<Oeuvre> oeuvres)
    {
        Set<String> descripteurs = new TreeSet<>();
        for (Oeuvre o : oeuvres)
        {
            descripteurs.addAll(o.getListeDescripteur());
        }
        return descripteurs;
    }
    
    public static Set<Integer> getIdOeuvres (Collection<Oeuvre> oeuvres)
    {
        Set<Integer> ids = new HashSet<>(oeuvres.size());
        for (Oeuvre o: oeuvres)
            ids.add(o.getId());
        return ids;
    }
    
    public static Collection<String> getTokens (Collection<Oeuvre> oeuvres)
    {
        Collection<String> t = new ArrayList<>();
        for (Oeuvre o: oeuvres)
        {
            t.addAll(o.getListeDescripteur());
        }
        return t;
    }
    
    public static Map<Integer, String> getMap_id_descripteur(Collection<Oeuvre> oeuvres)
    {
        Map<Integer, String> id_descrip = new TreeMap<>();
        int id=0;
        for (String descrip: getDescripteur(oeuvres))
        {
            id_descrip.put(id, descrip);
            id++;
        }
        return id_descrip;
    }
    public static Map<String, Integer> getMap_descripteur_id(Collection<Oeuvre> oeuvres)
    {
        Map<String, Integer> descrip_id = new TreeMap<>();
        int id=0;
        for (String descrip: getDescripteur(oeuvres))
        {
            descrip_id.put(descrip, id);
            id++;
        }
        return descrip_id;
    }
    
    public static Map<Integer, List<String>> getMap_idOeuvre_listeDescripteur (Collection<Oeuvre> oeuvres)
    {
        System.out.println("getMap_idOeuvre_listeDescripteur");
        Map<Integer, List<String>> idOeuvre_descripteurs = new HashMap<>(oeuvres.size());
        for (Oeuvre o: oeuvres)
        {
            idOeuvre_descripteurs.put(o.getId(), new ArrayList<>(o.getListeDescripteur()));
        }
        return idOeuvre_descripteurs;
    }
    
    public static Map<Integer, List<Integer>> getMap_idOeuvre_listeIdDescripteur (Map<Integer, List<String>> Map_idOeuvre_descripteurs, Map<String, Integer> descrip_id)
    {
        System.out.println("getMap_idOeuvre_listeIdDescripteur");
        Map<Integer, List<Integer>> map_idOeuvre_idDescripteurs = new HashMap<>(Map_idOeuvre_descripteurs.size());
        for (int id: Map_idOeuvre_descripteurs.keySet())
        {
            List<Integer> listeId = new ArrayList<>(Map_idOeuvre_descripteurs.get(id).size());
            for (String s: Map_idOeuvre_descripteurs.get(id))
            {
                if (descrip_id.get(s)==null)
                {
                    System.err.println("descripteur absent : " + s);
                    System.exit(id);
                }
                listeId.add(descrip_id.get(s));
            }
            map_idOeuvre_idDescripteurs.put(id, listeId);
        }
        return map_idOeuvre_idDescripteurs;
    }
    
    public static Map<Integer, List<Integer>> getMap_idOeuvre_listeIdDescripteur (Collection<Oeuvre> oeuvres, Map<String, Integer> descrip_id)
    {
        System.out.println("getMap_idOeuvre_listeIdDescripteur");
        Map<Integer, List<String>> map_idOeuvre_listeDescripteur = new TreeMap<>(getMap_idOeuvre_listeDescripteur(oeuvres));
        return getMap_idOeuvre_listeIdDescripteur(map_idOeuvre_listeDescripteur, descrip_id);
    }
    
    public static Map<Integer, Set<Integer>> getMap_idOeuvre_ensembleIdDescripteur (Collection<Oeuvre> oeuvres, Map<String, Integer> descrip_id)
    {
        System.out.println("getMap_idOeuvre_esembleIdDescripteur");
        Map<Integer, List<Integer>> map = getMap_idOeuvre_listeIdDescripteur(oeuvres, descrip_id);
        Map<Integer, Set<Integer>> map_idOeuvre_ensembleIdDescripteur = new HashMap<>(map.size());
        for (int idOeuvre: map.keySet())
        {
            map_idOeuvre_ensembleIdDescripteur.put(idOeuvre, new TreeSet<Integer>(map.get(idOeuvre)));
        }
        return map_idOeuvre_ensembleIdDescripteur;
    }
    
    // recuperer getMap_idOeuvre_ensembleIdDescripteur d'un sous-ensemble de idOeuvre
    public static Map<Integer, Set<Integer>> getMap_idOeuvre_ensembleIdDescripteur (Collection<Oeuvre> oeuvres, Set<Integer> subSetIdOeuvres, Map<String, Integer> descrip_id)
    {
        System.out.println("getMap_idOeuvre_esembleIdDescripteur");
        Map<Integer, List<Integer>> map = getMap_idOeuvre_listeIdDescripteur(oeuvres, descrip_id);
        Map<Integer, Set<Integer>> map_idOeuvre_ensembleIdDescripteur = new HashMap<>(map.size());
        for (int idOeuvre: subSetIdOeuvres)
        {
            map_idOeuvre_ensembleIdDescripteur.put(idOeuvre, new TreeSet<Integer>(map.get(idOeuvre)));
        }
        return map_idOeuvre_ensembleIdDescripteur;
    }
    
    /**
    *
    * @author JF Chartier
    * recuperer id oeuvres sans le descipteur cible
    */
    public static Set<Integer> getIdOeuvreSansCible (Collection<Oeuvre> oeuvres, String descripteur)
    {
        Set<Integer> idsO = new TreeSet<>();
        for (Oeuvre o: oeuvres)
        {
            if (!o.getListeDescripteur().contains(descripteur))
            {
                idsO.add(o.getId());
            }
        }
        return idsO;
    }
    
    /**
    *
    * @author JF Chartier
    * recuperer id oeuvres avec le descipteur cible
    */
    public static Set<Integer> getIdOeuvreAvecCible (Collection<Oeuvre> oeuvres, String descripteur)
    {
        Set<Integer> idsO = new TreeSet<>();
        for (Oeuvre o: oeuvres)
        {
            if (o.getListeDescripteur().contains(descripteur))
            {
                idsO.add(o.getId());
            }
        }
        return idsO;
    }
    
    public static Map<String, Set<Integer>> getMap_descripteur_ensembleIdOeuvre (Collection<Oeuvre> oeuvres)
    {
        System.out.println("getMap_descripteur_ensembleIdOeuvre");
        Map<Integer, List<String>> map_idOeuvre_listeToken = getMap_idOeuvre_listeDescripteur(oeuvres);
        return TrieUtils.recupererMap_valeur_ensembleCle(map_idOeuvre_listeToken);
    }
    
    public static Map<Integer, Set<Integer>> getMap_idDescripteur_ensembleIdOeuvre (Collection<Oeuvre> oeuvres, Map<String, Integer> map_descripteur_id)
    {
        System.out.println("getMap_idDescripteur_ensembleIdOeuvre");
        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur = getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
        return TrieUtils.recupererMap_valeur_ensembleCle(map_idOeuvre_listeIdDescripteur);
    }
    
    public static Map<Integer, List<Integer>> getMap_idDescripteur_listeIdOeuvre (Collection<Oeuvre> oeuvres, Map<String, Integer> map_descripteur_id)
    {
        System.out.println("getMap_idDescripteur_listeIdOeuvre");
        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur = getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
        return TrieUtils.recupererMap_Valeur_listeCle(map_idOeuvre_listeIdDescripteur);
    }
        
    public static Map<String, Integer> getMap_descripteur_freqOeuvre (Collection<Oeuvre> oeuvres)
    {
        Map<String, Set<Integer>> map_descripteur_ensembleIdOeuvre = getMap_descripteur_ensembleIdOeuvre(oeuvres);
        Map<String, Integer> map_descripteur_freq = new HashMap<>(map_descripteur_ensembleIdOeuvre.size());
        for (String descrip: map_descripteur_ensembleIdOeuvre.keySet())
        {
            map_descripteur_freq.put(descrip, map_descripteur_ensembleIdOeuvre.get(descrip).size());
        }
        return map_descripteur_freq;
    }
    
    public static Map<Integer, VecteurIndicie> getMap_idDescripteur_vecteurAssociationIdDescripteur (Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur, Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteurCooc, Set<Integer> ensembleIdOeuvre, CoefficientAssociation coefficientAssociation)
    {
        System.out.println("getMap_idDescripteur_vecteurAssociationIdDescripteur");
        return VectoriseurUtils.vectoriserAvecCoefficientAssociation1(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteurCooc, ensembleIdOeuvre, coefficientAssociation);
    }
    
    public static Map<Integer, VecteurIndicie> getMap_idOeuvre_vecteurIdDescripteurBM25 (Collection<Oeuvre> oeuvres, Map<String, Integer> map_descripteur_id)
    {
        System.out.println("getMap_idOeuvre_vecteurIdDescripteurBM25");
        return VectoriseurUtils.vectoriser_BM25(getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id));
    }
    
    public static Map<Integer, VecteurIndicie> getMap_idOeuvre_vecteurIdDescripteurFREQ (Collection<Oeuvre> oeuvres, Map<String, Integer> map_descripteur_id)
    {
        System.out.println("getMap_idOeuvre_vecteurIdDescripteurBM25");
        return VectoriseurUtils.vectoriser_FREQ(getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id));
    }
    
    public static Map<Integer, VecteurIndicie> getMap_idOeuvre_vecteurIdDescripteurBinaire (Collection<Oeuvre> oeuvres, Map<String, Integer> map_descripteur_id)
    {
        System.out.println("getMap_idOeuvre_vecteurIdDescripteurBinaire");
        return VectoriseurUtils.vectoriser_BINAIRE(getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id));
    }
      
    public static Map<Integer, VecteurIndicie> getMap_idDescripteur_vecteurIdOeuvreFREQ (Collection<Oeuvre> oeuvres, Map<String, Integer> map_descripteur_id)
    {
        System.out.println("getMap_idDescripteur_vecteurIdOeuvreBM25");
        return VectoriseurUtils.vectoriser_FREQ(getMap_idDescripteur_listeIdOeuvre(oeuvres, map_descripteur_id));
    }
    
    public static Map<Integer, Set<Integer>> getMap_idDescripteur_ensembleIdDescripteurCooccurrent (Collection<Oeuvre> oeuvres, Map<String, Integer> map_descrip_id)
    {
        System.out.println("getMap_idDescripteur_ensembleIdDescripteurCooccurrent");
        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur = getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descrip_id);
        Map<Integer, Set<Integer>> map_idDescrip_ensembleIdOeuvre = TrieUtils.recupererMap_valeur_ensembleCle(map_idOeuvre_listeIdDescripteur);
        Map<Integer, Set<Integer>> map_idDescripteur_ensembleIdDescripCooc = new HashMap<>(map_descrip_id.size());
        
        for (int idDesc: map_descrip_id.values())
        {
            Set<Integer> ensembleIdCooc = new TreeSet<>();
            for (int idOeuvre: map_idDescrip_ensembleIdOeuvre.get(idDesc))
            {
                ensembleIdCooc.addAll(map_idOeuvre_listeIdDescripteur.get(idOeuvre));
            }
//            ensembleIdCooc.remove(idDesc); //afin d'eviter qu'un descripteur soit cooccurrent avec lui-meme
            map_idDescripteur_ensembleIdDescripCooc.put(idDesc, ensembleIdCooc);
        }
        return map_idDescripteur_ensembleIdDescripCooc;
    }

        
    // cette methode est base sur des classes de descripteur
    public static Map<Integer, Set<Integer>> getMap_idClasse_ensembleIdOeuvre (Map<Integer, Set<Integer>> map_idClasse_ensembleIdDescripteur, Map<Integer, Set<Integer>> map_idDescripteur_ensembleIdOeuvre)
    {
        System.out.println("getMap_idClasse_ensembleIdOeuvre");
        Map<Integer, Set<Integer>> map_idClasse_ensembleIdOeuvre = new HashMap<>(map_idClasse_ensembleIdDescripteur.size());
        for (int idClasse: map_idClasse_ensembleIdDescripteur.keySet())
        {
            map_idClasse_ensembleIdOeuvre.put(idClasse, new TreeSet<Integer>());
            for (int idDescripteur: map_idClasse_ensembleIdDescripteur.get(idClasse))
            {
                map_idClasse_ensembleIdOeuvre.get(idClasse).addAll(map_idDescripteur_ensembleIdOeuvre.get(idDescripteur));
            }
        }
        return map_idClasse_ensembleIdOeuvre;
    }
    
    public static Map<Integer, Integer> getMap_idClasse_nbreInstanciationTokens(Collection<List<Integer>> listesIdDescripteurs, Map<Integer, Integer> map_idDescripteur_idClasse)
    {
        System.out.println("getMap_idClasse_nbreToken");
        Map<Integer, Integer> map_idClasse_nbreToken = new TreeMap<>();
        // intialisation
        for (int idClasse: new TreeSet<>(map_idDescripteur_idClasse.values()))
            map_idClasse_nbreToken.put(idClasse, 0);
        
        for (List<Integer> liste: listesIdDescripteurs)
        {
            for (int idToken: liste)
            {
                int idClasse = map_idDescripteur_idClasse.get(idToken);
                int n=map_idClasse_nbreToken.get(idClasse)+1;
                map_idClasse_nbreToken.put(idClasse, n);
            }
        }
        return  map_idClasse_nbreToken;       
    }
    
    public static Map<Integer, Integer> getMap_idOeuvre_date(Collection<Oeuvre> oeuvres)
    {
        System.out.println("getMap_idOeuvre_date");
        Map<Integer, Integer> map = new TreeMap<>();
        for (Oeuvre o: oeuvres)
        {
            map.put(o.getId(), o.getDate());
        }
        return map;
    }
    
    public static Map<Integer, String> getMap_idOeuvre_titre (Collection<Oeuvre> oeuvres)
    {
        System.out.println("getMap_idOeuvre_titre");
        Map<Integer, String> map = new HashMap<>(oeuvres.size());
        for (Oeuvre o: oeuvres)
        {
            map.put(o.getId(), o.getTitre());
        }
        return map;
    }
    
    public static Map<Integer, Set<Integer>> getMap_date_ensembleIdOeuvre(Collection<Oeuvre> oeuvres)
    {
        System.out.println("getMap_date_ensembleIdOeuvre");
        Map<Integer, Set<Integer>> map = new TreeMap<>();
        for (Oeuvre o: oeuvres)
        {
            if (!map.containsKey(o.getDate()))
            {
                map.put(o.getDate(), new TreeSet<Integer>());
            }
            map.get(o.getDate()).add(o.getId());
        }
        
        return map;
    }
    
    public static Map<Integer, Integer> getMap_date_nbreOeuvre(Collection<Oeuvre> oeuvres)
    {
        System.out.println("getMap_date_nbreOeuvre");
        Map<Integer, Set<Integer>> map = getMap_date_ensembleIdOeuvre(oeuvres);
        Map<Integer, Integer> map2 = new HashMap<Integer, Integer>(map.size());
        for (int i : map.keySet())
            map2.put(i, map.get(i).size());
        return map2;
    }
    
    public static Map<Integer, Set<String>> getMap_date_ensembleDescripteur(Collection<Oeuvre> oeuvres)
    {
        System.out.println("getMap_date_ensembleIdOeuvre");
        Map<Integer, Set<Integer>> map_date_idOeuvres = getMap_date_ensembleIdOeuvre(oeuvres);
        Map<Integer, Set<String>> map_date_descripteurs = new TreeMap<>();
        // ini
        for (int date: map_date_idOeuvres.keySet())
        {
            map_date_descripteurs.put(date, new TreeSet<>());
        }
        for (Oeuvre o: oeuvres)
        {
            for (String desc: o.getListeDescripteur())
            {
                map_date_descripteurs.get(o.getDate()).add(desc);
            }
        }
        return map_date_descripteurs;
    }
    
    public static Map<Integer, Set<Integer>> getMap_date_ensembleIdDescripteur(Collection<Oeuvre> oeuvres, Map<String, Integer> map_descripteur_id)
    {
        System.out.println("getMap_date_ensembleIdOeuvre");
        Map<Integer, Set<Integer>> map_date_idOeuvres = getMap_date_ensembleIdOeuvre(oeuvres);
        Map<Integer, Set<Integer>> map_date_idDescripteurs = new TreeMap<>();
        // ini
        for (int date: map_date_idOeuvres.keySet())
        {
            map_date_idDescripteurs.put(date, new TreeSet<>());
        }
        for (Oeuvre o: oeuvres)
        {
            for (String desc: o.getListeDescripteur())
            {
                map_date_idDescripteurs.get(o.getDate()).add(map_descripteur_id.get(desc));
            }
        }
        return map_date_idDescripteurs;
    }
    
    public static Map<Integer, Collection<Integer>> getMap_date_collectionIdDescripteur(Collection<Oeuvre> oeuvres, Map<String, Integer> map_descripteur_id)
    {
        System.out.println("getMap_date_collectionIdDescripteur");
        Map<Integer, Set<Integer>> map_date_idOeuvres = getMap_date_ensembleIdOeuvre(oeuvres);
        Map<Integer, Collection<Integer>> map_date_idDescripteurs = new TreeMap<>();
        // ini
        for (int date: map_date_idOeuvres.keySet())
        {
            map_date_idDescripteurs.put(date, new ArrayList<>());
        }
        for (Oeuvre o: oeuvres)
        {
            for (String desc: o.getListeDescripteur())
            {
                map_date_idDescripteurs.get(o.getDate()).add(map_descripteur_id.get(desc));
            }
        }
        return map_date_idDescripteurs;
    }
    
    public static Map<Integer, Classe> getMap_idDate_classeIdOeuvre (Collection<Oeuvre> oeuvres)
    {
        System.out.println("getMap_idDate_ensembleIdOeuvre");
        
        Map<Integer, Set<Integer>> map = getMap_date_ensembleIdOeuvre(oeuvres);        
        Map<Integer, Classe> map_idDate_classe = new TreeMap<>();
        for (int date: map.keySet())
        {
            map_idDate_classe.put(date, new Classe(date, map.get(date)));
        }
        return map_idDate_classe;
    }
    
    public static Map<Integer, VecteurIndicie> additionnerEnsembleVecteurDescripteur(Map<Integer, Set<Integer>> map_idVariable_ensembleIdDescripteurs, Map<Integer, VecteurIndicie> vecteursDescripteur)
    {
        System.out.println("additionnerEnsembleVecteurDescripteur");
        Map<Integer, VecteurIndicie> vecteursCummule = new HashMap<>(map_idVariable_ensembleIdDescripteurs.size());
        for (int idVariable: map_idVariable_ensembleIdDescripteurs.keySet())
        {
            VecteurIndicie vecteurCumul = new VecteurIndicie(idVariable, new TreeMap<>());
            for (int idDesc: map_idVariable_ensembleIdDescripteurs.get(idVariable))
            {
                vecteurCumul.additionnerVecteur(vecteursDescripteur.get(idDesc).getMap_Id_Ponderation());
            }
            vecteursCummule.put(idVariable, vecteurCumul);
        }
        return vecteursCummule;
    }
    
    public static Map<Integer, VecteurIndicie> additionnerCollectionVecteurDescripteur(Map<Integer, Collection<Integer>> map_idVariable_listeIdDescripteurs, Map<Integer, VecteurIndicie> vecteursDescripteur)
    {
        System.out.println("additionnerListeVecteurDescripteur");
        Map<Integer, VecteurIndicie> vecteursCummule = new HashMap<>(map_idVariable_listeIdDescripteurs.size());
        for (int idVariable: map_idVariable_listeIdDescripteurs.keySet())
        {
            VecteurIndicie vecteurCumul = new VecteurIndicie(idVariable, new TreeMap<>());
            for (int idDesc: map_idVariable_listeIdDescripteurs.get(idVariable))
            {
                vecteurCumul.additionnerVecteur(vecteursDescripteur.get(idDesc).getMap_Id_Ponderation());
            }
            vecteursCummule.put(idVariable, vecteurCumul);
        }
        return vecteursCummule;
    }
    
    public static File getFileCorpus() {
        return fileCorpus;
    }
    
}
