package de.muenchen.selenipo.config;

import de.muenchen.selenipo.ByFactory;
import de.muenchen.selenipo.po.BasePo;
import de.muenchen.selenipo.po.PageObject;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * Class with beandeclarations for springcontext.
 * 
 * @author matthias
 *
 */
@Configurable
public class Config {
	/**
	 * Name des PropertyFiles.
	 */
	public static final String CONFIG_FILE = "config.properties";
	private static final String[] DEFAULT_FIREFOX_LOCATIONS = {
			"/usr/lib/firefox-31esr/firefox-31esr",
			"/usr/lib/firefox-10.0-10.0/firefox-10.0", "firefox" };
	/** Suffix zum Hostnamen des Rechners (siehe ersten Eintrag in /etc/hosts) */
	private static final String ITAM_FQDN_SUFFIX = ".itm.muenchen.de";

	/**
	 * Logger.
	 */
	private static Logger log = Logger.getLogger(Config.class);

	/**
	 * WebDriver for Selenium. Gets instantiated with a base URL located in the
	 * config.properties.
	 * 
	 * @return WebDriver
	 * @throws IOException
	 *             for properties input stream.
	 */
	@Bean(destroyMethod = "quit")
	public final WebDriver webDriver() throws IOException {
		Properties props = new Properties();
		props.load(this.getClass().getResourceAsStream("/" + CONFIG_FILE));
		String baseUrl = props.getProperty("webDriver.baseUrl");
		log.info("Loaded baseUrl for driver: [baseUrl = " + baseUrl + "].");
		// ToDo: Verschiedene Driver auswäjlbar machen?

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
			log.warn("Konnte Hostname nicht ermitteln.", e);
		}
		final FirefoxProfile firefoxProfile = new FirefoxProfile();
		if (allowedHosts != null) {
			log.info("Firefox allowed host preference:" + allowedHosts);

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
		firefoxDriver.get(baseUrl);
		return firefoxDriver;
	}

	/**
	 * ByFactory.
	 * 
	 * @return ByFactory
	 */
	@Bean
	public final ByFactory byFactory() {
		return new ByFactory();
	}

	/**
	 * PageObject.
	 * 
	 * @return PageObject
	 */
	@Bean
	public final PageObject pageObject() {
		return new PageObject();
	}

	/**
	 * BasePo.
	 * 
	 * @return BasePo
	 */
	@Bean
	public final BasePo basePo() {
		return new BasePo();
	}

}