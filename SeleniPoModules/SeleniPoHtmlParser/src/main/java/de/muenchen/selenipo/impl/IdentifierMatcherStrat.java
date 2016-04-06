package de.muenchen.selenipo.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.TextField;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;

import com.steadystate.css.parser.ParseException;

import de.muenchen.selenipo.IdentifierStrategy;
import de.muenchen.selenipo.HtmlParserService.IdentifiertStratFactory;

public class IdentifierMatcherStrat implements IdentifierStrategy {

	private static final Logger logger = Logger
			.getLogger(IdentifierMatcherStrat.class);

	private IdentifierBaseStrat identifierBaseStrat;
	private TextField matcherRegex;
	private TextField attr;
	private TextField grp;

	public IdentifierMatcherStrat(TextField matcherRegex, TextField attr,
			TextField grp) {
		super();
		this.matcherRegex = matcherRegex;
		this.attr = attr;
		this.grp = grp;
		identifierBaseStrat = new IdentifierBaseStrat();
	}

	@Override
	public String genIdentefier(String prefix, Element element) {
		try {
			String returnIdentefier = prefix;
			String suffix = "";

			if (suffix.equals("") && element.hasAttr(attr.getText())) {
				String attrString = element.attr(attr.getText());
				Pattern pattern = Pattern.compile(matcherRegex.getText());
				Matcher matcher = pattern.matcher(attrString);
				if (matcher.find()) {
					String group = matcher
							.group(Integer.parseInt(grp.getText()));
					logger.debug("Matcher fand für grp [" + grp.getText()
							+ "] folgenden String: [" + group + "].");
					suffix = prepareIdentefierString(group);
				} else {
					logger.warn("Mattcher [" + matcher
							+ "] fand keinen Treffer in AttrString ["
							+ attrString + "].");
				}
			} else {
				logger.warn("Konnte Attribut [" + attr.getText()
						+ "] nicht finden.");
			}
			if (suffix.equals("")) {
				logger.warn("Konnte keinen Identifiert erzeugen. Verwende Base Strat.");
				return identifierBaseStrat.genIdentefier(prefix, element);
			} else {
				return returnIdentefier + suffix;
			}
		} catch (Exception e) {
			logger.warn("Exception beim Parsen: Verwende Base Strat zu Identifiertidentifizierung"
					+ e.getMessage());
			return identifierBaseStrat.genIdentefier(prefix, element);
		}
	}

	private String prepareIdentefierString(String str) {
		return WordUtils.capitalize(str).replaceAll("\\s+", "")
				.replaceAll("[^a-zA-Z]+", "");
	}

	public static void main(String[] args) {
		String typeName = "sf.chk";
		Pattern pattern4 = Pattern.compile("(.*)\\.(.*)");
		Matcher matcher = pattern4.matcher(typeName);

		String nameStr = "";
		if (matcher.find()) {
			nameStr = matcher.group(1);
			System.out.println(matcher.group(0));
			System.out.println(matcher.group(1));
			System.out.println(matcher.group(2));

		}
	}

}
