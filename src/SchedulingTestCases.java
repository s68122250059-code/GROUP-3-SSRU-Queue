import java.util.*;

/**
 * SchedulingTestCases: Test Cases อย่างน้อย 6 กรณีตามข้อกำหนดร่วม
 * 1. Normal Case  2. Empty Queue  3. Single Item
 * 4. Large Queue  5. Special/Edge Case  6. Cancel Case
 */
public class SchedulingTestCases {

    public static void runAll() {
        System.out.println("\n############################");
        System.out.println("#      TEST CASES          #");
        System.out.println("############################");

        // TC1: Normal Case (case study หลัก)
        System.out.println("\n[TC1] Normal Case: P1-P4 ตามโจทย์");
        List<Process> tc1 = ProcessGenerator.mainCaseStudy();
        List<Process> tc1rr = new ArrayList<>();
        for (Process p : tc1) tc1rr.add(p.copy());
        List<TraceRow> t1 = FCFSScheduler.simulate(tc1);
        ReportPrinter.printSummary("FCFS - TC1", tc1, QueueUtils.countContextSwitches(t1, "DEQUEUE"));
        List<TraceRow> t1b = RoundRobinScheduler.simulate(tc1rr, 3);
        ReportPrinter.printSummary("Round Robin - TC1", tc1rr, QueueUtils.countContextSwitches(t1b, "RUN"));

        // TC2: Empty Queue
        System.out.println("\n[TC2] Empty Queue: ไม่มี process เข้ามาเลย");
        List<Process> tc2 = new ArrayList<>();
        List<TraceRow> t2 = FCFSScheduler.simulate(tc2);
        System.out.println("จำนวน Trace rows = " + t2.size() + " (ควรเป็น 0 เพราะไม่มี process ให้ enqueue/dequeue)");

        // TC3: Single Item
        System.out.println("\n[TC3] Single Item: มี process เดียว");
        List<Process> tc3 = new ArrayList<>();
        tc3.add(new Process("P1", 0, 6));
        FCFSScheduler.simulate(tc3);
        ReportPrinter.printSummary("FCFS - TC3 (Single Item)", tc3, 0);

        // TC4: Large Queue (สุ่ม 200 processes)
        System.out.println("\n[TC4] Large Queue: จำลอง process จำนวนมาก (n=200)");
        List<Process> tc4 = ProcessGenerator.randomProcesses(200, 42L, 20);
        List<Process> tc4rr = new ArrayList<>();
        for (Process p : tc4) tc4rr.add(p.copy());
        long s1 = System.nanoTime();
        FCFSScheduler.simulate(tc4);
        long e1 = System.nanoTime();
        long s2 = System.nanoTime();
        RoundRobinScheduler.simulate(tc4rr, 3);
        long e2 = System.nanoTime();
        System.out.println("FCFS เวลาโดยประมาณ (ns) = " + (e1 - s1));
        System.out.println("Round Robin เวลาโดยประมาณ (ns) = " + (e2 - s2));

        // TC5: Special/Edge Case - Burst Time เท่ากันหมด (ทดสอบ Fairness ของ Round Robin)
        System.out.println("\n[TC5] Edge Case: ทุก process มี Burst Time เท่ากัน (=4) และ arrival พร้อมกัน");
        List<Process> tc5 = new ArrayList<>();
        for (int i = 1; i <= 5; i++) tc5.add(new Process("P" + i, 0, 4));
        RoundRobinScheduler.simulate(tc5, 3);
        ReportPrinter.printSummary("Round Robin - TC5 (Equal Burst)", tc5, 0);

        // TC6: Cancel Case - ยกเลิก process กลาง queue และยกเลิก process ที่ไม่มีอยู่จริง
        System.out.println("\n[TC6] Cancel Case: ยกเลิก process ที่อยู่กลางคิว และ process ที่ไม่มีอยู่จริง");
        Queue<Process> q = new ArrayDeque<>();
        q.offer(new Process("P1", 0, 5));
        q.offer(new Process("P2", 0, 5));
        q.offer(new Process("P3", 0, 5));
        q.offer(new Process("P4", 0, 5));
        System.out.println("Queue ก่อนยกเลิก: " + QueueUtils.queueToString(q));
        boolean removed = QueueUtils.cancelById(q, "P2"); // ยกเลิกรายการกลางคิว
        System.out.println("ยกเลิก P2 สำเร็จ? " + removed + " -> Queue: " + QueueUtils.queueToString(q));
        boolean removedFake = QueueUtils.cancelById(q, "P99"); // ยกเลิกรายการที่ไม่มีอยู่จริง
        System.out.println("ยกเลิก P99 (ไม่มีอยู่จริง) สำเร็จ? " + removedFake + " -> Queue: " + QueueUtils.queueToString(q));
    }
}
