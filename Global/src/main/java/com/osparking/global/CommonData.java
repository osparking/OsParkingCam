/*
 * Copyright (C) 2016 Open Source Parking, Inc.(www.osparking.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.osparking.global;

import static com.osparking.global.DataSheet.noOverwritePossibleExistingSameFile;
import static com.osparking.global.Globals.GENERAL_DEVICE;
import static com.osparking.global.Globals.closeDBstuff;
import static com.osparking.global.Globals.font_Size;
import static com.osparking.global.Globals.font_Style;
import static com.osparking.global.Globals.font_Type;
import static com.osparking.global.Globals.getBufferedImage;
import static com.osparking.global.Globals.getPathAndDay;
import static com.osparking.global.Globals.getPercentString;
import static com.osparking.global.Globals.getTagNumber;
import static com.osparking.global.Globals.logParkingException;
import static com.osparking.global.Globals.logParkingExceptionStatus;
import com.osparking.global.names.ControlEnums;
import static com.osparking.global.names.ControlEnums.DialogTitleTypes.DELETE_RESULT_DIALOGTITLE;
import static com.osparking.global.names.ControlEnums.LabelContent.TABLE_DEL_DIALOG_1;
import static com.osparking.global.names.ControlEnums.LabelContent.TABLE_DEL_DIALOG_2;
import static com.osparking.global.names.ControlEnums.OsPaLang.KOREAN;
import static com.osparking.global.names.ControlEnums.MenuITemTypes.META_KEY_LABEL;
import com.osparking.global.names.ControlEnums.OsPaTable;
import com.osparking.global.names.ControlEnums.RowName;
import static com.osparking.global.names.DB_Access.deviceType;
import static com.osparking.global.names.DB_Access.getRecordCount;
import com.osparking.global.names.JDBCMySQL;
import static com.osparking.global.names.OSP_enums.CameraType.CarButton;
import static com.osparking.global.names.OSP_enums.DeviceType.Camera;
import com.osparking.global.names.PasswordValidator;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.im.InputContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.jopendocument.dom.OOUtils;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

/** new Dimension(CommonData.bigButtonWidth, bigButtonHeight)
 *
 * @author Open Source Parking, Inc.
 */
public class CommonData { // new Dimension(carTagWidth, 30)
    /**
     * Combobox selected item index when not selected.
     */
    public static PasswordValidator pwValidator = null;
    public static final int FIRST_ROW = 0;
    public static final int PASS_MAX = 20;
    
    public static final String ODS_FILE_DIR = "ods";
    public static final String ADMIN_ID = "admin";
    public static final String CA_ROW_VAR = "T3";
    public static final int NOT_SELECTED = -1; 
    public static final int NOT_LISTED = -1; 
    public static final int PROMPTER_KEY = 0; 

    public static final int ImgWidth = 1280;
    public static final int ImgHeight = 960;    
    
    public static final int carTagWidth = 125; // Normal Width
    public static final int buttonWidthNorm = 90; // Normal Width
    public static final int buttonWidthWide = 110; // Wide Width
    public static final int buttonHeightNorm = 40;
    public static final int buttonHeightShort = 30; 
    public static final int TEXT_FIELD_HEIGHT = 30; 
    public static final int CBOX_HEIGHT = 28; 
    public static final int normGUIwidth = 1027; 
    public static final int normGUIheight = 720; 
    public static final int SETTINGS_WIDTH = 800; 
    public static final int SETTINGS_HEIGHT = 840; 
    public static final int tableRowHeight = 30; 
    public static final int bigButtonWidth = 160;
    public static final int bigButtonHeight = 60;
    public static final Dimension bigButtonDim = 
            new Dimension(CommonData.bigButtonWidth, bigButtonHeight);
    public static File ODS_DIRECTORY = null; 
    public static String ODS_FILEPATH = System.getProperty("user.home") + File.separator + ODS_FILE_DIR; 
    static {
        makeSurePathExists(ODS_FILEPATH);
        ODS_DIRECTORY = new File(ODS_FILEPATH);
    }
    public static JLabel metaKeyLabel = new JLabel(META_KEY_LABEL.getContent());  
    public static final Color tipColor = new java.awt.Color(0xff, 0x85, 0x33);
    public static final Color tipColorTrans = new java.awt.Color(0xff, 0x85, 0x33, 127);
    static {
        metaKeyLabel.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        metaKeyLabel.setForeground(tipColor);
    }
    public static final Color DARK_BLUE = new Color(0x00, 0x33, 0x66);
    public static final Color LIGHT_BLUE = new Color(0xb3, 0xd9, 0xFF);
    public static final Color pointColor = new java.awt.Color(255, 51, 51);
    
