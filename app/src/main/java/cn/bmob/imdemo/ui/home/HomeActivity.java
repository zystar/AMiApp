/**
 * 
 */
package cn.bmob.imdemo.ui.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;

import org.apache.commons.codec1.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.bmob.imdemo.R;

public class HomeActivity extends Activity implements SpeechSynthesizerListener{
	private ListView lv;
	private Button btn1;
	private EditText et1;
	private String arrs[] =new String[]{};
	private String fromTv;
	private TextView tranlateTv;
	private ImageView mHomeTopLeftImg;
	private View mMainActivity;
	private DrawerLayout mDrawerLayout;
	//创建list集合
	private List<Map<String, Object>> listItems;
	private SimpleAdapter simpleAdapter;
	
	private SQLiteDatabase db;
	private Cursor c;
	//复制粘贴
	ClipboardManager myClipboard;
	ClipData myClip;
	SpeechSynthesizer mSpeechSynthesizer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		init();
		adapter();
		initBaiduTts();
		//打开或创建inpson.db数据库  
        db = openOrCreateDatabase("inpson.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS mycollections (id INTEGER PRIMARY KEY AUTOINCREMENT, content VARCHAR)");
        //插入数据  
        c = db.rawQuery("select * from mycollections", null);
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
        	String content = c.getString(c.getColumnIndex("content"));
        	Log.e("content", content);
        	HashMap<String, Object> map = new HashMap<String, Object>();  
			map.put("home_lv_item_tv1", content);
			listItems.add(map);
			simpleAdapter.notifyDataSetChanged();
        }
		//添加收藏事件按钮
		btn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String s = et1.getText().toString();
				if(s == null || "".equals(s)){
					Toast.makeText(HomeActivity.this, "添加信息不能为空", Toast.LENGTH_SHORT).show();
				}else{
					HashMap<String, Object> map = new HashMap<String, Object>();  
					map.put("home_lv_item_tv1", s);
					listItems.add(map);
					simpleAdapter.notifyDataSetChanged();
					et1.setText("");
			        db.execSQL("insert into mycollections(content) VALUES (?)", new Object[]{s});
				}
			}
		});
		//ListView长按事件
		lv.setOnItemLongClickListener(new OnItemLongClickListener(){  
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				HashMap<String,String> map=(HashMap<String,String>)lv.getItemAtPosition(position);
				fromTv = map.get("home_lv_item_tv1");
				Toast.makeText(HomeActivity.this, fromTv, Toast.LENGTH_SHORT).show();
				showDialog(fromTv, position);
				return false;
			}  
        });
		/*//调用侧滑抽屉
		mHomeTopLeftImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(HomeActivity.this, "显示左侧抽屉", Toast.LENGTH_SHORT).show();
				mDrawerLayout.openDrawer(Gravity.START);
			}
		});*/
	}
	private void init(){
		lv = (ListView)findViewById(R.id.home_lv1);
		btn1 = (Button)findViewById(R.id.home_btn1);
		et1 = (EditText)findViewById(R.id.home_et1);// 初始化四个布局
		myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
		mHomeTopLeftImg = (ImageView)findViewById(R.id.home_top_left_img);

		/*LayoutInflater flater = LayoutInflater.from(this);
		mMainActivity = flater.inflate(R.layout.activity_main, null);
		mDrawerLayout = (DrawerLayout) mMainActivity.findViewById(R.id.id_drawerlayout);*/
	}
	private void adapter(){
		listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < arrs.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("home_lv_item_tv1",arrs[i]);
			listItems.add(listItem);
		}
		//创建SimpleAdapter
		simpleAdapter = new SimpleAdapter(this, listItems, R.layout.home_listview_item, 
				new String[] {"home_lv_item_tv1"}, 
				new int[] {R.id.home_lv_item_tv1});
		//为ListView设置Adapter
		lv.setAdapter(simpleAdapter);
    }
	private void showDialog(final String s, final int position){
		//布局文件转换为view对象
        LayoutInflater inflaterDl = LayoutInflater.from(HomeActivity.this);
        LinearLayout layout = (LinearLayout)inflaterDl.inflate(R.layout.home_list_item_dialog, null );
        //对话框
        final Dialog dialog = new AlertDialog.Builder(HomeActivity.this).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        //翻译按钮
        Button moreTv = (Button)layout.findViewById(R.id.hlid_tranlate);
        moreTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "翻译", Toast.LENGTH_SHORT).show();
                LinearLayout ll = (LinearLayout)lv.getChildAt(position);
                tranlateTv = (TextView)ll.findViewById(R.id.home_lv_item_tv2);
                tranThread();  
                tranlateTv.setVisibility(View.VISIBLE);
   			 	simpleAdapter.notifyDataSetChanged();
  			 	dialog.dismiss();
            }
          });
        //朗读按钮
        Button readbtn = (Button)layout.findViewById(R.id.hlid_reading);
        readbtn.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		/*Toast.makeText(getApplicationContext(), "朗读", Toast.LENGTH_SHORT).show();
        		LinearLayout ll = (LinearLayout)lv.getChildAt(position);
        		tranlateTv = (TextView)ll.findViewById(R.id.home_lv_item_tv2);
        		String s = tranlateTv.getText().toString();*/
        		mSpeechSynthesizer.speak(fromTv);
        		
