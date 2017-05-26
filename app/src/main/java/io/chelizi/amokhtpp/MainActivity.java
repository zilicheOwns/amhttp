package io.chelizi.amokhtpp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import io.chelizi.amokhtpp.entity.Blog;
import io.chelizi.amokhttp.entity.HttpError;
import io.chelizi.amokhttp.post.AMPost;
import io.chelizi.amokhttp.query.AMQuery;
import io.chelizi.amokhttp.post.OnAddListener;
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
                add();
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
                if (response != null){
                    Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onResponseError(int code, @Nullable HttpError httpError) {
                if (httpError != null){
                    Toast.makeText(MainActivity.this,"title = "+httpError.getError_message(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(MainActivity.this,"network error",Toast.LENGTH_LONG).show();
            }
        });
    }


    private void load() {
        AMQuery<Blog> query = new AMQuery<>();
        query.setUrl("http://192.168.1.7:8090/blog/id?id=1");
        query.findObjects(this, new OnFindListener<Blog>() {
            @Override
            public void onResponseSuccess(Blog response) {
                if (response != null){
                    Toast.makeText(MainActivity.this,"title = "+response.getTitle(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onResponseError(int code, @Nullable HttpError httpError) {
                if (httpError != null){
                    Toast.makeText(MainActivity.this,"title = "+httpError.getError_message(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(MainActivity.this,"network error",Toast.LENGTH_LONG).show();
            }
        });
    }
}
