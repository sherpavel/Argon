package sher.argon.core.window.panels;

import java.awt.Color;

public class DoubleValueInputField extends ValueInputField<Double> {
    public DoubleValueInputField(double v, ValueInputListener<Double> valueListener, Color valid, Color invalid) {
        super(v, valueListener, valid, invalid);
    }

    public DoubleValueInputField(double value, ValueInputListener<Double> valueListener) {
        super(value, valueListener);
    }

    @Override
    public Double toValue(String text) {
        double value = Double.NaN;
        try {
            value = Double.parseDouble(text);
        } catch (NumberFormatException ignore) {}
        return value;
    }
}
