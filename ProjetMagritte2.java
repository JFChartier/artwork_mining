/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte;

import Affichage.Tableau.AfficherTableau;
import AnalyseSimilitude.AnalyseSimilitude;
import Classification.Partition;
import ClassificationAutomatique.NonSupervisee.WrapperCommonMath.Dbscan;
import ClassificationAutomatique.NonSupervisee.KMeans.ClassifieurKmeans;
import ClassificationAutomatique.NonSupervisee.KMeans.ClassifieurKmeansRepete;
import ClassificationAutomatique.NonSupervisee.KMeans.MultiClassifieurKmeans;
import ClassificationAutomatique.Supervisee.KPPV.KPlusProcheVoisin;
import Corpus.Corpus;
import Corpus.EurekaNico.EurekaDocs;
import Corpus.Magritte.ParserMagritte;
import Corpus.ParseurCorpusFormatJFC;
import Evaluateur.ApproximateurNombreClasse.ApproximateurNombreClasse;
import Evaluateur.AvecCentroide.Calinski.EvaluateurCalinskiOLD;
import Evaluateur.AvecCentroide.Calinski.EvaluateurCalinskiMultiClassifieurOLD;
import Evaluateur.AvecCentroide.EvaluateurAvecCentroide;
import Evaluateur.AvecCentroide.Silhouette.EvaluateurSilhouette;
import Evaluateur.AvecCentroide.Silhouette.EvaluateurSilhouetteMultiClassifieur;
import Evaluateur.AvecCentroide.VarianceRatio.EvaluateurRatioVariance;
import Evaluateur.AvecCentroide.VarianceRatio.EvaluateurRatioVarianceMultiClassifieur;
import Fichier.CSV.FileCSV;
import Fichier.FichierTxt;
import Fichier.Weka.FichierArff;
import FiltreurDomif.FiltreurDomifLongueurMin;
import FiltreurDomif.FiltreurDomifSansAuteur;
import FiltreurDomif.FiltreurDomifSelonDictionnaire;
import FiltreurToken.FiltreurAntidictionnaire;
import FiltreurToken.FiltreurFrequenceDocumentaire;
import Indexation.Indexation;
import Indexation.IndexationBase;
import Indexation.Indexeur.Unif;
import Indexation.Indexeur.UnifBase;
import Indexation.Indexeur.UnifContenu;
import Indexation.Indexeur.UnifParDomif.OLD.IndexeurUnifContenuParDomif;
import Indexation.Indexeur.UnifParDomif.IndexeurUnifParDomif;
import Indexation.Indexeur.UnifParDomif.IndexeurUnifParDomif_JBlas;
import Indexation.Indexeur.UnifParDomif.IndexeurUnifParDomif_MTJ;
import Indexation.Indexeur.UnifParDomif.OLD.IndexeurUnifparDomifOld;
import Indexation.Indexeur.UnifParUnif.IndexeurUnifParUnif;
import MDS.MDS;
import Matrice.MtjMatrice;
import Matrice.Vectorisateur.Domif.VectorisateurDomif;
import Matrice.Vectorisateur.UnifParDomif.VectorisateurUnifParDomif;
import Matrice.Vectorisateur.VecteurCreux;
import Matrice.Vectorisateur.VecteurIndicie;
import Matrice.Vectorisateur.Vectoriseur;
import Metrique.Distance.DistanceCosinus;
import Metrique.Distance.DistanceEuclidienne;
import Metrique.Distance.ErreurQuadratique;
import Metrique.Similitude.Cosinus;
import Metrique.Similitude.MetriqueSim;
import Ponderation.InformationMutuelle;
import Ponderation.ProbConditionnelle1;
import Ponderation.CorrelationMatthews;
import Ponderation.InformationMutuellePositive;
import Ponderation.PointwiseMutualInformation;
import Ponderation.PonderateurParClasse;
import Ponderation.Ponderation;
import Projets.Projet;
import ReductionDimensionnelle.SVD.CalculateurSVD_JBlas;
import ReductionDimensionnelle.SVD.ReducteurSVD_MTJ;
import SQLite.BdSimParadigmatiqueEntreMot;
import SQLite.Connexion;
import Segmentation.Domif;
import Segmentation.Segmentation;
import Segmentation.Segmenteur.Contexte.SegmenteurParDocument;
import Segmentation.Segmenteur.Mot.SegmenteurDeMotParContexte;
import cern.colt.Arrays;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.NotConvergedException;
import no.uib.cipr.matrix.SVD;
import org.jblas.DoubleMatrix;

/**
 *
 * @author JF Chartier
 */
