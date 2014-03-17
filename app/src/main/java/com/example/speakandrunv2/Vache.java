package com.example.speakandrunv2;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Vache extends Ennemi {
	
	public Vache(Bitmap[] img, int canvasW) {
		super(img, canvasW);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(Canvas canvas, int i) {
		// TODO Auto-generated method stub
		
		if(i == 20) {
			frame++;
			
		}
		y += 2;

		if (frame == 3) frame = 0;

		canvas.drawBitmap(img[frame], x, y, null);

	}

}
