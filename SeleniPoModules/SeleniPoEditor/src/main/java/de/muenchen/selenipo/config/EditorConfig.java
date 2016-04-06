package de.muenchen.selenipo.config;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

/**
 * Class with beandeclarations for springcontext.
 * 
 * @author matthias
 *
 */
@Configurable
public class EditorConfig {

	private static final Logger logger = Logger.getLogger(EditorConfig.class);

	private static final String[] DEFAULT_FIREFOX_LOCATIONS = {
			"/usr/lib/firefox-31esr/firefox-31esr",
			"/usr/lib/firefox-10.0-10.0/firefox-10.0", "firefox" };
	/** Suffix zum Hostnamen des Rechners (siehe ersten Eintrag in /etc/hosts) */
	private static final String ITAM_FQDN_SUFFIX = ".itm.muenchen.de";

	@Bean(destroyMethod = "quit")
	@Scope("prototype")
	public WebDriver webDriver() {
		String binaryPath = null;
		for (int i = 0; i < DEFAULT_FIREFOX_LOCATIONS.length; i++) {
			String pathToCheck = DEFAULT_FIREFOX_LOCATIONS[i];
			File file = new File(pathToCheck);
			if (file.exists()) {
				binaryPath = pathToCheck;
				break;
			}
		}
		String allowedHosts = null;
		try {
			allowedHosts = InetAddress.getLocalHost().getHostName();
			if (!allowedHosts.contains(ITAM_FQDN_SUFFIX)) {
				allowedHosts += ITAM_FQDN_SUFFIX;
			}
		} catch (UnknownHostException e) {
			logger.warn("Konnte Hostname nicht ermitteln.", e);
		}
		final FirefoxProfile firefoxProfile = new FirefoxProfile();
		if (allowedHosts != null) {
			logger.info("Firefox allowed host preference:" + allowedHosts);

			firefoxProfile.setPreference(
					FirefoxProfile.ALLOWED_HOSTS_PREFERENCE, allowedHosts);
		}
		FirefoxDriver firefoxDriver;
		if (binaryPath != null) {
			FirefoxBinary firefoxBinary = new FirefoxBinary(
					new File(binaryPath));
			firefoxDriver = new FirefoxDriver(firefoxBinary, firefoxProfile);
		} else {
			firefoxDriver = new FirefoxDriver(firefoxProfile);
		}
		return firefoxDriver;
	}
}
