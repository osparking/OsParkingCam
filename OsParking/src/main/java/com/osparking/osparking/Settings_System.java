/* 
 * Copyright (C) 2015, 2016  Open Source Parking, Inc.(www.osparking.com)
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
package com.osparking.osparking;

import com.osparking.global.names.JDBCMySQL;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import javax.swing.JTextField;
import com.osparking.global.names.ConvComboBoxItem;
import static com.osparking.global.names.DB_Access.EBD_blinkCycle;
import static com.osparking.global.names.DB_Access.EBD_flowCycle;
import static com.osparking.global.names.DB_Access.PIC_HEIGHT;
import static com.osparking.global.names.DB_Access.PIC_WIDTH;
import static com.osparking.global.names.DB_Access.deviceIP;
import static com.osparking.global.names.DB_Access.gateCount;
import static com.osparking.global.names.DB_Access.gateNames;
import static com.osparking.global.names.DB_Access.localeIndex;
import static com.osparking.global.names.DB_Access.passingDelayCurrentTotalMs;
import static com.osparking.global.names.DB_Access.maxMaintainDate;
import static com.osparking.global.names.DB_Access.maxMessageLines;
import static com.osparking.global.names.DB_Access.opLoggingIndex;
import static com.osparking.global.names.DB_Access.storePassingDelay;
import static com.osparking.global.names.DB_Access.pwStrengthLevel;
import static com.osparking.global.names.DB_Access.readSettings;
import static com.osparking.global.names.DB_Access.statCount;
import static com.osparking.global.names.DB_Access.passingCountCurrent;
import com.osparking.global.Globals;
import static com.osparking.global.Globals.*; 
import static com.osparking.global.names.OSP_enums.DeviceType.*;
import com.osparking.global.names.OSP_enums.OpLogLevel;
import static com.osparking.global.names.OSP_enums.OpLogLevel.LogAlways;
import static com.osparking.global.names.OSP_enums.OpLogLevel.SettingsChange;
import static com.osparking.global.names.OSP_enums.OpLogLevel.EBDsettingsChange;
import com.osparking.global.names.PasswordValidator;
import org.apache.commons.validator.routines.InetAddressValidator;
import com.osparking.attendant.PWHelpJDialog;
import com.osparking.global.ChangedComponentSave;
import com.osparking.global.CommonData;
import static com.osparking.global.CommonData.CBOX_HEIGHT;
import static com.osparking.global.CommonData.SETTINGS_HEIGHT;
import static com.osparking.global.CommonData.SETTINGS_WIDTH;
import static com.osparking.global.CommonData.TEXT_FIELD_HEIGHT;
import static com.osparking.global.CommonData.buttonHeightNorm;
import static com.osparking.global.CommonData.buttonHeightShort;
import static com.osparking.global.CommonData.buttonWidthNorm;
import static com.osparking.global.CommonData.statCountArr;
import static com.osparking.global.CommonData.tipColor;
import com.osparking.global.names.ChangeSettings;
import static com.osparking.global.names.ControlEnums.ButtonTypes.CANCEL_BTN;
import static com.osparking.global.names.ControlEnums.ButtonTypes.CLOSE_BTN;
import static com.osparking.global.names.ControlEnums.ButtonTypes.SAVE_BTN;
import static com.osparking.global.names.ControlEnums.ButtonTypes.SET_BUTTON;
import static com.osparking.global.names.ControlEnums.ComboBoxItemTypes.COMPLEX_CB_ITEM;
import static com.osparking.global.names.ControlEnums.ComboBoxItemTypes.DAY_SUFFIX;
import static com.osparking.global.names.ControlEnums.ComboBoxItemTypes.EBD_CHANGE_CB_ITEM;
import static com.osparking.global.names.ControlEnums.ComboBoxItemTypes.FOUR_DIGIT_CB_ITEM;
import static com.osparking.global.names.ControlEnums.ComboBoxItemTypes.NO_LOGGING_CB_ITEM;
import static com.osparking.global.names.ControlEnums.ComboBoxItemTypes.SETTING_CHANGE_CB_ITEM;
import static com.osparking.global.names.ControlEnums.ComboBoxItemTypes.SIX_DIGIT_CB_ITEM;
import static com.osparking.global.names.ControlEnums.DialogMessages.PopSizeHelp1;
import static com.osparking.global.names.ControlEnums.DialogMessages.PopSizeHelp2;
import static com.osparking.global.names.ControlEnums.DialogMessages.PopSizeHelp3;
import static com.osparking.global.names.ControlEnums.DialogMessages.PopSizeHelp4;
import static com.osparking.global.names.ControlEnums.DialogMessages.PopSizeHelp5;
import static com.osparking.global.names.ControlEnums.DialogMessages.PopSizeHelp6;
import static com.osparking.global.names.ControlEnums.DialogMessages.REBOOT_MESSAGE;
import static com.osparking.global.names.ControlEnums.DialogMessages.RECORD_DELAY_DEBUG;
import static com.osparking.global.names.ControlEnums.DialogMessages.SAVE_SETTINGS_DIALOG;
import static com.osparking.global.names.ControlEnums.DialogTitleTypes.ATT_HELP_DIALOGTITLE;
import static com.osparking.global.names.ControlEnums.DialogTitleTypes.IP_ERROR_TITLE;
import static com.osparking.global.names.ControlEnums.DialogTitleTypes.LANGUAGE_SELECT_DIALOGTITLE;
import static com.osparking.global.names.ControlEnums.DialogTitleTypes.LOGGING_DIALOGTITLE;
import static com.osparking.global.names.ControlEnums.DialogTitleTypes.SETTINGS_SAVE_RESULT;
import static com.osparking.global.names.ControlEnums.LabelContent.BLINGKING_LABEL;
import static com.osparking.global.names.ControlEnums.LabelContent.CAMERA_LABEL;
import static com.osparking.global.names.ControlEnums.LabelContent.CONN_LABEL;
import static com.osparking.global.names.ControlEnums.LabelContent.CYCLE_LABEL;
import static com.osparking.global.names.ControlEnums.LabelContent.DEVICE_LABEL;
import static com.osparking.global.names.ControlEnums.LabelContent.E_BOARD_LABEL;
import static com.osparking.global.names.ControlEnums.LabelContent.FLOWING_LABEL;
import static com.osparking.global.names.ControlEnums.LabelContent.GATE_BAR_LABEL;
import static com.osparking.global.names.ControlEnums.LabelContent.GATE_LABEL;
import static com.osparking.global.names.ControlEnums.LabelContent.GATE_NAME_LABEL;
import static com.osparking.global.names.ControlEnums.LabelContent.GATE_NUM_LABEL;
import static com.osparking.global.names.ControlEnums.LabelContent.IMG_KEEP_LABEL;
import static com.osparking.global.names.ControlEnums.LabelContent.IP_ADDR_ERROR_1;
import static com.osparking.global.names.ControlEnums.LabelContent.IP_ADDR_ERROR_2;
import static com.osparking.global.names.ControlEnums.LabelContent.IP_ADDR_LABEL;
import static com.osparking.global.names.ControlEnums.LabelContent.LANGUAGE_HELP_1;
import static com.osparking.global.names.ControlEnums.LabelContent.LANGUAGE_HELP_2;
import static com.osparking.global.names.ControlEnums.LabelContent.LANGUAGE_LABEL;
import static com.osparking.global.names.ControlEnums.LabelContent.LEVEL_LABEL;
import static com.osparking.global.names.ControlEnums.LabelContent.LOGGIND_DIALOG_1;
import static com.osparking.global.names.ControlEnums.LabelContent.LOGGIND_DIALOG_2;
import static com.osparking.global.names.ControlEnums.LabelContent.LOGGIND_DIALOG_3;
import static com.osparking.global.names.ControlEnums.LabelContent.LOGGIND_DIALOG_4;
import static com.osparking.global.names.ControlEnums.LabelContent.LOGGIND_DIALOG_5;
import static com.osparking.global.names.ControlEnums.LabelContent.LOGGIND_DIALOG_6;
import static com.osparking.global.names.ControlEnums.LabelContent.LOGGIND_DIALOG_A;
import static com.osparking.global.names.ControlEnums.LabelContent.LOGGIND_DIALOG_B;
import static com.osparking.global.names.ControlEnums.LabelContent.LOGGIND_DIALOG_C;
import static com.osparking.global.names.ControlEnums.LabelContent.LOGGIND_DIALOG_D;
import static com.osparking.global.names.ControlEnums.LabelContent.LOGGIND_DIALOG_E;
import static com.osparking.global.names.ControlEnums.LabelContent.LOGGIND_DIALOG_F;
import static com.osparking.global.names.ControlEnums.LabelContent.LOGGING_LEVEL_LABEL;
import static com.osparking.global.names.ControlEnums.LabelContent.LOT_NAME_LABEL;
import static com.osparking.global.names.ControlEnums.LabelContent.MAX_LINE_LABEL;
import static com.osparking.global.names.ControlEnums.LabelContent.PASSWORD_LEVEL_LABEL;
import static com.osparking.global.names.ControlEnums.LabelContent.PORT_LABEL;
import static com.osparking.global.names.ControlEnums.LabelContent.RECORD_PASSING_LABEL;
import static com.osparking.global.names.ControlEnums.LabelContent.SECONDS_LABEL;
import static com.osparking.global.names.ControlEnums.LabelContent.STATISTICS_SIZE_LABEL;
import static com.osparking.global.names.ControlEnums.LabelContent.TYPE_LABEL;
import static com.osparking.global.names.ControlEnums.LabelContent.VEHICLE_IMG_HEIGHT_LABEL;
import static com.osparking.global.names.ControlEnums.LabelContent.VEHICLE_IMG_SIZE_LABEL;
import static com.osparking.global.names.ControlEnums.LabelContent.VEHICLE_IMG_WIDTH_LABEL;
import static com.osparking.global.names.ControlEnums.MenuITemTypes.META_KEY_LABEL;
import static com.osparking.global.names.ControlEnums.MsgContent.AVERAGE_WORDS;
import static com.osparking.global.names.ControlEnums.MsgContent.RECENT_WORD;
import static com.osparking.global.names.ControlEnums.TitleTypes.E_BOARD_SETTINGS_FRAME_TITLE;
import static com.osparking.global.names.ControlEnums.TitleTypes.REBOOT_POPUP;
import static com.osparking.global.names.ControlEnums.TitleTypes.SETTINGS_TITLE;
import static com.osparking.global.names.ControlEnums.ToolTipContent.*;
import com.osparking.global.names.DB_Access;
import static com.osparking.global.names.DB_Access.connectionType;
import static com.osparking.global.names.DB_Access.deviceComID;
import static com.osparking.global.names.DB_Access.devicePort;
import static com.osparking.global.names.DB_Access.deviceType;
import static com.osparking.global.names.DB_Access.parkingLotLocale;
import static com.osparking.global.names.DB_Access.parkingLotName;
import static com.osparking.global.names.DB_Access.statCountIndex;
import com.osparking.global.names.EBD_DisplaySetting;
import com.osparking.global.names.OSP_enums.CameraType;
import com.osparking.global.names.OSP_enums.ConnectionType;
import static com.osparking.global.names.OSP_enums.ConnectionType.RS_232;
import com.osparking.global.names.OSP_enums.DeviceType;
import com.osparking.global.names.OSP_enums.E_BoardType;
import com.osparking.global.names.OSP_enums.GateBarType;
import com.osparking.global.names.OSP_enums.PWStrengthLevel;
import com.osparking.osparking.device.LEDnotice.LEDnoticeManager;
import com.osparking.osparking.device.LEDnotice.Settings_LEDnotice;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JLabel;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

/**
 * Provides GUI to set system parameters like image size(pixels), gate count, device IP address, etc.
 * For a stable operation, it is recommended to reboot this system(OS.Parking program) after any setting change.
 * @author Open Source Parking Inc.
 */
