package sher.argon.core.window.panels;

import sher.argon.core.window.WindowUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public abstract class ValueInputField<T> extends JTextField {
    T value;
    boolean validState;
    Color valid, invalid;

    public ValueInputField(T v, ValueInputListener<T> valueListener, Color valid, Color invalid) {
        this.value = v;
        setText(String.valueOf(v));
        this.valid = valid;
        this.invalid = invalid;
        validState = true;
        this.setColumns(10);

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                setBackground(WindowUtil.DARK_GREY_COLOR);
            }

            @Override
            public void focusLost(FocusEvent e) {
                setBackground(WindowUtil.DARK_COLOR);
                if (isValid(getText())) {
                    T value = toValue(getText());
                    setValue(value);
                    valueListener.valueChanged(value);
                } else {
                    setText(String.valueOf(value));
                    decorateValid();
                }
            }
        });

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!String.valueOf(e.getKeyChar()).matches("[\\-0-9.]"))
                    e.consume();
            }
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (isValid(getText())) {
                        T value = toValue(getText());
                        setValue(value);
                        valueListener.valueChanged(value);
                    }
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });

        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validateInput();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                validateInput();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                validateInput();
            }
        });
    }
    public ValueInputField(T value, ValueInputListener<T> valueListener) {
        this(value, valueListener, WindowUtil.DARK_COLOR, WindowUtil.ERROR);
    }

    // Getters / Setters
    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        setText(String.valueOf(value));
    }

    // Validating and error checking
    private void validateInput() {
        if (isValid(getText())) decorateValid();
        else decorateInvalid();
    }

    private void decorateValid() {
        if (validState) return;
        validState = true;
        setBorder(BorderFactory.createCompoundBorder(
                WindowUtil.simpleBorder(1, valid),
                BorderFactory.createEmptyBorder(3, 3, 3, 3)
        ));
    }
    private void decorateInvalid() {
        if (!validState) return;
        validState = false;
        setBorder(BorderFactory.createCompoundBorder(
                WindowUtil.simpleBorder(1, invalid),
                BorderFactory.createEmptyBorder(3, 3, 3, 3)
        ));
    }

    private static boolean isValid(String input) {
        return input.matches("^[+-]?([0-9]*[.])?[0-9]+$");
    }

    abstract T toValue(String text);
}
