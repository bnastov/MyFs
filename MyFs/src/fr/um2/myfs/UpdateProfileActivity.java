package fr.um2.myfs;

import fr.um2.apicaller.ResponseApi;
import fr.um2.user.OwerUser;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateProfileActivity extends Activity implements OnClickListener {
	EditText firstname;
	EditText lastname;
	EditText city;
	EditText age;
	EditText imageLink;
	EditText number;
	Button  submit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_profile);
		
		firstname = (EditText) findViewById(R.id.update_profile_firstname);
		lastname = (EditText) findViewById(R.id.update_profile_lastname);
		city = (EditText) findViewById(R.id.update_profile_city);
		number = (EditText) findViewById(R.id.update_profile_phonenumber);
		age = (EditText) findViewById(R.id.update_profile_age);
		imageLink = (EditText) findViewById(R.id.update_profile_imagelink);
		
		firstname.setText(OwerUser.getUser().getFirstName());
		lastname.setText(OwerUser.getUser().getLastName());
		city.setText(OwerUser.getUser().getCity());
		age.setText(OwerUser.getUser().getAge());
		number.setText(OwerUser.getUser().getNumber());
		imageLink.setText(OwerUser.getUser().getImagelink());
		
		submit = (Button) findViewById(R.id.update_profile_submit);
		submit.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@SuppressLint("ShowToast")
	@Override
	public void onClick(View arg0) {
		ResponseApi<Void> res = OwerUser.getUser().updateProfile(firstname.getText()+"", lastname.getText()+"", city.getText()+"", age.getText()+"", imageLink.getText()+"", number.getText()+"");
		if(res.isOK()){
			this.finish();
		} else {
			Toast.makeText(this, res.toString(), Toast.LENGTH_LONG);
		}
	}

}
