package virgil.dailycost.helper;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import virgil.dailycost.App;

public class DatabaseHelper {
    public static String loadSqlFromSeed(String seedPath) throws IOException {
        InputStream inputStream = App.getInstance().getAssets().open(seedPath);
        System.out.println(inputStream);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        return stringBuilder.toString();
    }
}
