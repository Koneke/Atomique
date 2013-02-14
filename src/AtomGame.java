#include "../head"

package lh.koneke.games.Atomique;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import java.util.List;
import java.util.ArrayList;
import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;
import net.java.games.input.Version;

public class AtomGame {

	public static final int ScrW = 640;
	public static final int ScrH = 480;
	public static final int SpriteLim = 256;

	List<Entity> entities;
	List<Entity> modifiedEntities;
	Entity CE; //current entity
	ControllerEnvironment controllerEnvironment;
	Controller[] controllers;
	AtomController[] acontrollers;
	
	public void setup() {
		try {
			Display.setDisplayMode(new DisplayMode(ScrW,ScrH));
			Display.create();
		}
		catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}	

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0,ScrW,ScrH,0,1,-1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		entities = new ArrayList<Entity>();
		
		controllerEnvironment = ControllerEnvironment.getDefaultEnvironment();
		controllers = controllerEnvironment.getControllers();
		acontrollers = new AtomController[controllers.length];
		for(int i = 0;i<controllers.length;i++) {
			acontrollers[i] = new AtomController(controllers[i].getName());
			acontrollers[i].setController(controllers[i]);
		}
	}

	public void init() {
		Entity e = new Entity();
		e.setSprite(Sprite.getSprite("res/testship.png"));
		e.getRectangle().X = ScrW/2;
		e.getRectangle().Y = ScrH/2;
		e.update = f(
			double angle = Math.atan2(
				acontrollers[0].getValue("y"),
				acontrollers[0].getValue("x"));
			double velocity = Math.sqrt(
				Math.pow(acontrollers[0].getValue("x"),2)+
				Math.pow(acontrollers[0].getValue("y"),2));
			velocity = Math.floor(velocity*10)/10;
			double deadzone = .2;
			if(velocity <= deadzone) { velocity = 0f; }

			CE.getRectangle().X+=Math.cos(angle)*.4*Time.dt*velocity;
			CE.getRectangle().Y+=Math.sin(angle)*.4*Time.dt*velocity;

			double aimangle = Math.PI+Math.atan2(
				acontrollers[0].getValue("ry"),
				acontrollers[0].getValue("rx"));
			velocity = Math.sqrt(
				Math.pow(acontrollers[0].getValue("rx"),2)+
				Math.pow(acontrollers[0].getValue("ry"),2));
			velocity = Math.floor(velocity*10)/10;
			if(velocity > deadzone) { CE.rotation=aimangle; }

			if(acontrollers[0].getValue("z") >= .7f) {
				Entity b = new Entity(
					new Rectangle(
						CE.getRectangle().X-16,
						CE.getRectangle().Y-16,
						4, 4));
				b.setSprite(Sprite.getSprite("res/bullet.png"));
				b.rotation = CE.rotation-Math.PI;
				b.update = f( 
					double speed = .8;
					CE.getRectangle().X+=Math.cos(CE.rotation)*speed*Time.dt;	
					CE.getRectangle().Y+=Math.sin(CE.rotation)*speed*Time.dt;	
					if(CE.getRectangle().X > ScrW || CE.getRectangle().X < 0 ||
						CE.getRectangle().Y > ScrH || CE.getRectangle().Y < 0) {
						modifiedEntities.remove(CE);
					}
				);
				modifiedEntities.add(b);	
			}
		);
		e.setCentered(true);
		entities.add(e);
	}

	public void update() {
		Time.update();
		for(int i = 0;i<acontrollers.length;i++) {
			acontrollers[i].update();
		}
		modifiedEntities = new ArrayList<Entity>(entities);
		for(Entity e : entities) {
			CE = e;
			e.update.call();
		}
		entities = new ArrayList<Entity>(modifiedEntities);
	}

	public void draw() {
		GL11.glClear(
			GL11.GL_COLOR_BUFFER_BIT |
			GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glColor3f(1f,1f,1f);

		for(Entity e : entities) {
			CE = e;
			e.getSprite().draw(e.getRectangle(),e.getCentered(),e.getRotation());
			e.draw.call();
		}

		Display.update();
	}

	public void start() {
		setup();
		init();

		while(!Display.isCloseRequested()) {
			update();
			draw();
		}

		Display.destroy();
	}

	public static void main(String[] args) {
		AtomGame game = new AtomGame();
		game.start();
	}
}
