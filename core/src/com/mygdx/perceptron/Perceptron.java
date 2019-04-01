package com.mygdx.perceptron;

public class Perceptron {
    // Two weights and bias
    float w1, w2, b;
    // Learning speed
    float learningSpeed;
    // Time counter used to update the perceptron
    float timeCounter;

    public Perceptron(float learningSpeed, float limit) {
        /* Weights and bias are randomized between -1 and 1 (except if x is between -0.001 and 0.001,
         as the interval exceeds the graphic limits, then it needs to be randomized again
        */

        /*

        this.w1 = 0.37f;
        this.w2 = -0.90f;
        this.b = 0.91f;

        this.w1 = calculateValidWeightOrBias(limit);
        this.w2 = calculateValidWeightOrBias(limit);
        this.b = calculateValidWeightOrBias(limit);

        this.w1 = -0.93f;
        this.w2 = -0.26f;
        this.b = 0.93f;

        this.w1 = 0.471f;
        this.w2 = 0.035f;
        this.b = 0.385f;

        */

        this.w1 = calculateValidWeightOrBias(limit);
        this.w2 = calculateValidWeightOrBias(limit);
        this.b = calculateValidWeightOrBias(limit);

        this.learningSpeed = learningSpeed;
        timeCounter = 0f;
    }

    /* Weights and bias are randomized between -1 and 1 (except if x is between -(1/limit) and 1/limit,
     as the interval exceeds the graphic limits, then it needs to be randomized again
    */
    float calculateValidWeightOrBias(float limit) {
        float value = (float) ((Math.random() * 2) - 1);
        while(value < 1/limit && value > -(1/limit)) {
            value = (float) ((Math.random() * 2) - 1);
        }
        return value;
    }


    public float getW1() {
        return w1;
    }

    public void setW1(float w1) {
        this.w1 = w1;
    }

    public float getW2() {
        return w2;
    }

    public void setW2(float w2) {
        this.w2 = w2;
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
    }

    public float getLearningSpeed() {
        return learningSpeed;
    }

    public void setLearningSpeed(float learningSpeed) {
        this.learningSpeed = learningSpeed;
    }

    public void resetTimeCounter() {
        timeCounter = 0f;
    }

    public void incrementTimeCounter(float increment) {
        timeCounter += increment;
    }

    public float getTimeCounter() {
        return timeCounter;
    }
}
