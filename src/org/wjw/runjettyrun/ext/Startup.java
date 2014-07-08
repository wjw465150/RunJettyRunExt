package org.wjw.runjettyrun.ext;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.UserLibraryManager;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroPart;
import org.osgi.framework.Bundle;

@SuppressWarnings("restriction")
public class Startup implements IStartup {

	@Override
	public void earlyStartup() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		workbench.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
				if (window == null) {
					System.out.println("^^^window is null^^^:");
				}

				//关闭欢迎页
				try {
					final IIntroPart intro = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getWorkbenchWindow().getWorkbench().getIntroManager().getIntro();
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getWorkbenchWindow().getWorkbench().getIntroManager().closeIntro(intro);
				} catch (Exception e) {
					Activator.logError(e);
				}

				//窗口最大化
				try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setMaximized(true);
				} catch (Exception e) {
					Activator.logError(e);
				}

				//设置缺省的java源代码编辑器:org.eclipse.jdt.ui.CompilationUnitEditor
				PlatformUI.getWorkbench().getEditorRegistry().setDefaultEditor("*.java", "org.eclipse.jdt.ui.CompilationUnitEditor");

				{
					String JSP_API = "JSP_API_2.5";
					String[] userLibraryNames = JavaCore.getUserLibraryNames();
					Bundle bundle = Platform.getBundle("runjettyrun");
					if (Arrays.asList(userLibraryNames).contains(JSP_API) == false) {
						String[] libNames = new String[] { "lib/jsp-2.1.jar", "lib/jsp-api-2.1.jar", "lib/servlet-api-2.5-20081211.jar" };
						try {
							IClasspathEntry[] ics = getBundleLibs(bundle, libNames);
							UserLibraryManager user = new UserLibraryManager();
							user.setUserLibrary(JSP_API, ics, false);
						} catch (Exception e) {
							Activator.logError(e);
						}
					} else { //检查lib路径是否正确
						String[] libNames = new String[] { "lib/jsp-2.1.jar", "lib/jsp-api-2.1.jar", "lib/servlet-api-2.5-20081211.jar" };
						if (validBundleLibs(bundle, libNames) == false) {
							try {
								IClasspathEntry[] ics = getBundleLibs(bundle, libNames);
								UserLibraryManager user = new UserLibraryManager();
								user.setUserLibrary(JSP_API, ics, false);
							} catch (Exception e) {
								Activator.logError(e);
							}
						}
					}
				} //JSP_API_2.5

				{
					String JSP_API = "JSP_API_3.0";
					String[] userLibraryNames = JavaCore.getUserLibraryNames();
					Bundle bundle = Platform.getBundle("runjettyrun.jetty8");
					if (Arrays.asList(userLibraryNames).contains(JSP_API) == false) {
						String[] libNames = new String[] { "lib/javax.annotation_1.1.0.v201105051105.jar", "lib/javax.el-2.2.0.v201108011116.jar", "lib/javax.servlet.jsp.jstl-1.2.0.v201105211821.jar", "lib/javax.servlet.jsp-2.2.0.v201112011158.jar", "lib/servlet-api-3.0.jar" };
						try {
							IClasspathEntry[] ics = getBundleLibs(bundle, libNames);
							UserLibraryManager user = new UserLibraryManager();
							user.setUserLibrary(JSP_API, ics, false);
						} catch (Exception e) {
							Activator.logError(e);
						}
					} else { //检查lib路径是否正确
						String[] libNames = new String[] { "lib/javax.annotation_1.1.0.v201105051105.jar", "lib/javax.el-2.2.0.v201108011116.jar", "lib/javax.servlet.jsp.jstl-1.2.0.v201105211821.jar", "lib/javax.servlet.jsp-2.2.0.v201112011158.jar", "lib/servlet-api-3.0.jar" };
						if (validBundleLibs(bundle, libNames) == false) {
							try {
								IClasspathEntry[] ics = getBundleLibs(bundle, libNames);
								UserLibraryManager user = new UserLibraryManager();
								user.setUserLibrary(JSP_API, ics, false);
							} catch (Exception e) {
								Activator.logError(e);
							}
						}
					}
				} //JSP_API_3.0

			}
		});

	}

	public static IClasspathEntry[] getBundleLibs(Bundle bundle, String[] filelist) throws MalformedURLException,
	    URISyntaxException {

		List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
		URL installUrl = bundle.getEntry("/");

		try {
			for (String filepath : filelist) {
				// Note the FileLocator will generate a file for us when we use
				// FileLocator.toFileURL ,
				// it's very important.
				try {
					URL fileUrl = FileLocator.toFileURL(new URL(installUrl, filepath));
					entries.add(JavaCore.newLibraryEntry(new Path(fileUrl.getPath()), null, null, true));
				} catch (Exception e) {
				}
			}
			if (entries.size() == 0) {
				throw new IllegalStateException("RJR finding jar failed");
			}

		} catch (Exception e) {
			Activator.logError(e);
		}
		return entries.toArray(new IClasspathEntry[0]);
	}

	public static boolean validBundleLibs(Bundle bundle, String[] filelist) {
		URL installUrl = bundle.getEntry("/");
		try {
			for (String filepath : filelist) {
				// Note the FileLocator will generate a file for us when we use
				// FileLocator.toFileURL ,
				// it's very important.
				try {
					URL fileUrl = FileLocator.toFileURL(new URL(installUrl, filepath));
					File ff = new Path(fileUrl.getPath()).toFile();
					if (ff.exists()) {
						return false;
					}
				} catch (Exception e) {
					return true;
				}
			}
		} catch (Exception e) {
			Activator.logError(e);
		}
		return false;
	}
}
