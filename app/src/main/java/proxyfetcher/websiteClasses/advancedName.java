package proxyfetcher.websiteClasses;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import java.io.IOException;
import java.util.*;
import org.json.JSONObject;

public class advancedName extends TimerTask {
    public static class proxyInstance {
        private String IP;
        private String Port;
        private final ArrayList<String> Descriptors = new ArrayList<>();
        private String Location;
        private int Speed;
        private long LastCheckup;

        private void setIP(String inputIP) {
            this.IP = inputIP;
        }

        private void setPort(String inputPort) {
            this.Port = inputPort;
        }

        private void setDescriptors(DomNodeList<HtmlElement> objHTMLDesc, int index) {
            this.Descriptors.addAll(Arrays.asList(objHTMLDesc.get(index).asNormalizedText().split(" ")));
        }

        private void setLocation(String inputLocation) {
            if (inputLocation.isEmpty()) {
                this.Location = "Unknown";
            } else {
                this.Location = inputLocation;
            }
        }

        private void setSpeed(int inputSpeed) {
            this.Speed = inputSpeed;
        }

        private void setLastCheckup() {
            this.LastCheckup = System.currentTimeMillis();
        }

        public JSONObject serveProxy() {
            JSONObject jsonProxy = new JSONObject();

            jsonProxy.put("IP", this.IP);
            jsonProxy.put("Port", this.Port);
            jsonProxy.put("Speed", this.Speed);
            jsonProxy.put("LastCheckup", this.LastCheckup);
            jsonProxy.put("Descriptors", this.Descriptors);
            jsonProxy.put("Location", this.Location);

            return jsonProxy;
        }
    }

    ArrayList<proxyInstance> arrProxies = new ArrayList<>();

    @Override
    public void run() {
        fetchProxies();
        System.out.println("Proxies refreshed");
    }

    public boolean isStringInt(String s)
    {
        try
        {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }

    private void fetchProxies() {
        WebClient client = new WebClient();
        arrProxies = new ArrayList<proxyInstance>();

        try {
            client.getOptions().setCssEnabled(false);
            client.getOptions().setJavaScriptEnabled(false);

            String searchGlobalUrl = "https://advanced.name/freeproxy";
            HtmlPage pageGlobal = client.getPage(searchGlobalUrl);

            pageGlobal.getElementsByTagName("ul").forEach((paginationElements) -> {
                paginationElements.getElementsByTagName("a").forEach((aElement) -> {
                    if (isStringInt(aElement.asNormalizedText())) {
                        try {
                            String searchUrl = "https://advanced.name/freeproxy?page=" + aElement.asNormalizedText();
                            HtmlPage page = client.getPage(searchUrl);

                            page.getElementsByTagName("tr").forEach((tableRow) -> {
                                proxyInstance objProxy = new proxyInstance();
                                DomNodeList<HtmlElement> arrColumns = tableRow.getElementsByTagName("td");

                                for (int i = 0; i < arrColumns.getLength(); i++) {
                                    switch (i) {
                                        case 1 -> objProxy.setIP(new String(Base64.getDecoder().decode(arrColumns.get(i).getAttributeDirect("data-ip"))));
                                        case 2 -> objProxy.setPort(new String(Base64.getDecoder().decode(arrColumns.get(i).getAttributeDirect("data-port"))));
                                        case 3 -> objProxy.setDescriptors(arrColumns, i);
                                        case 4 -> objProxy.setLocation(arrColumns.get(i).asNormalizedText());
                                        case 5 -> objProxy.setSpeed(Integer.parseInt(arrColumns.get(i).asNormalizedText()));
                                        case 6 -> objProxy.setLastCheckup();
                                    }
                                }

                                if (objProxy.IP != null && objProxy.Port != null) {
                                    arrProxies.add(objProxy);
                                }
                            });
                        } catch (IOException e) {
                            System.out.println("An error occurred: " + e);
                        }
                    }
                });
            });

            client.close();

        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
    }

    public proxyInstance fetchRandomProxy() {
        int min = 0;
        int max = (this.arrProxies.size() - 1);
        return this.arrProxies.get(new Random().nextInt(max-min) + min);
    }

    public JSONObject fetchProxyQuantity() {
        return new JSONObject().put("ProxyCount", this.arrProxies.size());
    }

    public proxyInstance fetchHighSpeed() {
        proxyInstance objRandom = this.fetchRandomProxy();

        if (objRandom.Speed < 3000) {
            return fetchHighSpeed();
        } else {
            return objRandom;
        }
    }

    public proxyInstance fetchSpecificLocation(String strLocation) {
        proxyInstance objRandom = this.fetchRandomProxy();

        if (!objRandom.Location.equals(strLocation)) {
            return fetchSpecificLocation(strLocation);
        } else {
            return objRandom;
        }
    }

    public proxyInstance fetchDescriptors(ArrayList<String> arrDescriptors) {
        proxyInstance objRandom = this.fetchRandomProxy();

        if (!objRandom.Descriptors.containsAll(arrDescriptors)) {
            return fetchDescriptors(arrDescriptors);
        } else {
            return objRandom;
        }
    }
}
