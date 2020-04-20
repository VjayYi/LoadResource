package my.test.loadresrouce;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class MainActivity extends Activity {

    private Resources resources;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String loadPath = LoadUtil.loadFile(this, "app-release.apk");
        LoadUtil.loadResource(loadPath,this);
        LoadUtil.init(loadPath,this);
        img = findViewById(R.id.img);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id=LoadUtil.getResourceId("mipmap","pyy2");
                img.setImageDrawable(LoadUtil.getResources().getDrawable(id));
            }
        });
    }



}
