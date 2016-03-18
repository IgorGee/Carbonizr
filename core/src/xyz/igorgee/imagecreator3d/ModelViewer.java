package xyz.igorgee.imagecreator3d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.UBJsonReader;

import java.io.File;


public class ModelViewer extends ApplicationAdapter {
    Environment environment;
    PerspectiveCamera cam;
    CameraInputController camController;
    ModelBatch modelBatch;
    G3dModelLoader g3dModelLoader;
    Model model;
    ModelInstance instance;

    File file;

    public ModelViewer(File file) {
        this.file = file;
    }

    @Override
    public void create() {
        g3dModelLoader = new G3dModelLoader(new UBJsonReader());

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        modelBatch = new ModelBatch();

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        model = g3dModelLoader.loadModel(Gdx.files.absolute(file.getAbsolutePath()));
        instance = new ModelInstance(model);

        Vector3 dimensions = instance.calculateBoundingBox(new BoundingBox())
                .getDimensions(new Vector3());
        float largest = dimensions.x;
        if(dimensions.y > largest)
            largest = dimensions.y;
        if(dimensions.z > largest)
            largest= dimensions.z;
        if(largest > 25){
            float s = 25f / largest;
            instance.transform.setToScaling(s, s, s);
        }else if(largest < 0.1f) {
            float s = 5 / largest;
            instance.transform.setToScaling(s, s, s);
        }

        if(instance != null){
            for (Material mat : model.materials) {
                mat.remove(BlendingAttribute.Type);
            }
        }

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);
    }

    @Override
    public void render() {
        camController.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        modelBatch.render(instance, environment);
        modelBatch.end();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        model.dispose();
    }
}