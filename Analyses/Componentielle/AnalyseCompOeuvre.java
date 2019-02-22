/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte.Analyses.Componentielle;

import magritte.Analyses.Componentielle.OLD.Decomposeur2;
import Affichage.Tableau.AfficherTableau;
import Affichage.Tableau.AfficheurTableau;
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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import magritte.Oeuvre;
import magritte.OeuvresUtils;
import magritte.ParserMagritte;
import magritte.Pretraitement;

/**
 *
 * @author JF Chartier
 */
public class AnalyseCompOeuvre extends Decomposeur2
{
    public static void main(String[] args) throws IOException 
    {
        new AnalyseCompOeuvre().analyse(OeuvresUtils.getFileCorpus(), new ProduitScalaire(), new InformationMutuelle(), 0.1);
    }
    
    public Map<Integer, Map<Integer, Double>> analyse(File fileOeuvre, MetriqueSim metrique, CoefficientAssociation coefficient, double minSim) throws IOException
    {
        Collection<Oeuvre> oeuvres = ParserMagritte.recupererOeuvre(fileOeuvre);
        Pretraitement pre = new Pretraitement(false, false, false, false, -1, 2, 9999999, true, true);
        oeuvres = pre.filtrerOeuvres(oeuvres);
//        oeuvres = Pretraitement.filtrerOeuvreSelonCible(oeuvres, "<femme>");
        Map<String, Integer> map_descripteur_id = OeuvresUtils.getMap_descripteur_id(oeuvres);
        new AfficherTableau("Descripteurs de Magritte", "descripteur", "ID", map_descripteur_id).setVisible(true);
//        TreeMap<Integer, Collection<String>> map_date_tokens = new TreeMap<>(OeuvresUtils.getMap_date_ensembleDescripteur(oeuvres));
        
//        AfficheurTableau.afficher2Colonnes("Accroissement vocabulaire", "date", "vocabulaire centre et reduit", AccroissementVocabulaireVoSurVt.calculerAccroissementVocabulaire(map_date_tokens));
        
        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
        Map<Integer, Set<Integer>> map_idDes_idO = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteursDescripteur = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDes_idO, map_idDes_idO, OeuvresUtils.getIdOeuvres(oeuvres), coefficient, 0.0);
        vecteursDescripteur = VectoriseurUtils.normerVecteurCreuxB(vecteursDescripteur.values());
        Map<Integer, VecteurCreux> vecteursOeuvre = VectoriseurUtils.additionnerVecteursCreux(map_idOeuvre_listeIdDescripteur, vecteursDescripteur, map_idDes_idO.size());
        vecteursOeuvre = VectoriseurUtils.normerVecteurCreuxB(vecteursOeuvre.values());
        Map<Integer, String> map_idOeuvre_titre = OeuvresUtils.getMap_idOeuvre_titre(oeuvres);
        AfficheurTableau.afficher2Colonnes("id oeuvre et titre", "id", "titre", map_idOeuvre_titre);
        
        
        Map<Integer, Map<Integer, Double>> map_idOeuvre_composition = super.decomposerVecteurs(MatriceCreuse.getArrays(vecteursOeuvre.values()), MatriceCreuse.getArrays(vecteursDescripteur.values()), metrique, minSim);
        AfficheurTableau.afficheurStructureImbriquee(map_idOeuvre_composition, TrieUtils.inverserMap(map_descripteur_id));
        return  map_idOeuvre_composition;       
    }
    
}
