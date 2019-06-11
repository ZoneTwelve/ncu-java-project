import processing.core.*;
//import processing.core.PVector;
//import processing.core.PImage;

import java.util.ArrayList;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;

public class game extends PApplet{
  boolean lock = false;
  static int level = 0;
  static boolean pause = false;
  actorObject player;
  static backgroundSystem mainbg = null;
  public ArrayList<PImage> pimgpack = new ArrayList<PImage>();
  ArrayList<bulletObject> bullets = new ArrayList<bulletObject>();
  ArrayList<actorObject>  enemys  = new ArrayList<actorObject>();
  ArrayList<actorObject>  enemy2  = new ArrayList<actorObject>();
  buttonObject menuBtn[] = new buttonObject[5];
  buttonObject pauseBtn[] = new buttonObject[4];
  int levelDelay = 250;
  int levelCount;
  int generateCount = 0;
  int generatePoint = -1;
  int randomPoint[] = new int[20];
  int enemyLimit = 0;
  int em2Clock = 0, em2CLim = 0;
  int mode = 0;

  int grade[] = new int[10];
  //ArrayList keyEvents = new ArrayList();
  public static void main(String[] args) {
      PApplet.main("game");
  }
  public void settings(){
    //fullScreen();
    size(900, 900);
  }

  public void setup(){
    pimgpack.add(loadImage("images/main-page.png"));//0
    PImage playerStyle = loadImage("images/player.png");
    player = new actorObject(playerStyle, new PVector(width/2, height/2));
    player.setBulletSpeed(20);
//    enemys.add(new actorObject(new PVector(200, 50), new PVector(0, 1)));
//    actorObject e1 = enemys.get(0);
//    e1.control(' ', true);
//    e1.setAtkSpeed(50);
    for(int i=0;i<randomPoint.length;i++) {
      randomPoint[i] = (int) random(width/5, width/10*8);
//      println(randomPoint[i]);
    }
//    for(int i=0;i<3&&false;i++) {
//      actorObject enemy = new actorObject(new PVector(random(width/5, width/10*8), 0), new PVector(0, 1));
//      enemy.setAtkSpeed((int)random(40, 50));
//      enemy.control(' ', true);
//      enemy.randomWalk(true);
//      enemys.add(enemy);
//    }
    levelCount = 0;
    int p = height/10*4;
    int sw = 60;
    menuBtn[0x0] = new buttonObject(new PVector(width/2, p+=sw+(sw/10)), "Easy",    new PVector(sw*3, sw, 48), new Color("#FFFFFF"), new Color("#006620"));
    menuBtn[0x1] = new buttonObject(new PVector(width/2, p+=sw+(sw/10)), "Normal",  new PVector(sw*3, sw, 48), new Color("#FFFFFF"), new Color("#1F00AF"));
    menuBtn[0x2] = new buttonObject(new PVector(width/2, p+=sw+(sw/10)), "Hard",    new PVector(sw*3, sw, 48), new Color("#ffffff"), new Color("#FF4500"));
    menuBtn[0x3] = new buttonObject(new PVector(width/2, p+=sw+(sw/10)), "Grade",   new PVector(sw*3, sw, 48));
    menuBtn[0x4] = new buttonObject(new PVector(width/2, p+=sw+(sw/10)), "Exit",    new PVector(sw*3, sw, 48), new Color("#FF0000"), new Color("#D40000"));
    p = height/10*4;
    pauseBtn[0x0] = new buttonObject(new PVector(width/2, p+=sw+(sw/10)), "Continue",   new PVector(100, sw, 24), new Color("#FFFFFF"), new Color("#006620"));
    pauseBtn[0x1] = new buttonObject(new PVector(width/2, p+=sw+(sw/10)), "Grade",      new PVector(100, sw, 24), new Color("#FFFFFF"), new Color("#1F00AF"));
    pauseBtn[0x2] = new buttonObject(new PVector(width/2, p+=sw+(sw/10)), "Main",       new PVector(100, sw, 24), new Color("#ffffff"), new Color("#FF4500"));
    pauseBtn[0x3] = new buttonObject(new PVector(width/2, p+=sw+(sw/10)), "Exit",       new PVector(100, sw, 24), new Color("#FF0000"), new Color("#D40000"));
    PImage test = loadImage("images/game-bg.png");
    PImage mainbglist[] = {test, test};
    mainbg = new backgroundSystem(mainbglist);
    //keyEvents.add(player);
  }
  public void draw(){
    background(0);
    textSize(24);
    if(!lock&&keyPressed&&key=='k'&&level!=0){
      pause = !pause;
      lock = true;
    }else if(lock&&!keyPressed)
      lock = false;
    if(pause){
      pauseMenu();
    }else {
      switch(level) {
        case 0:
          startMenu();
        break;
        case 1:
          gameing();
        break;
        case 99:
          gradeList();
        break;
      }      
    }
  }
  
