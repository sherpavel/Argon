package sher.argon.core.window.panels;

import sher.argon.core.window.WindowUtil;
import sher.argon.core.window.ui.LightButtonUI;
import sher.argon.variable.Flag;

import javax.swing.*;
import java.awt.*;

public class TogglePanel extends JPanel {
    JLabel nameLabel;
    JButton toggle;

    public TogglePanel(Flag flag) {
        this.setBackground(WindowUtil.DARK_COLOR);
        this.setForeground(WindowUtil.BRIGHT_COLOR);
//        this.setBorder(WindowUtil.varPanelBorder());

        nameLabel = WindowUtil.decoratedLabel(new JLabel(flag.getName()));

        toggle = decoratedButton(new JButton());
        toggle.setUI(new LightButtonUI());
        toggle.setMinimumSize(new Dimension(100, 0));
        toggle.addActionListener(e -> {
            flag.toggle();
            decorate(flag);
        });

        decorate(flag);
        flag.addListener((oldValue, newValue) -> decorate(flag));

        // Adding to panel
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(toggle)
                .addComponent(nameLabel)
                .addGap(10, 10, 1000)
        );

        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(nameLabel)
                .addComponent(toggle)
        );
    }

    void decorate(Flag flag) {
        if (flag.isSet()) switchOn();
        else switchOff();
    }
    void switchOn() {
        toggle.setText("On");
        toggle.setBackground(WindowUtil.BRIGHT_COLOR);
        toggle.setForeground(WindowUtil.DARK_COLOR);
    }
    void switchOff() {
        toggle.setText("Off");
        toggle.setBackground(WindowUtil.DARK_COLOR);
        toggle.setForeground(WindowUtil.BRIGHT_COLOR);
    }

    JButton decoratedButton(JButton button) {
        button.setFont(WindowUtil.getFont(WindowUtil.FontSize.DEFAULT, WindowUtil.FontWeight.PLAIN));
        button.setBackground(WindowUtil.DARK_COLOR);
        button.setForeground(WindowUtil.BRIGHT_COLOR);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                WindowUtil.simpleBorder(3, WindowUtil.BRIGHT_COLOR),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        button.addPropertyChangeListener("enabled", evt -> {
            if ((boolean) evt.getNewValue()) {
                button.setBackground(WindowUtil.DARK_COLOR);
                button.setForeground(WindowUtil.BRIGHT_COLOR);
            } else {
                button.setBackground(WindowUtil.BRIGHT_COLOR);
                button.setForeground(WindowUtil.DARK_COLOR);
            }
        });
        return button;
    }
}
