/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.me;

import java.awt.Component;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author root
 */
public class Office extends javax.swing.JApplet {

    private Connection con;
    private boolean Edit;
    private boolean EditT;
    private String Name;
    private String Password;
    private Vector rule = new Vector();
    private List<Vector> Edited = new ArrayList();
    private List<Vector> Empls = new ArrayList();
    private List<Vector<Vector>> Techs = new ArrayList<Vector<Vector>>();
    /**
     * Initializes the applet Office
     */
    @Override
    public void init() {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Office.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Office.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Office.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Office.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        this.setSize(600, 550);

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Office.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, ex);
        }
        try {
            con = DriverManager.getConnection("jdbc:postgresql://192.168.137.2:5432/db", "worker", "passwd");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT setauditall();");
            rs.next();
            rs.close();
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(Office.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, ex);
        }
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {

                public void run() {
                    initComponents();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        jComboBox1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox jBox = (JComboBox)e.getSource();
                if (jBox.getSelectedIndex()==-1) return;
                if (jBox.getSelectedIndex()==0) {
                    Model dtm = (Model) jTable1.getModel();
                    dtm.setDataVector(new Vector(20), new Vector(Arrays.asList("Employees")));
                    dtm.setRowCount(20);
                    jTable1.setModel(dtm);
                    jTable1.setEnabled(false);
                }
                else {
                    jTable1.setEnabled(true);
                    Model dtm = (Model) jTable1.getModel();
                    Object [] s = {"Employees"};
                    Object ob [][] = new Object[Empls.get(jComboBox1.getSelectedIndex()-1).size()][1];
                    for (int i=0; i<Empls.get(jBox.getSelectedIndex()-1).size(); i++)
                    {
                        ob[i][0] = Empls.get(jBox.getSelectedIndex()-1).get(i);
                    }
                    dtm.setDataVector(ob, s);
                    dtm.addRow(new Vector(Arrays.asList("...")));
                    if (dtm.getRowCount()<20) {
                        int c = dtm.getRowCount();
                        dtm.setRowCount(20);
                        for (int i=0; i<20;i++) {
                            if (i<c) dtm.setRowEditable(i, true);
                            else dtm.setRowEditable(i, false);
                        }
                    }
                    jTable1.setModel(dtm);
                }
            }
        });
        
        jComboBox2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox jBox = (JComboBox)e.getSource();
                if (jBox.getSelectedIndex()==-1) return;
                if (jBox.getSelectedIndex()==0) {
                    Model dtm = (Model) jTable2.getModel();
                    dtm.setDataVector(new Vector(20), new Vector(Arrays.asList("Technique","Count")));
                    dtm.setRowCount(20);
                    jTable2.setModel(dtm);
                    jTable2.setEnabled(false);
                }
                else {
                    jTable2.setEnabled(true);
                    Model dtm = (Model) jTable2.getModel();
                    Techs.get(jBox.getSelectedIndex()-1).get(0).size();
                    Object [] s = {"Technique","Count"};
                    Object ob [][] = new Object[Techs.get(jBox.getSelectedIndex()-1).get(0).size()][2];
                    for (int i=0; i<Techs.get(jBox.getSelectedIndex()-1).get(0).size(); i++)
                    {
                        ob[i][0] = Techs.get(jBox.getSelectedIndex()-1).get(0).get(i);
                        ob[i][1] = Techs.get(jBox.getSelectedIndex()-1).get(1).get(i);
                    }
                    dtm.setDataVector(ob, s);
                    dtm.addRow(new Vector(Arrays.asList("...","...")));
                    if (dtm.getRowCount()<20) {
                        int c = dtm.getRowCount();
                        dtm.setRowCount(20);
                        for (int i=0; i<20;i++) {
                            if (i<c) dtm.setRowEditable(i, true);
                            else dtm.setRowEditable(i, false);
                        }
                    }
                    jTable2.setModel(dtm);
                }
            }
        });
        
        jTable1.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (!jTable1.isEnabled()) return;
                if (e.getClickCount()==1) { 
                    Model m = (Model) jTable1.getModel();
                    m.setCellEditable(jTable1.getSelectedRow(), jTable1.getSelectedColumn(), false);
                    jTable1.setModel(m);
                }
                if (e.getClickCount()==2) {
                    if (!(boolean)rule.get(0) && !(boolean)rule.get(4)) return;
                    if ((boolean)rule.get(4)) {
                        if (!jTable1.getValueAt(jTable1.getSelectedRow(), jTable1.getSelectedColumn()).toString().equals(Name)) return;
                    }
                    if (jTable1.getSelectedRow()>Empls.get(jComboBox1.getSelectedIndex()-1).size()) return;
                    Model m = (Model) jTable1.getModel();
                    m.setCellEditable(jTable1.getSelectedRow(), jTable1.getSelectedColumn(), true);
                    jTable1.setModel(m);
                    if (jTable1.getSelectedRow()==Empls.get(jComboBox1.getSelectedIndex()-1).size()) {
                        Edit = false;
                        jTable1.setValueAt("", jTable1.getSelectedRow(), jTable1.getSelectedColumn());
                    }
                    jTable1.setModel(m);
                    jTable1.editCellAt(jTable1.getSelectedRow(), jTable1.getSelectedColumn());
                    Component editor = jTable1.getEditorComponent();
                    editor.requestFocus();
                    Edit = true;
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        
        jTable2.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (jComboBox2.getSelectedIndex()==0) return;
                if (e.getClickCount()==1) { 
                    Model m = (Model) jTable2.getModel();
                    m.setRowEditable(jTable2.getSelectedRow(), false);
                    jTable2.setModel(m);
                }
                if (e.getClickCount()==2) {
                    if (!(boolean)rule.get(2)) {
                        if (jTable2.getSelectedRow()==Techs.get(jComboBox2.getSelectedIndex()-1).get(0).size()) return;
                    }
                    if (!(boolean)rule.get(3)) {
                        if (jTable2.getSelectedRow()<Techs.get(jComboBox2.getSelectedIndex()-1).get(0).size()) return;
                    }
                    if (jTable2.getSelectedRow()>=Techs.get(jComboBox2.getSelectedIndex()-1).get(0).size()) return;
                    Model m = (Model) jTable2.getModel();
                    m.setRowEditable(jTable2.getSelectedRow(), true);
                    jTable2.setModel(m);
/*
                    if (jTable2.getSelectedRow()==Techs.get(jComboBox2.getSelectedIndex()-1).get(0).size()) {
                        EditT = false;
                        jTable2.setValueAt("", jTable2.getSelectedRow(), jTable2.getSelectedColumn());
                    }
*/                    
                    jTable2.setModel(m);
                    jTable2.editCellAt(jTable2.getSelectedRow(), jTable2.getSelectedColumn());
                    Component editor = jTable2.getEditorComponent();
                    editor.requestFocus();
                    EditT = true;
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        
        Model m = (Model) jTable1.getModel();
        m.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                if ((jComboBox1.getSelectedIndex()==0) || (jTable1.getSelectedRow()==-1) || (jTable1.getSelectedColumn()==-1)) return;
                if (jTable1.getSelectedRow()==Empls.get(jComboBox1.getSelectedIndex()-1).size()) {
                    if (jTable1.getValueAt(jTable1.getSelectedRow(), jTable1.getSelectedColumn()).equals("") && Edit) {
                        Edit = false;
                        jTable1.setValueAt("...", jTable1.getSelectedRow(), jTable1.getSelectedColumn());
                        Edit = true;
                    }
                    else {
                        if (!jTable1.getValueAt(jTable1.getSelectedRow(), jTable1.getSelectedColumn()).equals("") && Edit) {
                            for (int i=0; i<Empls.get(jComboBox1.getSelectedIndex()-1).size(); i++) {
                                if (Empls.get(jComboBox1.getSelectedIndex()-1).get(i).toString().equals(jTable1.getValueAt(jTable1.getSelectedRow(), jTable1.getSelectedColumn()))) {
                                    JOptionPane.showMessageDialog(rootPane, "Such item already exists!", "Office", JOptionPane.ERROR_MESSAGE);
                                    Edit = false;
                                    jTable1.setValueAt("...", jTable1.getSelectedRow(), jTable1.getSelectedColumn());
                                    Edit = true;
                                    jTable1.requestFocus();
                                    return;
                                }
                            }
                            if (jTable1.getValueAt(jTable1.getSelectedRow(), jTable1.getSelectedColumn()).toString().startsWith(".")) {
                                Edit = false;
                                jTable1.setValueAt("...", jTable1.getSelectedRow(), jTable1.getSelectedColumn());
                                Edit = true;
                                return;
                            }
                            if (Empls.get(jComboBox1.getSelectedIndex()-1).size()>=19) {
                                Edit = false;
                                Model m = (Model) jTable1.getModel();
                                m.addRow(new Vector());
                                jTable1.setModel(m);
                            }
                            Edit = false;
                            jTable1.setValueAt("...", jTable1.getSelectedRow()+1, jTable1.getSelectedColumn());
                            Edit = true;
                            String passwd;
                            JPasswordField pass = new JPasswordField();
                            pass.setEchoChar('*');
                            Object[] obj = {"Please enter password:\n\n", pass};
                            Object stringArray[] = {"OK"};
                            boolean b = false;
                            while (!b) {
                                if (JOptionPane.showOptionDialog(rootPane, obj, "Need password", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, stringArray, obj)==JOptionPane.YES_OPTION) {
                                    if (!pass.getText().isEmpty()) b = true;
                                }
                            }
                            passwd = pass.getText();
                            jButton2.setEnabled(true);
                            jButton3.setEnabled(true);
                            Edited.add(new Vector(Arrays.asList(1, "jTable1", jComboBox1.getSelectedIndex(), jTable1.getValueAt(jTable1.getSelectedRow(), jTable1.getSelectedColumn()), passwd, jComboBox1.getItemAt(jComboBox1.getSelectedIndex()))));
                            Empls.get(jComboBox1.getSelectedIndex()-1).add(jTable1.getValueAt(jTable1.getSelectedRow(), jTable1.getSelectedColumn()));
                            return;
                        }
                    }
                }
                if (jTable1.getSelectedRow()<Empls.get(jComboBox1.getSelectedIndex()-1).size()) {
                    if (jTable1.getValueAt(jTable1.getSelectedRow(), jTable1.getSelectedColumn()).equals("")) {
                        jTable1.setValueAt(Empls.get(jComboBox1.getSelectedIndex()-1).get(jTable1.getSelectedRow()), jTable1.getSelectedRow(), jTable1.getSelectedColumn());
                        return;
                    }
                    if (jTable1.getValueAt(jTable1.getSelectedRow(), jTable1.getSelectedColumn()).equals(Empls.get(jComboBox1.getSelectedIndex()-1).get(jTable1.getSelectedRow()).toString())) {
                        return;
                    }
                    for (int i=0; i<Empls.get(jComboBox1.getSelectedIndex()-1).size(); i++) {
                        if (Empls.get(jComboBox1.getSelectedIndex()-1).get(i).toString().equals(jTable1.getValueAt(jTable1.getSelectedRow(), jTable1.getSelectedColumn()))) {
                            JOptionPane.showMessageDialog(rootPane, "Such item already exists!", "Office", JOptionPane.ERROR_MESSAGE);
                            jTable1.requestFocus();
                            jTable1.setValueAt(Empls.get(jComboBox1.getSelectedIndex()-1).get(jTable1.getSelectedRow()), jTable1.getSelectedRow(), jTable1.getSelectedColumn());
                            return;
                        }
                    }
                    if (jTable1.getValueAt(jTable1.getSelectedRow(), jTable1.getSelectedColumn()).toString().startsWith(".")) {
                        jTable1.setValueAt(Empls.get(jComboBox1.getSelectedIndex()-1).get(jTable1.getSelectedRow()), jTable1.getSelectedRow(), jTable1.getSelectedColumn());
                        return;
                    }
                    jButton2.setEnabled(true);
                    jButton3.setEnabled(true);
                    Edited.add(new Vector(Arrays.asList(2, "jTable1", jComboBox1.getSelectedIndex(), jTable1.getSelectedRow(), Empls.get(jComboBox1.getSelectedIndex()-1).get(jTable1.getSelectedRow()), jTable1.getValueAt(jTable1.getSelectedRow(), jTable1.getSelectedColumn()))));
                    Empls.get(jComboBox1.getSelectedIndex()-1).set(jTable1.getSelectedRow(), jTable1.getValueAt(jTable1.getSelectedRow(), jTable1.getSelectedColumn()));
                }
            }
        });
        jTable1.setModel(m);
        
        m=(Model) jTable2.getModel();
        m.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                if ((jComboBox2.getSelectedIndex()==0) || (jTable2.getSelectedRow()==-1) || (jTable2.getSelectedColumn()==-1)) return;
                if (jTable2.getSelectedRow()==Techs.get(jComboBox2.getSelectedIndex()-1).get(0).size()) {
                    if (EditT && jTable2.getValueAt(jTable2.getSelectedRow(), 0).equals("")) {
                        EditT = false;
                        jTable2.setValueAt("...", jTable2.getSelectedRow(), jTable2.getEditingColumn());
                        EditT = true;
                    }
                    else {
                        if (EditT && !jTable2.getValueAt(jTable2.getSelectedRow(), 0).equals("")) {

                            for (int i=0; i<Techs.get(jComboBox2.getSelectedIndex()-1).get(0).size(); i++) {
                                if (Techs.get(jComboBox2.getSelectedIndex()-1).get(0).get(i).toString().equals(jTable2.getValueAt(jTable2.getSelectedRow(), 0))) {
                                    JOptionPane.showMessageDialog(rootPane, "Such item already exists!", "Office", JOptionPane.ERROR_MESSAGE);
                                    EditT = false;
                                    jTable2.setValueAt("...", jTable2.getSelectedRow(), 0);
                                    EditT = true;
                                    jTable2.requestFocus();
                                    return;
                                }
                            }
                            if (jTable2.getValueAt(jTable2.getSelectedRow(), jTable2.getSelectedColumn()).toString().startsWith(".")) {
                                EditT = false;
                                jTable2.setValueAt("...", jTable2.getSelectedRow(), 0);
                                EditT = true;
                                return;
                            }

                            if (jTable2.getEditingColumn()==1) {
                                try {
                                    Integer.parseInt(jTable2.getValueAt(jTable2.getSelectedRow(), 1).toString());
                                    if (Techs.get(jComboBox2.getSelectedIndex()-1).get(0).size()>=19) {
                                        EditT = false;
                                        Model m = (Model) jTable2.getModel();
                                        m.addRow(new Vector());
                                        jTable2.setModel(m);
                                    }
                                    EditT = false;
                                    jTable2.setValueAt("...", jTable2.getSelectedRow()+1, 0);
                                    jTable2.setValueAt("...", jTable2.getSelectedRow()+1, 1);
                                    EditT = true;
                                    jButton2.setEnabled(true);
                                    jButton3.setEnabled(true);
                                    Edited.add(new Vector(Arrays.asList(1, "jTable2", jComboBox2.getSelectedIndex(), jTable2.getValueAt(jTable2.getSelectedRow(), 0), jTable2.getValueAt(jTable2.getSelectedRow(), 1), jComboBox2.getItemAt(jComboBox2.getSelectedIndex()))));
                                    Techs.get(jComboBox2.getSelectedIndex()-1).get(0).add(jTable2.getValueAt(jTable2.getSelectedRow(), 0));
                                    Techs.get(jComboBox2.getSelectedIndex()-1).get(1).add(jTable2.getValueAt(jTable2.getSelectedRow(), 1));
                                }
                                catch(Exception ex) {
                                    EditT = false;
                                    jTable2.setValueAt("...", jTable2.getSelectedRow(), 1);
                                    JOptionPane.showMessageDialog(rootPane, "Enter correct count!", "Office", JOptionPane.ERROR_MESSAGE);
                                    EditT = true;
                                }
                            }
                        }
                    }
                }
                if (jTable2.getSelectedRow()<Techs.get(jComboBox2.getSelectedIndex()-1).get(0).size()) {
                   if (jTable2.getValueAt(jTable2.getSelectedRow(), jTable2.getSelectedColumn()).equals("")) {
                        jTable2.setValueAt(Techs.get(jComboBox2.getSelectedIndex()-1).get(jTable2.getSelectedColumn()).get(jTable2.getSelectedRow()), jTable2.getSelectedRow(), jTable2.getSelectedColumn());
                        return;
                    }
                    if (jTable2.getValueAt(jTable2.getSelectedRow(), jTable2.getSelectedColumn()).equals(Techs.get(jComboBox2.getSelectedIndex()-1).get(jTable2.getSelectedColumn()).get(jTable2.getSelectedRow()).toString())) {
                        return;
                    }
                    if (jTable2.getSelectedColumn()==1) {
                        try {
                            Integer.parseInt(jTable2.getValueAt(jTable2.getSelectedRow(), 1).toString());
                        }
                        catch(Exception ex) {
                            jTable2.setValueAt(Techs.get(jComboBox2.getSelectedIndex()-1).get(1).get(jTable2.getSelectedRow()), jTable2.getSelectedRow(), 1);
                            return;
                        }
                    }
                    for (int i=0; i<Techs.get(jComboBox2.getSelectedIndex()-1).get(0).size(); i++) {
                        if (Techs.get(jComboBox2.getSelectedIndex()-1).get(jTable2.getSelectedColumn()).get(i).toString().equals(jTable2.getValueAt(jTable2.getSelectedRow(), jTable2.getSelectedColumn()))) {
                            JOptionPane.showMessageDialog(rootPane, "Such item already exists!", "Office", JOptionPane.ERROR_MESSAGE);
                            EditT = false;
                            jTable2.setValueAt(Techs.get(jComboBox2.getSelectedIndex()-1).get(jTable2.getSelectedColumn()).get(i), jTable2.getSelectedRow(), jTable2.getSelectedColumn());
                            EditT = true;
                            jTable2.requestFocus();
                            return;
                        }
                    }
                    if (jTable2.getValueAt(jTable2.getSelectedRow(), jTable2.getSelectedColumn()).toString().startsWith(".")) {
                        jTable2.setValueAt(Techs.get(jComboBox2.getSelectedIndex()-1).get(jTable2.getSelectedColumn()).get(jTable2.getSelectedRow()), jTable2.getSelectedRow(), jTable2.getSelectedColumn());
                        return;
                    }
                    jButton2.setEnabled(true);
                    jButton3.setEnabled(true);
                    Edited.add(new Vector(Arrays.asList(2, "jTable2", jComboBox2.getSelectedIndex(), jTable2.getSelectedRow(), jTable2.getSelectedColumn(), Techs.get(jComboBox2.getSelectedIndex()-1).get(jTable2.getSelectedColumn()).get(jTable2.getSelectedRow()), jTable2.getValueAt(jTable2.getSelectedRow(), jTable2.getSelectedColumn()), jTable2.getValueAt(jTable2.getSelectedRow(), 0))));
                    Techs.get(jComboBox2.getSelectedIndex()-1).get(jTable2.getSelectedColumn()).set(jTable2.getSelectedRow(), jTable2.getValueAt(jTable2.getSelectedRow(), jTable2.getSelectedColumn()));
                }
            }
        });
        jTable2.setModel(m);

        jTable1.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "key-enter");
        jTable1.getActionMap().put("key-enter", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (jTable1.isEditing()) jTable1.getCellEditor().stopCellEditing();
                else {
                    if (!(boolean)rule.get(0)) return;
                    if (jTable1.getSelectedRow()>Empls.get(jComboBox1.getSelectedIndex()-1).size()) return;
                    Model m = (Model) jTable1.getModel();
                    m.setCellEditable(jTable1.getSelectedRow(), jTable1.getSelectedColumn(), true);
                    Edit = false;
                    if (jTable1.getSelectedRow()==Empls.get(jComboBox1.getSelectedIndex()-1).size()) {
                        jTable1.setValueAt("", jTable1.getSelectedRow(), jTable1.getSelectedColumn());
                    }
                    jTable1.setModel(m);
                    jTable1.editCellAt(jTable1.getSelectedRow(), jTable1.getSelectedColumn());
                    Component editor = jTable1.getEditorComponent();
                    editor.requestFocus();
                    Edit = true;
                }
            }
        });
        
        jTable2.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "key-enter");
        jTable2.getActionMap().put("key-enter", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                if (jTable2.isEditing()) {
                    jTable2.getCellEditor().stopCellEditing();
                    if (jTable2.getSelectedRow()==Techs.get(jComboBox2.getSelectedIndex()-1).get(0).size()) {
                        if (!jTable2.getValueAt(jTable2.getSelectedRow(), 0).equals("...")) {
                            if (jTable2.getValueAt(jTable2.getSelectedRow(), 1).equals("...")) {
                                EditT = false;
                                jTable2.setValueAt("", jTable2.getSelectedRow(), 1);
                                jTable2.editCellAt(jTable2.getSelectedRow(), 1);
                                EditT = true;
                            }
                        }
                    }
                }
                else {
                    if (!(boolean)rule.get(2)) {
                        if (jTable2.getSelectedRow()==Techs.get(jComboBox2.getSelectedIndex()-1).get(0).size()) return;
                    }
                    if (!(boolean)rule.get(3)) {
                        if (jTable2.getSelectedRow()<Techs.get(jComboBox2.getSelectedIndex()-1).get(0).size()) return;
                    }
                    if (jTable2.getSelectedRow()>Techs.get(jComboBox2.getSelectedIndex()-1).get(0).size()) return;
                    Model m = (Model) jTable2.getModel();
                    m.setRowEditable(jTable2.getSelectedRow(), true);
                    EditT = false;
                    if (jTable2.getSelectedRow()==Techs.get(jComboBox2.getSelectedIndex()-1).get(0).size()) {
                        jTable2.setValueAt("", jTable2.getSelectedRow(), 0);
                        jTable2.editCellAt(jTable2.getSelectedRow(), 0);
                        jTable2.getEditorComponent().requestFocus();
                    }
                    jTable2.setModel(m);
                    if (jTable2.getSelectedRow()<Techs.get(jComboBox2.getSelectedIndex()-1).get(0).size()) {
                        jTable2.editCellAt(jTable2.getSelectedRow(), jTable2.getSelectedColumn());
                        jTable2.getEditorComponent().requestFocus();
                    }
                    else {
                        jTable2.editCellAt(jTable2.getSelectedRow(), 0);
                    }
                    EditT = true;
                }
            }
        });
        jTable1.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "key-delete");
        jTable1.getActionMap().put("key-delete", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (jTable1.getSelectedRow()!=-1 && jTable1.getSelectedColumn()!=-1) {
                    if (!jTable1.isEditing() && !jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString().equals("...")) {
                        if (jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString().equals(Name)) {
                            JOptionPane.showMessageDialog(rootPane, "You can't delete yourself!", "Office", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        if (!(boolean)rule.get(0)) return;
                        jButton2.setEnabled(true);
                        jButton3.setEnabled(true);
                        Edited.add(new Vector(Arrays.asList(3, "jTable1", jComboBox1.getSelectedIndex(), jTable1.getSelectedRow(), jTable1.getValueAt(jTable1.getSelectedRow(), jTable1.getSelectedColumn()))));
                        Model m = (Model) jTable1.getModel();
                        int i = jTable1.getSelectedRow();
                        m.removeRow(jTable1.getSelectedRow());
                        Empls.get(jComboBox1.getSelectedIndex()-1).remove(i);
                        if (m.getRowCount()<20) m.setRowCount(20);
                        jTable1.setModel(m);
                        jTable1.setValueAt("...", i, jTable1.getSelectedColumn());
                    }
                }
            }
        });
        
        jTable2.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "key-delete");
        jTable2.getActionMap().put("key-delete", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (jTable2.getSelectedRow()!=-1 && jTable2.getSelectedColumn()!=-1) {
                    if (!jTable2.isEditing() && !jTable2.getValueAt(jTable2.getSelectedRow(), 0).equals("...")) {
                        if (!(boolean)rule.get(2)) {
                            if (jTable2.getSelectedRow()==Techs.get(jComboBox2.getSelectedIndex()-1).get(0).size()) return;
                        }
                        jButton2.setEnabled(true);
                        jButton3.setEnabled(true);
                        Edited.add(new Vector(Arrays.asList(3, "jTable2", jComboBox2.getSelectedIndex(), jTable2.getSelectedRow(), jTable2.getValueAt(jTable2.getSelectedRow(), 0), jTable2.getValueAt(jTable2.getSelectedRow(), 1))));
                        Model m = (Model) jTable2.getModel();
                        int i = jTable2.getSelectedRow();
                        m.removeRow(jTable2.getSelectedRow());
                        Techs.get(jComboBox2.getSelectedIndex()-1).get(0).remove(i);
                        Techs.get(jComboBox2.getSelectedIndex()-1).get(1).remove(i);
                        if (m.getRowCount()<20) m.setRowCount(20);
                        jTable2.setModel(m);
                        jTable2.setValueAt("...", i, 0);
                        jTable2.setValueAt("...", i, 1);
                    }
                }
            }
        });
        
        jTable1.setShowVerticalLines(true);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable2.setShowVerticalLines(true);
        jTable2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        jLayeredPane1.setVisible(false);
   }
    
    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane2 = new javax.swing.JLayeredPane();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox();
        jComboBox1 = new javax.swing.JComboBox();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setText("Enter Name and Password");
        jLabel1.setBounds(190, 150, 220, 19);
        jLayeredPane2.add(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLabel2.setText("Name:");
        jLabel2.setBounds(150, 180, 31, 30);
        jLayeredPane2.add(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLabel3.setText("Password:");
        jLabel3.setToolTipText("");
        jLabel3.setBounds(150, 220, 80, 30);
        jLayeredPane2.add(jLabel3, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jTextField1.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTextField1CaretUpdate(evt);
            }
        });
        jTextField1.setBounds(240, 180, 200, 30);
        jLayeredPane2.add(jTextField1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jButton10.setText("Login");
        jButton10.setEnabled(false);
        jButton10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton10MouseClicked(evt);
            }
        });
        jButton10.setBounds(360, 260, 80, 30);
        jLayeredPane2.add(jButton10, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jPasswordField1.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jPasswordField1CaretUpdate(evt);
            }
        });
        jPasswordField1.setBounds(240, 220, 200, 30);
        jLayeredPane2.add(jPasswordField1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jButton8.setText("Cancel last action");
        jButton8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton8MouseClicked(evt);
            }
        });
        jButton8.setBounds(10, 500, 117, 23);
        jLayeredPane1.add(jButton8, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jButton9.setText("Change my password");
        jButton9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton9MouseClicked(evt);
            }
        });
        jButton9.setBounds(10, 20, 135, 23);
        jLayeredPane1.add(jButton9, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jButton6.setText("Delete group");
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton6MouseClicked(evt);
            }
        });
        jButton6.setBounds(120, 450, 103, 23);
        jLayeredPane1.add(jButton6, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jButton7.setText("Delete group");
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton7MouseClicked(evt);
            }
        });
        jButton7.setBounds(420, 450, 172, 23);
        jLayeredPane1.add(jButton7, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jButton4.setText("Add group");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });
        jButton4.setBounds(10, 450, 103, 23);
        jLayeredPane1.add(jButton4, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jButton5.setText("Add group");
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton5MouseClicked(evt);
            }
        });
        jButton5.setBounds(240, 450, 172, 23);
        jLayeredPane1.add(jButton5, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<Choose Technique Group>" }));
        jComboBox2.setBounds(240, 60, 348, 20);
        jLayeredPane1.add(jComboBox2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<Choose Staff Group>" }));
        jComboBox1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBox1MouseClicked(evt);
            }
        });
        jComboBox1.setBounds(10, 60, 212, 20);
        jLayeredPane1.add(jComboBox1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jButton3.setText("Cancel");
        jButton3.setEnabled(false);
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });
        jButton3.setBounds(430, 500, 65, 23);
        jLayeredPane1.add(jButton3, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jTable1.setModel(new Model(
            new Object [][] {
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Employees"
            }
        ));
        jTable1.setEnabled(false);
        jScrollPane1.setViewportView(jTable1);

        jScrollPane1.setBounds(10, 90, 212, 348);
        jLayeredPane1.add(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jTable2.setModel(new Model(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Technique", "Count"
            }
        ));
        jTable2.setEnabled(false);
        jScrollPane2.setViewportView(jTable2);

        jScrollPane2.setBounds(240, 90, 348, 348);
        jLayeredPane1.add(jScrollPane2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jButton2.setText("Apply");
        jButton2.setEnabled(false);
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        jButton2.setBounds(520, 500, 59, 23);
        jLayeredPane1.add(jButton2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jButton1.setText("Logout");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.setBounds(490, 20, 65, 23);
        jLayeredPane1.add(jButton1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 606, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLayeredPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLayeredPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        jLayeredPane1.setVisible(false);
        jTextField1.setText("");
        jPasswordField1.setText("");
        jLayeredPane2.setVisible(true);
        Empls.clear();
        Techs.clear();
        Edited.clear();
        jComboBox1.setSelectedIndex(-1);
        jComboBox2.setSelectedIndex(-1);
        jComboBox1.removeAllItems();
        jComboBox1.addItem("<Choose Staff Group>");
        jComboBox2.removeAllItems();
        jComboBox2.addItem("<Choose Technique Group>");
        jButton2.setEnabled(false);
        jButton3.setEnabled(false);
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        // TODO add your handling code here:
        if (!jButton2.isEnabled()) return;
        for (int i=0;i<Edited.size();i++) {
            switch ((int)Edited.get(i).get(0)) {
                case 1: {
                    switch (Edited.get(i).get(1).toString()) {
                        case "jTable1": {
                            try {
                                Statement st = con.createStatement();
                                ResultSet rs = st.executeQuery("SELECT id_sg FROM sgroups WHERE name='"+Edited.get(i).get(5)+"';");
                                rs.next();
                                st.executeUpdate("INSERT INTO staff (id_sg, name, password) VALUES ("+rs.getInt("id_sg")+",'"+Edited.get(i).get(3)+"','"+Edited.get(i).get(4)+"')");
                                rs.close();
                                st.close();
                            } catch (SQLException ex) {
                                Logger.getLogger(Office.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                        }
                        case "jTable2": {
                            try {
                                Statement st = con.createStatement();
                                ResultSet rs = st.executeQuery("SELECT id_gt FROM tgroups WHERE name='"+Edited.get(i).get(5)+"';");
                                rs.next();
                                st.executeUpdate("INSERT INTO techs (id_gt, name, numbs) VALUES ("+rs.getInt("id_gt")+",'"+Edited.get(i).get(3)+"',"+Edited.get(i).get(4)+")");
                                rs.close();
                                st.close();
                            } catch (SQLException ex) {
                                Logger.getLogger(Office.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                        }
                    }
                    break;
                }
                case 2: {
                    switch (Edited.get(i).get(1).toString()) {
                        case "jTable1": {
                            try {
                                Statement st = con.createStatement();
                                st.executeUpdate("UPDATE staff SET name='"+Edited.get(i).get(4)+"' WHERE name='"+Edited.get(i).get(5)+"';");
                                st.close();
                            } catch (SQLException ex) {
                                Logger.getLogger(Office.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                        }
                        case "jTable2": {
                            try {
                                Statement st = con.createStatement();
                                if ((int)Edited.get(i).get(4)==0) {
                                    st.executeUpdate("UPDATE techs SET name='"+Edited.get(i).get(6)+"' WHERE name='"+Edited.get(i).get(5)+"';");
                                }
                                if ((int)Edited.get(i).get(4)==1) {
                                    st.executeUpdate("UPDATE techs SET numbs='"+Edited.get(i).get(7)+"' WHERE name='"+Edited.get(i).get(5)+"';");
                                }
                                st.close();
                            } catch (SQLException ex) {
                                Logger.getLogger(Office.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                        }
                    }
                    break;
                }
                case 3: {
                    switch (Edited.get(i).get(1).toString()) {
                        case "jTable1": {
                            try {
                                Statement st = con.createStatement();
                                st.executeUpdate("DELETE FROM staff WHERE name='"+Edited.get(i).get(4)+"';");
                                st.close();
                            } catch (SQLException ex) {
                                Logger.getLogger(Office.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                        }
                        case "jTable2": {
                            try {
                                Statement st = con.createStatement();
                                st.executeUpdate("DELETE FROM techs WHERE name='"+Edited.get(i).get(4)+"';");
                                st.close();
                            } catch (SQLException ex) {
                                Logger.getLogger(Office.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                        }
                    }
                    break;
                }
                case 4: {
                    switch (Edited.get(i).get(1).toString()) {
                        case "jTable1": {
                            try {
                                Statement st = con.createStatement();
                                st.executeUpdate("INSERT INTO sgroups(name) VALUES ('"+Edited.get(i).get(2)+"');");
                                ResultSet rs = st.executeQuery("SELECT id_sg FROM sgroups WHERE name='"+Edited.get(i).get(2)+"';");
                                rs.next();
                                int index = rs.getInt("id_sg");
                                st.executeUpdate("INSERT INTO rules(id_sg) VALUES ("+index+");");
                                rs.close();
                                st.close();
                            } catch (SQLException ex) {
                                Logger.getLogger(Office.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                        }
                        case "jTable2": {
                            try {
                                Statement st = con.createStatement();
                                st.executeUpdate("INSERT INTO tgroups(name) VALUES ('"+Edited.get(i).get(2)+"');");
                                st.close();
                            } catch (SQLException ex) {
                                Logger.getLogger(Office.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                        }
                    }
                    break;
                }
                case 5: {
                    switch (Edited.get(i).get(1).toString()) {
                        case "jTable1": {
                            try {
                                Statement st = con.createStatement();
                                ResultSet rs = st.executeQuery("SELECT id_sg FROM sgroups WHERE name='"+Edited.get(i).get(3)+"';");
                                rs.next();
                                int n = rs.getInt("id_sg");
                                rs.close();
                                st.execute("DELETE FROM sgroups WHERE id_sg="+n);
                                st.close();
                            } catch (SQLException ex) {
                                Logger.getLogger(Office.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                        }
                        case "jTable2": {
                            try {
                                Statement st = con.createStatement();
                                ResultSet rs = st.executeQuery("SELECT id_gt FROM tgroups WHERE name='"+Edited.get(i).get(3)+"';");
                                rs.next();
                                int n = rs.getInt("id_gt");
                                rs.close();
                                st.executeUpdate("DELETE FROM tgroups WHERE id_gt="+n);
                                st.close();
                            } catch (SQLException ex) {
                                Logger.getLogger(Office.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                        }
                    }
                    break;
                }
            }
        }
        Edited.clear();
        jButton2.setEnabled(false);
        jButton3.setEnabled(false);
    }//GEN-LAST:event_jButton2MouseClicked

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        // TODO add your handling code here:
        if (!jButton3.isEnabled()) return;
        while (Edited.size()>0) {
            jButton8MouseClicked(evt);
        }
        jButton2.setEnabled(false);
        jButton3.setEnabled(false);
    }//GEN-LAST:event_jButton3MouseClicked

    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseClicked
        // TODO add your handling code here:
        if (!jButton4.isEnabled()) return;
        String N = JOptionPane.showInputDialog(rootPane,"Enter group name:", "New group", JOptionPane.QUESTION_MESSAGE);
        if (N==null || N.equals("")) return;
        for (int i=0; i<jComboBox1.getItemCount();i++) {
            if (jComboBox1.getItemAt(i).toString().equals(N)) {
                JOptionPane.showMessageDialog(rootPane, "Such group already exists!", "Adding", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        jButton2.setEnabled(true);
        jButton3.setEnabled(true);
        Edited.add(new Vector(Arrays.asList(4, "jTable1", N)));
        Empls.add(new Vector());
        jComboBox1.addItem(N);
    }//GEN-LAST:event_jButton4MouseClicked

    private void jButton5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseClicked
        // TODO add your handling code here:
        if (!jButton5.isEnabled()) return;
        String N = JOptionPane.showInputDialog(rootPane,"Enter group name:", "New group", JOptionPane.QUESTION_MESSAGE);
        if (N==null || N.equals("")) return;
        for (int i=0; i<jComboBox2.getItemCount();i++) {
            if (jComboBox2.getItemAt(i).toString().equals(N)) {
                JOptionPane.showMessageDialog(rootPane, "Such group already exists!", "Adding", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        jButton2.setEnabled(true);
        jButton3.setEnabled(true);
        Edited.add(new Vector(Arrays.asList(4, "jTable2", N)));
        Techs.add(new Vector(Arrays.asList(new Vector(), new Vector())));
        jComboBox2.addItem(N);
    }//GEN-LAST:event_jButton5MouseClicked

    private void jButton6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseClicked
        // TODO add your handling code here:
        if (!jButton6.isEnabled()) return;
        if (jComboBox1.getItemCount()<2) {
            JOptionPane.showMessageDialog(rootPane, "Nothing to delete!", "Deleting", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (int i=0; i<Empls.get(jComboBox1.getSelectedIndex()-1).size();i++) {
            if (Empls.get(jComboBox1.getSelectedIndex()-1).get(i).toString().equals(Name)) {
                JOptionPane.showMessageDialog(rootPane, "You can't delete your group!", "Deleting", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        jButton2.setEnabled(true);
        jButton3.setEnabled(true);
        Edited.add(new Vector(Arrays.asList(5, "jTable1", jComboBox1.getSelectedIndex()-1, jComboBox1.getItemAt(jComboBox1.getSelectedIndex()), Empls.get(jComboBox1.getSelectedIndex()-1))));
        Empls.remove(jComboBox1.getSelectedIndex()-1);
        jComboBox1.removeItemAt(jComboBox1.getSelectedIndex());
    }//GEN-LAST:event_jButton6MouseClicked

    private void jButton7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseClicked
        // TODO add your handling code here:
        if (!jButton7.isEnabled()) return;
        if (jComboBox2.getItemCount()<2) {
            JOptionPane.showMessageDialog(rootPane, "Nothing to delete!", "Deleting", JOptionPane.ERROR_MESSAGE);
            return;
        }
        jButton2.setEnabled(true);
        jButton3.setEnabled(true);
        Edited.add(new Vector(Arrays.asList(5, "jTable2", jComboBox2.getSelectedIndex()-1, jComboBox2.getItemAt(jComboBox2.getSelectedIndex()), Techs.get(jComboBox2.getSelectedIndex()-1))));
        Techs.remove(jComboBox2.getSelectedIndex()-1);
        jComboBox2.removeItemAt(jComboBox2.getSelectedIndex());
    }//GEN-LAST:event_jButton7MouseClicked

    private void jButton8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MouseClicked
        // TODO add your handling code here:
        if (Edited.size()==0) {
            JOptionPane.showMessageDialog(rootPane, "Nothing to cancel!", "Editing", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int i = Edited.size()-1;
        switch ((int)Edited.get(i).get(0)) {
            case 1: {
                switch (Edited.get(i).get(1).toString()) {
                    case "jTable1": {
                        int n = (int)Edited.get(i).get(2) - 1;
                        Empls.get(n).remove(Empls.get(n).size()-1);
                        if (jComboBox1.getSelectedIndex()==n+1) {
                            Model m = (Model) jTable1.getModel();
                            m.removeRow(Empls.get(n).size());
                            if (m.getRowCount()<20) m.setRowCount(20);
                            jTable1.setModel(m);
                        }
                        break;
                    }
                    case "jTable2": {
                        int n = (int)Edited.get(i).get(2) - 1;
                        Techs.get(n).get(0).remove(Techs.get(n).get(0).size()-1);
                        Techs.get(n).get(1).remove(Techs.get(n).get(1).size()-1);
                        if (jComboBox2.getSelectedIndex()==n+1) {
                            Model m = (Model) jTable2.getModel();
                            m.removeRow(Techs.get(n).get(0).size());
                            if (m.getRowCount()<20) m.setRowCount(20);
                            jTable2.setModel(m);
                        }
                        break;
                    }
                }
                break;
            }
            case 2: {
                switch (Edited.get(i).get(1).toString()) {
                    case "jTable1": {
                        int n = (int)Edited.get(i).get(2) - 1;
                        int m = (int)Edited.get(i).get(3);
                        String s = Edited.get(i).get(4).toString();
                        Empls.get(n).set(m, s);
                        if (jComboBox1.getSelectedIndex()==n+1) {
                            jTable1.setValueAt(s, m, 0);
                        }
                        break;
                    }
                    case "jTable2": {
                        int n = (int)Edited.get(i).get(2) - 1;
                        int r = (int)Edited.get(i).get(3);
                        int c = (int)Edited.get(i).get(4);
                        String s = Edited.get(i).get(5).toString();
                        Techs.get(n).get(c).set(r, s);
                        if (jComboBox2.getSelectedIndex()==n+1) {
                            jTable2.setValueAt(s, r, c);
                        }
                        break;
                    }
                }
                break;
            }
            case 3: {
                switch (Edited.get(i).get(1).toString()) {
                    case "jTable1": {
                        int n = (int)Edited.get(i).get(2) - 1;
                        int r = (int)Edited.get(i).get(3);
                        String s = Edited.get(i).get(4).toString();
                        Empls.get(n).add(r, s);
                        if (jComboBox1.getSelectedIndex()==n+1) {
                            Model m = (Model) jTable1.getModel();
                            m.insertRow(r, new Vector(Arrays.asList(s)));
                            if (Empls.get(n).size()<19) m.setRowCount(20);
                            jTable1.setModel(m);
                        }
                        break;
                    }
                    case "jTable2": {
                        int n = (int)Edited.get(i).get(2) - 1;
                        int r = (int)Edited.get(i).get(3);
                        String s1 = Edited.get(i).get(4).toString();
                        String s2 = Edited.get(i).get(5).toString();
                        Techs.get(n).get(0).add(r, s1);
                        Techs.get(n).get(1).add(r, s2);
                        if (jComboBox2.getSelectedIndex()==n+1) {
                            Model m = (Model) jTable2.getModel();
                            m.insertRow(r, new Vector(Arrays.asList(s1, s2)));
                            if (Techs.get(n).get(0).size()<19) m.setRowCount(20);
                            jTable2.setModel(m);
                        }
                        break;
                    }
                }
                break;
            }
            case 4: {
                switch (Edited.get(i).get(1).toString()) {
                    case "jTable1": {
                        Empls.remove(jComboBox1.getItemCount()-2);
                        jComboBox1.removeItemAt(jComboBox1.getItemCount()-1);
                        break;
                    }
                    case "jTable2": {
                        Techs.remove(jComboBox2.getItemCount()-2);
                        jComboBox2.removeItemAt(jComboBox2.getItemCount()-1);
                        break;
                    }
                }
                break;
            }
            case 5: {
                switch (Edited.get(i).get(1).toString()) {
                    case "jTable1": {
                        Vector v = (Vector)Edited.get(i).get(4);
                        int r = (int)Edited.get(i).get(2);
                        String s = Edited.get(i).get(3).toString();
                        Empls.add(r, v);
                        jComboBox1.insertItemAt(s, r+1);
                        break;
                    }
                    case "jTable2": {
                        Vector<Vector> v = (Vector<Vector>)Edited.get(i).get(4);
                        int r = (int)Edited.get(i).get(2);
                        String s = Edited.get(i).get(3).toString();
                        Techs.add(r, v);
                        jComboBox2.insertItemAt(s, r+1);
                        break;
                    }
                }
                break;
            }
        }
        Edited.remove(i);
        if (Edited.size()==0) {
            jButton2.setEnabled(false);
            jButton3.setEnabled(false);
        }
    }//GEN-LAST:event_jButton8MouseClicked

    private void jButton9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MouseClicked
        // TODO add your handling code here:
        String passwd;
        JPasswordField pass = new JPasswordField();
        JPasswordField pass2 = new JPasswordField();
        JPasswordField pass3 = new JPasswordField();
        Object[] obj = {"Please enter password:\n\n", "Your password:", pass, "New password:",pass2, "Retry new password:",pass3};
        Object stringArray[] = {"OK","Cancel"};
        boolean b = false;
        while (!b) {
            if (JOptionPane.showOptionDialog(rootPane, obj, "Changing password", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, stringArray, obj)==JOptionPane.YES_OPTION) {
                if (!pass.getText().isEmpty() && !pass2.getText().isEmpty() && !pass3.getText().isEmpty()) {
                    if (pass.getText().equals(Password)) {
                        if (pass2.getText().equals(pass3.getText())) {
                            b = true;
                        }
                        else {
                            JOptionPane.showMessageDialog(rootPane, "Passwords are different!", "Changing", JOptionPane.ERROR_MESSAGE);
                            pass2.setText("");
                            pass3.setText("");
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(rootPane, "Wrong password!", "Changing", JOptionPane.ERROR_MESSAGE);
                        pass.setText("");
                        pass2.setText("");
                        pass3.setText("");
                    }
                }
            }
            else return;
        }
        passwd = pass2.getText();
        try {
            Statement st = con.createStatement();
            st.executeUpdate("UPDATE staff SET password='"+passwd+"' WHERE name='"+Name+"';");
        } catch (SQLException ex) {
            Logger.getLogger(Office.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton9MouseClicked

    private void jTextField1CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTextField1CaretUpdate
        // TODO add your handling code here:
        if (jTextField1.getText().length()!=0 && jPasswordField1.getText().length()!=0) {
            jButton10.setEnabled(true);
        }
        else jButton10.setEnabled(false);
    }//GEN-LAST:event_jTextField1CaretUpdate

    private void jPasswordField1CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jPasswordField1CaretUpdate
        // TODO add your handling code here:
        if (jTextField1.getText().length()!=0 && jPasswordField1.getText().length()!=0) {
            jButton10.setEnabled(true);
        }
        else jButton10.setEnabled(false);
    }//GEN-LAST:event_jPasswordField1CaretUpdate

    private void jButton10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton10MouseClicked
        // TODO add your handling code here:
        if (jButton10.isEnabled()) {
            try {
                Statement st = con.createStatement();
                Statement st2 = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM staff WHERE name='"+jTextField1.getText()+"' AND password='"+jPasswordField1.getText()+"';");
                if (rs.next()) {
                    Name = jTextField1.getText();
                    Password = jPasswordField1.getText();
                    String ip = null;
                    try {
                        ip = (new Socket(getDocumentBase().getHost(), getDocumentBase().getPort())).getLocalAddress().getHostAddress();
//                        JOptionPane.showMessageDialog(rootPane, ip);
                    } catch (UnknownHostException ex) {
                        Logger.getLogger(Office.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(Office.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Statement st3 = con.createStatement();
                    ResultSet rs3 = st.executeQuery("SELECT removeauditall();");
                    rs3.next();
                    rs3.close();
                    st3.close();
                    st2.executeUpdate("INSERT INTO inout (name, ip) VALUES ('"+Name+"', '"+ip+"');");
                    rs3 = st.executeQuery("SELECT setauditall();");
                    st2.close();
                    GetInf();
                    jLayeredPane2.setVisible(false);
                    jLayeredPane1.setVisible(true);
                }
                else {
                    JOptionPane.showMessageDialog(rootPane, "Wrong Name or Password!", "Entering", JOptionPane.ERROR_MESSAGE);
                    jTextField1.setText("");
                    jPasswordField1.setText("");
                }
                rs.close();
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(Office.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton10MouseClicked

    private void jComboBox1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox1MouseClicked
        // TODO add your handling code here:
        if (jComboBox1.getSelectedIndex()==-1 || !(boolean)rule.get(0)) return;
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM rules WHERE id_sg=(SELECT id_sg FROM sgroups WHERE name='"+jComboBox1.getSelectedItem()+"')");
            if (!rs.next()) return;
            if (evt.getClickCount()==2) {
                int id = rs.getInt("id_sg");
                JCheckBox box1 = new JCheckBox();
                box1.setText("Add/Delete staff and satff groups");
                box1.setSelected(rs.getBoolean("s_editing"));
                JCheckBox box2 = new JCheckBox();
                box2.setText("Add/Delete technique groups");
                box2.setSelected(rs.getBoolean("tg_add_del"));
                JCheckBox box3 = new JCheckBox();
                box3.setText("Add/Delete technique");
                box3.setSelected(rs.getBoolean("t_add_del"));
                JCheckBox box4 = new JCheckBox();
                box4.setText("Editing technique");
                box4.setSelected(rs.getBoolean("t_editing"));
                JCheckBox box5 = new JCheckBox();
                box5.setText("Editing Name");
                box5.setSelected(rs.getBoolean("name_e"));
                JCheckBox box6 = new JCheckBox();
                box6.setText("Editing rules");
                box6.setSelected(rs.getBoolean("rules_e"));
                Object[] obj = {"Choose options:\n\n", box1, box2, box3, box4, box4, box6};
                Object stringArray[] = {"OK"};
                if (JOptionPane.showOptionDialog(rootPane, obj, "Rules", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, stringArray, obj)==JOptionPane.YES_OPTION) {
                    st.executeUpdate("UPDATE rules SET s_editing="+box1.isSelected()+", tg_add_del="+box2.isSelected()+", t_add_del="+box3.isSelected()+", t_editing="+box4.isSelected()+", name_e="+box5.isSelected()+", rules_e="+box6.isSelected()+" WHERE id_sg="+id+";");
                }
            }
            rs.close();
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(Office.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jComboBox1MouseClicked

    private void GetInf() {
        try {
            Statement st = con.createStatement();
            Statement st2 = con.createStatement();
            Statement st3 = con.createStatement();
            rule.clear();
            ResultSet rs = st.executeQuery("SELECT name FROM sgroups");
            ResultSet rs3 = st3.executeQuery("SELECT * FROM rules WHERE id_sg=(SELECT id_sg FROM staff WHERE name='"+Name+"');");
            rs3.next();
            rule.add(rs3.getBoolean("s_editing"));
            rule.add(rs3.getBoolean("tg_add_del"));
            rule.add(rs3.getBoolean("t_add_del"));
            rule.add(rs3.getBoolean("t_editing"));
            rule.add(rs3.getBoolean("name_e"));
            rule.add(rs3.getBoolean("rules_e"));
            rs3.close();
            st3.close();
            jButton4.setEnabled((boolean)rule.get(0));
            jButton5.setEnabled((boolean)rule.get(1));
            jButton6.setEnabled((boolean)rule.get(0));
            jButton7.setEnabled((boolean)rule.get(1));
            while (rs.next()) {
                ResultSet rs2 = st2.executeQuery("SELECT staff.name FROM staff INNER JOIN sgroups ON sgroups.id_sg=staff.id_sg WHERE sgroups.name='"+rs.getString("name") +"';");
                jComboBox1.addItem(rs.getString("name"));
                Vector n = new Vector();
                n.clear();
                while (rs2.next()) {
                    n.add(rs2.getObject("name"));
                }
                Empls.add(n);
                rs2.close();
            }
            rs.close();
            rs = st.executeQuery("SELECT name FROM tgroups");
            while (rs.next()) {
                ResultSet rs2 = st2.executeQuery("SELECT techs.name,numbs FROM techs INNER JOIN tgroups ON tgroups.id_gt=techs.id_gt WHERE tgroups.name='"+rs.getString("name")+"';");
                jComboBox2.addItem(rs.getString("name"));
                if (rs2.next()) {
                    Vector n = new Vector();
                    Vector c = new Vector();
                    n.add(rs2.getObject("name"));
                    c.add(rs2.getObject("numbs"));
                    while (rs2.next()) {
                        n.add(rs2.getObject("name"));
                        c.add(rs2.getObject("numbs"));
                    }
                    Techs.add(new Vector(Arrays.asList(n, c)));
                }
                else Techs.add(new Vector(Arrays.asList(new Vector(), new Vector())));
                rs2.close();
            }
            rs.close();
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(Office.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public class Model extends DefaultTableModel {

        public Model(Object[][] data, Object[] columnNames) {
            super(data, columnNames);
        }

        @Override
        public void fireTableRowsUpdated(int firstRow, int lastRow) {
            super.fireTableRowsUpdated(firstRow, lastRow);
        }

        @Override
        public void fireTableRowsInserted(int firstRow, int lastRow) {
            super.fireTableRowsInserted(firstRow, lastRow);
        }

        @Override
        public void fireTableDataChanged() {
            super.fireTableDataChanged();
        }

        @Override
        public void fireTableChanged(TableModelEvent e) {
            super.fireTableChanged(e);
        }

        @Override
        public void fireTableCellUpdated(int row, int column) {
            super.fireTableCellUpdated(row, column);
        }
        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
        @Override
        public void addRow(Object[] rowData) {
            super.addRow(rowData);
        }

        @Override
        public void removeRow(int row) {
            super.removeRow(row);
        }

        @Override
        public Object getValueAt(int row, int column) {
            return super.getValueAt(row, column);
        }

        @Override
        public void addTableModelListener(TableModelListener l) {
            super.addTableModelListener(l);
        }
        
        @Override
        public void setValueAt(Object aValue, int row, int column) {
            super.setValueAt(aValue, row, column);
        }
        public Model(Vector columnNames, int rowCount) {
            super(columnNames, rowCount);
        }
        private ArrayList nonEditableCells = new ArrayList();
        
        public void setCellEditable(int row, int column, boolean editable)
        {
            Cell cell = new Cell(row, column);
            if (editable)
            {
                while (nonEditableCells.remove(cell)) {}
            }
            else
            {
                nonEditableCells.add(cell);
            }
        }
  
        public void setRowEditable(int row, boolean editable)
        {
            for (int i = 0; i < this.getColumnCount(); i++)
            {
                if (editable)
                {
                    while (nonEditableCells.remove(new Cell(row, i))) {}
                }
                else {
                    nonEditableCells.add(new Cell(row, i));
                }
            }
        }

        public void setColumnEditable(int column, boolean editable)
        {
            for (int i = 0; i < this.getRowCount(); i++)
            {
                if (editable)
                {
                    while (nonEditableCells.remove(new Cell(i, column))) {}
                }
                else
                {
                    nonEditableCells.add(new Cell(i, column));
                }
            }    
        }

        @Override
        public boolean isCellEditable(int row, int column)
        {
            Cell cell = new Cell(row, column);
            for (int i = 0; i < nonEditableCells.size(); i++)
            {
                if (cell.equals(nonEditableCells.get(i)))
                {
                    return false;
                }
            }
            return super.isCellEditable(row, column);
        }

        class Cell
        {
            Integer Row = null;
            Integer Col = null;
    
            public Cell(Integer row, Integer col)
            {
                if (row != null)
                {
                    this.Row = row;
                }
                    else
                {
                    this.Row = 0;
                }
                if (col != null)
                {
                    this.Col = col;
                }
                else
                {
                    this.Col = 0;
                }
            }
    
            public Integer getRow()
            {
                return this.Row;
            }
    
            public Integer getCol()
            {
                return this.Col;
            }   
    
            public boolean equals(Cell cell)
            {
                if (cell.getRow() == this.getRow() && cell.getCol() == this.getCol())
                {
                    return true;
                }
                    else
                {
                    return false;
                }
            }
    
            public boolean equals(Object oCell)
            {
                Cell cell = (Cell)oCell;
                return equals(cell);
            }    
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
