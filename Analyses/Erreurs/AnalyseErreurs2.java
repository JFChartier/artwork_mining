/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte.Analyses.Erreurs;

import Metrique.Similitude.ProduitScalaire;
import Ponderation.InformationMutuelle;
import UtilsJFC.TrieUtils;
import java.io.IOException;
import java.util.Map;
import magritte.OeuvresUtils;

/**
 *
 * @author JF Chartier
 */
public class AnalyseErreurs2 extends AnalyseErreurs
{
    public static void main(String[] args) throws IOException 
    {
        new AnalyseErreurs2().analyse(OeuvresUtils.getFileCorpus(), new ProduitScalaire(), new InformationMutuelle());
    }
    
    @Override
    public double getSeuilMin(Map<Integer, Double> map_idAvecCible_simAvecCible)
    {
        double moyenne = map_idAvecCible_simAvecCible.values().stream().mapToDouble(a -> a).average().getAsDouble();
        return moyenne;
    }
    
    @Override
    public double getSeuilMax(Map<Integer, Double> map_idOSansCible_simAvecCible)
    {
        return map_idOSansCible_simAvecCible.values().stream().mapToDouble(a -> a).average().getAsDouble();
    }
}
