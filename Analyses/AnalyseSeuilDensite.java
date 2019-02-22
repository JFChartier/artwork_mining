/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte.Analyses;

import Affichage.Tableau.AfficheurTableau;
import AnalyseSimilitude.AnalyseSimilitude;
import Matrice.Vectorisateur.VecteurIndicie;
import Metrique.Distance.DistanceCosinusIndicie;
import Metrique.Distance.MetriqueIndicieDist;
import Metrique.Similitude.MetriqueIndicieSim;
import UtilsJFC.TirageAleatoire;
import UtilsJFC.TrieUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

/**
 *
 * @author JF Chartier
 */
public class AnalyseSeuilDensite 
{
    
    public static Map<Integer, Double> calculerSeuilDensite (Map<Integer, VecteurIndicie> map_id_vecteurDist, int kePlusProche, Map<Integer, VecteurIndicie> vecteurs)
    {
        System.out.println("calculerSeuilDensite");
        Map<Integer, Double> map_idVecteur_simkrang = new HashMap<>(vecteurs.size());
        for (VecteurIndicie v : map_id_vecteurDist.values())
        {
            Entry<Integer,Double> e = TrieUtils.trieMapCroissantSelonValeur(v.getMap_Id_Ponderation()).get(kePlusProche-1);
            map_idVecteur_simkrang.put(v.getId(), e.getValue());
        }
        AfficheurTableau.afficher2Colonnes("distance du ie vecteur le plus loin", "id Descripteur", "distance", map_idVecteur_simkrang);
//        calculerDensiteNull(kePlusProche, map_id_vecteurDist, 100);
        return map_idVecteur_simkrang;
    }
    
    public static double calculerDensiteNull (int k, Map<Integer, VecteurIndicie> map_id_vecteurDist, int tirage)
    {
        System.out.println("calculerDensiteNull");
//        Map<Integer, Double> map_idVecteur_distanceMaxMoyenne = new HashMap<>(map_id_vecteurDist.size());
        List<Integer> ids = new ArrayList<>(map_id_vecteurDist.keySet());
        Collections.shuffle(ids);
        List<Integer> echantillon = ids.subList(0, tirage-1);
        double somme = 0;
        double[] resultats = new double[tirage];
        StandardDeviation std = new StandardDeviation();
////        for (int id: echantillon)
        for (int j = 0; j < echantillon.size(); j++)
        {
            int id = echantillon.get(j);
            Collections.shuffle(ids);
            int indexDeId = ids.indexOf(id);
            Collections.swap(ids, indexDeId, ids.size()-1);
            double distMax = 0;
            for (int i = 0; i < k; i++)
            {
                double dist = map_id_vecteurDist.get(id).getMap_Id_Ponderation().get(ids.get(i));
                if (dist > distMax)
                    distMax = dist;
            }
            resultats[j]=distMax;
            somme+=distMax;
        }
        double moyenne = somme/(double)tirage;
        double ecartType = std.evaluate(resultats);
                
        double erreurType = Math.sqrt(1+(1/(double)tirage))*ecartType;
        System.out.println("Distance maximale moyenne entre k vecteurs tires aleatoirement: " + moyenne);
        System.out.println("Erreur Type de la distance maximale entre k vecteurs tires aleatoirement: " + erreurType);
        
        return moyenne;
    }
           
        
//    ne fonctionne pas
//    public static Map<Integer, Double> calculerDensiteNull (int k, Map<Integer, VecteurIndicie> map_id_vecteurDist, int tirage)
//    {
//        System.out.println("calculerDensiteNull");
//        Map<Integer, Double> map_idVecteur_distanceMaxMoyenne = new HashMap<>(map_id_vecteurDist.size());
//        List<Integer> ids = new ArrayList<>(map_id_vecteurDist.keySet());
//        for (VecteurIndicie v : map_id_vecteurDist.values())
//        {
//            double somme = 0;
//            for (int i = 0; i < tirage; i++)
//            {
//                Collections.shuffle(ids);
//                List<Integer> L = ids.subList(0, k);
//                double distMax = 0;
//                // trouver la distance max parmi les k vecteur tire de maniere aleatoire
//                for (int l: L)
//                {
//                    if (v.getMap_Id_Ponderation().get(l) > distMax)
//                        distMax = v.getMap_Id_Ponderation().get(l);
//                }
//                somme+=distMax;
//            }
//            double moyenne = somme/(double)tirage;
//            map_idVecteur_distanceMaxMoyenne.put(v.getId(), moyenne);
//        }
//        return  map_idVecteur_distanceMaxMoyenne;
//    }
    
}
