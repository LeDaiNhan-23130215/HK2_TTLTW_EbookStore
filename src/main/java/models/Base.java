package models;

import java.io.Serializable;

public abstract class Base implements Serializable {
    protected int id;

    public Base(int id) {
        this.id = id;
    }

    public abstract int getId();

    public abstract void setId(int id);

}
