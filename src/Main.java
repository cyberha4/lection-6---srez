import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by root on 13.02.17.
 */
public class Main {
    public volatile static HashMap<Integer, Integer> hashMap = new HashMap<>();
    public static Object lock = new Object();
    public volatile static int counterPut = 0;
    public volatile static int counterHash = 0;


    public static void main(String[] args) {
        Runnable run = new Runnable() {
            @Override
            public void run() {

                Random random = new Random(590);
                while(true) {
                    // Создадим число от 0 до 10
                    //тестовое изменение
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int number = random.nextInt(100);
                    Integer frequency = hashMap.get(number);

                    synchronized (lock) {
                        counterPut++;
                        hashMap.put(number, frequency == null ? 1 : frequency + 1);
                    }
                }
            }
        };

        Runnable run2 = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(2000);

                        synchronized (lock) {
                            //System.out.println(hashMap);

                            Set<Map.Entry<Integer, Integer>> set = hashMap.entrySet();

                            counterHash = 0;


                            for (Map.Entry<Integer, Integer> me : set) {
                                System.out.print(me.getKey() + ":");
                                System.out.print(me.getValue());
                                System.out.print(" ");
                                counterHash += me.getValue();
                            }

                            System.out.println();
                            if(counterHash == counterPut)
                                System.out.println("good");
                            System.out.println("counterHash " + counterHash);
                            System.out.println("counterPut " + counterPut);

                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread thread = new Thread(run);
        Thread thread2 = new Thread(run2);

        thread.start();
        thread2.start();
    }
}
