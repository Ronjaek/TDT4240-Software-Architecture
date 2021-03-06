package no.birdygolf.gruppe19.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import no.birdygolf.gruppe19.component.PhysicsComponent;
import no.birdygolf.gruppe19.factory.WorldFactory;
import no.birdygolf.gruppe19.level.Level;

public class LevelSystem extends EntitySystem {
    private static final Family family = Family.all(PhysicsComponent.class).get();
    private final WorldFactory worldFactory;
    private ImmutableArray<Entity> entities;

    public LevelSystem(WorldFactory worldFactory) {
        this.worldFactory = worldFactory;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        entities = getEngine().getEntitiesFor(family);
    }

    /**
     * Clears current level and generates a new one.
     *
     * @param level The new level.
     */
    public void initializeLevel(Level level) {

        while (entities.size() > 0) {
            entities.forEach(entity -> getEngine().removeAllEntities());
        }

        worldFactory.createLevel(level);
    }
}
