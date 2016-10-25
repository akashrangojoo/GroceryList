package edu.niu.cs.akash.grocerylist;

/**
 * Created by  on 5/4/2016.
 */
public class Grocery_Item {
    private int id;
    private String description;
    private int isDone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }
}
