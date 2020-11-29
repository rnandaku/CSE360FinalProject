import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import org.jdatepicker.impl.*;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;

public class Attendance extends JPanel{

    private ArrayList<String> copy;
    private ArrayList<ArrayList<String>> notOnRoster;
    private int attendeeCount = 0;
    private StringBuilder aMessage;


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
            //System.out.println(outer.toString());
            //System.out.println("Outer: " + outer.get(0));
            //outer.add(0, String.valueOf(outer.get(0).get(0)));
            String firstElementName = outer.get(0).get(0).replaceAll("[^a-zA-Z0-9]", "");
            String secondElementName = outer.get(0).get(1).replaceAll("[^a-zA-Z0-9]", "");
            outer.set(0, new ArrayList<>(Arrays.asList(firstElementName, secondElementName)));
            return outer;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }

    /**
     * Function to load in attendance CSV
     */
    public ArrayList<ArrayList<String>> loadAttendance(ArrayList<ArrayList<String>> roster, ArrayList<String> header) {

        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".csv", "csv");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(null);

        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, null);
        JOptionPane.showMessageDialog(null, datePicker, "Date of Attendance", JOptionPane.QUESTION_MESSAGE);
        Date selectedDate = (Date) datePicker.getModel().getValue();
        String date=selectedDate+"";
        String delims = "[ ]+";
        String[] tokens = date.split(delims);
        String finalDate = tokens[1] + " " + tokens[2] + "";
        header.add(finalDate);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (roster.get(0).size() > 4) matchedRosterAttendance(mergeDuplicatesAttendance(selectedFile, roster), roster);
        }
        copy = new ArrayList<String>(header);
        return roster;
         
    }

    public ArrayList<String> getHeader(){
        return copy;
    }

    /**
     * This function is used by the loadAttendance() function to merge all the duplicate entries
     *
     * @param file
     * @return
     */
    public ArrayList mergeDuplicatesAttendance(File file, ArrayList<ArrayList<String>> roster) {
        ArrayList<ArrayList<String>> attendance = read(file.getAbsolutePath());
        ArrayList<Object> inner = new ArrayList<>();
        ArrayList<ArrayList<String>> finalList = new ArrayList<>();
        attendeeCount = 0;

        for (int i = 0; i < roster.size(); i++) {
            int individualValue = 0;
            String rosterId = (String) roster.get(i).get(5);
            for (int j = 0; j < attendance.size(); j++) {
                String attendanceId = (String) attendance.get(j).get(0);
                int duration = Integer.valueOf(String.valueOf(attendance.get(j).get(1)));

                //DEBUGGING PURPOSES (problem can be seen here as well, please look thru the list, especially at
                // the "johnsmith"s)
                //System.out.println(attendanceId + "=" + rosterId + "\t" + attendanceId.equals(rosterId));

                // PROBLEM IS RIGHT HERE WHEN attendanceId doesn't equal the value of rosterId
                if (attendanceId.equals(String.valueOf(rosterId))) individualValue += duration;
            }
            inner.add(rosterId);
            inner.add(individualValue);

            finalList.add((ArrayList) inner.clone());
            inner.clear();
        }
        //System.out.println(finalList); //[[rnandaku, 40], [weiurh, 0], [johnsmith, 40]]

        notOnRoster = new ArrayList<ArrayList<String>>();
        //COMPARE FINAL LIST WITH ATTENDANCE TO SEE WHICH ATTENDEES WERENT ON THE ROSTER 
        for(int k = 0; k < attendance.size(); k++){
            Boolean flag = false;
            String attendanceId = (String) attendance.get(k).get(0);
            for(int l = 0; l < finalList.size(); l++){
                String finalId = (String) finalList.get(l).get(0);
                if(attendanceId.equals(String.valueOf(finalId))){
                    flag = true;
                }
            }
            if(flag == false){
                notOnRoster.add(attendance.get(k));
                attendeeCount++;
            }
            
        }

        return finalList;
    }

    /** DELETE THIS LATER
    public ArrayList<ArrayList> notOnRoster(){
        return notOnRoster;
    }

    public int getAttendeeCount(){
        return attendeeCount;
    }
    */

    public StringBuilder getAttendanceMessage(){
       
        String attendanceMessage = "Data loaded for " + attendeeCount + " users in the roster.\n\n" + notOnRoster.size() + " additional attendee(s) were found:\n\n";
        String attendeeList = "";
        for(int i = 0; i < notOnRoster.size(); i++){
            attendeeList = attendeeList + notOnRoster.get(i).get(0) + ", connected for " + notOnRoster.get(i).get(1) + " minutes\n";
        }
        attendanceMessage = attendanceMessage + attendeeList;
        aMessage = new StringBuilder(attendanceMessage);
        return aMessage;
    } 

    /**
     * This function is also used by the loadAttendance function to match up the
     * roster and the attendance file together and not include entries in the
     * attendance onto the roster file
     *
     * @param attendance
     */
    public void matchedRosterAttendance(ArrayList<ArrayList<String>> attendance, ArrayList<ArrayList<String>> roster) {
        //attendance.add(new ArrayList<>(Arrays.asList("0", "0")));
        for (int i = 0; i < attendance.size(); i++) {
            String attendanceId = (String) attendance.get(i).get(0);
            int attendanceDuration = Integer.parseInt(String.valueOf(attendance.get(i).get(1)));
            for (int j = 0; j < roster.size(); j++) {
                String rosterId = (String) roster.get(j).get(5);
                if (attendanceId.equals(rosterId)){
                    roster.get(i).add(String.valueOf(attendanceDuration));
                } 
            }
        }
    }


}
