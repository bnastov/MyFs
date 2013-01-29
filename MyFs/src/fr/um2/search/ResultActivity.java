package fr.um2.search;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import fr.um2.apicaller.ResponseApi;
import fr.um2.myfs.ConnectedActivity;
import fr.um2.myfs.R;
import fr.um2.user.Friend;
import fr.um2.user.OwerUser;
import fr.um2.utils.OwerUserAdapter;

public class ResultActivity extends ListActivity {
	List<Friend> found;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String searched = "b";
		if (this.getIntent().getExtras() != null) {
			searched = getIntent().getExtras().getString("search");
		}

		found = OwerUser.getUser().search(searched).getResults();

		OwerUserAdapter ad = new OwerUserAdapter(this,
				R.layout.oweruser_adapter, R.id.ad_pseudo, found);

		ListView listView = getListView();
		
		setListAdapter(ad);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View item,
					int position, long id) {
				Friend planet = found.get(position);
				ResponseApi<Void> res = OwerUser.getUser().addFriend(
						planet.getPublictoken());
				if (res.isOK()) {
					Toast.makeText(getApplicationContext(),
							  "Ajoute de " + planet.getPseudo(), Toast.LENGTH_LONG).show();
					
					Log.i("My", planet.getPseudo() + " est ajouté a "
							+ OwerUser.getUser().getPublictoken());
				} else {
					Toast.makeText(getApplicationContext(),
							  "l'ami que vous essayer d'ajouté existe deja " + planet.getPseudo(), Toast.LENGTH_LONG).show();
					
					Log.i("My", res.toString());
				}
				
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
