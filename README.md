### 1.AMDownload 下载
   构造一个AMDownload对象，设置FildCard.支持下载进度条。
```java
AMDownload<File> download = new AMDownload<>();
        download.setUrl("http://img0.utuku.china.com/550x0/news/20170528/1b3b24eb-44d4-4548-a40a-e6c089f6b4db.jpg")
                .setFileCard(new FileCard(
                        getCacheDir().getAbsolutePath(),
                        System.currentTimeMillis() + ".jpg"));

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
```


### 2. AMPost 提交
```java
 AMPost<String> post = new AMPost<>();
        post.setUrl("http://192.168.43.36:8090/blog/save")
            .addWhereEqualTo("title","最新报道")
            .addWhereEqualTo("content","tianjin")
            .setCacheControl(CacheControl.FORCE_NETWORK)
            .setTag(hashCode());

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

```



### 3.AMQuery 查询
封装了一个AMQuery对象，调用者不需要关心reqeust和response,从构造的对象来看就知道对应的是get请求。
```java
  AMQuery<Blog> query = new AMQuery<>();
        query.setUrl("http://xx.xxx.xx:8090/blog/id?id=1");
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
  
