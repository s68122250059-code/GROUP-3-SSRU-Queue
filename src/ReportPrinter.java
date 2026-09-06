import java.util.*;

/**
 * ReportPrinter: พิมพ์ตารางสรุปผล (PID, Arrival, Burst, Start, Finish, Wait, Turnaround)
 * พร้อม Average Waiting Time / Average Turnaround Time / Context Switches
 */
public class ReportPrinter {

    public static void printSummary(String title, List<Process> procs, int contextSwitches) {
        System.out.println("\n=== " + title + " ===");
        System.out.printf("%-6s %-8s %-8s %-8s %-8s %-8s %-10s%n",
                "PID", "Arrival", "Burst", "Start", "Finish", "Wait", "Turnaround");
        double totalWait = 0, totalTurn = 0;
        List<Process> sorted = new ArrayList<>(procs);
        sorted.sort(Comparator.comparing(p -> p.id));
        for (Process p : sorted) {
            System.out.printf("%-6s %-8d %-8d %-8d %-8d %-8d %-10d%n",
                    p.id, p.arrivalTime, p.burstTime, p.startTime, p.finishTime, p.waitingTime, p.turnaroundTime);
            totalWait += p.waitingTime;
            totalTurn += p.turnaroundTime;
        }
        System.out.printf("Average Waiting Time    = %.2f%n", totalWait / procs.size());
        System.out.printf("Average Turnaround Time = %.2f%n", totalTurn / procs.size());
        System.out.println("Context Switches        = " + contextSwitches);
    }
}
