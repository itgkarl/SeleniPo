
package de.muenchen.selenipo.po;

import de.muenchen.selenipo.po.BasePo;
import de.muenchen.selenipo.po.Control;
import de.muenchen.selenipo.po.PageObject;
import de.muenchen.selenipo.po.detailPage;

public class newPageGenerated extends BasePo {

	public final Control iTodotitle = control(by.input("todo_title"));
	public final Control iTodonotes = control(by.input("todo_notes"));
	public final Control bCreateTodo = control(by.button("commit"));

	public newPageGenerated(PageObject po) {
		super(po);
	}
 
	
	public detailPage clickBCreateTodo() {
		bCreateTodo.click();
		return new detailPage(this);
	}
	
 
	/**
	 * Get the Control for the Element $transition.getIdentifier().
	 * @return $transition.getIdentifier() - Element
	 */
	public Control getITodotitle() {
		return iTodotitle;
	}
	
	/**
	 * Get the Control for the Element $transition.getIdentifier().
	 * @return $transition.getIdentifier() - Element
	 */
	public Control getITodonotes() {
		return iTodonotes;
	}
	
	/**
	 * Get the Control for the Transition bCreateTodo.
	 * @return bCreateTodo - Transition
	 */
	public Control getBCreateTodo() {
		return bCreateTodo;
	}
	

}
