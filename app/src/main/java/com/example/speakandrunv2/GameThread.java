package com.example.speakandrunv2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GameThread extends Thread {

    // Attributes
    private String TAG = this.toString();
    private boolean stop = false;
    private Handler gh = null;
    private Message msg = null;
    private Bundle bundle = null;

    private String motCommandeVocale = null;

    public GameThread() {
	Log.d(TAG, " GameThread()");
    }

    public void mot_reco_vocale(String word_voice_recognition) {
	motCommandeVocale = word_voice_recognition;
    }

    @Override
    public void run() {

	/*do {
	    

	    // Communication avec Activit
	    if (motCommandeVocale != null) {
		msg = gh.obtainMessage(); // r�cup�ration message
		bundle = new Bundle(); // infos
		// Ajouter des données à transmettre au Handler via le Bundle
		//bundle.putBoolean("ALIVE", true);
		//bundle.putInt("SCORE", count);
		//bundle.putString("word", motCommandeVocale);

		// Ajouter le Bundle au message
		msg.setData(bundle);
		// Envoyer le message
		gh.sendMessage(msg);

		motCommandeVocale = null;
	    }

	
	} while (stop != true);*/

	Log.d(TAG, "finish()");

    }

    public String getMotRecoVocale()
    {
	return motCommandeVocale;
    }

    public void finish() {
	stop = true;
    }

    public void setHandler(Handler handler) {
	this.gh = handler;

    }

}
