/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte.Analyses;

import AccroissementVocabulaire.AccroissementVocabulaireVoSurVt;
import Affichage.Tableau.AfficherTableau;
import Affichage.Tableau.AfficheurTableau;
import Affichage.Tableau.AfficheurVecteur;
import AnalyseSimilitude.AnalyseSimilitude;
import Classification.Classe;
import Classification.Partition;
import ClassificationAutomatique.NonSupervisee.KMeans.ClassifieurKmeans;
import ClassificationAutomatique.NonSupervisee.KMeans.ClassifieurKmeansAvecMultiplication;
import ClassificationAutomatique.NonSupervisee.KMeans.ClassifieurKmeansMaximum;
import ClassificationAutomatique.NonSupervisee.KMeans.ClassifieurKmeansRepete;
import ClassificationAutomatique.NonSupervisee.KMeans.MultiClassifieurKmeansMaximum;
import ClassificationAutomatique.NonSupervisee.WrapperCommonMath.WrapperClassifieur;
import ClassificationAutomatique.Supervisee.KPPV.KPlusProcheVoisin;
import Evaluateur.ApproximateurNombreClasse.ApproximateurNombreClasse;
import Evaluateur.AvecCentroide.EvaluateurAvecCentroide;
import Evaluateur.AvecCentroide.Gap.EvaluateurGap;
import Evaluateur.AvecCentroide.Gap.EvaluateurGapAvecKmeans;
import Evaluateur.AvecCentroide.Gap.EvaluateurGapDistUniforme;
import Evaluateur.AvecCentroide.Silhouette.EvaluateurSilhouette;
import Evaluateur.AvecCentroide.Silhouette.EvaluateurSilhouetteK;
import Evaluateur.AvecCentroide.VarianceRatio.EvaluateurRatioVariance;
import MDS.MDS;
import Matrice.VecteurCreux.MatriceCreuse;
import Matrice.Vectorisateur.VecteurCreux;
import Matrice.Vectorisateur.VecteurIndicie;
import Matrice.Vectorisateur.VectoriseurUtils;
import Metrique.Distance.DistanceCosinus;
import Metrique.Distance.DistanceCosinusCommonMath;
import Metrique.Distance.DistanceCosinusIndicie;
import Metrique.Distance.DistanceEuclidienne;
import Metrique.Distance.DistanceManhattan;
import Metrique.Distance.DistanceManhattanIndicie;
import Metrique.Distance.DistanceManhattanPondere;
import Metrique.Distance.DistanceManhattanPondereCommonMath;
import Metrique.Distance.DistanceMinkowskiCommonMath;
import Metrique.Distance.ErreurQuadratique;
import Metrique.Distance.ProduitScalaireInverse;
import Metrique.Metrique;
import Metrique.Similitude.CorrelationPearsonIndicie;
import Metrique.Similitude.Cosinus;
import Metrique.Similitude.CosinusIndicie;
import Metrique.Similitude.EuclidienneInverseIndicie;
import Metrique.Similitude.ManhattanInverse;
import Metrique.Similitude.MetriqueIndicieSim;
import Metrique.Similitude.MetriqueSim;
import Metrique.Similitude.ProduitScalaire;
import Ponderation.CoefficientAssociation;
import Ponderation.CoefficientChi2;
import Ponderation.CorrelationMatthews;
import Ponderation.InformationMutuelle;
import Ponderation.PonderateurParClasse;
import Ponderation.Ponderation;
import UtilsJFC.TrieUtils;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import magritte.Analyses.Componentielle.OLD.Decomposeur1;
import magritte.Analyses.Componentielle.OLD.Decomposeur2;
import magritte.Analyses.Componentielle.Decomposeur6;
import magritte.Oeuvre;
import magritte.OeuvresUtils;
import magritte.ParserMagritte;
import magritte.Pretraitement;
import no.uib.cipr.matrix.NotConvergedException;
import org.apache.commons.math3.ml.clustering.Clusterer;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.FuzzyKMeansClusterer;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.clustering.MultiKMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.clustering.evaluation.SumOfClusterVariances;
import org.apache.commons.math3.ml.distance.CanberraDistance;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EarthMoversDistance;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.ml.distance.ManhattanDistance;
import org.apache.commons.math3.random.MersenneTwister;

/**
 *
 * @author JF Chartier
 */
public class AnalyseThematique 
{
    public static void main(String[] args) throws IOException, NotConvergedException 
    {
//        AnalyseK.approximerKRatioVariance(OeuvresUtils.getFileCorpus(), new Cosinus(), 2, 40, 5);
//        AnalyseThematique.analyse1(OeuvresUtils.getFileCorpus(), new Cosinus(), new InformationMutuelle(), 30, 10);
//        
//        AnalyseThematique.analyse2(OeuvresUtils.getFileCorpus(), new Cosinus(), new CorrelationMatthews(), 30, 10);
//        AnalyseThematique.analyse3(OeuvresUtils.getFileCorpus(), new Cosinus(), new CorrelationMatthews(), 20, 5, 200);
//        AnalyseThematique.analyse4(OeuvresUtils.getFileCorpus(), new DBSCANClusterer(450.80, 2), new CorrelationMatthews());
//        AnalyseThematique.analyse5(OeuvresUtils.getFileCorpus(), new Cosinus(), new CorrelationMatthews(), 20, 5);
//        AnalyseThematique.analyse6(OeuvresUtils.getFileCorpus(), new Cosinus(), new CorrelationMatthews(), 30, 5, 200);
//        AnalyseThematique.analyse7(OeuvresUtils.getFileCorpus(), new FuzzyKMeansClusterer(20, 1.001), new CorrelationMatthews(), new Cosinus());
//        AnalyseThematique.analyse8(OeuvresUtils.getFileCorpus(), new Cosinus(), new CorrelationMatthews(), 30, 5, 200);
//        AnalyseThematique.analyse9(OeuvresUtils.getFileCorpus(), new FuzzyKMeansClusterer(20, 1.5), new Cosinus(), new InformationMutuelle());
//        AnalyseThematique.analyse11(OeuvresUtils.getFileCorpus(), new ProduitScalaire(), new InformationMutuelle(), 20, 20);
        AnalyseThematique.analyse12(OeuvresUtils.getFileCorpus(), new ProduitScalaire(), new InformationMutuelle(), 20, 20);
//        AnalyseThematique.analyse13(OeuvresUtils.getFileCorpus(), new ProduitScalaire(), new InformationMutuelle(), 337, 20, 20);
    
    }
    
    
    
    
    // classification de descripteur en fonction des descripteurs coocurrents specifiques     
    public static Map<Integer, Classe> analyse1 (File fileOeuvre, MetriqueSim metrique, CoefficientAssociation coefficient, int kClasse, int iterationKm) throws IOException
    {
        Collection<Oeuvre> oeuvres = ParserMagritte.recupererOeuvre(fileOeuvre);
        Pretraitement pre = new Pretraitement(false, false, false, false, -1, -1, 99999999, true, true);
        oeuvres = pre.filtrerOeuvres(oeuvres);
        Map<String, Integer> map_descripteur_id = OeuvresUtils.getMap_descripteur_id(oeuvres);
        new AfficherTableau("Descripteurs de Magritte", "descripteur", "ID", map_descripteur_id).setVisible(true);
        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
        Map<Integer, VecteurIndicie> vecteurs = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteurs = VectoriseurUtils.dichotomiserSeuilMax(0.00000, vecteurs.values());
        
        AnalyseK.approximerK(new EvaluateurRatioVariance(VectoriseurUtils.convertir(vecteurs.values(), 0), new ErreurQuadratique()), new Cosinus(), 2, 60, 10);
//        MDS.calculerReductionDimensionelle(VectoriseurUtils.convertir(vecteurs.values(), 0.0), new DistanceEuclidienne());
//        System.exit(1);
        ClassifieurKmeansMaximum classifieur = new ClassifieurKmeansMaximum(VectoriseurUtils.convertir(vecteurs.values(), 0.0), metrique, kClasse, 500, 0, true);
        TreeMap<Integer, Classe> map_id_classe = classifieur.partitionnerMax(iterationKm);
        
        TreeMap<Integer, Integer> map_idDescripteur_idClasse = Partition.recupererMap_IdDomif_IdClasse(map_id_classe);
        new AfficherTableau("Le idClasse pour chaque idDescripteur d'une partition", "ID Descripteur", "ID CLASSE", map_idDescripteur_idClasse).setVisible(true);
        TreeMap<Integer, Integer> map_idClasse_nbreDescripteur = Partition.recupererMap_idClasse_nbreDomif(map_id_classe);
        new AfficherTableau("Nombre de descripteur par classe", "idClasse", "nbre de descripteur", map_idClasse_nbreDescripteur).setVisible(true);
//        TreeMap<Integer, Double> map_idDescripteur_proximiteCentroide = Partition.calculerEcartAuCentroide(VectoriseurUtils.convertir(vecteurs.values(), 0.0), metrique, map_id_classe);
//        new AfficherTableau("Proximite de chaque descripteur a son centroide", "idDescripteur", "proximite avec le centroide", map_idDescripteur_proximiteCentroide).setVisible(true);
//        AfficheurTableau.afficher4Colonnes("Proximite de chaque descripteur a son centroide", "idDescripteur", "descripteur","proximite avec le centroide","classe", TriageCollection.inverserMap(map_descripteur_id), map_idDescripteur_proximiteCentroide, map_idDescripteur_idClasse);
        AfficheurVecteur.afficherVecteurs("proximite du descripteur avec centroide", TrieUtils.inverserMap(map_descripteur_id), map_id_classe.keySet(), Partition.calculerMetriqueEntreVecteurEtCentroidesDesClasses(VectoriseurUtils.convertir(vecteurs.values(), 0.0), map_id_classe, metrique).values());
        
        Map<Integer, Set<Integer>> map_idClasse_ensembleIdDescripteur = Partition.recupererMap_IdClasse_ensembleIdDomif(map_id_classe);
        Map<Integer, Set<Integer>> map_idDescripteur_ensembleIdDescripteurCooc = OeuvresUtils.getMap_idDescripteur_ensembleIdDescripteurCooccurrent(oeuvres, map_descripteur_id);
        Map<Integer, VecteurIndicie> map_idDescripteur_vecteurIdClasseAssocie = VectoriseurUtils.vectoriserAvecCoefficientAssociation2(map_idDescripteur_ensembleIdDescripteurCooc, map_idClasse_ensembleIdDescripteur, map_idDescripteur_ensembleIdDescripteurCooc.keySet(), new CorrelationMatthews());
        AfficheurVecteur.afficherVecteurs("coefficient association entre descripteur et classe", TrieUtils.inverserMap(map_descripteur_id), map_idClasse_ensembleIdDescripteur.keySet(), map_idDescripteur_vecteurIdClasseAssocie.values());
                
        return map_id_classe;
    }
    
