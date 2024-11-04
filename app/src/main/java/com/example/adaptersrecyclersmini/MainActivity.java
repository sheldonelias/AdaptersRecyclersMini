package com.example.adaptersrecyclersmini;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    //Instance Variables found below Android LifeCycle Methods

    //ANDROID LIFECYLE METHOD
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialzes one recycler view, there can be more than one
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        // Because data depends on IO, must be in try/catch with IOException
        // in case connection fails
        try
        {
            pullData();

        }catch(IOException exception)
        {
            System.out.println(exception.getMessage());
        }

        //Recyclers must use the LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Recyclers must have an adapter
        //tuples ArrayList object is sent to Adaper to expand with RecyclerView object
        recyclerView.setAdapter(new MyAdapter(getApplicationContext(), tuples));
    }

    //INSTANCE VARIABLES

    // Declared to hold each tuple of data from contacts
    List<Item> tuples;

    // Declared for file coming from internal source:
    // Device Explorer: /data/data/<nameOfPackage>/files/<nameOfFile>
    Scanner fileScan;

    //Storing a file name in string variable
    String MY_FILE_NAME;
    /*
    pullData(): Gets data from a CSV file that is functioning as a stub for database. Data is
    fed into
     */
    protected void pullData() throws IOException
    {
        tuples = new ArrayList<Item>();

        //File stored in phone, to be replaced by stream from database
        MY_FILE_NAME = "contacts.csv";
        //Gets the internal path of the file
        File path = getApplicationContext().getFilesDir();
        //Converts the text file to a File object
        File readFrom = new File(path, MY_FILE_NAME);
        //Creates a byte[] array of the correct number of indices
        byte[] content = new byte[(int) readFrom.length()];

        try
        {
            //Creates a new file input stream using the file, readFrom
            FileInputStream fileis = new FileInputStream(readFrom);
            //Reads the fileis stream into the content byte[] array
            fileis.read(content);
            //Creates a string object from the byte[] array object
            String stringContent = new String(content);

            //Debugging
            System.out.println(stringContent);
            //Scanner contains file data in structured multi-d array
            fileScan = new Scanner(stringContent);
            //Used to hold one tuple of data
            String[] line;

            //Loop goes through all linw content in text file
            while(true)
            {
                //Stops loop when no more lines
                if(!fileScan.hasNext())
                {
                    break;
                }

                //Declared to hold data to pass to Item constructor
                //Can be eliminated if strArr[] replaces named String variable for
                //constructor call to time
                String name;
                String email;
                String pictureResourceName;

                //split() converts a string into a string array with a delimiter
                //to determine division of content for each array element
                line = fileScan.nextLine().split(",");
                pictureResourceName = line[0]; //This carries the string portion of the resource name
                name = line[1];
                email = line[2];

                //Debugging check
                System.out.println("picture: " + pictureResourceName);
                //Debugging check. Notice how picture is used in
                System.out.println(
                        getResources().getIdentifier(pictureResourceName,"drawable", getPackageName()));

                //Instantiates item that gets added to tuples ArrayList
                Item item = new Item(name, email,
                        getResources().getIdentifier(pictureResourceName,"drawable", getPackageName()));

                //Adds one item, a tuple, to tuples ArrayList
                tuples.add(item);
            }

            //Use return if needed
            //return new String(content);

        } catch (Exception exception)
        {
            exception.printStackTrace();
            String msg = exception.toString();
            Toast.makeText(getApplicationContext(),"Trouble with IO with input." + msg,
                    Toast.LENGTH_SHORT).show();
            Log.wtf("XXX", "Trouble with IO with input." + msg);
            //Use return if needed
            //return e.toString();
        }
    }
}