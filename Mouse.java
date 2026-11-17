
import java.util.List;
import java.util.Random;

/**
 * A simple model of a mouse.
 * Mice age, move, breed, and die.
 *
 * Assumptions:
 * - Mice are herbivores and do not hunt other animals.
 * - Mice move to a random free adjacent location.
 * - Mice can reproduce after reaching age 3.
 * - Mice can live for a maximum of 25 steps.
 * - Mice have a 15% chance of breeding each step.
 * - Mice can produce up to 5 offspring at once.
 * - Mice die if they become too old or overcrowded.
 * - Foxes do not eat mice in this version.
 *
 * @author Colten Aguilar
 * @version 2026
 */
public class Mouse extends Animal
{
    // The age at which a mouse can breed.
    private static final int BREEDING_AGE = 3;

    // The maximum age of a mouse.
    private static final int MAX_AGE = 25;

    // The probability of breeding.
    private static final double BREEDING_PROBABILITY = 0.15;

    // The maximum number of offspring.
    private static final int MAX_LITTER_SIZE = 5;

    // Random number generator.
    private static final Random rand = Randomizer.getRandom();

    /**
     * Create a new mouse.
     */
    public Mouse(boolean randomAge, Field field, Location location)
    {
        super(field, location);

        if(randomAge) {
            setAge(rand.nextInt(MAX_AGE));
        }
        else {
            setAge(0);
        }
    }

    /**
     * Make the mouse act during one simulation step.
     */
    public void act(List<Animal> newMice)
    {
        incrementAge();

        if(isAlive()) {
            giveBirth(newMice);

            Location newLocation =
                getField().freeAdjacentLocation(getLocation());

            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                setDead();
            }
        }
    }

    /**
     * Give birth to new mice in free adjacent locations.
     */
    private void giveBirth(List<Animal> newMice)
    {
        Field field = getField();

        List<Location> free =
            field.getFreeAdjacentLocations(getLocation());

        int births = breed();

        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);

            Mouse young = new Mouse(false, field, loc);

            newMice.add(young);
        }
    }

    /**
     * Return the maximum age of a mouse.
     */
    protected int getMaxAge()
    {
        return MAX_AGE;
    }

    /**
     * Return the breeding age of a mouse.
     */
    protected int getBreedingAge()
    {
        return BREEDING_AGE;
    }

    /**
     * Return the breeding probability of a mouse.
     */
    protected double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }

    /**
     * Return the maximum litter size of a mouse.
     */
    protected int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
}
