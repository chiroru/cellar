package jp.ddo.chiroru.cellar;

import static org.junit.Assert.assertThat;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import jp.ddo.chiroru.cellar.util.Fixture;
import jp.ddo.chiroru.cellar.util.FixtureResource;
import jp.ddo.chiroru.cellar.util.ServletContainerResource;

public class WineResourceTest {

    static final String URI = "http://127.0.0.1:8080/cellar/rest";

    @ClassRule
    public static ServletContainerResource server = new ServletContainerResource();

    @Rule
    public FixtureResource fixture = new FixtureResource();

    @Test
    @SuppressWarnings("unchecked")
    @Fixture("seeds.sql")
    public void Wineリソースのリストを取得する() throws Exception {
        Client client = Client.create();
        WebResource webResource = client.resource(URI + "/wines");
        ClientResponse response = webResource.get(ClientResponse.class);
        String responseJSON = response.getEntity(String.class);
        System.out.println(responseJSON);
        ObjectMapper mapper = new ObjectMapper();
        List<Wine> wineList = mapper.readValue(responseJSON, List.class);
        assertThat(wineList.size(), is(equalTo(12)));
    }
}
