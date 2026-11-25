import edu.princeton.cs.algs4.StdRandom;

import java.awt.*;
import java.util.Map;

public class Particle {
    public ParticleFlavor flavor;
    public int lifespan;

    public static final int PLANT_LIFESPAN = 150;
    public static final int FLOWER_LIFESPAN = 75;
    public static final int FIRE_LIFESPAN = 10;
    public static final Map<ParticleFlavor, Integer> LIFESPANS =
            Map.of(ParticleFlavor.FLOWER, FLOWER_LIFESPAN,
                   ParticleFlavor.PLANT, PLANT_LIFESPAN,
                   ParticleFlavor.FIRE, FIRE_LIFESPAN);

    public Particle(ParticleFlavor flavor) {
        this.flavor = flavor;
        if (LIFESPANS.containsKey(this.flavor)){
            lifespan = LIFESPANS.get(this.flavor);
        }else {
            lifespan = -1;
        }
    }

    public void decrementLifespan(){
        if (this.lifespan > 0){
            this.lifespan -=1;
        }if (this.lifespan == 0) {
            this.flavor = ParticleFlavor.EMPTY;
            this.lifespan = -1;
        }
    }

    public Color color() {
        if (flavor == ParticleFlavor.EMPTY) {
            return Color.BLACK;
        } else if (flavor == ParticleFlavor.SAND) {
            return Color.YELLOW;
        } else if (flavor == ParticleFlavor.WATER) {
            return Color.BLUE;
        } else if (flavor == ParticleFlavor.FOUNTAIN) {
            return Color.CYAN;
        } else if (flavor == ParticleFlavor.PLANT) {
            double ratio = (double) Math.max(0, Math.min(lifespan, PLANT_LIFESPAN)) / PLANT_LIFESPAN;
            int g = 120 + (int) Math.round((255 - 120) * ratio);
            return new Color(0, g, 0);
        } else if (flavor == ParticleFlavor.FIRE) {
            double ratio = (double) Math.max(0, Math.min(lifespan, FIRE_LIFESPAN)) / FIRE_LIFESPAN;
            int r = (int) Math.round(255 * ratio);
            return new Color(r, 0, 0);
        } else if (flavor == ParticleFlavor.FLOWER) {
            double ratio = (double) Math.max(0, Math.min(lifespan, FLOWER_LIFESPAN)) / FLOWER_LIFESPAN;
            int r = 120 + (int) Math.round((255 - 120) * ratio);
            int g = 70 + (int) Math.round((141 - 70) * ratio);
            int b = 80 + (int) Math.round((161 - 80) * ratio);
            return new Color(r, g, b);
        } else {
            return Color.GRAY;
        }
    }

    public void moveInto(Particle other) {
        other.flavor = this.flavor;
        other.lifespan = this.lifespan;

        this.flavor = ParticleFlavor.EMPTY;
        this.lifespan = -1;
    }

    public void fall(Map<Direction, Particle> neighbors) {
        Particle p = neighbors.get(Direction.DOWN);
        if (p.flavor == ParticleFlavor.EMPTY) {
            this.moveInto(p);
        }
    }

    public void flow(Map<Direction, Particle> neighbors) {
        int chance = StdRandom.uniformInt(3);
        Particle leftN = neighbors.get(Direction.LEFT);
        Particle rightN = neighbors.get(Direction.RIGHT);
        if (chance == 0){
            return;
        } if (chance == 1) {
            if (leftN.flavor == ParticleFlavor.EMPTY){
                this.moveInto(leftN);
            }
        } if (chance == 2) {
            if (rightN.flavor == ParticleFlavor.EMPTY){
                this.moveInto(rightN);
            }
        }
    }

    public void grow(Map<Direction, Particle> neighbors) {
        int chance = StdRandom.uniformInt(10);
        Particle upN = neighbors.get(Direction.UP);
        Particle leftN = neighbors.get(Direction.LEFT);
        Particle rightN = neighbors.get(Direction.RIGHT);
        if (chance == 0){
            if (upN.flavor == ParticleFlavor.EMPTY){
                upN.flavor = this.flavor;
                upN.lifespan = this.lifespan;
            }
        } else if (chance == 1) {
            if (leftN.flavor == ParticleFlavor.EMPTY){
                leftN.flavor = this.flavor;
                leftN.lifespan = this.lifespan;
            }
        } else if (chance == 2) {
            if (rightN.flavor == ParticleFlavor.EMPTY){
                rightN.flavor = this.flavor;
                rightN.lifespan = this.lifespan;
            }
        } else {
            return;
        }
    }

    public void burn(Map<Direction, Particle> neighbors) {
        for (Direction dir : neighbors.keySet()){
            int chance = StdRandom.uniformInt(10);
            if (neighbors.get(dir).flavor == ParticleFlavor.FLOWER || neighbors.get(dir).flavor == ParticleFlavor.PLANT){
                if (chance < 4){
                    neighbors.get(dir).flavor = ParticleFlavor.FIRE;
                    neighbors.get(dir).lifespan = FIRE_LIFESPAN;
                }
            }
        }
    }

    public void action(Map<Direction, Particle> neighbors) {
        if (this.flavor == ParticleFlavor.EMPTY || this.flavor == ParticleFlavor.BARRIER){
            return;
        } if (this.flavor == ParticleFlavor.WATER){
            this.fall(neighbors);
            this.flow(neighbors);
        } if (this.flavor == ParticleFlavor.PLANT || this.flavor == ParticleFlavor.FLOWER){
            this.fall(neighbors);
            this.grow(neighbors);
        } if (this.flavor == ParticleFlavor.FIRE){
            this.fall(neighbors);
            this.burn(neighbors);
        }if (this.flavor != ParticleFlavor.BARRIER){
            this.fall(neighbors);
        }
    }
}