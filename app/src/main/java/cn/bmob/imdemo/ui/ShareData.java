package cn.bmob.imdemo.ui;

import android.app.Application;

public class ShareData extends Application {
	private static String username;

	public static String getUsername() {
		return username;
	}

	public static void setUsername(String username) {
		ShareData.username = username;
	}
}
