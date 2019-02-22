/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte.Analyses;

import Affichage.Tableau.AfficherTableau;
import Affichage.Tableau.AfficheurTableau;
import ClassificationAutomatique.NonSupervisee.KMeans.ClassifieurKmeans;
import ClassificationAutomatique.Supervisee.KPPV.KPlusProcheVoisin;
import Matrice.VecteurCreux.GramSchmidt;
import Matrice.VecteurCreux.MatriceCreuse;
import Matrice.Vectorisateur.VecteurCreux;
import Matrice.Vectorisateur.VecteurIndicie;
import Matrice.Vectorisateur.Vectoriseur;
import Matrice.Vectorisateur.VectoriseurUtils;
import Metrique.Similitude.CorrelationPearson;
import Metrique.Similitude.Cosinus;
import Metrique.Similitude.EuclidienneInverse;
import Metrique.Similitude.EuclidienneInverseIndicie;
import Metrique.Similitude.ManhattanInverse;
import Metrique.Similitude.MetriqueSim;
import Metrique.Similitude.ProduitScalaire;
import Metrique.Similitude.ProduitScalaireIndicie;
import Ponderation.CoefficientAssociation;
import Ponderation.CorrelationMatthews;
import Ponderation.Frequence;
import Ponderation.InformationMutuelle;
import Ponderation.Jaccard;
import Ponderation.Kappa;
import Ponderation.LogLikelihood;
import UtilsJFC.TrieUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import magritte.Oeuvre;
import magritte.OeuvresUtils;
import magritte.ParserMagritte;
import magritte.Pretraitement;

/**
 *
 * @author JF Chartier
 */
public class AnalyseOperateurSimple 
{
    private static Map<String, Integer> map_descripteur_id;
    private static Map<Integer, String> map_id_descripteur;
    
    
    public static void main(String[] args) throws IOException 
    {
                
//        AnalyseOperateurSimple.pretraitement("<femme>", "<poisson>", OeuvresUtils.getFileCorpus(), new Cosinus(), new InformationMutuelle(), 50);
//        AnalyseOperateurSimple.intersection(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<femme>", "<homme>", new Cosinus(), 50);
//        AnalyseOperateurSimple.multiplication(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<nudité>", "<cheveux>", new ProduitScalaire(), 50);
        AnalyseOperateurSimple.soustraireVecteurEtNPlusProcheVoisin(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<femme>", "<veste>", new ProduitScalaire(), 50);
//        AnalyseOperateurSimple.soustraire2VecteurEtNPlusProcheVoisin(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<femme>", "<visage>", "<yeux>", new Cosinus(), 50);
//        AnalyseOperateurSimple.soustraire2VecteurEtNPlusProcheVoisinB(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<femme>", "<nudité>", "<visage>",  new Cosinus(), 50);
//        AnalyseOperateurSimple.soustraire2VecteurEtNPlusProcheVoisinC(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<femme>", "<nudité>", "<visage>", new ProduitScalaire(), 50);
//        AnalyseOperateurSimple.soustraire2VecteurEtNPlusProcheVoisinD(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<femme>", "<nudité>", "<visage>", new ProduitScalaire(), 50);
//        AnalyseOperateurSimple.soustraire2VecteurEtNPlusProcheVoisinE(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<femme>", "<nudité>", "<cheveux>", new ProduitScalaire(), 50);
//        AnalyseOperateurSimple.soustraire2VecteurEtNPlusProcheVoisinF(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<femme>", "<nudité>", "<cheveux>", new ProduitScalaire(), 50);
        AnalyseOperateurSimple.soustraire3VecteurEtNPlusProcheVoisinF(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<femme>", "<nudité>", "<cheveux>", "<décolleté>", new ProduitScalaire(), 50);
        AnalyseOperateurSimple.plusSimilaires(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<cheveux>", new ProduitScalaire(), 20);
//        AnalyseOperateurSimple.analyseSyntagmes(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<femme>", 50);
//        AnalyseOperateurSimple.soustractionEtAnalyseSyntagmes(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<femme>", "<nudité>", 50);
//        AnalyseOperateurSimple.analyseParadigme(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<homme>", new Cosinus(), 50, 0);
//        AnalyseOperateurSimple.similarite3eOrdre(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<homme>", new Cosinus(), 51);
//        AnalyseOperateurSimple.analyseSyntagmes(OeuvresUtils.getFileCorpus(), new LogLikelihood(), "<papillon>", 50);
//        AnalyseOperateurSimple.additionner(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<nudité>", "<cheveux>", new Cosinus(), 50);
//        AnalyseOperateurSimple.additionnerComplement(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<piano>", "<garçon>", new Cosinus(), 50);
//        AnalyseOperateurSimple.additionnerEtScale(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<piano>",0.658, "<garçon>", 0.401, new Cosinus(), 50);
        
//        AnalyseOperateurSimple.getSimilatite(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<fille>", "<sein>", new Cosinus());
//        AnalyseOperateurSimple.getAnalogie1(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<homme>", "<femme>", "<statue>", new Cosinus(), 10);
//        AnalyseOperateurSimple.trouverOeuvreSansCible(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<épaule>", new ProduitScalaire(), 800);
//        AnalyseOperateurSimple.trouverOeuvreAvecCible(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<femme>", new ProduitScalaire(), 402);
        
//        AnalyseOperateurSimple.trouverOeuvreAvecMetaphoreDeCible(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<femme>","<main>", new ProduitScalaire(), 50);
//        AnalyseOperateurSimple.additionnerEtSoustraire(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<femme>", "<homme>", "<nudité>", new ProduitScalaire(), 50);
//        AnalyseOperateurSimple.getAnalogie2(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<femme>", "<statue>", new ProduitScalaire());
//        AnalyseOperateurSimple.getAnalogie3b(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<fille>", "<garçon>","<homme>", new ProduitScalaire());
//        AnalyseOperateurSimple.getMetaphore(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<femme>", new ProduitScalaire(), 1357);
    }
    
    
    
    public static Collection<Oeuvre> pretraitement (File fileOeuvre) throws IOException
    {
        Collection<Oeuvre> oeuvres = ParserMagritte.recupererOeuvre(fileOeuvre);
        Pretraitement pre = new Pretraitement(false, false, false, false, -1, 2, 999999, true, true);
        oeuvres = pre.filtrerOeuvres(oeuvres);
        map_descripteur_id = OeuvresUtils.getMap_descripteur_id(oeuvres);
        map_id_descripteur = TrieUtils.inverserMap(map_descripteur_id);
//        new AfficherTableau("Descripteurs de Magritte", "descripteur", "ID", map_descripteur_id).setVisible(true);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
//        vecteursDescripteur = VectoriseurUtils.normaliserZScore(vecteursDescripteur);
//        vecteursDescripteur = VectoriseurUtils.dichotomiserSeuilMax(-0.00, vecteursDescripteur.values());
//        vecteursDescripteur = VectoriseurUtils.normerVecteurIndicie(vecteursDescripteur);
        
        return oeuvres;
                
//        plusSimilaires(TrieUtils.inverserMap(map_descripteur_id), map_id_vecteur, map_descripteur_id.get(descripteurCible), metrique, nPlusProche);
//        additionner(TrieUtils.inverserMap(map_descripteur_id), map_id_vecteur, map_descripteur_id.get(descripteurCible), map_descripteur_id.get(descripteurCovariable), metrique, nPlusProche);
//        moyenne(TrieUtils.inverserMap(map_descripteur_id), map_id_vecteur, map_descripteur_id.get(descripteurCible), map_descripteur_id.get(descripteurCovariable), metrique, nPlusProche);
//        soustraireVecteurEtNPlusProcheVoisin(TrieUtils.inverserMap(map_descripteur_id), map_id_vecteur, map_descripteur_id.get(descripteurCible), map_descripteur_id.get(descripteurCovariable), metrique, nPlusProche);
//        multiplication(TrieUtils.inverserMap(map_descripteur_id), map_id_vecteur, map_descripteur_id.get(descripteurCible), map_descripteur_id.get(descripteurCovariable), metrique, nPlusProche);
//        intersection(TrieUtils.inverserMap(map_descripteur_id), map_id_vecteur, map_descripteur_id.get(descripteurCible), map_descripteur_id.get(descripteurCovariable), metrique, nPlusProche);
//        soustraire2VecteurEtNPlusProcheVoisin(TrieUtils.inverserMap(map_descripteur_id), map_id_vecteur, map_descripteur_id.get(descripteurCible), map_descripteur_id.get(descripteurCovariable), metrique, nPlusProche);
    }
    
    // recuperer les plus simipaires qui ne sont pas cooccurrents avec la cible
    public static void analyseParadigme (File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, MetriqueSim metrique, int n, double seuilMin) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurIndicie> vecteursDescripteur = VectoriseurUtils.vectoriserAvecCoefficientAssociation4(map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, true);
        vecteursDescripteur = VectoriseurUtils.normerVecteurIndicie(vecteursDescripteur);
        
        Map<Integer, VecteurCreux> map_id_vecteur = new TreeMap<>();
        int idVecteurReference = map_descripteur_id.get(descripteurCible);
        // en selectionnant Kappa=0 comme seuilMin, on stipule que seul les cooccurrences moins frequente que le hasard sont retenues 
        Map<Integer, Double> vecteurReference = VectoriseurUtils.vectoriserAvecCoefficientAssociation4(map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), new Frequence(), true).get(idVecteurReference).getMap_Id_Ponderation();
//        Map<Integer, Double> vecteurReference = vecteursDescripteur.get(idVecteurReference).getMap_Id_Ponderation();
        
//        for (VecteurIndicie v: vecteursDescripteur.values())
        for (int id: vecteurReference.keySet())    
        {
//            System.out.println(map_id_descripteur.get(v.getId()));
//            if (v.getId()!= idVecteurReference)
//            else if 
//                if(v.getId()!= idVecteurReference && v.getMap_Id_Ponderation().get(idVecteurReference) >= seuilMin)
                if (vecteurReference.get(id) <= seuilMin)
                {
                    VecteurIndicie v = vecteursDescripteur.get(id);
                    VecteurCreux vc = VectoriseurUtils.convertir(v, map_descripteur_id.size(), 0.0);
                    map_id_vecteur.put(vc.getId(), vc);
                }
                    
        }
        System.out.println("nombre de descripteurs non cooccurrents : " + map_id_vecteur.size());
        double[] vecteurX = VectoriseurUtils.convertir(vecteursDescripteur.get(idVecteurReference), map_descripteur_id.size(), 0.0).getTabPonderation();
        recupererNPlusProche(vecteurX, map_id_descripteur, map_id_vecteur, metrique, n);
    }
    
    // recuperer les syntagmes les plus fort
    public static void analyseSyntagmes (File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, int n) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurIndicie> vecteursDescripteur = VectoriseurUtils.vectoriserAvecCoefficientAssociation3(map_idDesc_idOeuvres, map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, true);
        vecteursDescripteur = VectoriseurUtils.normerVecteurIndicie(vecteursDescripteur);
//        Map<Integer, Set<Integer>> m = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = VectoriseurUtils.vectoriserAvecCoefficientAssociation3(m, m, OeuvresUtils.getIdOeuvres(oeuvres), coefficient, false);
        
        int idVecteurReference = map_descripteur_id.get(descripteurCible);
        List<Map.Entry<Integer, Double>> list = TrieUtils.trieMapDecroissantSelonValeur(vecteursDescripteur.get(idVecteurReference).getMap_Id_Ponderation());
        if (n<0)
            n=list.size();
        for (int w = 0; w < n; w++)
        {
            System.out.printf("%-30.30s  %-30.30s\n", map_id_descripteur.get(list.get(w).getKey()), list.get(w).getValue());
        }
                
    }
    
