package com.jacr.demotextviewj;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.jacr.textviewj.TextViewJ;

/**
 * Main Class.
 * 
 * @author j.castro 5/09/2014
 * 
 */
public class MainActivity extends ActionBarActivity {

	private static final int PROGRESS_MAX_VALUE = 100;

	private static final String KEY_LINE_SPACING = "key_line_spacing";
	private static final String KEY_WORDS_BY_LINE = "key_words_by_line";
	private static final String KEY_TEXT_SIZE = "key_text_size";
	private static final String KEY_FONT = "key_font";
	private static final String KEY_TEXT_COLOR = "key_text_color";
	private static final String KEY_TEXT_COLOR_POSITION_SPINNER = "key_text_color_pos_spinner";

	private static final int DEFAULT_TEXT_SIZE = 12;
	private static final int DEFAULT_LINE_SPACING = 10;
	private static final int DEFAULT_WORDS_BY_LINE = 5;
	private static final Typeface DEFAULT_FONT = Typeface.DEFAULT;
	private static final int DEFAULT_TEXT_COLOR = Color.BLACK;

	private static final int MAX_TEXT_SIZE = 50;
	private static final int MAX_LINE_SPACING = 50;
	private static final int MAX_WORDS_BY_LINE = 20;

	private static final String HELVETICA_FONT = "helvetica.ttf";

	private int mTextSize;
	private int mLineSpacing;
	private int mWordsByLine;
	private Typeface mFont;
	private String mFontAux;
	private int mTextColor;
	private int mTextColorAux;

