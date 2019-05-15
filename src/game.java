import processing.core.PApplet;
import processing.core.PVector;
import processing.core.PImage;
import java.util.ArrayList;
public class game extends PApplet{
  actorObject player;
  actorObject emenys[];
  public static void main(String[] args) {
      PApplet.main("game");
  }
  public void settings(){
    fullScreen();
    //size(600, 600);
  }

  public void setup(){
    
  }

  public void draw(){}
  
  public void gameing(){
    
  }

  class actorObject{
    PImage style;
    PVector pos;
    actorObject(){
      
    }
    void display(){
      image(style, pos.y, pos.x);
    }
  }
}
