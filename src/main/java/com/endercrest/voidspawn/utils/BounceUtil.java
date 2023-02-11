package com.endercrest.voidspawn.utils;

import java.util.function.Function;

public final class BounceUtil {
    private static final double ACCELERATION = 0.08;
    private static final double ACC = ACCELERATION;
    private static final double DRAG = 0.02;
    private static final int MAX_ITERATIONS = 100;
    private static final double TOLERANCE = 1e-3;

    private BounceUtil() {
    }

    private static double getHeightForVelocity(double v0) {
        return (-DRAG * v0 +
                ACC * (-1 + DRAG) * Math.log((ACC - ACC * DRAG) / (ACC - ACC * DRAG + DRAG * v0)))
                / (DRAG * Math.log(1 - DRAG));
    }

    public static double getVelocityForHeight(double height) {
        Estimator estimator = new Estimator(height, guess -> getHeightForVelocity(guess), MAX_ITERATIONS, TOLERANCE);
        double guess = Math.sqrt(2 * height * ACC);
        return estimator.estimate(guess, 0);
    }

    private static final class Estimator {
        private final double target;
        private final Function<Double, Double> calculator;
        private final int maxIterations;
        private final double tolerance;

        private Estimator(double target, Function<Double, Double> calculator, int maxIterations, double tolerance) {
            this.target = target;
            this.calculator = calculator;
            this.maxIterations = maxIterations;
            this.tolerance = tolerance;
        }

        private double incrementUp(double guess, double cur) {
            double coef = target / cur;
            return guess * Math.sqrt(coef);
        }

        private double incrementDown(double guess, double cur) {
            double coef = cur / target;
            return guess / Math.sqrt(coef);
        }

        private double estimate(double guess, int iteration) {
            double result = calculator.apply(guess);
            if (iteration >= maxIterations) {
                return guess;
            }
            if (result >= target - tolerance && result <= target + tolerance) {
                return guess;
            }
            if (result < target) {
                return estimate(incrementUp(guess, result), iteration + 1);
            } else {
                return estimate(incrementDown(guess, result), iteration + 1);
            }
        }
    }
}
