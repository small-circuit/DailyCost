package virgil.dailycost;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class App extends Application{
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    public static Context getInstance() {
        return App.context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        App.context = getApplicationContext();
    }
}
