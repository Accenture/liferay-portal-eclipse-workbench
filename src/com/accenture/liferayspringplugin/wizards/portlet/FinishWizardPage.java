package com.accenture.liferayspringplugin.wizards.portlet;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import static com.accenture.liferayspringplugin.wizards.Constants.*;

/**
 * The "Finish" wizard page will show message to user about how to import newly created project in eclipse.
 * 
 * @author Digital Liferay Capability IDC
 */

public class FinishWizardPage extends WizardPage {


	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param page
	 */
	public FinishWizardPage() {
		super(WIZARD_PAGE);
		setTitle(FINISH_PAGE_WIZARD_TITLE);
		setDescription(FINISH_PAGE_WIZARD_DESCRIPTION);
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 1;
		layout.verticalSpacing = 5;

		Label label = new Label(container, SWT.NULL);
		label.setText(EMPTY);
		
		label = new Label(container, SWT.NULL);
		label.setText("&New porlet is added to the selected plugins project. Please import the project into Eclipse to access the newly created portlet or refresh the existing project, if already imported in eclipse");
		
		setControl(container);
	}
}