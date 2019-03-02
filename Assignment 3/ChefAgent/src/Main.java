/**
 * Main class to run the problem
 *
 * @author Dmytro Sytnik (VanArman)
 * @version 05 February 2019
 */

public class Main {
    public static final int NUMBER_TO_CONSUME = 1;
    public static final String agent = "Agent";
    public static final String chef1 = "Chef 1";
    public static final String chef2 = "Chef 2";
    public static final String chef3 = "Chef 3";



    /**
     * Program enter method
     * @param args String[] - not used for this project
     */
    public static void main(String[] args) {
        Thread agent, chef1, chef2, chef3;
        Table t = new Table();

        agent = new Thread(new Agent(t), Main.agent);
        chef1 = new Thread(new Chef(t, Ingredients.BREAD), Main.chef1);
        chef2 = new Thread(new Chef(t, Ingredients.PEANUT_BUTTER), Main.chef2);
        chef3 = new Thread(new Chef(t, Ingredients.JAM), Main.chef3);

        agent.start();
        chef1.start();
        chef2.start();
        chef3.start();
    }
}
