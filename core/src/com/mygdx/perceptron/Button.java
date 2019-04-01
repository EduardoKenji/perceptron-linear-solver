package com.mygdx.perceptron;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Button {
    Rectangle buttonBorders;
    String text;
    float relativeFullWidth, relativeFullHeight;

    public Button(Rectangle buttonBorders, String text) {
        this.buttonBorders = buttonBorders;
        this.text = text;
        // Relative positions to be in the top or right side of the button
        relativeFullWidth = this.buttonBorders.getX() + this.buttonBorders.getWidth();
        relativeFullHeight = this.buttonBorders.getY() + this.buttonBorders.getHeight();
    }

    public Rectangle getButtonBorders() {
        return buttonBorders;
    }

    public void draw_outline(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(buttonBorders.getX(), buttonBorders.getY(), buttonBorders.getWidth(), buttonBorders.getHeight());
    }

    public void draw_text(SpriteBatch spriteBatch, BitmapFont font) {
        // Glyphlayout is used to help calculating the text width and the text height, with which the text can be centered in the button
        Main.glyphLayout.setText(font, text);
        font.draw(spriteBatch, text, buttonBorders.getX()+(buttonBorders.getWidth()/2)-(Main.glyphLayout.width/2),
                buttonBorders.getY()+(buttonBorders.getHeight()/2)+(Main.glyphLayout.height/2));
    }

    public boolean isPointerInsideButton(Point pointerPosition) {
        if(pointerPosition.getX() > buttonBorders.getX() &&
                pointerPosition.getX() < relativeFullWidth &&
                pointerPosition.getY() > buttonBorders.getY() &&
                pointerPosition.getY() < relativeFullHeight) {
            return true;
        }
        return false;
    }

    public void setButtonBorders(Rectangle buttonBorders) {
        this.buttonBorders = buttonBorders;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
