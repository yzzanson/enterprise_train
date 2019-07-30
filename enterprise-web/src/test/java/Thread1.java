/**
 * @Description
 * @Author zezhouyang
 * @Date 18/7/26 上午11:42
 */
public class Thread1 implements Runnable{

    @Override
    public void run() {
        try {
            System.out.println("sssss");
            Thread.sleep(2000);
            System.out.println("aaaaaaaaaa");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
