package fr.um2.utils;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.um2.apicaller.OwerUser;
import fr.um2.imageloader.ImageLoader;
import fr.um2.myfs.R;

public class OwerUserAdapter extends ArrayAdapter<OwerUser> {

	LayoutInflater inflater;

	public OwerUserAdapter(Context context, int layout,int textView, List<OwerUser> objects) {
		super(context, layout, textView, objects);

		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		OwerUser planet = (OwerUser) this.getItem(position);
		TextView userName;
		TextView userNumber;
		ImageView userImage;
		
		if (convertView == null) {			
			convertView = inflater.inflate(R.layout.oweruser_adapter, null);
			
			userName 	= (TextView) convertView.findViewById(R.id.ad_pseudo);
			userNumber 	= (TextView) convertView.findViewById(R.id.ad_telephone);
			userImage 	= (ImageView) convertView.findViewById(R.id.ad_image);
			
			
			
			String image_url = planet.getImagelink();
			
			initFriendImage( userImage, image_url );

			convertView.setTag(new OwerUserViewHolder(userName, userNumber, userImage));
			
		} else {
			OwerUserViewHolder viewHolder = (OwerUserViewHolder) convertView.getTag();
			userName 	= viewHolder.getUserNameView();
			userNumber 	= viewHolder.getUserNumber();
			userImage 	= viewHolder.getUserImage();
		}
		userName.setText(planet.getPseudo());
		if(!planet.getNumber().isEmpty())
			userNumber.setText(planet.getNumber());
		userName.setTextColor(Color.WHITE);
		//userNumber.setTextColor(Color.GRAY);
		convertView.setBackgroundColor(Color.GRAY);
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

		public OwerUserViewHolder() {
		}

		public OwerUserViewHolder(TextView userName, TextView userNumber, ImageView userImage) {
			this.userNameView 	= userName;
			this.userNumber 	= userNumber;
			this.userImage 		= userImage;
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

	}

}
