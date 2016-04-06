package de.muenchen.selenipo;

import org.jsoup.nodes.Element;

public interface IdentifierStrategy {

	String genIdentefier(String prefix, Element element);

}
