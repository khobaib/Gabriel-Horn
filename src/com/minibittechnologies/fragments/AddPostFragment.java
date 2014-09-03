package com.minibittechnologies.fragments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import javax.security.auth.PrivateCredentialPermission;

import org.woodyguthriecenter.app.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.minibittechnologies.adapter.NothingSelectedSpinnerAdapter;
import com.minibittechnologies.fragments.FragmentMore.OnDataPass;
import com.minibittechnologies.interfaces.OnDateOrTimSetListener;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class AddPostFragment extends Fragment {

	OnDataPass dataPasser;

	private final String TAG = this.getClass().getSimpleName();
	private static final int CAMERA_REQ_CODE = 901;
	private static final int GALLERY_REQ_CODE = 902;
	private static final String[] monthArray = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct",
			"Nov", "Dec" };

	private Uri mImageCaptureUri;
	private File picFile=null;

	private LinearLayout llImageHolder;
	// private ImageView ivToPostimage;
	private boolean isImageVisible = false;

	private Bitmap scaledBmp;

	private Button bCamera, bHLink,btnAddPost;
	private ImageView ivSelectedImage;
	private LinearLayout llDateLayout, llTimeLayout;
	private EditText edtCategory;
	private Spinner spCategory;
	String[] catList={"Offer","Event","Post"};

	View v;
	Activity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(">>>>>>", "onCreate, LoginFragment");

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		dataPasser = (OnDataPass) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e(">>>>>>", "onCreateView, fragment_add_post");
		v = inflater.inflate(R.layout.fragment_add_post, container, false);

		bCamera = (Button) v.findViewById(R.id.btnCamera);
		bCamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPicDialog();

			}
		});

		bHLink = (Button) v.findViewById(R.id.btnHyperLink);
		bHLink.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addHyperLink();

			}
		});

		ivSelectedImage = (ImageView) v.findViewById(R.id.ivToBePostedImage);
		ivSelectedImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPicRemovePrompt();

			}
		});
      btnAddPost=(Button)v.findViewById(R.id.btnAddPost);
      btnAddPost.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			byte[] data=new byte[(int)picFile.length()];
			FileInputStream fileInputStream;
			try {


				fileInputStream = new FileInputStream(picFile);
				fileInputStream.read(data);
				ParseFile pFile=new ParseFile(picFile.getName(), data);
				ParseObject parseObject=ParseObject.create("Post");
				parseObject.put("contents","test upload");
				parseObject.put("image",pFile);
				parseObject.put("title","myImage");
				parseObject.saveInBackground(new SaveCallback() {
					
					@Override
					public void done(ParseException e) {
						if(e==null)

						{
							Log.e("TAG","success");

						}
						else
						{
							Log.e("TAG","error");
						}
					}
				});
			} catch (IOException e) {

				e.printStackTrace();
			}




		}
	});
      edtCategory=(EditText)v.findViewById(R.id.etCategoryAddPost);
    /*  edtCategory.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			
		}
	});*/
      edtCategory.setOnTouchListener(new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent arg1) {
			spCategory.performClick();
			return true;
		}
	});
      spCategory=(Spinner)v.findViewById(R.id.sp_catgory);
      spCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View v, int position,
				long id) {
			
			if(position>0)
			{
				edtCategory.setText(catList[position-1]);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	});
      setSpinner();
		llImageHolder = (LinearLayout) v.findViewById(R.id.llImageToPostHolderAddPost);
		isImageVisible = false;
		llImageHolder.setVisibility(View.GONE);

		llDateLayout = (LinearLayout) v.findViewById(R.id.llDateAddPost);
		llDateLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatePicker();

			}
		});

		llTimeLayout = (LinearLayout) v.findViewById(R.id.llTimeAddPost);
		llTimeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showTimePicker();

			}
		});

		if (savedInstanceState != null) {
			picFile = (File) savedInstanceState.getSerializable("post_pic");
			isImageVisible = savedInstanceState.getBoolean("is_image_visible");
			if (isImageVisible)
				setImageInTheView();
		}

		return v;
	}
		private void setSpinner()
		{ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_dropdown_item,Arrays.asList(catList));
        spCategory.setAdapter(new NothingSelectedSpinnerAdapter(myAdapter, R.layout.row_spinner_nothing_selected,                         
                getActivity()));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
		}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		activity = getActivity();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (picFile == null)
			setImageFile();
		outState.putSerializable("post_pic", picFile);
		outState.putBoolean("is_image_visible", isImageVisible);
	}

	// Time --- Time
	private void showTimePicker() {
		DialogFragment df = new DateOrTimePickerFragment(new OnDateOrTimSetListener() {
			@Override
			public void dateOrTimeSet(int hour, int minute, int ignored) {
				// TODO Auto-generated method stub
				Log.d(TAG, "Hour: " + hour + ", minute: " + minute + ", ignored: " + ignored);
				TextView t = (TextView) v.findViewById(R.id.tvHourAddPost);
				if (hour >= 12) {
					t.setText((hour - 12) + "");
					t = (TextView) v.findViewById(R.id.tvDayPosiAddPost);
					t.setText("PM");
				} else {
					t.setText(hour + "");
					t = (TextView) v.findViewById(R.id.tvDayPosiAddPost);
					t.setText("AM");
				}
				t = (TextView) v.findViewById(R.id.tvMinAddPost);
				t.setText(minute + "");
			}
		}, DateOrTimePickerFragment.TIME_PICKER);
		df.show(((FragmentActivity) activity).getSupportFragmentManager(), "time_picker");
	}

	// Date --- Date
	private void showDatePicker() {
		DialogFragment df = new DateOrTimePickerFragment(new OnDateOrTimSetListener() {
			@Override
			public void dateOrTimeSet(int year, int month, int day) {
				Log.d(TAG, "Year: " + year + ", month: " + month + ", day: " + day);
				TextView t = (TextView) v.findViewById(R.id.tvYearAddPost);
				t.setText(year + "");
				t = (TextView) v.findViewById(R.id.tvMonthAddPost);
				t.setText(monthArray[month]);
				t = (TextView) v.findViewById(R.id.tvDateAddPost);
				t.setText(day + "");
			}
		}, DateOrTimePickerFragment.DATE_PICKER);
		df.show(((FragmentActivity) activity).getSupportFragmentManager(), "date_picker");
	}

	private void showPicRemovePrompt() {
		final Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		dialog.setContentView(R.layout.dialog_pic_remove);

		dialog.findViewById(R.id.btnRemoveDialogPicRemover).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				llImageHolder.setVisibility(View.GONE);
				isImageVisible = false;
				scaledBmp = null;
				if (picFile == null)
					setImageFile();
				if (picFile.delete())
					Log.e(TAG, "picFile is deleted from the SD card.");
				dialog.dismiss();
			}
		});
		dialog.findViewById(R.id.btnCancelDialogPicRemover).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		// Center-focus the dialog
		Window window = dialog.getWindow();
		window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.BOTTOM);
		// The below code is EXTRA - to dim the parent view by 70% :D
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.dimAmount = 0.7f;
		lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		dialog.getWindow().setAttributes(lp);
		dialog.show();
	}

	private void addHyperLink() {
		final Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		dialog.setContentView(R.layout.dialog_add_link);
		// TODO Handle input data

		// Center-focus the dialog
		Window window = dialog.getWindow();
		window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.CENTER);

		// The below code is EXTRA - to dim the parent view by 70% :D
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.dimAmount = 0.7f;
		lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		dialog.getWindow().setAttributes(lp);
		dialog.show();
	}

	private void showPicDialog() {

		final Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		dialog.setContentView(R.layout.dialog_pic_taker);
		Button btnTakePic = (Button) dialog.findViewById(R.id.btnCameraDialogPicTaker);
		btnTakePic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				takePicture();
				dialog.dismiss();
			}
		});
		Button btnChoosePic = (Button) dialog.findViewById(R.id.btnChooseDialogPicTaker);
		btnChoosePic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setImageFile();
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType("image/*");
				startActivityForResult(intent, GALLERY_REQ_CODE);
				dialog.dismiss();
			}
		});
		Button btnCancel = (Button) dialog.findViewById(R.id.btnCancelDialogPicTaker);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		// Center-focus the dialog
		Window window = dialog.getWindow();
		window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.BOTTOM);

		// The below code is EXTRA - to dim the parent view by 70% :D
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.dimAmount = 0.7f;
		lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		dialog.getWindow().setAttributes(lp);
		dialog.show();
	}

	private void takePicture() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			setImageFile();

			// set uri from the file
			if (isSDCardMounted()) {
				mImageCaptureUri = Uri.fromFile(picFile);
			} else {
				Toast.makeText(activity, "Media Not Mounted!", Toast.LENGTH_LONG).show();
				return;
			}
			intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
			//intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
			intent.putExtra("return-data", true);
			Log.d(TAG, "cam intent starting");
			startActivityForResult(intent, CAMERA_REQ_CODE);
		} catch (ActivityNotFoundException e) {
			Log.d("Error", "Activity Not Found" + e.toString());
		}
	}

	private void setImageFile() {
		// Set the file name
		File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Gabriel Horn");
		directory.mkdir();
		String mainDir = directory.toString();
		String photoFileName = "post_pic" + ".png";
		Log.d(TAG, photoFileName);
		picFile = new File(directory.getPath(), photoFileName);
	}

	private int getCorrectionAngleForCam() throws IOException {
		ExifInterface exif = new ExifInterface(picFile.getPath());
		int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

		int angle = 0;
		if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
			angle = 90;
		} else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
			angle = 180;
		} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
			angle = 270;
		}
		Log.d(TAG, "angle = " + angle);
		return angle;
	}

	private void loadPicasaImageFromGallery(final Uri uri) {
		String[] projection = { MediaColumns.DATA, MediaColumns.DISPLAY_NAME };
		Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(MediaColumns.DISPLAY_NAME);
			if (columnIndex != -1) {
				new Thread(new Runnable() {
					public void run() {
						try {
							Bitmap bitmap = android.provider.MediaStore.Images.Media.getBitmap(
									activity.getContentResolver(), uri);
							scaledBmp = Bitmap.createScaledBitmap(bitmap, 200, 200, true);

							activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									try {
										FileOutputStream out = new FileOutputStream(picFile);
										scaledBmp.compress(Bitmap.CompressFormat.PNG, 90, out);
										mImageCaptureUri = Uri.fromFile(picFile);
										out.close();
										// ivProfilePic.setImageBitmap(scaledBmp);
										// btnUpdate.setVisibility(View.VISIBLE);
									} catch (FileNotFoundException e) {
										e.printStackTrace();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							});

						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}).start();
			}
		}
		cursor.close();

	}

	public String getPath(Uri uri) {
		String[] projection = { MediaColumns.DATA };
		Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
		if (cursor != null) {
			// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
			// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			String filePath = cursor.getString(columnIndex);
			cursor.close();
			return filePath;
		} else
			return uri.getPath();
	}

	private Bitmap decodeFile(File f, int imageQuality) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			FileInputStream stream1 = new FileInputStream(f);
			BitmapFactory.decodeStream(stream1, null, o);
			stream1.close();

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = imageQuality;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;

			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}
			Log.i("SCALE", "scale = " + scale);

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			FileInputStream stream2 = new FileInputStream(f);
			Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
			stream2.close();
			return bitmap;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean isSDCardMounted() {
		String status = Environment.getExternalStorageState();

		if (status.equals(Environment.MEDIA_MOUNTED))
			return true;
		return false;
	}

	public static void copyStream(InputStream input, OutputStream output) throws IOException {

		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == activity.RESULT_OK) {
			switch (requestCode) {
			case CAMERA_REQ_CODE:
				Log.d(TAG, "cam onActivityResult");
				try {
					if (picFile == null)
						setImageFile();
					//Log.e("msg",data.getData().toString());
					Bitmap bmp = decodeFile(picFile, 500);
					int angle = getCorrectionAngleForCam();
					int w = bmp.getWidth();
					if (w < 200)
						w = 200;
					int h = bmp.getHeight();
					if (h < 200)
						h = 200;
					if (angle == 0) {
						scaledBmp = Bitmap.createScaledBitmap(bmp, w, h, true);
					} else {
						Matrix mat = new Matrix();
						mat.postRotate(angle);
						Bitmap correctBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);
						scaledBmp = Bitmap.createScaledBitmap(correctBmp, w, h, true);
						Log.d("", "scaled");
					}

					try {
						FileOutputStream out = new FileOutputStream(picFile);
						scaledBmp.compress(Bitmap.CompressFormat.PNG, 90, out);
						mImageCaptureUri = Uri.fromFile(picFile);
						out.close();
					} catch (Exception e) {
						Log.e("Error_Touhid", e.toString());
					}
					// code b4 crop: ivProfilePic.setImageBitmap(scaledBmp);
					// btnUpdate.setVisibility(View.VISIBLE);
				} catch (IOException e) {
					Toast.makeText(activity, "IOException - Failed to load", Toast.LENGTH_SHORT).show();
					Log.e("Camera", e.toString());
				} catch (OutOfMemoryError oom) {
					Toast.makeText(activity, "OOM error - Failed to load", Toast.LENGTH_SHORT).show();
					Log.e("Camera", oom.toString());
				}
				Log.d(TAG, "starting crop");
				saveImage();
				//
				// doCrop();
				// imageUriToString = mImageCaptureUri.toString();
				break;

			case GALLERY_REQ_CODE:
				try {
					mImageCaptureUri = data.getData();

					if (getPath(mImageCaptureUri) != null) {

						InputStream inputStream = activity.getContentResolver().openInputStream(mImageCaptureUri);
						FileOutputStream fileOutputStream = new FileOutputStream(picFile);
						copyStream(inputStream, fileOutputStream);
						fileOutputStream.close();
						inputStream.close();

						Log.d(TAG, "Gal code: starting crop");
						saveImage();
					} else {
						System.out.println("Picasa Image!");
						loadPicasaImageFromGallery(mImageCaptureUri);
						saveImage();
					}

				} catch (Exception e) {
					Log.e(TAG, "Error while creating thr picture file", e);
					if (picFile == null)
						Log.i(TAG, "<<<<<<<<<<<<<<<<<< Pic file is null !! >>>>>>>>>>>>>>>>>");
				}
				// doCrop();
				// imageUriToString = mImageCaptureUri.toString();
				break;
			}
		}
	}

	private void saveImage() {
		scaledBmp = BitmapFactory.decodeFile(picFile.getPath());
		scaledBmp = Bitmap.createScaledBitmap(scaledBmp, 200, 200, true);
		setImageInTheView();
	}

	private void setImageInTheView() {
		llImageHolder.setVisibility(View.VISIBLE);
		isImageVisible = true;
		ImageView ivToPostimage = (ImageView) v.findViewById(R.id.ivToBePostedImage);
		ivToPostimage.setBackgroundResource(android.R.color.transparent);
		if (scaledBmp == null) {
			if (picFile == null)
				setImageFile();
			scaledBmp = decodeFile(picFile, 500);
			if (scaledBmp == null) {
				Log.e(TAG, "No image is found :o ... How dare you wanting to set it after removing it >:(");
				return;
			}
		}
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int dw = dm.widthPixels;
		int iw = scaledBmp.getWidth();
		int ih = scaledBmp.getHeight();
		int nh = (int) (((float) dw / iw) * ih);
		scaledBmp = Bitmap.createScaledBitmap(scaledBmp, dw, nh, true);
		ivToPostimage.setImageBitmap(scaledBmp);
	}

}
