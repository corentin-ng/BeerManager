package com.example.beermanager;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;


import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;

import android.widget.TextView;
import android.widget.Toast;

import com.example.beermanager.beans.Fermentable;
import com.example.beermanager.beans.Hop;
import com.example.beermanager.beans.Recipe;
import com.example.beermanager.beans.Yeast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayDeque;
import java.util.ArrayList;


public class AffichageRecette extends Fragment {

    private static final String TAG = "BeerManagerLogs";
    private static String urlRecette = null;

    private long downloadId; //id du fichier téléchargé
    private String fileUrl = "";
    private TextView txt;

    private final static int REQUEST_CODE=1;
    private DownloadManager downloadManager;


    public AffichageRecette() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        if (getArguments() != null) {
            urlRecette = getArguments().getString("urlRecette");
            Log.i(TAG, "urlRcette" + urlRecette + getClass().getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_affichage_recette,container, false);
        txt = (TextView) view.findViewById(R.id.txt);
        Log.i(TAG, "onCreateView txt - " + txt);

        if(urlRecette.equals("fichierDemo")){
            //        parseXML();
            // Request permission needed for the fragment
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

            //catching download complete events from android download manager which broadcast message
            requireActivity().registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            URI uri = null;
            try {
                uri = new URI(urlRecette);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            String path = uri.getPath();
            String recipeSlug = path.substring(path.lastIndexOf('/') + 1);
            fileUrl = "https://brewdogrecipes.com/beerxml/" + recipeSlug + ".xml";
            Log.i(TAG, "onCreateView fileUrl - " + getClass().getSimpleName() + " - " + fileUrl);
            try {
                getRecipeFromUrl();
            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG, "onCreateView - getRecipeFromUrl failed : " + e);
            }
        } else {
//            Uri uri2 = Uri.parse("file:///android_asset/punkipa");

//            Uri uri2 = Uri.parse("android.resource://com.example.beermanager/punkipa");

            parseXMLFichierDemo(); // Erreur dans lérécupération du fichier
            Log.i(TAG, "onCreateView fichierDemo - " + getClass().getSimpleName());
          //  Uri.parse(urlRecette);
        }
        return view ;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.i(TAG, "on attach " + getClass().getSimpleName());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "on activity created " + getClass().getSimpleName());
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "on start " + getClass().getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "on resume " + getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "on pause " + getClass().getSimpleName());
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "on stop " + getClass().getSimpleName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "destroy view " + getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "on destroy " + getClass().getSimpleName());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "on detach " + getClass().getSimpleName());
    }

    private void getRecipeFromUrl() throws IOException {

        File file=new File(getActivity().getExternalFilesDir(null),"recettes");
        DownloadManager.Request request = null;

        String fileName = URLUtil.guessFileName(fileUrl, null, MimeTypeMap.getFileExtensionFromUrl(fileUrl));

        try {
            request = new DownloadManager.Request(Uri.parse(fileUrl))
                    .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE) //Restrict the types of networks over which this download may proceed.
                    .setAllowedOverRoaming(false)   //Set whether this download may proceed over a roaming connection.
                    .setTitle("Beer Manager")   //Set the title of this download, to be displayed in notifications (if enabled).
                    .setDescription("Recette " + fileName + " téléchargée")  //Set a description of this download, to be displayed in notifications (if enabled)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                    .setVisibleInDownloadsUi(true)
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            ;

            //Enqueue a new download and same the referenceId
            downloadManager= (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);      //for Fragment= getActivity.getSystemService();
            downloadId = downloadManager.enqueue(request);

            boolean finishDownload = false;
            int progress = -1;
            int status = 0;
            while (!finishDownload) {
                Cursor cursor = downloadManager.query(new DownloadManager.Query().setFilterById(downloadId));
                if (cursor.moveToFirst()) {
                    status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    switch (status) {
                        case DownloadManager.STATUS_FAILED: {
                            finishDownload = true;
                            break;
                        }
                        case DownloadManager.STATUS_PAUSED:
                        case DownloadManager.STATUS_PENDING:
                            break;
                        case DownloadManager.STATUS_RUNNING: {
                            final long total = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                            if (total >= 0) {
                                final long downloaded = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                                progress = (int) ((downloaded * 100L) / total);
                                // if you use downloadmanger in async task, here you can use like this to display progress.
                                // Don't forget to do the division in long to get more digits rather than double.
                                //  publishProgress((int) ((downloaded * 100L) / total));
                            }
                            break;
                        }
                        case DownloadManager.STATUS_SUCCESSFUL: {
                            progress = 100;
                            // if you use aysnc task
                            // publishProgress(100);
                            finishDownload = true;
                            break;
                        }
                    }
                }
            }
            Log.i(TAG, "Download status / progress : " + status + " / " + progress);
        } catch (IllegalStateException e) {
            Toast.makeText(getActivity(), "Downloading folder not found", Toast.LENGTH_SHORT).show();
        }
    }

    //check if download complete
    private final BroadcastReceiver onDownloadComplete=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                Log.i(TAG, "File received : " + downloadId);

