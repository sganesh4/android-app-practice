package com.example.tipcalc;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class TipCalc extends Activity {

	private static final String TOTAL_BILL="TOTAL_BILL";
	private static final String CURRENT_TIP="CURRENT_TIP";
	private static final String BILL_WITHOUT_TIP="BILL_WITHOUT_TIP";
	
	private double billBeforeTip;
	private double tipAmount;
	private double finalBill;
	
	private EditText billBeforeTipET;
	private EditText tipAmountET;
	private EditText finalBillET; 
	SeekBar tipSeekBar;
	private int[] checklistValues=new int[12];
	
	CheckBox friendlyCheckBox;
	CheckBox specialsCheckBox;
	CheckBox opinionCheckBox;
	
	RadioGroup availableRadioGroup;
	RadioButton availableBadRadioButton;
	RadioButton availableOkRadioButton;
	RadioButton availableGoodRadioButton;
	
	Button startChronometerButton;
	Button resetChronometerButton;
	Button pauseChronometerButton;
	
	Chronometer timeWaitingChronometer;
	Spinner problemsSpinner;
	long secondsYouWaited;
	TextView timeWaitingTextView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tip_calc);
		if(savedInstanceState==null){
			billBeforeTip=0.0;
			tipAmount=0.15;
			finalBill=0.0;
		}
		else {
			billBeforeTip=savedInstanceState.getDouble(BILL_WITHOUT_TIP);
			tipAmount=savedInstanceState.getDouble(CURRENT_TIP);
			finalBill=savedInstanceState.getDouble(TOTAL_BILL);
		}
		billBeforeTipET=(EditText)findViewById(R.id.billEditText);
		tipAmountET=(EditText)findViewById(R.id.tipEditText);
		finalBillET=(EditText)findViewById(R.id.finalBillEditText);
		tipSeekBar=(SeekBar)findViewById(R.id.changeTipSeekBar);
		 friendlyCheckBox=(CheckBox)findViewById(R.id.friendyCheckBox);
		 specialsCheckBox=(CheckBox)findViewById(R.id.specialsCheckBox);
		 opinionCheckBox=(CheckBox)findViewById(R.id.opinionCheckBox);
		
		 availableRadioGroup=(RadioGroup)findViewById(R.id.availableRadioGroup);
		 availableBadRadioButton=(RadioButton)findViewById(R.id.availableBadRadioButton);
		 availableOkRadioButton=(RadioButton)findViewById(R.id.availableOkRadioButton);
		 availableGoodRadioButton=(RadioButton)findViewById(R.id.availableGoodRadioButton);
		 startChronometerButton=(Button)findViewById(R.id.startChronometerButton);
		 resetChronometerButton=(Button)findViewById(R.id.resetChronometerButton);
		 pauseChronometerButton=(Button)findViewById(R.id.pauseChronometerButton);
		
		 timeWaitingChronometer=(Chronometer)findViewById(R.id.timeWaitingChronometer);
		 problemsSpinner=(Spinner)findViewById(R.id.problemSpinner);
		 setUpIntroCheckBoxes();
		 addChangeListenerToRadios();
		 addItemSelectedListenerToSpinner();
		 setButtonOnClickListeners();
		 timeWaitingTextView=(TextView)findViewById(R.id.timeWaitingTextView);
		
		tipSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				tipAmount = tipSeekBar.getProgress()*.01;
				tipAmountET.setText(String.format("%.02f", tipAmount));
				updateBillAndTipAmount();
				
			}
		});
		billBeforeTipET.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				try {
					billBeforeTip=Double.parseDouble(s.toString());
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					billBeforeTip=0.0;
				}
				updateBillAndTipAmount();
			}

			

			@Override
			public void afterTextChanged(Editable s) {
		
				
			}
			
		});
	}
	private void setButtonOnClickListeners() {
		startChronometerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int stoppedMilliseconds = 0;
				String chronoText=timeWaitingChronometer.getText().toString();
				String array[]=chronoText.split(":");
				if(array.length==2)
				{
					stoppedMilliseconds=Integer.parseInt(array[0])*60*1000+Integer.parseInt(array[1])*1000;
				}
				else if(array.length==3)
				{
					stoppedMilliseconds=Integer.parseInt(array[0])*60*60*1000+Integer.parseInt(array[1])*60*1000+Integer.parseInt(array[2])*1000;
				}
				timeWaitingChronometer.setBase(SystemClock.elapsedRealtime()-stoppedMilliseconds);
				secondsYouWaited=Long.parseLong(array[1]);
				updateTipBasedOnWaitedTime(secondsYouWaited);
				timeWaitingChronometer.start();
			}
		});
		pauseChronometerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				timeWaitingChronometer.stop();
			}
		});
		resetChronometerButton.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				timeWaitingChronometer.setBase(SystemClock.elapsedRealtime());
				secondsYouWaited=0;
		
			}
		});	
		
	}
	private void updateTipBasedOnWaitedTime(long secondsYouWaited) {
		checklistValues[9]=(secondsYouWaited>10)?-2:2;
		setTipFromWaitress();
		updateBillAndTipAmount();
		
	}
	private void addItemSelectedListenerToSpinner() {
		
		problemsSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				checklistValues[6]=(problemsSpinner.getSelectedItem().equals("Bad"))?-1:0;
				checklistValues[7]=(problemsSpinner.getSelectedItem().equals("OK"))?3:0;
				checklistValues[8]=(problemsSpinner.getSelectedItem().equals("Good"))?6:0;
				setTipFromWaitress();
				updateBillAndTipAmount();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	private void setUpIntroCheckBoxes() {
		friendlyCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checklistValues[0]=(friendlyCheckBox.isChecked())?4:0;
				setTipFromWaitress();
				
				
			}
		});
		specialsCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checklistValues[1]=(friendlyCheckBox.isChecked())?1:0;
				setTipFromWaitress();
				
				
			}
		});
		opinionCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checklistValues[2]=(opinionCheckBox.isChecked())?2:0;
				setTipFromWaitress();
				
				
			}
		});
	}
	private void setTipFromWaitress() {
		int checklistTotal=0;
		for (int item:checklistValues)
		{
			checklistTotal+=item;
		}
		tipAmountET.setText(String.format("%.02f", .01*checklistTotal));
		
	}
	private void addChangeListenerToRadios() {
		availableRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				checklistValues[3]=(availableBadRadioButton.isChecked())?-1:0;
				checklistValues[4]=(availableOkRadioButton.isChecked())?2:0;
				checklistValues[5]=(availableGoodRadioButton.isChecked())?4:0;
				setTipFromWaitress();
				
			}
		});
		
	}
	private void updateBillAndTipAmount() {
		// TODO Auto-generated method stub
		double tipAmount=Double.parseDouble(tipAmountET.getText().toString());
		double finalBill=billBeforeTip+(billBeforeTip*tipAmount);
		finalBillET.setText(String.format("%.02f", finalBill));
		
	}
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putDouble(BILL_WITHOUT_TIP, billBeforeTip);
		outState.putDouble(CURRENT_TIP, tipAmount);
		outState.putDouble(TOTAL_BILL, finalBill);
	}
	
}
