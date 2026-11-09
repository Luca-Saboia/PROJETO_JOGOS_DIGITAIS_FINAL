package br.mackenzie;

/** Configurações de dificuldade dos obstáculos. */
public enum Difficulty {
    FACIL("FACIL", 130f, 2.00f, 165f, 55f),
    MEDIO("MEDIO", 145f, 1.65f, 150f, 90f),
    DIFICIL("DIFICIL", 160f, 1.40f, 140f, 120f);

    private final String displayName;
    private final float pipeSpeed;
    private final float spawnInterval;
    private final float gapSize;
    private final float maxGapCenterChange;

    Difficulty(String displayName, float pipeSpeed, float spawnInterval,
               float gapSize, float maxGapCenterChange) {
        this.displayName = displayName;
        this.pipeSpeed = pipeSpeed;
        this.spawnInterval = spawnInterval;
        this.gapSize = gapSize;
        this.maxGapCenterChange = maxGapCenterChange;
    }

    public String getDisplayName() {
        return displayName;
    }

    public float getPipeSpeed() {
        return pipeSpeed;
    }

    public float getSpawnInterval() {
        return spawnInterval;
    }

    public float getGapSize() {
        return gapSize;
    }

    public float getMaxGapCenterChange() {
        return maxGapCenterChange;
    }
}