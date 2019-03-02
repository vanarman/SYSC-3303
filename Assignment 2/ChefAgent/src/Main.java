/**
 * Main class to run the problem
 *
 * @author Dmytro Sytnik (VanArman)
 * @version 05 February 2019
 */

public class Main {
    public static final int NUMBER_TO_CONSUME = 20;

    /**
     * Program enter method
     * @param args String[] - not used for this project
     */
    public static void main(String[] args) {
        Thread agent, chef1, chef2, chef3;
        Table t = new Table();

        agent = new Thread(new Agent(t), "Agent");
        chef1 = new Thread(new Chef(t, Ingredients.BREAD), "Chef 1");
        chef2 = new Thread(new Chef(t, Ingredients.PEANUT_BUTTER), "Chef 2");
        chef3 = new Thread(new Chef(t, Ingredients.JAM), "Chef 3");

        agent.start();
        chef1.start();
        chef2.start();
        chef3.start();
    }
}
