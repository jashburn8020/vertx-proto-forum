package com.jashburn.protoforum;

import com.jashburn.protoforum.model.Forum;
import com.jashburn.protoforum.storage.Storage;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

class ForumHandlers {

	private final Storage storage;

	ForumHandlers(Storage storage) {
		this.storage = storage;
	}

	void getForum(RoutingContext context) {
		Forum forum = storage.getForum();
		context.response().setStatusCode(HttpResponseStatus.OK.code())
			.putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValue.APPLICATION_JSON).end(Json.encodePrettily(forum));
	}
}
