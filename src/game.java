import processing.core.PApplet;
import processing.core.PVector;
import processing.core.PImage;
import java.util.ArrayList;
//import java.awt.event.ActionListener;
public class game extends PApplet{
  actorObject player;
  public ArrayList<PImage> pimgpack = new ArrayList<PImage>();
  ArrayList<bulletObject> bullets = new ArrayList<bulletObject>();
  ArrayList<actorObject>  enemys  = new ArrayList<actorObject>();
  //ArrayList keyEvents = new ArrayList();
  public static void main(String[] args) {
      PApplet.main("game");
  }
  public void settings(){
    //fullScreen();
    size(900, 900);
  }

  public void setup(){
    PImage playerStyle = loadImage("images/player2.png");
    player = new actorObject(playerStyle, new PVector(width/2, height/2));
    enemys.add(new actorObject(new PVector(200, 50), new PVector(0, 1)));
    actorObject e1 = enemys.get(0);
    e1.control(' ', true);
    e1.setAtkSpeed(50);
    for(int i=0;i<20;i++) {
      actorObject enemy = new actorObject(new PVector(random(0, 600), random(0, 200)), new PVector(0, 1));
      enemy.setAtkSpeed((int)random(40, 50));
      enemy.control(' ', true);
      enemys.add(enemy);
    }
    //keyEvents.add(player);
  }
  public void draw(){
    background(51);
    gameing();
    if(enemys.size()<10) {
      actorObject enemy = new actorObject(new PVector(random(0, 600), random(0, 200)), new PVector(0, 1));
      enemy.setAtkSpeed(50);
      enemy.control(' ', true);
      enemys.add(enemy);
    }
  }
  
  public void gameing(){
    player.run();
    for(int i=0;i<enemys.size();i++) {
      enemys.get(i).run();
    }
    
    for(int i=0;i<bullets.size();i++){
      bulletObject b = bullets.get(i);
      b.run();
      for(int j=0;j<enemys.size()||b==null;j++){
        actorObject e = enemys.get(j);
        if(b!=null&&b.touch(e)&&b.acc.y<0) {
          bullets.remove(i);
          enemys.remove(j);
          j--;
          i--;
//          println("touch!!");
//          noLoop();         
        }
      }
      if(b!=null) {
        if(b.touch(player)){
//          println("player lose");
        }
        if(b.outside()) {
          bullets.remove(i);
          i--;
        }
      }
      
    }

  }
  
  public void keyTyped(){
    //debug mode
    if(key=='T')
      player.changeMode(0);
    if(key=='G')
      player.changeMode(1);
    if(key=='Y')
      player.changeSpeed(1);
    if(key=='H')
      player.changeSpeed(-1);
    //debug mode end
  }
  
  public void mousePressed(){
    
  }
  public void keyPressed(){
    //for(int i=0;i<keyEvents.size();i++)
      //keyEvents[i].control(key, true);
    player.control(key, true);
//    enemys.get(0).control(key, true);
  }
  public void keyReleased(){
    player.control(key, false);
//    enemys.get(0).control(key, false);
  }
  
