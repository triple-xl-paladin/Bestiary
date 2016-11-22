package com.oddjobs.bestiary.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Handles all database calls.
 * <P></P>
 * Created by Alexander Chen on 02/03/16.
 * <P></P>
 * <b>To do</b>
 * <ul>
 * <li></li>
 * </ul>
 * <p></p>
 * <b>Change Log</b>
 * <ul>
 * <li></li>
 * </ul>
 *
 * @author Alexander Chen
 * @version %I%, %G%
 * @since 0.1
 */
public class DBHandler
{
  String db_name = "monsters.sqlite3";
  //String db_path = "/data/data/com.oddjob.draginet/databases/";
  String db_path = ""; // Path is determined in constructor from Context
  Context context;
  SQLiteDatabase db;
  //Logger log;

  /**
   * Constructor. The constructor will determine if the database already exists. If it doesnt, it
   * will copy the database from the assets.
   *
   * @param context //* @param log
   */
  //public DBHandler(Context context, Logger log)
  public DBHandler(Context context)
  {
    this.context = context;
    //this.log = log;
    //this.logFile = logFile;

    // databases folder does not exist by default, therefore the next line is bullshit. Need to
    // create directory on initialization. Easier just to use the files folder instead
    //db_path = context.getApplicationInfo().dataDir + "/databases/";

    // Get the path to files directory instead
    db_path = context.getFilesDir().getAbsolutePath();
    //l("Constructor db_path: " + db_path);

    // Alternatively can use SQLiteOpenHelper to do the following steps

    // Check if db exists
    if (!checkDB())
    {
      // This should only happen on first time initialization of app
      // Copies the database to the files directory
      copyDatabase();
      //l("Constructor copy db: ");
    }
  }

  /**
   * Opens the database as read write if the database isn't already open
   *
   * @return True if the database has been opened. False unlikely to happen as
   * I expect exception to be thrown first
   */
  public boolean openDatabase()
  {
    boolean opendbcheck = false;

    if (db == null)
    {
      db = SQLiteDatabase.openDatabase(db_path + "/" + db_name, null, SQLiteDatabase.OPEN_READWRITE);
      //l("openDatabase(): Database opened");
      l(db_path + "/" + db_name);
      opendbcheck = true;
    }
    else
      l("openDatabase(): Database already open!");

    return opendbcheck;
  }

  /**
   * Closes the open database
   *
   * @return True if the database is closed, false if it can't close it.
   */
  public boolean closeDatabase()
  {
    boolean check = db.isOpen();

    if (check)
    {
      //l("closeDatabase(): path " + db.getPath());
      db.close();
      //l("closeDatabase(): db closed");
    }
    else
    {
      check = false;
      //l("closeDatabase(): db was never open in the first place");
    }

    return check;
  }


  public Cursor executeQuerySQL(String sql)
  {
    boolean check = false;
    Cursor recordSet = null;

    try
    {
      if (db.isOpen())
      {
        recordSet = db.rawQuery(sql, null);
        //l("executeQuery(): recordset count = " + String.valueOf(recordSet.getCount()));

        check = true;
      }
    } catch (SQLException e)
    {
      e.printStackTrace();
    }

    //l("executeQuery(): recordset finished " + String.valueOf(check));

    return recordSet;
  }

  /**
   * Executes a SQL query based on sql code stored in a file. The method returns a Cursor object
   * containing the results from the query. Use for select queries only.
   *
   * @param filename The filename containing the SQL code
   * @return A Cursor object containing the results of the SQL query
   */
  public Cursor executeQueryFile(String filename)
  {
    boolean check = false;
    Cursor recordSet = null;

    try
    {
      if (db.isOpen())
      {
        //l("executeQuery(): opening sql file = " + filename);
        DroidFileHandler dfh = new DroidFileHandler(context, "sql/" + filename);
        String sql = dfh.readFile();

        //l("executeQuery(): about to execute sql file = " + sql);
        recordSet = db.rawQuery(sql, null);
        //String [] args = {"_id","spell_id","name"};
        //recordSet = db.query("tbSpells",args,null,null,null,null,null,null);

        //l("executeQuery(): recordset count = " + String.valueOf(recordSet.getCount()));

        check = true;
      }
    } catch (SQLException e)
    {
      e.printStackTrace();
    }

    //l("executeQuery(): recordset finished " + String.valueOf(check));

    return recordSet;
  }

