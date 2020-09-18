package com.example.unzipping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.unzipping.ApiActivity;
import com.example.unzipping.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Button btn;
    EditText woid,roid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        woid=findViewById(R.id.edt_work);
        roid=findViewById(R.id.edt_route);
        if( (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},101);
        }
            btn=findViewById(R.id.btn_zip);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    unzip(Environment.getExternalStorageDirectory()+File.separator+"ApkZip.zip",Environment.getExternalStorageDirectory()+File.separator);
                    /*int rid=Integer.parseInt(roid.getText().toString());
                    int wid=Integer.parseInt(woid.getText().toString());
                    new NetworkAsync(rid,wid).execute();*/

                    /*btn.setText("Unzipping");
                    File file=new File(Environment.getExternalStorageDirectory()+File.separator+"TestZip");
                    new AsyncZip(Environment.getExternalStorageDirectory()+File.separator+"Screenshots.zip",file.getPath()).execute();*/
                    // unzip(Environment.getExternalStorageDirectory()+File.separator+"LogZip.zip",file.getPath());
                   /* try {
                        unzip(Environment.getExternalStorageDirectory()+File.separator+"LogZip.zip",Environment.getExternalStorageDirectory()+File.separator+"TestZip");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                }
            });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==101){
            if (grantResults.length>0){
                Toast.makeText(this,"Permission granted",Toast.LENGTH_SHORT).show();
                findViewById(R.id.btn_zip).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       File file=new File(Environment.getExternalStorageDirectory()+File.separator+"TestZip");

                       /* int rid=Integer.parseInt(roid.getText().toString());
                        int wid=Integer.parseInt(woid.getText().toString());
                        new NetworkAsync(rid,wid).execute();*/

                    }
                });
            }
        }
    }
    public static void unzip(String zipFile, String location){
        try {
            File f = new File(location);
            if (!f.isDirectory()) {
                f.mkdirs();
            }
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
            BufferedInputStream bin=new BufferedInputStream(zin);
            try {
                ZipEntry ze = null;
                while ((ze = zin.getNextEntry()) != null) {
                    String path = location + File.separator + ze.getName();
                    System.out.println("Name of files inside zip:"+ze.getName());
                    File unzipFile = new File(path);
                    if (ze.isDirectory()) {
                        if (!unzipFile.isDirectory()) {
                            unzipFile.mkdirs();
                        }
                    } else {
                        FileOutputStream fout = new FileOutputStream(path, false);
                        BufferedOutputStream bout=new BufferedOutputStream(fout);
                        try {
                            for (int c = bin.read(); c != -1; c = bin.read()) {
                                bout.write(c);
                            }
                            zin.closeEntry();
                        } finally {
                            bout.close();
                        }
                    }
                }
            } finally {
                zin.close();
                System.out.println("Closing");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Unzip exception", e);
        }

    }
    /*void unzip(String fileZip,String Dir) throws IOException {
        File destDir = new File(Dir);
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
        btn.setText("Unzipping Done");
    }
    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }*/

    public class AsyncZip extends AsyncTask{
        String zipFile,location;
        public AsyncZip(String zipFile,String location){
            this.location=location;this.zipFile=zipFile;
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                File f = new File(location);
                if (!f.isDirectory()) {
                    f.mkdirs();
                }
                ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
                BufferedInputStream bin=new BufferedInputStream(zin);
                try {
                    ZipEntry ze = null;
                    while ((ze = zin.getNextEntry()) != null) {
                        String path = location + File.separator + ze.getName();
                        System.out.println("Name of files inside zip:"+ze.getName());
                        File unzipFile = new File(path);
                        if (ze.isDirectory()) {
                            if (!unzipFile.isDirectory()) {
                                unzipFile.mkdirs();
                            }
                        } else {
                            FileOutputStream fout = new FileOutputStream(path, false);
                            BufferedOutputStream bout=new BufferedOutputStream(fout);
                            try {
                                for (int c = bin.read(); c != -1; c = bin.read()) {
                                    bout.write(c);
                                }
                                zin.closeEntry();
                            } finally {
                                bout.close();
                            }
                        }
                    }
                } finally {
                    zin.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Unzip exception", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            btn.setText("Unzipping Done");
        }
    }
    public class AsyncZip2 extends AsyncTask{
        String zipFile,location;
        public AsyncZip2(String zipFile,String location){
            this.location=location;this.zipFile=zipFile;
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                File f = new File(location);
                if (!f.isDirectory()) {
                    f.mkdirs();
                }
                ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
                BufferedInputStream bin=new BufferedInputStream(zin);
                try {
                    ZipEntry ze = null;
                    while ((ze = zin.getNextEntry()) != null) {
                        String path = location + File.separator + ze.getName();
                        System.out.println("Name of files inside zip:"+ze.getName());
                        File unzipFile = new File(path);
                        if (ze.isDirectory()) {
                            if (!unzipFile.isDirectory()) {
                                unzipFile.mkdirs();
                            }
                        } else {
                            FileOutputStream fout = new FileOutputStream(path, false);
                            BufferedOutputStream bout=new BufferedOutputStream(fout);
                            try {
                                for (int c = bin.read(); c != -1; c = bin.read()) {
                                    bout.write(c);
                                }
                                zin.closeEntry();
                            } finally {
                                bout.close();
                            }
                        }
                    }
                } finally {
                    zin.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Unzip exception", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            btn.setText("Unzipping Done");
        }
    }
      public class NetworkAsync extends AsyncTask{
            int rid,wid;
        public NetworkAsync(int rid, int wid) {
            this.rid=rid;this.wid=wid;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            ApiActivity apiActivity=new ApiActivity(getApplicationContext());
            apiActivity.getRoutesDataCompressedJSON(rid,wid);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Toast.makeText(getApplicationContext(),"Decompression is done",Toast.LENGTH_LONG).show();
        }
    }

}
