package com.example.brewclock;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
//Be sure not to import
//`android.content.dialoginterface.OnClickListener`.
import android.view.View;
import android.view.View.OnClickListener; 

public class BrewClockActivity extends Activity
implements OnClickListener {

	protected Button brewAddTime;
	protected Button brewDecreaseTime;
	protected Button startBrew;
	protected TextView brewCountLabel;
	protected TextView brewTimeLabel;
	protected int brewTime = 3;
	protected CountDownTimer brewCountDownTimer;
	protected int brewCount = 0;
	protected boolean isBrewing = false;
	protected int leftSeconds = 0;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Connect interface elements to properties
		brewAddTime = (Button) findViewById(R.id.brew_time_up);
		brewDecreaseTime = (Button) findViewById(R.id.brew_time_down);
		startBrew = (Button) findViewById(R.id.brew_start);
		brewCountLabel = (TextView) findViewById(R.id.brew_count_label);
		brewTimeLabel = (TextView) findViewById(R.id.brew_time);

		brewAddTime.setOnClickListener(this);
		brewDecreaseTime.setOnClickListener(this);
		startBrew.setOnClickListener(this);
		setBrewCount(brewCount);
		setBrewTime(brewTime);
	}

	/**
	 * Add observers on button click
	 */
	public void onClick(View v) {
		if(v == brewAddTime)
			setBrewTime(brewTime + 1);
		else if(v == brewDecreaseTime)
			setBrewTime(brewTime -1);
		else if(v == startBrew) {
			if(isBrewing)
				stopBrew();
			else
				startBrew();
		}
	}

	/**
	 * Set an absolute value for the number of minutes to brew.
	 * Has no effect if a brew is currently running.
	 * @param minutes The number of minutes to brew.
	 */
	public void setBrewTime(int minutes) {
		if(isBrewing)
			return;

		brewTime = minutes;

		if(brewTime < 1)
			brewTime = 1;

		brewTimeLabel.setText(String.format(getResources().getString(R.string.min), String.valueOf(brewTime)));
	}

	/**
	 * Set the number of brews that have been made, and update
	 * the interface.
	 * @param count The new number of brews
	 */
	public void setBrewCount(int count) {
		brewCount = count;
		brewCountLabel.setText(String.valueOf(brewCount));
	}

	/**
	 * Start the brew timer
	 */
	public void startBrew() {
		int Timer;

		if(leftSeconds > 0) {
			Timer = leftSeconds;
		} else {
			Timer = brewTime * 60;
		}

		// Create a new CountDownTimer to track the brew time
		brewCountDownTimer = new CountDownTimer(Timer * 1000, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				leftSeconds = (int) millisUntilFinished / 1000;
				brewTimeLabel.setText(String.format(getResources().getString(R.string.sec), leftSeconds));
			}

			@Override
			public void onFinish() {
				isBrewing = false;
				setBrewCount(brewCount + 1);

				brewTimeLabel.setText(R.string.brew_up);
				startBrew.setText(R.string.start);
				startBrew.setBackgroundColor(Color.GREEN);
				leftSeconds = 0;
			}
		};

		brewCountDownTimer.start();
		startBrew.setText(R.string.stop);
		startBrew.setBackgroundColor(Color.RED);
		isBrewing = true;
	}

	/**
	 * Stop the brew timer
	 */
	public void stopBrew() {
		if(brewCountDownTimer != null)
			brewCountDownTimer.cancel();

		isBrewing = false;
		startBrew.setText(R.string.start);
		startBrew.setBackgroundColor(Color.GREEN);
	}
	
	/**
	 * Save Activity State 
	 */
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putBoolean("isBrewing", isBrewing);
		savedInstanceState.putInt("brewCount", brewCount);
		savedInstanceState.putInt("brewTime", brewTime);
		savedInstanceState.putInt("leftSeconds", leftSeconds);
		super.onSaveInstanceState(savedInstanceState);
	}
	
	
	/**
	 * Restore Activity State
	 */
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		isBrewing = savedInstanceState.getBoolean("isBrewing");
		brewCount = savedInstanceState.getInt("brewCount");
		brewTime = savedInstanceState.getInt("brewTime");
		leftSeconds = savedInstanceState.getInt("leftSeconds");
		setBrewCount(brewCount);
		if (isBrewing) {
			startBrew();
		} else {
			setBrewTime(brewTime);
		}
	}
}