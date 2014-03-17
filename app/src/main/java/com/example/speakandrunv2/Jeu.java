package com.example.speakandrunv2;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Jeu extends Activity implements View.OnClickListener {

	PouleView maView;
	TestThread monThread;
	Button buttonG;
	Button buttonD;
	TextView score;
	Handler mHandler;
	TextView best;

	public String word = null;
	public GameThread gt = null;
	public GameHandler gh = null;
	public SpeechRecognizer sr = null;
	private String TAG = this.toString();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jeu);
		SharedPreferences sharedPreferences = getSharedPreferences("HighScores",0);
		int high = sharedPreferences.getInt("score", 0);
		

		gh = new GameHandler(this);
		mHandler = new Handler();

		// Background recognition service
		//startRecognitionService();
		Log.d(TAG, "Reconnaissance vocale lancee");

		if (gt == null) {
			gt = new GameThread();
			gt.setHandler(gh);
			gt.start();
		}

		buttonG = (Button) findViewById(R.id.Button01);
		buttonD = (Button) findViewById(R.id.Button02);
		score = (TextView) findViewById(R.id.score);
		best = (TextView) findViewById(R.id.best);
		maView = (PouleView) findViewById(R.id.gameView);
		maView.setScore(score);
		monThread = maView.getThread();
		maView.setScore(score);
		best.setText("HighScore : " + String.valueOf(high));

		buttonG.setOnClickListener(this);
		buttonD.setOnClickListener(this);

	}
	
	

	public void doAnAction(String word) {

		if (word != null) {

			if (word.contains("ch")) { // gauche
				if (monThread.maPoule.getColonne() != 1) {
					monThread.maPoule
							.setColonne(monThread.maPoule.getColonne() - 1);
				}
				Log.d(TAG, word);
			}

			if (word.contains("oi")) { // droite
				if (monThread.maPoule.getColonne() != 3) {
					monThread.maPoule
							.setColonne(monThread.maPoule.getColonne() + 1);
				}

				Log.d(TAG, word);
			}

		}

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Button01:
			if (monThread.maPoule.getColonne() != 1)
				monThread.maPoule
						.setColonne(monThread.maPoule.getColonne() - 1);

			break;

		case R.id.Button02:
			if (monThread.maPoule.getColonne() != 3)
				monThread.maPoule
						.setColonne(monThread.maPoule.getColonne() + 1);
			break;
		}
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

		//stopRecognitionService();
	}

	public void startRecognitionService() {

		if (sr != null) {
			sr.destroy();
		}

		sr = SpeechRecognizer.createSpeechRecognizer(this);
		sr.setRecognitionListener(new listener());
		sr.startListening(getIntent());

	}

	public Intent getIntent() {

		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
				this.getPackageName());
		intent.putExtra(
				RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,
				1000);
		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
		return intent;

	}

	class listener implements RecognitionListener {
		@Override
		public void onRmsChanged(float arg0) {
			

		}

		@Override
		public void onResults(Bundle arg0) {

			String res = arg0
					.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
					.get(0).toString();
			Log.d(TAG, "onResults " + res);

			gt.mot_reco_vocale(res);

			//startRecognitionService();
			sr.startListening(getIntent());
		}

		@Override
		public void onReadyForSpeech(Bundle arg0) {
			

		}

		@Override
		public void onPartialResults(Bundle arg0) {
			

		}

		@Override
		public void onEvent(int arg0, Bundle arg1) {
			
			Log.d(TAG, "onEvent");

		}

		@Override
		public void onError(int arg0) {
			
			Log.d(TAG, "onError : " + arg0);

			switch (arg0) {
			case 2:
				createDialog();
				break;

			default:
				//startRecognitionService();
				sr.startListening(getIntent());
				break;
			}

		}

		@Override
		public void onEndOfSpeech() {

		}

		@Override
		public void onBufferReceived(byte[] arg0) {

		}

		@Override
		public void onBeginningOfSpeech() {

		}
	}


	public void stopRecognitionService() {
		if (sr != null) {

			// sr.stopListening();
			sr.destroy();
		}
	};

	public void createDialog() {

		/*AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Add the buttons
		builder.setNeutralButton("Continue",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						startRecognitionService();
					}
				});

		// Create the AlertDialog
		AlertDialog dialog = builder.create();
		dialog.setTitle("Network Connection");
		dialog.setMessage("An error occured while trying to connect to Internet. Please check that you are well connected and press continue.");
		dialog.show();*/
		
		Toast.makeText(this, "Probleme r�seau ! Pas de reconnaissance vocale !", Toast.LENGTH_SHORT).show();

	}

}