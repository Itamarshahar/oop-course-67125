package image_char_matching;

import java.util.*;


public class SubImgCharMatcher {

    private final HashMap<Character, Double> brightness;
    private final HashSet<Character> maxChars;
    private final HashSet<Character> minChars;
    private final TreeMap<Double, Character> normalizedBrightness;
    private double maxBrightness;
    private double minBrightness;
    private TreeSet<Character> charSet;
    private boolean reGenerateTree;

    public SubImgCharMatcher(char[] charset) {
        charSet = new TreeSet<Character>();
        for (char c : charset) {
            charSet.add(c);
        }
        this.maxChars = new HashSet<>();
        this.minChars = new HashSet<>();
        this.brightness = new HashMap<>();
        this.normalizedBrightness = new TreeMap<>();
        this.reGenerateTree = true;
        this.maxBrightness = Double.MIN_VALUE;
        this.minBrightness = Double.MAX_VALUE;
        generateBrightness(charset);
    }

    public void addChar(char c) {
        if (charSet.contains(c)) {
            return;
        }
        charSet.add(c);
        double charBrightness = calculateBrightness(c);
        updateMax(c, charBrightness);
        updateMin(c, charBrightness);
        this.brightness.put(c, charBrightness);
        this.reGenerateTree = true;
    }

    public void removeChar(char c){
        if (!charSet.contains(c)) {
            return;
        }
        charSet.remove(c);
        Double cBrightness = calculateBrightness(c);

        this.brightness.remove(c); // TODO why this?
        if (cBrightness == maxBrightness) {
            maxChars.remove(c);
            if (maxChars.isEmpty()) {
                recalculateMaxMinBrightness();
            }
        } else if (cBrightness == minBrightness) {
            minChars.remove(c);
            if (minChars.isEmpty()) {
                recalculateMaxMinBrightness();
            }
        }
        this.reGenerateTree = true;
    }

    public TreeSet<Character> getCharSet() {return charSet;}
    private void recalculateMaxMinBrightness() {
        maxBrightness = Double.MIN_VALUE;
        minBrightness = Double.MAX_VALUE;
        maxChars.clear();
        minChars.clear();
        for (char c : charSet) {
            double charBrightness = calculateBrightness(c);
            updateMax(c, charBrightness);
            updateMin(c, charBrightness);
        }
    }

    private double calculateNormalizedBrightness(char c, double min, double max) {
        return (brightness.get(c) - min) / (max - min);
    }

    private double calculateBrightness(char c) {
    return brightness.computeIfAbsent(c, key -> {
        boolean[][] boolArray = CharConverter.convertToBoolArray(key);
        int count = 0;
        int totalPixels = boolArray.length * boolArray[0].length;

        for (boolean[] row : boolArray) {
            for (boolean pixel : row) {
                if (pixel) {
                    count++;
                }
            }
        }

        return count / (double) totalPixels;
    });
}


    private void updateMax(char c, double charBrightness) {
        if (charBrightness > maxBrightness) {
            maxBrightness = charBrightness;
            maxChars.clear();
        }
        if (charBrightness >= maxBrightness) {
            maxChars.add(c);
        }
    }

    private void updateMin(char c, double charBrightness) {
        if (charBrightness < minBrightness) {
            minBrightness = charBrightness;
            minChars.clear();
        }
        if (charBrightness <= minBrightness) {
            minChars.add(c);
        }
    }

    public char getCharByImageBrightness(double brightness) {
        if (reGenerateTree){
            generateTree();  // Ensure the TreeMap is
        }
        // up-to-date

        Map.Entry<Double, Character> floorEntry = normalizedBrightness.floorEntry(brightness);
        Map.Entry<Double, Character> ceilingEntry = normalizedBrightness.ceilingEntry(brightness);

//        if (floorEntry == null && ceilingEntry == null) {
//            return '\0'; // No keys in the map
        if ((floorEntry == null) && (ceilingEntry!=null)) {
            return ceilingEntry.getValue();
        } else if ((ceilingEntry == null) && (floorEntry!=null)) {
            return floorEntry.getValue();
        } else {
            double floorDiff = Math.abs(floorEntry.getKey() - brightness);
            double ceilingDiff = Math.abs(ceilingEntry.getKey() - brightness);
            return (floorDiff <= ceilingDiff) ? floorEntry.getValue() : ceilingEntry.getValue();
        }
    }

    private void generateTree() {
        if (reGenerateTree) {
            normalizedBrightness.clear();
            for (char c : this.charSet) {
                double curNormal = calculateNormalizedBrightness(c, minBrightness, maxBrightness);
                if (normalizedBrightness.containsKey(curNormal) && normalizedBrightness.get(curNormal) < c) {
                    continue;
                }
                normalizedBrightness.put(curNormal, c);
            }
        }
        this.reGenerateTree = false;
    }
    private void generateBrightness(char[] charset) { // TODO ITAMAR EDIT
        // and change the logic
        for (char c: charset){
            double charBrightness = calculateBrightness(c);
            this.brightness.put(c, charBrightness);
            if (charBrightness > maxBrightness){
                maxBrightness = charBrightness;
                maxChars.clear();
                maxChars.add(c);
            }
            if (charBrightness == maxBrightness){
                maxChars.add(c);
            }
            if (charBrightness < minBrightness){
                minBrightness = charBrightness;
                minChars.clear();
                minChars.add(c);
            } if (charBrightness == minBrightness){
                minChars.add(c);
            }
        }
    }
}