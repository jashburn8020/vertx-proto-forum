package com.jashburn.protoforum;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;

import com.jashburn.protoforum.model.MessageThread;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class ProtoForumTest {

	private Vertx vertx;
	
	@Before
	public void setUp(TestContext context) throws Exception {
		vertx = Vertx.vertx();
		vertx.deployVerticle(ProtoForum.class.getName(), context.asyncAssertSuccess());
	}

	@After
	public void tearDown(TestContext context) throws Exception {
		vertx.close(context.asyncAssertSuccess());
	}

/*
{
    "name":"ProtoForum",
    "description":"Simple prototype of a forum"
}
 */	
	@Test
	public void testGetForum(TestContext context) {
		final String expResponse = new StringBuilder().append("{")
			.append("\"name\":\"ProtoForum\",")
			.append("\"description\":\"Simple prototype of a forum\"")
			.append("}")
			.toString();
		
		final Async async = context.async();
		vertx.createHttpClient().getNow(ProtoForum.PORT, "localhost", "/forum/v1", response -> {
			context.assertEquals(200, response.statusCode());
			context.assertEquals(HttpHeaderValues.APPLICATION_JSON.toString(),
				response.getHeader(HttpHeaders.CONTENT_TYPE));

			response.bodyHandler(body -> {
				try {
					JSONAssert.assertEquals(expResponse, body.toString(), true);
				} catch (Throwable e) {
					context.fail(e.getMessage());
				}
				async.complete();
			});
		});
	}

/*
{
    "id":"some-uuid",
    "title":"Test thread"
}
 */
	@Test
	public void testPostThread(TestContext context) throws Exception {
		final String title = "Test thread";
		
		MessageThread thread = new MessageThread();
		thread.setTitle(title);
		String payload = Json.encode(thread);

		final Async async = context.async();
		vertx.createHttpClient().post(ProtoForum.PORT, "localhost", ProtoForum.PATH_APP_V1 + ProtoForum.PATH_THREADS)
			.putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
			.putHeader(HttpHeaders.CONTENT_LENGTH, Integer.toString(payload.getBytes("UTF-8").length))
			.handler(response -> {
				context.assertEquals(201, response.statusCode());
				context.assertEquals(HttpHeaderValues.APPLICATION_JSON.toString(),
					response.getHeader(HttpHeaders.CONTENT_TYPE));

				final StringBuilder location = new StringBuilder();
				location.append(response.getHeader(HttpHeaders.LOCATION));

				response.bodyHandler(body -> {
					MessageThread actThread = Json.decodeValue(body, MessageThread.class);
					context.assertEquals(title, actThread.getTitle());

					UUID actId = actThread.getId();
					context.assertNotNull(actId);
					context.assertEquals(ProtoForum.PATH_APP_V1 + ProtoForum.PATH_THREADS + "/" + actId,
						location.toString());

					async.complete();
				});
			}).write(payload).end();
	}
}
