package image_char_matching;

import java.util.*;


public class SubImgCharMatcher {

    private final HashMap<Character, Double> brightness;
    private final HashSet<Character> maxChars;
    private final HashSet<Character> minChars;
    private final TreeMap<Double, Character> normalizedBrightness;
    private final TreeSet<Character> charSet;
    private double maxBrightness;
    private double minBrightness;
    private boolean reGenerateTree;

    /**
     * Constructs a SubImgCharMatcher with the given charset.
     * Initializes data structures and calculates brightness values for the characters.
     *
     * @param charset array of characters to initialize the matcher with
     */
    public SubImgCharMatcher(char[] charset) {
        charSet = new TreeSet<>();
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

    /**
     * Adds a character to the charset.
     * Updates brightness values and re-generates the brightness tree if needed.
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
        this.reGenerateTree = true;
    }

    /**
     * Removes a character from the charset.
     * Updates brightness values and re-generates the brightness tree if needed.
     *
     * @param c the character to remove
     */
    public void removeChar(char c) {
        if (!charSet.contains(c)) {
            return;
        }
        charSet.remove(c);
        Double cBrightness = calculateBrightness(c);

        // Remove the character's brightness value from the map
        this.brightness.remove(c);

        // Update max and min brightness sets if necessary
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

    /**
     * Returns the set of characters in the charset.
     *
     * @return a TreeSet containing the characters
     */
    public TreeSet<Character> getCharSet() {
        return charSet;
    }

    /**
     * Returns a character whose brightness is closest to the given brightness value.
     * Re-generates the brightness tree if needed.
     *
     * @param brightness the brightness value to match
     * @return the character with the closest brightness value
     */
    public char getCharByImageBrightness(double brightness) {
        if (reGenerateTree) {
            generateTree();  // Ensure the TreeMap is up-to-date
        }

        Map.Entry<Double, Character> floorEntry = normalizedBrightness.floorEntry(brightness);
        Map.Entry<Double, Character> ceilingEntry = normalizedBrightness.ceilingEntry(brightness);

        if ((floorEntry == null) && (ceilingEntry != null)) {
            return ceilingEntry.getValue();
        } else if ((ceilingEntry == null) && (floorEntry != null)) {
            return floorEntry.getValue();
        } else {
            double floorDiff = Math.abs(floorEntry.getKey() - brightness);
            double ceilingDiff = Math.abs(ceilingEntry.getKey() - brightness);
            return (floorDiff <= ceilingDiff) ? floorEntry.getValue() : ceilingEntry.getValue();
        }
    }

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

    private void generateTree() {
        if (reGenerateTree) {
            normalizedBrightness.clear();
            for (char c : this.charSet) {
                double charNormalizedBrightness = calculateNormalizedBrightness(c, minBrightness, maxBrightness);
                if (this.normalizedBrightness.containsKey(charNormalizedBrightness) && this.normalizedBrightness.get(charNormalizedBrightness) < c) {
                    continue;
                }
                this.normalizedBrightness.put(charNormalizedBrightness, c);
            }
        }
        this.reGenerateTree = false;
    }

    private void generateBrightness(char[] charset) {

        for (char c : charset) {
            double charBrightness = calculateBrightness(c);
            this.brightness.put(c, charBrightness);
            if (charBrightness > maxBrightness) {
                maxBrightness = charBrightness;
                maxChars.clear();
                maxChars.add(c);
            }
            if (charBrightness == maxBrightness) {
                maxChars.add(c);
            }
            if (charBrightness < minBrightness) {
                minBrightness = charBrightness;
                minChars.clear();
                minChars.add(c);
            }
            if (charBrightness == minBrightness) {
                minChars.add(c);
            }
        }
    }
}