public class Settings_System extends javax.swing.JFrame {
    private boolean isStand_Alone = false;
    private static Logger logException = null;
    private static Logger logOperation = null;
    public static ControlGUI mainForm = null;
    private HashMap<String, Component> componentMap = new HashMap<String,Component>();
    public short maxArrivalCBoxIndex = 0;
    private HashSet<Component> changedControls = new HashSet<Component>();    
    JDialog eBoardDialog = null;
    static EBD_DisplaySetting[] EBD_DisplaySettings = null;
    static private ChangedComponentSave changedControls2; 
    
    
    /**
     * Initialize some controls on the system settings change GUI. 
     */
    public Settings_System(ControlGUI mainForm) {
        initComponents();
        changedControls2 = new ChangedComponentSave(SettingsSaveButton, 
                SettingsCancelButton, SettingsCloseButton);
        
        setIconImages(OSPiconList);                
        augmentComponentMap(this, componentMap);
        tuneComponentSize();
        
        this.mainForm = mainForm;
        if (mainForm == null)
            isStand_Alone = true;
        else {
            EBD_DisplaySettings = mainForm.EBD_DisplaySettings;
        }
        addPWStrengthItems();
        addMaxArrivalItems();
        maxArrivalCBoxIndex = findCBoxIndex(ImageDurationCBox, maxMaintainDate);
        addOperationLoggingLevelOptions();
        addPopSizeOptions();

        /**
         * Initialize device combobox items.
         */
        for (int gate = 1; gate <= gateCount; gate++) { 
            //<editor-fold desc="-- Combo Box item init for device and connection type of each gate">
            JComboBox comboBx = ((JComboBox)getComponentByName("Camera" +gate + "_TypeCBox"));
            if (comboBx != null) {
                comboBx.removeAllItems();
                for (CameraType type: CameraType.values()) {
                    comboBx.addItem(type.getLabel());
                }
            }
            
            comboBx = ((JComboBox)getComponentByName("E_Board" +gate + "_TypeCBox"));
            if (comboBx != null) {
                comboBx.removeAllItems();
                for (E_BoardType type: E_BoardType.values()) {
                    comboBx.addItem(type.getLabel());
                }
            }
            
            comboBx = ((JComboBox)getComponentByName("GateBar" +gate + "_TypeCBox"));
            if (comboBx != null) {
                comboBx.removeAllItems();
                for (GateBarType type: GateBarType.values()) {
                    comboBx.addItem(type.getLabel());
                }
            }
            
            for (DeviceType devType: DeviceType.values()) {
                comboBx = ((JComboBox)getComponentByName(devType.name() +gate + "_connTypeCBox"));
                if (comboBx != null) {
                    comboBx.removeAllItems();
                    for (ConnectionType connType : ConnectionType.values()) {
                        if (devType != Camera || connType == ConnectionType.TCP_IP) 
                        {
                            comboBx.addItem(connType.getLabel());
                        }
                    }
                }
            }
            //</editor-fold>
        }        
        
        loadComponentValues();
        makeEnterActAsTab();
        setLocation(0, 0);        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        wholePanel = new javax.swing.JPanel();
        titlePanel = new javax.swing.JPanel();
        attendantGUI_title = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        parkinglotOptionPanel = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        lotNameTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        PWHelpButton = new javax.swing.JButton();
        PWStrengthChoiceComboBox = new javax.swing.JComboBox<ConvComboBoxItem>();
        jLabel2 = new javax.swing.JLabel();
        LoggingLevelHelpButton = new javax.swing.JButton();
        OptnLoggingLevelComboBox = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        MessageMaxLineComboBox = new javax.swing.JComboBox();
        ImageDurationCBox = new javax.swing.JComboBox<ConvComboBoxItem>();
        ImageDurationLabel = new javax.swing.JLabel();
        GateCountComboBox = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        pxLabel1 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        TextFieldPicWidth = new javax.swing.JTextField();
        TextFieldPicHeight = new javax.swing.JTextField();
        LanguageHelpButton = new javax.swing.JButton();
        DateChooserLangCBox = new com.toedter.components.JLocaleChooser(parkingLotLocale);
        pxLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        PopSizeHelpButton = new javax.swing.JButton();
        PopSizeCBox = new javax.swing.JComboBox();
        RecordPassingDelayChkBox = new javax.swing.JCheckBox();
        gateSettingPanel = new javax.swing.JPanel();
        GatesTabbedPane = new javax.swing.JTabbedPane();
        gate1Panel = new javax.swing.JPanel();
        gate_name_p = new javax.swing.JPanel();
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(50, 32767));
        gateNameLabel1 = new javax.swing.JLabel();
        TextFieldGateName1 = new javax.swing.JTextField();
        topLabelsPanel = new javax.swing.JPanel();
        filler9 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(15, 0), new java.awt.Dimension(50, 32767));
        device1_Label = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        cameraPan = new javax.swing.JPanel();
        filler10 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(50, 32767));
        jLabel30 = new javax.swing.JLabel();
        Camera1_TypeCBox = new javax.swing.JComboBox();
        Camera1_connTypeCBox = new javax.swing.JComboBox();
        Camera1_IP_TextField = new javax.swing.JTextField();
        Camera1_Port_TextField = new javax.swing.JTextField();
        eBoardPan5 = new javax.swing.JPanel();
        filler35 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(50, 32767));
        jLabel63 = new javax.swing.JLabel();
        E_Board1_TypeCBox = new javax.swing.JComboBox();
        E_Board1_connTypeCBox = new javax.swing.JComboBox();
        E_Board1_IP_TextField = new javax.swing.JTextField();
        E_Board1_Port_TextField = new javax.swing.JTextField();
        gateBarPan5 = new javax.swing.JPanel();
        filler36 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(50, 32767));
        ebdLbl5 = new javax.swing.JLabel();
        GateBar1_TypeCBox = new javax.swing.JComboBox();
        GateBar1_connTypeCBox = new javax.swing.JComboBox();
        GateBar1_IP_TextField = new javax.swing.JTextField();
        GateBar1_Port_TextField = new javax.swing.JTextField();
        gate2Panel = new javax.swing.JPanel();
        gate_name_p4 = new javax.swing.JPanel();
        filler30 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(50, 32767));
        gateNameLabel5 = new javax.swing.JLabel();
        TextFieldGateName2 = new javax.swing.JTextField();
        topLabelsPanel4 = new javax.swing.JPanel();
        filler31 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(15, 0), new java.awt.Dimension(50, 32767));
        device1_Label4 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        cameraPan4 = new javax.swing.JPanel();
        filler32 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(50, 32767));
        jLabel61 = new javax.swing.JLabel();
        Camera2_TypeCBox = new javax.swing.JComboBox();
        Camera2_connTypeCBox = new javax.swing.JComboBox();
        Camera2_IP_TextField = new javax.swing.JTextField();
        Camera2_Port_TextField = new javax.swing.JTextField();
        eBoardPan4 = new javax.swing.JPanel();
        filler33 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(50, 32767));
        jLabel62 = new javax.swing.JLabel();
        E_Board2_TypeCBox = new javax.swing.JComboBox();
        E_Board2_connTypeCBox = new javax.swing.JComboBox();
        E_Board2_IP_TextField = new javax.swing.JTextField();
        E_Board2_Port_TextField = new javax.swing.JTextField();
        gateBarPan4 = new javax.swing.JPanel();
        filler34 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(50, 32767));
        ebdLbl4 = new javax.swing.JLabel();
        GateBar2_TypeCBox = new javax.swing.JComboBox();
        GateBar2_connTypeCBox = new javax.swing.JComboBox();
        GateBar2_IP_TextField = new javax.swing.JTextField();
        GateBar2_Port_TextField = new javax.swing.JTextField();
        gate3Panel = new javax.swing.JPanel();
        gate_name_p2 = new javax.swing.JPanel();
        filler13 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(50, 32767));
        gateNameLabel3 = new javax.swing.JLabel();
        TextFieldGateName3 = new javax.swing.JTextField();
        topLabelsPanel2 = new javax.swing.JPanel();
        filler14 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(15, 0), new java.awt.Dimension(50, 32767));
        device1_Label2 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        cameraPan2 = new javax.swing.JPanel();
        filler15 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(50, 32767));
        jLabel49 = new javax.swing.JLabel();
        Camera3_TypeCBox = new javax.swing.JComboBox();
        Camera3_connTypeCBox = new javax.swing.JComboBox();
        Camera3_IP_TextField = new javax.swing.JTextField();
        Camera3_Port_TextField = new javax.swing.JTextField();
        eBoardPan2 = new javax.swing.JPanel();
        filler23 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(50, 32767));
        jLabel50 = new javax.swing.JLabel();
        E_Board3_TypeCBox = new javax.swing.JComboBox();
        E_Board3_connTypeCBox = new javax.swing.JComboBox();
        E_Board3_IP_TextField = new javax.swing.JTextField();
        E_Board3_Port_TextField = new javax.swing.JTextField();
        gateBarPan2 = new javax.swing.JPanel();
        filler24 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(50, 32767));
        ebdLbl2 = new javax.swing.JLabel();
        GateBar3_TypeCBox = new javax.swing.JComboBox();
        GateBar3_connTypeCBox = new javax.swing.JComboBox();
        GateBar3_IP_TextField = new javax.swing.JTextField();
        GateBar3_Port_TextField = new javax.swing.JTextField();
        gate4Panel = new javax.swing.JPanel();
        gate_name_p3 = new javax.swing.JPanel();
        filler25 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(50, 32767));
        gateNameLabel4 = new javax.swing.JLabel();
        TextFieldGateName4 = new javax.swing.JTextField();
        topLabelsPanel3 = new javax.swing.JPanel();
        filler26 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(15, 0), new java.awt.Dimension(50, 32767));
        device1_Label3 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        cameraPan3 = new javax.swing.JPanel();
        filler27 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(50, 32767));
        jLabel55 = new javax.swing.JLabel();
        Camera4_TypeCBox = new javax.swing.JComboBox();
        Camera4_connTypeCBox = new javax.swing.JComboBox();
        Camera4_IP_TextField = new javax.swing.JTextField();
        Camera4_Port_TextField = new javax.swing.JTextField();
        eBoardPan3 = new javax.swing.JPanel();
        filler28 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(50, 32767));
        jLabel56 = new javax.swing.JLabel();
        E_Board4_TypeCBox = new javax.swing.JComboBox();
        E_Board4_connTypeCBox = new javax.swing.JComboBox();
        E_Board4_IP_TextField = new javax.swing.JTextField();
        E_Board4_Port_TextField = new javax.swing.JTextField();
        gateBarPan3 = new javax.swing.JPanel();
        filler29 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(50, 32767));
        ebdLbl3 = new javax.swing.JLabel();
        GateBar4_TypeCBox = new javax.swing.JComboBox();
        GateBar4_connTypeCBox = new javax.swing.JComboBox();
        GateBar4_IP_TextField = new javax.swing.JTextField();
        GateBar4_Port_TextField = new javax.swing.JTextField();
        eBoardSettingPanel = new javax.swing.JPanel();
        E_BoardSettingsButtonPanel = new javax.swing.JPanel();
        EBD_settings_label = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        EBD_settings = new javax.swing.JPanel();
        EBoardSettingsButton = new javax.swing.JButton();
        allCyclesPanel = new javax.swing.JPanel();
        cycleLabel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        twoCycles = new javax.swing.JPanel();
        real2Pan = new javax.swing.JPanel();
        flowPanel = new javax.swing.JPanel();
        labelFlow = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        cBoxPanel = new javax.swing.JPanel();
        filler18 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        FlowingComboBox = new javax.swing.JComboBox();
        jLabel33 = new javax.swing.JLabel();
        blinkPanel = new javax.swing.JPanel();
        labelBlink = new javax.swing.JPanel();
        blinkingL = new javax.swing.JLabel();
        cBoxPan = new javax.swing.JPanel();
        filler17 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        BlinkingComboBox = new javax.swing.JComboBox();
        jLabel32 = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 20), new java.awt.Dimension(0, 20), new java.awt.Dimension(32767, 20));
        bottomPanel = new javax.swing.JPanel();
        filler8 = new javax.swing.Box.Filler(new java.awt.Dimension(100, 0), new java.awt.Dimension(100, 0), new java.awt.Dimension(100, 32767));
        myMetaKeyLabel = new javax.swing.JLabel();
        buttonsPanel = new javax.swing.JPanel();
        SettingsSaveButton = new javax.swing.JButton();
        SettingsCancelButton = new javax.swing.JButton();
        SettingsCloseButton = new javax.swing.JButton();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 20), new java.awt.Dimension(0, 40), new java.awt.Dimension(32767, 20));
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(30, 0), new java.awt.Dimension(40, 0), new java.awt.Dimension(10, 32767));
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(30, 0), new java.awt.Dimension(40, 0), new java.awt.Dimension(10, 32767));
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 20), new java.awt.Dimension(0, 40), new java.awt.Dimension(32767, 20));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(SETTINGS_TITLE.getContent());
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocation(new java.awt.Point(0, 0));
        setMaximumSize(new Dimension(SETTINGS_WIDTH, SETTINGS_HEIGHT));
        setMinimumSize(new Dimension(SETTINGS_WIDTH, SETTINGS_HEIGHT));
        setPreferredSize(new Dimension(SETTINGS_WIDTH, SETTINGS_HEIGHT));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                finishSettingsForm(evt);
            }
        });

        wholePanel.setMinimumSize(new java.awt.Dimension(630, 738));
        wholePanel.setPreferredSize(new java.awt.Dimension(702, 800));
        wholePanel.setLayout(new javax.swing.BoxLayout(wholePanel, javax.swing.BoxLayout.PAGE_AXIS));

        titlePanel.setMaximumSize(new java.awt.Dimension(32767, 40));
        titlePanel.setMinimumSize(new java.awt.Dimension(100, 40));
        titlePanel.setPreferredSize(new java.awt.Dimension(500, 40));

        attendantGUI_title.setFont(new java.awt.Font(font_Type, font_Style, head_font_Size));
        attendantGUI_title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        attendantGUI_title.setText(SETTINGS_TITLE.getContent());
        attendantGUI_title.setMaximumSize(new java.awt.Dimension(120, 30));
        attendantGUI_title.setMinimumSize(new java.awt.Dimension(76, 30));
        attendantGUI_title.setPreferredSize(new java.awt.Dimension(120, 30));

        javax.swing.GroupLayout titlePanelLayout = new javax.swing.GroupLayout(titlePanel);
        titlePanel.setLayout(titlePanelLayout);
        titlePanelLayout.setHorizontalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, titlePanelLayout.createSequentialGroup()
                .addContainerGap(292, Short.MAX_VALUE)
                .addComponent(attendantGUI_title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(288, Short.MAX_VALUE))
        );
        titlePanelLayout.setVerticalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addComponent(attendantGUI_title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        wholePanel.add(titlePanel);
        wholePanel.add(jSeparator1);

        parkinglotOptionPanel.setMinimumSize(new java.awt.Dimension(690, 300));
        parkinglotOptionPanel.setPreferredSize(new java.awt.Dimension(0, 330));
        parkinglotOptionPanel.setLayout(new java.awt.GridBagLayout());

        jLabel42.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel42.setText(LOT_NAME_LABEL.getContent());
        jLabel42.setMaximumSize(new java.awt.Dimension(76, 27));
        jLabel42.setMinimumSize(new java.awt.Dimension(76, 27));
        jLabel42.setPreferredSize(new java.awt.Dimension(76, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        parkinglotOptionPanel.add(jLabel42, gridBagConstraints);

        lotNameTextField.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        lotNameTextField.setToolTipText("");
        lotNameTextField.setMinimumSize(new java.awt.Dimension(250, 25));
        lotNameTextField.setName("lotNameTextField"); // NOI18N
        lotNameTextField.setPreferredSize(new java.awt.Dimension(250, 28));
        lotNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lotNameTextFieldKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        parkinglotOptionPanel.add(lotNameTextField, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText(PASSWORD_LEVEL_LABEL.getContent());
        jLabel1.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        parkinglotOptionPanel.add(jLabel1, gridBagConstraints);

        PWHelpButton.setBackground(new java.awt.Color(153, 255, 153));
        PWHelpButton.setFont(new java.awt.Font("Dotum", 1, 14)); // NOI18N
        PWHelpButton.setIcon(getQuest20_Icon());
        PWHelpButton.setMargin(new java.awt.Insets(2, 4, 2, 4));
        PWHelpButton.setMaximumSize(new java.awt.Dimension(20, 20));
        PWHelpButton.setMinimumSize(new java.awt.Dimension(20, 20));
        PWHelpButton.setOpaque(false);
        PWHelpButton.setPreferredSize(new java.awt.Dimension(20, 20));
        PWHelpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PWHelpButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        parkinglotOptionPanel.add(PWHelpButton, gridBagConstraints);

        PWStrengthChoiceComboBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        PWStrengthChoiceComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        PWStrengthChoiceComboBox.setMaximumSize(new java.awt.Dimension(32767, 30));
        PWStrengthChoiceComboBox.setMinimumSize(new java.awt.Dimension(150, 30));
        PWStrengthChoiceComboBox.setName("PWStrengthChoiceComboBox"); // NOI18N
        PWStrengthChoiceComboBox.setPreferredSize(new java.awt.Dimension(150, 30));
        PWStrengthChoiceComboBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                PWStrengthChoiceComboBoxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 40;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        parkinglotOptionPanel.add(PWStrengthChoiceComboBox, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText(LOGGING_LEVEL_LABEL.getContent());
        jLabel2.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        parkinglotOptionPanel.add(jLabel2, gridBagConstraints);

        LoggingLevelHelpButton.setBackground(new java.awt.Color(153, 255, 153));
        LoggingLevelHelpButton.setFont(new java.awt.Font("Dotum", 1, 14)); // NOI18N
        LoggingLevelHelpButton.setIcon(getQuest20_Icon());
        LoggingLevelHelpButton.setMargin(new java.awt.Insets(2, 4, 2, 4));
        LoggingLevelHelpButton.setMinimumSize(new java.awt.Dimension(20, 20));
        LoggingLevelHelpButton.setOpaque(false);
        LoggingLevelHelpButton.setPreferredSize(new java.awt.Dimension(20, 20));
        LoggingLevelHelpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoggingLevelHelpButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        parkinglotOptionPanel.add(LoggingLevelHelpButton, gridBagConstraints);

        OptnLoggingLevelComboBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        OptnLoggingLevelComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        OptnLoggingLevelComboBox.setMinimumSize(new java.awt.Dimension(150, 23));
        OptnLoggingLevelComboBox.setName("OptnLoggingLevelComboBox"); // NOI18N
        OptnLoggingLevelComboBox.setPreferredSize(new java.awt.Dimension(150, 23));
        OptnLoggingLevelComboBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                OptnLoggingLevelComboBoxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 40;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        parkinglotOptionPanel.add(OptnLoggingLevelComboBox, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText(LANGUAGE_LABEL.getContent());
        jLabel3.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        parkinglotOptionPanel.add(jLabel3, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText(MAX_LINE_LABEL.getContent());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        parkinglotOptionPanel.add(jLabel5, gridBagConstraints);

        MessageMaxLineComboBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        MessageMaxLineComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "100", "200", "300", "500", "1000" }));
        MessageMaxLineComboBox.setMinimumSize(new java.awt.Dimension(70, 23));
        MessageMaxLineComboBox.setName("MessageMaxLineComboBox"); // NOI18N
        MessageMaxLineComboBox.setPreferredSize(new java.awt.Dimension(70, 23));
        MessageMaxLineComboBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                MessageMaxLineComboBoxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 19;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        parkinglotOptionPanel.add(MessageMaxLineComboBox, gridBagConstraints);

        ImageDurationCBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        ImageDurationCBox.setToolTipText("");
        ImageDurationCBox.setMinimumSize(new java.awt.Dimension(70, 23));
        ImageDurationCBox.setName("ImageDurationCBox"); // NOI18N
        ImageDurationCBox.setPreferredSize(new java.awt.Dimension(70, 23));
        ImageDurationCBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                ImageDurationCBoxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 19;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        parkinglotOptionPanel.add(ImageDurationCBox, gridBagConstraints);

        ImageDurationLabel.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        ImageDurationLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ImageDurationLabel.setText(IMG_KEEP_LABEL.getContent());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        parkinglotOptionPanel.add(ImageDurationLabel, gridBagConstraints);

        GateCountComboBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        GateCountComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4" }));
        GateCountComboBox.setMinimumSize(new java.awt.Dimension(70, 23));
        GateCountComboBox.setName("GateCountComboBox"); // NOI18N
        GateCountComboBox.setPreferredSize(new java.awt.Dimension(70, 23));
        GateCountComboBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                GateCountComboBoxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 19;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        parkinglotOptionPanel.add(GateCountComboBox, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText(GATE_NUM_LABEL.getContent());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        parkinglotOptionPanel.add(jLabel6, gridBagConstraints);

        jLabel19.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel19.setText(VEHICLE_IMG_SIZE_LABEL.getContent());
        jLabel19.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 0, 0);
        parkinglotOptionPanel.add(jLabel19, gridBagConstraints);

        jLabel11.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText(VEHICLE_IMG_WIDTH_LABEL.getContent());
        jLabel11.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(10, 30, 0, 0);
        parkinglotOptionPanel.add(jLabel11, gridBagConstraints);

        pxLabel1.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        pxLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        pxLabel1.setText("px");
        pxLabel1.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        parkinglotOptionPanel.add(pxLabel1, gridBagConstraints);

        jLabel12.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText(VEHICLE_IMG_HEIGHT_LABEL.getContent());
        jLabel12.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(10, 30, 0, 0);
        parkinglotOptionPanel.add(jLabel12, gridBagConstraints);

        TextFieldPicWidth.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        TextFieldPicWidth.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TextFieldPicWidth.setName("TextFieldPicWidth"); // NOI18N
        TextFieldPicWidth.setPreferredSize(new java.awt.Dimension(40, 30));
        TextFieldPicWidth.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldPicWidthKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextFieldPicWidthKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 20);
        parkinglotOptionPanel.add(TextFieldPicWidth, gridBagConstraints);

        TextFieldPicHeight.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        TextFieldPicHeight.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TextFieldPicHeight.setName("TextFieldPicHeight"); // NOI18N
        TextFieldPicHeight.setPreferredSize(new java.awt.Dimension(40, 30));
        TextFieldPicHeight.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldPicHeightKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextFieldPicHeightKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 20);
        parkinglotOptionPanel.add(TextFieldPicHeight, gridBagConstraints);

        LanguageHelpButton.setBackground(new java.awt.Color(153, 255, 153));
        LanguageHelpButton.setFont(new java.awt.Font("Dotum", 1, 14)); // NOI18N
        LanguageHelpButton.setIcon(getQuest20_Icon());
        LanguageHelpButton.setMargin(new java.awt.Insets(2, 4, 2, 4));
        LanguageHelpButton.setMinimumSize(new java.awt.Dimension(20, 20));
        LanguageHelpButton.setOpaque(false);
        LanguageHelpButton.setPreferredSize(new java.awt.Dimension(20, 20));
        LanguageHelpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LanguageHelpButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        parkinglotOptionPanel.add(LanguageHelpButton, gridBagConstraints);

        DateChooserLangCBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        DateChooserLangCBox.setMaximumSize(new java.awt.Dimension(32767, 28));
        DateChooserLangCBox.setMinimumSize(new java.awt.Dimension(294, 23));
        DateChooserLangCBox.setName("DateChooserLangCBox"); // NOI18N
        DateChooserLangCBox.setPreferredSize(new java.awt.Dimension(280, 28));
        DateChooserLangCBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                DateChooserLangCBoxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        parkinglotOptionPanel.add(DateChooserLangCBox, gridBagConstraints);

        pxLabel2.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        pxLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        pxLabel2.setText("px");
        pxLabel2.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 0, 10, 0);
        parkinglotOptionPanel.add(pxLabel2, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText(STATISTICS_SIZE_LABEL.getContent());
        jLabel4.setMaximumSize(new java.awt.Dimension(176, 27));
        jLabel4.setMinimumSize(new java.awt.Dimension(76, 27));
        jLabel4.setPreferredSize(new java.awt.Dimension(170, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        parkinglotOptionPanel.add(jLabel4, gridBagConstraints);

        PopSizeHelpButton.setBackground(new java.awt.Color(153, 255, 153));
        PopSizeHelpButton.setFont(new java.awt.Font("Dotum", 1, 14)); // NOI18N
        PopSizeHelpButton.setIcon(getQuest20_Icon());
        PopSizeHelpButton.setMargin(new java.awt.Insets(2, 4, 2, 4));
        PopSizeHelpButton.setMinimumSize(new java.awt.Dimension(20, 20));
        PopSizeHelpButton.setOpaque(false);
        PopSizeHelpButton.setPreferredSize(new java.awt.Dimension(20, 20));
        PopSizeHelpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PopSizeHelpButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        parkinglotOptionPanel.add(PopSizeHelpButton, gridBagConstraints);

        PopSizeCBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        PopSizeCBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "100", "200", "300", "500", "1000" }));
        PopSizeCBox.setMinimumSize(new java.awt.Dimension(70, 23));
        PopSizeCBox.setName("MessageMaxLineComboBox"); // NOI18N
        PopSizeCBox.setPreferredSize(new java.awt.Dimension(70, 23));
        PopSizeCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PopSizeCBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.ipadx = 19;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        parkinglotOptionPanel.add(PopSizeCBox, gridBagConstraints);

        RecordPassingDelayChkBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        RecordPassingDelayChkBox.setText(RECORD_PASSING_LABEL.getContent());
        RecordPassingDelayChkBox.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        RecordPassingDelayChkBox.setName("RecordPassingDelayChkBox"); // NOI18N
        RecordPassingDelayChkBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RecordPassingDelayChkBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        parkinglotOptionPanel.add(RecordPassingDelayChkBox, gridBagConstraints);

        wholePanel.add(parkinglotOptionPanel);

        gateSettingPanel.setMinimumSize(new java.awt.Dimension(430, 250));
        gateSettingPanel.setPreferredSize(new java.awt.Dimension(700, 250));

        GatesTabbedPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        GatesTabbedPane.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        GatesTabbedPane.setMinimumSize(new java.awt.Dimension(300, 250));
        GatesTabbedPane.setPreferredSize(new java.awt.Dimension(520, 250));

        gate1Panel.setEnabled(false);
        gate1Panel.setMaximumSize(new java.awt.Dimension(49151, 196));
        gate1Panel.setMinimumSize(new java.awt.Dimension(300, 196));
        gate1Panel.setPreferredSize(new java.awt.Dimension(518, 196));
        gate1Panel.setLayout(new javax.swing.BoxLayout(gate1Panel, javax.swing.BoxLayout.Y_AXIS));

        gate_name_p.setAlignmentX(1.0F);
        gate_name_p.setMaximumSize(new java.awt.Dimension(32767, 50));
        gate_name_p.setMinimumSize(new java.awt.Dimension(109, 40));
        gate_name_p.setPreferredSize(new java.awt.Dimension(100, 50));
        gate_name_p.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 10));
        gate_name_p.add(filler7);

        gateNameLabel1.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        gateNameLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        gateNameLabel1.setText(GATE_NAME_LABEL.getContent());
        gateNameLabel1.setToolTipText("");
        gate_name_p.add(gateNameLabel1);

        TextFieldGateName1.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        TextFieldGateName1.setText("Front Gate");
        TextFieldGateName1.setToolTipText("");
        TextFieldGateName1.setMaximumSize(new java.awt.Dimension(2147483647, 30));
        TextFieldGateName1.setMinimumSize(new java.awt.Dimension(120, 30));
        TextFieldGateName1.setName("TextFieldGateName1"); // NOI18N
        TextFieldGateName1.setPreferredSize(new java.awt.Dimension(120, 30));
        TextFieldGateName1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldGateName1KeyReleased(evt);
            }
        });
        gate_name_p.add(TextFieldGateName1);

        gate1Panel.add(gate_name_p);

        topLabelsPanel.setMaximumSize(new java.awt.Dimension(32767, 40));
        topLabelsPanel.setMinimumSize(new java.awt.Dimension(266, 30));
        topLabelsPanel.setPreferredSize(new java.awt.Dimension(100, 30));
        topLabelsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 15));
        topLabelsPanel.add(filler9);

        device1_Label.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        device1_Label.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        device1_Label.setText(DEVICE_LABEL.getContent());
        device1_Label.setPreferredSize(new java.awt.Dimension(60, 15));
        topLabelsPanel.add(device1_Label);

        jLabel26.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText(TYPE_LABEL.getContent());
        jLabel26.setPreferredSize(new java.awt.Dimension(125, 15));
        topLabelsPanel.add(jLabel26);

        jLabel27.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText(CONN_LABEL.getContent());
        jLabel27.setPreferredSize(new java.awt.Dimension(90, 15));
        topLabelsPanel.add(jLabel27);

        jLabel28.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText(IP_ADDR_LABEL.getContent());
        jLabel28.setPreferredSize(new java.awt.Dimension(125, 15));
        topLabelsPanel.add(jLabel28);

        jLabel29.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText(PORT_LABEL.getContent());
        jLabel29.setPreferredSize(new java.awt.Dimension(45, 15));
        topLabelsPanel.add(jLabel29);
        jLabel29.getAccessibleContext().setAccessibleName("PortLbl");

        gate1Panel.add(topLabelsPanel);

        cameraPan.setMaximumSize(new java.awt.Dimension(32767, 40));
        cameraPan.setMinimumSize(new java.awt.Dimension(426, 32));
        cameraPan.setPreferredSize(new java.awt.Dimension(518, 32));
        cameraPan.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        cameraPan.add(filler10);

        jLabel30.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel30.setText(CAMERA_LABEL.getContent());
        jLabel30.setMinimumSize(new java.awt.Dimension(60, 15));
        jLabel30.setPreferredSize(new java.awt.Dimension(70, 15));
        cameraPan.add(jLabel30);

        Camera1_TypeCBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        Camera1_TypeCBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "list e-board types" }));
        Camera1_TypeCBox.setToolTipText("");
        Camera1_TypeCBox.setLightWeightPopupEnabled(false);
        Camera1_TypeCBox.setMinimumSize(new java.awt.Dimension(115, 25));
        Camera1_TypeCBox.setName("Camera1_TypeCBox"); // NOI18N
        Camera1_TypeCBox.setPreferredSize(new java.awt.Dimension(115, 27));
        Camera1_TypeCBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                Camera1_TypeCBoxItemStateChanged(evt);
            }
        });
        cameraPan.add(Camera1_TypeCBox);

        Camera1_connTypeCBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        Camera1_connTypeCBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TCP/IP", "RS-232" }));
        Camera1_connTypeCBox.setEnabled(false);
        Camera1_connTypeCBox.setMinimumSize(new java.awt.Dimension(80, 23));
        Camera1_connTypeCBox.setName("Camera1_connTypeCBox"); // NOI18N
        Camera1_connTypeCBox.setPreferredSize(new java.awt.Dimension(90, 27));
        cameraPan.add(Camera1_connTypeCBox);

        Camera1_IP_TextField.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        Camera1_IP_TextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        Camera1_IP_TextField.setText("127.0.0.1");
        Camera1_IP_TextField.setToolTipText("");
        Camera1_IP_TextField.setMaximumSize(new java.awt.Dimension(32767, 32767));
        Camera1_IP_TextField.setMinimumSize(new java.awt.Dimension(125, 25));
        Camera1_IP_TextField.setName("Camera1_IP_TextField"); // NOI18N
        Camera1_IP_TextField.setPreferredSize(new java.awt.Dimension(125, 27));
        Camera1_IP_TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Camera1_IP_TextFieldKeyReleased(evt);
            }
        });
        cameraPan.add(Camera1_IP_TextField);

        Camera1_Port_TextField.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        Camera1_Port_TextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        Camera1_Port_TextField.setText("8080");
        Camera1_Port_TextField.setToolTipText("");
        Camera1_Port_TextField.setMaximumSize(new java.awt.Dimension(32767, 32767));
        Camera1_Port_TextField.setMinimumSize(new java.awt.Dimension(40, 25));
        Camera1_Port_TextField.setName("Camera1_Port_TextField"); // NOI18N
        Camera1_Port_TextField.setPreferredSize(new java.awt.Dimension(57, 27));
        Camera1_Port_TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Camera1_Port_TextFieldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                Camera1_Port_TextFieldKeyTyped(evt);
            }
        });
        cameraPan.add(Camera1_Port_TextField);

        gate1Panel.add(cameraPan);

        eBoardPan5.setMaximumSize(new java.awt.Dimension(32767, 40));
        eBoardPan5.setMinimumSize(new java.awt.Dimension(426, 32));
        eBoardPan5.setPreferredSize(new java.awt.Dimension(518, 32));
        eBoardPan5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        eBoardPan5.add(filler35);

        jLabel63.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel63.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel63.setText(E_BOARD_LABEL.getContent());
        jLabel63.setMinimumSize(new java.awt.Dimension(60, 15));
        jLabel63.setPreferredSize(new java.awt.Dimension(70, 15));
        eBoardPan5.add(jLabel63);

        E_Board1_TypeCBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        E_Board1_TypeCBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "list e-board types" }));
        E_Board1_TypeCBox.setToolTipText("");
        E_Board1_TypeCBox.setMinimumSize(new java.awt.Dimension(115, 25));
        E_Board1_TypeCBox.setName("E_Board1_TypeCBox"); // NOI18N
        E_Board1_TypeCBox.setPreferredSize(new java.awt.Dimension(115, 27));
        E_Board1_TypeCBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                E_Board1_TypeCBoxItemStateChanged(evt);
            }
        });
        E_Board1_TypeCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                E_Board1_TypeCBoxActionPerformed(evt);
            }
        });
        eBoardPan5.add(E_Board1_TypeCBox);

        E_Board1_connTypeCBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        E_Board1_connTypeCBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TCP/IP", "RS-232" }));
        E_Board1_connTypeCBox.setMinimumSize(new java.awt.Dimension(80, 23));
        E_Board1_connTypeCBox.setName("E_Board1_connTypeCBox"); // NOI18N
        E_Board1_connTypeCBox.setPreferredSize(new java.awt.Dimension(90, 27));
        E_Board1_connTypeCBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                E_Board1_connTypeCBoxItemStateChanged(evt);
            }
        });
        E_Board1_connTypeCBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                E_Board1_connTypeCBoxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        E_Board1_connTypeCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                E_Board1_connTypeCBoxActionPerformed(evt);
            }
        });
        eBoardPan5.add(E_Board1_connTypeCBox);

        E_Board1_IP_TextField.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        E_Board1_IP_TextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        E_Board1_IP_TextField.setText("127.0.0.1");
        E_Board1_IP_TextField.setMinimumSize(new java.awt.Dimension(125, 25));
        E_Board1_IP_TextField.setName("E_Board1_IP_TextField"); // NOI18N
        E_Board1_IP_TextField.setPreferredSize(new java.awt.Dimension(125, 27));
        E_Board1_IP_TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                E_Board1_IP_TextFieldKeyReleased(evt);
            }
        });
        eBoardPan5.add(E_Board1_IP_TextField);

        E_Board1_Port_TextField.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        E_Board1_Port_TextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        E_Board1_Port_TextField.setMinimumSize(new java.awt.Dimension(40, 25));
        E_Board1_Port_TextField.setName("E_Board1_Port_TextField"); // NOI18N
        E_Board1_Port_TextField.setPreferredSize(new java.awt.Dimension(57, 27));
        E_Board1_Port_TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                E_Board1_Port_TextFieldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                E_Board1_Port_TextFieldKeyTyped(evt);
            }
        });
        eBoardPan5.add(E_Board1_Port_TextField);

        gate1Panel.add(eBoardPan5);

        gateBarPan5.setMaximumSize(new java.awt.Dimension(32767, 40));
        gateBarPan5.setMinimumSize(new java.awt.Dimension(426, 32));
        gateBarPan5.setPreferredSize(new java.awt.Dimension(518, 32));
        gateBarPan5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        gateBarPan5.add(filler36);

        ebdLbl5.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        ebdLbl5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        ebdLbl5.setText(GATE_BAR_LABEL.getContent());
        ebdLbl5.setMinimumSize(new java.awt.Dimension(60, 15));
        ebdLbl5.setPreferredSize(new java.awt.Dimension(70, 15));
        gateBarPan5.add(ebdLbl5);

        GateBar1_TypeCBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        GateBar1_TypeCBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "list e-board types" }));
        GateBar1_TypeCBox.setToolTipText("");
        GateBar1_TypeCBox.setMinimumSize(new java.awt.Dimension(115, 25));
        GateBar1_TypeCBox.setName("GateBar1_TypeCBox"); // NOI18N
        GateBar1_TypeCBox.setPreferredSize(new java.awt.Dimension(115, 27));
        GateBar1_TypeCBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                GateBar1_TypeCBoxItemStateChanged(evt);
            }
        });
        GateBar1_TypeCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GateBar1_TypeCBoxActionPerformed(evt);
            }
        });
        gateBarPan5.add(GateBar1_TypeCBox);

        GateBar1_connTypeCBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        GateBar1_connTypeCBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TCP/IP", "RS-232" }));
        GateBar1_connTypeCBox.setMinimumSize(new java.awt.Dimension(80, 23));
        GateBar1_connTypeCBox.setName("GateBar1_connTypeCBox"); // NOI18N
        GateBar1_connTypeCBox.setPreferredSize(new java.awt.Dimension(90, 27));
        GateBar1_connTypeCBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                GateBar1_connTypeCBoxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        GateBar1_connTypeCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GateBar1_connTypeCBoxActionPerformed(evt);
            }
        });
        gateBarPan5.add(GateBar1_connTypeCBox);

        GateBar1_IP_TextField.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        GateBar1_IP_TextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        GateBar1_IP_TextField.setText("127.0.0.1");
        GateBar1_IP_TextField.setMinimumSize(new java.awt.Dimension(125, 25));
        GateBar1_IP_TextField.setName("GateBar1_IP_TextField"); // NOI18N
        GateBar1_IP_TextField.setPreferredSize(new java.awt.Dimension(125, 27));
        GateBar1_IP_TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                GateBar1_IP_TextFieldKeyReleased(evt);
            }
        });
        gateBarPan5.add(GateBar1_IP_TextField);

        GateBar1_Port_TextField.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        GateBar1_Port_TextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        GateBar1_Port_TextField.setMinimumSize(new java.awt.Dimension(40, 25));
        GateBar1_Port_TextField.setName("GateBar1_Port_TextField"); // NOI18N
        GateBar1_Port_TextField.setPreferredSize(new java.awt.Dimension(57, 27));
        GateBar1_Port_TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                GateBar1_Port_TextFieldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                GateBar1_Port_TextFieldKeyTyped(evt);
            }
        });
        gateBarPan5.add(GateBar1_Port_TextField);

        gate1Panel.add(gateBarPan5);

        GatesTabbedPane.addTab(GATE_LABEL.getContent() + "1", gate1Panel);

        gate2Panel.setEnabled(false);
        gate2Panel.setMinimumSize(new java.awt.Dimension(300, 115));
        gate2Panel.setPreferredSize(new java.awt.Dimension(518, 196));
        gate2Panel.setLayout(new javax.swing.BoxLayout(gate2Panel, javax.swing.BoxLayout.Y_AXIS));

        gate_name_p4.setAlignmentX(1.0F);
        gate_name_p4.setMaximumSize(new java.awt.Dimension(32767, 50));
        gate_name_p4.setMinimumSize(new java.awt.Dimension(109, 40));
        gate_name_p4.setPreferredSize(new java.awt.Dimension(100, 50));
        gate_name_p4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 10));
        gate_name_p4.add(filler30);

        gateNameLabel5.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        gateNameLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        gateNameLabel5.setText(GATE_NAME_LABEL.getContent());
        gateNameLabel5.setToolTipText("");
        gate_name_p4.add(gateNameLabel5);

        TextFieldGateName2.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        TextFieldGateName2.setText("Front Gate");
        TextFieldGateName2.setToolTipText("");
        TextFieldGateName2.setMaximumSize(new java.awt.Dimension(2147483647, 30));
        TextFieldGateName2.setMinimumSize(new java.awt.Dimension(120, 30));
        TextFieldGateName2.setName("TextFieldGateName2"); // NOI18N
        TextFieldGateName2.setPreferredSize(new java.awt.Dimension(120, 30));
        TextFieldGateName2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldGateName2KeyReleased(evt);
            }
        });
        gate_name_p4.add(TextFieldGateName2);

        gate2Panel.add(gate_name_p4);

        topLabelsPanel4.setMaximumSize(new java.awt.Dimension(32767, 40));
        topLabelsPanel4.setMinimumSize(new java.awt.Dimension(266, 30));
        topLabelsPanel4.setPreferredSize(new java.awt.Dimension(100, 30));
        topLabelsPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 15));
        topLabelsPanel4.add(filler31);

        device1_Label4.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        device1_Label4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        device1_Label4.setText(DEVICE_LABEL.getContent());
        device1_Label4.setPreferredSize(new java.awt.Dimension(60, 15));
        topLabelsPanel4.add(device1_Label4);

        jLabel57.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel57.setText(TYPE_LABEL.getContent());
        jLabel57.setPreferredSize(new java.awt.Dimension(125, 15));
        topLabelsPanel4.add(jLabel57);

        jLabel58.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel58.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel58.setText(CONN_LABEL.getContent());
        jLabel58.setPreferredSize(new java.awt.Dimension(90, 15));
        topLabelsPanel4.add(jLabel58);

        jLabel59.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel59.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel59.setText(IP_ADDR_LABEL.getContent());
        jLabel59.setPreferredSize(new java.awt.Dimension(125, 15));
        topLabelsPanel4.add(jLabel59);

        jLabel60.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel60.setText(PORT_LABEL.getContent());
        jLabel60.setPreferredSize(new java.awt.Dimension(45, 15));
        topLabelsPanel4.add(jLabel60);

        gate2Panel.add(topLabelsPanel4);

        cameraPan4.setMaximumSize(new java.awt.Dimension(32767, 40));
        cameraPan4.setMinimumSize(new java.awt.Dimension(426, 32));
        cameraPan4.setPreferredSize(new java.awt.Dimension(518, 32));
        cameraPan4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        cameraPan4.add(filler32);

        jLabel61.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel61.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel61.setText(CAMERA_LABEL.getContent());
        jLabel61.setMinimumSize(new java.awt.Dimension(60, 15));
        jLabel61.setPreferredSize(new java.awt.Dimension(70, 15));
        cameraPan4.add(jLabel61);

        Camera2_TypeCBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        Camera2_TypeCBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "list e-board types" }));
        Camera2_TypeCBox.setToolTipText("");
        Camera2_TypeCBox.setLightWeightPopupEnabled(false);
        Camera2_TypeCBox.setMinimumSize(new java.awt.Dimension(115, 25));
        Camera2_TypeCBox.setName("Camera2_TypeCBox"); // NOI18N
        Camera2_TypeCBox.setPreferredSize(new java.awt.Dimension(115, 27));
        Camera2_TypeCBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                Camera2_TypeCBoxItemStateChanged(evt);
            }
        });
        cameraPan4.add(Camera2_TypeCBox);

        Camera2_connTypeCBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        Camera2_connTypeCBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TCP/IP", "RS-232" }));
        Camera2_connTypeCBox.setEnabled(false);
        Camera2_connTypeCBox.setMinimumSize(new java.awt.Dimension(80, 23));
        Camera2_connTypeCBox.setName("Camera2_connTypeCBox"); // NOI18N
        Camera2_connTypeCBox.setPreferredSize(new java.awt.Dimension(90, 27));
        Camera2_connTypeCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Camera2_connTypeCBoxActionPerformed(evt);
            }
        });
        cameraPan4.add(Camera2_connTypeCBox);

        Camera2_IP_TextField.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        Camera2_IP_TextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        Camera2_IP_TextField.setText("127.0.0.1");
        Camera2_IP_TextField.setToolTipText("");
        Camera2_IP_TextField.setMaximumSize(new java.awt.Dimension(32767, 32767));
        Camera2_IP_TextField.setMinimumSize(new java.awt.Dimension(125, 25));
        Camera2_IP_TextField.setName("Camera2_IP_TextField"); // NOI18N
        Camera2_IP_TextField.setPreferredSize(new java.awt.Dimension(125, 27));
        Camera2_IP_TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Camera2_IP_TextFieldKeyReleased(evt);
            }
        });
        cameraPan4.add(Camera2_IP_TextField);

        Camera2_Port_TextField.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        Camera2_Port_TextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        Camera2_Port_TextField.setText("8080");
        Camera2_Port_TextField.setToolTipText("");
        Camera2_Port_TextField.setMaximumSize(new java.awt.Dimension(32767, 32767));
        Camera2_Port_TextField.setMinimumSize(new java.awt.Dimension(40, 25));
        Camera2_Port_TextField.setName("Camera2_Port_TextField"); // NOI18N
        Camera2_Port_TextField.setPreferredSize(new java.awt.Dimension(57, 27));
        Camera2_Port_TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Camera2_Port_TextFieldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                Camera2_Port_TextFieldKeyTyped(evt);
            }
        });
        cameraPan4.add(Camera2_Port_TextField);

        gate2Panel.add(cameraPan4);

        eBoardPan4.setMaximumSize(new java.awt.Dimension(32767, 40));
        eBoardPan4.setMinimumSize(new java.awt.Dimension(426, 32));
        eBoardPan4.setPreferredSize(new java.awt.Dimension(518, 32));
        eBoardPan4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        eBoardPan4.add(filler33);

        jLabel62.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel62.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel62.setText(E_BOARD_LABEL.getContent());
        jLabel62.setMinimumSize(new java.awt.Dimension(60, 15));
        jLabel62.setPreferredSize(new java.awt.Dimension(70, 15));
        eBoardPan4.add(jLabel62);

        E_Board2_TypeCBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        E_Board2_TypeCBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "list e-board types" }));
        E_Board2_TypeCBox.setToolTipText("");
        E_Board2_TypeCBox.setMinimumSize(new java.awt.Dimension(115, 25));
        E_Board2_TypeCBox.setName("E_Board2_TypeCBox"); // NOI18N
        E_Board2_TypeCBox.setPreferredSize(new java.awt.Dimension(115, 27));
        E_Board2_TypeCBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                E_Board2_TypeCBoxItemStateChanged(evt);
            }
        });
        E_Board2_TypeCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                E_Board2_TypeCBoxActionPerformed(evt);
            }
        });
        eBoardPan4.add(E_Board2_TypeCBox);

        E_Board2_connTypeCBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        E_Board2_connTypeCBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TCP/IP", "RS-232" }));
        E_Board2_connTypeCBox.setMinimumSize(new java.awt.Dimension(80, 23));
        E_Board2_connTypeCBox.setName("E_Board2_connTypeCBox"); // NOI18N
        E_Board2_connTypeCBox.setPreferredSize(new java.awt.Dimension(90, 27));
        E_Board2_connTypeCBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                E_Board2_connTypeCBoxItemStateChanged(evt);
            }
        });
        E_Board2_connTypeCBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                E_Board2_connTypeCBoxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        E_Board2_connTypeCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                E_Board2_connTypeCBoxActionPerformed(evt);
            }
        });
        eBoardPan4.add(E_Board2_connTypeCBox);

        E_Board2_IP_TextField.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        E_Board2_IP_TextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        E_Board2_IP_TextField.setText("127.0.0.1");
        E_Board2_IP_TextField.setMinimumSize(new java.awt.Dimension(125, 25));
        E_Board2_IP_TextField.setName("E_Board2_IP_TextField"); // NOI18N
        E_Board2_IP_TextField.setPreferredSize(new java.awt.Dimension(125, 27));
        E_Board2_IP_TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                E_Board2_IP_TextFieldKeyReleased(evt);
            }
        });
        eBoardPan4.add(E_Board2_IP_TextField);

        E_Board2_Port_TextField.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        E_Board2_Port_TextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        E_Board2_Port_TextField.setMinimumSize(new java.awt.Dimension(40, 25));
        E_Board2_Port_TextField.setName("E_Board2_Port_TextField"); // NOI18N
        E_Board2_Port_TextField.setPreferredSize(new java.awt.Dimension(57, 27));
        E_Board2_Port_TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                E_Board2_Port_TextFieldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                E_Board2_Port_TextFieldKeyTyped(evt);
            }
        });
        eBoardPan4.add(E_Board2_Port_TextField);

        gate2Panel.add(eBoardPan4);

        gateBarPan4.setMaximumSize(new java.awt.Dimension(32767, 40));
        gateBarPan4.setMinimumSize(new java.awt.Dimension(426, 32));
        gateBarPan4.setPreferredSize(new java.awt.Dimension(518, 32));
        gateBarPan4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        gateBarPan4.add(filler34);

        ebdLbl4.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        ebdLbl4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        ebdLbl4.setText(GATE_BAR_LABEL.getContent());
        ebdLbl4.setMinimumSize(new java.awt.Dimension(60, 15));
        ebdLbl4.setPreferredSize(new java.awt.Dimension(70, 15));
        gateBarPan4.add(ebdLbl4);

        GateBar2_TypeCBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        GateBar2_TypeCBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "list e-board types" }));
        GateBar2_TypeCBox.setToolTipText("");
        GateBar2_TypeCBox.setMinimumSize(new java.awt.Dimension(115, 25));
        GateBar2_TypeCBox.setName("GateBar2_TypeCBox"); // NOI18N
        GateBar2_TypeCBox.setPreferredSize(new java.awt.Dimension(115, 27));
        GateBar2_TypeCBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                GateBar2_TypeCBoxItemStateChanged(evt);
            }
        });
        GateBar2_TypeCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GateBar2_TypeCBoxActionPerformed(evt);
            }
        });
        gateBarPan4.add(GateBar2_TypeCBox);

        GateBar2_connTypeCBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        GateBar2_connTypeCBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TCP/IP", "RS-232" }));
        GateBar2_connTypeCBox.setMinimumSize(new java.awt.Dimension(80, 23));
        GateBar2_connTypeCBox.setName("GateBar2_connTypeCBox"); // NOI18N
        GateBar2_connTypeCBox.setPreferredSize(new java.awt.Dimension(90, 27));
        GateBar2_connTypeCBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                GateBar2_connTypeCBoxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        GateBar2_connTypeCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GateBar2_connTypeCBoxActionPerformed(evt);
            }
        });
        gateBarPan4.add(GateBar2_connTypeCBox);

        GateBar2_IP_TextField.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        GateBar2_IP_TextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        GateBar2_IP_TextField.setText("127.0.0.1");
        GateBar2_IP_TextField.setMinimumSize(new java.awt.Dimension(125, 25));
        GateBar2_IP_TextField.setName("GateBar2_IP_TextField"); // NOI18N
        GateBar2_IP_TextField.setPreferredSize(new java.awt.Dimension(125, 27));
        GateBar2_IP_TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                GateBar2_IP_TextFieldKeyReleased(evt);
            }
        });
        gateBarPan4.add(GateBar2_IP_TextField);

        GateBar2_Port_TextField.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        GateBar2_Port_TextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        GateBar2_Port_TextField.setMinimumSize(new java.awt.Dimension(40, 25));
        GateBar2_Port_TextField.setName("GateBar2_Port_TextField"); // NOI18N
        GateBar2_Port_TextField.setPreferredSize(new java.awt.Dimension(57, 27));
        GateBar2_Port_TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                GateBar2_Port_TextFieldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                GateBar2_Port_TextFieldKeyTyped(evt);
            }
        });
        gateBarPan4.add(GateBar2_Port_TextField);

        gate2Panel.add(gateBarPan4);

        GatesTabbedPane.addTab(GATE_LABEL.getContent() + "2", gate2Panel);

        gate3Panel.setEnabled(false);
        gate3Panel.setMinimumSize(new java.awt.Dimension(300, 115));
        gate3Panel.setPreferredSize(new java.awt.Dimension(518, 196));
        gate3Panel.setLayout(new javax.swing.BoxLayout(gate3Panel, javax.swing.BoxLayout.Y_AXIS));

        gate_name_p2.setAlignmentX(1.0F);
        gate_name_p2.setMaximumSize(new java.awt.Dimension(32767, 50));
        gate_name_p2.setMinimumSize(new java.awt.Dimension(109, 40));
        gate_name_p2.setPreferredSize(new java.awt.Dimension(100, 50));
        gate_name_p2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 10));
        gate_name_p2.add(filler13);

        gateNameLabel3.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        gateNameLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        gateNameLabel3.setText(GATE_NAME_LABEL.getContent());
        gateNameLabel3.setToolTipText("");
        gate_name_p2.add(gateNameLabel3);

        TextFieldGateName3.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        TextFieldGateName3.setText("Front Gate");
        TextFieldGateName3.setToolTipText("");
        TextFieldGateName3.setMaximumSize(new java.awt.Dimension(2147483647, 30));
        TextFieldGateName3.setMinimumSize(new java.awt.Dimension(120, 30));
        TextFieldGateName3.setName("TextFieldGateName3"); // NOI18N
        TextFieldGateName3.setPreferredSize(new java.awt.Dimension(120, 30));
        TextFieldGateName3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldGateName3KeyReleased(evt);
            }
        });
        gate_name_p2.add(TextFieldGateName3);

        gate3Panel.add(gate_name_p2);

        topLabelsPanel2.setMaximumSize(new java.awt.Dimension(32767, 40));
        topLabelsPanel2.setMinimumSize(new java.awt.Dimension(266, 30));
        topLabelsPanel2.setPreferredSize(new java.awt.Dimension(100, 30));
        topLabelsPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 15));
        topLabelsPanel2.add(filler14);

        device1_Label2.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        device1_Label2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        device1_Label2.setText(DEVICE_LABEL.getContent());
        device1_Label2.setPreferredSize(new java.awt.Dimension(60, 15));
        topLabelsPanel2.add(device1_Label2);

        jLabel36.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText(TYPE_LABEL.getContent());
        jLabel36.setPreferredSize(new java.awt.Dimension(125, 15));
        topLabelsPanel2.add(jLabel36);

        jLabel46.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setText(CONN_LABEL.getContent());
        jLabel46.setPreferredSize(new java.awt.Dimension(90, 15));
        topLabelsPanel2.add(jLabel46);

        jLabel47.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel47.setText(IP_ADDR_LABEL.getContent());
        jLabel47.setPreferredSize(new java.awt.Dimension(125, 15));
        topLabelsPanel2.add(jLabel47);

        jLabel48.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel48.setText(PORT_LABEL.getContent());
        jLabel48.setPreferredSize(new java.awt.Dimension(45, 15));
        topLabelsPanel2.add(jLabel48);

        gate3Panel.add(topLabelsPanel2);

        cameraPan2.setMaximumSize(new java.awt.Dimension(32767, 40));
        cameraPan2.setMinimumSize(new java.awt.Dimension(426, 32));
        cameraPan2.setPreferredSize(new java.awt.Dimension(518, 32));
        cameraPan2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        cameraPan2.add(filler15);

        jLabel49.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel49.setText(CAMERA_LABEL.getContent());
        jLabel49.setMinimumSize(new java.awt.Dimension(60, 15));
        jLabel49.setPreferredSize(new java.awt.Dimension(70, 15));
        cameraPan2.add(jLabel49);

        Camera3_TypeCBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        Camera3_TypeCBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "list e-board types" }));
        Camera3_TypeCBox.setToolTipText("");
        Camera3_TypeCBox.setLightWeightPopupEnabled(false);
        Camera3_TypeCBox.setMinimumSize(new java.awt.Dimension(115, 25));
        Camera3_TypeCBox.setName("Camera3_TypeCBox"); // NOI18N
        Camera3_TypeCBox.setPreferredSize(new java.awt.Dimension(115, 27));
        Camera3_TypeCBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                Camera3_TypeCBoxItemStateChanged(evt);
            }
        });
        cameraPan2.add(Camera3_TypeCBox);

        Camera3_connTypeCBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        Camera3_connTypeCBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TCP/IP", "RS-232" }));
        Camera3_connTypeCBox.setEnabled(false);
        Camera3_connTypeCBox.setMinimumSize(new java.awt.Dimension(80, 23));
        Camera3_connTypeCBox.setName("Camera3_connTypeCBox"); // NOI18N
        Camera3_connTypeCBox.setPreferredSize(new java.awt.Dimension(90, 27));
        Camera3_connTypeCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Camera3_connTypeCBoxActionPerformed(evt);
            }
        });
        cameraPan2.add(Camera3_connTypeCBox);

        Camera3_IP_TextField.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        Camera3_IP_TextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        Camera3_IP_TextField.setText("127.0.0.1");
        Camera3_IP_TextField.setToolTipText("");
        Camera3_IP_TextField.setMaximumSize(new java.awt.Dimension(32767, 32767));
        Camera3_IP_TextField.setMinimumSize(new java.awt.Dimension(125, 25));
        Camera3_IP_TextField.setName("Camera3_IP_TextField"); // NOI18N
        Camera3_IP_TextField.setPreferredSize(new java.awt.Dimension(125, 27));
        Camera3_IP_TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Camera3_IP_TextFieldKeyReleased(evt);
            }
        });
        cameraPan2.add(Camera3_IP_TextField);

        Camera3_Port_TextField.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        Camera3_Port_TextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        Camera3_Port_TextField.setText("8080");
        Camera3_Port_TextField.setToolTipText("");
        Camera3_Port_TextField.setMaximumSize(new java.awt.Dimension(32767, 32767));
        Camera3_Port_TextField.setMinimumSize(new java.awt.Dimension(40, 25));
        Camera3_Port_TextField.setName("Camera3_Port_TextField"); // NOI18N
        Camera3_Port_TextField.setPreferredSize(new java.awt.Dimension(57, 27));
        Camera3_Port_TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Camera3_Port_TextFieldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                Camera3_Port_TextFieldKeyTyped(evt);
            }
        });
        cameraPan2.add(Camera3_Port_TextField);

        gate3Panel.add(cameraPan2);

        eBoardPan2.setMaximumSize(new java.awt.Dimension(32767, 40));
        eBoardPan2.setMinimumSize(new java.awt.Dimension(426, 32));
        eBoardPan2.setPreferredSize(new java.awt.Dimension(518, 32));
        eBoardPan2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        eBoardPan2.add(filler23);

        jLabel50.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel50.setText(E_BOARD_LABEL.getContent());
        jLabel50.setMinimumSize(new java.awt.Dimension(60, 15));
        jLabel50.setPreferredSize(new java.awt.Dimension(70, 15));
        eBoardPan2.add(jLabel50);

        E_Board3_TypeCBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        E_Board3_TypeCBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "list e-board types" }));
        E_Board3_TypeCBox.setToolTipText("");
        E_Board3_TypeCBox.setMinimumSize(new java.awt.Dimension(115, 25));
        E_Board3_TypeCBox.setName("E_Board3_TypeCBox"); // NOI18N
        E_Board3_TypeCBox.setPreferredSize(new java.awt.Dimension(115, 27));
        E_Board3_TypeCBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                E_Board3_TypeCBoxItemStateChanged(evt);
            }
        });
        E_Board3_TypeCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                E_Board3_TypeCBoxActionPerformed(evt);
            }
        });
        eBoardPan2.add(E_Board3_TypeCBox);

        E_Board3_connTypeCBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        E_Board3_connTypeCBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TCP/IP", "RS-232" }));
        E_Board3_connTypeCBox.setMinimumSize(new java.awt.Dimension(80, 23));
        E_Board3_connTypeCBox.setName("E_Board3_connTypeCBox"); // NOI18N
        E_Board3_connTypeCBox.setPreferredSize(new java.awt.Dimension(90, 27));
        E_Board3_connTypeCBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                E_Board3_connTypeCBoxItemStateChanged(evt);
            }
        });
        E_Board3_connTypeCBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                E_Board3_connTypeCBoxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        E_Board3_connTypeCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                E_Board3_connTypeCBoxActionPerformed(evt);
            }
        });
        eBoardPan2.add(E_Board3_connTypeCBox);

        E_Board3_IP_TextField.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        E_Board3_IP_TextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        E_Board3_IP_TextField.setText("127.0.0.1");
        E_Board3_IP_TextField.setMinimumSize(new java.awt.Dimension(125, 25));
        E_Board3_IP_TextField.setName("E_Board3_IP_TextField"); // NOI18N
        E_Board3_IP_TextField.setPreferredSize(new java.awt.Dimension(125, 27));
        E_Board3_IP_TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                E_Board3_IP_TextFieldKeyReleased(evt);
            }
        });
        eBoardPan2.add(E_Board3_IP_TextField);

        E_Board3_Port_TextField.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        E_Board3_Port_TextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        E_Board3_Port_TextField.setMinimumSize(new java.awt.Dimension(40, 25));
        E_Board3_Port_TextField.setName("E_Board3_Port_TextField"); // NOI18N
        E_Board3_Port_TextField.setPreferredSize(new java.awt.Dimension(57, 27));
        E_Board3_Port_TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                E_Board3_Port_TextFieldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                E_Board3_Port_TextFieldKeyTyped(evt);
            }
        });
        eBoardPan2.add(E_Board3_Port_TextField);

        gate3Panel.add(eBoardPan2);

        gateBarPan2.setMaximumSize(new java.awt.Dimension(32767, 40));
        gateBarPan2.setMinimumSize(new java.awt.Dimension(426, 32));
        gateBarPan2.setPreferredSize(new java.awt.Dimension(518, 32));
        gateBarPan2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        gateBarPan2.add(filler24);

        ebdLbl2.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        ebdLbl2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        ebdLbl2.setText(GATE_BAR_LABEL.getContent());
        ebdLbl2.setMinimumSize(new java.awt.Dimension(60, 15));
        ebdLbl2.setPreferredSize(new java.awt.Dimension(70, 15));
        gateBarPan2.add(ebdLbl2);

        GateBar3_TypeCBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        GateBar3_TypeCBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "list e-board types" }));
        GateBar3_TypeCBox.setToolTipText("");
        GateBar3_TypeCBox.setMinimumSize(new java.awt.Dimension(115, 25));
        GateBar3_TypeCBox.setName("GateBar3_TypeCBox"); // NOI18N
        GateBar3_TypeCBox.setPreferredSize(new java.awt.Dimension(115, 27));
        GateBar3_TypeCBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                GateBar3_TypeCBoxItemStateChanged(evt);
            }
        });
        GateBar3_TypeCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GateBar3_TypeCBoxActionPerformed(evt);
            }
        });
        gateBarPan2.add(GateBar3_TypeCBox);

        GateBar3_connTypeCBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        GateBar3_connTypeCBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TCP/IP", "RS-232" }));
        GateBar3_connTypeCBox.setMinimumSize(new java.awt.Dimension(80, 23));
        GateBar3_connTypeCBox.setName("GateBar3_connTypeCBox"); // NOI18N
        GateBar3_connTypeCBox.setPreferredSize(new java.awt.Dimension(90, 27));
        GateBar3_connTypeCBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                GateBar3_connTypeCBoxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        GateBar3_connTypeCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GateBar3_connTypeCBoxActionPerformed(evt);
            }
        });
        gateBarPan2.add(GateBar3_connTypeCBox);

        GateBar3_IP_TextField.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        GateBar3_IP_TextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        GateBar3_IP_TextField.setText("127.0.0.1");
        GateBar3_IP_TextField.setMinimumSize(new java.awt.Dimension(125, 25));
        GateBar3_IP_TextField.setName("GateBar3_IP_TextField"); // NOI18N
        GateBar3_IP_TextField.setPreferredSize(new java.awt.Dimension(125, 27));
        GateBar3_IP_TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                GateBar3_IP_TextFieldKeyReleased(evt);
            }
        });
        gateBarPan2.add(GateBar3_IP_TextField);

        GateBar3_Port_TextField.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        GateBar3_Port_TextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        GateBar3_Port_TextField.setMinimumSize(new java.awt.Dimension(40, 25));
        GateBar3_Port_TextField.setName("GateBar3_Port_TextField"); // NOI18N
        GateBar3_Port_TextField.setPreferredSize(new java.awt.Dimension(57, 27));
        GateBar3_Port_TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                GateBar3_Port_TextFieldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                GateBar3_Port_TextFieldKeyTyped(evt);
            }
        });
        gateBarPan2.add(GateBar3_Port_TextField);

        gate3Panel.add(gateBarPan2);

        GatesTabbedPane.addTab(GATE_LABEL.getContent() + "3", gate3Panel);

        gate4Panel.setEnabled(false);
        gate4Panel.setMinimumSize(new java.awt.Dimension(300, 115));
        gate4Panel.setPreferredSize(new java.awt.Dimension(518, 196));
        gate4Panel.setLayout(new javax.swing.BoxLayout(gate4Panel, javax.swing.BoxLayout.Y_AXIS));

        gate_name_p3.setAlignmentX(1.0F);
        gate_name_p3.setMaximumSize(new java.awt.Dimension(32767, 50));
        gate_name_p3.setMinimumSize(new java.awt.Dimension(109, 40));
        gate_name_p3.setPreferredSize(new java.awt.Dimension(100, 50));
        gate_name_p3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 10));
        gate_name_p3.add(filler25);

        gateNameLabel4.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        gateNameLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        gateNameLabel4.setText(GATE_NAME_LABEL.getContent());
        gateNameLabel4.setToolTipText("");
        gate_name_p3.add(gateNameLabel4);

        TextFieldGateName4.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        TextFieldGateName4.setText("Front Gate");
        TextFieldGateName4.setToolTipText("");
        TextFieldGateName4.setMaximumSize(new java.awt.Dimension(2147483647, 30));
        TextFieldGateName4.setMinimumSize(new java.awt.Dimension(120, 30));
        TextFieldGateName4.setName("TextFieldGateName4"); // NOI18N
        TextFieldGateName4.setPreferredSize(new java.awt.Dimension(120, 30));
        TextFieldGateName4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldGateName4KeyReleased(evt);
            }
        });
        gate_name_p3.add(TextFieldGateName4);

        gate4Panel.add(gate_name_p3);

        topLabelsPanel3.setMaximumSize(new java.awt.Dimension(32767, 40));
        topLabelsPanel3.setMinimumSize(new java.awt.Dimension(266, 30));
        topLabelsPanel3.setPreferredSize(new java.awt.Dimension(100, 30));
        topLabelsPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 15));
        topLabelsPanel3.add(filler26);

        device1_Label3.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        device1_Label3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        device1_Label3.setText(DEVICE_LABEL.getContent());
        device1_Label3.setPreferredSize(new java.awt.Dimension(60, 15));
        topLabelsPanel3.add(device1_Label3);

        jLabel51.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel51.setText(TYPE_LABEL.getContent());
        jLabel51.setPreferredSize(new java.awt.Dimension(125, 15));
        topLabelsPanel3.add(jLabel51);

        jLabel52.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel52.setText(CONN_LABEL.getContent());
        jLabel52.setPreferredSize(new java.awt.Dimension(90, 15));
        topLabelsPanel3.add(jLabel52);

        jLabel53.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel53.setText(IP_ADDR_LABEL.getContent());
        jLabel53.setPreferredSize(new java.awt.Dimension(125, 15));
        topLabelsPanel3.add(jLabel53);

        jLabel54.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel54.setText(PORT_LABEL.getContent());
        jLabel54.setPreferredSize(new java.awt.Dimension(45, 15));
        topLabelsPanel3.add(jLabel54);

        gate4Panel.add(topLabelsPanel3);

        cameraPan3.setMaximumSize(new java.awt.Dimension(32767, 40));
        cameraPan3.setMinimumSize(new java.awt.Dimension(426, 32));
        cameraPan3.setPreferredSize(new java.awt.Dimension(518, 32));
        cameraPan3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        cameraPan3.add(filler27);

        jLabel55.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel55.setText(CAMERA_LABEL.getContent());
        jLabel55.setMinimumSize(new java.awt.Dimension(60, 15));
        jLabel55.setPreferredSize(new java.awt.Dimension(70, 15));
        cameraPan3.add(jLabel55);

        Camera4_TypeCBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        Camera4_TypeCBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "list e-board types" }));
        Camera4_TypeCBox.setToolTipText("");
        Camera4_TypeCBox.setLightWeightPopupEnabled(false);
        Camera4_TypeCBox.setMinimumSize(new java.awt.Dimension(115, 25));
        Camera4_TypeCBox.setName("Camera4_TypeCBox"); // NOI18N
        Camera4_TypeCBox.setPreferredSize(new java.awt.Dimension(115, 27));
        Camera4_TypeCBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                Camera4_TypeCBoxItemStateChanged(evt);
            }
        });
        cameraPan3.add(Camera4_TypeCBox);

        Camera4_connTypeCBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        Camera4_connTypeCBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TCP/IP", "RS-232" }));
        Camera4_connTypeCBox.setEnabled(false);
        Camera4_connTypeCBox.setMinimumSize(new java.awt.Dimension(80, 23));
        Camera4_connTypeCBox.setName("Camera4_connTypeCBox"); // NOI18N
        Camera4_connTypeCBox.setPreferredSize(new java.awt.Dimension(90, 27));
        Camera4_connTypeCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Camera4_connTypeCBoxActionPerformed(evt);
            }
        });
        cameraPan3.add(Camera4_connTypeCBox);

        Camera4_IP_TextField.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        Camera4_IP_TextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        Camera4_IP_TextField.setText("127.0.0.1");
        Camera4_IP_TextField.setToolTipText("");
        Camera4_IP_TextField.setMaximumSize(new java.awt.Dimension(32767, 32767));
        Camera4_IP_TextField.setMinimumSize(new java.awt.Dimension(125, 25));
        Camera4_IP_TextField.setName("Camera4_IP_TextField"); // NOI18N
        Camera4_IP_TextField.setPreferredSize(new java.awt.Dimension(125, 27));
        Camera4_IP_TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Camera4_IP_TextFieldKeyReleased(evt);
            }
        });
        cameraPan3.add(Camera4_IP_TextField);

        Camera4_Port_TextField.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        Camera4_Port_TextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        Camera4_Port_TextField.setText("8080");
        Camera4_Port_TextField.setToolTipText("");
        Camera4_Port_TextField.setMaximumSize(new java.awt.Dimension(32767, 32767));
        Camera4_Port_TextField.setMinimumSize(new java.awt.Dimension(40, 25));
        Camera4_Port_TextField.setName("Camera4_Port_TextField"); // NOI18N
        Camera4_Port_TextField.setPreferredSize(new java.awt.Dimension(57, 27));
        Camera4_Port_TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Camera4_Port_TextFieldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                Camera4_Port_TextFieldKeyTyped(evt);
            }
        });
        cameraPan3.add(Camera4_Port_TextField);

        gate4Panel.add(cameraPan3);

        eBoardPan3.setMaximumSize(new java.awt.Dimension(32767, 40));
        eBoardPan3.setMinimumSize(new java.awt.Dimension(426, 32));
        eBoardPan3.setPreferredSize(new java.awt.Dimension(518, 32));
        eBoardPan3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        eBoardPan3.add(filler28);

        jLabel56.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel56.setText(E_BOARD_LABEL.getContent());
        jLabel56.setMinimumSize(new java.awt.Dimension(60, 15));
        jLabel56.setPreferredSize(new java.awt.Dimension(70, 15));
        eBoardPan3.add(jLabel56);

        E_Board4_TypeCBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        E_Board4_TypeCBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "list e-board types" }));
        E_Board4_TypeCBox.setToolTipText("");
        E_Board4_TypeCBox.setMinimumSize(new java.awt.Dimension(115, 25));
        E_Board4_TypeCBox.setName("E_Board4_TypeCBox"); // NOI18N
        E_Board4_TypeCBox.setPreferredSize(new java.awt.Dimension(115, 27));
        E_Board4_TypeCBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                E_Board4_TypeCBoxItemStateChanged(evt);
            }
        });
        E_Board4_TypeCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                E_Board4_TypeCBoxActionPerformed(evt);
            }
        });
        eBoardPan3.add(E_Board4_TypeCBox);

        E_Board4_connTypeCBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        E_Board4_connTypeCBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TCP/IP", "RS-232" }));
        E_Board4_connTypeCBox.setMinimumSize(new java.awt.Dimension(80, 23));
        E_Board4_connTypeCBox.setName("E_Board4_connTypeCBox"); // NOI18N
        E_Board4_connTypeCBox.setPreferredSize(new java.awt.Dimension(90, 27));
        E_Board4_connTypeCBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                E_Board4_connTypeCBoxItemStateChanged(evt);
            }
        });
        E_Board4_connTypeCBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                E_Board4_connTypeCBoxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        E_Board4_connTypeCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                E_Board4_connTypeCBoxActionPerformed(evt);
            }
        });
        eBoardPan3.add(E_Board4_connTypeCBox);

        E_Board4_IP_TextField.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        E_Board4_IP_TextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        E_Board4_IP_TextField.setText("127.0.0.1");
        E_Board4_IP_TextField.setMinimumSize(new java.awt.Dimension(125, 25));
        E_Board4_IP_TextField.setName("E_Board4_IP_TextField"); // NOI18N
        E_Board4_IP_TextField.setPreferredSize(new java.awt.Dimension(125, 27));
        E_Board4_IP_TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                E_Board4_IP_TextFieldKeyReleased(evt);
            }
        });
        eBoardPan3.add(E_Board4_IP_TextField);

        E_Board4_Port_TextField.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        E_Board4_Port_TextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        E_Board4_Port_TextField.setMinimumSize(new java.awt.Dimension(40, 25));
        E_Board4_Port_TextField.setName("E_Board4_Port_TextField"); // NOI18N
        E_Board4_Port_TextField.setPreferredSize(new java.awt.Dimension(57, 27));
        E_Board4_Port_TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                E_Board4_Port_TextFieldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                E_Board4_Port_TextFieldKeyTyped(evt);
            }
        });
        eBoardPan3.add(E_Board4_Port_TextField);

        gate4Panel.add(eBoardPan3);

        gateBarPan3.setMaximumSize(new java.awt.Dimension(32767, 40));
        gateBarPan3.setMinimumSize(new java.awt.Dimension(426, 32));
        gateBarPan3.setPreferredSize(new java.awt.Dimension(518, 32));
        gateBarPan3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        gateBarPan3.add(filler29);

        ebdLbl3.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        ebdLbl3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        ebdLbl3.setText(GATE_BAR_LABEL.getContent());
        ebdLbl3.setMinimumSize(new java.awt.Dimension(60, 15));
        ebdLbl3.setPreferredSize(new java.awt.Dimension(70, 15));
        gateBarPan3.add(ebdLbl3);

        GateBar4_TypeCBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        GateBar4_TypeCBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "list e-board types" }));
        GateBar4_TypeCBox.setToolTipText("");
        GateBar4_TypeCBox.setMinimumSize(new java.awt.Dimension(115, 25));
        GateBar4_TypeCBox.setName("GateBar4_TypeCBox"); // NOI18N
        GateBar4_TypeCBox.setPreferredSize(new java.awt.Dimension(115, 27));
        GateBar4_TypeCBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                GateBar4_TypeCBoxItemStateChanged(evt);
            }
        });
        GateBar4_TypeCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GateBar4_TypeCBoxActionPerformed(evt);
            }
        });
        gateBarPan3.add(GateBar4_TypeCBox);

        GateBar4_connTypeCBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        GateBar4_connTypeCBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TCP/IP", "RS-232" }));
        GateBar4_connTypeCBox.setMinimumSize(new java.awt.Dimension(80, 23));
        GateBar4_connTypeCBox.setName("GateBar4_connTypeCBox"); // NOI18N
        GateBar4_connTypeCBox.setPreferredSize(new java.awt.Dimension(90, 27));
        GateBar4_connTypeCBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                GateBar4_connTypeCBoxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        GateBar4_connTypeCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GateBar4_connTypeCBoxActionPerformed(evt);
            }
        });
        gateBarPan3.add(GateBar4_connTypeCBox);

        GateBar4_IP_TextField.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        GateBar4_IP_TextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        GateBar4_IP_TextField.setText("127.0.0.1");
        GateBar4_IP_TextField.setMinimumSize(new java.awt.Dimension(125, 25));
        GateBar4_IP_TextField.setName("GateBar4_IP_TextField"); // NOI18N
        GateBar4_IP_TextField.setPreferredSize(new java.awt.Dimension(125, 27));
        GateBar4_IP_TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                GateBar4_IP_TextFieldKeyReleased(evt);
            }
        });
        gateBarPan3.add(GateBar4_IP_TextField);

        GateBar4_Port_TextField.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        GateBar4_Port_TextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        GateBar4_Port_TextField.setMinimumSize(new java.awt.Dimension(40, 25));
        GateBar4_Port_TextField.setName("GateBar4_Port_TextField"); // NOI18N
        GateBar4_Port_TextField.setPreferredSize(new java.awt.Dimension(57, 27));
        GateBar4_Port_TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                GateBar4_Port_TextFieldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                GateBar4_Port_TextFieldKeyTyped(evt);
            }
        });
        gateBarPan3.add(GateBar4_Port_TextField);

        gate4Panel.add(gateBarPan3);

        GatesTabbedPane.addTab(GATE_LABEL.getContent() + "4", gate4Panel);

        eBoardSettingPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        eBoardSettingPanel.setMinimumSize(new java.awt.Dimension(170, 250));
        eBoardSettingPanel.setOpaque(false);
        eBoardSettingPanel.setPreferredSize(new java.awt.Dimension(170, 250));

        E_BoardSettingsButtonPanel.setMinimumSize(new java.awt.Dimension(150, 67));
        E_BoardSettingsButtonPanel.setPreferredSize(new java.awt.Dimension(150, 67));
        E_BoardSettingsButtonPanel.setLayout(new javax.swing.BoxLayout(E_BoardSettingsButtonPanel, javax.swing.BoxLayout.Y_AXIS));

        EBD_settings_label.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jLabel20.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel20.setText(E_BOARD_LABEL.getContent());
        jLabel20.setToolTipText("");
        jLabel20.setMaximumSize(new java.awt.Dimension(300, 27));
        jLabel20.setPreferredSize(new java.awt.Dimension(150, 27));
        EBD_settings_label.add(jLabel20);

        E_BoardSettingsButtonPanel.add(EBD_settings_label);

        EBD_settings.setMaximumSize(new java.awt.Dimension(110, 37));
        EBD_settings.setMinimumSize(new java.awt.Dimension(90, 37));
        EBD_settings.setPreferredSize(new java.awt.Dimension(90, 37));

        EBoardSettingsButton.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        EBoardSettingsButton.setMnemonic('S');
        EBoardSettingsButton.setText(SET_BUTTON.getContent());
        EBoardSettingsButton.setToolTipText("");
        EBoardSettingsButton.setMaximumSize(new java.awt.Dimension(79, 30));
        EBoardSettingsButton.setMinimumSize(new java.awt.Dimension(79, 30));
        EBoardSettingsButton.setPreferredSize(new java.awt.Dimension(79, 30));
        EBoardSettingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EBoardSettingsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout EBD_settingsLayout = new javax.swing.GroupLayout(EBD_settings);
        EBD_settings.setLayout(EBD_settingsLayout);
        EBD_settingsLayout.setHorizontalGroup(
            EBD_settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EBD_settingsLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(EBoardSettingsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        EBD_settingsLayout.setVerticalGroup(
            EBD_settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EBD_settingsLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(EBoardSettingsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        E_BoardSettingsButtonPanel.add(EBD_settings);

        allCyclesPanel.setMinimumSize(new java.awt.Dimension(150, 150));
        allCyclesPanel.setPreferredSize(new java.awt.Dimension(150, 150));

        cycleLabel.setMaximumSize(new java.awt.Dimension(32767, 20));
        cycleLabel.setMinimumSize(new java.awt.Dimension(148, 20));
        cycleLabel.setPreferredSize(new java.awt.Dimension(148, 20));

        jLabel8.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText(CYCLE_LABEL.getContent());
        jLabel8.setMaximumSize(new java.awt.Dimension(80, 15));
        jLabel8.setMinimumSize(new java.awt.Dimension(80, 15));
        jLabel8.setPreferredSize(new java.awt.Dimension(150, 15));

        javax.swing.GroupLayout cycleLabelLayout = new javax.swing.GroupLayout(cycleLabel);
        cycleLabel.setLayout(cycleLabelLayout);
        cycleLabelLayout.setHorizontalGroup(
            cycleLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cycleLabelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cycleLabelLayout.setVerticalGroup(
            cycleLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cycleLabelLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        allCyclesPanel.add(cycleLabel);

        twoCycles.setMaximumSize(new java.awt.Dimension(2147483647, 130));
        twoCycles.setMinimumSize(new java.awt.Dimension(150, 130));
        twoCycles.setName(""); // NOI18N
        twoCycles.setPreferredSize(new java.awt.Dimension(150, 130));
        twoCycles.setLayout(new java.awt.BorderLayout());

        real2Pan.setMaximumSize(new java.awt.Dimension(2147483647, 130));
        real2Pan.setMinimumSize(new java.awt.Dimension(140, 130));
        real2Pan.setPreferredSize(new java.awt.Dimension(140, 130));
        real2Pan.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        flowPanel.setMaximumSize(new java.awt.Dimension(2147483647, 60));
        flowPanel.setMinimumSize(new java.awt.Dimension(140, 60));
        flowPanel.setPreferredSize(new java.awt.Dimension(140, 60));
        flowPanel.setLayout(new javax.swing.BoxLayout(flowPanel, javax.swing.BoxLayout.Y_AXIS));

        labelFlow.setMaximumSize(new java.awt.Dimension(32767, 17));
        labelFlow.setMinimumSize(new java.awt.Dimension(140, 17));
        labelFlow.setName(""); // NOI18N
        labelFlow.setPreferredSize(new java.awt.Dimension(140, 17));
        labelFlow.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 20, 0));

        jLabel31.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel31.setText(FLOWING_LABEL.getContent());
        jLabel31.setToolTipText("");
        jLabel31.setMaximumSize(new java.awt.Dimension(80, 17));
        jLabel31.setMinimumSize(new java.awt.Dimension(80, 17));
        jLabel31.setPreferredSize(new java.awt.Dimension(80, 17));
        labelFlow.add(jLabel31);

        flowPanel.add(labelFlow);

        cBoxPanel.setMaximumSize(new java.awt.Dimension(32767, 40));
        cBoxPanel.setMinimumSize(new java.awt.Dimension(140, 32));
        cBoxPanel.setPreferredSize(new java.awt.Dimension(140, 32));
        cBoxPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));
        cBoxPanel.add(filler18);

        FlowingComboBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        FlowingComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "4,000", "6,000", "8,000", "10,000", "12,000" }));
        FlowingComboBox.setMaximumSize(new java.awt.Dimension(32767, 32));
        FlowingComboBox.setMinimumSize(new java.awt.Dimension(80, 32));
        FlowingComboBox.setName("FlowingComboBox"); // NOI18N
        FlowingComboBox.setPreferredSize(new java.awt.Dimension(80, 32));
        FlowingComboBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                FlowingComboBoxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        cBoxPanel.add(FlowingComboBox);

        jLabel33.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel33.setText("ms");
        jLabel33.setToolTipText("");
        jLabel33.setMaximumSize(new java.awt.Dimension(20, 28));
        jLabel33.setMinimumSize(new java.awt.Dimension(20, 28));
        jLabel33.setPreferredSize(new java.awt.Dimension(20, 28));
        cBoxPanel.add(jLabel33);

        flowPanel.add(cBoxPanel);

        real2Pan.add(flowPanel);

        blinkPanel.setMaximumSize(new java.awt.Dimension(2147483647, 60));
        blinkPanel.setMinimumSize(new java.awt.Dimension(140, 60));
        blinkPanel.setPreferredSize(new java.awt.Dimension(140, 60));
        blinkPanel.setLayout(new javax.swing.BoxLayout(blinkPanel, javax.swing.BoxLayout.Y_AXIS));

        labelBlink.setMaximumSize(new java.awt.Dimension(32767, 17));
        labelBlink.setMinimumSize(new java.awt.Dimension(140, 17));
        labelBlink.setName(""); // NOI18N
        labelBlink.setPreferredSize(new java.awt.Dimension(140, 17));
        labelBlink.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 20, 0));

        blinkingL.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        blinkingL.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        blinkingL.setText(BLINGKING_LABEL.getContent());
        blinkingL.setToolTipText("");
        blinkingL.setMaximumSize(new java.awt.Dimension(80, 17));
        blinkingL.setMinimumSize(new java.awt.Dimension(80, 17));
        blinkingL.setPreferredSize(new java.awt.Dimension(80, 17));
        labelBlink.add(blinkingL);

        blinkPanel.add(labelBlink);

        cBoxPan.setMaximumSize(new java.awt.Dimension(32767, 40));
        cBoxPan.setMinimumSize(new java.awt.Dimension(140, 32));
        cBoxPan.setPreferredSize(new java.awt.Dimension(140, 32));
        cBoxPan.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));
        cBoxPan.add(filler17);

        BlinkingComboBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        BlinkingComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "500", "750", "1,000", "1,250", "1,500" }));
        BlinkingComboBox.setMaximumSize(new java.awt.Dimension(32767, 32));
        BlinkingComboBox.setMinimumSize(new java.awt.Dimension(80, 32));
        BlinkingComboBox.setName("BlinkingComboBox"); // NOI18N
        BlinkingComboBox.setPreferredSize(new java.awt.Dimension(80, 32));
        BlinkingComboBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                BlinkingComboBoxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        cBoxPan.add(BlinkingComboBox);

        jLabel32.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel32.setText("ms");
        jLabel32.setToolTipText("");
        jLabel32.setMaximumSize(new java.awt.Dimension(20, 28));
        jLabel32.setMinimumSize(new java.awt.Dimension(20, 28));
        jLabel32.setPreferredSize(new java.awt.Dimension(20, 28));
        cBoxPan.add(jLabel32);

        blinkPanel.add(cBoxPan);

        real2Pan.add(blinkPanel);

        twoCycles.add(real2Pan, java.awt.BorderLayout.CENTER);

        allCyclesPanel.add(twoCycles);

        javax.swing.GroupLayout eBoardSettingPanelLayout = new javax.swing.GroupLayout(eBoardSettingPanel);
        eBoardSettingPanel.setLayout(eBoardSettingPanelLayout);
        eBoardSettingPanelLayout.setHorizontalGroup(
            eBoardSettingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(eBoardSettingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(eBoardSettingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(eBoardSettingPanelLayout.createSequentialGroup()
                        .addComponent(E_BoardSettingsButtonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(allCyclesPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        eBoardSettingPanelLayout.setVerticalGroup(
            eBoardSettingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(eBoardSettingPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(E_BoardSettingsButtonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(allCyclesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout gateSettingPanelLayout = new javax.swing.GroupLayout(gateSettingPanel);
        gateSettingPanel.setLayout(gateSettingPanelLayout);
        gateSettingPanelLayout.setHorizontalGroup(
            gateSettingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gateSettingPanelLayout.createSequentialGroup()
                .addComponent(GatesTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(eBoardSettingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        gateSettingPanelLayout.setVerticalGroup(
            gateSettingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gateSettingPanelLayout.createSequentialGroup()
                .addGroup(gateSettingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(GatesTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eBoardSettingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GatesTabbedPane.getAccessibleContext().setAccessibleName("\"GATE_LABEL.getContent() + \\\"2\\\"\"");

        wholePanel.add(gateSettingPanel);
        wholePanel.add(filler1);

        bottomPanel.setMaximumSize(new java.awt.Dimension(32767, 40));
        bottomPanel.setMinimumSize(new java.awt.Dimension(275, 40));
        bottomPanel.setPreferredSize(new java.awt.Dimension(700, 40));
        bottomPanel.setLayout(new java.awt.BorderLayout());
        bottomPanel.add(filler8, java.awt.BorderLayout.WEST);

        myMetaKeyLabel.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        myMetaKeyLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        myMetaKeyLabel.setText(META_KEY_LABEL.getContent());
        myMetaKeyLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        myMetaKeyLabel.setMaximumSize(new java.awt.Dimension(100, 25));
        myMetaKeyLabel.setMinimumSize(new java.awt.Dimension(100, 25));
        myMetaKeyLabel.setName(""); // NOI18N
        myMetaKeyLabel.setPreferredSize(new java.awt.Dimension(100, 25));
        myMetaKeyLabel.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        myMetaKeyLabel.setForeground(tipColor);
        bottomPanel.add(myMetaKeyLabel, java.awt.BorderLayout.EAST);

        buttonsPanel.setMaximumSize(new java.awt.Dimension(32767, 40));
        buttonsPanel.setMinimumSize(new java.awt.Dimension(100, 40));
        buttonsPanel.setPreferredSize(new java.awt.Dimension(300, 40));
        buttonsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 0));

        SettingsSaveButton.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        SettingsSaveButton.setMnemonic('s');
        SettingsSaveButton.setText(SAVE_BTN.getContent());
        SettingsSaveButton.setToolTipText(SETTINGS_SAVE_TOOLTIP.getContent());
        SettingsSaveButton.setEnabled(false);
        SettingsSaveButton.setMaximumSize(new java.awt.Dimension(90, 40));
        SettingsSaveButton.setMinimumSize(new java.awt.Dimension(90, 40));
        SettingsSaveButton.setPreferredSize(new java.awt.Dimension(90, 40));
        SettingsSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SettingsSaveButtonActionPerformed(evt);
            }
        });
        buttonsPanel.add(SettingsSaveButton);

        SettingsCancelButton.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        SettingsCancelButton.setMnemonic('c');
        SettingsCancelButton.setText(CANCEL_BTN.getContent());
        SettingsCancelButton.setToolTipText(SETTINGS_CANCEL_TOOLTIP.getContent());
        SettingsCancelButton.setEnabled(false);
        SettingsCancelButton.setMaximumSize(new java.awt.Dimension(90, 40));
        SettingsCancelButton.setMinimumSize(new java.awt.Dimension(90, 40));
        SettingsCancelButton.setPreferredSize(new java.awt.Dimension(90, 40));
        SettingsCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SettingsCancelButtonActionPerformed(evt);
            }
        });
        buttonsPanel.add(SettingsCancelButton);

        SettingsCloseButton.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        SettingsCloseButton.setMnemonic('c');
        SettingsCloseButton.setText(CLOSE_BTN.getContent());
        SettingsCloseButton.setToolTipText(CLOSE_BTN_TOOLTIP.getContent());
        SettingsCloseButton.setPreferredSize(new java.awt.Dimension(90, 40));
        SettingsCloseButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                SettingsCloseButtonStateChanged(evt);
            }
        });
        SettingsCloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SettingsCloseButtonActionPerformed(evt);
            }
        });
        buttonsPanel.add(SettingsCloseButton);

        bottomPanel.add(buttonsPanel, java.awt.BorderLayout.CENTER);

        wholePanel.add(bottomPanel);

        getContentPane().add(wholePanel, java.awt.BorderLayout.CENTER);
        getContentPane().add(filler2, java.awt.BorderLayout.PAGE_START);
        getContentPane().add(filler4, java.awt.BorderLayout.LINE_START);
        getContentPane().add(filler6, java.awt.BorderLayout.LINE_END);
        getContentPane().add(filler3, java.awt.BorderLayout.PAGE_END);

        setSize(new java.awt.Dimension(788, 842));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void LanguageHelpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LanguageHelpButtonActionPerformed
        String helpText = LANGUAGE_HELP_1.getContent() + System.lineSeparator() +
                LANGUAGE_HELP_2.getContent();

        JDialog helpDialog = new PWHelpJDialog(this, false,
            LANGUAGE_SELECT_DIALOGTITLE.getContent(), helpText, false);
        locateAndShowHelpDialog(this, helpDialog, LanguageHelpButton);
    }//GEN-LAST:event_LanguageHelpButtonActionPerformed

    private void LoggingLevelHelpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoggingLevelHelpButtonActionPerformed

        String helpText = LEVEL_LABEL.getContent() + "[" +
                OptnLoggingLevelComboBox.getSelectedItem().toString() 
                + "]" + System.lineSeparator() + System.lineSeparator() + "* ";
        if (OptnLoggingLevelComboBox.getSelectedIndex() == OpLogLevel.LogAlways.ordinal()) {
            helpText += LOGGIND_DIALOG_1.getContent() + System.lineSeparator() 
                    + System.lineSeparator() 
                    + LOGGIND_DIALOG_2.getContent() + System.lineSeparator() 
                    + LOGGIND_DIALOG_3.getContent() + System.lineSeparator() 
                    + LOGGIND_DIALOG_4.getContent() + System.lineSeparator() 
                    + LOGGIND_DIALOG_5.getContent() + System.lineSeparator() 
                    + LOGGIND_DIALOG_6.getContent() + System.lineSeparator();
        } else
        if (OptnLoggingLevelComboBox.getSelectedIndex() == OpLogLevel.SettingsChange.ordinal()) {
            helpText += LOGGIND_DIALOG_A.getContent() + System.lineSeparator() 
                    + System.lineSeparator() 
                    + LOGGIND_DIALOG_B.getContent() + System.lineSeparator() 
                    + LOGGIND_DIALOG_C.getContent() + System.lineSeparator()
                    + LOGGIND_DIALOG_D.getContent() + System.lineSeparator() 
                    + LOGGIND_DIALOG_E.getContent() + System.lineSeparator();
        } else
        if (OptnLoggingLevelComboBox.getSelectedIndex() == OpLogLevel.EBDsettingsChange.ordinal()) {
            helpText += LOGGIND_DIALOG_A.getContent() + System.lineSeparator() 
                    + System.lineSeparator() 
                    + LOGGIND_DIALOG_F.getContent() + System.lineSeparator();
        }

        JDialog helpDialog = new PWHelpJDialog(this, false,
            LOGGING_DIALOGTITLE.getContent(), helpText, false);
        locateAndShowHelpDialog(this, helpDialog, LoggingLevelHelpButton);
    }//GEN-LAST:event_LoggingLevelHelpButtonActionPerformed

    private void PWHelpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PWHelpButtonActionPerformed
        PasswordValidator pwValidator = new PasswordValidator();
        short pwPowerLevel = (short)PWStrengthChoiceComboBox.getSelectedIndex();
        String helpText = pwValidator.getWrongPWFormatMsg(pwPowerLevel);

        JDialog helpDialog = new PWHelpJDialog(this, false, ATT_HELP_DIALOGTITLE.getContent(),
                helpText, true);
        locateAndShowHelpDialog(this, helpDialog, PWHelpButton);
    }//GEN-LAST:event_PWHelpButtonActionPerformed
    
    private void SettingsSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SettingsSaveButtonActionPerformed
        Connection conn = null;
        PreparedStatement updateSettings = null;
        int result = -1;
        boolean newStorePassingDelay = RecordPassingDelayChkBox.isSelected();

        //<editor-fold desc="--check setting input errors">
        if (!TextFieldNumericValueOK(TextFieldPicWidth, "Photo Extent Typing Errors")) {
            return;
        }

        if (!TextFieldNumericValueOK(TextFieldPicHeight, "Photo Height Typing Errors")) {
            return;
        }

        if(Integer.parseInt(TextFieldPicHeight.getText().trim().replace(",","")) < 100){
            TextFieldPicHeight.requestFocusInWindow();
            JOptionPane.showConfirmDialog(this, "Please enter a height value of 100 or more",
                    "Picture Height Input Error", JOptionPane.PLAIN_MESSAGE, WARNING_MESSAGE);
            return;
        }
        
        if(Integer.parseInt(TextFieldPicWidth.getText().trim().replace(",","")) < 100){
            TextFieldPicWidth.requestFocusInWindow();
            JOptionPane.showConfirmDialog(this, "Please enter a width value of 100 or more",
                    "Picture Width Input Error", JOptionPane.PLAIN_MESSAGE, WARNING_MESSAGE);
            return;
        }
        
        if (someIPaddressWrong()) {
            return;
        }
        
        if (someCOMportIDsame()) {
            return;
        }
        //</editor-fold>

        int newStatCount = 0;
        short pwLevel = -1;
        short optnLogLevel = -1;
        String maxLineStr = "";
        int imageKeepDuration = 0;
        int picWidth = Integer.parseInt(((String) TextFieldPicWidth.getText()).replace(",", ""));
        int picHeight = Integer.parseInt(((String) TextFieldPicHeight.getText()).replace(",", ""));
        int flowCycle = Integer.parseInt(((String) FlowingComboBox.getSelectedItem()).replace(",", ""));
        int blinkCycle = Integer.parseInt(((String) BlinkingComboBox.getSelectedItem()).replace(",", ""));
        
        try {
            StringBuffer sb = new StringBuffer("Update SettingsTable SET ");
            //<editor-fold desc="--create update statement">
            sb.append("Lot_Name = ?, ");
            sb.append("perfEvalNeeded = ?, PWStrengthLevel = ?, OptnLoggingLevel = ?, ");
            sb.append("languageCode = ?, countryCode = ?, localeIndex = ?, statCount =  ?, ");
            sb.append("MaxMessageLines = ?, GateCount = ?, ");
            sb.append("PictureWidth = ?, PictureHeight = ?, ");
            sb.append("EBD_flow_cycle = ?, EBD_blink_cycle = ?, ");
            sb.append("max_maintain_date = ? ");
            //</editor-fold>

            ConvComboBoxItem selectedItem = (ConvComboBoxItem)PopSizeCBox.getSelectedItem();
            newStatCount = (Integer)selectedItem.getKeyValue();
            if (newStorePassingDelay) {
                for (int gateID = 1; gateID <= gateCount; gateID++) {
                    initPassingDelayStatIfNeeded(newStatCount, gateID);
                }
            }
            conn = JDBCMySQL.getConnection();
            updateSettings = conn.prepareStatement (sb.toString());

            int pIndex = 1;

            // <editor-fold defaultstate="collapsed" desc="--Provide values to each parameters of the UPDATE statement">
            updateSettings.setString(pIndex++, lotNameTextField.getText().trim());
            if (newStorePassingDelay) {
                updateSettings.setInt(pIndex++, 1);
            } else {
                updateSettings.setInt(pIndex++, 0);
                if (DEBUG) {
                    // Give warning that in debug mode PassingDelay is always recorded.
                    JOptionPane.showMessageDialog(null, RECORD_DELAY_DEBUG.getContent());
                }
            }

            pwLevel = (short)(PWStrengthChoiceComboBox.getSelectedIndex());
            updateSettings.setShort(pIndex++, pwLevel);

            optnLogLevel = (short)(OptnLoggingLevelComboBox.getSelectedIndex());
            updateSettings.setShort(pIndex++, optnLogLevel);

            updateSettings.setString(pIndex++, DateChooserLangCBox.getLocale().getLanguage());
            updateSettings.setString(pIndex++, DateChooserLangCBox.getLocale().getCountry());
            updateSettings.setShort(pIndex++, (short)DateChooserLangCBox.getSelectedIndex());
            updateSettings.setInt(pIndex++, PopSizeCBox.getSelectedIndex());
            
            maxLineStr = (String)MessageMaxLineComboBox.getSelectedItem();
            updateSettings.setShort(pIndex++, new Short(maxLineStr));
            updateSettings.setShort(pIndex++, new Short((String)GateCountComboBox.getSelectedItem()));

            updateSettings.setInt(pIndex++, picWidth);
            updateSettings.setInt(pIndex++, picHeight);
            updateSettings.setInt(pIndex++, flowCycle);
            updateSettings.setInt(pIndex++, blinkCycle);

            ConvComboBoxItem item = (ConvComboBoxItem)ImageDurationCBox.getSelectedItem();
            imageKeepDuration = (Integer)(item.getKeyValue());
            updateSettings.setInt(pIndex++, imageKeepDuration);
            // </editor-fold>

            result = updateSettings.executeUpdate();
            if (index2Level(opLoggingIndex) != Level.OFF && index2Level(optnLogLevel) == Level.OFF) {
                Globals.isFinalWishLog = true;
            }
        } catch (SQLException se) {
            Globals.logParkingException(Level.SEVERE, se,
                    "(Save settings: " + (newStorePassingDelay ? "Y" : "N") + ")");
        } finally {
            // <editor-fold defaultstate="collapsed" desc="--Return resources and display the save result">
            closeDBstuff(conn, updateSettings, null, "(Save settings: " + (newStorePassingDelay ? "Y" : "N") + ")");

            if (result == 1) {
                //<editor-fold desc="-- Log system settings change if set to do so">
                if (statCount != newStatCount) 
                {
                    logParkingOperation(OpLogLevel.SettingsChange, "Settings Change, Statistics Population Size: " 
                            + statCount + " => " + newStatCount);
                }
               
                if (storePassingDelay != newStorePassingDelay)
                {
                    logParkingOperation(OpLogLevel.SettingsChange, "Settings Change, Average Passing Delay: " 
                            + storePassingDelay + " => " + newStorePassingDelay);
                }
                
                if (pwStrengthLevel != pwLevel)
                {
                    logParkingOperation(OpLogLevel.SettingsChange, "Settings Change, Password Strength Level: " 
                            + PWStrengthChoiceComboBox.getItemAt(pwStrengthLevel) + " => " 
                            + PWStrengthChoiceComboBox.getItemAt(pwLevel));
                }
                
                if (opLoggingIndex != optnLogLevel)
                {
                    logParkingOperation(OpLogLevel.SettingsChange, "Settings Change, Gen' Operation Log Level: " 
                            + OptnLoggingLevelComboBox.getItemAt(opLoggingIndex) + " => " 
                            + OptnLoggingLevelComboBox.getItemAt(optnLogLevel));
                }
                
                if (localeIndex != (short)DateChooserLangCBox.getSelectedIndex())
                {
                    logParkingOperation(OpLogLevel.SettingsChange, "Settings Change, Date Chooser Lang': " 
                            + DateChooserLangCBox.getItemAt(localeIndex) + " => " 
                            + DateChooserLangCBox.getItemAt((short)DateChooserLangCBox.getSelectedIndex()));
                }
                
                if (maxMessageLines != new Short(maxLineStr))
                {
                    logParkingOperation(OpLogLevel.SettingsChange, "Settings Change, Recent Event Line Max: " 
                            + maxMessageLines + " => " + new Short(maxLineStr));
                }
                
                short newGateCount = new Short((String)GateCountComboBox.getSelectedItem());
                
                boolean gateCountChanged = gateCount != newGateCount;
                if (gateCountChanged)
                {
                    logParkingOperation(OpLogLevel.SettingsChange, "Settings Change, Number of Gates: " 
                            + gateCount + " => " + newGateCount);
                }
                
                if (maxMaintainDate != imageKeepDuration) {
                    logParkingOperation(OpLogLevel.SettingsChange, "Settings Change, Image Keep Duration: " 
                            + maxMaintainDate + " => " + imageKeepDuration);
                }
                
                if (PIC_WIDTH != picWidth) {
                    logParkingOperation(OpLogLevel.SettingsChange, "Settings Change, Image width: " 
                            + PIC_WIDTH + " => " + picWidth);
                }
                
                if (PIC_HEIGHT != picHeight) {
                    logParkingOperation(OpLogLevel.SettingsChange, "Settings Change, Image Height: " 
                            + PIC_HEIGHT + " => " + picHeight);
                }
                
                if (EBD_flowCycle != flowCycle)
                {
                    logParkingOperation(OpLogLevel.EBDsettingsChange, "E-Board Settings Change, Cycles--flowing: " 
                            + EBD_flowCycle + " => " + flowCycle);
                }                
                
                if (EBD_blinkCycle != blinkCycle)
                {
                    logParkingOperation(OpLogLevel.EBDsettingsChange, "E-Board Settings Change, Cycles--blinking: " 
                            + EBD_blinkCycle + " => " + blinkCycle);
                }    
                
                if (mainForm != null && gateCountChanged)
                {
                    JOptionPane.showMessageDialog(mainForm, REBOOT_MESSAGE.getContent(), 
                            REBOOT_POPUP.getContent(), WARNING_MESSAGE, 
                            new javax.swing.ImageIcon(mainForm.getClass().getResource("/restart.png")));
                    mainForm.askUserIntentionOnProgramStop(true);
                }                
                //</editor-fold>
                
                Globals.getOperationLog().setLevel(index2Level(opLoggingIndex));
            } else {
                JOptionPane.showMessageDialog(this, "The storage system settings failed.",
                    SETTINGS_SAVE_RESULT.getContent(), JOptionPane.ERROR_MESSAGE);
            }
            // </editor-fold>
        }
        
        result += saveGateDevices();
        
        if (result == gateCount + 1) {
            readSettings();
            Globals.getOperationLog().setLevel(index2Level(opLoggingIndex));
            JOptionPane.showMessageDialog(this, SAVE_SETTINGS_DIALOG.getContent(),
                SETTINGS_SAVE_RESULT.getContent(), JOptionPane.PLAIN_MESSAGE);
            enableSaveCancelButtons(false);
            ChangeSettings.changeEnabled_of_SaveCancelButtons(SettingsSaveButton, SettingsCancelButton, 
                    SettingsCloseButton, changedControls.size());            
        } else {
            JOptionPane.showMessageDialog(this, "The storage system settings failed.",
                SETTINGS_SAVE_RESULT.getContent(), JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_SettingsSaveButtonActionPerformed

    private void addPopSizeOptions() {
        PopSizeCBox.removeAllItems();
        
        for (int option : statCountArr) {
            PopSizeCBox.addItem(new ConvComboBoxItem(new Integer(option), 
                    NumberFormat.getNumberInstance(Locale.US).format(option)));
        }
    }    
    
    private void SettingsCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SettingsCancelButtonActionPerformed
        loadComponentValues();
    }//GEN-LAST:event_SettingsCancelButtonActionPerformed

    private void SettingsCloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SettingsCloseButtonActionPerformed
        tryToCloseSettingsForm();
    }//GEN-LAST:event_SettingsCloseButtonActionPerformed

    private void TextFieldPicHeightKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldPicHeightKeyTyped
        rejectNonNumericKeys(evt);
    }//GEN-LAST:event_TextFieldPicHeightKeyTyped

    private void TextFieldPicWidthKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldPicWidthKeyTyped
        rejectNonNumericKeys(evt);
    }//GEN-LAST:event_TextFieldPicWidthKeyTyped

    private void finishSettingsForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_finishSettingsForm
        tryToCloseSettingsForm();
    }//GEN-LAST:event_finishSettingsForm

    private void RecordPassingDelayChkBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RecordPassingDelayChkBoxActionPerformed
        ChangeSettings.changeStatus_Manager(SettingsSaveButton, SettingsCancelButton, SettingsCloseButton, 
                changedControls, (Component) evt.getSource(), RecordPassingDelayChkBox.isSelected(), storePassingDelay);
    }//GEN-LAST:event_RecordPassingDelayChkBoxActionPerformed

    private void ImageDurationCBoxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_ImageDurationCBoxPopupMenuWillBecomeInvisible
        ChangeSettings.changeStatus_Manager(SettingsSaveButton, SettingsCancelButton, SettingsCloseButton, 
                changedControls, (Component) evt.getSource(), ImageDurationCBox.getSelectedIndex(), maxArrivalCBoxIndex);        
    }//GEN-LAST:event_ImageDurationCBoxPopupMenuWillBecomeInvisible

    private void PWStrengthChoiceComboBoxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_PWStrengthChoiceComboBoxPopupMenuWillBecomeInvisible
        ChangeSettings.changeStatus_Manager(SettingsSaveButton, SettingsCancelButton, SettingsCloseButton, 
                changedControls, (Component) evt.getSource(), PWStrengthChoiceComboBox.getSelectedIndex(), pwStrengthLevel);
    }//GEN-LAST:event_PWStrengthChoiceComboBoxPopupMenuWillBecomeInvisible

    private void MessageMaxLineComboBoxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_MessageMaxLineComboBoxPopupMenuWillBecomeInvisible
        short lines = (short) Integer.parseInt((String)MessageMaxLineComboBox.getSelectedItem());
        ChangeSettings.changeStatus_Manager(SettingsSaveButton, SettingsCancelButton, SettingsCloseButton, 
                changedControls, (Component) evt.getSource(), lines, maxMessageLines);        
    }//GEN-LAST:event_MessageMaxLineComboBoxPopupMenuWillBecomeInvisible

    private void OptnLoggingLevelComboBoxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_OptnLoggingLevelComboBoxPopupMenuWillBecomeInvisible
        ChangeSettings.changeStatus_Manager(SettingsSaveButton, SettingsCancelButton, SettingsCloseButton, 
                changedControls, (Component)evt.getSource(),
                OptnLoggingLevelComboBox.getSelectedIndex(), opLoggingIndex);        
    }//GEN-LAST:event_OptnLoggingLevelComboBoxPopupMenuWillBecomeInvisible

    private void DateChooserLangCBoxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_DateChooserLangCBoxPopupMenuWillBecomeInvisible
        ChangeSettings.changeStatus_Manager(SettingsSaveButton, SettingsCancelButton, SettingsCloseButton, 
                changedControls, (Component) evt.getSource(), DateChooserLangCBox.getSelectedIndex(), localeIndex);        
    }//GEN-LAST:event_DateChooserLangCBoxPopupMenuWillBecomeInvisible

    private void GateCountComboBoxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_GateCountComboBoxPopupMenuWillBecomeInvisible
        String gateCountStr = (String)GateCountComboBox.getSelectedItem();
        ChangeSettings.changeStatus_Manager(SettingsSaveButton, SettingsCancelButton, SettingsCloseButton, 
                changedControls, (Component) evt.getSource(), Integer.parseInt(gateCountStr), gateCount);        
    }//GEN-LAST:event_GateCountComboBoxPopupMenuWillBecomeInvisible

    private void TextFieldPicWidthKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldPicWidthKeyReleased
        String newPicWidthStr = ((String) TextFieldPicWidth.getText()).replace(",", "");
        if(newPicWidthStr.length() == 0){
            newPicWidthStr = "0";
         }
        ChangeSettings.changeStatus_Manager(SettingsSaveButton, SettingsCancelButton, SettingsCloseButton, 
                changedControls, (Component) evt.getSource(), Integer.parseInt(newPicWidthStr), PIC_WIDTH);
    }//GEN-LAST:event_TextFieldPicWidthKeyReleased

    private void TextFieldPicHeightKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldPicHeightKeyReleased
        String newPicHeightStr = ((String) TextFieldPicHeight.getText()).replace(",", "");
        if(newPicHeightStr.length() == 0){
            newPicHeightStr = "0";
         }
        ChangeSettings.changeStatus_Manager(SettingsSaveButton, SettingsCancelButton, SettingsCloseButton, 
                changedControls, (Component) evt.getSource(), Integer.parseInt(newPicHeightStr), PIC_HEIGHT);
    }//GEN-LAST:event_TextFieldPicHeightKeyReleased

    LEDnoticeManager managerLEDnotice = null;    
    
    private void lotNameTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lotNameTextFieldKeyReleased
        ChangeSettings.changeStatus_Manager(SettingsSaveButton, SettingsCancelButton, SettingsCloseButton, 
                changedControls, (Component) evt.getSource(), lotNameTextField.getText().trim(), parkingLotName);
    }//GEN-LAST:event_lotNameTextFieldKeyReleased

    private void BlinkingComboBoxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_BlinkingComboBoxPopupMenuWillBecomeInvisible
        String newBlinkCycleStr = ((String)BlinkingComboBox.getSelectedItem()).replace(",", "");
        ChangeSettings.changeStatus_Manager(SettingsSaveButton, SettingsCancelButton, SettingsCloseButton, 
                changedControls, (Component) evt.getSource(), Integer.parseInt(newBlinkCycleStr), EBD_blinkCycle);        
    }//GEN-LAST:event_BlinkingComboBoxPopupMenuWillBecomeInvisible

    private void FlowingComboBoxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_FlowingComboBoxPopupMenuWillBecomeInvisible
        String newFlowCycleStr =((String) FlowingComboBox.getSelectedItem()).replace(",", "");
        ChangeSettings.changeStatus_Manager(SettingsSaveButton, SettingsCancelButton, SettingsCloseButton, 
                changedControls, (Component) evt.getSource(), Integer.parseInt(newFlowCycleStr), EBD_flowCycle);
    }//GEN-LAST:event_FlowingComboBoxPopupMenuWillBecomeInvisible

    private void EBoardSettingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EBoardSettingsButtonActionPerformed
        int tabIndex = GatesTabbedPane.getSelectedIndex();
        JComboBox typeCBox = (JComboBox)componentMap.get("E_Board" + (tabIndex + 1) + "_TypeCBox");

        eBoardDialog = new JDialog(this, E_BOARD_SETTINGS_FRAME_TITLE.getContent(), true);
        if (typeCBox.getSelectedIndex() == E_BoardType.Simulator.ordinal()) {
            eBoardDialog.getContentPane().add(
                    (new Settings_EBoard(mainForm, this)).getContentPane());
        } else if (typeCBox.getSelectedIndex() == E_BoardType.LEDnotice.ordinal()) {
            eBoardDialog.getContentPane().add(
                    (new Settings_LEDnotice(mainForm, this, null, tabIndex + 1)).getContentPane());
        }
        eBoardDialog.pack();
        eBoardDialog.setResizable(false);

        /**
         * Place E-board settings frame around invoking button and inside monitor.
         */
        Point buttonPoint = EBoardSettingsButton.getLocationOnScreen();
        int idealX = buttonPoint.x + EBoardSettingsButton.getSize().width + 10;
        int moniWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int maxX = moniWidth - (int)eBoardDialog.getSize().getWidth();
        int finalX = idealX;
        int finalY = buttonPoint.y - (int)eBoardDialog.getSize().getHeight() / 2;
        
        if (idealX > maxX) {
            finalX = maxX;
            finalY = buttonPoint.y - (int)eBoardDialog.getSize().getHeight()- 10;
        } 
        eBoardDialog.setLocation(finalX, finalY);
        
        eBoardDialog.setVisible(true);
    }//GEN-LAST:event_EBoardSettingsButtonActionPerformed

    private void Camera1_Port_TextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Camera1_Port_TextFieldKeyTyped
        rejectNonNumericKeys(evt);
    }//GEN-LAST:event_Camera1_Port_TextFieldKeyTyped

    private void Camera1_Port_TextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Camera1_Port_TextFieldKeyReleased
        changeButtonEnabledForPortChange(Camera, 1, (Component) evt.getSource());
    }//GEN-LAST:event_Camera1_Port_TextFieldKeyReleased

    private void Camera1_IP_TextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Camera1_IP_TextFieldKeyReleased
        changeButtonEnabledForIPchange(Camera, 1, (Component) evt.getSource());
    }//GEN-LAST:event_Camera1_IP_TextFieldKeyReleased

    private void TextFieldGateName1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldGateName1KeyReleased
        checkGateNameChangeAndChangeEnabled(1, (Component) evt.getSource());
    }//GEN-LAST:event_TextFieldGateName1KeyReleased

    private void TextFieldGateName3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldGateName3KeyReleased
        // TODO add your handling code here:
        checkGateNameChangeAndChangeEnabled(3, (Component) evt.getSource());        
    }//GEN-LAST:event_TextFieldGateName3KeyReleased

    private void Camera3_connTypeCBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Camera3_connTypeCBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Camera3_connTypeCBoxActionPerformed

    private void Camera3_IP_TextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Camera3_IP_TextFieldKeyReleased
        changeButtonEnabledForIPchange(Camera, 3, (Component) evt.getSource());        
    }//GEN-LAST:event_Camera3_IP_TextFieldKeyReleased

    private void Camera3_Port_TextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Camera3_Port_TextFieldKeyReleased
        changeButtonEnabledForPortChange(Camera, 3, (Component) evt.getSource());
    }//GEN-LAST:event_Camera3_Port_TextFieldKeyReleased

    private void Camera3_Port_TextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Camera3_Port_TextFieldKeyTyped
        rejectNonNumericKeys(evt);
    }//GEN-LAST:event_Camera3_Port_TextFieldKeyTyped

    private void E_Board3_TypeCBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_E_Board3_TypeCBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_E_Board3_TypeCBoxActionPerformed

    private void E_Board3_connTypeCBoxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_E_Board3_connTypeCBoxPopupMenuWillBecomeInvisible
        ChangeSettings.changeStatus_Manager(
                SettingsSaveButton, SettingsCancelButton, SettingsCloseButton, 
                changedControls, ((Component) evt.getSource()), E_Board3_connTypeCBox.getSelectedIndex(), 
                connectionType[E_Board.ordinal()][1]);        
    }//GEN-LAST:event_E_Board3_connTypeCBoxPopupMenuWillBecomeInvisible

    private void E_Board3_connTypeCBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_E_Board3_connTypeCBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_E_Board3_connTypeCBoxActionPerformed

    private void E_Board3_IP_TextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_E_Board3_IP_TextFieldKeyReleased
        changeButtonEnabledForIPchange(E_Board, 3, (Component) evt.getSource());
    }//GEN-LAST:event_E_Board3_IP_TextFieldKeyReleased

    private void E_Board3_Port_TextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_E_Board3_Port_TextFieldKeyReleased
        changeButtonEnabledForPortChange(E_Board, 3, (Component) evt.getSource());
    }//GEN-LAST:event_E_Board3_Port_TextFieldKeyReleased

    private void E_Board3_Port_TextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_E_Board3_Port_TextFieldKeyTyped
        rejectNonNumericKeys(evt);
    }//GEN-LAST:event_E_Board3_Port_TextFieldKeyTyped

    private void GateBar3_TypeCBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GateBar3_TypeCBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_GateBar3_TypeCBoxActionPerformed

    private void GateBar3_connTypeCBoxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_GateBar3_connTypeCBoxPopupMenuWillBecomeInvisible
        ChangeSettings.changeStatus_Manager(
                SettingsSaveButton, SettingsCancelButton, SettingsCloseButton, 
                changedControls, ((Component) evt.getSource()), GateBar3_connTypeCBox.getSelectedIndex(), 
                connectionType[GateBar.ordinal()][1]);        
    }//GEN-LAST:event_GateBar3_connTypeCBoxPopupMenuWillBecomeInvisible

    private void GateBar3_connTypeCBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GateBar3_connTypeCBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_GateBar3_connTypeCBoxActionPerformed

    private void GateBar3_IP_TextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GateBar3_IP_TextFieldKeyReleased
        changeButtonEnabledForIPchange(GateBar, 3, (Component) evt.getSource());        
    }//GEN-LAST:event_GateBar3_IP_TextFieldKeyReleased

    private void GateBar3_Port_TextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GateBar3_Port_TextFieldKeyReleased
        changeButtonEnabledForPortChange(GateBar, 3, (Component) evt.getSource());
    }//GEN-LAST:event_GateBar3_Port_TextFieldKeyReleased

    private void GateBar3_Port_TextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GateBar3_Port_TextFieldKeyTyped
        rejectNonNumericKeys(evt);
    }//GEN-LAST:event_GateBar3_Port_TextFieldKeyTyped

    private void TextFieldGateName4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldGateName4KeyReleased
        checkGateNameChangeAndChangeEnabled(4, (Component) evt.getSource());
    }//GEN-LAST:event_TextFieldGateName4KeyReleased

    private void Camera4_connTypeCBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Camera4_connTypeCBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Camera4_connTypeCBoxActionPerformed

    private void Camera4_IP_TextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Camera4_IP_TextFieldKeyReleased
        changeButtonEnabledForIPchange(Camera, 4, (Component) evt.getSource());
    }//GEN-LAST:event_Camera4_IP_TextFieldKeyReleased

    private void Camera4_Port_TextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Camera4_Port_TextFieldKeyReleased
        changeButtonEnabledForPortChange(Camera, 4, (Component) evt.getSource());
    }//GEN-LAST:event_Camera4_Port_TextFieldKeyReleased

    private void Camera4_Port_TextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Camera4_Port_TextFieldKeyTyped
        rejectNonNumericKeys(evt);
    }//GEN-LAST:event_Camera4_Port_TextFieldKeyTyped

    private void E_Board4_TypeCBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_E_Board4_TypeCBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_E_Board4_TypeCBoxActionPerformed

    private void E_Board4_connTypeCBoxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_E_Board4_connTypeCBoxPopupMenuWillBecomeInvisible
        ChangeSettings.changeStatus_Manager(
                SettingsSaveButton, SettingsCancelButton, SettingsCloseButton, 
                changedControls, ((Component) evt.getSource()), E_Board4_connTypeCBox.getSelectedIndex(), 
                connectionType[E_Board.ordinal()][1]);        
    }//GEN-LAST:event_E_Board4_connTypeCBoxPopupMenuWillBecomeInvisible

    private void E_Board4_connTypeCBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_E_Board4_connTypeCBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_E_Board4_connTypeCBoxActionPerformed

    private void E_Board4_IP_TextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_E_Board4_IP_TextFieldKeyReleased
        changeButtonEnabledForIPchange(E_Board, 4, (Component) evt.getSource());
    }//GEN-LAST:event_E_Board4_IP_TextFieldKeyReleased

    private void E_Board4_Port_TextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_E_Board4_Port_TextFieldKeyReleased
        changeButtonEnabledForPortChange(E_Board, 4, (Component) evt.getSource());
    }//GEN-LAST:event_E_Board4_Port_TextFieldKeyReleased

    private void E_Board4_Port_TextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_E_Board4_Port_TextFieldKeyTyped
        rejectNonNumericKeys(evt);
    }//GEN-LAST:event_E_Board4_Port_TextFieldKeyTyped

    private void GateBar4_TypeCBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GateBar4_TypeCBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_GateBar4_TypeCBoxActionPerformed

    private void GateBar4_connTypeCBoxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_GateBar4_connTypeCBoxPopupMenuWillBecomeInvisible
        ChangeSettings.changeStatus_Manager(
                SettingsSaveButton, SettingsCancelButton, SettingsCloseButton, 
                changedControls, ((Component) evt.getSource()), GateBar4_connTypeCBox.getSelectedIndex(), 
                connectionType[GateBar.ordinal()][1]);        
    }//GEN-LAST:event_GateBar4_connTypeCBoxPopupMenuWillBecomeInvisible

    private void GateBar4_connTypeCBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GateBar4_connTypeCBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_GateBar4_connTypeCBoxActionPerformed

    private void GateBar4_IP_TextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GateBar4_IP_TextFieldKeyReleased
        changeButtonEnabledForIPchange(GateBar, 4, (Component) evt.getSource());
    }//GEN-LAST:event_GateBar4_IP_TextFieldKeyReleased

    private void GateBar4_Port_TextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GateBar4_Port_TextFieldKeyReleased
        changeButtonEnabledForPortChange(GateBar, 4, (Component) evt.getSource());
    }//GEN-LAST:event_GateBar4_Port_TextFieldKeyReleased

    private void GateBar4_Port_TextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GateBar4_Port_TextFieldKeyTyped
        rejectNonNumericKeys(evt);
    }//GEN-LAST:event_GateBar4_Port_TextFieldKeyTyped

    private void TextFieldGateName2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldGateName2KeyReleased
        checkGateNameChangeAndChangeEnabled(2, (Component) evt.getSource());
    }//GEN-LAST:event_TextFieldGateName2KeyReleased

    private void Camera2_connTypeCBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Camera2_connTypeCBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Camera2_connTypeCBoxActionPerformed

    private void Camera2_IP_TextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Camera2_IP_TextFieldKeyReleased
        changeButtonEnabledForIPchange(Camera, 2, (Component) evt.getSource());
    }//GEN-LAST:event_Camera2_IP_TextFieldKeyReleased

    private void Camera2_Port_TextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Camera2_Port_TextFieldKeyReleased
        changeButtonEnabledForPortChange(Camera, 2, (Component) evt.getSource());
    }//GEN-LAST:event_Camera2_Port_TextFieldKeyReleased

    private void Camera2_Port_TextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Camera2_Port_TextFieldKeyTyped
        rejectNonNumericKeys(evt);
    }//GEN-LAST:event_Camera2_Port_TextFieldKeyTyped

    private void E_Board2_TypeCBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_E_Board2_TypeCBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_E_Board2_TypeCBoxActionPerformed

    private void E_Board2_connTypeCBoxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_E_Board2_connTypeCBoxPopupMenuWillBecomeInvisible
        ChangeSettings.changeStatus_Manager(
                SettingsSaveButton, SettingsCancelButton, SettingsCloseButton, 
                changedControls, ((Component) evt.getSource()), E_Board2_connTypeCBox.getSelectedIndex(), 
                connectionType[E_Board.ordinal()][1]);        
    }//GEN-LAST:event_E_Board2_connTypeCBoxPopupMenuWillBecomeInvisible

    private void E_Board2_connTypeCBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_E_Board2_connTypeCBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_E_Board2_connTypeCBoxActionPerformed

    private void E_Board2_IP_TextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_E_Board2_IP_TextFieldKeyReleased
        changeButtonEnabledForIPchange(E_Board, 2, (Component) evt.getSource());
    }//GEN-LAST:event_E_Board2_IP_TextFieldKeyReleased

    private void E_Board2_Port_TextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_E_Board2_Port_TextFieldKeyReleased
        changeButtonEnabledForPortChange(E_Board, 2, (Component) evt.getSource());
    }//GEN-LAST:event_E_Board2_Port_TextFieldKeyReleased

    private void E_Board2_Port_TextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_E_Board2_Port_TextFieldKeyTyped
        rejectNonNumericKeys(evt);
    }//GEN-LAST:event_E_Board2_Port_TextFieldKeyTyped

    private void GateBar2_TypeCBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GateBar2_TypeCBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_GateBar2_TypeCBoxActionPerformed

    private void GateBar2_connTypeCBoxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_GateBar2_connTypeCBoxPopupMenuWillBecomeInvisible
        ChangeSettings.changeStatus_Manager(
                SettingsSaveButton, SettingsCancelButton, SettingsCloseButton, 
                changedControls, ((Component) evt.getSource()), GateBar2_connTypeCBox.getSelectedIndex(), 
                connectionType[GateBar.ordinal()][1]);        
    }//GEN-LAST:event_GateBar2_connTypeCBoxPopupMenuWillBecomeInvisible

    private void GateBar2_connTypeCBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GateBar2_connTypeCBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_GateBar2_connTypeCBoxActionPerformed

    private void GateBar2_IP_TextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GateBar2_IP_TextFieldKeyReleased
        changeButtonEnabledForIPchange(GateBar, 2, (Component) evt.getSource());
    }//GEN-LAST:event_GateBar2_IP_TextFieldKeyReleased

    private void GateBar2_Port_TextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GateBar2_Port_TextFieldKeyReleased
        changeButtonEnabledForPortChange(GateBar, 2, (Component) evt.getSource());
    }//GEN-LAST:event_GateBar2_Port_TextFieldKeyReleased

    private void GateBar2_Port_TextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GateBar2_Port_TextFieldKeyTyped
        rejectNonNumericKeys(evt);
    }//GEN-LAST:event_GateBar2_Port_TextFieldKeyTyped

    private void E_Board1_TypeCBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_E_Board1_TypeCBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_E_Board1_TypeCBoxActionPerformed

    private void E_Board1_connTypeCBoxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_E_Board1_connTypeCBoxPopupMenuWillBecomeInvisible
        ChangeSettings.changeStatus_Manager(
                SettingsSaveButton, SettingsCancelButton, SettingsCloseButton, 
                changedControls, ((Component) evt.getSource()), E_Board1_connTypeCBox.getSelectedIndex(), 
                connectionType[E_Board.ordinal()][1]);
    }//GEN-LAST:event_E_Board1_connTypeCBoxPopupMenuWillBecomeInvisible

    private void E_Board1_connTypeCBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_E_Board1_connTypeCBoxActionPerformed
    }//GEN-LAST:event_E_Board1_connTypeCBoxActionPerformed

    private void E_Board1_IP_TextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_E_Board1_IP_TextFieldKeyReleased
        changeButtonEnabledForIPchange(E_Board, 1, (Component) evt.getSource());
    }//GEN-LAST:event_E_Board1_IP_TextFieldKeyReleased

    private void E_Board1_Port_TextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_E_Board1_Port_TextFieldKeyReleased
        changeButtonEnabledForPortChange(E_Board, 1, (Component) evt.getSource());
    }//GEN-LAST:event_E_Board1_Port_TextFieldKeyReleased

    private void E_Board1_Port_TextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_E_Board1_Port_TextFieldKeyTyped
        rejectNonNumericKeys(evt);
    }//GEN-LAST:event_E_Board1_Port_TextFieldKeyTyped

    private void GateBar1_TypeCBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GateBar1_TypeCBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_GateBar1_TypeCBoxActionPerformed

    private void GateBar1_connTypeCBoxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_GateBar1_connTypeCBoxPopupMenuWillBecomeInvisible
        ChangeSettings.changeStatus_Manager(
                SettingsSaveButton, SettingsCancelButton, SettingsCloseButton, 
                changedControls, ((Component) evt.getSource()), GateBar1_connTypeCBox.getSelectedIndex(), 
                connectionType[GateBar.ordinal()][1]);
    }//GEN-LAST:event_GateBar1_connTypeCBoxPopupMenuWillBecomeInvisible

    private void GateBar1_connTypeCBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GateBar1_connTypeCBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_GateBar1_connTypeCBoxActionPerformed

    private void GateBar1_IP_TextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GateBar1_IP_TextFieldKeyReleased
        changeButtonEnabledForIPchange(GateBar, 1, (Component) evt.getSource());
    }//GEN-LAST:event_GateBar1_IP_TextFieldKeyReleased

    private void GateBar1_Port_TextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GateBar1_Port_TextFieldKeyReleased
        changeButtonEnabledForPortChange(GateBar, 1, (Component) evt.getSource());
    }//GEN-LAST:event_GateBar1_Port_TextFieldKeyReleased

    private void GateBar1_Port_TextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GateBar1_Port_TextFieldKeyTyped
        rejectNonNumericKeys(evt);
    }//GEN-LAST:event_GateBar1_Port_TextFieldKeyTyped

    private void PopSizeCBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PopSizeCBoxActionPerformed
        /**
         * Check selected item and record into the changed control set if needed.
         */
        ChangeSettings.changeStatus_Manager(SettingsSaveButton, SettingsCancelButton, SettingsCloseButton, 
                changedControls, (Component) evt.getSource(), PopSizeCBox.getSelectedIndex(), 
                statCountIndex);        
    }//GEN-LAST:event_PopSizeCBoxActionPerformed

    private void PopSizeHelpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PopSizeHelpButtonActionPerformed
        String helpText = PopSizeHelp1.getContent() + System.lineSeparator() +
                PopSizeHelp2.getContent() + System.lineSeparator() +
                PopSizeHelp3.getContent() + System.lineSeparator() +
                System.lineSeparator() +
                PopSizeHelp4.getContent() + System.lineSeparator() +
                PopSizeHelp5.getContent() + System.lineSeparator() +
                PopSizeHelp6.getContent() + System.lineSeparator();

        JDialog helpDialog = new PWHelpJDialog(this, false,
            STATISTICS_SIZE_LABEL.getContent(), helpText, false);
        locateAndShowHelpDialog(this, helpDialog, PopSizeHelpButton);
    }//GEN-LAST:event_PopSizeHelpButtonActionPerformed

    private void SettingsCloseButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_SettingsCloseButtonStateChanged
        EBoardSettingsButton.setEnabled(SettingsCloseButton.isEnabled());
    }//GEN-LAST:event_SettingsCloseButtonStateChanged

    private void Camera1_TypeCBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_Camera1_TypeCBoxItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            setButtonEnabled_If_DeviceTypeChanged(1, Camera);
        }
    }//GEN-LAST:event_Camera1_TypeCBoxItemStateChanged

    private void Camera2_TypeCBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_Camera2_TypeCBoxItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            setButtonEnabled_If_DeviceTypeChanged(2, Camera);
        }
    }//GEN-LAST:event_Camera2_TypeCBoxItemStateChanged

    private void Camera3_TypeCBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_Camera3_TypeCBoxItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            setButtonEnabled_If_DeviceTypeChanged(3, Camera);
        }
    }//GEN-LAST:event_Camera3_TypeCBoxItemStateChanged

    private void Camera4_TypeCBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_Camera4_TypeCBoxItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            setButtonEnabled_If_DeviceTypeChanged(4, Camera);
        }
    }//GEN-LAST:event_Camera4_TypeCBoxItemStateChanged

    private void E_Board2_TypeCBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_E_Board2_TypeCBoxItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            setButtonEnabled_If_DeviceTypeChanged(2, E_Board);
        }
    }//GEN-LAST:event_E_Board2_TypeCBoxItemStateChanged

    private void E_Board1_TypeCBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_E_Board1_TypeCBoxItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            setButtonEnabled_If_DeviceTypeChanged(1, E_Board);
        }
    }//GEN-LAST:event_E_Board1_TypeCBoxItemStateChanged

    private void E_Board3_TypeCBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_E_Board3_TypeCBoxItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            setButtonEnabled_If_DeviceTypeChanged(3, E_Board);
        }
    }//GEN-LAST:event_E_Board3_TypeCBoxItemStateChanged

    private void E_Board4_TypeCBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_E_Board4_TypeCBoxItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            setButtonEnabled_If_DeviceTypeChanged(4, E_Board);
        }
    }//GEN-LAST:event_E_Board4_TypeCBoxItemStateChanged

    private void GateBar1_TypeCBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_GateBar1_TypeCBoxItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            setButtonEnabled_If_DeviceTypeChanged(1, GateBar);
        }
    }//GEN-LAST:event_GateBar1_TypeCBoxItemStateChanged

    private void GateBar2_TypeCBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_GateBar2_TypeCBoxItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            setButtonEnabled_If_DeviceTypeChanged(2, GateBar);
        }
    }//GEN-LAST:event_GateBar2_TypeCBoxItemStateChanged

    private void GateBar3_TypeCBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_GateBar3_TypeCBoxItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            setButtonEnabled_If_DeviceTypeChanged(3, GateBar);
        }
    }//GEN-LAST:event_GateBar3_TypeCBoxItemStateChanged

    private void GateBar4_TypeCBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_GateBar4_TypeCBoxItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            setButtonEnabled_If_DeviceTypeChanged(4, GateBar);
        }
    }//GEN-LAST:event_GateBar4_TypeCBoxItemStateChanged

    private void E_Board4_connTypeCBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_E_Board4_connTypeCBoxItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            setButtonEnabled_If_ConnTypeChanged(4, GateBar);
        }
    }//GEN-LAST:event_E_Board4_connTypeCBoxItemStateChanged

    private void E_Board1_connTypeCBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_E_Board1_connTypeCBoxItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_E_Board1_connTypeCBoxItemStateChanged

    private void E_Board2_connTypeCBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_E_Board2_connTypeCBoxItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_E_Board2_connTypeCBoxItemStateChanged

    private void E_Board3_connTypeCBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_E_Board3_connTypeCBoxItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_E_Board3_connTypeCBoxItemStateChanged
                                              
    void closeSettingsForm() {
        if(mainForm != null)
            mainForm.setConfigureSettingsForm(null);
        
        if (isStand_Alone) {
            this.setVisible(false);
            System.exit(0);
        } else {
            dispose();
        }
    }    
    
    /**
     * Initiate the configuration settings form of OS.Parking Program by itself.
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc="-- Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Settings_System.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Settings_System.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Settings_System.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Settings_System.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        initializeLoggers();
        checkOptions(args);
        readSettings();
        EBD_DisplaySettings = DB_Access.readEBoardSettings();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //ControlGUI mainForm = new ControlGUI();
                Settings_System form = new Settings_System(null);
                form.setVisible(true);
            }
        });
    }

    //<editor-fold desc="-- Automatically generated form controls">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox BlinkingComboBox;
    private javax.swing.JTextField Camera1_IP_TextField;
    private javax.swing.JTextField Camera1_Port_TextField;
    private javax.swing.JComboBox Camera1_TypeCBox;
    private javax.swing.JComboBox Camera1_connTypeCBox;
    private javax.swing.JTextField Camera2_IP_TextField;
    private javax.swing.JTextField Camera2_Port_TextField;
    private javax.swing.JComboBox Camera2_TypeCBox;
    private javax.swing.JComboBox Camera2_connTypeCBox;
    private javax.swing.JTextField Camera3_IP_TextField;
    private javax.swing.JTextField Camera3_Port_TextField;
    private javax.swing.JComboBox Camera3_TypeCBox;
    private javax.swing.JComboBox Camera3_connTypeCBox;
    private javax.swing.JTextField Camera4_IP_TextField;
    private javax.swing.JTextField Camera4_Port_TextField;
    private javax.swing.JComboBox Camera4_TypeCBox;
    private javax.swing.JComboBox Camera4_connTypeCBox;
    private com.toedter.components.JLocaleChooser DateChooserLangCBox;
    private javax.swing.JPanel EBD_settings;
    private javax.swing.JPanel EBD_settings_label;
    private javax.swing.JButton EBoardSettingsButton;
    private javax.swing.JTextField E_Board1_IP_TextField;
    private javax.swing.JTextField E_Board1_Port_TextField;
    private javax.swing.JComboBox E_Board1_TypeCBox;
    private javax.swing.JComboBox E_Board1_connTypeCBox;
    private javax.swing.JTextField E_Board2_IP_TextField;
    private javax.swing.JTextField E_Board2_Port_TextField;
    private javax.swing.JComboBox E_Board2_TypeCBox;
    private javax.swing.JComboBox E_Board2_connTypeCBox;
    private javax.swing.JTextField E_Board3_IP_TextField;
    private javax.swing.JTextField E_Board3_Port_TextField;
    private javax.swing.JComboBox E_Board3_TypeCBox;
    private javax.swing.JComboBox E_Board3_connTypeCBox;
    private javax.swing.JTextField E_Board4_IP_TextField;
    private javax.swing.JTextField E_Board4_Port_TextField;
    private javax.swing.JComboBox E_Board4_TypeCBox;
    private javax.swing.JComboBox E_Board4_connTypeCBox;
    private javax.swing.JPanel E_BoardSettingsButtonPanel;
    private javax.swing.JComboBox FlowingComboBox;
    private javax.swing.JTextField GateBar1_IP_TextField;
    private javax.swing.JTextField GateBar1_Port_TextField;
    private javax.swing.JComboBox GateBar1_TypeCBox;
    private javax.swing.JComboBox GateBar1_connTypeCBox;
    private javax.swing.JTextField GateBar2_IP_TextField;
    private javax.swing.JTextField GateBar2_Port_TextField;
    private javax.swing.JComboBox GateBar2_TypeCBox;
    private javax.swing.JComboBox GateBar2_connTypeCBox;
    private javax.swing.JTextField GateBar3_IP_TextField;
    private javax.swing.JTextField GateBar3_Port_TextField;
    private javax.swing.JComboBox GateBar3_TypeCBox;
    private javax.swing.JComboBox GateBar3_connTypeCBox;
    private javax.swing.JTextField GateBar4_IP_TextField;
    private javax.swing.JTextField GateBar4_Port_TextField;
    private javax.swing.JComboBox GateBar4_TypeCBox;
    private javax.swing.JComboBox GateBar4_connTypeCBox;
    private javax.swing.JComboBox GateCountComboBox;
    public javax.swing.JTabbedPane GatesTabbedPane;
    private javax.swing.JComboBox ImageDurationCBox;
    private javax.swing.JLabel ImageDurationLabel;
    private javax.swing.JButton LanguageHelpButton;
    private javax.swing.JButton LoggingLevelHelpButton;
    private javax.swing.JComboBox MessageMaxLineComboBox;
    private javax.swing.JComboBox OptnLoggingLevelComboBox;
    private javax.swing.JButton PWHelpButton;
    /*
    private javax.swing.JComboBox PWStrengthChoiceComboBox;
    */
    private javax.swing.JComboBox<ConvComboBoxItem> PWStrengthChoiceComboBox;
    private javax.swing.JComboBox PopSizeCBox;
    private javax.swing.JButton PopSizeHelpButton;
    private javax.swing.JCheckBox RecordPassingDelayChkBox;
    private javax.swing.JButton SettingsCancelButton;
    private javax.swing.JButton SettingsCloseButton;
    private javax.swing.JButton SettingsSaveButton;
    private javax.swing.JTextField TextFieldGateName1;
    private javax.swing.JTextField TextFieldGateName2;
    private javax.swing.JTextField TextFieldGateName3;
    private javax.swing.JTextField TextFieldGateName4;
    private javax.swing.JTextField TextFieldPicHeight;
    private javax.swing.JTextField TextFieldPicWidth;
    private javax.swing.JPanel allCyclesPanel;
    private javax.swing.JLabel attendantGUI_title;
    private javax.swing.JPanel blinkPanel;
    private javax.swing.JLabel blinkingL;
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JPanel cBoxPan;
    private javax.swing.JPanel cBoxPanel;
    private javax.swing.JPanel cameraPan;
    private javax.swing.JPanel cameraPan2;
    private javax.swing.JPanel cameraPan3;
    private javax.swing.JPanel cameraPan4;
    private javax.swing.JPanel cycleLabel;
    private javax.swing.JLabel device1_Label;
    private javax.swing.JLabel device1_Label2;
    private javax.swing.JLabel device1_Label3;
    private javax.swing.JLabel device1_Label4;
    private javax.swing.JPanel eBoardPan2;
    private javax.swing.JPanel eBoardPan3;
    private javax.swing.JPanel eBoardPan4;
    private javax.swing.JPanel eBoardPan5;
    private javax.swing.JPanel eBoardSettingPanel;
    private javax.swing.JLabel ebdLbl2;
    private javax.swing.JLabel ebdLbl3;
    private javax.swing.JLabel ebdLbl4;
    private javax.swing.JLabel ebdLbl5;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler10;
    private javax.swing.Box.Filler filler13;
    private javax.swing.Box.Filler filler14;
    private javax.swing.Box.Filler filler15;
    private javax.swing.Box.Filler filler17;
    private javax.swing.Box.Filler filler18;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler23;
    private javax.swing.Box.Filler filler24;
    private javax.swing.Box.Filler filler25;
    private javax.swing.Box.Filler filler26;
    private javax.swing.Box.Filler filler27;
    private javax.swing.Box.Filler filler28;
    private javax.swing.Box.Filler filler29;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler30;
    private javax.swing.Box.Filler filler31;
    private javax.swing.Box.Filler filler32;
    private javax.swing.Box.Filler filler33;
    private javax.swing.Box.Filler filler34;
    private javax.swing.Box.Filler filler35;
    private javax.swing.Box.Filler filler36;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler6;
    private javax.swing.Box.Filler filler7;
    private javax.swing.Box.Filler filler8;
    private javax.swing.Box.Filler filler9;
    private javax.swing.JPanel flowPanel;
    private javax.swing.JPanel gate1Panel;
    private javax.swing.JPanel gate2Panel;
    private javax.swing.JPanel gate3Panel;
    private javax.swing.JPanel gate4Panel;
    private javax.swing.JPanel gateBarPan2;
    private javax.swing.JPanel gateBarPan3;
    private javax.swing.JPanel gateBarPan4;
    private javax.swing.JPanel gateBarPan5;
    private javax.swing.JLabel gateNameLabel1;
    private javax.swing.JLabel gateNameLabel3;
    private javax.swing.JLabel gateNameLabel4;
    private javax.swing.JLabel gateNameLabel5;
    private javax.swing.JPanel gateSettingPanel;
    private javax.swing.JPanel gate_name_p;
    private javax.swing.JPanel gate_name_p2;
    private javax.swing.JPanel gate_name_p3;
    private javax.swing.JPanel gate_name_p4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel labelBlink;
    private javax.swing.JPanel labelFlow;
    private javax.swing.JTextField lotNameTextField;
    private javax.swing.JLabel myMetaKeyLabel;
    private javax.swing.JPanel parkinglotOptionPanel;
    private javax.swing.JLabel pxLabel1;
    private javax.swing.JLabel pxLabel2;
    private javax.swing.JPanel real2Pan;
    private javax.swing.JPanel titlePanel;
    private javax.swing.JPanel topLabelsPanel;
    private javax.swing.JPanel topLabelsPanel2;
    private javax.swing.JPanel topLabelsPanel3;
    private javax.swing.JPanel topLabelsPanel4;
    private javax.swing.JPanel twoCycles;
    private javax.swing.JPanel wholePanel;
    // End of variables declaration//GEN-END:variables
    //</editor-fold>

    /**
     * Read settings values from the DB table and implant form component values using them.
     */
    private void loadComponentValues() {
        lotNameTextField.setText(parkingLotName);
        RecordPassingDelayChkBox.setSelected(storePassingDelay);
        PWStrengthChoiceComboBox.setSelectedIndex(pwStrengthLevel);
        OptnLoggingLevelComboBox.setSelectedIndex(opLoggingIndex);
        DateChooserLangCBox.setSelectedItem(parkingLotLocale.getDisplayName());
        localeIndex = (short) DateChooserLangCBox.getSelectedIndex();
        
        PopSizeCBox.setSelectedIndex(statCountIndex);
        MessageMaxLineComboBox.setSelectedItem(String.valueOf(maxMessageLines));
        GateCountComboBox.setSelectedIndex(gateCount - 1);
        ImageDurationCBox.setSelectedIndex(maxArrivalCBoxIndex);

        TextFieldPicWidth.setText(String.valueOf(new DecimalFormat("#,##0").format(PIC_WIDTH)));
        TextFieldPicHeight.setText(String.valueOf(new DecimalFormat("#,##0").format(PIC_HEIGHT)));
        BlinkingComboBox.setSelectedItem(String.valueOf(new DecimalFormat("#,##0").format(EBD_blinkCycle)));
        FlowingComboBox.setSelectedItem(String.valueOf(new DecimalFormat("#,##0").format(EBD_flowCycle)));
        
        for (int i = 0; i < 4; i++) {
            GatesTabbedPane.setEnabledAt(i, false);
        }
        
        for (int i = 0; i < gateCount; i++) {
            //<editor-fold desc="-- Init device IP address and Port numbers">
            GatesTabbedPane.setEnabledAt(i, true);

            // fill gate name textfields
            ((JTextField)getComponentByName("TextFieldGateName" +(i+1))).setText(gateNames[i+1]);
            
            // load 3 device IP address textfields
            ((JTextField)getComponentByName("Camera" +(i+1) + "_IP_TextField"))
                    .setText(deviceIP[Camera.ordinal() ][i+1]);
            
            ((JTextField)getComponentByName("GateBar" +(i+1) + "_IP_TextField"))
                    .setText(deviceIP[GateBar.ordinal() ][i+1]);
            
            ((JTextField)getComponentByName("E_Board" +(i+1) + "_IP_TextField"))
                    .setText(deviceIP[E_Board.ordinal() ][i+1]);
            
            // load 3 device port textfields
            ((JTextField)getComponentByName("Camera" +(i+1) + "_Port_TextField"))
                    .setText(devicePort[Camera.ordinal() ][i+1]);
            
            ((JTextField)getComponentByName("GateBar" +(i+1) + "_Port_TextField"))
                    .setText(devicePort[GateBar.ordinal() ][i+1]);
            
            ((JTextField)getComponentByName("E_Board" +(i+1) + "_Port_TextField"))
                    .setText(devicePort[E_Board.ordinal() ][i+1]);
            //</editor-fold>
        }
        
        for (int gate = 1; gate <= gateCount; gate++) { 
            //<editor-fold desc="-- Combo Box item init for device and connection type of each gate">
            JComboBox comboBx = ((JComboBox)getComponentByName("Camera" +gate + "_TypeCBox"));
            if (comboBx != null) {
                comboBx.setSelectedIndex(DB_Access.deviceType[Camera.ordinal()][gate]);
            }
            
            comboBx = ((JComboBox)getComponentByName("E_Board" +gate + "_TypeCBox"));
            if (comboBx != null) {
                comboBx.setSelectedIndex(DB_Access.deviceType[E_Board.ordinal()][gate]);
            }
            
            comboBx = ((JComboBox)getComponentByName("GateBar" +gate + "_TypeCBox"));
            if (comboBx != null) {
                comboBx.setSelectedIndex(DB_Access.deviceType[GateBar.ordinal()][gate]);
            }
            
            for (DeviceType devType: DeviceType.values()) {
                comboBx = ((JComboBox)getComponentByName(devType.name() +gate + "_connTypeCBox"));
                if (comboBx != null) {
                    comboBx.setSelectedIndex(DB_Access.connectionType[devType.ordinal()][gate]);
                }
            }
            //</editor-fold>
        }
        
        enableSaveCancelButtons(false);
    }
    
    /**
     * Load password complexity level selection combo box options.
     */
    private void addPWStrengthItems() {
        PWStrengthChoiceComboBox.removeAllItems();
        
        ConvComboBoxItem CBItem =  null; 

        for (PWStrengthLevel level : PWStrengthLevel.values()) {
            switch (level) {
                case FourDigit:
                    CBItem = new ConvComboBoxItem(level, FOUR_DIGIT_CB_ITEM.getContent());
                    break;
                case SixDigit:
                    CBItem = new ConvComboBoxItem(level, SIX_DIGIT_CB_ITEM.getContent());
                    break;
                case Complex:
                    CBItem = new ConvComboBoxItem(level, COMPLEX_CB_ITEM.getContent());
                    break;
                default:
            }
            PWStrengthChoiceComboBox.addItem((ConvComboBoxItem) CBItem);
        }
    }
    
    @SuppressWarnings("unchecked")
    /**
     * Load normal operation logging level combo box options.
     */
    private void addOperationLoggingLevelOptions() {
        OptnLoggingLevelComboBox.removeAllItems();
        
        for (OpLogLevel level : OpLogLevel.values()) {
            switch (level) {
                case LogAlways:
                    OptnLoggingLevelComboBox.addItem(NO_LOGGING_CB_ITEM.getContent());
                    break;
                case SettingsChange:
                    OptnLoggingLevelComboBox.addItem(SETTING_CHANGE_CB_ITEM.getContent());
                    break;
                case EBDsettingsChange:
                    OptnLoggingLevelComboBox.addItem(EBD_CHANGE_CB_ITEM.getContent());
                    break;
            }
        }        
    }

    /**
     * Checks if natural number is entered and give warning when non-numeric entered.
     * @param textField field whose text is supposed to contain a number
     * @param dialogTitle title of the dialog box that will be shown when non-natural number string is entered
     * @return <b>true</b> when a natural number is in the <code>textField</code>, 
     * <b>false</b> otherwise
     */
    private boolean TextFieldNumericValueOK(JTextField textField, String dialogTitle) {
        // Check if empty string or numeric 0 were entered. 
        String input = textField.getText().trim();
        
        input = input.replace(",", "");
        
        if (input.length() == 0 || Integer.parseInt(input) == 0) {
            JOptionPane.showConfirmDialog(this, "Enter a value of 1 or more ..",
                    dialogTitle, JOptionPane.PLAIN_MESSAGE, WARNING_MESSAGE);
            textField.requestFocusInWindow();
            textField.select(0, input.length());
            return false;
        }  else {
            return true;
        }      
    }

    private void initPassingDelayStatIfNeeded(int newStatCount, int gateID) {
        if (storePassingDelay) {
            /**
             * Passing Delay Statistics Population Size Changed while Statistics Are Gathered.
             */
            if (newStatCount != statCount) { 
                if (passingCountCurrent[gateID] != 0) {
                    String secs = String.format("%.3f " + SECONDS_LABEL.getContent(), 
                            (float)passingDelayCurrentTotalMs[gateID]/passingCountCurrent[gateID]/1000f);
                    
                    String msg4GUI = "(#" + gateID + ")" + RECENT_WORD.getContent() + 
                            passingCountCurrent[gateID] + AVERAGE_WORDS.getContent() + secs;
                    if (DEBUG) {
                        logParkingOperation(OpLogLevel.LogAlways, 
                                msg4GUI + System.getProperty("line.separator"), GENERAL_DEVICE);
                    }
                    if (mainForm != null)
                        addMessageLine(mainForm.getMessageTextArea(), msg4GUI);
                    initializePassingDelayStatistics(gateID);
                }
            } 
        } else {
            initializePassingDelayStatistics(gateID);
        }
    }

    private boolean someIPaddressWrong() {
        InetAddressValidator validator = InetAddressValidator.getInstance();

        for (int i = 0; i < gateCount; i++) {
            JTextField txtField;
            
            for (DeviceType devType : DeviceType.values()) {
                String devName = devType.toString();
                txtField = (JTextField)getComponentByName(devName + (i+1) + "_IP_TextField");
                if (!validator.isValidInet4Address( txtField.getText())) {
                    GatesTabbedPane.setSelectedIndex(i);
                    txtField.requestFocusInWindow();
                    JOptionPane.showConfirmDialog(this,
                            devType.getContent() + " #" + (i+1) + " " + IP_ADDR_ERROR_1.getContent() 
                                    + System.lineSeparator() +
                                    IP_ADDR_ERROR_2.getContent() + "127.0.0.1",
                            IP_ERROR_TITLE.getContent(), JOptionPane.PLAIN_MESSAGE, WARNING_MESSAGE);
                    return true;
                }
            }
        }        
        return false;
    }
    
    public Component getComponentByName(String name) {
        if (componentMap.containsKey(name)) {
            return (Component) componentMap.get(name);
        }
        else 
            return null;
    }    

    @SuppressWarnings("unchecked")
    private void addMaxArrivalItems() {
        Integer options[] = {1, 7, 30, 60, 90, 120};
        
        for (Integer duration : options) {
            ImageDurationCBox.addItem(new ConvComboBoxItem(duration, 
                    duration.toString() + " " + DAY_SUFFIX.getContent()));
        }
    }

    private short findCBoxIndex(JComboBox maxArrivalComboBox, int maxArrival) {
        short index = -1;

        Object item;
        for (short idx = 0; idx < maxArrivalComboBox.getItemCount(); idx++) {
            item = maxArrivalComboBox.getItemAt(idx);
            if (item.getClass() == ConvComboBoxItem.class 
                    && (Integer)((ConvComboBoxItem)item).getKeyValue() == maxArrival) {
                index = idx;
                break;
            }
        }
        return index;
    }        

    public void disposeEBoardDialog() {
        eBoardDialog.dispose();
    }

    private int saveGateDevices() {
        Connection conn = null;
        PreparedStatement updateSettings = null;
        int result = 0;
        int updateRowCount = 0;
        
        //<editor-fold desc="-- Create update statement">
        StringBuffer sb = new StringBuffer("Update gatedevices SET ");
        sb.append("  gatename = ? ");
        
        sb.append("  , cameraType = ?");
        sb.append("  , cameraIP = ? ");
        sb.append("  , cameraPort = ?");
        
        sb.append("  , e_boardType = ? ");
        sb.append("  , e_boardConnType = ? ");
        sb.append("  , e_boardCOM_ID = ? ");
        
        sb.append("  , e_boardIP = ? ");
        sb.append("  , e_boardPort = ? ");
        
        sb.append("  , gatebarType = ? ");
        sb.append("  , gatebarConnType = ? ");
        sb.append("  , gatebarCOM_ID = ? ");
        
        sb.append("  , gatebarIP = ? ");
        sb.append("  , gatebarPort = ? ");
        
        sb.append("WHERE GateID = ?");
        //</editor-fold>

        for (int gateID = 1; gateID <= gateCount; gateID++) {
            try 
            {
                conn = JDBCMySQL.getConnection();
                updateSettings = conn.prepareStatement (sb.toString());

                int pIndex = 1;
                JComboBox cBox;

                //<editor-fold defaultstate="collapsed" desc="--Provide actual values to the UPDATE">
                updateSettings.setString(pIndex++, 
                        ((JTextField) componentMap.get("TextFieldGateName" + gateID)).getText().trim());
                
                for (DeviceType type : DeviceType.values()) {
                    cBox = (JComboBox)componentMap.get(type.toString() + gateID + "_TypeCBox");
                    updateSettings.setInt(pIndex++, cBox == null ? 0 : cBox.getSelectedIndex());
                    if (type != Camera) {
                        cBox = (JComboBox)componentMap.get(type.toString() + gateID + "_connTypeCBox");
                        updateSettings.setInt(pIndex++, cBox == null ? 0 : cBox.getSelectedIndex());
                        
                        cBox = (JComboBox)componentMap.get(type.toString() + gateID + "_comID_CBox");
                        updateSettings.setString(pIndex++, cBox == null ? "" : (String)cBox.getSelectedItem());
                    }
                    updateSettings.setString(pIndex++, 
                            ((JTextField) componentMap.get(type.toString() + gateID + "_IP_TextField")).getText().trim());
                    updateSettings.setString(pIndex++, 
                            ((JTextField) componentMap.get(type.toString() + gateID + "_Port_TextField")).getText().trim());
                }
                updateSettings.setInt(pIndex++, gateID);
                // </editor-fold>

                result = updateSettings.executeUpdate();
                
            } catch (SQLException se) {
                Globals.logParkingException(Level.SEVERE, se, "(Save gate & device settings)");
            } finally {
                // <editor-fold defaultstate="collapsed" desc="--Return resources and display the save result">
                closeDBstuff(conn, updateSettings, null, "(Save gate & device settings)");

                if (result == 1) {
                    updateRowCount++;
                }
                // </editor-fold>
            }  
        }
        return updateRowCount;
    }

    private void initializePassingDelayStatistics(int gateID) {
        int result = 0;
        Connection conn = null;
        PreparedStatement pStmt = null;    
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE gatedevices ");
        sql.append("SET passingCountCurrent = 0, ");
        sql.append("  passingDelayCurrentTotalMs = 0 ");
        sql.append("WHERE gateid = ?");
        
        try 
        {
            conn = JDBCMySQL.getConnection();
            pStmt = conn.prepareStatement(sql.toString());
            
            int loc = 1;
            pStmt.setInt(loc++, gateID);
            result = pStmt.executeUpdate();
        } catch(Exception e) {
            logParkingException(Level.SEVERE, e, "(init passing delay statistics)");
        } finally {
            if (result != 1)
                logParkingException(Level.SEVERE, null, "(failed initialization of passing delay statistics)");
                
            closeDBstuff(conn, pStmt, null, "(init passing delay statistics)");
        }  
    }
    
    private void enableSaveCancelButtons(boolean enable) {
        SettingsSaveButton.setEnabled(enable);
        SettingsCancelButton.setEnabled(enable);        
        SettingsCloseButton.setEnabled(!enable);
        EBoardSettingsButton.setEnabled(!enable);
        if (!enable) {
            changedControls.clear();
        }
    } 

    private void tryToCloseSettingsForm() {
        if (SettingsSaveButton.isEnabled()) {
            JOptionPane.showMessageDialog(this, "Settings Changed.\n \n"
                    + "Either [Save] or [Cancel], please.",
                "Confirm Request", JOptionPane.WARNING_MESSAGE);
            this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        } else {
            closeSettingsForm();
        }     
    }

    private void checkGateNameChangeAndChangeEnabled(int gateNo, Component comp) {
        JTextField gateNameField = (JTextField)componentMap.get("TextFieldGateName" + gateNo);
        
        ChangeSettings.changeStatus_Manager(SettingsSaveButton, SettingsCancelButton, SettingsCloseButton, 
                changedControls, comp, gateNameField.getText().trim(), gateNames[gateNo]);
    }

    private void changeButtonEnabledForIPchange(DeviceType device, int gateNo, Component comp) {
        JTextField deviceIpField = (JTextField)componentMap.get(device.toString() + gateNo + "_IP_TextField");
        
        ChangeSettings.changeStatus_Manager(SettingsSaveButton, SettingsCancelButton, SettingsCloseButton, 
                changedControls, comp, deviceIpField.getText().trim(), deviceIP[device.ordinal()][gateNo]);
    }
