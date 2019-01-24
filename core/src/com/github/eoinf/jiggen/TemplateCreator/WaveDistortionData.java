package com.github.eoinf.jiggen.TemplateCreator;

public class WaveDistortionData {
    public float sinAmplitude;
    public float sinPhase;
    public float sinPeriod;

    public WaveDistortionData(float sinAmplitude, float sinPhase, float sinPeriod) {
        this.sinAmplitude = sinAmplitude;
        this.sinPhase = sinPhase;
        this.sinPeriod = sinPeriod;
    }

    public int getDistortion(int x) {
        return (int)(sinAmplitude * Math.sin(sinPhase + (sinPeriod * x)));
    }
}
