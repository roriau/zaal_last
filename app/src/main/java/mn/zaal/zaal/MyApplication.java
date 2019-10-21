package mn.zaal.zaal;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import timber.log.Timber;

public class MyApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static final String TAG = MyApplication.class.getSimpleName();
    private RequestQueue mRequestQueue;

    @SuppressLint("StaticFieldLeak")
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        MyApplication.context = getApplicationContext();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
    public static Context getAppContext() {
        return MyApplication.context;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public static boolean isOnline() {
        ConnectivityManager connectionManager = (ConnectivityManager) MyApplication.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectionManager == null) {
            return false;
        }
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return false;
        }
        return networkInfo.isConnected();
    }

}
