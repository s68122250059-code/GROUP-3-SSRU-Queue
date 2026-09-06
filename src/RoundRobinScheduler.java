import java.util.*;

/**
 * RoundRobinScheduler: Algorithm B — Round Robin
 * ใช้ Deque<Process> (ArrayDeque) จำลอง Circular Queue:
 * dequeue จากหัวคิว, รันไม่เกิน quantum, ถ้ายังไม่เสร็จให้ enqueue กลับไปท้ายคิว (วนรอบ)
 *
 * Pseudocode:
 *   Algorithm ROUND_ROBIN(processList, quantum)
 *     sort processList by arrivalTime
 *     queue <- empty Circular Queue
 *     t <- 0
 *     add processes with arrivalTime <= t into queue
 *     while queue is not empty
 *         p <- dequeue(queue)
 *         run <- min(quantum, p.remainingTime)
 *         t <- t + run
 *         p.remainingTime <- p.remainingTime - run
 *         add newly-arrived processes (arrivalTime <= t) into queue
 *         if p.remainingTime > 0
 *             enqueue(queue, p)          // กลับไปต่อท้ายคิว (circular)
 *         else
 *             p.finishTime <- t
 *             p.turnaroundTime <- p.finishTime - p.arrivalTime
 *             p.waitingTime <- p.turnaroundTime - p.burstTime
 *         if queue is empty and still has processes not yet arrived
 *             t <- arrivalTime of next process ; add it to queue
 *   end while
 */
public class RoundRobinScheduler {

    public static List<TraceRow> simulate(List<Process> input, int quantum) {
        List<Process> procs = new ArrayList<>(input);
        procs.sort(Comparator.comparingInt(p -> p.arrivalTime));

        Deque<Process> queue = new ArrayDeque<>(); // Algorithm B: Circular Queue (poll หัว / offer ท้าย)
        List<TraceRow> trace = new ArrayList<>();
        int step = 1;
        int t = 0;
        int idx = 0;
        int n = procs.size();
        int finishedCount = 0;

        // เติม process ที่มาถึงตั้งแต่ t=0
        while (idx < n && procs.get(idx).arrivalTime <= t) {
            String before = QueueUtils.queueToString(queue);
            queue.offer(procs.get(idx));
            trace.add(new TraceRow(step++, "ENQUEUE " + procs.get(idx).id, before, QueueUtils.queueToString(queue), "-"));
            idx++;
        }

        while (finishedCount < n) {
            if (queue.isEmpty()) {
                // ไม่มีใครพร้อมทำงาน -> กระโดดเวลาไปยัง process ถัดไปที่จะมาถึง
                t = procs.get(idx).arrivalTime;
                while (idx < n && procs.get(idx).arrivalTime <= t) {
                    String before = QueueUtils.queueToString(queue);
                    queue.offer(procs.get(idx));
                    trace.add(new TraceRow(step++, "ENQUEUE " + procs.get(idx).id, before, QueueUtils.queueToString(queue), "-"));
                    idx++;
                }
                continue;
            }
            String before = QueueUtils.queueToString(queue);
            Process p = queue.poll(); // dequeue front, O(1)
            if (p.startTime == -1) p.startTime = t; // response time ครั้งแรกที่ได้ CPU

            int run = Math.min(quantum, p.remainingTime);
            t += run;
            p.remainingTime -= run;

            trace.add(new TraceRow(step++, "RUN " + p.id, before, QueueUtils.queueToString(queue), "used " + run));

            // เติม process ใหม่ที่มาถึงระหว่างช่วงที่กำลังรัน (ก่อนนำ process เดิมกลับเข้าคิว)
            while (idx < n && procs.get(idx).arrivalTime <= t) {
                String b2 = QueueUtils.queueToString(queue);
                queue.offer(procs.get(idx));
                trace.add(new TraceRow(step++, "ENQUEUE " + procs.get(idx).id, b2, QueueUtils.queueToString(queue), "-"));
                idx++;
            }

            if (p.remainingTime > 0) {
                String b3 = QueueUtils.queueToString(queue);
                queue.offer(p); // circular: กลับไปต่อท้ายคิว
                trace.add(new TraceRow(step++, "REQUEUE " + p.id, b3, QueueUtils.queueToString(queue), "-"));
            } else {
                p.finishTime = t;
                p.turnaroundTime = p.finishTime - p.arrivalTime;
                p.waitingTime = p.turnaroundTime - p.burstTime;
                finishedCount++;
                trace.add(new TraceRow(step++, "FINISH " + p.id, QueueUtils.queueToString(queue), QueueUtils.queueToString(queue), p.id));
            }
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
