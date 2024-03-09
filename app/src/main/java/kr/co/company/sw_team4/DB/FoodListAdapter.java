package kr.co.company.sw_team4.DB;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.company.sw_team4.R;


public class FoodListAdapter extends BaseAdapter implements Filterable {
    private ArrayList<FoodListAdapterData> list = new ArrayList<FoodListAdapterData>();
    private ArrayList<FoodListAdapterData> filterList = list;
    Filter listFilter;

    @Override
    public int getCount() {
        return filterList.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final Context context = viewGroup.getContext();

        // food_listview를 inflate 해서 view를 참조한다
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.food_listview, viewGroup, false);
        }
        TextView food_kcal = view.findViewById(R.id.kcal_list);
        TextView food_name = view.findViewById(R.id.name_list);

        //현재 포지션에 해당하는 아이템에 글자를 적용하기 위해 list배열에서 객체를 가져온다
        FoodListAdapterData listData = filterList.get(i);

        food_name.setText(listData.getName());
        food_kcal.setText(listData.getKcal() + " kcal");

        return view;
    }

    @Override
    public Object getItem(int i) {
        return filterList.get(i); //배열에 아이템을 현재 위치값을 넣어 가져온다
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    //ArrayList로 선언된 list 변수에 목록을 채워준다
    public void addItemToList(String name, String kcal){
        FoodListAdapterData listData = new FoodListAdapterData();

        listData.setName(name);
        listData.setKcal(kcal);

        //값들의 조립이 완성된 listdata 객체 한개를 list배열에 추가
        list.add(listData);
    }

    @Override
    public Filter getFilter() {
        if(listFilter == null) {
            listFilter = new ListFilter();
        }
        return listFilter;
    }

    private class ListFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                results.values = list;
                results.count = list.size();
            } else {
                ArrayList<FoodListAdapterData> itemList = new ArrayList<FoodListAdapterData>() ;

                for (FoodListAdapterData item : list) {
                    if (item.getName().toLowerCase().contains(constraint.toString().toLowerCase()))
                    {
                        // 검색창에 입력된 이름과 같은 음식 이름만 리스트에 나오게 한다.
                        itemList.add(item);
                    }
                }
                results.values = itemList;
                results.count = itemList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filterList = (ArrayList<FoodListAdapterData>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
