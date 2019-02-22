/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte.Analyses;

import Affichage.Tableau.AfficherTableau;
import Affichage.Tableau.AfficheurTableau;
import Affichage.Tableau.AfficheurVecteur;
import Matrice.Vectorisateur.VecteurCreux;
import Matrice.Vectorisateur.VecteurIndicie;
import Matrice.Vectorisateur.VectoriseurUtils;
import Metrique.Similitude.MetriqueIndicieSim;
import Metrique.Similitude.MetriqueSim;
import Metrique.Similitude.ProduitScalaire;
import Metrique.Similitude.ProduitScalaireIndicie;
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
import java.util.Set;
import java.util.TreeMap;
import magritte.Oeuvre;
import magritte.OeuvresUtils;
import magritte.ParserMagritte;
import magritte.Pretraitement;

/**
 *
 * @author JF Chartier
 */
public class SimilariteEntreDescripteurs 
{
    private static Map<String, Integer> map_descripteur_id;
    
    public static void main(String[] args) throws IOException 
    {
//      SimilariteEntreDescripteurs.calculerSimEntreDesc(OeuvresUtils.getFileCorpus(), new ProduitScalaireIndicie(),new InformationMutuelle());
      SimilariteEntreDescripteurs.calculerSimPlusforte(OeuvresUtils.getFileCorpus(), new ProduitScalaireIndicie(),new InformationMutuelle());
    }
    
    
    public static Map<Integer, VecteurIndicie> calculerSimEntreDesc (File fileOeuvre, MetriqueIndicieSim metrique, CoefficientAssociation coefficient) throws IOException
    {
        Collection<Oeuvre> oeuvres = ParserMagritte.recupererOeuvre(fileOeuvre);
        Pretraitement pre = new Pretraitement(false, false, false, false, -1, 1, 999999, true, true);
        oeuvres = pre.filtrerOeuvres(oeuvres);
        map_descripteur_id = OeuvresUtils.getMap_descripteur_id(oeuvres);
        new AfficherTableau("Descripteurs de Magritte", "descripteur", "ID", map_descripteur_id).setVisible(true);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurIndicie> vecteurDesc = VectoriseurUtils.vectoriserAvecCoefficientAssociation4(map_idDesc_idOeuvres, OeuvresUtils.getIdOeuvres(oeuvres), coefficient, false);
        vecteurDesc = VectoriseurUtils.normerVecteurIndicie(vecteurDesc);
        vecteurDesc = AnalyseSimilitude.AnalyseSimilitude.calculerMetriqueEntreVecteurs(vecteurDesc.values(), vecteurDesc.values(), metrique, false);
                
//        AfficheurVecteur.afficherVecteurs("sim des descripteurs avec les descripteurs", TrieUtils.inverserMap(map_descripteur_id), TrieUtils.inverserMap(map_descripteur_id), vecteurDesc.values());
        return vecteurDesc;
    }
    
    public static Map<String, Double> calculerSimPlusforte (File fileOeuvre, MetriqueIndicieSim metrique, CoefficientAssociation coefficient) throws IOException
    {
        Map<Integer, VecteurIndicie> vecteur = calculerSimEntreDesc(fileOeuvre, metrique, coefficient);
        TreeMap<String, Double> map_desDes_sim = new TreeMap();
        List<String> descripteurs = new ArrayList<>(map_descripteur_id.keySet());
        for (int i=0;i<descripteurs.size();i++)
        {
            String d1 = descripteurs.get(i);
            int id1 = map_descripteur_id.get(descripteurs.get(i));
//            TreeMap<String, Double> map_des_sim = new TreeMap<>();
            for (int j = i+1; j<descripteurs.size();j++)
            {
                String d2 = descripteurs.get(j);
                int id2 = map_descripteur_id.get(descripteurs.get(j));
                double sim = vecteur.get(id1).getMap_Id_Ponderation().get(id2);
                map_desDes_sim.put(d1.concat(d2), sim);
            }
        }
        AfficheurTableau.afficher2Colonnes("sim entre deux descripteur", "deux descripteurs", "sim", map_desDes_sim);
        return map_desDes_sim;
    }
    
    
    
    
    
}
