package sher.argon.core.window.ui;

import sher.argon.core.window.WindowUtil;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public class LightButtonUI extends BasicButtonUI {
    @Override
    protected void paintButtonPressed(Graphics g, AbstractButton b) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(WindowUtil.MID_COLOR);
        g2.fillRect(0, 0, b.getWidth(), b.getHeight());
        g2.dispose();
    }

    @Override
    protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect, Rectangle textRect, Rectangle iconRect) {
        // Do not delete
        // Override with nothing so focused border is not drawn
    }
}
