package com.example.zx.myapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> mItems = new ArrayList<String>();
    private IndexableListView mIndexableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init data
        initData();
        ContentAdapter adapter = new ContentAdapter(this, android.R.layout.simple_list_item_1, mItems);
        mIndexableListView = (IndexableListView) findViewById(R.id.listview);
        mIndexableListView.setAdapter(adapter);
        mIndexableListView.setFastScrollEnabled(true);

    }

    private void initData() {

        mItems.add("12345");
        mItems.add("AAAAAA");
        mItems.add("BBBBBB");
        mItems.add("CCCCCC");
        mItems.add("DDDDDD");
        mItems.add("EEEEEE");
        mItems.add("FFFFFF");
        mItems.add("GGGGGG");
        mItems.add("HHHHHH");
        mItems.add("IIIIII");
        mItems.add("JJJJJJ");
        mItems.add("KKKKKK");
        mItems.add("LLLLLL");
        mItems.add("MMMMMMM");
        mItems.add("NNNNN");
        mItems.add("OOOOO");
        mItems.add("PPPPPP");
        mItems.add("QQQQQQ");
        mItems.add("RRRRR");
        mItems.add("SSSSSSSSS");
        mItems.add("TTTTTTTT");
    }

    private class ContentAdapter extends ArrayAdapter<String> implements SectionIndexer {

        private String mSections = new String("#ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        public ContentAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        @Override
        public Object[] getSections() {
            //获得Sections中的每一个字符
            String[] sections = new String[mSections.length()];
            for (int i = 0; i < mSections.length(); i++) {
                sections[i] = String.valueOf(mSections.charAt(i));
            }
            return sections;
        }

        @Override
        public int getPositionForSection(int sectionIndex) {
            //从当前位置向前查询，直到查询到为止，否则不动
            for (int i = sectionIndex; i>=0; i--) {
                // 在listview中进行查询
                for (int j = 0; j < getCount(); j++) {
                    //查询数字
                    if (i == 0) {
                        for (int k = 0; k <=9; k++) {
                            if (StringMatcher.match(String.valueOf(getItem(j).charAt(0)), String.valueOf(k))) {
                                return j;
                            }
                        }
                    } else { // 查询字母
                        if (StringMatcher.match(String.valueOf(getItem(j).charAt(0)), String.valueOf(mSections.charAt(i)))) {
                            return j;
                        }
                    }
                }
            }
            return 0;
        }

        @Override
        public int getSectionForPosition(int position) {
            return 0;
        }
    }

}
