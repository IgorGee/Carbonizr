package xyz.igorgee.imagecreatorg3dx;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import xyz.igorgee.imagecreator3d.MainActivity;
import xyz.igorgee.imagecreator3d.MyGDXClass;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		startActivity(new Intent(this, MainActivity.class));
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new MyGDXClass(), config);
	}
}