    // recuperer les syntagmes les plus fort
    public static void soustractionEtAnalyseSyntagmes (File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, String coDescripteur, int n) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurIndicie> vecteursDescripteur = VectoriseurUtils.vectoriserAvecCoefficientAssociation3(map_idDesc_idOeuvres, map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, true);
        vecteursDescripteur = VectoriseurUtils.normerVecteurIndicie(vecteursDescripteur);
        
        int x = map_descripteur_id.get(descripteurCible);
        int y = map_descripteur_id.get(coDescripteur);
        Map<Integer, Double> XmoinsY = VecteurIndicie.complementOrthogonaux(vecteursDescripteur.get(x).getMap_Id_Ponderation(), vecteursDescripteur.get(y).getMap_Id_Ponderation());
        XmoinsY = VecteurIndicie.normerVecteur(XmoinsY);
        
        List<Map.Entry<Integer, Double>> list = TrieUtils.trieMapDecroissantSelonValeur(XmoinsY);
        if (n<0)
            n=list.size();
        for (int w = 0; w < n; w++)
        {
            System.out.printf("%-30.30s  %-30.30s\n", map_id_descripteur.get(list.get(w).getKey()), list.get(w).getValue());
        }
                
    }
    
    public static void similarite3eOrdre(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, MetriqueSim metrique, int nPlusProcheVoisin) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurIndicie> vecteursDescripteur = VectoriseurUtils.vectoriserAvecCoefficientAssociation4(map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, true);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteursDescripteur = VectoriseurUtils.normerVecteurIndicie(vecteursDescripteur);
        vecteursDescripteur = AnalyseSimilitude.AnalyseSimilitude.calculerMetriqueMatriceCarre(vecteursDescripteur, new ProduitScalaireIndicie(), false, false);
        vecteursDescripteur = VectoriseurUtils.normerVecteurIndicie(vecteursDescripteur);
        
        
        Map<Integer, VecteurCreux> map_id_vecteur = VectoriseurUtils.convertir(vecteursDescripteur.values(), 0);
//        double[] centroide = ClassifieurKmeans.calculerCentroide(VectoriseurUtils.getVecteurs(map_id_vecteur.values()), map_idDesc_idOeuvres.keySet().size());
//        map_id_vecteur = VectoriseurUtils.soustraire(map_id_vecteur, centroide);
//        map_id_vecteur = VectoriseurUtils.normerVecteurCreuxB(map_id_vecteur.values());
        
        int idVecteurReference = map_descripteur_id.get(descripteurCible);
        recupererNPlusProche(map_id_vecteur.get(idVecteurReference).getTabPonderation(), map_id_descripteur, map_id_vecteur, metrique, nPlusProcheVoisin);
    }
    
    public static void plusSimilaires(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, MetriqueSim metrique, int nPlusProcheVoisin) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteursDescripteur = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, 0.0);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteursDescripteur = VectoriseurUtils.normerVecteurCreuxB(vecteursDescripteur.values());
//        vecteursDescripteur = VectoriseurUtils.dichotomiserSeuilMax(0.005, vecteursDescripteur.values());
//        Map<Integer, VecteurCreux> map_id_vecteur = VectoriseurUtils.convertir(vecteursDescripteur.values(), 0.0000);
//        double[] centroide = ClassifieurKmeans.calculerCentroide(VectoriseurUtils.getVecteurs(map_id_vecteur.values()), map_idDesc_idOeuvres.keySet().size());
//        map_id_vecteur = VectoriseurUtils.soustraire(map_id_vecteur, centroide);
//        map_id_vecteur = VectoriseurUtils.normerVecteurCreuxB(map_id_vecteur.values());
        
        
        int idVecteurReference = map_descripteur_id.get(descripteurCible);
        recupererNPlusProche(vecteursDescripteur.get(idVecteurReference).getTabPonderation(), map_id_descripteur, vecteursDescripteur, metrique, nPlusProcheVoisin);
        
        
        Map<Integer, Set<Integer>> map_idO_idsDesc = OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, map_descripteur_id);
        Map<Integer, String> map_idO_titre = OeuvresUtils.getMap_idOeuvre_titre(oeuvres);        
        getOeuvrePlusProcheDeCible(vecteursDescripteur, vecteursDescripteur.get(idVecteurReference).getTabPonderation(), map_idO_titre, map_idO_idsDesc, metrique, nPlusProcheVoisin);
        
    
    }
    
////////    public static void plusSimilaires (final Map<Integer, String> map_idUnif_unifString, final Map<Integer, VecteurCreux> map_id_vecteur, int idVecteurReference, MetriqueSim metrique, int nPlusProcheVoisin)
////////    {
////////        recupererNPlusProche(map_id_vecteur.get(idVecteurReference).getTabPonderation(), map_idUnif_unifString, map_id_vecteur, metrique, nPlusProcheVoisin);
////////    }
    
    private static void recupererNPlusProche (double[] vecteurX, final Map<Integer, String> map_idUnif_unifString, final Map<Integer, VecteurCreux> map_id_vecteur, MetriqueSim metrique, int nPlusProcheVoisin)
    {
        Map<Integer, Double> map_id_similitude = KPlusProcheVoisin.kPlusProcheVoisinDeX(map_id_vecteur, vecteurX, nPlusProcheVoisin, metrique);
        HashMap<String, Double> map_nom_sim = new HashMap<>(); 
        for (Map.Entry<Integer, Double> w: TrieUtils.trieMapDecroissantSelonValeur(map_id_similitude, nPlusProcheVoisin))
        {
            map_nom_sim.put(map_idUnif_unifString.get(w.getKey()), map_id_similitude.get(w.getKey()));
//            System.out.println(w.getKey() + "\t " + map_idUnif_unifString.get(w.getKey()) + "\t " +map_id_similitude.get(w.getKey()));
            System.out.println(w.getKey() +" ; "+map_idUnif_unifString.get(w.getKey()) +" ; "+ map_id_similitude.get(w.getKey()));
//            System.out.printf("%-15.150s %-60.60s %-30.30s\n", w.getKey(), map_idUnif_unifString.get(w.getKey()), map_id_similitude.get(w.getKey()));
        }
        AfficheurTableau.afficher2Colonnes("nom des element et similitude", "element", "metrique sim", map_nom_sim);
    }
    
    
    public static void soustraireVecteurEtNPlusProcheVoisin(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, String descripteurCovariable, MetriqueSim metrique, int nPlusProcheVoisin) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteurs = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, 0.0);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteurs = VectoriseurUtils.normerVecteurCreuxB(vecteurs.values());
        
        final int idVecteurReference = map_descripteur_id.get(descripteurCible);
        int idVecteurSoustraire = map_descripteur_id.get(descripteurCovariable);
        double[] x = vecteurs.get(idVecteurReference).soustraireComplementOrthogonaux(vecteurs.get(idVecteurSoustraire).getTabPonderation());
