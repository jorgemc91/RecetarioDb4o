package com.example.jorge.recetariodb4o;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;

import java.util.ArrayList;
import java.util.List;


public class Principal extends Activity {
    ObjectContainer bd;
    private ArrayList<Receta> datosv2 = new ArrayList<Receta>();
    private AdaptadorArrayList aal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);
        bd = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), getExternalFilesDir(null) + "/bd.db4o");

        aal = new AdaptadorArrayList(this, R.layout.lista_detalle, datosv2);
        final ListView lv = (ListView) findViewById(R.id.lvLista);
        lv.setAdapter(aal);
        registerForContextMenu(lv);
        leerBD();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        bd = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), getExternalFilesDir(null) + "/bd.db4o");
    }

    @Override
    protected void onPause() {
        super.onPause();
        bd.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.action_anadir){
            Intent i = new Intent(this,Anadir.class);
            Bundle b=new Bundle();
            b.putParcelableArrayList("recetas",datosv2);
            i.putExtras(b);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        if (id == R.id.action_borrar) {
            return borrar(index);
        } else if (id == R.id.action_editar) {
            return editar(index);
        }
        return super.onContextItemSelected(item);
    }

    private void leerBD(){
        Receta receta = new Receta(null, null, null, null);
        List<Receta> recetas = bd.queryByExample(receta);
        for(Receta r: recetas){
            datosv2.add(new Receta(r.getNombre(), r.getDescri(), r.getTipo(), r.getImg()));
        }
    }

    private boolean borrar(int index){
        final String desc;
        desc = datosv2.get(index).getDescri();
        ObjectSet<Receta> recetas = bd.query(new Predicate<Receta>() {
            @Override
            public boolean match(Receta r) {
                return r.getDescri().compareTo(desc)==0;
            }
        });
        if (recetas.hasNext()){
            Receta r = recetas.next();
            bd.delete(r);
            bd.commit();
            datosv2.remove(index);
            aal.notifyDataSetChanged();
            tostada("Receta eliminada");
        }
        return true;
    }

    private boolean editar(final int index) {
        final Receta datosN = new Receta();
        final String nom,desc,tipo;
        Receta datosA = new Receta();
        datosA = datosv2.get(index);
        nom = datosA.getNombre();
        desc = datosA.getDescri();
        tipo = datosA.getTipo();
        Intent i = new Intent(this,Editar.class);
        Bundle b=new Bundle();
        b.putParcelableArrayList("recetas",datosv2);
        i.putExtras(b);
        i.putExtra("id",index);
        i.putExtra("nombre",nom);
        i.putExtra("descripcion",desc);
        i.putExtra("tipo",tipo);
        startActivity(i);
        return true;
    }

    private void tostada(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
