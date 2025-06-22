import java.util.LinkedList;
import java.util.Queue;

public class TaskQueue {
    private final Queue<RideTask> queue = new LinkedList<>();

    public synchronized void addTask(RideTask task) {
        queue.add(task);
        notify();  // Notify waiting workers
    }

    public synchronized RideTask getTask() {
        while (queue.isEmpty()) {
            try {
                wait();  // Wait until a task is available
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}
  