//        double[] x = vecteurs.get(idVecteurReference).soustraireVecteur(vecteurs.get(idVecteurSoustraire).getTabPonderation());
        
        x = VecteurCreux.normer(x);
        System.out.println("les plus proche voisin du descripteur "+map_id_descripteur.get(idVecteurReference)+" - " + map_id_descripteur.get(idVecteurSoustraire));
        recupererNPlusProche(x, map_id_descripteur, vecteurs, metrique, nPlusProcheVoisin);
            
        Map<Integer, Set<Integer>> map_idO_idsDesc = OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, map_descripteur_id);
        Map<Integer, String> map_idO_titre = OeuvresUtils.getMap_idOeuvre_titre(oeuvres);        
        getOeuvrePlusProcheDeCible(vecteurs, x, map_idO_titre, map_idO_idsDesc, metrique, nPlusProcheVoisin);
        
        getCooccurrentPlusForts(x, map_id_descripteur, nPlusProcheVoisin);
////        // get cooccurrents plus forts 
////        List<Map.Entry<Integer, Double>> list = TrieUtils.trieMapDecroissantSelonValeur(vecteursDescripteur.get(idVecteurReference).getMap_Id_Ponderation());
////        if (n<0)
////            n=list.size();
////        for (int w = 0; w < n; w++)
////        {
////            System.out.printf("%-30.30s  %-30.30s\n", map_id_descripteur.get(list.get(w).getKey()), list.get(w).getValue());
////        }
        
    }
    
    public static void soustraireVecteurBEtNPlusProcheVoisin(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, String descripteurCovariable, MetriqueSim metrique, int nPlusProcheVoisin) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteurs = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, 0.0);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteurs = VectoriseurUtils.normerVecteurCreuxB(vecteurs.values());
        
        final int idVecteurReference = map_descripteur_id.get(descripteurCible);
        int idVecteurSoustraire = map_descripteur_id.get(descripteurCovariable);
        double[] x = vecteurs.get(idVecteurReference).soustraireVecteur(vecteurs.get(idVecteurSoustraire).getTabPonderation());
//        double[] x = vecteurs.get(idVecteurReference).soustraireVecteur(vecteurs.get(idVecteurSoustraire).getTabPonderation());
        
        x = VecteurCreux.normer(x);
        System.out.println("les plus proche voisin du descripteur "+map_id_descripteur.get(idVecteurReference)+" - " + map_id_descripteur.get(idVecteurSoustraire));
        recupererNPlusProche(x, map_id_descripteur, vecteurs, metrique, nPlusProcheVoisin);
            
        Map<Integer, Set<Integer>> map_idO_idsDesc = OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, map_descripteur_id);
        Map<Integer, String> map_idO_titre = OeuvresUtils.getMap_idOeuvre_titre(oeuvres);        
        getOeuvrePlusProcheDeCible(vecteurs, x, map_idO_titre, map_idO_idsDesc, metrique, nPlusProcheVoisin);
        getCooccurrentPlusForts(x, map_id_descripteur, nPlusProcheVoisin);
        
////        // get cooccurrents plus forts 
////        List<Map.Entry<Integer, Double>> list = TrieUtils.trieMapDecroissantSelonValeur(vecteursDescripteur.get(idVecteurReference).getMap_Id_Ponderation());
////        if (n<0)
////            n=list.size();
////        for (int w = 0; w < n; w++)
////        {
////            System.out.printf("%-30.30s  %-30.30s\n", map_id_descripteur.get(list.get(w).getKey()), list.get(w).getValue());
////        }
        
    }
    
    // i = (x-y)-z
    public static void soustraire2VecteurEtNPlusProcheVoisin(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, String descripteur1, String descripteur2, MetriqueSim metrique, int nPlusProcheVoisin) throws IOException
    {
    
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteursDescripteur = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, 0.0);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteursDescripteur = VectoriseurUtils.normerVecteurCreuxB(vecteursDescripteur.values());
        
        int idVecteurReference = map_descripteur_id.get(descripteurCible);
        int idVecteurSoustraire = map_descripteur_id.get(descripteur1);
        double[] vecteurX = vecteursDescripteur.get(idVecteurReference).soustraireComplementOrthogonaux(vecteursDescripteur.get(idVecteurSoustraire).getTabPonderation());
        vecteurX = VecteurCreux.normer(vecteurX);
        vecteurX = new VecteurCreux(-1, vecteurX).soustraireComplementOrthogonaux(vecteursDescripteur.get(map_descripteur_id.get(descripteur2)).getTabPonderation());
        vecteurX = VecteurCreux.normer(vecteurX);
        System.out.println("les plus proche voisin du descripteur "+descripteurCible+" - " + descripteur1 +" - "+descripteur2);
        recupererNPlusProche(vecteurX, map_id_descripteur, vecteursDescripteur, metrique, nPlusProcheVoisin);
                     
        Map<Integer, Set<Integer>> map_idO_idsDesc = OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, map_descripteur_id);
        Map<Integer, String> map_idO_titre = OeuvresUtils.getMap_idOeuvre_titre(oeuvres);        
        getOeuvrePlusProcheDeCible(vecteursDescripteur, vecteurX, map_idO_titre, map_idO_idsDesc, metrique, nPlusProcheVoisin);
    }
    
    // i = (x-y)-(z-y)
    public static void soustraire2VecteurEtNPlusProcheVoisinC(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, String descripteur1, String descripteur2, MetriqueSim metrique, int nPlusProcheVoisin) throws IOException
    {
    
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteursDescripteur = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, 0.0);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteursDescripteur = VectoriseurUtils.normerVecteurCreuxB(vecteursDescripteur.values());
        
        int idVecteurReference = map_descripteur_id.get(descripteurCible);
        int idVecteurSoustraire = map_descripteur_id.get(descripteur1);
        int idVecteurSoustraire2 = map_descripteur_id.get(descripteur2);
        double[]x = vecteursDescripteur.get(idVecteurReference).getTabPonderation();
        double[]y = vecteursDescripteur.get(idVecteurSoustraire).getTabPonderation();
        double[]z= vecteursDescripteur.get(idVecteurSoustraire2).getTabPonderation();
        
        double[]out = VecteurCreux.soustraireComplementOrthogonaux(x, y);
        out = VecteurCreux.normer(out);
        z = VecteurCreux.soustraireComplementOrthogonaux(z, y);
        z = VecteurCreux.normer(z);
        out = VecteurCreux.soustraireComplementOrthogonaux(out, z);
        out = VecteurCreux.normer(out);
        
        System.out.println("les plus proche voisin du descripteur "+descripteurCible+" - " + descripteur1 +" - "+descripteur2);
        recupererNPlusProche(out, map_id_descripteur, vecteursDescripteur, metrique, nPlusProcheVoisin);
                     
        Map<Integer, Set<Integer>> map_idO_idsDesc = OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, map_descripteur_id);
        Map<Integer, String> map_idO_titre = OeuvresUtils.getMap_idOeuvre_titre(oeuvres);        
        getOeuvrePlusProcheDeCible(vecteursDescripteur, out, map_idO_titre, map_idO_idsDesc, metrique, nPlusProcheVoisin);
        
        
        getCooccurrentPlusForts(out, map_id_descripteur, nPlusProcheVoisin);
    }
    
    // i = (x-y)-(z-y))
    public static void soustraire2VecteurEtNPlusProcheVoisinD(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, String descripteur1, String descripteur2, MetriqueSim metrique, int nPlusProcheVoisin) throws IOException
    {
    
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteursDescripteur = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, 0.0);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteursDescripteur = VectoriseurUtils.normerVecteurCreuxB(vecteursDescripteur.values());
        
        int idVecteurReference = map_descripteur_id.get(descripteurCible);
        int idVecteurSoustraire = map_descripteur_id.get(descripteur1);
        int idVecteurSoustraire2 = map_descripteur_id.get(descripteur2);
        double[]x = vecteursDescripteur.get(idVecteurReference).getTabPonderation();
        double[]y = vecteursDescripteur.get(idVecteurSoustraire).getTabPonderation();
        double[]z= vecteursDescripteur.get(idVecteurSoustraire2).getTabPonderation();
        
        double[]soustracteur = y;
        double[]out = VecteurCreux.soustraireVecteur(x, soustracteur);
        out = VecteurCreux.normer(out);
        soustracteur=VecteurCreux.soustraireComplementOrthogonaux(z, soustracteur);
        soustracteur = VecteurCreux.normer(soustracteur);
        out = VecteurCreux.soustraireVecteur(out, soustracteur);
        out = VecteurCreux.normer(out);
        
        System.out.println("les plus proche voisin du descripteur "+descripteurCible+" - " + descripteur1 +" - "+descripteur2);
        recupererNPlusProche(out, map_id_descripteur, vecteursDescripteur, metrique, nPlusProcheVoisin);
                     
        Map<Integer, Set<Integer>> map_idO_idsDesc = OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, map_descripteur_id);
        Map<Integer, String> map_idO_titre = OeuvresUtils.getMap_idOeuvre_titre(oeuvres);        
        getOeuvrePlusProcheDeCible(vecteursDescripteur, out, map_idO_titre, map_idO_idsDesc, metrique, nPlusProcheVoisin);
        
        getCooccurrentPlusForts(out, map_id_descripteur, nPlusProcheVoisin);
    }
    
    // i = (x-y)-(z-y))
    // cette version est celle de Widdows qui respecte le processus de Projection orthogonale Gram-Schidt
    public static void soustraire2VecteurEtNPlusProcheVoisinE(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, String descripteur1, String descripteur2, MetriqueSim metrique, int nPlusProcheVoisin) throws IOException
    {
    
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteursDescripteur = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, 0.0);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteursDescripteur = VectoriseurUtils.normerVecteurCreuxB(vecteursDescripteur.values());
        
        int idVecteurReference = map_descripteur_id.get(descripteurCible);
        int idVecteurSoustraire = map_descripteur_id.get(descripteur1);
        int idVecteurSoustraire2 = map_descripteur_id.get(descripteur2);
        double[]x = vecteursDescripteur.get(idVecteurReference).getTabPonderation();
        double[]y = vecteursDescripteur.get(idVecteurSoustraire).getTabPonderation();
        double[]z= vecteursDescripteur.get(idVecteurSoustraire2).getTabPonderation();
        
        List<double[]> vecteursOrtho = new ArrayList<>();
        vecteursOrtho.add(y);
        vecteursOrtho.add(VecteurCreux.normer(VecteurCreux.soustraireComplementOrthogonaux(z,y)));
        double[]out=x;
        double[]sum=new double[x.length];
        Arrays.fill(sum, 0.0);
        for (int i=0;i<vecteursOrtho.size();i++)
        {
            sum=VecteurCreux.additionnerVecteurs(sum, VecteurCreux.getComplementOrthogonauxDeYsurX(x, vecteursOrtho.get(i)));

//            out=VecteurCreux.soustraireVecteur(out, VecteurCreux.getComplementOrthogonauxDeYsurX(x, vecteursOrtho.get(i)));
//////            out=VecteurCreux.soustraireComplementOrthogonaux(x, vecteursOrtho.get(i));
        }
        
        out=VecteurCreux.soustraireVecteur(x, sum);
        
