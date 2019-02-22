/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte.Analyses.Componentielle;

import ClassificationAutomatique.Supervisee.KPPV.KPlusProcheVoisin;
import Matrice.VecteurCreux.GramSchmidt;
import Metrique.Similitude.MetriqueSim;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 *
 * @author JF Chartier
 * 22-09-2017
 * developpe dans le cadre du projet Pierce avec Davide Pullizzotto
 */
public class DecomposerVecteur 
{
    private List<double[]> listeComposantes = new ArrayList<>();
    
    
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
            System.out.println("* vecteur trop proche");
            copieSousVec.remove(pq.peek().getKey());
            pq = KPlusProcheVoisin.getKVecteursPlusSimilairesDeX(copieSousVec, cible, 1, metrique);
        }
        
        composantes.add(sousVecteurs.get(pq.peek().getKey()));
        double[] residuCible = gs.projectionOrthogonale(cible, composantes);
               
        double simResidu = metrique.calculerMetrique(residuCible, cible);
        System.out.println("simResidu : " + simResidu);

        while (simResidu<minSim)
        {
            listeComposantes.add(residuCible); 
            double simAvecCible = metrique.calculerMetrique(cible, copieSousVec.get(pq.peek().getKey()));
//            map_idComposante_sim.put(pq.peek().getKey(), 1.0-simResidu);   
            map_idComposante_sim.put(pq.peek().getKey(), simAvecCible); 
            System.out.println("id composante : " + pq.peek().getKey());
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
        return map_idComposante_sim;
    }

    public List<double[]> getListeComposantes() {
        return listeComposantes;
    }
    
    
    
}
