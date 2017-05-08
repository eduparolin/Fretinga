package com.example.edupa.testefrete;

/**
 * Created by edupa on 24/02/2017.
 */

public interface closeListener {
    void close();
    void firstScreen(String user, String email, String password, String tipo);
    void secondScreen(String cpf, String rg, String dataNasc);
    void lastScreen(String nomeComp, byte[] foto);
}
