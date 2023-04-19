package com.cellauto;

public class Map {
    private final Cell [][] presentMap;
    private final Cell [][] futureMap;
    private final int cellSize;

    public Map(int width, int height, int size) {
        cellSize = size;
        presentMap = new Cell [height][width];
        for (int line = 0; line < presentMap.length; line++) {
            for (int cell = 0; cell < presentMap[line].length; cell++) {
                presentMap[line][cell] = new Cell("A");
            }
        }

        futureMap = new Cell [height][width];
        for (int line = 0; line < futureMap.length; line++) {
            for (int cell = 0; cell < futureMap[line].length; cell++) {
                futureMap[line][cell] = new Cell("A");
            }
        }
    }

    public void setCell(int line, int cell, boolean isLife) {
        presentMap[line][cell].setLife(isLife);
    }

    public void update() {
        for (int line = 1; line < presentMap.length - 1; line++) {
            for (int cell = 1; cell < presentMap[line].length - 1; cell++) {

                int count = 0;
                for (int j = line - 1; j < line + 2; j++) {
                    for (int i = cell - 1; i < cell + 2; i++) {
                        if (presentMap[j][i].getLife()) count++;
                    }
                }

                if (presentMap[line][cell].getLife()) {
                    count--;
                    futureMap[line][cell].setLife(count == 2 || count == 3);
                } else {
                    futureMap[line][cell].setLife(count == 3);
                }
            }
        }

        for (int line = 0; line < presentMap.length; line++) {
            for (int cell = 0; cell < presentMap[line].length; cell++) {
                presentMap[line][cell].setLife(futureMap[line][cell].getLife());
            }
        }
    }

    public int getCellSize() {
        return cellSize;
    }

    public Cell[][] getPresentMap() {
        return presentMap;
    }

    public int getWidthSize() {
        return presentMap[0].length * cellSize;
    }

    public int getHeightSize() {
        return presentMap.length * cellSize;
    }
}
