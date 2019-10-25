package com.example.tappyspaceship01;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class Player {

    private Bitmap playerImage;
    private ArrayList<Rect> playerBullets = new ArrayList<Rect>();
    private Rect playerHitbox;
    private int speed = 20;


    public int xPos;
    public int yPos;



    public Player(Context context, int x, int y, int special)
    {

        this.xPos = x;
        this.yPos = y;

        if (special == 0)
        {
            this.playerImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);

        }
        else if (special == 1)
        {
            this.playerImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.medikit);

        }
        else if (special == 2)
        {
            this.playerImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.slow);

        }


        this.playerHitbox = new Rect(
                this.xPos,
                this.yPos,
                this.xPos + this.playerImage.getWidth(),
                this.yPos + this.playerImage.getHeight()
        );


    }

    public Bitmap getPlayerImage() {
        return playerImage;
    }

    public void setPlayerImage(Bitmap playerImage) {
        this.playerImage = playerImage;
    }

    public Rect getPlayerHitbox() {
        return playerHitbox;
    }

    public void setPlayerHitbox(Rect playerHitbox) {
        this.playerHitbox = playerHitbox;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public ArrayList<Rect> getPlayerBullets() {
        return playerBullets;
    }

    public void setPlayerBullets(ArrayList<Rect> playerBullets) {
        this.playerBullets = playerBullets;
    }

    public void updateHitbox() {
        this.playerHitbox.left = this.xPos;
        this.playerHitbox.top = this.yPos;
        this.playerHitbox.right = this.xPos + this.playerImage.getWidth();
        this.playerHitbox.bottom = this.yPos + this.playerImage.getHeight();
    }

}
