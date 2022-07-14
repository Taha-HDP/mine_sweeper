import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.sound.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class mineswepper extends PApplet {

int user_X=-1, user_Y=-1;
int x=10, y=10, bomb_number= 5, collor1=145, collor2=255, collor3=255;
String version = "(V1.5)" ;
int[][] hide ;
int[][] counter ;
int[][] bomb_loc  ;
int per = 0;
boolean tryagain=false, flag=false, start=false, win=false, lose=false, intro=true, game=true;
boolean vic_sound=true, lose_sound=true, sound=true ;
PImage mine, box, logo , mute , unmute ;

SoundFile music1;
SoundFile victory;
SoundFile losing;
SoundFile click;
String back_music = "data/back_music.mp3";
String victory_effect = "data/victory-royale.mp3";
String lose_effect = "data/lose.mp3";
String click_effect = "data/click.mp3";
String path1, path2, path3, path4;
public void setup() {
  
  mine = loadImage("mine.png");
  box = loadImage("box.png");
  logo = loadImage("logo.png");
  mute = loadImage("mute.png");
  unmute = loadImage("sound.png");
  set_array();
  frameRate(30);
  path1 = sketchPath( back_music);
  music1 = new SoundFile(this, path1);
  music1.amp(0.1f);

  path2 = sketchPath( victory_effect);
  victory = new SoundFile(this, path2);
  victory.amp(0.2f);

  path3 = sketchPath( lose_effect);
  losing = new SoundFile(this, path3);
  losing.amp(0.2f);

  path4 = sketchPath( click_effect);
  click = new SoundFile(this, path4);
  click.amp(0.3f);
}
public void draw() {
  if (tryagain==false && start==true && win==false) {
    base();
    stroke(0);
    fill(212);
    rect(50, 40, 80, 30, 6);
    fill(0);
    textSize(17);
    text(" Back", 65, 61);
    textSize (25);
    int arz=133, win_counter =0 ;
    for (int i=0; i<x; i++) {
      int tool=73 ;
      for (int j=0; j<y; j++) {
        fill(56, 50, 34);
        if ((i==user_X)&&(j==user_Y)) {
          if (counter[i][j] < 0) {
            lose=true ;
          } else {
            fill(95, 194, 194);
            text (counter[i][j], tool, arz);
            hide[i][j] = counter[i][j] ;
          }
        } else {
          if (hide[i][j]<0) {
            win_counter++ ;
            image(box, tool-13, arz-27, 40, 40);
          } else {
            fill(95, 194, 194);
            text (hide[i][j], tool, arz);
          }
        }
        tool = tool+60 ;
      }
      arz = arz+50 ;
    }
    if (lose==true) {
      lose_page() ;
      lose=false ;
    }
    if (win_counter==bomb_number) {
      win = true ;
    }
  } else if (start==false && intro==false) {
    menu();
  } else if (start==true && win==true) {
    win_page();
  } else if (intro==true) {
    intro_page();
  }
  //print(mouseX);
  //print("  ");
  //println(mouseY);
}
public void intro_page() {
  per = (per + 1) % 100;
  background(0, 0, 0);
  fill(255);
  rect(120, 130, 400, 100, 40);
  if (per<50) {
    textSize(50);
    fill(0);
    text("Mine sweeper", 150, 195);
  } else {
    textSize(50);
    fill(0);
    text("AIO compony", 155, 195);
  }
  textSize(15);
  fill(255);
  text ( "created By Taha_HDP", 500, 650);
  image(logo, 200, 260, 300, 300);
  textSize(20);
  fill(255);
  text("Loading ... " + per + " %", 100, 600);
  rect(100, 620, per * 2, 20, 7);
  if (per>=99) {
    intro=false ;
  }
}
public void menu() {
  if (game==true) {
    music1.play();
    music1.loop();
    game=false ;
  }
  background(0);
  noFill();
  PFont font1 = createFont("Lato Black", 1000);
  for (int i = 0; i < 200; i += 20) {
    stroke(255, i, i+50);
    bezier(600-(i/2.0f), 40+i, 410, 20, 440, 300, 50-(i/16.0f), 300+(i/8.0f));
  }
  stroke(0);
  fill(collor1);
  rect(430, 520, 40, 35, 5);
  fill(collor2);
  rect(500, 520, 40, 35, 5);
  fill(collor3);
  rect(570, 520, 40, 35, 5);
  fill(30, 247, 94);
  rect(300, 600, 100, 50, 10);
  fill(247, 59, 42);
  rect(50, 620, 150, 30, 10);
  textSize(15);
  fill(255);
  rect(630, 50, 40, 40, 5);
  if(sound==true){
    image(unmute, 637, 57, 25, 25);
  }else{
    image(mute, 637, 57, 25, 25);
  }
  text ( "created By Taha_HDP", 500, 650);
  fill(255);
  textFont(font1);
  textSize (30);
  text ("Mine sweeper", 80, 90);
  textSize (20);
  text (version, 280, 130);
  text ( "Bomb Number :", 430, 500);
  text ( "You must click on Box and then they give you a number between 0 - 8  \nIt means in the round of that Box is 0-8 mine .\nIf you click on mine you Lose !\nIf you find all number you Win !\n\nAIO compony (2020)", 30, 400);
  textSize (23);
  fill(0);
  text ("Start", 321, 634);
  text ("5           10          15", 443, 546 ) ;
  textSize (18);
  text ("Exit to Desktop", 60, 641);
}
public void base() {
  background(0);
  fill(196, 12, 58);
  textSize(15);
  text ( "created By Taha_HDP", 500, 650);
  textSize(20);
  text ( "bomb number : ", 50, 650);
  text ( bomb_number, 200, 650);
  PFont font1 = createFont("Lato Black", 1000);
  textFont(font1);
  textSize (30);
  text ("Mine sweeper", 240, 53);
  textSize(18);
  text (version, 430, 80);
  fill(94);
  stroke(255, 255, 255);
  rect(50, 100, 600, 500);
}
public void win_page() {
  if (vic_sound==true) {
    victory.play();
    vic_sound=false ;
  }  
  base();
  fill(50, 168, 72);
  stroke(50, 168, 72);
  rect(200, 200, 300, 250);
  fill(255, 255, 255);
  textSize(30);
  text ("!! victory !!", 265, 330);
  fill(204);
  stroke(0);
  rect(250, 630, 180, 40, 7);
  fill(0);
  PFont font1 = createFont("Lato Black", 1000);
  textFont(font1);
  textSize (23);
  text ("Play again", 280, 658);
  tryagain=true ;
  set_array();
  user_X=-1 ; 
  user_Y=-1;
}
public void lose_page() {
  if (lose_sound==true) {
    losing.play();
    lose_sound=false ;
  } 
  for (int i=0; i<bomb_number; i++) {
    int arz=133 ;
    int tool=73 ;
    for (int h=0; h<bomb_loc[i][1]; h++) {
      tool = tool+60 ;
    }
    for (int j=0; j<bomb_loc[i][0]; j++) {
      arz = arz+50 ;
    }
    image(mine, tool-13, arz-27, 40, 40);
  }
  fill(204);
  stroke(0);
  rect(250, 630, 180, 40, 7);
  fill(0);
  PFont font1 = createFont("Lato Black", 1000);
  textFont(font1);
  textSize (25);
  text ("try again", 280, 658);
  tryagain=true ;
}
public void set_loc() {
  bomb_loc = new int[bomb_number][2];
  for (int i=0; i<bomb_number; i++) {
    for (int j=0; j<2; j++) {
      bomb_loc[i][j] = PApplet.parseInt(random(0, 9));
      //print(bomb_loc[i][j]);
      //print(' ');
    }
    //println();
  }
}
public void set_array() {
  counter = new int[x][y];
  hide = new int[x][y];
  for (int i=0; i<x; i++) {
    for (int j=0; j<y; j++) {
      counter[i][j] = 0;
      hide[i][j] = -1;
    }
  }
}
public void calculate() {
  for (int i=0; i<x; i++) {
    for (int j=0; j<y; j++) {
      for (int h=0; h< bomb_number; h++) {
        if ( (bomb_loc[h][0] == i)&&(bomb_loc[h][1] == j) ) {
          counter[i][j]= -10 ;
        } 
        if ((bomb_loc[h][0] == i+1)&&(bomb_loc[h][1] == j)) {
          counter[i][j] ++ ;
        }
        if ((bomb_loc[h][0] == i-1)&&(bomb_loc[h][1] == j)) {
          counter[i][j] ++ ;
        }
        if ((bomb_loc[h][0] == i)&&(bomb_loc[h][1] == j+1)) {
          counter[i][j] ++ ;
        }
        if ((bomb_loc[h][0] == i)&&(bomb_loc[h][1] == j-1)) {
          counter[i][j] ++ ;
        }
        if ((bomb_loc[h][0] == i+1)&&(bomb_loc[h][1] == j+1)) {
          counter[i][j] ++ ;
        }
        if ((bomb_loc[h][0] == i-1)&&(bomb_loc[h][1] == j+1)) {
          counter[i][j] ++ ;
        }
        if ((bomb_loc[h][0] == i+1)&&(bomb_loc[h][1] == j-1)) {
          counter[i][j] ++ ;
        }
        if ((bomb_loc[h][0] == i-1)&&(bomb_loc[h][1] == j-1)) {
          counter[i][j] ++ ;
        }
      }
    }
  }
}
public void mousePressed() {

  if (tryagain==true && (mouseX<=430 && mouseX>=250 && mouseY>=630 && mouseY<=670) || (mouseX<=130 && mouseX>50 && mouseY>=40 && mouseY<=70)) {
    click.play();
    start=false ;
    tryagain=false ;
    win=false ;
    vic_sound=true ;
    lose_sound=true;
  } else if (tryagain==false&&start==true) {
    int xmin=60, xmax=100, ymin=105, ymax=145 ;
    for (int i=0; i<10; i++) {
      xmin=60;
      xmax=100;
      for (int j=0; j<10; j++) {
        if (mouseX<=130 && mouseX>50 && mouseY>=40 && mouseY<=70) {
          click.play();
          start=false ;
        }
        if (mouseX<=xmax && mouseX>=xmin && mouseY>=ymin && mouseY<=ymax) {
          click.play();
          user_X=i ;
          user_Y=j ;
        }
        xmin = xmin+60 ;
        xmax = xmax+60 ;
      }
      ymin = ymin+50 ;
      ymax = ymax+50 ;
    }
  } else if (start==false && mouseX<=400 && mouseX>=300 && mouseY>=600 && mouseY<=650) {
    click.play();
    set_array();
    user_X=-1 ; 
    user_Y=-1;
    set_loc();
    calculate();
    start=true ;
  } else if (start==false && mouseX<=470 && mouseX>=430 && mouseY>=520 && mouseY<=555) {
    click.play();
    bomb_number=5 ;
    collor1=145 ;
    collor2=255 ;
    collor3=255 ;
  } else if (start==false && mouseX<=540 && mouseX>=500 && mouseY>=520 && mouseY<=555) {
    click.play();
    bomb_number=10 ;
    collor1=255 ;
    collor2=145 ;
    collor3=255 ;
  } else if (start==false && mouseX<=610 && mouseX>=570 && mouseY>=520 && mouseY<=555) {
    click.play();
    bomb_number=15 ;
    collor1=255 ;
    collor2=255 ;
    collor3=145 ;
  } else if (start==false && mouseX<=670 && mouseX>=630 && mouseY>=50 && mouseY<=90) {
    click.play();
    if (sound == true) {
      click.amp(0.0f);
      music1.amp(0.0f);
      victory.amp(0.0f);
      losing.amp(0.0f);
      sound=false ;
    } else {
      click.amp(0.3f);
      music1.amp(0.1f);
      victory.amp(0.2f);
      losing.amp(0.2f);
      sound=true ;
    }
  } else if (start==false && mouseX<=200 && mouseX>=50 && mouseY>=620 && mouseY<=650) {
    exit();
  }
}

  public void settings() {  size(700, 700); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "mineswepper" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
