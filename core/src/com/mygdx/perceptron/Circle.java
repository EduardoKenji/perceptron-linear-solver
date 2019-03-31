package com.mygdx.perceptron;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Circle {
    Point center;
    float radius;
    int expected;
    Color color;

    // If expected is -2, the circle is inapplicable to the 2D plane map (it is not a point in the map)
    public Circle(Point center, float radius, int expected, Color color) {
        this.center = center;
        this.radius = radius;
        this.expected = expected;
        this.color = color;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(center.getX(), center.getY(), radius);
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getExpected() {
        return expected;
    }

    public void setExpected(int expected) {
        this.expected = expected;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }
}
