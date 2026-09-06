/** ข้อมูล Process หนึ่งตัวในระบบ */
public class Process {
    String id;
    int arrival, burst, remaining;
    int start, finish, wait, turnaround;

    Process(String id, int arrival, int burst) {
        this.id = id;
        this.arrival = arrival;
        this.burst = burst;
        this.remaining = burst;
    }

    Process copy() { return new Process(id, arrival, burst); }
}
