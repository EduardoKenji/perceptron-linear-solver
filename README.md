# perceptron-linear-solver

A visual representation of a perceptron being trained to linearly separate two distinct groups of points on a 2D plane. The project is being developed with Java and LibGDX framework. 

The perceptron is a binary classifier, it is a supervised machine learning algorithm. The perceptron is also a linear classifier, which means the algorithm can only solve linearly separable problem.

* The Java code and all classes are contained in [core/src/com/mygdx/perceptron](https://github.com/EduardoKenji/perceptron-linear-solver/tree/master/core/src/com/mygdx/perceptron).

Currently in the application:
* The perceptron starts with randomized weights and bias (between -1 and 1).
* There is a convoluted algorithm to create a 2D plane map, calculate and then draw the line representing the perceptron's weights (W1 and W2) and bias (B) as a line crossing a 2D plane. 
* The line equation is (w1 * x) + (w2 * y) + b = 0 <=> (w2 * y) = (-w1 * x) - b
* There are two groups of circles, the white circles (with expected value as -1) and the black circles (with expected value as 1). Each circle will have its coordinates used as input for the perceptron. The perceptron will eventually acquire a valid configuration to separate these groups.
* The current threshold for the evaluation function is 0. The learning rate starts at 0.7 to acelerate the initial iterations, but it will steadily decrease (each iteration) to accurately determine a valid configuration.

<img src="pictures/perceptron_training.gif" width="500">
