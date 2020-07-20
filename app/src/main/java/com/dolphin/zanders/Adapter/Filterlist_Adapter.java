package com.dolphin.zanders.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dolphin.zanders.Fragment.FilterListFragment;
import com.dolphin.zanders.Model.Filter_model.FilterInnerModel;
import com.dolphin.zanders.Model.Filter_model.FilterModel;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Util.CheckNetwork;
import com.dolphin.zanders.Util.TotalListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Filterlist_Adapter extends BaseExpandableListAdapter {
    //this arraylist is main array list that is used for selected vvalue id and not selected value id to "0"
    public static ArrayList<ArrayList<String>> filter_child_value_list = new ArrayList<>();
    public static ArrayList<ArrayList<String>> mGroupList = new ArrayList<>();

    //before selected child use arraylist
    public static ArrayList<String> pass_ArrayList_child = new ArrayList<>();
    public static ArrayList<String> pass_ArrayList_group = new ArrayList<>();

    private Context context;
    private List<FilterModel> filterModels;

    TotalListener mListener;
    public static CheckBox childCheckBox;
    public static ArrayList<ArrayList<Boolean>> selectedChildCheckBoxStates = new ArrayList<>();
    ArrayList<Boolean> selectedParentCheckBoxesState = new ArrayList<>();
    public static ArrayList<String> filter_grouppp_namelist = new ArrayList<>();
    public static String newFilterGroup = null, newFilterChild = null, filterscreen, filter_cateid,searhkey,subcatename;

    public static int sort = 0;

    public Filterlist_Adapter(Context context, ArrayList<FilterModel> filterModels) {
        this.context = context;
        this.filterModels = filterModels;

        Log.e("selected_child_list", "=" + filter_child_value_list);
        addDataToview();
    }

    /**
     * Called to initialize the default check states of items
     *
     * @param defaultState : false
     */
    private void initCheckStates(boolean defaultState) {
        for (int i = 0; i < mGroupList.size(); i++) {
            selectedParentCheckBoxesState.add(i, defaultState);
            ArrayList<Boolean> childStates = new ArrayList<>();
            for (int j = 0; j < mGroupList.get(i).size(); j++) {
                Log.e("debu_99", "" + mGroupList.get(i).get(j));
                Log.e("debu_68", "" + FilterListFragment.filter_old_childlist.equals(mGroupList.get(i).get(j)));

                if(FilterListFragment.filter_old_childlist.isEmpty())
                {
                    childStates.add(defaultState);

                }else {

                    Log.e("debu_112", "6="+FilterListFragment.filter_old_childlist.contains(mGroupList.get(i).get(j)));
                    Log.e("debu_1133", "6="+FilterListFragment.filter_old_childlist);
                        if(FilterListFragment.filter_old_childlist.contains(mGroupList.get(i).get(j)))
                        {
                            childStates.add(true);
                            Log.e("debu_9911", "d=" );
                        }else {
                            Log.e("debu_111", "d=" );
                            childStates.add(defaultState);
                        }
                }


               /* if (filter_child_value_list.isEmpty()) {

                    Log.e("debu_67", "" + filter_child_value_list);

                    childStates.add(defaultState);

                } else {

                    Log.e("debu70", "" + filter_child_value_list);
                    //error here
                    if (filter_child_value_list.get(i).get(j).equals("0")) {
                        childStates.add(defaultState);
                    } else {
                        //if selected set true
                        childStates.add(true);
                    }
                }*/

                Log.e("debug_70sele", "" + childStates);
                Log.e("filterlist70sele", "" + filter_child_value_list);
                //   childStates.add(defaultState);
            }
            Log.e("debug_70sele", "" + childStates);

            selectedChildCheckBoxStates.add(i, childStates);
            Log.e("debg_888", "" + selectedChildCheckBoxStates);
        }
    }

    private void addDataToview() {
        //Add raw data into Group List Array
        for (int i = 0; i < filterModels.size(); i++) { //
            Log.e("debug_filtemodel", "" + filterModels.size());
            ArrayList<String> prices = new ArrayList<>();
            for (int j = 0; j < filterModels.get(i).getItems().size(); j++) {
                Log.e("debug_child123", "" + filterModels.get(i).getItems().size());

                Log.e("debug_88aaa", "childe" + filterModels.get(i).getItems().get(j).getAttribute_value());
                prices.add(filterModels.get(i).getItems().get(j).getAttribute_value());
            }
            mGroupList.add(i, prices);
            Log.e("mGroupList", "gr=" + mGroupList);
        }

        //initialize default check states of checkboxes
        initCheckStates(false);

    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        //child model
        final FilterInnerModel childmodel = (FilterInnerModel) getChild(groupPosition, childPosition);
        ///group model
        final FilterModel group = (FilterModel) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.row_listfilter_item, null);
        }

        childCheckBox = (CheckBox) convertView.findViewById(R.id.myCheckBox);
        childCheckBox.setText(childmodel.getAttribute_name());
        Log.e("debug_87s", "=" + selectedChildCheckBoxStates.size());
        Log.e("debug_88s", "=" + groupPosition);
        if (selectedChildCheckBoxStates.size() <= groupPosition) {
            ArrayList<Boolean> childState = new ArrayList<>();
            Log.e("debug_91_childstate", "=" + childState);
            for (int i = 0; i < mGroupList.get(groupPosition).size(); i++) {
                if (childState.size() > childPosition) {
                    Log.e("debug_91_childstate", "=" + childPosition);
                    childState.add(childPosition, false);
                } else {
                    childState.add(false);
                }
                Log.e("childState888", "=" + childState);
            }
            if (selectedChildCheckBoxStates.size() > groupPosition) {
                selectedChildCheckBoxStates.add(groupPosition, childState);
                Log.e("childState888", "=" + childState);
            } else
                selectedChildCheckBoxStates.add(childState);
        } else {
//            Log.e("debug_102", "=" + selectedChildCheckBoxStates.get(groupPosition).get(childPosition));
            childCheckBox.setChecked(selectedChildCheckBoxStates.get(groupPosition).get(childPosition));
        }


        Log.e("debugselected_child_129", "=" + filter_child_value_list);
        childCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear selected child model

                filter_grouppp_namelist.clear();
                FilterListFragment.filter_old_childlist.clear();
                FilterListFragment.selected_child="";

                boolean state = selectedChildCheckBoxStates.get(groupPosition).get(childPosition);
                selectedChildCheckBoxStates.get(groupPosition).remove(childPosition);
                selectedChildCheckBoxStates.get(groupPosition).add(childPosition, state ? false : true);

                //code  before selected true of filter item
                if (selectedChildCheckBoxStates.get(groupPosition).get(childPosition).booleanValue() == true) {
                    // childvalue.add(childmodel.getAttribute_value());
                    pass_ArrayList_child.add(childmodel.getAttribute_value());
                    pass_ArrayList_group.add(group.getName());


                }

                //filter  selected true state
                ///filter coding add data to selected view true to peticular position
                for (int i = 0; i < filterModels.size(); i++) {

                    ArrayList<String> value = new ArrayList<>();
                    for (int j = 0; j < filterModels.get(i).getItems().size(); j++) {

                        if (selectedChildCheckBoxStates.get(i).get(j).booleanValue() == true) {
                            value.add(filterModels.get(i).getItems().get(j).getAttribute_value());

                            FilterListFragment.filter_old_childlist.add(filterModels.get(i).getItems().get(j).getAttribute_value());

                            newFilterGroup = filterModels.get(i).getName();
                            newFilterChild = filterModels.get(i).getItems().get(j).getAttribute_value();

                            Log.e("debug_216", "=" );

                            if(filter_grouppp_namelist.contains(filterModels.get(i).getName()))
                            {

                            }else {
                                filter_grouppp_namelist.add(filterModels.get(i).getName());
                            }

                            if (FilterListFragment.selected_child.contains(newFilterGroup)) {

                                Log.e("debug_44", "="+FilterListFragment.selected_child );
                                Log.e("debug_445", "="+newFilterChild );

                                if(FilterListFragment.selected_child.contains(newFilterChild))
                                {
                                    Log.e("debug_222", "="+FilterListFragment.selected_child.contains(newFilterChild) );
                                }else {
                                    Log.e("debug_233", "=" );
                                    FilterListFragment.selected_child += "_" + newFilterChild;
                                }



                            } else {

                                FilterListFragment.selected_child += "&" + newFilterGroup + "=" + newFilterChild;
                            }

                        } else {
                            Log.e("debug_233", "=" );
                            value.add("0");
                        }


                    }
                    filter_child_value_list.add(i, value);
                    Log.e("value_224", "=" +  FilterListFragment.filter_old_childlist);
                    Log.e("value_2245", "=" +  filter_child_value_list);
                }


                filterscreen = FilterListFragment.screen;
                filter_cateid = FilterListFragment.sub_category_id;
                searhkey = FilterListFragment.searhkey;
                subcatename = FilterListFragment.subcatename;
                sort=FilterListFragment.sort;


                Log.e("debug_2111", "=" + FilterListFragment.selected_child);
                //   Toast.makeText(context, "swdfewfwfwf1rsdf3wfewfwe", Toast.LENGTH_SHORT).show();

                //call api here
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                FilterListFragment filterListFragment = new FilterListFragment();
                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).replace(R.id.framlayout, filterListFragment).commit();
                Log.e("filter_child_list_156", "=" + filter_child_value_list);
            }
        });

        Log.e("debug_main", "=" + selectedChildCheckBoxStates);
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        ArrayList<FilterInnerModel> chList = filterModels.get(listPosition).getItems();
        return chList.size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return filterModels.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return filterModels.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded,
                             View convertView, ViewGroup parent) {
        FilterModel group = (FilterModel) getGroup(groupPosition);
        ImageView iv_arrow;
        LinearLayout lv_main_group;
        CheckBox checkBox;
        TextView tv_group_name;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.row_listfilter_group, null);
        }
        iv_arrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
        checkBox = (CheckBox) convertView.findViewById(R.id.group_chk_box);
        lv_main_group = (LinearLayout) convertView.findViewById(R.id.lv_main_group);
        iv_arrow.setSelected(isExpanded);
        tv_group_name = (TextView) convertView.findViewById(R.id.tv_group_name);
        tv_group_name.setTypeface(null, Typeface.BOLD);
        tv_group_name.setText(group.getLabel().toUpperCase());

        if (selectedParentCheckBoxesState.size() <= groupPosition) {
            selectedParentCheckBoxesState.add(groupPosition, false);
        } else {
            checkBox.setChecked(selectedParentCheckBoxesState.get(groupPosition));
        }
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Callback to expansion of group item
                if (!isExpanded)
                    mListener.expandGroupEvent(groupPosition, isExpanded);
                boolean state = selectedParentCheckBoxesState.get(groupPosition);
                Log.e("195TAG", "STATE = " + state);
                selectedParentCheckBoxesState.remove(groupPosition);
                selectedParentCheckBoxesState.add(groupPosition, state ? false : true);

                for (int i = 0; i < mGroupList.get(groupPosition).size(); i++) {
                    selectedChildCheckBoxStates.get(groupPosition).remove(i);
                    selectedChildCheckBoxStates.get(groupPosition).add(i, state ? false : true);
                }
                notifyDataSetChanged();

            }
        });

        lv_main_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isExpanded) {
                    mListener.expandGroupEvent(groupPosition, isExpanded);
                } else {
                    mListener.expandGroupEvent(groupPosition, isExpanded);
                }
            }
        });
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        ArrayList<FilterInnerModel> chList = filterModels.get(listPosition).getItems();
        return chList.get(expandedListPosition);
    }

    public void setmListener(TotalListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }
}