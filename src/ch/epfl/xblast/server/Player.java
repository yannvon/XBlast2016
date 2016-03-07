package ch.epfl.xblast.server;

import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.SubCell;

/**
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class Player {
 
    /**
     *
     */
    public static final class LifeState{
        
        /**
         *
         */
        public enum State{
            INVULNERABLE,VULNERABLE,DYING,DEAD;
            
        }
        
        //ATTRIBUTES
        private int lives;
        private State state;
        
        /**
         * @param lives
         * @param State
         * @throws IllegalArgumentException
         * @throws NullPointerException
         */
        public LifeState(int lives, State State){
            this.lives= ArgumentChecker.requireNonNegative(lives);
            this.state =Objects.requireNonNull(state);
        }

        /**
         * @return
         */
        public int lives() {
            return lives;
        }

        /**
         * @return
         */
        public State state() {
            return state;
        }
        
        /**
         * @return
         */
        public boolean canMove(){
            switch(state){
            case VULNERABLE:
            case INVULNERABLE:
                return true;
            default:
                return false;
            }
        }
        
        
    }

    /**
     *
     */
    public static final class DirectedPosition{


        /**
         * @param p
         * @return
         */
        public static Sq<DirectedPosition> stopped(DirectedPosition p){
            return Sq.constant(p);
        }
        
        /**
         * @param p
         * @return
         */
        public static Sq<DirectedPosition> moving(DirectedPosition p){
            return Sq.iterate(p,pos->new DirectedPosition(pos.position.neighbor(pos.direction),pos.direction));
        }
        
        //ATTRIBUTES
        private SubCell position;
        private Direction direction;
       

        /**
         * @param position
         * @param direction
         * @throws NullPointerException
         */
        public DirectedPosition(SubCell position, Direction direction) {
            this.position=Objects.requireNonNull(position);
            this.direction=Objects.requireNonNull(direction);
        }
        
        /**
         * @return
         */
        public SubCell position() {
            return position;
        }

        /**
         * @return
         */
        public Direction direction() {
            return direction;
        }

        /**
         * @param newPosition
         * @return
         */
        public DirectedPosition withPosition(SubCell newPosition){
            return new DirectedPosition(newPosition,direction);
        }
        /**
         * @param newDirection
         * @return
         */
        public DirectedPosition withDirection(Direction newDirection){
            return new DirectedPosition(position,newDirection);
        }
    }
        


}