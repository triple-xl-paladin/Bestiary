package com.oddjobs.bestiary.db;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by aprc1 on 15/10/16.
 */

public class DroidFileHandler {
    BufferedReader r = null;
    String filename = null;
    Context context;

    /**
     * Constructor for file handler
     *
     * @param filename The file to be read or written. The file must exist within the assets directory of the Android app.
     */
    public DroidFileHandler(Context context, String filename)
    {
        this.filename = filename;
        this.context = context;
    }

    /**
     * The method returns the file as defined by the contructor.
     *
     * @return 	The contents of the file as a single text string.
     */
    public String readFile()
    {
        String out = new String();

        try
        {
            r = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));

            String mLine;

            while((mLine = r.readLine()) != null)
            {
                out += mLine;
            }
        }
        catch(IOException e)
        {
            //TODO: Log exception
            e.printStackTrace();
        }
        finally
        {
            if(r != null)
            {
                try
                {
                    r.close();
                }
                catch(IOException e)
                {
                    //TODO: Log exception
                    e.printStackTrace();
                }
            }
        }

        return out;
    }

    public void writeFile(String content)
    {
        // TODO: method to write contents to file
    }
}
