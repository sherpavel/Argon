package sher.argon.core.window.panels;

import java.awt.Color;

public class IntValueInputField extends ValueInputField<Integer> {
    public IntValueInputField(int v, ValueInputListener<Integer> valueListener, Color valid, Color invalid) {
        super(v, valueListener, valid, invalid);
    }

    public IntValueInputField(int value, ValueInputListener<Integer> valueListener) {
        super(value, valueListener);
    }

    @Override
    public Integer toValue(String text) {
        int value = 0;
        try {
            value = Integer.parseInt(text);
        } catch (NumberFormatException ignore) {}
        return value;
    }
}
