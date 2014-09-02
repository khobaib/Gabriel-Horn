/**
 * 
 */
package com.minibittechnologies.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @author Touhid
 * 
 */
public class AddPost extends Activity implements OnClickListener {

	private final String TAG = this.getClass().getSimpleName();
	private Uri mImageCaptureUri;
	private File picFile;
	private static final int CAMERA_REQ_CODE = 901;
	private static final int GALLERY_REQ_CODE = 902;

	private LinearLayout llImageHolder;
	// private ImageView ivToPostimage;
	private boolean isImageVisible = false;

	private Bitmap scaledBmp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_add_post);
		findViewById(R.id.btnCamera).setOnClickListener(this);
		findViewById(R.id.btnHyperLink).setOnClickListener(this);

		findViewById(R.id.ivToBePostedImage).setOnClickListener(this);
		llImageHolder = (LinearLayout) findViewById(R.id.llImageToPostHolderAddPost);
		isImageVisible = false;
		llImageHolder.setVisibility(View.GONE);
		// TODO set spinners on click

		if (savedInstanceState != null) {
			picFile = (File) savedInstanceState.getSerializable("post_pic");
			isImageVisible = savedInstanceState.getBoolean("is_image_visible");
			if (isImageVisible)
				setImageInTheView();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (picFile == null)
			setImageFile();
		outState.putSerializable("post_pic", picFile);
		outState.putBoolean("is_image_visible", isImageVisible);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnCamera:
			showPicDialog();
			break;
		case R.id.btnHyperLink:
			addHyperLink();
			break;
		case R.id.ivToBePostedImage:
			showPicRemovePrompt();
			break;

		default:
			break;
		}
	}

	private void showPicRemovePrompt() {
		final Dialog dialog = new Dialog(AddPost.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
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
		final Dialog dialog = new Dialog(AddPost.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
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

		final Dialog dialog = new Dialog(AddPost.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
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
				Toast.makeText(AddPost.this, "Media Not Mounted!", Toast.LENGTH_LONG).show();
				return;
			}
			intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
			intent.putExtra("return-data", true);
			Log.d(TAG, "cam intent starting");
			startActivityForResult(intent, CAMERA_REQ_CODE);
		} catch (ActivityNotFoundException e) {
			Log.d("Error", "Activity Not Found" + e.toString());
		}
	}

	private void setImageFile() {
		// Set the file name
		File directory = new File(Environment.getExternalStorageDirectory(), "Gabriel Horn");
		String mainDir = directory.toString();
		String photoFileName = "post_pic" + ".png";
		Log.d(TAG, photoFileName);
		picFile = new File(mainDir, photoFileName);
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
		Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(MediaColumns.DISPLAY_NAME);
			if (columnIndex != -1) {
				new Thread(new Runnable() {
					public void run() {
						try {
							Bitmap bitmap = android.provider.MediaStore.Images.Media.getBitmap(getContentResolver(),
									uri);
							scaledBmp = Bitmap.createScaledBitmap(bitmap, 200, 200, true);

							runOnUiThread(new Runnable() {
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
		Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CAMERA_REQ_CODE:
				Log.d(TAG, "cam onActivityResult");
				try {
					if (picFile == null)
						setImageFile();
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
					Toast.makeText(AddPost.this, "IOException - Failed to load", Toast.LENGTH_SHORT).show();
					Log.e("Camera", e.toString());
				} catch (OutOfMemoryError oom) {
					Toast.makeText(AddPost.this, "OOM error - Failed to load", Toast.LENGTH_SHORT).show();
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

						InputStream inputStream = getContentResolver().openInputStream(mImageCaptureUri);
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
		ImageView ivToPostimage = (ImageView) findViewById(R.id.ivToBePostedImage);
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
