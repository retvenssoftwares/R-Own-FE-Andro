package app.retvens.rown.MesiboMediaPickerClasses.sources;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import app.retvens.rown.R;


public class AlbumStartActivity extends AppCompatActivity implements GalleryPhotoGridFragment.facebookPicturecallback {
    FragmentTransaction mFragmentTransaction;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        AlbumStartActivity.super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_album);
        setSupportActionBar(findViewById(R.id.fb_activity_toolbar));
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(getIntent().getStringExtra("title"));
        ab.setBackgroundDrawable(new ColorDrawable(MediaPicker.getToolbarColor()));
        this.mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.mFragmentTransaction.replace(R.id.fb_fragment, new AlbumPhotoListFragment());
        this.mFragmentTransaction.commit();
    }

    public void facebookPictureSelected(String filePath) {
        Intent reverseIntent = new Intent();
        reverseIntent.putExtra("PATH", filePath);
        setResult(2, reverseIntent);
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_topics, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == 16908332) {
            onBackPressed();
            return true;
        } else if (id != R.id.action_settings) {
            return AlbumStartActivity.super.onOptionsItemSelected(item);
        } else {
            return true;
        }
    }
}
