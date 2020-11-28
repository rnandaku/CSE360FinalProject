import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.jdatepicker.impl.*;
import org.jdatepicker.*;
import org.jdatepicker.graphics.*;

import java.io.File; //not sure if I can use this import 

public class Menu extends JFrame implements ActionListener {

    private JFrame frame;
    private JMenuBar menubar;
    private JMenu file;
    //private JMenu about;  <---- use this instead if we decide to make submenu with about menu item
    private JRadioButtonMenuItem aboutMenu;
    private JMenuItem rosterMenu;
    private JMenuItem attendanceMenu;
    private JMenuItem saveMenu;
    private JMenuItem plotMenu;
    boolean rosterLoaded = false; 
    boolean plotReady = false;
    private String saveLocation;
    private ArrayList<ArrayList> rosterList;
    private ArrayList<String> header = new ArrayList<>(Arrays.asList("ID", "First Name", "Last Name", "Program",
            "Level", "ASURITE"));

    //Creates Menu Object that contains initial GUI elements
    public Menu(){

        //Set up Frame/Window
        frame = new JFrame("CSE360 Final Project");
        frame.setVisible(true);
        frame.setSize(600,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and add the menu bar to the frame
        menubar = new JMenuBar();
        frame.setJMenuBar(menubar);

        //Add menu item to the menu bar "File"
        file = new JMenu("File");
        menubar.add(file);

        //Add each sub-item that falls under "File"
        rosterMenu = new JMenuItem("Load a Roster");
        file.add(rosterMenu);
        attendanceMenu = new JMenuItem("Add Attendance");
        file.add(attendanceMenu);
        saveMenu = new JMenuItem("Save");
        file.add(saveMenu);
        plotMenu = new JMenuItem("Plot Data");
        file.add(plotMenu);

        //Add second menu item to main menu bar: "About"
        aboutMenu = new JRadioButtonMenuItem("About", null, false);

        // replace above line with: about = new JMenu("About") if we decide to add a submenu for about  
        menubar.add(aboutMenu);

        //Adds each menu to the actionlistener in the menuSelect class
        menuSelect menuSelect = new menuSelect();
        rosterMenu.addActionListener(menuSelect);
        attendanceMenu.addActionListener(menuSelect);
        saveMenu.addActionListener(menuSelect);
        plotMenu.addActionListener(menuSelect);
        aboutMenu.addActionListener(menuSelect);


    }

    //Class containing our actionPerformed method. This will determine what to do when
    //a user clicks on anything in the GUI
    private class menuSelect implements ActionListener{

        Component aPanel;
        Component aPlot;

        public void actionPerformed(ActionEvent event){

            String teamInfo = "";
            Table table = new Table();

            //Determines which menu was selected and performs whatever is put in the
            //if statement for that event

            //ROSTER SELECTED
            if(event.getSource() == rosterMenu){

                Roster roster = new Roster();
                rosterList = roster.loadRoster(rosterList);
                saveLocation = roster.getSavePath().toString();
                aPanel = table.getFullRoster(rosterList, header);
                frame.add(aPanel);
                frame.validate();
                frame.repaint();
                rosterLoaded = true;

            }
            //ATTENDANCE SELECTED
            else if(event.getSource() == attendanceMenu){
                //
                if(rosterLoaded){

                    Attendance attendance = new Attendance();
                    rosterList = attendance.loadAttendance(rosterList, header);
                    header = attendance.getHeader();

                    //code to display users that connected but were not on the roster
                    String attendanceMessage = attendance.getAttendanceMessage().toString();
                    JOptionPane.showMessageDialog(frame, attendanceMessage);

                    frame.remove(aPanel);
                    frame.revalidate();
                    frame.repaint();
                    aPanel = table.getFullRoster(rosterList, header);
                    frame.add(aPanel);
                    frame.validate();
                    frame.repaint(); 

                    //Once one attendance is loaded, plot can now be selected in the menu
                    plotReady = true;               

                }else{

                    //display message if no rosters have been added yet
                    JOptionPane.showMessageDialog(frame, "Wait! Try adding a roster first!");
                }
                
            }
            
            //SAVE SELECTED
            else if(event.getSource() == saveMenu){
                
                //if a roster has been loaded in, allow save function
                if(rosterLoaded){
                    Save save = new Save();
                    save.saveFile(saveLocation,rosterList, header); 
                }else{
                    JOptionPane.showMessageDialog(frame, "Nothing to save yet. Try loading a roster.");
                }
            }

            //PLOT SELECTED
            else if(event.getSource() == plotMenu){
                
                //if some attendance has been added, allow for a graph to be made
                if(plotReady){
                    Plot plot = new Plot();
                    aPlot = plot.plotGraph(rosterList, header);
                    JOptionPane.showMessageDialog(frame, aPlot);
                }else{
                    JOptionPane.showMessageDialog(frame, "Wait! Load some attendance to plot.");
                }
                
            }
            //ABOUT SELECTED: Displays team mates names.
            else if(event.getSource() == aboutMenu){   
                //Team names
                teamInfo = "Team:\nJacob\nKaden\nCathy\nCarson\nRnandaku";
                //Displays dialog box with team information
                JOptionPane.showMessageDialog(frame, teamInfo);
            }

        }
    }

    //Driver class for program
    public static void main(String[] args) {
        //calls Menu object to run instance of program
        new Menu();
    }

}