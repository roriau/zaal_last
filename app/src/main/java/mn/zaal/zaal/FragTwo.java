package mn.zaal.zaal;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import mn.zaal.zaal.adapters.FragTwoRecyclerView;
import mn.zaal.zaal.models.zaalModel;
import mn.zaal.zaal.tuslah.DatabaseJson;
import mn.zaal.zaal.tuslah.Functions;
import timber.log.Timber;


public class FragTwo extends Fragment {
    private ProgressDialog progressDialog;
    private List<zaalModel> allZaals = new ArrayList<>();
    private View view;
    private FragTwoRecyclerView adapter;
    private DatabaseJson databaseJson;
    private SearchView searchView;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_frag_two, container, false);
            databaseJson = new DatabaseJson(getContext());
            progressDialog = new ProgressDialog(getContext(),R.style.AppCompatAlertDialogStyle);
            adapter = new FragTwoRecyclerView(getContext(), allZaals);
            RecyclerView recyclerView = view.findViewById(R.id.fragTwoRecycler);
            ((SimpleItemAnimator) Objects.requireNonNull(recyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
            recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL));
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
            int count = databaseJson.getRowCount();
            getCount(count);
        }
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    private void getCount(int dbCount) {
        StringRequest request2 = new StringRequest(Request.Method.GET, Functions.COUNTDB_URL, (String response) -> {
            Timber.d(response);
            try {
                JSONArray jsonArray = new JSONArray(response);
                JSONObject obj = jsonArray.getJSONObject(0);
                int count = Integer.valueOf(obj.getString("count"));
                if (dbCount != count) {
                    progressDialog.setMessage("Уншиж байна...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        allZaals.addAll(databaseJson.allZaal());
                        progressDialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }, 2000);
                } else {
                    allZaals.addAll(databaseJson.allZaal());
                    adapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> Timber.e(error.toString())) {
        };
        MyApplication.getInstance().addToRequestQueue(request2);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
}
