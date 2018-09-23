package com.example.hv12.ejercicio_1_temporizador;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView lblContador;
    Button btniIniciar, btnPausar, btnReanudar;
    ContadorAsincrono contador;
    EditText editTextContar;

    //Declaracion de variables de ProgressBar
    private static int contProgreso = 0;
    private ProgressBar pgbarHorizontal;
    private Handler manejarProcesos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lblContador = findViewById(R.id.lblContador);
        btniIniciar = findViewById(R.id.btnIniciar);
        btnPausar=findViewById(R.id.btnPausar);
        btnReanudar=findViewById(R.id.btnReanudar);
        editTextContar=findViewById(R.id.editTextContar);

        //Inicializamos los controles de ProgressBar
        this.pgbarHorizontal = findViewById(R.id.pgbarHorizontal);
        this.manejarProcesos = new Handler();

        //Modificamos las propiedades de los controles de ProgressBar
        this.pgbarHorizontal.setMax(60);

        btniIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarContador();
                //Creamos el HIlo Secundario de ProgressBar
                new Thread(new HiloSecundario()).start();
                }

            //HILO SECUNDARIO
            //====================================================
            final class HiloSecundario implements Runnable{

                @Override
                //Ejecución de la Taerea secundaria
                public void run() {
                    //Simular el avance del Progressbar
                    while (contProgreso < 60){

                        MetodoEspera();
                        //EStablecemos un manejador para la parte visual
                        //================================================
                        manejarProcesos.post(new Runnable() {
                            @Override
                            public void run() {
                                pgbarHorizontal.setProgress(contProgreso);
                                //Preguntamos si ya se termino de completar el progress
                                if (contProgreso == 60){
                                    //Si el proceso ya termino mostramos el mensaje
                                    Toast.makeText(MainActivity.this,
                                            "Proceso Concluido!",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        //================================================
                    }
                }
                //Metodo que simula un tiempo de espera
                private void MetodoEspera(){
                    try {
                        Thread.sleep(1000);
                        contProgreso++;
                    }catch (Exception e){

                    }
                }
            }
            //====================================================

        });

        btnPausar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pausarContador();
            }
        });

        btnReanudar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reanudarContador();
            }
        });
    }

    private void iniciarContador() {

        /**si es primera vez -> se inicia **/
        if (contador == null) {
            contador = new ContadorAsincrono(this, lblContador);
            contador.execute(60);
            /**si ha terminado de ejecutar el hilo -> se crea otro hilo **/
        } else if (contador.getStatus() == AsyncTask.Status.FINISHED) {
            contador = new ContadorAsincrono(this, lblContador);
            contador.execute(60);
        }
    }

    private void pausarContador() {
        /** si esta ejecutado y no esta pausado -> entonces se pausa**/
        contador.pausarContador();
    }

    private void reanudarContador() {
            /** si no entro en las condiciones anteriores por defecto esta pausado -> se reanuda*/
            contador.reanudarContador();
        }
}