    public static final DefaultTableCellRenderer putCellCenter = new DefaultTableCellRenderer();
    static {
        putCellCenter.setHorizontalAlignment(JLabel.CENTER);    
    }

    static Toolkit toolkit = null;
    public static String requiredChar = Character.toString((char)10035);
    
    public static void checkOdsExistance(String fnInfix, int dev_ID, String disc1, 
            String disc2, JTextField criticalField, File[] odsFile, TableModel model) 
    {
        //<editor-fold desc="-- Prepare log file to store 'E-Board display interrupt' message Sequence Number">
        StringBuilder pathname = new StringBuilder();
        StringBuilder daySB = new StringBuilder();

        getPathAndDay("operation", pathname, daySB);

        /**
         * Full path name of today's ods file for interrupt command logging.
         */
        String odsFilePath = pathname + File.separator 
                + daySB.toString() + fnInfix + dev_ID + ".ods";
        try {
            odsFile[0] = new File(odsFilePath);
            if (!odsFile[0].exists() || odsFile[0].isDirectory()) {
                SpreadSheet.createEmpty(model).saveAs(odsFile[0]);
                appendOdsLine(odsFile[0], "#" + dev_ID + disc1, disc2, criticalField);
            }            
        } catch (IOException ex) {
            logParkingExceptionStatus(Level.SEVERE, ex, "while preparing log file", 
                    criticalField, GENERAL_DEVICE);
        } catch (Exception ex) {
            logParkingExceptionStatus(Level.SEVERE, ex, "while preparing log text file", 
                    criticalField, GENERAL_DEVICE);
        }  
        //</editor-fold>        
    }    
    
    public static void appendOdsLine(File odsFile, String currID, String prevID,
            JTextField criticalField) {
        try {
            Sheet sheet = SpreadSheet.createFromFile(odsFile).getSheet(0);
            
            sheet.ensureRowCount(sheet.getRowCount() + 1);
            sheet.getCellAt(0, sheet.getRowCount() - 1).setValue(currID);
            
            sheet.getCellAt(1, sheet.getRowCount() - 1).setValue(prevID);
            
            sheet.getSpreadSheet().saveAs(odsFile);
        } catch (IOException ex) {
            logParkingExceptionStatus(Level.SEVERE, ex, "Creating a sheet from ods file",
                    criticalField, 0);            
        } catch (Exception e) {
            logParkingExceptionStatus(Level.SEVERE, e, "Exception a sheet from ods file",
                    criticalField, 0);            
        }
    }  
    
    public static void appendOdsLine(File odsFile, String seqNo, JTextField criticalField) {
        try {
            Sheet sheet = SpreadSheet.createFromFile(odsFile).getSheet(0);
            sheet.ensureRowCount(sheet.getRowCount() + 1);
            sheet.getCellAt(0, sheet.getRowCount() - 1).setValue(seqNo);
            sheet.getSpreadSheet().saveAs(odsFile);
        } catch (IOException ex) {
            logParkingExceptionStatus(Level.SEVERE, ex, "Creating a sheet from ods file",
                    criticalField, 0);            
        } catch (Exception e) {
            logParkingExceptionStatus(Level.SEVERE, e, "Exception a sheet from ods file",
                    criticalField, 0);            
        }
    }        
    
    public static void displayRateLimit(JTextField displayField, float rate, boolean isMax) {
        if (toolkit == null) {
            toolkit = Toolkit.getDefaultToolkit();
        }
        toolkit.beep();
        displayField.setText("Current error rate(=" + getPercentString(rate)
                + ") is " + (isMax ? "max!" : "min!"));
    }     
    
    public static void displayPercent(JTextField infoField, float E_RATE, JLabel perLabel, 
            boolean enable) 
    {
        if (enable) {
            infoField.setText("Artificial error rate : " + getPercentString(E_RATE));
            perLabel.setText(getPercentString(E_RATE));
        } else {
            displayRateLimit(infoField, E_RATE, false);
            perLabel.setText(getPercentString(E_RATE));
        }
        perLabel.setEnabled(enable);
    }    
    
    /**
     * Set keyboard input language for a component.
     * @param comp component for which keyboard language to set
     * @param language keyboard language to set for the component
     */
    public static void setKeyboardLanguage(Component comp, ControlEnums.OsPaLang language) {
        try {
            InputContext inCtx  =  comp.getInputContext();
            if (inCtx != null) {
                Character.Subset[] subset = new Character.Subset[1];

                if (language == KOREAN) {
                    subset[0] = Character.UnicodeBlock.HANGUL_SYLLABLES;
                } else {
                    subset = null;
                }
                inCtx.setCharacterSubsets(subset);
            }
        } catch(Exception e) {
            String lang = (language == KOREAN ? "KOREAN" : "English");
            
            logParkingException(Level.SEVERE, null, "Keyboard language to " + lang + " error");
        }
    }
    