// changeButtonEnabledForIPchange
    private void changeButtonEnabledForPortChange(DeviceType device, 
            int gateNo, Component comp) 
    {
        JTextField devicePortField 
                = (JTextField)componentMap.get(device.toString() + gateNo + "_Port_TextField");
        
        ChangeSettings.changeStatus_Manager(SettingsSaveButton, SettingsCancelButton, 
                SettingsCloseButton, changedControls, comp, devicePortField.getText().trim(), 
                devicePort[device.ordinal()][gateNo]);
    }

    private void makeEnterActAsTab() {
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke ctrlTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.CTRL_DOWN_MASK);
        Set<KeyStroke> keys = new HashSet<>();
        keys.add(enter);
        keys.add(tab);
        keys.add(ctrlTab);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().
                setDefaultFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, keys);    
    }

    private void rejectNonNumericKeys(KeyEvent evt) {
        char c = evt.getKeyChar();
        if ( !(
                (c >= '0') && (c <= '9') ||
                (c == KeyEvent.VK_BACK_SPACE) ||
                (c == KeyEvent.VK_DELETE) ||
                (c == KeyEvent.VK_ENTER)
                ))
        {
            getToolkit().beep();
            evt.consume();
        }    
    }

    private void changeSaveEnabledForConnTypeChange(DeviceType currDevType, int gateNo) {
        JComboBox cBox = (JComboBox)componentMap.get("" + currDevType + gateNo + "_connTypeCBox");
        
        if (cBox.getSelectedIndex() == CommonData.NOT_SELECTED 
                || cBox.getSelectedIndex() == connectionType[currDevType.ordinal()][gateNo])
        {
            enableSaveCancelButtons(false);
        } else {
            enableSaveCancelButtons(true);
        }   
        
        JTextField txtField;
        if (cBox.getSelectedIndex() == RS_232.ordinal()) {
            txtField = (JTextField)componentMap.get(currDevType.toString() + gateNo + "_IP_TextField");
            txtField.setEnabled(false);
            txtField = (JTextField)componentMap.get(currDevType.toString() + gateNo + "_Port_TextField");
            txtField.setEnabled(false);
        } else {
            txtField = (JTextField)componentMap.get(currDevType.toString() + gateNo + "_IP_TextField");
            txtField.setEnabled(true);
            txtField = (JTextField)componentMap.get(currDevType.toString() + gateNo + "_Port_TextField");
            txtField.setEnabled(true);
        }
    }

    private void showSerialConnectionDetail(final DeviceType deviceType, final int gateNo) {
        JPanel detailPan = (JPanel)componentMap.get (deviceType.toString() + gateNo + "_conn_detail_Pan");
        detailPan.removeAll();

        JLabel comLabel = new javax.swing.JLabel();
        //<editor-fold desc="-- COM 포트 레이블 생성 및 추가">
        comLabel.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        comLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        comLabel.setText("COM port");
        comLabel.setPreferredSize(new java.awt.Dimension(90, 15));

        comLabel.getAccessibleContext().setAccessibleName("COM_Lbl");
        //</editor-fold>

        final JComboBox comPortID_CBox = new javax.swing.JComboBox();
        //<editor-fold desc="-- 시리얼 콤포트 번호 선택 콤보박스 생성 및 추가">
        comPortID_CBox.setFont(new java.awt.Font(font_Type, font_Style, font_Size));
        comPortID_CBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"3", "4", "5", "6"}));
        comPortID_CBox.setMinimumSize(new java.awt.Dimension(50, 23));
        comPortID_CBox.setName(deviceType.toString() + gateNo + "_comID_CBox"); // NOI18N
        comPortID_CBox.setPreferredSize(new java.awt.Dimension(50, 23));
        comPortID_CBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                ChangeSettings.changeStatus_Manager(SettingsSaveButton, SettingsCancelButton, SettingsCloseButton, 
                        changedControls, ((Component) evt.getSource()), 
                        (String) comPortID_CBox.getSelectedItem(), 
                        deviceComID[deviceType.ordinal()][gateNo]);                
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        comPortID_CBox.setSelectedItem(deviceComID[deviceType.ordinal()][gateNo]);
        //</editor-fold>

        detailPan.setLayout (new java.awt.FlowLayout(FlowLayout.CENTER, 5, 3));
        detailPan.add(comLabel);
        detailPan.add(comPortID_CBox);
        
        String name = ((JComboBox)comPortID_CBox).getName();

        if (name != null && name.length() > 0) {
            componentMap.put(name, comPortID_CBox);
        }
    }

    private void showSocketConnectionDetail(DeviceType deviceType, int gateNo) {
        String controlPrefix = deviceType.toString() + gateNo;
        JPanel detailPan = (JPanel)componentMap.get (controlPrefix + "_conn_detail_Pan");
        detailPan.removeAll();
        detailPan.setLayout(new java.awt.BorderLayout());
        
        Object obj = componentMap.get(controlPrefix + "_IP_TextField");
        detailPan.add((JTextField)obj, java.awt.BorderLayout.WEST);
        
        obj = componentMap.get(controlPrefix + "_Port_TextField"); 
        detailPan.add((JTextField)obj, java.awt.BorderLayout.EAST);
    }

    private boolean someCOMportIDsame() {
        ArrayList<COM_ID_Usage> com_ID_usageList = new ArrayList<COM_ID_Usage>();
        
        int gateID;
        
        for (gateID = 1; gateID <= gateCount; gateID++) 
        {
            if (someCOMportIDsame(gateID, E_Board, com_ID_usageList) ||
                someCOMportIDsame(gateID, GateBar, com_ID_usageList)) 
            {
                return true;
            }
            
        }
        return false;    
    }

    private boolean someCOMportIDsame
        (int gateNo, DeviceType deviceType, ArrayList<COM_ID_Usage> com_ID_usageList) 
    {
        JComboBox cBox = (JComboBox)componentMap.get(deviceType.toString() + gateNo + "_comID_CBox");
        if (cBox == null)
            return false;
        
        String currCOM_ID = (String)cBox.getSelectedItem();
        
        for (COM_ID_Usage usage : com_ID_usageList) {
            if (usage.COM_ID.equals(currCOM_ID)) {
                String msg = 
                        usage.gateNo + "번 입구 " + usage.deviceType + "와" + System.lineSeparator() +
                        gateNo + "번 입구 " + deviceType + "가" + System.lineSeparator() +
                        "둘 다 COM"+ usage.COM_ID + "를 사용하고 있습니다." + System.lineSeparator() +
                        "그래도 저장하겠습니까?";
                int response = JOptionPane.showConfirmDialog(this, msg, "COM포트 중복사용", 
                        YES_NO_OPTION, ERROR_MESSAGE);
                if (response == YES_OPTION) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        com_ID_usageList.add(new COM_ID_Usage(gateNo, deviceType, currCOM_ID));
        return false;
    }

    /**
     * 모의 장치가 연결 유형이 TCP/IP 가 아닌 경우 경고 창을 띄우고 연결 유형을
     * 자동으로 TCP/IP 유형으로 설정함.
     * 
     * @param deviceType
     * @param gateNo 
     */
    private void enforceSimulatorTCP_IP(final DeviceType deviceType, final int gateNo) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                String prefix = deviceType.name() + gateNo;

                JComboBox typeCBox = (JComboBox)componentMap.get(prefix + "_TypeCBox");
                JComboBox connTypeCBox = (JComboBox)componentMap.get(prefix + "_connTypeCBox");

                if (typeCBox.getSelectedIndex() == GateBarType.Simulator.ordinal() 
                        && connTypeCBox.getSelectedIndex() != ConnectionType.TCP_IP.ordinal()) 
                {
                    JOptionPane.showMessageDialog(null, "모의 장치는 연결이 TCP-IP 라야 됨!");
                    connTypeCBox.setSelectedIndex(ConnectionType.TCP_IP.ordinal());
                }    
            }
        });        
    }

    private void tuneComponentSize() {
        
        /**
         * Standardize TextField heights
         */
        changeComponentHeight(lotNameTextField, TEXT_FIELD_HEIGHT);
        changeComponentHeight(TextFieldPicWidth, TEXT_FIELD_HEIGHT);
        changeComponentHeight(TextFieldPicHeight, TEXT_FIELD_HEIGHT);

        JComponent setCompo = null;
        for (int gateID = 1; gateID <= gateCount; gateID++) {
            setCompo = (JComponent)componentMap.get("TextFieldGateName" + gateID);
            changeComponentHeight(setCompo, TEXT_FIELD_HEIGHT);
            
            for (DeviceType type : DeviceType.values()) {
                setCompo = (JComponent)componentMap.get(type.toString() + gateID + "_IP_TextField");
                changeComponentHeight(setCompo, TEXT_FIELD_HEIGHT);

                setCompo = (JComponent)componentMap.get(type.toString() + gateID + "_Port_TextField");
                changeComponentHeight(setCompo, TEXT_FIELD_HEIGHT);
            }
        }
        
        /**
         * Standardize ComboBox heights
         */
        changeComponentHeight(PWStrengthChoiceComboBox, CBOX_HEIGHT);
        changeComponentHeight(OptnLoggingLevelComboBox, CBOX_HEIGHT);
        changeComponentHeight(DateChooserLangCBox, CBOX_HEIGHT);
        changeComponentHeight(MessageMaxLineComboBox, CBOX_HEIGHT);
        changeComponentHeight(GateCountComboBox, CBOX_HEIGHT);
        changeComponentHeight(PopSizeCBox, CBOX_HEIGHT);
        changeComponentHeight(ImageDurationCBox, CBOX_HEIGHT);
        
        changeComponentHeight(FlowingComboBox, CBOX_HEIGHT + 4);
        changeComponentHeight(BlinkingComboBox, CBOX_HEIGHT + 4);
        
        for (int gateID = 1; gateID <= gateCount; gateID++) {
            for (DeviceType type : DeviceType.values()) {
                setCompo = (JComponent)componentMap.get(type.toString() + gateID + "_TypeCBox");
                changeComponentHeight(setCompo, CBOX_HEIGHT + 4);

                setCompo = (JComponent)componentMap.get(type.toString() + gateID + "_connTypeCBox");
                changeComponentHeight(setCompo, CBOX_HEIGHT + 4);
            }
        } 
        
        /**
         * Standardize Button Sizes 
         */
        setComponentSize(EBoardSettingsButton, new Dimension(buttonWidthNorm, buttonHeightShort + 5));
        setComponentSize(SettingsCloseButton, new Dimension(buttonWidthNorm, buttonHeightNorm));
        setComponentSize(SettingsCancelButton, new Dimension(buttonWidthNorm, buttonHeightNorm));
        setComponentSize(SettingsCloseButton, new Dimension(buttonWidthNorm, buttonHeightNorm));
    }

    private void changeComponentHeight(JComponent compo, int height) {
        setComponentSize(compo, new Dimension(compo.getPreferredSize().width, height));
    }

    private void setButtonEnabled_If_DeviceTypeChanged(int gate, DeviceType devType) {
        JComboBox comboBx = ((JComboBox)getComponentByName(devType.toString() +gate + "_TypeCBox"));
        int selectedType = comboBx.getSelectedIndex();

        if (selectedType == deviceType[devType.ordinal()][gate]) {
            changedControls2.remove(comboBx);            
        }
        else {
            changedControls2.add(comboBx);            
        }
    }

    private void setButtonEnabled_If_ConnTypeChanged(int i, DeviceType deviceType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static class COM_ID_Usage {
        int gateNo;
        DeviceType deviceType;
        String COM_ID;

        private COM_ID_Usage(int gateNo, DeviceType deviceType, String COM_ID) {
            this.gateNo = gateNo;
            this.deviceType = deviceType;
            this.COM_ID = COM_ID;
        }
        
        boolean equals(COM_ID_Usage idUsage) {
            if (COM_ID == idUsage.COM_ID) 
                return true;
            else 
                return false;
        }
    }
}
