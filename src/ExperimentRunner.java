import java.util.*;

/**
 * ExperimentRunner: Algorithm Experiment ตามข้อกำหนดร่วม
 * n = 100, 1000, 10000, 50000 — วัดด้วย nanoTime, มี warm-up, seed คงที่, เฉลี่ย 5 รอบ
 */
public class ExperimentRunner {

    public static void run() {
        System.out.println("\n############################");
        System.out.println("#   ALGORITHM EXPERIMENT   #");
        System.out.println("############################");
        int[] sizes = {100, 1000, 10000, 50000};
        long seed = 12345L;

        System.out.printf("%-10s %-20s %-20s%n", "n", "FCFS avg time (ms)", "RoundRobin avg time (ms)");
        for (int n : sizes) {
            // Warm-up (ไม่นับเวลา) เพื่อให้ JIT compile และลด noise
            for (int w = 0; w < 2; w++) {
                List<Process> warm = ProcessGenerator.randomProcesses(n, seed, 50);
                FCFSScheduler.simulate(warm);
                List<Process> warmRR = ProcessGenerator.randomProcesses(n, seed, 50);
                RoundRobinScheduler.simulate(warmRR, 3);
            }

            double totalFcfsNs = 0;
            double totalRrNs = 0;
            int rounds = 5;
            for (int r = 0; r < rounds; r++) {
                List<Process> data1 = ProcessGenerator.randomProcesses(n, seed, 50);
                long s1 = System.nanoTime();
                FCFSScheduler.simulate(data1);
                long e1 = System.nanoTime();
                totalFcfsNs += (e1 - s1);

                List<Process> data2 = ProcessGenerator.randomProcesses(n, seed, 50);
                long s2 = System.nanoTime();
                RoundRobinScheduler.simulate(data2, 3);
                long e2 = System.nanoTime();
                totalRrNs += (e2 - s2);
            }
            double avgFcfsMs = (totalFcfsNs / rounds) / 1_000_000.0;
            double avgRrMs = (totalRrNs / rounds) / 1_000_000.0;
            System.out.printf("%-10d %-20.3f %-20.3f%n", n, avgFcfsMs, avgRrMs);
        }
        System.out.println("\nหมายเหตุ: FCFS คือ O(n log n) จาก sort + O(n) จาก enqueue/dequeue");
        System.out.println("Round Robin คือ O(n * ceil(burst/quantum)) เพราะแต่ละ process อาจถูก enqueue กลับหลายครั้ง");
    }
}
