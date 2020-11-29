import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.*;


public class Roster extends JPanel{

    private StringBuilder location;

    public ArrayList<ArrayList<String>> loadRoster() {
        ArrayList<ArrayList<String>> roster = null;
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".csv", "csv");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            //System.out.println(selectedFile.getAbsolutePath());
            roster = read(selectedFile.getAbsolutePath());
            location = new StringBuilder();
            location.append(selectedFile.getAbsolutePath());
        }
        return roster;

    }

    public StringBuilder getSavePath(){

        if(location == null){
            location = new StringBuilder(System.getProperty("user.home"));
        }
        return location;
    }

    /**
     * Function to read in CSV file.  Used by both the loadRoster() function and the loadAttendance() function
     *
     * @param csvFile
     * @return
     */
    public ArrayList read(String csvFile) {
        final String delimiter = ",";
        ArrayList<ArrayList<String>> outer = new ArrayList<ArrayList<String>>();
        ArrayList<String> inner = new ArrayList<String>();
        try {
            File file = new File(csvFile);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            String[] tempArr;
            while ((line = br.readLine()) != null) {
                tempArr = line.split(delimiter);
                for (String tempStr : tempArr) {
                    inner.add(tempStr);
                }
                outer.add((ArrayList) inner.clone());
                inner.clear();
            }
            br.close();

            String firstElementName = outer.get(0).get(0).replaceAll("[^a-zA-Z0-9]", "");
            String secondElementName = outer.get(0).get(1).replaceAll("[^a-zA-Z0-9]", "");
            outer.set(0, new ArrayList<>(Arrays.asList(firstElementName, outer.get(0).get(1), outer.get(0).get(2), outer.get(0).get(3), outer.get(0).get(4), outer.get(0).get(5))));
            return outer;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }

}
