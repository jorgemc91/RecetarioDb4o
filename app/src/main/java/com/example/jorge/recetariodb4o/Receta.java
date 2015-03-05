package com.example.jorge.recetariodb4o;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Receta<ArrayList> implements Serializable, Parcelable{
    private String nombre, descri, tipo, img;

    public Receta(String nombre, String descri, String tipo, String img) {
        this.nombre = nombre;
        this.descri = descri;
        this.tipo = tipo;
        this.img = img;
    }

    public Receta() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescri() {
        return descri;
    }

    public void setDescri(String descri) {
        this.descri = descri;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Receta receta = (Receta) o;

        if (!descri.equals(receta.descri)) return false;
        if (!img.equals(receta.img)) return false;
        if (!nombre.equals(receta.nombre)) return false;
        if (!tipo.equals(receta.tipo)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = nombre.hashCode();
        result = 31 * result + descri.hashCode();
        result = 31 * result + tipo.hashCode();
        result = 31 * result + img.hashCode();
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.nombre);
        parcel.writeString(this.descri);
        parcel.writeString(this.tipo);
        parcel.writeString(this.img);
    }

    public Receta(Parcel p){
        this.nombre = p.readString();
        this.descri = p.readString();
        this.tipo = p.readString();
        this.img = p.readString();
    }

    public static final Creator<Receta> CREATOR = new Creator<Receta>() {

        @Override
        public Receta createFromParcel(Parcel parcel) {
            String nombre = parcel.readString();
            String descri = parcel.readString();
            String tipo = parcel.readString();
            String img = parcel.readString();
            return new Receta(nombre,descri,tipo,img);
        }

        @Override
        public Receta[] newArray(int i) {
            return new Receta[0];
        }
    };
}
