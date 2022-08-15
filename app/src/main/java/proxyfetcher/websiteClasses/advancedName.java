package proxyfetcher.websiteClasses;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import java.io.IOException;
import java.util.*;
import org.json.JSONObject;

public class advancedName {

    public advancedName() {
        fetchProxies();

        System.out.println("Constructor ran");
    }

    ArrayList<proxyInstance> arrProxies = new ArrayList<>();

    public class proxyInstance {
        private String IP;
        private String Port;
        private ArrayList<String> Descriptors = new ArrayList<>();
        private String Location;
        private int Speed;
        private long LastCheckup;

        public void setIP(String inputIP) {
            this.IP = inputIP;
        }

        public void setPort(String inputPort) {
            this.Port = inputPort;
        }

        public void setDescriptors(DomNodeList<HtmlElement> objHTMLDesc, int index) {
            String[] subDescriptor = objHTMLDesc.get(index).asNormalizedText().split(" ");

            for (int w = 0; w < subDescriptor.length; w++) {
                this.Descriptors.add(subDescriptor[w]);
            }
        }

        public void setLocation(String inputLocation) {
            if (inputLocation.isEmpty()) {
                this.Location = "Unknown";
            } else {
                this.Location = inputLocation;
            }
        }

        public void setSpeed(int inputSpeed) {
            this.Speed = inputSpeed;
        }

        public void setLastCheckup() {
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
            client.getOptions().setCssEnabled(true);
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
                                    switch(i) {
                                        case 1:
                                            objProxy.setIP(new String(Base64.getDecoder().decode(arrColumns.get(i).getAttributeDirect("data-ip"))));
                                            break;
                                        case 2:
                                            objProxy.setPort(new String(Base64.getDecoder().decode(arrColumns.get(i).getAttributeDirect("data-port"))));
                                            break;
                                        case 3:
                                            objProxy.setDescriptors(arrColumns, i);
                                            break;
                                        case 4:
                                            objProxy.setLocation(arrColumns.get(i).asNormalizedText());
                                            break;
                                        case 5:
                                            objProxy.setSpeed(Integer.parseInt(arrColumns.get(i).asNormalizedText()));
                                            break;
                                        case 6:
                                            objProxy.setLastCheckup();
                                            break;
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
}
