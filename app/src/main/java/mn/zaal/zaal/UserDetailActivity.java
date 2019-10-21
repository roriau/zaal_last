package mn.zaal.zaal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

import mn.zaal.zaal.tuslah.DatabaseHandler;
import mn.zaal.zaal.tuslah.SessionManager;

public class UserDetailActivity extends AppCompatActivity {
    DatabaseHandler db;
    SessionManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        db = new DatabaseHandler(this);
        sm = new SessionManager(this);
        HashMap<String, String> user = db.getUserDetails();
        String username = user.get("name");
        String email = user.get("email");

        TextView headerName,headerEmail;
        RelativeLayout headerChangePassword,headerOrders,headerHelp,headerAbout,headerContact,headerLogout;
        headerName = findViewById(R.id.headerName);
        headerEmail = findViewById(R.id.headerEmail);

        headerName.setText(email);
        headerEmail.setText(username);

        headerChangePassword = findViewById(R.id.headerChangePassword);
        headerOrders = findViewById(R.id.headerOrders);
        headerHelp = findViewById(R.id.headerHelp);
        headerAbout = findViewById(R.id.headerAbout);
        headerContact = findViewById(R.id.headerContact);
        headerLogout = findViewById(R.id.headerLogout);

        headerChangePassword.setOnClickListener(v -> {
            //TODO Password solih function hiih
        });

        headerOrders.setOnClickListener(v -> {
            //TODO Zahialgiin function hiih
        });
        headerHelp.setOnClickListener(v -> {
            //TODO Tuslamjiin function hiih
        });
        headerAbout.setOnClickListener(v -> {
            //TODO Bidnii tuhai function hiih
        });
        headerContact.setOnClickListener(v -> {
            //TODO Holboo barih function hiih
        });
        headerLogout.setOnClickListener(v -> {
            //TODO logout function hiih
        });

    }
}
