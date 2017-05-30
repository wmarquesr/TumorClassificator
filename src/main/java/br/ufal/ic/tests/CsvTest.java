package br.ufal.ic.tests;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import com.google.common.collect.ObjectArrays;
import org.scijava.util.ObjectArray;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

/**
 * Created by wesle on 28/05/2017.
 */
public class CsvTest {

    public static void main(String[] args) {

        String Col0[] = {"A1", "B1", "C1", "D1", "E1", "F1", "G1", "H1", "I1", "J1"};
        String Col1[] = {"A2", "B2", "C2", "D2", "E2", "F2", "G2", "H2", "I2", "J2"};
        String Col2[] = {"A3", "B3", "C3", "D3", "E3", "F3", "G3", "H3", "I3", "J3"};
        //String[] both = Stream.concat(Arrays.stream(Col0), Arrays.stream(Col1)).toArray(String[]::new);
        //both = Stream.concat(Arrays.stream(both), Arrays.stream(Col2)).toArray(String[]::new);
        String[] both = ObjectArrays.concat(Col0, Col1, String.class);
        both = ObjectArrays.concat(both, Col2, String.class);

        // you can assemble this 2D array however you want
        final String[][] csvMatrix = new String[10][3];

        int k = 0;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 10; ++j, ++k){
                csvMatrix[j][i] = both[k];
            }
        }

        /*csvMatrix[0][0] = "first0";
        csvMatrix[0][1] = "second0";
        csvMatrix[0][2] = "third0";
        csvMatrix[1][0] = "first1";
        csvMatrix[1][1] = "second1";
        csvMatrix[1][2] = "third1";
        csvMatrix[2][0] = "first2";
        csvMatrix[2][1] = "second2";
        csvMatrix[2][2] = "third2";*/

        //writeCsv(csvMatrix);

    }

    /*private static void writeCsv(String[][] csvMatrix) {
        String fileName = "C:\\Users\\wesle\\IdeaProjects\\TumorClassificator\\src\\main\\resources\\TumorData.csv";

        ICsvListWriter csvWriter = null;
        try {
            csvWriter = new CsvListWriter(new FileWriter(fileName), CsvPreference.STANDARD_PREFERENCE);

            for (int i = 0; i < csvMatrix.length; i++) {
                csvWriter.write(csvMatrix[i]);
            }

        } catch (IOException e) {
            e.printStackTrace(); // TODO handle exception properly
        } finally {
            try {
                csvWriter.close();
            } catch (IOException e) {
            }
        }
    }*/
}
