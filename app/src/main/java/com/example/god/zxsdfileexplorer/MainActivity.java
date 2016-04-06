package com.example.god.zxsdfileexplorer;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private TextView mTextView;
    private Button parent;

    private File currentParent;
    private File[] currentFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView= (ListView) findViewById(R.id.listView);
        mTextView= (TextView) findViewById(R.id.path);
        parent= (Button) findViewById(R.id.parent);
        File root=new File("mnt/sdcard");
        if(root.exists())
        {
            currentParent=root;
            currentFiles=root.listFiles();
            inflateListView(currentFiles);
        }

        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(!currentParent.getCanonicalPath().equals("/mnt/sdcard"))
                    {
                        currentParent=currentParent.getParentFile();
                        currentFiles=currentParent.listFiles();
                        inflateListView(currentFiles);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentFiles[position].isFile())
                {
                    return;
                }
                File[] temp=currentFiles[position].listFiles();
                if(temp==null||temp.length==0)
                {
                    Toast.makeText(MainActivity.this,"您点击的这个文件夹为空",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    currentParent=currentFiles[position];
                    currentFiles=temp;
                    inflateListView(currentFiles);
                }
            }
        });
    }



    private void inflateListView(File[] files)
    {
        List<Map<String,Object>> listItems=new ArrayList<>();
        for(int i=0;i<files.length;i++)
        {
            Map<String,Object> item=new HashMap<>();
            if(files[i].isDirectory())
            {
                item.put("icon",R.drawable.folder);
            }
            else
            {
                item.put("icon",R.drawable.file);
            }
            item.put("fileName",files[i].getName());
            listItems.add(item);
        }
        SimpleAdapter adapter=new SimpleAdapter(this,listItems,R.layout.line,new String[]{"icon","fileName"},new int[]{R.id.icon,R.id.file_name});
        mListView.setAdapter(adapter);
        mTextView.setText("");

    }
}
