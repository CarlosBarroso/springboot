package es.test.springboot.worker.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;


@Component
public class HostnameHolder {
    private static final Logger LOG = LoggerFactory.getLogger(HostnameHolder.class);

    @Value("${server.address:}")
    private String serverAddress;

    private String hostname;

    @PostConstruct
    public void postConstruct() {
        if (!"".equals(serverAddress)) {
            hostname = serverAddress;
        } else {
            try {
                hostname = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                hostname = "<unknown>";
            }
        }

        LOG.debug("Hostname: {}", hostname);
    }

    public String getHostname() {
        return hostname;
    }
}
