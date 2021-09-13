package sher.argon.core.window;

import sher.argon.core.window.panels.ValueInputField;
import sher.argon.core.window.ui.LightButtonUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class WindowUtil {
    public static final Color DARK_COLOR = new Color(28, 28, 33);
    public static final Color DARK_GREY_COLOR = new Color(60, 60, 64);
    public static final Color MID_COLOR = new Color(128, 128, 128);
    public static final Color LIGHT_GREY_COLOR = new Color(192, 192, 192);
    public static final Color BRIGHT_COLOR = new Color(230, 230, 230);
    public static final Color ERROR = Color.RED;
    public static String FONT_NAME;

    static {
        UIManager.put("ToolTip.font", new FontUIResource(Font.MONOSPACED, Font.BOLD, 14));
        UIManager.put("ToolTip.background", Color.WHITE);
        UIManager.put("ToolTip.foreground", Color.BLACK);

        FONT_NAME = "Ubuntu";
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, WindowUtil.class.getResourceAsStream("resources/Ubuntu-R.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, WindowUtil.class.getResourceAsStream("resources/Ubuntu-B.ttf")));
        } catch (Exception e) {
            FONT_NAME = Font.MONOSPACED;
        }
    }

    public enum FontSize {
        SMALLER, DEFAULT, LARGER
    }
    public enum  FontWeight {
        PLAIN, BOLD
    }
    public static Font getFont(FontSize fontSize, FontWeight fontWeight) {
        int size = switch (fontSize) {
            case SMALLER -> 18;
            case DEFAULT -> 22;
            case LARGER -> 26;
        };
        int weight = switch (fontWeight) {
            case PLAIN -> Font.PLAIN;
            case BOLD -> Font.BOLD;
        };

        return new Font(FONT_NAME, weight, size);
    }

    public static JLabel decoratedLabel(JLabel label, FontSize fontSize, FontWeight fontWeight, Color background, Color foreground) {
        label.setFont(getFont(fontSize, fontWeight));
        label.setBackground(background);
        label.setForeground(foreground);
        return label;
    }
    public static JLabel decoratedLabel(JLabel label, FontSize fontSize, FontWeight fontWeight) {
        return decoratedLabel(label, fontSize, fontWeight, DARK_COLOR, BRIGHT_COLOR);
    }
    public static JLabel decoratedLabel(JLabel label, FontSize fontSize) {
        return decoratedLabel(label, fontSize, FontWeight.PLAIN, DARK_COLOR, BRIGHT_COLOR);
    }
    public static JLabel decoratedLabel(JLabel label) {
        return decoratedLabel(label, FontSize.DEFAULT, FontWeight.PLAIN, DARK_COLOR, BRIGHT_COLOR);
    }

    public static ValueInputField decoratedInputField(ValueInputField inputField, String tip, Color background, Color foreground) {
        inputField.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
        inputField.setBackground(background);
        inputField.setForeground(foreground);
        inputField.setBorder(BorderFactory.createCompoundBorder(
                simpleBorder(1, DARK_COLOR),
                BorderFactory.createEmptyBorder(3, 3, 3, 3)
        ));
        inputField.setCaretColor(foreground);
        inputField.setToolTipText(tip);
        return inputField;
    }
    public static ValueInputField decoratedInputField(ValueInputField inputField) {
        return decoratedInputField(inputField, null, DARK_COLOR, BRIGHT_COLOR);
    }

    public static JButton decoratedButton(Icon icon, String altText) {
        JButton button = new JButton();
        button.setUI(new LightButtonUI());
        button.setFont(getFont(FontSize.SMALLER, FontWeight.PLAIN));
        button.setBackground(DARK_COLOR);
        button.setForeground(BRIGHT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(WindowUtil.DARK_GREY_COLOR);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(WindowUtil.DARK_COLOR);
            }
        });

        if (icon != null)
            button.setIcon(icon);
        else
            button.setText(altText);

//        button.addPropertyChangeListener("enabled", evt -> {
//            if ((boolean) evt.getNewValue()) {
//                button.setBackground(background);
//                button.setForeground(foreground);
//            } else {
//                button.setBackground(foreground);
//                button.setForeground(background);
//            }
//        });
        return button;
    }

    static Icon loadIcon(String filename, int width, int height) {
        Icon icon = null;
        try {
            Image img = ImageIO.read(WindowUtil.class.getResource("resources/"+filename));
            img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
        } catch (Exception ignore) {}
        return icon;
    }
    public static Icon loadIcon(String filepath) {
        return loadIcon(filepath, 60, 60);
    }

    public static ArrayList<Image> loadArgonIcons() throws NullPointerException {
        ArrayList<Image> icons = new ArrayList<>();
        icons.add(new ImageIcon(WindowUtil.class.getResource("resources/icon128.png")).getImage());
        icons.add(new ImageIcon(WindowUtil.class.getResource("resources/icon64.png")).getImage());
        icons.add(new ImageIcon(WindowUtil.class.getResource("resources/icon32.png")).getImage());
        icons.add(new ImageIcon(WindowUtil.class.getResource("resources/icon16.png")).getImage());
//        icons.add(new ImageIcon(WindowUtil.class.getResource("resources/icon.png")).getImage());
        return icons;
    }

    public static Border simpleBorder(int width, Color color) {
        return BorderFactory.createMatteBorder(width, width, width, width, color);
    }
    public static Border simpleBorder(Color color) {
        return simpleBorder(3, color);
    }

    public static Border varPanelBorder() {
        return BorderFactory.createCompoundBorder(
                simpleBorder(DARK_GREY_COLOR),
                BorderFactory.createLoweredBevelBorder());
    }
}
