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
import net.imagej.ops.Ops;
import net.imagej.ops.image.cooccurrenceMatrix.MatrixOrientation;
import net.imagej.ops.image.cooccurrenceMatrix.MatrixOrientation2D;
import net.imagej.plugins.commands.typechange.TypeChanger;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.ops.data.CooccurrenceMatrix;
import net.imglib2.type.numeric.real.DoubleType;
import org.scijava.module.ModuleInfo;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

/**
 * Created by Wesley on 28/05/2017.
 */
public class Main {

    private static int numberOfNodules = 920;
    private static int numberOfFeatures = 79;

    private static int X, Y, count = 0;
    private static int tX = -1, tY =-1;
    private static String fileName = "";
    private static ArrayList<File> tempFile = new ArrayList<>();

    /* ---------  Arrays for each feature -------------*/
    private static DoubleType[] allFeatures;

    private static DoubleType[] zernikeMagnitude = new DoubleType[numberOfNodules];

    private static DoubleType[] tamuraCoarseness = new DoubleType[numberOfNodules];
    private static DoubleType[] tamuraContrast = new DoubleType[numberOfNodules];
    private static DoubleType[] tamuraDirectionality = new DoubleType[numberOfNodules];

    private static DoubleType[] imagejStatsGeometricMean = new DoubleType[numberOfNodules];
    private static DoubleType[] imagejStatsHarmonicMean = new DoubleType[numberOfNodules];
    private static DoubleType[] imagejStatsMean = new DoubleType[numberOfNodules];
    private static DoubleType[] imagejStatsKurtosis = new DoubleType[numberOfNodules];
    private static DoubleType[] imagejStatsStdDev = new DoubleType[numberOfNodules];
    private static DoubleType[] imagejStatsVariance = new DoubleType[numberOfNodules];
    private static DoubleType[] imageJStatsSize = new DoubleType[numberOfNodules];

    private static DoubleType[] haralickASMVertical = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickASMHorizontal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickASMDiagonal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickASMAntidiagonal = new DoubleType[numberOfNodules];

    private static DoubleType[] haralickclusterPromenenceVertical = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickclusterPromenenceHorizontal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickclusterPromenenceDiagonal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickclusterPromenenceAntidiagonal = new DoubleType[numberOfNodules];

    private static DoubleType[] haralickclusterShadeVertical = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickclusterShadeHorizontal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickclusterShadeDiagonal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickclusterShadeAntidiagonal = new DoubleType[numberOfNodules];

    private static DoubleType[] haralickContrastVertical = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickContrastHorizontal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickContrastDiagonal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickContrastAntidiagonal = new DoubleType[numberOfNodules];

    private static DoubleType[] haralickcorrelationVertical = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickcorrelationHorizontal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickcorrelationDiagonal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickcorrelationAntidiagonal = new DoubleType[numberOfNodules];

    private static DoubleType[] haralickdifferenceEntropyVertical = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickdifferenceEntropyHorizontal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickdifferenceEntropyDiagonal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickdifferenceEntropyAntidiagonal = new DoubleType[numberOfNodules];

    private static DoubleType[] haralickdifferenceVarianceVertical = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickdifferenceVarianceHorizontal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickdifferenceVarianceDiagonal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickdifferenceVarianceAntidiagonal = new DoubleType[numberOfNodules];

    private static DoubleType[] haralickentropyVertical = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickentropyHorizontal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickentropyDiagonal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickentropyAntidiagonal = new DoubleType[numberOfNodules];

    private static DoubleType[] haralickicm1Vertical = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickicm1Horizontal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickicm1Diagonal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickicm1Antidiagonal = new DoubleType[numberOfNodules];

    private static DoubleType[] haralickicm2Vertical = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickicm2Horizontal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickicm2Diagonal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickicm2Antidiagonal = new DoubleType[numberOfNodules];

    private static DoubleType[] haralickifdmVertical = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickifdmHorizontal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickifdmDiagonal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickifdmAntidiagonal = new DoubleType[numberOfNodules];

    private static DoubleType[] haralickmaxProbabilityVertical = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickmaxProbabilityHorizontal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickmaxProbabilityDiagonal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickmaxProbabilityAntidiagonal = new DoubleType[numberOfNodules];

    private static DoubleType[] haralicksumAverageVertical = new DoubleType[numberOfNodules];
    private static DoubleType[] haralicksumAverageHorizontal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralicksumAverageDiagonal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralicksumAverageAntidiagonal = new DoubleType[numberOfNodules];

    private static DoubleType[] haralicksumEntropyVertical = new DoubleType[numberOfNodules];
    private static DoubleType[] haralicksumEntropyHorizontal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralicksumEntropyDiagonal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralicksumEntropyAntidiagonal = new DoubleType[numberOfNodules];