  /**
   * Executes a SQL query based on sql code stored in the db. The method returns a Cursor object
   * containing the results from the query. Use for select queries only.
   *
   * @param sql_id The primary key containing the SQL code
   * @return A Cursor object containing the results of the SQL query
   */
  public Cursor executeQuerySqlID(String sql_id)
  {
    Cursor recordSet = null;
    boolean check = false;

    // The table name to compile the query against.
    String table = "sql_code";

    // A list of which columns to return. Passing null will return all columns, which is
    // discouraged to prevent reading data from storage that isn't going to be used.
    String[] cols = {"sql_id", "sql_code", "sql_comments"};

    // A filter declaring which rows to return, formatted as an SQL WHERE clause (excluding the
    // WHERE itself). Passing null will return all rows for the given table.
    String selection = "sql_id";

    // You may include ?s in selection, which will be replaced by the values from selectionArgs, in
    // order that they appear in the selection. The values will be bound as Strings
    String[] selectionArgs = {sql_id};

    // A filter declaring how to group rows, formatted as an SQL GROUP BY clause (excluding the
    // GROUP BY itself). Passing null will cause the rows to not be grouped.
    String groupBy = null;

    // A filter declare which row groups to include in the cursor, if row grouping is being used,
    // formatted as an SQL HAVING clause (excluding the HAVING itself). Passing null will cause all
    // row groups to be included, and is required when row grouping is not being used.
    String having = null;

    // How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself).
    // Passing null will use the default sort order, which may be unordered.
    String orderBy = null;

    // Limits the number of rows returned by the query, formatted as LIMIT clause. Passing null
    // denotes no LIMIT clause.
    String limit = null;

    try
    {
      // Check if db is open
      if (db.isOpen())
      {
        recordSet = db.query(table, cols, selection, selectionArgs, groupBy, having, limit);
        check = true;
      }
      else
        l("executeQuerySqlID(): DB is not open!");
    } catch (SQLException e)
    {
      e.printStackTrace();
    }

    return recordSet;
  }

  /**
   * Executes a custom SQL query based on the parameters. The method returns a Cursor object
   * containing the results from the query. Use for select queries only.Can not do joins.
   *
   * @param table         The table name to compile the query against.
   * @param cols          A list of which columns to return. Passing null will return all columns, which is
   *                      discouraged to prevent reading data from storage that isn't going to be used.
   * @param selection     A filter declaring which rows to return, formatted as an SQL WHERE clause
   *                      (excluding the WHERE itself). Passing null will return all rows for
   *                      the given table.
   * @param selectionArgs You may include ?s in selection, which will be replaced by the values
   *                      from selectionArgs, in order that they appear in the selection. The
   *                      values will be bound as Strings
   * @param groupBy       A filter declaring how to group rows, formatted as an SQL GROUP BY clause
   *                      (excluding the GROUP BY itself). Passing null will cause the rows to not be
   *                      grouped.
   * @param having        A filter declare which row groups to include in the cursor, if row grouping
   *                      is being used, formatted as an SQL HAVING clause (excluding the HAVING itself).
   *                      Passing null will cause all row groups to be included, and is required when row
   *                      grouping is not being used.
   * @param orderBY       How to order the rows, formatted as an SQL ORDER BY clause (excluding the
   *                      ORDER BY itself).Passing null will use the default sort order, which may be
   *                      unordered.
   * @param limit         Limits the number of rows returned by the query, formatted as LIMIT clause.
   *                      Passing null denotes no LIMIT clause.
   * @return A Cursor object containing the results of the SQL query
   */
  public Cursor executeQuery(String table, String[] cols, String selection, String[] selectionArgs, String groupBy, String having, String orderBY, String limit)
  {
    Cursor recordSet = null;
    boolean check = false;

    try
    {
      // Check if db is open
      if (db.isOpen())
      {
        //l("executeQuerySqlID(): About to execute query");
        recordSet = db.query(table, cols, selection, selectionArgs, groupBy, having, limit);
        check = true;
        //l("executeQuerySqlID(): Query executed and check=" + String.valueOf(check));
      }
      else
        l("executeQuerySqlID(): DB is not open!");
    } catch (SQLException e)
    {
      e.printStackTrace();
    }

    return recordSet;
  }

  /**
   * Inserts data into tables using the ContentValues container.
   *
   * @param table_name    Name of the table
   * @param contentValues The contents to be inserted into the table
   * @return Success or failure of insert
   */
  public boolean insertQuery(String table_name, ContentValues contentValues)
  {
    boolean insert = false;
    long rowid = 0;

    try
    {
      if (db.isOpen())
      {
        rowid = db.insert(table_name, null, contentValues);
        insert = true;
        //l("insertQuery(): insert rowid=" + rowid);
      }
      else
        l("insertQuery(): db is not open");
    } catch (SQLException e)
    {
      e.printStackTrace();
    }

    return insert;

  }

  /**
   * Converts primitive float array to Float object array
   *
   * @param values primitive float array
   * @return Float object array
   */
  private Float[] convertFloat(float[] values)
  {
    Float[] floatArray = new Float[values.length];

    for (int i = 0; i < values.length; i++)
      floatArray[i] = Float.valueOf(values[i]);

    return floatArray;
  }

  /**
   * Converts primitive int array to Integer object array
   *
   * @param values primitive int array
   * @return Integer object array
   */
  private Integer[] convertInt(int[] values)
  {
    Integer[] intArray = new Integer[values.length];

    for (int i = 0; i < values.length; i++)
      intArray[i] = Integer.valueOf(values[i]);

    return intArray;
  }

