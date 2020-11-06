package me.sergiomartin.tvshowmovietracker.moviesModule.module;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import me.sergiomartin.tvshowmovietracker.R;

/**
 * Class just to avoid annotationProccessor compile dependency
 * SO URL: https://stackoverflow.com/a/49934146/1552146
 */
@GlideModule
public final class TrackerAppGlideModule extends AppGlideModule {

    //Below override should only be used if not using legacy modules registered via manifest
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void applyOptions(@NotNull Context context, @NotNull GlideBuilder builder) {
        super.applyOptions(context, builder);
        builder.setDefaultRequestOptions(new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new CenterCrop())
                .placeholder(R.color.colorPrimary));
    }
}