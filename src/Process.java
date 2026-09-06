/**
 * Process: โครงสร้างข้อมูล Process ที่ใช้ร่วมกันทั้ง FCFS และ Round Robin
 */
public class Process {
    final String id;
    final int arrivalTime;
    final int burstTime;
    int remainingTime;

    // ผลลัพธ์หลังจำลองเสร็จ
    int startTime = -1;      // เวลาที่เริ่มทำงานครั้งแรก (Response Time)
    int finishTime = -1;
    int waitingTime = -1;
    int turnaroundTime = -1;

    public Process(String id, int arrivalTime, int burstTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }

    public Process copy() {
        return new Process(this.id, this.arrivalTime, this.burstTime);
    }

    @Override
    public String toString() {
        return id + "(arr=" + arrivalTime + ",burst=" + burstTime + ")";
    }
}
