package lh.koneke.games.Atomique;

public class Rectangle {
	public float X;
	public float Y;
	public float W;
	public float H;
	
	public Rectangle(float x, float y, float w, float h) {
		X = x; Y = y; W = w; H = h;
	}
	public float centerX() {
		return X+W/2;
	}
	public float centerY() {
		return Y+H/2;
	}
	public void setSize(Vector2 size) {
		this.W = size.X;
		this.H = size.Y;
	}
}
