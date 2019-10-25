package com.example.tappyspaceship01;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.Random;

public class GameEngine extends SurfaceView implements Runnable
{

    final static String TAG="CHIPPY";

    int screenHeight;
    int screenWidth;

    boolean gameIsRunning;

    Thread gameThread;

    SurfaceHolder holder;
    Canvas canvas;
    Paint paintbrush;

    Player gameplayer;


    ArrayList<Enemy> enemies = new ArrayList<Enemy>(25);
    Bitmap bg;

    float mouseX;
    float mouseY;
    String move = "";
    String fire = "";
    int life = 5;
    private ArrayList<Rect> eBullets = new ArrayList<Rect>();
    Player slowPower, incereaseLifePower;
    String powerLabel = "no", slowLabel = "no", hurdleLabel = "no";
    int ENEMY_SPEED = 30;
    int enemyMoveTime = 20;
    ArrayList<Enemy> hurdleFromLeft = new ArrayList<Enemy>(5);
    ArrayList<Enemy> hurdleFromBottom = new ArrayList<Enemy>(5);


//    Drawable drawable;


    public GameEngine(Context context, int w, int h)
    {
        super(context);

        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        this.screenWidth = w;
        this.screenHeight = h;

        this.printScreenInfo();

        gameplayer= new Player(getContext(), 100, 200, 0);

        this.bg = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg);
        this.bg = Bitmap.createScaledBitmap(
                this.bg,
                this.screenWidth,
                this.screenHeight,
                false);

//        this.drawable = getResources().getDrawable(R.drawable.bullet1);


        int myX = 1800, myY = 150;

