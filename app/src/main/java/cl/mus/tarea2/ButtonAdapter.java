package cl.mus.tarea2;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class ButtonAdapter extends BaseAdapter {
    private Context mContext;
    int cantidad;
    String url;
    int size;
    String[][] mapa,mapa_fin;
    String [][] colores;
    Map<Integer,Integer> size_button;
    // Gets the context so it can be used later
    public ButtonAdapter(Context c, int size, int id, String url, String[][] colores) {
        size_button=new HashMap<Integer, Integer>();
        this.url=url;
        mContext = c;
        this.cantidad=size*size;
        this.size=size;
//   this.colores= new String[size][size];
        mapa = getMapa(url);
        size_button.put(4,165);
        size_button.put(5,135);
        size_button.put(6,105);
        size_button.put(7,90);
        size_button.put(9,70);
        this.mapa_fin=getMapa_final(url);
        if(colores!=null){
            this.colores=colores;
        }


    }

    public ButtonAdapter(Context c, int size, int id, String url) {
        size_button=new HashMap<Integer, Integer>();
        this.url=url;
        mContext = c;
        this.cantidad=size*size;
        this.size=size;
        mapa = getMapa(url);
        size_button.put(4,165);
        size_button.put(5,135);
        size_button.put(6,105);
        size_button.put(7,90);
        size_button.put(9,70);
        this.colores=new String[size][size];
        this.mapa_fin=getMapa_final(url);


    }

    private String[][] getMapa(String url) {
        String text="";
        try{
            InputStream is = mContext.getApplicationContext().getAssets().open("juegos/"+url);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            text=new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String var[]= text.split("\n");
        String out[][]=new String[size][size];
        for (int i = 0; i < size; i++) {
            out[i]=var[i].split(" ");
        }

        return out;
    }

    private String[][] getMapa_final(String url) {
        this.colores= new String[size][size];
        String text="";
        try{
            InputStream is = mContext.getApplicationContext().getAssets().open("juegos/"+url);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            text=new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] inf = text.split("\n\n");
        text = inf[1];
        Log.d("final",text);
        String var[]= text.split("\n");
        String out[][]=new String[size][size];
        for (int i = 0; i < size; i++) {
            out[i]=var[i].split(" ");
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(out[i][j].contains("B")|| out[i][j].contains("N")){
                    colores[i][j]="G";

                }else{
                    colores[i][j]="B";
                }
            }
        }
        return out;
    }

    // Total number of things contained within the adapter
    public int getCount() {
        return cantidad;
    }

    // Require for structure, not really used in my code.
    public Object getItem(int position) {
        return null;
    }

    // Require for structure, not really used in my code. Can be used to get the id of an item in the adapter for manual control.
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final Button btn;
        if (convertView == null) { // if it's not recycled, initialize some
            // attributes
            btn = new Button (mContext);
            btn.setLayoutParams(new GridView.LayoutParams(size_button.get(size), size_button.get(size)));


            set_color_inicial(btn,position);
        } else {
            btn = (Button) convertView;
        }
        // btn.setBackgroundColor (Color.BLACK);
        btn.setId(position);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            if(colores[position/size][position%size].contains("G")&& mapa[position/size][position%size].contains(".")){
                colores[position/size][position%size] = "B";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btn.setBackground(mContext.getDrawable(R.drawable.blanco));
                }
                else btn.setBackgroundColor(Color.WHITE);
                sonar();
            }
            else if(colores[position/size][position%size].contains("B")&& mapa[position/size][position%size].contains(".")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btn.setBackground(mContext.getDrawable(R.drawable.negro));
                }else  btn.setBackgroundColor(Color.BLACK);
                colores[position/size][position%size] = "N";
                checkConstrain();
                sonar();
            }else if(colores[position/size][position%size].contains("N")&& mapa[position/size][position%size].contains(".")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btn.setBackground(mContext.getDrawable(R.drawable.blanco));
                }
                else btn.setBackgroundColor(Color.WHITE);
                colores[position/size][position%size] = "B";
                sonar();
            }
            if(getMovRestantes()==0){
                    ShowFinal(resultado_final(),"");
            }
            Juego.setColores_a_guardar(colores);
            }
        });
        return btn;

    }

    private void checkConstrain() {
        String aux;
        for (int i = 0; i <size ; i++) {
            for (int j = 0; j < size-1; j++) {
                if(colores[i][j].equals(colores[i][j+1])&& colores[i][j].equals("N")){
                    Toast.makeText(mContext,"No deben haber dos casillas negras continuas",Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private void set_color_inicial(Button btn,int pos) {

        String[][] mapa_t = mapa;
        String[] fila=mapa_t[pos/size];

        Log.d("fila :",String.valueOf(pos/size));
        Log.d("columna :", String.valueOf(pos%size));
        Log.d("Valor:" ,String.valueOf(fila[pos%size]));

        if(colores[pos/size][pos%size].equals("B")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btn.setBackground(mContext.getDrawable(R.drawable.blanco));
            }
            if(!fila[pos%size].contains(".")){
                btn.setText(fila[pos%size]);
            }

        }else if(colores[pos/size][pos%size].equals("G")){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btn.setBackground(mContext.getDrawable(R.drawable.gris));
            }

            btn.setText("");

        }else if(colores[pos/size][pos%size].equals("N")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btn.setBackground(mContext.getDrawable(R.drawable.negro));
            }
        }
    }
    private int getMovRestantes(){
        int cant=0;
        for (int i = 0; i <size; i++) {
            for (int j = 0; j <size ; j++) {
                if(colores[i][j].contains("G")){
                    cant++;
                }
            }
        }
        return  cant;
    }
    private boolean resultado_final(){
        boolean gano=true;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(mapa[i][j].contains(".")){
                    if(!mapa_fin[i][j].contains(colores[i][j])){
                       gano=false;
                    }
                }
            }
        }
        return gano;
    }

    public void ShowFinal(Boolean fin, String celdas_malas) {
        if(fin){
            sonar_victoria();
            Toast.makeText(mContext.getApplicationContext(),"Felicidades Completo el Juego Exitosamente",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mContext.getApplicationContext(),"Ud aun no gana, existen celdas incorrectas",Toast.LENGTH_SHORT).show();

        }

    }

    private void sonar_victoria() {
        MediaPlayer rana = MediaPlayer.create(mContext, R.raw.success);
        rana.start();
        rana.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {

                mp.release();
            }
        });
    }

    private void sonar(){
        MediaPlayer rana = MediaPlayer.create(mContext, R.raw.piece);
        rana.start();
        rana.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {

                mp.release();
            }
        });
    }
}