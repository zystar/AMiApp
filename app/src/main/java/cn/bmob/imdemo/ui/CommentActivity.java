package cn.bmob.imdemo.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.imdemo.MyApplication;
import cn.bmob.imdemo.R;
import cn.bmob.imdemo.adapter.CommentAdapter;
import cn.bmob.imdemo.bean.Comment;
import cn.bmob.imdemo.bean.QiangYu;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.util.ActivityUtil;
import cn.bmob.imdemo.util.LogUtils;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by STAR on 2016/5/20.
 */
public class CommentActivity extends Activity implements View.OnClickListener {
    private QiangYu qiangYu;
    private TextView textView1;
    private TextView content;
    private ImageView contentImg;

    private ListView commentList;
    private CommentAdapter mAdapter;
    private List<Comment> comments = new ArrayList<Comment>();
    private TextView love;
    private ImageView myFav;
    private int pageNum;
    private TextView footer;
    private String commentEdit = "";
    private EditText commentContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        qiangYu = (QiangYu)getIntent().getSerializableExtra("data");
        init();
        /*setupViews();
        setListener();*/
    }
    public void init(){
        textView1 = (TextView)findViewById(R.id.comment_item).findViewById(R.id.textView1);
        content = (TextView)findViewById(R.id.comment_item).findViewById(R.id.desc);
        contentImg = (ImageView) findViewById(R.id.comment_item).findViewById(R.id.imageView2);
        commentList = (ListView) findViewById(R.id.comment_list);
        love = (TextView) findViewById(R.id.item_action_love);
        myFav = (ImageView) findViewById(R.id.imageView6);
        commentContent = (EditText) findViewById(R.id.comment_content);

        textView1.setText(qiangYu.getAuthor().getUsername());
        content.setText(qiangYu.getContent());
//        contentImg.setBackgroundDrawable(qiangYu.getContentfigureurl());
        BmobFile bmobFile = qiangYu.getContentfigureurl();
        if (null != bmobFile) {
            ImageLoader.getInstance().displayImage(
                    bmobFile.getFileUrl(CommentActivity.this),
                    contentImg,
                    new SimpleImageLoadingListener() {

                        @Override
                        public void onLoadingComplete(String imageUri,
                                                      View view, Bitmap loadedImage) {
                            // TODO Auto-generated method stub
                            super.onLoadingComplete(imageUri, view, loadedImage);
                            LogUtils.i("CommentAcitvity", "load comment img completed.");
                        }

                    });
        }
    }

    protected void setupViews() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        qiangYu = (QiangYu) getIntent().getSerializableExtra("data");// MyApplication.getInstance().getCurrentQiangYu();
        pageNum = 0;

        mAdapter = new CommentAdapter(CommentActivity.this, comments);
        commentList.setAdapter(mAdapter);
        setListViewHeightBasedOnChildren(commentList);
        commentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(CommentActivity.this, "po" + position, Toast.LENGTH_SHORT).show();
            }
        });
        commentList.setCacheColorHint(0);
        commentList.setScrollingCacheEnabled(false);
        commentList.setScrollContainer(false);
        commentList.setFastScrollEnabled(true);
        commentList.setSmoothScrollbarEnabled(true);

        initMoodView(qiangYu);
    }
    private void initMoodView(QiangYu mood2) {
        // TODO Auto-generated method stub
        if (mood2 == null) {
            return;
        }
        textView1.setText(qiangYu.getAuthor().getUsername());
        content.setText(qiangYu.getContent());
        if (null == qiangYu.getContentfigureurl()) {
            contentImg.setVisibility(View.GONE);
        } else {
            contentImg.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(
                    qiangYu.getContentfigureurl().getFileUrl(
                            CommentActivity.this) == null ? "" : qiangYu
                            .getContentfigureurl().getFileUrl(
                                    CommentActivity.this),
                    contentImg,
                    MyApplication.getInstance().getOptions(
                            R.drawable.bg_pic_loading),
                    new SimpleImageLoadingListener() {

                        @Override
                        public void onLoadingComplete(String imageUri,
                                                      View view, Bitmap loadedImage) {
                            // TODO Auto-generated method stub
                            super.onLoadingComplete(imageUri, view, loadedImage);
                            float[] cons = ActivityUtil.getBitmapConfiguration(
                                    loadedImage, contentImg, 1.0f);
                            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                                    (int) cons[0], (int) cons[1]);
                            layoutParams.addRule(RelativeLayout.BELOW,
                                    R.id.desc);
                            contentImg.setLayoutParams(layoutParams);
                        }

                    });
        }

        love.setText(qiangYu.getLove() + "");
        if (qiangYu.getMyLove()) {
            love.setTextColor(Color.parseColor("#D95555"));
        } else {
            love.setTextColor(Color.parseColor("#000000"));
        }
