package cl.mus.tarea2;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements  AdapterView.OnItemClickListener {
    ListView juegos;
    String[] lista_juego_asset;
    FloatingActionButton instruccioness;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instruccioness = (FloatingActionButton) findViewById(R.id.instruction);
        instruccioness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instrucciones();
            }
        });
        juegos = (ListView) findViewById(R.id.juegos);
        int size=0;
         lista_juego_asset= new String[0];

        try {
            lista_juego_asset = getAssets().list("juegos");
        } catch (IOException e) {
            e.printStackTrace();
        }
        size = lista_juego_asset.length;
        String[] lista_juego = new String[size];
        for (int i = 0; i < size; i++) {
            lista_juego[i] = "Kuromasu "+ lista_juego_asset[i].substring(8,11);

        }
        juegos.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_2,android.R.id.text1,lista_juego));
        juegos.setOnItemClickListener(this);
    }

    private void instrucciones() {
        startActivity(new Intent(this,instrucciones.class));
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,Juego.class);
        intent.putExtra("Juego",position);
        intent.putExtra("name",lista_juego_asset[position]);
        Log.d("id",String.valueOf(position));
        startActivity(intent);
    }
}
