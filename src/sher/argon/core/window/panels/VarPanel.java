package sher.argon.core.window.panels;

import sher.argon.core.window.WindowUtil;
import sher.argon.core.window.ui.LightSliderUI;
import sher.argon.variable.Variable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class VarPanel<T> extends JPanel {
    JLabel nameLabel;
    ValueInputField<T> valueInputField, minValueInputField, maxValueInputField;
    JSlider slider;
    boolean sliderMousePressed;

    public static int sliderResolution = 200;

    public VarPanel(Variable var) {
        this.setBackground(WindowUtil.DARK_COLOR);
        this.setForeground(WindowUtil.BRIGHT_COLOR);
//        this.setBorder(WindowUtil.varPanelBorder());
        nameLabel = WindowUtil.decoratedLabel(new JLabel(var.getName()), WindowUtil.FontSize.LARGER);
        sliderMousePressed = false;
    }

    void addLayout() {
        JLabel valueLabel = WindowUtil.decoratedLabel(new JLabel("Value:"), WindowUtil.FontSize.SMALLER);
        JLabel minLabel = WindowUtil.decoratedLabel(new JLabel("Slider min:"), WindowUtil.FontSize.SMALLER);
        JLabel maxLabel = WindowUtil.decoratedLabel(new JLabel("Slider max:"), WindowUtil.FontSize.SMALLER);

        slider.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                sliderMousePressed = true;
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                sliderMousePressed = false;
            }
        });
        slider.setBorder(null);
        slider.setBackground(WindowUtil.MID_COLOR);
        slider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        slider.setUI(new LightSliderUI(slider));

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(nameLabel)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(valueLabel)
                                .addComponent(minLabel)
                                .addComponent(maxLabel))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(valueInputField)
                                .addComponent(minValueInputField)
                                .addComponent(maxValueInputField)))
                .addComponent(slider)
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(nameLabel)
                .addGap(10)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(valueLabel)
                                        .addComponent(valueInputField))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(minLabel)
                                        .addComponent(minValueInputField))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(maxLabel)
                                        .addComponent(maxValueInputField))))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(slider))
        );
    }
}
