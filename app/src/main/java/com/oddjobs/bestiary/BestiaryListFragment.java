package com.oddjobs.bestiary;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static android.support.v7.recyclerview.R.styleable.RecyclerView;


/**
 */
public class BestiaryListFragment extends Fragment
{
  private RecyclerView recyclerView;
  private BestiaryAdapter bestiaryAdapter;
  private SearchView searchView;
  private Bundle instanceSave;
  private static String parcelString = "parcel";
  private LinearLayoutManager linearLayoutManager;

  public BestiaryListFragment()
  {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    bestiaryAdapter = new BestiaryAdapter(this, getContext());
    //bestiaryAdapter.createList();
    //bestiaryAdapter.setMasterList();

    //Log.d("BestiaryListFragment","oncreate");
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    //Log.d("BestiaryListFragment", "oncreateview start");
    View view = inflater.inflate(R.layout.fragment_bestiary_list, container, false);

    searchView = (SearchView) view.findViewById(R.id.monster_searchview);
    recyclerView = (RecyclerView) view.findViewById(R.id.bestiary_list_recyclerview);
    linearLayoutManager = new LinearLayoutManager(getActivity());

    recyclerView.setLayoutManager(linearLayoutManager);
    recyclerView.setAdapter(bestiaryAdapter);

    //clearSearchView();
    searchView.setOnQueryTextListener(listener);

    //Log.d("BestiaryListFragment", "oncreateview end");
    return view;
  }

  SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener(){

    @Override
    public boolean onQueryTextChange(String query)
    {
      //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
      //bestiaryAdapter = new BestiaryAdapter(getParentFragment(), getContext());
      if(query != null)
      {
        bestiaryAdapter.searchString(query);
        recyclerView.swapAdapter(bestiaryAdapter, false);
        bestiaryAdapter.notifyDataSetChanged();
      }
      return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
      return false;
    }
  };

  @Override
  public void onSaveInstanceState(Bundle outState)
  {
    super.onSaveInstanceState(outState);
  }

  public void clearSearchView()
  {
    searchView.setQuery("", false);
    searchView.setIconified(true);
    searchView.clearFocus();
  }

  @Override
  public void onResume()
  {
    clearSearchView();
    super.onResume();
  }
}
