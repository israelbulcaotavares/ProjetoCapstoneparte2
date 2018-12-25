package com.example.mechatronicse.projetocapstoneparte2.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import com.example.mechatronicse.projetocapstoneparte2.R;
import com.example.mechatronicse.projetocapstoneparte2.activity.DetalhesActivity;
import com.example.mechatronicse.projetocapstoneparte2.adapter.AdapterAnuncios;
import com.example.mechatronicse.projetocapstoneparte2.data.FirebaseConfig;
import com.example.mechatronicse.projetocapstoneparte2.helper.RecyclerItemClickListener;
import com.example.mechatronicse.projetocapstoneparte2.model.Anuncio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MeusAnunciosFragment extends Fragment {
    @BindView(R.id.recyclerAnuncios)
    RecyclerView recyclerAnuncios;

    private List<Anuncio> listaAnuncios = new ArrayList<>();
    private AdapterAnuncios adapterAnuncios;
    private DatabaseReference anuncioUsuarioDatabaseReferece;
    private FirebaseUser user;
    FirebaseAuth auth;
    private String userId;
    public static final String EXTRA_DADOS = "anunciosPet";

    public MeusAnunciosFragment() {

     }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meus_anuncios, container, false);
        ButterKnife.bind(this,view);

        configFirebases();
        configRecyclerView();
        restoreAnuncios();
        clickEventRecyclerView();
         return view;
    }


    private void configFirebases(){
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        assert user != null;

        userId = auth.getCurrentUser().getUid();
        anuncioUsuarioDatabaseReferece = FirebaseConfig.getFirebase()
                .child(getString(R.string.meus_anuncios))
                .child( userId);

    }

    private void visualizarDetalhes(int position){
        final Anuncio anuncio = listaAnuncios.get(position);
        Intent i = new Intent(getActivity(), DetalhesActivity.class);
        i.putExtra(EXTRA_DADOS, anuncio );
         startActivity( i );

    }

    private void removerAnuncio(int position){
        Anuncio anuncioSelecionado = listaAnuncios.get(position);
        anuncioSelecionado.remove();
        adapterAnuncios.notifyDataSetChanged();
        Toast.makeText(getContext(), getString(R.string.removido), Toast.LENGTH_SHORT).show();
    }

    private void clickEventRecyclerView(){
        recyclerAnuncios.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getContext(),
                        recyclerAnuncios,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                visualizarDetalhes( position);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                removerAnuncio( position);
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );
    }

    private void configRecyclerView(){
        recyclerAnuncios.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerAnuncios.setHasFixedSize(true);
        adapterAnuncios = new AdapterAnuncios(listaAnuncios,getActivity());
        recyclerAnuncios.setAdapter( adapterAnuncios );
    }


    private void restoreAnuncios(){
        anuncioUsuarioDatabaseReferece.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaAnuncios.clear();
                for ( DataSnapshot ds : dataSnapshot.getChildren() ){
                    listaAnuncios.add( ds.getValue(Anuncio.class) );
                }

                Collections.reverse( listaAnuncios );
                adapterAnuncios.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
