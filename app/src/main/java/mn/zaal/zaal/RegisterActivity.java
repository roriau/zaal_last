package mn.zaal.zaal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;
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

public class RegisterActivity extends AppCompatActivity {
    ImageView backButton;
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private DatabaseHandler db;
    private SessionManager sm;
    private EditText register_email,register_username,register_phone,register_name,register_password,register_password_confirm;
    private ImageView register_button;
    private View parentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        backButton = findViewById(R.id.back);
        db = new DatabaseHandler(this);
        sm = new SessionManager(this);
        register_email = findViewById(R.id.register_email);
        register_username = findViewById(R.id.register_username);
        register_phone = findViewById(R.id.register_phone);
        register_name = findViewById(R.id.register_name);
        register_password = findViewById(R.id.register_password);
        register_password_confirm = findViewById(R.id.register_password_confirm);
        register_button = findViewById(R.id.register_button);
        parentLayout = findViewById(android.R.id.content);

        backButton.setOnClickListener(v -> onBackPressed());

        register_button.setOnClickListener(v -> {
            String email = register_email.getText().toString().trim().toLowerCase();
            String username = register_username.getText().toString().trim().toLowerCase();
            String phone = register_phone.getText().toString().trim().toLowerCase();
            String name = register_name.getText().toString().trim().toLowerCase();
            String pass = register_password.getText().toString().trim().toLowerCase();
            String pass_confirm = register_password_confirm.getText().toString().trim().toLowerCase();
            RegisterProcess(email,username,phone,name,pass,pass_confirm);
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

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

    private void RegisterProcess(String email, String username, String phone, String name, String password, String password_confirm) {
        if (!TextUtils.isEmpty(email) & email != null & !TextUtils.isEmpty(username) & username != null & !TextUtils.isEmpty(phone) & phone != null &
                !TextUtils.isEmpty(name) & name != null & !TextUtils.isEmpty(password) & password != null & !TextUtils.isEmpty(password_confirm) & password_confirm != null){
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if (email.matches(emailPattern)) {
                if (password.equals(password_confirm)) {
                    int min = 8;
                    int max = 11;
                    if (phone.length() >= min & phone.length() <= max) {
                        StringRequest strReq = new StringRequest(Request.Method.POST, Functions.REGISTER_URL, (String response) -> {
                            Timber.d("Login Response: %s", response);
                            try {
                                JSONObject jObj = new JSONObject(response);
                                boolean error = jObj.getBoolean("error");
                                if (!error) {
                                    Snackbar.make(parentLayout, jObj.getString("message"), Snackbar.LENGTH_LONG)
                                            .setAction("Хаах", view -> {
                                            })
                                            .setActionTextColor(getResources().getColor(android.R.color.holo_green_light))
                                            .show();
                                } else {
                                    // Error in login. Get the error message
                                    String errorMsg = jObj.getString("message");
                                    Snackbar.make(parentLayout, errorMsg, Snackbar.LENGTH_LONG)
                                            .setAction("Хаах", view -> {
                                            })
                                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                                            .show();
                                }
                            } catch (JSONException e) {
                                // JSON error
                                e.printStackTrace();
                            }

                        }, error -> {
                            Timber.e("Алдаа: %s", error.getMessage());
                            Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("email", email);
                                params.put("username", username);
                                params.put("name", name);
                                params.put("phone_number", phone);
                                params.put("password", password);
                                return params;
                            }
                        };
                        MyApplication.getInstance().addToRequestQueue(strReq);
                    } else {
                        Snackbar.make(parentLayout, "Утасны дугаараа зөв оруулсан эсэхээ шалгана уу!", Snackbar.LENGTH_LONG)
                                .setAction("Хаах", view -> {
                                })
                                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                                .show();
                    }
                } else {
                    Snackbar.make(parentLayout, "Нууц үг баталгаажуулалт буруу байна!", Snackbar.LENGTH_LONG)
                            .setAction("Хаах", view -> {
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                            .show();
                }
            } else {
                Snackbar.make(parentLayout, "И-Мэйл хаягаа зөв оруулсан эсэхээ шалгана уу!", Snackbar.LENGTH_LONG)
                        .setAction("Хаах", view -> {
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                        .show();
            }
        } else {
            Snackbar.make(parentLayout, "Бүх талбарыг зөв бөглөнө үү!", Snackbar.LENGTH_LONG)
                    .setAction("Хаах", view -> {
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                    .show();
        }
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

    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}
