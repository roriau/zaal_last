package mn.zaal.zaal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import mn.zaal.zaal.tuslah.DatabaseHandler;
import mn.zaal.zaal.tuslah.SessionManager;


public class FragThree extends Fragment {
    private View view;
    private SessionManager sm;
    private DatabaseHandler db;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sm = new SessionManager(MyApplication.getAppContext());
        db = new DatabaseHandler(MyApplication.getAppContext());
        if(view == null){
            if (sm.isLoggedin()){
                //TODO
                view = inflater.inflate(R.layout.fragment_frag_three, container, false);
                View loginButton = view.findViewById(R.id.loginButton);
                ((ViewManager)loginButton.getParent()).removeView(loginButton);
            } else {
                view = inflater.inflate(R.layout.fragment_frag_three, container, false);
                MaterialButton loginButton = view.findViewById(R.id.loginButton);
                loginButton.setOnClickListener(v -> {
                    Intent loginIntent = new Intent(MyApplication.getAppContext(), LoginActivity.class);
                    MyApplication.getAppContext().startActivity(loginIntent);
                });
            }
        }
        return view;
    }
}
