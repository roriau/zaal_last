package mn.zaal.zaal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mn.zaal.zaal.tuslah.DatabaseHandler;
import mn.zaal.zaal.tuslah.Functions;
import mn.zaal.zaal.tuslah.SessionManager;
import timber.log.Timber;


public class FragFour extends Fragment {

    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PHONENUMBER = "phone_number";
    private static final String KEY_CREATED_AT = "created_at";

    private EditText usernameEditTxt,passwordEditTxt;
    private SessionManager sm = new SessionManager(MyApplication.getAppContext());
    private DatabaseHandler db;
    private ProgressBar progressBar;
    private RelativeLayout relativeLayout;
    private RelativeLayout relativeLayout2;
    private boolean timerProcessing = false;
    private boolean timerStarts = false;
    private View view;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            if (!sm.isLoggedin()) {
                view = inflater.inflate(R.layout.activity_login, container, false);

            }
        }
            /*
            view = inflater.inflate(R.layout.fragment_frag_four, container, false);
            usernameEditTxt = view.findViewById(R.id.usernameEditTxt);
            passwordEditTxt = view.findViewById(R.id.passwordEditTxt);
            TextView createAcc = view.findViewById(R.id.createAcc);
            TextView forgetPass = view.findViewById(R.id.forgetPass);
            relativeLayout = view.findViewById(R.id.loginLayout3);
            relativeLayout2 = view.findViewById(R.id.tuslahLayout);
            TextView versionTxt = view.findViewById(R.id.versionTxt);
            sm = new SessionManager(view.getContext());
            db = new DatabaseHandler(getContext());
            HashMap<String, String> user = db.getUserDetails();
            if (sm.isLoggedin()){
                relativeLayout.setVisibility(View.GONE);
                relativeLayout2.setVisibility(View.VISIBLE);
                TextView versionTxt1 = view.findViewById(R.id.versionTxt1);
                String AppVersion = "Аппликэйшн хувилбар: " + BuildConfig.VERSION_NAME + "-" + BuildConfig.VERSION_CODE;
                versionTxt1.setText(AppVersion);
                MaterialButton logoutButton = view.findViewById(R.id.logoutButton);
                logoutButton.setOnClickListener(v -> {
                    relativeLayout2.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        db.resetTables();
                        sm.removeIsLoggedin();
                        sm.removePhoneNumber();
                        progressBar.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                    },1000);
                });

                TextView phone_number_txt = view.findViewById(R.id.phone_number);
                TextView email_txt = view.findViewById(R.id.email);
                TextView name_txt = view.findViewById(R.id.name);
                TextView created_txt = view.findViewById(R.id.created);

                String phone_number = user.get(KEY_PHONENUMBER);
                String email = user.get(KEY_EMAIL);
                String name = user.get(KEY_NAME);
                String created = user.get(KEY_CREATED_AT);

                phone_number_txt.setText(phone_number);
                email_txt.setText(email);
                name_txt.setText(name);
                created_txt.setText(created);
            }
            String AppVersion = "Аппликэйшн хувилбар: " + BuildConfig.VERSION_NAME + "-" + BuildConfig.VERSION_CODE;
            versionTxt.setText(AppVersion);
            MaterialButton button = view.findViewById(R.id.loginButton);
            progressBar = view.findViewById(R.id.ProgressBar);
            button.setOnClickListener(v -> {
                String username = usernameEditTxt.getText().toString().trim();
                String password = passwordEditTxt.getText().toString().trim();
                if (!TextUtils.isEmpty(username) & !TextUtils.isEmpty(password)) {
                    adduser(username,password, view);
                }
            });
            createAcc.setOnClickListener(v -> {
                View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.create_acc, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.showAsDropDown(popupView, 0, 0);

                EditText email_input,username_input,name_input,phone_number_input,password_input,password_confirmation_input;
                CheckBox checkBox = popupView.findViewById(R.id.checkbox);
                MaterialButton submitBtn = popupView.findViewById(R.id.submit_button);
                submitBtn.setVisibility(View.GONE);
                email_input = popupView.findViewById(R.id.email_input);
                username_input = popupView.findViewById(R.id.username_input);
                name_input = popupView.findViewById(R.id.name_input);
                phone_number_input = popupView.findViewById(R.id.phone_number_input);
                password_input = popupView.findViewById(R.id.password_input);
                password_confirmation_input = popupView.findViewById(R.id.password_confirmation_input);
                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked){
                        submitBtn.setVisibility(View.VISIBLE);
                    } else {
                        submitBtn.setVisibility(View.GONE);
                    }
                });

                submitBtn.setOnClickListener(v1 -> {
                    String email = email_input.getText().toString().trim();
                    String username = username_input.getText().toString().trim();
                    String name = name_input.getText().toString().trim();
                    String phone = phone_number_input.getText().toString().trim();
                    String password = password_input.getText().toString().trim();
                    String password_confirmation = password_confirmation_input.getText().toString().trim();
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    if (!TextUtils.isEmpty(email) & !TextUtils.isEmpty(username) & !TextUtils.isEmpty(name) & !TextUtils.isEmpty(phone)
                            & !TextUtils.isEmpty(password) & !TextUtils.isEmpty(password_confirmation)) {
                        if (email.matches(emailPattern)) {
                            email_input.setBackgroundColor(getResources().getColor(R.color.primary_dark));
                            if (password.equals(password_confirmation)) {
                                password_input.setBackgroundColor(getResources().getColor(R.color.primary_dark));
                                password_confirmation_input.setBackgroundColor(getResources().getColor(R.color.primary_dark));
                                if (phone.length() == 8) {
                                    phone_number_input.setBackgroundColor(getResources().getColor(R.color.primary_dark));

                                    StringRequest strReq = new StringRequest(Request.Method.POST, Functions.REGISTER_URL, (String response) -> {
                                        Timber.d("Login Response: %s", response);
                                        try {
                                            JSONObject jObj = new JSONObject(response);
                                            boolean error = jObj.getBoolean("error");
                                            if (!error) {
                                                Toast.makeText(getContext(), jObj.getString("message"),Toast.LENGTH_SHORT).show();
                                                if (popupWindow.isShowing()){
                                                    popupWindow.dismiss();
                                                }
                                            } else {
                                                // Error in login. Get the error message
                                                String errorMsg = jObj.getString("message");
                                                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            // JSON error
                                            e.printStackTrace();
                                        }

                                    }, error -> {
                                        Timber.e("Алдаа: %s", error.getMessage());
                                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
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
                                    phone_number_input.setBackgroundColor(getResources().getColor(R.color.md_red_900));
                                    Toast.makeText(getContext(), "Зөвхөн гар утасны дугаар оруулна уу.", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                password_input.setBackgroundColor(getResources().getColor(R.color.md_red_900));
                                password_confirmation_input.setBackgroundColor(getResources().getColor(R.color.md_red_900));
                                Toast.makeText(getContext(), "Нууц үгийн баталгаажуулалт зөрсөн байна.", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            email_input.setBackgroundColor(getResources().getColor(R.color.md_red_900));
                            Toast.makeText(getContext(),"Зөв имэйл хаяг оруулна уу",Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getContext(),"Бүх талбарыг бөглөнө үү!", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        } */

        return view;
    }

  /*  private void adduser(final String login, final String password, View view) {
        progressBar.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.GONE);
        HashMap<String, String> user = db.getUserDetails();
        if (user.get("pnumber") == null) {
            StringRequest strReq = new StringRequest(Request.Method.POST, Functions.LOGIN_URL, (String response) -> {
                Timber.d("Login Response: %s", response);
                progressBar.setVisibility(View.INVISIBLE);
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    String is_active = jObj.getString("is_active");
                    if (!error) {
                        switch (is_active) {
                            case "1":
                                relativeLayout2.setVisibility(View.VISIBLE);
                                JSONObject json_user = jObj.getJSONObject("user");
                                db.addUser(json_user.getString(KEY_EMAIL),
                                        json_user.getString(KEY_NAME),
                                        json_user.getString(KEY_USERNAME),
                                        json_user.getString(KEY_PHONENUMBER),
                                        json_user.getString(KEY_CREATED_AT));

                                TextView phone_number_txt = view.findViewById(R.id.phone_number);
                                TextView email_txt = view.findViewById(R.id.email);
                                TextView name_txt = view.findViewById(R.id.name);
                                TextView created_txt = view.findViewById(R.id.created);

                                String phone_number = user.get(KEY_PHONENUMBER);
                                String email = user.get(KEY_EMAIL);
                                String name = user.get(KEY_NAME);
                                String created = user.get(KEY_CREATED_AT);

                                phone_number_txt.setText(phone_number);
                                email_txt.setText(email);
                                name_txt.setText(name);
                                created_txt.setText(created);

                                sm.setIsLoggedin(true);

                                break;
                            case "2":
                                relativeLayout2.setVisibility(View.GONE);
                                relativeLayout.setVisibility(View.VISIBLE);
                                sm.setPhoneNumber(jObj.getString("phone_number"));
                                View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.activation_layout, null);
                                final PopupWindow popupWindow1 = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                                popupWindow1.setOutsideTouchable(true);
                                popupWindow1.setFocusable(true);
                                popupWindow1.showAsDropDown(popupView, 0, 0);
                                TextView activation_count = popupView.findViewById(R.id.activation_count);
                                EditText activation_et = popupView.findViewById(R.id.activation_et);
                                MaterialButton activation_button = popupView.findViewById(R.id.activation_button);
                                MaterialButton activation_resend_button = popupView.findViewById(R.id.activation_resend_button);
                                final CountDownTimer timer = new CountDownTimer(120000, 1) {
                                    @SuppressLint("SetTextI18n")
                                    public void onTick(long millisUntilFinished) {
                                        activation_count.setText("Код иртэл " + millisUntilFinished / 1000 + " секунд түр хүлээнэ үү!");
                                    }

                                    @SuppressLint("SetTextI18n")
                                    public void onFinish() {
                                        activation_count.setText("");
                                        timerProcessing = false;
                                        activation_resend_button.setEnabled(true);
                                        activation_resend_button.setVisibility(View.VISIBLE);
                                    }
                                };

                                activation_resend_button.setOnClickListener(v -> {
                                    if (!timerStarts) {
                                        timer.start();
                                        timerStarts = true;
                                        timerProcessing = true;
                                    }
                                    if (timerProcessing) {
                                        activation_resend_button.setVisibility(View.GONE);
                                        activation_resend_button.setEnabled(false);
                                    }
                                });

                                activation_button.setOnClickListener(v -> {
                                    String act_code = activation_et.getText().toString().trim();
                                    if (!TextUtils.isEmpty(act_code)) {

                                        StringRequest authReq = new StringRequest(Request.Method.POST, Functions.ACTIVATION_URL, (String response2) -> {

                                            try {
                                                JSONObject jObj1 = new JSONObject(response2);
                                                boolean error1 = jObj1.getBoolean("error");
                                                if (!error1) {
                                                    Toast.makeText(getContext(), jObj1.getString("message"), Toast.LENGTH_SHORT).show();
                                                    if (popupWindow1.isShowing()) {
                                                        popupWindow1.dismiss();
                                                        relativeLayout.setVisibility(View.GONE);
                                                        relativeLayout2.setVisibility(View.VISIBLE);
                                                    }
                                                } else {
                                                    // Error in login. Get the error message
                                                    String errorMsg = jObj1.getString("message");
                                                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                                                }
                                            } catch (JSONException e) {
                                                // JSON error
                                                e.printStackTrace();
                                            }

                                        }, error1 -> {
                                            Timber.e("Алдаа: %s", error1.getMessage());
                                            Toast.makeText(getContext(), error1.getMessage(), Toast.LENGTH_LONG).show();
                                        }) {
                                            @Override
                                            protected Map<String, String> getParams() {
                                                String login = sm.getPhoneNumber();
                                                Map<String, String> params = new HashMap<>();
                                                params.put("confirmation_code", act_code);
                                                params.put("login", login);
                                                return params;
                                            }
                                        };
                                        MyApplication.getInstance().addToRequestQueue(authReq);

                                    } else {
                                        Toast.makeText(getContext(), "6-н оронтой кодоо оруулна уу", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                break;
                            case "3":
                                relativeLayout2.setVisibility(View.GONE);
                                relativeLayout.setVisibility(View.VISIBLE);
                                Toast.makeText(getContext(), "Хэрэглэгчийн нэр нууц үг буруу байна.", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } else {
                        // Error in login. Get the error message
                        progressBar.setVisibility(View.INVISIBLE);
                        relativeLayout2.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    progressBar.setVisibility(View.INVISIBLE);
                    relativeLayout2.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }

            }, error -> {
                Timber.e("Алдаа: %s", error.getMessage());
                progressBar.setVisibility(View.INVISIBLE);
                relativeLayout2.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("login", login);
                    params.put("password", password);
                    return params;
                }
            };
            MyApplication.getInstance().addToRequestQueue(strReq);
        }*/
}
