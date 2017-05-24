package com.lyl.outsourcing.activity.test.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:just_szl@hotmail.com"> Geray</a>
 * @version 1.0,2012-6-12
 */
public class MaterialDataBuilder {

    private String url;

    public MaterialDataBuilder(String url) {
        this.url = url;
    }

    //file1与file2在同一个文件夹下 filepath是该文件夹指定的路径
    public void SubmitPost(String filename, Map<String, String> params){

        HttpClient httpclient = new DefaultHttpClient();

        try {

            HttpPost httppost = new HttpPost(url);

            FileBody bin = new FileBody(new File(filename));

            MultipartEntity reqEntity = new MultipartEntity();

            reqEntity.addPart("file", bin);//file1为请求后台的File upload;属性

            for(String key : params.keySet()) {
                reqEntity.addPart(key, new StringBody(params.get(key)));
            }

            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();

            if(statusCode == HttpStatus.SC_OK){
                System.out.println("服务器正常响应.....");
                HttpEntity resEntity = response.getEntity();
                System.out.println(EntityUtils.toString(resEntity));//httpclient自带的工具类读取返回数据
                System.out.println(resEntity.getContent());
                EntityUtils.consume(resEntity);
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();
            } catch (Exception ignore) {

            }
        }
    }


    @Test
    public void build() {
        final MaterialDataBuilder materialDataBuilder = new MaterialDataBuilder("http://39.108.6.235/activity/material");
        Path path = Paths.get("/Users/liyilin/Downloads/tmp/图片");
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
            directoryStream.forEach(new Consumer<Path>() {
                @Override
                public void accept(Path path) {
                    String filepath = path.toString();
                    Map<String, String> params = new HashMap<>(2);
                    params.put("name", UUID.randomUUID().toString().replace("-", "").substring(0, 30));
                    params.put("type", "image");

                    System.out.println("filepath:" + filepath + ", params: " + params);
                    materialDataBuilder.SubmitPost(filepath, params);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
