package com.example.jorge.recetariodb4o;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

import java.util.ArrayList;

public class Anadir extends Activity{
    ObjectContainer bd;
    private ArrayList<Receta> alReceta = new ArrayList<Receta>();
    private EditText et1, et2;
    private Spinner sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogo_anadir);
        bd = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), getExternalFilesDir(null) + "/bd.db4o");

        Bundle b = getIntent().getExtras();
        alReceta = b.getParcelableArrayList("recetas");

        Spinner spinner = (Spinner) this.findViewById(R.id.sTipo);
        ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(this, R.array.tipos,
                android.R.layout.simple_spinner_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adaptador);
        et1 = (EditText) this.findViewById(R.id.etNombre);
        et2 = (EditText) this.findViewById(R.id.etDescripcion);
        sp = (Spinner) this.findViewById(R.id.sTipo);
    }

    @Override
    protected void onPause() {
        super.onPause();
        bd.close();
    }

    public void anadir(View v) {
        final Receta receta = new Receta();
        try {
            String nom = et1.getText().toString();
            String desc = et2.getText().toString();
            String tipo = String.valueOf(sp.getSelectedItem());
            receta.setNombre(nom);
            receta.setDescri(desc);
            receta.setTipo(tipo);
            if (tipo.equals("Entrante")) {
                receta.setImg("entrante");
                bd.store(receta);
                bd.commit();
                alReceta.add(receta);
            } else if (tipo.equals("Plato")) {
                receta.setImg("plato");
                bd.store(receta);
                bd.commit();
                alReceta.add(receta);
            } else if (tipo.equals("Postre")) {
                receta.setImg("postre");
                bd.store(receta);
                bd.commit();
                alReceta.add(receta);
            }
            tostada("Receta Añadida");
            Intent i = new Intent(this,Principal.class);
            startActivity(i);
        }catch (Exception e){
            tostada("No se ha podido añadir");
        }
    }

    public void cancelar(View v){
        Intent i = new Intent(this,Principal.class);
        startActivity(i);
    }

    private void tostada(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
