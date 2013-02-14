package lh.koneke.games.Atomique;
import java.io.IOException;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import java.util.Map;
import java.util.HashMap;
import org.newdawn.slick.Color;
public class Sprite {
 Texture texture;
 public Sprite(Texture texture) {
  this.texture = texture;
 }
 public void draw(Rectangle area) {
  draw(area, false, 0);
 }
 public void draw(
  Rectangle area, boolean centered) { draw(area, centered, 0);
 }
 public void draw(Rectangle area, boolean centered, Vector2 lookat) {
  draw(
   area, centered,
   Math.atan2(
    (centered?-(area.H/2):0)+area.Y-lookat.Y,
    (centered?-(area.W/2):0)+area.X-lookat.X),
   Color.white);
 }
 public void draw(Rectangle area, boolean centered, double rotation) {
  draw(area, centered, rotation, Color.white);
 }
 public void draw(Rectangle area, boolean centered, double rotation, Color c) {
  draw(area, centered, rotation, c, 1f);
 }
 public void draw(Rectangle area, boolean centered, double rotation, Color c, float scale) {
  texture.bind();
  c.bind();
  float dx, dy;
  GL11.glBegin(GL11.GL_QUADS);
  dx = -area.W/2; dy = -area.H/2;
  GL11.glTexCoord2f(0,0);GL11.glVertex2f(
   (centered ? -(area.W/2):0)+area.X-dx*scale*(float)Math.cos(rotation)+dy*scale*(float)Math.sin(rotation),
   (centered ?- (area.H/2):0)+area.Y-dx*scale*(float)Math.sin(rotation)-dy*scale*(float)Math.cos(rotation));
  dx = area.W/2; dy = -area.H/2;
  GL11.glTexCoord2f(1,0);GL11.glVertex2f(
   (centered ? -(area.W/2):0)+area.X-dx*scale*(float)Math.cos(rotation)+dy*scale*(float)Math.sin(rotation),
   (centered ?- (area.H/2):0)+area.Y-dx*scale*(float)Math.sin(rotation)-dy*scale*(float)Math.cos(rotation));
  dx = area.W/2; dy = area.H/2;
  GL11.glTexCoord2f(1,1);GL11.glVertex2f(
   (centered ? -(area.W/2):0)+area.X-dx*scale*(float)Math.cos(rotation)+dy*scale*(float)Math.sin(rotation),
   (centered ?- (area.H/2):0)+area.Y-dx*scale*(float)Math.sin(rotation)-dy*scale*(float)Math.cos(rotation));
  dx = -area.W/2; dy = area.H/2;
  GL11.glTexCoord2f(0,1);GL11.glVertex2f(
   (centered ? -(area.W/2):0)+area.X-dx*scale*(float)Math.cos(rotation)+dy*scale*(float)Math.sin(rotation),
   (centered ?- (area.H/2):0)+area.Y-dx*scale*(float)Math.sin(rotation)-dy*scale*(float)Math.cos(rotation));
  GL11.glEnd();
  Color.white.bind();
 }
 static HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
 public static Sprite getSprite(String path) {
  if(!sprites.keySet().contains(path)) {
   try {
    Texture texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(path));
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
       GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
    sprites.put(path, new Sprite(texture));
    return sprites.get(path);
   } catch (IOException e) {
    e.printStackTrace();
    System.exit(0);
   }
   return null;
  }
  else {
   return sprites.get(path);
  }
 }
}
