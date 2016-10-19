package io.whitegoldlabs.bias.models;

public class Item
{
    private int id;
    private String name;
    private boolean crossed;

    public Item() {}

    public int getId() {return this.id;}
    public String getName() {return this.name;}
    public boolean isCrossed() {return this.crossed;}

    public void setName(String newName) {this.name = newName;}
    public void setId(int newId) {this.id = newId;}

    public boolean toggleCrossed()
    {
        this.crossed = !this.crossed;
        return this.crossed;
    }
}
