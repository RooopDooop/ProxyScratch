package proxyfetcher.proxyController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import proxyfetcher.websiteClasses.advancedName;

@RestController
public class proxyController {
    advancedName objAdvance = new advancedName();

    @GetMapping("/randomProxy")
	public String randomProxy() {
		return objAdvance.fetchRandomProxy().serveAddress();
	}

    @GetMapping("/availableProxyCount")
    public int countProxy() {
        return objAdvance.fetchProxyQuantity();
    }
}
