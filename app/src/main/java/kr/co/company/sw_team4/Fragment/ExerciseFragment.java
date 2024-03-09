package kr.co.company.sw_team4.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import kr.co.company.sw_team4.DB.ExerciseDB;
import kr.co.company.sw_team4.DB.ExerciseListAdapter;
import kr.co.company.sw_team4.DB.ExerciseListAdapterData;
import kr.co.company.sw_team4.Activity.ExerciseDetailActivity;
import kr.co.company.sw_team4.R;

public class ExerciseFragment extends Fragment {

    SearchView searchView;
    ListView listView;
    ExerciseListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exercise, container, false);

        ExerciseDB helper = new ExerciseDB(getActivity());
        SQLiteDatabase database = helper.getWritableDatabase();
        adapter = new ExerciseListAdapter();
        listView = v.findViewById(R.id.list_exercise);

        Cursor cursor = database.rawQuery("select * from Exercise",null);
        while(cursor.moveToNext()){
            adapter.addItemToList(cursor.getString(1), cursor.getString(2), cursor.getString(3));
        }
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ExerciseListAdapterData exerciseListAdapterData = (ExerciseListAdapterData) listView.getItemAtPosition(position);
                Intent showDetail_exercise = new Intent(getActivity(), ExerciseDetailActivity.class);
                showDetail_exercise.putExtra("name", exerciseListAdapterData.getName());

                startActivity(showDetail_exercise);
            }
        });

        searchView = v.findViewById(R.id.search_exercise);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                ((ExerciseListAdapter)listView.getAdapter()).getFilter().filter(newText);
                return false;
            }
        });

        return v;
    }
}