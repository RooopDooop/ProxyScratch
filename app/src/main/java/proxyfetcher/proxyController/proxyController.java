package proxyfetcher.proxyController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import proxyfetcher.websiteClasses.advancedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;

@RestController
public class proxyController {
    advancedName objAdvance = new advancedName();

    public proxyController() {
        new Timer().scheduleAtFixedRate(objAdvance, 0, 60000);
    }

    @RequestMapping(value = "/randomProxy", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public String randomProxy() {
        return objAdvance.fetchRandomProxy().serveProxy().toString();
	}

    @RequestMapping(value = "/availableProxyCount", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public String countProxy() {
        return objAdvance.fetchProxyQuantity().toString();
    }

    //Add return high speed proxy
    @RequestMapping(value = "/highSpeedProxy", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public String highSpeedProxy() {
        return objAdvance.fetchHighSpeed().serveProxy().toString();
    }

    //Add return proxy from specific location
    @RequestMapping(value = "/specificLocation", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public String specificLocation(@RequestParam String filterLocation) {
        return objAdvance.fetchSpecificLocation(filterLocation).serveProxy().toString();
    }

    @RequestMapping(value = "/specificDescriptors", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public String specificDescriptors(@RequestParam String filterDescriptors) {
        return objAdvance.fetchDescriptors(new ArrayList<String>(Arrays.asList(filterDescriptors.split(", ")))).serveProxy().toString();
    }
}
