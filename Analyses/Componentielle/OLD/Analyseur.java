/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte.Analyses.Componentielle.OLD;

import Matrice.Vectorisateur.VecteurCreux;
import Matrice.Vectorisateur.VectoriseurUtils;
import Metrique.Similitude.MetriqueSim;
import Metrique.Similitude.ProduitScalaire;
import Metrique.Similitude.ProduitScalaireParal;
import Ponderation.CoefficientAssociation;
import Ponderation.InformationMutuelle;
import UtilsJFC.TrieUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
import magritte.Oeuvre;
import magritte.OeuvresUtils;
import magritte.ParserMagritte;
import magritte.Pretraitement;

/**
 *
 * @author JF Chartier
 */
public class Analyseur 
{
//    private Map<Integer, String> map_id_desc;
    
    protected class Association 
    {
        int idToken;
        int idSousToken1;
        int idSousToken2;
        double sim;

        public Association(int idToken, int idSousToken1, int idSousToken2, double similitude) {
            this.idToken = idToken;
            this.idSousToken1 = idSousToken1;
            this.idSousToken2 = idSousToken2;
            this.sim = similitude;
        }

        public double getSim() {
            return sim;
        }

        public int getIdToken() {
            return idToken;
        }

        public int getIdSousToken1() {
            return idSousToken1;
        }

        public int getIdSousToken2() {
            return idSousToken2;
        }
    }
    
    public static void main(String[] args) throws IOException 
    {
//        new Analyseur().analyses(OeuvresUtils.getFileCorpus(), new ProduitScalaire(), new InformationMutuelle());
        new Analyseur3(0.2).analyses(OeuvresUtils.getFileCorpus(), new ProduitScalaire(), new InformationMutuelle());
    }
    
    public void analyses (File fileOeuvre, MetriqueSim metrique, CoefficientAssociation coefficient) throws IOException
    {
        System.out.println("analyse componentielle");
        Collection<Oeuvre> oeuvres = ParserMagritte.recupererOeuvre(fileOeuvre);
        Pretraitement pre = new Pretraitement(false, false, false, false, -1, 3, 999999, true, true);
        oeuvres = pre.filtrerOeuvres(oeuvres);
//        oeuvres = Pretraitement.filtrerOeuvreSelonCible(oeuvres, "<femme>");
        Map<String, Integer> map_descripteur_id = OeuvresUtils.getMap_descripteur_id(oeuvres);
        Map<Integer, String> map_id_token = TrieUtils.inverserMap(map_descripteur_id);
        Map<Integer, Set<Integer>> map_idDesc_idsOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vv = VectoriseurUtils.vectoriserAvecCoefficientAssociation4(map_idDesc_idsOeuvres, OeuvresUtils.getIdOeuvres(oeuvres), coefficient, true);
        
        List<Association> resultats = analyses(VectoriseurUtils.vectoriserAvecCoefficientAssociation5(map_idDesc_idsOeuvres, map_idDesc_idsOeuvres, OeuvresUtils.getIdOeuvres(oeuvres), coefficient, 0.0), metrique);
        for (Association a: resultats)
        {
            System.out.printf("%-30.30s %-30.30s %-30.30s %-30.30s\n", map_id_token.get(a.getIdToken()), map_id_token.get(a.getIdSousToken1()), map_id_token.get(a.getIdSousToken2()), a.getSim());
        }
    }
    
    public List<Association> analyses (Map<Integer, double[]> vecteurs, MetriqueSim metrique)
    {
//        List<Association> resultats = new ArrayList<>();
//        for (int i: vecteurs.keySet())
//            resultats.add(analyse(i, vecteurs, metrique));
//        return resultats;
        
        return vecteurs.keySet().parallelStream().map(v->analyse(v, vecteurs, metrique)).collect(Collectors.toList());

    }
    
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
