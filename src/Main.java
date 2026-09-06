import java.util.*;

/**
 * กลุ่มที่ 4: CPU Process Scheduling — FCFS vs Round Robin (quantum=3)
 * คอมไพล์: javac *.java   |   รัน: java Main
 */
public class Main {
    public static void main(String[] args) {

        // ---------- Case Study หลัก (P1..P4 ตามโจทย์) ----------
        // หมายเหตุ: โจทย์ระบุเฉพาะ Burst Time จึงสมมติ Arrival Time เพิ่ม (0,1,2,3)
        List<Process> caseStudy = List.of(
                new Process("P1", 0, 8), new Process("P2", 1, 4),
                new Process("P3", 2, 9), new Process("P4", 3, 5));

        List<Process> fcfsResult = Scheduler.fcfs(copy(caseStudy), true);
        Scheduler.printSummary("FCFS - Case Study", fcfsResult);

        List<Process> rrResult = Scheduler.roundRobin(copy(caseStudy), 3, true);
        Scheduler.printSummary("Round Robin - Case Study", rrResult);

        // ---------- Test Cases (6 กรณี) ----------
        System.out.println("\n===== TEST CASES =====");

        System.out.println("[TC1] Normal Case: ดูผลลัพธ์ Case Study ด้านบน");

        System.out.println("\n[TC2] Empty Queue:");
        Scheduler.printSummary("FCFS - Empty", Scheduler.fcfs(new ArrayList<>(), false));

        System.out.println("\n[TC3] Single Item:");
        Scheduler.printSummary("FCFS - Single", Scheduler.fcfs(List.of(new Process("P1", 0, 6)), false));

        System.out.println("\n[TC4] Large Queue (n=200, random):");
        List<Process> big = Scheduler.randomProcesses(200, 42L, 20);
        Scheduler.fcfs(copy(big), false);
        Scheduler.roundRobin(copy(big), 3, false);
        System.out.println("รันสำเร็จโดยไม่มี Error สำหรับ n=200");

        System.out.println("\n[TC5] Edge Case: Burst เท่ากันหมด (=4), arrival พร้อมกัน:");
        List<Process> equalBurst = new ArrayList<>();
        for (int i = 1; i <= 5; i++) equalBurst.add(new Process("P" + i, 0, 4));
        Scheduler.printSummary("Round Robin - Equal Burst", Scheduler.roundRobin(equalBurst, 3, false));

        System.out.println("\n[TC6] Cancel Case:");
        Queue<Process> q = new ArrayDeque<>(List.of(
                new Process("P1", 0, 5), new Process("P2", 0, 5),
                new Process("P3", 0, 5), new Process("P4", 0, 5)));
        System.out.println("ก่อนยกเลิก: " + Scheduler.show(q));
        System.out.println("ยกเลิก P2 (กลางคิว) สำเร็จ? " + Scheduler.cancel(q, "P2") + " -> " + Scheduler.show(q));
        System.out.println("ยกเลิก P99 (ไม่มีจริง) สำเร็จ? " + Scheduler.cancel(q, "P99") + " -> " + Scheduler.show(q));

        // ---------- Experiment: n = 100, 1000, 10000, 50000 ----------
        System.out.println("\n===== ALGORITHM EXPERIMENT =====");
        System.out.printf("%-10s %-18s %-18s%n", "n", "FCFS avg (ms)", "RoundRobin avg (ms)");
        long seed = 12345L;
        for (int n : new int[]{100, 1000, 10000, 50000}) {
            for (int w = 0; w < 2; w++) { // warm-up
                Scheduler.fcfs(Scheduler.randomProcesses(n, seed, 50), false);
                Scheduler.roundRobin(Scheduler.randomProcesses(n, seed, 50), 3, false);
            }
            long fcfsNs = 0, rrNs = 0;
            int rounds = 5;
            for (int r = 0; r < rounds; r++) {
                long s = System.nanoTime();
                Scheduler.fcfs(Scheduler.randomProcesses(n, seed, 50), false);
                fcfsNs += System.nanoTime() - s;

                s = System.nanoTime();
                Scheduler.roundRobin(Scheduler.randomProcesses(n, seed, 50), 3, false);
                rrNs += System.nanoTime() - s;
            }
            System.out.printf("%-10d %-18.3f %-18.3f%n", n,
                    (fcfsNs / rounds) / 1e6, (rrNs / rounds) / 1e6);
        }
    }

    static List<Process> copy(List<Process> src) {
        List<Process> out = new ArrayList<>();
        for (Process p : src) out.add(p.copy());
        return out;
    }
}
