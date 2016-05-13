/**
 * 
 */
package cn.bmob.imdemo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.model.UserModel;

/**
 * 
 */
public class MoreActivity extends Activity {
	private TextView am_tv_username;
	private TextView am_tv_explain;
	private String pn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		init();
		
	}
	public void init(){
		am_tv_username = (TextView)findViewById(R.id.am_tv_username);
		am_tv_explain = (TextView)findViewById(R.id.am_tv_explain);
		String username = UserModel.getInstance().getCurrentUser().getUsername();
		am_tv_username.setText(TextUtils.isEmpty(username)?"":username);
	}
}
