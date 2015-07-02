// Remember to add Facebook SDK to gradle dependencies.


public class ShareDemo extends Activity{
	ShareDialog shareDialog; 
	CallbackManager callbackManager; 

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.share_demo); 

		FacebookSdk.sdkInitialize(getApplicationContext());
		callbackManager = CallbackManager.Factory.create();
		shareDialog = new ShareDialog(this);
	}


    // Return to App Screen after sharing with Facebook

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}

    //Share Intent to Share Screenshot with other apps

	public void shareClick(View view) {
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_STREAM, saveImageUri(getApplicationContext(), ScreenShot()));
		shareIntent.setType("image/png");
		startActivity(Intent.createChooser(shareIntent, "Share with..."));
	}

    // Saving Bitmap and getting returning URI to be able to use it in share intent. 

	public Uri saveImageUri(Context context, Bitmap bitmap) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
		String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "ImageTitle", null);
		return Uri.parse(path);
	}

    //Taking screenshot 

	public Bitmap ScreenShot() {
		View currentScreen = this.findViewById(android.R.id.content).getRootView();
		currentScreen.setDrawingCacheEnabled(true);
		return currentScreen.getDrawingCache();
	}


	//Using Facebook SDK to create share dialog in Facebook. 

	public void facebookClick(View view) {
		try {
			if (ShareDialog.canShow(ShareLinkContent.class)) {
				ShareLinkContent linkContent = new ShareLinkContent.Builder()
				.setContentTitle("I just scored " + score + "!")
				.setContentDescription(
					"Beat my score on Android!")
				.setContentUrl(Uri.parse("http://www.example.com"))
				.setImageUrl(Uri.parse("http://www.example.com"))
				.build();
				shareDialog.show(linkContent);
			}
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Error Sharing with Facebook.", Toast.LENGTH_LONG).show();
		}
	}

	public void facebookPhotoShare(View view) {
		if (ShareDialog.canShow(SharePhotoContent.class)) {
			Bitmap screenshot = Screenshot();

			SharePhoto photo = new SharePhoto.Builder()
				.setBitmap(screenshot)
				.build();

			SharePhotoContent content = new SharePhotoContent.Builder()
				.addPhoto(photo)
				.build();
				shareDialog.show(content);
		}
	}