package com.example.edupa.testefrete;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.app.Fragment;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, loginCloseListener {

    static boolean isLoggedIn = false;
    static int TIPO_CONTA = 0;//0: Motorista, 1: Distribuidor, ....

    //TAGS
    static String AUTO_LOGIN = "auto_login";
    //
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    static DataControl data;
    static Button add;
    List<DadosCards> listCards;
    RecyclerView rv;
    static SharedPreferences user_prefs;
    static Motoristas actualUser;

    static MenuItem menuItem;

    static TextView headerNome, headerEmail;

    public static class Usuario{
        public Motoristas getMotorista() {
            return motorista;
        }

        public void setMotorista(Motoristas motorista) {
            this.motorista = motorista;
        }

        public Distribuidores getDistribuidor() {
            return distribuidor;
        }

        public void setDistribuidor(Distribuidores distribuidor) {
            this.distribuidor = distribuidor;
        }

        private Motoristas motorista;
        private Distribuidores distribuidor;
    }

    public static Usuario usuario = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        data = new DataControl(this);
        data.open();
        user_prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        boolean autoLogin = user_prefs.getBoolean(AUTO_LOGIN, false);
        if(autoLogin) {
            //Abre o fragmento de login.
            String usuario = user_prefs.getString("usuario", "");
            String senha = user_prefs.getString("senha", "");
            TIPO_CONTA = user_prefs.getInt("tipo_conta", 0);
            if(TIPO_CONTA == 0) {
                new startTask().execute("192.168.25.18", "1234", "loginMotorista", usuario, senha);
            }else if(TIPO_CONTA == 1){
                new startTask().execute("192.168.25.18", "1234", "loginDistribuidor", usuario, senha);
            }

        }else{
            openLoginScreen();
            toggle.setDrawerIndicatorEnabled(false);
            /*actualUser = fetchUser(user_prefs.getString("user", ""));
            new startTask().execute("192.168.25.18", "1234", "login",
                    actualUser.getUser(), actualUser.getPassword());
            isLoggedIn = true;*/
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        add = (Button)findViewById(R.id.add);

        /*add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, addActivity.class);
                startActivity(intent);
            }
        });*/



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        //View header = navigationView.inflateHeaderView(R.layout.nav_header_main2);
        headerNome = (TextView)header.findViewById(R.id.nome);
        headerEmail = (TextView)header.findViewById(R.id.edit_email);

        Menu menus = navigationView.getMenu();
        menuItem = menus.findItem(R.id.nav_camera);
        //updateLoginInfo(isLoggedIn);
        List<String> items = new ArrayList<>();
        items.add("Status: em transito");
        items.add("Status: entregue");

        rv = (RecyclerView)findViewById(R.id.recyclerList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        if(TIPO_CONTA == 0){
            add.setText("Pedir frete");
            pedirFrete pf = new pedirFrete();
            add.setOnClickListener(pf);
        }
        else if (TIPO_CONTA == 1){
            add.setText("Pedir frete");
            estouDisponivel ed = new estouDisponivel();
            add.setOnClickListener(ed);
        }

    }
    public class pedirFrete implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent pf = new Intent(Main2Activity.this, Frete.class);
            startActivity(pf);
        }
    }
    public class estouDisponivel implements View.OnClickListener{

        @Override
        public void onClick(View v) {

        }
    }
    public static void updateLoginInfo(boolean isLoggedIn){
        if(TIPO_CONTA == 0){
            headerNome.setText(usuario.getMotorista().getNome());
            headerEmail.setText(usuario.getMotorista().getEmail());
        }
        else if(TIPO_CONTA == 1){
            headerNome.setText(usuario.getDistribuidor().getNome_fantasia());
            headerEmail.setText(usuario.getDistribuidor().getEmail());
        }
        //if(isLoggedIn){

            /*headerNome.setText(actualUser.getNome());
            headerEmail.setText(actualUser.getEmail());*/
            //if()
        //}
    }
    public void openLoginScreen(){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        LoginFragment fragment = new LoginFragment();
        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.addToBackStack("login");
        fragmentTransaction.commit();
    }

    @Override
    public void onResume(){
        super.onResume();
        listCards = data.getAllCards();
        CardsAdapter adapter = new CardsAdapter(this, listCards);
        rv.setAdapter(adapter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        data.close();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(TIPO_CONTA == 0) {//Motorista

            if (id == R.id.nav_camera) {
                Intent intent = new Intent(Main2Activity.this, autoList.class);
                //Bundle b = new Bundle();
                //b.putString("user", actualUser.getUser());
                intent.putExtra("user", actualUser.getUsuario());
                startActivity(intent);
                // Handle the camera action
            } else if (id == R.id.nav_gallery) {

            } else if (id == R.id.nav_slideshow) {

            } else if (id == R.id.nav_manage) {

            } else if (id == R.id.nav_share) {
                Intent configuracoes = new Intent(Main2Activity.this, Configuracoes.class);
                startActivity(configuracoes);
            } else if (id == R.id.logout) {
                isLoggedIn = false;
                //Todo: Fechar todos os sockets e bancos de dados.
                openLoginScreen();
                SharedPreferences.Editor editor = user_prefs.edit();
                editor.putBoolean(AUTO_LOGIN, false);
                editor.commit();
                usuario = new Usuario();
            }
        }else if(TIPO_CONTA == 1){// Distribuidor

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void closeMain() {
        finish();
    }

    @Override
    public void showNavDrawer() {
        toggle.setDrawerIndicatorEnabled(true);
    }

    public class startTask extends connectionTask{
        @Override
        protected void onProgressUpdate(String... progress) {
            if(TIPO_CONTA == 0){
                try{
                    //JSONObject userInfo = new JSONObject(progress[0]);
                    //Toast.makeText(getActivity(), userInfo.getString("cpf"), Toast.LENGTH_SHORT).show();
                    JSONObject userInfo = new JSONObject(progress[0]);
                    Motoristas users = new Motoristas();
                    users.setNome(userInfo.getString("nome_completo"));
                    users.setCpf(userInfo.getString("cpf"));
                    users.setRg(userInfo.getString("rg"));
                    users.setEmail(userInfo.getString("email"));
                    users.setCelular(userInfo.getString("celular"));
                    users.setTelefone(userInfo.getString("telefone"));
                    users.setUsuario(userInfo.getString("usuario"));
                    users.setSenha(userInfo.getString("senha"));
                    usuario.setMotorista(users);
                    //actualUser = users;
                    Toast.makeText(Main2Activity.this, "Bem vindo, "+users.getNome(), Toast.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                    updateLoginInfo(true);
                    //listener.showNavDrawer();
                    //String teste = userInfo.getString("email");
                    //Log.d("Login", teste);
                }catch(Exception e){
                    Log.e("Login", e.toString());
                }
            }
            else if(TIPO_CONTA == 1){
                try{
                    //JSONObject userInfo = new JSONObject(progress[0]);
                    //Toast.makeText(getActivity(), userInfo.getString("cpf"), Toast.LENGTH_SHORT).show();
                    JSONObject userInfo = new JSONObject(progress[0]);
                    Distribuidores users = new Distribuidores();
                    users.setRazao_social(userInfo.getString("razao_social"));
                    users.setNome_fantasia(userInfo.getString("nome_fantasia"));
                    users.setCnpj(userInfo.getString("cnpj"));
                    users.setInscricao_estadual(userInfo.getString("inscricao_estadual"));
                    users.setPais(userInfo.getString("pais"));
                    users.setEstado(userInfo.getString("estado"));
                    users.setCidade(userInfo.getString("cidade"));
                    users.setSenha(userInfo.getString("senha"));
                    users.setEmail(userInfo.getString("email"));
                    usuario.setDistribuidor(users);
                    //actualUser = users;
                    Toast.makeText(Main2Activity.this, "Bem vindo, "+userInfo.getString("nome_fantasia"), Toast.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                    updateLoginInfo(true);
                    //listener.showNavDrawer();
                    //String teste = userInfo.getString("email");
                    //Log.d("Login", teste);
                }catch(Exception e){
                    Log.e("Login", e.toString());
                }
            }
            // if(progress[0].equals("True"))loginInfo.setText("Logado !");
            //else loginInfo.setText("Usuário ou senha incorretos ):");
        }

    }

    public static class LoginFragment extends Fragment{

        TextView cc;
        Button motorista, distribuidor;
        EditText user, pass;
        CheckBox remind;
        loginCloseListener listener;
        String sUser;
        //SharedPreferences userPrefs = getActivity().getSharedPreferences("user_prefs", MODE_PRIVATE);

        public void storeUser(String user, String senha, boolean auto_login, int TIPO_CONTA){
            SharedPreferences.Editor editor = user_prefs.edit();
            editor.putString("usuario", user);
            editor.putString("senha", senha);
            editor.putInt("tipo_conta", TIPO_CONTA);
            editor.putBoolean(AUTO_LOGIN, auto_login);
            editor.commit();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup vGroup, Bundle bundle) {
            final View actualView = inflater.inflate(R.layout.login_fragment, vGroup, false);
            cc = (TextView)actualView.findViewById(R.id.criar_conta);
            user = (EditText)actualView.findViewById(R.id.edit_user);
            final String storedUser = user_prefs.getString("user", "");
            user.setText(storedUser);
            pass = (EditText)actualView.findViewById(R.id.edit_pass);
            remind = (CheckBox)actualView.findViewById(R.id.checkBox);
            cc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), addUser.class);
                    startActivity(intent);
                    //Criar conta.
                }
            });
            motorista = (Button)actualView.findViewById(R.id.button_motorista);
            motorista.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TIPO_CONTA = 0;
                    sUser = user.getText().toString();
                    String sPass = pass.getText().toString();
                    new startTask().execute("192.168.25.18", "1234", "loginMotorista", sUser, sPass);
                    storeUser(sUser, sPass, remind.isChecked(), 0);
                    InputMethodManager imm = (InputMethodManager)getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(actualView.getWindowToken(), 0);
                    //Login.
                }
            });
            distribuidor = (Button)actualView.findViewById(R.id.button_distribuidor);
            distribuidor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TIPO_CONTA = 1;
                    sUser = user.getText().toString();
                    String sPass = pass.getText().toString();
                    new startTask().execute("192.168.25.18", "1234", "loginDistribuidor", sUser, sPass);
                    storeUser(sUser, sPass, remind.isChecked(), 1);
                    InputMethodManager imm = (InputMethodManager)getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(actualView.getWindowToken(), 0);
                    //Login.
                }
            });
            actualView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(keyCode == KeyEvent.KEYCODE_BACK){

                        return true;
                    }
                    return false;
                }
            });
            return actualView;
        }
        @Override
        public void onResume() {
            super.onResume();
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                        listener.closeMain();
                        Log.d("LoginFragment", "Tentando sair da aplicação");

                        return true;
                    }
                    return false;
                }
            });
        }
        @Override
        public void onAttach(Context context){
            super.onAttach(context);
            try {
                listener = (loginCloseListener) context;
            } catch (ClassCastException castException) {
                /** The activity does not implement the listener. */
            }
        }
        public class startTask extends connectionTask{
            @Override
            protected void onProgressUpdate(String... progress) {
                if(progress[0]!=null){
                    if(TIPO_CONTA == 0){
                        try{
                            //JSONObject userInfo = new JSONObject(progress[0]);
                            //Toast.makeText(getActivity(), userInfo.getString("cpf"), Toast.LENGTH_SHORT).show();
                            JSONObject userInfo = new JSONObject(progress[0]);
                            Motoristas users = new Motoristas();
                            users.setNome(userInfo.getString("nome_completo"));
                            users.setCpf(userInfo.getString("cpf"));
                            users.setRg(userInfo.getString("rg"));
                            users.setEmail(userInfo.getString("email"));
                            users.setCelular(userInfo.getString("celular"));
                            users.setTelefone(userInfo.getString("telefone"));
                            users.setUsuario(userInfo.getString("usuario"));
                            users.setSenha(userInfo.getString("senha"));
                            usuario.setMotorista(users);
                            //actualUser = users;
                            Toast.makeText(getActivity(), "Bem vindo, "+users.getNome(), Toast.LENGTH_SHORT).show();
                            getFragmentManager().popBackStack();
                            updateLoginInfo(true);
                            listener.showNavDrawer();
                            //String teste = userInfo.getString("email");
                            //Log.d("Login", teste);
                        }catch(Exception e){
                            Log.e("Login", e.toString());
                        }
                    }
                    else if(TIPO_CONTA == 1){
                        try{
                            //JSONObject userInfo = new JSONObject(progress[0]);
                            //Toast.makeText(getActivity(), userInfo.getString("cpf"), Toast.LENGTH_SHORT).show();
                            JSONObject userInfo = new JSONObject(progress[0]);
                            Distribuidores users = new Distribuidores();
                            users.setRazao_social(userInfo.getString("razao_social"));
                            users.setNome_fantasia(userInfo.getString("nome_fantasia"));
                            users.setCnpj(userInfo.getString("cnpj"));
                            users.setInscricao_estadual(userInfo.getString("inscricao_estadual"));
                            users.setPais(userInfo.getString("pais"));
                            users.setEstado(userInfo.getString("estado"));
                            users.setCidade(userInfo.getString("cidade"));
                            users.setSenha(userInfo.getString("senha"));
                            users.setEmail(userInfo.getString("email"));
                            usuario.setDistribuidor(users);
                            //actualUser = users;
                            Toast.makeText(getActivity(), "Bem vindo, "+userInfo.getString("nome_fantasia"), Toast.LENGTH_SHORT).show();
                            getFragmentManager().popBackStack();
                            updateLoginInfo(true);
                            listener.showNavDrawer();
                            //String teste = userInfo.getString("email");
                            //Log.d("Login", teste);
                        }catch(Exception e){
                            Log.e("Login", e.toString());
                        }
                    }


                }

            }

        }

    }
}
