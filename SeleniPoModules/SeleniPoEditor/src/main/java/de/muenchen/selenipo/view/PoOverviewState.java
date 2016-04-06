package de.muenchen.selenipo.view;

public interface PoOverviewState {

	void handleNew();

	void handleDelete();

	void handleEdit();

	boolean handleTest();

	boolean handleTestWithMessage();

	boolean handleTestAndClick();

	void handleMoveHtmlToElement();

	void handleMoveHtmlToTransition();

}
