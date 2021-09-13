package sher.argon.core.recorder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Recorder {
    int bufferSize;
    String outFolder;
    BlockingQueue<IndexedImage> imageQueue;
    Thread[] bufferingThreads;
    int clearingThreads;
    Runnable bufferRunnable;

    boolean INTERRUPT = true;

    public Recorder(int bufferSize, int writingThreads, int clearingThreads, String outFolder) {
        this.bufferSize = bufferSize;
        this.outFolder = outFolder;
        imageQueue = new ArrayBlockingQueue<>(bufferSize);
        bufferingThreads = new Thread[writingThreads];
        this.clearingThreads = clearingThreads;

        bufferRunnable = () -> {
            IndexedImage indexedImage;
            try {
                while (!INTERRUPT) {
                    if ((indexedImage = imageQueue.poll()) != null)
                        ImageIO.write(indexedImage.image, "png", new File(outFolder + File.separator + indexedImage.index + ".png"));
                }
            } catch(IOException e) {
                System.err.println("Writing error");
                System.exit(1);
            }
            Thread.currentThread().interrupt();
        };

        for (int t = 0; t < bufferingThreads.length; t++)
            bufferingThreads[t] = new Thread(bufferRunnable);
    }

    public void start() {
        if (!INTERRUPT) return;
        INTERRUPT = false;
        new File(outFolder).mkdirs();
        for (Thread thread : bufferingThreads)
            thread.start();
    }

    public void stop() {
        if (INTERRUPT) return;
        INTERRUPT = true;
        boolean threadsComplete;
        do {
            threadsComplete = true;
            for (Thread thread : bufferingThreads) {
                if (thread.isAlive()) {
                    threadsComplete = false;
                    break;
                }
            }
        } while (!threadsComplete);
    }

    public void reset() {
        INTERRUPT = true;
        for (int t = 0; t < bufferingThreads.length; t++)
            bufferingThreads[t] = new Thread(bufferRunnable);
    }

    public void waitForBuffer(boolean log) {
        // Stop current threads
        stop();

        // Expand writing threads
        bufferingThreads = new Thread[clearingThreads];
        for (int t = 0; t < bufferingThreads.length; t++)
            bufferingThreads[t] = new Thread(bufferRunnable);

        // Start new threads
        start();

        // Wait
        long timer = System.currentTimeMillis();
        while (!imageQueue.isEmpty()) {
            if (System.currentTimeMillis() - timer >= 1000) {
                timer = System.currentTimeMillis();
                if (log)
                    System.out.println(String.format("%.1f", getBufferUsage()*100) + "% left");
            }
        }

        // Complete
        stop();
    }

    public boolean isRecording() {
        return !INTERRUPT;
    }

    // Frame buffer
    public void queue(BufferedImage image, int index) throws InterruptedException {
        imageQueue.put(new IndexedImage(image, index));
    }

    public int getBufferSize() {
        return bufferSize;
    }

    // Misc
    public int getThreadsCount() {
        return bufferingThreads.length;
    }

    public int getClearingThreads() {
        return clearingThreads;
    }

    public String getOutFolder() {
        return outFolder;
    }

    public float getBufferUsage() {
        return (float) imageQueue.size() / bufferSize;
    }

}
