package com.exercise.AndroidCompas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AndroidCompass extends Activity {
    static float SCALE = 0;
    private static SensorManager mySensorManager;
    private boolean sersorrunning;
    private MyCompassView myCompassView;
    private TextView datos;
    Button btnShowLocation;
    static double Lat1, Lon1;
    static double latsave1 = 0, lonsave1 = 0, latsave2, lonsave2;
    static float[][] geopos = new float[1][1];
    static boolean firstpos = true;
    static int contador = 0;
    static float Result;
    GPSTracker gps;// = new GPSTracker(AndroidCompass.this);

    //variables de los EditText y del boton Buscar
    double longitudUsr, latitudUsr;
    EditText longitudInput, latitudInput;
    Button buscarBtn;

    /**
     * Called when the activity is first created.
     */
    @Override


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        gps = new GPSTracker(getApplicationContext());
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        float ancho = display.getWidth();
        float alto = display.getHeight();
        getScale(ancho, alto);
        myCompassView = (MyCompassView) findViewById(R.id.mycompassview);
        datos = (TextView) findViewById(R.id.data);


        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> mySensors = mySensorManager
                .getSensorList(Sensor.TYPE_ORIENTATION);

        if (mySensors.size() > 0) {
            mySensorManager.registerListener(mySensorEventListener,
                    mySensors.get(0), SensorManager.SENSOR_DELAY_FASTEST);
            sersorrunning = true;
            Toast.makeText(this, "Start ORIENTATION Sensor", Toast.LENGTH_LONG)
                    .show();

        } else {
            Toast.makeText(this, "No ORIENTATION Sensor", Toast.LENGTH_LONG)
                    .show();
            sersorrunning = false;
            finish();
        }


        /////// GPS Things

        btnShowLocation = (Button) findViewById(R.id.btnShow);
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (gps.canGetLocation()) {
                    if (firstpos) {
                        latsave1 = gps.getLatitude();
                        lonsave1 = gps.getLongitude();

                    } else {
                        latsave2 = gps.getLatitude();
                        lonsave2 = gps.getLongitude();

                    }
                    firstpos = false;


                    //Result = 6378.137 * Math.acos( Math.cos( Lat1 ) * Math.cos( Lat2 ) * Math.cos( Lon2 - Lon1 ) + Math.sin( Lat1 ) * Math.sin( Lat2 ) );
                    // \n is for new line
                    //Result = (float) (6378.137 * Math.acos( Math.cos( latsave1*Math.PI/180 ) * Math.cos( latsave2*Math.PI/180 ) * Math.cos( (lonsave2*Math.PI/180) - (lonsave1*Math.PI/180) ) + Math.sin( latsave1*Math.PI/180 ) * Math.sin( latsave2*Math.PI ) ));
                    //Result = (float) (6378.137 * Math.acos( Math.cos( (latsave1*Math.PI/180) ) * Math.cos( (latsave2*Math.PI/180) ) * Math.cos( (lonsave2*Math.PI/180) - (lonsave1*Math.PI/180) ) + Math.sin( (latsave1*Math.PI/180) ) * Math.sin( (latsave2*Math.PI/180) ) )); //<--km
                    //Result = 6378.137 * Mat.acos( Cos( RADIANS(Latitude) ) * Cos( RADIANS( Lat ) ) * Cos( RADIANS( Lon ) - RADIANS(Longitude) ) + Sin( RADIANS(Latitude) ) * Sin( RADIANS( Lat ) ) )

                    Result = (float) (6378.137 * Math.acos(Math.cos(latsave1) * Math.cos(latsave2) * Math.cos(lonsave2 - lonsave1) + Math.sin(latsave1) * Math.sin(latsave2)));


                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latsave1 + "\nLong: " + lonsave1 + " radio" + MyCompassView.radiusCompass + "\n lat2 " + latsave2 + "\n lon2 " + lonsave2 + "\n Resultado distancia= " + (Result * 1000), Toast.LENGTH_LONG).show();
                    //Iniciar actividad de mapas
                    if (latsave1 != 0 && lonsave1 != 0 && latitudUsr != 0 && longitudUsr != 0) {
                        Intent mapa = new Intent(v.getContext(), MapsActivity.class);
                        System.out.println("Intent creado.");
                        mapa.putExtra("latsave1", latsave1);
                        mapa.putExtra("lonsave1", lonsave1);
                        mapa.putExtra("latitudUsr", latitudUsr);
                        mapa.putExtra("longitudUsr", longitudUsr);
                        System.out.println("Valores enviados.");
                        startActivity(mapa);
                        System.out.println("Nueva actividad iniciada.");
                    }else{
                        Toast.makeText(getApplicationContext(), "Al menos uno de los valores de ubicación no tiene un valor válido.",  Toast.LENGTH_LONG).show();
                        System.out.println("Al menos uno de los valores de ubicación no tiene un valor válido.");
                    }
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

            }
        });

        // Codigo de los input de las coordenadas
        // obtiene el objeto de la vista con el ID correspondiente
        longitudInput = (EditText) findViewById(R.id.longitudTxt);
        latitudInput = (EditText) findViewById(R.id.latitudTxt);
        buscarBtn = (Button) findViewById(R.id.buscarBtn);

        buscarBtn.setEnabled(false);//boton desactivado por defecto

        //Agregar el listener para el click en el boton
        buscarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                latitudUsr = Double.parseDouble(latitudInput.getText().toString());
                longitudUsr = Double.parseDouble(longitudInput.getText().toString());
                System.out.println(latitudUsr);
                System.out.println(longitudUsr);
            }
        });

        //Es el "Listener" para ver cuando cambia el texto en los inputs
        TextWatcher EnableBuscarBtnListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            //este listener se usara para activar o desactivar el boton segun los numeros cambien
            //validando que sean valores dobles validos en ambos inputs
            @Override
            public void afterTextChanged(Editable editable) {
                try{
                    //se lanza excepcion si no son valores dobles validos
                    Double.parseDouble(longitudInput.getText().toString());
                    Double.parseDouble(latitudInput.getText().toString());

                    //se activa si los dos valores son validos
                    buscarBtn.setEnabled(true);
                }catch (Exception e){

                    //se desactiva el boton si al menos un valor no sea valido en los inputs
                    buscarBtn.setEnabled(false);
                }
            }
        };

        //Se agrega el listener a cada input
        longitudInput.addTextChangedListener(EnableBuscarBtnListener);
        latitudInput.addTextChangedListener(EnableBuscarBtnListener);
    }

    private SensorEventListener mySensorEventListener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            myCompassView.updateDirection((float) event.values[0]);
            datos.setText("dir: " + myCompassView.direction + " lat" + latsave1 + " lon" + lonsave1);

        }
    };

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        if (sersorrunning) {
            mySensorManager.unregisterListener(mySensorEventListener);
        }
    }

    public double getScale(float w, float h) {

        if (h >= w) {
            SCALE = h / 600;

        } else {

            SCALE = 300;
        }
        return SCALE;
    }

}