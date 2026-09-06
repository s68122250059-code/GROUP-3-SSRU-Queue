# CPU Process Scheduling — กลุ่มที่ 4

โปรแกรมจำลอง Algorithm A (FCFS) และ Algorithm B (Round Robin) สำหรับ CPU Process Scheduling

## โครงสร้างไฟล์

| ไฟล์ | หน้าที่ |
|---|---|
| `Process.java` | โครงสร้างข้อมูล Process (id, arrival, burst, remaining, ผลลัพธ์) |
| `TraceRow.java` | หนึ่งแถวของตาราง Queue Trace |
| `QueueUtils.java` | ฟังก์ชันช่วยเหลือ: แสดงคิว, นับ Context Switch, ยกเลิกรายการกลางคิว |
| `FCFSScheduler.java` | Algorithm A: First Come First Served (Linear Queue) |
| `RoundRobinScheduler.java` | Algorithm B: Round Robin (Circular Queue, quantum=3) |
| `ProcessGenerator.java` | สร้างชุดข้อมูล Case Study หลัก และข้อมูลสุ่มสำหรับทดสอบ/ทดลอง |
| `ReportPrinter.java` | พิมพ์ตารางสรุปผล (Waiting Time, Turnaround Time ฯลฯ) |
| `SchedulingTestCases.java` | Test Cases ทั้ง 6 กรณีตามข้อกำหนด |
| `ExperimentRunner.java` | ทดลองวัดเวลาด้วย n = 100 / 1,000 / 10,000 / 50,000 |
| `Main.java` | จุดเริ่มโปรแกรม (entry point) |

## วิธีคอมไพล์และรัน

```bash
javac *.java
java Main
```

## หมายเหตุ
ตัวเลข Waiting Time / Turnaround Time / Trace ทั้งหมดได้ตรวจสอบความถูกต้องด้วยการจำลองอัลกอริทึมเดียวกันแยกต่างหากแล้ว
ส่วนตาราง Experiment ในรายงาน ให้ใช้ตัวเลขที่รันได้จริงจากโปรแกรมนี้บนเครื่องของตนเองเป็นค่าสุดท้าย
