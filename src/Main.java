import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Хидир on 13.02.2017.
 */
public class Main {
    static HashMap<Integer, Integer> hashMap = new HashMap<>();

    static volatile boolean play = true;
    static volatile int sec;

    static volatile Object lock = new Object();

    public static void main(String[] args) {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {

                while(play) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int randInt = new Random().nextInt(10);
                    Integer curVal = hashMap.containsKey(randInt) ? hashMap.get(randInt) : 0;

                    if(++curVal > 5){
                        System.out.println("Значение "+randInt+ " сгенерировано больше 5 раз - ");
                        play = false;
                    }

                    sec++;
                    hashMap.put(randInt, curVal);
                    synchronized (lock) {
                        lock.notify();
                    }

                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {

                while(play){
                    System.out.println(sec);    if(sec != 0 && sec%5 == 0)  for (Map.Entry<Integer, Integer> item : hashMap.entrySet()) {
                        System.out.printf("Ключ: %d  Значение: %s \n", item.getKey(), item.getValue());
                    }
                    synchronized (lock) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}
