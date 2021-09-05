=====================================================
CLASS
=====================================================

CS547		: Computer Game Design 
Homework #2	: From pong to Breakout
Date		: September 12, 2011
	


======================================================
HOW TO EXECUTE THE PROGRAM
======================================================

Put the pong.jar file in the "jig-1.7.5-13" directory

move to under the "jig-1.7.5-13" directory 

on the command line
$java -jar pong.jar



=======================================================
DESCRIPTION
=======================================================
This is the breakout game with three stages. 
Players use the paddle and hit the ball to break bricks.
The feature details of this version are listed below:



Levels:

  Level 1 - Regular ball speed,         48 Normal bricks / 12 Hard bricks
  Level 2 - Ball speed increases (+5) , 72 Normal bricks / 12 Hard bricks
  Level 3 - Ball speed increases (+10), 72 Normal bricks / 24 Hard bricks
        

Bricks:
  
  Normal  - Hit once  to destroy. Light Green
  Hard    - Hit twice to destroy. Dark Green
  Power   - Hit once  to destroy. Drop a power-up when destroyed. White with 'P' letter 


Power-up:

  Bullets - When shooting power-up is taken, the paddle starts shooting 
            bullets automatically. Players do not need to click any buttons 
            to shoot bullets. 

Score:

  1 point - by destroying a brick with a ball
  2 points- by destroying a brick with bullets
  
(will flip this for the next version because destroying a brick with a ball is harder)



Cheat Codes:
Hit the key to see the feature on demand
10-key cannot be used for the number code
(Please hit the number '2' above 'Q' & 'W')
(Please hit the number '3' above 'W' & 'E')

  '2'       - Move onto the Level 2
  '3'       - Move onto the Level 3
  'd'       - Drop the power-up
  


=======================================================
SUBMITTED ITEMS
=======================================================


Description of each file added by me:


Ball.java   - The Ball class and contains ball's variables & methods
Brick.java  - The Brick class and contains brick's variables & methods
Bullet.java - The Bullet class and contains bullet's variables & methods
Paddle.java - The Paddle class and contains paddle's variables & methods
Pong.java   - The Pong class that contains variables, methods, and the main method
Power.java  - The Power class for power up items and contains variables & methods
brick1.png  - image of a light green normal brick
brick2.png  - image of a dark green hard brick
brick3.png  - image of a white power up brick
bullet.png  - image of a yellow bullet
power.png   - image of a power up item
jig-splash.png - image of my splash screen 

 

