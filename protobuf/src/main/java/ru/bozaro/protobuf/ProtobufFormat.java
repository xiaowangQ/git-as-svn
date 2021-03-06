/**
 * This file is part of git-as-svn. It is subject to the license terms
 * in the LICENSE file found in the top-level directory of this distribution
 * and at http://www.gnu.org/licenses/gpl-2.0.html. No part of git-as-svn,
 * including this file, may be copied, modified, propagated, or distributed
 * except according to the terms contained in the LICENSE file.
 */
package ru.bozaro.protobuf;

import com.google.protobuf.Message;
import org.atteo.classindex.ClassIndex;
import org.atteo.classindex.IndexSubclasses;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.stream.StreamSupport;

/**
 * Protobuf formatter.
 *
 * @author Artem V. Navrotskiy <bozaro@users.noreply.github.com>
 */
@IndexSubclasses
public abstract class ProtobufFormat {
  @NotNull
  private static final ProtobufFormat[] formats = collectFormats();
  @NotNull
  private final String mimeType;
  @NotNull
  private final String suffix;

  public ProtobufFormat(@NotNull String mimeType, @NotNull String suffix) {
    this.mimeType = mimeType;
    this.suffix = suffix;
  }

  @NotNull
  public final String getMimeType() {
    return mimeType;
  }

  @NotNull
  public final String getSuffix() {
    return suffix;
  }

  @Override
  public String toString() {
    return mimeType;
  }

  public abstract void write(@NotNull Message message, @NotNull OutputStream stream, @NotNull Charset charset) throws IOException;

  @NotNull
  public abstract Message.Builder read(@NotNull Message.Builder builder, @NotNull InputStream stream, @NotNull Charset charset) throws IOException;

  @NotNull
  public static ProtobufFormat[] getFormats() {
    return formats;
  }

  private static ProtobufFormat[] collectFormats() {
    return StreamSupport
        .stream(ClassIndex.getSubclasses(ProtobufFormat.class).spliterator(), false)
        .filter(type -> !Modifier.isAbstract(type.getModifiers()))
        .map(type -> {
          try {
            return type.newInstance();
          } catch (IllegalAccessException | InstantiationException e) {
            throw new IllegalStateException(e);
          }
        })
        .toArray(ProtobufFormat[]::new);
  }
}
