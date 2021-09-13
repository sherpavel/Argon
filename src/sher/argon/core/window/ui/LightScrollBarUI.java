package sher.argon.core.window.ui;

import sher.argon.core.window.WindowUtil;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class LightScrollBarUI extends BasicScrollBarUI {
    private final Dimension dimension = new Dimension();
    private final int d = 5;

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return new JButton() {
            @Override
            public Dimension getPreferredSize() {
                return dimension;
            }
        };
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return new JButton() {
            @Override
            public Dimension getPreferredSize() {
                return dimension;
            }
        };
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
        // Do nothing so nothing is drawn
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
        Graphics2D g2 = (Graphics2D) g.create();

        if (!c.isEnabled() || r.width > r.height) return;

        g2.setColor(WindowUtil.LIGHT_GREY_COLOR);
        g2.fillRect(r.x, r.y, r.width, r.height);
//        g2.setColor(WindowUtil.DARK_COLOR);
//        g2.fillRect(r.x, r.y + r.height/2 - d, r.width, d);
        g2.dispose();
    }

    @Override
    protected void setThumbBounds(int x, int y, int width, int height) {
        super.setThumbBounds(x, y, width, height);
        scrollbar.repaint();
    }
}
