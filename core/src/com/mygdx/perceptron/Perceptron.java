package com.mygdx.perceptron;

public class Perceptron {
    // Two weights and bias
    float w1, w2, b;
    // Learning speed
    float learningSpeed;
    // Time counter used to update the perceptron
    float timeCounter;

    public Perceptron(float w1, float w2, float b, float learningSpeed) {
        this.w1 = w1;
        this.w2 = w2;
        this.b = b;
        this.learningSpeed = learningSpeed;
        timeCounter = 0f;
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
