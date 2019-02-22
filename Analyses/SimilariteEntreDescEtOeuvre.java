/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte.Analyses;

import Affichage.Tableau.AfficherTableau;
import Affichage.Tableau.AfficheurVecteur;
import Matrice.Vectorisateur.VecteurCreux;
import Matrice.Vectorisateur.VecteurIndicie;
import Matrice.Vectorisateur.VectoriseurUtils;
import Metrique.Similitude.Cosinus;
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
public class SimilariteEntreDescEtOeuvre 
{
    public static void main(String[] args) throws IOException 
    {
      SimilariteEntreDescEtOeuvre.calculerSimEntreDescEtOeuvre(OeuvresUtils.getFileCorpus(), new ProduitScalaire(),new InformationMutuelle());
    }
    
    
    public static void calculerSimEntreDescEtOeuvre (File fileOeuvre, MetriqueSim metrique, CoefficientAssociation coefficient) throws IOException
    {
        Collection<Oeuvre> oeuvres = ParserMagritte.recupererOeuvre(fileOeuvre);
        Pretraitement pre = new Pretraitement(false, false, false, false, -1, 1, 999999, true, true);
        oeuvres = pre.filtrerOeuvres(oeuvres);
//        oeuvres = Pretraitement.filtrerOeuvreSelonCible(oeuvres, "<femme>");
        Map<String, Integer> map_descripteur_id = OeuvresUtils.getMap_descripteur_id(oeuvres);
        new AfficherTableau("Descripteurs de Magritte", "descripteur", "ID", map_descripteur_id).setVisible(true);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteurDesc = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, OeuvresUtils.getIdOeuvres(oeuvres), coefficient, 0.0);
        vecteurDesc = VectoriseurUtils.normerVecteurCreuxB(vecteurDesc.values());
        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
        
        Map<Integer, VecteurIndicie> map_idO_simAvecDes = new TreeMap<>();
        
        
        for (int idO: map_idOeuvre_listeIdDescripteur.keySet())
        {
            double[] vecOeuvre = new double[map_descripteur_id.size()];
            Arrays.fill(vecOeuvre, 0.0);
            for (int idDes: map_idOeuvre_listeIdDescripteur.get(idO))
            {
                vecOeuvre = VecteurCreux.additionnerVecteurs(vecOeuvre, vecteurDesc.get(idDes).getTabPonderation());
            }
            vecOeuvre = VecteurCreux.normer(vecOeuvre);
            VecteurIndicie v = new VecteurIndicie(idO, new TreeMap<>());
            for (int idDes: vecteurDesc.keySet())
            {
                double sim = metrique.calculerMetrique(vecOeuvre, vecteurDesc.get(idDes).getTabPonderation());
                v.ajouter(idDes, sim);
//                System.out.println(idO +"\t"+ map_id_desc.get(idDes) + "\t" + sim);
            }
            map_idO_simAvecDes.put(idO, v);
        }
        
        AfficheurVecteur.afficherVecteurs("sim des descripteurs avec les oeuvres", OeuvresUtils.getMap_idOeuvre_titre(oeuvres), TrieUtils.inverserMap(map_descripteur_id), map_idO_simAvecDes.values());
      
    }
    
}
