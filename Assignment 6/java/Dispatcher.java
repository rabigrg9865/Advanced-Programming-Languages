import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Dispatcher {
    private static final int NUM_WORKERS = 4;
    private static final int NUM_TASKS = 10;
    private static final String OUTPUT_FILE = "output.txt";

    public static void main(String[] args) {
        TaskQueue queue = new TaskQueue();

        // Start worker threads
        for (int i = 0; i < NUM_WORKERS; i++) {
            final int workerId = i;
            new Thread(() -> {
                while (true) {
                    RideTask task = queue.getTask();
                    if (task == null) {
                        break;
                    }

                    log("Worker " + workerId + " started task " + task.getTaskId());
                    task.process();
                    log("Worker " + workerId + " completed task " + task.getTaskId());
                }
                log("Worker " + workerId + " exiting.");
            }).start();
        }

        // Feed ride tasks into the queue
        for (int i = 0; i < NUM_TASKS; i++) {
            RideTask task = new RideTask(i);
            queue.addTask(task);
        }

        // Add null tasks to signal shutdown
        for (int i = 0; i < NUM_WORKERS; i++) {
            queue.addTask(null);
        }
    }

    private static synchronized void log(String message) {
    String timestamp = java.time.LocalDateTime.now().toString();
    String logEntry = "[" + timestamp + "] " + message;
    System.out.println(logEntry);
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE, true))) {
        writer.write(logEntry);
        writer.newLine();
    } catch (IOException e) {
        System.err.println("Logging failed: " + e.getMessage());
    }
    }
}
