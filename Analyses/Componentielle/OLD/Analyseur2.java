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
import java.util.stream.Collectors;

/**
 *
 * @author JF Chartier
 */
public class Analyseur2 extends Analyseur
{
    
    
        
    @Override
    public Association analyse (int idToken, Map<Integer, double[]> vecteurs, MetriqueSim metrique)
    {
        System.out.println("calcul du token : "+idToken);
        int idSeme1 =-1;
        int idSeme2=-1;
        double simMax = 0;
        
        for (int i: vecteurs.keySet())
        {
            if (i==idToken) continue;
            for (int j: vecteurs.keySet())
            {
                if (j==i) continue;
                if (j==idToken) continue;
                if (metrique.calculerMetrique(vecteurs.get(i), vecteurs.get(j))>0.1) continue;
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
        return new Association(idToken, idSeme1, idSeme2, simMax);
    }
    
}
