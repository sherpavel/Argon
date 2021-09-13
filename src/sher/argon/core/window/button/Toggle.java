package sher.argon.core.window.button;

import sher.argon.core.window.WindowUtil;
import sher.argon.core.window.ui.LightButtonUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Toggle extends JButton {
    Icon offIcon, onIcon;
    String offText, onText;
    boolean isOn;
    ArrayList<ToggleListener> listeners;

    private void setup() {
        this.setFont(WindowUtil.getFont(WindowUtil.FontSize.SMALLER, WindowUtil.FontWeight.PLAIN));
        this.setBackground(WindowUtil.DARK_COLOR);
        this.setForeground(WindowUtil.BRIGHT_COLOR);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setUI(new LightButtonUI());
        this.setFocusPainted(false);
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(WindowUtil.DARK_GREY_COLOR);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(WindowUtil.DARK_COLOR);
            }
        });
    }
    public Toggle(Icon offIcon, String altOffText, Icon onIcon, String altOnText) {
        this.offIcon = offIcon;
        this.offText = altOffText;
        this.onIcon = onIcon;
        this.onText = altOnText;
        isOn = false;
        listeners = new ArrayList<>();
        setup();

        if (offIcon == null || onIcon == null) setText(altOffText);
        else setIcon(offIcon);

        addActionListener(e -> {
            if (isOn) switchOff(true);
            else switchOn(true);
        });
    }
    public Toggle(String offText, String onText) {
        this(null, offText, null, onText);
    }

    public boolean getState() {
        return isOn;
    }

    public void addListener(ToggleListener l) {
        listeners.add(l);
    }

    public void switchOn(boolean updateListeners) {
        if (isOn) return;
        isOn = true;
        if (offIcon == null || onIcon == null) setText(onText);
        else setIcon(onIcon);
        if (!updateListeners) return;
        for (ToggleListener listener : listeners)
            listener.toggled(isOn);
    }
    public void switchOff(boolean updateListeners) {
        if (!isOn) return;
        isOn = false;
        if (offIcon == null || onIcon == null) setText(offText);
        else setIcon(offIcon);
        if (!updateListeners) return;
        for (ToggleListener listener : listeners)
            listener.toggled(isOn);
    }
}
