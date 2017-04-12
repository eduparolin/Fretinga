package com.example.edupa.testefrete;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.app.Fragment;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, loginCloseListener {

    static boolean isLoggedIn = false;
    static int TIPO_CONTA = 0;//0: Motorista, 1: Distribuidor, ....

    //TAGS
    static String AUTO_LOGIN = "auto_login";
    //

    static DataControl data;
    Button add;
    List<DadosCards> listCards;
    RecyclerView rv;
    static SharedPreferences user_prefs;
    static Users actualUser;

    static MenuItem menuItem;

    static TextView headerNome, headerEmail;

    public static Users fetchUser(String user){
        return data.getUser(user);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        data = new DataControl(this);
        data.open();
        user_prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        if(!user_prefs.getBoolean(AUTO_LOGIN, false)) {
            //Abre o fragmento de login.
            openLoginScreen();
        }else{
            actualUser = fetchUser(user_prefs.getString("user", ""));
            new startTask().execute("192.168.25.18", "1234", "login",
                    actualUser.getUser(), actualUser.getPassword());
            isLoggedIn = true;
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        add = (Button)findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, addActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        //View header = navigationView.inflateHeaderView(R.layout.nav_header_main2);
        headerNome = (TextView)header.findViewById(R.id.nome);
        headerEmail = (TextView)header.findViewById(R.id.email);

        Menu menus = navigationView.getMenu();
        menuItem = menus.findItem(R.id.nav_camera);



        updateLoginInfo(isLoggedIn);

        List<String> items = new ArrayList<>();
        items.add("Status: em transito");
        items.add("Status: entregue");

        rv = (RecyclerView)findViewById(R.id.recyclerList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

    }
    public static void updateLoginInfo(boolean isLoggedIn){
        if(isLoggedIn){
            String nome;
            if(actualUser.getTipo().equals("cnpj")) {
                nome = actualUser.getNomeCompleto() +
                        " - Distribuidor";
                TIPO_CONTA = 1;
                menuItem.setTitle("Meus pedidos");
            }
            else {
                nome = actualUser.getNomeCompleto() +
                        " - Motorista";
                TIPO_CONTA = 0;
                menuItem.setTitle("Veículos");
            }
            headerNome.setText(nome);
            headerEmail.setText(actualUser.getEmail());
        }
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
                Intent intent = new Intent(Main2Activity.this, addAuto.class);
                //Bundle b = new Bundle();
                //b.putString("user", actualUser.getUser());
                intent.putExtra("user", actualUser.getUser());
                startActivity(intent);
                // Handle the camera action
            } else if (id == R.id.nav_gallery) {

            } else if (id == R.id.nav_slideshow) {

            } else if (id == R.id.nav_manage) {

            } else if (id == R.id.nav_share) {

            } else if (id == R.id.logout) {
                isLoggedIn = false;
                //Todo: Fechar todos os sockets e bancos de dados.
                openLoginScreen();
                SharedPreferences.Editor editor = user_prefs.edit();
                editor.putBoolean(AUTO_LOGIN, false);
                editor.commit();
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

    public class startTask extends connectionTask{
        @Override
        protected void onProgressUpdate(String... progress) {
            if(progress[0].equals("True")) {
                //getFragmentManager().popBackStack();
                isLoggedIn = true;
                updateLoginInfo(isLoggedIn);
                Toast.makeText(Main2Activity.this, "Bem vindo !", Toast.LENGTH_SHORT).show();
            }else{
                isLoggedIn = false;
                SharedPreferences.Editor editor = user_prefs.edit();
                editor.putBoolean(AUTO_LOGIN, false);
                editor.commit();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                LoginFragment fragment = new LoginFragment();
                fragmentTransaction.add(R.id.container, fragment);
                fragmentTransaction.addToBackStack("login");
                fragmentTransaction.commit();
                //Toast.makeText(getActivity(), "Usuário ou senha incorretos ):", Toast.LENGTH_SHORT).show();
                //pass.setText("");
            }
            // if(progress[0].equals("True"))loginInfo.setText("Logado !");
            //else loginInfo.setText("Usuário ou senha incorretos ):");
        }

    }

    public static class LoginFragment extends Fragment{

        TextView cc;
        Button login;
        EditText user, pass;
        CheckBox remind;
        loginCloseListener listener;
        String sUser;
        //SharedPreferences userPrefs = getActivity().getSharedPreferences("user_prefs", MODE_PRIVATE);

        public void storeUser(String user, boolean auto_login){
            SharedPreferences.Editor editor = user_prefs.edit();
            editor.putString("user", user);
            editor.putBoolean(AUTO_LOGIN, auto_login);
            editor.commit();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup vGroup, Bundle bundle) {
            final View actualView = inflater.inflate(R.layout.login_fragment, vGroup, false);
            cc = (TextView)actualView.findViewById(R.id.criar_conta);
            user = (EditText)actualView.findViewById(R.id.user);
            String storedUser = user_prefs.getString("user", "");
            user.setText(storedUser);
            pass = (EditText)actualView.findViewById(R.id.pass);
            remind = (CheckBox)actualView.findViewById(R.id.checkBox);
            cc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), addUser.class);
                    startActivity(intent);
                    //Criar conta.
                }
            });
            login = (Button)actualView.findViewById(R.id.login);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sUser = user.getText().toString();
                    String sPass = pass.getText().toString();
                    new startTask().execute("192.168.25.18", "1234", "login", sUser, sPass);
                    if(remind.isChecked())storeUser(sUser, remind.isChecked());
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
                if(progress[0].equals("True")) {
                    getFragmentManager().popBackStack();
                    Toast.makeText(getActivity(), "Bem vindo, "+user.getText().toString(), Toast.LENGTH_SHORT).show();
                    actualUser = fetchUser(sUser);
                    updateLoginInfo(true);

                }else{
                    Toast.makeText(getActivity(), "Usuário ou senha incorretos ):", Toast.LENGTH_SHORT).show();
                    pass.setText("");
                }
               // if(progress[0].equals("True"))loginInfo.setText("Logado !");
                //else loginInfo.setText("Usuário ou senha incorretos ):");
            }

        }

    }
}
