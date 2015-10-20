import java.util.LinkedList;
import java.util.Queue;

/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:15/6/5
 */

public class QueueTest {

    public static void main(String[] args) {
        Queue<String> queueTest = new LinkedList<String>();
        queueTest.offer("Hello");
        queueTest.offer("World!");
        queueTest.offer("你好！");
        queueTest.offer("good");
        System.out.println(queueTest.size());
        String str;
        while((str= queueTest.poll())!=null){
            System.out.println(str);
        }
        System.out.println();
        System.out.println(queueTest.size());
    }
}