    // classer des oeuvres sans reduction dimensionnelle
    public static Map<Integer, Classe> analyse2 (File fileOeuvre, MetriqueSim metrique, CoefficientAssociation coefficient, int kClasse, int iterationKm) throws IOException
    {
        Collection<Oeuvre> oeuvres = ParserMagritte.recupererOeuvre(fileOeuvre);
        Pretraitement pre = new Pretraitement(false, false, false, false, -1, -1, 99999999, true, true);
        oeuvres = pre.filtrerOeuvres(oeuvres);
        
        Map<String, Integer> map_descripteur_id = OeuvresUtils.getMap_descripteur_id(oeuvres);
        Map<Integer, VecteurIndicie> map_idOeuvre_vecteurIdDescripteur = OeuvresUtils.getMap_idOeuvre_vecteurIdDescripteurBM25(oeuvres, map_descripteur_id);
//        map_idOeuvre_vecteurIdDescripteur = VectoriseurUtils.dichotomiser(0.000001, map_idOeuvre_vecteurIdDescripteur.values());
        
        ClassifieurKmeansMaximum classifieur = new ClassifieurKmeansMaximum(VectoriseurUtils.convertir(map_idOeuvre_vecteurIdDescripteur.values(), 0.0), metrique, kClasse, 500, 0, true);
        TreeMap<Integer, Classe> map_id_classe = classifieur.partitionnerMax(iterationKm);
        
        TreeMap<Integer, Integer> map_idOeuvre_idClasse = Partition.recupererMap_IdDomif_IdClasse(map_id_classe);
        new AfficherTableau("Le idClasse pour chaque idOeuvre", "ID oeuvre", "ID CLASSE", map_idOeuvre_idClasse).setVisible(true);
        TreeMap<Integer, Integer> map_idClasse_nbreOeuvre = Partition.recupererMap_idClasse_nbreDomif(map_id_classe);
        new AfficherTableau("Nombre d'oeuvre par classe", "idClasse", "nbre oeuvre", map_idClasse_nbreOeuvre).setVisible(true);
        TreeMap<Integer, Double> map_idOeuvre_proximiteCentroide = Partition.calculerEcartAuCentroide(VectoriseurUtils.convertir(map_idOeuvre_vecteurIdDescripteur.values(), 0.0), metrique, map_id_classe);
        new AfficherTableau("Proximite de chaque oeuvre a son centroide", "idOeuvre", "proximite avec le centroide", map_idOeuvre_proximiteCentroide).setVisible(true);
        
        Map<Integer, Set<Integer>> map_idClasse_ensembleIdOeuvre = Partition.recupererMap_IdClasse_ensembleIdDomif(map_id_classe);
        Map<Integer, Set<Integer>> map_idDescripteur_ensembleIdOeuvre = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurIndicie> map_idDescripteur_vecteurIdClasseAssocie = VectoriseurUtils.vectoriserAvecCoefficientAssociation2(map_idDescripteur_ensembleIdOeuvre, map_idClasse_ensembleIdOeuvre, OeuvresUtils.getIdOeuvres(oeuvres), coefficient);
        AfficheurVecteur.afficherVecteurs("coefficient association entre descripteur et classe", TrieUtils.inverserMap(map_descripteur_id), map_idClasse_ensembleIdOeuvre.keySet(), map_idDescripteur_vecteurIdClasseAssocie.values());
                
        return map_id_classe;
    }
    
    // classer des oeuvre avec reduction dimensionnelle
    public static Map<Integer, Classe> analyse3 (File fileOeuvre, MetriqueSim metrique, CoefficientAssociation coefficient, int kClasse, int iterationKm, int facteurs) throws IOException, NotConvergedException
    {
        Collection<Oeuvre> oeuvres = ParserMagritte.recupererOeuvre(fileOeuvre);
        oeuvres = Pretraitement.filtrerDescripteurMot(oeuvres);
        Map<String, Integer> map_descripteur_id = OeuvresUtils.getMap_descripteur_id(oeuvres);
        Map<Integer, VecteurIndicie> map_idOeuvre_vecteurIdDescripteur = OeuvresUtils.getMap_idOeuvre_vecteurIdDescripteurBM25(oeuvres, map_descripteur_id);
//        map_idOeuvre_vecteurIdDescripteur = VectoriseurUtils.dichotomiser(0.000001, map_idOeuvre_vecteurIdDescripteur.values());
        Map<Integer, VecteurCreux> vecteurs = VectoriseurUtils.convertir(map_idOeuvre_vecteurIdDescripteur.values(), 0);
        vecteurs = Pretraitement.reduireViaSvd(vecteurs, map_descripteur_id.size(), map_idOeuvre_vecteurIdDescripteur.size(), facteurs);
        ClassifieurKmeansMaximum classifieur = new ClassifieurKmeansMaximum(vecteurs, metrique, kClasse, 500, 0, true);
        TreeMap<Integer, Classe> map_id_classe = classifieur.partitionnerMax(iterationKm);
                
        TreeMap<Integer, Integer> map_idOeuvre_idClasse = Partition.recupererMap_IdDomif_IdClasse(map_id_classe);
        new AfficherTableau("Le idClasse pour chaque idOeuvre", "ID oeuvre", "ID CLASSE", map_idOeuvre_idClasse).setVisible(true);
        TreeMap<Integer, Integer> map_idClasse_nbreOeuvre = Partition.recupererMap_idClasse_nbreDomif(map_id_classe);
        new AfficherTableau("Nombre d'oeuvre par classe", "idClasse", "nbre oeuvre", map_idClasse_nbreOeuvre).setVisible(true);
        TreeMap<Integer, Double> map_idOeuvre_proximiteCentroide = Partition.calculerEcartAuCentroide(VectoriseurUtils.convertir(map_idOeuvre_vecteurIdDescripteur.values(), 0.0), metrique, map_id_classe);
        new AfficherTableau("Proximite de chaque oeuvre a son centroide", "idOeuvre", "proximite avec le centroide", map_idOeuvre_proximiteCentroide).setVisible(true);
        
        Map<Integer, Set<Integer>> map_idClasse_ensembleIdOeuvre = Partition.recupererMap_IdClasse_ensembleIdDomif(map_id_classe);
        Map<Integer, Set<Integer>> map_idDescripteur_ensembleIdOeuvre = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurIndicie> map_idDescripteur_vecteurIdClasseAssocie = VectoriseurUtils.vectoriserAvecCoefficientAssociation2(map_idDescripteur_ensembleIdOeuvre, map_idClasse_ensembleIdOeuvre, OeuvresUtils.getIdOeuvres(oeuvres), coefficient);
        AfficheurVecteur.afficherVecteurs("coefficient association entre descripteur et classe", TrieUtils.inverserMap(map_descripteur_id), map_idClasse_ensembleIdOeuvre.keySet(), map_idDescripteur_vecteurIdClasseAssocie.values());
                
        return map_id_classe;
    }
    
    // classer des oeuvre sans reduction dimensionnelle
    public static Map<Integer, Classe> analyse4 (File fileOeuvre, Clusterer classifieur, CoefficientAssociation coefficient) throws IOException
    {
        Collection<Oeuvre> oeuvres = ParserMagritte.recupererOeuvre(fileOeuvre);
        Map<String, Integer> map_descripteur_id = OeuvresUtils.getMap_descripteur_id(oeuvres);
        Map<Integer, VecteurIndicie> map_idOeuvre_vecteurIdDescripteur = OeuvresUtils.getMap_idOeuvre_vecteurIdDescripteurBM25(oeuvres, map_descripteur_id);
//        map_idOeuvre_vecteurIdDescripteur = VectoriseurUtils.dichotomiser(0.000001, map_idOeuvre_vecteurIdDescripteur.values());
        
        
        TreeMap<Integer, Classe> map_id_classe = new WrapperClassifieur().partitionner(classifieur, VectoriseurUtils.convertir(map_idOeuvre_vecteurIdDescripteur.values(), 0).values());
//        ClassifieurKmeansMaximum classifieur = new ClassifieurKmeansMaximum(VectoriseurUtils.convertir(map_idOeuvre_vecteurIdDescripteur.values(), 0.0), metrique, kClasse, 500, 0, true);
//        TreeMap<Integer, Classe> map_id_classe = classifieur.partitionnerMax(iterationKm);
        
//        TreeMap<Integer, Integer> map_idOeuvre_idClasse = Partition.recupererMap_IdDomif_IdClasse(map_id_classe);
//        new AfficherTableau("Le idClasse pour chaque idOeuvre", "ID oeuvre", "ID CLASSE", map_idOeuvre_idClasse).setVisible(true);
        TreeMap<Integer, Integer> map_idClasse_nbreOeuvre = Partition.recupererMap_idClasse_nbreDomif(map_id_classe);
        new AfficherTableau("Nombre d'oeuvre par classe", "idClasse", "nbre oeuvre", map_idClasse_nbreOeuvre).setVisible(true);
//        TreeMap<Integer, Double> map_idOeuvre_proximiteCentroide = Partition.calculerEcartAuCentroide(VectoriseurUtils.convertir(map_idOeuvre_vecteurIdDescripteur.values(), 0.0), metrique, map_id_classe);
//        new AfficherTableau("Proximite de chaque oeuvre a son centroide", "idOeuvre", "proximite avec le centroide", map_idOeuvre_proximiteCentroide).setVisible(true);
        
        Map<Integer, Set<Integer>> map_idClasse_ensembleIdOeuvre = Partition.recupererMap_IdClasse_ensembleIdDomif(map_id_classe);
        Map<Integer, Set<Integer>> map_idDescripteur_ensembleIdOeuvre = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurIndicie> map_idDescripteur_vecteurIdClasseAssocie = VectoriseurUtils.vectoriserAvecCoefficientAssociation2(map_idDescripteur_ensembleIdOeuvre, map_idClasse_ensembleIdOeuvre, OeuvresUtils.getIdOeuvres(oeuvres), coefficient);
        AfficheurVecteur.afficherVecteurs("coefficient association entre descripteur et classe", TrieUtils.inverserMap(map_descripteur_id), map_idClasse_ensembleIdOeuvre.keySet(), map_idDescripteur_vecteurIdClasseAssocie.values());
                
        return map_id_classe;
    }
    
