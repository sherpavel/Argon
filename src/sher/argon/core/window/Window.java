package sher.argon.core.window;

import sher.argon.core.window.button.Toggle;
import sher.argon.core.window.ui.LightScrollBarUI;
import sher.argon.variable.Variable;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Window {
    int width, height;
    JFrame frame;
    JPanel renderPanel, sidePanel;
    BufferedImage imageReference;
    WindowListener windowListener;

    public Toggle chronosToggle, recordingToggle;
    JButton resetButton, captureButton;
    JLabel[][] clocksTable;

    public Window(int width, int height, BufferedImage imageReference, WindowListener windowListener) {
        this.width = width;
        this.height = height;
        this.windowListener = windowListener;

        this.imageReference = imageReference;

        renderPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imageReference, 0, 0, width, height, null);
            }
        };
        renderPanel.setPreferredSize(new Dimension(width, height));
        renderPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                switch (e.getModifiersEx()) {
                    case MouseEvent.BUTTON1_DOWN_MASK -> windowListener.mouseClick(e.getX(), e.getY());
//                    case MouseEvent.BUTTON2_DOWN_MASK ->
                }
            }
        });
        renderPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                windowListener.mouseMoved(e.getX(), e.getY());
            }
        });
        renderPanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

        // Side panel
        sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(400, 0));
        sidePanel.setBackground(WindowUtil.DARK_COLOR);
//        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setLayout(new BorderLayout());
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    public void show(ArrayList<Variable> vars, int numClocks, boolean recorder) {
        sidePanel.add(createActionPanel(recorder, numClocks), BorderLayout.PAGE_START);

        // Variables
        if (vars.size() != 0)
            sidePanel.add(createVarPane(vars), BorderLayout.CENTER);

        // Filler panel
        JPanel filler = new JPanel();
        filler.setBackground(WindowUtil.DARK_COLOR);
//        sidePanel.add(filler, BorderLayout.PAGE_END);

        // Frame
        frame = new JFrame("Argon");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            frame.setIconImages(WindowUtil.loadArgonIcons());
        } catch (Exception ignore) {}

        frame.add(renderPanel, BorderLayout.CENTER);
        frame.add(sidePanel, BorderLayout.EAST);
        frame.pack();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(
                (dim.width - frame.getWidth()) / 2,
                (dim.height - frame.getHeight()) / 2);

        frame.setVisible(true);
    }

    public void draw() {
        renderPanel.repaint();
    }

    // Actions panel
    private JPanel createActionPanel(boolean recorder, int numClocks) {
        chronosToggle = new Toggle(
                WindowUtil.loadIcon("start.png"),
                "Start",
                WindowUtil.loadIcon("pause.png"),
                "Paused");
        chronosToggle.addListener(isOn -> {
            if (isOn) {
                windowListener.startChronos();
            } else {
                windowListener.stopChronos();
            }
//            if (chronosToggle.isEnabled()) {
//                if (windowListener.startChronos()) {
//                    // TODO
//                }
//            }
        });

        resetButton = WindowUtil.decoratedButton(
                WindowUtil.loadIcon("reset.png"),
                "Reset");
        resetButton.addActionListener(e -> {
            chronosToggle.switchOff(true);
            windowListener.reset();
        });

        captureButton = WindowUtil.decoratedButton(
                WindowUtil.loadIcon("capture.png"),
                "Capture");
        captureButton.addActionListener(e -> windowListener.capture());

        recordingToggle = new Toggle(
                WindowUtil.loadIcon("record_start.png"),
                "Start recording",
                WindowUtil.loadIcon("record_stop.png"),
                "Stop recording");
        recordingToggle.addListener(isOn -> {
            if (isOn) {
                chronosToggle.switchOff(false);
                chronosToggle.setEnabled(false);
                resetButton.setEnabled(false);
                captureButton.setEnabled(false);
                windowListener.startRecording();
            } else {
                recordingToggle.setEnabled(false);
                windowListener.stopRecording();
                recordingToggle.setEnabled(true);
                stopRecordingDecoration();
            }
        });

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 4));
        buttonsPanel.setBackground(WindowUtil.DARK_COLOR);
        buttonsPanel.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5, 5, 5, 5),
                    BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)
                )