    public static void deleteTable(Component parent, OsPaTable tableName, 
            String condition, String unitName) 
    {
        String sqlDelete = "Delete From " + tableName;
        Connection conn = null;
        Statement deleteStmt = null; 
        int resultCount = 0;
        
        if (condition != null) {
            sqlDelete += " Where " + condition;
        }
        String appendStr = null;
        int[] preCount = new int[OsPaTable.values().length];
        
        //<editor-fold desc="-- Save row count of cascading delete victim tables">
        switch (tableName) {
            case CarDriver:
                preCount[OsPaTable.Vehicles.ordinal()] = getRecordCount(OsPaTable.Vehicles, -1);
                break;
                
            case L1_affiliation:
            case Building_table:
                if (tableName == OsPaTable.L1_affiliation) {
                    preCount[OsPaTable.L2_affiliation.ordinal()] = getRecordCount(OsPaTable.L2_affiliation, -1);
                } else {
                    preCount[OsPaTable.Building_unit.ordinal()] = getRecordCount(OsPaTable.Building_unit, -1);
                }
                preCount[OsPaTable.CarDriver.ordinal()] = getRecordCount(OsPaTable.CarDriver, -1);
                preCount[OsPaTable.Vehicles.ordinal()] = getRecordCount(OsPaTable.Vehicles, -1);
                break;
                
            default:
                break;
        }
        //</editor-fold>
        try {
            conn = JDBCMySQL.getConnection();
            deleteStmt = conn.createStatement();
            resultCount = deleteStmt.executeUpdate(sqlDelete);

            String delDesc = null;
            
            delDesc = " -" + TABLE_DEL_DIALOG_1.getContent() + unitName + 
                            TABLE_DEL_DIALOG_2.getContent() + resultCount;
            //<editor-fold desc="-- Append stat' of deleted rows from cascading delete">
            switch (tableName) {
                case CarDriver:
                    appendStr = appStr(RowName.VEHICLE.getContent(), 
                            preCount[OsPaTable.Vehicles.ordinal()] -
                                    getRecordCount(OsPaTable.Vehicles, -1));
                      
                    break;
                    
                case L1_affiliation:
                case Building_table:
                    if (tableName == OsPaTable.L1_affiliation) {
                        appendStr = appStr(RowName.L2_AFFILI.getContent(), 
                                preCount[OsPaTable.L2_affiliation.ordinal()] - 
                                        getRecordCount(OsPaTable.L2_affiliation, -1));
                    } else {
                        appendStr = appStr(RowName.UNIT.getContent(),
                                preCount[OsPaTable.Building_unit.ordinal()] -
                                        getRecordCount(OsPaTable.Building_unit, -1));
                    }
                    appendStr += System.lineSeparator() + appStr(RowName.DRIVER.getContent(), 
                            preCount[OsPaTable.CarDriver.ordinal()] - 
                                    getRecordCount(OsPaTable.CarDriver, -1));
                    appendStr += System.lineSeparator() + appStr(RowName.VEHICLE.getContent(), 
                            preCount[OsPaTable.Vehicles.ordinal()] -
                                    getRecordCount(OsPaTable.Vehicles, -1));
                    break;
                    
                default:
                    break;
            }
            //</editor-fold>
            if (appendStr != null) {
                delDesc += System.lineSeparator() + appendStr;
            }
            JOptionPane.showMessageDialog(parent, delDesc,
                    DELETE_RESULT_DIALOGTITLE.getContent(), JOptionPane.PLAIN_MESSAGE);
        } catch (Exception se) {
            logParkingException(Level.SEVERE, se, "(" + sqlDelete + ")");
        }  finally {
            closeDBstuff(conn, deleteStmt, null, "(" + sqlDelete + ")");
        } 
    }    
    
