package sher.argon.core.window.panels;

import sher.argon.core.window.WindowUtil;
import sher.argon.core.window.ui.LightSliderUI;
import sher.argon.variable.IntVar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class IntVarPanel extends VarPanel<Integer> {

    public IntVarPanel(IntVar var) {
        super(var);

        // Current value
        valueInputField = (IntValueInputField) WindowUtil.decoratedInputField(
                new IntValueInputField(
                        var.value(),
                        var::setVal));

        // Minimum slider value
        minValueInputField = (IntValueInputField) WindowUtil.decoratedInputField(
                new IntValueInputField(
                        var.getMin(),
                        newValue -> {
                            var.setVal(Math.max(var.value(), newValue));
                            slider.setMinimum(newValue);
                        }));

        // Maximum slider value
        maxValueInputField = (IntValueInputField) WindowUtil.decoratedInputField(
                new IntValueInputField(
                        var.getMax(),
                        newValue -> {
                            var.setVal(Math.min(var.value(), newValue));
                            slider.setMaximum(newValue);
                        }));

        // Slider
        slider = new JSlider(
                minValueInputField.getValue(), maxValueInputField.getValue(),
                var.value());

        slider.addChangeListener(e -> {
            if (sliderMousePressed)
                var.setVal(slider.getValue());
        });

        // Variable change listener
        var.addListener((oldValue, newValue) -> {
            valueInputField.setValue(newValue);
            if (newValue < minValueInputField.getValue()) {
                minValueInputField.setValue(newValue);
                slider.setMinimum(newValue);
            }
            if (newValue > maxValueInputField.getValue()) {
                maxValueInputField.setValue(newValue);
                slider.setMaximum(newValue);
            }
            slider.setValue(newValue);
        });

        super.addLayout();
    }
}
