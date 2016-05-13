/**
 * 
 */
package cn.bmob.imdemo.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.bean.QiangYu;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.util.CacheUtils;
import cn.bmob.imdemo.util.LogUtils;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 */
public class AddActivity extends Activity implements View.OnClickListener {

    private static final int REQUEST_CODE_ALBUM = 1;
    private static final int REQUEST_CODE_CAMERA = 2;
    LinearLayout openLayout;
    LinearLayout takeLayout;
    ImageView albumPic;
    ImageView takePic;
    ImageView add_top_right_img;
    EditText edit_content;
    String dateTime;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
        setContentView(R.layout.activity_add);
        init();
        setListener();
    }
    public void init(){
        openLayout = (LinearLayout) findViewById(R.id.open_layout);
        takeLayout = (LinearLayout) findViewById(R.id.take_layout);
        add_top_right_img = (ImageView) findViewById(R.id.add_top_right_img);
        albumPic = (ImageView) findViewById(R.id.open_pic);
        takePic = (ImageView) findViewById(R.id.take_pic);
        edit_content = (EditText) findViewById(R.id.edit_content);
    }
    public void setListener() {
        openLayout.setOnClickListener(this);
        takeLayout.setOnClickListener(this);
        add_top_right_img.setOnClickListener(this);

        albumPic.setOnClickListener(this);
        takePic.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.open_layout:
                Date date1 = new Date(System.currentTimeMillis());
                dateTime = date1.getTime() + "";
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                        "image/*");
                startActivityForResult(intent, REQUEST_CODE_ALBUM);
                break;
            case R.id.take_layout:
                Date date = new Date(System.currentTimeMillis());
                dateTime = date.getTime() + "";
                File f = new File(CacheUtils.getCacheDirectory(this, true,
                        "pic") + dateTime);
                if (f.exists()) {
                    f.delete();
                }
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri uri = Uri.fromFile(f);
                Log.e("uri", uri + "");

                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(camera, REQUEST_CODE_CAMERA);
                break;
            case R.id.add_top_right_img:
                String s = edit_content.getText().toString();
                publish(s);
            default:
                break;
        }
    }

    /*
	 * 发表带图片
	 */
    private void publish(final String commitContent) {

        // final BmobFile figureFile = new BmobFile(QiangYu.class, new
        // File(targeturl));

        final BmobFile figureFile = new BmobFile(new File(targeturl));

        figureFile.upload(this, new UploadFileListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                LogUtils.i("TAG",
                        "上传文件成功。" + figureFile.getFileUrl(AddActivity.this));
                publishWithoutFigure(commitContent, figureFile);

            }

            @Override
            public void onProgress(Integer arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFailure(int arg0, String arg1) {
                // TODO Auto-generated method stub
                LogUtils.i("TAG", "上传文件失败。" + arg1);
            }
        });

    }

    private void publishWithoutFigure(final String commitContent,
                                      final BmobFile figureFile) {
        User user = BmobUser.getCurrentUser(this, User.class);

        final QiangYu qiangYu = new QiangYu();
        qiangYu.setAuthor(user);
        qiangYu.setContent(commitContent);
        if (figureFile != null) {
            qiangYu.setContentfigureurl(figureFile);
        }
        qiangYu.setLove(0);
        qiangYu.setHate(0);
        qiangYu.setShare(0);
        qiangYu.setComment(0);
        qiangYu.setPass(true);
        qiangYu.save(this, new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
//                ActivityUtil.show(EditActivity.this, "发表成功！");
                Toast.makeText(AddActivity.this, "发表成功！", Toast.LENGTH_SHORT).show();
                LogUtils.i("TAG", "创建成功。");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                // TODO Auto-generated method stub
                Toast.makeText(AddActivity.this, "发表失败！", Toast.LENGTH_SHORT).show();
                LogUtils.i("TAG", "创建失败。" + arg1);
            }
        });
    }

    String targeturl = null;

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.i("TAG", "get album:");
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ALBUM:
                    String fileName = null;
                    if (data != null) {
                        Uri originalUri = data.getData();
                        ContentResolver cr = getContentResolver();
                        Cursor cursor = cr.query(originalUri, null, null, null,
                                null);
                        if (cursor.moveToFirst()) {
                            do {
                                fileName = cursor.getString(cursor
                                        .getColumnIndex("_data"));
                                LogUtils.i("TAG", "get album:" + fileName);
                            } while (cursor.moveToNext());
                        }
                        Bitmap bitmap = compressImageFromFile(fileName);
                        targeturl = saveToSdCard(bitmap);
                        albumPic.setBackgroundDrawable(new BitmapDrawable(bitmap));
                        takeLayout.setVisibility(View.GONE);
                    }
                    break;
                case REQUEST_CODE_CAMERA:
                    String files = CacheUtils.getCacheDirectory(this, true,
                            "pic") + dateTime;
                    File file = new File(files);
                    if (file.exists()) {
                        Bitmap bitmap = compressImageFromFile(files);
                        targeturl = saveToSdCard(bitmap);
                        takePic.setBackgroundDrawable(new BitmapDrawable(bitmap));
                        openLayout.setVisibility(View.GONE);
                    } else {

                    }
                    break;
                default:
                    break;
            }
        }
    }
    private Bitmap compressImageFromFile(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;// 只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800f;//
        float ww = 480f;//
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置采样率

        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;// 该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        // return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
        // 其实是无效的,大家尽管尝试
        return bitmap;
    }

    public String saveToSdCard(Bitmap bitmap) {
        String files = CacheUtils.getCacheDirectory(this, true, "pic")
                + dateTime + "_11.jpg";
        File file = new File(files);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        LogUtils.i("TAG", file.getAbsolutePath());
        return file.getAbsolutePath();
    }
}
