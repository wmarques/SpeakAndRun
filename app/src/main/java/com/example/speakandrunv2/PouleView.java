package com.example.speakandrunv2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

public class PouleView extends SurfaceView implements SurfaceHolder.Callback {

	private TestThread _thread;
	TextView scoreView;

	public PouleView(Context context, AttributeSet attrs) {
		super(context,attrs);
		getHolder().addCallback(this);
		_thread = new TestThread(getHolder(),context, new Handler() {
			public void handleMessage (Message m) {
				scoreView.setText(m.getData().getString("score"));
			}
		});
		setFocusable(true);
	}

	public TestThread getThread() {
		return _thread;
	}
	
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		_thread.setSurfaceSize(width, height);

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		_thread.setRunning(true);
		_thread.start();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		_thread.setRunning(false);
		boolean retry = true;
		
		while(retry) {
			try {
				_thread.join();
				retry = false;
			}
			catch(Exception e) {
			}
		}

	}
	
	public void setScore(TextView tv) {
		scoreView = tv;
	}
	
	
}

class TestThread extends Thread {
	
	//Déclaration des ressources
	SurfaceHolder _surfaceHolder;
	boolean _run = false;
	Resources mRes;
	private Context mContext;
	String word;
	int score;
	//TextView scoreView;
	private Handler mHandler;
	
	Bitmap background;
	Bitmap imgpoule[] = new Bitmap[4];
	Bitmap imgVache[] = new Bitmap[3];
	Bitmap imgTrou[] = new Bitmap[1];
	Bitmap imgCaillou[] = new Bitmap[1];
	Bitmap[][] tabImages = new Bitmap[3][];
	
	EnnemiFactory ennemiFactory;
	
	Poule maPoule;
	List<Ennemi> listeEnnemi = new ArrayList<Ennemi>();
	
	int canvasW = 1;
	int canvasH = 1;
	
	private int depY=0;
	Handler handdialog = new Handler();
	int incrscore;
		
	//Constructeur du Thread
	public TestThread(SurfaceHolder surfaceHolder, Context ctx, Handler handler) {
		
		_surfaceHolder = surfaceHolder;
		mContext = ctx;
		_run = false;
		mRes = ctx.getResources();
		score = 0;
		mHandler = handler;
		incrscore=0;
		
		background = BitmapFactory.decodeResource(mRes,R.drawable.grass);
		
		imgpoule[0] = BitmapFactory.decodeResource(mRes,R.drawable.poule1);
		imgpoule[1] = BitmapFactory.decodeResource(mRes,R.drawable.poule2);
		imgpoule[2] = BitmapFactory.decodeResource(mRes,R.drawable.poule3);
		imgpoule[3] = BitmapFactory.decodeResource(mRes,R.drawable.poule4);
		
		imgVache[0] = BitmapFactory.decodeResource(mRes,R.drawable.vache1);
		imgVache[1] = BitmapFactory.decodeResource(mRes,R.drawable.vache2);
		imgVache[2] = BitmapFactory.decodeResource(mRes,R.drawable.vache3);
		
		imgTrou[0] = BitmapFactory.decodeResource(mRes,R.drawable.hole);
		imgCaillou[0] = BitmapFactory.decodeResource(mRes,R.drawable.rock);
		
		maPoule = new Poule(imgpoule);
		maPoule.setColonne(2);
		
		
		
		tabImages[0] = imgVache;
		tabImages[1] = imgTrou;
		tabImages[2] = imgCaillou;
		
		ennemiFactory = new EnnemiFactory(tabImages);
	}

	public void setRunning (boolean run) {
		_run = run;
	}
	
	public void setSurfaceSize(int width, int height) {
		synchronized (_surfaceHolder) {
            canvasW = width;
            canvasH = height;
    		maPoule.setY(canvasH-100);

            // don't forget to resize the background image
            background = Bitmap.createScaledBitmap(background, width, height, true);
        }
	}

	@Override
	public void run() {
		Canvas c;
		
		
		int i=0,j=0;
		while(_run) {
			i++;
			j++;
			incrscore++;
			
			if(incrscore==10) {
				score++;
				incrscore=0;
			}
			
			Message msg = mHandler.obtainMessage();
			Bundle b = new Bundle();
			b.putString("score", String.valueOf(score));
			msg.setData(b);
			mHandler.sendMessage(msg);

			c = null;
			try {
				c = _surfaceHolder.lockCanvas(null);
				synchronized (_surfaceHolder) {
						onDraw(c,i);
						maPoule.update(c,i,canvasW);
						
						
						for(Ennemi unEnnemi:listeEnnemi) {
							unEnnemi.update(c,i);
						}
						
						
						}
			} finally {
				if (c != null) {
					_surfaceHolder.unlockCanvasAndPost(c);
				}
			}
			
			if(i==20) i=0;
			
			if(j==400) {
				listeEnnemi.add(ennemiFactory.genereEnnemi(canvasW));
				j=0;
			}
			
			
			for(Iterator<Ennemi> it = listeEnnemi.iterator(); it.hasNext();) {
				Ennemi unEnnemi = it.next();
				
				if(unEnnemi.y >= canvasH){
					it.remove();
					//break;
				}
				
				if(unEnnemi.y + unEnnemi.img[0].getHeight() > maPoule.y && unEnnemi.colonne == maPoule.colonne) {
					
					handdialog.post(new Runnable() {
						public void run() {
							setRunning(false);
							showDialogScores();

						}
					});
					
				}

			}
		}
	}
	
	public void showDialogScores() {
		
		SharedPreferences sharedPreferences = mContext.getSharedPreferences("HighScores",0);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		int high = sharedPreferences.getInt("score", 0);
		
		if(score<=high) {
			Toast.makeText(mContext, "Perdu !", Toast.LENGTH_SHORT).show();
		}
		
		else {
			Toast.makeText(mContext, "Meilleur score battu !", Toast.LENGTH_SHORT).show();
			editor.putInt("score", score);
			
			editor.commit(); 
			
		}
		
		((Activity) mContext).finish();
		this.interrupt();
		
	}
	
	
	//Boucle de jeu
	public void onDraw (Canvas canvas,int i) {
		
		// on décrémente les Y
        depY = depY + 2;
        int newY = depY + background.getHeight();

        // Si on a scrollé tout le background on remet à zéro
        if (depY >= 0) {
            depY = -background.getHeight();
            // On dessine juste la bitmap en entier
            canvas.drawBitmap(background, 0, 0, null);

        } else {
            // On dessine la bitmap et le bout restant
        	canvas.drawBitmap(background, 0, depY, null);
        	canvas.drawBitmap(background, 0, newY, null);
        }
	}

	
}