  private <T> ContentValues addContent(ContentValues vals, String[] cols, T[] values)
  {
    if (cols != null && (cols.length == values.length))
    {
      //l("insertQuery(): Array lengths are the same. r_cols=" + String.valueOf(cols.length) + " r_values=" + String.valueOf(values.length));

      for (int i = 0; i < cols.length; i++)
      {
        //Object o = values[i];

        switch (values[i].getClass().getName())
        {
          case "java.lang.String":
            vals.put(cols[i], (String) values[i]);
            l("addContent(): cols=" + cols[i] + " value=" + (String) values[i]);
            break;
          case "java.land.Integer":
            vals.put(cols[i], (Integer) values[i]);
            l("addContent(): cols=" + cols[i] + " value=" + String.valueOf((Integer) values[i]));
            break;
          case "java.lang.Float":
            vals.put(cols[i], (Float) values[i]);
            l("addContent(): cols=" + cols[i] + " value=" + String.valueOf((Float) values[i]));
            break;
          case "java.lang.Double":
            vals.put(cols[i], (Double) values[i]);
            l("addContent(): cols=" + cols[i] + " value=" + String.valueOf((Double) values[i]));
            break;
          case "java.lang.Short":
            vals.put(cols[i], (Short) values[i]);
            l("addContent(): cols=" + cols[i] + " value=" + String.valueOf((Short) values[i]));
            break;
          case "java.lang.Long":
            vals.put(cols[i], (Long) values[i]);
            l("addContent(): cols=" + cols[i] + " value=" + String.valueOf((Long) values[i]));
            break;
          case "java.lang.Byte":
            vals.put(cols[i], (Byte) values[i]);
            l("addContent(): cols=" + cols[i] + " value=" + String.valueOf((Byte) values[i]));
            break;
          case "java.lang.Boolean":
            vals.put(cols[i], (Boolean) values[i]);
            l("addContent(): cols=" + cols[i] + " value=" + String.valueOf((Boolean) values[i]));
            break;
        }
      }
    }
    else
    {
      if (cols != null)
        l("addContent():: The two arrays are of different lengths! r_cols=" + String.valueOf(cols.length) + " r_values=" + String.valueOf(values.length));
      else
        l("addContent():: no entries for cols");
    }

    return vals;
  }

  /**
   * Debug messages to log file.
   *
   * @param mesg The message to post.
   */
  private void l(String mesg)
  {
    Log.d("DBHandler: ", mesg);
  }

  /**
   * Check if the database file exists on the file system
   *
   * @return True if the file exists, false otherwise.
   */
  private boolean checkDB()
  {
    File file = new File(db_path + "/" + db_name);

    if (file.exists())
      return true;
    else
      return false;
  }

  /**
   * DO NOT USE. Debugging mode onlu method. Deletes the database file.
   */
  public boolean deleteDB()
  {
    // Opens the file
    File file = new File(db_path + "/" + db_name);
    boolean delete = false;

    if (file.exists())
    {
      delete = file.delete();
      l("deleteDB(): File deleted");
    }
    else
      l("deleteDB(): File does not exist");

    return delete;
  }

  /**
   * Copies the database from the assets folder onto the internal drive.
   */
  public void copyDatabase()
  {
    //l("copyDataBase(): start");

    String fromdb = "db/" + db_name; // location of the db in assets
    String todb = db_path + "/" + db_name; // get the destination location. Path includes the filename.

    //l("copyDataBase(): Asset location: " + fromdb);
    //l("copyDataBase(): Destination: " + todb);

    try
    {
      //Open your local db as the input stream
      InputStream myInput = context.getAssets().open(fromdb);

      //l("copyDataBase(): AssetDB opened");

      //Open the empty db as the output stream
      OutputStream myOutput = new FileOutputStream(todb);

      //l("copyDataBase(): Dest open");

      //transfer bytes from the inputfile to the outputfile
      byte[] buffer = new byte[1024];
      int length;

      while ((length = myInput.read(buffer)) > 0)
      {
        //Log.d("Draginet",new Integer(length).toString());
        myOutput.write(buffer, 0, length);
      }

      //Close the streams
      myOutput.flush();
      myOutput.close();
      myInput.close();
    } catch (IOException e)
    {
      e.printStackTrace();
    }

    //l("copyDataBase(): finich copy");
  }

  /**
   * Finds the max value on the primary key of table. e.g.
   * <pre>
   *   {@Code
   *     SELECT max(spell_bk) from tbSpells;
   *   }
   * </pre>
   * The column must be an INTEGER column for the query to function, or it will throw an SQLException
   *
   * @param table The table to find max on
   * @return The highest number found in the column.
   */
  public int findMaxValue(String table, String column)
  {
    String sql = "SELECT MAX(" + column + ") as max_id FROM " + table + ";";
    Cursor maxCursor;
    int max = 0;

    if (db.isOpen())
    {
      //l("findMaxKeyValue(): About to execute query=" + sql);
      maxCursor = db.rawQuery(sql, null);

      if (maxCursor != null)
      {
        //l("findMaxKeyValue(): Query returned something.");
        // Move cursor to beginning
        maxCursor.moveToFirst();

        max = maxCursor.getInt(maxCursor.getColumnIndex("max_id"));
        //l("findMaxKeyValue(): max value=" + String.valueOf(max));
      }

      maxCursor.close();
    }
    return max;
  }
}
