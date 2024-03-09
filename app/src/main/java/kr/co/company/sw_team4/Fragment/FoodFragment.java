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

import kr.co.company.sw_team4.Activity.FoodDetailActivity;
import kr.co.company.sw_team4.DB.FoodDB;
import kr.co.company.sw_team4.DB.FoodListAdapter;
import kr.co.company.sw_team4.DB.FoodListAdapterData;
import kr.co.company.sw_team4.R;

public class FoodFragment extends Fragment {

    SearchView searchView;
    ListView listView;
    FoodListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_food, container, false);

        FoodDB helper = new FoodDB(getActivity());
        SQLiteDatabase database = helper.getWritableDatabase();
        adapter = new FoodListAdapter();
        listView = v.findViewById(R.id.list_item);

        Cursor cursor = database.rawQuery("select * from Food",null);
        while(cursor.moveToNext()){
            adapter.addItemToList(cursor.getString(1), cursor.getString(3));
        }
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                FoodListAdapterData foodData = (FoodListAdapterData) listView.getItemAtPosition(position);
                Intent showDetail_food = new Intent(getActivity(), FoodDetailActivity.class);
                showDetail_food.putExtra("name", foodData.getName());
                startActivity(showDetail_food);
            }
        });

        // 검색
        searchView = v.findViewById(R.id.search_food);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ((FoodListAdapter)listView.getAdapter()).getFilter().filter(newText);
                return false;
            }
        });
        return v;
    }
}