//        double[]soustracteur = MatriceCreuse.additionnerVecteurs(vecteursOrtho, x.length);
//        soustracteur=VecteurCreux.normer(soustracteur);
//        double[]out = VecteurCreux.soustraireVecteur(x, soustracteur);
        out = VecteurCreux.normer(out);
        
        
        System.out.println("les plus proche voisin du descripteur "+descripteurCible+" - " + descripteur1 +" - "+descripteur2);
        recupererNPlusProche(out, map_id_descripteur, vecteursDescripteur, metrique, nPlusProcheVoisin);
                     
        Map<Integer, Set<Integer>> map_idO_idsDesc = OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, map_descripteur_id);
        Map<Integer, String> map_idO_titre = OeuvresUtils.getMap_idOeuvre_titre(oeuvres);        
        getOeuvrePlusProcheDeCible(vecteursDescripteur, out, map_idO_titre, map_idO_idsDesc, metrique, nPlusProcheVoisin);
        
        getCooccurrentPlusForts(out, map_id_descripteur, nPlusProcheVoisin);
    }
    
    // i = (x-y)-(z-y))
    // cette version est celle de Widdows qui respecte le processus de Projection orthogonale Gram-Schidt
    public static void soustraire2VecteurEtNPlusProcheVoisinF(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, String descripteur1, String descripteur2, MetriqueSim metrique, int nPlusProcheVoisin) throws IOException
    {
    
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteursDescripteur = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, 0.0);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteursDescripteur = VectoriseurUtils.normerVecteurCreuxB(vecteursDescripteur.values());
        
        int idVecteurReference = map_descripteur_id.get(descripteurCible);
        int idVecteurSoustraire = map_descripteur_id.get(descripteur1);
        int idVecteurSoustraire2 = map_descripteur_id.get(descripteur2);
        double[]x = vecteursDescripteur.get(idVecteurReference).getTabPonderation();
        
        double[]y = vecteursDescripteur.get(idVecteurSoustraire).getTabPonderation();
        double[]z= vecteursDescripteur.get(idVecteurSoustraire2).getTabPonderation();
        Map<Integer, double[]> vv=new TreeMap<>();
        vv.put(idVecteurSoustraire, y);
        vv.put(idVecteurSoustraire2, z);
        GramSchmidt gs= new GramSchmidt();
        vv = gs.projectionOrthogonale(vv);
        
        double[]out = gs.projectionOrthogonale(x, vv.values());
                
        
        System.out.println("les plus proche voisin du descripteur "+descripteurCible+" - " + descripteur1 +" - "+descripteur2);
        recupererNPlusProche(out, map_id_descripteur, vecteursDescripteur, metrique, nPlusProcheVoisin);
                     
        Map<Integer, Set<Integer>> map_idO_idsDesc = OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, map_descripteur_id);
        Map<Integer, String> map_idO_titre = OeuvresUtils.getMap_idOeuvre_titre(oeuvres);        
        getOeuvrePlusProcheDeCible(vecteursDescripteur, out, map_idO_titre, map_idO_idsDesc, metrique, nPlusProcheVoisin);
        
        getCooccurrentPlusForts(out, map_id_descripteur, nPlusProcheVoisin);
    }
    
    // i = (x-y)-(z-y))
    // cette version est celle de Widdows qui respecte le processus de Projection orthogonale Gram-Schidt
    public static void soustraire3VecteurEtNPlusProcheVoisinF(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, String descripteur1, String descripteur2, String descripteur3, MetriqueSim metrique, int nPlusProcheVoisin) throws IOException
    {
    
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteursDescripteur = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, 0.0);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteursDescripteur = VectoriseurUtils.normerVecteurCreuxB(vecteursDescripteur.values());
        
        int idVecteurReference = map_descripteur_id.get(descripteurCible);
        int idVecteurSoustraire1 = map_descripteur_id.get(descripteur1);
        int idVecteurSoustraire2 = map_descripteur_id.get(descripteur2);
        int idVecteurSoustraire3 = map_descripteur_id.get(descripteur3);
        double[]x = vecteursDescripteur.get(idVecteurReference).getTabPonderation();
        
        double[]y = vecteursDescripteur.get(idVecteurSoustraire1).getTabPonderation();
        double[]z= vecteursDescripteur.get(idVecteurSoustraire2).getTabPonderation();
        double[]w = vecteursDescripteur.get(idVecteurSoustraire3).getTabPonderation();
        Map<Integer, double[]> vv=new TreeMap<>();
        vv.put(idVecteurSoustraire1, y);
        vv.put(idVecteurSoustraire2, z);
        vv.put(idVecteurSoustraire3, w);
        GramSchmidt gs= new GramSchmidt();
        vv = gs.projectionOrthogonale(vv);
        
        double[]out = gs.projectionOrthogonale(x, vv.values());
                
        
        System.out.println("les plus proche voisin du descripteur "+descripteurCible+" - " + descripteur1 +" - "+descripteur2+"-"+descripteur3);
        recupererNPlusProche(out, map_id_descripteur, vecteursDescripteur, metrique, nPlusProcheVoisin);
                     
        Map<Integer, Set<Integer>> map_idO_idsDesc = OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, map_descripteur_id);
        Map<Integer, String> map_idO_titre = OeuvresUtils.getMap_idOeuvre_titre(oeuvres);        
        getOeuvrePlusProcheDeCible(vecteursDescripteur, out, map_idO_titre, map_idO_idsDesc, metrique, nPlusProcheVoisin);
        
        getCooccurrentPlusForts(out, map_id_descripteur, nPlusProcheVoisin);
    }
    
    public static void getCooccurrentPlusForts (double[] vecteur, Map<Integer, String> map_id_descripteur, int n)
    {
        List<Map.Entry<Integer, Double>> list =TrieUtils.trieArrayDecroissantSelonValeur(vecteur);
        if (n<0)
            n=list.size();
        for (int w = 0; w < n; w++)
        {
            System.out.printf("%-30.30s  %-30.30s\n", map_id_descripteur.get(list.get(w).getKey()), list.get(w).getValue());
        }
    }
    
    // cette methode semble etre moin interressante
    // selon Widdows, cette methode pose probleme
    // soustraire i = x - (y+z) ne semble donner un vecteur independant a la fois de y et z
    public static void soustraire2VecteurEtNPlusProcheVoisinB(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, String descripteur1, String descripteur2, MetriqueSim metrique, int nPlusProcheVoisin) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurIndicie> vecteursDescripteur = VectoriseurUtils.vectoriserAvecCoefficientAssociation4(map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, true);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteursDescripteur = VectoriseurUtils.normerVecteurIndicie(vecteursDescripteur);
        Map<Integer, VecteurCreux> map_id_vecteur = VectoriseurUtils.convertir(vecteursDescripteur.values(), 0);
        
