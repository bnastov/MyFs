package fr.um2.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import fr.um2.myfs.R;

public class SearchActivity extends Activity implements OnClickListener {

	Button search;
	EditText searchfriend;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		search = (Button) findViewById(R.id.search);
		searchfriend = (EditText) findViewById(R.id.friendsearched);

		search.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		if (searchfriend.getText().toString().isEmpty()) {
			searchfriend.setError("You must give a name or public token");
		} else {

			Intent excute = new Intent(this, ResultActivity.class);
			excute.putExtra("search", searchfriend.getText().toString());

			this.startActivity(excute);
			this.finish();
		}

	}
}
