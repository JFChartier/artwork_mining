/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte;

import Corpus.DocumentJFC;
import Fichier.FileUtils;
import Parser.ParserRegex.ParserRegex;
import Segmentation.Domif;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author JF Chartier
 */
public class ParserMagritte 
{
        
    public static void main(String[] args)
    {
                
        try
        {
            File file = new File("C:\\Users\\JF Chartier\\Documents\\Collections standard\\Magritte\\html\\Magritte fichier html prepare.txt");
            new ParserMagritte().recupererOeuvre(file);
            
            
        } 
        
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    public static Collection<Oeuvre> recupererOeuvre (File file) throws IOException
    {
        System.out.println("RECUPERER LES ANNOTATIONS, DATE ET ID DE LA BD DE MAGRITTE");
        
        String corpus = FileUtils.read(file, StandardCharsets.UTF_8.displayName());
       
        String[] listeOeuvre = corpus.split("<tr id=\"trRef");
        String date = "";
        ArrayList<String> annotation;
        String titre;
        Collection<Oeuvre> oeuvres = new ArrayList<>();
        for (int i = 1; i<listeOeuvre.length; i++)
        {
            String o = listeOeuvre[i];
            date = recupererDate(o);
            annotation = recupererAnnotation(o);
            titre = recupererTitreOeuvre(o);
//            System.out.println(titre);
            if (annotation!=null)
                oeuvres.add(new Oeuvre(i, annotation, Integer.parseInt(date), titre));
                
        }
//        System.out.println("nbre de domif" + map.size());
        return oeuvres;
    }
    
////////    public Map<Integer, Domif> recupererOeuvre (File file) throws IOException
////////    {
////////        System.out.println("RECUPERER LES ANNOTATIONS, DATE ET ID DE LA BD DE MAGRITTE");
////////        
////////        String corpus = FileUtils.read(file, "utf8");
////////        String[] listeOeuvre = corpus.split("<tr id=\"trRef");
////////        String id = "";
////////        String date = "";
////////        ArrayList<String> annotation;
////////        Map<Integer, Domif> map = new TreeMap<>();
////////        for (int i = 1; i<listeOeuvre.length; i++)
////////        {
////////            String o = listeOeuvre[i];
////////            id = recupererIdOeuvre(o);
////////            date = recupererDate(o);
////////            annotation = recupererAnnotation(o);
////////            if (annotation!=null)
////////                map.put(Integer.parseInt(id), new Domif(new TreeSet<String>(), Integer.parseInt(date), Integer.parseInt(id), annotation, -1));
////////        }
//////////        System.out.println("nbre de domif" + map.size());
////////        return map;
////////    }
    
    protected static String recupererTitreOeuvre(String texte)
    {
        String a = "<td class=\"colNorm\">";
        String b = "</td>";
        
        List<String> liste = ParserRegex.extraireEntreBalise2(texte, a, b);
//        System.out.println(liste.get(2));
        String titre = liste.get(2); // titre officiel
        if (titre.length()==0)
            titre=liste.get(3); // titre 2
        if (titre.length()==0)
        {
//            System.out.println(liste.get(4));
            titre=liste.get(4); // titre suppletif
        } 
        return titre;
    }
    
    protected static ArrayList<String> recupererAnnotation(String texte)
    {
        String a="class=\"el\">";
        String b="</td>";
        Pattern pattern = Pattern.compile(a+"(.*?)"+b, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(texte);
//        StringBuilder annotation = new StringBuilder();
        ArrayList<String> annotation = new ArrayList<String>();
        while (matcher.find())
        {
//            annotation.append("<").append(matcher.group(1)).append(">");
            String descripteur = matcher.group(1).trim();
            descripteur = "<"+descripteur+">";
            if (descripteur.equals("<Aucune analyse>"))
                return null;
            else 
                annotation.add(descripteur);
        }
        return annotation;
    }
    
    protected static String recupererDate(String texte)
    {
        String baliseA = "<td style=\"width:5%\" class=\"colNorm\">";
        String baliseB = "</td>";
        
        Pattern pattern = Pattern.compile(baliseA+"(.*?)"+baliseB, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(texte);
        String date = "";
        while (matcher.find())
        {
            date = extractLast4digits(matcher.group(1));
        }
        return date;
    }
    
    private static String extractLast4digits(String date)
    {
        List<String> fourDigits = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\d{4})");
        Matcher matcher = pattern.matcher(date);
        
        while (matcher.find())
        {
            fourDigits.add(matcher.group(1));
        }
        if (fourDigits.isEmpty())
            return "-1";
        return fourDigits.get(fourDigits.size()-1);
    }
    
    protected String recupererIdOeuvre (String texte)
    {
        int i = texte.indexOf("\"" );
        return texte.substring(0, i);
    }
    
    
    
}
