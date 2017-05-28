
#### 1.get 查询
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
  
