package br.ufal.ic.src;

import net.imglib2.type.numeric.real.DoubleType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by DVD on 29/05/2017.
 */
public class CSVWriter {

    private PrintWriter pw;
    private StringBuilder sb;

    public CSVWriter(String pathFile){

        try {
            pw = new PrintWriter(new File(pathFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<String> header = new ArrayList<>();
        header.add("Image_file");
        header.add("Magnitude");
        header.add("Phase");
        header.add("Coarseness");
        header.add("Contrast");
        header.add("Directionality");
        header.add("Mean");
        header.add("Kurtoses");
       // header.add("Median"); // precisão sempre 0
        header.add("Standard Deviation");
        header.add("Variance");
        headerRecord(header);

    }
    private void headerRecord(ArrayList<String> line){

        sb = new StringBuilder();

        for ( String features: line){
            sb.append(features);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);

        pw.println(sb);

    }

    public void writeRecord(ArrayList<DoubleType> line, File file){

        sb = new StringBuilder();
        sb.append(file.getName());
        sb.append(", ");

        for (DoubleType features: line){
            sb.append(features);
            sb.append(", ");
        }
        sb.deleteCharAt(sb.length()-1);

        pw.println(sb);

    }
    public void close(){
        pw.close();
    }



}
