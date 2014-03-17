package com.example.speakandrunv2;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

// GameHandler pour le MainActivity
public class GameHandler extends Handler {

	private String TAG = this.toString();
	MainActivity main_act = null;
	Jeu jeu_act = null;
	int type;

	public GameHandler(MainActivity ma) {
		this.main_act = ma;
		type = 0;
	}
	
	public GameHandler(Jeu j) {
		this.jeu_act = j;
		type = 1;
		
	}

	@Override
	public void handleMessage(Message msg) {
		String word = null;

		Log.d("gameHandler", "gameHandler");
		word = msg.getData().getString("word");
		if(type==0) main_act.doAnAction(word);
		if(type==1) jeu_act.doAnAction(word);
		Log.d(TAG, "GAMEHANDLER word = " + word);


	}

}
