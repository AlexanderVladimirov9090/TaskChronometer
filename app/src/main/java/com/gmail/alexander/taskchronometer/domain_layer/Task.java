package com.gmail.alexander.taskchronometer.domain_layer;

import java.io.Serializable;

/**
 * Created by:
 *
 * @author Alexander Vladimirov
 * <alexandervladimirov1902@gmail.com>
 * This is the domain class of Task that is represented in the database.
 */
public class Task implements Serializable {
    public static final long serialVersionUID = 29161120L;
    private long id;
    private final String name;
    private final String description;
    private final int sortOrder;

    public Task(long id, String name, String description, int sortOrder) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.sortOrder = sortOrder;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", sortOrder=" + sortOrder +
                '}';
    }
}
