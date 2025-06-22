public class RideTask {
    private final int taskId;

    public RideTask(int taskId) {
        this.taskId = taskId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void process() {
    long start = System.currentTimeMillis();
    try {
        Thread.sleep(1000 + (int)(Math.random() * 2000)); // 1–3 seconds
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        System.err.println("Task " + taskId + " was interrupted.");
    }
    long end = System.currentTimeMillis();
    long duration = end - start;
    System.out.println("Task " + taskId + " completed in " + duration + " ms.");
    }
}
  
