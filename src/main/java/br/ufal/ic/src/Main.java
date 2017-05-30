package br.ufal.ic.src;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ObjectArrays;
import ij.io.Opener;
import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.plugins.commands.typechange.TypeChanger;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.DoubleType;
import org.scijava.module.ModuleInfo;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

/**
 * Created by Wesley on 28/05/2017.
 */
public class Main {

    private static int numberOfNodules = 4123;
    private static int numberOfFeatures = 8;

    /* ---------  Arrays for each feature -------------*/
    private static DoubleType[] allFeatures;

    private static DoubleType[] zernikeMagnitude = new DoubleType[numberOfNodules];
    //private static DoubleType[] zernikePhase = new DoubleType[numberOfNodules];

    private static DoubleType[] tamuraCoarseness = new DoubleType[numberOfNodules];
    private static DoubleType[] tamuraContrast = new DoubleType[numberOfNodules];
    private static DoubleType[] tamuraDirectionality = new DoubleType[numberOfNodules];

    //private static DoubleType[] imagejStatsGeometricMean = new DoubleType[numberOfNodules];
    //private static DoubleType[] imagejStatsHarmonicMean = new DoubleType[numberOfNodules];
    private static DoubleType[] imagejStatsMean = new DoubleType[numberOfNodules];
    private static DoubleType[] imagejStatsKurtosis = new DoubleType[numberOfNodules];
    private static DoubleType[] imagejStatsStdDev = new DoubleType[numberOfNodules];
    private static DoubleType[] imagejStatsVariance = new DoubleType[numberOfNodules];
    /* ---------  Arrays for each feature -------------*/

    public static void main(String[] args) throws IOException {
        ImageJ ij = new ImageJ();
        ModuleInfo module = ij.command().getCommand(TypeChanger.class);

        File[] filesPath = new File("C:\\Users\\wesle\\Documents\\Ref-Med\\nodulo").listFiles();
        System.out.println(filesPath.length);
        File file = filesPath[0];
        for (int i = 0; i < filesPath.length; ++i) {
            file = filesPath[i];
            Dataset dataset = ij.scifio().datasetIO().open(file.getPath());
            Map<String, Object> input = new HashMap<String, Object>();

            input.put("data", dataset);
            input.put("typeName", "8-bit unsigned integer");
            input.put("combineChannels", true);
            ij.module().waitFor(ij.module().run(module, true, input));

            //ArrayList<DoubleType> features = new ArrayList<DoubleType>();

            zernikeMagnitude[i] = ij.op().zernike().magnitude((IterableInterval<DoubleType>) dataset.getImgPlus(), 0, 0);
            //zernikePhase[i] = ij.op().zernike().phase((IterableInterval<DoubleType>) dataset.getImgPlus(),0,0);

            int histogramSize = new Opener().openImage(file.getPath()).getStatistics().histogram.length;
            tamuraCoarseness[i] = ij.op().tamura().coarseness((RandomAccessibleInterval<DoubleType>) dataset.getImgPlus());
            tamuraContrast[i] = ij.op().tamura().contrast((RandomAccessibleInterval<DoubleType>) dataset.getImgPlus());
            tamuraDirectionality[i] = ij.op().tamura().directionality((RandomAccessibleInterval<DoubleType>) dataset.getImgPlus(),histogramSize);

            //imagejStatsGeometricMean[i] = ij.op().stats().geometricMean((Iterable<DoubleType>) dataset.getImgPlus());
            //imagejStatsHarmonicMean[i] = ij.op().stats().harmonicMean((Iterable<DoubleType>) dataset.getImgPlus());

            imagejStatsMean[i] = ij.op().stats().mean((Iterable<DoubleType>) dataset.getImgPlus());
            imagejStatsKurtosis[i] = ij.op().stats().kurtosis((Iterable<DoubleType>) dataset.getImgPlus());
            // features.add(ij.op().stats().median((Iterable<DoubleType>) dataset.getImgPlus())); // precisão sempre 0
            imagejStatsStdDev[i] = ij.op().stats().stdDev((Iterable<DoubleType>) dataset.getImgPlus());
            imagejStatsVariance[i] = ij.op().stats().variance((Iterable<DoubleType>) dataset.getImgPlus());

            System.out.println(i);
        }

        concatenateArraysFeatures();

        makeNoduleDataCSV();
    }

    private static void concatenateArraysFeatures() {

        //If you add more features, remember concatenate them here.
        //allFeatures = ObjectArrays.concat(zernikeMagnitude, zernikePhase, DoubleType.class);
        allFeatures = ObjectArrays.concat(zernikeMagnitude, tamuraCoarseness, DoubleType.class);
        //allFeatures = ObjectArrays.concat(allFeatures, tamuraCoarseness, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, tamuraContrast, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, tamuraDirectionality, DoubleType.class);
        //allFeatures = ObjectArrays.concat(allFeatures, imagejStatsGeometricMean, DoubleType.class);
        //allFeatures = ObjectArrays.concat(allFeatures, imagejStatsHarmonicMean, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, imagejStatsMean, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, imagejStatsKurtosis, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, imagejStatsStdDev, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, imagejStatsVariance, DoubleType.class);
    }

    private static void makeNoduleDataCSV() {
        System.out.println("Making CSV");
        final DoubleType[][] csvMatrix = new DoubleType[numberOfNodules][numberOfFeatures];

        int k = 0;
        for (int i = 0; i < numberOfFeatures; ++i) {
            for (int j = 0; j < numberOfNodules; ++j, ++k) {
                csvMatrix[j][i] = allFeatures[k];
                System.out.println(i + " " + j);
            }
        }

        System.out.println("End Making CSV");

        writeCsv(csvMatrix);
    }

    private static void writeCsv(DoubleType[][] csvMatrix) {
        String fileName = "C:\\Users\\wesle\\IdeaProjects\\TumorClassificator\\src\\main\\resources\\TumorData.csv";

        ICsvListWriter csvWriter = null;
        try {
            csvWriter = new CsvListWriter(new FileWriter(fileName), CsvPreference.STANDARD_PREFERENCE);

            for (int i = 0; i < csvMatrix.length; i++) {
                csvWriter.write(csvMatrix[i]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                csvWriter.close();
            } catch (IOException e) {
            }
        }
    }
}
