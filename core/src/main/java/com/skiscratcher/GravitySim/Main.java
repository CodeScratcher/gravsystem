package com.skiscratcher.GravitySim;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer sr;
    private OrthographicCamera cam;
    private int objFollowed = 0;
    private final double scale = 1e7;
    private final double timescale = 10000;
    private List<Object> objects;

    @Override
    public void create() {
        batch = new SpriteBatch();
        sr = new ShapeRenderer();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        cam = new OrthographicCamera(30, 30 * (h / w));
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);

        objects = new ArrayList<>();
        // objects.add(new Object(new Vector2D(0, 0), new Vector2D(0, 0), 1.988400e30));
        // objects.add(new Object(new Vector2D(152099968880.0, 0), new Vector2D(0, 29290), 5.9722e24));
        objects.add(new Object(new Vector2D(0, 0), new Vector2D(0, 0), 9.45996e24));
        objects.add(new Object(new Vector2D(416490000, 0), new Vector2D(0, 1243.49862203), 1.06e20));
    }

    @Override
    public void render() {
       if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            cam.zoom += 0.02f;
            ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
       }
       if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
           cam.zoom -= 0.02f;
           ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
       }



       batch.begin();

       batch.end();

       cam.position.x = (float)(objects.get(objFollowed).getPosition().getX() / scale);
       cam.position.y = (float)(objects.get(objFollowed).getPosition().getY() / scale);
       cam.update();

       sr.setProjectionMatrix(cam.combined);

       sr.begin(ShapeType.Filled);
       sr.setColor(1, 1, 0, 1);

       for (Object obj : objects) {
           obj.update(objects, Gdx.graphics.getDeltaTime() * timescale);
           sr.circle((float)(obj.getPosition().getX() / scale), (float)(obj.getPosition().getY() / scale), 5);
       }

       sr.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        sr.dispose();
    }
}
