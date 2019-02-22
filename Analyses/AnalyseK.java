/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte.Analyses;

import Affichage.Tableau.AfficherTableau;
import Classification.Classe;
import ClassificationAutomatique.NonSupervisee.KMeans.ClassifieurKmeansRepete;
import ClassificationAutomatique.NonSupervisee.KMeansPlusPlus.ClassifieurKmeansPlusPlus;
import ClassificationAutomatique.NonSupervisee.KMeansPlusPlus.KmeansPlusPlusMax;
import Evaluateur.ApproximateurNombreClasse.ApproximateurNombreClasse;
import Evaluateur.AvecCentroide.EvaluateurAvecCentroide;
import Evaluateur.AvecCentroide.Gap.EvaluateurGapAvecKmeans;
import Evaluateur.AvecCentroide.Gap.EvaluateurGapDistUniforme;
import Evaluateur.AvecCentroide.Silhouette.EvaluateurSilhouette;
import Evaluateur.AvecCentroide.VarianceRatio.EvaluateurRatioVariance;
import Matrice.Vectorisateur.VecteurCreux;
import Matrice.Vectorisateur.VecteurIndicie;
import Matrice.Vectorisateur.VectoriseurUtils;
import Metrique.Distance.DistanceCosinus;
import Metrique.Distance.DistanceCosinusCommonMath;
import Metrique.Distance.ErreurQuadratique;
import Metrique.Similitude.Cosinus;
import Metrique.Similitude.MetriqueSim;
import Ponderation.CoefficientAssociation;
import Ponderation.CoefficientChi2;
import Ponderation.CorrelationMatthewsPositive;
import Ponderation.InformationMutuelle;
import Ponderation.PositiveNPMI;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import magritte.Oeuvre;
import magritte.OeuvresUtils;
import magritte.ParserMagritte;
import magritte.Pretraitement;
import no.uib.cipr.matrix.NotConvergedException;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.ml.distance.ManhattanDistance;

/**
 *
 * @author JF Chartier
 */
public class AnalyseK 
{
    public static void main(String[] args) throws IOException, NotConvergedException 
    {
        AnalyseK.analyserKSurEspaceDescripteur(OeuvresUtils.getFileCorpus(), new Cosinus(), new InformationMutuelle(), 31, 101, 20);
        
    }
    
