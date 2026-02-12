package nms.device.discovery.discover.ssh;

import com.jcraft.jsch.*;
import nms.device.discovery.model.raw.RawDeviceData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class SshDiscoveryServiceImpl implements SshDiscoveryService {

    private static final Logger logger = LoggerFactory.getLogger(SshDiscoveryServiceImpl.class);
    private static final String HOST = "127.0.0.1";
    private static final String USER = "acer";
    private static final String PASSWORD = "Akalya@231204";

    @Override
    public void enrichRawDeviceData(RawDeviceData raw) {
        try {
            logger.info("Starting SSH discovery for host: {}", HOST);
            JSch jsch = new JSch();
            Session session = jsch.getSession(USER, HOST, 22);
            session.setPassword(PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand("systeminfo");
            channel.setInputStream(null);

            InputStream input = channel.getInputStream();
            channel.connect();

            StringBuilder output = new StringBuilder();
            byte[] buffer = new byte[1024];
            int read;
            while ((read = input.read(buffer)) != -1) {
                output.append(new String(buffer, 0, read));
            }

            String rawOutput = output.toString();
            raw.setOsDetails(rawOutput);

            if (rawOutput.contains("System Manufacturer:"))
                raw.setManufacturer(rawOutput.split("System Manufacturer:")[1].split("\n")[0].trim());
            if (rawOutput.contains("System Type:"))
                raw.setArchitecture(rawOutput.split("System Type:")[1].split("\n")[0].trim());

            channel.disconnect();
            session.disconnect();

            logger.info("SSH discovery completed: Osdetails={},Manufacturer={}, Architecture={}", raw.getOsDetails(),raw.getManufacturer(), raw.getArchitecture());

        } catch (Exception e) {
            logger.error("Error during SSH discovery", e);
        }
    }
}
