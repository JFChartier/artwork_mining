/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte;

import java.util.List;

/**
 *
 * @author JF Chartier
 */
public class Oeuvre 
{
    private int id;
    private List<String> listeDescripteur;
    private int date;
    private String titre;

    public Oeuvre(int id, List<String> listeDescripteur, int date) {
        this.id = id;
        this.listeDescripteur = listeDescripteur;
        this.date = date;
    }

    public Oeuvre(int id, List<String> listeDescripteur, int date, String titre) {
        this.id = id;
        this.listeDescripteur = listeDescripteur;
        this.date = date;
        this.titre = titre;
    }
    
    

    public int getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public List<String> getListeDescripteur() {
        return listeDescripteur;
    }

    public String getTitre() {
        return titre;
    }
    
    
    
}
