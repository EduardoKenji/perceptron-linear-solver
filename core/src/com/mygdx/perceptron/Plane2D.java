package com.mygdx.perceptron;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Plane2D {
    // Rectangle dictates map borders
    Rectangle mapBorders;
    // Relative position to be in the middle of the plane in terms of width or height
    // Relative position to have crossed the plane in terms of width or height
    float relativeHalfWidth, relativeHalfHeight;
    float relativeFullWidth, relativeFullHeight;
    // Line that represents the perceptron
    Point perceptronPoint1, perceptronPoint2;

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
        // If there is a perceptron line, draw it
        if(perceptronPoint1 != null && perceptronPoint2 != null) {
            shapeRenderer.line(perceptronPoint1.getX(), perceptronPoint1.getY(), perceptronPoint2.getX(), perceptronPoint2.getY());
        }
    }

    void calculatePerceptronLine(Perceptron perceptron, int lowerLimit, int upperLimit) {
        // Reset, recalculate and update perceptron line
        perceptronPoint1 = null;
        perceptronPoint2 = null;
        updatePerceptronLine(perceptron, lowerLimit, upperLimit);
    }

    void updatePerceptronLine(Perceptron perceptron, int lowerLimit, int upperLimit) {

        // Get perceptron weights and bias
        float w1 = perceptron.getW1();
        float w2 = perceptron.getW2();
        float b = perceptron.getB();
        // Calculate map abstract unit
        float mapUnitX = (relativeFullWidth - mapBorders.getX())/(upperLimit*2);
        float mapUnitY = (relativeFullWidth - mapBorders.getX())/(upperLimit*2);

        // If y factor is lower than 1 in a equation, we multiply all other members of the equation by (1/y_factor)
        float yMultiplier = calculateYMultiplier(w2);
        // Calculate expected X for Y = limit
        float expectedXForPositiveLimit = (- upperLimit - (b*yMultiplier))/(w1*yMultiplier);
        createPerceptronLinePointByExpX(expectedXForPositiveLimit, upperLimit, mapUnitX, mapUnitY, "X");
        // Calculate expected X for Y = -limit
        float expectedXForNegativeLimit = (- lowerLimit - (b*yMultiplier))/(w1*yMultiplier);
        createPerceptronLinePointByExpX(expectedXForNegativeLimit, lowerLimit, mapUnitX, mapUnitY, "X");
        // Calculate expected Y for X = limit
        float expectedYForPositiveLimit = - (b*yMultiplier) - (w1*yMultiplier)*upperLimit;
        createPerceptronLinePointByExpX(expectedYForPositiveLimit, upperLimit, mapUnitX, mapUnitY, "Y");
        // Calculate expected Y for X = -limit
        float expectedYForNegativeLimit = - (b*yMultiplier) - (w1*yMultiplier)*lowerLimit;
        createPerceptronLinePointByExpX(expectedYForNegativeLimit, lowerLimit, mapUnitX, mapUnitY, "Y");
    }

    float calculateYMultiplier(float yFactor) {
        float yMultiplier = 1/yFactor;
        return yMultiplier;
    }

    // Create point using expected X for Y = limit
    void createPerceptronLinePointByExpX(float expectedValue, float limit, float mapUnitX, float mapUnitY, String axis) {
        float lowerLimit = 0, upperLimit = 0;
        float xOffset = 0, yOffset = 0;
        if(limit < 0) {
            lowerLimit = limit;
            upperLimit = limit*(-1);
        } else {
            lowerLimit = limit*(-1);
            upperLimit = limit;
        }
        if(axis.equals("X")) {
            xOffset = expectedValue;
            yOffset = limit;
        } else if(axis.equals("Y")){
            xOffset = limit;
            yOffset = expectedValue;
        }
        if(expectedValue >= lowerLimit && expectedValue <= upperLimit) {
            // Perceptron line point 1 is not defined
            if(perceptronPoint1 == null) {
                perceptronPoint1 = new Point(relativeHalfWidth+(xOffset*mapUnitX), relativeHalfHeight+(yOffset*mapUnitY));
                // Perceptron line point 2 is not defined
            } else if(perceptronPoint2 == null){
                perceptronPoint2 = new Point(relativeHalfWidth+(xOffset*mapUnitX), relativeHalfHeight+(yOffset*mapUnitY));
            }
        }
    }

    public float getRelativeHalfWidth() {
        return relativeHalfWidth;
    }

    public void setRelativeHalfWidth(float relativeHalfWidth) {
        this.relativeHalfWidth = relativeHalfWidth;
    }

    public float getRelativeHalfHeight() {
        return relativeHalfHeight;
    }

    public void setRelativeHalfHeight(float relativeHalfHeight) {
        this.relativeHalfHeight = relativeHalfHeight;
    }

    public float getRelativeFullWidth() {
        return relativeFullWidth;
    }

    public void setRelativeFullWidth(float relativeFullWidth) {
        this.relativeFullWidth = relativeFullWidth;
    }

    public float getRelativeFullHeight() {
        return relativeFullHeight;
    }

    public void setRelativeFullHeight(float relativeFullHeight) {
        this.relativeFullHeight = relativeFullHeight;
    }
}
