/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte.Analyses.Metaphore;

import Affichage.Tableau.AfficheurTableau;
import ClassificationAutomatique.Supervisee.KPPV.KPlusProcheVoisin;
import Matrice.Vectorisateur.VecteurCreux;
import Matrice.Vectorisateur.VectoriseurUtils;
import Metrique.Similitude.MetriqueSim;
import Metrique.Similitude.ProduitScalaire;
import Ponderation.CoefficientAssociation;
import Ponderation.InformationMutuelle;
import UtilsJFC.TrieUtils;
import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;
import magritte.Analyses.Analogie.Analyse;
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
public class AnalyseMetaphore 
{
    private static Map<String, Integer> map_descripteur_id;
    private static Map<Integer, String> map_id_descripteur;
    class Resultat
    {
        private double score;
        private double sim;
        private double diff;
        private int idCible;
        private int idCo;

        public Resultat(double score, int idCible, int idCo) {
            this.score = score;
            this.idCible = idCible;
            this.idCo = idCo;
        }

        public Resultat(double score, double sim, double diff, int idCible, int idCo) {
            this.score = score;
            this.sim = sim;
            this.diff = diff;
            this.idCible = idCible;
            this.idCo = idCo;
        }
        
        

        public int getIdCible() {
            return idCible;
        }

        public int getIdCo() {
            return idCo;
        }
        
        public double getScore() {
            return score;
        }

        public double getDiff() {
            return diff;
        }

        public double getSim() {
            return sim;
        }
        
        
        
    }
    
    public static void main(String[] args) throws IOException 
    {
        AnalyseMetaphore.trouverOeuvreAvecMetaphoreDeCible2(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<femme>","<main>", new ProduitScalaire(), 50);
        
//        AnalyseMetaphore.getMetaphore(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<femme>", new ProduitScalaire(), 1357);
//        new AnalyseMetaphore().getMetaphoreMax(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), new ProduitScalaire(), 200);
    }
    
    public static Collection<Oeuvre> pretraitement (File fileOeuvre) throws IOException
    {
        Collection<Oeuvre> oeuvres = ParserMagritte.recupererOeuvre(fileOeuvre);
        Pretraitement pre = new Pretraitement(false, false, false, false, -1, 1, 999999, true, true);
        oeuvres = pre.filtrerOeuvres(oeuvres);
        map_descripteur_id = OeuvresUtils.getMap_descripteur_id(oeuvres);
        map_id_descripteur = TrieUtils.inverserMap(map_descripteur_id);
        return oeuvres;
        
    }
    
    public static void getOeuvrePlusProcheDeCible(Map<Integer, VecteurCreux> vecteurs, double[] vecteurCible, Map<Integer, String> map_idO_titre, Map<Integer, Set<Integer>> map_idO_idsDesc, MetriqueSim metrique, int n)
    {
        vecteurs = VectoriseurUtils.additionnerVecteursCreux(map_idO_idsDesc, vecteurs, map_descripteur_id.size());
        vecteurs = VectoriseurUtils.normerVecteurCreuxB(vecteurs.values());        
        recupererNPlusProche(vecteurCible, map_idO_titre, vecteurs, metrique, n);
    }
    
    private static void recupererNPlusProche (double[] vecteurX, final Map<Integer, String> map_idUnif_unifString, final Map<Integer, VecteurCreux> map_id_vecteur, MetriqueSim metrique, int nPlusProcheVoisin)
    {
        Map<Integer, Double> map_id_similitude = KPlusProcheVoisin.kPlusProcheVoisinDeX(map_id_vecteur, vecteurX, nPlusProcheVoisin, metrique);
        HashMap<String, Double> map_nom_sim = new HashMap<>(); 
        for (Map.Entry<Integer, Double> w: TrieUtils.trieMapDecroissantSelonValeur(map_id_similitude, nPlusProcheVoisin))
        {
            map_nom_sim.put(map_idUnif_unifString.get(w.getKey()), map_id_similitude.get(w.getKey()));
//            System.out.println(w.getKey() + "\t " + map_idUnif_unifString.get(w.getKey()) + "\t " +map_id_similitude.get(w.getKey()));
            System.out.printf("%-15.150s %-60.60s %-30.30s\n", w.getKey(), map_idUnif_unifString.get(w.getKey()), map_id_similitude.get(w.getKey()));
        }
        AfficheurTableau.afficher2Colonnes("nom des element et similitude", "element", "metrique sim", map_nom_sim);
    }
    
    // recuperer les plus simipaires qui ne sont pas cooccurrents avec la cible
    // c=x*y
    // d=y-c
    // z = y-d
    public static void trouverOeuvreAvecMetaphoreDeCible1 (File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, String coDescripteur, MetriqueSim metrique, int n) throws IOException
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
        
//        double [] somme = VecteurCreux.additionnerVecteurs(vecteurCible.getTabPonderation(), coVecteur.getTabPonderation());
        double [] somme = VecteurCreux.soustraireComplementOrthogonaux(coVecteur.getTabPonderation(), diffCo);
        somme = VecteurCreux.normer(somme);
        recupererNPlusProche(somme, map_id_descripteur, vecteurs, metrique, n);
        
        Set<Integer> oeuvresSansCible = OeuvresUtils.getIdOeuvreSansCible(oeuvres, descripteurCible);
        Map<Integer, Set<Integer>> map_idO_idsDesc = OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, oeuvresSansCible, map_descripteur_id);
        Map<Integer, String> map_idO_titre = OeuvresUtils.getMap_idOeuvre_titre(oeuvres);        
        getOeuvrePlusProcheDeCible(vecteurs, somme, map_idO_titre, map_idO_idsDesc, metrique, n);
    }
    
    // recuperer les oeuvre les plus simipaires a x*y qui ne sont pas cooccurrents avec x
    public static void trouverOeuvreAvecMetaphoreDeCible2 (File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, String coDescripteur, MetriqueSim metrique, int n) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteurs = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, OeuvresUtils.getIdOeuvres(oeuvres), coefficient, 0.0);
        vecteurs = VectoriseurUtils.normerVecteurCreuxB(vecteurs.values());
        VecteurCreux vecteurCible = vecteurs.get(map_descripteur_id.get(descripteurCible));
        VecteurCreux coVecteur = vecteurs.get(map_descripteur_id.get(coDescripteur));
        double[] c = vecteurCible.multiplicationSimple(coVecteur.getTabPonderation());
        c = VecteurCreux.normer(c);
