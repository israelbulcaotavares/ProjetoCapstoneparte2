package com.example.mechatronicse.projetocapstoneparte2.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mechatronicse.projetocapstoneparte2.R;
import com.example.mechatronicse.projetocapstoneparte2.constants.Constants;
import com.example.mechatronicse.projetocapstoneparte2.data.AnuncioContract;
import com.example.mechatronicse.projetocapstoneparte2.model.Anuncio;
import com.example.mechatronicse.projetocapstoneparte2.widget.AnuncioWidgetService;
import com.google.firebase.auth.FirebaseAuth;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.Alpha;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.NUMBER_OFFSET;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.Y;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.ZERO;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.alphaNumber;

public class DetalhesActivity extends AppCompatActivity {
    @BindView(R.id.textTituloDetalhe)
    TextView titulo;
    @BindView(R.id.textDescricaoDetalhe)
    TextView descricao;
    @BindView(R.id.textPrecoDetalhe)
    TextView preco;
    @BindView(R.id.textEstadoDetalhe)
    TextView estado;
    @BindView(R.id.favorite_image)
    FloatingActionButton favorite;
    @BindView(R.id.favorite_image_no)
    FloatingActionButton no_favorite;
    @BindView(R.id.progressBar2)
    ProgressBar progressBar2;
    @BindView(R.id.carouselView)
    CarouselView carouselView;

    private Anuncio anuncio;
    FirebaseAuth auth;
    Uri mUri;

     @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
         ButterKnife.bind(this);

         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
          toolbar.setTitle(getString(R.string.detalhes_activity));
         setSupportActionBar(toolbar);
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         auth = FirebaseAuth.getInstance();

         recuperarItens();
         ativarBotoesFavorites();
          animateViewsIn();
         new AnuncioDetalhesAsynkTask().execute();
     }

     private void recuperarItens(){
         anuncio = (Anuncio) getIntent().getSerializableExtra(getString(R.string.serializable));

         if( anuncio != null ){
             titulo.setText( anuncio.getTitulo() );
             descricao.setText( anuncio.getDescricao() );
             estado.setText( anuncio.getEstado() );
             preco.setText( anuncio.getValor());

             ImageListener imageListener = new ImageListener() {
                 @Override
                 public void setImageForPosition(int position, ImageView imageView) {
                     String urlString = anuncio.getFotos().get( position );

                     progressBar2.setVisibility(View.VISIBLE);
                     Glide.with(getApplicationContext())
                             .load(urlString).placeholder(android.R.drawable.ic_menu_report_image)
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
                                     progressBar2.setVisibility(View.GONE);
                                     return false;
                                 }
                             })
                             .into(imageView);

                 }
             };

             carouselView.setPageCount( anuncio.getFotos().size() );
             carouselView.setImageListener( imageListener );

         }
     }


     private void ativarBotoesFavorites(){
         favorite.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(final View v) {

                 ContentValues contentValues = new ContentValues();
                 contentValues.put(AnuncioContract.AnuncioEntry.COLUMN_ANUNCIO_ID, anuncio.getIdAnuncio());
                 contentValues.put(AnuncioContract.AnuncioEntry.COLUMN_TELEFONE, anuncio.getTelefone());

                 mUri = getContentResolver().insert(AnuncioContract.AnuncioEntry.CONTENT_URI, contentValues);

                 //Toast.makeText(DetalhesActivity.this, "Salvo em sua Widget", Toast.LENGTH_SHORT).show();
                 Snackbar.make( v,getString(R.string.salvo_widget), Snackbar.LENGTH_LONG)
                         .setAction(getString(R.string.action), null).show();
                 removeButtonFavoriteVisible();
                 updateWidget();

             }
         });

         no_favorite.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 mUri= AnuncioContract.AnuncioEntry.CONTENT_URI.buildUpon().appendPath(anuncio.getIdAnuncio()).build();
                 getContentResolver().delete(mUri, null, null);
                 buttonFavoriteVisible();
                 //  Toast.makeText(DetalhesActivity.this, "Desmarcado da sua Widget", Toast.LENGTH_SHORT).show();
                 Snackbar.make( v, getString(R.string.desmarcado_favoritos), Snackbar.LENGTH_LONG)
                         .setAction(getString(R.string.action), null).show();
                 updateWidget();
             }
         });

     }

    @Override
    public void onBackPressed() {
        telaMain();
    }

    private void telaMain(){
         Intent intent = new Intent(DetalhesActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
         startActivity(intent);
    }



    private void animateViewsIn() {
        ViewGroup root =   findViewById(R.id.toolbar_layout);
        int count = root.getChildCount();
        float offset = getResources().getDimensionPixelSize(R.dimen.offset_y);
         Interpolator interpolator = AnimationUtils.loadInterpolator(this, android.R.interpolator.anticipate_overshoot);

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

    private void updateWidget() {
        AnuncioWidgetService.startActionUpdateWidget(this);
    }

    public void visualizarTelefone(View view){
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", anuncio.getTelefone(), null ));
        startActivity( i );
    }

    public void enviarEmail(View view){
        String email = anuncio.getEmail();
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse(getString(R.string.mail_to)));
        intent.putExtra(Intent.EXTRA_EMAIL  , new String[] { email});
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.meu_assunto));
        startActivity(Intent.createChooser(intent, getString(R.string.email_via)));

    }

    @SuppressLint("StaticFieldLeak")
    private  class AnuncioDetalhesAsynkTask extends AsyncTask<Void, Void, Void> {
        private boolean isFavoritesList;
        @Override
        protected Void doInBackground(Void... params) {
            isFavoritesList = queryFavorites();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (isFavoritesList) {
                removeButtonFavoriteVisible();
            } else {
                buttonFavoriteVisible();
            }

        }
    }

    private boolean queryFavorites() {
        Integer countsOfCursor;
        Cursor cursor = getContentResolver().query(AnuncioContract.AnuncioEntry.CONTENT_URI,
                null,
                AnuncioContract.AnuncioEntry.COLUMN_ANUNCIO_ID + getString(R.string.query),
                new String[]{anuncio.getIdAnuncio()},
                null
        );
        countsOfCursor = cursor.getCount();
        cursor.close();
        return countsOfCursor > 0;
    }

    @SuppressLint("RestrictedApi")
    private void buttonFavoriteVisible() {
        favorite.setVisibility(View.VISIBLE);
        no_favorite.setVisibility(View.GONE);
    }

    @SuppressLint("RestrictedApi")
    private void removeButtonFavoriteVisible() {
        favorite.setVisibility(View.GONE);
        no_favorite.setVisibility(View.VISIBLE);
    }

}
