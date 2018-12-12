package be.ucll.da.dentravak.model;

import java.util.UUID;

public class SandwichPreference implements Comparable<SandwichPreference> {
    private UUID sandwichId;
    private float preference;

    public SandwichPreference(UUID sandwichId, float preference) {
        setSandwichId(sandwichId);
        setPreference(preference);
    }

    public UUID getSandwichId() {
        return sandwichId;
    }

    public void setSandwichId(UUID sandwichId) {
        this.sandwichId = sandwichId;
    }

    public float getPreference() {
        return preference;
    }

    public void setPreference(float preference) {
        this.preference = preference;
    }

    @Override
    public int compareTo(SandwichPreference sandwichPreference) {
        if (this.preference == sandwichPreference.preference) return 0;
        else if (this.preference < sandwichPreference.preference) return 1;
        else return -1;
    }
}
