package com.accenture.liferayspringplugin.wizards.portlet;

import java.io.File;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.accenture.liferayspringplugin.wizards.SpringPortletWizard;
import com.accenture.liferayspringplugin.wizards.WizardUtil;

import static com.accenture.liferayspringplugin.wizards.Constants.*;

/**
 * The "LiferaySpring" wizard page allows setting the portlet/project name and
 * other required settings.
 * 
 * @author Digital Liferay Capability IDC
 */

public class SpringPortletMavenWizardPage extends WizardPage {
	private Combo projectName;
	private Text portletName;
	private Text sdkHome;
	private Text jspPath;
	private Text displayCategry;
	private SpringPortletWizard springPortletWizard;
	private SpringPortletAntWizardPage antPage;

	/**
	 * Constructor for SampleNewWizardPage.
	 * @param antPage 
	 * 
	 * @param pageName
	 */
	public SpringPortletMavenWizardPage(SpringPortletWizard springPortletWizard, SpringPortletAntWizardPage antPage) {
		super(WIZARD_PAGE);
		setTitle(PORTLET_WIZARD_PAGE_TITLE);
		setDescription(PORTLET_WIZARD_DESCRIPTION);
		this.springPortletWizard = springPortletWizard;
		this.antPage = antPage;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;

		Label label = new Label(container, SWT.NULL);
		label.setText(LOCATION_PATH_LABEL);

		sdkHome = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		sdkHome.setLayoutData(gd);
		sdkHome.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
				updateProjectList(getSdkHome());
			}
		});

		Button button = new Button(container, SWT.PUSH);
		button.setText(BROWSE_BUTTON_LABEL);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});
		
		label = new Label(container, SWT.NULL);
		label.setText(_PROJECT_NAME_LABLE);

		projectName = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		projectName.setLayoutData(gd);
		projectName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		label = new Label(container, SWT.NULL);
		label.setText(EMPTY);

		label = new Label(container, SWT.NULL);
		label.setText(_PORTLET_NAME_LABEL);

		portletName = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		portletName.setLayoutData(gd);
		portletName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		label = new Label(container, SWT.NULL);
		label.setText(EMPTY);

		label = new Label(container, SWT.NULL);
		label.setText(_JSP_PATH_LABEL);

		jspPath = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		jspPath.setLayoutData(gd);
		jspPath.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		label = new Label(container, SWT.NULL);
		label.setText(EMPTY);
		label = new Label(container, SWT.NULL);
		label.setText(_DISPLAY_CATEGORY_LABEL);

		displayCategry = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		displayCategry.setLayoutData(gd);
		displayCategry.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		initialize();
		//dialogChanged();
		setControl(container);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		jspPath.setText(DEFAULT_JSP_FOLDER);
		displayCategry.setText(DEFAULT_DISPLAY_CATEGORY);
		String path = ResourcesPlugin.getWorkspace().getRoot().getLocation()
				.toString();
		if (isValidSdkPath(path)) {
			sdkHome.setText(path);
			updateProjectList(path);
		}
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */

	private void handleBrowse() {
		DirectoryDialog dialog = new DirectoryDialog(getShell());
		dialog.setText(LIFERAY_SDK_HOME);
		dialog.setMessage(SELECT_LIFERAY_SDK_PATH);
		dialog.setFilterPath(getSdkHome());
		String path = dialog.open();
		if (path != null && path.length() > 0) {
			sdkHome.setText(path);
		}
	}

	/**
	 * 
	 * @param path
	 */
	private void updateProjectList(String path) {
		projectName.removeAll();
		if(path == null || path.length() == 0) {
			return;
		}
		File sdk = new File(path);
		if(sdk.isDirectory()) {
			for (File child : sdk.listFiles()) {
				if(child.isDirectory()) {
					File springApp = new File(child, MVN_APPLICATION_CONTEXT_XML_PATH);
					if(springApp.exists()) {
						projectName.add(child.getName());
					}
				}
			}
		}
		projectName.select(0);
	}

	/**
	 * Ensures that both text fields are set.
	 */

	public void dialogChanged() {
		if (!isValidSdkPath(getSdkHome())) {
			return;
		}

		if (!isProjectNameValid(getProjectName())) {
			return;
		}
		if(getPortletName().length() == 0) {
			updateStatus(PORTLET_NAME_REQUIRED);
			return;
		}
		if (!getPortletName().matches(PORTLET_NAME_REGEX)) {
			updateStatus(PORTLET_NAME_ERROR);
			return;
		}

		if(WizardUtil.isPortletExist(getSdkHome(), getProjectName(), getPortletName())) {
			updateStatus(DUPLICATE_PORTLET_NAME);
			return;
		}

		if (getJspPath().length() == 0) {
			updateStatus(VIEW_PATH_ERROR);
			return;
		}
		if (getDisplayCategry().length() == 0) {
			updateStatus(DISPLAY_CATEGORY_ERROR);
			return;
		}
		updateStatus(null);
		antPage.updateStatus(null);
		antPage.setPageComplete(true);
	}

	/**
	 * 
	 * @param pName
	 * @return
	 */
	private boolean isProjectNameValid(String pName) {
		if (pName.length() == 0) {
			updateStatus(PROJECT_NAME_REQUIRED);
			return false;
		} else if (!pName.matches(PROJECT_NAME_REGEX)) {
			updateStatus(PROJECT_NAME_IN_CORRECT);
			return false;
		}

		//portletName.setMessage(getProjectName());

		return true;
	}

	/**
	 * 
	 * @param sdkPath
	 * @return
	 */
	private boolean isValidSdkPath(String sdkPath) {
		if (sdkPath.length() == 0) {
			updateStatus(LOCATION_PATH_REQUIRED);
			return false;
		} else {
			File file = new File(sdkPath);
			if (file.isDirectory()) {
				return true;
			} else {
				updateStatus(LOCATION_PATH_INVALID);
				return false;
			}
		}
	}

	/**
	 * 
	 * @param message
	 */
	public void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getProjectName() {
		return projectName.getText();
	}

	public String getPortletName() {
		if(portletName.getText().trim().length() == 0) {
			return getPortletMessage();
		}
		return portletName.getText();
	}

	public String getPortletMessage() {
		return portletName.getMessage();
	}

	public String getJspPath() {
		return jspPath.getText();
	}

	public String getDisplayCategry() {
		return displayCategry.getText();
	}

	public String getSdkHome() {
		return sdkHome.getText();
	}

	public void setAntPlugin(boolean isAntPlugin) {}
	
	@Override
	public IWizardPage getNextPage() {
		return springPortletWizard.getNextPage(super.getNextPage());
	}
}