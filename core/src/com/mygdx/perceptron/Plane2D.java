package com.mygdx.perceptron;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Plane2D {
    Rectangle mapBorders;
    float relativeHalfWidth, relativeHalfHeight;
    float relativeFullWidth, relativeFullHeight;

    public Plane2D(Rectangle mapBorders) {
        // Rectangle dictates map borders
        this.mapBorders = mapBorders;
        // Relative position to be in the middle of the plane in terms of width or height
        relativeHalfWidth = this.mapBorders.getX() + (this.mapBorders.getWidth()/2);
        relativeHalfHeight = this.mapBorders.getY() + (this.mapBorders.getHeight()/2);
        // Relative position to have crossed the plane in terms of width or height
        relativeFullWidth = this.mapBorders.getX() + this.mapBorders.getWidth();
        relativeFullHeight = this.mapBorders.getY() + this.mapBorders.getHeight();
    }

    public Rectangle getMapBorders() {
        return mapBorders;
    }

    public void setMapBorders(Rectangle mapBorders) {
        this.mapBorders = mapBorders;
    }

    // Draw the 2D plane on screen
    public void draw(ShapeRenderer shapeRenderer, Color color) {
        // Set color to draw
        shapeRenderer.setColor(color);
        // Draw map borders
        shapeRenderer.rect(mapBorders.getX(), mapBorders.getY(), mapBorders.getWidth(), mapBorders.getHeight());
        //Draw x axis (horizontal line)
        shapeRenderer.line(mapBorders.getX(), relativeHalfHeight, relativeFullWidth, relativeHalfHeight);
        //Draw y axis (vertical line)
        shapeRenderer.line(relativeHalfWidth, mapBorders.getY(), relativeHalfWidth, relativeFullHeight);
    }
}