//        hate.setText(qiangYu.getHate() + "");
        if (qiangYu.getMyFav()) {
            myFav.setImageResource(R.drawable.collect2);
        } else {
            myFav.setImageResource(R.drawable.collect);
        }

        User user = qiangYu.getAuthor();
        //应该是头像相关的代码
        /*BmobFile avatar = user.getAvatar();
        if (null != avatar) {
            ImageLoader.getInstance().displayImage(
                    avatar.getFileUrl(CommentActivity.this),
                    userLogo,
                    MyApplication.getInstance().getOptions(
                            R.drawable.content_image_default),
                    new SimpleImageLoadingListener() {

                        @Override
                        public void onLoadingComplete(String imageUri,
                                                      View view, Bitmap loadedImage) {
                            // TODO Auto-generated method stub
                            super.onLoadingComplete(imageUri, view, loadedImage);
                            LogUtils.i("CommentActivity", "load personal icon completed.");
                        }

                    });
        }*/
    }
    /***
     * 动态设置listview的高度 item 总布局必须是linearLayout
     *
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1))
                + 15;
        listView.setLayoutParams(params);
    }
    private void fetchComment() {
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.addWhereRelatedTo("relation", new BmobPointer(qiangYu));
        query.include("user");
        query.order("createdAt");
        query.setLimit(15);
        query.setSkip(15 * (pageNum++));
        query.findObjects(this, new FindListener<Comment>() {

            @Override
            public void onSuccess(List<Comment> data) {
                // TODO Auto-generated method stub
                LogUtils.i("CommentActivity", "get comment success!" + data.size());
                if (data.size() != 0 && data.get(data.size() - 1) != null) {

                    if (data.size() < 15) {
                        ActivityUtil.show(CommentActivity.this, "已加载完所有评论~");
                        footer.setText("暂无更多评论~");
                    }

                    mAdapter.getDataList().addAll(data);
                    mAdapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(commentList);
                    LogUtils.i("CommentActivity:", "refresh");
                } else {
                    ActivityUtil.show(CommentActivity.this, "暂无更多评论~");
                    footer.setText("暂无更多评论~");
                    pageNum--;
                }
            }

            @Override
            public void onError(int arg0, String arg1) {
                // TODO Auto-generated method stub
                ActivityUtil.show(CommentActivity.this, "获取评论失败。请检查网络~");
                pageNum--;
            }
        });
    }
    protected void setListener() {
        myFav.setOnClickListener(this);
        love.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loadmore:
                onClickLoadMore();
                break;
            case R.id.comment_commit:
                onClickCommit();
                break;
            /*case R.id.imageView6:
                onClickFav(v);
                break;
            case R.id.item_action_love:
                onClickLove();
                break;
            case R.id.item_action_hate:
                onClickHate();
                break;
            case R.id.item_action_share:
                onClickShare();
                break;
            case R.id.item_action_comment:
                onClickComment();
                break;*/
            default:
            break;
        }
    }
    private void onClickLoadMore() {
        fetchComment();
    }
    private void onClickCommit() {
        // TODO Auto-generated method stub
        User currentUser = BmobUser.getCurrentUser(this, User.class);
        if (currentUser != null) {// 已登录
            commentEdit = commentContent.getText().toString().trim();
            if (TextUtils.isEmpty(commentEdit)) {
                ActivityUtil.show(this, "评论内容不能为空。");
                return;
            }
            // comment now
//            publishComment(currentUser, commentEdit);
        } else {// 未登录
            ActivityUtil.show(this, "发表评论前请先登录。");
            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            startActivityForResult(intent, 1);
        }

    }
}