    // classe des descripteurs et extrait les descripteurs specifiques aux classes
    public static Map<Integer, Classe> analyse5 (File fileOeuvre, MetriqueSim metrique, CoefficientAssociation coefficient, int kClasse, int iterationKm) throws IOException
    {
        Collection<Oeuvre> oeuvres = ParserMagritte.recupererOeuvre(fileOeuvre);
        oeuvres = Pretraitement.filtrerDescripteurMot(oeuvres);
        Map<String, Integer> map_descripteur_id = OeuvresUtils.getMap_descripteur_id(oeuvres);
        new AfficherTableau("Descripteurs de Magritte", "descripteur", "ID", map_descripteur_id).setVisible(true);
        Map<Integer, VecteurIndicie> map_idDescripteur_vecteurIdOeuvre = OeuvresUtils.getMap_idDescripteur_vecteurIdOeuvreFREQ(oeuvres, map_descripteur_id);
//        map_idOeuvre_vecteurIdDescripteur = VectoriseurUtils.dichotomiser(0.000001, map_idOeuvre_vecteurIdDescripteur.values());
        
        ClassifieurKmeansMaximum classifieur = new ClassifieurKmeansMaximum(VectoriseurUtils.convertir(map_idDescripteur_vecteurIdOeuvre.values(), 0.0), metrique, kClasse, 500, 0, true);
        TreeMap<Integer, Classe> map_id_classe = classifieur.partitionnerMax(iterationKm);
        
        TreeMap<Integer, Integer> map_idDescripteur_idClasse = Partition.recupererMap_IdDomif_IdClasse(map_id_classe);
        new AfficherTableau("Le idClasse pour chaque idDescripteur", "ID descripteur", "ID CLASSE", map_idDescripteur_idClasse).setVisible(true);
        TreeMap<Integer, Integer> map_idClasse_nbreDescripteur = Partition.recupererMap_idClasse_nbreDomif(map_id_classe);
        new AfficherTableau("Nombre de descripteur par classe", "idClasse", "nbre descripteur", map_idClasse_nbreDescripteur).setVisible(true);
        TreeMap<Integer, Double> map_idDescripteur_proximiteCentroide = Partition.calculerEcartAuCentroide(VectoriseurUtils.convertir(map_idDescripteur_vecteurIdOeuvre.values(), 0.0), metrique, map_id_classe);
        new AfficherTableau("Proximite de chaque descripteur a son centroide", "idOeuvre", "proximite avec le centroide", map_idDescripteur_proximiteCentroide).setVisible(true);
        
        Map<Integer, Set<Integer>> map_idClasse_ensembleIdDescripteur = Partition.recupererMap_IdClasse_ensembleIdDomif(map_id_classe);
        Map<Integer, Set<Integer>> map_idClasse_ensembleIdOeuvre = OeuvresUtils.getMap_idClasse_ensembleIdOeuvre(map_idClasse_ensembleIdDescripteur, OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id));
        Map<Integer, Set<Integer>> map_idDescripteur_ensembleIdOeuvre = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurIndicie> map_idDescripteur_vecteurIdClasseAssocie = VectoriseurUtils.vectoriserAvecCoefficientAssociation2(map_idDescripteur_ensembleIdOeuvre, map_idClasse_ensembleIdOeuvre, OeuvresUtils.getIdOeuvres(oeuvres), coefficient);
        AfficheurVecteur.afficherVecteurs("coefficient association entre descripteur et classe", TrieUtils.inverserMap(map_descripteur_id), map_idClasse_ensembleIdDescripteur.keySet(), map_idDescripteur_vecteurIdClasseAssocie.values());
                
