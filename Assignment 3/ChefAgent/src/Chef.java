/**
 * Chef class that represents consumer in Producer-Consumer problem
 *
 * @author Dmytro Sytnik (VanArman)
 * @version 05 February 2019
 */

public class Chef implements Runnable {
    private Table buf;
    private Ingredients have;
    private Ingredients[] pulled = null;

    /**
     * Default construction obtains Table object (shared memory part) and ingredient
     *
     * @param buf Table shared object between producer and consumers
     * @param have Ingredient the ingredient that Chef will have in unlimited amount
     */
    public Chef(Table buf, Ingredients have) {
        this.buf = buf;
        this.have = have;
    }

    /**
     * Validate that chef can create a sandwich and consume it (Current chef have missing ingredient)
     *
     * @return boolean true if chef can consume sandwich; false - otherwise
     */
    private boolean validate() {
        if(pulled != null) {
            if (pulled[0].ordinal() != have.ordinal() && pulled[1].ordinal() != have.ordinal()) {
//                System.out.println(Thread.currentThread().getName() +" have "+ have + " and pulled "+ pulled[0] +" & "+ pulled[1]);
                return true;
            }
        }
        return false;
    }

    /**
     * Overwritten method from Runnable interface that responsible for sandwich consumption (20 sandwiches in total)
     */
    @Override
    public void run() {
        while(true) {
            synchronized (buf) {
                long start = System.nanoTime();
                pulled = buf.get();
                buf.calculateTime(start);
                if (validate()) {
                    buf.consumed();
                } else {
                    buf.notifyAll();
                }

            }

        }
    }
}