    public static void downloadSample(String destPath, InputStream sampleIn, String sampleFile) {
        OutputStream outStream = null;

        try {
            // Write resource into the file chosen by the user
            File destFile = new File(destPath);
            
            if (!noOverwritePossibleExistingSameFile(destFile, destPath)) 
            {
                outStream = new FileOutputStream(destFile);

                int read = 0;
                byte[] bytes = new byte[4096];

                while ((read = sampleIn.read(bytes)) != -1) {
                    outStream.write(bytes, 0, read);
                }
            }
        } catch (IOException ex) {
            logParkingException(Level.SEVERE, ex, sampleFile + " read error");
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    logParkingException(Level.SEVERE, e, destPath + " ostrm close error");
                }
            }       
        }
        
        try {
            OOUtils.open(new File(destPath));
        } catch (IOException ex) {
            logParkingException(Level.SEVERE, ex, destPath + " file open error");
        }
    }    
    
    public static void adminOperationEnabled(boolean flag, 
            JButton odsHelpButton, JButton sampleButton, JButton readSheet_Button) 
    {
        if (flag && Globals.loginID != null && Globals.loginID.equals(ADMIN_ID)) 
        {
            odsHelpButton.setEnabled(true);
            sampleButton.setEnabled(true);
            readSheet_Button.setEnabled(true);
        } else {
            odsHelpButton.setEnabled(false);
            sampleButton.setEnabled(false);
            readSheet_Button.setEnabled(false);
        }
    }    

    public static void adjustTableHeight(JTable usersTable) {
        Dimension tableDim = new Dimension(usersTable.getSize().width, 
                usersTable.getRowHeight() * usersTable.getRowCount()); 
        usersTable.setSize(tableDim);
        usersTable.setPreferredSize(tableDim);
    }
    
    public static void rejectNonNumericKeys(KeyEvent evt) {
        char c = evt.getKeyChar();
        if ( !(
                (c >= '0') && (c <= '9') ||
                (c == KeyEvent.VK_BACK_SPACE) ||
                (c == KeyEvent.VK_DELETE) ||
                (c == KeyEvent.VK_ENTER)
                ))
        {
            Toolkit.getDefaultToolkit().beep();
            evt.consume();
        }    
    }    
    
    // Make sure the 'ods' folder exists in the user home directory.
    public static void makeSurePathExists(String dirPath) {
        File dirFile = new File(dirPath);
        dirFile.mkdirs();
    }    
    
    public static final int[] statCountArr = {1, 10, 100, 1000, 10000, 100000};   

    public static CameraMessage[] dummyMessages = new CameraMessage[7]; 
    static {
        for (byte idx = 1; idx <= 6; idx++) {
            dummyMessages[idx] = new CameraMessage( "car" + idx + ".jpg", 
                    getTagNumber(idx), getBufferedImage(idx)); 
        }
    }
    
    public static DefaultTableCellRenderer centerCellRenderer = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) 
        {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                    row, column);
            setHorizontalAlignment(JLabel.CENTER);
            return this;            
        }        
    };
    
    public static DefaultTableCellRenderer leftCellRenderer = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) 
        {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                    row, column);
            setHorizontalAlignment(JLabel.LEFT);
            return this;            
        }        
    };
    
    public static DefaultTableCellRenderer numberCellRenderer = getCellRenderer(15);
    
    public static DefaultTableCellRenderer numberCellRendererW = getCellRenderer(40);
    
    private static DefaultTableCellRenderer getCellRenderer(final int hMargin) {
        return new DefaultTableCellRenderer() {
            Border padding = BorderFactory.createEmptyBorder(0, hMargin, 0, hMargin);

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) 
            {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                        row, column);
                setBorder(BorderFactory.createCompoundBorder(getBorder(), padding));
                setHorizontalAlignment(JLabel.RIGHT);
                return this;            
            }
        };         
    }
    
    public static void showCount(JTable RunRecordTable, JButton saveSheet_Button, 
            JLabel countValue) 
    {
        DefaultTableModel model = (DefaultTableModel) RunRecordTable.getModel();  
        
        int numRows = model.getRowCount();
        
        countValue.setText(Integer.toString(numRows));
        if (numRows == 0) {
            saveSheet_Button.setEnabled(false);
        } else {
            saveSheet_Button.setEnabled(Globals.isManager);
        }       
    }    
    
    public static boolean cameraOneIsButton() {
        return deviceType[Camera.ordinal()][1] == CarButton.ordinal();
    }     
    
    public static void resizeComponentFor(Component titleLabel, String thisText) {
        int minW = titleLabel.getMinimumSize().width, w;
        
        if (getStringWidth(thisText, titleLabel.getFont()) + 10 > minW) {
            w = getStringWidth(thisText, titleLabel.getFont()) + 10;
        } else {
            w = minW;
        }
        Dimension idLabelDim = new Dimension(w, titleLabel.getPreferredSize().height);
        titleLabel.setSize(idLabelDim);
        titleLabel.setPreferredSize(idLabelDim);
    }      
    
    public static int getStringWidth(String loginID, Font font) {
        AffineTransform affinetransform = new AffineTransform();     
        FontRenderContext frc = new FontRenderContext(affinetransform, true, true);     
        return (int)((font.getStringBounds(loginID, frc)).getWidth());
    }

    private static String appStr(String content, int recordCount) {
      return " -" + TABLE_DEL_DIALOG_1.getContent() + content + 
              TABLE_DEL_DIALOG_2.getContent() + recordCount;
    }
}
