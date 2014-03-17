package com.example.speakandrunv2;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class EnnemiStatique extends Ennemi {

	public EnnemiStatique(Bitmap[] img, int canvasW) {
		super(img, canvasW);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(Canvas canvas, int i) {
		// TODO Auto-generated method stub
		
			y += 2;
		

		canvas.drawBitmap(img[frame], x, y, null);
	}

}
