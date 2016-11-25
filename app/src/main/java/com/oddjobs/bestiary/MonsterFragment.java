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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oddjobs.bestiary.db.DBHandler;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


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
    //View view = inflater.inflate(R.layout.fragment_monster, container, false);
    View view = inflater.inflate(R.layout.fragment_monster, container, false);
    createHeader(view);
    createStats(view);
    createTraits(view, inflater, container);
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

  private void createTraits(View view, LayoutInflater inflater, ViewGroup container)
  {
    getTraits();

    //List trait_tvs = new ArrayList<TextView>();

    if(trait_names.size()!=0)
    {
      String prev_trait_header = null;

      LinearLayout traits_llm = (LinearLayout) view.findViewById(R.id.traits_llm);

      Log.d("MonsFragment","Trait size before for loop="+trait_names.size());
      for (int i = 0; i < trait_names.size(); i++)
      {
        Log.d("MonsFragment","i counter="+i);

        //Log.d("MonsFrag","Create the relativelayout layoutparams");
        //RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        String trait_header = trait_names.get(i);
        // Don't repeat header if it is same
        if (!trait_header.equals(prev_trait_header))
        {
          Log.d("MonsFrag","The header is not the same as the last one. Create a new TextView");
          TextView traits_name_view = new TextView(getContext());
          //Log.d("MonsFrag","Set the id for the text view to="+trait_tvs.size());
          Log.d("MonsFrag","Set the text for the text view to="+trait_header);
          //traits_name_view.setId(trait_tvs.size());
          traits_name_view.setText(trait_header);

          /*
          Log.d("monsFrag","What size is traits_tvs?="+trait_tvs.size());
          if(trait_tvs.size()==0)
          {
            Log.d("MonsFrag","If traits_tvs is 0 then add below red horiz line");
            p.addRule(RelativeLayout.BELOW, R.id.red_horiz_line4);
          }
          else
          {
            Log.d("MonsFrag","Already a trait textview and traits_tvs_size="+trait_tvs.size());
            TextView tmp = (TextView) trait_tvs.get(trait_tvs.size()-1);
            p.addRule(RelativeLayout.BELOW, tmp.getId());
            Log.d("MonsFrag","TextView id="+tmp.getId()+" TextView text="+tmp.getText());
          }


          Log.d("MonsFrag","add layout params to text view");
          traits_name_view.setLayoutParams(p);
          */
          Log.d("MonsFrag","Set colours and typeface");
          traits_name_view.setLinkTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.dndColourGrey));
          traits_name_view.setTypeface(traits_name_view.getTypeface(), Typeface.BOLD);
          // left top right bottom
          traits_name_view.setPadding(0, 5, 0, 0);
          traits_llm.addView(traits_name_view);
          Log.d("MonsFrag","prev_trait_header before setting trait_header="+prev_trait_header);
          prev_trait_header = trait_header;
          Log.d("MonsFrag","prev_trait_header after setting trait_header="+prev_trait_header);
          Log.d("MonsFrag","adding TextView to trait_tvs");
          //trait_tvs.add(traits_name_view);
        }

        //Log.d("MonsFRag","Traits TVS size after header code="+trait_tvs.size());

        TextView traits_text_view = new TextView(getContext());
        //Log.d("MonsFrag","Setting traits_text_view id="+trait_tvs.size()+" and trait_text as="+trait_texts.get(i));
        //traits_text_view.setId(trait_tvs.size());
        traits_text_view.setText(trait_texts.get(i));

        //Log.d("MonsFrag","Create the relativelayout layoutparams for traits_text_view");
        //RelativeLayout.LayoutParams x = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //TextView tmp2 = (TextView) trait_tvs.get(trait_tvs.size()-1);
        //p.removeRule(RelativeLayout.BELOW);
        //x.addRule(RelativeLayout.BELOW, tmp2.getId());

        //Log.d("MonsFrag","Adding tmp2 text getid="+tmp2.getId()+" with text as="+tmp2.getText());
        //traits_text_view.setLayoutParams(x);
        traits_text_view.setLinkTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.dndColourGrey));
        // left top right bottom
        traits_text_view.setPadding(0, 0, 0, 5);
        traits_llm.addView(traits_text_view);
        //trait_tvs.add(traits_text_view);

        //Log.d("MonsFRag","End Traits TVS="+trait_tvs.size());
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

      /*
      for(int i=0; i<trait_tvs.size();i++)
      {
        TextView result = (TextView) trait_tvs.get(i);
        Log.d("MonsFrag","The current id="+result.getId()+" and the current tex="+result.getText());
        traits_llm.addView((TextView) trait_tvs.get(i));
      }
      */
    }
  }

  private void createActions(View view)
  {
    getActions();

    //List act_tvs = new ArrayList<TextView>();

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
          //RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

          /*
          if(act_tvs.size()==0)
          {
            p.addRule(RelativeLayout.BELOW, R.id.actions_header_tv);
          }
          else
          {
            TextView tmp = (TextView) act_tvs.get(i-1);
            p.addRule(RelativeLayout.BELOW, tmp.getId());
          }

          actions_name_view.setLayoutParams(p);
          */
          actions_name_view.setLinkTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.dndColourGrey));
          actions_name_view.setTypeface(actions_name_view.getTypeface(), Typeface.BOLD);
          // left top right bottom
          actions_name_view.setPadding(0, 5, 0, 0);
          actions_llm.addView(actions_name_view);
          //act_tvs.add(actions_name_view);
          prev_action_header = action_name_header;
        }

        TextView actions_text_view = new TextView(getContext());
        actions_text_view.setText(action_texts.get(i));

        //RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //TextView tmp = (TextView) act_tvs.get(i);
        //p.addRule(RelativeLayout.BELOW, tmp.getId());
        //actions_text_view.setLayoutParams(p);
        actions_text_view.setLinkTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.dndColourGrey));
        // left top right bottom
        actions_text_view.setPadding(0, 0, 0, 5);
        actions_llm.addView(actions_text_view);
        //act_tvs.add(actions_text_view);

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

      /*
      for (int i=0;i<act_tvs.size();i++)
      {
        actions_llm.addView((TextView) act_tvs.get(i));
      }
      */
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
