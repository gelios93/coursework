package com.imap.game;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.imap.game.AppRepository.MaketViewModel;

public class AndroidLauncher extends AndroidApplication {

	private MaketViewModel mViewModel;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViewModel();
	}

	private void initViewModel() {
		mViewModel = new ViewModelProvider((ViewModelStoreOwner) this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(MaketViewModel.class);
	}

	@Override
	protected void onStart() {
		super.onStart();
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
//		initialize(new ImapGame(mViewModel.getAllMakets()), config);
	}

	//	@Override
//	protected void onStart() {
//		super.onStart();
//		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
//		config.useAccelerometer = false;
//		config.useCompass = false;
//		updateData(config);
//	}

//	private ImapGame updateData() {
//
//		executor.execute(() -> {
//			// С помощью метода getAll() получаем все заметки из БД
//			List<BuildMaket> m = new ArrayList<>((db.maketDAO().getAll()));
//			Array<BuildMaket> makets = new Array<>();
//			for (BuildMaket buildMaket : m){
//				System.out.println(buildMaket.getName()+"ttttttttttttttttttttttttttttt");
//				makets.add(buildMaket);
//			}
//			AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
//			config.useAccelerometer = false;
//			config.useCompass = false;
//			return new ImapGame(makets);
//			initialize(new ImapGame(makets), config);
//			runOnUiThread(() -> initialize(new ImapGame(makets), config));
//		});
//
//	}
}
