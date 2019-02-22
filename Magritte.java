/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magritte;

import Metrique.Similitude.CorrelationPearson;
import Metrique.Similitude.Cosinus;
import Ponderation.CoefficientChi2;
import Ponderation.CoefficientProbConditionnelle2;
import Ponderation.CorrelationMatthews;
import Ponderation.InformationMutuelle;
import java.io.IOException;
import magritte.Analyses.AnalyseK;
import magritte.Analyses.AnalyseThematique;
import no.uib.cipr.matrix.NotConvergedException;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.FuzzyKMeansClusterer;

/**
 *
 * @author JF Chartier
 */
public class Magritte {

    /**
     * @param args the command line arguments
     */
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
        AnalyseThematique.analyse10(OeuvresUtils.getFileCorpus(), new Cosinus(), new CoefficientChi2(), 4, 10);
    }
    
    
    
}
