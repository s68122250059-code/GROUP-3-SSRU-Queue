import java.util.Collection;

/**
 * QueueUtils: ฟังก์ชันช่วยเหลือที่ใช้ร่วมกันระหว่าง Scheduler ต่าง ๆ
 */
public class QueueUtils {

    // แสดง Queue ปัจจุบันเป็น string รูปแบบ [P1,P2,P3] สำหรับตาราง Trace (O(n) - DISPLAY operation)
    public static String queueToString(Collection<Process> q) {
        StringBuilder sb = new StringBuilder("[");
        int i = 0;
        for (Process p : q) {
            if (i++ > 0) sb.append(",");
            sb.append(p.id);
        }
        sb.append("]");
        return sb.toString();
    }

    // นับจำนวน Context Switch จาก Trace โดยนับ operation ที่ขึ้นต้นด้วย prefix ที่กำหนด (เช่น "DEQUEUE" หรือ "RUN")
    public static int countContextSwitches(java.util.List<TraceRow> trace, String runPrefix) {
        int count = 0;
        for (TraceRow r : trace) {
            if (r.operation.startsWith(runPrefix)) count++;
        }
        return Math.max(0, count - 1);
    }

    // ยกเลิกรายการกลาง Queue: ไล่หาทีละตัว (O(n)) เพราะ Queue ปกติไม่รองรับ random access โดยตรง
    // ใช้สำหรับ Test Case "Cancel Case" (ยกเลิกรายการที่อยู่กลาง queue / ยกเลิกรายการที่ไม่มีอยู่จริง)
    public static boolean cancelById(java.util.Queue<Process> queue, String id) {
        java.util.Iterator<Process> it = queue.iterator();
        while (it.hasNext()) {
            Process p = it.next();
            if (p.id.equals(id)) {
                it.remove();
                return true;
            }
        }
        return false; // ไม่พบรายการ -> คืนค่า false แทนการโยน exception
    }
}
