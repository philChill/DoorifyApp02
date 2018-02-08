package philipp.doorifyapp;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import philipp.doorifyapp.R;
import philipp.doorifyapp.mainListViewDataModel;

/**
 * Created by Carlo on 13.12.2017.
 */

public class mainListViewCustomAdapter extends ArrayAdapter<mainListViewDataModel> {

    private ArrayList<mainListViewDataModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtZugriff;
        ImageView info;
    }

    public mainListViewCustomAdapter(ArrayList<mainListViewDataModel> data, Context context) {
        super(context, R.layout.mainlistviewadapterview, data);
        this.dataSet = data;
        this.mContext=context;
    }

    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        mainListViewDataModel dataModel=(mainListViewDataModel) object;

        switch (v.getId())
        {
            case R.id.ml_name:
                Snackbar.make(v, "Release date " + dataModel.getName(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        mainListViewDataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.mainlistviewadapterview, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.ml_name);
            viewHolder.txtZugriff = (TextView) convertView.findViewById(R.id.ml_zugriff);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.imageView);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtZugriff.setText(dataModel.getZugriff());
        // Return the completed view to render on screen
        return convertView;
    }
}