public class ProjetMagritte2 
{
    public static void main(String[] args)
    {
        try
        {
            
                        
            String cheminFichier;
            cheminFichier = ("C:\\Users\\JF Chartier\\Documents\\Collections standard\\Magritte\\html\\Magritte fichier html prepare.txt");            
//            cheminFichier = "C:\\Users\\JF Chartier\\Documents\\Collections standard\\Pierce\\The Collected Papers of Charles Sanders Peirce5(Utf-8Sanshtmltags).txt";
//            cheminFichier = "C:\\Users\\JF Chartier\\Documents\\Collections standard\\Magritte\\magritteAvecHapaxCorpusPourAcfas.txt";
//            cheminFichier = "C:\\Users\\JF Chartier\\Documents\\Collections standard\\Magritte\\magritteAvecHapax.txt";
            
            new ProjetMagritte2().analyserThemeOeuvre(cheminFichier);
//            new ProjetMagritte2().classificationDescripteurs(cheminFichier);
//            ProjetMagritte2.analyserDescripteurParadigmatique(cheminFichier);
//            ProjetMagritte2.analyserDescripteurSyntagmatique(cheminFichier);
            
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            
        }
        
    
    }
    
    public Map<Integer, VecteurCreux> reduireViaSvd (Map<Integer, VecteurCreux> map_id_vecteur, int nbreUnif, int nbreDomif, int kFacteur) throws NotConvergedException
    {
        Map<Integer,Integer> map_idDomif_idLigne = new HashMap<>(map_id_vecteur.size());
        int idLigne = 0;
        for (int idDomif: map_id_vecteur.keySet())
        {
            map_idDomif_idLigne.put(idDomif, idLigne);
            idLigne++;
        }
        DenseMatrix matrice = MtjMatrice.convertirVecteurVersLigneMatrice(map_id_vecteur, map_idDomif_idLigne, nbreDomif, nbreUnif);
        matrice = new DenseMatrix(matrice.transpose(new DenseMatrix(nbreUnif, nbreDomif)));
        if (matrice.numRows()!=nbreUnif && matrice.numColumns() != nbreDomif)
        {
            System.out.println("mauvais nombre de ligne");
            System.exit(1);
        }

        matrice = ReducteurSVD_MTJ.reduireEtPondererMatriceV(new SVD(matrice.numRows(), matrice.numColumns()).factor(matrice), kFacteur);
        return MtjMatrice.convertirLigneDeLaMatrice(matrice, map_idDomif_idLigne);
    }
    
    public static void analyserDescripteurSyntagmatique (String cheminFichier) throws IOException, SQLException
    {
        Map<Integer, Domif> map_id_domif = ProjetMagritte2.pretraitementOeuvre(cheminFichier, "");
        Map<String, Integer> map_unifString_id = Segmentation.recupererMap_unifString_idUnif(map_id_domif.values());
        Map<Integer, Unif> map_id_unif = IndexeurUnifParDomif.indexerUnif(map_id_domif, map_unifString_id);
        TreeMap<Integer, String> map_idUnif_unifString = new TreeMap(IndexationBase.recupererMap_IdUnif_UnifString(map_id_unif));
        new AfficherTableau("Les unifs du corpus", "IdUnif", "unif", map_idUnif_unifString).setVisible(true);
                
        Map<Integer, Set<Integer>> map_idDomif_ensembleIdUnif = IndexationBase.recupererMap_idUnif_ensembleIdDomif(map_id_unif);

        Map<Integer, VecteurCreux> map_id_vecteur = new VectorisateurUnifParDomif(map_idDomif_ensembleIdUnif).vectorisationCreuse(map_id_unif);
        
        soustraireVecteurEtNPlusProcheVoisin(map_idUnif_unifString, map_id_vecteur, 747, 1560, 15);
        addidionner2VecteursEtNPlusProcheVoisin(map_idUnif_unifString, map_id_vecteur, 747, 600, 15);
        multiplier2VecteursEtNPlusProcheVoisin(map_idUnif_unifString, map_id_vecteur, 747, 600, 15);
        
        System.exit(1);
        
        
        MDS.calculerReductionDimensionelle(map_id_vecteur, new DistanceEuclidienne());
        System.exit(1);
        
        Partition partition = classerOeuvre(map_id_vecteur);
        int nbrePPV = 10;
        calculerProximiteEntreUnif(map_id_vecteur, nbrePPV, map_idUnif_unifString, partition.recupererMap_IdDomif_IdClasse());
        ponderationUnifParClasse(map_id_vecteur, map_id_unif, partition);
        
    }
    
