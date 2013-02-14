package lh.koneke.games.Atomique;
import org.lwjgl.Sys;
public class Time {
 static long lastFrame = getTime();
 public static int dt = 0;
 public static long getTime() {
  return (Sys.getTime() * 1000) / Sys.getTimerResolution();
 }
 public static void update() {
  long time = getTime();
  dt = (int) (time - lastFrame);
  lastFrame = time;
 }
}
