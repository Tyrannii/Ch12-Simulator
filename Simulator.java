
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator containing
 * rabbits, foxes, and mice.
 *
 * Mice are herbivores that reproduce and move
 * independently. Foxes only hunt rabbits.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2026
 */
public class Simulator
{
    // Default dimensions of the field.
    private static final int DEFAULT_WIDTH = 120;
    private static final int DEFAULT_DEPTH = 80;

    // Probability of creating each animal.
    private static final double FOX_CREATION_PROBABILITY = 0.02;
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;
    private static final double MOUSE_CREATION_PROBABILITY = 0.06;

    // Animals currently in the simulation.
    private List<Animal> animals;

    // The simulation field.
    private Field field;

    // Current simulation step.
    private int step;

    // Graphical display.
    private SimulatorView view;

    /**
     * Construct a simulation with default dimensions.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Construct a simulation with specified dimensions.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        animals = new ArrayList<>();
        field = new Field(depth, width);

        // Set the display colors.
        view = new SimulatorView(depth, width);
        view.setColor(Rabbit.class, Color.ORANGE);
        view.setColor(Fox.class, Color.BLUE);
        view.setColor(Mouse.class, Color.GREEN);

        reset();
    }

    /**
     * Run the simulation for 4000 steps.
     */
    public void runLongSimulation()
    {
        simulate(4000);
    }

    /**
     * Run the simulation for a given number of steps.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1;
            step <= numSteps && view.isViable(field);
            step++) {

            simulateOneStep();
        }
    }

    /**
     * Run one step of the simulation.
     */
    public void simulateOneStep()
    {
        step++;

        List<Animal> newAnimals = new ArrayList<>();

        for(Iterator<Animal> it = animals.iterator();
            it.hasNext(); ) {

            Animal animal = it.next();
            animal.act(newAnimals);

            if(!animal.isAlive()) {
                it.remove();
            }
        }

        // Add newborn animals.
        animals.addAll(newAnimals);

        // Update the display.
        view.showStatus(step, field);
    }

    /**
     * Reset the simulation.
     */
    public void reset()
    {
        step = 0;
        animals.clear();
        populate();

        view.showStatus(step, field);
    }

    /**
     * Randomly populate the field with foxes,
     * rabbits, and mice.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();

        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {

                Location location = new Location(row, col);

                if(rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Fox fox = new Fox(true, field, location);
                    animals.add(fox);
                }
                else if(rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Rabbit rabbit = new Rabbit(true, field, location);
                    animals.add(rabbit);
                }
                else if(rand.nextDouble() <= MOUSE_CREATION_PROBABILITY) {
                    Mouse mouse = new Mouse(true, field, location);
                    animals.add(mouse);
                }
            }
        }
    }

    /**
     * Pause the simulation for a given time.
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch(InterruptedException ie) {
            // Wake up.
        }
    }
}