//        double[] diffCo = coVecteur.complementOrthogonaux(c);
//        diffCo = VecteurCreux.normer(diffCo);
//        double [] somme = VecteurCreux.additionnerVecteurs(vecteurCible.getTabPonderation(), coVecteur.getTabPonderation());
//        somme = VecteurCreux.complementOrthogonaux(somme, diffCo);
        
        recupererNPlusProche(c, map_id_descripteur, vecteurs, metrique, n);
        
        Set<Integer> oeuvresSansCible = OeuvresUtils.getIdOeuvreSansCible(oeuvres, descripteurCible);
//        Set<Integer> oeuvresSansCible = OeuvresUtils.getIdOeuvres(oeuvres);
        Map<Integer, Set<Integer>> map_idO_idsDesc = OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, oeuvresSansCible, map_descripteur_id);
        Map<Integer, String> map_idO_titre = OeuvresUtils.getMap_idOeuvre_titre(oeuvres);        
        getOeuvrePlusProcheDeCible(vecteurs, c, map_idO_titre, map_idO_idsDesc, metrique, n);
    }
    
    // recuperer les oeuvre les plus simipaires a x+y qui ne contiennent pas x
    public static void trouverOeuvreAvecMetaphoreDeCible3 (File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, String coDescripteur, MetriqueSim metrique, int n) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteurs = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, OeuvresUtils.getIdOeuvres(oeuvres), coefficient, 0.0);
        vecteurs = VectoriseurUtils.normerVecteurCreuxB(vecteurs.values());
        VecteurCreux vecteurCible = vecteurs.get(map_descripteur_id.get(descripteurCible));
        VecteurCreux coVecteur = vecteurs.get(map_descripteur_id.get(coDescripteur));
        double[] c = VecteurCreux.additionnerVecteurs(vecteurCible.getTabPonderation(), coVecteur.getTabPonderation());
        c = VecteurCreux.normer(c);
        
        Set<Integer> oeuvresSansCible = OeuvresUtils.getIdOeuvreSansCible(oeuvres, descripteurCible);
        Map<Integer, Set<Integer>> map_idO_idsDesc = OeuvresUtils.getMap_idOeuvre_ensembleIdDescripteur(oeuvres, oeuvresSansCible, map_descripteur_id);
        Map<Integer, String> map_idO_titre = OeuvresUtils.getMap_idOeuvre_titre(oeuvres);        
        getOeuvrePlusProcheDeCible(vecteurs, c, map_idO_titre, map_idO_idsDesc, metrique, n);
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
//            c = VecteurCreux.normer(c);
            double[] dif1 = vecteurs.get(idVec).soustraireComplementOrthogonaux(c);
            dif1 = VecteurCreux.normer(dif1);
