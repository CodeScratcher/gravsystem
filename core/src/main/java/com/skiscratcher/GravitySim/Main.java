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
    private int objFollowed = 1;
    private final double scale = 1e4;
    private final double timescale = 100;
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
        objects.add(new Object(new Vector2D(416490000 + 400000, 0), new Vector2D(0, 2243.498), 450000, new Color(1, 1, 1, 1), 0.5));
        objects.add(new Object(new Vector2D(416490000 + 391836.382161, 60319.560066), new Vector2D(-269.324186, 2222.906252), 450000, new Color(1, 1, 1, 1), 0.5));
        objects.add(new Object(new Vector2D(416490000 + 366633.905791, 119093.87444), new Vector2D(-546.855841, 2156.978295), 450000, new Color(1, 1, 1, 1), 0.5));
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

    private double angle(Vector2D v1, Vector2D v2) {
        double dot = v1.dotProduct(v2);
        double det = v1.getX() * v2.getY() - v1.getY() * v2.getX();
        return Math.atan2(det, dot) + Math.PI;
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
                        Vector2D normal = new Vector2D(tangent.getY(), -tangent.getX()); // TODO this assumes ccw
                        Vector2D actual = tangent.scalarMultiply(firstBurn.getVelocity().getX()).add(normal.scalarMultiply(firstBurn.getVelocity().getY()));
                        System.out.println();
                        obj.setVelocity(obj.getVelocity().add(actual));

                        burns.remove(0);
                    }
                }

                for (Object obj : objects) {
                    sr.setColor(obj.getColor());

                    obj.update(objects, Gdx.graphics.getDeltaTime() * timescale / 10000);

                    sr.circle((float)(obj.getPosition().getX() / scale), (float)(obj.getPosition().getY() / scale), (float)obj.getRadius());
                    cam.position.x = (float)(objects.get(objFollowed).getPosition().getX() / scale);
                    cam.position.y = (float)(objects.get(objFollowed).getPosition().getY() / scale);
                    cam.update();

                    sr.setProjectionMatrix(cam.combined);


                }

                double angle = angle(objects.get(2).getPosition().add(objects.get(objFollowed).getPosition().scalarMultiply(-1)), objects.get(1).getPosition().add(objects.get(objFollowed).getPosition().scalarMultiply(-1)));
                if (5.90 < angle && angle < 5.91) {
                    System.out.println("Time to launch: " + time);
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
