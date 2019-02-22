/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte.Analyses.Componentielle.OLD;

import ClassificationAutomatique.Supervisee.KPPV.KPlusProcheVoisin;
import Matrice.Vectorisateur.VecteurCreux;
import Metrique.Similitude.MetriqueSim;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 *
 * @author JF Chartier
 */
public class Decomposeur4 
{
    
    private Map<Integer, double[]> map_id_residu = new TreeMap<>();
    
    public Map<Integer, Map<Integer,Double>> decomposerVecteurs (Map<Integer, double[]> vecteursCibles, Map<Integer, double[]> sousVecteurs, MetriqueSim metrique, double minSim)
    {
        System.out.println("decomposer plusieurs vecteurs");
        Map<Integer, Map<Integer,Double>> resultats = new HashMap<>();
        for (int id: vecteursCibles.keySet())
        {
            // fouiller parmi les vecteurs different de la cible 
            Map<Integer, double[]> sousVecteursMoinsCible = sousVecteurs.entrySet().stream().filter(p->p.getKey()!=id).collect(Collectors.toMap(p->p.getKey(), p->p.getValue()));
            System.out.println("decomposer vecteur: "+ id);
            Map<Integer,Double> map_id_sim = decomposerVecteur(id, vecteursCibles.get(id), sousVecteursMoinsCible, metrique, minSim);
            resultats.put(id, map_id_sim);
        }
        return resultats;        
    }
    
    
    
    public Map<Integer, Double> decomposerVecteur (int id, double[] cible, Map<Integer, double[]> sousVecteurs, MetriqueSim metrique, double minSim)
    {
        System.out.println("decomposer un vecteur");
        Map<Integer, double[]> copieSousVec = new HashMap<>(sousVecteurs);
        Map<Integer, Double> map_idVecteur_sim = new HashMap<>();
        double[]soustracteurPrecedent = new double[cible.length];
        Arrays.fill(soustracteurPrecedent, 0.0);
        
        PriorityQueue<Map.Entry<Integer, Double>> pq;// = KPlusProcheVoisin.getKVecteursPlusSimilairesDeX(copieSousVec, cible, 1, metrique);
        double[] residu=cible;// = VecteurCreux.complementOrthogonaux(cible, copieSousVec.get(pq.peek().getKey()));
        
//        pq = KPlusProcheVoisin.getKVecteursPlusSimilairesDeX(copieSousVec, residu, 1, metrique);
//        residu = VecteurCreux.complementOrthogonaux(residu, copieSousVec.get(pq.peek().getKey()));
//        residu = VecteurCreux.normer(residu);
//        double sim=pq.peek().getValue();  
        
        double simResidu = 1.0;
//        double simResidu = metrique.calculerMetrique(residu, cible);
//        map_idVecteur_sim.put(pq.peek().getKey(), sim);
        int i=copieSousVec.size();
        
//        while (simResidu>minSim && i>1&& sim>0.1)
        pq = KPlusProcheVoisin.getKVecteursPlusSimilairesDeX(copieSousVec, residu, 1, metrique);
        double sim=pq.peek().getValue();
        while (sim>minSim)
        {
//            pq = KPlusProcheVoisin.getKVecteursPlusSimilairesDeX(copieSousVec, residu, 1, metrique);
                        
            while(sim>0.95) // afin d'eviter de recuperer les equivalences parfaites (e.g. synonymes) et le complement d'orthogaunaux sur deux vecteurs tres similaire ne fonctionne pas 
            {
                System.out.println("*");
                copieSousVec.remove(pq.peek().getKey());
                pq = KPlusProcheVoisin.getKVecteursPlusSimilairesDeX(copieSousVec, residu, 1, metrique);
                sim=pq.peek().getValue();
            }
//            if (pq.peek().getValue()>minSim)
//            {
                double[] soustracteur = copieSousVec.get(pq.peek().getKey());
                soustracteur = VecteurCreux.soustraireComplementOrthogonaux(soustracteur, soustracteurPrecedent);
                soustracteur = VecteurCreux.normer(soustracteur);
                
                double s = metrique.calculerMetrique(cible, copieSousVec.get(pq.peek().getKey()));
                map_idVecteur_sim.put(pq.peek().getKey(), s);
                
                residu = VecteurCreux.soustraireComplementOrthogonaux(residu, soustracteur);
                residu = VecteurCreux.normer(residu);
                soustracteurPrecedent = soustracteur;//copieSousVec.get(pq.peek().getKey());
                copieSousVec.remove(pq.peek().getKey());
                pq = KPlusProcheVoisin.getKVecteursPlusSimilairesDeX(copieSousVec, residu, 1, metrique);
                simResidu=metrique.calculerMetrique(residu, cible);
                sim=pq.peek().getValue();
//                sim = metrique.calculerMetrique(cible, copieSousVec.get(pq.peek().getKey()));
                
                
//                pq = KPlusProcheVoisin.getKVecteursPlusSimilairesDeX(copieSousVec, residu, 1, metrique);
//                double[] newV = copieSousVec.get(pq.peek().getKey());
//                newV = VecteurCreux.complementOrthogonaux(newV, lastVector);
//                newV = VecteurCreux.normer(newV);
//    //            cibleDecomp = VecteurCreux.soustraireVecteur(cibleDecomp, newV);
//                residu=VecteurCreux.complementOrthogonaux(residu, newV);
//                residu = VecteurCreux.normer(residu);

                
    //            sim=pq.peek().getValue();
    //            map_idVecteur_sim.put(pq.peek().getKey(), sim);



    //            sim = metrique.calculerMetrique(cible, copieSousVec.get(pq.peek().getKey()));
                i=copieSousVec.size();
                System.out.println("nbre de descripteur restant: " + copieSousVec.size());
//            }
//            else
//                break;
            
            
            // a partir du deuxieme soustracteur et les suivants doivent etre orthogaunaux au pecendent
////            double[] newV = copieSousVec.get(pq.peek().getKey());
////            newV = VecteurCreux.complementOrthogonaux(newV, lastVector);
////            newV = VecteurCreux.normer(newV);
//////            
//////            System.out.println("sim: " + sim);
////            cibleDecomp=VecteurCreux.complementOrthogonaux(cibleDecomp, newV);
////            cibleDecomp = VecteurCreux.normer(cibleDecomp);
            
//            map_idVecteur_sim.put(pq.peek().getKey(), sim);
            
//            lastId=pq.peek().getKey();
        }
//        map_idVecteur_sim.put(pq.peek().getKey(), sim);
        map_id_residu.put(id, residu);
        return map_idVecteur_sim;
    }

    public Map<Integer, double[]> getMap_id_residu() {
        return map_id_residu;
    }
    
    
    
}
