package cs2340.bob_over_troubled_waters.homelessshelterapplication.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

import cs2340.bob_over_troubled_waters.homelessshelterapplication.R;
import cs2340.bob_over_troubled_waters.homelessshelterapplication.model.Shelter;
import cs2340.bob_over_troubled_waters.homelessshelterapplication.model.enums.AgeRanges;
import cs2340.bob_over_troubled_waters.homelessshelterapplication.model.enums.Gender;

/**
 * Created by Francine on 2/21/2018.
 */

public class ShelterListingActivity extends AppCompatActivity {
    private static ArrayList<Shelter> shelters = new ArrayList<Shelter>();
    private ListView shelterListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shelters.clear();
        shelters.addAll(Shelter.getShelters());

        narrowResults();

        setContentView(R.layout.activity_shelter_listing);
        shelterListView = (ListView) findViewById(R.id.shelter_listing);
        ArrayAdapter<Shelter> arrayAdapter = new ArrayAdapter<Shelter>(this,
                android.R.layout.simple_list_item_1, shelters);
        shelterListView.setAdapter(arrayAdapter);
        shelterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getBaseContext(), ShelterPage.class);
                        intent.putExtra("position", i);
                        System.out.println("About to open ShelterPage");
                        startActivity(intent);
                        System.out.println("Started new intent");
                    }
                });
    }

    public static ArrayList<Shelter> getShelters() {
        return shelters;
    }

    public void backButtonAction(View view) {
        finish();
    }

    /**
     * if we're searching for shelters in a particular category
     */
    private void narrowResults() {
        HashSet<Shelter> narrowed = new HashSet<>();
        boolean genderNarrowed = false;
        boolean ageNarrowed = false;
        String searchString = ShelterSearch.getSearchString();
        System.out.println(ShelterSearch.getGenderCriteria());
        if (ShelterSearch.getGenderCriteria() != null) {
            genderNarrowed = true;
            for (Gender gender : ShelterSearch.getGenderCriteria()) {
                if (searchString != null) {
                    for (Shelter shelter : gender.getShelters()) {
                        if (shelter.toString().toLowerCase().contains(searchString)) {
                            narrowed.add(shelter);
                        }
                    }
                } else {
                    narrowed.addAll(gender.getShelters());
                }
            }
        }
        System.out.println(ShelterSearch.getAgeCriteria());
        if (ShelterSearch.getAgeCriteria() != null) {
            ageNarrowed = true;
            if (!genderNarrowed) {
                for (AgeRanges range : ShelterSearch.getAgeCriteria()) {
                    if (searchString != null) {
                        for (Shelter shelter : range.getShelters()) {
                            if (shelter.toString().toLowerCase().contains(searchString)) {
                                narrowed.add(shelter);
                            }
                        }
                    } else {
                        narrowed.addAll(range.getShelters());
                    }
                }
            } else {
                ArrayList<Shelter> temp = new ArrayList<>();
                for (AgeRanges range : ShelterSearch.getAgeCriteria()) {
                    for (Shelter shelter : range.getShelters()) {
                        if (narrowed.contains(shelter)) {
                            temp.add(shelter);
                        }
                    }
                }
                narrowed.clear();
                narrowed.addAll(temp);
            }
        }
        if (genderNarrowed || ageNarrowed) {
            shelters.clear();
            shelters.addAll(narrowed);
        } else if (searchString != null) {
            for (Shelter shelter : shelters) {
                if (shelter.toString().toLowerCase().contains(searchString)) {
                    narrowed.add(shelter);
                }
                shelters.clear();
                shelters.addAll(narrowed);
            }
        }
    }
}
