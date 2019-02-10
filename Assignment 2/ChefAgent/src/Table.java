/**
 * Shared object for Producer-Consumer problem
 *
 * @author Dmytro Sytnik (VanArman)
 * @version 05 February 2019
 */

public class Table {
    private Ingredients[] ingredients = null;
    private boolean onTable = false;

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

        Ingredients[] temp = ingredients;
        ingredients = null;
        onTable = false;
        notifyAll();
        return temp;
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
                System.out.println("Cannot WAIT on "+ this.getClass().getName() +" class on PUT");
            }
        }

        ingredients = o;
        onTable = true;
        notifyAll();
    }
}
