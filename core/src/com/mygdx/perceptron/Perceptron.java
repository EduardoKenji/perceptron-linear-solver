package com.mygdx.perceptron;

public class Perceptron {
    // Two weights and bias
    float w1, w2, b;
    // Learning speed
    float learningRate;
    // Time counter used to update the perceptron
    float timeCounter;
    // Threshold will be used to evaluate neuron functions
    float threshold;
    // Graphic limit for perceptron, variable will also be used for approximation
    float limit;
    // Boolean to check if perceptron has finished learning
    boolean finished;


    public Perceptron(float learningRate, float threshold, float limit) {
        /* Weights and bias are randomized between -1 and 1 (except if x is between -0.001 and 0.001,
         as the interval exceeds the graphic limits, then it needs to be randomized again
        */
        this.w1 = calculateValidWeightOrBias(limit);
        this.w2 = calculateValidWeightOrBias(limit);
        this.b = calculateValidWeightOrBias(limit);
        this.limit = limit;

        this.learningRate = learningRate;
        this.threshold = threshold;
        timeCounter = 0f;
        finished = false;
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

    // Adjust perceptron weight and bias
    boolean update(Circle perceptronInput, Plane2D map) {
        // The sum will be later evaluated by the neuron function
        float sum = 0;
        // Relative coordinates are the coordinates of the circle according to the placement in % inside the map (ex.: (40, 10) of a 100x100 pixels map)
        // Abstract coordinates are the coordinates of the circle according to the abstract lower and upper limits of x and y axes (ex.: (40, 10) of a -100 to 100 abstract map limit)
        float relativeCircleMapX = (perceptronInput.getCenter().getX()-map.getMapBorders().getX())/(map.getMapBorders().getWidth());
        float abstractCircleMapX = (relativeCircleMapX - 0.5f)*2*this.limit;
        float relativeCircleMapY = (perceptronInput.getCenter().getY()-map.getMapBorders().getY())/(map.getMapBorders().getHeight());
        float abstractCircleMapY = (relativeCircleMapY - 0.5f)*2*this.limit;
        // Increment sum with inputs * weights and bias
        sum += abstractCircleMapX * w1;
        sum += abstractCircleMapY * w2;
        sum += b;
        // Get evaluate output
        float evaluateOutput = evaluate(sum, threshold);
        // Compute error
        float error = perceptronInput.getExpected() - evaluateOutput;
        // Adjust weights and bias, if error != 0
        boolean hasError = adjustNeuron(error, abstractCircleMapX, abstractCircleMapY);
        // hasError: Used to check if perceptron has finished training, as no input will get error
        return hasError;
    }

    boolean adjustNeuron(float error, float abstractX, float abstractY) {
        if(error != 0) {
            w1 = w1 + (learningRate * error * abstractX);
            w2 = w2 + (learningRate * error * abstractY);
            b = b + (learningRate * error);
            return true;
        }
        return false;
    }

    float evaluate(float sum, float threshold) {
        if(sum >= 0) {
            return 1;
        }
        return -1;
    }

    // Currently, decrease learning rate with time
    void updateLearningRate() {
        learningRate -= 0.002;
        if(learningRate < 0.1f) {
            learningRate = 0.1f;
        }
    }

    public void setTimeCounter(float timeCounter) {
        this.timeCounter = timeCounter;
    }

    public float getThreshold() {
        return threshold;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
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

    public float getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(float learningRate) {
        this.learningRate = learningRate;
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

    public float getLimit() {
        return limit;
    }

    public void setLimit(float limit) {
        this.limit = limit;
    }
}
