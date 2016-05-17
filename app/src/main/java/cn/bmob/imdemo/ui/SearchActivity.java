/**
 * 
 */
package cn.bmob.imdemo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.bean.QiangYu;
import cn.bmob.imdemo.bean.User;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

/**
 * 搜索Activity
 * @author 飞雪无情
 * @since 2011-3-8
 */
public class SearchActivity extends Activity {
	private ListView list;
	private SimpleAdapter simpleAdapter;
	private ArrayList<QiangYu> mListItems = new ArrayList<QiangYu>();
	//tab01临时数据
	private String[] names = new String[]{"Mr.M","NiHuang","Jw","mz"};
	private String[] descs = new String[]{"Age wrinkles the body. Quitting wrinkles the soul.",
			"You know my loneliness is only kept for you, my sweet songs are only sung for you.","皇七子萧景琰，林殊（梅长苏）儿时挚友","禁军大统领，赤焰军旧部"};
	private int[] imageIds = new int[]{R.drawable.user, R.drawable.nh,R.drawable.jw,R.drawable.mz,R.drawable.mz};
	private int[] photos = new int[]{R.drawable.mcs2,R.drawable.nh,R.drawable.jw2,R.drawable.mz2};

	String username;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		init();
		fetchData();
	}
	private void init(){
		list = (ListView) findViewById(R.id.listView1);
	}
	private void tab01Adapter(){
    	//创建list集合
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < mListItems.size(); i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("imageView1",imageIds[i]);
//			listItem.put("textView1",mListItems.get(i).getAuthor().toString());
			String s = mListItems.get(i).getAuthor().getObjectId().toString();
			listItem.put("textView1",getUserById(s));
			listItem.put("desc",mListItems.get(i).getContent().toString());
			listItem.put("photo",mListItems.get(i).getContentfigureurl().getUrl());
			listItems.add(listItem);
		}
		//创建SimpleAdapter
		simpleAdapter = new SimpleAdapter(this, listItems, R.layout.activity_list_view,
				new String[] {"textView1", "imageView1","desc","photo"},
				new int[] {R.id.textView1, R.id.imageView1, R.id.desc, R.id.imageView2});
		//为ListView设置Adapter
		list.setAdapter(simpleAdapter);
    }
	public void fetchData(){
		BmobQuery<QiangYu> query = new BmobQuery<QiangYu>();
		query.order("createdAt");
		query.findObjects(this, new FindListener<QiangYu>() {

			@Override
			public void onSuccess(List<QiangYu> list) {
				mListItems.addAll(list);
				Toast.makeText(SearchActivity.this, "总共有"+mListItems.size()+"条动态",Toast.LENGTH_SHORT).show();
				tab01Adapter();
			}

			@Override
			public void onError(int arg0, String arg1) {
			}
		});
	}
	public String getUserById(String s){
		BmobQuery<User> query = new BmobQuery<User>();
		query.getObject(this, s, new GetListener<User>() {

			@Override
			public void onSuccess(User object) {
				username = object.getUsername().toString();
			}

			@Override
			public void onFailure(int code, String arg0) {
				Toast.makeText(SearchActivity.this, "获取用户名失败", Toast.LENGTH_SHORT).show();
			}

		});
		return username;
	}
}