    public static void analyserKSurEspaceDescripteur (File fileOeuvre, MetriqueSim metrique, CoefficientAssociation coefficient, int minK, int maxK, int iterationKm) throws IOException, NotConvergedException
    {
        Collection<Oeuvre> oeuvres = ParserMagritte.recupererOeuvre(fileOeuvre);
        Pretraitement pre = new Pretraitement(false, false, false, false, -1, 0, 99999999, true, true);
        oeuvres = pre.filtrerOeuvres(oeuvres);
        Map<String, Integer> map_descripteur_id = OeuvresUtils.getMap_descripteur_id(oeuvres);
        new AfficherTableau("Descripteurs de Magritte", "descripteur", "ID", map_descripteur_id).setVisible(true);
        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
//        vecteursDescripteur = VectoriseurUtils.normaliserZScore(vecteursDescripteur);

//        vecteursDescripteur = VectoriseurUtils.dichotomiserSeuilMin(10.0, vecteursDescripteur.values());
//        vecteursDescripteur = VectoriseurUtils.normerVecteurIndicie(vecteursDescripteur);
        
        Map<Integer, VecteurCreux> vecteurCreux = VectoriseurUtils.convertir(vecteursDescripteur.values(), 0);
//        vecteurCreux = Pretraitement.reduireViaSvd(vecteurCreux, map_descripteur_id.size(), map_descripteur_id.size(), 50);
        AnalyseK.approximerKRatioVariance(vecteurCreux, metrique, minK, maxK, iterationKm);
//        AnalyseK.approximerKAvecGap(VectoriseurUtils.convertir(vecteursDescripteur.values(), 0), metrique, minK, maxK, iterationKm);
//        AnalyseK.approximerKSilhouette(vecteurCreux, metrique, minK, maxK, iterationKm);        
    }
    
    
    public static void approximerKAvecGap (Map<Integer, VecteurCreux> vecteurs, MetriqueSim metriqueSim, int minK, int maxK, int nbreDataReference) throws IOException
    {
        EvaluateurGapAvecKmeans evaluateur = new EvaluateurGapAvecKmeans(vecteurs, nbreDataReference, true);
        // ici c'est delicat. Premierement, on ne peut appliquer km qu'une seul fois sur les donnees observees
        // toutefois, km est applique une fois sur chaque donnees de reference
        // l'ecart-type calcule devrait toujours etre de 0.0 car km n'est applique qu'une seule fois sur les donnees observees
       // ce qu'il manque c'est l'ecart-type sur les donnees de reference. Mais il faudrait pour cela utiliser 
        // une autre methode que EvaluateurGapAvecKmeans
        ApproximateurNombreClasse approximateur = new ApproximateurNombreClasse();
        ClassifieurKmeansPlusPlus kmPlusPlus = new ClassifieurKmeansPlusPlus(vecteurs, 500, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        kmPlusPlus.setSeed(1);
        approximateur.approximerNombreClasseSelonClassifieurNonSupervise(kmPlusPlus, 1, evaluateur, minK, maxK, 1);
        
    }
    
    public static void approximerKRatioVariance (Map<Integer, VecteurCreux> vecteurs, MetriqueSim metriqueSim, int minK, int maxK, int iterationKM) throws IOException
    {
//        Collection<Oeuvre> oeuvres = ParserMagritte.recupererOeuvre(fileOeuvre);
//        Map<String, Integer> map_descripteur_id = OeuvresUtils.getMap_descripteur_id(oeuvres);
//        Map<Integer, VecteurIndicie> map_idOeuvre_vecteurIdDescripteur = OeuvresUtils.getMap_idOeuvre_vecteurIdDescripteurBM25(oeuvres, map_descripteur_id);
//        map_idOeuvre_vecteurIdDescripteur = VectoriseurUtils.dichotomiser(0.01, map_idOeuvre_vecteurIdDescripteur.values());
        
        EvaluateurRatioVariance eval = new EvaluateurRatioVariance(vecteurs, new DistanceCosinus());
        approximerK(eval, metriqueSim, minK, maxK, iterationKM);
    }
    
    public static void approximerKSilhouette (Map<Integer, VecteurCreux> vecteurs, MetriqueSim metriqueSim, int minK, int maxK, int iterationKM) throws IOException
    {
//        Collection<Oeuvre> oeuvres = ParserMagritte.recupererOeuvre(fileOeuvre);
//        Map<String, Integer> map_descripteur_id = OeuvresUtils.getMap_descripteur_id(oeuvres);
//        Map<Integer, VecteurIndicie> map_idOeuvre_vecteurIdDescripteur = OeuvresUtils.getMap_idOeuvre_vecteurIdDescripteurBM25(oeuvres, map_descripteur_id);
        EvaluateurSilhouette eval = new EvaluateurSilhouette(vecteurs, new ErreurQuadratique());
        approximerK(eval, metriqueSim, minK, maxK, iterationKM);
    }
    
    public static void approximerK (EvaluateurAvecCentroide evaluateur, MetriqueSim metriqueSim, int minNombreClasse, int maxNombreClasse, int repetitionClassifieur) throws IOException
    {
        ApproximateurNombreClasse approximateur = new ApproximateurNombreClasse();
        ClassifieurKmeansPlusPlus kmPlusPlus = new ClassifieurKmeansPlusPlus(evaluateur.getMap_Id_VecteurCreux(), 500, new DistanceCosinusCommonMath(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        approximateur.approximerNombreClasseSelonClassifieurNonSupervise(kmPlusPlus, repetitionClassifieur, evaluateur, minNombreClasse, maxNombreClasse, 1);       
        
    }
    
    public static void approximerK (EvaluateurAvecCentroide evaluateur, MetriqueSim metriqueSim, int minNombreClasse, int maxNombreClasse, int repetitionClassifieur, int trial) throws IOException
    {
        ApproximateurNombreClasse approximateur = new ApproximateurNombreClasse();
        KmeansPlusPlusMax kmPlusPlusMax = new KmeansPlusPlusMax(trial, evaluateur.getMap_Id_VecteurCreux(), 500, new DistanceCosinusCommonMath(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        approximateur.approximerNombreClasseSelonClassifieurNonSupervise(kmPlusPlusMax, repetitionClassifieur, evaluateur, minNombreClasse, maxNombreClasse, 1);       
      
    }
    
    // old version
//    public static void approximerK (EvaluateurAvecCentroide evaluateur, MetriqueSim metriqueSim, int minNombreClasse, int maxNombreClasse, int iterationKM) throws IOException
//    {
//        ClassifieurKmeansRepete kmR = new ClassifieurKmeansRepete(evaluateur.getMap_Id_VecteurCreux(), metriqueSim, 500, 0.0, true, iterationKM);
//        new ApproximateurNombreClasse().approximerNombreClasseKmeans(kmR, evaluateur, minNombreClasse, maxNombreClasse, 1);
//    }
    
}
