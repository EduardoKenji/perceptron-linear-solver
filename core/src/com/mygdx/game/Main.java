package com.mygdx.game;

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

import java.util.HashMap;

public class Main extends ApplicationAdapter implements GestureDetector.GestureListener {
	// Map with colors (k: Color name, v: RGBA values)
	HashMap<String, Color> colorMap;
	// Shape renderer: Used to draw rectangles, circles, lines, and so forth
	ShapeRenderer shapeRenderer;
	// Sprite batch: Used to draw sprites and texts
	SpriteBatch spriteBatch;
	// Font used to draw text on screen
	BitmapFont font;
	// The map is a 2D plane
	Plane2D map;
	
	@Override
	public void create () {
		// Create and instantiate color map
		colorMap = createColorMap();
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
	}

	public Plane2D create2DPlane(float initialX, float initialY, float planeWidth, float planeHeight) {
		Rectangle mapBorders = new Rectangle(initialX, initialY, planeWidth, planeHeight);
		Plane2D map = new Plane2D(mapBorders);
		return map;
	}

	public HashMap createColorMap() {
		colorMap = new HashMap<String, Color>();
		colorMap.put("WHITE", new Color(1, 1, 1, 1));
		colorMap.put("BLACK", new Color(0, 0, 0, 1));
		colorMap.put("BLUE", new Color(0, 0, 1, 1));
		return colorMap;
	}

	// Create font
	public BitmapFont createFont(String fontName, int fontSize) {
		FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(fontName));
		FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameter.size = fontSize;
		fontParameter.color = colorMap.get("BLUE");
		return fontGenerator.generateFont(fontParameter);
	}

	@Override
	public void render () {
		// Fill background with designated color
		fillBackground(colorMap.get("WHITE"));
		// Draw map and other shapes
		drawShapes(shapeRenderer);
	}

	// Fill background with designated color
	public void fillBackground(Color backgroundColor) {
		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
	}

	void drawShapes(ShapeRenderer shapeRenderer) {
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		map.draw(shapeRenderer, colorMap.get("BLACK"));
		shapeRenderer.end();
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
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
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
