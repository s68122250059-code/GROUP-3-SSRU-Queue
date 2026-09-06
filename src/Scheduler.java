import java.util.*;

/**
 * Scheduler: รวม Algorithm A (FCFS) และ Algorithm B (Round Robin) ไว้ในที่เดียว
 * ทั้งสอง Algorithm ใช้ ArrayDeque เป็น Queue (FCFS = Linear, Round Robin = Circular)
 */
public class Scheduler {

    /** Algorithm A: FCFS — เข้าก่อนได้ CPU ก่อน จนกว่าจะเสร็จ */
    static List<Process> fcfs(List<Process> input, boolean printTrace) {
        List<Process> ps = new ArrayList<>(input);
        ps.sort(Comparator.comparingInt(p -> p.arrival));
        Queue<Process> q = new ArrayDeque<>();

        if (printTrace) System.out.printf("%n--- FCFS Trace ---%n%-4s %-10s %-18s %-18s %-6s%n",
                "Step", "Op", "Before", "After", "Out");
        int step = 1, t = 0;

        for (Process p : ps) {
            String before = show(q);
            q.offer(p); // enqueue O(1)
            if (printTrace) System.out.printf("%-4d %-10s %-18s %-18s %-6s%n", step++, "ENQUEUE " + p.id, before, show(q), "-");
        }
        while (!q.isEmpty()) {
            String before = show(q);
            Process p = q.poll(); // dequeue O(1)
            t = Math.max(t, p.arrival);
            p.start = t;
            t += p.burst;
            p.finish = t;
            p.wait = p.start - p.arrival;
            p.turnaround = p.finish - p.arrival;
            if (printTrace) System.out.printf("%-4d %-10s %-18s %-18s %-6s%n", step++, "DEQUEUE", before, show(q), p.id);
        }
        return ps;
    }

    /** Algorithm B: Round Robin — สลับกันใช้ CPU ทีละ quantum แบบวนรอบ (Circular Queue) */
    static List<Process> roundRobin(List<Process> input, int quantum, boolean printTrace) {
        List<Process> ps = new ArrayList<>(input);
        ps.sort(Comparator.comparingInt(p -> p.arrival));
        Deque<Process> q = new ArrayDeque<>();
        int n = ps.size(), idx = 0, t = 0, done = 0, step = 1;

        if (printTrace) System.out.printf("%n--- Round Robin Trace (quantum=%d) ---%n%-4s %-10s %-18s %-18s %-8s%n",
                quantum, "Step", "Op", "Before", "After", "Out");

        while (idx < n && ps.get(idx).arrival <= t) { // arrivals ณ t=0
            String before = show(q);
            q.offer(ps.get(idx));
            if (printTrace) System.out.printf("%-4d %-10s %-18s %-18s %-8s%n", step++, "ENQUEUE " + ps.get(idx).id, before, show(q), "-");
            idx++;
        }

        while (done < n) {
            if (q.isEmpty()) { // ไม่มีใครพร้อม -> ข้ามเวลาไปยัง process ถัดไป
                t = ps.get(idx).arrival;
                while (idx < n && ps.get(idx).arrival <= t) { q.offer(ps.get(idx)); idx++; }
                continue;
            }
            String before = show(q);
            Process p = q.poll(); // O(1)
            int run = Math.min(quantum, p.remaining);
            t += run;
            p.remaining -= run;
            if (printTrace) System.out.printf("%-4d %-10s %-18s %-18s %-8s%n", step++, "RUN " + p.id, before, show(q), "used " + run);

            while (idx < n && ps.get(idx).arrival <= t) { // process ใหม่ที่มาถึงระหว่างรัน
                String b2 = show(q);
                q.offer(ps.get(idx));
                if (printTrace) System.out.printf("%-4d %-10s %-18s %-18s %-8s%n", step++, "ENQUEUE " + ps.get(idx).id, b2, show(q), "-");
                idx++;
            }

            if (p.remaining > 0) {
                String b3 = show(q);
                q.offer(p); // circular: กลับไปต่อท้ายคิว
                if (printTrace) System.out.printf("%-4d %-10s %-18s %-18s %-8s%n", step++, "REQUEUE " + p.id, b3, show(q), "-");
            } else {
                p.finish = t;
                p.turnaround = p.finish - p.arrival;
                p.wait = p.turnaround - p.burst;
                done++;
                if (printTrace) System.out.printf("%-4d %-10s %-18s %-18s %-8s%n", step++, "FINISH " + p.id, show(q), show(q), p.id);
            }
        }
        return ps;
    }

    // แสดงคิวเป็น [P1,P2,...] — ใช้ทำ Trace / DISPLAY (O(n))
    static String show(Collection<Process> q) {
        StringBuilder sb = new StringBuilder("[");
        int i = 0;
        for (Process p : q) sb.append(i++ > 0 ? "," : "").append(p.id);
        return sb.append("]").toString();
    }

    // ยกเลิก process กลางคิว หรือคืน false ถ้าไม่พบ (ใช้ในกรณี Cancel Case) — O(n)
    static boolean cancel(Queue<Process> q, String id) {
        Iterator<Process> it = q.iterator();
        while (it.hasNext()) if (it.next().id.equals(id)) { it.remove(); return true; }
        return false;
    }

    // สรุปผล: Average Waiting / Turnaround Time
    static void printSummary(String title, List<Process> ps) {
        System.out.println("\n=== " + title + " ===");
        System.out.printf("%-6s %-8s %-8s %-8s %-8s%n", "PID", "Arrival", "Burst", "Wait", "Turnaround");
        double w = 0, tt = 0;
        for (Process p : ps) {
            System.out.printf("%-6s %-8d %-8d %-8d %-8d%n", p.id, p.arrival, p.burst, p.wait, p.turnaround);
            w += p.wait; tt += p.turnaround;
        }
        System.out.printf("Avg Wait = %.2f, Avg Turnaround = %.2f%n", w / ps.size(), tt / ps.size());
    }

    // สุ่ม process ด้วย seed คงที่ (ทำซ้ำได้) — ใช้ใน Test Cases และ Experiment
    static List<Process> randomProcesses(int n, long seed, int maxBurst) {
        Random r = new Random(seed);
        List<Process> list = new ArrayList<>();
        int arrival = 0;
        for (int i = 0; i < n; i++) {
            arrival += r.nextInt(3);
            list.add(new Process("P" + (i + 1), arrival, 1 + r.nextInt(maxBurst)));
        }
        return list;
    }
}
