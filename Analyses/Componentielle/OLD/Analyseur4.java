/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte.Analyses.Componentielle.OLD;

import Matrice.Vectorisateur.VecteurCreux;
import Metrique.Similitude.MetriqueSim;
import java.util.List;
import java.util.Map;

/**
 *
 * @author JF Chartier
 */
public class Analyseur4 
{
    private double seuilIndependance;
    private double seuilEquivalence;

    public Analyseur4(double seuilIndependance, double seuilEquivalence) {
        this.seuilIndependance = seuilIndependance;
        this.seuilEquivalence = seuilEquivalence;
    }
    
    
    
    
    protected class Association 
    {
        List<Integer> idsSousToken;
        double sim;

        public Association(List<Integer> idsSousToken, double sim) {
            this.idsSousToken = idsSousToken;
            this.sim = sim;
        }

        

        public double getSim() {
            return sim;
        }

        public List<Integer> getIdsSousToken() {
            return idsSousToken;
        }

        
    }
    
    
    public Analyseur.Association analyse (int idToken, Map<Integer, double[]> vecteurs, MetriqueSim metrique)
    {
        System.out.println("calcul du token : "+idToken);
        int idSeme1 =-1;
        int idSeme2=-1;
        double simMax = 0;
        
        for (int i: vecteurs.keySet())
        {
            if (i==idToken) continue;
            if (metrique.calculerMetrique(vecteurs.get(i), vecteurs.get(idToken))>seuilIndependance) continue;
            for (int j: vecteurs.keySet())
            {
                if (j==i) continue;
                if (j==idToken) continue;
                if (metrique.calculerMetrique(vecteurs.get(j), vecteurs.get(i))>seuilIndependance) continue;
                if (metrique.calculerMetrique(vecteurs.get(j), vecteurs.get(idToken))>seuilIndependance) continue;
                double[] vecCom = VecteurCreux.additionnerVecteurs(vecteurs.get(i), vecteurs.get(j));
//                System.out.println(Arrays.toString(vecCom));
                vecCom = VecteurCreux.normer(vecCom);
//                System.out.println(Arrays.toString(vecCom));
                double sim = metrique.calculerMetrique(vecteurs.get(idToken), vecCom);
//                System.out.println(sim);
                if (sim>simMax)
                {
                    idSeme1=i;
                    idSeme2=j;
                    simMax=sim;
                    
                }
            }
        }
        System.out.println(idToken+" ; "+ idSeme1 + " ; " + idSeme2 +" ; " + simMax);
        return new Analyseur.Association(idToken, idSeme1, idSeme2, simMax);
    }
}
