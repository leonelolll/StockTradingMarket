/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dashboard;
 import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
/**
 *
 * @author NADHEA ISMAIL
 */

  

public class ViolationCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        // Check if the value is "yes" in the "violation" column
        String violationValue = table.getValueAt(row, table.getColumn("violation").getModelIndex()).toString();
        if ("yes".equals(violationValue)) {
            // Set the background color for cells with "yes" value
            cell.setBackground(Color.RED);
            cell.setForeground(Color.WHITE);
        } else {
            // Reset the background color for other cells
            cell.setBackground(table.getBackground());
            cell.setForeground(table.getForeground());
        }
        
        return cell;
    }
}

