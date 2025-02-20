import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a clock that holds the current time and date.
 * Uses synchronization to ensure thread-safe updates and reads.
 */
class Clock {
    private String currentTime;

    /**
     * Updates the current time using the system time.
     * Synchronized to prevent concurrent modification.
     */
    public synchronized void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        currentTime = sdf.format(new Date());
    }

    /**
     * Retrieves the current time.
     * Synchronized to ensure consistency with the updater thread.
     * @return The formatted current time as a string.
     */
    public synchronized String getCurrentTime() {
        return currentTime;
    }
}

/**
 * Thread responsible for updating the clock's time every second.
 */
class UpdaterThread implements Runnable {
    private Clock clock;

    public UpdaterThread(Clock clock) {
        this.clock = clock;
    }

    @Override
    public void run() {
        while (true) {
            clock.updateTime();
            try {
                Thread.sleep(1000); // Update every second
            } catch (InterruptedException e) {
                System.err.println("Updater thread interrupted: " + e.getMessage());
            }
        }
    }
}

/**
 * Thread responsible for displaying the clock's time every second.
 * Runs at a higher priority than the updater thread.
 */
class DisplayThread implements Runnable {
    private Clock clock;

    public DisplayThread(Clock clock) {
        this.clock = clock;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(clock.getCurrentTime());
            try {
                Thread.sleep(1000); // Display every second
            } catch (InterruptedException e) {
                System.err.println("Display thread interrupted: " + e.getMessage());
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Clock clock = new Clock();
        Thread updater = new Thread(new UpdaterThread(clock));
        Thread display = new Thread(new DisplayThread(clock));

        // Set thread priorities
        display.setPriority(Thread.MAX_PRIORITY); // Higher priority for display
        updater.setPriority(Thread.NORM_PRIORITY); // Normal priority for updater

        updater.start();
        display.start();
    }
}