    private static DoubleType[] haralicksumVarianceVertical = new DoubleType[numberOfNodules];
    private static DoubleType[] haralicksumVarianceHorizontal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralicksumVarianceDiagonal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralicksumVarianceAntidiagonal = new DoubleType[numberOfNodules];

    private static DoubleType[] haralicktextureHomogeneityVertical = new DoubleType[numberOfNodules];
    private static DoubleType[] haralicktextureHomogeneityHorizontal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralicktextureHomogeneityDiagonal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralicktextureHomogeneityAntidiagonal = new DoubleType[numberOfNodules];

    private static DoubleType[] haralickvarianceVertical = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickvarianceHorizontal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickvarianceDiagonal = new DoubleType[numberOfNodules];
    private static DoubleType[] haralickvarianceAntidiagonal = new DoubleType[numberOfNodules];
    /* ---------  Arrays for each feature -------------*/

    public static void main(String[] args) throws IOException {
        ImageJ ij = new ImageJ();
        ModuleInfo module = ij.command().getCommand(TypeChanger.class);

        File[] filesPath = new File("C:\\Users\\wesle\\Documents\\Ref-Med\\nodulo").listFiles();
        System.out.println(filesPath.length);
        File file;
        int k = 0;
        for (int i = 0; i < filesPath.length; ++i) {
            file = filesPath[i];

            fileName = file.getName();
            fileName = fileName.replaceAll("[^0-9]+", " ");

            X = Integer.parseInt(fileName.trim().split(" ")[0]);
            Y = Integer.parseInt(fileName.trim().split(" ")[1]);

            if (tX == -1 && tY == -1) {
                tX = X; tY = Y;
            }

            if (X == tX && Y == tY) {
                tempFile.add(file);
            } else {

                Dataset dataset = ij.scifio().datasetIO().open(file.getPath());
                Map<String, Object> input = new HashMap<String, Object>();

                input.put("data", dataset);
                input.put("typeName", "8-bit unsigned integer");
                input.put("combineChannels", true);
                ij.module().waitFor(ij.module().run(module, true, input));

                zernikeMagnitude[k] = ij.op().zernike().magnitude((IterableInterval<DoubleType>) dataset.getImgPlus(), 0, 0);

                //int histogramSize = new Opener().openImage(file.getPath()).getStatistics().histogram.length;
                int histogramSize = (int) ij.op().image().histogram((Iterable<DoubleType>) dataset.getImgPlus()).size();
                tamuraCoarseness[k] = ij.op().tamura().coarseness((RandomAccessibleInterval<DoubleType>) dataset.getImgPlus());
                tamuraContrast[k] = ij.op().tamura().contrast((RandomAccessibleInterval<DoubleType>) dataset.getImgPlus());
                tamuraDirectionality[k] = ij.op().tamura().directionality((RandomAccessibleInterval<DoubleType>) dataset.getImgPlus(),histogramSize);

                imagejStatsGeometricMean[i] = ij.op().stats().geometricMean((Iterable<DoubleType>) dataset.getImgPlus());
                imagejStatsHarmonicMean[i] = ij.op().stats().harmonicMean((Iterable<DoubleType>) dataset.getImgPlus());

                imagejStatsMean[k] = ij.op().stats().mean((Iterable<DoubleType>) dataset.getImgPlus());
                imagejStatsKurtosis[k] = ij.op().stats().kurtosis((Iterable<DoubleType>) dataset.getImgPlus());
                imagejStatsStdDev[k] = ij.op().stats().stdDev((Iterable<DoubleType>) dataset.getImgPlus());
                imagejStatsVariance[k] = ij.op().stats().variance((Iterable<DoubleType>) dataset.getImgPlus());
                imageJStatsSize[k] = ij.op().stats().size((IterableInterval<DoubleType>) dataset.getImgPlus());

                haralickASMVertical[k] = ij.op().haralick().asm((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.VERTICAL);
                haralickASMHorizontal[k] = ij.op().haralick().asm((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.HORIZONTAL);
                haralickASMDiagonal[k] = ij.op().haralick().asm((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.DIAGONAL);
                haralickASMAntidiagonal[k] = ij.op().haralick().asm((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.ANTIDIAGONAL);

                haralickclusterPromenenceVertical[k] = ij.op().haralick().clusterPromenence((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.VERTICAL);
                haralickclusterPromenenceHorizontal[k] = ij.op().haralick().clusterPromenence((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.HORIZONTAL);
                haralickclusterPromenenceDiagonal[k] = ij.op().haralick().clusterPromenence((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.DIAGONAL);
                haralickclusterPromenenceAntidiagonal[k] = ij.op().haralick().clusterPromenence((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.ANTIDIAGONAL);

                haralickclusterShadeVertical[k] = ij.op().haralick().clusterShade((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.VERTICAL);
                haralickclusterShadeHorizontal[k] = ij.op().haralick().clusterShade((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.HORIZONTAL);
                haralickclusterShadeDiagonal[k] = ij.op().haralick().clusterShade((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.DIAGONAL);
                haralickclusterShadeAntidiagonal[k] = ij.op().haralick().clusterShade((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.ANTIDIAGONAL);

                haralickContrastVertical[k] = ij.op().haralick().contrast((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.VERTICAL);
                haralickContrastHorizontal[k] = ij.op().haralick().contrast((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.HORIZONTAL);
                haralickContrastDiagonal[k] = ij.op().haralick().contrast((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.DIAGONAL);
                haralickContrastAntidiagonal[k] = ij.op().haralick().contrast((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.ANTIDIAGONAL);

                haralickcorrelationVertical[k] = ij.op().haralick().correlation((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.VERTICAL);
                haralickcorrelationHorizontal[k] = ij.op().haralick().correlation((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.HORIZONTAL);
                haralickcorrelationDiagonal[k] = ij.op().haralick().correlation((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.DIAGONAL);
                haralickcorrelationAntidiagonal[k] = ij.op().haralick().correlation((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.ANTIDIAGONAL);

                haralickdifferenceEntropyVertical[k] = ij.op().haralick().differenceEntropy((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.VERTICAL);
                haralickdifferenceEntropyHorizontal[k] = ij.op().haralick().differenceEntropy((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.HORIZONTAL);
                haralickdifferenceEntropyDiagonal[k] = ij.op().haralick().differenceEntropy((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.DIAGONAL);
                haralickdifferenceEntropyAntidiagonal[k] = ij.op().haralick().differenceEntropy((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.ANTIDIAGONAL);

                haralickdifferenceVarianceVertical[k] = ij.op().haralick().differenceVariance((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.VERTICAL);
                haralickdifferenceVarianceHorizontal[k] = ij.op().haralick().differenceVariance((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.HORIZONTAL);
                haralickdifferenceVarianceDiagonal[k] = ij.op().haralick().differenceVariance((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.DIAGONAL);
                haralickdifferenceVarianceAntidiagonal[k] = ij.op().haralick().differenceVariance((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.ANTIDIAGONAL);

                haralickentropyVertical[k] = ij.op().haralick().entropy((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.VERTICAL);
                haralickentropyHorizontal[k] = ij.op().haralick().entropy((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.HORIZONTAL);
                haralickentropyDiagonal[k] = ij.op().haralick().entropy((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.DIAGONAL);
                haralickentropyAntidiagonal[k] = ij.op().haralick().entropy((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.ANTIDIAGONAL);

                haralickicm1Vertical[k] = ij.op().haralick().icm1((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.VERTICAL);
                haralickicm1Horizontal[k] = ij.op().haralick().icm1((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.HORIZONTAL);
                haralickicm1Diagonal[k] = ij.op().haralick().icm1((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.DIAGONAL);
                haralickicm1Antidiagonal[k] = ij.op().haralick().icm1((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.ANTIDIAGONAL);

                haralickicm2Vertical[k] = ij.op().haralick().icm2((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.VERTICAL);
                haralickicm2Horizontal[k] = ij.op().haralick().icm2((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.HORIZONTAL);
                haralickicm2Diagonal[k] = ij.op().haralick().icm2((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.DIAGONAL);
                haralickicm2Antidiagonal[k] = ij.op().haralick().icm2((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.ANTIDIAGONAL);

                haralickifdmVertical[k] = ij.op().haralick().ifdm((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.VERTICAL);
                haralickifdmHorizontal[k] = ij.op().haralick().ifdm((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.HORIZONTAL);
                haralickifdmDiagonal[k] = ij.op().haralick().ifdm((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.DIAGONAL);
                haralickifdmAntidiagonal[k] = ij.op().haralick().ifdm((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.ANTIDIAGONAL);

                haralickmaxProbabilityVertical[k] = ij.op().haralick().maxProbability((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.VERTICAL);
                haralickmaxProbabilityHorizontal[k] = ij.op().haralick().maxProbability((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.HORIZONTAL);
                haralickmaxProbabilityDiagonal[k] = ij.op().haralick().maxProbability((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.DIAGONAL);
                haralickmaxProbabilityAntidiagonal[k] = ij.op().haralick().maxProbability((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.ANTIDIAGONAL);

                haralicksumAverageVertical[k] = ij.op().haralick().sumAverage((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.VERTICAL);
                haralicksumAverageHorizontal[k] = ij.op().haralick().sumAverage((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.HORIZONTAL);
                haralicksumAverageDiagonal[k] = ij.op().haralick().sumAverage((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.DIAGONAL);
                haralicksumAverageAntidiagonal[k] = ij.op().haralick().sumAverage((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.ANTIDIAGONAL);

                haralicksumEntropyVertical[k] = ij.op().haralick().sumEntropy((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.VERTICAL);
                haralicksumEntropyHorizontal[k] = ij.op().haralick().sumEntropy((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.HORIZONTAL);
                haralicksumEntropyDiagonal[k] = ij.op().haralick().sumEntropy((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.DIAGONAL);
                haralicksumEntropyAntidiagonal[k] = ij.op().haralick().sumEntropy((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.ANTIDIAGONAL);

                haralicksumVarianceVertical[k] = ij.op().haralick().sumVariance((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.VERTICAL);
                haralicksumVarianceHorizontal[k] = ij.op().haralick().sumVariance((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.HORIZONTAL);
                haralicksumVarianceDiagonal[k] = ij.op().haralick().sumVariance((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.DIAGONAL);
                haralicksumVarianceAntidiagonal[k] = ij.op().haralick().sumVariance((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.ANTIDIAGONAL);

                haralicktextureHomogeneityVertical[k] = ij.op().haralick().textureHomogeneity((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.VERTICAL);
                haralicktextureHomogeneityHorizontal[k] = ij.op().haralick().textureHomogeneity((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.HORIZONTAL);
                haralicktextureHomogeneityDiagonal[k] = ij.op().haralick().textureHomogeneity((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.DIAGONAL);
                haralicktextureHomogeneityAntidiagonal[k] = ij.op().haralick().textureHomogeneity((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.ANTIDIAGONAL);

                haralickvarianceVertical[k] = ij.op().haralick().variance((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.VERTICAL);
                haralickvarianceHorizontal[k] = ij.op().haralick().variance((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.HORIZONTAL);
                haralickvarianceDiagonal[k] = ij.op().haralick().variance((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.DIAGONAL);
                haralickvarianceAntidiagonal[k] = ij.op().haralick().variance((IterableInterval<DoubleType>)dataset.getImgPlus(),256,1, MatrixOrientation2D.ANTIDIAGONAL);

                ++k;
                count++;
                tX = X; tY = Y;
                tempFile.clear();
                tempFile.add(file);
            }

            System.out.println(i);
        }
        System.out.println(count);

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
        allFeatures = ObjectArrays.concat(allFeatures, imagejStatsGeometricMean, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, imagejStatsHarmonicMean, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, imagejStatsMean, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, imagejStatsKurtosis, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, imagejStatsStdDev, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, imagejStatsVariance, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, imageJStatsSize, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickASMVertical, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickASMHorizontal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickASMDiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickASMAntidiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickclusterPromenenceVertical, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickclusterPromenenceHorizontal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickclusterPromenenceDiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickclusterPromenenceAntidiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickclusterShadeVertical, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickclusterShadeHorizontal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickclusterShadeDiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickclusterShadeAntidiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickContrastVertical, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickContrastHorizontal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickContrastDiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickContrastAntidiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickcorrelationVertical, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickcorrelationHorizontal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickcorrelationDiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickcorrelationAntidiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickdifferenceEntropyVertical, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickdifferenceEntropyHorizontal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickdifferenceEntropyDiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickdifferenceEntropyAntidiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickdifferenceVarianceVertical, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickdifferenceVarianceHorizontal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickdifferenceVarianceDiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickdifferenceVarianceAntidiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickentropyVertical, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickentropyHorizontal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickentropyDiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickentropyAntidiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickicm1Vertical, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickicm1Horizontal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickicm1Diagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickicm1Antidiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickicm2Vertical, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickicm2Horizontal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickicm2Diagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickicm2Antidiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickifdmVertical, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickifdmHorizontal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickifdmDiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickifdmAntidiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickmaxProbabilityVertical, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickmaxProbabilityHorizontal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickmaxProbabilityDiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickmaxProbabilityAntidiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralicksumAverageVertical, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralicksumAverageHorizontal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralicksumAverageDiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralicksumAverageAntidiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralicksumEntropyVertical, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralicksumEntropyHorizontal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralicksumEntropyDiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralicksumEntropyAntidiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralicksumVarianceVertical, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralicksumVarianceHorizontal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralicksumVarianceDiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralicksumVarianceAntidiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralicktextureHomogeneityVertical, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralicktextureHomogeneityHorizontal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralicktextureHomogeneityDiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralicktextureHomogeneityAntidiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickvarianceVertical, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickvarianceHorizontal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickvarianceDiagonal, DoubleType.class);
        allFeatures = ObjectArrays.concat(allFeatures, haralickvarianceAntidiagonal, DoubleType.class);

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
