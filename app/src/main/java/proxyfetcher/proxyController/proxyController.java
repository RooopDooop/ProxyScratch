package proxyfetcher.proxyController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import proxyfetcher.websiteClasses.advancedName;

@RestController
public class proxyController {
    advancedName objAdvance = new advancedName();

    @RequestMapping(value = "/randomProxy", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public String randomProxy() {
        return objAdvance.fetchRandomProxy().serveProxy().toString();
	}

    @RequestMapping(value = "/availableProxyCount", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public String countProxy() {
        return objAdvance.fetchProxyQuantity().toString();
    }

    //Add return high speed proxy

    //Add return proxy from specific location

    //Add return proxy with specific descriptor(s)
}
