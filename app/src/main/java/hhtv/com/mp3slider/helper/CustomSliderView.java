package hhtv.com.mp3slider.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;

import hhtv.com.mp3slider.R;

/**
 * Created by nienb on 12/4/16.
 */
public class CustomSliderView extends BaseSliderView {
    public CustomSliderView(Context context) {
        super(context);
    }
    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.custom_slide_view, null);
        ImageView target = (ImageView)v.findViewById(R.id.custom_slide_view_image);
        bindEventAndShow(v, target);
        return v;
    }
}
