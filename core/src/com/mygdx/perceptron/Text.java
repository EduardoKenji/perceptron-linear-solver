package com.mygdx.perceptron;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Text {
    Point origin;
    String text;
    String placement;

    public Text(Point origin, String text, String placement) {
        this.origin = origin;
        this.text = text;
        this.placement = placement;
    }

    void draw_text(SpriteBatch spriteBatch, BitmapFont font) {
        // Glyphlayout is used to help calculating the text width and the text height, with which the text can be centered in the button
        Main.glyphLayout.setText(font, text);
        if(placement.equals("LEFT")) {
            font.draw(spriteBatch, text, origin.getX()-(Main.glyphLayout.width), origin.getY()+(Main.glyphLayout.height/2));
        } else if(placement.equals("RIGHT")) {
            font.draw(spriteBatch, text, origin.getX()+(Main.glyphLayout.width), origin.getY()+(Main.glyphLayout.height/2));
        } else {
            font.draw(spriteBatch, text, origin.getX(), origin.getY()+(Main.glyphLayout.height/2));
        }

    }

    public Point getOrigin() {
        return origin;
    }

    public void setOrigin(Point origin) {
        this.origin = origin;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPlacement() {
        return placement;
    }

    public void setPlacement(String placement) {
        this.placement = placement;
    }
}