    public static void analyserDescripteurParadigmatique (String cheminFichier) throws IOException, SQLException
    {
        Map<Integer, Domif> map_id_domif = ProjetMagritte2.pretraitementOeuvre(cheminFichier, "");
        Map<String, Integer> map_unifString_id = Segmentation.recupererMap_unifString_idUnif(map_id_domif.values());
        Map<Integer, Unif> map_id_unif = IndexeurUnifParDomif.indexerUnif(map_id_domif, map_unifString_id);
        TreeMap<Integer, String> map_idUnif_unifString = new TreeMap(IndexationBase.recupererMap_IdUnif_UnifString(map_id_unif));
        new AfficherTableau("Les unifs du corpus", "IdUnif", "unif", map_idUnif_unifString).setVisible(true);
                
        Map<Integer, UnifContenu> map_idCooc_unifCooc = new IndexeurUnifParUnif().indexerUnifContenuParUnifContenu(IndexationBase.recupererMap_IdDomif_listeIdUnifUnique(map_id_unif), map_idUnif_unifString);
        
        Map<Integer, Set<Integer>> map_idDomif_ensembleIdUnif = IndexationBase.recupererMap_IdDomif_ensembleIdUnif(map_idCooc_unifCooc);
//        for (int idUnif: map_id_vecteur.keySet())
//        {
//            if (!map_idUnif_unifString.keySet().contains(idUnif))
//            {
//                System.out.println("le idUnif :" + idUnif + " " + map_idUnif_unifString.get(idUnif)+" est un idUnif inconnu mais vectorise");
//                System.exit(1);
//            }
//        }
        Map<Integer, VecteurCreux> map_id_vecteur = new VectorisateurDomif(map_idDomif_ensembleIdUnif).vectorisationCreuse(map_idCooc_unifCooc);
        
        soustraireVecteurEtNPlusProcheVoisin(map_idUnif_unifString, map_id_vecteur, 747, 1560, 15);
        addidionner2VecteursEtNPlusProcheVoisin(map_idUnif_unifString, map_id_vecteur, 747, 600, 10);
        multiplier2VecteursEtNPlusProcheVoisin(map_idUnif_unifString, map_id_vecteur, 747, 600, 10);
        System.exit(1);
        
        for (int idUnif: map_id_vecteur.keySet())
        {
            if (!map_idUnif_unifString.keySet().contains(idUnif))
            {
                System.out.println("le idUnif :" + idUnif + " " + map_idUnif_unifString.get(idUnif)+" est un idUnif inconnu mais vectorise");
                System.exit(1);
            }
        }
        
//        MDS.calculerReductionDimensionelle(map_id_vecteur, new DistanceEuclidienne());
        
        
        Partition partition = classerOeuvre(map_id_vecteur);
        int nbrePPV = 10;
        calculerProximiteEntreUnif(map_id_vecteur, nbrePPV, map_idUnif_unifString, partition.recupererMap_IdDomif_IdClasse());
        ponderationUnifParClasse(map_id_vecteur, map_id_unif, partition);
        
    }
    
    
    public void analyserThemeOeuvre (String cheminFichier) throws IOException, SQLException, NotConvergedException, Exception
    {
//        Map<Integer, Domif> map_id_domif =  ProjetPierce.pretraitement(cheminFichier);
//        Map<Integer, Domif> map_id_domif = ProjetMagritte2.pretraitementOeuvre(cheminFichier, "");
        Map<Integer, Domif> map_id_domif = new ParserMagritte().recupererOeuvre(new File(cheminFichier));
                
        
//        Map<String, Integer> map_u_f = Segmentation.recupererMap_unifString_frequenceDocumentaire(Segmentation.recupererMap_idDomif_listeTokens(map_id_domif));
//        map_id_domif  = new FiltreurFrequenceDocumentaire(map_u_f, 2, (map_id_domif.size()/2)).filtrer(map_id_domif);

//        Set<String> dictionnaire = new TreeSet<String>();
//        dictionnaire.add("mind");
//        map_id_domif = new FiltreurDomifSelonDictionnaire(dictionnaire).filtrerDomif(map_id_domif);
        
        Map<String, Integer> map_unifString_id = Segmentation.recupererMap_unifString_idUnif(map_id_domif.values());
        new AfficherTableau("unif", "unif", "id", new TreeMap(map_unifString_id)).setVisible(true);
        Map<Integer, List<Integer>> map_idDomif_listeIdUnif = Segmentation.recupererMap_idDomif_listeIdUnifUnique(map_id_domif, map_unifString_id);
        Map<Integer, Integer> map_idUnif_idLigne = associerId(new TreeSet<Integer>(map_unifString_id.values()));
        Map<Integer, Integer> map_idDomif_id = associerId(map_idDomif_listeIdUnif.keySet());
//        DenseMatrix matrice = IndexeurUnifParDomif_MTJ.indexerUnifParDomif(map_idDomif_listeIdUnif, map_idDomif_id, map_idUnif_idLigne);
//        DenseMatrix matrice = IndexeurUnifParDomif_MTJ.indexerTFIDFUnifParDomif(map_idDomif_listeIdUnif,Segmentation.recupererMap_idUnif_ensembleIdDomif(map_idDomif_listeIdUnif, map_unifString_id), map_idDomif_id, map_idUnif_idLigne);
        
        Map<Integer, VecteurCreux> map_id_vecteur = CalculateurSVD_JBlas.reduireEtPondererMatriceV(IndexeurUnifParDomif_JBlas.indexerUnifParDomif_double(map_idDomif_listeIdUnif, map_idDomif_id, map_idUnif_idLigne), 200, map_idDomif_id);
        
//        matrice = CalculateurSVD_MTJ.reduireEtPondererMatriceV(new SVD(matrice.numRows(), matrice.numColumns()).factor(matrice), 200);
//        Map<Integer, VecteurCreux> map_id_vecteur = MtjMatrice.convertir(matrice, map_idDomif_id);
//        for (int i: map_id_vecteur.keySet())
//            System.out.println(Arrays.toString(map_id_vecteur.get(i).getTabPonderation()));
//        System.exit(1);
        
        
        
////////        Map<Integer, UnifContenu> map_id_unif = IndexeurUnifParDomif.indexerUnifContenu(map_id_domif, map_unifString_id);
//        IndexeurUnifContenuParDomif index = new IndexeurUnifContenuParDomif(map_unifString_id, Segmentation.recupererMap_idDomif_listeTokens(map_id_domif));
//        index.indexer();
//        Map<Integer, UnifContenu> map_id_unif = index.getMap_id_unif();
//        
//        statistiqueIndexation(map_id_unif);
//        Map<Integer, Set<Integer>> map_idDomif_ensembleIdUnif = IndexationBase.recupererMap_IdDomif_ensembleIdUnif(map_id_unif);
//        Map<Integer, VecteurCreux> map_id_vecteur = new VectorisateurDomif(map_idDomif_ensembleIdUnif).vectorisationCreuse(map_id_unif);
        
        
//        map_id_vecteur = Vectorisateur.normerVecteurCreux(map_id_vecteur.values());
        
        
//            String csv = new FichierCSV().construireFichierCSV(map_id_vecteur); 
//            String pathCsv = "C:\\Users\\JF Chartier\\Documents\\Lanci\\Projet sur BioMed\\MatriceCsvMagritte.csv";
//            FichierTxt.enregistrerFichierTxt(pathCsv, csv);
//            System.exit(1);
//        MDS.calculerReductionDimensionelle(map_id_vecteur, new DistanceEuclidienne());
//        System.exit(1);
        map_id_vecteur = reduireViaSvd(map_id_vecteur, index.getMap_id_unif().size(), map_id_vecteur.size(), 10);
        
//        ProjetMagritte2.approximerK(new EvaluateurSilhouette(map_id_vecteur, new ErreurQuadratique()), new Cosinus(), 2, 30, 1, 5);
//        ProjetMagritte2.approximerK(new EvaluateurRatioVariance(map_id_vecteur, new ErreurQuadratique()), new Cosinus(), 2, 30, 1, 5);
                
        
//        Partition partition  = new Partition(0, new Dbscan().partitionner(map_id_vecteur, 2.80, 4));
        
        Partition partition = classerOeuvre(map_id_vecteur, 15, 15);
        TreeMap<Integer, Integer> mapIdClasseNbreDomif = partition.recupererMapIdClasseNbreDomif();
        new AfficherTableau("Nombre de domif par classe", "idClasse", "nbre domif", mapIdClasseNbreDomif).setVisible(true);
        
        
//        ponderationUnifParClasse(map_id_vecteur, map_id_unif, partition);
        ponderationParClasse(map_id_domif.keySet(), partition.recupererMap_IdClasse_ensembleIdDomif(), Segmentation.recupererMap_idUnif_ensembleIdDomif(map_idDomif_listeIdUnif, map_unifString_id));
//        ponderationParClasse(map_idDomif_ensembleIdUnif.keySet(), partition.recupererMap_IdClasse_ensembleIdDomif(), Segmentation.recupererMap_date_ensembleIdDomif(map_id_domif));
        
    }
    
