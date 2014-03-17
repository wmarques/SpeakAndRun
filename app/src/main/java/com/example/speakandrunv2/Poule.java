package com.example.speakandrunv2;

import android.graphics.Bitmap;
import android.graphics.Canvas;


public class Poule {
	int x;
	int y;
	int frame;
	int colonne;
	String mot=null;
	Bitmap img[];
	
	public Poule(Bitmap[] imgpoule) {
		super();
		this.frame = 0;
		this.img = imgpoule;
		this.colonne = 2;
	}
	
 
	
	public int getColonne() {
		return colonne;
	}
	public void setColonne(int colonne) {
		this.colonne = colonne;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public Bitmap[] getImg() {
		return img;
	}
	public void setImg(Bitmap[] img) {
		this.img = img;
	}
	
	public void update(Canvas canvas,int i, int canvasW) {
        if(i==20) frame++;
        
        if (frame == 4)
        	frame = 0;
        switch(colonne) {
        case 1 :
        	setX(20);
        	break;
        	
        case 2 :
        	setX(canvasW/2-img[0].getWidth()/2);
        	break;
        	
        case 3 :
        	setX(canvasW-img[0].getWidth()-20);
        	break;
        }
        canvas.drawBitmap(img[frame],x, y, null);

	}
}
