package eu.mayeur.mika.tvstrip;

import android.media.audiofx.Visualizer;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.lang.reflect.Array;

/**
 * Created by Mika on 3/26/17.
 */

public class soundListener extends AsyncTask<String, String, TcpClient> {
    double max = 0;


    public void sendData(byte[] fft){
        //   Log.i("test", "sending rate ");
        //\Log.i("sda","sending");

        double magnitude = 0;
        int[] magnitudePoints= new int[fft.length/2];
        for (int i = 0; i < fft.length/2; i++) {
            byte rfk = fft[2 * i];
            byte ifk = fft[2 * i + 1];
            magnitude = rfk * rfk + ifk * ifk;

            int dbValue = (int) (10 * Math.log10(magnitude));
            magnitude = Math.round(dbValue * 8);

            try { magnitudePoints[i] = (int) magnitude; }
            catch (Exception e) { e.printStackTrace(); }

            if( Math.max(magnitude,max) != max){
                Log.i("test", "max : "+ max);

            }
            max = Math.max(magnitude,max);

        }

        // JSONObject jsonobj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        // for(int i=0;i<10;i++) {
        //  Log.i("test", "length : "+ fft.length);
        int nbitem = 1;
        for (int i = 1; i < 20; i+=nbitem) {
            jsonArray.put(magnitudePoints[i]);
/*
            double val = 0;
            for (int j = 0; j < nbitem; j++) {

                    val +=  magnitudePoints[i+j];

            }

          //  Log.i("test", "val : "+ val);

            val*=100;

            val /=nbitem;


            val/=250;


            if(val < 0 )
                val = 0;

            if(val >100 )
                val = 100;
            Log.i("test", "percent : "+ val);
            int red   = (int)(val > 50 ? 2*(val-50)*2.55 : 0);
            int green = (int)(val > 50 ?  255-val*2 : 2*val*2);
           // Log.i("test", "red : "+ red);
           // Log.i("test", "green : "+ green);

            int c = (red << 16) + (0 << 8) + green;
            //  Log.i("test", "val : "+ c);

            jsonArray.put(c);
*/
        }
        TcpClient mTcpClient = ConnectTask.mTcpClient;

        if (mTcpClient != null) {
            // Log.i("sda","sending2");
            mTcpClient.sendMessage(jsonArray.toString());
        }


    }
    @Override
    protected TcpClient doInBackground(String... message) {

        //we create a TCPClient object


        try {
            Log.i("test", "log : -1");

            Visualizer visual = new Visualizer(0);
            Log.i("test", "log : 0");

            visual.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
            Log.i("test", "log : 1");

            visual.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
                @Override
                public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
                    int max=0, min=255;
                    for(int i=0;i<waveform.length;i++) {
                        int w=(int)waveform[i] & 0xFF;
                        max = Math.max(w, max);
                        min = Math.min(w, min);
                    }

                    //Log.i("test", "wform "+max+" / "+min);
                }

                @Override
                public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {

                    int max=0;
                    // Log.i("test", "fft rate "+samplingRate);

                    for(int i=0;i<fft.length;i++) {
                        max = Math.max((int) fft[i] & 0xFF, max);
                        // Log.i("test", "fft val "+fft[i]);

                    }
                    sendData(fft);

                    //  Log.i("test", "fft max "+max);
                     //  Log.i("test", "fft length "+fft.length);
                }
            }, Visualizer.getMaxCaptureRate(), true, true);
            Log.i("test", "log : 2");

            visual.setEnabled(true);
        }
        catch (Exception ex) {
            Log.e("Visual Ex", ex.getMessage());
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        //response received from server
        Log.d("test", "response " + values[0]);
        //process server response here....

    }
}
