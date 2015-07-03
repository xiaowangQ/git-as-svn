/**
 * This file is part of git-as-svn. It is subject to the license terms
 * in the LICENSE file found in the top-level directory of this distribution
 * and at http://www.gnu.org/licenses/gpl-2.0.html. No part of git-as-svn,
 * including this file, may be copied, modified, propagated, or distributed
 * except according to the terms contained in the LICENSE file.
 */
package svnserver.repository.git.path;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Git wildcard mask.
 * <p>
 * Pattern format: http://git-scm.com/docs/gitignore
 *
 * @author Artem V. Navrotskiy <bozaro@users.noreply.github.com>
 */
public class Wildcard {
  private final boolean negativeMask;
  //private final String[] tokens;

  public Wildcard(@NotNull String pattern) {
    negativeMask = pattern.startsWith("!");
    String pattern1 = negativeMask ? pattern.substring(1) : pattern;
    List<String> tokens = WildcardHelper.splitPattern(pattern1);
    WildcardHelper.normalizePattern(tokens);
  }
}
