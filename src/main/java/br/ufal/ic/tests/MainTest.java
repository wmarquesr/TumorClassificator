package br.ufal.ic.tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ij.ImagePlus;
import ij.io.Opener;
import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.plugins.commands.typechange.TypeChanger;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.DoubleType;
import org.scijava.module.ModuleInfo;

/**
 * Created by Wesley on 23/05/2017.
 */
public class MainTest {

    public static void main(String[] args) throws IOException {
        List<String> results = new ArrayList<String>();

        File[] files = new File("C:\\Users\\wesle\\Downloads\\Ref-Med\\nodulo").listFiles();
        //If this pathname does not denote a directory, then listFiles() returns null.

        for (File file : files) {
            if (file.isFile()) {
                //extractZernickeFeature(file);
                extractTamuraFeature(file);
            }
        }
    }

    public static void extractZernickeFeature(File imageFile) throws IOException {
        ImageJ ij = new ImageJ();
        //File file = ij.ui().chooseFile(null, "Open");
        //File file = new File("C:\\Users\\wesle\\Downloads\\Ref-Med\\nodulo\\e3n0r0B.png");
        Dataset dataset = ij.scifio().datasetIO().open(imageFile.getPath());
        //ij.ui().show(dataset);
        ModuleInfo module = ij.command().getCommand(TypeChanger.class);
        Map<String, Object> input = new HashMap<String, Object>();
        input.put("data", dataset);
        input.put("typeName", "8-bit unsigned integer");
        input.put("combineChannels", true);
        ij.module().waitFor(ij.module().run(module, true, input));
        // ij.op().haralick, ij.op().zernike, ij.op().tamura, ij.op().stats estes são os descritores que possuem
        // os atributos
        DoubleType value = ij.op().zernike().magnitude((IterableInterval<DoubleType>)dataset.getImgPlus(), 0,0);
        System.out.println(value);
    }

    public static void extractTamuraFeature(File imageFile) throws IOException {
        ImageJ ij = new ImageJ();
        Dataset dataset = ij.scifio().datasetIO().open(imageFile.getPath());

        ModuleInfo module = ij.command().getCommand(TypeChanger.class);
        Map<String, Object> input = new HashMap<String, Object>();
        input.put("data", dataset);
        input.put("typeName", "8-bit unsigned integer");
        input.put("combineChannels", true);
        ij.module().waitFor(ij.module().run(module, true, input));

        ImagePlus img = new Opener().openImage(imageFile.getPath());
        int histogramSize = img.getStatistics().histogram.length;
        //OpService ops = null;
        //Long histogramSize = ops.image().histogram((IterableInterval<DoubleType>) dataset.getImgPlus()).size();

        DoubleType value = ij.op().tamura().directionality((RandomAccessibleInterval<DoubleType>) dataset.getImgPlus(),histogramSize);
        System.out.println(value);

    }
}
