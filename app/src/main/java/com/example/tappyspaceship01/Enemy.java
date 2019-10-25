package com.example.tappyspaceship01;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.ArrayList;

public class Enemy {

    private Bitmap enemyImage;
    private Rect enemyHitbox;
    public int gotHit;



    private int xPos;
    private int yPos;
    String isThisChip;


    public Enemy(Context context, int xPosition, int yPosition, String chip)
    {
        this.xPos = xPosition;
        this.yPos = yPosition;
        this.gotHit = 0;

        if (chip == "chip")
        {
            this.enemyImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.chip);
            this.isThisChip = "yes";
        }
        else if (chip == "wall")
        {
            this.enemyImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemymatrix);
            this.isThisChip = "no";
        }
        else if (chip == "hurdle")
        {
            this.enemyImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet);
            this.isThisChip = "thisIsHurdle";

        }



        this.enemyHitbox = new Rect(
                this.xPos,
                this.yPos,
                this.xPos + this.enemyImage.getWidth(),
                this.yPos + this.enemyImage.getHeight()
        );


    }

    public Bitmap getEnemyImage() {
        return enemyImage;
    }

    public void setEnemyImage(Bitmap enemyImage) {
        this.enemyImage = enemyImage;
    }

    public Rect getEnemyHitbox() {
        return enemyHitbox;
    }

    public void setEnemyHitbox(Rect enemyHitbox) {
        this.enemyHitbox = enemyHitbox;
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

    public int getGotHit() {
        return gotHit;
    }

    public void setGotHit(int gotHit) {
        this.gotHit = gotHit;
    }

    public String getIsThisChip() {
        return isThisChip;
    }

    public void setIsThisChip(String isThisChip) {
        this.isThisChip = isThisChip;
    }

    public void updateHitbox() {
        this.enemyHitbox.left = this.xPos;
        this.enemyHitbox.top = this.yPos;
        this.enemyHitbox.right = this.xPos + this.enemyImage.getWidth();
        this.enemyHitbox.bottom = this.yPos + this.enemyImage.getHeight();
    }
}
