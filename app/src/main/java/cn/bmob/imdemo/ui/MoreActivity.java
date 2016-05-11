/**
 * 
 */
package cn.bmob.imdemo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.bean.User;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

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
		am_tv_username.setText(ShareData.getUsername());
		User u = new User();
		u.setPersonalNote("填写一下个人说明吧");
		pn = u.getPersonalNote();
		u.update(this, "cdba84a0fa", new UpdateListener() {
		    @Override
		    public void onSuccess() {
		        Log.i("bmob","更新成功：");
		    }

		    @Override
		    public void onFailure(int code, String msg) {
		        Log.i("bmob","更新失败："+msg);
		    }
		});
		am_tv_explain.setText(pn);
		BmobQuery<User> query = new BmobQuery<User>();
		query.addWhereEqualTo("username", ShareData.getUsername());
		query.findObjects(this, new FindListener<User>() {
	        @Override
	        public void onSuccess(List<User> object) {
//	            toast("查询成功：共"+object.size()+"条数据。");
	            for (User user : object) {
	               user.getUsername();
	               user.getObjectId();
//	               user.setPersonalNote("填写一下个人说明吧");
	               pn = user.getPersonalNote();
	            }
	        }
	        @Override
	        public void onError(int code, String msg) {
//	            toast("查询失败："+msg);
	        }
		});
	}
}
