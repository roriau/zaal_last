package mn.zaal.zaal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import mn.zaal.zaal.adapters.FragOneRecyclerView;
import mn.zaal.zaal.models.zaalModel;
import mn.zaal.zaal.tuslah.DatabaseJson;
import mn.zaal.zaal.tuslah.Functions;
import mumayank.com.airlocationlibrary.AirLocation;
import timber.log.Timber;

public class FragOne extends Fragment {
    private AirLocation airLocation;

    private List<zaalModel> allZaals = new ArrayList<>();
    private RecyclerView recyclerView;
    private FragOneRecyclerView adapter;

    private ProgressDialog dialog;
    private View view;
    private DatabaseJson databaseJson;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_frag_one, container, false);
            databaseJson = new DatabaseJson(getContext());
            dialog = new ProgressDialog(getContext(),R.style.AppCompatAlertDialogStyle);
            adapter = new FragOneRecyclerView(getContext(),allZaals);
            RecyclerView recyclerView = view.findViewById(R.id.homeRecyclerView);
            recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL));
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),2);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
            int count = databaseJson.getRowCount();
            getCount(count);
        }

        airLocation = new AirLocation(Objects.requireNonNull(getActivity()), true, true, new AirLocation.Callbacks() {
            @Override
            public void onSuccess(@NotNull Location location) {
                // do something
            }

            @Override
            public void onFailed(@NotNull AirLocation.LocationFailedEnum locationFailedEnum) {
                // do something
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        airLocation.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private void getCount(int dbCount) {
        StringRequest request2 = new StringRequest(Request.Method.GET, Functions.COUNTDB_URL, (String response) -> {
            Timber.d(response);
            try {
                JSONArray jsonArray = new JSONArray(response);
                JSONObject obj = jsonArray.getJSONObject(0);
                int count = Integer.valueOf(obj.getString("count"));
                if (dbCount != count) {
                    dialog.setMessage("Уншиж байна...");
                    dialog.setCancelable(false);
                    dialog.show();
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        allZaals.addAll(databaseJson.allZaal());
                        dialog.dismiss();
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
}
