package com.example.edupa.testefrete;

/**
 * Created by edupa on 21/02/2017.
 */

public class DadosCards {
    long id;
    String status;
    String nome_motorista;
    String peso;
    String carga;
    String cidade_inicial, cidade_final;
    int circleStatus;

    public void setId(long id){
        this.id = id;
    }
    public long getId(){
        return id;
    }

    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return status;
    }

    public void setNomeMot(String nome_motorista){
        this.nome_motorista = nome_motorista;
    }
    public String getNomeMot(){
        return nome_motorista;
    }

    public void setPeso(String peso){
        this.peso = peso;
    }
    public String getPeso(){
        return peso;
    }

    public void setCarga(String carga){
        this.carga = carga;
    }
    public String getCarga(){
        return carga;
    }

    public void setCidadeI(String cidade_inicial){
        this.cidade_inicial = cidade_inicial;
    }
    public String getCidadeI(){
        return cidade_inicial;
    }

    public void setCidadeF(String cidade_final){
        this.cidade_final = cidade_final;
    }
    public String getCidadeF(){
        return cidade_final;
    }

    public void setCircleStatus(int circleStatus){
        this.circleStatus = circleStatus;
    }
    public int getCircleStatus(){
        return circleStatus;
    }

}