    public void classificationDescripteurs (String cheminFichier) throws IOException, SQLException, NotConvergedException
    {
        Map<Integer, Domif> map_id_domif = ProjetMagritte2.pretraitementOeuvre(cheminFichier, "");
        Map<String, Integer> map_unifString_id = Segmentation.recupererMap_unifString_idUnif(map_id_domif.values());
        Map<Integer, List<Integer>> map_idDomif_listeIdUnif = Segmentation.recupererMap_idDomif_listeIdUnifUnique(map_id_domif, map_unifString_id);
        
        Map<Integer, Integer> map_idUnif_id = associerId(new TreeSet<Integer>(map_unifString_id.values()));
        Map<Integer, Integer> map_idDomif_id = associerId(map_idDomif_listeIdUnif.keySet());
        DenseMatrix matrice = IndexeurUnifParDomif_MTJ.indexerUnifParDomif(map_idDomif_listeIdUnif, map_idDomif_id, map_idUnif_id);
        matrice = ReducteurSVD_MTJ.reduireEtPondererMatriceU(new SVD(matrice.numRows(), matrice.numColumns()).factor(matrice), 300);
        matrice = MtjMatrice.normerVecteurLigne(matrice);
        Map<Integer, VecteurCreux> map_id_vecteur = MtjMatrice.convertirLigneDeLaMatrice(matrice, map_idUnif_id);
        
        ProjetMagritte2.approximerK(map_id_vecteur, new Cosinus(), 500, 0.000000, 2, 30, 1, 15);
//        Partition partition = classerOeuvre(map_id_vecteur);
        
//        ponderationUnifParClasse(map_id_vecteur, map_id_unif, partition);
//        ponderationParClasse(map_idDomif_ensembleIdUnif.keySet(), partition.recupererMap_IdClasse_ensembleIdDomif(), Segmentation.recupererMap_date_ensembleIdDomif(map_id_domif));
        
    }
    private Map<Integer, Integer> associerId (Set<Integer> ensembleId)
    {
        Map<Integer, Integer> map =new HashMap<>(ensembleId.size());
        int id = 0;
        for (int e: ensembleId)
        {
            map.put(e, id);
            id++;
        }
        return map;        
    }
    
