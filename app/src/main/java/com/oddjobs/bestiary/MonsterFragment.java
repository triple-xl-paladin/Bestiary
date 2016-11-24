package com.oddjobs.bestiary;


import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oddjobs.bestiary.db.DBHandler;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MonsterFragment extends Fragment
{
  private static String arg_key = "monster_id";
  private String monsterID;

  private ArrayList<String> trait_names;
  private ArrayList<String> trait_texts;
  private ArrayList<String> trait_attacks;

  private ArrayList<String> action_names;
  private ArrayList<String> action_texts;
  private ArrayList<String> action_attacks;

  private ArrayList<String> legendary_names;
  private ArrayList<String> legendary_texts;

  private ArrayList<String> reaction_names;
  private ArrayList<String> reaction_texts;

  private DBHandler dbHandler;

  public MonsterFragment()
  {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    monsterID = (String) getArguments().get(arg_key);

    MainActivity mainActivity = (MainActivity) getContext();
    mainActivity.setUpButton(true); // Make the action bar show the up buttone

    //Log.d("MonsterFragment","MonsterID="+monsterID);

    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_monster, container, false);
    createHeader(view);
    createStats(view);
    createTraits(view);
    createActions(view);
    createLegendary(view);
    createReactions(view);
    createFooter(view);

    return view;
  }

  private void createHeader(View view)
  {
    TextView monster_name_tv = (TextView) view.findViewById(R.id.monster_name_tv);
    monster_name_tv.setText(getField("name"));

    TextView mtype_alignment_tv = (TextView) view.findViewById(R.id.mtype_alignment_tv);
    mtype_alignment_tv.setText(getField("type"));
  }

  private void createStats(View view)
  {
    TextView mac_tv = (TextView) view.findViewById(R.id.mac_tv);
    mac_tv.setText(getField("ac"));

    TextView mhp_tv = (TextView) view.findViewById(R.id.mhp_tv);
    mhp_tv.setText(getField("hp"));

    TextView mspeed_tv = (TextView) view.findViewById(R.id.mspeed_tv);
    mspeed_tv.setText(getField("speed"));

    TextView str_tv = (TextView) view.findViewById(R.id.str_tv);
    str_tv.setText(getField("str"));

    TextView dex_tv = (TextView) view.findViewById(R.id.dex_tv);
    dex_tv.setText(getField("dex"));

    TextView con_tv = (TextView) view.findViewById(R.id.con_tv);
    con_tv.setText(getField("con"));

    TextView int_tv = (TextView) view.findViewById(R.id.int_tv);
    int_tv.setText(getField("int"));

    TextView wis_tv = (TextView) view.findViewById(R.id.wis_tv);
    wis_tv.setText(getField("wis"));

    TextView cha_tv = (TextView) view.findViewById(R.id.cha_tv);
    cha_tv.setText(getField("cha"));

    TextView msave_tv = (TextView) view.findViewById(R.id.msave_tv);
    TextView msave_header_tv = (TextView) view.findViewById(R.id.msave_header_tv);
    checkForNull(msave_header_tv, msave_tv, "save");

    TextView msenses_tv = (TextView) view.findViewById(R.id.msenses_tv);
    TextView msenses_header_tv = (TextView) view.findViewById(R.id.msenses_header_tv);
    checkForNull(msenses_header_tv, msenses_tv, "senses");

    TextView mpassive_perception_tv = (TextView) view.findViewById(R.id.mpassive_perception_tv);
    TextView mpassive_header_tv = (TextView) view.findViewById(R.id.mpassive_header_tv);
    checkForNull(mpassive_header_tv, mpassive_perception_tv, "passive");

    TextView mresist_tv = (TextView) view.findViewById(R.id.mresist_tv);
    TextView mresist_header_tv = (TextView) view.findViewById(R.id.mresists_header_tv);
    checkForNull(mresist_header_tv, mresist_tv, "resist");

    TextView mimmune_tv = (TextView) view.findViewById(R.id.mimmune_tv);
    TextView mimmune_header_tv = (TextView) view.findViewById(R.id.mimmune_header_tv);
    checkForNull(mimmune_header_tv, mimmune_tv, "immune");

    TextView mcondition_immune_tv = (TextView) view.findViewById(R.id.mcondition_immune_tv);
    TextView mcond_immune_header_tv = (TextView) view.findViewById(R.id.mcondition_imm_header_rv);
    checkForNull(mcond_immune_header_tv, mcondition_immune_tv, "conditionImmune");

    TextView mvulnerable_tv = (TextView) view.findViewById(R.id.mvulnerable_tv);
    TextView mvulnerable_header_tv = (TextView) view.findViewById(R.id.mvulberable_header_tv);
    checkForNull(mvulnerable_header_tv, mvulnerable_tv, "vulnerable");

    TextView mskills_tv = (TextView) view.findViewById(R.id.mskills_tv);
    TextView mskills_header_tv = (TextView) view.findViewById(R.id.mskills_header_tv);
    checkForNull(mskills_header_tv, mskills_tv, "skill");

    TextView mlanguages_tv = (TextView) view.findViewById(R.id.mlanguages_tv);
    TextView mlangs_header_tv = (TextView) view.findViewById(R.id.mlanguages_header_tv);
    checkForNull(mlangs_header_tv, mlanguages_tv, "languages");

    TextView mcr_tv = (TextView) view.findViewById(R.id.mcr_tv);
    TextView mcr_header_tv = (TextView) view.findViewById(R.id.mcr_header_tv);
    checkForNull(mcr_header_tv, mcr_tv, "cr");

  }

  private void createTraits(View view)
  {
    getTraits();

    if(trait_names.size()!=0)
    {
      String prev_trait_header = null;

      LinearLayout traits_llm = (LinearLayout) view.findViewById(R.id.traits_llm);

      //Log.d("MonsFragment","Trait size="+trait_names.size());
      for (int i = 0; i < trait_names.size(); i++)
      {
        //Log.d("MonsFragment","i="+i);

        String trait_header = trait_names.get(i);
        // Don't repeat header if it is same
        if (!trait_header.equals(prev_trait_header))
        {
          TextView traits_name_view = new TextView(getContext());
          traits_name_view.setText(trait_header);
          traits_name_view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
          traits_name_view.setLinkTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.dndColourGrey));
          traits_name_view.setTypeface(traits_name_view.getTypeface(), Typeface.BOLD);
          // left top right bottom
          traits_name_view.setPadding(0, 5, 0, 0);
          traits_llm.addView(traits_name_view);
          prev_trait_header = trait_header;
        }

        TextView traits_text_view = new TextView(getContext());
        traits_text_view.setText(trait_texts.get(i));
        traits_text_view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        traits_text_view.setLinkTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.dndColourGrey));
        // left top right bottom
        traits_text_view.setPadding(0, 0, 0, 5);
        traits_llm.addView(traits_text_view);

      /* Not convinced this is needed
      if(trait_attacks.get(i) != null)
      {
        TextView traits_att_view = new TextView(getContext());
        traits_att_view.setText(trait_attacks.get(i));
        traits_att_view.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
            ));
        traits_att_view.setLinkTextColor(ContextCompat.getColor(getActivity().getApplicationContext() ,R.color.dndColourGrey));
        traits_llm.addView(traits_att_view);
      } */
      }
    }
  }

  private void createActions(View view)
  {
    getActions();

    if(action_names.size()!=0)
    {
      String prev_action_header = null;

      LinearLayout actions_llm = (LinearLayout) view.findViewById(R.id.actions_llm);

      //Log.d("MonsFragment","Action size="+action_names.size());
      for (int i = 0; i < action_names.size(); i++)
      {
        //Log.d("MonsFragment","i="+i);
        String action_name_header = action_names.get(i);

        if (!action_name_header.equals(prev_action_header))
        {
          TextView actions_name_view = new TextView(getContext());
          actions_name_view.setText(action_name_header);
          actions_name_view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
          actions_name_view.setLinkTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.dndColourGrey));
          actions_name_view.setTypeface(actions_name_view.getTypeface(), Typeface.BOLD);
          // left top right bottom
          actions_name_view.setPadding(0, 5, 0, 0);
          actions_llm.addView(actions_name_view);
          prev_action_header = action_name_header;
        }

        TextView actions_text_view = new TextView(getContext());
        actions_text_view.setText(action_texts.get(i));
        actions_text_view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        actions_text_view.setLinkTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.dndColourGrey));
        // left top right bottom
        actions_text_view.setPadding(0, 0, 0, 5);
        actions_llm.addView(actions_text_view);

      /* Not convinced this is needed
      if(action_attacks.get(i) != null)
      {
        TextView action_att_view = new TextView(getContext());
        action_att_view.setText(action_attacks.get(i));
        action_att_view.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
            ));
        action_att_view.setLinkTextColor(ContextCompat.getColor(getActivity().getApplicationContext() ,R.color.dndColourGrey));
        actions_llm.addView(action_att_view);
      }
      */
      }
    }
  }

  private void createLegendary(View view)
  {
    getLegendary();

    if(legendary_names.size()!=0)
    {
      String prev_legendary_header = null;

      LinearLayout legendary_llm = (LinearLayout) view.findViewById(R.id.legendary_llm);

      // Legendary Actions header
      TextView legendary_header_view = new TextView(getContext());
      legendary_header_view.setText("Legendary Actions");
      // width height
      legendary_header_view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
      legendary_header_view.setTypeface(legendary_header_view.getTypeface(),Typeface.BOLD);
      legendary_header_view.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.dndColourRed));
      legendary_header_view.setTextSize(18);
      // left top right bottom
      legendary_header_view.setPadding(0, convertDP(5), 0, 0);
      legendary_llm.addView(legendary_header_view);
      legendary_llm.addView(createHorizontalBar());

      //Log.d("MonsFragment","Action size="+action_names.size());
      for (int i = 0; i < legendary_names.size(); i++)
      {
        //Log.d("MonsFragment","i="+i);
        String legendary_header = legendary_names.get(i);

        if (!legendary_header.equals(prev_legendary_header))
        {
          TextView legendary_name_view = new TextView(getContext());
          legendary_name_view.setText(legendary_names.get(i));
          legendary_name_view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
          legendary_name_view.setLinkTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.dndColourGrey));
          legendary_name_view.setTypeface(legendary_name_view.getTypeface(), Typeface.BOLD);
          // left top right bottom
          legendary_name_view.setPadding(0, convertDP(5), 0, 0);
          legendary_llm.addView(legendary_name_view);
          prev_legendary_header = legendary_header;
        }

        TextView legendary_text_view = new TextView(getContext());
        legendary_text_view.setText(legendary_texts.get(i));
        legendary_text_view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        legendary_text_view.setLinkTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.dndColourGrey));
        // left top right bottom
        legendary_text_view.setPadding(0, 0, 0, convertDP(5));
        legendary_llm.addView(legendary_text_view);
      }
    }
  }

  private void createReactions(View view)
  {
    getReactions();

    if (reaction_names.size() != 0)
    {
      String prev_reaction_header = null;

      LinearLayout reactions_llm = (LinearLayout) view.findViewById(R.id.reactions_llm);

      // Legendary Actions header
      TextView reaction_header_view = new TextView(getContext());
      reaction_header_view.setText("Reactions");
      // width height
      reaction_header_view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
      reaction_header_view.setTypeface(reaction_header_view.getTypeface(), Typeface.BOLD);
      reaction_header_view.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.dndColourRed));
      reaction_header_view.setTextSize(18);
      // left top right bottom
      reaction_header_view.setPadding(0, convertDP(5), 0, 0);
      reactions_llm.addView(reaction_header_view);
      reactions_llm.addView(createHorizontalBar());

      //Log.d("MonsFragment","Action size="+action_names.size());
      for (int i = 0; i < reaction_names.size(); i++)
      {
        //Log.d("MonsFragment","i="+i);
        String reaction_header = reaction_names.get(i);

        if (!reaction_header.equals(prev_reaction_header))
        {
          TextView reaction_name_view = new TextView(getContext());
          reaction_name_view.setText(reaction_names.get(i));
          reaction_name_view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
          reaction_name_view.setLinkTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.dndColourGrey));
          reaction_name_view.setTypeface(reaction_name_view.getTypeface(), Typeface.BOLD);
          // left top right bottom
          reaction_name_view.setPadding(0, convertDP(5), 0, 0);
          reactions_llm.addView(reaction_name_view);
          prev_reaction_header = reaction_header;
        }

        TextView reaction_text_view = new TextView(getContext());
        reaction_text_view.setText(reaction_texts.get(i));
        reaction_text_view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        reaction_text_view.setLinkTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.dndColourGrey));
        // left top right bottom
        reaction_text_view.setPadding(0, 0, 0, convertDP(5));
        reactions_llm.addView(reaction_text_view);
      }
    }
  }

  private View createHorizontalBar()
  {
    ImageView horiz_bar_iv = new ImageView(getContext());
    horiz_bar_iv.setImageResource(R.color.dndColourRed);
    horiz_bar_iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, convertDP(5)));

    return horiz_bar_iv;
  }

  private void createFooter(View view)
  {

    TextView mdescription_tv = (TextView) view.findViewById(R.id.mdescription_tv);
    checkForNull(null, mdescription_tv, "description");

    TextView msource_tv = (TextView) view.findViewById(R.id.msource_tv);
    checkForNull(null, msource_tv, "source_text");

  }

  private void checkForNull(TextView header, TextView tv, String field)
  {
    String rfield = getField(field);
    //Log.d("MonsFrag", "rfield=" + rfield);

    if (rfield == null)
    {
      if (header != null)
        header.setVisibility(View.GONE);
      tv.setVisibility(View.GONE);
      //Log.d("MonsFrag", "Gone");
    }
    else
    {
      if (header != null)
        header.setVisibility(View.VISIBLE);
      tv.setVisibility(View.VISIBLE);
      tv.setText(rfield);
      //Log.d("MonsFrag", "setText"+tv.toString());
    }
  }

  private String getField(String field)
  {
    String sql = "SELECT " +
        field + " " +
        "from vw_list_of_monsters " +
        "where monster_id='" + monsterID + "'";

    String return_field = null;
    Cursor recordSet = null;

    dbHandler = new DBHandler(getActivity().getApplicationContext());

    dbHandler.openDatabase();
    recordSet = dbHandler.executeQuerySQL(sql);

    int rows = recordSet.getCount();
    //Log.d("MonsFrag","row count="+rows);

    recordSet.moveToFirst();

    for (int i = 0; i < rows; i++)
    {
      return_field = recordSet.getString(recordSet.getColumnIndex(field));
      recordSet.moveToNext();
    }
    // Apparently you need to close the Cursor to free RAM.
    recordSet.close();

    //Log.d("MonsFrag","monster="+return_field);

    dbHandler.closeDatabase();

    //DEBUG!!!!!
    //dbHandler.deleteDB();

    return return_field;
  }

  private void getTraits()
  {
    trait_names = new ArrayList<String>();
    trait_texts = new ArrayList<String>();
    trait_attacks = new ArrayList<String>();

    String sql = "SELECT " +
        "trait_name " +
        ",trait_text " +
        ",trait_attack " +
        "from vw_traits " +
        "where monster_id='" + monsterID + "'";

    Cursor recordSet = null;

    dbHandler = new DBHandler(getActivity().getApplicationContext());

    dbHandler.openDatabase();
    recordSet = dbHandler.executeQuerySQL(sql);

    int rows = recordSet.getCount();
    Log.d("MonsFrag", "row count=" + rows);

    recordSet.moveToFirst();

    for (int i = 0; i < rows; i++)
    {
      trait_names.add(recordSet.getString(recordSet.getColumnIndex("trait_name")));
      trait_texts.add(recordSet.getString(recordSet.getColumnIndex("trait_text")));
      trait_attacks.add(recordSet.getString(recordSet.getColumnIndex("trait_attack")));
      recordSet.moveToNext();
    }
    // Apparently you need to close the Cursor to free RAM.
    recordSet.close();
    dbHandler.closeDatabase();
  }

  private void getActions()
  {
    action_names = new ArrayList<String>();
    action_texts = new ArrayList<String>();
    action_attacks = new ArrayList<String>();

    String sql = "SELECT " +
        "action_name " +
        ",action_text " +
        ",action_attack " +
        "from vw_actions " +
        "where monster_id='" + monsterID + "'";

    Cursor recordSet = null;

    dbHandler = new DBHandler(getActivity().getApplicationContext());

    dbHandler.openDatabase();
    recordSet = dbHandler.executeQuerySQL(sql);

    int rows = recordSet.getCount();
    //Log.d("MonsFrag", "row count=" + rows);

    recordSet.moveToFirst();

    for (int i = 0; i < rows; i++)
    {
      action_names.add(recordSet.getString(recordSet.getColumnIndex("action_name")));
      action_texts.add(recordSet.getString(recordSet.getColumnIndex("action_text")));
      action_attacks.add(recordSet.getString(recordSet.getColumnIndex("action_attack")));
      recordSet.moveToNext();
    }
    // Apparently you need to close the Cursor to free RAM.
    recordSet.close();

    dbHandler.closeDatabase();
  }

  private void getLegendary()
  {
    legendary_names = new ArrayList<String>();
    legendary_texts = new ArrayList<String>();

    String sql = "SELECT " +
        "legendary_name " +
        ",legendary_text " +
        "from vw_legendary " +
        "where monster_id='" + monsterID + "'";

    Cursor recordSet = null;

    dbHandler = new DBHandler(getActivity().getApplicationContext());

    dbHandler.openDatabase();
    recordSet = dbHandler.executeQuerySQL(sql);

    int rows = recordSet.getCount();
    //Log.d("MonsFrag", "row count=" + rows);

    recordSet.moveToFirst();

    for (int i = 0; i < rows; i++)
    {
      legendary_names.add(recordSet.getString(recordSet.getColumnIndex("legendary_name")));
      legendary_texts.add(recordSet.getString(recordSet.getColumnIndex("legendary_text")));
      recordSet.moveToNext();
    }
    // Apparently you need to close the Cursor to free RAM.
    recordSet.close();

    dbHandler.closeDatabase();
  }

  private void getReactions()
  {
    reaction_names = new ArrayList<String>();
    reaction_texts = new ArrayList<String>();

    String sql = "SELECT " +
        "reaction_name " +
        ",reaction_text " +
        "from vw_reactions " +
        "where monster_id='" + monsterID + "'";

    Cursor recordSet = null;

    dbHandler = new DBHandler(getActivity().getApplicationContext());

    dbHandler.openDatabase();
    recordSet = dbHandler.executeQuerySQL(sql);

    int rows = recordSet.getCount();
    //Log.d("MonsFrag", "row count=" + rows);

    recordSet.moveToFirst();

    for (int i = 0; i < rows; i++)
    {
      reaction_names.add(recordSet.getString(recordSet.getColumnIndex("reaction_name")));
      reaction_texts.add(recordSet.getString(recordSet.getColumnIndex("reaction_text")));
      recordSet.moveToNext();
    }
    // Apparently you need to close the Cursor to free RAM.
    recordSet.close();

    dbHandler.closeDatabase();
  }

  /**
   * Converts dp size to an accepted figure
   *
   * @param size
   * @return
   */
  private int convertDP(int size)
  {
    return (int) (size*getContext().getResources().getDisplayMetrics().density);
  }

}
