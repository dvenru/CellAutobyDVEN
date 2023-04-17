package com.cellauto;

public class Map {
    private int [][] presentMap;
    private final int [][] futureMap;
    private final int cellSize;

    public Map(int width, int height, int size) {
        cellSize = size;
        presentMap = new int [(int) height / size][(int) width / size];
        futureMap = new int [(int) height / size][(int) width / size];
    }

    public void setCell(int line, int cell, boolean isLife) {
        presentMap[line][cell] = isLife ? 1 : 0;
    }

    public void update() {
        for (int line = 1; line < presentMap.length - 1; line++) {
            for (int cell = 1; cell < presentMap[line].length - 1; cell++) {

                int count = 0;
                for (int j = line - 1; j < line + 2; j++) {
                    for (int i = cell - 1; i < cell + 2; i++) {
                        if (presentMap[j][i] == 1) count++;
                    }
                }

                if (presentMap[line][cell] == 1) {
                    count--;
                    futureMap[line][cell] = count == 2 || count == 3 ? 1 : 0;
                } else {
                    futureMap[line][cell] = count == 3 ? 1 : 0;
                }
            }
        }

        for (int line = 0; line < presentMap.length; line++) presentMap[line] = futureMap[line].clone();
    }

    public int getCellSize() {
        return cellSize;
    }

    public int[][] getPresentMap() {
        return presentMap;
    }

    public int[][] getFutureMap() {
        return futureMap;
    }
}
