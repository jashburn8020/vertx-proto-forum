package com.jashburn.protoforum;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.jashburn.protoforum.model.Forum;
import com.jashburn.protoforum.storage.Storage;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class ForumHandlersTest {

	// Temporarily downgraded Mockito from 2.10.0 to 1.10.19 due to problem with "Mockito cannot mock this class"
	// against RoutingContext, and so cannot use STRICT_STUBS rule.
	// @Rule
	// public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

	private RoutingContext mockContext;
	private Storage mockStorage;
	private HttpServerResponse mockResponse;
	private ForumHandlers handlers;

	@Before
	public void setUp() throws Exception {
		mockContext = Mockito.mock(RoutingContext.class);

		mockResponse = Mockito.mock(HttpServerResponse.class);
		Mockito.when(mockResponse.setStatusCode(Mockito.anyInt())).thenReturn(mockResponse);
		Mockito.when(mockResponse.putHeader(Mockito.any(CharSequence.class), Mockito.any(CharSequence.class)))
			.thenReturn(mockResponse);

		mockStorage = Mockito.mock(Storage.class);
		handlers = new ForumHandlers(mockStorage);
	}

	@After
	public void tearDown() throws Exception {
		// Nothing to tear down.
	}

	@Test
	public void testGetForum() {
		// set up Storage mock
		Forum forum = new Forum("name", "description");
		Mockito.when(mockStorage.getForum()).thenReturn(forum);

		// set up RoutingContext mock
		Mockito.when(mockContext.response()).thenReturn(mockResponse);

		handlers.getForum(mockContext);

		Mockito.verify(mockResponse).setStatusCode(HttpResponseStatus.OK.code());
		Mockito.verify(mockResponse).putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
		Mockito.verify(mockResponse).end(Json.encodePrettily(forum));
	}

}
