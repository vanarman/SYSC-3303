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

    /**
     * Take ingredients from the table if there some exists and notify all threads; if not - put thread to the wait section
     *
     * @return Ingredients[] array of ingredients
     */
    public synchronized Ingredients[] get() {
        while(!onTable) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Cannot WAIT on "+ this.getClass().getName() +" class on GET");
            }
        }

        notifyAll();
        return ingredients;
    }

    /**
     * Place ingredients on the table if it is available and notify all threads; if not - put thread to the wait section.
     *
     * @param o
     */
    public synchronized void put(Ingredients[] o){
        while (onTable) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Cannot WAIT on " + this.getClass().getName() + " class on PUT");
            }
        }

        System.out.println(Thread.currentThread().getName() +" put on table: "+ o[0] +" and "+ o[1]);

        produced++;
        ingredients = o;
        onTable = true;
        notifyAll();
    }

    public synchronized void consumed() {
        System.out.println("Sandwiches consumed by the chefs: "+ ++consumed + "\n");
        if(consumed < Main.NUMBER_TO_CONSUME) {
            onTable = false;
            notifyAll();
        } else {
            System.out.println("Agent produced " + produced);
            System.exit(0);
        }
    }
}
