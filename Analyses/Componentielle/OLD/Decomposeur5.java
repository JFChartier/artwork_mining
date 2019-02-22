/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte.Analyses.Componentielle.OLD;

import ClassificationAutomatique.Supervisee.KPPV.KPlusProcheVoisin;
import Matrice.VecteurCreux.MatriceCreuse;
import Matrice.Vectorisateur.VecteurCreux;
import Matrice.Vectorisateur.VectoriseurUtils;
import Metrique.Similitude.MetriqueSim;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 *
 * @author JF Chartier
 */
public class Decomposeur5 
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
        double[] soustracteur = new double[cible.length];
        Arrays.fill(soustracteur, 0.0);
        
        PriorityQueue<Map.Entry<Integer, Double>> pq;// = KPlusProcheVoisin.getKVecteursPlusSimilairesDeX(copieSousVec, cible, 1, metrique);
        double[] residu=cible;// = VecteurCreux.complementOrthogonaux(cible, copieSousVec.get(pq.peek().getKey()));
        
        pq = KPlusProcheVoisin.getKVecteursPlusSimilairesDeX(copieSousVec, residu, 1, metrique);
        soustracteur = sousVecteurs.get(pq.peek().getKey());
        residu = VecteurCreux.soustraireVecteur(cible, soustracteur);
        residu = VecteurCreux.normer(residu);
        
//        sousVecteurs = MatriceCreuse.complementOrthogonaux(sousVecteurs, soustracteur);
//        sousVecteurs = VectoriseurUtils.normerVecteurCreuxC(sousVecteurs);
        
        double sim=pq.peek().getValue();  
        double simResidu = metrique.calculerMetrique(residu, cible);
        
//        map_idVecteur_sim.put(pq.peek().getKey(), sim);
        int i=copieSousVec.size();
        while (simResidu>minSim && sim>=0.05)
        {
//            if (pq.peek().getValue()>0.98) // afin d'eviter de recuperer les equivalences parfaites (e.g. synonymes) 
//            {
//                copieSousVec.remove(pq.peek().getKey());
//                pq = KPlusProcheVoisin.getKVecteursPlusSimilairesDeX(copieSousVec, residu, 1, metrique);
//                continue;
//            }
            map_idVecteur_sim.put(pq.peek().getKey(), sim);
            
            copieSousVec.remove(pq.peek().getKey());
            
            pq = KPlusProcheVoisin.getKVecteursPlusSimilairesDeX(copieSousVec, residu, 1, metrique);
            soustracteur = VecteurCreux.soustraireComplementOrthogonaux(copieSousVec.get(pq.peek().getKey()), soustracteur);
            soustracteur = VecteurCreux.normer(soustracteur);
            
//            sousVecteurs = MatriceCreuse.complementOrthogonaux(sousVecteurs, soustracteur);
//            sousVecteurs = VectoriseurUtils.normerVecteurCreuxC(sousVecteurs);
        
            
//            newV = VecteurCreux.complementOrthogonaux(newV, lastVector);
//            newV = VecteurCreux.normer(newV);
            residu = VecteurCreux.soustraireVecteur(residu, soustracteur);
//            residu=VecteurCreux.complementOrthogonaux(residu, newV);
            residu = VecteurCreux.normer(residu);
            
            simResidu = metrique.calculerMetrique(residu, cible);
            sim = metrique.calculerMetrique(cible, copieSousVec.get(pq.peek().getKey()));
//            sim=pq.peek().getValue();
//            map_idVecteur_sim.put(pq.peek().getKey(), sim);
            
            
            
//            sim = metrique.calculerMetrique(cible, copieSousVec.get(pq.peek().getKey()));
            i=copieSousVec.size();
            System.out.println("nbre de descripteur restant: " + copieSousVec.size());
            
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
        map_idVecteur_sim.put(pq.peek().getKey(), sim);
        map_id_residu.put(id, residu);
        return map_idVecteur_sim;
    }

    public Map<Integer, double[]> getMap_id_residu() {
        return map_id_residu;
    }
    
    
    
}
