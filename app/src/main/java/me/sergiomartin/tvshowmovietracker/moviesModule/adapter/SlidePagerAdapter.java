package me.sergiomartin.tvshowmovietracker.moviesModule.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import butterknife.BindView;
import me.sergiomartin.tvshowmovietracker.R;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Slide;

public class SlidePagerAdapter extends PagerAdapter {

    @BindView(R.id.slide_item_pic_imageview)
    ImageView slideItemPicImageview;
    @BindView(R.id.slide_item_title_textview)
    TextView slideItemTitleTextview;

    private Context mContext;
    private List<Slide> mSlideList;

    public SlidePagerAdapter(Context mContext, List<Slide> mSlideList) {
        this.mContext = mContext;
        this.mSlideList = mSlideList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View slideLayoutView = inflater.inflate(R.layout.slide_item, null);

        slideItemPicImageview = slideLayoutView.findViewById(R.id.slide_item_pic_imageview);
        slideItemTitleTextview = slideLayoutView.findViewById(R.id.slide_item_title_textview);

        slideItemPicImageview.setImageResource(mSlideList.get(position).getImage());
        slideItemTitleTextview.setText(mSlideList.get(position).getTitle());

        container.addView(slideLayoutView);

        return slideLayoutView;
    }

    @Override
    public int getCount() {
        return mSlideList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