//            double a = metrique.calculerMetrique(vecteurs.get(idVec).getTabPonderation(), dif1);
            double diff = metrique.calculerMetrique(dif1, vecteurs.get(idCoVec).getTabPonderation());
            
//            double[] dif2 = vecteurs.get(idCoVec).complementOrthogonaux(c);
//            dif2 = VecteurCreux.normer(dif2);
//            double diff = metrique.calculerMetrique(dif1, dif2);
            
            double sim = metrique.calculerMetrique(vecteurs.get(idVec).getTabPonderation(), vecteurs.get(idCoVec).getTabPonderation());
            
            diff = diff/-1;
            double out = 2*((sim*diff)/(sim+diff));
            
            System.out.println(map_id_descripteur.get(idVec)+ " et "+ map_id_descripteur.get(idCoVec) +" : "+out +" : "+"sim : " + sim+" : "+"diff : " + diff);
        }
         
    }
    
    // c = x*y
    // diff = (y-c)/-1 donne un score de difference
    // sim = cos(x,y)
    // out = 2*((sim*diff)/(sim+diff))  moyenne harminique entre diff et sim
    public static void getMetaphore1a(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, MetriqueSim metrique, int nPlusProcheVoisin) throws IOException
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
//            c = VecteurCreux.normer(c);
            double[] dif1 = vecteurs.get(idCoVec).soustraireComplementOrthogonaux(c);
            dif1 = VecteurCreux.normer(dif1);
//            double a = metrique.calculerMetrique(vecteurs.get(idVec).getTabPonderation(), dif1);
            double diff = metrique.calculerMetrique(dif1, vecteurs.get(idVec).getTabPonderation());
            
//            double[] dif2 = vecteurs.get(idCoVec).complementOrthogonaux(c);
//            dif2 = VecteurCreux.normer(dif2);
//            double diff = metrique.calculerMetrique(dif1, dif2);
            
            double sim = metrique.calculerMetrique(vecteurs.get(idVec).getTabPonderation(), vecteurs.get(idCoVec).getTabPonderation());
            
            diff = diff/-1;
            double out = 2*((sim*diff)/(sim+diff));
            
            System.out.println(map_id_descripteur.get(idVec)+ " et "+ map_id_descripteur.get(idCoVec) +" : "+out +" : "+"sim : " + sim+" : "+"diff : " + diff);
        }
         
    }
    
// deux mots ont un lien metaphorique lorsqu'ils sont tres different sauf pour quelques usages
    public static void getMetaphore1b(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, MetriqueSim metrique, int nPlusProcheVoisin) throws IOException
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
            
//            double[] dif1 = vecteurs.get(idVec).complementOrthogonaux(c);
//            dif1 = VecteurCreux.normer(dif1);
//            double a = metrique.calculerMetrique(vecteurs.get(idVec).getTabPonderation(), dif1);
//            double diff = metrique.calculerMetrique(dif1, vecteurs.get(idCoVec).getTabPonderation());
            
            double[] dif2 = vecteurs.get(idCoVec).soustraireComplementOrthogonaux(c);
            dif2 = VecteurCreux.normer(dif2);
            double diff = metrique.calculerMetrique(dif2, vecteurs.get(idVec).getTabPonderation());
//            double diff = metrique.calculerMetrique(dif1, dif2);
            
            double sim = metrique.calculerMetrique(vecteurs.get(idVec).getTabPonderation(), vecteurs.get(idCoVec).getTabPonderation());

            
            diff = diff*-1;
            double out = 2*((sim*diff)/(sim+diff));
            
            System.out.println(map_id_descripteur.get(idVec)+ " et "+ map_id_descripteur.get(idCoVec) +" : "+out +" : "+"sim : " + sim+" : "+"diff : " + diff);

        }
         
    }
    
    // methode ininteressante
    public static void getMetaphore1c(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, MetriqueSim metrique, int nPlusProcheVoisin) throws IOException
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
            double simBase = metrique.calculerMetrique(vecteurs.get(idVec).getTabPonderation(),vecteurs.get(idCoVec).getTabPonderation());
            double difBase = 1-simBase;
            double [] diff2 = VecteurCreux.soustraireComplementOrthogonaux(vecteurs.get(idCoVec).getTabPonderation(), c);
            diff2 = VecteurCreux.normer(diff2);
            double diff = metrique.calculerMetrique(vecteurs.get(idVec).getTabPonderation(), diff2)*-1;
            diff = diff/difBase;
            
            double[] sim2 = VecteurCreux.soustraireComplementOrthogonaux(vecteurs.get(idCoVec).getTabPonderation(), diff2);
            sim2 = VecteurCreux.normer(sim2);
            double sim = metrique.calculerMetrique(vecteurs.get(idVec).getTabPonderation(), sim2);
            sim = simBase/sim;
            
            double out = 2*((sim*diff)/(sim+diff));
                        
            System.out.println(map_id_descripteur.get(idVec)+ " et "+ map_id_descripteur.get(idCoVec) +" : "+out +" : "+"sim : " + sim+" : "+"diff : " + diff);
