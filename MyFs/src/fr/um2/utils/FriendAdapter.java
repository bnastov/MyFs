package fr.um2.utils;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import fr.um2.imageloader.ImageLoader;
import fr.um2.myfs.R;
import fr.um2.user.Friend;

public class FriendAdapter extends ArrayAdapter<Friend> {

	LayoutInflater inflater;

	public FriendAdapter(Context context, int layout,int textView, List<Friend> found) {
		super(context, layout, textView, found);

		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final Friend friend = (Friend) this.getItem(position);
		TextView userName;
		TextView userNumber;
		ImageView userImage;
		Switch userSwicher;
		Switch userCheck;
		
		if (convertView == null) {			
			convertView = inflater.inflate(R.layout.friends_adapter, null);
			
			userName 	= (TextView) convertView.findViewById(R.id.ad_friend_pseudo);
			userNumber 	= (TextView) convertView.findViewById(R.id.ad_friend_telephone);
			userImage 	= (ImageView) convertView.findViewById(R.id.ad_friend_image);
			userSwicher = (Switch) convertView.findViewById(R.id.ad_friend_switch);
			userCheck	= (Switch) convertView.findViewById(R.id.ad_friend_checkBox);
			
			
			String image_url = friend.getImagelink();
			
			initFriendImage( userImage, image_url );

			convertView.setTag(new OwerUserViewHolder(userName, userNumber, userImage, userSwicher, userCheck));
			
		} else {
			OwerUserViewHolder viewHolder = (OwerUserViewHolder) convertView.getTag();
			userName 	= viewHolder.getUserNameView();
			userNumber 	= viewHolder.getUserNumber();
			userImage 	= viewHolder.getUserImage();
			userSwicher = viewHolder.getUserSwitcher();
			userCheck	= viewHolder.getUserCheck();
		}
		userName.setText(friend.getPseudo());
		if(!friend.getNumber().isEmpty())
			userNumber.setText(friend.getNumber());
		userName.setTextColor(Color.WHITE);
		userNumber.setTextColor(Color.WHITE);
		userCheck.setTextColor(Color.WHITE);
		userSwicher.setTextColor(Color.WHITE);
		convertView.setBackgroundColor(Color.GRAY);
		
		
		userSwicher.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				friend.setTracable(isChecked);
				
			}
		});
		
		userCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				friend.setVisibleInMap(isChecked);
				
			}
		});//*/
		
		return convertView;
	}

	private void initFriendImage(ImageView imageFriend, String image_url) {
		// Loader image - will be shown before loading image, and if image not found
		int loader = R.drawable.ic_launcher; 
        // ImageLoader class instance
        ImageLoader imgLoader = new ImageLoader(this.getContext());
 
        // whenever you want to load an image from url
        // call DisplayImage function
        // url - image url to load
        // loader - loader image, will be displayed before getting image
        // image - ImageView
        imgLoader.DisplayImage(image_url, loader, imageFriend);
	}

	public static class OwerUserViewHolder {
		private TextView userNameView;
		private TextView userNumber;
		private ImageView userImage;
		private Switch userSwitcher;
		private Switch userCheck;	

		public OwerUserViewHolder(TextView userName, TextView userNumber, ImageView userImage, Switch switcher, Switch cb) {
			this.userNameView 	= userName;
			this.userNumber 	= userNumber;
			this.userImage 		= userImage;
			this.userCheck		= cb;
			this.userSwitcher	= switcher;
			
		}

		public Switch getUserSwitcher() {
			return userSwitcher;
		}
		
		public TextView getUserNameView() {
			return userNameView;
		}

		public void setUserNameView(TextView textView) {
			this.userNameView = textView;
		}
		public TextView getUserNumber() {
			return userNumber;
		}

		public void setUserNumber(TextView userNumber) {
			this.userNumber = userNumber;
		}

		public ImageView getUserImage() {
			return userImage;
		}

		public void setUserImage(ImageView userImage) {
			this.userImage = userImage;
		}
		
		public void setUserSwitcher(Switch userSwitcher) {
			this.userSwitcher = userSwitcher;
		}

		public Switch getUserCheck() {
			return userCheck;
		}

		public void setUserCheck(Switch userCheck) {
			this.userCheck = userCheck;
		}

		

		public OwerUserViewHolder() {
		}

	}

}
