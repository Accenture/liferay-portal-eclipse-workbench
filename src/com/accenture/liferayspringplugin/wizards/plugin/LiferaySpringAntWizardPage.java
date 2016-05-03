package com.accenture.liferayspringplugin.wizards.plugin;

import static com.accenture.liferayspringplugin.wizards.Constants.*;

import java.io.File;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.accenture.liferayspringplugin.wizards.WizardUtil;

/**
 * The "LiferaySpring" wizard page allows setting the portlet/project name and
 * other required settings.
 * 
 * @author Digital Liferay Capability IDC
 */

public class LiferaySpringAntWizardPage extends WizardPage {
	private Text projectName;
	private Text packageName;
	private Text portletName;
	private Text sdkHome;
	private Text jspPath;
	private Text displayCategry;
	private Button enableDSK;
	
	//private Label chkLabel;
	private Label projectLabel;
	private Label portletLabel;
	private Label packageLabel;
	private Label sdkLabel;

	private boolean isEnabledSDK = true;

	/**
	 * Constructor for SampleNewWizardPage.
	 * @param pageName
	 */
	public LiferaySpringAntWizardPage() {
		super(WIZARD_PAGE);
		setTitle(PLUGIN_WIZARD_PAGE_TITLE);
		setDescription(PLUGIN_WIZARD_PAGE_SDK_DESCRIPTION);
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 4;
		layout.verticalSpacing = 12;
		
		/*chkLabel = new Label(container, SWT.NULL);
		chkLabel.setText(STORE_LOCATION_LABEL);*/
		
		enableDSK = new Button(container, SWT.CHECK);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 4;
		enableDSK.setLayoutData(gd);
		enableDSK.setText(STORE_LOCATION_LABEL);
		enableDSK.addSelectionListener(new SelectionListener() {			
			@Override
			public void widgetSelected(SelectionEvent e) {
				isEnabledSDK = enableDSK.getSelection();
				if(isEnabledSDK) {
					sdkLabel.setText(SDK_LOCATION_PATH_LABEL);
				} else {
					sdkLabel.setText(LOCATION_PATH_LABEL);	
				}
				dialogChanged();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {				
			}
		});
		
		sdkLabel = new Label(container, SWT.NULL);
		sdkLabel.setText(SDK_LOCATION_PATH_LABEL);

		sdkHome = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		sdkHome.setLayoutData(gd);
		sdkHome.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		Button button = new Button(container, SWT.PUSH);
		button.setText(BROWSE_BUTTON_LABEL);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});

		projectLabel = new Label(container, SWT.NULL);
		projectLabel.setText(_PROJECT_NAME_LABLE);

		projectName = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		projectName.setLayoutData(gd);
		projectName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		Label label = new Label(container, SWT.NULL);
		label.setText(EMPTY);

		portletLabel = new Label(container, SWT.NULL);
		portletLabel.setText(_PORTLET_NAME_LABEL);

		portletName = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		portletName.setLayoutData(gd);
		portletName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		label = new Label(container, SWT.NULL);
		label.setText(EMPTY);
		packageLabel = new Label(container, SWT.NULL);
		packageLabel.setText(PACKAGE_LABEL);

		packageName = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		packageName.setLayoutData(gd);
		packageName.addModifyListener(new ModifyListener() {
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
		gd.horizontalSpan = 2;
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
		gd.horizontalSpan = 2;
		displayCategry.setLayoutData(gd);
		displayCategry.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		initialize();
		//dialogChanged();
		setControl(container);
		setPageComplete(true);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		packageName.setMessage(COM_DOT_ACCENTURE);
		jspPath.setText(DEFAULT_JSP_FOLDER);
		displayCategry.setText(DEFAULT_DISPLAY_CATEGORY);
		String path = ResourcesPlugin.getWorkspace().getRoot().getLocation()
				.toString();
		if (isValidSdkPath(path)) {
			sdkHome.setText(path);
		}
		enableDSK.setSelection(true);
		enableDSK.setEnabled(true);
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
	 * Ensures that both text fields are set.
	 */

	public void dialogChanged() {
		if (!isValidSdkPath(getSdkHome())) {
			return;
		}

		if (!isProjectNameValid(getSdkHome(), getProjectName())) {
			return;
		}
		if (getPortletName().length() > 0 && !getPortletName().matches(PORTLET_NAME_REGEX)) {
			updateStatus(PORTLET_NAME_ERROR);
			return;
		}

		if (getPackageName().length() > 0 && !getPackageName().matches(PACKAGE_NAME_REGEX)) {
			updateStatus(PACKAGE_NAME_ERROR);
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
	}

	/**
	 * 
	 * @param sdkPath
	 * @param projectName
	 * @return
	 */
	private boolean isProjectNameValid(String sdkPath, String projectName) {
		if (projectName.length() == 0) {
			updateStatus(PROJECT_NAME_REQUIRED);
			return false;
		} else if (!projectName.matches(PROJECT_NAME_REGEX)) {
			updateStatus(PROJECT_NAME_IN_CORRECT);
			return false;
		}

		if(WizardUtil.isProjectExist(sdkPath, projectName)) {
			updateStatus(DUPLICATE_PORTLET_NAME);
			return false;
		}

		portletName.setMessage(getProjectName());

		return true;
	}

	/**
	 * 
	 * @param sdkPath
	 * @return
	 */
	private boolean isValidSdkPath(String sdkPath) {
		if (sdkPath.length() == 0) {
			if(isEnabledSDK) {
				updateStatus(LIFERAY_SDK_PATH_REQUIRED);
			} else {
				updateStatus(LOCATION_PATH_REQUIRED);
			}
			return false;
		} else {
			int count = 0;
			File file = new File(sdkPath);
			if (file.isDirectory()) {
				if(!isEnabledSDK) {
					return true;
				}
				for (String fileName : file.list()) {
					if (fileName.equals(PORTLETS_LBL)
							|| fileName.equals(BUILD_XML)) {
						count++;
					}
					if (count == 2) {
						return true;
					}
				}
				updateStatus(LIFERAY_SDK_PATH_INVALID);
				return false;
			} else {
				if(isEnabledSDK) {
					updateStatus(LIFERAY_SDK_PATH_INVALID);
				} else {
					updateStatus(LOCATION_PATH_INVALID);
				}
				return false;
			}
		}
	}

	/**
	 * 
	 * @param message
	 */
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getProjectName() {
		return projectName.getText();
	}

	public String getGroupId() {
		if(projectName.getText().trim().length() == 0) {
			return projectName.getMessage();
		}
		return projectName.getText();
	}	

	public String getPackageName() {
		if(packageName.getText().trim().length() == 0) {
			return packageName.getMessage();
		}
		return packageName.getText();
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

	public boolean isEnabledSDK() {
		return isEnabledSDK;
	}
}