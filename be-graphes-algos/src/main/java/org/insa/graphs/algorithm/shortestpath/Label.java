package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;

public class Label implements Comparable<Label>{
    // Doit contenir :
    /*
     * Sommet courant : int ou Sommet(Node)
     * Marque : float bool
     * Coût : int
     * Père : Sommet(Node)
     */
    protected Node courant;
    protected boolean marque;
    protected double coutRevise;
    protected Node pere;

    public Label(Node courant,boolean marque,double coutRevise, Node pere){
        this.courant=courant;
        this.marque = marque;
        this.coutRevise=coutRevise;
        this.pere=pere;
    }

     //Getters dont une méthode int getCost()
    public double getCost(){
        return this.coutRevise;
    }

    public boolean getMarque(){
        return this.marque;
    }

    public Node getPere(){
        return this.pere;
    }

    public Node getCourant(){
        return this.courant;
    }

    public double getTotalCost(){
        return this.getCost();
    }

    @Override
    public int compareTo(Label arg0) {
        if (this.getTotalCost()>arg0.getTotalCost()){
            return 1;
        } else if (this.getTotalCost()==arg0.getTotalCost()){
            return 0;
        }else {
            return -1;
        }
    }

    public Node compareNode(Label L1, Label L2){
        if (L1.coutRevise>L2.coutRevise){
            return L1.courant;
        }
        return L2.courant;
    }

    // Associer un Label à chaque Node sans modifier la classe Node 


}
