package com.example.customlayoutproject;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.customlayoutproject.databinding.ActivityMainBinding;

import org.w3c.dom.Text;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Demigod> {
    Context context;
    int xmlResource;
    List<Demigod> list, viewList;
    String selectedName;
    TextView lifeStatus, charsWhat, bulkInfo;
    ImageView imageView2;
    ListView listView;

//landscape
    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<Demigod> objects, List<Demigod> viewList, String selectedName, TextView lifeStatus, TextView charsWhat, TextView bulkInfo, ImageView imageView2, ListView listView) {
        super(context, resource, objects);
        this.context = context;
        xmlResource = resource;
        list = objects;
        this.selectedName = selectedName;
        this.lifeStatus = lifeStatus;
        this.charsWhat = charsWhat;
        this.bulkInfo = bulkInfo;
        this.imageView2 = imageView2;
        this.viewList = viewList;
        this.listView = listView;
    }

//portrait
    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<Demigod> objects, List<Demigod> viewList, String selectedName, TextView lifeStatus, TextView charsWhat, ListView listView) {
        super(context, resource, objects);
        this.context = context;
        xmlResource = resource;
        list = objects;
        this.selectedName = selectedName;
        this.lifeStatus = lifeStatus;
        this.charsWhat = charsWhat;
        this.viewList = viewList;
        this.listView = listView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View adapterLayout = layoutInflater.inflate(xmlResource, null);

        TextView name = adapterLayout.findViewById(R.id.name);
        ImageView image = adapterLayout.findViewById(R.id.imageView);
        Button remove = adapterLayout.findViewById(R.id.removeButton);

        Log.d("GOINGTHROUGHCUSTOMADAPTER", list.toString());

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("trueorfalse: ", String.valueOf(list.get(position).getName().equals(selectedName)));
                //Log.d("position: ", String.valueOf(position));
                if (selectedName != null)
                    Log.d("selectedName: ", selectedName);
                if (list.get(position).getName().equals(selectedName)) {
                    lifeStatus.setText("Status: ");
                    charsWhat.setText("This person is ...");
                    if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        bulkInfo.setText("Click on a character to find out more about them!");
                        imageView2.setImageResource(R.drawable.pjo_logo);
                    }
                }
                for (Demigod d : viewList) {
                    if (list.get(position).getName().equals(d.getName())) {
                        viewList.remove(d);
                        break;
                    }
                }
                list.remove(position);
                notifyDataSetChanged();

            for (Demigod d: viewList) {
                Log.i("viewList: ", d.getName());
            }
            for(Demigod d: list){
                Log.i("filterList: ", d.getName());
            }
            if (list.size() == 0)
                Log.i("nullList: ", "NULL");
            }
        });


        name.setText(list.get(position).getName());
        image.setImageResource(list.get(position).getImage());
        return adapterLayout;
    }

    public void setSelectedName(String selectedName) {
        this.selectedName = selectedName;
    }

    public void setList(List<Demigod> list) {
        this.list = list;
    }
}

