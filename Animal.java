
import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 * Animals can age, breed, move, and die.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2026
 */
public abstract class Animal
{
    // Whether the animal is alive.
    private boolean alive;

    // The animal's age.
    private int age;

    // The animal's field.
    private Field field;

    // The animal's location.
    private Location location;

    // Shared random number generator.
    private static final Random rand = Randomizer.getRandom();

    /**
     * Create a new animal at a location in a field.
     */
    public Animal(Field field, Location location)
    {
        alive = true;
        age = 0;
        this.field = field;
        setLocation(location);
    }

    /**
     * Make the animal act during one simulation step.
     */
    public abstract void act(List<Animal> newAnimals);

    /**
     * Check whether the animal is alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }

    /**
     * Mark the animal as dead and remove it from the field.
     */
    protected void setDead()
    {
        alive = false;

        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the animal's location.
     */
    protected Location getLocation()
    {
        return location;
    }

    /**
     * Move the animal to a new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }

        location = newLocation;
        field.place(this, newLocation);
    }

    /**
     * Return the animal's field.
     */
    protected Field getField()
    {
        return field;
    }

    /**
     * Return the animal's age.
     */
    protected int getAge()
    {
        return age;
    }

    /**
     * Set the animal's age.
     */
    protected void setAge(int newAge)
    {
        age = newAge;
    }

    /**
     * Increase the animal's age.
     * The animal dies if it exceeds its maximum age.
     */
    protected void incrementAge()
    {
        age++;

        if(age > getMaxAge()) {
            setDead();
        }
    }

    /**
     * Check whether the animal is old enough to breed.
     */
    protected boolean canBreed()
    {
        return age >= getBreedingAge();
    }

    /**
     * Calculate how many offspring the animal produces.
     */
    protected int breed()
    {
        int births = 0;

        if(canBreed() &&
           rand.nextDouble() <= getBreedingProbability()) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }

        return births;
    }

    /**
     * Return the maximum age for this animal.
     */
    protected abstract int getMaxAge();

    /**
     * Return the breeding age for this animal.
     */
    protected abstract int getBreedingAge();

    /**
     * Return the breeding probability for this animal.
     */
    protected abstract double getBreedingProbability();

    /**
     * Return the maximum litter size for this animal.
     */
    protected abstract int getMaxLitterSize();
}
