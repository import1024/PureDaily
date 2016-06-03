package com.melodyxxx.puredaily.ui.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.adapter.ColorPickerAdapter;
import com.melodyxxx.puredaily.constant.PrefConstants;
import com.melodyxxx.puredaily.utils.CommonUtils;
import com.melodyxxx.puredaily.utils.PrefUtils;

/**
 * Created by hanjie on 2016/6/3.
 */
public class ColorPickerDialogFragment extends DialogFragment {

    private GridView mGridView;

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_color_picker, container, false);
        mGridView = (GridView) view.findViewById(R.id.grid_view);
        mGridView.setAdapter(new ColorPickerAdapter(mContext, getColors()));
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PrefUtils.putInt(mContext, PrefConstants.CURRENT_SKIN, position);
                dismiss();
                CommonUtils.restartApp(mContext);
            }
        });
        return view;
    }

    private int[] getColors() {
        Resources res = getResources();
        return new int[]{
                res.getColor(R.color.material_blue),
                res.getColor(R.color.material_light_blue),
                res.getColor(R.color.material_pink),
                res.getColor(R.color.material_red),
                res.getColor(R.color.material_purple),
                res.getColor(R.color.material_deep_purple),
                res.getColor(R.color.material_teal),
                res.getColor(R.color.material_deep_orange),
                res.getColor(R.color.material_green),
                res.getColor(R.color.material_cyan),
                res.getColor(R.color.material_orange),
                res.getColor(R.color.material_indigo),
                res.getColor(R.color.material_brown),
                res.getColor(R.color.material_blue_gray),
                res.getColor(R.color.material_amber),
        };
    }

}
