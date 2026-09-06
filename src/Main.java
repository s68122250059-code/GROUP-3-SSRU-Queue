import java.util.*;

/**
 * งานปฏิบัติการกลุ่มที่ 4: CPU Process Scheduling
 * เปรียบเทียบ Algorithm A (FCFS) กับ Algorithm B (Round Robin)
 *
 * คอมไพล์:  javac *.java
 * รัน:      java Main
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println(" กลุ่มที่ 4: CPU Process Scheduling");
        System.out.println(" Algorithm A: FCFS (Queue)");
        System.out.println(" Algorithm B: Round Robin (Circular Queue, quantum=3)");
        System.out.println("========================================");

        // --- Case Study หลัก พร้อม Trace ---
        List<Process> caseA = ProcessGenerator.mainCaseStudy();
        List<Process> caseB = new ArrayList<>();
        for (Process p : caseA) caseB.add(p.copy());

        System.out.println("\n--- Queue Trace: FCFS ---");
        System.out.printf("%-4s %-10s %-22s %-22s %-6s%n", "Step", "Operation", "Queue Before", "Queue After", "Output");
        List<TraceRow> traceFcfs = FCFSScheduler.simulate(caseA);
        for (TraceRow row : traceFcfs) row.print();
        ReportPrinter.printSummary("FCFS - Case Study", caseA, QueueUtils.countContextSwitches(traceFcfs, "DEQUEUE"));

        System.out.println("\n--- Queue Trace: Round Robin (quantum=3) ---");
        System.out.printf("%-4s %-10s %-22s %-22s %-6s%n", "Step", "Operation", "Queue Before", "Queue After", "Output");
        List<TraceRow> traceRr = RoundRobinScheduler.simulate(caseB, 3);
        for (TraceRow row : traceRr) row.print();
        ReportPrinter.printSummary("Round Robin - Case Study", caseB, QueueUtils.countContextSwitches(traceRr, "RUN"));

        // --- Test Cases ---
        SchedulingTestCases.runAll();

        // --- Experiment ---
        ExperimentRunner.run();
    }
}
