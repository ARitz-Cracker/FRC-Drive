package com.aquamorph.frcdrive;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnGenericMotionListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.aquamorph.frcdrive.Joystick;
import com.aquamorph.frcdrive.R;
import com.aquamorph.frcdrive.Joystick.OnChangeListener;

public class UIManager {
	public boolean enabled = false;
	public boolean auto = false;
	public byte joy1X = 0;
	public byte joy1Y = 0;
	public byte joy2X = 0;
	public byte joy2Y = 0;
	public boolean[] Joy1Bttns = new boolean[12];
	public boolean[] Joy2Bttns = new boolean[12];

	private Joystick joystick1;
	private Joystick joystick2;
	private ToggleButton enableBttn;
	private RadioButton enableAuto;
	private ViewGroup Joy1Buttons;
	private ViewGroup Joy2Buttons;
	private View mainView;

	@SuppressLint("NewApi")
	public UIManager(Activity activity) {

		// Initialize UI components.
		joystick1 = (Joystick) activity.findViewById(R.id.joystick1);
		joystick2 = (Joystick) activity.findViewById(R.id.joystick2);
		enableBttn = (ToggleButton) activity.findViewById(R.id.enable_button);
		enableAuto = (RadioButton) activity.findViewById(R.id.run_autonomous);
		mainView = (View) activity.findViewById(R.id.controls);

		// Set event listeners
		joystick1.setOnChangeListener(joyListener1);
		joystick2.setOnChangeListener(joyListener2);
		enableBttn.setOnCheckedChangeListener(enableListener);
		enableAuto.setOnCheckedChangeListener(autoListener);
		mainView.setOnGenericMotionListener(phyJoystickListener);
		mainView.setOnKeyListener(phyButtonListener);
		// Don't need tele listener because auto listener takes off auto
		// mode and puts in tele.

		// Joystick one buttons
		Joy1Buttons = (ViewGroup) activity.findViewById(R.id.Joy1Buttons);
		for (int i = 0; i < Joy1Buttons.getChildCount(); i++) {
			final int x = i;
			// Add listeners to the buttons.
			final Button bttn = (Button) Joy1Buttons.getChildAt(i);

			bttn.setOnTouchListener(new OnTouchListener() {

				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View view, MotionEvent event) {
					Log.d("Buttons", "Button hit: " + x);
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						Joy1Bttns[x] = true;
						break;
					case MotionEvent.ACTION_UP:
						Joy1Bttns[x] = false;
						break;
					}
					return false;
				}
			});
		}

		// Joystick two buttons
		Joy2Buttons = (ViewGroup) activity.findViewById(R.id.Joy2Buttons);
		for (int i = 0; i < Joy2Buttons.getChildCount(); i++) {
			final int x = i;
			// Add listeners to the buttons.
			final Button bttn = (Button) Joy2Buttons.getChildAt(i);

			bttn.setOnTouchListener(new OnTouchListener() {

				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View view, MotionEvent event) {
					Log.d("Buttons", "Button hit: " + x);
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						Joy2Bttns[x] = true;
						break;
					case MotionEvent.ACTION_UP:
						Joy2Bttns[x] = false;
						break;
					}
					return false;
				}
			});
		}
	}

	// Joystick1
	OnChangeListener joyListener1 = new OnChangeListener() {

		@Override
		public boolean onChange(byte xAxis, byte yAxis) {
			joy1X = (byte) (xAxis*(100/77));
			joy1Y = yAxis;
			return false;
		}
	};

	// Joystick2
	OnChangeListener joyListener2 = new OnChangeListener() {

		@Override
		public boolean onChange(byte xAxis, byte yAxis) {
			joy2X = xAxis;
			joy2Y = yAxis;
			return false;
		}
	};

	// Enable button
	OnCheckedChangeListener enableListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton button, boolean value) {
			if (value) {
				enabled = true;
			} else {
				enabled = false;
			}
		}
	};

	// Autonimous radio
	OnCheckedChangeListener autoListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton button, boolean value) {
			// Disable robot when changing modes.
			enabled = false;
			enableBttn.setChecked(false);
			if (value)
				auto = true;
			else
				auto = false;
		}

	};

	// Listener for physical gamepad or controller without buttons.
	OnGenericMotionListener phyJoystickListener = new OnGenericMotionListener() {

		@SuppressLint("InlinedApi")
		@Override
		public boolean onGenericMotion(View view, MotionEvent event) {
			if (event.getSource() == InputDevice.SOURCE_JOYSTICK
					&& event.getAction() == MotionEvent.ACTION_MOVE) {

				// Joystick 1 on the controller
				joy1X = (byte) (127 * event.getAxisValue(MotionEvent.AXIS_X));
				joy1Y = (byte) (127 * event.getAxisValue(MotionEvent.AXIS_Y));

				// Joystick 2
				joy2X = (byte) (127 * event.getAxisValue(MotionEvent.AXIS_Z));
				joy2Y = (byte) (127 * event.getAxisValue(MotionEvent.AXIS_RZ));
			}
			return true;
		}

	};

	// Physical button listener.
	OnKeyListener phyButtonListener = new OnKeyListener() {

		@Override
		public boolean onKey(View view, int keyCode, KeyEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_BUTTON_1:
					Joy1Bttns[0] = true;
					break;
				case KeyEvent.KEYCODE_BUTTON_2:
					Joy1Bttns[1] = true;
					break;
				case KeyEvent.KEYCODE_BUTTON_3:
					Joy1Bttns[2] = true;
					break;
				case KeyEvent.KEYCODE_BUTTON_4:
					Joy1Bttns[3] = true;
					break;
				case KeyEvent.KEYCODE_BUTTON_5:
					Joy1Bttns[4] = true;
					break;
				case KeyEvent.KEYCODE_BUTTON_6:
					Joy1Bttns[5] = true;
					break;
				case KeyEvent.KEYCODE_BUTTON_7:
					Joy1Bttns[6] = true;
					break;
				case KeyEvent.KEYCODE_BUTTON_8:
					Joy1Bttns[7] = true;
					break;
				case KeyEvent.KEYCODE_BUTTON_9:
					Joy1Bttns[8] = true;
					break;
				case KeyEvent.KEYCODE_BUTTON_10:
					Joy1Bttns[9] = true;
					break;
				case KeyEvent.KEYCODE_BUTTON_11:
					Joy1Bttns[10] = true;
					break;
				case KeyEvent.KEYCODE_BUTTON_12:
					Joy1Bttns[11] = true;
				}
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_BUTTON_1:
					Joy1Bttns[0] = false;
					break;
				case KeyEvent.KEYCODE_BUTTON_2:
					Joy1Bttns[1] = false;
					break;
				case KeyEvent.KEYCODE_BUTTON_3:
					Joy1Bttns[2] = false;
					break;
				case KeyEvent.KEYCODE_BUTTON_4:
					Joy1Bttns[3] = false;
					break;
				case KeyEvent.KEYCODE_BUTTON_5:
					Joy1Bttns[4] = false;
					break;
				case KeyEvent.KEYCODE_BUTTON_6:
					Joy1Bttns[5] = false;
					break;
				case KeyEvent.KEYCODE_BUTTON_7:
					Joy1Bttns[6] = false;
					break;
				case KeyEvent.KEYCODE_BUTTON_8:
					Joy1Bttns[7] = false;
					break;
				case KeyEvent.KEYCODE_BUTTON_9:
					Joy1Bttns[8] = false;
					break;
				case KeyEvent.KEYCODE_BUTTON_10:
					Joy1Bttns[9] = false;
					break;
				case KeyEvent.KEYCODE_BUTTON_11:
					Joy1Bttns[10] = false;
					break;
				case KeyEvent.KEYCODE_BUTTON_12:
					Joy1Bttns[11] = false;
				}
			}
			return true;
		}
	};
}