package lh.koneke.games.Atomique;
public class Util {
 public static double getShortAngle(double a1, double a2) {
  double a = (Math.abs(a1-a2))%360;
  return (a > 180 ? 360-a : a);
 }
}
