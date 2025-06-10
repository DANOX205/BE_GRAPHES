package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;

public class LabelStar extends Label implements Comparable<Label> {
    protected double estimation;

    public LabelStar(Node courant,boolean marque,double coutRevise, Node pere, double estimation){
        super(courant,marque,coutRevise,pere);
        this.estimation = estimation;
    }

    public double getEstimation(){
        return this.estimation;
    }

    @Override
    public double getTotalCost(){
        return this.getCost() + this.getEstimation();
    }
    
}