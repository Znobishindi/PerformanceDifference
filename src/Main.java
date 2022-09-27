import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    private static final ConcurrentHashMap<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();
    private static final Map<Integer, Integer> map = Collections.synchronizedMap(new HashMap<>());
    private static final int MASS_SIZE = 5000000;
    private static final int[] mass = new int[MASS_SIZE];

    public static void main(String[] args) {

        generateMass(MASS_SIZE);
        Thread concurrent = new Thread(() -> {
            long start = System.currentTimeMillis();
            for (int i = 0; i < mass.length; i++) {
                concurrentHashMap.put(i, mass[i]);
            }
            long finish = System.currentTimeMillis();
            long time = finish - start;
            System.out.println("Добавление элементов в concurrentHashMap выполнено за " + time + " миллисекунд. Количество элементов: " + MASS_SIZE);
        });
        concurrent.start();

        new Thread(() -> {
            try {
                concurrent.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long sum = 0;
            long start = System.currentTimeMillis();
            for (int number : concurrentHashMap.values()) {
                sum += number;
            }
            long finish = System.currentTimeMillis();
            long time = finish - start;
            System.out.println(Thread.currentThread().getName() + ": Чтение значений из concurrentHashMap выполнено за " + time + " миллисекунд. Сумма всех элементов: " + sum);
        }, "Поток 1").start();


        new Thread(() -> {
            try {
                concurrent.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long sum = 0;
            long start = System.currentTimeMillis();
            for (int number : concurrentHashMap.values()) {
                sum += number;
            }
            long finish = System.currentTimeMillis();
            long time = finish - start;
            System.out.println(Thread.currentThread().getName() + ": Чтение значений из concurrentHashMap выполнено за " + time + " миллисекунд. Сумма всех элементов: " + sum);
        }, "Поток 2").start();


        Thread synchronizedMap = new Thread(() -> {
            long start = System.currentTimeMillis();
            for (int i = 0; i < mass.length; i++) {
                map.put(i, mass[i]);
            }
            long finish = System.currentTimeMillis();
            long time = finish - start;
            System.out.println("Добавление элементов в synchronizedMap выполнено за " + time + " миллисекунд. Количество элементов: " + MASS_SIZE);
        });
        synchronizedMap.start();


        new Thread(() -> {
            try {
                synchronizedMap.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long sum = 0;
            long start = System.currentTimeMillis();
            for (int number : map.values()) {
                sum += number;
            }
            long finish = System.currentTimeMillis();
            long time = finish - start;
            System.out.println(Thread.currentThread().getName() + ": Чтение значений из synchronizedMap выполнено за " + time + " миллисекунд. Сумма всех элементов: " + sum);
        }, "Поток 1 (S)").start();


        new Thread(() -> {
            try {
                synchronizedMap.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long sum = 0;
            long start = System.currentTimeMillis();
            for (int number : map.values()) {
                sum += number;
            }
            long finish = System.currentTimeMillis();
            long time = finish - start;
            System.out.println(Thread.currentThread().getName() + ": Чтение значений из synchronizedMap выполнено за " + time + " миллисекунд. Сумма всех элементов: " + sum);
        }, "Поток 2 (S)").start();
    }



    public static void generateMass(int masSize) {
        for (int i = 0; i < mass.length; i++) {
            mass[i] = (int) (Math.random() * 100 - 1);
        }
    }
}
