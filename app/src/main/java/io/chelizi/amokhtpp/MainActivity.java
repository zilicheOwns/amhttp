package io.chelizi.amokhtpp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

import io.chelizi.amokhtpp.entity.Blog;
import io.chelizi.amokhtpp.entity.Size;
import io.chelizi.amokhttp.download.AMDownload;
import io.chelizi.amokhttp.download.OnDownloadListener;
import io.chelizi.amokhttp.entity.FileCard;
import io.chelizi.amokhttp.entity.HttpError;
import io.chelizi.amokhttp.post.AMPost;
import io.chelizi.amokhttp.post.OnAddListener;
import io.chelizi.amokhttp.query.AMQuery;
import io.chelizi.amokhttp.query.OnFindListener;
import io.chelizi.amokhttp.upload.AMUpload;
import io.chelizi.amokhttp.upload.OnUploadListener;
import okhttp3.CacheControl;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image);
        findViewById(R.id.query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
            }
        });

        findViewById(R.id.post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

        findViewById(R.id.download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
            }
        });

        findViewById(R.id.upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
    }

    private void upload() {
        AMUpload<Size> upload = new AMUpload<>();
        upload.setUrl("http://192.168.1.9:8090/blog/upload")
                .setFile(new File(getFilesDir().getAbsolutePath() + "/image.jpg"))
                .setFileName("image.jpg")
                .addWhereEqualTo("image","mark");

        upload.uploadObjects(this, new OnUploadListener<Size>() {
            @Override
            public void onRequestProgress(long progress, long total, boolean done) {
                Log.d("upload_success","progress= " + progress + ",total = " + total);
            }

            @Override
            public void onResponseSuccess(Size response) {
                Toast.makeText(MainActivity.this,response.getDesc(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponseError(int code, HttpError httpError) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    private void download() {
        AMDownload<File> download = new AMDownload<>();
        download.setUrl("http://img0.utuku.china.com/550x0/news/20170528/1b3b24eb-44d4-4548-a40a-e6c089f6b4db.jpg")
                .setFileCard(new FileCard(
                        getFilesDir().getAbsolutePath(),
                        "image.jpg"));

        download.downloadObjects(this, new OnDownloadListener<File>() {

            @Override
            public void onProgressChanged(long progress, long total) {
                Log.d(MainActivity.class.getSimpleName(),"progress = " + progress + ",total = " + total );
            }

            @Override
            public void onResponseSuccess(File response) {
                Glide.with(MainActivity.this).load(response.getAbsolutePath()).into(
                        imageView);
            }

            @Override
            public void onResponseError(int code, @Nullable HttpError httpError) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    private void add() {
        AMPost<String> post = new AMPost<>();
        post.setUrl("http://192.168.1.9:8090/blog/save")
            .addWhereEqualTo("title","最新报道")
            .addWhereEqualTo("content","tianjin")
            .setCacheControl(CacheControl.FORCE_NETWORK)
            .setTag(hashCode());

        post.addObjects(this, new OnAddListener<String>() {
            @Override
            public void onResponseSuccess(String response) {
                if (Thread.currentThread().getName().equals("main")){
                    Toast.makeText(MainActivity.this,",并且在主线程中",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onResponseError(int code, @Nullable HttpError httpError) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }


    private void load() {
        AMQuery<Blog> query = new AMQuery<>();
        query.setUrl("http://192.168.1.9:8090/blog/id?id=1")
             .setCacheControl(CacheControl.FORCE_NETWORK)
             .setTag(hashCode());
        query.findObjects(this, new OnFindListener<Blog>() {
            @Override
            public void onResponseSuccess(Blog response) {
                if (Thread.currentThread().getName().equals("main")){
                    Toast.makeText(MainActivity.this,response.getTitle() + ",并且在主线程中",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onResponseError(int code, @Nullable HttpError httpError) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }
}
