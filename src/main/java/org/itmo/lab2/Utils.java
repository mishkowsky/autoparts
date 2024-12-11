package org.itmo.lab2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Utils {
    public static String[] loadLinesFromFile(String path){
        ClassLoader classLoader = Utils.class.getClassLoader();
        File file = new File(classLoader.getResource(path).getFile());
        List<String> lines = null;
        try {
            lines = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (lines != null)
            return lines.toArray(new String[0]);
        else
            return new String[0];
    }

    public static Date between(Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = new Random().nextLong(startMillis, endMillis);
        return new Date(randomMillisSinceEpoch);
    }

    public static class LongWrapper {

        private long value;

        public LongWrapper (long value){
            this.value = value;
        }

        public long getValue() {
            return value;
        }

        public void setValue(long value) {
            this.value = value;
        }
    }
}
