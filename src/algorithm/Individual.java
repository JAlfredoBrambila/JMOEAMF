/**
 * Java multi-Objective Evolutionary Algorithm Mini Framework (JMOEAMF)
 * Clase: Individual.java
 * Paquete: algorithm
 * Info: Representa a un individuo
 * @version: 0.7
 * @autor: José Alfredo Brambila Hernámdez <alfredo.brambila@outlook.com>
 * @autor: Miguel Angel Garcia Morales <talivan12@hotmail.com>
 * @autor: Hector Fraire Huacuja <hector.fraire2014@gmail.com>
 * Proyecto Biblioteca de clases para JMOEAMF 
 * Desarrollo: Enero de 2022
 * Actualización: abril de 2022
 * Se permite el uso total o parcial de este código fuente siempre y cuando se le dé el crédito correspondiente a los autores
 *
 */

package algorithm;

import java.util.LinkedList;

/**
 *
 * @author J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 */
public class Individual {
    
    private double[] position;
    private double[] cost;
    private double g; // MOEA/D
    private int rank;
    private LinkedList<Integer> dominationSet;
    private int dominatedCount;
    private double crowdingDistance;
    private int index;
    private boolean isDominated; // MOEA/D

    public Individual() {
        dominationSet = new LinkedList<Integer>();
        rank = 0;
        dominatedCount = 0;
        crowdingDistance = 0;
        index = -1;
    }
    
    public void copy(Individual a) {
        this.position = new double[a.getPosition().length];
        for(int i=0; i<a.getPosition().length; i++) {
            this.position[i] = a.getPosition()[i];
        }
        
        this.cost = new double[a.getCost().length];
        for(int i=0; i<a.getCost().length; i++) {
            this.cost[i] = a.getCost()[i];
        }  
    }
    
    public void clone(Individual a) {
        this.position = new double[a.getPosition().length];
        for(int i=0; i<a.getPosition().length; i++) {
            this.position[i] = a.getPosition()[i];
        }
        
        this.cost = new double[a.getCost().length];
        for(int i=0; i<a.getCost().length; i++) {
            this.cost[i] = a.getCost()[i];
        } 
        
        this.rank = a.rank;
        for(Integer ii : a.dominationSet) {
            this.dominationSet.addLast(ii);
        }
        dominatedCount = a.dominatedCount;
        crowdingDistance = a.crowdingDistance;
        index = a.index;
        g = a.g;
        isDominated = a.isDominated;
    }
    
    @Override
    public String toString() {
        String s = "";
        for(int i=0; i<this.position.length; i++) {
            s += this.position[i] + " ";
        }
        s += "| ";
        for(int i=0; i<this.cost.length; i++) {
            s += "F" + (i+1) + ": " + this.cost[i] + " ";
        }
        
        return s;
    }
    
    public String printInfo() {
        String s = "";
        for(int i=0; i<this.position.length; i++) {
            s += this.position[i] + " ";
        }
        s += "| ";
        for(int i=0; i<this.cost.length; i++) {
            s += "F" + (i+1) + ": " + this.cost[i] + " ";
        }
        
        s += "g: " + g + " isDominated: " + isDominated;
        
        return s;
    }
    
    public void addTodominationSet(int val) {
        if(dominationSet != null) {
            this.dominationSet.addLast(val);
        } else {
            System.out.println("No dominationSet created... ");
        }
    }
    
    public void addTodominationSetFromList(LinkedList<Integer> l) {
        for(Integer v : l) {
            this.dominationSet.addLast(v);
        }
    }
    
    public void dominatedCountIncrement() {
        this.dominatedCount++;
    }
    
    public void dominatedCountDecrement() {
        this.dominatedCount--;
    }
    
    
    public Individual(int nPos, int nCos) {
        this.position = new double[nPos];
        this.cost = new double[nCos];
        dominationSet = new LinkedList<Integer>();
        index = -1;
    }
    
    public void copyConfigAtributes(Individual a) {
        try {
            this.position = new double[a.getPosition().length];
            this.cost = new double[a.getCost().length];
        } catch(Exception e) {
            System.out.println(">>> Error: " + e);
        }
        
    }
    
    public void setCost(int i, double value) {
        this.cost[i] = value;
    }
    
    public void setPosition(int i, double value) {
        this.position[i] = value;
    }
    
    /**
     * @return the position
     */
    public double[] getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(double[] position) {
        this.position = position;
    }

    /**
     * @return the cost
     */
    public double[] getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(double[] cost) {
        this.cost = cost;
    }

    /**
     * @return the rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * @param rank the rank to set
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * @return the dominationSet
     */
    public LinkedList<Integer> getDominationSet() {
        return dominationSet;
    }

    /**
     * @param dominationSet the dominationSet to set
     */
    public void setDominationSet(LinkedList<Integer> dominationSet) {
        this.dominationSet = dominationSet;
    }

    /**
     * @return the dominatedCount
     */
    public int getDominatedCount() {
        return dominatedCount;
    }

    /**
     * @param dominatedCount the dominatedCount to set
     */
    public void setDominatedCount(int dominatedCount) {
        this.dominatedCount = dominatedCount;
    }

    /**
     * @return the crowdingDistance
     */
    public double getCrowdingDistance() {
        return crowdingDistance;
    }

    /**
     * @param crowdingDistance the crowdingDistance to set
     */
    public void setCrowdingDistance(double crowdingDistance) {
        this.crowdingDistance = crowdingDistance;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @return the g
     */
    public double getG() {
        return g;
    }

    /**
     * @param g the g to set
     */
    public void setG(double g) {
        this.g = g;
    }

    /**
     * @return the isDominated
     */
    public boolean isIsDominated() {
        return isDominated;
    }

    /**
     * @param isDominated the isDominated to set
     */
    public void setIsDominated(boolean isDominated) {
        this.isDominated = isDominated;
    }
    
}
