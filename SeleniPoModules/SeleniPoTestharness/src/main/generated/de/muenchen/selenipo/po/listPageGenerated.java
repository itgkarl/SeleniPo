
package de.muenchen.selenipo.po;

import de.muenchen.selenipo.po.BasePo;
import de.muenchen.selenipo.po.Control;
import de.muenchen.selenipo.po.PageObject;
import de.muenchen.selenipo.po.newPage;

public class listPageGenerated extends BasePo {

	public final Control aNew = control(by.link("bNew"));

	public listPageGenerated(PageObject po) {
		super(po);
	}
 
	
	public newPage clickANew() {
		aNew.click();
		return new newPage(this);
	}
	
 
	/**
	 * Get the Control for the Transition aNew.
	 * @return aNew - Transition
	 */
	public Control getANew() {
		return aNew;
	}
	

}
