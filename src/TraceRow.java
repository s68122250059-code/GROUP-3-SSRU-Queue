/**
 * TraceRow: หนึ่งแถวของตาราง Queue Trace (Step, Operation, Queue Before, Queue After, Output)
 */
public class TraceRow {
    int step;
    String operation;
    String queueBefore;
    String queueAfter;
    String output;

    public TraceRow(int step, String operation, String queueBefore, String queueAfter, String output) {
        this.step = step;
        this.operation = operation;
        this.queueBefore = queueBefore;
        this.queueAfter = queueAfter;
        this.output = output;
    }

    public void print() {
        System.out.printf("%-4d %-10s %-22s %-22s %-6s%n",
                step, operation, queueBefore, queueAfter, output);
    }
}
