package ru.yandex.praktikum.order;

import service.IngredientsResponse;

import java.util.ArrayList;
import java.util.Random;

public class OrderRandomData {

    private ArrayList<String> data = new ArrayList<>();
    private Random random = new Random();
    private static int INGREDIENTS_COUNT = 10;

    public ArrayList <String> createRandomOrder(IngredientsResponse ingredients){
        int n = 1 + random.nextInt(INGREDIENTS_COUNT -1);
        for (int i = 0; i < n; i++) {
            int ingIndex = random.nextInt(INGREDIENTS_COUNT);
            data.add(ingredients.getData().get(ingIndex).getId());
        }
        return data;
    }
}
