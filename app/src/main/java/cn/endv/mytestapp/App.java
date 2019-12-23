package cn.endv.mytestapp;


import android.app.Application;
import android.content.Context;

public class App extends Application {
    private static Context context;
    public void onCreate() {
        super.onCreate();
        if (context == null) {
            context = getApplicationContext();
        }
    }
}
