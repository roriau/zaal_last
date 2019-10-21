package mn.zaal.zaal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.DimenHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import mn.zaal.zaal.tuslah.DatabaseHandler;
import mn.zaal.zaal.tuslah.Functions;
import mn.zaal.zaal.tuslah.SessionManager;
import timber.log.Timber;

import static mn.zaal.zaal.MyApplication.isOnline;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    TextView forgot,signUp;
    ImageView signIn;
    ImageView backButton;
    private SessionManager sm;
    private ProgressDialog pDialog;
    private DatabaseHandler db;
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PHONENUMBER = "phone_number";
    private static final String KEY_CREATED_AT = "created_at";
    private AccountHeader headerResult = null;
    private Drawer result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.login_et);
        password = findViewById(R.id.password_et);
        forgot = findViewById(R.id.forget_tv);
        signIn = findViewById(R.id.signIn);
        signUp = findViewById(R.id.signUp);
        sm = new SessionManager(this);
        pDialog = new ProgressDialog(this);
        db = new DatabaseHandler(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        backButton = findViewById(R.id.back);
        signUp.setOnClickListener(v -> {
            Intent signUpIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(signUpIntent);
        });

        backButton.setOnClickListener(v -> onBackPressed());

        signIn.setOnClickListener(v -> {
            String login = username.getText().toString().trim();
            String pass = password.getText().toString().trim();
            if (!TextUtils.isEmpty(login) & !TextUtils.isEmpty(pass)) {
                if (isOnline()) {
                    loginProcess(login, pass);
                } else {
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Та интернэт-д холбогдсон байх шаардлагатай.", Snackbar.LENGTH_LONG)
                            .setAction("Хаах", view -> {
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                            .show();
                }
            } else {
                View parentLayout = findViewById(android.R.id.content);
                Snackbar.make(parentLayout, "Бүх талбарыг бөглөнө үү.", Snackbar.LENGTH_LONG)
                        .setAction("Хаах", view -> {
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                        .show();
            }
        });

            createGuestAccountHeader();
            result = new DrawerBuilder(this)
                    .withToolbar(toolbar)
                    .withAccountHeader(headerResult)
                    .withHeaderHeight(DimenHolder.fromDp(200))
                    .withHeaderDivider(false)
                    .withTranslucentStatusBar(false)
                    .withActionBarDrawerToggle(true)
                    .withDrawerWidthDp(250)
                    .withActionBarDrawerToggleAnimated(true)
                    .withInnerShadow(false)
                    .withSliderBackgroundColor(Color.WHITE)
                    .withActionBarDrawerToggle(true)
                    .withTranslucentNavigationBar(false)
                    .withOnDrawerListener(new Drawer.OnDrawerListener() {
                        @Override
                        public void onDrawerOpened(View drawerView) {

                        }

                        @Override
                        public void onDrawerClosed(View drawerView) {
                        }

                        @Override
                        public void onDrawerSlide(View drawerView, float slideOffset) {
                        }
                    })

                    .addDrawerItems(initDrawerItems())
                    .withSavedInstance(savedInstanceState)
                    .withSelectedItem(-1)
                    .withDrawerGravity(Gravity.START)
                    .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                        if (drawerItem != null) {
                            switch ((int) drawerItem.getIdentifier()) {
                                case 1:
                                    Intent homeIntent = new Intent(MyApplication.getAppContext(), MainActivity.class);
                                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    MyApplication.getAppContext().startActivity(homeIntent);
                                    break;
                                case 2:
                                    break;
                                case 3:
                                    break;
                                case 4:
                                    break;
                                case 5:

                                    break;
                                case 6:
                                    break;
                                case 7:
                                    break;
                            }
                        }
                        return false;
                    }).build();

    }

    @NonNull
    private IDrawerItem[] initDrawerItems() {
             return new IDrawerItem[]{new PrimaryDrawerItem().withIdentifier(1).withName("Нүүр").withSetSelected(true)
            };
    }

    private void createGuestAccountHeader() {
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTextColorRes(R.color.material_drawer_dark_header_selection_text)
                .withHeaderBackground(R.drawable.header)
                .withDividerBelowHeader(true)
                .build();
    }

    private void loginProcess (String username, String password) {
        pDialog.show();
        pDialog.setMessage("Уншиж байна...");
        pDialog.setCancelable(false);
        pDialog.setOwnerActivity(this);
        StringRequest strReq = new StringRequest(Request.Method.POST, Functions.LOGIN_URL, (String response) -> {
            pDialog.dismiss();
            Timber.d("Login Response: %s", response);
            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                String is_active = jObj.getString("is_active");
                if (!error) {
                    switch (is_active) {
                        case "1":
                            JSONObject json_user = jObj.getJSONObject("user");
                            db.addUser(json_user.getString(KEY_EMAIL),
                                       json_user.getString(KEY_NAME),
                                       json_user.getString(KEY_USERNAME),
                                       json_user.getString(KEY_PHONENUMBER),
                                       json_user.getString(KEY_CREATED_AT));
                            sm.setIsLoggedin(true);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        break;
                        case "2":
                            Intent activationIntent = new Intent(LoginActivity.this, ActivationActivity.class);
                            startActivity(activationIntent);
                            //TODO send Email
                        break;
                        case "3":
                            View parentLayout = findViewById(android.R.id.content);
                            Snackbar.make(parentLayout, "Нэвтрэх нэр, нууц үг буруу байна.", Snackbar.LENGTH_LONG)
                                    .setAction("Хаах", view -> {
                                    })
                                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                                    .show();
                        break;
                    }
                } else {
                    // Error in login. Get the error message
                    String errorMsg = jObj.getString("message");
                    Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            Timber.e("Алдаа: %s", error.getMessage());
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("login", username);
                params.put("password", password);
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}
