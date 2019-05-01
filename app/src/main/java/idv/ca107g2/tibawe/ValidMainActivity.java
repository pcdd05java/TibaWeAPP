package idv.ca107g2.tibawe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import idv.ca107g2.tibawe.classzone.CourseQueryFragment;
import idv.ca107g2.tibawe.lifezone.HotArticleFragment;

public class ValidMainActivity extends AppCompatActivity {
    Menu menu;
    SharedPreferences preferences;

    boolean login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
        login = preferences.getBoolean("login", false);

        setContentView(R.layout.activity_validmain);
        Toolbar toolbar = findViewById(R.id.toolbar_validmain);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        // Menu item click的監聽事件一樣要設定在setSupportActionBar之後才有作用
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_logout:
                        AlertFragment alertFragment = new AlertFragment();
                        FragmentManager fm = getSupportFragmentManager();
                        alertFragment.show(fm, "alert");
           }

                return true;
            }
        });



        ValidMainPagerAdapter pagerAdapter = new ValidMainPagerAdapter(getSupportFragmentManager());
        ViewPager pager =  findViewById(R.id.vpMain);
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(1);

        TabLayout tabLayout = findViewById(R.id.tbMain);
        tabLayout.setupWithViewPager(pager);

    }

    public static class AlertFragment extends DialogFragment implements DialogInterface.OnClickListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    //設定圖示
                    .setIcon(R.drawable.icons8_export_24)
                    .setTitle(R.string.logout)
                    //設定訊息內容
                    .setMessage(R.string.logout_msg)
                    //設定確認鍵 (positive用於確認)
                    .setPositiveButton(R.string.logout, this)
                    //設定取消鍵 (negative用於取消)
                    .setNegativeButton(R.string.logout_cancel, this)
                    .create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button posbtn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    posbtn.setTextColor(getResources().getColor(R.color.colorDarkBlue));
                    Button negbtn = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                    negbtn.setTextColor(getResources().getColor(R.color.colorDarkBlue));
                }
            });
            return alertDialog;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    SharedPreferences pref = getActivity().getSharedPreferences(Util.PREF_FILE,
                            MODE_PRIVATE);
                    pref.edit().putBoolean("login", false).apply();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    getActivity().finish();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.cancel();
                    break;
                default:
                    break;
            }
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 為了讓Toolbar的 Menu有作用，這邊的程式不可以拿掉
        getMenuInflater().inflate(R.menu.menu_top_valid, menu);
        this.menu = menu;
        menu.findItem(R.id.action_membername).setTitle(preferences.getString("membername", ""));
        return true;
    }

    public void onClickQRCode(View view){
        Intent intent = new Intent(this,QRCodeSignInActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    private class ValidMainPagerAdapter extends FragmentPagerAdapter {

        public ValidMainPagerAdapter (FragmentManager fm){
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new HotArticleFragment();
                case 1:
                    return new ValidMainMenuFragment();
                case 2:
                    return new CourseQueryFragment();
            }
            return null;
        }

        public CharSequence getPageTitle(int position){
            switch (position){
                case 0:
                    return getResources().getText(R.string.hot_article);
                case 1:
                    return getResources().getText(R.string.validmain_menu_tab);
                case 2:
                    return getResources().getText(R.string.course_query_quick);
            }
            return null;
        }
    }
    @Override
    public void onBackPressed() {
        if(login){
            AlertFragment alertFragment = new AlertFragment();
            FragmentManager fm = getSupportFragmentManager();
            alertFragment.show(fm, "alert");
        }else{
            super.onBackPressed();
            overridePendingTransition(R.anim.swipeback_stack_to_front,
                    R.anim.swipeback_stack_right_out);}


    }
}
