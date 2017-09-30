package activitys;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xx.hello.hellondk.DimensionUtil;
import com.xx.hello.hellondk.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hello on 2017/8/18.
 */

public class ListActivity extends Activity {

    private List<Person> mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mData = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Person person = new Person();
            person.o = "你好" + i;
            mData.add(person);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        QuickAdapter adapter = new QuickAdapter();
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(R.mipmap.itembg);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DimensionUtil.dip2px(this,45)));
        adapter.addHeaderView(imageView);
        recyclerView.setAdapter(adapter);

    }

    class Person {
        public String o;
    }

    class QuickAdapter extends BaseQuickAdapter<Person, BaseViewHolder> {

        public QuickAdapter() {
            super(R.layout.list_item, ListActivity.this.mData);
        }

        @Override
        protected void convert(BaseViewHolder helper, Person item) {
            helper.setText(R.id.item_text, item.o);
        }
    }
}
