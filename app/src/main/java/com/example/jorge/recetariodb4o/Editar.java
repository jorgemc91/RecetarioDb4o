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
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;

import java.util.ArrayList;


public class Editar extends Activity {
    ObjectContainer bd;
    private ArrayList<Receta> alReceta = new ArrayList<Receta>();
    private EditText et1, et2;
    private Spinner sp;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogo_anadir);
        bd = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), getExternalFilesDir(null) + "/bd.db4o");
        String nom, desc, tipo;
        Bundle rec = getIntent().getExtras();
        alReceta = rec.getParcelableArrayList("recetas");
        index = rec.getInt("id");
        nom = rec.getString("nombre");
        desc = rec.getString("descripcion");
        tipo = rec.getString("tipo");
        et1 = (EditText) this.findViewById(R.id.etNombre);
        et2 = (EditText) this.findViewById(R.id.etDescripcion);
        sp = (Spinner) this.findViewById(R.id.sTipo);
        ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(this, R.array.tipos, android.R.layout.simple_spinner_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adaptador);
        et1.setText(nom);
        et2.setText(desc);
        if (tipo.equals("Entrante")) {
            sp.setSelection(0);
        } else if (tipo.equals("Plato")) {
            sp.setSelection(1);
        } else if (tipo.equals("Postre")) {
            sp.setSelection(2);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        bd.close();
    }

    public void anadir(View v) {
        final String descripcion;
        descripcion = alReceta.get(index).getDescri();
        ObjectSet<Receta> recetas = bd.query(new Predicate<Receta>() {
            @Override
            public boolean match(Receta r) {
                return r.getDescri().compareTo(descripcion)==0;
            }
        });
        String nom = et1.getText().toString().trim();
        String desc = et2.getText().toString().trim();
        String tipo = String.valueOf(sp.getSelectedItem());
        if (nom.length() > 0 || desc.length() > 0) {
            if(recetas.hasNext()){
                Receta datosN = recetas.next();
                datosN.setNombre(nom);
                datosN.setDescri(desc);
                datosN.setTipo(tipo);
                if (tipo.equals("Entrante")) {
                    datosN.setImg("entrante");
                    bd.store(datosN);
                    bd.commit();
                    alReceta.set(index, datosN);
                } else if (tipo.equals("Plato")) {
                    datosN.setImg("plato");
                    bd.store(datosN);
                    bd.commit();
                    alReceta.set(index, datosN);
                } else if (tipo.equals("Postre")) {
                    datosN.setImg("postre");
                    bd.store(datosN);
                    bd.commit();
                    alReceta.set(index, datosN);
                }
            }
            tostada("Receta modificada");
            Intent i = new Intent(this,Principal.class);
            startActivity(i);
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