//        double[] centroide = ClassifieurKmeans.calculerCentroide(VectoriseurUtils.getVecteurs(map_id_vecteur.values()), map_idDesc_idOeuvres.keySet().size());
//        map_id_vecteur = VectoriseurUtils.soustraire(map_id_vecteur, centroide);
//        map_id_vecteur = VectoriseurUtils.normerVecteurCreuxB(map_id_vecteur.values());
        
        int idVecteurReference = map_descripteur_id.get(descripteurCible);
        int idVecteurSoustraire1 = map_descripteur_id.get(descripteur1);
        int idVecteurSoustraire2 = map_descripteur_id.get(descripteur2);
        double[] vecteurX = VecteurCreux.additionnerVecteurs(map_id_vecteur.get(idVecteurSoustraire1).getTabPonderation(), map_id_vecteur.get(idVecteurSoustraire2).getTabPonderation());
//        double[] vecteurX = VecteurCreux.moyenneVecteurs(map_id_vecteur.get(idVecteurSoustraire1).getTabPonderation(), map_id_vecteur.get(idVecteurSoustraire2).getTabPonderation());
        
        vecteurX = VecteurCreux.normer(vecteurX);
//        map_id_vecteur.get(idVecteurSoustraire1).additionnerVecteurs(map_id_vecteur.get(idVecteurSoustraire2).getTabPonderation());
        vecteurX = map_id_vecteur.get(idVecteurReference).soustraireComplementOrthogonaux(vecteurX);
        vecteurX = VecteurCreux.normer(vecteurX);
        
//        vecteurX = new VecteurCreux(-1, vecteurX).complementOrthogonaux(map_id_vecteur.get(map_descripteur_id.get(descripteur2)).getTabPonderation());
        
        System.out.println("les plus proche voisin du descripteur "+descripteurCible+" - " + descripteur1 +" + "+descripteur2);
        recupererNPlusProche(vecteurX, map_id_descripteur, map_id_vecteur, metrique, nPlusProcheVoisin);
                 
    }
    
    public static void intersection(final Map<Integer, String> map_idUnif_unifString, final Map<Integer, VecteurCreux> map_id_vecteur, int idVecteurReference, int idVecteurSoustraire, MetriqueSim metrique, int nPlusProcheVoisin)
    {
        double[] differenceA = map_id_vecteur.get(idVecteurReference).soustraireComplementOrthogonaux(map_id_vecteur.get(idVecteurSoustraire).getTabPonderation());
        double[] differenceB = map_id_vecteur.get(idVecteurSoustraire).soustraireComplementOrthogonaux(map_id_vecteur.get(idVecteurReference).getTabPonderation());
        double[] sommeDifferenceAB = VecteurCreux.additionnerVecteurs(differenceA, differenceB);
        
        double [] somme  = map_id_vecteur.get(idVecteurReference).additionnerVecteursOLD(map_id_vecteur.get(idVecteurSoustraire).getTabPonderation());
        double[] vecteurX = VecteurCreux.soustraireComplementOrthogonaux(somme, sommeDifferenceAB);
//        Map<Integer, Double> map_id_similitude = KPlusProcheVoisin.kPlusProcheVoisinDeX(map_id_vecteur, vecteurX, nPlusProcheVoisin, metrique);
        System.out.println("les plus proche voisin de l'intersection de "+map_idUnif_unifString.get(idVecteurReference)+" et " + map_idUnif_unifString.get(idVecteurSoustraire));
        recupererNPlusProche(vecteurX, map_idUnif_unifString, map_id_vecteur, metrique, nPlusProcheVoisin);
//        for (int idUnif: map_id_similitude.keySet())
//        {
//            System.out.println(idUnif + "\t" + map_idUnif_unifString.get(idUnif) + "\t" +map_id_similitude.get(idUnif));
//        }
    }
    
    public static void intersection(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, String descripteurCovariable, MetriqueSim metrique, int nPlusProcheVoisin) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurIndicie> vecteursDescripteur = VectoriseurUtils.vectoriserAvecCoefficientAssociation4(map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, true);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteursDescripteur = VectoriseurUtils.normerVecteurIndicie(vecteursDescripteur);
        
        Map<Integer, VecteurCreux> map_id_vecteur = VectoriseurUtils.convertir(vecteursDescripteur.values(), 0);
        int idVecteurReference = map_descripteur_id.get(descripteurCible);
        int idVecteurCovariable = map_descripteur_id.get(descripteurCovariable);
        
        double[] differenceA = map_id_vecteur.get(idVecteurReference).soustraireComplementOrthogonaux(map_id_vecteur.get(idVecteurCovariable).getTabPonderation());
        differenceA = VecteurCreux.normer(differenceA);
        double[] differenceB = map_id_vecteur.get(idVecteurCovariable).soustraireComplementOrthogonaux(map_id_vecteur.get(idVecteurReference).getTabPonderation());
        differenceB = VecteurCreux.normer(differenceB);
        double[] sommeDifferenceAB = VecteurCreux.additionnerVecteurs(differenceA, differenceB);
        sommeDifferenceAB = VecteurCreux.normer(sommeDifferenceAB);
        
        double [] somme  = map_id_vecteur.get(idVecteurReference).additionnerVecteursOLD(map_id_vecteur.get(idVecteurCovariable).getTabPonderation());
        double[] vecteurX = VecteurCreux.soustraireComplementOrthogonaux(somme, sommeDifferenceAB);
        
//        double[] vecteurX = VecteurCreux.multiplicationPointWise(map_id_vecteur.get(idVecteurReference).getTabPonderation(), map_id_vecteur.get(idVecteurCovariable).getTabPonderation());
        System.out.println("les plus proche voisin du descripteur "+map_id_descripteur.get(idVecteurReference)+" intersection " + map_id_descripteur.get(idVecteurCovariable));
        recupererNPlusProche(vecteurX, map_id_descripteur, map_id_vecteur, metrique, nPlusProcheVoisin);
             
    }
    
    public static void multiplication(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, String descripteurCovariable, MetriqueSim metrique, int nPlusProcheVoisin) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteursDescripteur = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, 0.0);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteursDescripteur = VectoriseurUtils.normerVecteurCreuxB(vecteursDescripteur.values());
        
        int idVecteurReference = map_descripteur_id.get(descripteurCible);
        int idVecteurMultiplier = map_descripteur_id.get(descripteurCovariable);
        
        double[] vecteurX = VecteurCreux.multiplicationPointWise(vecteursDescripteur.get(idVecteurReference).getTabPonderation(), vecteursDescripteur.get(idVecteurMultiplier).getTabPonderation());
        vecteurX = VecteurCreux.normer(vecteurX);
        System.out.println("les plus proche voisin du descripteur "+map_id_descripteur.get(idVecteurReference)+" * " + map_id_descripteur.get(idVecteurMultiplier));
        recupererNPlusProche(vecteurX, map_id_descripteur, vecteursDescripteur, metrique, nPlusProcheVoisin);
             
        Map<Integer, Set<Integer>> map_idO_idsDesc = OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, map_descripteur_id);
        Map<Integer, String> map_idO_titre = OeuvresUtils.getMap_idOeuvre_titre(oeuvres);        
        getOeuvrePlusProcheDeCible(vecteursDescripteur, vecteurX, map_idO_titre, map_idO_idsDesc, metrique, nPlusProcheVoisin);
    }
    
    public static void multiplication(final Map<Integer, String> map_idUnif_unifString, final Map<Integer, VecteurCreux> map_id_vecteur, int idVecteurReference, int idVecteurSoustraire, MetriqueSim metrique, int nPlusProcheVoisin)
    {
        double[] vecteurX = VecteurCreux.multiplicationPointWise(map_id_vecteur.get(idVecteurReference).getTabPonderation(), map_id_vecteur.get(idVecteurSoustraire).getTabPonderation());
//        Map<Integer, Double> map_id_similitude = KPlusProcheVoisin.kPlusProcheVoisinDeX(map_id_vecteur, vecteurX, nPlusProcheVoisin, metrique);
        System.out.println("les plus proche voisin du descripteur "+map_idUnif_unifString.get(idVecteurReference)+" multiplier par " + map_idUnif_unifString.get(idVecteurSoustraire));
        recupererNPlusProche(vecteurX, map_idUnif_unifString, map_id_vecteur, metrique, nPlusProcheVoisin);
//        for (int idUnif: map_id_similitude.keySet())
//        {
//            System.out.println(idUnif + "\t" + map_idUnif_unifString.get(idUnif) + "\t" +map_id_similitude.get(idUnif));
//        }
    }
    
    public static void additionner(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, String descripteurCovariable, MetriqueSim metrique, int nPlusProcheVoisin) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteurs = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, 0.0);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteurs = VectoriseurUtils.normerVecteurCreuxB(vecteurs.values());
        
        int idVecteurReference = map_descripteur_id.get(descripteurCible);
        int idVecteurAddi = map_descripteur_id.get(descripteurCovariable);
        
        double[] vecteurX = VecteurCreux.additionnerVecteurs(vecteurs.get(idVecteurReference).getTabPonderation(), vecteurs.get(idVecteurAddi).getTabPonderation());
