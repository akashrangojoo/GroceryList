package edu.niu.cs.akash.grocerylist;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import android.app.Activity;
import android.widget.ListView;

public class MainActivity extends Activity {

    DBAdapter myDB;
    MyAdapter dataAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new DBAdapter(this);
        myDB.open();
    }

    protected void onDestroy() {
        super.onDestroy();
        myDB.close();
    }

    private void displayText( String message ){
        TextView dbContents = (TextView)findViewById(R.id.statusTextView);
        dbContents.setText(message);
    }//end of displayText

    public void addRecord(View view){
        EditText editText=(EditText) findViewById(R.id.ItemsEditText);
        String desc = editText.getText().toString();
        if(!desc.equals("")) {
            long newID = myDB.insertRow(desc,0);
            displayText("Added new item : "+newID );
        }
        else displayText(" Enter an item ");
    }//end of addRecord

    public void delete(View view){
        displayText("Deleted all the items.");
        myDB.deleteAll();
    }//end of delete

    public void displayDB(View view){
        ArrayList<Grocery_Item> grocery_items = new ArrayList<Grocery_Item>();
        displayText("Showing...");
        Cursor cursor = myDB.getAllRows();
        if(cursor.moveToFirst()){
            String message = "";
            boolean isData = cursor.moveToFirst();
            while(isData){
                int id = cursor.getInt(DBAdapter.COL_ROWID),
                        checked = cursor.getInt(DBAdapter.COL_ISDONE);
                String desc = cursor.getString(DBAdapter.COL_DESCRIPTION);
                Grocery_Item temp=new Grocery_Item();
                temp.setId(id);
                temp.setDescription(desc);
                temp.setIsDone(checked);
                grocery_items.add(temp);
                isData = cursor.moveToNext();
            }
            displayText(message);
        }
        cursor.close();
        dataAdapter = new MyAdapter(this,R.layout.grocery_item,grocery_items);
        ListView listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(dataAdapter);
    }//end of displayDB



    private class MyAdapter extends ArrayAdapter<Grocery_Item>
    {
        Context context;
        ArrayList<Grocery_Item> taskList = new ArrayList<Grocery_Item>();

        //Constructor
        //
        //c is the current context
        //resourceId is the resource ID for a layout file to use when instantiating views
        //objects are the objects to represent in the ListView

        public MyAdapter(Context c, int resourceId, ArrayList<Grocery_Item> objects)
        {
            super(c, resourceId, objects);
            taskList = objects;
            context = c;
        }//end constructor


        //getView
        //Get a View that displays the data at a specified position in the data set
        //
        //position is the position of the item within the adapter's data set
        //convertView is the old view
        //parent is the parent that the view will be attached to

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            CheckBox isDoneCB = null;

            if( convertView == null )
            {
                //Get a layout inflater so that the layout can be inflated in the ListView
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);

                //Use the custom layout for a grocery item for each grocery item in the ListView
                convertView = inflater.inflate(R.layout.grocery_item, parent, false);

                //Connect to the checkbox on the custom layout
                isDoneCB = (CheckBox)convertView.findViewById(R.id.checkBox1);

                //Store the checkbox state with the view
                convertView.setTag(isDoneCB);

                //Listen for changes to the checkbox
                isDoneCB.setOnClickListener(new View.OnClickListener()
                {
                    //When the checkbox is clicked
                    @Override
                    public void onClick(View v)
                    {
                        //Create a named checkbox from the clicked checkbox
                        CheckBox checkBox = (CheckBox)v;

                        //Get the item that was selected
                        Grocery_Item changeItem = (Grocery_Item)checkBox.getTag();

                        //Set the is_done property for the specific grocery item
                        //A checked box indicates the grocery item has been purchased
                        if( checkBox.isChecked() )
                        {
                            changeItem.setIsDone(1);
                        }
                        else
                        {
                            changeItem.setIsDone(0);
                        }

                        //Update the specific item in the database
                        myDB.updateItem(changeItem);
                    }
                });//end of onClickListener for checkbox
            }
            else
            {
                isDoneCB = (CheckBox)convertView.getTag();
            }

            //Get the item that was selected from the ListView
            Grocery_Item current = taskList.get(position);

            //Set the information for the checkbox from the grocery item in the ListView
            isDoneCB.setText(current.getDescription());
            isDoneCB.setChecked(current.getIsDone() == 1 ? true : false);
            isDoneCB.setTag(current);

            //Return the item
            return convertView;
        }//end of getView

    }//end MyAdapter
}

