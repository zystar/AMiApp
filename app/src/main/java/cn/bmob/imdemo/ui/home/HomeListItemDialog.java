package cn.bmob.imdemo.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class HomeListItemDialog extends Dialog implements OnClickListener{
    private Context context;  
	private SQLiteDatabase db;

    public HomeListItemDialog(Context context) {  
            super(context);  
            this.context = context;  
    }  

    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
            // TODO Auto-generated method stub  
            super.onCreate(savedInstanceState);  
//            setContentView(R.layout.home_list_item_dialog);  
    }  

    @Override  
    public void onClick(View v) {  
            /*switch (v.getId()) {  
            case R.id.hlid_tv1:  
//            	db.openDatabase("inpson.db", null, Context.MODE_PRIVATE);
            	Toast.makeText(getContext(), "删除", Toast.LENGTH_SHORT).show();
                 break;  
            case R.id.hlid_tv2:  
                 break;  
            default:  
                    break;  
            } */ 
    }  
}
