# perceptron-linear-solver

A visual representation of a perceptron being trained to linearly separate two distinct groups of points on a 2D plane. The project is being developed with Java and LibGDX framework. 

The perceptron is a binary classifier, it is a supervised machine learning algorithm. The perceptron is also a linear classifier, which means the algorithm can only solve linearly separable problem.

Currently, the application contains:
* A convoluted algorithm to create a 2D plane map, calculate and then draw the line representing the perceptron's weights (W1 and W2) and bias (B) as a line crossing a 2D plane.
* There are two groups of circles, the white circles and the black circles. Each circle will have its coordinates used as input for the perceptron. The perceptron will eventually acquire a valid configuration to separate these groups.
* The current threshold for the evaluation function is 0. The learning rate starts at 0.7 to acelerate the initial iterations, but it will steadily decrease (each iteration) to accurately determine a valid configuration.

<img src="pictures/perceptron_training.gif" width="500">