  public void pauseMenu(){
    for(buttonObject btn : pauseBtn) {
      boolean s = btn.run();
      if(s){
        switch(btn.msg) {
          case "Continue":
            pause = false;
          break;
          case "Main":
            //故意不reset Game
            level = 0;
          break;
          case "Grade":
            while(gradeList());
          break;
          case "Exit":
            exit();
          break;
        }
      }
    }
  }
  public void startMenu(){
    image(pimgpack.get(0), 0, 0, width, height);
//    PImage bg = pimgpack.get(0);
//    image(bg, 0, 0, width, height);
//    String msg = "Welcome...";
//    fill(255);
//    text(msg, width/2-textWidth(msg)/2, (float)(height*0.2));
    for(buttonObject btn : menuBtn) {
      boolean s = btn.run();
      if(s){
        switch(btn.msg) {
          case "Hard":
            player.hp = 3;
            mode = 3;
            enemyLimit = 30;
            player.changeMode(0);
            player.setAtkSpeed(25);
            levelDelay = 100;
            em2CLim = 300;
            level++;
          break;
          case "Normal":
            player.hp = 4;
            mode = 2;
            enemyLimit = 20;
            player.changeMode(1);
            player.setAtkSpeed(20);
            levelDelay = 150;
            em2CLim = 500;
            level++;
          break;
          case "Easy":
            player.hp = 5;
            mode = 1;
            enemyLimit = 10;
            player.changeMode(1);
            player.setAtkSpeed(3);
            levelDelay = 250;
            em2CLim = 800;
            level++;
          break;
          case "Grade":
            level = 99;
          break;
          case "Exit":
            exit();
          break;
        }
      }
    }
  }
  
  public void gameing(){
//    mainbg.display();
    player.run();
    levelCount = (levelCount+1)%levelDelay;
    em2Clock = (em2Clock+1)%em2CLim;
    if(em2Clock==0) {
      int dire = random(0, 10)>5?8:2;
      actorObject enemy = new actorObject(new PVector(width/10*(dire), 0), new PVector(dire==8?-1:1, 1));
//      enemy.setAtkSpeed((int)random(10, 15+(3-mode)*10));
      enemy.setAtkSpeed(mode==3?10:mode==2?20:30);
//      enemy.control(' ', true);
      enemy.randomWalk(2);
      enemy2.add(enemy);
    }
    if(levelCount%20==0)
//    println(levelCount);
    if(levelCount==0) {
//      println("generate");
      if(levelDelay<150) {
        levelDelay+=50;
      }else if(levelDelay>250) {
        levelDelay = 50;
      }
//      levelDelay = (200+(int)random(0, 100))%(int)random(100, 300);
      generateCount = (int)random(enemyLimit/2, enemyLimit);
      generatePoint = (int)random(0, randomPoint.length);
      
    }
//    println(levelCount);
    if(generateCount!=0&&levelCount%20==0){
//      for(int i=0;i<random(10, 20);i++){
      if(generateCount%5==0)
        generatePoint = (int)random(0, randomPoint.length);
      actorObject enemy = new actorObject(new PVector(randomPoint[generatePoint], 0), new PVector(0, 1));
      enemy.setAtkSpeed((int)random(80+enemyLimit, 100+enemyLimit));
      enemy.control(' ', true);
      enemy.randomWalk(1);
      enemys.add(enemy);
//      }
      generateCount--;
    }

    for(int i=0;i<enemys.size();i++) {//display and move
      enemys.get(i).run();
      if(enemys.get(i).pos.y>height) {
        enemys.remove(i);
        i--;
      }
    }
    for(int i=0;i<enemy2.size();i++) {//display and move
      enemy2.get(i).run();
      enemy2.get(i).attack(player.pos);
      if(enemy2.get(i).pos.y>height) {
        enemy2.remove(i);
        i--;
      }
    }
    
    for(int i=0;i<bullets.size();i++){
      bulletObject b = bullets.get(i);
      b.run();
      for(int j=0;j<enemys.size()&&b!=null;j++){
        actorObject e = enemys.get(j);
        if(b!=null&&b.touch(e)&&b.acc.y<0) {
          bullets.remove(i);
          enemys.remove(j);
          j--;
          i--;
          b = null;
        }
      }
      for(int j=0;j<enemy2.size()&&b!=null;j++){
        actorObject e = enemy2.get(j);
        if(b!=null&&b.touch(e)&&b.acc.y<0) {
          bullets.remove(i);
          enemy2.remove(j);
          b = null;
          j--;
          i--;
        }
      }
      if(b!=null){
        if(b.touch(player)&&player.invincible==false){
          player.injured();
//          enemy2 = new ArrayList<actorObject>();
//          enemys = new ArrayList<actorObject>();
//          bullets = new ArrayList<bulletObject>();
//          level = 0;
        }
        if(b.outside()&&bullets.size()>0) {
//          println(i+" "+bullets.size());
          bullets.remove(i);
          i--;
        }
      }
    }
  }
  
