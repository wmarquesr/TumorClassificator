package br.ufal.ic.src;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ij.IJ;
import ij.ImagePlus;
import ij.io.Opener;
import ij.process.ImageConverter;
import io.scif.formats.qt.LegacyQTFormat;
import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.plugins.commands.typechange.TypeChanger;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.DoubleType;
import org.scijava.module.ModuleInfo;

import static ij.io.RoiDecoder.open;

/**
 * Created by Wesley on 28/05/2017.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        ImageJ ij = new ImageJ();
        ModuleInfo module = ij.command().getCommand(TypeChanger.class);
        CSVWriter csv = new CSVWriter("feature.csv");


        System.out.println(new File("").getAbsolutePath());
        File filesPath = new File("src\\main\\java\\br\\ufal\\ic\\src\\Ref-Med\\nodulo");
        int processedFile=0;

        for (File file : filesPath.listFiles()) {

            Dataset dataset = ij.scifio().datasetIO().open(file.getPath());
            Map<String, Object> input = new HashMap<String, Object>();

            input.put("data", dataset);
            input.put("typeName", "8-bit unsigned integer");
            input.put("combineChannels", true);
            ij.module().waitFor(ij.module().run(module, true, input));


            ArrayList<DoubleType> features = new ArrayList<DoubleType>();

            features.add(ij.op().zernike().magnitude((IterableInterval<DoubleType>) dataset.getImgPlus(), 0, 0));
            features.add(ij.op().zernike().phase((IterableInterval<DoubleType>) dataset.getImgPlus(),0,0));

            int histogramSize = new Opener().openImage(file.getPath()).getStatistics().histogram.length;
            features.add(ij.op().tamura().coarseness((RandomAccessibleInterval<DoubleType>) dataset.getImgPlus()));
            features.add(ij.op().tamura().contrast((RandomAccessibleInterval<DoubleType>) dataset.getImgPlus()));
            features.add(ij.op().tamura().directionality((RandomAccessibleInterval<DoubleType>) dataset.getImgPlus(),histogramSize));

            features.add(ij.op().stats().mean((Iterable<DoubleType>) dataset.getImgPlus()));
            features.add(ij.op().stats().kurtosis((Iterable<DoubleType>) dataset.getImgPlus()));
           // features.add(ij.op().stats().median((Iterable<DoubleType>) dataset.getImgPlus())); // precisão sempre 0
            features.add(ij.op().stats().stdDev((Iterable<DoubleType>) dataset.getImgPlus()));
            features.add(ij.op().stats().variance((Iterable<DoubleType>) dataset.getImgPlus()));
            //features.add((DoubleType) ij.op().stats().harmonicMean((Iterable<DoubleType>) dataset.getImgPlus())); // valores praticamente zero

            csv.writeRecord(features, file);
            processedFile++;
            System.out.println("(" + processedFile + "/" + filesPath.listFiles().length + ")");

            if(processedFile == 2) break;
        }
        csv.close();
    }
}