//                openDownloadedAttachment(context, downloadId);
                parseXmlDownloadedFile(downloadId);
            }
        }
    };

    /**
     * Used to open the downloaded attachment.
     * Based on : https://stackoverflow.com/questions/7239996/android-downloadmanager-api-opening-file-after-download
     */
    private void parseXmlDownloadedFile(final long downloadId) {
        Log.i(TAG, "Opening downloaded file");
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            int downloadStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            String downloadLocalUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
//            String downloadMimeType = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));
            if ((downloadStatus == DownloadManager.STATUS_SUCCESSFUL) && downloadLocalUri != null) {
//                openDownloadedAttachment(context, Uri.parse(downloadLocalUri), downloadMimeType);
                Log.i(TAG, "Launching xml parser on downloaded file : " + Uri.parse(downloadLocalUri).toString());
                parseXML(Uri.parse(downloadLocalUri));
            }
        }
        cursor.close();
    }

    // POur utiliser un fichier en dur
    private void parseXMLFichierDemo() {
        XmlPullParserFactory parserFactory;
        try {
            Log.i(TAG, "create parserXml - " + getClass().getSimpleName() );

            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = getActivity().getAssets().open("punkipa");
            Log.i(TAG, "create parseXMLFichierDemo - " + getClass().getSimpleName() );
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            processParsing(parser);

        } catch (XmlPullParserException e) {
            Log.i(TAG, "XmlPullParserException - " + getClass().getSimpleName() );

        } catch (IOException e) {
            Log.i(TAG, "XmlParser IOException - " + getClass().getSimpleName() );
        }
    }

    private void parseXML(Uri uri) {
        XmlPullParserFactory parserFactory;
        try {
            Log.i(TAG, "create parserXml - " + getClass().getSimpleName() );

            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = getActivity().getContentResolver().openInputStream(uri);
            Log.i(TAG, "create parserXml - " + getClass().getSimpleName() );
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            processParsing(parser);

        } catch (XmlPullParserException e) {
            Log.i(TAG, "XmlPullParserException - " + getClass().getSimpleName() );

        } catch (IOException e) {
            Log.i(TAG, "XmlParser IOException - " + getClass().getSimpleName() );
        }
    }

    private void processParsing(XmlPullParser parser) throws IOException, XmlPullParserException{
        Log.i(TAG, "process parserXml - " + getClass().getSimpleName() );
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
//                    eltName = parser.getName();
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
        Log.i(TAG, "print parserXml - " + getClass().getSimpleName() );

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

            for (Hop hop : recipe.hops) {
                builder.append(hop.name).append("\n").
                        append(hop.type).append(" - ").
                        append(hop.form).append("\n").
                        append(hop.use).append(" - ").
                        append(hop.amount).append(" - ").
                        append(hop.time).append("\n\n")
                ;
            }

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
