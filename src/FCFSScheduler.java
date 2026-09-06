import java.util.*;

/**
 * FCFSScheduler: Algorithm A — First Come First Served
 * ใช้ Queue<Process> (ArrayDeque) แบบ FIFO ธรรมดา
 *
 * Pseudocode:
 *   Algorithm FCFS(processList)
 *     sort processList by arrivalTime
 *     queue <- empty Queue
 *     for each p in processList
 *         enqueue(queue, p)
 *     t <- 0
 *     while queue is not empty
 *         p <- dequeue(queue)
 *         t <- max(t, p.arrivalTime)
 *         p.startTime <- t
 *         t <- t + p.burstTime
 *         p.finishTime <- t
 *         p.waitingTime <- p.startTime - p.arrivalTime
 *         p.turnaroundTime <- p.finishTime - p.arrivalTime
 *     end while
 */
public class FCFSScheduler {

    public static List<TraceRow> simulate(List<Process> input) {
        List<Process> procs = new ArrayList<>(input);
        procs.sort(Comparator.comparingInt(p -> p.arrivalTime));

        Queue<Process> queue = new ArrayDeque<>(); // Algorithm A: Linear Queue (FIFO)
        List<TraceRow> trace = new ArrayList<>();
        int step = 1;

        for (Process p : procs) {
            String before = QueueUtils.queueToString(queue);
            queue.offer(p); // enqueue -> O(1)
            trace.add(new TraceRow(step++, "ENQUEUE " + p.id, before, QueueUtils.queueToString(queue), "-"));
        }

        int t = 0;
        while (!queue.isEmpty()) {
            String before = QueueUtils.queueToString(queue);
            Process p = queue.poll(); // dequeue -> O(1)
            t = Math.max(t, p.arrivalTime);
            p.startTime = t;
            t += p.burstTime;
            p.finishTime = t;
            p.waitingTime = p.startTime - p.arrivalTime;
            p.turnaroundTime = p.finishTime - p.arrivalTime;
            trace.add(new TraceRow(step++, "DEQUEUE", before, QueueUtils.queueToString(queue), p.id));
        }

        // เขียนผลลัพธ์กลับเข้า object เดิมของ input (อ้างอิงตาม id)
        for (Process orig : input) {
            for (Process done : procs) {
                if (done.id.equals(orig.id)) {
                    orig.startTime = done.startTime;
                    orig.finishTime = done.finishTime;
                    orig.waitingTime = done.waitingTime;
                    orig.turnaroundTime = done.turnaroundTime;
                }
            }
        }
        return trace;
    }
}
