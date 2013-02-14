#include "../head"
package lh.koneke.games.Atomique;

public class Entity {
	Sprite s;
	public void setSprite(Sprite s) {
		this.s = s;
	}
	public Sprite getSprite() {
		return this.s;
	}

	boolean centered;
	public void setCentered(boolean centered) { this.centered = centered; }
	public boolean getCentered() { return centered; }

	fptr draw;
	fptr update;

	Rectangle rectangle;
	public void setRectangle(Rectangle r) {
		this.rectangle = r;
	}
	public Rectangle getRectangle() {
		return this.rectangle;
	}

	public Entity(Rectangle r) {
		draw = f();
		update = f();
		rectangle = r;
		killed = false;
	}
	public Entity() {
		draw = f();
		update = f();
		rectangle = new Rectangle(0,0,32,32);
		killed = false;
	}

	double rotation = 0;
	public void setRotation(double d) { this.rotation = d; }
	public double getRotation() { return this.rotation; }

	boolean killed;
	public boolean getKilled() { return killed; }
	public void kill() { killed = true; }
}
