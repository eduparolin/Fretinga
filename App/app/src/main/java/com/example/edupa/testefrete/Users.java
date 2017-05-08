package com.example.edupa.testefrete;

import android.graphics.Bitmap;

/**
 * Created by edupa on 24/02/2017.
 */

public class Users {
    private int id;
    private String user;
    private String email;
    private String password;
    private String tipo;
    private String CPF;
    private String RG;
    private String dataNasc;
    private String nomeCompleto;
    private byte[] foto;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }

    public void setUser(String user){
        this.user = user;
    }
    public String getUser(){
        return user;
    }

    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return email;
    }

    public void setTipo(String tipo){
        this.tipo = tipo;
    }
    public String getTipo(){
        return tipo;
    }

    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return password;
    }

    public void setCPF(String CPF){
        this.CPF = CPF;
    }
    public String getCPF(){
        return CPF;
    }

    public void setRG(String RG){
        this.RG = RG;
    }
    public String getRG(){
        return RG;
    }

    public void setDataNasc(String dataNasc){
        this.dataNasc = dataNasc;
    }
    public String getDataNasc(){
        return dataNasc;
    }

    public void setNomeCompleto(String nomeCompleto){
        this.nomeCompleto = nomeCompleto;
    }
    public String getNomeCompleto(){
        return nomeCompleto;
    }

    public void setFoto(byte[] foto){
        this.foto = foto;
    }
    public byte[] getFoto(){
        return foto;
    }
}