        return map_id_classe;
    }
    
    // classification des descripteur avec reduction dimensionnelle
    public static Map<Integer, Classe> analyse6 (File fileOeuvre, MetriqueSim metrique, CoefficientAssociation coefficient, int kClasse, int iterationKm, int facteurs) throws IOException, NotConvergedException
    {
        Collection<Oeuvre> oeuvres = ParserMagritte.recupererOeuvre(fileOeuvre);
        oeuvres = Pretraitement.filtrerDescripteurMot(oeuvres);
        Map<String, Integer> map_descripteur_id = OeuvresUtils.getMap_descripteur_id(oeuvres);
        new AfficherTableau("Descripteurs de Magritte", "descripteur", "ID", map_descripteur_id).setVisible(true);
        Map<Integer, VecteurIndicie> map_idDescripteur_vecteurIdOeuvre = OeuvresUtils.getMap_idDescripteur_vecteurIdOeuvreFREQ(oeuvres, map_descripteur_id);
//        map_idOeuvre_vecteurIdDescripteur = VectoriseurUtils.dichotomiser(0.000001, map_idOeuvre_vecteurIdDescripteur.values());
        
        Map<Integer, VecteurCreux> vecteurs = VectoriseurUtils.convertir(map_idDescripteur_vecteurIdOeuvre.values(), 0);
        vecteurs = Pretraitement.reduireViaSvd(vecteurs, map_descripteur_id.size(), map_idDescripteur_vecteurIdOeuvre.size(), facteurs);
                
        ClassifieurKmeansMaximum classifieur = new ClassifieurKmeansMaximum(vecteurs, metrique, kClasse, 500, 0, true);
        TreeMap<Integer, Classe> map_id_classe = classifieur.partitionnerMax(iterationKm);
        
        TreeMap<Integer, Integer> map_idDescripteur_idClasse = Partition.recupererMap_IdDomif_IdClasse(map_id_classe);
        new AfficherTableau("Le idClasse pour chaque idDescripteur", "ID descripteur", "ID CLASSE", map_idDescripteur_idClasse).setVisible(true);
        TreeMap<Integer, Integer> map_idClasse_nbreDescripteur = Partition.recupererMap_idClasse_nbreDomif(map_id_classe);
        new AfficherTableau("Nombre de descripteur par classe", "idClasse", "nbre descripteur", map_idClasse_nbreDescripteur).setVisible(true);
        TreeMap<Integer, Double> map_idDescripteur_proximiteCentroide = Partition.calculerEcartAuCentroide(VectoriseurUtils.convertir(map_idDescripteur_vecteurIdOeuvre.values(), 0.0), metrique, map_id_classe);
        new AfficherTableau("Proximite de chaque descripteur a son centroide", "idDescripteur", "proximite avec le centroide", map_idDescripteur_proximiteCentroide).setVisible(true);
        
        Map<Integer, Set<Integer>> map_idClasse_ensembleIdDescripteur = Partition.recupererMap_IdClasse_ensembleIdDomif(map_id_classe);
        Map<Integer, Set<Integer>> map_idClasse_ensembleIdOeuvre = OeuvresUtils.getMap_idClasse_ensembleIdOeuvre(map_idClasse_ensembleIdDescripteur, OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id));
        Map<Integer, Set<Integer>> map_idDescripteur_ensembleIdOeuvre = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurIndicie> map_idDescripteur_vecteurIdClasseAssocie = VectoriseurUtils.vectoriserAvecCoefficientAssociation2(map_idDescripteur_ensembleIdOeuvre, map_idClasse_ensembleIdOeuvre, OeuvresUtils.getIdOeuvres(oeuvres), coefficient);
        AfficheurVecteur.afficherVecteurs("coefficient association entre descripteur et classe", TrieUtils.inverserMap(map_descripteur_id), map_idClasse_ensembleIdDescripteur.keySet(), map_idDescripteur_vecteurIdClasseAssocie.values());
                
        return map_id_classe;
    }
    
    // classe des descripteurs sans reduction dimensionnelle et extrait les descripteurs specifiques aux classes
    public static Map<Integer, Classe> analyse7 (File fileOeuvre, Clusterer classifieur, CoefficientAssociation coefficient, MetriqueSim metrique) throws IOException
    {
        Collection<Oeuvre> oeuvres = ParserMagritte.recupererOeuvre(fileOeuvre);
        Map<String, Integer> map_descripteur_id = OeuvresUtils.getMap_descripteur_id(oeuvres);
        
        Map<Integer, VecteurIndicie> map_idDescripteur_vecteurIdOeuvre = OeuvresUtils.getMap_idDescripteur_vecteurIdOeuvreFREQ(oeuvres, map_descripteur_id);
//        map_idOeuvre_vecteurIdDescripteur = VectoriseurUtils.dichotomiser(0.000001, map_idOeuvre_vecteurIdDescripteur.values());
        
        TreeMap<Integer, Classe> map_id_classe = new WrapperClassifieur().partitionner(classifieur, VectoriseurUtils.convertir(map_idDescripteur_vecteurIdOeuvre.values(), 0).values());
        
        
        TreeMap<Integer, Integer> map_idDescripteur_idClasse = Partition.recupererMap_IdDomif_IdClasse(map_id_classe);
        new AfficherTableau("Le idClasse pour chaque idDescripteur", "ID descripteur", "ID CLASSE", map_idDescripteur_idClasse).setVisible(true);
        TreeMap<Integer, Integer> map_idClasse_nbreDescripteur = Partition.recupererMap_idClasse_nbreDomif(map_id_classe);
        new AfficherTableau("Nombre de descripteur par classe", "idClasse", "nbre descripteur", map_idClasse_nbreDescripteur).setVisible(true);
//        TreeMap<Integer, Double> map_idDescripteur_proximiteCentroide = Partition.calculerEcartAuCentroide(VectoriseurUtils.convertir(map_idDescripteur_vecteurIdOeuvre.values(), 0.0), metrique, map_id_classe);
//        new AfficherTableau("Proximite de chaque descripteur a son centroide", "idOeuvre", "proximite avec le centroide", map_idDescripteur_proximiteCentroide).setVisible(true);
        
        Map<Integer, Set<Integer>> map_idClasse_ensembleIdDescripteur = Partition.recupererMap_IdClasse_ensembleIdDomif(map_id_classe);
        Map<Integer, Set<Integer>> map_idClasse_ensembleIdOeuvre = OeuvresUtils.getMap_idClasse_ensembleIdOeuvre(map_idClasse_ensembleIdDescripteur, OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id));
        Map<Integer, Set<Integer>> map_idDescripteur_ensembleIdOeuvre = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurIndicie> map_idDescripteur_vecteurIdClasseAssocie = VectoriseurUtils.vectoriserAvecCoefficientAssociation2(map_idDescripteur_ensembleIdOeuvre, map_idClasse_ensembleIdOeuvre, OeuvresUtils.getIdOeuvres(oeuvres), coefficient);
        AfficheurVecteur.afficherVecteurs("coefficient association entre descripteur et classe", TrieUtils.inverserMap(map_descripteur_id), map_idClasse_ensembleIdDescripteur.keySet(), map_idDescripteur_vecteurIdClasseAssocie.values());
                
        return map_id_classe;
    }
    
    // classe des descripteurs et extrait les descripteurs specifiques aux classes
    // semble etre la meme methode que "analyse1" mais avec SVD
    public static Map<Integer, Classe> analyse8 (File fileOeuvre, MetriqueSim metrique, CoefficientAssociation coefficient, int kClasse, int iterationKm, int facteurs) throws IOException, NotConvergedException
    {
        Collection<Oeuvre> oeuvres = ParserMagritte.recupererOeuvre(fileOeuvre);
        oeuvres = Pretraitement.filtrerDescripteurMot(oeuvres);
        Map<String, Integer> map_descripteur_id = OeuvresUtils.getMap_descripteur_id(oeuvres);
        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
        Map<Integer, VecteurIndicie> map_idDescripteur_vecteurIdDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        
        Map<Integer, VecteurCreux> vecteurs = VectoriseurUtils.convertir(map_idDescripteur_vecteurIdDescripteur.values(), 0);
        vecteurs = Pretraitement.reduireViaSvd(vecteurs, map_descripteur_id.size(), map_idDescripteur_vecteurIdDescripteur.size(), facteurs);
                
        ClassifieurKmeansMaximum classifieur = new ClassifieurKmeansMaximum(vecteurs, metrique, kClasse, 500, 0, true);
        TreeMap<Integer, Classe> map_id_classe = classifieur.partitionnerMax(iterationKm);
        
        TreeMap<Integer, Integer> map_idDescripteur_idClasse = Partition.recupererMap_IdDomif_IdClasse(map_id_classe);
        new AfficherTableau("Le idClasse pour chaque idDescripteur", "ID descripteur", "ID CLASSE", map_idDescripteur_idClasse).setVisible(true);
        TreeMap<Integer, Integer> map_idClasse_nbreDescripteur = Partition.recupererMap_idClasse_nbreDomif(map_id_classe);
        new AfficherTableau("Nombre de descripteur par classe", "idClasse", "nbre descripteur", map_idClasse_nbreDescripteur).setVisible(true);
        TreeMap<Integer, Double> map_idDescripteur_proximiteCentroide = Partition.calculerEcartAuCentroide(VectoriseurUtils.convertir(map_idDescripteur_vecteurIdDescripteur.values(), 0.0), metrique, map_id_classe);
        new AfficherTableau("Proximite de chaque descripteur a son centroide", "idOeuvre", "proximite avec le centroide", map_idDescripteur_proximiteCentroide).setVisible(true);
        
        Map<Integer, Set<Integer>> map_idClasse_ensembleIdDescripteur = Partition.recupererMap_IdClasse_ensembleIdDomif(map_id_classe);
        Map<Integer, Set<Integer>> map_idDescripteur_ensembleIdDescripteurCooc = OeuvresUtils.getMap_idDescripteur_ensembleIdDescripteurCooccurrent(oeuvres, map_descripteur_id);
        Map<Integer, VecteurIndicie> map_idDescripteur_vecteurIdClasseAssocie = VectoriseurUtils.vectoriserAvecCoefficientAssociation2(map_idDescripteur_ensembleIdDescripteurCooc, map_idClasse_ensembleIdDescripteur, map_idDescripteur_ensembleIdDescripteurCooc.keySet(), coefficient);
        AfficheurVecteur.afficherVecteurs("coefficient association entre descripteur et classe", TrieUtils.inverserMap(map_descripteur_id), map_idClasse_ensembleIdDescripteur.keySet(), map_idDescripteur_vecteurIdClasseAssocie.values());
                
        return map_id_classe;
    }
    
    // classification fuzzy k-means de descripteur en fonction des descripteurs coocurrents specifiques     
    public static Map<Integer, Classe> analyse9 (File fileOeuvre, FuzzyKMeansClusterer classifieur, MetriqueSim metrique, CoefficientAssociation coefficient) throws IOException
    {
        Collection<Oeuvre> oeuvres = ParserMagritte.recupererOeuvre(fileOeuvre);
        Pretraitement pre = new Pretraitement(false, false, false, false, -1, -1, 99999999, true, true);
        oeuvres = pre.filtrerOeuvres(oeuvres);
        Map<String, Integer> map_descripteur_id = OeuvresUtils.getMap_descripteur_id(oeuvres);
        new AfficherTableau("Descripteurs de Magritte", "descripteur", "ID", map_descripteur_id).setVisible(true);
        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
        Map<Integer, VecteurIndicie> vecteurs = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteurs = VectoriseurUtils.dichotomiserSeuilMax(0.00000, vecteurs.values());
        
//        AnalyseK.approximerK(new EvaluateurSilhouette(VectoriseurUtils.convertir(vecteurs.values(), 0), new ErreurQuadratique()), new Cosinus(), 2, 50, 10);
//        MDS.calculerReductionDimensionelle(VectoriseurUtils.convertir(vecteurs.values(), 0.0), new DistanceEuclidienne());
        TreeMap<Integer, Classe> map_id_classe = new WrapperClassifieur().partitionner(classifieur, VectoriseurUtils.convertir(vecteurs.values(), 0).values());
        proximiteIdVecteurEnversCentroide(VectoriseurUtils.convertir(vecteurs.values(), 0), map_id_classe.values(), TrieUtils.inverserMap(map_descripteur_id), metrique);
//        
//        TreeMap<Integer, Integer> map_idDescripteur_idClasse = Partition.recupererMap_IdDomif_IdClasse(map_id_classe);
//        new AfficherTableau("Le idClasse pour chaque idDescripteur d'une partition", "ID Descripteur", "ID CLASSE", map_idDescripteur_idClasse).setVisible(true);
        TreeMap<Integer, Integer> map_idClasse_nbreDescripteur = Partition.recupererMap_idClasse_nbreDomif(map_id_classe);
        new AfficherTableau("Nombre de descripteur par classe", "idClasse", "nbre de descripteur", map_idClasse_nbreDescripteur).setVisible(true);
//        TreeMap<Integer, Double> map_idDescripteur_proximiteCentroide = Partition.calculerEcartAuCentroide(VectoriseurUtils.convertir(vecteurs.values(), 0.0), metrique, map_id_classe);
//        new AfficherTableau("Proximite de chaque descripteur a son centroide", "idDescripteur", "proximite avec le centroide", map_idDescripteur_proximiteCentroide).setVisible(true);
//        AfficheurTableau.afficher4Colonnes("Proximite de chaque descripteur a son centroide", "idDescripteur", "descripteur","proximite avec le centroide","classe", TriageCollection.inverserMap(map_descripteur_id), map_idDescripteur_proximiteCentroide, map_idDescripteur_idClasse);
////        new AfficherTableau("Proximite de chaque descripteur a son centroide", "idDescripteur", "descripteur", "proximite avec le centroide", TriageCollection.inverserMap(map_descripteur_id), map_idDescripteur_proximiteCentroide).setVisible(true);
//        
//        Map<Integer, Set<Integer>> map_idClasse_ensembleIdDescripteur = Partition.recupererMap_IdClasse_ensembleIdDomif(map_id_classe);
//        Map<Integer, Set<Integer>> map_idDescripteur_ensembleIdDescripteurCooc = OeuvresUtils.getMap_idDescripteur_ensembleIdDescripteurCooccurrent(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> map_idDescripteur_vecteurIdClasseAssocie = VectoriseurUtils.vectoriserAvecCoefficientAssociation2(map_idDescripteur_ensembleIdDescripteurCooc, map_idClasse_ensembleIdDescripteur, map_idDescripteur_ensembleIdDescripteurCooc.keySet(), new CorrelationMatthews());
//        AfficheurVecteur.afficherVecteurs("coefficient association entre descripteur et classe", TriageCollection.inverserMap(map_descripteur_id), map_idClasse_ensembleIdDescripteur.keySet(), map_idDescripteur_vecteurIdClasseAssocie.values());
                
        
        return map_id_classe;
    }
    
    // classification de descripteur en fonction des descripteurs coocurrents specifiques     
    public static Map<Integer, Classe> analyse10 (File fileOeuvre, MetriqueSim metrique, CoefficientAssociation coefficient, int kClasse, int iterationKm) throws IOException
    {
        Collection<Oeuvre> oeuvres = ParserMagritte.recupererOeuvre(fileOeuvre);
        Pretraitement pre = new Pretraitement(false, false, false, false, -1, 2, 999999, true, true);
        oeuvres = pre.filtrerOeuvres(oeuvres);
//        oeuvres = Pretraitement.filtrerOeuvreSelonCible(oeuvres, "<femme>");
        Map<String, Integer> map_descripteur_id = OeuvresUtils.getMap_descripteur_id(oeuvres);
        new AfficherTableau("Descripteurs de Magritte", "descripteur", "ID", map_descripteur_id).setVisible(true);
//        TreeMap<Integer, Collection<String>> map_date_tokens = new TreeMap<>(OeuvresUtils.getMap_date_ensembleDescripteur(oeuvres));
        
//        AfficheurTableau.afficher2Colonnes("Accroissement vocabulaire", "date", "vocabulaire centre et reduit", AccroissementVocabulaireVoSurVt.calculerAccroissementVocabulaire(map_date_tokens));
        
        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
//        vecteursDescripteur = VectoriseurUtils.normerVecteurIndicie(vecteursDescripteur);
        VectoriseurUtils.normerVecteurIndicieB(vecteursDescripteur);
//        vecteursDescripteur = VectoriseurUtils.normaliserZScore(vecteursDescripteur);
        
//        vecteursDescripteur = VectoriseurUtils.dichotomiserSeuilMin(0.0, vecteursDescripteur.values());
//        vecteursDescripteur = VectoriseurUtils.normerVecteurIndicie(vecteursDescripteur);
//        AfficheurVecteur.afficherVecteurs("matrice des rapports syntagmatique", TrieUtils.inverserMap(map_descripteur_id), TrieUtils.inverserMap(map_descripteur_id), vecteursDescripteur.values());
        
//        EvaluateurRatioVariance evalCluster = new EvaluateurRatioVariance(VectoriseurUtils.convertir(vecteursDescripteur.values(), 0), new ProduitScalaireInverse());
//        EvaluateurSilhouette evalCluster = new EvaluateurSilhouette(VectoriseurUtils.convertir(vecteursDescripteur.values(), 0), new ProduitScalaireInverse());
//        AnalyseK.approximerK(evalCluster, metrique, 2, 80, 1, 10);
//        MDS.calculerReductionDimensionelle(VectoriseurUtils.convertir(vecteursDescripteur.values(), 0.0), new DistanceEuclidienne());
//        System.exit(1);
        
        // 80 et 30 iterations semble bon
        DistanceMeasure distance = new DistanceCosinusCommonMath();
        KMeansPlusPlusClusterer kmPlus = new KMeansPlusPlusClusterer(kClasse, 500, distance, new MersenneTwister(3), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        Clusterer classifieur = new MultiKMeansPlusPlusClusterer(kmPlus, iterationKm, new SumOfClusterVariances<>(distance));
//        AfficheurTableau.afficher2Colonnes("distance du ie vecteur le plus proche", "id Descripteur", "proximite", AnalyseSeuilDensite.calculerSeuilDensite(8, vecteursDescripteur, new DistanceCosinusIndicie()));
//        Map<Integer, VecteurIndicie> map_id_vecteurSim = AnalyseSimilitude.calculerMetriqueMatriceCarre(vecteursDescripteur, new DistanceCosinusIndicie(), true, false);
//        AnalyseSeuilDensite.calculerSeuilDensite(map_id_vecteurSim, 10, vecteursDescripteur);
//        AnalyseSeuilDensite.calculerDensiteNull(1, map_id_vecteurSim, 500);
//        Clusterer classifieur = new DBSCANClusterer(0.6, 15, new DistanceCosinusCommonMath()); // les valeur (0.4 et 5) sont tr�s intressants lorsque qu'on prend tous les descripteur; (0.5, 5) sont int�ressante si on prend descripteurs dans 5 oeuvres
        TreeMap<Integer, Classe> map_id_classe = new WrapperClassifieur().partitionner(classifieur, VectoriseurUtils.convertir(vecteursDescripteur.values(), 0).values());
        
//        ClassifieurKmeansMaximum classifieur = new ClassifieurKmeansMaximum(VectoriseurUtils.convertir(vecteursDescripteur.values(), 0.0), metrique, kClasse, 500, 0, true);
//        TreeMap<Integer, Classe> map_id_classe = classifieur.partitionnerMax(iterationKm);
//        ClassifieurKmeansAvecMultiplication classifieur = new ClassifieurKmeansAvecMultiplication(VectoriseurUtils.convertir(vecteursDescripteur.values(), 0.0), metrique, kClasse, 500, 0, false);
//        TreeMap<Integer, Classe> map_id_classe = classifieur.partitionner();
        
        
        TreeMap<Integer, Integer> map_idDescripteur_idClasse = Partition.recupererMap_IdDomif_IdClasse(map_id_classe);
        new AfficherTableau("Le idClasse pour chaque idDescripteur d'une partition", "ID Descripteur", "ID CLASSE", map_idDescripteur_idClasse).setVisible(true);
        TreeMap<Integer, Integer> map_idClasse_nbreDescripteur = Partition.recupererMap_idClasse_nbreDomif(map_id_classe);
        new AfficherTableau("Nombre de descripteur par classe", "idClasse", "nbre de descripteur", map_idClasse_nbreDescripteur).setVisible(true);
        
        AnalyseAssociationTheme.analyserImportanceThemeCorpus(map_idOeuvre_listeIdDescripteur.values(), map_idDescripteur_idClasse);
        AnalyseAssociationTheme.analysePresenceThemeParOeuvre(OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, map_descripteur_id), map_idDescripteur_idClasse);
        
        Map<Integer, double[]> map_idTheme_centroide = Partition.calculerCentroideDesClasse(VectoriseurUtils.convertir(vecteursDescripteur.values(), 0.0), map_id_classe);
        AfficheurVecteur.afficherVecteurs("centroide des classes", map_idTheme_centroide);
        AfficheurVecteur.afficherVecteurs("proximite du descripteur avec centroide", TrieUtils.inverserMap(map_descripteur_id), map_id_classe.keySet(), Partition.calculerMetriqueEntreVecteurEtCentroides(VectoriseurUtils.convertir(vecteursDescripteur.values(), 0.0), map_idTheme_centroide, metrique).values());

        AfficheurTableau.afficher2Colonnes("nombre oeuvre par date", "date", "nbre oeuvre", OeuvresUtils.getMap_date_nbreOeuvre(oeuvres));
        
        AnalyseAssociationTheme.analyseThemeParAnneeNew(map_idTheme_centroide, vecteursDescripteur, OeuvresUtils.getMap_date_collectionIdDescripteur(oeuvres, map_descripteur_id), map_descripteur_id, metrique);
        AnalyseAssociationTheme.analyseProximiteThemeParOeuvre(map_idTheme_centroide, vecteursDescripteur, oeuvres, map_descripteur_id, metrique);
        AnalyseAssociationTheme.analyserRangThemeParOeuvre(map_idTheme_centroide, vecteursDescripteur, oeuvres, map_descripteur_id, metrique);
        
        TreeMap<Integer, Double> map_idDescripteur_proximiteCentroide = Partition.calculerEcartAuCentroide(VectoriseurUtils.convertir(vecteursDescripteur.values(), 0.0), metrique, map_id_classe);
//        new AfficherTableau("Proximite de chaque descripteur a son centroide", "idDescripteur", "proximite avec le centroide", map_idDescripteur_proximiteCentroide).setVisible(true);
        AfficheurTableau.afficher4Colonnes("Proximite de chaque descripteur a son centroide", "idDescripteur", "descripteur","proximite avec le centroide","classe", TrieUtils.inverserMap(map_descripteur_id), map_idDescripteur_proximiteCentroide, map_idDescripteur_idClasse);
        EvaluateurSilhouette eval = new EvaluateurSilhouette(VectoriseurUtils.convertir(vecteursDescripteur.values(), 0), new DistanceCosinus());
        eval.evaluerPartition(map_id_classe, map_idTheme_centroide);
        AfficheurTableau.afficher4Colonnes("Silhouette de chaque descripteur a son centroide", "idDescripteur", "descripteur","silhouette","classe", TrieUtils.inverserMap(map_descripteur_id), eval.getMap_idDomif_silhouette() , map_idDescripteur_idClasse);
////        new AfficherTableau("Proximite de chaque descripteur a son centroide", "idDescripteur", "descripteur", "proximite avec le centroide", TriageCollection.inverserMap(map_descripteur_id), map_idDescripteur_proximiteCentroide).setVisible(true);
        AfficheurTableau.afficher2Colonnes("Silhouette de chaque classe", "idclasse", "silhouette", eval.calculerSilhouetteParClasse(eval.getMap_idDomif_silhouette(), map_idDescripteur_idClasse));
        
//        Map<Integer, Set<Integer>> map_idClasse_ensembleIdDescripteur = Partition.recupererMap_IdClasse_ensembleIdDomif(map_id_classe);
//        Map<Integer, Set<Integer>> map_idDescripteur_ensembleIdDescripteurCooc = OeuvresUtils.getMap_idDescripteur_ensembleIdDescripteurCooccurrent(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> map_idDescripteur_vecteurIdClasseAssocie = VectoriseurUtils.vectoriserAvecCoefficientAssociation2(map_idDescripteur_ensembleIdDescripteurCooc, map_idClasse_ensembleIdDescripteur, map_idDescripteur_ensembleIdDescripteurCooc.keySet(), new CorrelationMatthews());
//        AfficheurVecteur.afficherVecteurs("coefficient association entre descripteur et classe", TriageCollection.inverserMap(map_descripteur_id), map_idClasse_ensembleIdDescripteur.keySet(), map_idDescripteur_vecteurIdClasseAssocie.values());
         
        return map_id_classe;
    }
    
    // classification de descripteur en fonction des descripteurs coocurrents specifiques     
    // on introduit ici une operationd e pretaitement qui consiste a supprimer de chaque vecteurs descripteurs
    // le centroide de tout le corpus. Le but est de rendre independant du centre tous les descripteurs
    // en esperant que cela accentue les difference entre descripteur
    public static Map<Integer, Classe> analyse11 (File fileOeuvre, MetriqueSim metrique, CoefficientAssociation coefficient, int kClasse, int iterationKm) throws IOException
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
//        double[] centre = ClassifieurKmeans.calculerCentroide(MatriceCreuse.getArrays(vecteursDescripteur.values()).values(), map_descripteur_id.size());
//        vecteursDescripteur = VectoriseurUtils.getComplementOrthogaunaux(vecteursDescripteur, centre);
//        vecteursDescripteur = VectoriseurUtils.normerVecteurCreuxB(vecteursDescripteur.values());
        
        
//        vecteursDescripteur = VectoriseurUtils.dichotomiserSeuilMin(0.0, vecteursDescripteur);
//        vecteursDescripteur = VectoriseurUtils.normerVecteurIndicie(vecteursDescripteur);
//        AfficheurVecteur.afficherVecteurs("matrice des rapports syntagmatique", TrieUtils.inverserMap(map_descripteur_id), TrieUtils.inverserMap(map_descripteur_id), vecteursDescripteur.values());
        
