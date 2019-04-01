package com.mygdx.perceptron;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main extends ApplicationAdapter implements GestureDetector.GestureListener {

    // Integer hash map
    HashMap<String, Integer> integerHashMap;
    // Buttons hash map
    HashMap<String, Button> buttonHashMap;
    // Texts hash map
    HashMap<String, Text> textHashMap;
    // Application booleans hash map
    HashMap<String, Boolean> booleanHashMap;
    // Map with colors (k: Color name, v: RGBA values)
    HashMap<String, Color> colorHashMap;
    // Shape renderer: Used to draw rectangles, circles, lines, and so forth
    ShapeRenderer shapeRenderer;
    // Sprite batch: Used to draw sprites and texts
    SpriteBatch spriteBatch;
    // Font used to draw text on screen
    BitmapFont font;
    // The map is a 2D plane
    Plane2D map;
    // List to hold the white circles group
    ArrayList<Circle> whiteCircleList;
    // List to hold the black circles group
    ArrayList<Circle> blackCircleList;
    // Perceptron
    Perceptron perceptron;
    // Relevant circles to be drawn in the window
    // White and black circles generators
    // Circle at pointer, if the pointer is dragging any
    Circle whiteCircleGenerator, blackCircleGenerator, circleAtPointer;

    // Globals
    // GlyphLayout is used to calculate text placement
    public static GlyphLayout glyphLayout;

    // Constants
    // Map dimension
    final float MAP_X = 100;
    final float MAP_Y = 100;
    final float MAP_W = 520;
    final float MAP_H = 520;
    // Font size
    final int FONT_SIZE = 30;
    // Circle properties
    final float CIRCLE_SIZE = 22f;
    final float CIRCLE_SIZE_ON_MAP = 6f;
    // Interval between updates
    final float DELAY_TO_UPDATE_PERCEPTRON = 0.038f;
    // Theta would be threshold for the perceptron evaluation function
    final float THETA = 0;
    // Abstract minimum and maximum 2d plane map coordinates (-10, -9, -8, ..., 8, 9, 10)
    final int LOWER_LIMIT = -30;
    final int UPPER_LIMIT = 30;
    // Learning rate for perceptron
    final float LEARNING_RATE = 0.7f;

    // create() is called before render() and it is a method used to create and instantiate objects
    @Override
    public void create () {
        // Create and instantiate integer hash map
        integerHashMap = createIntegerHashMap();
        // Create and instantiate button hash map
        buttonHashMap = createButtonHashMap();
        // Create and instantiate color hash map
        colorHashMap = createColorHashMap();
        // Create and instantiate boolean hash map
        booleanHashMap = createBooleanHashMap();
        // Instantiate shape renderer
        shapeRenderer = new ShapeRenderer();
        // Instantiate sprite batch
        spriteBatch = new SpriteBatch();
        // Create font
        font = createFont("dungeonFont.ttf", FONT_SIZE);
        // Create map
        map = create2DPlane(MAP_X, MAP_Y, MAP_W, MAP_H);
        // Instantiate the lists for the two circle groups
        whiteCircleList = new ArrayList<Circle>();
        blackCircleList = new ArrayList<Circle>();
        // Instantiate the perceptron for the first time
        // THETA represents the neuron evaluate function threshold
        perceptron = new Perceptron(LEARNING_RATE, THETA, UPPER_LIMIT);
        // Create and define relevant window circles
        createRelevantWindowCircles();
        // Instantiate GlyphLayout
        // GlyphLayout is used to calculate text placement
        glyphLayout = new GlyphLayout();
        // Create and instantiate text hash map
        textHashMap = createTextHashMap();
        // Configure the window to use the gesture detector implemented on this class
        GestureDetector gestureDetector = new GestureDetector(this);
        Gdx.input.setInputProcessor(gestureDetector);
    }

    // Create and return a new text hash map
    HashMap createTextHashMap() {
        HashMap<String, Text> textHashMapInstance = new HashMap<String, Text>();
        // 2d plane origin (coordinate: 0, 0)
        textHashMapInstance.put("origin", new Text(new Point(map.getRelativeHalfWidth()-8, map.getRelativeHalfHeight()-15), "0", "LEFT"));
        // x axis limits
        textHashMapInstance.put("negativeXAxis", new Text(new Point(map.getMapBorders().getX()-7, map.getRelativeHalfHeight()-15), "-1", "LEFT"));
        textHashMapInstance.put("positiveXAxis", new Text(new Point(map.getRelativeFullWidth()+7, map.getRelativeHalfHeight()-15), "+1", ""));
        // y axis limits
        textHashMapInstance.put("negativeYAxis", new Text(new Point(map.getRelativeHalfWidth()-7, map.getMapBorders().getY()-15), "-1", "LEFT"));
        textHashMapInstance.put("positiveYAxis", new Text(new Point(map.getRelativeHalfWidth()-7, map.getRelativeFullHeight()+15), "+1", "LEFT"));
        // Perceptron values
        textHashMapInstance.put("perceptron", new Text(new Point(map.getRelativeFullWidth(), 20), "", "LEFT"));
        textHashMapInstance.put("perceptronLearningRate", new Text(new Point(map.getRelativeFullWidth(), 50), "", "LEFT"));
        return textHashMapInstance;
    }

    // Create and return a new integer hash map
    HashMap createIntegerHashMap() {
        HashMap<String, Integer> integerHashMapInstance = new HashMap<String, Integer>();
        // Current index for white circle list
        integerHashMapInstance.put("whiteListIndex", 0);
        // Current index for black circle list
        integerHashMapInstance.put("blackListIndex", 0);
        // Current index to alternate between lists: 0 for white circle list, 1 for black circle list
        integerHashMapInstance.put("listIndex", 0);
        // Used to check if perceptron has finished training, as no input will get error
        integerHashMapInstance.put("noErrorCounter", 0);
        return integerHashMapInstance;
    }

    // Create and return a new button hash map
    HashMap createButtonHashMap() {
        HashMap<String, Button> buttonHashMapInstance = new HashMap<String, Button>();
        buttonHashMapInstance.put("start", new Button(new Rectangle(100, 50, 100, 45), "start"));
        return buttonHashMapInstance;
    }

    // Create and return a new boolean hash map
    HashMap createBooleanHashMap() {
        HashMap<String, Boolean> booleanHashMapInstance = new HashMap<String, Boolean>();
        booleanHashMapInstance.put("isRunning", false);
        booleanHashMapInstance.put("isFinished", false);
        return booleanHashMapInstance;
    }

    // Create and return a new color hash map
    HashMap createColorHashMap() {
        HashMap<String, Color> colorHashMapInstance = new HashMap<String, Color>();
        colorHashMapInstance.put("WHITE", new Color(1, 1, 1, 1));
        colorHashMapInstance.put("BLACK", new Color(0, 0, 0, 1));
        colorHashMapInstance.put("BLUE", new Color(0, 0, 1, 1));
        colorHashMapInstance.put("GREEN", new Color(0, 1, 0, 1));
        return colorHashMapInstance;
    }

    // Create and define relevant window circles
    void createRelevantWindowCircles() {
        /* White and black circle generators, they are used to spawn circles when the pointer drags itself above them,
         and these circles can then be placed in the map */
        whiteCircleGenerator = new Circle(new Point(50, 150), CIRCLE_SIZE, -1, colorHashMap.get("WHITE"));
        blackCircleGenerator = new Circle(new Point(50, 250), CIRCLE_SIZE, 1, colorHashMap.get("BLACK"));
        // Circle at pointer starts as null, as there is no circle being dragged by the pointer when the application starts
        circleAtPointer = null;
    }

    // Create and return a 2D plane to draw
    public Plane2D create2DPlane(float initialX, float initialY, float planeWidth, float planeHeight) {
        Rectangle mapBorders = new Rectangle(initialX, initialY, planeWidth, planeHeight);
        Plane2D map = new Plane2D(mapBorders);
        return map;
    }

    // Create and return a new font
    public BitmapFont createFont(String fontName, int fontSize) {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(fontName));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = fontSize;
        fontParameter.color = colorHashMap.get("BLUE");
        return fontGenerator.generateFont(fontParameter);
    }

    // Render loop for the application window
    @Override
    public void render () {
        // Fill background with designated color
        fillBackground(colorHashMap.get("WHITE"));
        // Draw map and other shapes with a line (empty) shape type
        drawShapesLine(shapeRenderer);
        // Draw map and other shapes with filled shape type
        drawShapesFilled(shapeRenderer);
        // Draw all circles in the map
        drawAllCirclesInTheMap(shapeRenderer);
        // Draw circle that the pointer is dragging to the 2D Plane map
        drawCircleAtPointer(shapeRenderer);
        // Update texts on screen
        updateTexts();
        // Draw texts on screen
        drawTexts(spriteBatch, font);
        // Draw all button outlines
        drawButtonsOutline(shapeRenderer);
        // Draw all button texts
        drawButtonsText(spriteBatch, font);
        // Update perceptron
        updatePerceptron(map);
    }

    // Perceptron will only be updated after a certain interval defined by DELAY_TO_UPDATE_PERCEPTRON
    void updatePerceptron(Plane2D map) {
        // Increment perceptron timer
        perceptron.incrementTimeCounter(Gdx.graphics.getDeltaTime());
        // Check if perceptron time counter exceeds the time interval needed and if it has finished the training
        if(perceptron.getTimeCounter() >= DELAY_TO_UPDATE_PERCEPTRON && !perceptron.isFinished()) {
            // Update perceptron and its line
            map.calculatePerceptronLine(perceptron, LOWER_LIMIT, UPPER_LIMIT);
            // Adjust perceptron only by one input(circle) per interval
            if(booleanHashMap.get("isRunning")) {
                adjustPerceptron();
                perceptron.updateLearningRate();
            }
            // Check if perceptron training/learning has finished
            if(integerHashMap.get("noErrorCounter") >= whiteCircleList.size()+blackCircleList.size()+1 && booleanHashMap.get("isRunning")) {
                // End training
                perceptron.setFinished(true);
            }
            // Reset time to count until interval again
            perceptron.resetTimeCounter();
        }
    }

    // Adjust perceptron only by one input(circle) per interval
    void adjustPerceptron() {
        // listIndex: Current index to alternate between lists: 0 for white circle list, 1 for black circle list
        // 0: one input(circle) from the white list
        if(integerHashMap.get("listIndex") == 0) {
            if(whiteCircleList.size() > 0) {
                // Adjust perceptron weights based on the input(circle)
                // Also checks if the perceptron on input got any error to verify if the training has finished, true is there is error
                if(!perceptron.update(whiteCircleList.get(integerHashMap.get("whiteListIndex")), map)) {
                    integerHashMap.put("noErrorCounter", integerHashMap.get("noErrorCounter")+1);
                } else {
                    integerHashMap.put("noErrorCounter", 0);
                }
                // Increment white circle list index
                integerHashMap.put("whiteListIndex", integerHashMap.get("whiteListIndex") + 1);
                // If post-increment white circle list index is equal or high than the list size, we reset it to 0
                if (integerHashMap.get("whiteListIndex") >= whiteCircleList.size()) {
                    integerHashMap.put("whiteListIndex", 0);
                }
            }
            // Assures next perceptron adjustment will be made by a input from the black circle list
            integerHashMap.put("listIndex", 1);
            // 1: one input(circle) from the black list
        } else if(integerHashMap.get("listIndex") == 1 && blackCircleList.size() > 0) {
            if(blackCircleList.size() > 0) {
                // Adjust perceptron weights based on the input(circle)
                // Also checks if the perceptron on input got any error to verify if the training has finished, true is there is error
                if(!perceptron.update(blackCircleList.get(integerHashMap.get("blackListIndex")), map)) {
                    integerHashMap.put("noErrorCounter", integerHashMap.get("noErrorCounter")+1);
                } else {
                    integerHashMap.put("noErrorCounter", 0);
                }
                // Increment black circle list index
                integerHashMap.put("blackListIndex", integerHashMap.get("blackListIndex")+1);
                // If post-increment black circle list index is equal or high than the list size, we reset it to 0
                if(integerHashMap.get("blackListIndex") >= blackCircleList.size()) {
                    integerHashMap.put("blackListIndex", 0);
                }
            }
            // Assures next perceptron adjustment will be made by a input from the white circle list
            integerHashMap.put("listIndex", 0);
        }
    }

    void drawButtonsOutline(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for(Map.Entry<String, Button> entry : buttonHashMap.entrySet()) {
            buttonHashMap.get(entry.getKey()).draw_outline(shapeRenderer);
        }
        shapeRenderer.end();
    }

    void drawButtonsText(SpriteBatch spriteBatch, BitmapFont font) {
        spriteBatch.begin();
        for(Map.Entry<String, Button> entry : buttonHashMap.entrySet()) {
            buttonHashMap.get(entry.getKey()).draw_text(spriteBatch, font);
        }
        spriteBatch.end();
    }

    // Fill background with designated color
    void fillBackground(Color backgroundColor) {
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
    }

    // Draw map and other shapes with a line (empty) shape type
    void drawShapesLine(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        // Draw map
        map.draw(shapeRenderer, colorHashMap.get("BLACK"));
        // Draw white circle generator
        whiteCircleGenerator.draw(shapeRenderer);
        shapeRenderer.end();
    }

    // Draw map and other shapes with filled shape type
    void drawShapesFilled(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // Draw black circle generator
        blackCircleGenerator.draw(shapeRenderer);
        shapeRenderer.end();
    }

    // Draw circle at pointer
    void drawCircleAtPointer(ShapeRenderer shapeRenderer) {
        if(circleAtPointer == null) return;
        if(circleAtPointer.getColor().equals(Color.BLACK)) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            // Draw circle at pointer
            circleAtPointer.draw(shapeRenderer);
            shapeRenderer.end();
        } else if(circleAtPointer.getColor().equals(Color.WHITE)) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            // Draw circle at pointer
            circleAtPointer.draw(shapeRenderer);
            shapeRenderer.end();
        }
    }

    // Draw all white and black circles in the map
    void drawAllCirclesInTheMap(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // Draw all black circles
        for(Circle circle: blackCircleList) {
            circle.draw(shapeRenderer);
        }
        // Draw current circle being used as input in the perception training iteration
        shapeRenderer.setColor(colorHashMap.get("GREEN"));
        if(booleanHashMap.get("isRunning")) {
            if(integerHashMap.get("listIndex") == 0 && whiteCircleList.size() > 0) {
                shapeRenderer.circle(whiteCircleList.get(integerHashMap.get("whiteListIndex")).getCenter().getX(),
                        whiteCircleList.get(integerHashMap.get("whiteListIndex")).getCenter().getY(), CIRCLE_SIZE_ON_MAP);
            } else if(blackCircleList.size() > 0) {
                shapeRenderer.circle(blackCircleList.get(integerHashMap.get("blackListIndex")).getCenter().getX(),
                        blackCircleList.get(integerHashMap.get("blackListIndex")).getCenter().getY(), CIRCLE_SIZE_ON_MAP);
            }
        }
        shapeRenderer.setColor(colorHashMap.get("BLACK"));
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        // Draw all white circles
        for(Circle circle: whiteCircleList) {
            circle.draw(shapeRenderer);
        }
        shapeRenderer.end();
    }

    // If the pointer is dragging any circle, draw circle next to the pointer
    void updateCircleAtPointerPosition(float pointerX, float pointerY) {
        if(circleAtPointer == null) return;
        circleAtPointer.getCenter().setX(pointerX);
        circleAtPointer.getCenter().setY(pointerY);
    }

    // Draw texts on screen
    void drawTexts(SpriteBatch spriteBatch, BitmapFont font) {
        spriteBatch.begin();
        for(Map.Entry<String, Text> entry : textHashMap.entrySet()) {
            textHashMap.get(entry.getKey()).draw_text(spriteBatch, font);
        }
        spriteBatch.end();
    }

    // Update texts on screen
    void updateTexts() {
        // Update perceptron weights and bias
        textHashMap.get("perceptron").setText(String.format("w1: %.03f, w2: %.03f, b: %.03f", (perceptron.getW1()/UPPER_LIMIT), (perceptron.getW2()/UPPER_LIMIT), (perceptron.getB()/UPPER_LIMIT)));
        // Update perceptron learning height
        textHashMap.get("perceptronLearningRate").setText(String.format("Learning rate: %.03f", perceptron.getLearningRate()));
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        // The y axis is flipped in touchDown()
        y = Gdx.graphics.getHeight()-y;
        if(buttonHashMap.get("start").isPointerInsideButton(new Point(x, y))) {
            // Perceptron training is not running
            if(!booleanHashMap.get("isRunning")) {
                booleanHashMap.put("isRunning", true);
                buttonHashMap.get("start").setText("stop");
            // Perceptron training is running
            } else {
                booleanHashMap.put("isRunning", false);
                buttonHashMap.get("start").setText("start");
            }
        }
        return true;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        // The y axis is flippen in pan()
        y = Gdx.graphics.getHeight()-y;
        // true if pointer is dragging inside the white generator and the pointer is not already holding a circle
        if(circleAtPointer == null && isPointerInsideCircSpawner(new Point(x, y), whiteCircleGenerator)) {
            circleAtPointer = new Circle(new Point(x, y), CIRCLE_SIZE, -1, colorHashMap.get("WHITE"));
            //whiteCircleList.add(circleAtPointer);
        }
        // true if pointer is dragging inside the black generator and the pointer is not already holding a circle
        else if(circleAtPointer == null && isPointerInsideCircSpawner(new Point(x, y), blackCircleGenerator)) {
            circleAtPointer = new Circle(new Point(x, y), CIRCLE_SIZE, 1, colorHashMap.get("BLACK"));
            //blackCircleList.add(circleAtPointer);
        }
        updateCircleAtPointerPosition(x, y);
        return true;
    }

    // Return true if pointer is dragging (from pan() function)inside a spawner
    boolean isPointerInsideCircSpawner(Point pointerPosition, Circle circle) {
        if(euclidianDistanceByPoints(circle.getCenter(), pointerPosition) < circle.getRadius()*3f) {
            return true;
        }
        return false;
    }

    boolean isPointerInsideMap(Point pointerPosition, Plane2D map) {
        if(pointerPosition.getX() > map.getMapBorders().getX() &&
                pointerPosition.getX() < map.getRelativeFullWidth() &&
                pointerPosition.getY() > map.getMapBorders().getY() &&
                pointerPosition.getY() < map.getRelativeFullHeight()) {
            return true;
        }
        return false;
    }

    // Returns the euclidian distance between two points, using absolute coordinates as a source
    float euclidianDistanceByCoords(float x1, float y1, float x2, float y2) {
        return (float)(Math.sqrt(Math.pow(x1 - x2, 2) +  Math.pow(y1 - y2, 2)));
    }

    // Returns the euclidian distance between two points, using points as a source
    float euclidianDistanceByPoints(Point point1, Point point2) {
        return (float)(Math.sqrt(Math.pow(point1.getX() - point2.getX(), 2) +  Math.pow(point1.getY() - point2.getY(), 2)));
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        if(circleAtPointer != null && isPointerInsideMap(new Point(x, y), map)) {
            circleAtPointer.setRadius(CIRCLE_SIZE_ON_MAP);
            if(circleAtPointer.getColor().equals(Color.BLACK)) {
                blackCircleList.add(circleAtPointer);
            } else if(circleAtPointer.getColor().equals(Color.WHITE)) {
                whiteCircleList.add(circleAtPointer);
            }
        }
        circleAtPointer = null;
        return true;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }

    @Override
    public void dispose () {
        font.dispose();
        shapeRenderer.dispose();
    }
}
