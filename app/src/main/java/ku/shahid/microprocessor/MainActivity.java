package ku.shahid.microprocessor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import static java.lang.Math.abs;
import static java.lang.StrictMath.atan;
import static java.lang.StrictMath.sqrt;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private  String fileName;
    private String TAG = "InternalFileWriteReadActivity";
    float ax,ay,az;
    int x,y,z;
    double target = 0;
    double RAD_TO_DEG = 57.295779513082320876798154814105f;
    TextView ACC;
    TextView TERGET;
    int backpress = 0;
    // File variables
    File outFile;
    PrintWriter pw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor ,500000);
        ACC = (TextView) findViewById(R.id.acc);
        TERGET = (TextView) findViewById(R.id.terget);

        fileName = Calendar.getInstance().getTime()+".txt";
        // Writing file
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
        {
            outFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
            try
            {
                pw = new PrintWriter(new FileOutputStream(outFile,true));
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            ax=event.values[0];
            ay=event.values[1];
            az=event.values[2];



            x = (int) (RAD_TO_DEG * atan(ax / sqrt(ay * ay + az * az)));
            y = (int) (RAD_TO_DEG * atan(ay / sqrt(ax * ax + az * az)));
            z = (int) (RAD_TO_DEG * atan(az / sqrt(ay * ay + ax * ax)));

            ACC.setText("X = " +x+ "\nY = " +y+ "\nZ = "+z);

            // Write into file. Filter applied.
            if(x>10 || x<-10 || y>7 || y<-7)
            {
                try {
                    writeFile(pw, x + "", y + "", z + "");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    private void writeFile(PrintWriter pw,String x,String y,String z) throws IOException
    {

        //pw.println("File writing successful");
        pw.println("X = " + x + " Y = " + y + " Z = " + z);

    }
    public void onBackPressed()
    {
        backpress = backpress +1;
        pw.close();
        Toast.makeText(MainActivity.this, "Press again to exit", Toast.LENGTH_SHORT).show();
        if(backpress>1)
        {
            super.onBackPressed();
        }
    }

}
