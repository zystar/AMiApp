/**
 * 
 */
package cn.bmob.imdemo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import cn.bmob.imdemo.R;

/**
 * 我的资料Activity
 * @author 飞雪无情
 * @since 2011-3-8
 */
public class AddActivity extends Activity{

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
        setContentView(R.layout.activity_camera);
    }

}
