/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.skiscratcher.GravitySim;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 *
 * @author afisher
 */
@Getter
@Setter
@AllArgsConstructor
public class Object {
    private Vector2D position;
    private Vector2D velocity;
    private Vector2D acceleration = new Vector2D(0, 0);
    private Vector2D force = new Vector2D(0, 0);

    private final double mass;
    private final double G = 6.67430e-11;

    public Object(Vector2D position, Vector2D velocity, double mass) {
        this.position = position;
        this.velocity = velocity;
        this.mass = mass;
    }

    /**
     * Apply gravity and then move
     * @param objects List of objects to be attracted to
     * @param delta Time since last frame in seconds per frame
     */
    public void update(List<Object> objects, double delta) {
        Vector2D midVelocity = velocity.add(acceleration.scalarMultiply(delta / 2));

        acceleration = force.scalarMultiply(1 / mass);
        applyGravity(objects);


        position = position.add(midVelocity.scalarMultiply(delta));
        velocity = midVelocity.add(acceleration.scalarMultiply(delta / 2));
    }
    /**
     * Applies gravity
     * @param objects List of objects to be attracted to
     */
    public void applyGravity(List<Object> objects) {
        force = new Vector2D(0.0, 0.0);
        for (Object object : objects) {
            if (object != this) {
                double pull = G * ((mass * object.mass) / (Math.pow(position.distance(object.position), 2)));
                force = force.add(object.position.subtract(position).normalize().scalarMultiply(pull));
            }
        }
    }

}
