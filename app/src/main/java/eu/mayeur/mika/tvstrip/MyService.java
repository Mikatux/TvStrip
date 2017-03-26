package eu.mayeur.mika.tvstrip;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.audiofx.Visualizer;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyService extends Service{
    public TcpClient mTcpClient;
    public MyService() {
        Log.i("service", "main");

    }
    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        Log.i("service", "onCreate");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        new soundListener().execute("");

        new ConnectTask().execute("");

    }
        @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.i("service", "onCretfyguhijokate");

        throw new UnsupportedOperationException("Not yet implemented");
    }


}