//        double[] vecteurX = vecteurs.get(idVecteurReference).additionnerComplementOrthogonaux(vecteurs.get(idVecteurAddi).getTabPonderation());
        
        vecteurX = VecteurCreux.normer(vecteurX);
        System.out.println("les plus proche voisin du descripteur "+map_id_descripteur.get(idVecteurReference)+" * " + map_id_descripteur.get(idVecteurAddi));
        recupererNPlusProche(vecteurX, map_id_descripteur, vecteurs, metrique, nPlusProcheVoisin);
           
        Map<Integer, Set<Integer>> map_idO_idsDesc = OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, map_descripteur_id);
        Map<Integer, String> map_idO_titre = OeuvresUtils.getMap_idOeuvre_titre(oeuvres);        
        getOeuvrePlusProcheDeCible(vecteurs, vecteurX, map_idO_titre, map_idO_idsDesc, metrique, nPlusProcheVoisin);
    }
    
    public static void additionnerComplement(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, String descripteurCovariable, MetriqueSim metrique, int nPlusProcheVoisin) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteurs = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, 0.0);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteurs = VectoriseurUtils.normerVecteurCreuxB(vecteurs.values());
        
        int idVecteurReference = map_descripteur_id.get(descripteurCible);
        int idVecteurAddi = map_descripteur_id.get(descripteurCovariable);
        
//        double[] vecteurX = VecteurCreux.additionnerVecteurs(vecteurs.get(idVecteurReference).getTabPonderation(), vecteurs.get(idVecteurAddi).getTabPonderation());
        double[] vecteurX = vecteurs.get(idVecteurReference).additionnerComplementOrthogonaux(vecteurs.get(idVecteurAddi).getTabPonderation());
        
        vecteurX = VecteurCreux.normer(vecteurX);
        System.out.println("les plus proche voisin du descripteur "+map_id_descripteur.get(idVecteurReference)+" * " + map_id_descripteur.get(idVecteurAddi));
        recupererNPlusProche(vecteurX, map_id_descripteur, vecteurs, metrique, nPlusProcheVoisin);
           
        Map<Integer, Set<Integer>> map_idO_idsDesc = OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, map_descripteur_id);
        Map<Integer, String> map_idO_titre = OeuvresUtils.getMap_idOeuvre_titre(oeuvres);        
        getOeuvrePlusProcheDeCible(vecteurs, vecteurX, map_idO_titre, map_idO_idsDesc, metrique, nPlusProcheVoisin);
    }
    
    public static void additionnerEtScale(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, double scale1, String descripteurCovariable, double scale2, MetriqueSim metrique, int nPlusProcheVoisin) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteurs = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, 0.0);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteurs = VectoriseurUtils.normerVecteurCreuxB(vecteurs.values());
        
        int idVecteurReference = map_descripteur_id.get(descripteurCible);
        int idVecteurAddi = map_descripteur_id.get(descripteurCovariable);
        double[] x = vecteurs.get(idVecteurReference).getTabPonderation();
        x = VecteurCreux.multiplierParUnScalaire(x, scale1);
        double[] y = vecteurs.get(idVecteurAddi).getTabPonderation();
        y=VecteurCreux.multiplierParUnScalaire(y, scale2);
        double[]out = VecteurCreux.additionnerVecteurs(x, y);
        out = VecteurCreux.normer(out);
        
        System.out.println("les plus proche voisin du descripteur "+map_id_descripteur.get(idVecteurReference)+" * " + map_id_descripteur.get(idVecteurAddi));
        recupererNPlusProche(out, map_id_descripteur, vecteurs, metrique, nPlusProcheVoisin);
           
        Map<Integer, Set<Integer>> map_idO_idsDesc = OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, map_descripteur_id);
        Map<Integer, String> map_idO_titre = OeuvresUtils.getMap_idOeuvre_titre(oeuvres);        
        getOeuvrePlusProcheDeCible(vecteurs, out, map_idO_titre, map_idO_idsDesc, metrique, nPlusProcheVoisin);
    }
    
    public static void additionnerEtSoustraire(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, String descripteurAddition, String descripterSoustraire, MetriqueSim metrique, int nPlusProcheVoisin) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteursDescripteur = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, 0.0);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteursDescripteur = VectoriseurUtils.normerVecteurCreuxB(vecteursDescripteur.values());
        
        int idVecteurReference = map_descripteur_id.get(descripteurCible);
        int idVecteurAddition = map_descripteur_id.get(descripteurAddition);
        int idVecteurSoustraction = map_descripteur_id.get(descripterSoustraire);
        
        double[] vecteurX = VecteurCreux.additionnerVecteurs(vecteursDescripteur.get(idVecteurReference).getTabPonderation(), vecteursDescripteur.get(idVecteurAddition).getTabPonderation());
        vecteurX = VecteurCreux.normer(vecteurX);
        vecteurX = VecteurCreux.soustraireComplementOrthogonaux(vecteurX, vecteursDescripteur.get(idVecteurSoustraction).getTabPonderation());
        System.out.println("les plus proche voisin du descripteur "+map_id_descripteur.get(idVecteurReference)+" + " + map_id_descripteur.get(idVecteurAddition) +" - " +map_id_descripteur.get(idVecteurSoustraction));
        recupererNPlusProche(vecteurX, map_id_descripteur, vecteursDescripteur, metrique, nPlusProcheVoisin);
             
        Map<Integer, Set<Integer>> map_idO_idsDesc = OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, map_descripteur_id);
        Map<Integer, String> map_idO_titre = OeuvresUtils.getMap_idOeuvre_titre(oeuvres);        
        getOeuvrePlusProcheDeCible(vecteursDescripteur, vecteurX, map_idO_titre, map_idO_idsDesc, metrique, nPlusProcheVoisin);
    }
    
    public static void getSimilatite(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, String descripteurCovariable, MetriqueSim metrique) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurIndicie> vecteursDescripteur = VectoriseurUtils.vectoriserAvecCoefficientAssociation4(map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, true);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteursDescripteur = VectoriseurUtils.normerVecteurIndicie(vecteursDescripteur);
        
        Map<Integer, VecteurCreux> map_id_vecteur = VectoriseurUtils.convertir(vecteursDescripteur.values(), 0);
        int idVecteurReference = map_descripteur_id.get(descripteurCible);
        int idCoVecteur = map_descripteur_id.get(descripteurCovariable);
        double sim = metrique.calculerMetrique(map_id_vecteur.get(idVecteurReference).getTabPonderation(), map_id_vecteur.get(idCoVecteur).getTabPonderation());
        System.out.println("la similarite entre "+ descripteurCible +" et " + descripteurCovariable +" est de :"+ sim);
             
    }
    
    // cos((X-Y), (Y-X))
    public static void getOpposition1(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, MetriqueSim metrique, int nPlusProcheVoisin) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteurs = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, 0.0);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteurs = VectoriseurUtils.normerVecteurCreuxB(vecteurs.values());
        
        int idVec = map_descripteur_id.get(descripteurCible);
        
        for (int idCoVec: map_id_descripteur.keySet())
        {
            if (idVec==idCoVec) continue;
            double[] dif1 = vecteurs.get(idVec).soustraireComplementOrthogonaux(vecteurs.get(idCoVec).getTabPonderation());
            dif1 = VecteurCreux.normer(dif1);
            double[] dif2 = vecteurs.get(idCoVec).soustraireComplementOrthogonaux(vecteurs.get(idVec).getTabPonderation());
            dif2 = VecteurCreux.normer(dif2);
            double diff = metrique.calculerMetrique(dif1, dif2);
            System.out.println(map_id_descripteur.get(idVec)+ " et "+ map_id_descripteur.get(idCoVec) +" : "+diff);
        }
         
//        Map<Integer, Set<Integer>> map_idO_idsDesc = OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, String> map_idO_titre = OeuvresUtils.getMap_idOeuvre_titre(oeuvres);        
//        getOeuvrePlusProcheDeCible(vecteurs, vecteurX, map_idO_titre, map_idO_idsDesc, metrique, nPlusProcheVoisin);
    }
    
    // c = x*y
    // diff = (x-c)/-1 donne un score de difference
    // sim = cos(x,y)
    // out = 2*((sim*diff)/(sim+diff))  moyenne harminique entre diff et sim
    public static void getMetaphore(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, MetriqueSim metrique, int nPlusProcheVoisin) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteurs = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, 0.0);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteurs = VectoriseurUtils.normerVecteurCreuxB(vecteurs.values());
        
        int idVec = map_descripteur_id.get(descripteurCible);
        
        for (int idCoVec: map_id_descripteur.keySet())
        {
            if (idVec==idCoVec) continue;
            double[] c = VecteurCreux.multiplicationPointWise(vecteurs.get(idVec).getTabPonderation(), vecteurs.get(idCoVec).getTabPonderation());
            
            double[] dif1 = vecteurs.get(idVec).soustraireComplementOrthogonaux(c);
            dif1 = VecteurCreux.normer(dif1);
//            double a = metrique.calculerMetrique(vecteurs.get(idVec).getTabPonderation(), dif1);
            double diff = metrique.calculerMetrique(dif1, vecteurs.get(idCoVec).getTabPonderation());
            
            double[] dif2 = vecteurs.get(idCoVec).soustraireComplementOrthogonaux(c);
            dif2 = VecteurCreux.normer(dif2);
//            double diff = metrique.calculerMetrique(dif1, dif2);
            
            double sim = metrique.calculerMetrique(vecteurs.get(idVec).getTabPonderation(), vecteurs.get(idCoVec).getTabPonderation());
            
//            double score = metrique.calculerMetrique(dif1, dif2);
//            double x = metrique.calculerMetrique(vecteurs.get(idVec).getTabPonderation(), c);
//            double y = metrique.calculerMetrique(vecteurs.get(idCoVec).getTabPonderation(), c);
//            double out = ((x+y)/2)-score;
//            double out = sim-score;
//            double out = sim-diff;
            
            diff = diff/-1;
            double out = 2*((sim*diff)/(sim+diff));
            
            System.out.println(map_id_descripteur.get(idVec)+ " et "+ map_id_descripteur.get(idCoVec) +" : "+out +" : "+"sim : " + sim+" : "+"diff : " + diff);
//            System.out.println("sim : " + sim);
//            System.out.println("diff : " + diff);
//            System.out.println("score : " + score);
        }
         