        for (int i = 0; i < 25; i++)
        {
            if (i == 12)
            {
                Enemy e = new Enemy(getContext(), myX + ((i - 10) * 120), myY + 260, "chip");
                this.enemies.add(e);
            }
            else
            {
                Enemy e = new Enemy(getContext(), myX , myY, "wall");

                if (i < 5)
                {
                    e.setxPos(e.getxPos() + (i*120));
                    Enemy hurdle = new Enemy(getContext(), myX, (i* 310), "hurdle");
                    this.hurdleFromLeft.add(hurdle);

                    Enemy hurdle1 = new Enemy(getContext(), (i* 500), 1400 , "hurdle");
                    this.hurdleFromBottom.add(hurdle1);
                }
                else if (i >=5 && i< 10 )
                {
                    e.setxPos(e.getxPos() + ((i-5)*120));
                    e.setyPos(e.getyPos() + 130);

                }
                else if (i >=10 && i < 15)
                {
                    e.setxPos(e.getxPos() + ((i - 10) * 120));
                    e.setyPos(e.getyPos() + 260);
                }
                else if (i >=15 && i < 20)
                {
                    e.setxPos(e.getxPos() + ((i-15)*120));
                    e.setyPos(e.getyPos() + 390);
                }
                else if (i >=20 && i < 25)
                {
                    e.setxPos(e.getxPos() +((i-20)*120));
                    e.setyPos(e.getyPos() + 520);
                }
                this.enemies.add(e);
            }
        }

    }



    private void printScreenInfo()
    {
        Log.d(TAG, "Screen (w, h) = " + this.screenWidth + "," + this.screenHeight);
    }



    @Override
    public void run()
    {
        while (gameIsRunning == true)
        {
            this.updatePositions();
            this.redrawSprites();
            this.setFPS();
        }
    }


    public void pauseGame()
    {
        gameIsRunning = false;
        try
        {
            gameThread.join();
        } catch (InterruptedException e)
        {
            // Error
        }
    }

    public void startGame()
    {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }



    int loop = 0;
    public void updatePositions()
    {
        loop = loop +1;

//        if(numLoops % 20 ==0)
//        {
//            MediaPlayer mediaPlayer= MediaPlayer.create(getContext(),R.raw.bgsound);
//            mediaPlayer.start();
//        }

        if (move == "yes")
        {
            movePlayerToMouseClick(this.mouseX, this.mouseY);
        }
        if (fire == "yes")
        {
            this.spawnPlayerBullet();
            fire = "";
        }


        int BULLET_SPEED = 50;
        for (int i = 0; i < this.gameplayer.getPlayerBullets().size();i++)
        {
            Rect bullet = this.gameplayer.getPlayerBullets().get(i);
            bullet.left = bullet.left + BULLET_SPEED;
            bullet.right = bullet.right + BULLET_SPEED;
//            drawable.draw(canvas);
        }


        for (int i = 0; i < this.eBullets.size();i++)
        {
            Rect bullet = this.eBullets.get(i);
            bullet.left = bullet.left - BULLET_SPEED;
            bullet.right = bullet.right - BULLET_SPEED;
//            drawable.draw(canvas);
        }


        for (int i = 0; i < this.gameplayer.getPlayerBullets().size();i++)
        {
            Rect bullet = this.gameplayer.getPlayerBullets().get(i);

            if (bullet.left > screenWidth)
            {
                this.gameplayer.getPlayerBullets().remove(bullet);
            }

        }


        for (int i = 0; i < this.eBullets.size();i++)
        {
            Rect bullet = this.eBullets.get(i);

            if (bullet.right < 0)
            {
                this.eBullets.remove(bullet);
            }

        }





        for (int i = 0; i < this.eBullets.size();i++)
        {
            Rect bullet = this.eBullets.get(i);

            if (bullet.right < 0)
            {
                this.eBullets.remove(bullet);
            }

        }


        for (int i = 0; i < this.gameplayer.getPlayerBullets().size();i++)
        {
            Rect bullet = this.gameplayer.getPlayerBullets().get(i);
            for (int j = 0; j < enemies.size(); j++)
            {
                if (this.enemies.get(j).getEnemyHitbox().intersect(bullet))
                {
                    this.gameplayer.getPlayerBullets().remove(i);

                    if (this.enemies.get(j).getIsThisChip() == "yes")
                    {
                        if(this.enemies.get(j).getGotHit() == 10)
                        {
                            this.enemies.remove(j);
                            this.moveToWin();
                        }
                        else
                        {
                            this.enemies.get(j).setGotHit(this.enemies.get(j).getGotHit() + 1);
                        }

                    }
                    else
                    {
                        if(this.enemies.get(j).getGotHit() == 3)
                        {
//                        Log.d(TAG, "it is 3");
                            this.enemies.remove(j);
                        }
                        else
                        {
                            this.enemies.get(j).setGotHit(this.enemies.get(j).getGotHit() + 1);
                        }

                    }

                }
            }
        }



        for (int i = 0; i < this.eBullets.size();i++)
        {
            Rect bullet = this.eBullets.get(i);
                if (this.gameplayer.getPlayerHitbox().intersect(bullet))
                {
                    this.life = this.life - 1;
                    this.eBullets.remove(i);

                    if (this.life == -1)
                    {
                        this.moveToLose();
                    }

                }
        }


//        for (int i = 0; i < this.hurdleFromBottom.size();i++)
//        {
//            Rect hurdle = this.hurdleFromBottom.get(i).getEnemyHitbox();
//            if (this.gameplayer.getPlayerHitbox().intersect(hurdle))
//            {
//                this.life = this.life - 1;
//                this.hurdleFromBottom.remove(i);
//                if (this.life == -1)
//                {
//                    this.moveToLose();
//                }
//            }
//
//        }

        for (int i = 0; i < this.hurdleFromLeft.size();i++)
        {
            Rect hurdle = this.hurdleFromLeft.get(i).getEnemyHitbox();
            if (this.gameplayer.getPlayerHitbox().intersect(hurdle))
            {
                this.life = this.life - 1;
                this.hurdleFromLeft.remove(i);
                if (this.life == -1)
                {
                    this.moveToLose();
                }
            }

        }

        if (powerLabel == "yes")
        {
            this.incereaseLifePower.setxPos(this.incereaseLifePower.getxPos() - 20);
            this.incereaseLifePower.updateHitbox();
            if (this.incereaseLifePower.getPlayerHitbox().intersect(this.gameplayer.getPlayerHitbox()) == true)
            {
                this.life = this.life + 1;
                this.powerLabel = "no";
            }
            else if (this.incereaseLifePower.getxPos() < 0)
            {
                this.powerLabel = "no";
            }
        }

        if (slowLabel == "yes")
        {
            this.slowPower.setxPos(this.slowPower.getxPos() - 20);
            this.slowPower.updateHitbox();
            if (this.slowPower.getPlayerHitbox().intersect(this.gameplayer.getPlayerHitbox()) == true)
            {
                this.enemyMoveTime = this.enemyMoveTime + 10;
                this.slowLabel = "no";
            }
            else if (this.incereaseLifePower.getxPos() < 0)
            {
                this.slowLabel = "no";
            }
        }
        if (hurdleLabel == "yes")
        {
            for(int i=0; i < this.hurdleFromLeft.size(); i++)
            {
                this.hurdleFromLeft.get(i).setxPos(this.hurdleFromLeft.get(i).getxPos() - 20);
                this.hurdleFromLeft.get(i).updateHitbox();
                this.hurdleFromBottom.get(i).setyPos(this.hurdleFromBottom.get(i).getyPos() - 25);
                this.hurdleFromBottom.get(i).updateHitbox();

            }
        }



        if (loop % 200 == 0)
        {
            incereaseLifePower = new Player(getContext(), 2000, 400, 1 );
            this.powerLabel = "yes";
        }
        else if (loop % 300 == 0)
        {
            slowPower = new Player(getContext(), 2000, 400, 2 );
            this.slowLabel = "yes";
        }
        else if(loop % 100 == 0)
        {
            hurdleLabel = "yes";
        }

        if (loop % enemyMoveTime == 0)
        {
            Random random = new Random();
            int r = random.nextInt(3);

//            Log.d(TAG, "r is " + r);

            for (int i = 0; i < this.enemies.size(); i++)
            {
                if (r == 0)
                {
                    this.enemies.get(i).setxPos(this.enemies.get(i).getxPos() - ENEMY_SPEED);
                    this.enemies.get(i).updateHitbox();
                }
                else if (r == 1)
                {
                    this.enemies.get(i).setxPos(this.enemies.get(i).getxPos() + ENEMY_SPEED);
                    this.enemies.get(i).updateHitbox();
                }
                else if (r == 2)
                {
                    this.enemies.get(i).setyPos(this.enemies.get(i).getyPos() + ENEMY_SPEED);
                    this.enemies.get(i).updateHitbox();
                }
                else
                    {
                        this.enemies.get(i).setyPos(this.enemies.get(i).getyPos() - ENEMY_SPEED);
                        this.enemies.get(i).updateHitbox();
                    }
            }
        }

        if (loop % 10 == 0)
        {
            this.spawnEnemyBullet();
        }

    }



