package io.chelizi.amokhtpp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import io.chelizi.amokhtpp.entity.Blog;
import io.chelizi.amokhttp.download.AMDownload;
import io.chelizi.amokhttp.download.OnDownloadListener;
import io.chelizi.amokhttp.entity.FileCard;
import io.chelizi.amokhttp.entity.HttpError;
import io.chelizi.amokhttp.post.AMPost;
import io.chelizi.amokhttp.post.OnAddListener;
import io.chelizi.amokhttp.query.AMQuery;
import io.chelizi.amokhttp.query.OnFindListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
               // add();

                download();

            }
        });
    }

    private void download() {
        AMDownload<File> download = new AMDownload<>();
        download.setUrl("http://img0.utuku.china.com/550x0/news/20170528/1b3b24eb-44d4-4548-a40a-e6c089f6b4db.jpg")
                .setFileCard(new FileCard("destDir","destName"));

        download.downloadObjects(this, new OnDownloadListener<File>() {

            @Override
            public void onProgressChanged(long progress, long total) {

            }

            @Override
            public void onResponseSuccess(File response) {

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
        post.setUrl("http://192.168.43.36:8090/blog/save")
            .addWhereEqualTo("title","最新报道")
            .addWhereEqualTo("content","tianjin");
        post.addObjects(this, new OnAddListener<String>() {
            @Override
            public void onResponseSuccess(String response) {

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
        query.setUrl("http://192.168.1.10:8090/blog/id?id=1");
        query.findObjects(this, new OnFindListener<Blog>() {
            @Override
            public void onResponseSuccess(Blog response) {
                Toast.makeText(MainActivity.this,response.getTitle(),Toast.LENGTH_LONG).show();
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