    public static void approximerK (EvaluateurAvecCentroide evaluateur, MetriqueSim metriqueSim, int minNombreClasse, int maxNombreClasse, int intervalle, int iterationKM)
    {
        ClassifieurKmeansRepete kmR = new ClassifieurKmeansRepete(new TreeMap(evaluateur.getMap_Id_VecteurCreux()), metriqueSim, 500, 0.0, true, iterationKM);
        new ApproximateurNombreClasse().approximerNombreClasseKmeans(kmR, evaluateur, minNombreClasse, maxNombreClasse, intervalle);
    }
    
    public static void approximerK (Map<Integer, VecteurCreux> map_id_vecteurCreux, MetriqueSim metriqueSim, int nbreMaxIteration, double minErreur, int minNombreClasse, int maxNombreClasse, int intervalle, int iterationKM)
    {
        ClassifieurKmeansRepete kmR = new ClassifieurKmeansRepete(new TreeMap(map_id_vecteurCreux), metriqueSim, nbreMaxIteration, minErreur, true, iterationKM);
//        EvaluateurRatioVarianceMultiClassifieur eval = new EvaluateurRatioVarianceMultiClassifieur();
        EvaluateurSilhouetteMultiClassifieur eval = new EvaluateurSilhouetteMultiClassifieur();
        ApproximateurNombreClasse aaa = new ApproximateurNombreClasse();
        aaa.approximerNombreClasseKmeans(kmR, eval, map_id_vecteurCreux, minNombreClasse, maxNombreClasse, intervalle);
    }
    
    public static Map<Integer, Domif> pretraitementOeuvre (String corpusFilePath, String bdPathMagritte) throws IOException, SQLException
    {
        System.out.println("PRETRAITEMENT");
        Segmentation segmentation = new SegmenteurParDocument().segmenter(Projet.telechargerFichierJFC(corpusFilePath));
        
        TreeMap<Integer, Domif> map_id_domif = Tokenisation.Tokeniseur.tokeniser(segmentation.getMap_Id_Domif());
//        map_id_domif = new SegmenteurDeMotParContexte().segmenter(map_id_domif);
        
        map_id_domif = new FiltreurAntidictionnaire("francais", false).filtrer(map_id_domif);
        
        TreeMap<Integer, String> map_id_segment = new TreeMap(Segmentation.recupererMap_idDomif_tokensEnString(map_id_domif));
        TreeMap<Integer, Integer> map_id_date = new TreeMap(Segmentation.recupererMap_idDomif_date(map_id_domif));
        new AfficherTableau("Les oeuvres", "id oeuvre", "descripteur des oeuvres", map_id_segment).setVisible(true);
        new AfficherTableau("Les date des oeuvres", "id oeuvre", "date", map_id_date).setVisible(true);
                    
        
        
//        Connexion bdMagritte = new BdSimParadigmatiqueEntreMot(bdPathMagritte);
//        bdMagritte.connect();
//        String nomTable = "OeuvreMagritte";
//        bdMagritte.createTable(nomTable, "(id INTEGER NOT NULL, descripteur TEXT NOT NULL, date INTEGER NOT NULL, PRIMARY KEY (id))");
//        
//        String sql = "INSERT INTO "+nomTable+" "+"(id,descripteur,date)"+" VALUES(?,?,?)";
//        PreparedStatement prepareStatement = bdMagritte.getConnexionSQL().prepareStatement(sql);
//        bdMagritte.getConnexionSQL().setAutoCommit(false);
//        for (Map.Entry<Integer, String> e: map_id_segment.entrySet())
//        {
//             prepareStatement.setInt(1, e.getKey());
//             prepareStatement.setString(2, e.getValue());
//             prepareStatement.setInt(3, map_id_date.get(e.getKey()));
//             prepareStatement.addBatch();
//        }
//        prepareStatement.executeBatch();
//        bdMagritte.getConnexionSQL().commit();
//        prepareStatement.close();
                
        return map_id_domif;
    }
    
