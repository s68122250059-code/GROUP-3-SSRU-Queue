# CPU Process Scheduling — กลุ่มที่ 4

โปรแกรมจำลอง Algorithm A (FCFS) และ Algorithm B (Round Robin) 

## โครงสร้างไฟล์

| ไฟล์ | หน้าที่ |
|---|---|
| `Process.java` | ข้อมูล Process หนึ่งตัว (id, arrival, burst, ผลลัพธ์) |
| `Scheduler.java` | รวม Algorithm A (fcfs), Algorithm B (roundRobin), และฟังก์ชันช่วยเหลือ (show, cancel, printSummary, randomProcesses) |
| `Main.java` | รัน Case Study, Test Cases 6 กรณี, และ Experiment (n=100 ถึง 50,000) |

## วิธีคอมไพล์และรัน

```bash
javac *.java
java Main
```

## หมายเหตุ
- `fcfs(...)` และ `roundRobin(...)` รับพารามิเตอร์ `printTrace` — ใส่ `true` เพื่อพิมพ์ตาราง Queue Trace แบบ step-by-step (ใช้กับ Case Study หลัก), ใส่ `false` เมื่อรันจำนวนมาก (Test Cases / Experiment) เพื่อไม่ให้ Console ยาวเกินไป
- ตัวเลข Waiting Time / Turnaround Time / Trace ได้ตรวจสอบความถูกต้องด้วยการจำลองอัลกอริทึมเดียวกันแยกต่างหากแล้ว
- ตาราง Experiment ในรายงาน ให้ใช้ตัวเลขที่รันได้จริงจากโปรแกรมนี้บนเครื่องของตนเองเป็นค่าสุดท้าย
