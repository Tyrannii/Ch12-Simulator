
import java.util.List;
import java.util.Random;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2026
 */
public class Rabbit extends Animal
{
    // The age at which a rabbit can breed.
    private static final int BREEDING_AGE = 5;

    // The maximum age of a rabbit.
    private static final int MAX_AGE = 40;

    // The probability of breeding.
    private static final double BREEDING_PROBABILITY = 0.12;

    // The maximum number of offspring.
    private static final int MAX_LITTER_SIZE = 4;

    // Random number generator.
    private static final Random rand = Randomizer.getRandom();

    /**
     * Create a new rabbit.
     */
    public Rabbit(boolean randomAge, Field field, Location location)
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
     * Make the rabbit act during one simulation step.
     */
    public void act(List<Animal> newRabbits)
    {
        incrementAge();

        if(isAlive()) {
            giveBirth(newRabbits);

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
     * Give birth to new rabbits in free adjacent locations.
     */
    private void giveBirth(List<Animal> newRabbits)
    {
        Field field = getField();

        List<Location> free =
            field.getFreeAdjacentLocations(getLocation());

        int births = breed();

        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);

            Rabbit young = new Rabbit(false, field, loc);

            newRabbits.add(young);
        }
    }

    /**
     * Return the maximum age of a rabbit.
     */
    protected int getMaxAge()
    {
        return MAX_AGE;
    }

    /**
     * Return the breeding age of a rabbit.
     */
    protected int getBreedingAge()
    {
        return BREEDING_AGE;
    }

    /**
     * Return the breeding probability of a rabbit.
     */
    protected double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }

    /**
     * Return the maximum litter size of a rabbit.
     */
    protected int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
}
