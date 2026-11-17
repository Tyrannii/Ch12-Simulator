
import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a fox.
 * Foxes age, move, eat rabbits, and die.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2026
 */
public class Fox extends Animal
{
    // The age at which a fox can breed.
    private static final int BREEDING_AGE = 15;

    // The maximum age of a fox.
    private static final int MAX_AGE = 150;

    // The probability of breeding.
    private static final double BREEDING_PROBABILITY = 0.08;

    // The maximum number of offspring.
    private static final int MAX_LITTER_SIZE = 2;

    // The food value of a rabbit.
    private static final int RABBIT_FOOD_VALUE = 9;

    // Random number generator.
    private static final Random rand = Randomizer.getRandom();

    // The fox's current food level.
    private int foodLevel;

    /**
     * Create a new fox.
     */
    public Fox(boolean randomAge, Field field, Location location)
    {
        super(field, location);

        if(randomAge) {
            setAge(rand.nextInt(MAX_AGE));
            foodLevel = rand.nextInt(RABBIT_FOOD_VALUE);
        }
        else {
            setAge(0);
            foodLevel = RABBIT_FOOD_VALUE;
        }
    }

    /**
     * Make the fox act during one simulation step.
     */
    public void act(List<Animal> newFoxes)
    {
        incrementAge();
        incrementHunger();

        if(isAlive()) {
            giveBirth(newFoxes);

            Location newLocation = findFood();

            if(newLocation == null) {
                newLocation =
                    getField().freeAdjacentLocation(getLocation());
            }

            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                setDead();
            }
        }
    }

    /**
     * Increase hunger.
     * The fox dies if it runs out of food.
     */
    private void incrementHunger()
    {
        foodLevel--;

        if(foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Look for rabbits in adjacent locations.
     */
    private Location findFood()
    {
        Field field = getField();

        List<Location> adjacent =
            field.adjacentLocations(getLocation());

        Iterator<Location> it = adjacent.iterator();

        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);

            if(animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;

                if(rabbit.isAlive()) {
                    rabbit.setDead();
                    foodLevel = RABBIT_FOOD_VALUE;
                    return where;
                }
            }
        }

        return null;
    }

    /**
     * Give birth to new foxes in free adjacent locations.
     */
    private void giveBirth(List<Animal> newFoxes)
    {
        Field field = getField();

        List<Location> free =
            field.getFreeAdjacentLocations(getLocation());

        int births = breed();

        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);

            Fox young = new Fox(false, field, loc);

            newFoxes.add(young);
        }
    }

    /**
     * Return the maximum age of a fox.
     */
    protected int getMaxAge()
    {
        return MAX_AGE;
    }

    /**
     * Return the breeding age of a fox.
     */
    protected int getBreedingAge()
    {
        return BREEDING_AGE;
    }

    /**
     * Return the breeding probability of a fox.
     */
    protected double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }

    /**
     * Return the maximum litter size of a fox.
     */
    protected int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
}
