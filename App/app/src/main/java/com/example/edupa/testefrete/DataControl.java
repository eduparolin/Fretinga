package com.example.edupa.testefrete;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.provider.UserDictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edupa on 21/02/2017.
 */

public class DataControl {
    private SQLiteDatabase database;

    private String[] allCards = { Database.COLUMN_ID,
            Database.COLUMN_STATUS, Database.COLUMN_NOMEMOT, Database.COLUMN_PESO,
            Database.COLUMN_CARGA, Database.COLUMN_CIDADEI, Database.COLUMN_CIDADEF,
            Database.COLUMN_CIRCLESTATUS};

    private String[] allUsers = { Database.COLUMN_ID,
            Database.COLUMN_USER, Database.COLUMN_EMAIL, Database.COLUMN_SENHA,
            Database.COLUMN_TIPO,
            Database.COLUMN_CPF, Database.COLUMN_RG, Database.COLUMN_DATANASC,
            Database.COLUMN_NOMECOMPLETO, Database.COLUMN_FOTO};
    private String[] allAutos = { Database.COLUMN_ID, Database.COLUMN_USER,
            Database.COLUMN_TIPO_CARRO, Database.COLUMN_TIPO_CARROCERIA, Database.COLUMN_CARGA_MAX,
            Database.COLUMN_PLACA,
            Database.COLUMN_NOME_PROP, Database.COLUMN_RASTREADOR, Database.COLUMN_MARCA,
            Database.COLUMN_MODELO, Database.COLUMN_ANO, Database.COLUMN_COR};

    Database db;

    public DataControl(Context context){
        db = new Database(context);
    }

    public void open(){
        database = db.getWritableDatabase();
    }
    public void close() {
        db.close();
    }

    public DadosCards newFreteCard(String status, String nomeM, String peso, String carga,
                             String cidade_i, String cidade_f, int circle_status){
        ContentValues values = new ContentValues();
        values.put(Database.COLUMN_STATUS, status);
        values.put(Database.COLUMN_NOMEMOT, nomeM);
        values.put(Database.COLUMN_PESO, peso);
        values.put(Database.COLUMN_CARGA, carga);
        values.put(Database.COLUMN_CIDADEI, cidade_i);
        values.put(Database.COLUMN_CIDADEF, cidade_f);
        values.put(Database.COLUMN_CIRCLESTATUS, circle_status);
        long insertId = database.insert(Database.TABLE_ACTUAL_TASKS, null,
                values);
        Cursor cursor = database.query(Database.TABLE_ACTUAL_TASKS,
                allCards, Database.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        DadosCards newModulo = cursorToCard(cursor);
        cursor.close();
        return newModulo;
    }
    /*public Users newUser(String user, String email, String password, String tipo, String cpf,
                                   String rg, String datanasc, String nomecomp, byte[] foto){
        ContentValues values = new ContentValues();
        values.put(Database.COLUMN_USER, user);
        values.put(Database.COLUMN_EMAIL, email);
        values.put(Database.COLUMN_SENHA, password);
        values.put(Database.COLUMN_TIPO, tipo);
        values.put(Database.COLUMN_CPF, cpf);
        values.put(Database.COLUMN_RG, rg);
        values.put(Database.COLUMN_DATANASC, datanasc);
        values.put(Database.COLUMN_NOMECOMPLETO, nomecomp);
        values.put(Database.COLUMN_FOTO, foto);
        long insertId = database.insert(Database.TABLE_USERS, null,
                values);
        Cursor cursor = database.query(Database.TABLE_USERS,
                allUsers, Database.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Users newUser = cursorToUser(cursor);
        cursor.close();
        return newUser;
    }*/
    public Automoveis newAutomovel(String user, String tipoCarro, String tipoCarroceria, int cargaMax,
                              String placa, String nomeProp, int rastreador, String marca,
                              String modelo, int ano, String cor){
        ContentValues values = new ContentValues();
        values.put(Database.COLUMN_USER, user);
        values.put(Database.COLUMN_TIPO_CARRO, tipoCarro);
        values.put(Database.COLUMN_TIPO_CARROCERIA, tipoCarroceria);
        values.put(Database.COLUMN_CARGA_MAX, cargaMax);
        values.put(Database.COLUMN_PLACA, placa);
        values.put(Database.COLUMN_NOME_PROP, nomeProp);
        values.put(Database.COLUMN_RASTREADOR, rastreador);
        values.put(Database.COLUMN_MARCA, marca);
        values.put(Database.COLUMN_MODELO, modelo);
        values.put(Database.COLUMN_ANO, ano);
        values.put(Database.COLUMN_COR, cor);
        long insertId = database.insert(Database.TABLE_AUTOS, null,
                values);
        Cursor cursor = database.query(Database.TABLE_AUTOS,
                allAutos, Database.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Automoveis newAuto = cursorToAutomovel(cursor);
        cursor.close();
        return newAuto;
    }
    /*public Users getUser(String user){
        Users u;
        Cursor cursor =
                database.rawQuery("select * from " + Database.TABLE_USERS +
                        " where user = '"+user+"'", null);
        cursor.moveToFirst();
        u = cursorToUser(cursor);
        cursor.close();
        return u;
    }*/

    public List<DadosCards> getAllCards(){
        List<DadosCards> list = new ArrayList<>();
        Cursor cursor = database.query(Database.TABLE_ACTUAL_TASKS,
                allCards, null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            DadosCards cards = cursorToCard(cursor);
            list.add(cards);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    private DadosCards cursorToCard(Cursor cursor) {
        DadosCards modulo = new DadosCards();
        modulo.setId(cursor.getLong(0));
        modulo.setStatus(cursor.getString(1));
        modulo.setNomeMot(cursor.getString(2));
        modulo.setPeso(cursor.getString(3));
        modulo.setCarga(cursor.getString(4));
        modulo.setCidadeI(cursor.getString(5));
        modulo.setCidadeF(cursor.getString(6));
        modulo.setCircleStatus(cursor.getInt(7));
        return modulo;
    }
    /*private Users cursorToUser(Cursor cursor) {
        Users user = new Users();
        user.setId(cursor.getInt(0));
        user.setUser(cursor.getString(1));
        user.setEmail(cursor.getString(2));
        user.setPassword(cursor.getString(3));
        user.setTipo(cursor.getString(4));

        user.setCPF(cursor.getString(5));
        user.setRG(cursor.getString(6));
        user.setDataNasc(cursor.getString(7));
        user.setNomeCompleto(cursor.getString(8));
        user.setFoto(cursor.getBlob(9));
        return user;
    }*/

    private Automoveis cursorToAutomovel(Cursor cursor) {
        Automoveis auto = new Automoveis();
        auto.setId(cursor.getInt(0));
        auto.setUser(cursor.getString(1));
        auto.setTipo(cursor.getString(2));
        auto.setCarroceria(cursor.getString(3));
        auto.setCargaMaxima(cursor.getInt(4));
        auto.setPlaca(cursor.getString(5));

        auto.setNome(cursor.getString(6));
        auto.setRastreador(cursor.getInt(7));
        auto.setMarca(cursor.getString(8));
        auto.setModelo(cursor.getString(9));
        auto.setAno(cursor.getInt(10));
        auto.setCor(cursor.getString(11));
        return auto;
    }
}