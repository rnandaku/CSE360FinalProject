import java.util.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;

public class Table { 

    private JTable table;
    private JScrollPane scroll; 

    //declare protected data structures for collumns and rows in JTable

    public Component getFullRoster(ArrayList<ArrayList<String>> roster, ArrayList<String> header){
       
        //Create JTable initial collumns
        table = new JTable(new DefaultTableModel(new Object[]{"id" , "First Name", "Last Name", "Program", "Level", "ASURITE"} , 0));
        table.setBounds(30,40,200,300);
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        //roster.add(0, header);


        for (int i = 6; i < header.size(); i++){
            model.addColumn(header.get(i));
        }

        //add rows from csv file 
        for (int i = 0; i < roster.size(); i++){
            model.addRow(roster.get(i).toArray());
        }

        //add JTable to JScrollPane and then repaint GUI
        scroll = new JScrollPane(table);
        return scroll;
    } 
}
