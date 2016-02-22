package com.clockworks.android.tablet.bigture.fragment;

import java.io.File;
import java.util.Date;

import com.clockworks.android.tablet.bigture.CropActivity;
import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.SettingPopupActivity;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.AccountEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.CommonCoverEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.CountryEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.SaveAccountEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.TempImgEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.LoginHandler;
import com.clockworks.android.tablet.bigture.serverInterface.handler.NewsHandler;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ProfileHandler;
import com.clockworks.android.tablet.bigture.utils.DateUtil;
import com.clockworks.android.tablet.bigture.utils.file.FileUtil;
import com.clockworks.android.tablet.bigture.utils.image.ImageUtil;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MyProfileFragment extends Fragment implements DatePickerDialog.OnDateSetListener,View.OnFocusChangeListener{
	private Context context;
	private AccountEntity account;
	private EditText editNickname;
	private EditText birthdayLabel;
	private Button btnMale;
	private Button btnFemale;
	private TextView countrySelect;
	private EditText editComment;
	private View passwordHiddenFrame;
	
	private EditText editPassword;
	private EditText editPassword1;
	private EditText editPassword2;
	
	private Date   birthday;
	private String birthdayString;
	BitmapFactory.Options bitmapOptions;
	private boolean canDone;
	private String selectedCountryCode;
	private CountrySelectListener listener;
	private TempImgEntity profileImageEntity;
	private ImageView profileImage;
	
	private PopupWindow imgMenu;
	
	private boolean modified;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context = activity;
		this.listener = (CountrySelectListener)activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		modified = false;
		this.account = AccountManager.getInstance().getAccountEntity();
		bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		bitmapOptions.inSampleSize = 3;
		this.canDone = false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_my_profile, container,false);
		
		this.profileImage = (ImageView)view.findViewById(R.id.profileImage);
		String imageUrl = account.getProfileImageURL();
		if(imageUrl != null){
			BitmapDownloader.getInstance().displayImage(imageUrl, bitmapOptions, profileImage, null);
		}
		
		Button btnEditImage = (Button)view.findViewById(R.id.btnEditImage);
		btnEditImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(imgMenu == null){
					LayoutInflater inflater = LayoutInflater.from(context);
					View menuView = inflater.inflate(R.layout.menu_select_picture, null);
					imgMenu = new PopupWindow(menuView,LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
					imgMenu.setOutsideTouchable(true);
					imgMenu.setBackgroundDrawable(new BitmapDrawable()) ;
				}
				
				imgMenu.dismiss();
				View mView = imgMenu.getContentView();
				
				Button menuTakePhoto = (Button)mView.findViewById(R.id.menuTakePhoto);
				menuTakePhoto.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						imgMenu.dismiss();
						
						String tempPath = FileUtil.getTempPath(context) + "/taken.image";
						File file = new File(tempPath);
						
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
						startActivityForResult(intent, BigtureEnvironment.REQ_CODE_TAKE_PICTURE);
					}
				});
				
				Button menuSelectGallery = (Button)mView.findViewById(R.id.menuSelectGallery);
				menuSelectGallery.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						imgMenu.dismiss();
						Intent intent = new Intent(Intent.ACTION_PICK);
						intent.setType("image/*");
						startActivityForResult(intent, BigtureEnvironment.REQ_CODE_PICKUP_PICTURE);
					}
				});
				
				mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
				
				imgMenu.setAnimationStyle(-1);
				imgMenu.showAsDropDown(v, -50, 0);
				
				
			}
		});
		
		TextView txtBigtureId = (TextView)view.findViewById(R.id.txtBigtureId);
		txtBigtureId.setText(account.loginId);
		
		TextView textView2 = (TextView)view.findViewById(R.id.textView2);
		TextView txtJob = (TextView)view.findViewById(R.id.txtJob);
		if(account.isPro){
			txtJob.setText(account.job);
		}else{
			textView2.setVisibility(View.GONE);
			txtJob.setVisibility(View.GONE);
		}
		
		
		
		this.editNickname = (EditText)view.findViewById(R.id.editNickname);
		this.editNickname.setText(account.nickName);
		
		this.editNickname.setOnKeyListener( new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				modified = true;
				return false;
			}
		});
		
		
		
		
		this.birthdayLabel = (EditText)view.findViewById(R.id.birthdayLabel);
		this.birthdayLabel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//selectBirthday();
				modified = true;
			}
		});
		if(account.birthday != null){
			this.birthday =  DateUtil.createDateFromString(account.birthday, "yyyy-MM-dd");
			this.birthdayString = DateUtil.getDateFormatString(birthday, "yyyy");
			this.birthdayLabel.setText(birthdayString);
		}
		
		
		ImageView imagePickupBirthday = (ImageView)view.findViewById(R.id.imagePickupBirthday);
		imagePickupBirthday.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectBirthday();
				modified = true;
			}
		});
		
		this.btnMale = (Button)view.findViewById(R.id.btnMale);
		this.btnMale.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(btnMale.isSelected()){
					btnMale.setSelected(false);
					btnFemale.setSelected(true);
				}else{
					btnMale.setSelected(false);
					btnFemale.setSelected(true);
				}
				modified = true;
			}
		});
		this.btnFemale = (Button)view.findViewById(R.id.btnFemale);
		this.btnFemale.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(btnFemale.isSelected()){
					btnMale.setSelected(false);
					btnFemale.setSelected(true);
				}else{
					btnMale.setSelected(false);
					btnFemale.setSelected(true);
				}
				modified = true;
			}
		});
		
		if(account.gender.equals("M")){
			btnMale.setSelected(true);
			btnFemale.setSelected(false);
		}else{
			btnMale.setSelected(false);
			btnFemale.setSelected(true);
		}
		
		this.countrySelect = (TextView)view.findViewById(R.id.countrySelect);
		if(AccountManager.getInstance().isKorean()){
			countrySelect.setText(account.countryNameKr);
		}else{
			countrySelect.setText(account.countryName);
		}
		
		
		
		this.selectedCountryCode = account.countryCode;
		countrySelect.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showCountryDialog();
				modified = true;
			}
		});
		
		ImageView pickupCountry = (ImageView)view.findViewById(R.id.pickupCountry);
		pickupCountry.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showCountryDialog();
				modified = true;
			}
		});
		
		this.editComment = (EditText)view.findViewById(R.id.editComment);
		this.editComment.setText(account.comment);
		this.editComment.setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				modified = true;
				return false;
			}
		});
		
		View changePasswordWrapper = view.findViewById(R.id.changePasswordWrapper);
		if(account.useSNS()){
			changePasswordWrapper.setVisibility(View.GONE);
		}else{
			this.passwordHiddenFrame = view.findViewById(R.id.passwordHiddenFrame);
			View changePasswordFrame = view.findViewById(R.id.changePasswordFrame);
			changePasswordFrame.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(passwordHiddenFrame.getVisibility() == View.GONE){
						passwordHiddenFrame.setVisibility(View.VISIBLE);
					}else{
						passwordHiddenFrame.setVisibility(View.GONE);
					}
					
				}
			});
			
			TextView txtBigtureId2 = (TextView)view.findViewById(R.id.txtBigtureId2);
			txtBigtureId2.setText(account.loginId);
			this.editPassword = (EditText)view.findViewById(R.id.editPassword);
			this.editPassword1 = (EditText)view.findViewById(R.id.editPassword1);
			this.editPassword2 = (EditText)view.findViewById(R.id.editPassword2);
			
			editPassword1.setOnFocusChangeListener(this);
			editPassword2.setOnFocusChangeListener(this);
			
			Button btnOK = (Button)view.findViewById(R.id.btnOK);
			btnOK.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(validatePasswd(true,true)){
						//변경시도를 한다.
						new ChangePasswordTask().execute(editPassword.getText().toString(),editPassword1.getText().toString());
					}
				}
			});
			
			
		}
		
		view.findViewById(R.id.withdrawTxt).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		return view;
	}

	private void selectBirthday(){
		int year  = DateUtil.getCurrentYear();
		int month = DateUtil.getCurrentMonth()-1;
		int day   = DateUtil.getCurrentDay();
		
		if (birthday != null){
			year = DateUtil.getYear(birthday);
			month = DateUtil.getMonth(birthday) - 1;
			day   = DateUtil.getDay(birthday);
		}
		
		DatePickerDialog dlg = new DatePickerDialog(context, this, year, month, day);
		dlg.show();
	}
	
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		birthday = DateUtil.createDate(year, monthOfYear+1, dayOfMonth);
		String dt = DateUtil.getDateFormatString(birthday, "yyyy-MM-dd");
		
		birthdayLabel.setText(dt);
		
		
		editNickname.clearFocus();
		
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus == false){
			switch(v.getId()){
			case R.id.editPassword1:
			case R.id.editPassword2:

				
				
				break;
			case R.id.editNickname:

				
				break;
			}
		}
	}
	
	/*
	 * 상위 Activity에게 country dialog를 띄우라고 handler를 통해 메시지를 보낸다.
	 */
	public void showCountryDialog(){
		
		listener.clickCountrySelect();
	}
	
	/**
	 * 나라가 선택이 된경우.
	 * @param country
	 */
	public void selectCountry(CountryEntity country){
		this.selectedCountryCode = country.code;
		
		countrySelect.setText(country.name);
		
		
		editNickname.clearFocus();
	}
	
	private boolean validatePasswd(boolean showAlert,boolean compareTest){
		String passwd = editPassword1.getText().toString();
		String passwd2 = editPassword2.getText().toString();
		
		if (passwd == null || passwd.length() < 6){
			canDone = false;
			if(showAlert){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Error");
				builder.setMessage("Password is too short.");
				builder.setNegativeButton("OK", null);
				builder.create().show();
				
				editPassword1.requestFocus();
				
			}
			return false;
		}else if (passwd.length() > 20){
			canDone = false;
			if(showAlert){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Error");
				builder.setMessage("Password is too long.");
				builder.setNegativeButton("OK", null);
				builder.create().show();
				
				editPassword1.requestFocus();
			}
			return false;
		}else if(compareTest && passwd != null && passwd2 != null && passwd.length() > 0 &&
				passwd2.length() > 0 && passwd.equals(passwd2) == false){
			canDone = false;
			if (showAlert){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Error");
				builder.setMessage("Password is not correct.");
				builder.setNegativeButton("OK", null);
				builder.create().show();
				
				editPassword2.setText("");
				editPassword2.requestFocus();
			}
			return false;
		}
		canDone = true;
		return true;
	}
	
	public SaveAccountEntity validateAndGetData(){
		SaveAccountEntity entity = new SaveAccountEntity();
		entity.setValid(true);
		String nickName = editNickname.getText().toString();
		
		if (nickName == null || nickName.length() == 0){
			canDone = false;
			entity.setValid(false);
		}
		entity.setNickName(nickName);
		
		if (birthday == null){
			canDone = false;
			entity.setValid(false);
		}
		//entity.setBirthday(DateUtil.getDateFormatString(birthday, "yyyy-MM-dd"));
		entity.setBirthday(birthdayLabel.getText().toString() + "-01-01");
		
		if (this.selectedCountryCode == null){
			canDone = false;
			entity.setValid(false);
		}
		entity.setCountryCode(selectedCountryCode);
		entity.setComment(editComment.getText().toString());
		entity.setGender(btnMale.isSelected() ? "M" : "F");
		if(profileImageEntity != null){
			entity.setTempImageId(profileImageEntity.id);
		}
		
		return entity;
	}
	
	public void close(){
		listener.closeWindow();
	}
	
	public interface CountrySelectListener{
		public void clickCountrySelect();
		public void closeWindow();
	}
	
	class ChangePasswordTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			String oldPassword = params[0];
			String newPassword = params[1];
			
			return LoginHandler.changePassword(oldPassword, newPassword);
		}

		@Override
		protected void onCancelled() {
			WaitDialog.hideWaitDialog();
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			WaitDialog.hideWaitDialog();
			super.onPostExecute(result);
			
			if(result){
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Notice");
				builder.setMessage(R.string.change_passwd_success);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which){
						new AsyncTask<Void,Void,Void>(){

							@Override
							protected Void doInBackground(Void... params){
								LoginHandler.logout();
								return null;
							}

							@Override
							protected void onPostExecute(Void result){
								AccountManager manager = AccountManager.getInstance();
								manager.setAutoLogin(false);
								manager.setLogin(false);

								super.onPostExecute(result);
								close();
							}
							
						}.execute();				
					}
				});
				
				builder.setNegativeButton("Cancel", null);
				builder.create().show();
				
				
			}else{
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Notice");
				builder.setMessage(R.string.change_passwd_error);
				builder.setPositiveButton("OK",null);
				builder.create().show();
			}
			
			
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			WaitDialog.showWailtDialog(getActivity(), false);
			
		}
		
	}
	
	public void showUploadImage(String path){
		
		Bitmap bitmap = ImageUtil.loadFromFile(path, bitmapOptions);
		profileImage.setImageBitmap(bitmap);
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Uri imageUri = null;
		Intent intent = null;
		if(resultCode == Activity.RESULT_OK){
			switch(requestCode){
			case BigtureEnvironment.REQ_CODE_TAKE_PICTURE:
				String tempPath = FileUtil.getTempPath(context) + "/taken.image";
				File file = new File(tempPath);
				imageUri = Uri.fromFile(file);

				intent = new Intent(context, CropActivity.class);
				intent.putExtra("imageUri", imageUri);
				intent.putExtra("returnImageWidth", 80);
				intent.putExtra("returnImageHeight", 80);
				intent.putExtra("aspectX", 80);
				intent.putExtra("aspectY", 80);
				
				startActivityForResult(intent, BigtureEnvironment.REQ_CODE_CROP_PICTURE);
				break;
			case BigtureEnvironment.REQ_CODE_CROP_PICTURE:
				final Bundle bundle = data.getExtras();
				
				String filePath = bundle.getString("imagePath");
				String newFilePath = FileUtil.getTempPath(context) + File.separator + System.currentTimeMillis() + ".image";
				try{
					FileUtil.moveFile(filePath, newFilePath);
					modified = true;
					new UploadImageTask().execute(newFilePath);
					
				}catch (Exception e){
					e.printStackTrace();
				}		
				
				break;
			case BigtureEnvironment.REQ_CODE_PICKUP_PICTURE:
				imageUri = data.getData();

				intent = new Intent(context, CropActivity.class);
				intent.putExtra("imageUri", imageUri);
				intent.putExtra("returnImageWidth", 80);
				intent.putExtra("returnImageHeight", 80);
				intent.putExtra("aspectX", 80);
				intent.putExtra("aspectY", 80);
				
				startActivityForResult(intent, BigtureEnvironment.REQ_CODE_CROP_PICTURE);
				
				break;
			}
			
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	class UploadImageTask extends AsyncTask<String, Void, TempImgEntity>{

		@Override
		protected TempImgEntity doInBackground(String... params) {
			String filePath = params[0];
			
			File image = new File(filePath);
			
			TempImgEntity tmpImage = ProfileHandler.updateProfileImage(image, filePath);
			
			return tmpImage;
		}

		@Override
		protected void onPostExecute(TempImgEntity data) {
			WaitDialog.hideWaitDialog();
			if(data != null){
				//addNewsImage(data.localImagePath,data.id);
				showUploadImage(data.localImagePath);
				profileImageEntity = data;
				
			}else{
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Error	");
				builder.setMessage(R.string.msg_upload_error);
				builder.setPositiveButton("OK", null);
				builder.create().show();
			}
			
			super.onPostExecute(data);
		}

		@Override
		protected void onCancelled() {
			WaitDialog.hideWaitDialog();
			super.onCancelled();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			WaitDialog.showWailtDialog(getActivity(),false);
		}
		
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}
	
}
