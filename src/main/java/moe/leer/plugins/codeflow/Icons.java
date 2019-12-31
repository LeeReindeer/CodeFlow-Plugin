package moe.leer.plugins.codeflow;

import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author leer
 * Created at 12/31/19 2:12 PM
 */
public class Icons {
//  public static final Icon CODEFLOW = IconLoader.getIcon("/icons/codeflow.png");

  @NotNull
  public static Icon getToolWindow() {
    // IntelliJ 2018.2+ has monochrome icons for tool windows so let's use one too
    if (ApplicationInfo.getInstance().getBuild().getBaselineVersion() >= 182) {
      return IconLoader.getIcon("/icons/codeflow.svg");
    }

    return IconLoader.getIcon("/icons/codeflow.png");
  }
}
