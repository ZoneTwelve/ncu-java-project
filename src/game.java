import processing.core.PApplet;
import processing.core.PVector;
import processing.core.PImage;
import java.util.ArrayList;
//import java.awt.event.ActionListener;
public class game extends PApplet{
  actorObject player;
  ArrayList<bulletObject> bullets = new ArrayList<bulletObject>();
  ArrayList<actorObject>  enemys  = new ArrayList<actorObject>();
  //ArrayList keyEvents = new ArrayList();
  public static void main(String[] args) {
      PApplet.main("game");
  }
  public void settings(){
    //fullScreen();
    size(600, 600);
  }

  public void setup(){
    player = new actorObject(new PVector(width/2, height/2));
    enemys.add(new actorObject(new PVector(20, 50), new PVector(0, 1)));
    enemys.get(0).control(' ', true);
    //keyEvents.add(player);
  }

  public void draw(){
    background(51);
    gameing();
    for(int i=0;i<enemys.size();i++)
      enemys.get(i).run();
    
    for(int i=0;i<bullets.size();i++){
      bulletObject b = bullets.get(i);
      b.run();
      if(b.outside()) {
        bullets.remove(i);
        i--;
      }
    }
  }
  
  public void gameing(){
    player.run();
  }
  
  public void keyTyped(){
    //debug mode
    if(key=='t')
      player.changeMode(0);
    if(key=='g')
      player.changeMode(1);
    if(key=='y')
      player.changeSpeed(1);
    if(key=='h')
      player.changeSpeed(-1);
    //debug mode end
  }
  public void keyPressed(){
    //for(int i=0;i<keyEvents.size();i++)
      //keyEvents[i].control(key, true);
    player.control(key, true);
    enemys.get(0).control(key, true);
  }
  public void keyReleased(){
    player.control(key, false);
    enemys.get(0).control(key, false);
  }
  
  class actorObject{
    private int weal;//weapon level
    private boolean stpd = false;//shotting key pressed
    private int bulletLimit = 9, bulletCounter = 0;
            //, shottingDelay = 0;
    PImage style;
    PVector pos = null,
            acc = new PVector(0,0,5);
    PVector dir = new PVector(0, -1);
    actorObject(PVector p, PVector dir){
      this.dir = dir;
      this.pos = p;
    }
    actorObject(PVector p){
      this.pos = p;
    }
    public void run() {
      this.move();
      this.display();
      if(stpd)
//        for(int i=0;i<10;i++)
        this.shooting();
//      if(bulletCounter>0)
//        bulletCounter--;
    }
    public void move(){
      float x = this.pos.x+this.acc.x, y = this.pos.y+this.acc.y;
      if(x>0&&x<width)
        this.pos.x+=this.acc.x;
      if(y>0&&y<height)
        this.pos.y+=this.acc.y;
    }
    public void display(){
      fill(255);
      stroke(255);
      rect(pos.x-10, pos.y-10, 20, 20);
      //image(style, pos.y, pos.x);
    }
    public void control(char k, boolean pressed){
      switch(k){
        case 'W':
        case 'w':
          acc.y =    (pressed?acc.z:0)*dir.y;
        break;
        case 'A':
        case 'a':
          acc.x = -1*(pressed?acc.z:0);
        break;
        case 'S':
        case 's':
          acc.y = -1*(pressed?acc.z:0)*dir.y;
        break;
        case 'D':
        case 'd':
          acc.x =    (pressed?acc.z:0);
        break;
        case ' ':
          stpd = pressed;
          //this.shooting();
        break;
      }
    }
    public void changeSpeed(int speed){
      if(bulletLimit+speed>0&&bulletLimit+speed<10)
        bulletLimit+=speed;
    }
    public void changeMode(int mode){
      weal = mode;
    }
    public void shooting(){
//      if(bulletCounter>bulletLimit) {
//        return;
//      }
//      bulletCounter+=25 ;
      bulletCounter = (bulletCounter+1)%bulletLimit;
      if(bulletCounter!=0)
        return;
      if(weal==0){
        bullets.add(
            new bulletObject(
              new PVector(this.pos.x-5, this.pos.y), 
              new PVector(0, 20*dir.y)
            )
          );
        bullets.add(
            new bulletObject(
              new PVector(this.pos.x+5, this.pos.y), 
              new PVector(0, 20*dir.y)
            )
          );
      }
      if(weal==1) {
        for(int i=-2;i<3;i++)
          bullets.add(
            new bulletObject(
              new PVector(this.pos.x, this.pos.y), 
              new PVector((float)1*i, 20*dir.y)
            )
          );
      }
    }
  }
  class bulletObject{
    PVector pos, acc;
    private int movingMode = 1;
    public void basicSetup(PVector pos, PVector acc){this.pos = pos;this.acc = acc;}
    bulletObject(PVector pos, PVector acc, int mode){
      movingMode = mode;
      basicSetup(pos, acc);
    }
    bulletObject(PVector pos, PVector acc){
      basicSetup(pos, acc);
    }
    public boolean touch(){
      return false;
    }
    public void run(){
      this.move();
//      this.pos.x+=(float)random((float) -2.2);
//      this.pos.y+=random(-10, 10)/100f;
      this.display();
    }
    public void move() {
      if(movingMode==0) {
        this.pos.add(this.acc);
      }else if(movingMode==1){
        this.pos.add(this.acc);
        this.pos.x = this.pos.x+5*sin(millis());
      }
    }
    public void display() {
      fill(255);
      stroke(255);
      ellipse(this.pos.x, this.pos.y, 5, 5);
    }
    public boolean outside(){
      return this.pos.x>width||this.pos.y>height||this.pos.x<0||this.pos.y<0;
    }
  }
}