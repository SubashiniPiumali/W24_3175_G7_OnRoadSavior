package com.example.w24_3175_g7_onroadsavior.Model;

public class BreakdownTypes {

    String breakdownType;

    int breakdownIcon;

    private float providerRating;

    public BreakdownTypes(String breakdownType, int breakdownIcon) {
        this.breakdownType = breakdownType;
        this.breakdownIcon = breakdownIcon;
    }

    public BreakdownTypes(float providerRating) {
        this.providerRating = providerRating;
    }

    public String getBreakdownType() {
        return breakdownType;
    }

    public void setBreakdownType(String breakdownType) {
        this.breakdownType = breakdownType;
    }

    public int getBreakdownIcon() {
        return breakdownIcon;
    }

    public void setBreakdownIcon(int breakdownIcon) {
        this.breakdownIcon = breakdownIcon;
    }

    public float getProviderRating() {
        return providerRating;
    }

    public void setProviderRating(float providerRating) {
        this.providerRating = providerRating;
    }
}