    public static void soustraireVecteurEtNPlusProcheVoisin(final Map<Integer, String> map_idUnif_unifString, final Map<Integer, VecteurCreux> map_id_vecteur, int idVecteurReference, int idVecteurSoustraire, int nPlusProcheVoisin)
    {
        double[] vecteurX = map_id_vecteur.get(idVecteurReference).soustraireComplementOrthogonaux(map_id_vecteur.get(idVecteurSoustraire).getTabPonderation());
        TreeMap<Integer, Double> map_id_similitude = KPlusProcheVoisin.kPlusProcheVoisinDeX(map_id_vecteur, vecteurX, nPlusProcheVoisin, new Cosinus());
        
//        TreeMap<Integer, Double> map_id_similitude = KPlusProcheVoisin.kPlusProcheVoisinDeX(map_id_vecteur, map_id_vecteur.get(idVecteurReference).getTabPonderation(), nPlusProcheVoisin, new Cosinus());
        
        
        System.out.println("les plus proche voisin du descripteur "+map_idUnif_unifString.get(idVecteurReference)+" - " + map_idUnif_unifString.get(idVecteurSoustraire));
        for (int idUnif: map_id_similitude.keySet())
        {
            System.out.println(idUnif + "\t" + map_idUnif_unifString.get(idUnif) + "\t" +map_id_similitude.get(idUnif));
        }
               
    }
    
    public static void addidionner2VecteursEtNPlusProcheVoisin(final Map<Integer, String> map_idUnif_unifString, final Map<Integer, VecteurCreux> map_id_vecteur, int idVecteurReference, int idVecteurSoustraire, int nPlusProcheVoisin)
    {
        double[] vecteurX = map_id_vecteur.get(idVecteurReference).additionnerVecteursOLD(map_id_vecteur.get(idVecteurSoustraire).getTabPonderation());
        TreeMap<Integer, Double> map_id_similitude = KPlusProcheVoisin.kPlusProcheVoisinDeX(map_id_vecteur, vecteurX, nPlusProcheVoisin, new Cosinus());
        
//        TreeMap<Integer, Double> map_id_similitude = KPlusProcheVoisin.kPlusProcheVoisinDeX(map_id_vecteur, map_id_vecteur.get(idVecteurReference).getTabPonderation(), nPlusProcheVoisin, new Cosinus());
        
        
        System.out.println("les plus proche voisin du descripteur "+map_idUnif_unifString.get(idVecteurReference)+" + " + map_idUnif_unifString.get(idVecteurSoustraire));
        for (int idUnif: map_id_similitude.keySet())
        {
            System.out.println(idUnif + "\t" + map_idUnif_unifString.get(idUnif) + "\t" +map_id_similitude.get(idUnif));
        }
               
    }
    
    public static void multiplier2VecteursEtNPlusProcheVoisin(final Map<Integer, String> map_idUnif_unifString, final Map<Integer, VecteurCreux> map_id_vecteur, int idVecteurReference, int idVecteurSoustraire, int nPlusProcheVoisin)
    {
        double[] vecteurX = map_id_vecteur.get(idVecteurReference).multiplicationSimple(map_id_vecteur.get(idVecteurSoustraire).getTabPonderation());
        TreeMap<Integer, Double> map_id_similitude = KPlusProcheVoisin.kPlusProcheVoisinDeX(map_id_vecteur, vecteurX, nPlusProcheVoisin, new Cosinus());
        
//        TreeMap<Integer, Double> map_id_similitude = KPlusProcheVoisin.kPlusProcheVoisinDeX(map_id_vecteur, map_id_vecteur.get(idVecteurReference).getTabPonderation(), nPlusProcheVoisin, new Cosinus());
        
        
        System.out.println("les plus proche voisin du descripteur "+map_idUnif_unifString.get(idVecteurReference)+" * " + map_idUnif_unifString.get(idVecteurSoustraire));
        for (int idUnif: map_id_similitude.keySet())
        {
            System.out.println(idUnif + "\t" + map_idUnif_unifString.get(idUnif) + "\t" +map_id_similitude.get(idUnif));
        }
               
    }
    
    public static void statistiqueIndexation (final Map<Integer, ? extends UnifContenu> map_id_unif)
    {
        TreeMap<String, Double> map_unif_freqTot = IndexationBase.recupererMap_unifString_freqTotale_SurUnifContenu(map_id_unif);
        new AfficherTableau("La frequence totale de chaque unif", "UNIF", "Frequence Totale", map_unif_freqTot).setVisible(true);
        TreeMap<String, Integer> map_unif_freqDoc = IndexationBase.recupererMap_unifString_freqDoc(map_id_unif);
        new AfficherTableau("La frequence documentaire de chaque unif", "UNIF", "Frequence Documentaire", map_unif_freqDoc).setVisible(true);
        TreeMap<Integer, Double> map_freqDoc_prob = IndexationBase.recupererMap_freqDocumentaire_proportion(map_id_unif);
        new AfficherTableau("Loi de Zipf sur la frequence documentaire", "Frequence documentaire", "Probabilite", map_freqDoc_prob).setVisible(true);
    }
    
