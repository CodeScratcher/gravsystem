package com.skiscratcher.GravitySim;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    private double time = 0;
    private boolean paused = false;

    private BitmapFont fnt;

    @Override
    public void create() {
        batch = new SpriteBatch();
        sr = new ShapeRenderer();

        fnt = new BitmapFont();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        cam = new OrthographicCamera(30, 30 * (h / w));
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);

        objects = new ArrayList<>();
        // objects.add(new Object(new Vector2D(0, 0), new Vector2D(0, 0), 1.988400e30));
        // objects.add(new Object(new Vector2D(152099968880.0, 0), new Vector2D(0, 29290), 5.9722e24));
        objects.add(new Object(new Vector2D(0, 0), new Vector2D(0, 0), 9.45996e24, new Color(0.3f, 0.7f, 1.0f, 1.0f), 5.0));
        objects.add(new Object(new Vector2D(416490000, 0), new Vector2D(0, 1243.49862203), 1.06e22, new Color(0.6f, 0.6f, 0.6f, 1.0f), 1.0));
        objects.add(new Object(new Vector2D(56541880, 0), new Vector2D(0, 3400), 450000, new Color(1, 1, 1, 1), 0.5));
        objects.add(new Object(new Vector2D(416490000 - 400000, 0), new Vector2D(0, -200), 450000, new Color(1, 1, 1, 1), 0.5));
        objects.add(new Object(new Vector2D(416490000 - 400000, 0), new Vector2D(0, -400), 450000, new Color(1, 1, 1, 1), 0.5));
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            paused = !paused;
            ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        }


        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        Camera cam2 = new OrthographicCamera(30, 30 * (h / w));
        cam2.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);


        cam.position.x = (float)(objects.get(objFollowed).getPosition().getX() / scale);
        cam.position.y = (float)(objects.get(objFollowed).getPosition().getY() / scale);
        cam.update();


        sr.setProjectionMatrix(cam2.combined);
        sr.begin(ShapeType.Filled);

        sr.setColor(new Color(1, 1, 1, 1));
        // sr.rect(-15, -15 * (h / w), 10, 2.5f);

        sr.setProjectionMatrix(cam.combined);

        if (!paused) {
            for (int i = 0; i < 10000; i++) {
                for (Object obj : objects) {
                    sr.setColor(obj.getColor());

                    obj.update(objects, Gdx.graphics.getDeltaTime() * timescale / 10000);

                    sr.circle((float)(obj.getPosition().getX() / scale), (float)(obj.getPosition().getY() / scale), (float)obj.getRadius());

                }
            }

            time += timescale * Gdx.graphics.getDeltaTime();

            int secs  = (int)time % 60;
            double min = time / 60;
            int leftoverMin = (int)min % 60;
            double hr = min / 60;
            int leftoverHr = (int)hr % 24;
            int day = (int)hr / 24;
            Gdx.graphics.setTitle(day + "days, " + leftoverHr + "hours, " + leftoverMin + "minutes, " + secs + "seconds.");

        }
        else {
            for (Object obj : objects) {
                sr.setColor(obj.getColor());
                sr.circle((float)(obj.getPosition().getX() / scale), (float)(obj.getPosition().getY() / scale), (float)obj.getRadius());
            }
        }


        sr.end();


        batch.begin();

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        sr.dispose();
    }
}
