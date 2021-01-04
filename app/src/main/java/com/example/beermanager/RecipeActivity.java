package com.example.beermanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.beermanager.beans.Fermentable;
import com.example.beermanager.beans.Hop;
import com.example.beermanager.beans.Recipe;
import com.example.beermanager.beans.Yeast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;

// https://developer.android.com/reference/org/xmlpull/v1/XmlPullParser
// https://ssaurel.medium.com/parsing-xml-data-in-android-apps-71ef607fbb16
// https://www.vogella.com/tutorials/AndroidXML/article.html
// https://www.journaldev.com/10653/android-xml-parser-xmlpullparser

public class RecipeActivity extends AppCompatActivity {

    private static final String TAG = "BeerManager";

    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Log.i(TAG, "onCreate - " + getLocalClassName());

        txt = (TextView) findViewById(R.id.txt);
//        parseXML();
        try {
            getRecipeFromUrl();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "onGetRecipeFromUrl - " + getLocalClassName());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart - " + getLocalClassName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop - " + getLocalClassName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy - " + getLocalClassName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause - " + getLocalClassName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume - " + getLocalClassName());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart - " + getLocalClassName());
    }

    private void parseXML() {
        XmlPullParserFactory parserFactory;
        try {
            Log.i(TAG, "create parserXml - " + getLocalClassName());

            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = getAssets().open("punkipa");
            Log.i(TAG, "create parserXml - " + getLocalClassName());
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            processParsing(parser);

        } catch (XmlPullParserException e) {
            Log.i(TAG, "XmlPullParserException - " + getLocalClassName());

        } catch (IOException e) {
            Log.i(TAG, "XmlParser IOException - " + getLocalClassName());
        }
    }

    private void getRecipeFromUrl() throws IOException {
        URL url = new URL("https://brewdogrecipes.com/beerxml/punk-ipa-2007-2010.xml");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        InputStream in;
        try {
            in = new BufferedInputStream(urlConnection.getInputStream());
            readStream(in);
        } finally {
            urlConnection.disconnect();
        }

        XmlPullParserFactory parserFactory;
        try {
            Log.i(TAG, "create parserXml - " + getLocalClassName());

            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            Log.i(TAG, "create parserXml - " + getLocalClassName());
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);

            processParsing(parser);

        } catch (XmlPullParserException e) {
            Log.i(TAG, "XmlPullParserException - " + getLocalClassName());

        } catch (IOException e) {
            Log.i(TAG, "XmlParser IOException - " + getLocalClassName());
        }
    }

    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }

    private void processParsing(XmlPullParser parser) throws IOException, XmlPullParserException{
        Log.i(TAG, "process parserXml - " + getLocalClassName());
        ArrayList<Recipe> recipes = new ArrayList<>();
        int eventType = parser.getEventType();

        Recipe currentRecipe = null;
        Yeast currentYeast = null;
        Hop currentHop = null;
        Fermentable currentFermentable = null;
        ArrayDeque<String> lifo = new ArrayDeque<>(); //https://www.happycoders.eu/java/queue-deque-stack-ultimate-guide/#Java_Deque_Example

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String eltName = null;


            switch (eventType) {
                case XmlPullParser.START_TAG:
                    eltName = parser.getName();
                    String parent = "";
                    Log.i(TAG, "XmlParser START lifo / eltName / value : " + lifo + " / " + eltName);

                    if (lifo.size() > 0) {
                        parent = lifo.getFirst();
                    }
                    lifo.addFirst(eltName);

                    if ("RECIPE".equals(eltName)) {
                        currentRecipe = new Recipe();
                        recipes.add(currentRecipe);
                    } else if (currentRecipe != null && "RECIPE".equals(parent) ) {
                        if ("NAME".equals(eltName)) {
                            currentRecipe.name = parser.nextText();
                            lifo.removeFirst();
                        } else if ("TYPE".equals(eltName)) {
                            currentRecipe.type = parser.nextText();
                            lifo.removeFirst();
                        } else if ("BREWER".equals(eltName)) {
                            currentRecipe.brewer = parser.nextText();
                            lifo.removeFirst();
                        } else if ("BATCH_SIZE".equals(eltName)) {
                            currentRecipe.batchSize = Integer.parseInt(parser.nextText());
                            lifo.removeFirst();
                        }
                    }

                    if (currentRecipe != null && "STYLE".equals(parent) && "NAME".equals(eltName)) {
                        currentRecipe.style = parser.nextText();
                        lifo.removeFirst();
                        }

                    if (currentRecipe != null && "FERMENTABLE".equals(eltName)) {
                        if (currentRecipe.fermentables == null) {
                            currentRecipe.fermentables = new ArrayList<>();
                        }
                        currentFermentable = new Fermentable();
                        currentRecipe.fermentables.add(currentFermentable);
                    } else if (currentFermentable != null && "FERMENTABLE".equals(parent) ) {
                        if ("NAME".equals(eltName)) {
                            currentFermentable.name = parser.nextText();
                            lifo.removeFirst();
                        } else if ("TYPE".equals(eltName)) {
                            currentFermentable.type = parser.nextText();
                            lifo.removeFirst();
                        } else if ("AMOUNT".equals(eltName)) {
                            currentFermentable.amount = Float.parseFloat(parser.nextText());
                            lifo.removeFirst();
                        } else if ("YIELD".equals(eltName)) {
                            currentFermentable.yield = Float.parseFloat(parser.nextText());
                            lifo.removeFirst();
                        } else if ("COLOR".equals(eltName)) {
                            currentFermentable.color = Integer.parseInt(parser.nextText());
                            lifo.removeFirst();
                        }
                    }

                    if (currentRecipe != null && "HOP".equals(eltName)) {
                        if (currentRecipe.hops == null) {
                            currentRecipe.hops = new ArrayList<>();
                        }
                        currentHop = new Hop();
                        currentRecipe.hops.add(currentHop);
                    } else if (currentHop != null && "HOP".equals(parent) ) {
                        if ("NAME".equals(eltName)) {
                            currentHop.name = parser.nextText();
                            lifo.removeFirst();
                        } else if ("TYPE".equals(eltName)) {
                            currentHop.type = parser.nextText();
                            lifo.removeFirst();
                        } else if ("FORM".equals(eltName)) {
                            currentHop.form = parser.nextText();
                            lifo.removeFirst();
                        } else if ("AMOUNT".equals(eltName)) {
                            currentHop.amount = Float.parseFloat(parser.nextText());
                            lifo.removeFirst();
                        } else if ("USE".equals(eltName)) {
                        currentHop.use = parser.nextText();
                        lifo.removeFirst();
                        } else if ("TIME".equals(eltName)) {
                        currentHop.time = Integer.parseInt(parser.nextText());
                        lifo.removeFirst();
                        }
                    }

                    if (currentRecipe != null && "YEAST".equals(eltName)) {
                        if (currentRecipe.yeasts == null) {
                            currentRecipe.yeasts = new ArrayList<>();
                        }
                        currentYeast = new Yeast();
                        currentRecipe.yeasts.add(currentYeast) ;
                    } else if (currentYeast != null && "YEAST".equals(parent) ) {
                        if ("NAME".equals(eltName)) {
                            currentYeast.name = parser.nextText();
                            lifo.removeFirst();
                        } else if ("TYPE".equals(eltName)) {
                            currentYeast.type = parser.nextText();
                            lifo.removeFirst();
                        } else if ("FORM".equals(eltName)) {
                            currentYeast.form = parser.nextText();
                            lifo.removeFirst();
                        } else if ("AMOUNT".equals(eltName)) {
                            currentYeast.amount = Float.parseFloat(parser.nextText());
                            lifo.removeFirst();
                        }
                    }
                    break;

                case XmlPullParser.END_TAG:
                    eltName = parser.getName();
                    Log.i(TAG, "XmlParser END element removed : " + lifo.pollFirst());
                    break;
            }
            eventType = parser.next();
        }
        Log.i(TAG, "XmlParser finished");
        printRecipes(recipes);
    }

    private void printRecipes(ArrayList<Recipe> recipes) {
        StringBuilder builder = new StringBuilder();
        Log.i(TAG, "print parserXml - " + getLocalClassName());

        for (Recipe recipe : recipes) {
            builder.append(recipe.name).append("\n").
                    append(recipe.type).append("\n").
                    append(recipe.brewer).append("\n").
                    append(recipe.batchSize).append("\n").
                    append(recipe.style).append("\n\n");

            for (Yeast yeast : recipe.yeasts) {
                builder.append(yeast.name).append("\n").
                        append(yeast.type).append(" - ").
                        append(yeast.form).append(" - ").
                        append(yeast.amount).append("\n\n")
                ;
            }

//            for (Hop hop : recipe.hops) {
//                builder.append(hop.name).append("\n").
//                        append(hop.type).append(" - ").
//                        append(hop.form).append("\n").
//                        append(hop.use).append(" - ").
//                        append(hop.amount).append(" - ").
//                        append(hop.time).append("\n\n")
//                ;
//            }

            for (Fermentable fermentable : recipe.fermentables) {
                builder.append(fermentable.name).append("\n").
                        append(fermentable.type).append("\n").
                        append(fermentable.amount).append(" - ").
                        append(fermentable.yield).append(" - ").
                        append(fermentable.color).append("\n\n")
                ;
            }

        }

        txt.setText(builder.toString());
    }

}