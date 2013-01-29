package fr.um2.myfs;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import fr.um2.user.Friend;
import fr.um2.user.OwerUser;


public class SendSmsActivity extends Activity implements OnClickListener {
	SmsManager smsManager;
	LinearLayout smsLayout;
	
	EditText villeTextEdit;
	
	LinearLayout ageLayout;
	LinearLayout minAgeLayout;
	LinearLayout maxAgeLayout;
	
	TextView minAgeText;
	TextView maxAgeText;
	
	NumberPicker minAgePicker;
	NumberPicker maxAgePicker;
	
	EditText smsCorps;
	Button sendButton;
	
	String smsType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_sms);
		Log.i("My", "SendSmsActivity started");
		smsManager = SmsManager.getDefault();
		smsType = getIntent().getStringExtra("sort-by");
		initGui();
	}

	private void initGui() {
		smsLayout = (LinearLayout) findViewById(R.id.sms_layout);
		
		if(smsType.equals("city"))
			initCityView();	
		if(smsType.equals("age"))
			initAgeView();	
		if(smsType.equals("costom")){
			initCityView();
			initAgeView();
		}
		initCorpsView();		
	}
	
	

	private void initCorpsView() {
		smsCorps = new EditText(this);
		smsCorps.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		smsCorps.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		smsCorps.setHint(R.string.sms_hint_message);
		smsCorps.setLines(5);
		smsCorps.setGravity(Gravity.TOP);
		
		sendButton = new Button(this);
		sendButton.setText(R.string.sms_button_name);
		sendButton.setOnClickListener(this);
		
		smsLayout.addView(smsCorps);
		smsLayout.addView(sendButton);
	}

	private void initAgeView() {
		ageLayout = new LinearLayout(this);
		ageLayout.setMinimumHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		ageLayout.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		ageLayout.setOrientation(LinearLayout.HORIZONTAL);
		
		minAgeLayout = new LinearLayout(this);
		minAgeLayout.setMinimumHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		minAgeLayout.setMinimumWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		minAgeLayout.setOrientation(LinearLayout.VERTICAL);
		
		maxAgeLayout = new LinearLayout(this);
		maxAgeLayout.setMinimumHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		maxAgeLayout.setMinimumWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		maxAgeLayout.setOrientation(LinearLayout.VERTICAL);
		
		minAgeText = new TextView(minAgeLayout.getContext());
		minAgeText.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		minAgeText.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		minAgeText.setText(R.string.sms_min_age_text);
		
		maxAgeText = new TextView(minAgeLayout.getContext());
		maxAgeText.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		maxAgeText.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		maxAgeText.setText(R.string.sms_max_age_text);		
		
		minAgePicker = new NumberPicker(this);
		maxAgePicker = new NumberPicker(this);
		
		minAgePicker.setMinimumHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		maxAgePicker.setMinimumHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		
		minAgePicker.setMinimumWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		maxAgePicker.setMinimumWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		
		String[] nums = new String[100];
	    for(int i=0; i<nums.length; i++)
	           nums[i] = Integer.toString(i);

	    minAgePicker.setMinValue(1);
	    maxAgePicker.setMinValue(1);
	    
	    minAgePicker.setMaxValue(100);
	    maxAgePicker.setMaxValue(100);
	    
	    minAgePicker.setWrapSelectorWheel(false);
	    maxAgePicker.setWrapSelectorWheel(false);
	    
	    minAgePicker.setDisplayedValues(nums);
	    maxAgePicker.setDisplayedValues(nums);
	    
	    minAgePicker.setValue(1);
	    maxAgePicker.setValue(40);
	    
	    minAgeLayout.addView(minAgeText);
	    minAgeLayout.addView(minAgePicker);
	    
	    maxAgeLayout.addView(maxAgeText);
	    maxAgeLayout.addView(maxAgePicker);
	    
	    ageLayout.addView(minAgeLayout);
	    ageLayout.addView(maxAgeLayout);
	    
	    smsLayout.addView(ageLayout);

	}

	private void initCityView() {
		villeTextEdit = new EditText(this);
		villeTextEdit.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		villeTextEdit.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		villeTextEdit.setHint(R.string.sms_hint_city);
		villeTextEdit.setMaxLines(1);
		villeTextEdit.setSingleLine(true);
		
		smsLayout.addView(villeTextEdit);
	}
	
	@Override
	public void onClick(View v) {
		if(smsType.equals("city"))
			sendSmsByCity();
		if(smsType.equals("age"))
			sendSmsByAge();
		if(smsType.equals("costom"))
			sendSmsByCostom();
		
		Log.i("My", "Message Send");
	}

	private void sendSmsByCity() {
		String city = villeTextEdit.getText().toString();
		String message = smsCorps.getText().toString(); 
		if(city.length() > 0){
			int cpt = 0;
			for(Friend friend : getFriendsWithCity(city)){
				smsManager.sendTextMessage(friend.getNumber(), null, message, null, null);
				cpt++;
			}
			Toast.makeText(this, "Message sent to : " + cpt + " friends", 
					Toast.LENGTH_LONG).show();
		}
		else{
			villeTextEdit.setError(getString(R.string.sms_city_error));
			villeTextEdit.requestFocus();
		}
		
	}
	

	private void sendSmsByAge() {
		int minAge = minAgePicker.getValue();
		int maxAge = maxAgePicker.getValue();
		String message = smsCorps.getText().toString();
		int cpt = 0;
		for(Friend friend : getFriendsWithAge(minAge, maxAge)){
			smsManager.sendTextMessage(friend.getNumber(), null, message, null, null);
			cpt++;
		}
		Toast.makeText(this, "Message sent to : " + cpt + " friends", 
				Toast.LENGTH_LONG).show();
	}
		
	private void sendSmsByCostom() {
		String message = villeTextEdit.getText().toString();
		
		String city = villeTextEdit.getText().toString();
		int minAge = minAgePicker.getValue();
		int maxAge = maxAgePicker.getValue();
		
		if(message.length() > 0){
			ArrayList<Friend> friendsWithCity = getFriendsWithCity(city);
			ArrayList<Friend> friends = new ArrayList<Friend>();
			
			for(Friend friend : getFriendsWithAge(minAge, maxAge)){
				if(friendsWithCity.contains(friend))
					friends.add(friend);
			}
			
			for(Friend friend : friends)
				smsManager.sendTextMessage(friend.getNumber(), null, message, null, null);
			
			Toast.makeText(this, "Message sent to : " + friends.size() + " friends", 
					Toast.LENGTH_LONG).show();
		}
		else{
			villeTextEdit.setError(getString(R.string.sms_city_error));
			villeTextEdit.requestFocus();
		}
	}
	
	private ArrayList<Friend> getFriendsWithCity(String city){
		ArrayList<Friend> result = new ArrayList<Friend>();
		for(Friend friend : OwerUser.getUser().getFriends()){
			if(friend.getCity().equals(city)){
				result.add(friend);
			}
		}
		return result;
	}
	
	private ArrayList<Friend> getFriendsWithAge(int minAge, int maxAge){
		ArrayList<Friend> result = new ArrayList<Friend>();
		for(Friend friend : OwerUser.getUser().getFriends()){
			if(friend.getAgeAsInteger() >= minAge
					&& friend.getAgeAsInteger() <= maxAge){
				result.add(friend);
			}
		}
		return result;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_back_to_profile, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_option_back_to_profile:
			Log.i("My", "Back to Profile Menu Clicked");
			Intent sea = new Intent(this, ConnectedActivity.class);
			this.startActivity(sea);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}

