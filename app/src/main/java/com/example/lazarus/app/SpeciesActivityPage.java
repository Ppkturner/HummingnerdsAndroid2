package com.example.lazarus.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;


public class SpeciesActivityPage extends ActionBarActivity {

    WebView speciesWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_species_activity_page);

        // Stuff to do with webview goes here possibly
        speciesWebView = (WebView) findViewById(R.id.webView);
        String body = "<h1>Species: Temp Species</h1> <p> Lorem ipsum dolor sit amet, at pri mollis percipit mediocritatem, duo alienum nostrum definitionem te, facete aliquam ex nam. Ius vero pertinacia omittantur at, ad molestie scribentur duo. Accusam scaevola \n" +
                "\n" +
                "definitiones vel ei, sit quas ferri in, vix in sint liber. Pri natum diceret dissentiet te, ut debet affert efficiendi duo. Delectus similique no has, per ad feugiat erroribus definitiones, iracundia voluptaria sit no. Quo quod periculis ex, porro intellegebat ea mel. At quem veri eum.rnrnEt aeterno fabellas ocurreret usu, ad cum propriae lobortis delicata, eos ei tritani prompta. No has choro melius accusam, dicit repudiare at mel. Quem tale in has, errem nostrum constituto ut sit. Audiam aperiam ut sea. Ea facer necessitatibus nec, eripuit omittantur definitiones ad sit, cum ut doming temporibus.rnrnNam id dicit clita scripserit, no eos graece petentium disputando. Fugit impetus eu pri, percipit constituto repudiandae ea quo. An prima appareat his. Vel ubique denique te.rnrnOratio soleat pro id. Enim nonumy nemore sea no. Usu solum ullum audire ea, ad  </p>";
        speciesWebView.loadData(body, "text/html", null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_species_activity_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
