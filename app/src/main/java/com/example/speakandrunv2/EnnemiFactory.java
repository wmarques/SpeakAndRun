package com.example.speakandrunv2;

import java.util.Random;

import android.graphics.Bitmap;

public class EnnemiFactory {

	private static Random r = new Random();
	private Bitmap[][] tabImages;
	
	public EnnemiFactory(Bitmap[][] tabImages) {
		// TODO Auto-generated constructor stub
		this.tabImages = tabImages;
	}
	
	public Ennemi genereEnnemi(int canvasW){
		
		switch(Math.abs(r.nextInt()%3)){
		
		case 0: return new Vache(tabImages[0], canvasW);
		
		case 1: return new EnnemiStatique(tabImages[1], canvasW);
		
		case 2: return new EnnemiStatique(tabImages[2], canvasW);
		
		default: return null;
		}
	}

}
