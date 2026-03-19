package models;

public abstract class Base {
    protected int id;

    public Base(int id) {
        this.id = id;
    }

    public abstract int getId();

    public abstract void setId(int id);

}
