import sher.argon.Argon;
import sher.argon.ArgonAdapter;
import sher.argon.Layer;
import sher.argon.parallel.Parallel;
import sher.argon.util.Gradient;
import sher.argon.util.Inline;
import sher.argon.util.Util;
import sher.argon.util.noise.PerlinNoise;
import sher.argon.variable.DoubleVar;
import sher.argon.variable.IntVar;

import java.awt.Color;

public class Mountains {
    // Define the gradient
    // https://sherpavel.github.io/Gradient/?c=26262b&c=00bfff&c=e6e6e6
    int[][] gradient = {
            {38, 38, 43},
            {0, 191, 255},
            {230, 230, 230}
    };

    // Pseudo-random noise
    PerlinNoise noise = new PerlinNoise();

    Mountains() {
        int width = 1000;
        int height = 1000;
        // initiate Argon framework
        Argon argon = new Argon(width, height);
        // Create layer
        Layer layer = argon.createLayer();

        // Define UI variables
        DoubleVar noiseScale = new DoubleVar("Noise scale", 500, 50, 1000);
        IntVar lineCount = new IntVar("Line count", 20, 10, 200);
        DoubleVar maxAmp = new DoubleVar("Max amplitude(% of height)", 1, 0.01, 2);
        IntVar octaves = new IntVar("Noise octaves", 8, 2, 16);
        argon.addVariables(lineCount, noiseScale, maxAmp, octaves, octaves, octaves);

        // Cyclical time to loop the animation
        // Here - 10 seconds cycle
        int cycleLength = 10;

        // Define multithreaded layer drawing function
        // From 0 to canvas width, split over 8 threads
        Parallel parallel = new Parallel(x -> {
            // Cyclical time to loop the animation
            double time = Math.PI * (argon.frameCounter()) / (cycleLength * 30d);
            double sinTime = Math.sin(time)/5;
            double cosTime = Math.cos(time)/5;

            // Calculate noise
            // Most of it is determining offset and amplitude to make it look more random
            int[] lines = new int[lineCount.value()];
            for (int l = 0; l < lines.length; l++) {
                double p = (double) l / (lines.length - 1);
                double ampMlp = p*p*p*(p*(6*p-15)+10);
                double n = noise(
                        x / noiseScale.value() + l,
                        sinTime + l, cosTime + l, octaves.value())
                        * height * maxAmp.value() * (1 - ampMlp);
                lines[l] = (int) n;
            }

            // Optimized drawing
            // Skips invisible parts
            for (int l = 0; l < lines.length-1; l++) {
                double p = (l+1d) / lines.length;
                int color = Gradient.gradient(gradient, p);
                for (int y = lines[l+1]; y < lines[l]; y++)
                    layer.setRGB(x, height-1-y, color);
            }

        });

        // Define an inline draw function
        // Used here to cut down on lines of code
        Inline draw = () -> {
            // Paints the layer with background color
            // Util class contains many such useful functions
            layer.fill(new Color(Util.rgbArrayToInt(gradient[0])));
            // Start parallel draw function with 8 threads
            parallel.start(width, 8);
        };

        // Initial draw call
        draw.call();

        // Call draw function at target of 60 calls per second
        argon.addClock("Draw", () -> {
            draw.call();
        }, 60);

        // Add UI and variable listeners
        argon.addListener(new ArgonAdapter() {
            @Override
            public void onReset() {
                // On pressing reset button, the layer is reset
                draw.call();
            }

            @Override
            public void variableUpdated() {
                // When the clock is stopped, this ensures a canvas update on any variable change through UI
                draw.call();
            }
        });

        /*
        Sets up frame recorder with
        buffer of 120 frames, 1 queueing thread, 1 clearing thread
        and an output folder
         */
        argon.setupRecorder(120, 1, 1, "MountainsOutFolder");

        // .show or .showAndStart must be on the last line
        // Show the framework UI and locks out any changes
//        argon.show();
        // Show and programmatically click the UI START button
        argon.showAndStart();
    }

    // Helper noise function
    // Uses octave noise to make more "mountain-like" output
    double noise(double x, double y, double z, int octaves) {
        double value = 0d;
        double amp = 0.5d;
        for (int i = 0; i < octaves; i++) {
            value += amp * noise.evalScaled(x+1000, y, z);
            x *= 2d;
            amp *= 0.5d;
        }
        return value;
    }

    public static void main(String[] args) {
        new Mountains();
    }
}
