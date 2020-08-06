package com.rytispetrauskas;

import java.util.ArrayList;

class Vertex implements Comparable<Vertex> {
    private int x;

    private int degree;

    ArrayList<Vertex> nbrs = new ArrayList<>();

    int getX() {
        return x;
    }

    void setX(int x) {
        this.x = x;
    }

    ArrayList<Vertex> getNbrs() {
        return nbrs;
    }

    boolean addNbr(Vertex y) {
        if(this.nbrs.contains(y)){
            return false;
        } else {
            this.nbrs.add(y);
            if (!y.getNbrs().contains(y)) {
                y.getNbrs().add(this);
                y.degree++;
            }
            this.degree++;
            return true;
        }
    }

    @Override
    public int compareTo(Vertex o) {
        return Integer.compare(this.degree, o.degree);
    }

    public String toString() {
        return "" + x;
    }
}
