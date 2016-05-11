package cn.bmob.imdemo.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.ui.home.HomeActivity;
import cn.bmob.v3.Bmob;


public class MainActivity extends TabActivity implements OnCheckedChangeListener {
	private RadioGroup mainTab;
	private TabHost mTabHost;
	
	private DrawerLayout mDrawerLayout;
	private View mHomeActivity;
	private ImageView mHomeTopLeftImg;
	private String[] menuList;
	private ListView mLv;
	
//	private String token = "vSrMyQAVMn8/eEo9pP9wBiGfALL49vWjvQ7M5yTBOy6i60MKvk/+3DOu82Uj25X/L7SuYblstIfIK1LGg+Vd8A==";
	//内容Intent
	private Intent mHomeIntent;
	private Intent mNewsIntent;
	private Intent mInfoIntent;
	private Intent mSearchIntent;
	private Intent mMoreIntent;
	
	private final static String TAB_TAG_HOME="tab_tag_home";
	private final static String TAB_TAG_NEWS="tab_tag_news";
	private final static String TAB_TAG_INFO="tab_tag_info";
	private final static String TAB_TAG_SEARCH="tab_tag_search";
	private final static String TAB_TAG_MORE="tab_tag_more";
    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 初始化 Bmob SDK
        Bmob.initialize(this, "99a4b37b7c186140860d4ffe0aabc013");
        setContentView(R.layout.activity_main);
		/*//透明状态栏  
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  
		//透明导航栏  
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);*/
        initDrawerLayout();
        mainTab=(RadioGroup)findViewById(R.id.main_tab);
        mainTab.setOnCheckedChangeListener(this);
        prepareIntent();
        setupIntent();

		/*mHomeTopLeftImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "显示左侧抽屉", Toast.LENGTH_SHORT).show();
				mDrawerLayout.openDrawer(R.id.id_drawer);
			}
		});*/
    }

    /**
     * 准备tab的内容Intent
     */
	private void prepareIntent() {
		mHomeIntent=new Intent(this, HomeActivity.class);
		mNewsIntent=new Intent(this, IMActivity.class);
		mInfoIntent=new Intent(this, AddActivity.class);
		mSearchIntent=new Intent(this,SearchActivity.class);
		mMoreIntent=new Intent(this, MoreActivity.class);
	}
	/**
	 * 
	 */
	private void setupIntent() {
		this.mTabHost=getTabHost();
		TabHost localTabHost=this.mTabHost;
		localTabHost.addTab(buildTabSpec(TAB_TAG_HOME, R.string.main_home, R.drawable.icon_1_n, mHomeIntent));
		localTabHost.addTab(buildTabSpec(TAB_TAG_NEWS, R.string.main_news, R.drawable.icon_2_n, mNewsIntent));
		localTabHost.addTab(buildTabSpec(TAB_TAG_INFO, R.string.main_add, R.drawable.icon_3_n, mInfoIntent));
		localTabHost.addTab(buildTabSpec(TAB_TAG_SEARCH, R.string.menu_search, R.drawable.icon_4_n, mSearchIntent));
		localTabHost.addTab(buildTabSpec(TAB_TAG_MORE, R.string.more, R.drawable.icon_5_n, mMoreIntent));
	}
	/**
	 * 构建TabHost的Tab页
	 * @param tag 标记
	 * @param resLabel 标签
	 * @param resIcon 图标
	 * @param content 该tab展示的内容
	 * @return 一个tab
	 */
	private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon,final Intent content) {
		return this.mTabHost.newTabSpec(tag).setIndicator(getString(resLabel),
				getResources().getDrawable(resIcon)).setContent(content);
	} 
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(checkedId){
		case R.id.radio_button0:
			this.mTabHost.setCurrentTabByTag(TAB_TAG_HOME);
			break;
		case R.id.radio_button1:
			this.mTabHost.setCurrentTabByTag(TAB_TAG_NEWS);
			break;
		case R.id.radio_button2:
//			this.mTabHost.setCurrentTabByTag(TAB_TAG_INFO);
			startActivity(new Intent(MainActivity.this, AddActivity.class));
			overridePendingTransition(R.anim.bottom_out, R.anim.bottom_in);//跳转动画
			break;
		case R.id.radio_button3:
			this.mTabHost.setCurrentTabByTag(TAB_TAG_SEARCH);
			break;
		case R.id.radio_button4:
			this.mTabHost.setCurrentTabByTag(TAB_TAG_MORE);
			break;
		}
	}
	/*
	 * 初始化侧滑抽屉
	 */
	private void initDrawerLayout() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerlayout);
		mDrawerLayout.setDrawerListener(new DrawerListener() {
			/**
			 * 当抽屉滑动状态改变的时候被调用
			 * 状态值是STATE_IDLE（闲置--0）, STATE_DRAGGING（拖拽的--1）, STATE_SETTLING（固定--2）中之一。
			 * 抽屉打开的时候，点击抽屉，drawer的状态就会变成STATE_DRAGGING，然后变成STATE_IDLE
			 */
			@Override
			public void onDrawerStateChanged(int arg0) {
				Log.i("drawer", "drawer的状态：" + arg0);
			}
			/**
			 * 当抽屉被滑动的时候调用此方法
			 * arg1 表示 滑动的幅度（0-1）
			 */
			@Override
			public void onDrawerSlide(View arg0, float arg1) {
				Log.i("drawer", arg1 + "");
			}
			/**
			 * 当一个抽屉被完全打开的时候被调用
			 */
			@Override
			public void onDrawerOpened(View arg0) {
				Log.i("drawer", "抽屉被完全打开了！");
			}
			/**
			 * 当一个抽屉完全关闭的时候调用此方法
			 */
			@Override
			public void onDrawerClosed(View arg0) {
				Log.i("drawer", "抽屉被完全关闭了！");
			}
		});
		
		/**
		 * 也可以使用DrawerListener的子类SimpleDrawerListener, 
		 * 或者是ActionBarDrawerToggle这个子类(详见FirstDemoActivity)
		 */
		mDrawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
			}
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}
		});
		//添加ListView适配器
        mLv = (ListView) findViewById(R.id.id_lv);
		menuList = new String[] { "商店", "翻译助手","设置", "隐私","关于","退出"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, menuList);
        mLv.setAdapter(adapter);
		/*LayoutInflater flater = LayoutInflater.from(this);
		mHomeActivity = flater.inflate(R.layout.activity_home, null);
		mHomeTopLeftImg = (ImageView)mHomeActivity.findViewById(R.id.home_top_left_img);*/
	}
}
