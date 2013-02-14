package lh.koneke.games.Atomique;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
import java.util.Comparator;
import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;
import net.java.games.input.Version;
import org.newdawn.slick.Color;
public class AtomGame {
 public static final int ScrW = 640;
 public static final int ScrH = 480;
 public static final int SpriteLim = 256;
 List<Entity> entities;
 List<Entity> modifiedEntities;
 Entity CE;
 ControllerEnvironment controllerEnvironment;
 Controller[] controllers;
 AtomController[] acontrollers;
 Entity player;
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
  final Random random = new Random();
  Entity e = new Entity("player");
  e.setSprite(Sprite.getSprite("res/testship.png"));
  e.getRectangle().X = ScrW/2;
  e.getRectangle().Y = ScrH/2;
  e.update = new fptr(){public void call(){double angle = Math.atan2( acontrollers[0].getValue("y"), acontrollers[0].getValue("x")); double velocity = Math.sqrt( Math.pow(acontrollers[0].getValue("x"),2)+ Math.pow(acontrollers[0].getValue("y"),2)); velocity = Math.floor(velocity*10)/10; double deadzone = .2; if(velocity <= deadzone) { velocity = 0f; } CE.getRectangle().X+=Math.cos(angle)*.4*Time.dt*velocity; CE.getRectangle().Y+=Math.sin(angle)*.4*Time.dt*velocity; double aimangle = Math.PI+Math.atan2( acontrollers[0].getValue("ry"), acontrollers[0].getValue("rx")); velocity = Math.sqrt( Math.pow(acontrollers[0].getValue("rx"),2)+ Math.pow(acontrollers[0].getValue("ry"),2)); velocity = Math.floor(velocity*10)/10; if(velocity > deadzone) { CE.rotation=aimangle; } if(acontrollers[0].getValue("z") >= .3f) { Entity b = new Entity("bullet", new Rectangle( CE.getRectangle().X-16, CE.getRectangle().Y-16, 4, 4)); b.setDepth(1); b.setSprite(Sprite.getSprite("res/bullet.png")); b.rotation = CE.rotation-Math.PI+Math.toRadians(-2+4*random.nextFloat()); b.update = new fptr(){public void call(){double speed = .7; CE.getRectangle().X+=Math.cos(CE.rotation)*speed*Time.dt; CE.getRectangle().Y+=Math.sin(CE.rotation)*speed*Time.dt; if( (CE.getRectangle().X > ScrW && Math.cos(CE.rotation) > 0) || (CE.getRectangle().X < 0 && Math.cos(CE.rotation) < 0) || (CE.getRectangle().Y > ScrH && Math.sin(CE.rotation) > 0) || (CE.getRectangle().Y < 0 && Math.sin(CE.rotation) < 0) ) { modifiedEntities.remove(CE); }}}; modifiedEntities.add(b); } if(acontrollers[0].getValue("A") == 1.0f && acontrollers[0].getLast("A") < 1.0f) { Entity b = new Entity("vortex", new Rectangle( CE.getRectangle().X, CE.getRectangle().Y, 32,32)); b.setSprite(Sprite.getSprite("res/vortex.png")); b.setCentered(true); modifiedEntities.add(b); }}};
  e.setCentered(true);
  player = e;
  entities.add(e);
  final Entity particlePrototype;
  particlePrototype = new Entity("particle");
  particlePrototype.setSprite(Sprite.getSprite("res/flare.png"));
  particlePrototype.getRectangle().W = 4;
  particlePrototype.getRectangle().H = 4;
  particlePrototype.setCentered(true);
  particlePrototype.setVar("lifetime", 400);
  particlePrototype.setVar("life", 0);
  particlePrototype.setVar("alphafadethres", 300);
  particlePrototype.setVar("speed", .5);
  particlePrototype.setVar("friction", .0005);
  particlePrototype.setVar("anglerotationdir",Math.round(random.nextFloat()));
  particlePrototype.setVar("anglerotationspeed", Math.toRadians(.6));
  particlePrototype.update = new fptr(){public void call(){double life = CE.getVar("life")+Time.dt; CE.setVar("life", life); if(CE.getVar("life") >= CE.getVar("lifetime")) { modifiedEntities.remove(CE); } double thres = CE.getVar("alphafadethres"); double lifet = CE.getVar("lifetime"); if(life > thres) { } CE.setVar("speed", CE.getVar("speed")-CE.getVar("friction")*Time.dt); CE.getRectangle().X += Math.cos(CE.rotation)*CE.getVar("speed")*Time.dt; CE.getRectangle().Y += Math.sin(CE.rotation)*CE.getVar("speed")*Time.dt; if( (CE.getRectangle().X > ScrW && Math.cos(CE.rotation) > 0) || (CE.getRectangle().X < 0 && Math.cos(CE.rotation) < 0) || (CE.getRectangle().Y > ScrH && Math.sin(CE.rotation) > 0) || (CE.getRectangle().Y < 0 && Math.sin(CE.rotation) < 0) ) { modifiedEntities.remove(CE); } CE.rotation += CE.getVar("anglerotationdir")*CE.getVar("anglerotationspeed");}};
  final Entity enemyPrototype;
  enemyPrototype = new Entity("enemy");
  enemyPrototype.setSprite(Sprite.getSprite("res/testship2.png"));
  enemyPrototype.getRectangle().X = 400;
  enemyPrototype.getRectangle().Y = ScrH/2;
  enemyPrototype.setCentered(true);
  enemyPrototype.setVar("hp", 40);
  enemyPrototype.setVar("enginetrailfreq", 3);
  enemyPrototype.setVar("enginetrailtime", 0);
  enemyPrototype.update = new fptr(){public void call(){double speed = .3; double angle = Math.atan2(player.getRectangle().Y-CE.getRectangle().Y, player.getRectangle().X-CE.getRectangle().X); CE.rotation = angle+Math.PI; CE.getRectangle().X+=Math.cos(angle)*Time.dt*speed; CE.getRectangle().Y+=Math.sin(angle)*Time.dt*speed; CE.setVar("enginetrailtime", CE.getVar("enginetrailtime")+Time.dt); while(CE.getVar("enginetrailtime") > CE.getVar("enginetrailfreq")) { CE.setVar("enginetrailtime", CE.getVar("enginetrailtime")-CE.getVar("enginetrailfreq")); Entity enginetrail = particlePrototype.clone(); enginetrail.setSprite(Sprite.getSprite("res/generic.png")); enginetrail.setVar("lifetime", 150); enginetrail.rotation = CE.rotation+Math.toRadians(-40+80*random.nextFloat()); enginetrail.getRectangle().X = CE.getRectangle().X-CE.getRectangle().W/2; enginetrail.getRectangle().Y = CE.getRectangle().Y-CE.getRectangle().H/2; enginetrail.setColor(new Color( 255,255,255)); enginetrail.setVar("anglerotationdir", 0); enginetrail.setVar("speed", .35); enginetrail.setVar("speed", enginetrail.getVar("speed")*random.nextFloat()); enginetrail.setVar("lifetime", enginetrail.getVar("lifetime")*(.8+.5*random.nextFloat())); enginetrail.scale = 2f; enginetrail.draw = new fptr(){public void call(){CE.color.b-=0.01*Time.dt;CE.color.g-=0.005*Time.dt;}}; modifiedEntities.add(enginetrail); } for(Entity x : entities) { if(!x.name.equals("bullet")) { continue; } if(!modifiedEntities.contains(x)) { continue; } Rectangle area = CE.getRectangle(); boolean centered = CE.getCentered(); float x1 = (centered ? -(area.W/2):0)+area.X-area.W/2; float x2 = (centered ? -(area.W/2):0)+area.X+area.W/2; float y1 = (centered ? -(area.H/2):0)+area.Y-area.H/2; float y2 = (centered ? -(area.H/2):0)+area.Y+area.H/2; float ex = x.getRectangle().X; float ey = x.getRectangle().Y; if( ex > x1 && ex < x2 && ey > y1 && ey < y2 ) { modifiedEntities.remove(x); Entity b = particlePrototype.clone(); b.rotation = Math.PI*2*random.nextFloat(); b.getRectangle().X = CE.getRectangle().X; b.getRectangle().Y = CE.getRectangle().Y; b.setColor(new Color( random.nextFloat(), random.nextFloat(), random.nextFloat())); b.setVar("anglerotationdir", Math.round(random.nextFloat())); b.setVar("speed", b.getVar("speed")*random.nextFloat()); b.setVar("lifetime", b.getVar("lifetime")*(.8+.5*random.nextFloat())); modifiedEntities.add(b); CE.setVar("hp", CE.getVar("hp")-1); if(CE.getVar("hp") <= 0) { modifiedEntities.remove(CE); } } }}};
  e = new Entity("spawner");
  e.setSprite(null);
  e.setVar("freq", 325);
  e.setVar("timer", e.getVar("freq"));
  e.update = new fptr(){public void call(){CE.setVar("timer", CE.getVar("timer")-Time.dt); if(CE.getVar("timer") <= 0) { CE.setVar("timer", CE.getVar("timer")+CE.getVar("freq")); Entity b = enemyPrototype.clone(); b.getRectangle().X = 640*Math.round(random.nextFloat()); b.getRectangle().Y = 460*random.nextFloat(); modifiedEntities.add(b); }}};
  entities.add(e);
 }
 public void update() {
  Time.update();
  for(int i = 0;i<acontrollers.length;i++) {
   acontrollers[i].update(); }
  modifiedEntities = new ArrayList<Entity>(entities);
  for(Entity e : entities) {
   CE = e;
   e.update.call();
  }
  entities = new ArrayList<Entity>(modifiedEntities);
  for(int i = 0;i<acontrollers.length;i++) {
   acontrollers[i].postupdate(); }
 }
 public void draw() {
  GL11.glClear(
   GL11.GL_COLOR_BUFFER_BIT |
   GL11.GL_DEPTH_BUFFER_BIT);
  GL11.glColor3f(1f,1f,1f);
  List<Entity> drawlist = new ArrayList<Entity>(entities);
  Collections.sort(drawlist, new Comparator<Entity>() {
   public int compare(Entity a, Entity b) {
    if(a.getDepth() < b.getDepth()) { return -1; }
    if(a.getDepth() > b.getDepth()) { return 1; }
    return 0;
   }
  });
  Collections.reverse(drawlist);
  for(Entity e : drawlist) {
   CE = e;
   e.draw.call();
   if(e.getSprite() != null) {
    e.getSprite().draw(
     e.getRectangle(),e.getCentered(),e.getRotation(),e.getColor(),e.getScale());
   }
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