//        Map<Integer, Set<Integer>> map_idO_idsDesc = OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, String> map_idO_titre = OeuvresUtils.getMap_idOeuvre_titre(oeuvres);        
//        getOeuvrePlusProcheDeCible(vecteurs, vecteurX, map_idO_titre, map_idO_idsDesc, metrique, nPlusProcheVoisin);
    }
    
    
    
    // sim((A-B), (C-D))
    // trouver le descripteur B qui maximise sim
    public static void getAnalogie1 (File fileOeuvre, CoefficientAssociation coefficient, String desc1, String descCible2, String coDesc2, MetriqueSim metrique, int nPlusProcheVoisin) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteursDescripteur = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, 0.0);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteursDescripteur = VectoriseurUtils.normerVecteurCreuxB(vecteursDescripteur.values());
        
        int idDesc1 = map_descripteur_id.get(desc1);
        int idDesc2 = map_descripteur_id.get(descCible2);
        int idCoDesc2 = map_descripteur_id.get(coDesc2);
        
        double[] diff2 = vecteursDescripteur.get(idDesc2).soustraireComplementOrthogonaux(vecteursDescripteur.get(idCoDesc2).getTabPonderation());
        diff2 = VecteurCreux.normer(diff2);
        double simMax = 0.0;
        int idDescOpti = -1;
        for (int idCoDesc1: map_id_descripteur.keySet())
        {
            if (idCoDesc1!=idDesc2 && idCoDesc1!=idDesc1 && idCoDesc1!=idCoDesc2)
            {
                double[] diff1 = vecteursDescripteur.get(idDesc1).soustraireComplementOrthogonaux(vecteursDescripteur.get(idCoDesc1).getTabPonderation());
                diff1 = VecteurCreux.normer(diff1);
                double sim = metrique.calculerMetrique(diff1, diff2);
                if (sim>simMax)
                {
                    idDescOpti=idCoDesc1;
                    simMax=sim;
                    System.out.println(desc1 +"-"+map_id_descripteur.get(idDescOpti)+ " et "+ descCible2+"-"+coDesc2+" : "+simMax);
                }
                
            }
            
        }
        System.out.println(desc1 +"-"+map_id_descripteur.get(idDescOpti)+ " et "+ descCible2+"-"+coDesc2+" : "+simMax);
        
//        System.out.println("similarite entre " + descCible1+"-"+coDesc1 + " et "+ descCible2+"-"+coDesc2+" : "+sim);
//        System.out.println("descripteurs plus similaires a "+ descCible1+"-"+coDesc1);
//        recupererNPlusProche(diff1, map_id_descripteur, vecteursDescripteur, metrique, nPlusProcheVoisin);
//        System.out.println("descripteurs plus similaires a "+ descCible2+"-"+coDesc2);
//        recupererNPlusProche(diff2, map_id_descripteur, vecteursDescripteur, metrique, nPlusProcheVoisin);
        
    }
    
    // sim((A-B), (C-D))
    // trouver lea descripteurs A-B qui maximisent sim((A-B,(C-D))
    public static void getAnalogie2 (File fileOeuvre, CoefficientAssociation coefficient, String descCible2, String coDesc2, MetriqueSim metrique) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteursDescripteur = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, 0.0);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteursDescripteur = VectoriseurUtils.normerVecteurCreuxB(vecteursDescripteur.values());
        
        int idDesc2 = map_descripteur_id.get(descCible2);
        int idCoDesc2 = map_descripteur_id.get(coDesc2);
        
        double[] diff2 = vecteursDescripteur.get(idDesc2).soustraireComplementOrthogonaux(vecteursDescripteur.get(idCoDesc2).getTabPonderation());
        diff2 = VecteurCreux.normer(diff2);
        double simMax = 0.0;
        int idDescMax = -1;
        int idCoMax=-1;
        for (int idDesc1: map_id_descripteur.keySet())
        {
            if (idDesc1==idDesc2 || idDesc1==idCoDesc2) continue;
            for (int idCoDesc1: map_id_descripteur.keySet())
            {
                if (idCoDesc1==idDesc2 || idCoDesc1==idCoDesc2 || idCoDesc1==idDesc1) continue;
                double[] diff1 = vecteursDescripteur.get(idDesc1).soustraireComplementOrthogonaux(vecteursDescripteur.get(idCoDesc1).getTabPonderation());
                diff1 = VecteurCreux.normer(diff1);
                double sim = metrique.calculerMetrique(diff1, diff2);
                if (sim>simMax)
                {
                    idDescMax=idDesc1;
                    idCoMax=idCoDesc1;
                    simMax=sim;
//                    System.out.println(idDesc1 +"-"+map_id_descripteur.get(idCoDesc1)+ " et "+ descCible2+"-"+coDesc2+" : "+simMax);
                }
            }
            
        }
        System.out.println(map_id_descripteur.get(idDescMax) +"-"+map_id_descripteur.get(idCoMax)+ " et "+ descCible2+"-"+coDesc2+" : "+simMax);
        
//        System.out.println("similarite entre " + descCible1+"-"+coDesc1 + " et "+ descCible2+"-"+coDesc2+" : "+sim);
//        System.out.println("descripteurs plus similaires a "+ descCible1+"-"+coDesc1);
//        recupererNPlusProche(diff1, map_id_descripteur, vecteursDescripteur, metrique, nPlusProcheVoisin);
//        System.out.println("descripteurs plus similaires a "+ descCible2+"-"+coDesc2);
//        recupererNPlusProche(diff2, map_id_descripteur, vecteursDescripteur, metrique, nPlusProcheVoisin);
        
    }
    
    // sim((B-b), (A-a))
    // trouver lea descripteurs b qui maximisent cos(b, B-A+a)
    public static void getAnalogie3 (File fileOeuvre, CoefficientAssociation coefficient, String cible1, String cible2, String coCible2, MetriqueSim metrique) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteursDescripteur = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, 0.0);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteursDescripteur = VectoriseurUtils.normerVecteurCreuxB(vecteursDescripteur.values());
        
        int idCible1 = map_descripteur_id.get(cible1);
        int idCible2 = map_descripteur_id.get(cible2);
        int idCoDesc2 = map_descripteur_id.get(coCible2);
                
        double[] x = vecteursDescripteur.get(idCible1).soustraireComplementOrthogonaux(vecteursDescripteur.get(idCible2).getTabPonderation());
        x = VecteurCreux.normer(x);
        x = VecteurCreux.additionnerVecteurs(x, vecteursDescripteur.get(idCoDesc2).getTabPonderation());
        x = VecteurCreux.normer(x);
        
        
//        double [] x = VecteurCreux.additionnerVecteurs(vecteursDescripteur.get(idCible2).getTabPonderation(), vecteursDescripteur.get(idCoDesc2).getTabPonderation());
//        x = VecteurCreux.normer(x);
//        x = vecteursDescripteur.get(idCible1).complementOrthogonaux(x);
//        x = VecteurCreux.normer(x);
        
        double simMax = 0.0;
        int idCoMax = -1;
        for (int idCo1: map_id_descripteur.keySet())
        {
            if (idCo1==idCible2 || idCo1==idCoDesc2||idCo1==idCible1) continue;
            double sim = metrique.calculerMetrique(vecteursDescripteur.get(idCo1).getTabPonderation(), x);
            if (sim>simMax)
            {
                idCoMax=idCo1;
                simMax=sim;
            }
                System.out.println(map_id_descripteur.get(idCible1) +"-"+map_id_descripteur.get(idCo1)+ " et "+ cible2+"-"+coCible2+" : "+sim);

            
            
        }
        System.out.println(map_id_descripteur.get(idCible1) +"-"+map_id_descripteur.get(idCoMax)+ " et "+ cible2+"-"+coCible2+" : "+simMax);
        
