package com.oddjobs.bestiary;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.oddjobs.bestiary.db.DBHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aprc1 on 20/11/16.
 */

public class BestiaryAdapter extends RecyclerView.Adapter<BestiaryAdapter.BestiaryViewHolder>
{
  private Fragment fragment;
  private Context context;
  private List<String> masterMonsterID;
  private List<String> masterMonsterNames;
  private List<String> masterMonsterCR;
  private List<String> masterMonsterType;
  private List<String> masterMonsterSource;

  private List<String> monsterID;
  private List<String> monsterNames;
  private List<String> monsterCR;
  private List<String> monsterType;
  private List<String> monsterSource;

  public BestiaryAdapter(Fragment fragment, Context context)
  {
    this.fragment = fragment;
    this.context = context;
    createList();
    setMasterList();
  }

  public static class BestiaryViewHolder extends RecyclerView.ViewHolder
  {
    protected TextView mnameTextView;
    protected TextView mcrTextView;
    protected TextView mtypeTextView;
    protected TextView msourceTextView;
    private Context context;

    public BestiaryViewHolder(View view)
    {
      super(view);
      mnameTextView = (TextView) view.findViewById(R.id.mname_textview);
      mcrTextView = (TextView) view.findViewById(R.id.mcr_textview);
      mtypeTextView = (TextView) view.findViewById(R.id.mtype_textview);
      msourceTextView = (TextView) view.findViewById(R.id.msource_textview);

      view.setOnClickListener(new View.OnClickListener()
      {
        @Override
        public void onClick(View v)
        {
          //Log.d("BestVH", "Clicked!" + v.toString());
          MainActivity mainActivity = (MainActivity) context;

          //Log.d("BestVH", "Soundscape clicked");
          //Log.d("onClick", (String) mnameTextView.getTag());

          Fragment monsterFragment = new MonsterFragment();
          Bundle args = new Bundle();
          args.putString("monster_id", (String) mnameTextView.getTag());
          monsterFragment.setArguments(args);

          // This should content_main, not main_frame. But why the app bar stays I do not know.
          mainActivity.switchContent(R.id.main_frame, monsterFragment, "mosnter_fragment");
          //Log.d("onClick", "New fragment should have launched.");
        }
      });
    }

    public void setContext(Context context)
    {
      this.context = context;
    }
  }

  @Override
  public BestiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
  {
    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_bestiary, parent, false);
    BestiaryViewHolder bvh = new BestiaryViewHolder(itemView);
    bvh.setContext(context);
    //bvh.setFragment(fragment);

    return bvh;
  }

  @Override
  public void onBindViewHolder(final BestiaryViewHolder holder, int position)
  {
    holder.mnameTextView.setText(monsterNames.get(position));
    holder.mnameTextView.setTag(monsterID.get(position));
    holder.mtypeTextView.setText(monsterType.get(position));
    holder.mcrTextView.setText(monsterCR.get(position));
    holder.msourceTextView.setText(monsterSource.get(position));
  }

  @Override
  public int getItemCount()
  {
    //Log.d("BestADapter","items cnt="+monsterNames.size());
    return monsterNames.size();
  }

  private void setMasterList()
  {
    monsterID=masterMonsterID;
    monsterNames=masterMonsterNames;
    monsterCR=masterMonsterCR;
    monsterType=masterMonsterType;
    monsterSource=masterMonsterSource;

  }

  private void createList()
  {
    masterMonsterID = new ArrayList<String>();
    masterMonsterNames = new ArrayList<String>();
    masterMonsterCR = new ArrayList<String>();
    masterMonsterType = new ArrayList<String>();
    masterMonsterSource = new ArrayList<String>();

    String sql = "SELECT " +
        "monster_id " +
        ",name " +
        ",type " +
        ",cr " +
        ",source_text " +
        "from vw_list_of_monsters";

    Cursor recordSet = null;

    DBHandler dbHandler = new DBHandler(context);
    dbHandler.openDatabase();
    recordSet = dbHandler.executeQuerySQL(sql);

    int rows = recordSet.getCount();
    //Log.d("BestAdapter","row count="+rows);

    recordSet.moveToFirst();

    for (int i = 0; i < rows; i++)
    {
      masterMonsterID.add(recordSet.getString(recordSet.getColumnIndex("monster_id")));
      masterMonsterNames.add(recordSet.getString(recordSet.getColumnIndex("name")));
      masterMonsterType.add(recordSet.getString(recordSet.getColumnIndex("type")));
      masterMonsterCR.add("CR "+recordSet.getString(recordSet.getColumnIndex("cr")));
      masterMonsterSource.add(recordSet.getString(recordSet.getColumnIndex("source_text")));
      recordSet.moveToNext();
    }
    // Apparently you need to close the Cursor to free RAM.
    recordSet.close();

    //Log.d("BestAdapter","monster_id count="+masterMonsterID.size());

    dbHandler.closeDatabase();

    //DEBUG!!!!!
    //Log.d("BestAdapt","DELETE DB!!!");
    //dbHandler.deleteDB();

  }

  public void searchString(String query)
  {
    List filteredNameList = new ArrayList<>();
    List filteredIDList = new ArrayList<>();
    List filteredTypeList = new ArrayList<>();
    List filteredCRList = new ArrayList<>();
    List filteredSourceList = new ArrayList<>();

    for(int i=0;i<masterMonsterNames.size();i++)
    {
      if(masterMonsterNames.get(i).toLowerCase().contains(query.toLowerCase()))
      {
        filteredNameList.add(masterMonsterNames.get(i));
        filteredIDList.add(masterMonsterID.get(i));
        filteredTypeList.add(masterMonsterType.get(i));
        filteredCRList.add(masterMonsterCR.get(i));
        filteredSourceList.add(masterMonsterSource.get(i));
      }
      else if(masterMonsterType.get(i).toLowerCase().contains(query.toLowerCase()))
      {
        filteredNameList.add(masterMonsterNames.get(i));
        filteredIDList.add(masterMonsterID.get(i));
        filteredTypeList.add(masterMonsterType.get(i));
        filteredCRList.add(masterMonsterCR.get(i));
        filteredSourceList.add(masterMonsterSource.get(i));
      }
      else if(masterMonsterCR.get(i).toLowerCase().contains(query.toLowerCase()))
      {
        filteredNameList.add(masterMonsterNames.get(i));
        filteredIDList.add(masterMonsterID.get(i));
        filteredTypeList.add(masterMonsterType.get(i));
        filteredCRList.add(masterMonsterCR.get(i));
        filteredSourceList.add(masterMonsterSource.get(i));
      }
      else if(masterMonsterSource.get(i).toLowerCase().contains(query.toLowerCase()))
      {
        filteredNameList.add(masterMonsterNames.get(i));
        filteredIDList.add(masterMonsterID.get(i));
        filteredTypeList.add(masterMonsterType.get(i));
        filteredCRList.add(masterMonsterCR.get(i));
        filteredSourceList.add(masterMonsterSource.get(i));
      }
    }

    monsterNames=filteredNameList;
    monsterID=filteredIDList;
    monsterType=filteredTypeList;
    monsterCR=filteredCRList;
    monsterSource=filteredSourceList;

  }
}