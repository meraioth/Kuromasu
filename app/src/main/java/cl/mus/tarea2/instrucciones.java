package cl.mus.tarea2;

import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

public class instrucciones extends AppCompatActivity {
    TextView ins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrucciones);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
       ins= (TextView)findViewById(R.id.contenido_inst);
        ins.setText("Se presenta un tablero dividido en celdas y que contiene algunos números. \nEl juego consiste en pintar algunas celdas de color negro de manera que cada número indique la cantidad de celdas de color blanco visibles en las cuatro direcciones: norte, sur, este y oeste incluyendo la propia celda numerada. Las casillas visibles se contabilizan hasta llegar a una casilla de color negro o al borde del tablero.\nLas casillas de color negro no pueden ser adyacentes ortogonalmente pero sí en diagonal. \nAdemás, todas las casillas de color blanco deben formar una única área continua ortogonal.");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ins.setElegantTextHeight(true);
        }
        ins.setGravity(Gravity.CENTER);
        ins.setTextSize(20);



    }


}