public void spawnPlayerBullet()
{
    Rect bullet = new Rect(this.gameplayer.getxPos(),
            this.gameplayer.getyPos() + this.gameplayer.getPlayerImage().getHeight() / 2,
            this.gameplayer.getxPos() + 50,
            this.gameplayer.getyPos() + this.gameplayer.getPlayerImage().getHeight() / 2 + 25
    );

//    this.drawable.setBounds(bullet);
    this.gameplayer.getPlayerBullets().add(bullet);

}
public void moveToLose()
{
        Intent intent = new Intent(this.getContext(),PlayerLoose.class);
        this.getContext().startActivity(intent);

}

    public void moveToWin()
    {
        Intent intent = new Intent(this.getContext(),PlayerWon.class);
        this.getContext().startActivity(intent);

    }



    public void spawnEnemyBullet()
    {
        Random random = new Random();
        int r = random.nextInt(this.enemies.size());
        Rect bullet = new Rect(this.enemies.get(r).getxPos(),
                this.enemies.get(r).getyPos() + this.enemies.get(r).getEnemyImage().getHeight() / 2,
                this.enemies.get(r).getxPos() + 50,
                this.enemies.get(r).getyPos() + this.enemies.get(r).getEnemyImage().getHeight() / 2 + 25
        );
        this.eBullets.add(bullet);
    }



    public void movePlayerToMouseClick(float mouseXPos, float mouseYPos)
    {
        // @TODO:  Move the square
        // 1. calculate distance between bullet and square
        double a = (mouseXPos - gameplayer.getxPos());
        double b = (mouseYPos - gameplayer.getyPos());
        double distance = Math.sqrt((a*a) + (b*b));

        // 2. calculate the "rate" to move
        double xn = (a / distance);
        double yn = (b / distance);

        // 3. move the bullet
        gameplayer.setxPos(gameplayer.getxPos() + (int)(xn * gameplayer.getSpeed()));
        gameplayer.setyPos(gameplayer.getyPos() + (int)(yn * gameplayer.getSpeed()));
        this.gameplayer.updateHitbox();
    }



    public void redrawSprites()
    {
        if (this.holder.getSurface().isValid())
        {
            this.canvas = this.holder.lockCanvas();

            this.canvas.drawColor(Color.argb(255,255,255,255));
            paintbrush.setColor(Color.WHITE);

            canvas.drawBitmap(this.bg, 0, 0, paintbrush);

            canvas.drawBitmap(this.gameplayer.getPlayerImage(), this.gameplayer.getxPos(), this.gameplayer.getyPos(), paintbrush);


            for (int i =0; i<enemies.size();i++)
            {
                    canvas.drawBitmap(enemies.get(i).getEnemyImage(), enemies.get(i).getxPos(), enemies.get(i).getyPos(), paintbrush);
            }


            if (powerLabel == "yes")
            {
                canvas.drawBitmap(this.incereaseLifePower.getPlayerImage(), this.incereaseLifePower.getxPos(), this.incereaseLifePower.getyPos(), paintbrush);
                canvas.drawRect(this.incereaseLifePower.getPlayerHitbox(), paintbrush);
            }

            if (slowLabel == "yes")
            {
                canvas.drawBitmap(this.slowPower.getPlayerImage(), this.slowPower.getxPos(), this.slowPower.getyPos(), paintbrush);
                canvas.drawRect(this.slowPower.getPlayerHitbox(), paintbrush);
            }


            if (hurdleLabel == "yes")
            {
                for (int i = 0; i < this.hurdleFromLeft.size(); i++)
                {
                    canvas.drawBitmap(this.hurdleFromLeft.get(i).getEnemyImage(), this.hurdleFromLeft.get(i).getxPos(), this.hurdleFromLeft.get(i).getyPos(), paintbrush);
                    canvas.drawBitmap(this.hurdleFromBottom.get(i).getEnemyImage(), this.hurdleFromBottom.get(i).getxPos(), this.hurdleFromBottom.get(i).getyPos(), paintbrush);

                }
            }


                for (int i = 0; i < this.enemies.size(); i++)
            {
                        canvas.drawBitmap(this.enemies.get(i).getEnemyImage(),
                                this.enemies.get(i).getxPos(),
                                this.enemies.get(i).getyPos(),
                                paintbrush);
                        canvas.drawRect(this.enemies.get(i).getEnemyHitbox(), paintbrush);
            }


            paintbrush.setColor(Color.GREEN);
            paintbrush.setStyle(Paint.Style.FILL);
            paintbrush.setTextSize(55);
            canvas.drawText("Lifes Left: " + this.life,this.screenWidth-700,120,paintbrush);
            canvas.drawText("Time: " + this.loop/10,300,120,paintbrush);
            canvas.drawText("Score: " + this.loop/30,600,120,paintbrush);


            for (int i = 0; i < this.gameplayer.getPlayerBullets().size(); i++)
            {
                Rect bullet = this.gameplayer.getPlayerBullets().get(i);
                canvas.drawRect(bullet, paintbrush);
//                drawable.draw(canvas);

            }

            paintbrush.setColor(Color.RED);
            paintbrush.setStyle(Paint.Style.FILL);


            for (int i = 0; i < this.eBullets.size(); i++)
            {
                Rect bullet = this.eBullets.get(i);
                canvas.drawRect(bullet, paintbrush);
//                drawable.draw(canvas);

            }



            paintbrush.setColor(Color.BLUE);
            paintbrush.setStyle(Paint.Style.STROKE);
            paintbrush.setStrokeWidth(5);

            //----------------
            this.holder.unlockCanvasAndPost(canvas);
        }
    }





    public void setFPS()
    {
        try
        {
            gameThread.sleep(60);
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int userAction = event.getActionMasked();
        //@TODO: What should happen when person touches the screen?
        if (userAction == MotionEvent.ACTION_DOWN)
        {

            int middleOfScreen = this.screenWidth / 2;
            if (event.getX() <= middleOfScreen)
            {
                move = "yes";
                this.mouseX = event.getX();
                this.mouseY = event.getY();
            }
            else if (event.getX() > middleOfScreen)
            {
                fire = "yes";
            }
        }
        else if (userAction == MotionEvent.ACTION_UP)
        {
            // user lifted their finger
        }
        return true;
    }
}