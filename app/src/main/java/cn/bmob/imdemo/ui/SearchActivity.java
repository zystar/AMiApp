/**
 * 
 */
package cn.bmob.imdemo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.imdemo.R;

/**
 * 搜索Activity
 * @author 飞雪无情
 * @since 2011-3-8
 */
public class SearchActivity extends Activity {
	private ListView list;
	//tab01临时数据
	private String[] names = new String[]{"Mr.M","NiHuang","Jw","mz"};
	private String[] descs = new String[]{"Age wrinkles the body. Quitting wrinkles the soul.",
			"You know my loneliness is only kept for you, my sweet songs are only sung for you.","皇七子萧景琰，林殊（梅长苏）儿时挚友","禁军大统领，赤焰军旧部"};
	private int[] imageIds = new int[]{R.drawable.mcs, R.drawable.nh,R.drawable.jw,R.drawable.mz};
	private int[] photos = new int[]{R.drawable.mcs2,R.drawable.nh,R.drawable.jw2,R.drawable.mz2};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		init();
		tab01Adapter();
	}
	private void init(){
		list = (ListView) findViewById(R.id.listView1);
	}
	private void tab01Adapter(){
    	//创建list集合
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < names.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("imageView1",imageIds[i]);
			listItem.put("textView1",names[i]);
			listItem.put("desc",descs[i]);
			listItem.put("photo",photos[i]);
			listItems.add(listItem);
		}
		//创建SimpleAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.activity_list_view, 
				new String[] {"textView1", "imageView1","desc","photo"}, 
				new int[] {R.id.textView1, R.id.imageView1, R.id.desc, R.id.imageView2});
		//为ListView设置Adapter
		list.setAdapter(simpleAdapter);
    }
}
