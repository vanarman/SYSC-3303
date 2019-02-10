import java.util.Random;

/**
 * Agent class that represents producer in Producer-Consumer problem
 *
 * @author Dmytro Sytnik (VanArman)
 * @version 05 February 2019
 */

public class Agent implements Runnable {
    private Table buf;

    /**
     * Default constructor that creates agent with shared Table object
     *
     * @param b Table shared object that will be shred between object of this class and chef's objects
     */
    public Agent(Table b) {
        this.buf = b;
    }

    /**
     * Randomly generates 2 ingredients
     *
     * @return Ingredients[] array of ingredients
     */
    public Ingredients[] generate() {
        int id1 = new Random().nextInt(Ingredients.values().length);
        int id2 = new Random().nextInt(Ingredients.values().length);
        while(id1 == id2) {
            id2 = new Random().nextInt(Ingredients.values().length);
        }

        Ingredients ing1 = Ingredients.values()[id1];
        Ingredients ing2 = Ingredients.values()[id2];

        System.out.println(Thread.currentThread().getName() +" put on table: "+ ing1 +" and "+ ing2);

        return new Ingredients[]{ing1, ing2};
    }

    /**
     * Overwritten method from Runnable interface that responsible for putting generated ingredients on the Table
     */
    @Override
    public void run() {
        while(true) {
            buf.put(generate());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
