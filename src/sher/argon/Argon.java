package sher.argon;

import sher.argon.core.chronos.ChronosListener;
import sher.argon.core.recorder.Recorder;
import sher.argon.core.renderer.Renderer;
import sher.argon.core.chronos.Chronos;
import sher.argon.core.window.Window;
import sher.argon.core.window.WindowListener;
import sher.argon.math.Calc;
import sher.argon.variable.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Argon is a framework that provides UI, loop management and interactivity with the code.
 * <br>
 * To learn more about the UI and Variables’ panel, visit the project’s <a href="https://github.com/sherpavel/Argon">GitHub page</a>, as it might be subject to change.
 * <br>
 * There is no one canvas to draw on, but rather drawing is done in {@link Layer layers}. Once created, they all have the same dimensions as when creating an Argon instance. Layers are transparent by default and are based on {@link BufferedImage} with exposed pixel raster. For more complex shapes {@link Graphics2D} can be used.
 * <br>
 * {@link Variable} is an intermediary between the UI and code. Interaction is done through the UI side panel, where the variables are added sequentially ({@link #addVariables(Variable[])}).
 * <br>
 * {@link Clock} is a use-created and Argon-managed loop function. Each can run at different updaterates (frequencies) from the viewport’s renderer. Clock manager prioritizes completing function calls over realtime rendering. Therefore, frequencies can be thought of as target updaterates and are relative to each other.
 * <br>
 * Argon has frame-by-frame recording functionality. To learn more, see {@link #setupRecorder(int, int, int, String)}.
 * @see Layer
 * @see Variable
 * @see Clock
 */
public class Argon {
    ArgonInterface argonInterface;
    ArrayList<ArgonListener> argonListeners;

    Renderer renderer;
    Window window;
    Chronos chronos;
    Recorder recorder;

    ArrayList<Variable> variables;

    int canvasWidth, canvasHeight;
    int RENDER_UPDATERATE, FRAME_COUNTER, RECORDING_FRAME_COUNTER;

    boolean RECORDER_FLAG;

    /**
     * Creates and initiates Argon.
     * Canvas dimensions are fixed and cannot be changed after initiation.
     * <br>
     * The window maximum dimension is the maximum allowed viewport's width and height.
     * @param canvasWidth canvas/layers width
     * @param canvasHeight canvas/layers height
     * @param windowMaxDim maximum allowed viewport's dimensions
     * @throws IllegalArgumentException if any of the dimensions are less then 1
     */
    public Argon(int canvasWidth, int canvasHeight, int windowMaxDim) {
        if (canvasWidth < 1 || canvasHeight < 1 || windowMaxDim < 1)
            throw new IllegalArgumentException("Illegal dimensions");

        argonInterface = new ArgonInterface(this);
        argonListeners = new ArrayList<>();

        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        renderer = new Renderer(canvasWidth, canvasHeight);

        int windowWidth = windowMaxDim;
        int windowHeight = windowMaxDim;
        if (canvasWidth != canvasHeight) {
            float aspectRatio = (float) canvasWidth / canvasHeight;
            if (aspectRatio > 1f)
                windowHeight = (int) (1000f / aspectRatio);
            else
                windowWidth = (int) (1000f * aspectRatio);
        }
        window = new Window(windowWidth, windowHeight, renderer.getRenderImage(), argonInterface);

        chronos = new Chronos();
        chronos.addChronosListener(argonInterface);

        variables = new ArrayList<>();

        RENDER_UPDATERATE = 0;
        FRAME_COUNTER = 0;
        RECORDING_FRAME_COUNTER = 0;
        RECORDER_FLAG = false;
    }
    /**
     * Creates and initiates Argon.
     * Canvas dimensions are fixed and cannot be changed after initiation.
     * <br>
     * The default maximum viewport size is 1000 pixels.
     * @param canvasWidth canvas/layers width
     * @param canvasHeight canvas/layers height
     * @throws IllegalArgumentException if any of the dimensions are less then 1
     */
    public Argon(int canvasWidth, int canvasHeight) {
        this(canvasWidth, canvasHeight, 1000);
    }

    /**
     * Adds listener for the UI and variables
     * @see ArgonListener
     * @see ArgonAdapter
     * @param listener listener
     */
    public void addListener(ArgonListener listener) {
        argonListeners.add(listener);
    }

    /**
     * Resets the global framecounter and calls {@link ArgonInterface#reset()} method.
     * Analogous to pressing the UI Reset button.
     */
    public void reset() {
        FRAME_COUNTER = 0;
        RECORDING_FRAME_COUNTER = 0;
        for (ArgonListener listener: argonListeners)
            listener.onReset();
        renderer.combineLayers();
        window.draw();
    }

    /**
     * Sets rendering updaterate.
     * This updaterate controls the displaying (rendering) and recording target framerates.
     * @param updaterate updaterate / FPS
     */
    public void setRendererUpdaterate(int updaterate) {
        if (updaterate < 1) throw new IllegalArgumentException("Updaterate must be more than 1");
        RENDER_UPDATERATE = updaterate;
    }

    /**
     * Adds {@link Clock}.
     * Clock is a user-defined function that runs at specific updaterate (frequency) per second.
     * This value is handled as a target and highly depends on the user's clock function.
     * Therefore, it is possible for the entire call loop to take longer then desired time.
     * @param clockName clock's name to be displayed in the UI panel
     * @param clock {@link Clock}
     * @param frequency frequency or updates per second of the function
     * @see Clock
     */
    public void addClock(String clockName, Clock clock, int frequency) {
        if (clock == null) throw new IllegalArgumentException("Clock is null");
        chronos.addClock(clockName, clock, frequency);
        if (RENDER_UPDATERATE == 0) RENDER_UPDATERATE = 60;
    }

    /**
     * Adds {@link Clock}.
     * Clock is a user-defined function that runs at specific updaterate (frequency) per second.
     * This value is handled as a target and highly depends on the user's clock function.
     * Therefore, it is possible for the entire call loop to take longer then desired time.
     * @param clock {@link Clock}
     * @param frequency frequency or updates per second of the function
     * @see Clock
     */
    public void addClock(Clock clock, int frequency) {
        addClock(null, clock, frequency);
    }

    /**
     * Creates and initializes {@link Layer layer}.
     * The layer's dimensions the equal Argon canvas's and are immutable.
     * @param renderLayer flag to either render or skip this layer.
     * @return {@link Layer}
     * @see Layer
     */
    public Layer createLayer(boolean renderLayer) {
        Layer layer = new Layer(
                renderer.getWidth(),
                renderer.getHeight(),
                renderLayer);
        renderer.addLayer(layer);
        return layer;
    }

    /**
     * Creates and initializes {@link Layer layer}.
     * The layer's dimensions the equal Argon canvas's and are immutable.
     * @return {@link Layer}
     * @see Layer
     */
    public Layer createLayer() {
        return createLayer(true);
    }

    /**
     * Removes layer from the renderer's queue.
     * @param layer layer to remove
     */
    public void removeLayer(Layer layer) {
        renderer.removeLayer(layer);
    }

    /**
     * Creates an empty image with alpha component.
     * @param width image width
     * @param height image height
     * @return {@link BufferedImage image}
     * @see BufferedImage
     */
    public BufferedImage createImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        g.setBackground(new Color(0, 0, 0, 0));
        g.dispose();
        return image;
    }

    /**
     * Creates an empty image with alpha component and dimensions equal to canvas's.
     * @return {@link BufferedImage image}
     * @see BufferedImage
     */
    public BufferedImage createImage() {
        return createImage(canvasWidth, canvasHeight);
    }

    /**
     * Initiates and shows the UI.
     * <br>
     * <b>This call must be on the last line</b>.
     * Any changes after this call might be detrimental to the framework's operation.
     */
    public void show() {
        if (RENDER_UPDATERATE != 0) {
            chronos.addClock("Render", () -> {
                renderer.combineLayers();
                window.draw();
                FRAME_COUNTER++;
            }, RENDER_UPDATERATE);
        }

        window.show(variables, chronos.getClocksCount(), recorder!=null);
        if (RENDER_UPDATERATE != 0)
            window.updateSceneTimes(chronos.getClockNames(), new int[chronos.getClocksCount()], new float[chronos.getClocksCount()]);
        renderer.combineLayers();
        window.draw();
    }

    /**
     * Initiates and shows the UI and starts the clocks.
     * <br>
     * <b>This call must be on the last line</b>.
     * Any changes after this call might be detrimental to the framework's operation.
     */
    public void showAndStart() {
        if (RENDER_UPDATERATE == 0) RENDER_UPDATERATE = 60;
        show();
        window.chronosToggle.switchOn(true);
    }

    /**
     * Adds variables to UI panel.
     * @param variables variables
     * @see Variable
     */
    public void addVariables(Variable... variables) {
        for (Variable variable : variables) {
            this.variables.add(variable);
            variable.addListener(argonInterface);
        }
    }

    /**
     * Starts all clocks.
     * Analogous to pressing UI Start button.
     */
    public void startClocks() {
        window.chronosToggle.switchOff(false);
        chronos.start();
    }

    /**
     * Stops all clocks.
     * Analogous to pressing UI Pause button.
     */
    public void stopClocks() {
        window.chronosToggle.switchOff(false);
        chronos.stop();
    }

    /**
     * Sets up frame-by-frame recorder and output folder.
     * Recorder operates sequentially, queueing frames as they are rendered on the viewport.
     * The output is a sequence of {@code PNG} images.
     * <br>
     * The buffer stores all the frames before they are saved to the folder.
     * If the buffer is exceeded, the clocks are paused and de-queueing process starts. It is recommended to set a higher amount of clearing threads if your storage throughput allows it.
     * <br>
     * The writing threads save frames one by one to the output folder.
     * Each frame's filename is their framecounter index, i.e. when they were displayed chronologically.
     * <br>
     * If the folder does not exists, it will be created.
     * @param bufferSize Buffer size for queueing frames (JVM Heap Memory settings must be changed to utilize a larger buffer)
     * @param writingThreads Amount of writing threads
     * @param clearingThreads Amount of buffer-clearing threads
     * @param outFolder Frames output folder path
     * @throws IllegalArgumentException if output folder is null
     */
    public void setupRecorder(int bufferSize, int writingThreads, int clearingThreads, String outFolder) {
        if (outFolder == null) throw new IllegalArgumentException("Null output folder");
        recorder = new Recorder(bufferSize, writingThreads, clearingThreads, outFolder);
    }

    void startRecording() {
        chronos.stop();
        recorder.start();
        RECORDING_FRAME_COUNTER = 0;
        chronos.addClock("Recording", () -> {
            // Queue frame
            try {
                BufferedImage copy = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = copy.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
                g.setBackground(new Color(0, 0, 0, 0));
                g.drawImage(renderer.getRenderImage(), 0, 0, null);
                g.dispose();
                recorder.queue(copy, FRAME_COUNTER);
            } catch (InterruptedException e) {
                System.err.println("Queue error at frame " + FRAME_COUNTER);
                System.exit(1);
            }

            // De-queue buffer
            if (recorder.getBufferUsage() >= 0.99f) {
                int bufferSize = recorder.getBufferSize();
                int threads = recorder.getThreadsCount();
                String outFolder = recorder.getOutFolder();

                System.out.println("Clearing buffer...");
                recorder.waitForBuffer(true);
                System.out.println("Resuming");

                recorder = new Recorder(bufferSize, threads, recorder.getClearingThreads(), outFolder);
                recorder.start();
            }

            RECORDING_FRAME_COUNTER++;
        }, RENDER_UPDATERATE);

        chronos.start();
    }

    /**
     * Stops the recorder and clocks.
     * The method waits until the buffer is cleared.
     * Analogous to pressing UI Stop recoding button.
     */
    void stopRecording() {
        window.stopRecordingDecoration();
        if (recorder == null) return;
        if (!recorder.isRecording()) return;
        chronos.stop();
        RECORDING_FRAME_COUNTER = 0;
        RECORDER_FLAG = true;
        System.out.println("Saving buffered frames...");
        recorder.waitForBuffer(true);
        System.out.println("Complete");
        recorder.stop();
        recorder.reset();
        chronos.stop();
        chronos.removeClock(chronos.getClocksCount()-1);
    }

    /**
     * Returns global frame counter.
     * The value depends on rendering framerate and is independent from clocks' frequencies.
     * @return number of frames since start.
     * @see #setRendererUpdaterate(int)
     */
    public int frameCounter() {
        return FRAME_COUNTER;
    }

    /**
     * Returns recoding frame counter.
     * When recording starts, the value is set to 0 and counties number of frames recorded.
     * The value depends on rendering framerate and is independent from clocks' frequencies.
     * @return number of frames since starting recording.
     * @see #setRendererUpdaterate(int)
     */
    public int recodingFrameCounter() {
        return RECORDING_FRAME_COUNTER;
    }
}

/**
 * UI/Core communication interface.
 */
class ArgonInterface implements WindowListener, ChronosListener, VariableListener {
    Argon argon;

    ArgonInterface(Argon argon) {
        this.argon = argon;
    }

    @Override
    public boolean startChronos() {
        return argon.chronos.start();
    }

    @Override
    public void stopChronos() {
        argon.chronos.stop();
    }

    @Override
    public void reset() {
        argon.chronos.stop();
        argon.reset();
    }

    @Override
    public void capture() {
        argon.chronos.stop();
        try {
            ImageIO.write(argon.renderer.getRenderImage(), "png", new File(System.currentTimeMillis() + ".png"));
        } catch (IOException e) {
            System.err.println("Capture error");
        }
        argon.chronos.start();
    }

    @Override
    public void startRecording() {
        argon.startRecording();
    }
    @Override
    public void stopRecording() {
        argon.stopRecording();
    }

    @Override
    public void mouseClick(int x, int y) {
        for (ArgonListener listener : argon.argonListeners)
            listener.windowMouseClick(
                    (int) Calc.map(x, 0, argon.window.getWidth(), 0, argon.canvasWidth),
                    (int) Calc.map(y, 0, argon.window.getHeight(), 0, argon.canvasHeight));
        if (!argon.chronos.isRunning())
            argon.renderer.combineLayers();
            argon.window.draw();
    }

    @Override
    public void mouseMoved(int x, int y) {
        for (ArgonListener listener : argon.argonListeners)
            listener.windowMouseMoved(
                    (int) Calc.map(x, 0, argon.window.getWidth(), 0, argon.canvasWidth),
                    (int) Calc.map(y, 0, argon.window.getHeight(), 0, argon.canvasHeight));
        if (!argon.chronos.isRunning()) {
            argon.renderer.combineLayers();
            argon.window.draw();
        }
    }

    @Override
    public void secondsTimer(String[] clockNames, int[] callCounters, float[] avgExecTimes) {
        argon.window.updateSceneTimes(clockNames, callCounters, avgExecTimes);
        if (argon.recorder != null) {
            if (argon.recorder.isRecording()) {
                System.out.println(String.format("%.1f", argon.recorder.getBufferUsage()*100) + "% buffer usage");
            }
        }
    }

    @Override
    public void valueChanged(Object oldValue, Object currentValue) {
        for (ArgonListener listener : argon.argonListeners)
            listener.variableUpdated();
        if (!argon.chronos.isRunning()) {
            argon.renderer.combineLayers();
            argon.window.draw();
        }
    }
}
