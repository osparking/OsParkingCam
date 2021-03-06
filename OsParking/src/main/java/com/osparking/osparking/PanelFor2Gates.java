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

import static com.osparking.osparking.ControlGUI.show100percentSizeImageOfGate;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import com.osparking.global.names.CarAdmission;
import static com.osparking.global.Globals.*;
import static com.osparking.global.names.ControlEnums.TitleTypes.CAR_ARRIVALS_TITLE;
import static com.osparking.osparking.Common.RECENT_ROW_HEIGHT;
import static com.osparking.osparking.Common.fixPanelDimemsion;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 *
 * @author Open Source Parking Inc.
 */
public class PanelFor2Gates extends GatePanel {
    JList entryList[] = new JList[5];
    final DefaultListModel model_1 = new DefaultListModel();    
    final DefaultListModel model_2 = new DefaultListModel();  
    DefaultListModel models[] = new DefaultListModel[5];
    private JPanel[] Panel_Gates = new JPanel[5];
    private JLabel[] CarPicLabels = new JLabel[3];
    private Dimension prevSize = null;

    /**
     * Creates new form PanelFor2Gates
     */
    public PanelFor2Gates() {
        initComponents();
        
        changeFonts();
        
        CarPicLabels[1] = CarPicLabel1;
        CarPicLabels[2] = CarPicLabel2;
        entryList[1] = List_Gate1;
        entryList[2] = List_Gate2;
        models[1] = model_1;
        models[2] = model_2;
        Panel_Gates[1] = Panel_Gate1;
        Panel_Gates[2] = Panel_Gate2;
          
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                fixPanelDimemsion((GatePanel)evt.getSource(), getSize(), CarPicLabel1.getSize().height/2);
                
                if (Panel_Gate1.getSize().equals(prevSize)) {
                    return;
                } else {
                    prevSize = Panel_Gate1.getSize();
                    for (int gateNo = 1; gateNo <= 2; gateNo++) {
                        if (getEntryList(gateNo).getModel().getSize() > 0) 
                        {
                            if (getEntryList(gateNo).getSelectedIndex() == -1) {
                                getEntryList(gateNo).setSelectedIndex(0);
                            }
                            ControlGUI.showImage(gateNo);
                        }
                    }
                }
            }
        });
    }
    
    private BufferedImage gateImages[] = new BufferedImage[3];
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Panel_Gate1 = new javax.swing.JPanel();
        CarPicLabel1 = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        ScrollPane_Gate1 = new javax.swing.JScrollPane();
        List_Gate1 = new javax.swing.JList();
        Panel_Gate2 = new javax.swing.JPanel();
        CarPicLabel2 = new javax.swing.JLabel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        ScrollPane_Gate2 = new javax.swing.JScrollPane();
        List_Gate2 = new javax.swing.JList();
        RightMargin = new javax.swing.JPanel();
        MarginLabel = new javax.swing.JLabel();

        setBackground(MainBackground);
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        Panel_Gate1.setBackground(MainBackground);
        Panel_Gate1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Gate 1 Title", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font(font_Type, font_Style, font_Size)));
        Panel_Gate1.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        Panel_Gate1.setName("Panel_Gate1"); // NOI18N
        Panel_Gate1.setPreferredSize(new java.awt.Dimension(343, 450));
        Panel_Gate1.setLayout(new javax.swing.BoxLayout(Panel_Gate1, javax.swing.BoxLayout.Y_AXIS));

        CarPicLabel1.setBackground(MainBackground);
        CarPicLabel1.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        CarPicLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CarPicLabel1.setAlignmentX(0.5F);
        CarPicLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        CarPicLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        CarPicLabel1.setIconTextGap(0);
        CarPicLabel1.setInheritsPopupMenu(false);
        CarPicLabel1.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        CarPicLabel1.setMinimumSize(new java.awt.Dimension(200, 200));
        CarPicLabel1.setName(""); // NOI18N
        CarPicLabel1.setOpaque(true);
        CarPicLabel1.setPreferredSize(new java.awt.Dimension(200, 200));
        CarPicLabel1.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        CarPicLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CarPicLabel1MouseClicked(evt);
            }
        });
        Panel_Gate1.add(CarPicLabel1);
        Panel_Gate1.add(filler2);

        ScrollPane_Gate1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Recent Car Arrivals", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font(font_Type, font_Style, font_Size)));
        ScrollPane_Gate1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        ScrollPane_Gate1.setPreferredSize(new java.awt.Dimension(302, 155));

        List_Gate1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        List_Gate1.setFont(new java.awt.Font(font_Type, font_Style, font_Size_List));
        List_Gate1.setModel((DefaultListModel<CarAdmission>)admissionListModel[1]);
        List_Gate1.setFixedCellHeight(RECENT_ROW_HEIGHT);
        List_Gate1.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        List_Gate1.setMinimumSize(new java.awt.Dimension(45, 240));
        List_Gate1.setName(""); // NOI18N
        ScrollPane_Gate1.setViewportView(List_Gate1);

        Panel_Gate1.add(ScrollPane_Gate1);

        add(Panel_Gate1);

        Panel_Gate2.setBackground(MainBackground);
        Panel_Gate2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Gate 2 Title", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font(font_Type, font_Style, font_Size)));
        Panel_Gate2.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        Panel_Gate2.setName("Panel_Gate1"); // NOI18N
        Panel_Gate2.setPreferredSize(new java.awt.Dimension(343, 450));
        Panel_Gate2.setLayout(new javax.swing.BoxLayout(Panel_Gate2, javax.swing.BoxLayout.Y_AXIS));

        CarPicLabel2.setBackground(MainBackground);
        CarPicLabel2.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        CarPicLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CarPicLabel2.setAlignmentX(0.5F);
        CarPicLabel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        CarPicLabel2.setDoubleBuffered(true);
        CarPicLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        CarPicLabel2.setIconTextGap(0);
        CarPicLabel2.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        CarPicLabel2.setMinimumSize(new java.awt.Dimension(200, 100));
        CarPicLabel2.setName(""); // NOI18N
        CarPicLabel2.setOpaque(true);
        CarPicLabel2.setPreferredSize(new java.awt.Dimension(200, 200));
        CarPicLabel2.setVerifyInputWhenFocusTarget(false);
        CarPicLabel2.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        CarPicLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CarPicLabel2MouseClicked(evt);
            }
        });
        Panel_Gate2.add(CarPicLabel2);
        Panel_Gate2.add(filler4);

        ScrollPane_Gate2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Recent Car Arrivals", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font(font_Type, font_Style, font_Size)));
        ScrollPane_Gate2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        ScrollPane_Gate2.setPreferredSize(new java.awt.Dimension(302, 155));

        List_Gate2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        List_Gate2.setFont(new java.awt.Font(font_Type, font_Style, font_Size_List));
        List_Gate2.setModel((DefaultListModel<CarAdmission>)admissionListModel[2]);
        List_Gate2.setFixedCellHeight(RECENT_ROW_HEIGHT);
        List_Gate2.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        List_Gate2.setMinimumSize(new java.awt.Dimension(45, 240));
        List_Gate2.setName(""); // NOI18N
        ScrollPane_Gate2.setViewportView(List_Gate2);

        Panel_Gate2.add(ScrollPane_Gate2);

        add(Panel_Gate2);

        RightMargin.setBackground(MainBackground);
        RightMargin.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        RightMargin.setMinimumSize(new java.awt.Dimension(145, 111));
        RightMargin.setPreferredSize(new java.awt.Dimension(0, 0));
        RightMargin.setLayout(new javax.swing.BoxLayout(RightMargin, javax.swing.BoxLayout.Y_AXIS));

        MarginLabel.setBackground(new java.awt.Color(255, 153, 153));
        MarginLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        MarginLabel.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        MarginLabel.setMinimumSize(new java.awt.Dimension(100, 100));
        MarginLabel.setName(""); // NOI18N
        MarginLabel.setPreferredSize(new java.awt.Dimension(500, 250));
        MarginLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        RightMargin.add(MarginLabel);

        /*

        add(RightMargin);
        */
    }// </editor-fold>//GEN-END:initComponents

    private void CarPicLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CarPicLabel1MouseClicked
        show100percentSizeImageOfGate(1, getGateImages()[1]);
    }//GEN-LAST:event_CarPicLabel1MouseClicked

    private void CarPicLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CarPicLabel2MouseClicked
        show100percentSizeImageOfGate(2, getGateImages()[2]);
    }//GEN-LAST:event_CarPicLabel2MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel CarPicLabel1;
    public javax.swing.JLabel CarPicLabel2;
    public javax.swing.JList List_Gate1;
    public javax.swing.JList List_Gate2;
    public javax.swing.JLabel MarginLabel;
    public javax.swing.JPanel Panel_Gate1;
    public javax.swing.JPanel Panel_Gate2;
    public javax.swing.JPanel RightMargin;
    private javax.swing.JScrollPane ScrollPane_Gate1;
    private javax.swing.JScrollPane ScrollPane_Gate2;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler4;
    // End of variables declaration//GEN-END:variables

    @Override
    public void displaySizes() {
        StringBuffer sb = new StringBuffer();
        
        sb.append("Whole");         
        sb.append(System.lineSeparator());
        sb.append("Pan = ");        
        sb.append(getSizeString(this));
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        
        sb.append("Gate1");         
        sb.append(System.lineSeparator());
        sb.append("Pan = ");        
        sb.append(getSizeString(Panel_Gate1));
        sb.append(System.lineSeparator());
        sb.append("PIC = ");        
        sb.append(getSizeString(CarPicLabel1));
        sb.append(System.lineSeparator());
        sb.append("ScL = ");        
        sb.append(getSizeString(ScrollPane_Gate1));
        sb.append(System.lineSeparator());
        sb.append("LsT = ");        
        sb.append(getSizeString(List_Gate1));
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        
        sb.append("Gate2");         
        sb.append(System.lineSeparator());
        sb.append("Pan = ");        
        sb.append(getSizeString(Panel_Gate2));
        sb.append(System.lineSeparator());
        sb.append("PIC = ");        
        sb.append(getSizeString(CarPicLabel2));
        sb.append(System.lineSeparator());
        sb.append("ScL = ");        
        sb.append(getSizeString(ScrollPane_Gate2));
        sb.append(System.lineSeparator());        
        sb.append("LsT = ");        
        sb.append(getSizeString(List_Gate2));
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        
        sb.append("Margin");         
        sb.append(System.lineSeparator());
        sb.append("Pan = ");        
        sb.append(getSizeString(getMarginLabel()));
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());        
        sb.append("PIC = ");        
        sb.append(getSizeString(MarginLabel));
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());        
        System.out.println(sb.toString());
    }    

    @Override
    public JList getEntryList(int gateNo) {
        return entryList[gateNo];
    }

    @Override
    public DefaultListModel getDefaultListModel(int gateNo) {
        return models[gateNo];
    }

    @Override
    public JPanel getPanel_Gate(int gateNo) {
        return Panel_Gates[gateNo];
    }

    /**
     * @return the gateImages
     */
    public BufferedImage[] getGateImages() {
        return gateImages;
    }

    @Override
    public void setGateImage(byte gateNo, BufferedImage gateImage) {
        this.gateImages[gateNo] = gateImage;
    }

    @Override
    public JLabel[] getCarPicLabels() {
        return CarPicLabels;
    }

    /**
     * @return the RightMargin
     */
    @Override
    public javax.swing.JLabel getMarginLabel() {
        return MarginLabel;
    }

    private void changeFonts() {
        ScrollPane_Gate1.setBorder(javax.swing.BorderFactory.createTitledBorder(
                null, CAR_ARRIVALS_TITLE.getContent(), 
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
                javax.swing.border.TitledBorder.BELOW_TOP, 
                new java.awt.Font(font_Type, font_Style, font_Size)));
        List_Gate1.setFont(new java.awt.Font(font_Type, font_Style, font_Size_List));
        ScrollPane_Gate2.setBorder(javax.swing.BorderFactory.createTitledBorder(
                null, CAR_ARRIVALS_TITLE.getContent(), 
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
                javax.swing.border.TitledBorder.BELOW_TOP, 
                new java.awt.Font(font_Type, font_Style, font_Size)));
        List_Gate2.setFont(new java.awt.Font(font_Type, font_Style, font_Size_List));
    }
}
