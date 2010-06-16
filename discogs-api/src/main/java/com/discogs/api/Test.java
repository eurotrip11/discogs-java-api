package com.discogs.api;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.discogs.api.exception.JDiscogsException;
import com.discogs.api.model.Artist;
import com.discogs.api.model.Label;
import com.discogs.api.model.Release;
import com.discogs.api.webservice.impl.HttpWebService;
import java.util.List;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.ProxyClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;

/**
 *
 * @author nexse
 */
public class Test {

    public static void main(String[] args) {
        try {
            Query query = new Query();
            /*Artist artist = query.getArtist("Aphex Twin");
            System.out.println(artist.getName());
            Release release = query.getRelease("1");
            System.out.println(release.getTitle());
            Label label = query.getLabel("Svek");
            System.out.println(label.getName());*/
            HttpClient client = new HttpClient();
            HttpWebService ser =  (HttpWebService) query.getWebService();
//            HostConfiguration configuration = new HostConfiguration();
//            configuration.setProxy("218.248.44.216", 8080);
//            client.setHostConfiguration(configuration);
            ser.setHttpClient(client);
            List<Artist> artists = query.findArtists("Vasco Rossi");
            for (Artist artist : artists) {
                System.out.println("artist:" + artist.getName());
                System.out.println("artist:" + artist.getRole());
            }
            List<Release> releases = query.findReleases("Vasco Rossi Stupido Hotel");
            for (Release release : releases) {
                System.out.println("release:" + release.getTitle());
                System.out.println("release:" + release.getCountry());
                System.out.println("release:" + release.getReleased());
                System.out.println("release:" + release.getLabel());
                System.out.println("release:" + release.getLabels().getLabel().getName());
            }
            List<Label> labels = query.findLabels("EMI Music (Italy)");
            for (Label label : labels) {
                List<Object> objects = label.getContent();
                for (Object object : objects) {
                     System.out.println("labels: "+object.toString());
                }
            }
        } catch (JDiscogsException ex) {
            ex.printStackTrace();
        }
    }
}
