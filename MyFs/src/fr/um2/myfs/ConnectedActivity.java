package fr.um2.myfs;

import java.util.ArrayList;
import java.util.Collections;

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
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import fr.um2.apicaller.Position;
import fr.um2.apicaller.ResponseApi;
import fr.um2.imageloader.ImageLoader;
import fr.um2.search.SearchActivity;
import fr.um2.service.GeoSendService;
import fr.um2.user.Friend;
import fr.um2.user.OwerUser;
import fr.um2.utils.FriendAdapter;

public class ConnectedActivity extends FragmentActivity implements
		OnLongClickListener {

	Intent serviceGeoSender;
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	static ArrayList<Friend> listFriends;
	static ArrayList<Friend> listSortedFriends = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StartGeoSender();
		setContentView(R.layout.activity_connected);

		OwerUser.loginUser("blaze_nastov@hotmail.com", "nastov123");

		listFriends = OwerUser.getUser().getFriendsWeb();

		initilizeFriendList();

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
		DummySectionFragment.listfriendsView = new ListView(this);
		DummySectionFragment.listfriendsView.setOnLongClickListener(this);
		DummySectionFragment.listfriendsView
				.setOnCreateContextMenuListener(this);

		DummySectionFragment.listfriendsView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View item,
							int position, long id) {
						Friend planet;
						if (listSortedFriends != null)
							planet = listSortedFriends.get(position);
						else
							planet = listFriends.get(position);

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
		case R.id.menu_option_map:
			Intent map = new Intent(this, GoogleMapActivity.class);
			this.startActivity(map);
			break;
		case R.id.menu_option_update_profile:
			Intent update = new Intent(this, UpdateProfileActivity.class);
			this.startActivity(update);
			this.finish();
			break;
		case R.id.menu_option_search_friends:
			Log.i("My", "search Menu Clicked");
			Intent sea = new Intent(this, SearchActivity.class);
			this.startActivity(sea);
			break;
		case R.id.menu_option_refresh_friends:
			OwerUser.getUser().getFriendsWeb();
			Log.i("My", "Refresh Freinds menu Clicked");
			listSortedFriends = null;
			DummySectionFragment.initilizeFriends(
					DummySectionFragment.listfriendsView, this, listFriends);
			break;
		case R.id.sub_sub_menu_option_sort_by_first_name:
			Log.i("My", "Sort by First Name menu Clicked");
			DummySectionFragment.initilizeFriends(
					DummySectionFragment.listfriendsView, this,
					sortByFirstName());
			break;
		case R.id.sub_sub_menu_option_sort_by_last_name:
			Log.i("My", "Sort by Last Name menu Clicked");
			DummySectionFragment.initilizeFriends(
					DummySectionFragment.listfriendsView, this,
					sortByLastName());
			break;
		case R.id.sub_sub_menu_option_sort_by_age_increasing:
			Log.i("My", "Sort by Age Increasing menu Clicked");
			DummySectionFragment.initilizeFriends(
					DummySectionFragment.listfriendsView, this,
					sortByAgeIncreasing());
			break;
		case R.id.sub_sub_menu_option_sort_by_age_decreasing:
			Log.i("My", "Sort by Age Decreasing menu Clicked");
			DummySectionFragment.initilizeFriends(
					DummySectionFragment.listfriendsView, this,
					sortByAgeDecreasing());
			break;
		case R.id.sub_menu_option_sort_by_city:
			Log.i("My", "Sort by City menu Clicked");
			DummySectionFragment.initilizeFriends(
					DummySectionFragment.listfriendsView, this, sortByCity());
			break;
		case R.id.sub_menu_option_sort_by_distance:
			Log.i("My", "Sort by Distance menu Clicked");
			DummySectionFragment.initilizeFriends(
					DummySectionFragment.listfriendsView, this,
					sortByDistance());
			break;
		case R.id.sub_menu_option_send_sms_by_city:
			Log.i("My", "Send Sms By City");
			startSmsActivity("city");
			break;
		case R.id.sub_menu_option_send_sms_by_age:
			Log.i("My", "Send Sms By age");
			startSmsActivity("age");
			break;
		case R.id.sub_menu_option_send_sms_by_costom:
			Log.i("My", "Send Costom Sms");
			startSmsActivity("costom");
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void startSmsActivity(String extra) {
		Intent intent = new Intent(this, SendSmsActivity.class);
		intent.putExtra("sort-by", extra);
		Log.i("My", "startSmsActyvity with extra : " + extra);
		this.startActivity(intent);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		public Fragment profile;
		public Fragment listFriends;
		
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			profile = getItem(0);
			listFriends = getItem(1);
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
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_section1);
			case 1:
				return getString(R.string.title_section2);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment implements
			OnCheckedChangeListener {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		TextView firstName;
		TextView lastName;
		TextView pseudo;
		TextView city;
		TextView age;
		ImageView imageView;
		Switch switcher;
		public static ListView listfriendsView;

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
				initilizeFriends(listfriendsView, getActivity(),
						ConnectedActivity.listFriends);
				ret = listfriendsView;

				break;

			case 2:

				View v3 = layoutInflater.inflate(R.layout.view_info, null);
				initializeGrid(v3);
				ret = v3;//

				break;

			default:
				break;
			}

			return ret;
		}

		private void initializeGrid(View v3) {

			GridView gridView;

			ArrayList<String> values = new ArrayList<String>();
			values.add("Pseudo");
			values.add("Latitude");
			values.add("Longitutde");
			values.add("time");

			// GeoLocationDBAdapteur base = new
			// GeoLocationDBAdapteur(getActivity());
			values.add("rabah");
			values.add("33");
			values.add("127");
			values.add("8 53");

			gridView = (GridView) v3.findViewById(R.id.gridView1);

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					getActivity(), android.R.layout.simple_list_item_1, values);

			gridView.setAdapter(adapter);

		}

		public static void initilizeFriends(ListView v2, Activity a,
				ArrayList<Friend> listFriends2) {

			/*
			 * OwerUserAdapter ad = new OwerUserAdapter(a,
			 * R.layout.oweruser_adapter, R.id.ad_pseudo, listFriends2);
			 * ListView listView = v2; listView.setTextFilterEnabled(true);//
			 */

			FriendAdapter ad = new FriendAdapter(a, R.layout.friends_adapter,
					R.id.ad_friend_pseudo, listFriends2);

			ListView listView = v2;
			listView.setTextFilterEnabled(true);

			v2.setAdapter(ad);

		}

		private void initilizeProfil(View v) {
			firstName = (TextView) v.findViewById(R.id.FirstName);
			lastName = (TextView) v.findViewById(R.id.LastName);
			pseudo = (TextView) v.findViewById(R.id.identifier);
			city = (TextView) v.findViewById(R.id.city);
			age = (TextView) v.findViewById(R.id.age);
			imageView = (ImageView) v.findViewById(R.id.imageView1);
			switcher = (Switch) v.findViewById(R.id.profile_switch_visible);
			switcher.setChecked(OwerUser.getUser().isVisible());
			switcher.setOnCheckedChangeListener(this);

			if (firstName == null) {
				Log.i("My", "firstname est null");
			}

			OwerUser f = OwerUser.getUser();
			pseudo.setText(f.getPseudo());
			firstName.setText(f.getFirstName());
			lastName.setText(f.getLastName());
			city.setText(f.getCity());
			age.setText(f.getAge());

			int loader = R.drawable.ic_launcher;
			ImageLoader imgLoader = new ImageLoader(this.getActivity());
			imgLoader.DisplayImage(OwerUser.getUser().getImagelink(), loader,
					imageView);
		}

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

			ResponseApi<Void> response = OwerUser.getUser().updateVisible(arg1);
			if (response.isOK()) {
				Toast.makeText(getActivity(), "Visibility changed to " + arg1,
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getActivity(), response.toString(),
						Toast.LENGTH_LONG).show();

			}
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
		menu.setHeaderTitle(R.string.menu_context_name);
		menu.setHeaderIcon(android.R.drawable.ic_delete);
		menu.add(0, 1, 0, R.string.menu_context_delete_friend);
		menu.add(0, 2, 0, R.string.menu_context_info_friend);
		menu.add(0, 3, 0, R.string.menu_context_call_friend);
		menu.add(0, 4, 0, R.string.menu_context_send_sms);
		menu.add(0, 5, 0, "Test Notification");

		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		/*
		 * Get Friend Selected
		 */
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		Friend selected;
		// Verify if the list is sorted
		if (ConnectedActivity.listSortedFriends == null)
			selected = ConnectedActivity.listFriends.get(info.position);
		else
			selected = ConnectedActivity.listSortedFriends.get(info.position);

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

				ConnectedActivity.listFriends.remove(selected);
				if (ConnectedActivity.listSortedFriends != null) {
					ConnectedActivity.listSortedFriends.remove(selected);
					DummySectionFragment.initilizeFriends(
							DummySectionFragment.listfriendsView, this,
							ConnectedActivity.listSortedFriends);
				} else
					DummySectionFragment.initilizeFriends(
							DummySectionFragment.listfriendsView, this,
							ConnectedActivity.listFriends);
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
			Toast.makeText(
					getApplicationContext(),
					"Info Friend \n Pseudo :" + selected.getPseudo()
							+ " \n First Name : " + selected.getFirstName()
							+ " \n Last Name :" + selected.getLastName()
							+ "\n postion at \n"
							+ selected.getGeoloc().toString(),
					Toast.LENGTH_LONG).show();

			break;
		case 3:
			// Simulation d'appel
			Uri uri = Uri.parse(selected.getNumber());
			Intent intent = new Intent(Intent.ACTION_DIAL, uri);
			startActivity(intent);
			break;
		case 4:
			Uri sms_uri = Uri.parse(selected.getNumber());
			Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
			sms_intent.putExtra("sms_body", "Good Morning ! how r U ?");
			startActivity(sms_intent);
			break;
		case 5:

			
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * This function sorts the friend list by first name
	 * 
	 * @return sorted {@link ArrayList} of {@link OwerUser}
	 */
	private ArrayList<Friend> sortByFirstName() {
		ArrayList<String> sortedFirstNames = new ArrayList<String>();
		for (Friend owerUser : listFriends) {
			sortedFirstNames.add(owerUser.getFirstName());
		}
		Collections.sort(sortedFirstNames);

		ArrayList<Friend> result = new ArrayList<Friend>();
		for (String firstName : sortedFirstNames) {
			for (Friend friend : listFriends) {
				if (friend.getFirstName().equals(firstName)) {
					result.add(friend);
				}
			}
		}
		ConnectedActivity.listSortedFriends = result;
		return result;
	}

	/**
	 * This function sorts the friend list by first name
	 * 
	 * @return sorted {@link ArrayList} of {@link OwerUser}
	 */
	private ArrayList<Friend> sortByLastName() {
		ArrayList<String> sortedLastNames = new ArrayList<String>();
		for (Friend owerUser : listFriends) {
			sortedLastNames.add(owerUser.getLastName());
		}
		Collections.sort(sortedLastNames);

		ArrayList<Friend> result = new ArrayList<Friend>();
		for (String lastName : sortedLastNames) {
			for (Friend friend : listFriends) {
				if (friend.getFirstName().equals(lastName)) {
					result.add(friend);
				}
			}
		}
		ConnectedActivity.listSortedFriends = result;
		return result;
	}

	/**
	 * This function sorts the friend list by increasing age
	 * 
	 * @return sorted {@link ArrayList} of {@link OwerUser}
	 */
	private ArrayList<Friend> sortByAgeIncreasing() {
		ArrayList<String> sortedAge = new ArrayList<String>();
		for (Friend owerUser : listFriends) {
			sortedAge.add(owerUser.getAge());
		}
		Collections.sort(sortedAge);

		ArrayList<Friend> result = new ArrayList<Friend>();
		for (String age : sortedAge) {
			for (Friend friend : listFriends) {
				if (friend.getAge().equals(age)) {
					result.add(friend);
				}
			}
		}
		ConnectedActivity.listSortedFriends = result;
		return result;
	}

	/**
	 * This function sorts the friend list by decreasing age
	 * 
	 * @return sorted {@link ArrayList} of {@link OwerUser}
	 */
	private ArrayList<Friend> sortByAgeDecreasing() {
		ArrayList<Friend> sortedFriendsByAgeDecreasing = sortByAgeIncreasing();
		Collections.reverse(sortedFriendsByAgeDecreasing);
		listSortedFriends = sortedFriendsByAgeDecreasing;
		return sortedFriendsByAgeDecreasing;
	}

	/**
	 * This function sorts the friend list by city
	 * 
	 * @return sorted {@link ArrayList} of {@link OwerUser}
	 */
	private ArrayList<Friend> sortByCity() {
		ArrayList<String> sortedCity = new ArrayList<String>();
		for (Friend owerUser : listFriends) {
			sortedCity.add(owerUser.getCity());
		}
		Collections.sort(sortedCity);

		ArrayList<Friend> result = new ArrayList<Friend>();
		for (String ciity : sortedCity) {
			for (Friend friend : listFriends) {
				if (friend.getCity().equals(ciity)) {
					result.add(friend);
				}
			}
		}
		ConnectedActivity.listSortedFriends = result;
		return result;
	}

	/**
	 * This function sorts the friend list by first distance
	 * 
	 * @return sorted {@link ArrayList} of {@link OwerUser}
	 */
	private ArrayList<Friend> sortByDistance() {
		ArrayList<Position> sortedDistances = new ArrayList<Position>();

		for (Friend owerUser : listFriends) {
			sortedDistances.add(owerUser.getGeoloc());
		}
		Collections.sort(sortedDistances);

		ArrayList<Friend> result = new ArrayList<Friend>();
		for (Position position : sortedDistances) {
			for (Friend friend : listFriends) {
				if (friend.getGeoloc().equals(position)) {
					result.add(friend);
				}
			}
		}
		ConnectedActivity.listSortedFriends = result;
		return result;
	}
}
