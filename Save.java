import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.StringUtils;

public class Save{

    /**
     * Function to save to a specific location
     *
     * @param location
     */

    //saves in same path as roster
    // HELP HERE: There's a question mark right before the first entry - Is it a charset issue?
    public void saveFile(String location, ArrayList<ArrayList> roster, ArrayList<String> header) {
        ArrayList<ArrayList> finalSaveFile = new ArrayList<ArrayList>((ArrayList) roster.clone());
        finalSaveFile.add(0, header);
        try {
            // Try changing the Charsets so it outputs everything properly?  I've tried most of the charsets to no avail
            FileWriter csvWriter = new FileWriter(location + "\\saveRoster.csv", StandardCharsets.US_ASCII);

            for (List<String> rowData : finalSaveFile) {
                String row = "";
                for (Object colData : rowData) {
                    row += String.valueOf(colData);
                    row += ",";
                }
                //System.out.println(row + "\n");
                csvWriter.append(row);
                csvWriter.append("\n");
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}