/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte.Analyses.Componentielle.OLD;

import ClassificationAutomatique.Supervisee.KPPV.KPlusProcheVoisin;
import Matrice.Vectorisateur.VecteurCreux;
import Metrique.Similitude.MetriqueSim;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 *
 * @author JF Chartier
 */
public class Decomposeur3 extends Decomposeur2
{
    
    // decompose chaque vecteur
    @Override
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
    
    @Override
    public Map<Integer, Double> decomposerVecteur (int id, double[] cible, Map<Integer, double[]> sousVecteurs, MetriqueSim metrique, double minSim)
    {
        System.out.println("decomposer un vecteur");
        Map<Integer, double[]> copieSousVec = new TreeMap<>(sousVecteurs);
        Map<Integer, Double> map_idVecteur_sim = new TreeMap<>();
        PriorityQueue<Map.Entry<Integer, Double>> pq = KPlusProcheVoisin.getKVecteursPlusSimilairesDeX(copieSousVec, cible, 1, metrique);
        double sim=pq.peek().getValue();
//        map_idVecteur_sim.put(pq.peek().getKey(), sim);
        double[] cibleDecomp = VecteurCreux.soustraireComplementOrthogonaux(cible, copieSousVec.get(pq.peek().getKey()));
        cibleDecomp = VecteurCreux.normer(cibleDecomp);
//        int lastId=pq.peek().getKey();   
        
        while (sim>minSim)
        {
            
            map_idVecteur_sim.put(pq.peek().getKey(), sim);
            double[] lastVector = copieSousVec.get(pq.peek().getKey());
            copieSousVec.remove(pq.peek().getKey());
            pq = KPlusProcheVoisin.getKVecteursPlusSimilairesDeX(copieSousVec, cibleDecomp, 1, metrique);
            
//            sim=metrique.calculerMetrique(newV, cible);
            sim=pq.peek().getValue();
            System.out.println("nbre de descripteur restant: " + copieSousVec.size());
            
            // a partir du deuxieme soustracteur et les suivants doivent etre orthogaunaux au pecendent
            double[] newV = copieSousVec.get(pq.peek().getKey());
            newV = VecteurCreux.soustraireComplementOrthogonaux(newV, lastVector);
            newV = VecteurCreux.normer(newV);
//            
//            System.out.println("sim: " + sim);
            cibleDecomp=VecteurCreux.soustraireComplementOrthogonaux(cibleDecomp, newV);
            cibleDecomp = VecteurCreux.normer(cibleDecomp);
            
//            map_idVecteur_sim.put(pq.peek().getKey(), sim);
            
//            lastId=pq.peek().getKey();
        }
        super.getMap_id_residu().put(id, cibleDecomp);
        return map_idVecteur_sim;
    }

    
    
    
    
}
