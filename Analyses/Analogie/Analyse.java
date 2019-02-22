/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte.Analyses.Analogie;

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
import magritte.Analyses.AnalyseOperateurSimple;
import magritte.Oeuvre;
import magritte.OeuvresUtils;
import magritte.ParserMagritte;
import magritte.Pretraitement;

/**
 *
 * @author JF Chartier
 */
public class Analyse 
{
    private static Map<String, Integer> map_descripteur_id;
    private static Map<Integer, String> map_id_descripteur;
    
    public static void main(String[] args) throws IOException 
    {
//        Analyse.getAnalogie1(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<pomme>","<pomme>","<visage>", new ProduitScalaire());
//        Analyse.getAnalogie2(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<pomme>", "<visage>", new ProduitScalaire());
//        Analyse.getAnalogie3(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<pomme>","<homme>", new ProduitScalaire());
//        Analyse.getAnalogie3(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<garçon>","<homme>","<fille>", new ProduitScalaire());
//        Analyse.getAnalogie3b(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<garçon>","<pomme>","<homme>", new ProduitScalaire());
    
//        Analyse.getAnalogie4(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<fille>", "<garçon>","<homme>", new ProduitScalaire());
    Analyse.getAnalogie5(OeuvresUtils.getFileCorpus(), new InformationMutuelle(), "<homme>","<garçon>","<femme>", new ProduitScalaire());
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
        
    // sim((A-B), (C-D))
    // trouver le descripteur B qui maximise sim
    public static void getAnalogie1 (File fileOeuvre, CoefficientAssociation coefficient, String desc1, String descCible2, String coDesc2, MetriqueSim metrique) throws IOException
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
    
    
    // (a:A) -> (b:B)
    // B=A-a+b
    public static void getAnalogie3 (File fileOeuvre, CoefficientAssociation coefficient, String cible_a, String cible_A, String coCible_b, MetriqueSim metrique) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteursDescripteur = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, 0.0);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteursDescripteur = VectoriseurUtils.normerVecteurCreuxB(vecteursDescripteur.values());
        
        int idCible_a = map_descripteur_id.get(cible_a);
        int idCible_A = map_descripteur_id.get(cible_A);
        int idCible_b = map_descripteur_id.get(coCible_b);
             
        double[] x = vecteursDescripteur.get(idCible_A).soustraireComplementOrthogonaux(vecteursDescripteur.get(idCible_a).getTabPonderation());
        x = VecteurCreux.normer(x);
        x = VecteurCreux.additionnerVecteurs(x, vecteursDescripteur.get(idCible_b).getTabPonderation());
        x = VecteurCreux.normer(x);
        
//        double[] x = vecteursDescripteur.get(idCible1).complementOrthogonaux(vecteursDescripteur.get(idCible2).getTabPonderation());
//        x = VecteurCreux.normer(x);
//        x = VecteurCreux.additionnerVecteurs(x, vecteursDescripteur.get(idCoDesc2).getTabPonderation());
//        x = VecteurCreux.normer(x);
//        x = VecteurCreux.complementOrthogonaux(x, vecteursDescripteur.get(idCible1).getTabPonderation());
//        x = VecteurCreux.normer(x);
        
        double simMax = 0.0;
        int idCoMax = -1;
        for (int idCo1: map_id_descripteur.keySet())
        {
            if (idCo1==idCible_A || idCo1==idCible_b||idCo1==idCible_b) continue;
            double sim = metrique.calculerMetrique(vecteursDescripteur.get(idCo1).getTabPonderation(), x);
            if (sim>simMax)
            {
                idCoMax=idCo1;
                simMax=sim;
            }
                System.out.println(map_id_descripteur.get(idCible_a) +"-"+map_id_descripteur.get(idCible_A)+ " et "+ coCible_b+"-"+map_id_descripteur.get(idCo1)+" : "+sim);

            
            
        }
//        System.out.println(map_id_descripteur.get(idCible_b) +"-"+map_id_descripteur.get(idCoMax)+ " et "+ cible_A+"-"+coCible_b+" : "+simMax);

    }
    
    // (A:a)->(B:b?)
    // (a1:a2)->(b1:b2?)
    // trouver le descripteurs b qui maximisent cos((B*A)+(a-A),(A*B)+(b?-B))
    public static void getAnalogie5 (File fileOeuvre, CoefficientAssociation coefficient, String A, String a, String B, MetriqueSim metrique) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteursDescripteur = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, 0.0);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteursDescripteur = VectoriseurUtils.normerVecteurCreuxB(vecteursDescripteur.values());
        
        int idA = map_descripteur_id.get(A);
        int ida = map_descripteur_id.get(a);
        int idB = map_descripteur_id.get(B);
             
//        double[] x = vecteursDescripteur.get(idCible2).complementOrthogonaux(vecteursDescripteur.get(idCoDesc2).getTabPonderation());
//        x = VecteurCreux.normer(x);
        double[] c = VecteurCreux.multiplicationPointWise(vecteursDescripteur.get(idA).getTabPonderation(), vecteursDescripteur.get(idB).getTabPonderation());
        c = VecteurCreux.normer(c);
        
        double[] aMinusA = vecteursDescripteur.get(ida).soustraireComplementOrthogonaux(vecteursDescripteur.get(idA).getTabPonderation());
        aMinusA = VecteurCreux.normer(aMinusA);
        aMinusA = VecteurCreux.additionnerVecteurs(aMinusA, c);
        aMinusA = VecteurCreux.normer(aMinusA);
        
        double simMax = 0.0;
        int idCoMax = -1;
        for (int idCo1: map_id_descripteur.keySet())
        {
            if (idCo1==ida || idCo1==idB||idCo1==idA) continue;
            double[] bMinusB = vecteursDescripteur.get(idCo1).soustraireComplementOrthogonaux(vecteursDescripteur.get(idB).getTabPonderation());
            bMinusB = VecteurCreux.normer(bMinusB);
        
            double sim = metrique.calculerMetrique(bMinusB,aMinusA);
            if (sim>simMax)
            {
                idCoMax=idCo1;
                simMax=sim;
            }
            System.out.println(A +":"+a+ " et "+ B+":"+map_id_descripteur.get(idCo1)+" = "+sim);

        }
