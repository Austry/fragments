package ru.yandex.yamblz.ui.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import javax.inject.Inject;
import javax.inject.Named;

import ru.yandex.yamblz.App;
import ru.yandex.yamblz.R;
import ru.yandex.yamblz.developer_settings.DeveloperSettingsModule;
import ru.yandex.yamblz.model.Artist;
import ru.yandex.yamblz.ui.fragments.ArtistDetailsFragment;
import ru.yandex.yamblz.ui.fragments.ContentFragment;
import ru.yandex.yamblz.ui.fragments.ShowDetailsCallback;
import ru.yandex.yamblz.ui.other.ViewModifier;

public class MainActivity extends BaseActivity implements ShowDetailsCallback{

    @Inject @Named(DeveloperSettingsModule.MAIN_ACTIVITY_VIEW_MODIFIER)
    ViewModifier viewModifier;

    private ContentFragment contentFragment;

    @SuppressLint("InflateParams") // It's okay in our case.
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get(this).applicationComponent().inject(this);

        setContentView(viewModifier.modify(getLayoutInflater().inflate(R.layout.activity_main, null)));

        if (savedInstanceState == null) {
            contentFragment = new ContentFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_frame_layout, contentFragment)
                    .commit();
        }
    }

    @Override
    public void showArtistDetails(Artist artist) {
        ArtistDetailsFragment fragment = new ArtistDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ArtistDetailsFragment.EXTRA_ARTIST, artist);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .hide(contentFragment)
                .add(R.id.main_frame_layout, fragment)
                .commit();
    }
}