    public static void calculerProximiteEntreUnif (Map<Integer, VecteurCreux> map_id_vecteur, int k, Map<Integer, String> map_id_unifString, Map<Integer, Integer> map_idUnif_idClasse)
    {
        TreeMap<Integer, VecteurIndicie> map_vecteurSim = AnalyseSimilitude.construireMatriceSimilitudeCosinusOptimise(map_id_vecteur);
        for (int idUnif: map_id_unifString.keySet())
        {
            if (!map_id_vecteur.keySet().contains(idUnif))
            {
                System.out.println("le idUnif :" + idUnif + " " + map_id_unifString.get(idUnif)+" n'est pas vectorise");
                System.exit(1);
            }
        }
        for (int idUnif: map_id_vecteur.keySet())
        {
            if (!map_id_unifString.keySet().contains(idUnif))
            {
                System.out.println("le idUnif :" + idUnif + " " + map_id_unifString.get(idUnif)+" est un idUnif inconnu mais vectorise");
                System.exit(1);
            }
        }
        
        
        
        TreeMap<Integer, VecteurIndicie> map_idVecteur_vecteurPlusProche = AnalyseSimilitude.selectionnerKplusProcheVoisinDeChaqueVecteur(map_vecteurSim, k);
        AnalyseSimilitude.afficherSimilitudeEntreMots(map_idVecteur_vecteurPlusProche, map_id_unifString, map_idUnif_idClasse);
        
    }
    
        
        
    public static Partition classerOeuvre (Map<Integer, VecteurCreux> map_id_vecteur, int minK, int maxK)
    {
//        Partition partitionBest = Projet.trouverMeilleurPartitionKmeansMaximumSelonGap(new TreeMap(map_id_vecteur), new Cosinus(), 500, 0.00000, minK, maxK, 1, true, 10);
        Partition partitionBest = Projet.trouverMeilleurPartitionKmeansMaximumSelonRatioVariance(new TreeMap(map_id_vecteur), new Cosinus(), 500, 0.00000, minK, maxK, 1, true, 15);
//        Partition partitionBest = Projet.trouverMeilleurPartitionKmeansSelonCalinski(new TreeMap(map_id_vecteur), new Cosinus(), 300, 0.0000001, 51, 75, 1, false);
//        Partition partitionBest = Projet.trouverMeilleurPartitionKmeansSelonCalinskyMonteCarlo(new TreeMap(map_id_vecteur), new Cosinus(), 300, 0.0000001, 2, 100, 1, true);
//        Partition partitionBest = Projet.trouverMeilleurPartitionKmeansMaximumSelonCalinskyMonteCarlo(new TreeMap(map_id_vecteur), new Cosinus(), 300, 0.0000001, 2, 50, 1, false, 1);
//        Partition partitionBest = Projet.trouverMeilleurPartitionKmeansSelonCompaciteMonteCarlo(new TreeMap(map_id_vecteur), new Cosinus(), 300, 0.0000001, 2, 40, 1, false);
        TreeMap<Integer, Integer> map_IdDomif_IdClasse = partitionBest.recupererMap_IdDomif_IdClasse();
        new AfficherTableau("Le idClasse pour chaque idDomif d'une partition", "ID DOMIF", "ID CLASSE", map_IdDomif_IdClasse).setVisible(true);
        TreeMap<Integer, Integer> mapIdClasseNbreDomif = partitionBest.recupererMapIdClasseNbreDomif();
        new AfficherTableau("Nombre de domif par classe", "idClasse", "nbre domif", mapIdClasseNbreDomif).setVisible(true);
        TreeMap<Integer, Double> map_idDomif_proximiteCentroide = partitionBest.calculerEcartAuCentroide(new TreeMap(map_id_vecteur), new Cosinus());
        new AfficherTableau("Proximite de chaque oeuvre a son centroide", "idOeuvre", "proximite avec le centroide", map_idDomif_proximiteCentroide).setVisible(true);
        
        return partitionBest;
    }
    
