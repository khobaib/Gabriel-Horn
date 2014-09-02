/**
 * 
 */
package com.minibittechnologies.activity;

import java.io.File;

import org.woodyguthriecenter.app.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_add_post);
		findViewById(R.id.btnCamera).setOnClickListener(this);
		findViewById(R.id.btnHyperLink).setOnClickListener(this);
		// TODO set spinners on click

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

		default:
			break;
		}
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
		// TODO implement onActivityResult
	}

	private void takePicture() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			// Set the file name
			File directory = new File(Environment.getExternalStorageDirectory(), "Gabriel Horn");
			String mainDir = directory.toString();
			String photoFileName = "profile_pic" + ".png";
			Log.d(TAG, photoFileName);
			picFile = new File(mainDir, photoFileName);

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

	private boolean isSDCardMounted() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED))
			return true;
		return false;
	}

}
