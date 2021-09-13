package sher.argon.core.window.panels;

import sher.argon.core.window.WindowUtil;
import sher.argon.core.window.ui.LightSliderUI;
import sher.argon.math.Calc;
import sher.argon.variable.DoubleVar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DoubleVarPanel extends VarPanel<Double> {

    public DoubleVarPanel(DoubleVar var) {
        super(var);

        // Current value
        valueInputField = (DoubleValueInputField) WindowUtil.decoratedInputField(
                new DoubleValueInputField(
                        var.value(),
                        var::setVal));

        // Minimum slider value
        minValueInputField = (DoubleValueInputField) WindowUtil.decoratedInputField(
                new DoubleValueInputField(
                        var.getMin(),
                        newValue -> var.setVal(
                                Math.max(var.value(), newValue))));

        // Maximum slider value
        maxValueInputField = (DoubleValueInputField) WindowUtil.decoratedInputField(
                new DoubleValueInputField(
                        var.getMax(),
                        newValue -> var.setVal(
                                Math.min(var.value(), newValue))));

        // Slider
        slider = new JSlider(
                0, sliderResolution,
                (int) Calc.mapClamp(
                        var.value(),
                        minValueInputField.getValue(), maxValueInputField.getValue(),
                        0, sliderResolution));

        slider.addChangeListener(e -> {
            if (sliderMousePressed)
                var.setVal(Calc.map(
                        slider.getValue(),
                        slider.getMinimum(), slider.getMaximum(),
                        minValueInputField.getValue(), maxValueInputField.getValue()));
        });

        // Variable change listener
        var.addListener((oldValue, newValue) -> {
            valueInputField.setValue(newValue);
            if (newValue < minValueInputField.getValue())
                minValueInputField.setValue(newValue);
            if (newValue > maxValueInputField.getValue())
                maxValueInputField.setValue(newValue);
            slider.setValue((int) Calc.map(
                    newValue,
                    minValueInputField.getValue(), maxValueInputField.getValue(),
                    slider.getMinimum(), slider.getMaximum()));
        });

        super.addLayout();
    }
}
