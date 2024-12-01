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
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer sr;
    private OrthographicCamera cam;
    private int objFollowed = 0;
    private final double scale = 1e7;
    private final double timescale = 100000;
    private List<Object> objects;
    private List<Burn> burns;
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
        objects.add(new Object(new Vector2D(-60447145.2f, 5025882.59f), new Vector2D(-271.86f, -3157.38f), 450000, new Color(1, 1, 1, 1), 0.5));
        objects.add(new Object(new Vector2D(-60194777.45, -7381123.25f), new Vector2D(400.1, -3144.26), 450000, new Color(1, 1, 1, 1), 0.5));
        objects.add(new Object(new Vector2D(-57282363.24, -19596152.41), new Vector2D(1063.53, -2991.85), 450000, new Color(1, 1, 1, 1), 0.5));
        objects.add(new Object(new Vector2D(416490000 + -399665.1066, 17766.317899), new Vector2D(-63.591422, -198.825719), 450000, new Color(1, 1, 1, 1), 0.5));
        objects.add(new Object(new Vector2D(416490000 + -399665.1066, 17766.317899), new Vector2D(-63.591422, -198.825719), 450000, new Color(1, 1, 1, 1), 0.5));
        objects.add(new Object(new Vector2D(416490000 + -291295.092563, -305091.800706), new Vector2D(857.382695, 178.842653), 450000, new Color(1, 1, 1, 1), 0.5));
        objects.add(new Object(new Vector2D(416490000 + 516142.330157, -716390.741616), new Vector2D(869.355337, 1305.260246), 450000, new Color(1, 1, 1, 1), 0.5));
        try {
            burns = Burn.fromCSV(Gdx.files.internal("burns.csv").toString(), objects);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    
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
            int i = 0;
            System.out.println(time);
            
                        
            for (Object obj : objects) {
                sr.setColor(obj.getColor());

                obj.update(objects, Gdx.graphics.getDeltaTime() * timescale / 10000);
                NumberFormat fmt = new DecimalFormat("#.######");

                System.out.println(i + ": " + obj.getPosition().subtract(objects.get(objFollowed).getPosition()).toString(fmt) + " " + obj.getVelocity().toString(fmt));

                i++;

            }
        }


        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        Camera cam2 = new OrthographicCamera(30, 30 * (h / w));
        cam2.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);


        cam.position.x = (float)(objects.get(objFollowed).getPosition().getX() / scale);
        cam.position.y = (float)(objects.get(objFollowed).getPosition().getY() / scale);
        cam.update();

        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);

        sr.setProjectionMatrix(cam2.combined);
        sr.begin(ShapeType.Filled);

        sr.setColor(new Color(0, 0, 0, 0.01f));
        sr.rect(-1920, -1080, 19200, 10800);

        sr.setProjectionMatrix(cam.combined);

        if (!paused) {
            for (int i = 0; i < 10000; i++) {
                if (!burns.isEmpty()) {
                    Burn firstBurn = burns.get(0);
                    if (time <= firstBurn.getTime() && time + Gdx.graphics.getDeltaTime() * timescale * i / 10000 > firstBurn.getTime()) {
                        Object obj = objects.get(firstBurn.getId());
                        Vector2D tangent = obj.getVelocity().normalize();
                        Vector2D normal = new Vector2D(-tangent.getY(), tangent.getX()); // TODO this assumes clockwise
                        Vector2D actual = tangent.scalarMultiply(firstBurn.getVelocity().getX()).add(normal.scalarMultiply(firstBurn.getVelocity().getY()));
                        obj.setVelocity(obj.getVelocity().add(actual));
                        
                        burns.remove(0);
                    }
                }

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

        Gdx.gl.glDisable(GL30.GL_BLEND);



        batch.begin();

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        sr.dispose();
    }
}
