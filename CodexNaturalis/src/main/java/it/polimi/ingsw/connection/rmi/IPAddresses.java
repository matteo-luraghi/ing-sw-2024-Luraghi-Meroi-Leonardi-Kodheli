package it.polimi.ingsw.connection.rmi;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Optional;

/**
 * IPAddresses class
 * @author Matteo Leonardo Luraghi
 */
public class IPAddresses {
    /**
     * Get the local ip address of the machine
     * @return the ip address
     */
    public static String getAddress() {
        ArrayList<String> ipAddresses = new ArrayList<>();
        // get all the valid ip addresses of the machine
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface networkInterface = null;
            Enumeration<InetAddress> inetAddresses = null;
            InetAddress inetAddress = null;

            while (networkInterfaces.hasMoreElements()) {
                networkInterface = networkInterfaces.nextElement();
                inetAddresses = networkInterface.getInetAddresses();

                while (inetAddresses.hasMoreElements()) {
                    inetAddress = inetAddresses.nextElement();
                    if (inetAddress instanceof java.net.Inet4Address && !inetAddress.isLoopbackAddress()) {
                        ipAddresses.add(inetAddress.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            return null;
        }

        // find the one interesting to start the server/client
        Optional<String> ipAddress = ipAddresses.stream()
                .filter(ip -> ip.startsWith("192."))
                .findFirst();
        return ipAddress.orElse(null);
    }
}