//                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        );

        if (numClocks > 0)
            buttonsPanel.add(chronosToggle);
        buttonsPanel.add(resetButton);
        buttonsPanel.add(captureButton);
        if (numClocks > 0 && recorder)
            buttonsPanel.add(recordingToggle);

        if (numClocks == 0)
            return buttonsPanel;

        clocksTable = new JLabel[numClocks + 1][3];
        clocksTable[0][0] = WindowUtil.decoratedLabel(new JLabel("Clock name"), WindowUtil.FontSize.SMALLER, WindowUtil.FontWeight.BOLD);
        clocksTable[0][1] = WindowUtil.decoratedLabel(new JLabel("Calls"), WindowUtil.FontSize.SMALLER, WindowUtil.FontWeight.BOLD);
        clocksTable[0][2] = WindowUtil.decoratedLabel(new JLabel("Time/call (ms)"), WindowUtil.FontSize.SMALLER, WindowUtil.FontWeight.BOLD);
        for (int i = 1; i <= numClocks; i++) {
            clocksTable[i][0] = WindowUtil.decoratedLabel(new JLabel("null"), WindowUtil.FontSize.SMALLER);
            clocksTable[i][1] = WindowUtil.decoratedLabel(new JLabel("0"), WindowUtil.FontSize.SMALLER);
            clocksTable[i][2] = WindowUtil.decoratedLabel(new JLabel("0.0"), WindowUtil.FontSize.SMALLER);
        }
        JPanel clocksPanel = new JPanel(new GridBagLayout());
        clocksPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(5, 5, 5, 5),
                        BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        clocksPanel.setBackground(WindowUtil.DARK_COLOR);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        JLabel label;
        for (int i = 0; i < clocksTable.length; i++) {
            label = clocksTable[i][0];
            label.setHorizontalAlignment((i==0)?JLabel.CENTER : JLabel.LEADING);
            c.gridx = 0;
            c.gridy = i;
            c.weightx = 1;
            clocksPanel.add(label, c);

            c.insets = new Insets(0, 5, 0, 5);

            label = clocksTable[i][1];
            label.setHorizontalAlignment(JLabel.CENTER);
            c.gridx = 1;
            c.gridy = i;
            c.weightx = 0.3;
            clocksPanel.add(label, c);

            c.insets = new Insets(0, 5, 0, 5);

            label = clocksTable[i][2];
            label.setHorizontalAlignment(JLabel.TRAILING);
            c.gridx = 2;
            c.gridy = i;
            c.weightx = 0;
            clocksPanel.add(label, c);
        }

        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.add(buttonsPanel, BorderLayout.PAGE_START);
        actionPanel.add(clocksPanel, BorderLayout.PAGE_END);

        return actionPanel;
    }

    // Var panel
    public JScrollPane createVarPane(ArrayList<Variable> vars) {
        JPanel varPanel = new JPanel();
        varPanel.setLayout(new BoxLayout(varPanel, BoxLayout.Y_AXIS));
        for (Variable var : vars)
            varPanel.add(var.createPanel());
        varPanel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(varPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUI(new LightScrollBarUI());
        scrollPane.getVerticalScrollBar().setBackground(WindowUtil.DARK_COLOR);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);

        scrollPane.setBorder(null);
//        scrollPane.setBorder(
////                BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
////                BorderFactory.createEmptyBorder(5, 5, 5, 5),
//                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)
//        );
        scrollPane.setBackground(WindowUtil.DARK_COLOR);
        scrollPane.getViewport().setBackground(WindowUtil.DARK_COLOR);

        if (scrollPane.getSize().height > scrollPane.getPreferredSize().height)
            scrollPane.setSize(new Dimension(
                    scrollPane.getSize().width,
                    scrollPane.getPreferredSize().height));
//        scrollPane.addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentResized(ComponentEvent e) {
//                if (scrollPane.getSize().height > scrollPane.getPreferredSize().height)
//                    scrollPane.setSize(new Dimension(
//                            scrollPane.getSize().width,
//                            scrollPane.getPreferredSize().height));
//            }
//        });

        return scrollPane;
    }

    public void updateSceneTimes(String[] clockNames, int[] callCounters, float[] avgExecTimes) {
        for (int i = 0; i < clocksTable.length-1; i++) {
            clocksTable[i+1][0].setText(clockNames[i]);
            clocksTable[i+1][1].setText(String.valueOf(callCounters[i]));
            clocksTable[i+1][2].setText(String.format("%.1f", avgExecTimes[i]));
        }
    }

    public void stopRecordingDecoration() {
        chronosToggle.setEnabled(true);
        resetButton.setEnabled(true);
        captureButton.setEnabled(true);
        recordingToggle.switchOff(false);
    }
}