  public boolean gradeList(){
    return true;
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
    if(key=='Q'&&enemys.size()>0)
      player.attack(enemys.get(0).pos);
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
    private int radnomWalker = 0;
//    private boolean ranMove = false;
    int hp = 1;
    private int weal;//weapon level
    boolean invincible = false;
    private boolean stpd = false;//shotting key pressed
    private final int bulletLimitMax = 20;
    private int bulletLimit = 20, bulletCounter = 0, bulletSpeed = 5;
    private int animationCounter = 0, animationMode = 0;
    public PVector size = new PVector(20, 20);
            //, shottingDelay = 0;
    PImage style = null;
    PVector pos = null,
            acc = new PVector(0,0,5);
    PVector dir = new PVector(0, -1);
    float ranwalk = 0;
    actorObject(PImage style, PVector p){
      this.style = style;
      size = new PVector(
        map(40, 0, style.width, 0, 800),
        map(100, 0, style.height, 0, 800)
      );
      this.pos = p;
    }
    actorObject(PVector p, PVector dir){
      this.dir = dir;
      this.pos = p;
    }
    actorObject(PVector p){
      this.pos = p;
    }
    public void setBulletSpeed(int s) {
      this.bulletSpeed = s;
    }
    public void setAtkSpeed(int speed) {
      bulletLimit = speed;
    }
    
    public void injured(){
      invincible = true;
      animationCounter = 100;
      hp--;
//      println(hp);
      animationMode = hp==0?2:1;      
    }
    
    public void run() {
      if(animationCounter>0){
        animationCounter--;
      }else if(invincible==true){
        animationMode = 0;
        invincible = false;
      }
      this.move();
      this.display();
      this.animation();
      if(radnomWalker==1){
        float s = 2*sin(PI/100*(ranwalk++));
//        println(s);
        pos.x+=s+random(-1, 1);
        pos.y+=random(1,2);
      }else if(radnomWalker==2){
        this.pos.y+=height/500;
        this.pos.x+=dir.x;
      }
        
      if(stpd)
        this.shooting();
//      if(bulletCounter>0)
//        bulletCounter--;
    }
    public void animation(){
      if(animationCounter!=0&&animationMode==0){
//        animationCounter = 0;
      }else if(animationMode==1){//injure

      }else if(animationMode==2){//explostion
        
      }
    }
    
    public void randomWalk(int status){
      this.radnomWalker = status;
    }
    
    public void move(){
      float x = this.pos.x+this.acc.x, y = this.pos.y+this.acc.y;
      if(x>0&&x<width)
        this.pos.x+=this.acc.x;
      if(y>0&&y<height)
        this.pos.y+=this.acc.y;
    }
    public void display(){
      if(animationCounter%2==1)
        return;
      if(style!=null){
        float xResize = (float) (size.x*(acc.x==0?1:0.9));
        image(style, this.pos.x-size.x/2, this.pos.y-size.y/2, xResize, size.y);
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
//      println(bulletLimit);
    }
    public void changeMode(int mode){
      weal = mode;
    }
    public void attack(PVector target){
      bulletCounter = (bulletCounter+1)%bulletLimit;
      if(bulletCounter!=0)
        return;
      if(dir.y*(pos.y-target.y)>0)
        return;
      if(this.pos.x<0||this.pos.x>width)
        return;
      float r = sqrt(pow(target.x-this.pos.x, 2)+pow(target.y-this.pos.y, 2));
      float x = bulletSpeed*(target.x-this.pos.x)/r,
            y = bulletSpeed*(target.y-this.pos.y)/r;
      bullets.add(
          new bulletObject(
            new PVector(this.pos.x+x*5, this.pos.y+y*5),
            new PVector(x, y)
          )
        );
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
                new PVector(0, bulletSpeed*dir.y)
              )
            );
        }
      }
      if(weal==1) {
        for(int i=-2;i<3;i++)
          bullets.add(
            new bulletObject(
              new PVector(this.pos.x+size.x/6*i, this.pos.y+this.size.y/2*this.dir.y), 
              new PVector((float)1*i, bulletSpeed*dir.y)
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
//      if(pos.x>=a.pos.x&&
//         pos.y>=a.pos.y&&
//         pos.x<=a.pos.x+a.size.x&&
//         pos.y<=a.pos.y+a.size.y)
      if(pos.x>=a.pos.x&&
         pos.y>=a.pos.y&&
         pos.x<=a.pos.x+a.size.x&&
         pos.y<=a.pos.y+a.size.y)
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
        this.pos.x = this.pos.x+sin((millis()+random(100, 500))/50);
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
  class buttonObject{
    PVector pos, size, speed;
    Color normal = new Color(255), hover = new Color(51);
    //size = new PVector(width, height, fontSize);
    String msg;
//    ActionListener func;
    buttonObject(PVector pos, String msg){
      setup(pos, msg, new PVector(textWidth(msg), 20, 16));
    }
    public buttonObject(PVector pos, String msg, PVector size) {
      setup(pos, msg, size);
    }
    public buttonObject(PVector pos, String msg, PVector size, Color n, Color h){
      setup(pos, msg, size);
      normal = n;
      hover  = h;
    }
    private void setup(PVector pos, String msg, PVector size){
      this.msg = msg;
      this.pos = pos;
      this.size = size;
    }
    public boolean run(){
      boolean mouseOver = mouseX>=pos.x-size.x/2&&mouseY>pos.y-size.y/2&&mouseX<=pos.x+size.x/2&&mouseY<=pos.y+size.y/2;
      this.display(mouseOver?hover:normal);
      return mouseOver&&mousePressed;
    }
    public void display(Color c){
//      fill(normal.r, normal.g, normal.b);
//      stroke(normal.r, normal.g, normal.b);
      fill(c.r, c.g, c.b);
      stroke(c.r, c.g, c.b);
      float x = pos.x-size.x/2, y = pos.y-size.y/2;
      rect(x, y, size.x, size.y);
//      fill(hover.r, hover.g, hover.b);
//      stroke(hover.r, hover.g, hover.b);
      fill(abs(255-c.r), abs(255-c.g), abs(255-c.b));
      stroke(abs(255-c.r), abs(255-c.g), abs(255-c.b));
      textSize(size.z);
      text(this.msg, pos.x-textWidth(msg)/2, y+size.z);
    }
  }
  class Color{
    int r,g,b;
    float a;
    Color(String code){
      int r = Integer.parseInt(code.substring(1, 3), 16),
          g = Integer.parseInt(code.substring(3, 5), 16),
          b = Integer.parseInt(code.substring(5, 7), 16);
      setup(r,g,b, 1f);
    }
    Color(int r){setup(r,r,r,1);}
    Color(int r, int g){setup(r,g,g,1);}
    Color(int r, int g, int b){setup(r,g,b,1);}
    Color(int r, int g, int b, float a){setup(r,g,b,a);}
    public void setup(int r, int g, int b, float a) {
      this.r = r;
      this.g = g;
      this.b = b;
      this.a = a;
    }
  }
  class backgroundSystem{
    PImage bgs[] = null;
    int index = 0;
    PVector pos = new PVector(), dir = new PVector(0, 1);
    //background image array
    backgroundSystem(PImage bga[]){this.bgs = bga;}
    public void scoll(PVector d){this.dir = d;}
    public void display(){
      PImage a = bgs[index%bgs.length];
      PImage b = bgs[(index+1)%bgs.length];
      index = (index+1)%bgs.length;
      image(a, pos.x, pos.y, width, height);
      this.pos.x = (this.pos.x+this.dir.x)%height;
      this.pos.y = (this.pos.y+this.dir.y)%height;
      image(b, pos.x, pos.y-height, width, height);
    }
  }
//  class sizeObject{
//    int width, height;
//  }
}