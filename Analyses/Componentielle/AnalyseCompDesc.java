/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte.Analyses.Componentielle;

import Affichage.Tableau.AfficherTableau;
import Affichage.Tableau.AfficheurTableau;
import ClassificationAutomatique.Supervisee.KPPV.KPlusProcheVoisin;
import Matrice.VecteurCreux.MatriceCreuse;
import Matrice.Vectorisateur.VecteurCreux;
import Matrice.Vectorisateur.VectoriseurUtils;
import Metrique.Similitude.MetriqueSim;
import Metrique.Similitude.ProduitScalaire;
import Ponderation.CoefficientAssociation;
import Ponderation.InformationMutuelle;
import UtilsJFC.TrieUtils;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import magritte.Oeuvre;
import magritte.OeuvresUtils;
import magritte.ParserMagritte;
import magritte.Pretraitement;

/**
 *
 * @author JF Chartier
 */
public class AnalyseCompDesc 
{
    public static void main(String[] args) throws IOException 
    {
        new AnalyseCompDesc().analyse(OeuvresUtils.getFileCorpus(), new ProduitScalaire(), new InformationMutuelle(), 0.95);
    }
    
    public Map<Integer, Map<Integer, Double>> analyse(File fileOeuvre, MetriqueSim metrique, CoefficientAssociation coefficient, double minSim) throws IOException
    {
        Collection<Oeuvre> oeuvres = ParserMagritte.recupererOeuvre(fileOeuvre);
        Pretraitement pre = new Pretraitement(false, false, false, false, -1, 2, 9999999, true, true);
        oeuvres = pre.filtrerOeuvres(oeuvres);
//        oeuvres = Pretraitement.filtrerOeuvreSelonCible(oeuvres, "<femme>");
        Map<String, Integer> map_descripteur_id = OeuvresUtils.getMap_descripteur_id(oeuvres);
        new AfficherTableau("Frequence des descripters", "descripteur", "nbre oeuvreID", OeuvresUtils.getMap_descripteur_freqOeuvre(oeuvres)).setVisible(true);
        
        new AfficherTableau("Descripteurs de Magritte", "descripteur", "ID", map_descripteur_id).setVisible(true);
//        TreeMap<Integer, Collection<String>> map_date_tokens = new TreeMap<>(OeuvresUtils.getMap_date_ensembleDescripteur(oeuvres));
        
//        AfficheurTableau.afficher2Colonnes("Accroissement vocabulaire", "date", "vocabulaire centre et reduit", AccroissementVocabulaireVoSurVt.calculerAccroissementVocabulaire(map_date_tokens));
        
        Map<Integer, Set<Integer>> map_idDes_idO = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteursDescripteur = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDes_idO, map_idDes_idO, OeuvresUtils.getIdOeuvres(oeuvres), coefficient, 0.0);
        vecteursDescripteur = VectoriseurUtils.normerVecteurCreuxB(vecteursDescripteur.values());
        Map<Integer, String> map_id_desc = TrieUtils.inverserMap(map_descripteur_id);
        Decomposeur6 decom = new Decomposeur6();
        Map<Integer, Map<Integer, Double>> map_idDescripteur_composition = decom.decomposerVecteurs(MatriceCreuse.getArrays(vecteursDescripteur.values()), metrique, minSim);
        AfficheurTableau.afficheurStructureImbriquee(map_idDescripteur_composition, map_id_desc, map_id_desc);
        
        System.out.println("id,descripteur,composition");
        for (int i: map_idDescripteur_composition.keySet())
        {
            System.out.println(i+","+map_id_desc.get(i)+","+map_idDescripteur_composition.get(i).size());
        }
        
        Map<Integer, Double> map_id_recomp = reconstruireDescripteur(decom.getMap_id_residu(), map_idDescripteur_composition, vecteursDescripteur, metrique);
        System.out.println("id,descripteur,cosineRecomposition");
        for (Entry<Integer,Double> kv: map_id_recomp.entrySet())
        {
            System.out.println(kv.getKey()+","+map_id_desc.get(kv.getKey())+","+kv.getValue());
        }
        
        return  map_idDescripteur_composition;       
    }
    
////    @Override
////    public Map<Integer, Map<Integer,Double>> decomposerVecteurs (Map<Integer, double[]> vecteursCibles, Map<Integer, double[]> sousVecteurs, MetriqueSim metrique, double minSim)
////    {
////        System.out.println("decomposer plusieurs vecteurs");
////        Map<Integer, Map<Integer,Double>> resultats = new TreeMap<>();
////        for (int id: vecteursCibles.keySet())
////        {
////            Map<Integer, double[]> sousVecteursMoinsCible = sousVecteurs.entrySet().stream().filter(p->p.getKey()!=id).collect(Collectors.toMap(p->p.getKey(), p->p.getValue()));
////            System.out.println("decomposer vecteur: "+ id);
////            Map<Integer,Double> map_id_sim = decomposerVecteur(id, vecteursCibles.get(id), sousVecteursMoinsCible, metrique, minSim);
////            resultats.put(id, map_id_sim);
////        }
////        return resultats;        
////    }
    
    public Map<Integer, Double> reconstruireDescripteur (Map<Integer, double[]> map_id_residu, Map<Integer, Map<Integer, Double>> map_idDescripteur_composition, Map<Integer, VecteurCreux> vecteursDescripteur, MetriqueSim metrique)
    {
        System.out.println("reconstruireDescripteur");
        Map<Integer, Double> map_idDesc_sim = new TreeMap<>();
        
        for (int id: map_idDescripteur_composition.keySet())
        {
            double[]v=new double[map_id_residu.get(id).length];
            Arrays.fill(v,0.0);
            
//            double[]v=map_id_residu.get(id);
//            double[]v=null;
            for (Entry<Integer, Double> kv: map_idDescripteur_composition.get(id).entrySet())
            {
                double[]x=vecteursDescripteur.get(kv.getKey()).getTabPonderation();
//                x=VecteurCreux.complementOrthogonaux(x, lastVector);
//                lastVector=vecteursDescripteur.get(kv.getKey()).getTabPonderation();
                x=VecteurCreux.multiplierParUnScalaire(x, kv.getValue());
                if (v==null)
                    v=x;
                else
                {
                    v=VecteurCreux.additionnerVecteurs(v, x);
//                    v=VecteurCreux.additionnerComplementOrthogonaux(v, x);
//                    v=VecteurCreux.normer(v);
                }
            }
            v=VecteurCreux.normer(v);
            double sim = metrique.calculerMetrique(v, vecteursDescripteur.get(id).getTabPonderation());
            map_idDesc_sim.put(id, sim);
        }
        return map_idDesc_sim;
    }
    
    
}