    public static void ponderationUnifParClasse (Map<Integer, VecteurCreux> map_id_vecteur, Map<Integer, ? extends UnifContenu> map_id_ToutLesUnif, Partition partition)
    {
//            // cette appel de methode est simplement pour permettre de calculer la ponderation sur tous les unif et pas seulement celles utilisees pour la classification
//        TreeMap<Integer, Unif> map_id_UnifReclasses = Indexation.reconstruireMap_IdUnif_Unif(new TreeMap(map_id_ToutLesUnif), partition.getMap_id_Classe());            
        TreeMap<Integer, VecteurIndicie> map_IdUnif_VecteurPonderationParClasse;
        Ponderation ponderationParClasse;
//            
//////            // information mutuelle
//        map_IdUnif_VecteurPonderationParClasse = new PonderateurParClasse().pondererUnifParClasse(map_id_ToutLesUnif, partition.recupererMap_IdDomif_IdClasse(), new InformationMutuelle());
//        ponderationParClasse = new Ponderation(map_IdUnif_VecteurPonderationParClasse);
//        ponderationParClasse.afficherTableauDesPonderationUnifParClasse(new TreeMap(Indexation.recupererMap_IdUnif_UnifString(map_id_ToutLesUnif)), "information mutuelle");
//        
//        ////            // PMI
//        map_IdUnif_VecteurPonderationParClasse = new PonderateurParClasse().pondererUnifParClasse(map_id_ToutLesUnif, partition.recupererMap_IdDomif_IdClasse(), new PointwiseMutualInformation());
//        ponderationParClasse = new Ponderation(map_IdUnif_VecteurPonderationParClasse);
//        ponderationParClasse.afficherTableauDesPonderationUnifParClasse(new TreeMap(Indexation.recupererMap_IdUnif_UnifString(map_id_ToutLesUnif)), "PMI");
//        
//        ////            // Information mutuelle positive
//        map_IdUnif_VecteurPonderationParClasse = new PonderateurParClasse().pondererUnifParClasse(map_id_ToutLesUnif, partition.recupererMap_IdDomif_IdClasse(), new InformationMutuellePositive());
//        ponderationParClasse = new Ponderation(map_IdUnif_VecteurPonderationParClasse);
//        ponderationParClasse.afficherTableauDesPonderationUnifParClasse(new TreeMap(Indexation.recupererMap_IdUnif_UnifString(map_id_ToutLesUnif)), "Info mutuelle positive");
// 
//        ////            // probabilite conditionnelle
//        map_IdUnif_VecteurPonderationParClasse = new PonderateurParClasse().pondererUnifParClasse(map_id_ToutLesUnif, partition.recupererMap_IdDomif_IdClasse(), new CoefficientProbConditionnelle1());
//        ponderationParClasse = new Ponderation(map_IdUnif_VecteurPonderationParClasse);
//        ponderationParClasse.afficherTableauDesPonderationUnifParClasse(new TreeMap(Indexation.recupererMap_IdUnif_UnifString(map_id_ToutLesUnif)), "probabilite conditionnelle");
// 
        ////            CorrelationMatthews
        map_IdUnif_VecteurPonderationParClasse = new PonderateurParClasse().pondererUnifParClasse(map_id_ToutLesUnif, partition.recupererMap_IdDomif_IdClasse(), new CorrelationMatthews());
        ponderationParClasse = new Ponderation(map_IdUnif_VecteurPonderationParClasse);
        ponderationParClasse.afficherTableauDesPonderationUnifParClasse(new TreeMap(Indexation.recupererMap_IdUnif_UnifString(map_id_ToutLesUnif)), "CorrelationMatthews");
 
        
        
//        TreeMap<Integer, VecteurIndicie> map_vecteurSim = AnalyseSimilitude.construireMatriceSimilitudeCosinusOptimise(map_id_vecteur);
//        AnalyseSimilitude.afficherSimilitudeEntreMots(map_vecteurSim, IndexationBase.recupererMap_IdUnif_UnifString(map_id_ToutLesUnif), partition.recupererMap_IdDomif_IdClasse());
    }
    
    public static void ponderationParClasse (Set<Integer> ensembleIdDomif, Map<Integer, Set<Integer>> map_idClasse_ensembleIdDomif, Map<Integer, Set<Integer>> map_idVariable_ensembleIdDomif)
    {
//            // cette appel de methode est simplement pour permettre de calculer la ponderation sur tous les unif et pas seulement celles utilisees pour la classification
//        TreeMap<Integer, Unif> map_id_UnifReclasses = Indexation.reconstruireMap_IdUnif_Unif(new TreeMap(map_id_ToutLesUnif), partition.getMap_id_Classe());            
        TreeMap<Integer, VecteurIndicie> map_IdVar_VecteurPonderationParClasse;
        Ponderation ponderationParClasse;
//            
////            // information mutuelle
        map_IdVar_VecteurPonderationParClasse = new PonderateurParClasse().pondererVariableParClasse(ensembleIdDomif, map_idClasse_ensembleIdDomif, map_idVariable_ensembleIdDomif, new CorrelationMatthews());
        ponderationParClasse = new Ponderation(map_IdVar_VecteurPonderationParClasse);
        Map<Integer, String> map_IdVar_VarString = new HashMap<Integer,String>(map_IdVar_VecteurPonderationParClasse.size());
        for (int idVar: map_IdVar_VecteurPonderationParClasse.keySet())
        {
            map_IdVar_VarString.put(idVar, String.valueOf(idVar));
        }
        ponderationParClasse.afficherTableauDesPonderationUnifParClasse(map_IdVar_VarString, "correlation Matthews");
        
        
//        TreeMap<Integer, VecteurIndicie> map_vecteurSim = AnalyseSimilitude.construireMatriceSimilitudeCosinusOptimise(map_id_vecteur);
//        AnalyseSimilitude.afficherSimilitudeEntreMots(map_vecteurSim, IndexationBase.recupererMap_IdUnif_UnifString(map_id_ToutLesUnif), partition.recupererMap_IdDomif_IdClasse());
    }
    
}