	private Dialog mDialog;
	private TextViewJ mTxtJ;
	private TextView mTxtWordsByLine;
	private TextView mTxtLineSpacing;
	private TextView mTxtTextSize;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE
				| ActionBar.DISPLAY_SHOW_HOME);
		actionBar.setTitle(getString(R.string.app_name));

		mTxtJ = (TextViewJ) findViewById(R.id.txtviewj);

		init();
		applyChanges();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.settings) {
			openSettingsDialog();
		}
		return true;
	}

	public void openSettingsDialog() {
		ContextThemeWrapper ctw = new ContextThemeWrapper(this,
				R.style.DialogTheme);
		AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
		builder.setTitle(getString(R.string.menu_settings));
		final LayoutInflater inflater = (LayoutInflater) ctw
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// Custom view for dialog
		LinearLayout layout = new LinearLayout(this);
		inflater.inflate(R.layout.view_settings, layout, true);
		builder.setView(layout);

		// Elements
		init();
		setDialogElements(layout);

		mDialog = builder.create();
		mDialog.show();
	}

	private void init() {
		String aux = getSharedPreferences(this, KEY_TEXT_SIZE);
		mTextSize = (aux == null) ? DEFAULT_TEXT_SIZE : Integer.parseInt(aux);
		aux = getSharedPreferences(this, KEY_LINE_SPACING);
		mLineSpacing = (aux == null) ? DEFAULT_LINE_SPACING : Integer
				.parseInt(aux);
		aux = getSharedPreferences(this, KEY_WORDS_BY_LINE);
		mWordsByLine = (aux == null) ? DEFAULT_WORDS_BY_LINE : Integer
				.parseInt(aux);
		aux = getSharedPreferences(this, KEY_FONT);
		mFont = (aux == null) ? DEFAULT_FONT : getFontFromAssets(aux);
		aux = getSharedPreferences(this, KEY_TEXT_COLOR);
		mTextColor = (aux == null) ? DEFAULT_TEXT_COLOR : Integer.parseInt(aux);
		aux = getSharedPreferences(this, KEY_TEXT_COLOR_POSITION_SPINNER);
		mTextColorAux = (aux == null) ? 0 : Integer.parseInt(aux);

	}

	private void applyChanges() {
		mTxtJ.setTextSize(mTextSize);
		mTxtJ.setLineSpacing(mLineSpacing);
		mTxtJ.setWordsByLine(mWordsByLine);
		mTxtJ.setTextColor(mTextColor);
		mTxtJ.setTypeface(mFont);
	}

	private void setDialogElements(View rootView) {
		// Buttons
		Button btnSubmit = (Button) rootView.findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				closeAndSaveDialog();
				applyChanges();
			}

		});

		// Seekbars
		SeekBar seekbarWordsByLine = (SeekBar) rootView
				.findViewById(R.id.seekBarWordByLine);
		SeekBar seekbarTextSize = (SeekBar) rootView
				.findViewById(R.id.seekBarTextSize);
		SeekBar seekbarLineSpacing = (SeekBar) rootView
				.findViewById(R.id.seekBarLineSpacing);

		seekbarWordsByLine.setProgress(loadSeekBarValue(mWordsByLine, 1,
				MAX_WORDS_BY_LINE, false));
		seekbarTextSize.setProgress(loadSeekBarValue(mTextSize, 1,
				MAX_TEXT_SIZE, false));
		seekbarLineSpacing.setProgress(loadSeekBarValue(mLineSpacing, 0,
				MAX_LINE_SPACING, false));

		seekbarWordsByLine.setMax(PROGRESS_MAX_VALUE);
		seekbarTextSize.setMax(PROGRESS_MAX_VALUE);
		seekbarLineSpacing.setMax(PROGRESS_MAX_VALUE);

		seekbarWordsByLine
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						mWordsByLine = loadSeekBarValue(progress, 1,
								MAX_WORDS_BY_LINE, true);
						setTextViewSeekBarValue(mTxtWordsByLine, mWordsByLine);
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
					}
				});

		seekbarLineSpacing
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						mLineSpacing = loadSeekBarValue(progress, 0,
								MAX_LINE_SPACING, true);
						setTextViewSeekBarValue(mTxtLineSpacing, mLineSpacing);
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
					}
				});

		seekbarTextSize
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						mTextSize = loadSeekBarValue(progress, 1,
								MAX_TEXT_SIZE, true);
						setTextViewSeekBarValue(mTxtTextSize, mTextSize);

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
					}
				});

		// TextViews
		mTxtWordsByLine = (TextView) rootView.findViewById(R.id.txtWordsByLine);
		mTxtLineSpacing = (TextView) rootView.findViewById(R.id.txtLineSpacing);
		mTxtTextSize = (TextView) rootView.findViewById(R.id.txtTextSize);
		setTextViewSeekBarValue(mTxtTextSize, mTextSize);
		setTextViewSeekBarValue(mTxtLineSpacing, mLineSpacing);
		setTextViewSeekBarValue(mTxtWordsByLine, mWordsByLine);

		// Checkbox
		CheckBox chkAnotherFont = (CheckBox) rootView
				.findViewById(R.id.chkAnotherFont);
		chkAnotherFont.setChecked(mFontAux == null ? false : true);
		chkAnotherFont
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						mFontAux = isChecked ? HELVETICA_FONT : null;
					}

				});

		// Spinners
		Spinner spColor = (Spinner) rootView.findViewById(R.id.spTextColor);
		ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(
				this, R.array.text_color_list,
				android.R.layout.simple_spinner_item);
		spAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spColor.setAdapter(spAdapter);
		spColor.setSelection(mTextColorAux);
		spColor.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String strColor = parent.getItemAtPosition(position).toString();
				mTextColorAux = (int) id;
				if (strColor
						.contentEquals(getString(R.string.txt_text_color_black))) {
					mTextColor = Color.BLACK;

				} else if (strColor
						.contentEquals(getString(R.string.txt_text_color_red))) {
					mTextColor = Color.RED;

				} else if (strColor
						.contentEquals(getString(R.string.txt_text_color_blue))) {
					mTextColor = Color.BLUE;
				} else if (strColor
						.contentEquals(getString(R.string.txt_text_color_green))) {
					mTextColor = Color.GREEN;
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}

		});

	}

	private void setTextViewSeekBarValue(TextView tv, int value) {
		if (tv != null) {
			String txt = tv.getText().toString();
			String keyword = txt.substring(0, txt.lastIndexOf(":") + 1);
			tv.setText(keyword + " " + value);
		}
	}

	private int loadSeekBarValue(float data, float minData, float maxData,
			boolean inverse) {
		final float pendiente = (PROGRESS_MAX_VALUE / (maxData - minData));
		int result = (inverse) ? (int) ((1 / pendiente) * data + minData)
				: (int) (pendiente * (data - minData));
		return result;
	}

	private void closeAndSaveDialog() {
		saveSharedPreferences(this, KEY_WORDS_BY_LINE, "" + mWordsByLine);
		saveSharedPreferences(this, KEY_LINE_SPACING, "" + mLineSpacing);
		saveSharedPreferences(this, KEY_TEXT_SIZE, "" + mTextSize);
		saveSharedPreferences(this, KEY_FONT, mFontAux);
		mFont = (mFontAux == null) ? DEFAULT_FONT : getFontFromAssets(mFontAux);
		saveSharedPreferences(this, KEY_TEXT_COLOR, "" + mTextColor);
		saveSharedPreferences(this, KEY_TEXT_COLOR_POSITION_SPINNER, ""
				+ mTextColorAux);
		mDialog.dismiss();
	}

	private Typeface getFontFromAssets(String fontName) {
		return Typeface.createFromAsset(getAssets(), fontName);
	}

	public void saveSharedPreferences(Activity act, String llave,
			String valorLlave) {
		SharedPreferences sp = act.getPreferences(Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(llave, valorLlave);
		editor.commit();
	}

	public String getSharedPreferences(Activity act, String llave) {
		SharedPreferences sp = act.getPreferences(Activity.MODE_PRIVATE);
		String r = sp.getString(llave, null);
		return r;
	}

}
