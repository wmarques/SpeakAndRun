package com.example.speakandrunv2;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public abstract class Ennemi {
	int x;
	int y;
	int frame;
	int colonne;
	Bitmap img[];
	
	static Random r = new Random();
	
	public Ennemi(Bitmap img[], int canvasW) {
		super();
		
		this.frame = 0;
		this.img = img;
		this.y = -img[0].getHeight();
		this.x = 20;
		
		int i = r.nextInt()%3;
		
		if(i == 0){
			this.colonne = 1;
			this.x = 20;
		}
		
		if(i == 1){
			this.colonne = 2;
			this.x = canvasW/2-img[0].getWidth()/2;
		}
		
		if(i == 2){
			this.colonne = 3;
			this.x = canvasW-img[0].getWidth()-20;
		}
	}

	public int getColonne() {
		return colonne;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	abstract public void update(Canvas canvas, int i);
		
}