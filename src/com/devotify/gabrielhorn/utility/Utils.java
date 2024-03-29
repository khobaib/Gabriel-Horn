package com.devotify.gabrielhorn.utility;

import com.devotify.gabrielhorn.R;

import com.parse.ParseObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.WindowManager.BadTokenException;
import android.widget.Toast;

public class Utils {
	public static final String PREF_NAME = "HGHORN";
	public static final int MODE = Context.MODE_WORLD_WRITEABLE;
	public static final String KEY_PARENT_APP_ID = "parent_app_id";
	public static final String APP_COMPANY_PHONE = "app_company_phone";
	public static final String APP_COMPANY_EMAIL = "app_company_email";
	public static final String VISIT_OUR_SITE = "site_url";
	public static ParseObject appCompany=null;
	public static void writeBoolean(Context context, String key, boolean value) {
		getEditor(context).putBoolean(key, value).commit();
	}

	public static boolean readBoolean(Context context, String key, boolean defValue) {
		return getPreferences(context).getBoolean(key, defValue);
	}

	public static void writeInteger(Context context, String key, int value) {
		getEditor(context).putInt(key, value).commit();

	}

	public static int readInteger(Context context, String key, int defValue) {
		return getPreferences(context).getInt(key, defValue);
	}

	public static void writeString(Context context, String key, String value) {
		getEditor(context).putString(key, value).commit();

	}

	public static String readString(Context context, String key, String defValue) {
		return getPreferences(context).getString(key, defValue);
	}

	public static void writeFloat(Context context, String key, float value) {
		getEditor(context).putFloat(key, value).commit();
	}

	public static float readFloat(Context context, String key, float defValue) {
		return getPreferences(context).getFloat(key, defValue);
	}

	public static void writeLong(Context context, String key, long value) {
		getEditor(context).putLong(key, value).commit();
	}

	public static long readLong(Context context, String key, long defValue) {
		return getPreferences(context).getLong(key, defValue);

	}

	public static SharedPreferences getPreferences(Context context) {
		return context.getSharedPreferences(PREF_NAME, MODE);
	}

	public static Editor getEditor(Context context) {
		return getPreferences(context).edit();
	}

	public static void remove(Context context, String key) {
		getEditor(context).remove(key);

	}
	public static  void toast(Context contxt,String str)
	{
		Toast.makeText(contxt,str,Toast.LENGTH_LONG).show();
		
	}
	public static ProgressDialog createProgressDialog(Context mContext) {
		ProgressDialog dialog = new ProgressDialog(mContext);
		try {
			dialog.show();
		} catch (BadTokenException e) {

		}
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.progress_dialog);
		// dialog.setMessage(Message);
		return dialog;
	}

}
