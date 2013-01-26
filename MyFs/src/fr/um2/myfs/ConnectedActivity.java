package fr.um2.myfs;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;

import fr.um2.apicaller.OwerUser;
import fr.um2.apicaller.Position;
import fr.um2.apicaller.ResponseApi;
import fr.um2.database.GeoLocationDBAdapteur;
import fr.um2.entities.GeoLocation;
import fr.um2.search.SearchActivity;
import fr.um2.service.GeoSendService;
import fr.um2.utils.OwerUserAdapter;

public class ConnectedActivity extends FragmentActivity implements
		OnLongClickListener {

	Intent serviceGeoSender;
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StartGeoSender();
		ActiveAndroid.initialize(this);

		//OwerUser.loginUser("blaze_nastov@hotmail.com", "nastov123");
		OwerUser.loginUser("rabah@hotmail.com", "rabah");
		OwerUser.getUser().getFriendsWeb();

		initilizeFriendList();
		setContentView(R.layout.activity_connected);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	private void StartGeoSender() {
		Intent serviceGeoSender = new Intent(this, GeoSendService.class);
		startService(serviceGeoSender);

	}

	private void initilizeFriendList() {
		DummySectionFragment.listfriends = new ListView(this);
		DummySectionFragment.listfriends.setOnLongClickListener(this);
		DummySectionFragment.listfriends.setOnCreateContextMenuListener(this);

		DummySectionFragment.listfriends
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View item,
							int position, long id) {
						OwerUser planet = OwerUser.getUser().getFriends()
								.get(position);
						Log.i("My", planet.toString());
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_connected, menu);

		return true;
	}

	// salim : 06 48 80 82 44

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_option_search_friends:
			Log.i("My", "search Menu Clicked");
			Intent sea = new Intent(this, SearchActivity.class);
			this.startActivity(sea);
			break;

		case R.id.menu_option_refresh_friends:
			OwerUser.getUser().getFriendsWeb();
			Log.i("My", "Refresh Freinds menu Clicked");
			DummySectionFragment.initilizeFriends(
					DummySectionFragment.listfriends, this);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		public Fragment profile;
		public Fragment listFriends;
		public Fragment map;
		
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			profile = getItem(0);
			listFriends = getItem(1);
			map = getItem(2);
		}
		

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_section1);
			case 1:
				return getString(R.string.title_section2);
			case 2:
				return getString(R.string.title_section3);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		TextView firstName;
		TextView lastName;
		TextView pseudo;
		TextView tokenpublic;
		ImageView imageView;
		public static ListView listfriends;

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View ret = null;
			int tab = getArguments().getInt(ARG_SECTION_NUMBER);

			LayoutInflater layoutInflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			switch (tab) {
			case 0:
				View v = layoutInflater.inflate(R.layout.view_profil, null);
				initilizeProfil(v);
				ret = v;

				break;

			case 1:
				initilizeFriends(listfriends, getActivity());
				ret = listfriends;

				break;

			case 2:
				/*
				 * View v3 = layoutInflater.inflate(R.layout.view_info, null);
				 * initializeMap(v3); ret = v3;//
				 */

				break;

			default:
				break;
			}

			return ret;
		}

		private void initializeMap(View v3) {
			TextView v = (TextView) v3.findViewById(R.id.debug);
			v.setText(GeoLocation.getRandom().toString());
		}

		public static void initilizeFriends(ListView v2, Activity a) {

			OwerUserAdapter ad = new OwerUserAdapter(a,
					R.layout.oweruser_adapter, R.id.ad_pseudo, OwerUser
							.getUser().getFriends());

			ListView listView = v2;
			listView.setTextFilterEnabled(true);

			v2.setAdapter(ad);

		}

		private void initilizeProfil(View v) {
			firstName = (TextView) v.findViewById(R.id.FirstName);
			lastName = (TextView) v.findViewById(R.id.LastName);
			pseudo = (TextView) v.findViewById(R.id.identifier);
			tokenpublic = (TextView) v.findViewById(R.id.tokenPublic);

			if (firstName == null) {
				Log.i("My", "firstname est null");
			}

			OwerUser f = OwerUser.getUser();
			pseudo.setText(f.getPseudo());
			firstName.setText(f.getFirstName());
			lastName.setText(f.getLastName());
			tokenpublic.setText(f.getPublictoken());

		}

	}

	/**
	 * Context Menu to delete Friends
	 */
	@Override
	public boolean onLongClick(View arg0) {
		arg0.showContextMenu();
		return false;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("Rabah Menu");
		menu.setHeaderIcon(R.drawable.rabah);
		menu.add(0, 1, 0, R.string.delete_menu);
		menu.add(0, 2, 0, R.string.info_menu);
		menu.add(0, 3, 0, R.string.call_menu);
		menu.add(0, 4, 0, R.string.sms_menu);
		
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		/*
		 * Get Friend Selected
		 */
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		OwerUser selected = OwerUser.getUser().getFriends().get(info.position);

		switch (item.getItemId()) {
		/**
		 * Delete Case
		 */
		case 1:
			ResponseApi<Void> res = OwerUser.getUser().removeFriend(
					selected.getPublictoken());
			if (res.isOK()) {
				Log.i("My", "Delete " + res);

				Toast.makeText(getApplicationContext(),
						"Suppression de " + selected.getPseudo(),
						Toast.LENGTH_LONG).show();

				OwerUser.getUser().getFriendsWeb();
				Log.i("My", "Refresh Freinds menu Clicked");
				DummySectionFragment.initilizeFriends(
						DummySectionFragment.listfriends, this);
			} else {
				Toast.makeText(getApplicationContext(),
						"Suppression Impossible \n " + res.toString(),
						Toast.LENGTH_LONG).show();
				Log.i("My", "ERROR " + res.toString());
			}
			break;

		/**
		 * info Case
		 */
		case 2:
			//Ajouter dans la dbb
			ResponseApi<Position> repo = OwerUser.getUser()
					.getGeoLocalizationOfFriend(selected.getPublictoken());
			if (repo.isOK()) {

				GeoLocation f = new GeoLocation(selected.getPublictoken(), repo
						.getResults().getLat(), repo.getResults().getLon(),
						repo.getResults().getTime());
				//mSectionsPagerAdapter.map
//				Toast.makeText(
//						getApplicationContext(),
//						"Info Friend \n Pseudo :" + selected.getPseudo()
//								+ " \n First Name : " + selected.getFirstName()
//								+ " \n Last Name :" + selected.getLastName() + 
//								f.toString(), 
//						Toast.LENGTH_LONG).show();

				GeoLocationDBAdapteur base = new GeoLocationDBAdapteur(getApplicationContext());
				base.open();
				base.insertGeoLocation(f);
				base.close();
				
			} else {
				Log.e("My", "ERREUR Dans la requete de recuperation");
			}
			
			break;
		case 3:
			//Simulation d'appel
			Uri uri = Uri.parse("tel:0612345678");
			Intent intent = new Intent(Intent.ACTION_DIAL, uri);
			startActivity(intent);
		
		case 4:
			//Simulation d'envoie de sms
			//cas 1 :
			/*
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage("0612345678", null, "sms message", null, null);
			*/
			//cas 2 :
			Uri sms_uri = Uri.parse("smsto:+33612345678"); 
	        Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri); 
	        sms_intent.putExtra("sms_body", "Good Morning ! how r U ?"); 
	        startActivity(sms_intent); 
			
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActiveAndroid.dispose();

	}
}
