/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte;

import FiltreurToken.FiltreurAntidictionnaire;
import FiltreurToken.FiltreurFrequenceDocumentaire;
import FiltreurToken.FiltreurNombre;
import FiltreurToken.FiltreurSingleton;
import Matrice.MtjMatrice;
import Matrice.Vectorisateur.VecteurCreux;
import N_Grams.N_Grams;
import N_Grams.token.NGramsToken;
import Racinisation.RacineurFrancais;
import ReductionDimensionnelle.SVD.ReducteurSVD_MTJ;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.NotConvergedException;
import no.uib.cipr.matrix.SVD;

/**
 *
 * @author JF Chartier
 */
public class Pretraitement 
{
    private boolean antidictionnaire; 
    private boolean nombre;
    private boolean singleton;
    private boolean racinisation;
    private int nGram;
    private int freqOeuvreMin;
    private int freqOeuvreMax;
    private boolean descripteurMot;
    private boolean descripteurIncertain;

    public Pretraitement(boolean antidictionnaire, boolean nombre, boolean singleton, boolean racinisation, int nGram, int freqOeuvreMin, int freqOeuvreMax, boolean descripteurMot, boolean descripteurIncertain) {
        this.antidictionnaire = antidictionnaire;
        this.nombre = nombre;
        this.singleton = singleton;
        this.racinisation = racinisation;
        this.nGram = nGram;
        this.freqOeuvreMin = freqOeuvreMin;
        this.freqOeuvreMax = freqOeuvreMax;
        this.descripteurMot = descripteurMot;
        this.descripteurIncertain = descripteurIncertain;
    }
    
    public static Collection<Oeuvre> filtrerOeuvreSelonCible (Collection<Oeuvre> oeuvres, String descripteur)
    {
        Collection<Oeuvre> oeuvresFiltres = new ArrayList<>(oeuvres.size());
        for (Oeuvre o: oeuvres)
        {
            if (o.getListeDescripteur().contains(descripteur))
                oeuvresFiltres.add(new Oeuvre(o.getId(), o.getListeDescripteur(), o.getDate(), o.getTitre()));
        }
        return oeuvresFiltres;
    }
    
        
    public Collection<Oeuvre> filtrerOeuvres (Collection<Oeuvre> oeuvres)
    {
        Collection<Oeuvre> oeuvresFiltres = new ArrayList<>(oeuvres.size());
        FiltreurAntidictionnaire filtreurAntidictionnaire = new FiltreurAntidictionnaire("francais", false);
        FiltreurNombre filtreurNombre = new FiltreurNombre();
        FiltreurSingleton filtreurSingleton = new FiltreurSingleton();
        N_Grams nGrams = new N_Grams(nGram);
        Map<String, Integer> map_descrip_freq = OeuvresUtils.getMap_descripteur_freqOeuvre(oeuvres);
        FiltreurFrequenceDocumentaire filtreurFreq = new FiltreurFrequenceDocumentaire(map_descrip_freq, freqOeuvreMin, freqOeuvreMax);
        for (Oeuvre oeuvre: oeuvres)
        {
            List<String> tokensFiltre = filtrerToken(oeuvre.getListeDescripteur(), filtreurAntidictionnaire, filtreurNombre, filtreurSingleton, nGrams, filtreurFreq);
//            List<String> tokensFiltre = filtrerToken(oeuvre.getListeDescripteur());
            oeuvresFiltres.add(new Oeuvre(oeuvre.getId(), tokensFiltre, oeuvre.getDate(), oeuvre.getTitre()));
        }
        int nbreToken = OeuvresUtils.getTokens(oeuvresFiltres).size();
        System.out.println(nbreToken);
        return oeuvresFiltres;
    }
    
//    public static Set<String> recupererEnsembleTagsControleEtNonControlePlusFrequent (final Collection<DocumentDeft2016> documents, int minFrequenceDoc)
//    {
//        Map<Integer, List<String>> map_idDoc_listeTagControle = recupererMap_idDoc_listeTagControleEtNonControle(documents);
//        Map<String, Set<Integer>> map_tagControle_ensembleIdDoc = TriageCollection.recupererMap_valeur_ensembleCle(map_idDoc_listeTagControle);
//        Set<String> tagsControle = new TreeSet<String>();
//        for (String tag: map_tagControle_ensembleIdDoc.keySet())
//        {
//            if (map_tagControle_ensembleIdDoc.get(tag).size()>minFrequenceDoc)
//                tagsControle.add(tag);
//        }
//        return tagsControle;
//    }
    