//        EvaluateurRatioVariance evalCluster = new EvaluateurRatioVariance(vecteursDescripteur, new ProduitScalaireInverse());
//        EvaluateurSilhouette evalCluster = new EvaluateurSilhouette(vecteursDescripteur, new ProduitScalaireInverse());
        EvaluateurSilhouetteK evalCluster = new EvaluateurSilhouetteK(vecteursDescripteur, new ProduitScalaireInverse(), new ProduitScalaire(), 15);
//        
        AnalyseK.approximerK(evalCluster, metrique, 2, 30, 10, 1);
//        MDS.calculerReductionDimensionelle(vecteursDescripteur, new ProduitScalaireInverse());
        System.exit(1);
        
        // 36 classes semble optimier le silhouette 30 iterations semble bon
        DistanceMeasure distance = new DistanceCosinusCommonMath();
        KMeansPlusPlusClusterer kmPlus = new KMeansPlusPlusClusterer(kClasse, 500, distance, new MersenneTwister(3), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        Clusterer classifieur = new MultiKMeansPlusPlusClusterer(kmPlus, iterationKm, new SumOfClusterVariances<>(distance));
//        AfficheurTableau.afficher2Colonnes("distance du ie vecteur le plus proche", "id Descripteur", "proximite", AnalyseSeuilDensite.calculerSeuilDensite(8, vecteursDescripteur, new DistanceCosinusIndicie()));
//        Map<Integer, VecteurIndicie> map_id_vecteurSim = AnalyseSimilitude.calculerMetriqueMatriceCarre(vecteursDescripteur, new DistanceCosinusIndicie(), true, false);
//        AnalyseSeuilDensite.calculerSeuilDensite(map_id_vecteurSim, 10, vecteursDescripteur);
//        AnalyseSeuilDensite.calculerDensiteNull(1, map_id_vecteurSim, 500);
//        Clusterer classifieur = new DBSCANClusterer(0.2, 1, new DistanceCosinusCommonMath()); // les valeur (0.4 et 5) sont tr�s intressants lorsque qu'on prend tous les descripteur; (0.5, 5) sont int�ressante si on prend descripteurs dans 5 oeuvres
        TreeMap<Integer, Classe> map_id_classe = new WrapperClassifieur().partitionner(classifieur, vecteursDescripteur.values());
////        
//////        ClassifieurKmeansMaximum classifieur = new ClassifieurKmeansMaximum(VectoriseurUtils.convertir(vecteursDescripteur.values(), 0.0), metrique, kClasse, 500, 0, true);
//////        TreeMap<Integer, Classe> map_id_classe = classifieur.partitionnerMax(iterationKm);
//////        ClassifieurKmeansAvecMultiplication classifieur = new ClassifieurKmeansAvecMultiplication(VectoriseurUtils.convertir(vecteursDescripteur.values(), 0.0), metrique, kClasse, 500, 0, false);
//////        TreeMap<Integer, Classe> map_id_classe = classifieur.partitionner();
        
        
        TreeMap<Integer, Integer> map_idDescripteur_idClasse = Partition.recupererMap_IdDomif_IdClasse(map_id_classe);
        new AfficherTableau("Le idClasse pour chaque idDescripteur d'une partition", "ID Descripteur", "ID CLASSE", map_idDescripteur_idClasse).setVisible(true);
        TreeMap<Integer, Integer> map_idClasse_nbreDescripteur = Partition.recupererMap_idClasse_nbreDomif(map_id_classe);
        new AfficherTableau("Nombre de descripteur par classe", "idClasse", "nbre de descripteur", map_idClasse_nbreDescripteur).setVisible(true);
        
        AnalyseAssociationTheme.analyserImportanceThemeCorpus(map_idOeuvre_listeIdDescripteur.values(), map_idDescripteur_idClasse);
        AnalyseAssociationTheme.analysePresenceThemeParOeuvre(OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, map_descripteur_id), map_idDescripteur_idClasse);
        
        Map<Integer, double[]> map_idTheme_centroide = Partition.calculerCentroideDesClasse(vecteursDescripteur, map_id_classe);
        AfficheurVecteur.afficherVecteurs("centroide des classes", map_idTheme_centroide);
        AfficheurVecteur.afficherVecteurs("proximite du descripteur avec centroide", TrieUtils.inverserMap(map_descripteur_id), map_id_classe.keySet(), Partition.calculerMetriqueEntreVecteurEtCentroides(vecteursDescripteur, map_idTheme_centroide, metrique).values());

        AfficheurTableau.afficher2Colonnes("nombre oeuvre par date", "date", "nbre oeuvre", OeuvresUtils.getMap_date_nbreOeuvre(oeuvres));
        
