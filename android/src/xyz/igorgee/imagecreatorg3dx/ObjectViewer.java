package xyz.igorgee.imagecreatorg3dx;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.io.File;

import xyz.igorgee.imagecreator3d.ModelViewer;

public class ObjectViewer extends AndroidApplication {

	public static final String EXTRA_MODEL_FILE = "model";

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		File object = (File) getIntent().getExtras().get(EXTRA_MODEL_FILE);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new ModelViewer(object), config);
	}
}