  class actorObject{
    private int hp = 1;
    private int weal;//weapon level
    private boolean stpd = false;//shotting key pressed
    private final int bulletLimitMax = 20;
    private int bulletLimit = 20, bulletCounter = 0;
    private int animationCounter = 0, animationMode = 0;
    public PVector size = new PVector(20, 20);
            //, shottingDelay = 0;
    PImage style = null;
    PVector pos = null,
            acc = new PVector(0,0,5);
    PVector dir = new PVector(0, -1);
    actorObject(PImage style, PVector p){
      this.style = style;
      size = new PVector(style.width, style.height);
      this.pos = p;
    }
    actorObject(PVector p, PVector dir){
      this.dir = dir;
      this.pos = p;
    }
    actorObject(PVector p){
      this.pos = p;
    }
    public void setAtkSpeed(int speed) {
      bulletLimit = speed;
    }
    public void run() {
      this.move();
      this.display();
      this.animation();
      if(stpd)
//        for(int i=0;i<10;i++)
        this.shooting();
//      if(bulletCounter>0)
//        bulletCounter--;
    }
    public void animation(){
      if(animationCounter!=0&&animationMode==0){
        animationCounter = 0;
      }else if(animationMode==1){//explostion
        
      }
    }
    public void move(){
      float x = this.pos.x+this.acc.x, y = this.pos.y+this.acc.y;
      if(x>0&&x<width)
        this.pos.x+=this.acc.x;
      if(y>0&&y<height)
        this.pos.y+=this.acc.y;
    }
    public void display(){
      if(style!=null) {
        float xResize = (float) (style.width*(acc.x==0?1:0.9));
        image(style, this.pos.x-style.width/2, this.pos.y-style.height/2, xResize, style.height);
        return;
      }else{
        fill(255);
        stroke(255);
        rect(pos.x-size.x/2, pos.y-size.y/2, size.x, size.y);
      }
      //image(style, pos.y, pos.x);
    }
    public void control(char k, boolean pressed){
      switch(k){
        case 'W':case 'w':
          acc.y =    (pressed?acc.z:0)*dir.y;
        break;
        case 'A':case 'a':
          acc.x = -1*(pressed?acc.z:0);
        break;
        case 'S':case 's':
          acc.y = -1*(pressed?acc.z:0)*dir.y;
        break;
        case 'D':case 'd':
          acc.x =    (pressed?acc.z:0);
        break;
        case ' ':
          stpd = pressed;
          //this.shooting();
        break;
      }
    }
    public void changeSpeed(int speed){
      if(bulletLimit+speed>0&&bulletLimit+speed<=bulletLimitMax)
        bulletLimit+=speed;
      else if(bulletLimit+speed<0)
        bulletLimit = 1;
      else if(bulletLimit+speed<0)
        bulletLimit = bulletLimitMax;
      println(bulletLimit);
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
        for(int i=0;i<2;i++){
          bullets.add(
              new bulletObject(
                new PVector(this.pos.x+size.x/2*(i==0?-1:1), this.pos.y+this.size.y/2*this.dir.y), 
                new PVector(0, 20*dir.y)
              )
            );
        }
      }
      if(weal==1) {
        for(int i=-2;i<3;i++)
          bullets.add(
            new bulletObject(
              new PVector(this.pos.x+size.x/6*i, this.pos.y+this.size.y/2*this.dir.y), 
              new PVector((float)1*i, 20*dir.y)
            )
          );
      }
    }
  }
  class bulletObject{
    PVector pos, acc;
    private int movingMode = 1;
    private PVector size = new PVector(5, 5);
    public void basicSetup(PVector pos, PVector acc){this.pos = pos;this.acc = acc;}
    bulletObject(PVector pos, PVector acc, int mode){
      movingMode = mode;
      basicSetup(pos, acc);
    }
    bulletObject(PVector pos, PVector acc){
      basicSetup(pos, acc);
    }
    public boolean touch(actorObject a){
//      float p1r = sqrt(pow(abs( pos.x-a.pos.x ), 2)+pow(abs( pos.y-a.pos.y ), 2)),
//            p2r = sqrt(pow(abs( pos.x-(a.pos.x+a.size.x) ), 2)+pow(abs( pos.y-a.pos.y ), 2)),
//            p3r = sqrt(pow(abs( pos.x-(a.pos.x+a.size.x) ), 2)+pow(abs( pos.y-(a.pos.y+a.size.y) ), 2)), 
//            p4r = sqrt(pow(abs( pos.x-a.pos.x ), 2)+pow(abs( pos.y-(a.pos.y+a.size.y) ), 2));
//      float pr  = sqrt( pow(size.x, 2) + pow(size.y, 2) )/2;
//      if(p1r<=pr||p2r<=pr||p3r<=pr||p4r<=pr)
      if(pos.x>=a.pos.x&&
         pos.y>=a.pos.y&&
         pos.x<=a.pos.x+a.size.x&&pos.y<=a.pos.y+a.size.y)
          return true;
      return false;
    }
    public boolean touch(bulletObject b){
      float r  = sqrt( pow(pos.x-b.pos.x, 2) + pow(pos.y-b.pos.y, 2) );
      if(r>=size.x+b.size.x&&r<=size.y+b.size.y)
        return true;
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
      fill(255,200);
      stroke(255);
      ellipse(this.pos.x, this.pos.y, size.x, size.y);
    }
    public boolean outside(){
      return this.pos.x>width||this.pos.y>height||this.pos.x<0||this.pos.y<0;
    }
  }
}