package fr.univtln.pierre.samples;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

public class Inputbinding {
	private final InputManager inputManager;
	private final ActionListener actionListener;

	public Inputbinding(InputManager inputManager, ActionListener actionListener) {
		this.inputManager = inputManager;
		this.actionListener = actionListener;
	}

	public void menuBindings() {
		inputManager.addMapping("MENU_UP", new KeyTrigger(KeyInput.KEY_UP));
		inputManager.addMapping("MENU_DOWN", new KeyTrigger(KeyInput.KEY_DOWN));
		inputManager.addMapping("SELECT", new KeyTrigger(KeyInput.KEY_RETURN));
		inputManager.addMapping("CHANGE_PLAYERS", new KeyTrigger(KeyInput.KEY_U));
		inputManager.addMapping("BACK_OR_QUIT", new KeyTrigger(KeyInput.KEY_ESCAPE));

		inputManager.addListener(actionListener,
				"MENU_UP",
				"MENU_DOWN",
				"SELECT",
				"CHANGE_PLAYERS",
				"BACK_OR_QUIT");
	}
}
