package ru.willir.dnaviewer.utils;

import android.util.Log;

final public class DLog {
	
	public final static String TAG = "DnaReader";

	public static final void i(String msg) {
		Log.i(TAG, msg);
	}

	public static final void e(String msg) {
		Log.e(TAG, msg);
	}

	public static final void v(String msg) {
		Log.v(TAG, msg);
	}

	public static final void e(Exception e) {
		Log.e(TAG, e.getLocalizedMessage());
		StackTraceElement[] ste = e.getStackTrace();
		for (int i = 0; i < ste.length; i++) {
			Log.e(TAG, "Class " + ste[i].getClassName() + " in " + ste[i].getMethodName() + " line " + ste[i].getLineNumber());			
		}
	}

	public static final void p(Exception e){
		e.printStackTrace();
	}

	public static final void d(String msg) {
		Log.d(TAG, msg);
	}

	public static final void w(String msg) {
		Log.w(TAG, msg);
	}
}
