package me.smt.mediaddict.module;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import me.smt.mediaddict.R;
import okhttp3.OkHttpClient;

/**
 * Clase creada para evitar la dependencia de compilación
 * annotationProccessor. Relacionada con Glide.
 * Más información: https://stackoverflow.com/a/49934146/1552146
 */
@GlideModule
public final class TrackerAppGlideModule extends AppGlideModule {

    /**
     * Método que debe ser utilizado si no se utilizan "legacy modules"
     * en el Manifest.
     * @return resultado.
     */
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    /**
     * Método que registra un conjunto de componentes a utilizar
     * cuando se inicializa Glide utilizando annotation processor.
     * @param context contexto de la aplicación.
     * @param glide instancia de Glide.
     * @param registry controla el registro de componentes.
     */
    @Override
    public void registerComponents(@NotNull Context context, @NotNull Glide glide, @NotNull Registry registry) {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build();

        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory(client);

        glide.getRegistry().replace(GlideUrl.class, InputStream.class, factory);
    }

    /**
     * Método que se encarga de aplicar unas opciones predeterminadas a todas las
     * llamadas que se hagan sobre GlideApp.
     * @param context el contexto de la aplicación.
     * @param builder define la estructura clases por defecto a usar por Glide.
     */
    @Override
    public void applyOptions(@NotNull Context context, @NotNull GlideBuilder builder) {
        super.applyOptions(context, builder);
        builder.setDefaultRequestOptions(new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.color.text_light_blue)
                .transform(new CenterCrop())
                .placeholder(R.color.colorPrimary));
    }
}