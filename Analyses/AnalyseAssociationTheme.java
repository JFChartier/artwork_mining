/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte.Analyses;

import Affichage.Tableau.AfficheurTableau;
import Affichage.Tableau.AfficheurVecteur;
import AnalyseSimilitude.AnalyseSimilitude;
import Classification.Classe;
import Classification.Partition;
import ClassificationAutomatique.Supervisee.KPPV.KPlusProcheVoisin;
import Matrice.Vectorisateur.VecteurCreux;
import Matrice.Vectorisateur.VecteurIndicie;
import Matrice.Vectorisateur.VectoriseurUtils;
import Metrique.Metrique;
import Metrique.Similitude.CorrelationPearsonIndicie;
import Metrique.Similitude.MetriqueSim;
import UtilsJFC.TrieUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import magritte.Oeuvre;
import magritte.OeuvresUtils;

/**
 *
 * @author JF Chartier
 */
public class AnalyseAssociationTheme 
{
    
    // cete methode genere des resultats peu interressant. Elle ne semble pas associe a une oeuvre les themes dominants
    // 
    public static void analyseThemeParOeuvre (Map<Integer, String> map_idOeuvre_titre, Map<Integer, double[]> map_idTheme_centroide, Map<Integer, VecteurIndicie> vecteursOeuvre, MetriqueSim metrique)
    {
        AfficheurTableau.afficher2Colonnes("id oeuvre et titre", "id", "titre", map_idOeuvre_titre);
//        Map<Integer, VecteurIndicie> map_idTheme_vecteurs = Partition.calculerMetriqueEntreCentroideEtVecteurs(map_idTheme_centroide, VectoriseurUtils.convertir(vecteursOeuvre.values(), 0.0), metrique);
//        AfficheurVecteur.afficherVecteurs("proximite du theme avec l'oeuvre", new TreeSet<Integer>(vecteursOeuvre.keySet()), map_idTheme_vecteurs.values());
        
        AfficheurVecteur.afficherVecteurs("proximite d'une oeuvre avec le centroide du theme", map_idOeuvre_titre, map_idTheme_centroide.keySet(), Partition.calculerMetriqueEntreVecteurEtCentroides(VectoriseurUtils.convertir(vecteursOeuvre.values(), 0.0), map_idTheme_centroide, metrique).values());
        
        analyserCorrelationEntreTheme(map_idTheme_centroide, vecteursOeuvre, metrique);
        analyseThemePlusProcheOeuvre(map_idTheme_centroide, vecteursOeuvre, 1, metrique);
    }
    
    // nombre d'oeuvre ou un theme est present
    public static void analysePresenceThemeParOeuvre(Map<Integer, Set<Integer>> map_idOeuvre_descripteurs, Map<Integer, Integer> map_idDescripteur_idClasse)
    {
        System.out.println("analysePresenceThemeParOeuvre");
        Map<Integer, Set<Integer>> map_idTheme_oeuvres = new TreeMap<>();
        Map<Integer, Integer> map_idTheme_nbreOeuvre = new TreeMap<>();
        for (int idT: new TreeSet<>(map_idDescripteur_idClasse.values()))
        {
            map_idTheme_oeuvres.put(idT, new TreeSet<>());
        }
        for (int idOeuvre: map_idOeuvre_descripteurs.keySet())
        {
            for (int idDes: map_idOeuvre_descripteurs.get(idOeuvre))
            {
                int idTheme = map_idDescripteur_idClasse.get(idDes);
                map_idTheme_oeuvres.get(idTheme).add(idOeuvre);
            }
        }
        for (int idT: map_idTheme_oeuvres.keySet())
            map_idTheme_nbreOeuvre.put(idT, map_idTheme_oeuvres.get(idT).size());
        AfficheurTableau.afficher2Colonnes("id theme et nbre oeuvre", "id theme", "nbre oeuvre", map_idTheme_nbreOeuvre);
        
    }
    
    public static void analyserImportanceThemeCorpus(Collection<List<Integer>> listesIdDescripteurs, Map<Integer, Integer> map_idDescripteur_idClasse)
    {
        System.out.println("analyserImportanceThemeCorpus");
        double nbreTokens=0.0;
        for (List<Integer> l: listesIdDescripteurs)
            nbreTokens= nbreTokens+l.size();
        Map<Integer, Double> map_idClasse_proportionTokens = new TreeMap<>();
        Map<Integer, Integer> map_idClasse_nbreTokens = OeuvresUtils.getMap_idClasse_nbreInstanciationTokens(listesIdDescripteurs, map_idDescripteur_idClasse);
        for (int idClasse: map_idClasse_nbreTokens.keySet())
            map_idClasse_proportionTokens.put(idClasse, ((double)map_idClasse_nbreTokens.get(idClasse))/nbreTokens);
        AfficheurTableau.afficher2Colonnes("id theme et nbre instanciations", "id theme", "nbre instanciatons", map_idClasse_proportionTokens);
    }
    
    
    
