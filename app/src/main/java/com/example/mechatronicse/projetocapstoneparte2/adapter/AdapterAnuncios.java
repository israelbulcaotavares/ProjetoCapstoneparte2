package com.example.mechatronicse.projetocapstoneparte2.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mechatronicse.projetocapstoneparte2.R;
import com.example.mechatronicse.projetocapstoneparte2.activity.DetalhesActivity;
import com.example.mechatronicse.projetocapstoneparte2.model.Anuncio;
import com.google.firebase.auth.FirebaseAuth;
import java.util.List;

public class AdapterAnuncios extends RecyclerView.Adapter<AdapterAnuncios.MyViewHolder> {
    private List<Anuncio> listaAnuncios;
    private FirebaseAuth autenticacao;
    private Context host;
    private LayoutInflater inflater;
    public static  final  String EXTRA = "anunciosPet";

    public AdapterAnuncios(List<Anuncio> listaAnuncios, Activity activity) {
        this.listaAnuncios = listaAnuncios;
        host = activity;
        inflater = LayoutInflater.from(host);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyViewHolder(inflater.inflate(R.layout.adapter_anuncio, parent, false));
     }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final Anuncio anuncio = listaAnuncios.get(position);

        holder.titulo.setText( anuncio.getCategoria() );
        holder.valor.setText( anuncio.getEstado() );

        List<String> urlFotos = anuncio.getFotos();
        String urlCapa = urlFotos.get(0);
        autenticacao = FirebaseAuth.getInstance();

        holder.progressBar.setVisibility(View.VISIBLE);

        Glide.with(host)
                .load(urlCapa).placeholder(android.R.drawable.ic_menu_report_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(200,200)
                .dontAnimate()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.foto);

        holder.foto.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                if(autenticacao.getCurrentUser() == null){
                     Snackbar.make(v, "Cadastre-se para mais detalhes", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{

                    Intent i = new Intent(host, DetalhesActivity.class);
                    i.putExtra(EXTRA, anuncio );
                    host.startActivity( i );

                }

            }
        });



    }


    @Override
    public int getItemCount() {
        return listaAnuncios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titulo;
        TextView valor;
        ImageView foto;
        ProgressBar progressBar;

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public MyViewHolder(View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.textTitulo);
            valor  = itemView.findViewById(R.id.textPreco);
            foto   = itemView.findViewById(R.id.imageAnuncio);
            progressBar   = itemView.findViewById(R.id.progressBar_anuncio);

        }

    }

}
