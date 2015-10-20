import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:15/6/8
 */

public class BlockingQueueTest {
    public static void main(String[] args) {
        BlockingQueue bq= new LinkedBlockingQueue(2);

            bq.offer("1");
            bq.offer("2");
            bq.offer("3");






        System.out.println(bq.poll());
        System.out.println(bq.poll());

        System.out.println(bq.peek());
        System.out.println(1111);
        System.out.println(bq.element());
    }
}
