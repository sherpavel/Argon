package sher.argon.core.window.ui;

import sher.argon.core.window.WindowUtil;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;

public class LightSliderUI extends BasicSliderUI {
    public LightSliderUI(JSlider b) {
        super(b);
    }

    @Override
    public void paintTrack(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(WindowUtil.MID_COLOR);
        g2.fillRect(trackRect.x, trackRect.y, trackRect.width, trackRect.height);
        g2.dispose();
    }

    @Override
    public void paintThumb(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(WindowUtil.BRIGHT_COLOR);
        g2.fillRect(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
        g2.dispose();
    }

    @Override
    public void paintFocus(Graphics g) {
        // Do not delete
        // Override with nothing so focused border is not drawn
    }
}
