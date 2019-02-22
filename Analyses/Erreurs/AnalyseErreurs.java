/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte.Analyses.Erreurs;

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
import java.util.TreeMap;
import static magritte.Analyses.AnalyseOperateurSimple.getOeuvrePlusProcheDeCible;
import magritte.Oeuvre;
import magritte.OeuvresUtils;
import magritte.ParserMagritte;
import magritte.Pretraitement;

/**
 *
 * @author JF Chartier
 */
public class AnalyseErreurs 
{
    public static void main(String[] args) throws IOException 
    {
        new AnalyseErreurs().analyse(OeuvresUtils.getFileCorpus(), new ProduitScalaire(), new InformationMutuelle());
    }
    
    private Map<String, Integer> map_desc_nBruit = new TreeMap<>();
    private Map<String, Integer> map_desc_nSilence = new TreeMap<>();
    private Map<String, Double> map_desc_seuilMin = new TreeMap<>();
    private Map<String, Double> map_desc_seuilMax = new TreeMap<>();
    
    public Map<String, Integer> analyse(File fileOeuvre, MetriqueSim metrique, CoefficientAssociation coefficient) throws IOException
    {
        Collection<Oeuvre> oeuvres = ParserMagritte.recupererOeuvre(fileOeuvre);
        Pretraitement pre = new Pretraitement(false, false, false, false, -1, 2, 9999999, true, true);
        oeuvres = pre.filtrerOeuvres(oeuvres);
//        oeuvres = Pretraitement.filtrerOeuvreSelonCible(oeuvres, "<femme>");
        
        Map<String, Integer> map_descripteur_id = OeuvresUtils.getMap_descripteur_id(oeuvres);
        new AfficherTableau("Descripteurs de Magritte", "descripteur", "ID", map_descripteur_id).setVisible(true);
//        TreeMap<Integer, Collection<String>> map_date_tokens = new TreeMap<>(OeuvresUtils.getMap_date_ensembleDescripteur(oeuvres));
        
//        AfficheurTableau.afficher2Colonnes("Accroissement vocabulaire", "date", "vocabulaire centre et reduit", AccroissementVocabulaireVoSurVt.calculerAccroissementVocabulaire(map_date_tokens));
        
        Map<Integer, Set<Integer>> map_idDes_idO = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteursDescripteur = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDes_idO, map_idDes_idO, OeuvresUtils.getIdOeuvres(oeuvres), coefficient, 0.0);
        vecteursDescripteur = VectoriseurUtils.normerVecteurCreuxB(vecteursDescripteur.values());
        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteursOeuvre = VectoriseurUtils.additionnerVecteursCreux(map_idOeuvre_listeIdDescripteur, vecteursDescripteur, map_idDes_idO.size());
        vecteursOeuvre = VectoriseurUtils.normerVecteurCreuxB(vecteursOeuvre.values());
        Map<Integer, String> map_idOeuvre_titre = OeuvresUtils.getMap_idOeuvre_titre(oeuvres);
        AfficheurTableau.afficher2Colonnes("id oeuvre et titre", "id", "titre", map_idOeuvre_titre);
        
//        Map<String, Integer> map_desc_nbreErreur = new TreeMap<>();
        Map<String, Integer> map_desc_nbreOeuvre = OeuvresUtils.getMap_descripteur_freqOeuvre(oeuvres);
        for (String desc: map_descripteur_id.keySet())
        {
            analyseUnDescriteur(desc, map_descripteur_id, oeuvres, vecteursDescripteur, vecteursOeuvre, metrique);
            
        }
        
        System.out.println("descripteur;bruit;silence;nbreOeuvre;seuilMin;seuilMax");
        for (String d: map_descripteur_id.keySet())
        {
            System.out.println(d +";"+map_desc_nBruit.get(d)+";"+map_desc_nSilence.get(d)+";"+map_desc_nbreOeuvre.get(d)+";"+map_desc_seuilMin.get(d)+";"+map_desc_seuilMax.get(d));
        }
        
        return map_desc_nbreOeuvre;
    }
    
    public void analyseUnDescriteur(String desc, Map<String, Integer> map_descripteur_id, Collection<Oeuvre> oeuvres,Map<Integer, VecteurCreux> vecteursDescripteur, Map<Integer, VecteurCreux> vecteursOeuvre, MetriqueSim metrique)
    {
        map_desc_nSilence.put(desc, 0);
        map_desc_nBruit.put(desc, 0);

        Set<Integer> oeuvresSansCible = OeuvresUtils.getIdOeuvreSansCible(oeuvres, desc);
        Map<Integer, Double> map_idOSansCible_simAvecCible = new TreeMap<>();
        for (int i: oeuvresSansCible)
        {
            double sim = metrique.calculerMetrique(vecteursDescripteur.get(map_descripteur_id.get(desc)).getTabPonderation(), vecteursOeuvre.get(i).getTabPonderation());
            map_idOSansCible_simAvecCible.put(i, sim);
        }
        Set<Integer> oeuvresAvecCible = OeuvresUtils.getIdOeuvreAvecCible(oeuvres, desc);
        Map<Integer, Double> map_idAvecCible_simAvecCible = new TreeMap<>();
        for (int i: oeuvresAvecCible)
        {
            double sim = metrique.calculerMetrique(vecteursDescripteur.get(map_descripteur_id.get(desc)).getTabPonderation(), vecteursOeuvre.get(i).getTabPonderation());
            map_idAvecCible_simAvecCible.put(i, sim);
        }
        // get seuils
        double seuilMin = getSeuilMin(map_idAvecCible_simAvecCible);
        map_desc_seuilMin.put(desc, seuilMin);
        double seuilMax = getSeuilMax(map_idOSansCible_simAvecCible);
        map_desc_seuilMax.put(desc, seuilMax);
        // compute silence
        for (int i: map_idOSansCible_simAvecCible.keySet())
        {
            if (map_idOSansCible_simAvecCible.get(i)>seuilMin)
            {
                map_desc_nSilence.put(desc, map_desc_nSilence.get(desc)+1);
//                    map_desc_nSilence.merge(desc, 1, Integer::sum);
            }
        }
        // compute noise
        for (int i: map_idAvecCible_simAvecCible.keySet())
        {
            if (map_idAvecCible_simAvecCible.get(i)<seuilMax)
            {
                map_desc_nBruit.put(desc, map_desc_nBruit.get(desc)+1);
//                    map_desc_nBruit.merge(desc, 1, Integer::sum);
            }
        }
    }
    
    public double getSeuilMin(Map<Integer, Double> map_idAvecCible_simAvecCible)
    {
        return TrieUtils.trieMapCroissantSelonValeur(map_idAvecCible_simAvecCible).get(0).getValue();
    }
    
    public double getSeuilMax(Map<Integer, Double> map_idOSansCible_simAvecCible)
    {
        return TrieUtils.trieMapDecroissantSelonValeur(map_idOSansCible_simAvecCible).get(0).getValue();
    }
    
}
