package jp.ddo.chiroru.cellar;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

/**
 * <p>
 * HEADERを制御するフィルターです.
 * </p>
 */
public class ResponseCorsFilter
        implements ContainerResponseFilter {

    @Override
    public final ContainerResponse filter(final ContainerRequest req,
            final ContainerResponse contResp) {

        ResponseBuilder resp = Response.fromResponse(contResp.getResponse());
        resp.header("Access-Control-Allow-Origin", "*")
        .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");

        String reqHead = req.getHeaderValue("Access-Control-Request-Headers");

        if (null != reqHead && !reqHead.equals(null)) {
            resp.header("Access-Control-Allow-Headers", reqHead);
        }

        contResp.setResponse(resp.build());
        return contResp;
    }

}
