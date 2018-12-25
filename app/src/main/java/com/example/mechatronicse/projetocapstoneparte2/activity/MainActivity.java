package com.example.mechatronicse.projetocapstoneparte2.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mechatronicse.projetocapstoneparte2.R;
import com.example.mechatronicse.projetocapstoneparte2.adapter.AdapterAnuncios;
import com.example.mechatronicse.projetocapstoneparte2.constants.Constants;
import com.example.mechatronicse.projetocapstoneparte2.helper.EmptyRecyclerView;
import com.example.mechatronicse.projetocapstoneparte2.data.FirebaseConfig;
import com.example.mechatronicse.projetocapstoneparte2.model.Anuncio;
import com.example.mechatronicse.projetocapstoneparte2.notifications.sync.ReminderUtilities;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.Alpha;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.NUMBER_OFFSET;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.Y;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.ZERO;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.alphaNumber;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.recyclerAnunciosPublicos)
    EmptyRecyclerView recyclerAnunciosPublicos;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.fab_add_post)
    FloatingActionButton fab;
    @BindView(R.id.mEmptyStateTextView)
    TextView textEmpty;
    @BindView(R.id.empty_view)
    TextView textVazio;

    private FirebaseAuth mAuth;
    private AdapterAnuncios adapterAnuncios;
    private List<Anuncio> listAnuncios = new ArrayList<>();
    private DatabaseReference anunciosDatabaseReference;
    private String filterEstado = "";
    private String filterCategoria = "";
    private boolean filterPorEstado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setTitle(getString(R.string.titulo_app));

        mAuth = FirebaseConfig.getFirebaseAutenticacao();
        anunciosDatabaseReference = FirebaseConfig.getFirebase().child(getString(R.string.anuncios_child));

        configButtonFab();
        agendamentosNotifications();
        animateViewsIn();
        configConexao();
    }

    @Override
    protected void onStart() {
        super.onStart();
        configConexao();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        configConexao();
    }

    @Override
    protected void onResume() {
        super.onResume();
        configConexao();
    }

    @SuppressLint("RestrictedApi")
    private void configButtonFab(){
        fab.setVisibility(View.GONE);
        if( mAuth.getCurrentUser() != null ){
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //para no caso de estiver logado
                    startActivity(new Intent(MainActivity.this,MinhaDashboardActivity.class));

                }
            });
        }
    }

    private void agendamentosNotifications(){
        ReminderUtilities.scheduleReminderPosts(this);
        ReminderUtilities.scheduleReminderPostsFor_NoUsers(this);
    }

    private void configRecyclerView(){

        //Configurar RecyclerView
        recyclerAnunciosPublicos.setLayoutManager(new LinearLayoutManager(this));
        recyclerAnunciosPublicos.setEmptyView(findViewById(R.id.empty_view));
         //  adapterAnuncios = new AdapterAnuncios(listAnuncios, this);
        adapterAnuncios = new AdapterAnuncios(listAnuncios,this);
        recyclerAnunciosPublicos.setAdapter( adapterAnuncios );

        recyclerAnunciosPublicos.setHasFixedSize(true);

    }

    //effect de pagina
    private void animateViewsIn() {
        ViewGroup root = (ViewGroup) findViewById(R.id.root);
        int count = root.getChildCount();
        float offset = getResources().getDimensionPixelSize(R.dimen.offset_y);
        Interpolator interpolator = AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in);

        for (int i = ZERO; i < count; i++) {
            View view = root.getChildAt(i);

            view.setTranslationY(offset);
            view.setAlpha(Alpha);
            view.animate()
                    .translationY(Y)
                    .alpha(alphaNumber)
                    .setInterpolator(interpolator)
                    .setDuration(Constants.DURATION)
                    .start();
            offset *= NUMBER_OFFSET;
        }
    }

    private void configConexao(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        //com internet
        if (networkInfo != null && networkInfo.isConnected()) {
            configRecyclerView();
            recuperarAnunciosPublicos();
            progressBar.setVisibility( View.GONE );
            textEmpty.setVisibility(View.GONE);

            //sem internet ou modo aviao
        } else {
            progressBar.setVisibility( View.VISIBLE );
            textEmpty.setVisibility(View.VISIBLE);
            textVazio.setVisibility(View.GONE);
        }


    }

    public void filterEstado(View view){
        AlertDialog.Builder dialogEstado = new AlertDialog.Builder(this);
        dialogEstado.setTitle(getString(R.string.estado_desejado));
        View viewSpinner = getLayoutInflater().inflate(R.layout.dialog_spinner, null);

         final Spinner spinnerEstado = viewSpinner.findViewById(R.id.spinnerFiltro);
        String[] estados = getResources().getStringArray(R.array.estados);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, estados

        );
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinnerEstado.setAdapter( adapter );

        dialogEstado.setView( viewSpinner );
        dialogEstado.setPositiveButton(getString(R.string.tudo_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = null;
                if (connectivityManager != null) {
                    networkInfo = connectivityManager.getActiveNetworkInfo();
                }

                if (networkInfo != null && networkInfo.isConnected()) {
                    filterEstado = spinnerEstado.getSelectedItem().toString();
                    restoreAnunciosPorEstado();
                    filterPorEstado = true;

                } else {
                    Toast.makeText(MainActivity.this, "Sem Conexão com a internet", Toast.LENGTH_SHORT).show();
                }


            }
        });

        dialogEstado.setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = dialogEstado.create();
        dialog.show();

    }

    public void filterCategoria(View view){
        if( filterPorEstado == true ){

            AlertDialog.Builder dialogEstado = new AlertDialog.Builder(this);
            dialogEstado.setTitle(getString(R.string.raca_selecione));

            View viewSpinner = getLayoutInflater().inflate(R.layout.dialog_spinner, null);

            final Spinner spinnerCategoria = viewSpinner.findViewById(R.id.spinnerFiltro);
            String[] estados = getResources().getStringArray(R.array.racas);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item,
                    estados
            );
            adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
            spinnerCategoria.setAdapter( adapter );

            dialogEstado.setView( viewSpinner );

            dialogEstado.setPositiveButton(getString(R.string.tudo_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo networkInfo = null;
                    if (connectivityManager != null) {
                        networkInfo = connectivityManager.getActiveNetworkInfo();
                    }

                    if (networkInfo != null && networkInfo.isConnected()) {
                        filterCategoria = spinnerCategoria.getSelectedItem().toString();
                        restoreAnunciosCategoria();
                    } else {
                        Toast.makeText(MainActivity.this, "Sem Conexão com a internet", Toast.LENGTH_SHORT).show();
                    }




                }
            });

            dialogEstado.setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog dialog = dialogEstado.create();
            dialog.show();

        }else {
            //Força o usuario a escolher a regiao primeiro antes de prosseguir com raça
            Toast.makeText(this, getString(R.string.regiao_primeiro),
                    Toast.LENGTH_SHORT).show();
        }

    }
    public void restoreAnunciosCategoria(){
        //node categoria
        anunciosDatabaseReference = FirebaseConfig.getFirebase()
                .child(getString(R.string.anuncios_child))
                .child(filterEstado)
                .child(filterCategoria);

        anunciosDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listAnuncios.clear();
                for(DataSnapshot anuncios: dataSnapshot.getChildren() ){

                    Anuncio anuncio = anuncios.getValue(Anuncio.class);
                    listAnuncios.add( anuncio );

                }

                Collections.reverse(listAnuncios);
                adapterAnuncios.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void restoreAnunciosPorEstado(){
        //node estado
        anunciosDatabaseReference = FirebaseConfig.getFirebase()
                .child(getString(R.string.anuncios_child))
                .child(filterEstado);

        anunciosDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listAnuncios.clear();
                for (DataSnapshot categorias: dataSnapshot.getChildren() ){
                    for(DataSnapshot anuncios: categorias.getChildren() ){

                        Anuncio anuncio = anuncios.getValue(Anuncio.class);
                        listAnuncios.add( anuncio );
                    }
                }

                Collections.reverse(listAnuncios);
                adapterAnuncios.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void recuperarAnunciosPublicos(){
        anunciosDatabaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot estados: dataSnapshot.getChildren()){
                    for (DataSnapshot categorias: estados.getChildren() ){
                        listAnuncios.clear();
                        for(DataSnapshot anuncios: categorias.getChildren() ){
                            progressBar.setVisibility( View.GONE );
                            Anuncio anuncio = anuncios.getValue(Anuncio.class);
                            listAnuncios.add( anuncio );

                        }
                    }
                }
                Collections.reverse(listAnuncios);
                adapterAnuncios.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if( mAuth.getCurrentUser() == null ){//para caso usuario estiver deslogado
            menu.setGroupVisible(R.id.group_on,false);
            menu.setGroupVisible(R.id.group_off,true);

        }else {
            if( mAuth.getCurrentUser() != null ){//para caso  usuario estiver logado
                menu.setGroupVisible(R.id.group_off,false);
                menu.setGroupVisible(R.id.group_on,true);

            }

        }

        return super.onPrepareOptionsMenu(menu);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ( item.getItemId() ){
            case R.id.menu_cadastrar :
                if( mAuth.getCurrentUser() == null ){ //se estiver deslogado
                startActivity( new Intent(MainActivity.this, CadastroActivity.class));
                }
                break;
            case R.id.menu_sair :
                telaPublica();
                if( mAuth.getCurrentUser() != null ){
                mAuth.signOut();
                    fab.setVisibility(View.INVISIBLE);
                }
                invalidateOptionsMenu();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void telaPublica(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