    public static void analyseProximiteThemeParOeuvre (Map<Integer, double[]> map_idTheme_centroide, Map<Integer, VecteurIndicie> vecteursDescripteur, Collection<Oeuvre> oeuvres, Map<String, Integer> map_descripteur_id, MetriqueSim metrique)
    {
        Map<Integer, String> map_idOeuvre_titre = OeuvresUtils.getMap_idOeuvre_titre(oeuvres);
        AfficheurTableau.afficher2Colonnes("id oeuvre et titre", "id", "titre", map_idOeuvre_titre);
        Map<Integer, VecteurIndicie> map_idOeuvre_vecteurIdDescripteur = OeuvresUtils.additionnerEnsembleVecteurDescripteur(OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, map_descripteur_id), vecteursDescripteur);
        AfficheurVecteur.afficherVecteurs("proximite d'une oeuvre avec le centroide du theme", map_idOeuvre_titre, map_idTheme_centroide.keySet(), Partition.calculerMetriqueEntreVecteurEtCentroides(VectoriseurUtils.convertir(map_idOeuvre_vecteurIdDescripteur.values(), 0.0), map_idTheme_centroide, metrique).values());
        analyserCorrelationEntreTheme(map_idTheme_centroide, map_idOeuvre_vecteurIdDescripteur, metrique);
        analyseThemePlusProcheOeuvre(map_idTheme_centroide, map_idOeuvre_vecteurIdDescripteur, 1, metrique);
    }
    
    // cette methode pose procleme car elle utilise le Pearson sur des vecteurs sparses
    // ce qui est impossible car les zeros commun aux deux vecteurs ne sont pas calcule, ce qui affecte le calcul
    // de la moyenne et donc de la correlation
    // la methode fonctionne si les vecteurs sont converti en verteur creux 
    
    public static void analyserCorrelationEntreTheme (Map<Integer, double[]> map_idTheme_centroide, Map<Integer, VecteurIndicie> map_idOeuvre_vecteurIdDescripteur, Metrique metrique)
    {
        // initialiser les nouveaux id vecteurOeuvre
        // cette etape est necessaire pour le calcul de la corelation
        Map<Integer, VecteurCreux> map_idOeuvre_vecteurIdDescripteurCreux = new TreeMap<>();
        int i = 0;
        int d = map_idTheme_centroide.get(0).length;
        for (int id: map_idOeuvre_vecteurIdDescripteur.keySet())
        {
            map_idOeuvre_vecteurIdDescripteurCreux.put(i, new VecteurCreux(i, VectoriseurUtils.convertir(map_idOeuvre_vecteurIdDescripteur.get(id), d, 0.0).getTabPonderation()));
            i++;
        }
        
        Map<Integer, VecteurIndicie> map_idCentroide_vecteurSimAvecOeuvre = Partition.calculerMetriqueEntreCentroideEtVecteurs(map_idTheme_centroide, map_idOeuvre_vecteurIdDescripteurCreux, metrique);
        Map<Integer, VecteurIndicie> map_idCentroide_vecteurCorrelationAvecCentroide = AnalyseSimilitude.calculerMetriqueMatriceCarre(map_idCentroide_vecteurSimAvecOeuvre, new CorrelationPearsonIndicie(map_idOeuvre_vecteurIdDescripteur.size()), true, true);
        AfficheurVecteur.afficherVecteurs("correlation de pearson enre centroide des themes", map_idTheme_centroide.keySet(), map_idCentroide_vecteurCorrelationAvecCentroide.values());
    }
    
    public static void analyseThemePlusProcheOeuvre(Map<Integer, double[]> map_idTheme_centroide, Map<Integer, VecteurIndicie> map_idOeuvre_vecteurIdDescripteur, int kPlusProche, MetriqueSim metrique)
    {
        Map<Integer, Set<Integer>> map_idOeuvre_kPlusProche = KPlusProcheVoisin.kPlusProcheVoisinPourChaqueVecteursCible(VectoriseurUtils.convertir(map_idOeuvre_vecteurIdDescripteur.values(), 0.0).values(), map_idTheme_centroide, kPlusProche, metrique);
        Map<Integer, Set<Integer>> map_idTheme_ensembleIdOeuvre = TrieUtils.recupererMap_valeur_ensembleCle(map_idOeuvre_kPlusProche);
        for (int idTheme: map_idTheme_centroide.keySet())
        {
            if (!map_idTheme_ensembleIdOeuvre.containsKey(idTheme))
                map_idTheme_ensembleIdOeuvre.put(idTheme, new TreeSet<Integer>());
        }
        AfficheurTableau.afficher2Colonnes("nombre d'oeuvre dont le theme est ce theme est le plus proche", "theme", "nombre oeuvre", TrieUtils.recupererMap_cle_nbreElement(map_idTheme_ensembleIdOeuvre));
    }
    public static void analyseThemePlusProcheOeuvre(Map<Integer, double[]> map_idTheme_centroide, Collection<VecteurCreux> vecteurOeuvres, int kPlusProche, MetriqueSim metrique)
    {
        Map<Integer, Set<Integer>> map_idOeuvre_kPlusProche = KPlusProcheVoisin.kPlusProcheVoisinPourChaqueVecteursCible(vecteurOeuvres, map_idTheme_centroide, kPlusProche, metrique);
        Map<Integer, Set<Integer>> map_idTheme_ensembleIdOeuvre = TrieUtils.recupererMap_valeur_ensembleCle(map_idOeuvre_kPlusProche);
        for (int idTheme: map_idTheme_centroide.keySet())
        {
            if (!map_idTheme_ensembleIdOeuvre.containsKey(idTheme))
                map_idTheme_ensembleIdOeuvre.put(idTheme, new TreeSet<Integer>());
        }
        AfficheurTableau.afficher2Colonnes("nombre d'oeuvre dont le theme est ce theme est le plus proche", "theme", "nombre oeuvre", TrieUtils.recupererMap_cle_nbreElement(map_idTheme_ensembleIdOeuvre));
    }
    
    public static Map<Integer, VecteurIndicie> analyserRangThemeParOeuvre (Map<Integer, double[]> map_idTheme_centroide, Map<Integer, VecteurIndicie> vecteursDescripteur, Collection<Oeuvre> oeuvres, Map<String, Integer> map_descripteur_id, MetriqueSim metrique)
    {
        Map<Integer, VecteurIndicie> map_idOeuvre_vecteurIdDescripteur = OeuvresUtils.additionnerEnsembleVecteurDescripteur(OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, map_descripteur_id), vecteursDescripteur);
        Map<Integer, VecteurIndicie> map_idOeuvre_ponderationTheme = Partition.calculerMetriqueEntreVecteurEtCentroides(VectoriseurUtils.convertir(map_idOeuvre_vecteurIdDescripteur.values(), 0.0), map_idTheme_centroide, metrique);
        
        // changer la similarite par son rang 
        for (VecteurIndicie o: map_idOeuvre_ponderationTheme.values())
        {
            List<Entry<Integer, Double>> list = TrieUtils.trieMapDecroissantSelonValeur(o.getMap_Id_Ponderation());
            Map<Integer, Double> w = new HashMap<>(list.size());
            for (int i = 0; i< list.size(); i++)
            {
                w.put(list.get(i).getKey(), i+1.0);
            }
            VecteurIndicie v = new VecteurIndicie(o.getId(), w);
            map_idOeuvre_ponderationTheme.put(o.getId(), v);
        }
        AfficheurVecteur.afficherVecteurs("Rang de proximite d'un theme avec une oeuvre", map_idOeuvre_vecteurIdDescripteur.keySet(), VectoriseurUtils.transposer(map_idOeuvre_ponderationTheme.values(), map_idTheme_centroide.keySet()).values());
        return map_idOeuvre_ponderationTheme;
    }
    
    public static void analyseThemeParAnneeNew (Map<Integer, double[]> map_idTheme_centroide, Map<Integer, VecteurIndicie> vecteursDescripteur, Map<Integer, Collection<Integer>> map_date_collectionIdDescripteur, Map<String, Integer> map_descripteur_id, MetriqueSim metrique)
    {
        Map<Integer, VecteurIndicie> map_date_vecteurIdDescripteur = OeuvresUtils.additionnerCollectionVecteurDescripteur(map_date_collectionIdDescripteur, vecteursDescripteur);
//        Map<Integer, Classe> map_date_classe = OeuvresUtils.getMap_idDate_classeIdOeuvre(oeuvres);
//        Map<Integer, double[]> map_date_centroide = Partition.calculerCentroideDesClasse(VectoriseurUtils.convertir(map_date_vecteurIdDescripteur.values(), 0.0), map_date_classe);
        Map<Integer, VecteurIndicie> map_idTheme_vecteurs = Partition.calculerMetriqueEntreCentroideEtVecteurs(map_idTheme_centroide, VectoriseurUtils.convertir(map_date_vecteurIdDescripteur.values(), 0.0), metrique);
        AfficheurVecteur.afficherVecteurs("proximite du theme avec la date", new TreeSet<Integer>(map_date_vecteurIdDescripteur.keySet()), map_idTheme_vecteurs.values());
    }
    
    public static void analyseThemeParAnnee (Map<Integer, double[]> map_idTheme_centroide, Map<Integer, VecteurIndicie> vecteursDescripteur, Map<Integer, Set<Integer>> map_date_ensembleIdDescripteur, Map<String, Integer> map_descripteur_id, MetriqueSim metrique)
    {
        Map<Integer, VecteurIndicie> map_date_vecteurIdDescripteur = OeuvresUtils.additionnerEnsembleVecteurDescripteur(map_date_ensembleIdDescripteur, vecteursDescripteur);
//        Map<Integer, Classe> map_date_classe = OeuvresUtils.getMap_idDate_classeIdOeuvre(oeuvres);
//        Map<Integer, double[]> map_date_centroide = Partition.calculerCentroideDesClasse(VectoriseurUtils.convertir(map_date_vecteurIdDescripteur.values(), 0.0), map_date_classe);
        Map<Integer, VecteurIndicie> map_idTheme_vecteurs = Partition.calculerMetriqueEntreCentroideEtVecteurs(map_idTheme_centroide, VectoriseurUtils.convertir(map_date_vecteurIdDescripteur.values(), 0.0), metrique);
        AfficheurVecteur.afficherVecteurs("proximite du theme avec la date", new TreeSet<Integer>(map_date_vecteurIdDescripteur.keySet()), map_idTheme_vecteurs.values());
    }
    
    
    public static void analyseThemeParAnnee (Map<Integer, double[]> map_idTheme_centroide, Collection<Oeuvre> oeuvres, Map<String, Integer> map_descripteur_id, MetriqueSim metrique)
    {
        Map<Integer, VecteurIndicie> map_idOeuvre_vecteurIdDescripteur = OeuvresUtils.getMap_idOeuvre_vecteurIdDescripteurBM25(oeuvres, map_descripteur_id);
        Map<Integer, Classe> map_date_classe = OeuvresUtils.getMap_idDate_classeIdOeuvre(oeuvres);
        Map<Integer, double[]> map_date_centroide = Partition.calculerCentroideDesClasse(VectoriseurUtils.convertir(map_idOeuvre_vecteurIdDescripteur.values(), 0.0), map_date_classe);
        Map<Integer, VecteurIndicie> vecteurs = Partition.calculerMetriqueEntreVecteursEtCentroides(map_idTheme_centroide, map_date_centroide, metrique);
        AfficheurVecteur.afficherVecteurs("proximite du theme avec la date", map_date_centroide.keySet(), vecteurs.values());
    }
    
    public static void analyseThemeParAnnee (Map<Integer, double[]> map_idTheme_centroide, Map<Integer, VecteurIndicie> vecteursDescripteur, Collection<Oeuvre> oeuvres, Map<String, Integer> map_descripteur_id, MetriqueSim metrique)
    {
        Map<Integer, VecteurIndicie> map_idOeuvre_vecteurIdDescripteur = OeuvresUtils.additionnerEnsembleVecteurDescripteur(OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, map_descripteur_id), vecteursDescripteur);
        Map<Integer, Classe> map_date_classe = OeuvresUtils.getMap_idDate_classeIdOeuvre(oeuvres);
        Map<Integer, double[]> map_date_centroide = Partition.calculerCentroideDesClasse(VectoriseurUtils.convertir(map_idOeuvre_vecteurIdDescripteur.values(), 0.0), map_date_classe);
        Map<Integer, VecteurIndicie> vecteurs = Partition.calculerMetriqueEntreVecteursEtCentroides(map_idTheme_centroide, map_date_centroide, metrique);
        AfficheurVecteur.afficherVecteurs("proximite du theme avec la date", map_date_centroide.keySet(), vecteurs.values());
    }
}
