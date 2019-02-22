/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte.Analyses.Componentielle;

import ClassificationAutomatique.Supervisee.KPPV.KPlusProcheVoisin;
import Matrice.VecteurCreux.GramSchmidt;
import Matrice.Vectorisateur.VecteurCreux;
import Metrique.Similitude.MetriqueSim;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 *
 * @author JF Chartier
 */
public class Decomposeur6 
{
     private Map<Integer, double[]> map_id_residu = new TreeMap<>();
    
    // cette vecrion est utile lorsque les vecteurs a decomposer sont le meme que les composantes 
    public Map<Integer, Map<Integer,Double>> decomposerVecteurs (Map<Integer, double[]> vecteursCibles, MetriqueSim metrique, double minSim)
    {
        System.out.println("decomposer plusieurs vecteurs");
        Map<Integer, Map<Integer,Double>> resultats = new HashMap<>();
        for (int id: vecteursCibles.keySet())
        {
            // fouiller parmi les vecteurs different de la cible 
            Map<Integer, double[]> sousVecteursMoinsCible = vecteursCibles.entrySet().stream().filter(p->p.getKey()!=id).collect(Collectors.toMap(p->p.getKey(), p->p.getValue()));
            System.out.println("decomposer vecteur: "+ id);
            Map<Integer,Double> map_id_sim = decomposerVecteur(id, vecteursCibles.get(id), sousVecteursMoinsCible, metrique, minSim);
            resultats.put(id, map_id_sim);
        }
        return resultats;        
    }
    
    // cette version est pertinente lorsque les vecteurs a decomposer et les composantes sont differentes
    public Map<Integer, Map<Integer,Double>> decomposerVecteurs (Map<Integer, double[]> vecteursCibles, Map<Integer, double[]> composantes, MetriqueSim metrique, double minSim)
    {
        System.out.println("decomposer plusieurs vecteurs");
        Map<Integer, Map<Integer,Double>> resultats = new HashMap<>();
        for (int id: vecteursCibles.keySet())
        {
            System.out.println("decomposer vecteur: "+ id);
            Map<Integer,Double> map_id_sim = decomposerVecteur(id, vecteursCibles.get(id), composantes, metrique, minSim);
            resultats.put(id, map_id_sim);
        }
        return resultats;        
    }
    
    public Map<Integer, Double> decomposerVecteur (int id, double[] cible, Map<Integer, double[]> sousVecteurs, MetriqueSim metrique, double minSim)
    {
        System.out.println("decomposer un vecteur");
        Map<Integer, double[]> copieSousVec = new HashMap<>(sousVecteurs);
        Map<Integer, Double> map_idComposante_sim = new HashMap<>();
        
        PriorityQueue<Map.Entry<Integer, Double>> pq;// = KPlusProcheVoisin.getKVecteursPlusSimilairesDeX(copieSousVec, cible, 1, metrique);
//        double[] residu=cible;// = VecteurCreux.complementOrthogonaux(cible, copieSousVec.get(pq.peek().getKey()));
        GramSchmidt gs = new GramSchmidt();
        Collection<double[]> composantes = new ArrayList<>(); // vecteurs orthogonaux
        pq = KPlusProcheVoisin.getKVecteursPlusSimilairesDeX(copieSousVec, cible, 1, metrique);
        while(pq.peek().getValue()>0.99) // afin d'eviter de recuperer les equivalences parfaites (e.g. synonymes) et le complement d'orthogaunaux sur deux vecteurs tres similaire ne fonctionne pas 
        {
            System.out.println("*");
            copieSousVec.remove(pq.peek().getKey());
            pq = KPlusProcheVoisin.getKVecteursPlusSimilairesDeX(copieSousVec, cible, 1, metrique);
        }
        
        composantes.add(sousVecteurs.get(pq.peek().getKey()));
        double[] residuCible = gs.projectionOrthogonale(cible, composantes);
                
        double simResidu = metrique.calculerMetrique(residuCible, cible);
        System.out.println("simResidu : " + simResidu);

        while (simResidu<minSim)
        {

            double simAvecCible = metrique.calculerMetrique(cible, copieSousVec.get(pq.peek().getKey()));
//            map_idComposante_sim.put(pq.peek().getKey(), 1.0-simResidu);   
            map_idComposante_sim.put(pq.peek().getKey(), simAvecCible);  
            copieSousVec.remove(pq.peek().getKey());
            
            
            pq = KPlusProcheVoisin.getKVecteursPlusSimilairesDeX(copieSousVec, residuCible, 1, metrique);
            while(pq.peek().getValue()>0.99) // afin d'eviter de recuperer les equivalences parfaites (e.g. synonymes) et le complement d'orthogaunaux sur deux vecteurs tres similaire ne fonctionne pas 
            {
                System.out.println("*");
                copieSousVec.remove(pq.peek().getKey());
                pq = KPlusProcheVoisin.getKVecteursPlusSimilairesDeX(copieSousVec, residuCible, 1, metrique);
            }
            double[]composante=gs.projectionOrthogonale(copieSousVec.get(pq.peek().getKey()), composantes);
            composantes.add(composante);
            double[] newResiduCible = gs.projectionOrthogonale(cible, composantes);
            simResidu = metrique.calculerMetrique(residuCible, newResiduCible);
            System.out.println("simResidu : " + simResidu);
            residuCible=newResiduCible;
            
//            i=copieSousVec.size();
            System.out.println("nbre de descripteur restant: " + copieSousVec.size());
            
        }
//        map_idComposante_sim.put(pq.peek().getKey(), simResidu);
        map_id_residu.put(id, residuCible);
        return map_idComposante_sim;
    }

    public Map<Integer, double[]> getMap_id_residu() {
        return map_id_residu;
    }
    
}