    private List<String> filtrerToken (final List<String> listeToken, FiltreurAntidictionnaire filtreurAntidictionnaire, FiltreurNombre filtreurNombre, 
            FiltreurSingleton filtreurSingleton, N_Grams nGrams, FiltreurFrequenceDocumentaire filtreurFreq)
    {
        List<String> listeTokenFiltre = new ArrayList<>(listeToken);
        if (antidictionnaire==true)
            listeTokenFiltre = filtreurAntidictionnaire.filtrerListe(listeTokenFiltre);
        if (nombre==true)
            listeTokenFiltre= filtreurNombre.filtrerListe(listeTokenFiltre);
        if (singleton==true)
            listeTokenFiltre = filtreurSingleton.filtrerListe(listeTokenFiltre);
        if (racinisation==true)
            listeTokenFiltre = RacineurFrancais.raciner(listeTokenFiltre);
        if (nGram>0)
            listeTokenFiltre = nGrams.decomposerSegment(String.join("_", listeTokenFiltre));
        if (descripteurMot==true)
            listeTokenFiltre = filtrerDescripteurMot(listeTokenFiltre);
        if (descripteurIncertain==true)
            listeTokenFiltre = filtrerDescripteurIncertain(listeTokenFiltre);
        listeTokenFiltre = filtreurFreq.filtrerListe(listeTokenFiltre);

      return listeTokenFiltre;        
    }
    
    
    public static Map<Integer, VecteurCreux> reduireViaSvd (Map<Integer, VecteurCreux> map_id_vecteur, int nbreDimensions, int nbreVecteurs, int kFacteur) throws NotConvergedException
    {
        Map<Integer,Integer> map_idOeuvre_idLigne = new HashMap<>(map_id_vecteur.size());
        int idLigne = 0;
        for (int idOeuvre: map_id_vecteur.keySet())
        {
            map_idOeuvre_idLigne.put(idOeuvre, idLigne);
            idLigne++;
        }
        DenseMatrix matrice = MtjMatrice.convertirVecteurVersLigneMatrice(map_id_vecteur.values(), map_idOeuvre_idLigne, nbreVecteurs, nbreDimensions);
        matrice = new DenseMatrix(matrice.transpose(new DenseMatrix(nbreDimensions, nbreVecteurs)));
        if (matrice.numRows()!=nbreDimensions && matrice.numColumns() != nbreVecteurs)
        {
            System.out.println("mauvais nombre de ligne");
            System.exit(1);
        }

        matrice = ReducteurSVD_MTJ.reduireEtPondererMatriceV(new SVD(matrice.numRows(), matrice.numColumns()).factor(matrice), kFacteur);
        return MtjMatrice.convertirLigneDeLaMatrice(matrice, map_idOeuvre_idLigne);
    }
    
    public static Collection<Oeuvre> filtrerDescripteurMot (Collection<Oeuvre> oeuvres)
    {
        System.out.println("filtrerDescripteurMot");
        Collection<Oeuvre> oeuvresFiltres = new ArrayList<>(oeuvres.size());
        for (Oeuvre o: oeuvres)
        {
            List<String> descs = filtrerDescripteurMot(o.getListeDescripteur());
////////            List<String> descs = new ArrayList<>();
////////            for (String d: o.getListeDescripteur())
////////            {
////////                boolean filtre = false;
////////                for (char c: d.toCharArray())
////////                {
//////////                    System.out.println(c);
////////                    if (c=='\"')
////////                        filtre=true;
////////                }
////////                if (filtre==false)
////////                    descs.add(d);
////////            }
            oeuvresFiltres.add(new Oeuvre(o.getId(), descs, o.getDate(), o.getTitre()));
        }
        return oeuvresFiltres;
    }
    
    private static List<String> filtrerDescripteurMot (List<String> descripteurs)
    {
        List<String> descs = new ArrayList<>();
        for (String d: descripteurs)
        {
            boolean filtre = false;
            for (char c: d.toCharArray())
            {
//                    System.out.println(c);
                if (c=='\"')
                {
                    filtre=true;
                    break;
                }
            }
            if (filtre==false)
                descs.add(d);
        }
        return descs;
    }
    
    private static List<String> filtrerDescripteurIncertain (List<String> descripteurs)
    {
        List<String> descs = new ArrayList<>();
        for (String d: descripteurs)
        {
            boolean filtre = false;
            for (char c: d.toCharArray())
            {
//                    System.out.println(c);
                if (c=='?')
                {
                    filtre=true;
                    break;
                }
            }
            if (filtre==false)
                descs.add(d);
        }
        return descs;
    }
    
//    public static Collection<Oeuvre> filtrerDescripteurMot (Collection<Oeuvre> oeuvres)
//    {
//        System.out.println("filtrerDescripteurMot");
//        Collection<Oeuvre> oeuvresFiltres = new ArrayList<>(oeuvres.size());
//        for (Oeuvre o: oeuvres)
//        {
//            List<String> descs = new ArrayList<>();
//            for (String d: o.getListeDescripteur())
//            {
//                if (!d.contains("<\""));
//                descs.add(d);
//            }
//            oeuvresFiltres.add(new Oeuvre(o.getId(), descs, o.getDate()));
//        }
//        return oeuvresFiltres;
//    }
    
}
