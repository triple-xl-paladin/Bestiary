package com.oddjobs.bestiary;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends AppCompatActivity
{
  private Fragment bestiary;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();;
    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

    if(savedInstanceState == null)
    {
      // Fragment not added to backstack!
      bestiary = new BestiaryListFragment();
      transaction.add(R.id.main_frame, bestiary, "bestiary_list_fragment");
    }
    else
    {
      bestiary = (BestiaryListFragment) getSupportFragmentManager().findFragmentByTag("bestiary_list_fragment");
      transaction.replace(R.id.main_frame, bestiary, "bestiary_list_fragment");
    }
    transaction.commit();

  }

  /**
   * Tells the activity what to do when the user hits the back button
   */
  @Override
  public void onBackPressed()
  {
    //Log.d("MainActivity", "about to remove frag");
    //Log.d("MainActivity", "frag backstackcnt="+getSupportFragmentManager().getBackStackEntryCount());
    //Log.d("MainActivity", "frag backstackcnt="+getFragmentManager().getBackStackEntryCount());
    getSupportFragmentManager().popBackStack();
    setUpButton(false);
    //Log.d("MainActivity", "frag should be removed");
    //Log.d("MainActivity", "frag backstackcnt="+getSupportFragmentManager().getBackStackEntryCount());
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    switch (item.getItemId())
    {
      case android.R.id.home:
        // Do something here. This is the event fired when up button is pressed.
        //setUpButton(false);
        onBackPressed();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * The method supports switching the out the fragment in the selected container.
   *
   * @param id The container to be used, such as main_frame
   * @param fragment The new fragment to replace the current one
   * @param name A tag for the new fragment
   */
  public void switchContent(int id, Fragment fragment, String name)
  {
    //Log.d("MainActivity","About to switch content");
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
    transaction.replace(id, fragment);
    transaction.addToBackStack(name);
    transaction.commit();
    //Log.d("MainActivity","New fragment should have appeared.");
  }

  /**
   * Make the action bar show the up button.
   *
   * @param isEnabled Show the up button (true) or don't show it (false)
   */
  public void setUpButton(boolean isEnabled)
  {
    if(isEnabled)
    {
      //Log.d("SoundTabsFrag", "actionbar=" + getSupportActionBar().getTitle());
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
      getSupportActionBar().setHomeButtonEnabled(true);
      //setTitle(R.string.soundtabs_fragment);
    }
    else
    {
      //Log.d("SoundTabsFrag", "actionbar=" + getSupportActionBar().getTitle());
      getSupportActionBar().setDisplayHomeAsUpEnabled(false);
      getSupportActionBar().setDisplayShowHomeEnabled(false);
      getSupportActionBar().setHomeButtonEnabled(false);
      //setTitle(R.string.soundscape_fragment);
    }
  }

}
