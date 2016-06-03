package com.melodyxxx.puredaily.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.widget.CircleView;

/**
 * Created by hanjie on 2016/6/3.
 */
public class ColorPickerAdapter extends android.widget.BaseAdapter {

    private Context mContext;

    private int[] mColors;

    public ColorPickerAdapter(Context context, int[] colors) {
        mContext = context;
        mColors = colors;
    }

    @Override
    public int getCount() {
        return mColors.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = View.inflate(mContext, R.layout.item_color_picker, null);
            viewHolder = new ViewHolder();
            viewHolder.circleView = (CircleView) view.findViewById(R.id.circle_view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.circleView.setColor(mColors[position]);
        return view;
    }

    public final class ViewHolder {
        CircleView circleView;
    }
}
