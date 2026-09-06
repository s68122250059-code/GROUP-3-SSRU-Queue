import java.util.*;

/**
 * ProcessGenerator: สร้างชุดข้อมูล Process
 *  - mainCaseStudy(): ชุดข้อมูลบังคับตามโจทย์ (P1..P4)
 *  - randomProcesses(): สุ่มข้อมูลด้วย seed คงที่ เพื่อใช้ใน Test Cases และ Experiment
 */
public class ProcessGenerator {

    // Case Study หลัก: โจทย์ระบุเฉพาะ Burst Time จึงสมมติ Arrival Time เพิ่มเติม
    // (P1=0, P2=1, P3=2, P4=3) เพื่อให้จำลอง Algorithm ได้ครบถ้วน
    public static List<Process> mainCaseStudy() {
        List<Process> list = new ArrayList<>();
        list.add(new Process("P1", 0, 8));
        list.add(new Process("P2", 1, 4));
        list.add(new Process("P3", 2, 9));
        list.add(new Process("P4", 3, 5));
        return list;
    }

    // สร้าง process แบบสุ่ม (seed คงที่ -> ผลลัพธ์ทำซ้ำได้)
    public static List<Process> randomProcesses(int n, long seed, int maxBurst) {
        Random rnd = new Random(seed);
        List<Process> list = new ArrayList<>(n);
        int arrival = 0;
        for (int i = 0; i < n; i++) {
            arrival += rnd.nextInt(3); // arrival time เพิ่มขึ้นแบบสุ่มเล็กน้อย
            int burst = 1 + rnd.nextInt(maxBurst);
            list.add(new Process("P" + (i + 1), arrival, burst));
        }
        return list;
    }
}