//            System.out.println("sim : " + sim);
//            System.out.println("diff : " + diff);
//            System.out.println("score : " + score);
        }
         
    }
    // c = x*y
    // diff = (y-c)*-1 donne un score de difference
    // dist = cos(x,diff)*-1
    // out = 2*((sim*diff)/(sim+diff))  moyenne harminique entre diff et sim
    public static void getMetaphore1d(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, MetriqueSim metrique, int nPlusProcheVoisin) throws IOException
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
            double simBase = metrique.calculerMetrique(vecteurs.get(idVec).getTabPonderation(),vecteurs.get(idCoVec).getTabPonderation());
//            double difBase = 1-simBase;
            double [] diff2 = VecteurCreux.soustraireComplementOrthogonaux(vecteurs.get(idCoVec).getTabPonderation(), c);
            diff2 = VecteurCreux.normer(diff2);
            double diff = metrique.calculerMetrique(vecteurs.get(idVec).getTabPonderation(), diff2)*-1;
//            diff = diff/difBase;
            
            double[] sim2 = VecteurCreux.soustraireComplementOrthogonaux(vecteurs.get(idCoVec).getTabPonderation(), diff2);
            sim2 = VecteurCreux.normer(sim2);
            double sim = metrique.calculerMetrique(vecteurs.get(idVec).getTabPonderation(), sim2);
//            sim = simBase/sim;
            
            double out = (2*((sim*diff)/(sim+diff)));
                        
            System.out.println(map_id_descripteur.get(idVec)+ " et "+ map_id_descripteur.get(idCoVec) +" : "+out +" : "+"sim : " + sim+" : "+"diff : " + diff);
//            System.out.println("sim : " + sim);
//            System.out.println("diff : " + diff);
//            System.out.println("score : " + score);
        }
         
    }
    
    // c = x*y
    // diff = (x-c)/-1 donne un score de difference
    // sim = cos(x,y)
    // out = 2*((sim*diff)/(sim+diff))  moyenne harminique entre diff et sim
    public static void getMetaphore2(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, MetriqueSim metrique, int nPlusProcheVoisin) throws IOException
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
            
            double[] dif2 = vecteurs.get(idCoVec).soustraireComplementOrthogonaux(c);
            dif2 = VecteurCreux.normer(dif2);
            double diff = metrique.calculerMetrique(dif1, dif2);
            diff = diff/-1;
            
            double[] sim1 = vecteurs.get(idVec).soustraireComplementOrthogonaux(dif1);
            sim1 = VecteurCreux.normer(sim1);
            double[] sim2 = vecteurs.get(idCoVec).soustraireComplementOrthogonaux(dif2);
            sim2 = VecteurCreux.normer(sim2);
            // cette sim donne toujours 1.0. 
            double sim = metrique.calculerMetrique(sim1, sim2); 
            
            double out = 2*((sim*diff)/(sim+diff));
            
            System.out.println(map_id_descripteur.get(idVec)+ " et "+ map_id_descripteur.get(idCoVec) +" : "+out +" : "+"sim : " + sim+" : "+"diff : " + diff);
//            System.out.println("sim : " + sim);
//            System.out.println("diff : " + diff);
//            System.out.println("score : " + score);
        }
         
    }
    
    // c = x*y
    // diff = (x-c)/-1 donne un score de difference
    // sim = cos(x,y)
    // out = 2*((sim*diff)/(sim+diff))  moyenne harminique entre diff et sim
    public static void getMetaphore3(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, MetriqueSim metrique, int nPlusProcheVoisin) throws IOException
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
//            double diff = metrique.calculerMetrique(dif1, vecteurs.get(idCoVec).getTabPonderation());
            
            double[] dif2 = vecteurs.get(idCoVec).soustraireComplementOrthogonaux(c);
            dif2 = VecteurCreux.normer(dif2);
            double diff = metrique.calculerMetrique(dif1, dif2);
            
            double sim = metrique.calculerMetrique(vecteurs.get(idVec).getTabPonderation(), vecteurs.get(idCoVec).getTabPonderation());
            
