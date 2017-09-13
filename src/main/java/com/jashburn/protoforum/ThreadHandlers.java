package com.jashburn.protoforum;

import com.jashburn.protoforum.model.MessageThread;
import com.jashburn.protoforum.storage.Storage;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

class ThreadHandlers {

	private Storage storage;

	ThreadHandlers(Storage storage) {
		this.storage = storage;
	}

	void createThread(RoutingContext context) {
		MessageThread thread = Json.decodeValue(context.getBodyAsString(), MessageThread.class);
		MessageThread storedThread = storage.storeThread(thread);

		String resourceLocation = ProtoForum.PATH_APP_V1 + ProtoForum.PATH_THREADS + "/" + storedThread.getId();
		context.response().setStatusCode(HttpResponseStatus.CREATED.code())
			.putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
			.putHeader(HttpHeaders.LOCATION, resourceLocation).end(Json.encodePrettily(storedThread));
	}
}
