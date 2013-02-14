package lh.koneke.games.Atomique;
import java.util.HashMap;
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
 public Entity(String name, Rectangle r) {
  draw = new fptr(){public void call(){}};
  update = new fptr(){public void call(){}};
  rectangle = r;
  killed = false;
  this.name = name;
  vars = new HashMap<String, Double>();
 }
 public Entity(String name) {
  draw = new fptr(){public void call(){}};
  update = new fptr(){public void call(){}};
  rectangle = new Rectangle(0,0,32,32);
  killed = false;
  this.name = name;
  vars = new HashMap<String, Double>();
 }
 public Entity clone() {
  Entity e = new Entity(this.name);
  e.setSprite(getSprite());
  e.setCentered(getCentered());
  e.draw = draw;
  e.update = update;
  e.setRectangle(new Rectangle(
   getRectangle().X,
   getRectangle().Y,
   getRectangle().W,
   getRectangle().H
  ));
  e.vars = new HashMap<String, Double>(vars);
  e.rotation = rotation;
  e.killed = killed;
  return e;
 }
 double rotation = 0;
 public void setRotation(double d) { this.rotation = d; }
 public double getRotation() { return this.rotation; }
 boolean killed;
 public boolean getKilled() { return killed; }
 public void kill() { killed = true; }
 String name;
 HashMap<String, Double> vars;
 public double getVar(String s) {
 try {
 return vars.get(s); }
 catch (Exception e) {
   System.out.println(name);
   System.out.println(s);
   for(String ss : vars.keySet())
   {
    System.out.print(ss+" ");
    System.out.println(vars.get(ss));
   }
  }
  System.exit(0);
  return 0;
 }
 public void setVar(String s, double d) { vars.put(s, d); }
}