//            diff = diff/-1;
//            double out = 2*((sim*diff)/(sim+diff));
            double out = sim/diff;
            
            System.out.println(map_id_descripteur.get(idVec)+ " et "+ map_id_descripteur.get(idCoVec) +" : "+out +" : "+"sim : " + sim+" : "+"diff : " + diff);
//            System.out.println("sim : " + sim);
//            System.out.println("diff : " + diff);
//            System.out.println("score : " + score);
        }
         
    }
    
    public static void getMetaphore4(File fileOeuvre, CoefficientAssociation coefficient, String descripteurCible, MetriqueSim metrique, int nPlusProcheVoisin) throws IOException
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
            
            double[] dif1 = vecteurs.get(idVec).soustraireComplementOrthogonaux(vecteurs.get(idCoVec).getTabPonderation());
            dif1 = VecteurCreux.normer(dif1);
//            double a = metrique.calculerMetrique(vecteurs.get(idVec).getTabPonderation(), dif1);
            
            double[] dif2 = vecteurs.get(idCoVec).soustraireComplementOrthogonaux(vecteurs.get(idVec).getTabPonderation());
            dif2 = VecteurCreux.normer(dif2);
            double diff = metrique.calculerMetrique(dif1, dif2);
            diff = diff/-1;
            
            double[] sim1 = vecteurs.get(idVec).soustraireComplementOrthogonaux(dif1);
            double[] sim2 = vecteurs.get(idCoVec).soustraireComplementOrthogonaux(dif2);
            double sim = metrique.calculerMetrique(sim1, sim2);
            
            double out = 2*((sim*diff)/(sim+diff));
            
            System.out.println(map_id_descripteur.get(idVec)+ " et "+ map_id_descripteur.get(idCoVec) +" : "+out +" : "+"sim : " + sim+" : "+"diff : " + diff);
//            System.out.println("sim : " + sim);
//            System.out.println("diff : " + diff);
//            System.out.println("score : " + score);
        }
         
    }
    
    // c = x*y
    // diff = (y-c)/-1 donne un score de difference
    // sim = cos(x,y)
    // out = 2*((sim*diff)/(sim+diff))  moyenne harminique entre diff et sim
    public void getMetaphoreMax(File fileOeuvre, CoefficientAssociation coefficient, MetriqueSim metrique, int k) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteurs = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, 0.0);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteurs = VectoriseurUtils.normerVecteurCreuxB(vecteurs.values());
        
        PriorityQueue<Resultat> pq = new PriorityQueue<>((rs1,rs2)->Double.compare(rs1.getScore(), rs2.getScore()));
                
        for (int idVec: map_id_descripteur.keySet())
        {
            for (int idCoVec: map_id_descripteur.keySet())
            {
                if (idVec==idCoVec) continue;
                double[] c = VecteurCreux.multiplicationPointWise(vecteurs.get(idVec).getTabPonderation(), vecteurs.get(idCoVec).getTabPonderation());

                double[] dif1 = vecteurs.get(idCoVec).soustraireComplementOrthogonaux(c);
                dif1 = VecteurCreux.normer(dif1);
                double diff = metrique.calculerMetrique(dif1, vecteurs.get(idVec).getTabPonderation());
                diff = diff/-1;
                double sim = metrique.calculerMetrique(vecteurs.get(idVec).getTabPonderation(), vecteurs.get(idCoVec).getTabPonderation());
                double out = 2*((sim*diff)/(sim+diff));
                Resultat rs = new Resultat(out, sim, diff, idVec, idCoVec);
                if (pq.size() < k)
                {
                    pq.add(rs);
                }
                else if (pq.peek().getScore()< out)
                {
                    pq.poll();
                    pq.add(rs);
                }
                
//                System.out.println(map_id_descripteur.get(idVec)+ " et "+ map_id_descripteur.get(idCoVec) +" : "+out +" : "+"sim : " + sim+" : "+"diff : " + diff);
            }
        }
        for (Resultat rs: pq)
        {
            System.out.println(map_id_descripteur.get(rs.getIdCible()) +" : " +map_id_descripteur.get(rs.getIdCo())+" : " +rs.getScore() +" : " + rs.getSim()+" : " + rs.getDiff());
        }
         
    }
    
    
    
    
    
    
}
