package com.example.customlayoutproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.customlayoutproject.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    List<Demigod> masterList, viewList, filterList;
    List<String> topicList, sortFilterList;
    int selectedIndex = -1;
    String selectedName, selectedTopic, selectedFilter;
    CustomAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        masterList = new ArrayList<Demigod>();
        masterList = populateMasterList();

        viewList = new ArrayList<Demigod>();
        viewList.addAll(masterList);

        filterList = new ArrayList<Demigod>();
        filterList.addAll(viewList);

        if (savedInstanceState != null){
            viewList = savedInstanceState.getParcelableArrayList("viewList");
            selectedTopic = savedInstanceState.getString("selected topic");
            selectedFilter = savedInstanceState.getString("selected filter");
            sortFilterList = savedInstanceState.getStringArrayList("filter topic list");
            filterList = savedInstanceState.getParcelableArrayList("filterList");
            if (filterList == null){Log.d("null: ", "TRUE");}
            selectedIndex = savedInstanceState.getInt("selected index");
            selectedName = savedInstanceState.getString("selected name");
        }

        if (selectedIndex > -1){
            try {
                if (filterList.get(selectedIndex).getName().equals(selectedName))
                    itemClicked(filterList, selectedIndex);
            }
            catch (IndexOutOfBoundsException e){ }
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            customAdapter = new CustomAdapter(this, R.layout.list_adapter, filterList, viewList, selectedName, binding.lifeStatus, binding.charsWhat, binding.listView);
        else
            customAdapter = new CustomAdapter(this, R.layout.list_adapter, filterList, viewList, selectedName, binding.lifeStatus, binding.charsWhat, binding.bulkInfo, binding.imageView2, binding.listView);
        binding.listView.setAdapter(customAdapter);

        topicList = new ArrayList<String>();
        topicList.add("");
        topicList.add("Location");
        topicList.add("Type");
        topicList.add("Status");
        topicList.add("Age");
        topicList.add("Opinion");
        ArrayAdapter<String> topicListAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,topicList);
        binding.spinner1.setAdapter(topicListAdapter);

        sortFilterList = new ArrayList<String>();
        sortFilterList.add("");
        ArrayAdapter<String> sortFilterListAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,sortFilterList);
        binding.spinner2.setAdapter(sortFilterListAdapter);

        try {
            Log.i("SELECTED TOPIC", selectedTopic);
            Log.i("SELECTED FILTER", selectedFilter);
        }catch(NullPointerException e){e.printStackTrace();}

        if (savedInstanceState != null && selectedFilter != null && selectedTopic != null) {

            fillSortFilterList(topicList.indexOf(selectedTopic));
            setFilterList();
            binding.spinner1.setSelection(topicList.indexOf(selectedTopic));
            binding.spinner2.setSelection(sortFilterList.indexOf(selectedFilter));
        }

        binding.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTopic = topicList.get(i);
                sortFilterList = fillSortFilterList(i);
                sortFilterListAdapter.notifyDataSetChanged();
                //binding.spinner2.setAdapter(sortFilterListAdapter);
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //filterList.addAll(viewList);
            }
        });

        binding.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedFilter = sortFilterList.get(i);
                if (selectedTopic != null) {
                    setFilterList();
                    customAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //filterList.addAll(viewList);
            }
        });
        customAdapter.notifyDataSetChanged();

        /*for (String s: sortFilterList) {
            Log.d("sortFilterList: ", s);
        }
        Log.d("length of sort filter list: ", String.valueOf(sortFilterList.size()));*/

        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedIndex = i;
                selectedName = filterList.get(i).getName();
                customAdapter.setSelectedName(selectedName);
                itemClicked(filterList, i);
                /*for (Demigod d: viewList) {
                    Log.i("viewList: ", d.getName());
                }
                for(Demigod d: filterList){
                    Log.i("filterList: ", d.getName());
                }*/
            }
        });

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewList.clear();
                filterList.clear();
                viewList.addAll(masterList);
                filterList.addAll(viewList);
                customAdapter.notifyDataSetChanged();
                binding.spinner1.setSelection(0);
                binding.spinner2.setSelection(0);
            }
        });

        //Log.d("VIEWLISTCONTENTS", viewList.toString());
        //Log.d("FILTERLISTCONTENTS", filterList.toString());
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("viewList",(ArrayList)viewList);
        outState.putParcelableArrayList("filterList", (ArrayList)filterList);
        outState.putInt("selected index", selectedIndex);
        outState.putString("selected name", selectedName);
        outState.putString("selected topic", selectedTopic);
        outState.putString("selected filter", selectedFilter);
        outState.putStringArrayList("filter topic list", (ArrayList)sortFilterList);
    }

    public List<Demigod> populateMasterList(){
        List<Demigod> list = new ArrayList<Demigod>();
        try{
            String json;
            InputStream is = this.getAssets().open("demigods.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            json = new String(buffer);
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("demigods");
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                String name = obj.getString("name");
                String parent = obj.getString("parent");
                String camp = obj.getString("camp");
                String species = obj.getString("species");
                boolean alive = obj.getBoolean("alive");
                int age = obj.getInt("age");
                String so = obj.getString("so");
                String opinion = obj.getString("opinion");
                int image = getResources().getIdentifier(getApplicationContext().getPackageName()+":drawable/"+obj.getString("image"), null, null);
                list.add(new Demigod(name, parent, camp, species, alive, age, so, opinion, image));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public String setInfo(List<Demigod> list, int index){
        String para = "";
    //name
        para += "Name: " +list.get(index).getName();
    //age
        para += "\nAge: ";
        if (list.get(index).getAge() == 0)
            para += "Unknown";
        else if (list.get(index).getAge() >= 2000)
            para += "over " +list.get(index).getAge();
        else if (list.get(index).getCamp().equals("Hunters of Artemis"))
            para += list.get(index).getAge() + " (before joining)";
        else
            para += list.get(index).getAge();
        if (!list.get(index).getAlive())
            para += " (at death)";
    //godly parent
        para += "\nParents: ";
        if (list.get(index).getParent().equals("N/A"))
            para += "No one of significance";
        else
            para += list.get(index).getParent();
    //where they reside
        para += "\nResidence: ";
        if (list.get(index).getCamp().equals("N/A"))
            para += "Unknown";
        else
            para += list.get(index).getCamp();
        if (!list.get(index).getAlive())
            para += " (at death)";
    //significant other
        para += "\nSignificant Other: " +list.get(index).getSo();
    //personal opinion
        para += "\nMy Opinion: ";
        if (list.get(index).getOpinion().equals("fav"))
            para += "I absolutely adore this character. They are extremely fun to read about. One of my favorites.";
        else if (list.get(index).getOpinion().equals("darling"))
            para += "Sally Jackson is fantastic. We love her. She deserves the world. Poseidon, give her the world.";
        else if (list.get(index).getOpinion().equals("great"))
            para += "This is a great character who doesn't get much love or development but is fun to read about nonetheless.";
        else if (list.get(index).getOpinion().equals("pretty good"))
            para += "This is a pretty good character. I'm not particularly attached to them, but they are nice as a side character.";
        else if (list.get(index).getOpinion().equals("okay"))
            para += "This is an okay character. They aren't very noteworthy but they aren't boring either. They just exist.";
        else if (list.get(index).getOpinion().equals("boring"))
            para += "This is a character that is pretty boring to read about. They may be good in small doses, but they get quite boring over time.";
        else
            para += "I have not specified my feelings about this character as of yet.";
        return para;
    }

    public void itemClicked(List<Demigod> list, int index){
        if (list.get(index).getAlive())
            binding.lifeStatus.setText("Status: Alive");
        else
            binding.lifeStatus.setText("Status: Dead");

        String what = "";
        switch (list.get(index).getSpecies()){
            case "g": what = "a greek demigod";
                break;
            case "r": what = "a roman demigod";
                break;
            case "m": what = "a mortal";
                break;
            case "i": what = "an immortal";
                break;
            case "s": what = "a satyr";
                break;
            case "h": what = "a semi-immortal hunter";
                break;
            default: what = "not in the pjo/hoo universe";
        }

        binding.charsWhat.setText("This person is " +what+ ".");

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            if(list.get(index).getBulkInfo() == null)
                binding.bulkInfo.setText(setInfo(list,index));
            else
                binding.bulkInfo.setText(list.get(index).getBulkInfo());
            binding.imageView2.setImageResource(list.get(index).getImage());
        }
    }

    public List<String> fillSortFilterList(int i){
        sortFilterList.clear();
        if (i == 0) {
            sortFilterList.add("");
            filterList.clear();
            filterList.addAll(viewList);
            customAdapter.notifyDataSetChanged();
        }
        else {
            switch(selectedTopic) {
                case "Location":
                    sortFilterList.add("");
                    for (Demigod d : viewList) {
                        if (!sortFilterList.contains(d.getCamp()) && !d.getCamp().contains("and"))
                            sortFilterList.add(d.getCamp());
                    }
                    break;
                case "Type":
                    sortFilterList.add("");
                    sortFilterList.add("Greek Demigod");
                    sortFilterList.add("Roman Demigod");
                    sortFilterList.add("Mortal");
                    sortFilterList.add("Immortal");
                    sortFilterList.add("Satyr");
                    sortFilterList.add("Semi-Immortal");
                    break;
                case "Status":
                    sortFilterList.add("");
                    sortFilterList.add("Alive");
                    sortFilterList.add("Dead");
                    break;
                case "Age":
                    sortFilterList.add("");
                    for (Demigod d : viewList) {
                        if ((d.getAge() == 0 || d.getAge() > 100) && !sortFilterList.contains("Old"))
                            sortFilterList.add("Old");
                        else if (!sortFilterList.contains(String.valueOf(d.getAge())) && !sortFilterList.contains("Old"))
                            sortFilterList.add(String.valueOf(d.getAge()));
                    }
                    Collections.sort(sortFilterList);
                    break;
                case "Opinion":
                    sortFilterList.add("");
                    sortFilterList.add("Love Them!");
                    sortFilterList.add("Great Character!");
                    sortFilterList.add("They're Okay");
                    sortFilterList.add("Not A Fan");
                    sortFilterList.add("Please Die");
                    break;
            }
        }
        return sortFilterList;
    }

    public void setFilterList(){
        filterList.clear();
        switch (selectedTopic){
            case "":
                filterList.addAll(viewList);
                break;
            case "Location":
                Log.d("CHECKSELECTEDFILTER", this.selectedFilter);
                for (Demigod d: viewList){
                    if (d.getCamp().contains(selectedFilter)) {
                        Log.d("CHECKLOCATION", d.getCamp());
                        filterList.add(d);
                    }
                }
                break;
            case "Type":
                for (Demigod d: viewList){
                    String type = d.getSpecies();
                    if (type.equals("g") && selectedFilter.equals("Greek Demigod"))
                        filterList.add(d);
                    if (type.equals("r") && selectedFilter.equals("Roman Demigod"))
                        filterList.add(d);
                    if (type.equals("m") && selectedFilter.equals("Mortal"))
                        filterList.add(d);
                    if (type.equals("i") && selectedFilter.equals("Immortal"))
                        filterList.add(d);
                    if (type.equals("s") && selectedFilter.equals("Satyr"))
                        filterList.add(d);
                    if (type.equals("h") && selectedFilter.equals("Semi-Immortal"))
                        filterList.add(d);
                }
                break;
            case "Status":
                for (Demigod d: viewList){
                    if (d.getAlive() && selectedFilter.equals("Alive"))
                        filterList.add(d);
                    if (!d.getAlive() && selectedFilter.equals("Dead"))
                        filterList.add(d);
                }
                break;
            case "Age":
                for (Demigod d: viewList){
                    if ((d.getAge() == 0 || d.getAge() > 100) && selectedFilter.equals("Old"))
                        filterList.add(d);
                    else if (String.valueOf(d.getAge()).equals(selectedFilter))
                        filterList.add(d);
                }
                break;
            case "Opinion":
                for (Demigod d: viewList){
                    String op = d.getOpinion();
                    if ((op.equals("fav") || op.equals("darling")) && selectedFilter.equals("Love Them!"))
                        filterList.add(d);
                    else if (op.equals("great") && selectedFilter.equals("Great Character!"))
                        filterList.add(d);
                    else if ((op.equals("pretty good") || op.equals("okay")) && selectedFilter.equals("They're Okay"))
                        filterList.add(d);
                    else if (op.equals("boring") && selectedFilter.equals("Not A Fan"))
                        filterList.add(d);
                    else if (op.equals("die") && selectedFilter.equals("Please Die"))
                        filterList.add(d);
                }
                break;
        }
    }
}