//        AnalyseAssociationTheme.analyseThemeParAnneeNew(map_idTheme_centroide, vecteursDescripteur, OeuvresUtils.getMap_date_collectionIdDescripteur(oeuvres, map_descripteur_id), map_descripteur_id, metrique);
//        AnalyseAssociationTheme.analyseProximiteThemeParOeuvre(map_idTheme_centroide, vecteursDescripteur, oeuvres, map_descripteur_id, metrique);
//        AnalyseAssociationTheme.analyserRangThemeParOeuvre(map_idTheme_centroide, vecteursDescripteur, oeuvres, map_descripteur_id, metrique);
        
        TreeMap<Integer, Double> map_idDescripteur_proximiteCentroide = Partition.calculerEcartAuCentroide(vecteursDescripteur, metrique, map_id_classe);
//        new AfficherTableau("Proximite de chaque descripteur a son centroide", "idDescripteur", "proximite avec le centroide", map_idDescripteur_proximiteCentroide).setVisible(true);
        AfficheurTableau.afficher4Colonnes("Proximite de chaque descripteur a son centroide", "idDescripteur", "descripteur","proximite avec le centroide","classe", TrieUtils.inverserMap(map_descripteur_id), map_idDescripteur_proximiteCentroide, map_idDescripteur_idClasse);
//        EvaluateurSilhouette eval = new EvaluateurSilhouette(vecteursDescripteur, new DistanceCosinus());
//        eval.evaluerPartition(map_id_classe, map_idTheme_centroide);
//        AfficheurTableau.afficher4Colonnes("Silhouette de chaque descripteur a son centroide", "idDescripteur", "descripteur","silhouette","classe", TrieUtils.inverserMap(map_descripteur_id), eval.getMap_idDomif_silhouette() , map_idDescripteur_idClasse);
//////        new AfficherTableau("Proximite de chaque descripteur a son centroide", "idDescripteur", "descripteur", "proximite avec le centroide", TriageCollection.inverserMap(map_descripteur_id), map_idDescripteur_proximiteCentroide).setVisible(true);
//        AfficheurTableau.afficher2Colonnes("Silhouette de chaque classe", "idclasse", "silhouette", eval.calculerSilhouetteParClasse(eval.getMap_idDomif_silhouette(), map_idDescripteur_idClasse));
        
