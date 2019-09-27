package com.mobiquityinc.packer;

public class Item {

    private float weight;
    private float cost;
    private int index;

    Item(int index, float weight, float cost) {
        this.weight = weight;
        this.cost = cost;
        this.index = index;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}