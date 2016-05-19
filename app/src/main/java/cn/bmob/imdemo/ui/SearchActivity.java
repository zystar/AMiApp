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
import java.util.List;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.adapter.QiangYuListAdapter;
import cn.bmob.imdemo.bean.QiangYu;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

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
    	List<QiangYu> dataArray = new ArrayList<QiangYu>();
		for (int i = 0; i < mListItems.size(); i++){
			QiangYu qiangYu = new QiangYu(mListItems.get(i).getAuthor(),
					mListItems.get(i).getContent(),
					mListItems.get(i).getContentfigureurl());
			dataArray.add(qiangYu);
		}
		QiangYuListAdapter adapter=new QiangYuListAdapter(this, dataArray, list);
		list.setAdapter(adapter);
    }
	public void fetchData(){
		BmobQuery<QiangYu> query = new BmobQuery<QiangYu>();
		query.order("createdAt");
		query.include("author");//关联查询
		query.findObjects(this, new FindListener<QiangYu>() {

			@Override
			public void onSuccess(List<QiangYu> list) {
				mListItems.addAll(list);
				Toast.makeText(SearchActivity.this, "总共有" + mListItems.size() + "条动态", Toast.LENGTH_SHORT).show();
				tab01Adapter();
			}

			@Override
			public void onError(int arg0, String arg1) {
			}
		});
	}
}
