package com.example.edupa.testefrete;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by edupa on 21/02/2017.
 */

public class Database extends SQLiteOpenHelper {

    public static final String DBName = "CardsDB.db";

    //Tabelas
    public static final String TABLE_ACTUAL_TASKS = "actual_card";
    public static final String TABLE_USERS = "usuarios";
    public static final String TABLE_AUTOS = "automoveis";
    //

    public static final String COLUMN_ID = "_id";//_id é geral

    //Colunas dos cards
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_NOMEMOT = "nome_mot";
    public static final String COLUMN_PESO = "peso";
    public static final String COLUMN_CARGA = "carga";
    public static final String COLUMN_CIDADEI = "cidade_i";
    public static final String COLUMN_CIDADEF = "cidade_f";
    public static final String COLUMN_CIRCLESTATUS = "circle_status";
    //

    //Colunas dos usuários
    public static final String COLUMN_USER = "user";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_SENHA = "senha";
    public static final String COLUMN_TIPO = "tipo";
    public static final String COLUMN_CPF = "cpf";
    public static final String COLUMN_RG = "rg";
    public static final String COLUMN_DATANASC = "data_nasc";
    public static final String COLUMN_NOMECOMPLETO = "nome_completo";
    public static final String COLUMN_FOTO = "foto";
    //

    //Colunas dos automoveis
    public static final String COLUMN_TIPO_CARRO = "tipo";
    public static final String COLUMN_TIPO_CARROCERIA = "carroceria";
    public static final String COLUMN_CARGA_MAX = "carga";
    public static final String COLUMN_PLACA = "placa";
    public static final String COLUMN_NOME_PROP = "nome";
    public static final String COLUMN_RASTREADOR = "rast";
    public static final String COLUMN_MARCA = "marca";
    public static final String COLUMN_MODELO = "modelo";
    public static final String COLUMN_ANO = "ano";
    public static final String COLUMN_COR = "cor";

    //Criando tabela dos cards
    private static final String CREATE_CARDS = "create table "
            + TABLE_ACTUAL_TASKS + "( " + COLUMN_ID
            + " integer primary key autoincrement, " +COLUMN_USER + " text not null, " + COLUMN_STATUS
            + " text not null, " + COLUMN_NOMEMOT + " text not null, " +
            COLUMN_PESO + " text not null, " + COLUMN_CARGA + " text not null, " +
            COLUMN_CIDADEI + " text not null, " + COLUMN_CIDADEF + " text not null, "
            + COLUMN_CIRCLESTATUS +" integer);";
    //

    //Criando tabela dos usuários
    private static final String CREATE_USERS = "create table "
            + TABLE_USERS + "( " + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_USER
            + " text not null, " + COLUMN_EMAIL + " text not null, " +
            COLUMN_SENHA + " text not null, " + COLUMN_TIPO + " text not null, " +
            COLUMN_CPF + " text not null, " + COLUMN_RG + " text not null, " +
            COLUMN_DATANASC + " text not null, " + COLUMN_NOMECOMPLETO +" text not null, " +
            COLUMN_FOTO + " blob);";
    //

    //Criando tabela dos automoveis
    private static final String CREATE_AUTOS = "create table "
            + TABLE_AUTOS + "( " + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TIPO_CARRO
            + " text not null, " + COLUMN_TIPO_CARROCERIA + " text not null, " +
            COLUMN_CARGA_MAX + " integer, " + COLUMN_PLACA + " text not null, " +
            COLUMN_NOME_PROP + " text not null, " + COLUMN_RASTREADOR + " integer, " +
            COLUMN_MARCA + " text not null, " + COLUMN_MODELO +" text not null, " +
            COLUMN_ANO + " integer, "+ COLUMN_COR + " text not null);";
    //

    public Database(Context context){
        super(context, DBName, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CARDS);
        db.execSQL(CREATE_USERS);
        db.execSQL(CREATE_AUTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(Database.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS actual_card");
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS automoveis");
        onCreate(db);
    }
}
