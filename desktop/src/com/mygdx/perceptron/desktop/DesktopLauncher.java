package com.mygdx.perceptron.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.perceptron.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		// Window width and height
		config.height = 720;
		config.width = 720;
		new LwjglApplication(new Main(), config);
	}
}
