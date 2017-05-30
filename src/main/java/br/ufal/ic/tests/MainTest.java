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

        File[] files = new File("C:\\Users\\wesle\\Documents\\Ref-Med\\nodulo").listFiles();
        File file;

        for (int i = 0; i < files.length; ++i) {
            file = files[i];
            if (file.isFile()) {
                //extractZernickeFeature(file);
                //extractTamuraFeature(file);
                //System.out.println(file.getName().replace(".png", ""));
                if (file.getName().replace(".png", "").endsWith("M")){
                    System.out.println("TRUE");
                } else if (file.getName().replace(".png", "").endsWith("B")) {
                    System.out.println("FALSE");
                }
            }
        }
    }

    public static void extractZernickeFeature(File imageFile) throws IOException {
        ImageJ ij = new ImageJ();
        Dataset dataset = ij.scifio().datasetIO().open(imageFile.getPath());
        ModuleInfo module = ij.command().getCommand(TypeChanger.class);
        Map<String, Object> input = new HashMap<String, Object>();
        input.put("data", dataset);
        input.put("typeName", "8-bit unsigned integer");
        input.put("combineChannels", true);
        ij.module().waitFor(ij.module().run(module, true, input));
        DoubleType value = ij.op().zernike().phase((IterableInterval<DoubleType>)dataset.getImgPlus(), 0,0);
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
