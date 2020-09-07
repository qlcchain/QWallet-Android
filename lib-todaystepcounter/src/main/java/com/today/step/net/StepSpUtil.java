package com.today.step.net;

import android.content.Context;
import android.content.SharedPreferences;

public class StepSpUtil {
	private static SharedPreferences sp;
	
	public static void putBoolean(Context context, String checkOnOff, Boolean value) {
		if(sp ==null) {
			sp = context.getSharedPreferences("step_config", Context.MODE_PRIVATE);
		}
		sp.edit().putBoolean(checkOnOff, value).commit();
	}

	public static boolean getBoolean(Context context, String key, Boolean defValue) {
		if(sp ==null) {
			sp = context.getSharedPreferences("step_config", Context.MODE_PRIVATE);

		}
		return sp.getBoolean(key, defValue);
	}

	public static void putString(Context context, String key, String value) {
		if(sp ==null) {
			sp = context.getSharedPreferences("step_config", Context.MODE_PRIVATE);
		}
		sp.edit().putString(key, value).commit();
	}

	public static String getString(Context context, String key, String defValue) {
		if(sp ==null) {
			sp = context.getSharedPreferences("step_config", Context.MODE_PRIVATE);

		}
		return sp.getString(key, defValue);
	}

	public static void putInt(Context context, String key, int value) {
		if(sp ==null) {
			sp = context.getSharedPreferences("step_config", Context.MODE_PRIVATE);
		}
		sp.edit().putInt(key, value).commit();
	}

	public static int getInt(Context context, String key, int defValue) {
		if(sp ==null) {
			sp = context.getSharedPreferences("step_config", Context.MODE_PRIVATE);

		}
		return sp.getInt(key, defValue);
	}
	public static void putLong(Context context, String key, long value) {
		if(sp ==null) {
			sp = context.getSharedPreferences("step_config", Context.MODE_PRIVATE);
		}
		sp.edit().putLong(key, value).commit();
	}

	public static long getLong(Context context, String key, long defValue) {
		if(sp ==null) {
			sp = context.getSharedPreferences("step_config", Context.MODE_PRIVATE);
		}
		return sp.getLong(key, defValue);
	}
}
