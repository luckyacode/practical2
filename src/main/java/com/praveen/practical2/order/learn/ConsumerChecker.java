package com.praveen.practical2.order.learn;

import org.springframework.stereotype.Component;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.*;

@Component("consumerChecker")
public class ConsumerChecker {

    private final String JMX_URL = "service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi";

    public boolean isQueueActivelyListening(String queueName) {
        try {
            JMXServiceURL url = new JMXServiceURL(JMX_URL);

            // Using default credentials
            Map<String, Object> env = new HashMap<>();
            env.put(JMXConnector.CREDENTIALS, new String[]{"admin", "activemq"});

            try (JMXConnector jmxc = JMXConnectorFactory.connect(url, env)) {
                MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

                // Construct the exact ObjectName for this queue
                // Note: If you see 'brokerName=localhost' in jConsole, keep it here.
                String objectNameStr = String.format("org.apache.activemq:type=Broker,brokerName=localhost,destinationType=Queue,destinationName=%s", queueName);
                ObjectName mbeanName = new ObjectName(objectNameStr);

                // Get the ConsumerCount attribute
                Long consumerCount = (Long) mbsc.getAttribute(mbeanName, "ConsumerCount");

                return consumerCount != null && consumerCount > 0;
            }
        } catch (Exception e) {
            // If Tomcat is down, the MBean might not exist yet, returning false is correct.
            return false;
        }
    }

    public List<String> getAllActiveQueues() {
        List<String> queueList = new ArrayList<>();
        // The URL for the Broker's JMX. Change localhost/1099 if your broker is elsewhere.
        String jmxUrl = "service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi";

        try {
            JMXServiceURL url = new JMXServiceURL(jmxUrl);
            try (JMXConnector jmxc = JMXConnectorFactory.connect(url, null)) {
                MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

                // Now we query the REMOTE server
                ObjectName pattern = new ObjectName("org.apache.activemq:*");
                Set<ObjectName> names = mbsc.queryNames(pattern, null);

                for (ObjectName name : names) {
                    String destName = name.getKeyProperty("destinationName");
                    if (destName != null && "Queue".equals(name.getKeyProperty("destinationType"))) {
                        queueList.add(destName);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Could not connect to Remote ActiveMQ JMX: " + e.getMessage());
        }
        return queueList;
    }

}