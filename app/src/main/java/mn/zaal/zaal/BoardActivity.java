package mn.zaal.zaal;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

import mn.zaal.zaal.tuslah.SessionManager;

public class BoardActivity extends AppIntro2 {
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);
        if (!sessionManager.isFirstTime()){
            Intent intent = new Intent(BoardActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        SliderPage sliderPage = new SliderPage();
        sliderPage.setDescription(getResources().getString(R.string.intro_desc));
        sliderPage.setDescColor(Color.parseColor("#5d94c9"));
        sliderPage.setImageDrawable(R.drawable.zaal_logo);
        sliderPage.setBgColor(getResources().getColor(R.color.md_white_1000));

        SliderPage sliderPage1 = new SliderPage();
        sliderPage1.setTitle(getResources().getString(R.string.intro2_title));
        sliderPage1.setTitleColor(Color.parseColor("#5d94c9"));
        sliderPage1.setDescription(getResources().getString(R.string.intro2_desc));
        sliderPage1.setDescColor(Color.parseColor("#5d94c9"));
        sliderPage1.setImageDrawable(R.drawable.zaal_logo);
        sliderPage1.setBgColor(getResources().getColor(R.color.md_white_1000));

        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle(getResources().getString(R.string.intro3_title));
        sliderPage2.setTitleColor(Color.parseColor("#5d94c9"));
        sliderPage2.setDescription(getResources().getString(R.string.intro3_desc));
        sliderPage2.setDescColor(Color.parseColor("#5d94c9"));
        sliderPage2.setImageDrawable(R.drawable.zaal_logo);
        sliderPage2.setBgColor(getResources().getColor(R.color.md_white_1000));

        addSlide(AppIntroFragment.newInstance(sliderPage));
        addSlide(AppIntroFragment.newInstance(sliderPage1));
        addSlide(AppIntroFragment.newInstance(sliderPage2));

        setBarColor(Color.parseColor("#9ad7ef"));

        setFlowAnimation();
        setVibrate(true);
        setVibrateIntensity(60);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(BoardActivity.this, MainActivity.class);
        startActivity(intent);
        sessionManager.setIsFirstTime(false);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