//        System.out.println(map_id_descripteur.get(idCible1) +"-"+map_id_descripteur.get(idCoMax)+ " et "+ cible2+"-"+coCible2+" : "+simMax);

    }
    
    // sim((B-b), (A-a))
    // trouver les descripteurs B et b qui maximisent cos(b, B-A+a)
    public static void getAnalogie3 (File fileOeuvre, CoefficientAssociation coefficient, String cible2, String coCible2, MetriqueSim metrique) throws IOException
    {
        Collection<Oeuvre> oeuvres = pretraitement(fileOeuvre);
        Map<Integer, Set<Integer>> map_idDesc_idOeuvres = OeuvresUtils.getMap_idDescripteur_ensembleIdOeuvre(oeuvres, map_descripteur_id);
        Map<Integer, VecteurCreux> vecteursDescripteur = VectoriseurUtils.vectoriserAvecCoefficientAssociation6(map_idDesc_idOeuvres, map_idDesc_idOeuvres, new TreeSet<>(OeuvresUtils.getIdOeuvres(oeuvres)), coefficient, 0.0);
//        Map<Integer, List<Integer>> map_idOeuvre_listeIdDescripteur =  OeuvresUtils.getMap_idOeuvre_listeIdDescripteur(oeuvres, map_descripteur_id);
//        Map<Integer, VecteurIndicie> vecteursDescripteur = OeuvresUtils.getMap_idDescripteur_vecteurAssociationIdDescripteur(map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur, map_idOeuvre_listeIdDescripteur.keySet(), coefficient);
        vecteursDescripteur = VectoriseurUtils.normerVecteurCreuxB(vecteursDescripteur.values());
        
//        int idCible1 = map_descripteur_id.get(cible1);
        int idCible2 = map_descripteur_id.get(cible2);
        int idCoDesc2 = map_descripteur_id.get(coCible2);
                
//        double[] x = vecteursDescripteur.get(idCible1).complementOrthogonaux(vecteursDescripteur.get(idCible2).getTabPonderation());
//        x = VecteurCreux.normer(x);
//        x = VecteurCreux.additionnerVecteurs(x, vecteursDescripteur.get(idCoDesc2).getTabPonderation());
//        x = VecteurCreux.normer(x);

        
        double simMax = 0.0;
        int idCo1Max = -1;
        int idCible1Max=-1;
        for (int idCible1: map_id_descripteur.keySet())
        {
            if (idCible1==idCible2 || idCible1==idCoDesc2) continue;
        
            double[] x = vecteursDescripteur.get(idCible1).soustraireComplementOrthogonaux(vecteursDescripteur.get(idCible2).getTabPonderation());
            x = VecteurCreux.normer(x);
            x = VecteurCreux.additionnerVecteurs(x, vecteursDescripteur.get(idCoDesc2).getTabPonderation());
            x = VecteurCreux.normer(x);

            for (int idCo1: map_id_descripteur.keySet())
            {
                if (idCo1==idCible2 || idCo1==idCoDesc2||idCo1==idCible1) continue;
                double sim = metrique.calculerMetrique(vecteursDescripteur.get(idCo1).getTabPonderation(), x);
                if (sim>simMax)
                {
                    idCo1Max=idCo1;
                    idCible1Max=idCible1;
                    simMax=sim;
                }
//                    System.out.println(map_id_descripteur.get(idCible1) +"-"+map_id_descripteur.get(idCo1)+ " et "+ cible2+"-"+coCible2+" : "+sim);
            }
        }
        System.out.println(map_id_descripteur.get(idCible1Max) +"-"+map_id_descripteur.get(idCo1Max)+ " et "+ cible2+"-"+coCible2+" : "+simMax);

    }
    
    // sim((B-b), (A-a))
    // trouver lea descripteurs b qui maximisent cos(b,B)-cos(b,A)+cos(b,a)
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
    
    // sim((B-b), (A-a))
    // trouver lea descripteurs b qui maximisent cos(b,B)*cos(b,a)/cos(b,A)+e
    public static void getAnalogie4 (File fileOeuvre, CoefficientAssociation coefficient, String cible1, String cible2, String coCible2, MetriqueSim metrique) throws IOException
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
        double e = 0.000001;
        for (int idCo1: map_id_descripteur.keySet())
        {
            if (idCo1==idCible2 || idCo1==idCoDesc2||idCo1==idCible1) continue;
            vecCo1 = vecteursDescripteur.get(idCo1).getTabPonderation();
            
            double simCo1_cible1 = metrique.calculerMetrique(vecCo1, vecCible1);
            double simCo1_cible2 = metrique.calculerMetrique(vecCo1, vecCible2);
            double simCo1_co2 = metrique.calculerMetrique(vecCo1, vecCo2);
            
            double sim = (simCo1_cible1*simCo1_co2)/(simCo1_cible2+e);
            
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
    
}