//        System.out.println("similarite entre " + descCible1+"-"+coDesc1 + " et "+ descCible2+"-"+coDesc2+" : "+sim);
//        System.out.println("descripteurs plus similaires a "+ descCible1+"-"+coDesc1);
//        recupererNPlusProche(diff1, map_id_descripteur, vecteursDescripteur, metrique, nPlusProcheVoisin);
//        System.out.println("descripteurs plus similaires a "+ descCible2+"-"+coDesc2);
//        recupererNPlusProche(diff2, map_id_descripteur, vecteursDescripteur, metrique, nPlusProcheVoisin);
        
    }
    
    // sim((B-b), (A-a))
    // trouver lea descripteurs b qui maximisent cos(b, B-A+a)
    public static void getAnalogie3b (File fileOeuvre, CoefficientAssociation coefficient, String cible1, String cible2, String coCible2, MetriqueSim metrique) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteursDescripteur = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, 0.0);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteursDescripteur = VectoriseurUtils.normerVecteurCreuxB(vecteursDescripteur.values());
        
        int idCible1 = map_descripteur_id.get(cible1); //fille
        int idCible2 = map_descripteur_id.get(cible2); // garcons
        int idCoDesc2 = map_descripteur_id.get(coCible2); // homme
                
        double[] vecCible1 = vecteursDescripteur.get(idCible1).getTabPonderation();
        double[] vecCible2 = vecteursDescripteur.get(idCible2).getTabPonderation();
        double[] vecCo2 = vecteursDescripteur.get(idCoDesc2).getTabPonderation();
        double[] vecCo1;   
        
        double simMax = 0.0;
        int idCoMax = -1;
        for (int idCo1: map_id_descripteur.keySet())
        {
            if (idCo1==idCible2 || idCo1==idCoDesc2||idCo1==idCible1) continue;
            vecCo1 = vecteursDescripteur.get(idCo1).getTabPonderation();
            
            double simCo1_cible1 = metrique.calculerMetrique(vecCo1, vecCible1);
            double simCo1_cible2 = metrique.calculerMetrique(vecCo1, vecCible2);
            double simCo1_co2 = metrique.calculerMetrique(vecCo1, vecCo2);
            
            double sim = simCo1_cible1-simCo1_cible2+simCo1_co2;
            
            if (sim>simMax)
            {
                idCoMax=idCo1;
                simMax=sim;
            }
                System.out.println(map_id_descripteur.get(idCible1) +"-"+map_id_descripteur.get(idCo1)+ " et "+ cible2+"-"+coCible2+" : "+sim);

            
            
        }
        System.out.println(map_id_descripteur.get(idCible1) +"-"+map_id_descripteur.get(idCoMax)+ " et "+ cible2+"-"+coCible2+" : "+simMax);
        
//        System.out.println("similarite entre " + descCible1+"-"+coDesc1 + " et "+ descCible2+"-"+coDesc2+" : "+sim);
//        System.out.println("descripteurs plus similaires a "+ descCible1+"-"+coDesc1);
//        recupererNPlusProche(diff1, map_id_descripteur, vecteursDescripteur, metrique, nPlusProcheVoisin);
//        System.out.println("descripteurs plus similaires a "+ descCible2+"-"+coDesc2);
//        recupererNPlusProche(diff2, map_id_descripteur, vecteursDescripteur, metrique, nPlusProcheVoisin);
        
    }
    
    public static void additionner(final Map<Integer, String> map_idUnif_unifString, final Map<Integer, VecteurCreux> map_id_vecteur, int idVecteurReference, int idVecteurAdditionne, MetriqueSim metrique, int nPlusProcheVoisin)
    {
        double[] vecteurX = VecteurCreux.additionnerVecteurs(map_id_vecteur.get(idVecteurReference).getTabPonderation(), map_id_vecteur.get(idVecteurAdditionne).getTabPonderation());
//        Map<Integer, Double> map_id_similitude = KPlusProcheVoisin.kPlusProcheVoisinDeX(map_id_vecteur, vecteurX, nPlusProcheVoisin, metrique);
        System.out.println("les plus proche voisin du descripteur "+map_idUnif_unifString.get(idVecteurReference)+" additionner avec " + map_idUnif_unifString.get(idVecteurAdditionne));
        recupererNPlusProche(vecteurX, map_idUnif_unifString, map_id_vecteur, metrique, nPlusProcheVoisin);
        
        
    }
    
    public static void moyenne(final Map<Integer, String> map_idUnif_unifString, final Map<Integer, VecteurCreux> map_id_vecteur, int idVecteurReference, int idVecteurSoustraire, MetriqueSim metrique, int nPlusProcheVoisin)
    {
        double[] vecteurX = VecteurCreux.moyenneVecteurs(map_id_vecteur.get(idVecteurReference).getTabPonderation(), map_id_vecteur.get(idVecteurSoustraire).getTabPonderation());
        System.out.println("les plus proche voisin de la moyenne des descripteurs "+map_idUnif_unifString.get(idVecteurReference)+" et " + map_idUnif_unifString.get(idVecteurSoustraire));
        recupererNPlusProche(vecteurX, map_idUnif_unifString, map_id_vecteur, metrique, nPlusProcheVoisin);
    }
    
    // recuperer les plus simipaires qui ne sont pas cooccurrents avec la cible
    public static void trouverOeuvreSansCible (File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, MetriqueSim metrique, int n) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteurs = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, OeuvresUtils.getIdOeuvres(oeuvres), coefficient, 0.0);
        vecteurs = VectoriseurUtils.normerVecteurCreuxB(vecteurs.values());
        VecteurCreux vecteurCible = vecteurs.get(map_descripteur_id.get(descripteurCible));
        Set<Integer> oeuvresSansCible = OeuvresUtils.getIdOeuvreSansCible(oeuvres, descripteurCible);
        
        Map<Integer, Set<Integer>> map_idO_idsDesc = OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, oeuvresSansCible, map_descripteur_id);
        Map<Integer, String> map_idO_titre = OeuvresUtils.getMap_idOeuvre_titre(oeuvres);        
        getOeuvrePlusProcheDeCible(vecteurs, vecteurCible.getTabPonderation(), map_idO_titre, map_idO_idsDesc, metrique, n);
    }
    
    // recuperer les oeuvre plus simipaires qui contiennent la cible
    public static void trouverOeuvreAvecCible (File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, MetriqueSim metrique, int n) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteurs = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, OeuvresUtils.getIdOeuvres(oeuvres), coefficient, 0.0);
        vecteurs = VectoriseurUtils.normerVecteurCreuxB(vecteurs.values());
        VecteurCreux vecteurCible = vecteurs.get(map_descripteur_id.get(descripteurCible));
        Set<Integer> oeuvresSansCible = OeuvresUtils.getIdOeuvreAvecCible(oeuvres, descripteurCible);
        
        Map<Integer, Set<Integer>> map_idO_idsDesc = OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, oeuvresSansCible, map_descripteur_id);
        Map<Integer, String> map_idO_titre = OeuvresUtils.getMap_idOeuvre_titre(oeuvres);        
        getOeuvrePlusProcheDeCible(vecteurs, vecteurCible.getTabPonderation(), map_idO_titre, map_idO_idsDesc, metrique, n);
    }
    
    // recuperer les plus simipaires qui ne sont pas cooccurrents avec la cible
    public static void trouverOeuvreAvecMetaphoreDeCible (File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, String coDescripteur, MetriqueSim metrique, int n) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteurs = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, OeuvresUtils.getIdOeuvres(oeuvres), coefficient, 0.0);
        vecteurs = VectoriseurUtils.normerVecteurCreuxB(vecteurs.values());
        VecteurCreux vecteurCible = vecteurs.get(map_descripteur_id.get(descripteurCible));
        VecteurCreux coVecteur = vecteurs.get(map_descripteur_id.get(coDescripteur));
        double[] c = vecteurCible.multiplicationSimple(coVecteur.getTabPonderation());
        c = VecteurCreux.normer(c);
        double[] diffCo = coVecteur.soustraireComplementOrthogonaux(c);
        diffCo = VecteurCreux.normer(diffCo);
        double [] somme = VecteurCreux.additionnerVecteurs(vecteurCible.getTabPonderation(), coVecteur.getTabPonderation());
        somme = VecteurCreux.soustraireComplementOrthogonaux(somme, diffCo);
        
        Set<Integer> oeuvresSansCible = OeuvresUtils.getIdOeuvreSansCible(oeuvres, descripteurCible);
        Map<Integer, Set<Integer>> map_idO_idsDesc = OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, oeuvresSansCible, map_descripteur_id);
        Map<Integer, String> map_idO_titre = OeuvresUtils.getMap_idOeuvre_titre(oeuvres);        
        getOeuvrePlusProcheDeCible(vecteurs, somme, map_idO_titre, map_idO_idsDesc, metrique, n);
    }
    
    public static void getOeuvrePlusProcheDeCible(Map<Integer, VecteurCreux> vecteurs, double[] vecteurCible, Map<Integer, String> map_idO_titre, Map<Integer, Set<Integer>> map_idO_idsDesc, MetriqueSim metrique, int n)
    {
        vecteurs = VectoriseurUtils.additionnerVecteursCreux(map_idO_idsDesc, vecteurs, map_descripteur_id.size());
        vecteurs = VectoriseurUtils.normerVecteurCreuxB(vecteurs.values());        
        recupererNPlusProche(vecteurCible, map_idO_titre, vecteurs, metrique, n);
    }

    public static Map<String, Integer> getMap_descripteur_id() {
        return map_descripteur_id;
    }

    public static Map<Integer, String> getMap_id_descripteur() {
        return map_id_descripteur;
    }
    
    
    
}
