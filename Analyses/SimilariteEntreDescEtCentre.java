/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte.Analyses;

import Affichage.Tableau.AfficherTableau;
import Affichage.Tableau.AfficheurTableau;
import Affichage.Tableau.AfficheurVecteur;
import ClassificationAutomatique.NonSupervisee.KMeans.ClassifieurKmeans;
import Matrice.Vectorisateur.VecteurCreux;
import Matrice.Vectorisateur.VecteurIndicie;
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
public class SimilariteEntreDescEtCentre 
{
    public static void main(String[] args) throws IOException 
    {
      SimilariteEntreDescEtCentre.calculerSim(OeuvresUtils.getFileCorpus(), new ProduitScalaire(),new InformationMutuelle());
    }
    
    public static void calculerSim (File fileOeuvre, MetriqueSim metrique, CoefficientAssociation coefficient) throws IOException
    {
        Collection<Oeuvre> oeuvres = ParserMagritte.recupererOeuvre(fileOeuvre);
        Pretraitement pre = new Pretraitement(false, false, false, false, -1, 1, 999999, true, true);
        oeuvres = pre.filtrerOeuvres(oeuvres);
//        oeuvres = Pretraitement.filtrerOeuvreSelonCible(oeuvres, "<femme>");
        Map<String, Integer> map_descripteur_id = OeuvresUtils.getMap_descripteur_id(oeuvres);
        new AfficherTableau("Descripteurs de Magritte", "descripteur", "ID", map_descripteur_id).setVisible(true);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, double[]> vecteurDesc = VectoriseurUtils.vectoriserAvecCoefficientAssociation5(map_idDesc_idOeuvres, map_idDesc_idOeuvres, OeuvresUtils.getIdOeuvres(oeuvres), coefficient, 0.0);
        vecteurDesc = VectoriseurUtils.normerVecteurCreuxC(vecteurDesc);
        
        double [] centroide = ClassifieurKmeans.calculerCentroide(vecteurDesc.values(), map_descripteur_id.size());
        centroide = VecteurCreux.normer(centroide);
        
        Map<String, Double> map_desc_simAvecCentre = new TreeMap<>();
        Map<Integer, String> map_id_desc = TrieUtils.inverserMap(map_descripteur_id);
        for (int idDes: vecteurDesc.keySet())
        {
            double sim = metrique.calculerMetrique(vecteurDesc.get(idDes), centroide);
            map_desc_simAvecCentre.put(map_id_desc.get(idDes), sim);
        }
        
        AfficheurTableau.afficher2Colonnes("proximite du descripteur avec le centre", "desc", "proximite", map_desc_simAvecCentre);
      
    }
    
}
