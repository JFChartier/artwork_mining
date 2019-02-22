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
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

/**
 *
 * @author JF Chartier
 */
public class Decomposeur1 
{
    private Map<Integer, double[]> map_id_residu = new TreeMap<>();
    
    // on decompose un vecteur jusqu'a ce qu'on ne trouve plus de vecteur suffisamment similaire au vecteur decomposee
    public Map<Integer, Double> decomposerVecteur (int id, double[] cible, Map<Integer, double[]> sousVecteurs, MetriqueSim metrique, double minSim)
    {
        System.out.println("decomposer un vecteur");
        Map<Integer, double[]> copieSousVec = new TreeMap<>(sousVecteurs);
        Map<Integer, Double> map_idVecteur_sim = new TreeMap<>();
        PriorityQueue<Map.Entry<Integer, Double>> pq = KPlusProcheVoisin.getKVecteursPlusSimilairesDeX(copieSousVec, cible, 1, metrique);
        double sim=pq.peek().getValue();
        
        double[] cibleDecomp = VecteurCreux.soustraireComplementOrthogonaux(cible, copieSousVec.get(pq.peek().getKey()));
        cibleDecomp = VecteurCreux.normer(cibleDecomp);
        int lastId=pq.peek().getKey();     
        while (sim>minSim)
        {
            map_idVecteur_sim.put(pq.peek().getKey(),sim);
            double[] lastVector = copieSousVec.get(pq.peek().getKey());
            copieSousVec.remove(pq.peek().getKey());
            pq = KPlusProcheVoisin.getKVecteursPlusSimilairesDeX(copieSousVec, cibleDecomp, 1, metrique);
            double[] v = copieSousVec.get(pq.peek().getKey());
            // a partir du deuxieme soustracteur et les suivants doivent etre orthogaunaux au pecendent
            v = VecteurCreux.soustraireComplementOrthogonaux(v, copieSousVec.get(lastId));
            v = VecteurCreux.normer(v);
            cibleDecomp=VecteurCreux.soustraireComplementOrthogonaux(cibleDecomp, v);
            cibleDecomp = VecteurCreux.normer(cibleDecomp);
//            map_idVecteur_sim.put(pq.peek().getKey(), pq.peek().getValue());
            sim=pq.peek().getValue();
            lastId=pq.peek().getKey();
        }
        map_id_residu.put(id, cibleDecomp);
        return map_idVecteur_sim;
    }
    
    
    
    public Map<Integer, Map<Integer,Double>> decomposerVecteurs (Map<Integer, double[]> vecteursCibles, Map<Integer, double[]> sousVecteurs, MetriqueSim metrique, double minSim)
    {
        System.out.println("decomposer plusieurs vecteurs");
        Map<Integer, Map<Integer,Double>> resultats = new TreeMap<>();
        for (int id: vecteursCibles.keySet())
        {
            Map<Integer,Double> map_id_sim = decomposerVecteur(id, vecteursCibles.get(id), sousVecteurs, metrique, minSim);
            resultats.put(id, map_id_sim);
        }
        return resultats;        
    }

    public Map<Integer, double[]> getMap_id_residu() {
        return map_id_residu;
    }
    
    
}
