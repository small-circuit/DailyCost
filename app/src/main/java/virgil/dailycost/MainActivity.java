package virgil.dailycost;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import virgil.dailycost.adapter.DailyCostEditorAdapter;
import virgil.dailycost.adapter.DayAdapter;
import virgil.dailycost.models.SubjectSpend;

public class MainActivity extends AppCompatActivity {

    private DailyCostEditorAdapter mAdapter;
    private List<SubjectSpend> subjectSpends = new ArrayList<>();
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        subjectSpends.add(new SubjectSpend("ds",2d));
//        subjectSpends.add(new SubjectSpend("ds",2d));
//
//        mAdapter = new DailyCostEditorAdapter(subjectSpends);
//
//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_trial);
//        recyclerView.setNestedScrollingEnabled(false);
//
//        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(mAdapter);
    }

}
