package cn.bmob.imdemo.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.bean.QiangYu;
import cn.bmob.imdemo.loader.AsyncImageLoader;

/**
 * Created by STAR on 2016/5/19.
 */
public class QiangYuListAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private ListView listView;
    private AsyncImageLoader asyncImageLoader;

    private List<QiangYu> dataArray=new ArrayList<QiangYu>();

    public QiangYuListAdapter(Activity activity, List<QiangYu> imageAndTexts, ListView listView) {

        this.listView = listView;
        asyncImageLoader = new AsyncImageLoader();
        inflater = activity.getLayoutInflater();
        dataArray=imageAndTexts;
    }
    @Override
    public int getCount() {
        return dataArray.size();
    }

    @Override
    public Object getItem(int position) {
        if(position >= getCount()){
            return null;
        }
        return dataArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_list_view, null);
        }
        convertView.setTag(position);

        QiangYu qiangYu = (QiangYu) getItem(position);
        String imageUrl = qiangYu.getContentfigureurl().getUrl();

        TextView textView1 =  (TextView) convertView.findViewById(R.id.textView1);
        TextView desc =  (TextView) convertView.findViewById(R.id.desc);
        // 将XML视图项与用户输入的URL和文本在绑定
        textView1.setText(qiangYu.getAuthor().getUsername());//加载TEXT
        desc.setText(qiangYu.getContent());//加载TEXT
        ImageView imageView2 = (ImageView) convertView.findViewById(R.id.imageView2);

        asyncImageLoader.loadDrawable(position,imageUrl, new AsyncImageLoader.ImageCallback() {
            @Override
            public void onImageLoad(Integer pos, Drawable drawable) {
                View view = listView.findViewWithTag(pos);
                if(view != null){
                    ImageView iv = (ImageView) view.findViewById(R.id.imageView2);
                    iv.setBackgroundDrawable(drawable);
                }
            }
            //加载不成功的图片处理
            @Override
            public void onError(Integer pos) {
                View view = listView.findViewWithTag(pos);
                if(view != null){
                    ImageView iv = (ImageView) view.findViewById(R.id.imageView2);
                    iv.setBackgroundResource(R.drawable.img_error);
                }
            }

        });
        return convertView;

    }
}
