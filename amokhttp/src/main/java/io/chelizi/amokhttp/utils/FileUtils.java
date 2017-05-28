package io.chelizi.amokhttp.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.chelizi.amokhttp.download.OnSaveListener;
import io.chelizi.amokhttp.entity.FileCard;

/**
 * Created by shanqiu on 2016/5/6.
 */
public class FileUtils {

    public static File saveFile(InputStream is, final long contentLength,
                                FileCard fileCard, OnSaveListener onSaveListener) throws IOException {
        byte[] buf = new byte[1024];
        int len = 0;
        File dir = new File(fileCard.getDestDir());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, fileCard.getDestName());
        FileOutputStream fos = new FileOutputStream(file);
        try {
            long sum = 0;
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                fos.flush();

                if (onSaveListener != null) {
                    sum += len;
                    final long bytesRead = sum;
                    onSaveListener.OnProgress(bytesRead,contentLength);
                }
            }

            fos.flush();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
            }

        }
        return file;
    }
}
