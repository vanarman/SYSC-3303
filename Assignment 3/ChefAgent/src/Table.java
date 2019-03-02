import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Shared object for Producer-Consumer problem
 *
 * @author Dmytro Sytnik (VanArman)
 * @version 05 February 2019
 */

public class Table {
    private Ingredients[] ingredients = null;
    private boolean onTable = false;
    private long consumed = 0;
    private int produced = 0;

    private ThreadMXBean threadMxBean = ManagementFactory.getThreadMXBean();
    protected Map<String, Long> nanoTime = new HashMap<>();
    protected Map<String, Long> mxBeanTime = new HashMap<>();

    /**
     * Take ingredients from the table if there some exists and notify all threads; if not - put thread to the wait section
     *
     * @return Ingredients[] array of ingredients
     */
    public synchronized Ingredients[] get() {
        long start = System.nanoTime();
        while(!onTable) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Cannot WAIT on "+ this.getClass().getName() +" class on GET");
            }
        }
        subtractWait(start);


        notifyAll();
        return ingredients;
    }

    /**
     * Place ingredients on the table if it is available and notify all threads; if not - put thread to the wait section.
     *
     * @param o
     */
    public synchronized void put(Ingredients[] o){
        long start = System.nanoTime();
        while (onTable) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Cannot WAIT on " + this.getClass().getName() + " class on PUT");
            }
        }
        subtractWait(start);

        produced++;
        ingredients = o;
        onTable = true;
        notifyAll();
    }

    public synchronized void consumed() {
        if(++consumed < Main.NUMBER_TO_CONSUME) {
            onTable = false;
            notifyAll();
        } else {
            System.out.println("====================== Nano Time ======================");
            printHashMap(nanoTime);
            System.out.println("======================= MxBean ========================");
            measure();
            printHashMap(mxBeanTime);

            System.out.println("Agent produced " + produced);
            System.exit(0);
        }
    }

    public synchronized void calculateTime(long startTime) {
        nanoTime.putIfAbsent(Thread.currentThread().getName(), (Long.valueOf(0)));
        long time = nanoTime.get(Thread.currentThread().getName()) + (System.nanoTime() - startTime);
        nanoTime.put(Thread.currentThread().getName(), time);
    }

    public synchronized void subtractWait(long startTime) {
        nanoTime.putIfAbsent(Thread.currentThread().getName(), (Long.valueOf(0)));
        long time = nanoTime.get(Thread.currentThread().getName());
        long subTime = (System.nanoTime() - startTime);
        nanoTime.put(Thread.currentThread().getName(), (time - subTime));
    }

    private synchronized void measure() {
        ThreadInfo[] threadInfos = threadMxBean.dumpAllThreads(false, false);
        for (ThreadInfo info : threadInfos) {
            String threadName = info.getThreadName();
            if(threadName.equals(Main.agent) || threadName.equals(Main.chef1) || threadName.equals(Main.chef2) || threadName.equals(Main.chef3)) {
                mxBeanTime.put(info.getThreadName(), threadMxBean.getThreadCpuTime(info.getThreadId()));
            }
        }
    }

    private synchronized void printHashMap(Map hs) {
        Iterator it = hs.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + "\t = \t" + (float)((long)pair.getValue()/1e+6) + " ms");
            it.remove();
        }
        System.out.println();
    }
}
