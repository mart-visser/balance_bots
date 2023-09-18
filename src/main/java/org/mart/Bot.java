package org.mart;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Bot {
    private final int number;
    private final List<Integer> chips = new ArrayList<>();
    private boolean lowToOutput;
    private boolean highToOutput;
    private Integer lowTarget;
    private Integer highTarget;

    public boolean isLowToOutput() {
        return lowToOutput;
    }

    public boolean isHighToOutput() {
        return highToOutput;
    }

    public Integer getLowTarget() {
        return lowTarget;
    }

    public Integer getHighTarget() {
        return highTarget;
    }

    public Bot(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
    public void addChip(Integer value) {
        chips.add(value);
    }

    public Integer getLowValue() {
        chips.sort(Comparator.naturalOrder());
        return chips.get(0);
    }

    public Integer getHighValue() {
        chips.sort(Comparator.naturalOrder());
        return chips.get(1);
    }

    public void clearChips() {
        chips.clear();
    }

    public boolean hasTwoChips() {
        return chips.size() == 2;
    }

    public void setInstructions(String[] parts) {
        lowToOutput = parts[5].equals("output");
        highToOutput = parts[10].equals("output");
        lowTarget = Integer.parseInt(parts[6]);
        highTarget = Integer.parseInt(parts[11]);
    }
}