//        		dialog.dismiss();
        	}
        });
        //复制按钮
        Button copyBtn = (Button) layout.findViewById(R.id.hlid_copy);
        copyBtn.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
             Toast.makeText(getApplicationContext(), "复制", Toast.LENGTH_SHORT).show();
             myClip = ClipData.newPlainText("fromTv", fromTv);
             myClipboard.setPrimaryClip(myClip);
			 dialog.dismiss();
          }
        });
        //删除按钮
        Button delTv = (Button) layout.findViewById(R.id.hlid_del);
        delTv.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
             Toast.makeText(getApplicationContext(), "删除", Toast.LENGTH_SHORT).show();
             db.execSQL("delete from mycollections where content=?", new Object[]{s});
             listItems.remove(position);
			 simpleAdapter.notifyDataSetChanged();
			 dialog.dismiss();
          }
        });
	}
	/*
	 * 百度翻译
	 */
	//申请者开发者id，实际使用时请修改成开发者自己的appid
	private static final String appId = "20160413000018633";
	//申请成功后的证书token，实际使用时请修改成开发者自己的token
	private static final String token = "JHyxWdNLTbU7LztjAMKQ";
	private static final String BaiduTrans = "http://api.fanyi.baidu.com/api/trans/vip/translate";
	//随机数，用于生成md5值，开发者使用时请激活下边第四行代码
	private static final Random random = new Random();
	
	private Handler insHandler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            // TODO Auto-generated method stub  
            switch (msg.what) {  
            case 0:  
                String word = msg.getData().getString("word");  
                tranlateTv.setText(word); 
                break;  
  
            default:  
                break;  
            }  
        }  
    };  
  
    /**  
     * 翻译  
     */  
    private void translate() {  
        // path: http://fanyi.baidu.com/#en/zh/  
        String putword = fromTv;
        
        //用于md5加密
        int salt = random.nextInt(10000);
        
        StringBuilder md5String = new StringBuilder();
        md5String.append(appId).append(putword).append(salt).append(token);
        String md5 = DigestUtils.md5Hex(md5String.toString());
        
        try {  
            // 对中文字符进行编码,否则传递乱码  
            putword = URLEncoder.encode(putword, "utf-8");  
            URL url = new URL(BaiduTrans + "?q=" + putword + "&from=auto&to=auto" +
            "&appid=" + appId + "&salt=" + salt + "&sign=" + md5);

            URLConnection con = url.openConnection();
            con.connect();  
            InputStreamReader reader = new InputStreamReader(  
                    con.getInputStream());  
            BufferedReader bufread = new BufferedReader(reader);  
            StringBuffer buff = new StringBuffer();  
            String line;  
            while ((line = bufread.readLine()) != null) {  
                buff.append(line);  
            }  
            // 对字符进行解码  
            String back = new String(buff.toString().getBytes("ISO-8859-1"),  
                    "UTF-8");  
            String str = JsonToString(back);  
            Message msg = new Message();  
            msg.what = 0;  
            Bundle bun = new Bundle();  
            bun.putString("word", str);  
            msg.setData(bun);  
            insHandler.sendMessage(msg);  
  
            reader.close();  
            bufread.close();  
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }  
  
    /**  
     * 获取jsoon中翻译的内容  
     *   
     * @param jstring  
     * @return  
     */  
    private String JsonToString(String jstring) {  
        try {  
            JSONObject obj = new JSONObject(jstring);  
            JSONArray array = obj.getJSONArray("trans_result");  
            obj = array.getJSONObject(0);  
            String word = obj.getString("dst");  
            return word;  
        } catch (JSONException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return "";  
    }  
  
    /**  
     * 访问网络线程  
     */  
    private void tranThread() {  
        new Thread() {  
            public void run() {  
                translate();  
            };  
        }.start();  
    }
	
	//==========百度翻译end=====
    
    /*
     * 百度语音合成
     */
    public void initBaiduTts(){
    	mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        mSpeechSynthesizer.setContext(HomeActivity.this);
        mSpeechSynthesizer.setSpeechSynthesizerListener(HomeActivity.this);
        // 注：your-apiKey 和 your-secretKey 需要换成在百度开发者中心注册应用得到的对应值
        mSpeechSynthesizer.setApiKey("MlGXNTaenIGXjTHWjYhlFAi7", "31ecc2c325fd5005c65e7beccd467a16");
        // 发音人（在线引擎），可用参数为0,1,2,3。。。（服务器端会动态增加，各值含义参考文档，以文档说明为准。0--普通女声，1--普通男声，2--特别男声，3--情感男声。。。）
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置Mix模式的合成策略
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 初始化tts
        mSpeechSynthesizer.initTts(TtsMode.MIX);
//        mSpeechSynthesizer.speak("百度一下，你就知道");
    }
	@Override
	public void onError(String arg0, SpeechError arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSpeechFinish(String arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSpeechProgressChanged(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSpeechStart(String arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSynthesizeDataArrived(String arg0, byte[] arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSynthesizeFinish(String arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSynthesizeStart(String arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
        c.close();
        db.close();
	}
}
