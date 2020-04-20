package my.test.loadresrouce;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Created by YiVjay
 * on 2020/4/19
 */
public class LoadUtil {
    private static ClassLoader classLoader;
    private static Resources resources;
    private static PackageInfo packageInfo;

    public static String loadFile(Context context, String name) {
        File apkDir = context.getDir("apk", Context.MODE_PRIVATE);
        String apkPath = apkDir.getAbsolutePath() + File.separator + name;
        File apkFile = new File(apkPath);
        if (!apkFile.exists()) {
            try {
                apkFile.createNewFile();
                copyFiles(context, name, apkFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return apkPath;
    }

    public static void copyFiles(Context context, String fileName, File desFile) {
        InputStream in = null;
        OutputStream out = null;

        try {
            in = context.getAssets().open(fileName);
            out = new FileOutputStream(desFile.getAbsolutePath());
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = in.read(bytes)) != -1)
                out.write(bytes, 0, len);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadResource(String loadPath, Context context) {
        Class cls = AssetManager.class;
        try {
            Object assetManager = cls.newInstance();
            Method addAssetPath = cls.getDeclaredMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, loadPath);
            resources = new Resources((AssetManager) assetManager, context.getResources().getDisplayMetrics(),
                    context.getResources().getConfiguration());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Resources getResources() {
        return resources;
    }

    public static ClassLoader getClassLoader() {
        return classLoader;
    }

    public static void init(String loadPath, Context context) {
        File dex = context.getDir("dex", Context.MODE_PRIVATE);
        classLoader = new DexClassLoader(loadPath, dex.getAbsolutePath(),
                null, context.getClassLoader());
        packageInfo = context.getPackageManager().getPackageArchiveInfo(loadPath, PackageManager.GET_ACTIVITIES);
    }

    public static int getResourceId(String type, String name) {
        int id = 0;
        String className = packageInfo.packageName + ".R$" + type;
        try {
            Class<?> aClass = classLoader.loadClass(className);
            Field declaredField = aClass.getDeclaredField(name);
            id= (Integer) declaredField.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
