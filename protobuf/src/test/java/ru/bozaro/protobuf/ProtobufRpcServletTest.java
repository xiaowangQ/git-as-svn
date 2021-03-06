/**
 * This file is part of git-as-svn. It is subject to the license terms
 * in the LICENSE file found in the top-level directory of this distribution
 * and at http://www.gnu.org/licenses/gpl-2.0.html. No part of git-as-svn,
 * including this file, may be copied, modified, propagated, or distributed
 * except according to the terms contained in the LICENSE file.
 */
package ru.bozaro.protobuf;

import com.google.protobuf.BlockingRpcChannel;
import com.google.protobuf.BlockingService;
import org.jetbrains.annotations.NotNull;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.bozaro.protobuf.client.ProtobufClient;
import ru.bozaro.protobuf.client.ProtobufClientGet;
import ru.bozaro.protobuf.example.EchoMessage;
import ru.bozaro.protobuf.example.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple test for ProtobufRpcServlet.
 *
 * @author Artem V. Navrotskiy
 */
public class ProtobufRpcServletTest {
  @DataProvider(name = "formatProvider")
  public static Object[][] formatProvider() {
    List<Object[]> result = new ArrayList<>();
    for (ProtobufFormat format : ProtobufFormat.getFormats()) {
      result.add(new Object[]{format});
    }
    return result.toArray(new Object[result.size()][]);
  }

  @Test(dataProvider = "formatProvider")
  public void echoPost(@NotNull ProtobufFormat format) throws Exception {
    final BlockingService service = Example.newReflectiveBlockingService(new ExampleBlockingImpl());
    try (final EmbeddedHttpServer server = new EmbeddedHttpServer()) {
      server.addServlet("/api/*", new ProtobufRpcServlet(new ServiceHolderImpl(service)));

      final BlockingRpcChannel channel = new ProtobufClient(server.getBase().resolve("/api"), null, format);
      Example.BlockingInterface stub = Example.newBlockingStub(channel);
      // Check echo method.
      for (int pass = 0; pass < 2; ++pass) {
        final EchoMessage echoRequest = EchoMessage.newBuilder()
            .setText("Foo " + pass)
            .setEmbedded(EchoMessage.Embedded.newBuilder()
                .setFoo("some text")
                .build())
            .build();
        final EchoMessage echoResponse = stub.echo(null, echoRequest);
        Assert.assertEquals(echoRequest, echoResponse);
      }
    }
  }

  @Test(dataProvider = "formatProvider")
  public void echoGet(@NotNull ProtobufFormat format) throws Exception {
    final BlockingService service = Example.newReflectiveBlockingService(new ExampleBlockingImpl());
    try (final EmbeddedHttpServer server = new EmbeddedHttpServer()) {
      server.addServlet("/api/*", new ProtobufRpcServlet(new ServiceHolderImpl(service)));

      final BlockingRpcChannel channel = new ProtobufClientGet(server.getBase().resolve("/api"), null, format);
      Example.BlockingInterface stub = Example.newBlockingStub(channel);
      // Check echo method.
      for (int pass = 0; pass < 2; ++pass) {
        final EchoMessage echoRequest = EchoMessage.newBuilder()
            .setText("Foo " + pass)
            .build();
        final EchoMessage echoResponse = stub.echo(null, echoRequest);
        Assert.assertEquals(echoRequest, echoResponse);
      }
    }
  }
}
