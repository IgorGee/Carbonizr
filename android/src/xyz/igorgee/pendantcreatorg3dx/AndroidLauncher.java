package xyz.igorgee.pendantcreatorg3dx;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import xyz.igorgee.pendantcreator3d.MainActivity;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		startActivity(new Intent(this, MainActivity.class));
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new MyGDXClass(), config);
	}
}
