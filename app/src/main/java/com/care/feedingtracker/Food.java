package com.care.feedingtracker;

import java.io.Serializable;

public class Food implements Serializable {
    public enum Food_Type {
        Solid, Milk, None
    }

    private Food_Type type;
    private int volume;
    private String food_name;


    public Food () {
        type = Food_Type.None;
        volume = 0;
        food_name = "";
    }

    public Food (int v) {
        type = Food_Type.Milk;
        volume = v;
        food_name = "";
    }

    public Food (String fn) {
        type = Food_Type.Solid;
        food_name = fn;
        volume = 0;
    }

    public Food_Type getType() {
        return type;
    }

    public int getVolume() {
        return volume;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public void setType(Food_Type type) {
        this.type = type;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {

        StringBuilder b = new StringBuilder("Food: ");

        if (type == Food_Type.Milk) {
            b.append(volume);
            b.append("ml Milk");
        } else if (type == Food_Type.Solid) {
            b.append(food_name + " Solid");
        }

        return b.toString();
    }
}
