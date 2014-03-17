package com.example.speakandrunv2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {


	// Attributes
	private String TAG = this.toString();
	public String word = null;
	private SpeechRecognizer sr = null;
	private GameThread gt = null;
	private GameHandler gh = null;
	Button jouer = null;
	Button scores = null;
	Button quitter = null;
	Jeu jeu = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		gh = new GameHandler(this);

		// Background recognition service
		//startRecognitionService();
		Log.d(TAG, "Reconnaissance vocale lancee");


		/*gt = new GameThread();
		gt.setHandler(gh);
		gt.start();*/


		this.jouer = (Button) findViewById(R.id.button1);
		
		this.quitter = (Button) findViewById(R.id.button3);

		jouer.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(getApplicationContext(), Jeu.class);
				startActivity(intent);
			}
		});



		quitter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

	}


	public void doAnAction(String word) {

		if (word != null) {
			Log.d(TAG, "back in activity !");

			if (word.contains("jouer") || word.contains("jouet") || word.contains("jou�")) {

				Log.d(TAG, word);

				Intent intent = new Intent(this, Jeu.class);
				startActivity(intent);
			}


			if (word.contains("quitter")) {
				// Fermeture Activité courante
				Log.d(TAG, word);
				finish();
			}
		}
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

		Log.d(TAG, "activity destroyed");

	}

	protected void onResume() {
		super.onResume();

		// Background recognition service
		//startRecognitionService();

		if(gt==null) {
			Log.d(TAG,"GameThread cr�� dans onResume");
			gt = new GameThread();
			gt.setHandler(gh);
			gt.start();
		}

	}

	protected void onPause() {
		super.onPause();

		//stopRecognitionService();

		//if (gt != null && gt.isAlive()) {
		gt.interrupt();
		gt=null;
		//}
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
		
		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
		return intent;

	}

	class listener implements RecognitionListener {
		@Override
		public void onRmsChanged(float arg0) {
			// Log.d(TAG, "onRmsChanged");

		}

		@Override
		public void onResults(Bundle arg0) {

			String res = arg0
					.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
					.get(0).toString();
			Log.d(TAG, "onResults " + res);

			gt.mot_reco_vocale(res);

			//startRecognitionService();
			//sr.startListening(getIntent());
		}

		@Override
		public void onReadyForSpeech(Bundle arg0) {
			// Log.d(TAG, "onReadyForSpeech");

		}

		@Override
		public void onPartialResults(Bundle arg0) {
			// Log.d(TAG, "onPartialResults");

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
			Log.d(TAG, "onEndOfSpeech");

		}

		@Override
		public void onBufferReceived(byte[] arg0) {
			Log.d(TAG, "onBufferReceived");

		}

		@Override
		public void onBeginningOfSpeech() {
			Log.d(TAG, "onBeginningOfSpeech");

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
