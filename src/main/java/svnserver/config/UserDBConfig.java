/**
 * This file is part of git-as-svn. It is subject to the license terms
 * in the LICENSE file found in the top-level directory of this distribution
 * and at http://www.gnu.org/licenses/gpl-2.0.html. No part of git-as-svn,
 * including this file, may be copied, modified, propagated, or distributed
 * except according to the terms contained in the LICENSE file.
 */
package svnserver.config;

import org.jetbrains.annotations.NotNull;
import svnserver.auth.UserDB;
import svnserver.context.SharedContext;

/**
 * @author Marat Radchenko <marat@slonopotamus.org>
 */
public interface UserDBConfig {
  UserDBConfig[] emptyArray = {};

  @NotNull
  UserDB create(@NotNull SharedContext context);
}
