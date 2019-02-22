/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte.Analyses;

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
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import static magritte.Analyses.AnalyseOperateurSimple.getOeuvrePlusProcheDeCible;
import static magritte.Analyses.AnalyseOperateurSimple.pretraitement;
import magritte.Oeuvre;
import magritte.OeuvresUtils;
import magritte.ParserMagritte;
import magritte.Pretraitement;

/**
 *
 * @author JF Chartier
 */
public class Test 
{
    public static void main(String[] args) throws IOException 
    {
        Test.comparerDeuxFormesSoustraction(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<nudité>", new ProduitScalaire());
    }
    
    private static Map<String, Integer> map_descripteur_id;
    private static Map<Integer, String> map_id_descripteur;
    
    public static Collection<Oeuvre> pretraitement (File fileOeuvre) throws IOException
    {
        Collection<Oeuvre> oeuvres = ParserMagritte.recupererOeuvre(fileOeuvre);
        Pretraitement pre = new Pretraitement(false, false, false, false, -1, 2, 999999, true, true);
        oeuvres = pre.filtrerOeuvres(oeuvres);
        map_descripteur_id = OeuvresUtils.getMap_descripteur_id(oeuvres);
        map_id_descripteur = TrieUtils.inverserMap(map_descripteur_id);        
        return oeuvres;
         
    }
    
    public static void comparerDeuxFormesSoustraction(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, MetriqueSim metrique) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteurs = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, 0.0);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteurs = VectoriseurUtils.normerVecteurCreuxB(vecteurs.values());
        
        int idVecteurSoustraire = map_descripteur_id.get(descripteurCible);
        
        for (int id: vecteurs.keySet())
        {
            double[] x = vecteurs.get(id).soustraireComplementOrthogonaux(vecteurs.get(idVecteurSoustraire).getTabPonderation());
            x = VecteurCreux.normer(x);
            double[]y = VecteurCreux.multiplicationPointWise(vecteurs.get(id).getTabPonderation(), vecteurs.get(idVecteurSoustraire).getTabPonderation());
            y = VecteurCreux.normer(y);
            y = VecteurCreux.soustraireVecteur(vecteurs.get(id).getTabPonderation(),y);
            y = VecteurCreux.normer(y);
            double cos = metrique.calculerMetrique(x, y);
            System.out.println(descripteurCible+" " + map_id_descripteur.get(id)+" " +cos);
        }
        
    }
    
}
