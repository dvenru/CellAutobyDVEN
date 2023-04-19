package com.cellauto;

public class Cell {
    private Boolean isLife = false;
    private String name;

    public Cell(String name) {
        this.name = name;
    }

    public void setLife(Boolean life) {
        isLife = life;
    }

    public Boolean getLife() {
        return isLife;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}