//        Map<Integer, Set<Integer>> map_idClasse_ensembleIdDescripteur = Partition.recupererMap_IdClasse_ensembleIdDomif(map_id_classe);
//        Map<Integer, Set<Integer>> map_idDescripteur_ensembleIdDescripteurCooc = OeuvresUtils.getMap_idDescripteur_ensembleIdDescripteurCooccurrent(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> map_idDescripteur_vecteurIdClasseAssocie = VectoriseurUtils.vectoriserAvecCoefficientAssociation2(map_idDescripteur_ensembleIdDescripteurCooc, map_idClasse_ensembleIdDescripteur, map_idDescripteur_ensembleIdDescripteurCooc.keySet(), new CorrelationMatthews());
//        AfficheurVecteur.afficherVecteurs("coefficient association entre descripteur et classe", TriageCollection.inverserMap(map_descripteur_id), map_idClasse_ensembleIdDescripteur.keySet(), map_idDescripteur_vecteurIdClasseAssocie.values());
         
        return map_id_classe;
    }
    
    
    // classification de oeuvres 
    public static Map<Integer, Classe> analyse12 (File fileOeuvre, MetriqueSim metrique, CoefficientAssociation coefficient, int kClasse, int iterationKm) throws IOException
    {
                
        Collection<Oeuvre> oeuvres = ParserMagritte.recupererOeuvre(fileOeuvre);
        Pretraitement pre = new Pretraitement(false, false, false, false, -1, 2, 9999999, true, true);
        oeuvres = pre.filtrerOeuvres(oeuvres);
//        System.exit(1);
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
        
//        double[] centre = ClassifieurKmeans.calculerCentroide(MatriceCreuse.getArrays(vecteursOeuvre.values()).values(), map_descripteur_id.size());
//        vecteursOeuvre = VectoriseurUtils.getComplementOrthogaunaux(vecteursOeuvre, centre);
//        vecteursOeuvre = VectoriseurUtils.soustraire(vecteursOeuvre, centre);
//        vecteursOeuvre = VectoriseurUtils.normerVecteurCreuxB(vecteursOeuvre.values());
        
        
//        vecteursDescripteur = VectoriseurUtils.dichotomiserSeuilMin(0.0, vecteursDescripteur);
//        vecteursDescripteur = VectoriseurUtils.normerVecteurIndicie(vecteursDescripteur);
//        AfficheurVecteur.afficherVecteurs("matrice des rapports syntagmatique", TrieUtils.inverserMap(map_descripteur_id), TrieUtils.inverserMap(map_descripteur_id), vecteursDescripteur.values());
        
//        EvaluateurRatioVariance evalCluster = new EvaluateurRatioVariance(vecteursOeuvre, new ProduitScalaireInverse());
//        EvaluateurSilhouette evalCluster = new EvaluateurSilhouette(vecteursOeuvre, new ProduitScalaireInverse());
        EvaluateurSilhouetteK evalCluster = new EvaluateurSilhouetteK(vecteursDescripteur, new ProduitScalaireInverse(), new ProduitScalaire(), 15);
//        
        AnalyseK.approximerK(evalCluster, metrique, 41, 60, 10, 1);
//        MDS.calculerReductionDimensionelle(vecteursDescripteur ,vecteursOeuvre, new ProduitScalaireInverse());
//        System.exit(1);
        
        // 36 classes semble optimier le silhouette 30 iterations semble bon
        DistanceMeasure distance = new DistanceCosinusCommonMath();
        KMeansPlusPlusClusterer kmPlus = new KMeansPlusPlusClusterer(kClasse, 500, distance, new MersenneTwister(3), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        Clusterer classifieur = new MultiKMeansPlusPlusClusterer(kmPlus, iterationKm, new SumOfClusterVariances<>(distance));
//        AfficheurTableau.afficher2Colonnes("distance du ie vecteur le plus proche", "id Descripteur", "proximite", AnalyseSeuilDensite.calculerSeuilDensite(8, vecteursDescripteur, new DistanceCosinusIndicie()));
//        Map<Integer, VecteurIndicie> map_id_vecteurSim = AnalyseSimilitude.calculerMetriqueMatriceCarre(vecteursDescripteur, new DistanceCosinusIndicie(), true, false);
//        AnalyseSeuilDensite.calculerSeuilDensite(map_id_vecteurSim, 10, vecteursDescripteur);
//        AnalyseSeuilDensite.calculerDensiteNull(1, map_id_vecteurSim, 500);
//        Clusterer classifieur = new DBSCANClusterer(0.10, 10, new DistanceCosinusCommonMath()); // les valeur (0.4 et 5) sont tr�s intressants lorsque qu'on prend tous les descripteur; (0.5, 5) sont int�ressante si on prend descripteurs dans 5 oeuvres
        TreeMap<Integer, Classe> map_id_classe = new WrapperClassifieur().partitionner(classifieur, vecteursOeuvre.values());
////        
//////        ClassifieurKmeansMaximum classifieur = new ClassifieurKmeansMaximum(VectoriseurUtils.convertir(vecteursDescripteur.values(), 0.0), metrique, kClasse, 500, 0, true);
//////        TreeMap<Integer, Classe> map_id_classe = classifieur.partitionnerMax(iterationKm);
//////        ClassifieurKmeansAvecMultiplication classifieur = new ClassifieurKmeansAvecMultiplication(VectoriseurUtils.convertir(vecteursDescripteur.values(), 0.0), metrique, kClasse, 500, 0, false);
//////        TreeMap<Integer, Classe> map_id_classe = classifieur.partitionner();
        
        
        TreeMap<Integer, Integer> map_idOeuvre_idClasse = Partition.recupererMap_IdDomif_IdClasse(map_id_classe);
        new AfficherTableau("Le idClasse pour chaque idOeuvre d'une partition", "ID Oeuvre", "ID CLASSE", map_idOeuvre_idClasse).setVisible(true);
        TreeMap<Integer, Integer> map_idClasse_nbreOeuvre = Partition.recupererMap_idClasse_nbreDomif(map_id_classe);
        new AfficherTableau("Nombre de oeuvre par classe", "idClasse", "nbre de oeuvre", map_idClasse_nbreOeuvre).setVisible(true);
        
//        AnalyseAssociationTheme.analyserImportanceThemeCorpus(map_idOeuvre_listeIdDescripteur.values(), map_idDescripteur_idClasse);
//        AnalyseAssociationTheme.analysePresenceThemeParOeuvre(OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, map_descripteur_id), map_idDescripteur_idClasse);
        
        Map<Integer, double[]> map_idTheme_centroide = Partition.calculerCentroideDesClasse(vecteursOeuvre, map_id_classe);
        AfficheurVecteur.afficherVecteurs("centroide des classes", map_idTheme_centroide);
        AfficheurVecteur.afficherVecteurs("proximite du descripteur avec centroide", TrieUtils.inverserMap(map_descripteur_id), map_id_classe.keySet(), Partition.calculerMetriqueEntreVecteurEtCentroides(vecteursDescripteur, map_idTheme_centroide, metrique).values());
        
//        MDS.calculerReductionDimensionelle(MatriceCreuse.convertir(map_idTheme_centroide), new DistanceCosinus());
//        System.exit(1);
        
        Decomposeur6 decom = new Decomposeur6();
        Map<Integer, String> map_idTheme_idTheme = new TreeMap<>();
        for (int i: map_idTheme_centroide.keySet())
            map_idTheme_idTheme.put(i, String.valueOf(i));
        
        Map<Integer, Map<Integer, Double>> map_idOeuvre_compoTheme = decom.decomposerVecteurs(MatriceCreuse.getArrays(vecteursOeuvre.values()), map_idTheme_centroide, metrique, 0.95);
        AfficheurTableau.afficheurStructureImbriquee(map_idOeuvre_compoTheme, map_idOeuvre_titre, map_idTheme_idTheme);
        
        System.out.println("id,descripteur,composition");
        for (int i: map_idOeuvre_compoTheme.keySet())
        {
            System.out.println(i+";"+map_idOeuvre_titre.get(i)+";"+map_idOeuvre_compoTheme.get(i).size());
        }
        System.exit(1);
//        AfficheurTableau.afficher2Colonnes("nombre oeuvre par date", "date", "nbre oeuvre", OeuvresUtils.getMap_date_nbreOeuvre(oeuvres));
        
//        AnalyseAssociationTheme.analyseThemeParAnneeNew(map_idTheme_centroide, vecteursDescripteur, OeuvresUtils.getMap_date_collectionIdDescripteur(oeuvres, map_descripteur_id), map_descripteur_id, metrique);
//        AnalyseAssociationTheme.analyseProximiteThemeParOeuvre(map_idTheme_centroide, vecteursOeuvre, oeuvres, map_descripteur_id, metrique);
//        AnalyseAssociationTheme.analyserRangThemeParOeuvre(map_idTheme_centroide, vecteursDescripteur, oeuvres, map_descripteur_id, metrique);
        AfficheurVecteur.afficherVecteurs("proximite d'une oeuvre avec le centroide du theme", map_idOeuvre_titre, map_idTheme_centroide.keySet(), Partition.calculerMetriqueEntreVecteurEtCentroides(vecteursOeuvre.values(), map_idTheme_centroide, metrique).values());
        
        TreeMap<Integer, Double> map_idOeuvre_proximiteCentroide = Partition.calculerEcartAuCentroide(vecteursOeuvre, metrique, map_id_classe);
//        new AfficherTableau("Proximite de chaque descripteur a son centroide", "idDescripteur", "proximite avec le centroide", map_idDescripteur_proximiteCentroide).setVisible(true);
        AfficheurTableau.afficher4Colonnes("Proximite de chaque oeuvre a son centroide", "idoeuvre", "oeuvre","proximite avec le centroide","classe", map_idOeuvre_titre, map_idOeuvre_proximiteCentroide, map_idOeuvre_idClasse);
//        EvaluateurSilhouette eval = new EvaluateurSilhouette(vecteursDescripteur, new DistanceCosinus());
//        eval.evaluerPartition(map_id_classe, map_idTheme_centroide);
//        AfficheurTableau.afficher4Colonnes("Silhouette de chaque descripteur a son centroide", "idDescripteur", "descripteur","silhouette","classe", TrieUtils.inverserMap(map_descripteur_id), eval.getMap_idDomif_silhouette() , map_idDescripteur_idClasse);
//////        new AfficherTableau("Proximite de chaque descripteur a son centroide", "idDescripteur", "descripteur", "proximite avec le centroide", TriageCollection.inverserMap(map_descripteur_id), map_idDescripteur_proximiteCentroide).setVisible(true);
//        AfficheurTableau.afficher2Colonnes("Silhouette de chaque classe", "idclasse", "silhouette", eval.calculerSilhouetteParClasse(eval.getMap_idDomif_silhouette(), map_idDescripteur_idClasse));
        
//        Map<Integer, Set<Integer>> map_idClasse_ensembleIdDescripteur = Partition.recupererMap_IdClasse_ensembleIdDomif(map_id_classe);
//        Map<Integer, Set<Integer>> map_idDescripteur_ensembleIdDescripteurCooc = OeuvresUtils.getMap_idDescripteur_ensembleIdDescripteurCooccurrent(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> map_idDescripteur_vecteurIdClasseAssocie = VectoriseurUtils.vectoriserAvecCoefficientAssociation2(map_idDescripteur_ensembleIdDescripteurCooc, map_idClasse_ensembleIdDescripteur, map_idDescripteur_ensembleIdDescripteurCooc.keySet(), new CorrelationMatthews());
//        AfficheurVecteur.afficherVecteurs("coefficient association entre descripteur et classe", TriageCollection.inverserMap(map_descripteur_id), map_idClasse_ensembleIdDescripteur.keySet(), map_idDescripteur_vecteurIdClasseAssocie.values());
        
        
        
        Map<Integer, Map<Integer, Double>> map_idTheme_composition = new Decomposeur6().decomposerVecteurs(map_idTheme_centroide, MatriceCreuse.getArrays(vecteursDescripteur.values()), metrique, 0.95);
        AfficheurTableau.afficheurStructureImbriquee(map_idTheme_composition, TrieUtils.inverserMap(map_descripteur_id));
        
        return map_id_classe;
    }
    
    
    
    // classification de oeuvres selon des classes de descripteurs
    public static Map<Integer, Classe> analyse13 (File fileOeuvre, MetriqueSim metrique, CoefficientAssociation coefficient, int kConcepts, int kTheme, int iterationKm) throws IOException
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
        
        DistanceMeasure distance = new DistanceCosinusCommonMath();
        KMeansPlusPlusClusterer kmPlus = new KMeansPlusPlusClusterer(kConcepts, 500, distance, new MersenneTwister(3), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        Clusterer classifieur = new MultiKMeansPlusPlusClusterer(kmPlus, iterationKm, new SumOfClusterVariances<>(distance));
        TreeMap<Integer, Classe> map_id_classeDesc = new WrapperClassifieur().partitionner(classifieur, vecteursDescripteur.values());
        
        Map<Integer, Set<Integer>> map_idConcept_extension = Partition.recupererMap_IdClasse_ensembleIdDomif(map_id_classeDesc);
        Map<String, Integer> map_concept_id = concatDescripteurs(TrieUtils.inverserMap(map_descripteur_id), map_idConcept_extension);
        Map<Integer, VecteurCreux> vecteursConcepts = VectoriseurUtils.additionnerVecteursCreux(map_idConcept_extension, vecteursDescripteur, vecteursDescripteur.size());
        vecteursConcepts = VectoriseurUtils.normerVecteurCreuxB(vecteursConcepts.values());
        
        Map<Integer, VecteurCreux> vecteursOeuvre = VectoriseurUtils.additionnerVecteursCreux(map_idOeuvre_listeIdDescripteur, vecteursDescripteur, map_idDes_idO.size());
        vecteursOeuvre = VectoriseurUtils.normerVecteurCreuxB(vecteursOeuvre.values());
        
        
//        double[] centre = ClassifieurKmeans.calculerCentroide(MatriceCreuse.getArrays(vecteursOeuvre.values()).values(), map_descripteur_id.size());
//        vecteursOeuvre = VectoriseurUtils.getComplementOrthogaunaux(vecteursOeuvre, centre);
//        vecteursOeuvre = VectoriseurUtils.soustraire(vecteursOeuvre, centre);
//        vecteursOeuvre = VectoriseurUtils.normerVecteurCreuxB(vecteursOeuvre.values());
        
        
//        vecteursDescripteur = VectoriseurUtils.dichotomiserSeuilMin(0.0, vecteursDescripteur);
//        vecteursDescripteur = VectoriseurUtils.normerVecteurIndicie(vecteursDescripteur);
//        AfficheurVecteur.afficherVecteurs("matrice des rapports syntagmatique", TrieUtils.inverserMap(map_descripteur_id), TrieUtils.inverserMap(map_descripteur_id), vecteursDescripteur.values());
        
//        EvaluateurRatioVariance evalCluster = new EvaluateurRatioVariance(vecteursOeuvre, new ProduitScalaireInverse());
//        EvaluateurSilhouette evalCluster = new EvaluateurSilhouette(vecteursOeuvre, new ProduitScalaireInverse());
//        EvaluateurSilhouetteK evalCluster = new EvaluateurSilhouetteK(vecteursDescripteur, new ProduitScalaireInverse(), new ProduitScalaire(), 15);
//        
//        AnalyseK.approximerK(evalCluster, metrique, 2, 40, 10, 1);
//        MDS.calculerReductionDimensionelle(vecteursDescripteur, new ProduitScalaireInverse());
//        System.exit(1);
        
        // 36 classes semble optimier le silhouette 30 iterations semble bon
//        DistanceMeasure distance = new DistanceCosinusCommonMath();
        KMeansPlusPlusClusterer kmPlusOeuvre = new KMeansPlusPlusClusterer(kTheme, 500, distance, new MersenneTwister(3), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        Clusterer classifieurOeuvre = new MultiKMeansPlusPlusClusterer(kmPlusOeuvre, iterationKm, new SumOfClusterVariances<>(distance));
//        AfficheurTableau.afficher2Colonnes("distance du ie vecteur le plus proche", "id Descripteur", "proximite", AnalyseSeuilDensite.calculerSeuilDensite(8, vecteursDescripteur, new DistanceCosinusIndicie()));
//        Map<Integer, VecteurIndicie> map_id_vecteurSim = AnalyseSimilitude.calculerMetriqueMatriceCarre(vecteursDescripteur, new DistanceCosinusIndicie(), true, false);
//        AnalyseSeuilDensite.calculerSeuilDensite(map_id_vecteurSim, 10, vecteursDescripteur);
//        AnalyseSeuilDensite.calculerDensiteNull(1, map_id_vecteurSim, 500);
//        Clusterer classifieur = new DBSCANClusterer(0.10, 10, new DistanceCosinusCommonMath()); // les valeur (0.4 et 5) sont tr�s intressants lorsque qu'on prend tous les descripteur; (0.5, 5) sont int�ressante si on prend descripteurs dans 5 oeuvres
        TreeMap<Integer, Classe> map_idTheme_classeOeuvre = new WrapperClassifieur().partitionner(classifieurOeuvre, vecteursOeuvre.values());
////        
//////        ClassifieurKmeansMaximum classifieur = new ClassifieurKmeansMaximum(VectoriseurUtils.convertir(vecteursDescripteur.values(), 0.0), metrique, kClasse, 500, 0, true);
//////        TreeMap<Integer, Classe> map_id_classe = classifieur.partitionnerMax(iterationKm);
//////        ClassifieurKmeansAvecMultiplication classifieur = new ClassifieurKmeansAvecMultiplication(VectoriseurUtils.convertir(vecteursDescripteur.values(), 0.0), metrique, kClasse, 500, 0, false);
//////        TreeMap<Integer, Classe> map_id_classe = classifieur.partitionner();
        
        
        TreeMap<Integer, Integer> map_idOeuvre_idClasse = Partition.recupererMap_IdDomif_IdClasse(map_idTheme_classeOeuvre);
        new AfficherTableau("Le idClasse pour chaque idOeuvre d'une partition", "ID Oeuvre", "ID CLASSE", map_idOeuvre_idClasse).setVisible(true);
        TreeMap<Integer, Integer> map_idClasse_nbreOeuvres = Partition.recupererMap_idClasse_nbreDomif(map_idTheme_classeOeuvre);
        new AfficherTableau("Nombre oeuvre par classe", "idClasse", "nbre oeuvre", map_idClasse_nbreOeuvres).setVisible(true);
        
//        AnalyseAssociationTheme.analyserImportanceThemeCorpus(map_idOeuvre_listeIdDescripteur.values(), map_idDescripteur_idClasse);
//        AnalyseAssociationTheme.analysePresenceThemeParOeuvre(OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, map_descripteur_id), map_idDescripteur_idClasse);
        
        Map<Integer, double[]> map_idTheme_centroide = Partition.calculerCentroideDesClasse(vecteursOeuvre, map_idTheme_classeOeuvre);
        AfficheurVecteur.afficherVecteurs("centroide des classes d'oeuvres", map_idTheme_centroide);
        AfficheurVecteur.afficherVecteurs("proximite du concept avec centroide", TrieUtils.inverserMap(map_concept_id), map_idTheme_classeOeuvre.keySet(), Partition.calculerMetriqueEntreVecteurEtCentroides(vecteursConcepts, map_idTheme_centroide, metrique).values());

//        AfficheurTableau.afficher2Colonnes("nombre oeuvre par date", "date", "nbre oeuvre", OeuvresUtils.getMap_date_nbreOeuvre(oeuvres));
        
//        AnalyseAssociationTheme.analyseThemeParAnneeNew(map_idTheme_centroide, vecteursDescripteur, OeuvresUtils.getMap_date_collectionIdDescripteur(oeuvres, map_descripteur_id), map_descripteur_id, metrique);
//        AnalyseAssociationTheme.analyseProximiteThemeParOeuvre(map_idTheme_centroide, vecteursDescripteur, oeuvres, map_descripteur_id, metrique);
//        AnalyseAssociationTheme.analyserRangThemeParOeuvre(map_idTheme_centroide, vecteursDescripteur, oeuvres, map_descripteur_id, metrique);
        
//        TreeMap<Integer, Double> map_idConcept_proximiteCentroide = Partition.calculerEcartAuCentroide(vecteursConcepts, metrique, map_idTheme_classeOeuvre);

//        AfficheurTableau.afficher4Colonnes("Proximite de chaque concept a son centroide", "idConcept", "extension lexicale","proximite avec le centroide","classe", TrieUtils.inverserMap(map_concept_id), map_idConcept_proximiteCentroide, map_idOeuvre_idClasse);
//        EvaluateurSilhouette eval = new EvaluateurSilhouette(vecteursDescripteur, new DistanceCosinus());
//        eval.evaluerPartition(map_id_classe, map_idTheme_centroide);
//        AfficheurTableau.afficher4Colonnes("Silhouette de chaque descripteur a son centroide", "idDescripteur", "descripteur","silhouette","classe", TrieUtils.inverserMap(map_descripteur_id), eval.getMap_idDomif_silhouette() , map_idDescripteur_idClasse);
//////        new AfficherTableau("Proximite de chaque descripteur a son centroide", "idDescripteur", "descripteur", "proximite avec le centroide", TriageCollection.inverserMap(map_descripteur_id), map_idDescripteur_proximiteCentroide).setVisible(true);
//        AfficheurTableau.afficher2Colonnes("Silhouette de chaque classe", "idclasse", "silhouette", eval.calculerSilhouetteParClasse(eval.getMap_idDomif_silhouette(), map_idDescripteur_idClasse));
        
//        Map<Integer, Set<Integer>> map_idClasse_ensembleIdDescripteur = Partition.recupererMap_IdClasse_ensembleIdDomif(map_id_classe);
//        Map<Integer, Set<Integer>> map_idDescripteur_ensembleIdDescripteurCooc = OeuvresUtils.getMap_idDescripteur_ensembleIdDescripteurCooccurrent(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> map_idDescripteur_vecteurIdClasseAssocie = VectoriseurUtils.vectoriserAvecCoefficientAssociation2(map_idDescripteur_ensembleIdDescripteurCooc, map_idClasse_ensembleIdDescripteur, map_idDescripteur_ensembleIdDescripteurCooc.keySet(), new CorrelationMatthews());
//        AfficheurVecteur.afficherVecteurs("coefficient association entre descripteur et classe", TriageCollection.inverserMap(map_descripteur_id), map_idClasse_ensembleIdDescripteur.keySet(), map_idDescripteur_vecteurIdClasseAssocie.values());
        
        Map<Integer, Map<Integer, Double>> map_idTheme_composition = new Decomposeur1().decomposerVecteurs(map_idTheme_centroide, MatriceCreuse.getArrays(vecteursConcepts.values()), metrique, 0.15);
        AfficheurTableau.afficheurStructureImbriquee(map_idTheme_composition, TrieUtils.inverserMap(map_concept_id));
        
        
        return map_idTheme_classeOeuvre;
    }
    
    private static Map<String, Integer> concatDescripteurs (Map<Integer, String> map_id_descr, Map<Integer, Set<Integer>>  map_idC_extensions)
    {
        Map<String, Integer> map_concept_id = new HashMap<>(map_idC_extensions.size());
        for (int idC: map_idC_extensions.keySet())
        {
            List<String> extensions = new ArrayList<>();
            for (int i: map_idC_extensions.get(idC))
            {
                extensions.add(map_id_descr.get(i));
            }
            map_concept_id.put(String.join("+", extensions), idC);
        }
        return map_concept_id;
    }
    
    
    
        
    // cette methode de fonctionne pas
//    public static void analyseThemeParOeuvre (Map<Integer, double[]> map_idTheme_centroide, Collection<Oeuvre> oeuvres, Map<String, Integer> map_descripteur_id, MetriqueSim metrique)
//    {
//        AfficheurTableau.affichier2Colonnes("id oeuvre et titre", "id", "titre", OeuvresUtils.getMap_idOeuvre_titre(oeuvres));
//        Map<Integer, VecteurIndicie> map_idOeuvre_vecteurIdDescripteur = OeuvresUtils.getMap_idOeuvre_vecteurIdDescripteurFREQ(oeuvres, map_descripteur_id);
//        AfficheurVecteur.afficherVecteurs("proximite d'une oeuvre avec le centroide du theme", map_idTheme_centroide.keySet(), Partition.calculerMetriqueEntreVecteurEtCentroides(VectoriseurUtils.convertir(map_idOeuvre_vecteurIdDescripteur.values(), 0.0), map_idTheme_centroide, metrique).values());
//        
//    }
    
    
    
    
    
    
    
    
    // decrire chaque mot par leur oeuvre et leur cooccurrents
//    public static Map<Integer, Classe> analyse9 (File fileOeuvre, MetriqueSim metrique, CoefficientAssociation coefficient, int kClasse, int iterationKm, int facteurs) throws IOException, NotConvergedException
//    {
//        Collection<Oeuvre> oeuvres = ParserMagritte.recupererOeuvre(fileOeuvre);
//        Map<String, Integer> map_descripteur_id = OeuvresUtils.getMap_descripteur_id(oeuvres);
//        new AfficherTableau("Descripteurs de Magritte", "descripteur", "ID", map_descripteur_id).setVisible(true);
//        Map<Integer, VecteurIndicie> map_idDescripteur_vecteurIdOeuvre = OeuvresUtils.getMap_idDescripteur_vecteurIdOeuvreBM25(oeuvres, map_descripteur_id);
////        map_idOeuvre_vecteurIdDescripteur = VectoriseurUtils.dichotomiser(0.000001, map_idOeuvre_vecteurIdDescripteur.values());
//        
//        Map<Integer, VecteurCreux> vecteurs = VectoriseurUtils.convertir(map_idDescripteur_vecteurIdOeuvre.values(), 0);
////        vecteurs = Pretraitement.reduireViaSvd(vecteurs, map_descripteur_id.size(), map_idDescripteur_vecteurIdOeuvre.size(), facteurs);
//                
//        ClassifieurKmeansMaximum classifieur = new ClassifieurKmeansMaximum(vecteurs, metrique, kClasse, 500, 0, true);
//        TreeMap<Integer, Classe> map_id_classe = classifieur.partitionnerMax(iterationKm);
//        
//        TreeMap<Integer, Integer> map_idDescripteur_idClasse = Partition.recupererMap_IdDomif_IdClasse(map_id_classe);
//        new AfficherTableau("Le idClasse pour chaque idDescripteur", "ID descripteur", "ID CLASSE", map_idDescripteur_idClasse).setVisible(true);
//        TreeMap<Integer, Integer> map_idClasse_nbreDescripteur = Partition.recupererMap_idClasse_nbreDomif(map_id_classe);
//        new AfficherTableau("Nombre de descripteur par classe", "idClasse", "nbre descripteur", map_idClasse_nbreDescripteur).setVisible(true);
//        TreeMap<Integer, Double> map_idDescripteur_proximiteCentroide = Partition.calculerEcartAuCentroide(VectoriseurUtils.convertir(map_idDescripteur_vecteurIdOeuvre.values(), 0.0), metrique, map_id_classe);
//        new AfficherTableau("Proximite de chaque descripteur a son centroide", "idDescripteur", "proximite avec le centroide", map_idDescripteur_proximiteCentroide).setVisible(true);
//        
//        Map<Integer, Set<Integer>> map_idClasse_ensembleIdDescripteur = Partition.recupererMap_IdClasse_ensembleIdDomif(map_id_classe);
//        Map<Integer, Set<Integer>> map_idClasse_ensembleIdOeuvre = OeuvresUtils.getMap_idClasse_ensembleIdOeuvre(map_idClasse_ensembleIdDescripteur, OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id));
//        Map<Integer, Set<Integer>> map_idDescripteur_ensembleIdOeuvre = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> map_idDescripteur_vecteurIdClasseAssocie = VectoriseurUtils.vectoriserAvecCoefficientAssociation2(map_idDescripteur_ensembleIdOeuvre, map_idClasse_ensembleIdOeuvre, OeuvresUtils.getIdOeuvres(oeuvres), coefficient);
//        AfficheurVecteur.afficherVecteurs("coefficient association entre descripteur et classe", TriageCollection.inverserMap(map_descripteur_id), map_idClasse_ensembleIdDescripteur.keySet(), map_idDescripteur_vecteurIdClasseAssocie.values());
//                
//        return map_id_classe;
//    }
    
    public static void ponderationDescripteurParClasse (Set<Integer> ensembleIdOeuvre, Map<Integer, Set<Integer>> map_idClasse_ensembleIdOeuvre, Map<Integer, Set<Integer>> map_idDescripteur_ensembleIdOeuvre, Map<Integer, String> map_idDescripteur_descripteur, CoefficientAssociation coefficient)
    {
        TreeMap<Integer, VecteurIndicie> map_IdDescripteur_VecteurPonderationParClasse = new PonderateurParClasse().pondererVariableParClasse(ensembleIdOeuvre, map_idClasse_ensembleIdOeuvre, map_idDescripteur_ensembleIdOeuvre, coefficient);
        Ponderation ponderationParClasse = new Ponderation(map_IdDescripteur_VecteurPonderationParClasse);
        ponderationParClasse.afficherTableauDesPonderationUnifParClasse(map_idDescripteur_descripteur, "coefficient association");
        
        
//        TreeMap<Integer, VecteurIndicie> map_vecteurSim = AnalyseSimilitude.construireMatriceSimilitudeCosinusOptimise(map_id_vecteur);
//        AnalyseSimilitude.afficherSimilitudeEntreMots(map_vecteurSim, IndexationBase.recupererMap_IdUnif_UnifString(map_id_ToutLesUnif), partition.recupererMap_IdDomif_IdClasse());
    }
    
    public static void proximiteIdVecteurEnversCentroide (Map<Integer, VecteurCreux> vecteurs, Collection<Classe> classification, Map<Integer, String> map_id_descripteur, MetriqueSim metrique)
    {
        Map<Integer, Map<String, Double>> map_idClasse_descrip_proximiteCentre = new TreeMap();
        for (Classe c: classification)
        {
            if (!c.getIdElements().isEmpty())
            {
                Collection<VecteurCreux> vecteursClasse = new ArrayList<>(c.getIdElements().size());
                for (int idDesc: c.getIdElements())
                {
                    vecteursClasse.add(vecteurs.get(idDesc));
                }
                Map<Integer, Double> idDes_prox = Partition.calculerMetriqueAvecCentroide(vecteursClasse, metrique);
                Map<String, Double> descr_prox = new HashMap<>(idDes_prox.size());
                for (int idDesc: idDes_prox.keySet())
                {
                    descr_prox.put(map_id_descripteur.get(idDesc), idDes_prox.get(idDesc));
                }
                map_idClasse_descrip_proximiteCentre.put(c.getIdClasse(), descr_prox);
                }
        }
        AfficheurTableau.afficher3Colonnes("proximite de chaque descripteur avec le centroide de sa classe", "classe", "descripteur", "proximite", map_idClasse_descrip_proximiteCentre);
        
    }
    
}
