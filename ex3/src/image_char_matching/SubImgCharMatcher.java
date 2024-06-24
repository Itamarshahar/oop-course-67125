package image_char_matching;

import java.util.*;

// TODO add doc
class SubImgCharMatcher {
    private final HashMap<Character, Double> brightness;
    private final HashSet<Character> maxChars;
    private final HashSet<Character> minChars;
    private final boolean reGenerateTree;
    private final TreeMap<Double, Character> normalizedBrightness;
    // TODO add doc
    TreeSet<Character> charSet;
    private double maxBrightness;
    private double minBrightness;

    // TODO ADD doc
    // TODO ADD doc
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

    }


    /**
     * Adds a character to the character set and updates the maximum and minimum brightness values.
     * If the character already exists in the set, it does nothing.
     *
     * @param c the character to add
     */
    public void addChar(char c) {
        if (charSet.contains(c)) {
            return;
        }
        charSet.add(c);
        double charBrightness = calculateBrightness(c);
        updateMax(c, charBrightness);
        updateMin(c, charBrightness);
        this.brightness.put(c, charBrightness);
    }

    /**
     * Removes a character from the character set and updates the maximum and minimum brightness values if necessary.
     * If the character does not exist in the set, it does nothing.
     *
     * @param c the character to remove
     */
    public void removeChar(char c) {
        // TODO finish impl
        charSet.remove(c);
    }

    private double calculateNormalizedBrightness(char c, double min, double max) {
        return (brightness.get(c) - min) / (max - min);
    }


    private double calculateBrightness(char c) {
        return brightness.computeIfAbsent(c, key -> {
            boolean[][] boolArray = CharConverter.convertToBoolArray(key);
            return Arrays.stream(boolArray).flatMap(Arrays::stream).filter(Boolean::booleanValue).count() / (double) (boolArray.length * boolArray[0].length);
        });
    }

    private void updateMax(char c, double charBrightness) {
        if (charBrightness >= maxBrightness) {
            if (charBrightness > maxBrightness) {
                maxBrightness = charBrightness;
                maxChars.clear();
            }
            maxChars.add(c);
        }
    }

    private void updateMin(char c, double charBrightness) {
        if (charBrightness <= minBrightness) {
            if (charBrightness < minBrightness) {
                minBrightness = charBrightness;
                minChars.clear();
            }
            minChars.add(c);
        }
    }


    public char getCharByImageBrightness(double brightness) {
        Map.Entry<Double, Character> floorEntry = normalizedBrightness.ceilingEntry(brightness);
        Map.Entry<Double, Character> ceilingEntry = normalizedBrightness.ceilingEntry(brightness);

        if ((floorEntry == null) && (ceilingEntry!=null)) {
            return ceilingEntry.getValue();
        } else if ((ceilingEntry == null) && (floorEntry!=null) ) {
            return floorEntry.getValue();
        } else {
             double floorDiff = Math.abs(floorEntry.getKey() - brightness);
             // TODO error handling
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
            this.toRemakeTree = false;
        }
    }
}

