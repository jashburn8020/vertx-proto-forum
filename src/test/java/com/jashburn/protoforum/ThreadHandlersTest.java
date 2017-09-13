package com.jashburn.protoforum;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.jashburn.protoforum.model.MessageThread;
import com.jashburn.protoforum.storage.Storage;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class ThreadHandlersTest {

	private RoutingContext mockContext;
	private HttpServerResponse mockResponse;
	private Storage mockStorage;

	private ThreadHandlers threadHandlers;

	@Before
	public void setUp() throws Exception {
		mockContext = Mockito.mock(RoutingContext.class);

		mockResponse = Mockito.mock(HttpServerResponse.class);
		Mockito.when(mockContext.response()).thenReturn(mockResponse);

		Mockito.when(mockResponse.setStatusCode(Mockito.anyInt())).thenReturn(mockResponse);
		Mockito.when(mockResponse.putHeader(Mockito.any(CharSequence.class), Mockito.any(CharSequence.class)))
			.thenReturn(mockResponse);

		mockStorage = Mockito.mock(Storage.class);

		threadHandlers = new ThreadHandlers(mockStorage);
	}

	@After
	public void tearDown() throws Exception {
		// Nothing to tear down.
	}

	/*
{
    "id": Int,
    "title": String,
    "messages": Message[]
}
	 */
	
	@Test
	public void testCreateThread() {
		String threadTitle = "test thread";
		JsonObject createThreadPayload = new JsonObject().put("title", threadTitle);
		Mockito.when(mockContext.getBodyAsString()).thenReturn(createThreadPayload.toString());

		MessageThread returnThread = new MessageThread();
		returnThread.setTitle(threadTitle);
		returnThread.setId(UUID.randomUUID());
		Mockito.when(mockStorage.storeThread(Mockito.any(MessageThread.class))).thenReturn(returnThread);

		threadHandlers.createThread(mockContext);

		MessageThread storeThread = new MessageThread();
		storeThread.setTitle(threadTitle);
		Mockito.verify(mockStorage).storeThread(storeThread);

		Mockito.verify(mockResponse).setStatusCode(HttpResponseStatus.CREATED.code());
		Mockito.verify(mockResponse).putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
		Mockito.verify(mockResponse).putHeader(HttpHeaders.LOCATION,
			ProtoForum.PATH_APP_V1 + ProtoForum.PATH_THREADS + "/" + returnThread.getId());
		Mockito.verify(mockResponse).end(Json.encodePrettily(returnThread));
	}

}
