package com.sparta.game.core;

/**
 * ============================================================================
 * PILAR DA POO: ENCAPSULAMENTO
 * ============================================================================
 * Câmera 2D com interpolação suave (Lerp) e efeito de tremor de tela (Screen Shake)
 * para transmitir o impacto brutal dos golpes das Lâminas do Caos.
 */
public class Camera {

    private double x;
    private double y;
    private double shakeIntensity;
    private int shakeDuration;

    public Camera(double startX, double startY) {
        this.x = startX;
        this.y = startY;
        this.shakeIntensity = 0;
        this.shakeDuration = 0;
    }

    public void update(double targetX, double targetY, int viewportWidth, int levelWidth) {
        // Interpolação suave em direção ao alvo centralizado
        double desiredX = targetX - (viewportWidth / 2.0);

        // Clamping nos limites do cenário
        if (desiredX < 0) desiredX = 0;
        if (desiredX > levelWidth - viewportWidth) desiredX = levelWidth - viewportWidth;

        // Suavização (lerp)
        x += (desiredX - x) * 0.08;

        // Atualização do Screen Shake
        if (shakeDuration > 0) {
            shakeDuration--;
            if (shakeDuration <= 0) {
                shakeIntensity = 0;
            }
        }
    }

    public void triggerShake(double intensity, int durationFrames) {
        this.shakeIntensity = intensity;
        this.shakeDuration = durationFrames;
    }

    public int getRenderX() {
        int shakeOffset = 0;
        if (shakeDuration > 0) {
            shakeOffset = (int) ((Math.random() - 0.5) * 2 * shakeIntensity);
        }
        return (int) x + shakeOffset;
    }

    public int getRenderY() {
        int shakeOffset = 0;
        if (shakeDuration > 0) {
            shakeOffset = (int) ((Math.random() - 0.5) * 2 * shakeIntensity);
        }
        return (int) y + shakeOffset;
    }

    public double getX() {
        return x;
    }
}

