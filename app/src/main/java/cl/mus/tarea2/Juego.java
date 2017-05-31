package cl.mus.tarea2;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Juego extends AppCompatActivity {
    TextView juego;
    Map<Integer,String> map;
    static String[][] colores_para_guardar;
    SharedPreferences sp;
    int id;
    FloatingActionButton instruccion,cargar,guardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        cargar= (FloatingActionButton) findViewById(R.id.cargar);
        guardar= (FloatingActionButton) findViewById(R.id.guardar);
        instruccion = (FloatingActionButton) findViewById(R.id.instruccion);
        instruccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = createSimpleDialog();
                dialog.show();
            }
        });
        map= getMapa();
        //Se obtiene info de la activity anterior, el nombre del archivo
        Intent intent= getIntent();
        this.id =intent.getIntExtra("Juego",0);
        final String url = intent.getStringExtra("name");
        //----
        //Arreglo de string q almacena en cada casilla una fila de la grilla
        String[] tablero = getInit(id).split("\n");

        final int size_tablero = (tablero.length-1)/2;
        final GridView gridview = (GridView) findViewById(R.id.juego_grilla);
        gridview.setNumColumns(size_tablero);
        gridview.setHorizontalSpacing(5);
        gridview.setVerticalSpacing(5);
        gridview.setAdapter(new ButtonAdapter(this,size_tablero,id,url));

        cargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("sharedpref","juego"+String.valueOf(id));
                sp= getSharedPreferences("juego"+String.valueOf(id),0);
               String colores_guardados = sp.getString("colores", "");
                Log.d("str colores_guard",colores_guardados);
                    if(colores_guardados.compareTo("")!=0){
                    Log.i("load colores_guardados", colores_guardados);
                    gridview.setAdapter(new ButtonAdapter(v.getContext(),size_tablero,id,url,colores_as_Matrix(colores_guardados,size_tablero)));
                        Toast.makeText(v.getContext(),"Juego Cargado",Toast.LENGTH_SHORT).show();
                    }else Toast.makeText(v.getContext(),"Debe guardar antes de querer cargar",Toast.LENGTH_SHORT).show();
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp= getSharedPreferences("juego"+String.valueOf(id),0);
                if(sp!=null) {
                    if(colores_para_guardar!=null){
                        Log.i("save colores_guardados", colores_as_String());
                        sp.edit().putString("colores", colores_as_String()).commit();
                        sp.edit().putInt("id", id).commit();
                        sp.edit().putInt("size", colores_para_guardar.length).commit();
                        Toast.makeText(v.getContext(),"Juego Guardado",Toast.LENGTH_SHORT).show();
                    }else Toast.makeText(v.getContext(),"Debe jugar antes de guardar",Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private Map<Integer,String> getMapa() {
        Map<Integer,String> mapita= new HashMap<Integer, String>();
        int size;
        String[] lista_juego_asset = new String[0];

        try {
            lista_juego_asset = getAssets().list("juegos");
        } catch (IOException e) {
            e.printStackTrace();
        }
        size = lista_juego_asset.length;
        for (int i = 0; i < size; i++) {
            mapita.put(i,lista_juego_asset[i]);
            Log.d("id:"+i,lista_juego_asset[i]);
        }
        return mapita;
    }

    public String getInit(int id){
        String text="";
        try{
            InputStream is = getApplicationContext().getAssets().open("juegos/"+map.get(id));
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            text=new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;

    }
    public static void setColores_a_guardar(String[][] colores){
        colores_para_guardar=colores;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private String colores_as_String(){
        String aux="";
        for (int i = 0; i < colores_para_guardar.length; i++) {
            for (int j = 0; j < colores_para_guardar.length; j++) {
                aux+=colores_para_guardar[i][j];
            }
        }
        return aux;
    }

    private String[][] colores_as_Matrix(String colores,int size){
        String[][] salida = new String[size][size];
        Log.d("colores",colores);
        int contador=0;
        if(!colores.equals(""))
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                salida[i][j]=colores.substring(contador,contador+1);
                contador++;
                System.out.print(salida[i][j]);
            }
            System.out.println("");
        }
        return salida;
    }

    public AlertDialog createSimpleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Instrucciones")
                .setMessage("Se presenta un tablero dividido en celdas y que contiene algunos números. \nEl juego consiste en pintar algunas celdas de color negro de manera que cada número indique la cantidad de celdas de color blanco visibles en las cuatro direcciones: norte, sur, este y oeste incluyendo la propia celda numerada. Las casillas visibles se contabilizan hasta llegar a una casilla de color negro o al borde del tablero.\nLas casillas de color negro no pueden ser adyacentes ortogonalmente pero sí en diagonal. \nAdemás, todas las casillas de color blanco deben formar una única área continua ortogonal.")
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

        return builder.create();
    }

}
