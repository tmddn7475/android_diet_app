package kr.co.company.sw_team4.DB;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.company.sw_team4.R;


public class ExerciseListAdapter extends BaseAdapter implements Filterable {
    private ArrayList<ExerciseListAdapterData> list = new ArrayList<ExerciseListAdapterData>();
    private ArrayList<ExerciseListAdapterData> filterList = list;

    Filter listFilter;

    @Override
    public int getCount() {
        return filterList.size(); //리스트 배열의 크기를 반환함
    }

    @Override
    public Object getItem(int i) {
        return filterList.get(i); //배열에 아이템을 현재 위치값을 넣어 가져옴
    }

    @Override
    public long getItemId(int i) {
        return i; //그냥 위치값을 반환해도 되지만 원한다면 아이템의 num 을 반환해도 된다
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final Context context = viewGroup.getContext();

        // exercise_listview를 inflate 해서 view를 참조한다
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.exercise_listview, viewGroup, false);
        }
        TextView exercise_name = view.findViewById(R.id.name_list);
        TextView exercise_part = view.findViewById(R.id.part_list);
        ImageView imageView = view.findViewById(R.id.image_list);

        //현재 포지션에 해당하는 아이템에 글자를 적용하기 위해 list배열에서 객체를 가져온다
        ExerciseListAdapterData listData = filterList.get(i);

        //getResources().getIdentifier()를 통해 drawable 내에 해당 이름의 이미지 리소스를 가져온다
        int imageID = context.getResources().getIdentifier(listData.getImage(), "drawable", context.getPackageName());

        exercise_name.setText(listData.getName());
        exercise_part.setText(listData.getPart());
        imageView.setImageResource(imageID);

        return view;
    }

    //ArrayList로 선언된 list 변수에 목록을 채워주기 위함
    public void addItemToList(String name, String image, String part){
        ExerciseListAdapterData listData = new ExerciseListAdapterData();
        listData.setName(name);
        listData.setImage(image);
        listData.setPart(part);

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
                ArrayList<ExerciseListAdapterData> itemList = new ArrayList<ExerciseListAdapterData>();
                for (ExerciseListAdapterData item : list) {
                    if (item.getName().toLowerCase().contains(constraint.toString().toLowerCase()))
                    {
                        // 검색창에 입력된 이름과 같은 운동 이름만 리스트에 나오게 한다.
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
            filterList = (ArrayList<ExerciseListAdapterData>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
