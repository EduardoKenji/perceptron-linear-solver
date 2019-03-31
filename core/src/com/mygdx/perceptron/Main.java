package com.mygdx.perceptron;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends ApplicationAdapter implements GestureDetector.GestureListener {

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
	// Application booleans hash map
	HashMap<String, Boolean> booleanHashMap;
	// Relevant circles to be drawn in the window
	// White and black circles generators
	// Circle at pointer, if the pointer is dragging any
	Circle whiteCircleGenerator, blackCircleGenerator, circleAtPointer;

	// Constants
	final float CIRCLE_SIZE = 22f;
	final float CIRCLE_SIZE_ON_MAP = 8.8f;
	final float DELAY_TO_UPDATE_PERCEPTRON = 0.5f;
	// Theta would be threshold for the perceptron evaluation function
	final float THETA = 0;

	float testX, testY, testValue;

	// create() is called before render() and it is a method used to create and instantiate objects
	@Override
	public void create () {
		// Create and instantiate color hash map
		colorHashMap = createColorHashMap();
		// Create and instantiate boolean hash map
		booleanHashMap = createBooleanHashMap();
		// Instantiate shape renderer
		shapeRenderer = new ShapeRenderer();
		// Instantiate sprite batch
		spriteBatch = new SpriteBatch();
		// Create font
		int fontSize = 30;
		font = createFont("dungeonFont.ttf", fontSize);
		// Create map
		float initialX = 100;
		float initialY = 100;
		float planeWidth = 520;
		float planeHeight = 520;
		map = create2DPlane(initialX, initialY, planeWidth, planeHeight);
		// Instantiate the lists for the two circle groups
		whiteCircleList = new ArrayList<Circle>();
		blackCircleList = new ArrayList<Circle>();
		// Instantiate the perceptron for the first time
		// Weights and bias are randomized between -1 and 1
		float w1 = (float) ((Math.random() * 2) - 1);
		float w2 = (float) ((Math.random() * 2) - 1);
		float b = (float) ((Math.random() * 2) - 1);
		// Default learning speed for the perceptron
		float learningSpeed = 0.01f;
		perceptron = new Perceptron(w1, w2, b, learningSpeed);
		// Create and define relevant window circles
		createRelevantWindowCircles();
		// Configure the window to use the gesture detector implemented on this class
		GestureDetector gestureDetector = new GestureDetector(this);
		Gdx.input.setInputProcessor(gestureDetector);
	}

	// Create and return a new color map
	public HashMap createBooleanHashMap() {
		HashMap<String, Boolean> booleanHashMapInstance = new HashMap<String, Boolean>();
		booleanHashMapInstance.put("is_running", false);
		return booleanHashMapInstance;
	}

	// Create and return a new color map
	public HashMap createColorHashMap() {
		HashMap<String, Color> colorHashMapInstance = new HashMap<String, Color>();
		colorHashMapInstance.put("WHITE", new Color(1, 1, 1, 1));
		colorHashMapInstance.put("BLACK", new Color(0, 0, 0, 1));
		colorHashMapInstance.put("BLUE", new Color(0, 0, 1, 1));
		return colorHashMapInstance;
	}

	// Create and define relevant window circles
	void createRelevantWindowCircles() {
		/* White and black circle generators, they are used to spawn circles when the pointer drags itself above them,
		 and these circles can then be placed in the map */
		whiteCircleGenerator = new Circle(new Point(50, 150), CIRCLE_SIZE, -2, colorHashMap.get("WHITE"));
		blackCircleGenerator = new Circle(new Point(50, 250), CIRCLE_SIZE, -2, colorHashMap.get("BLACK"));
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
		// Draw texts on screen
		drawTexts(spriteBatch, font);
		// Update perceptron
		update_perceptron();
	}

	public void update_perceptron() {
		perceptron.incrementTimeCounter(Gdx.graphics.getDeltaTime());
		if(perceptron.getTimeCounter() >= DELAY_TO_UPDATE_PERCEPTRON) {
			// Update perceptron and its line
			perceptron.resetTimeCounter();
		}
	}

	// Fill background with designated color
	public void fillBackground(Color backgroundColor) {
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

	void drawAllCirclesInTheMap(ShapeRenderer shapeRenderer) {
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		// Draw all black circles
		for(Circle circle: blackCircleList) {
			circle.draw(shapeRenderer);
		}
		shapeRenderer.end();
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		// Draw all white circles
		for(Circle circle: whiteCircleList) {
			circle.draw(shapeRenderer);
		}
		shapeRenderer.end();
	}

	void updateCircleAtPointerPosition(float pointerX, float pointerY) {
		if(circleAtPointer == null) return;
		circleAtPointer.getCenter().setX(pointerX);
		circleAtPointer.getCenter().setY(pointerY);
	}

	// Draw texts on screen
	void drawTexts(SpriteBatch spriteBatch, BitmapFont font) {
		spriteBatch.begin();
		font.draw(spriteBatch, "test: "+testX+", "+testY+" "+testValue, 100, 90);
		spriteBatch.end();
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
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
		y = Gdx.graphics.getHeight()-y;
		// true if pointer is dragging inside the white generator and the pointer is not already holding a circle
		if(circleAtPointer == null && isPointerInsideCircSpawner(new Point(x, y), whiteCircleGenerator)) {
			circleAtPointer = new Circle(new Point(x, y), CIRCLE_SIZE, -2, colorHashMap.get("WHITE"));
			//whiteCircleList.add(circleAtPointer);
		}
		// true if pointer is dragging inside the black generator and the pointer is not already holding a circle
		else if(circleAtPointer == null && isPointerInsideCircSpawner(new Point(x, y), blackCircleGenerator)) {
			circleAtPointer = new Circle(new Point(x, y), CIRCLE_SIZE, -2, colorHashMap.get("BLACK"));
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
