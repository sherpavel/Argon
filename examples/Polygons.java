import sher.argon.Argon;
import sher.argon.ArgonAdapter;
import sher.argon.Layer;
import sher.argon.math.Vec2;
import sher.argon.util.*;
import sher.argon.variable.DoubleVar;
import sher.argon.variable.Flag;
import sher.argon.variable.IntVar;

import java.awt.Color;
import java.awt.BasicStroke;
import java.util.ArrayList;
import java.util.Arrays;

public class Polygons {
    // https://sherpavel.github.io/Gradient/?c=00ffff&c=cc00ff
    int[][] gradient = {
            {0, 255, 255},
            {204, 0, 255}
    };

    Polygons() {
        int width = 1000;
        int height = 1000;
        Argon argon = new Argon(width, height);
        Layer layer = argon.createLayer();

        IntVar verticesCount = new IntVar("Number of vertices", 100, 10, 1000);
        DoubleVar offsetMlp = new DoubleVar("Screen offset", 0.2, -0.2);
        Flag showOutline = new Flag("Show outline", false);
        IntVar outlineWidth = new IntVar("Outline width", 1, 1, 100);
        argon.addVariables(verticesCount, showOutline, outlineWidth, offsetMlp);

        Inline draw = () -> {
            Vec2[] vertices = new Vec2[verticesCount.value()];
            double offset = offsetMlp.value();
            for (int i = 0; i < vertices.length; i++) {
                vertices[i] = new Vec2(
                        (1 + 2 * offset) * width * Math.random(),
                        (1 + 2 * offset) * height * Math.random())
                        .sub(offset * width, offset * height);
            }

            ArrayList<Triangle> triangles = Delaunay.generate(new ArrayList<>(Arrays.asList(vertices)));
            layer.fill(Util.rgbToInt(38, 38, 43));
            for (Triangle triangle : triangles) {
                double p = triangle.getMidPoint().x / width;
                layer.g.setColor(new Color(Gradient.gradient(gradient, p)));
                triangle.drawPolygon(layer);
            }
            if (showOutline.isSet()) {
                layer.g.setStroke(new BasicStroke(outlineWidth.value()));
                layer.g.setColor(new Color(38, 38, 43));
                for (Triangle triangle : triangles) {
                    triangle.drawOutline(layer);
                }
            }
        };
        draw.call();


        argon.addListener(new ArgonAdapter() {
            @Override
            public void onReset() {
                draw.call();
            }

            @Override
            public void variableUpdated() {
                draw.call();
            }
        });
        argon.show();
    }

    public static void main(String[] args) {
        new Polygons();
    }
}
